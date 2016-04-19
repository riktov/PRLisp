package org.riktov.spinja;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A special operation may choose to evaluate some arguments conditionally,
 * during execution, rather than receiving them evaluated. So it may need the environment in
 * which the arguments are to be evaluated. By contrast, a normal procedure is applied to values
 * which have already been evaluated in an environment.
 */
abstract class SpecialOperation extends LispProcedure {

	protected Environment argEnv;

	//public LispObject apply() { return new NilAtom()  ; }

	public LispObject apply(LispList argList) {
		if(argList.isNull()) {
			LispRestarter restart = new LispRestarter();
			restart.offerRestarts("Ill-formed special form ") ;
			throw new LispAbortEvaluationException() ;
		}
		return applyNonNil((ConsCell)argList) ;
	}	
	
	/**
	 * A special operation with no arguments would be meaningless. So we can assume that all special operations
	 * will be called with a non-zero number of arguments. So the arg list can be typed as a ConsCell, not just
	 * a LispList (which could be a NilAtom).
	 *
	 * @param argList
	 * @return
	 */
	abstract LispObject applyNonNil(ConsCell argList) ;
	
	/**
	 * processArguments - For special operations, we don't immediately evaluate the arguments, but save the environment so 
	 * they can be evaluated if needed.
	 * 
	 * @param unevaluatedArgForms
	 * @param argEnv
	 * 
	 * @return the same argument list
	 * 
	 */
	@Override public LispList processArguments(LispList unevaluatedArgForms, Environment argEnv) {
		this.argEnv = argEnv;
		return unevaluatedArgForms;
	}

	// constructors

	/**
	 * Return a HashMap with the initial set of SpecialOperation objects.
	 * 
	 * @param env This is used only by SETQ, which creates a closure over it.
	 * @return a HashMap mapping strings to subclassed SpecialOperation objects.
	 */
	static HashMap<String, SpecialOperation> initialSpecials(final Environment env) {
		HashMap<String, SpecialOperation> specials = new HashMap<String, SpecialOperation>();

		/**
		 * setq(CL) / set!(Scheme) - Treat the first argument as a symbol(unevaluated), and intern
		 * it with the value of the second argument
		 */

		specials.put("set!".toUpperCase(), new SpecialOperation() {
			public LispObject applyNonNil(ConsCell argForms) {
				LispObject assignedValue = null ;

				ConsCell current = argForms ;
				while(!current.cdr.isNull()) {
					String symbolName = current.car().toString().toUpperCase() ;//current key
					current = (ConsCell)current.cdr ;//current value
					assignedValue = current.car ;
					env.put(symbolName, assignedValue.eval(argEnv));
					if(!current.cdr.isNull()) {
						current = (ConsCell)current.cdr ;//next key-value pair						
					}
				}
				return assignedValue;
			}
		});
		
		specials.put("setq".toUpperCase(), specials.get("set!".toUpperCase())) ;

		/**
		 * if - Treat the first argument as a boolean(evaluated), and depending
		 * on its truth value return the value of the second or third argument.
		 */
		specials.put("if".toUpperCase(), new SpecialOperation() {
			@Override
			public LispObject applyNonNil(ConsCell argForms) {
				//System.out.println("special op IF: " + argEnv.toString()) ;
				LispObject[] args = argForms.toArray() ;

				LispObject condition = args[0] ;
				LispObject consequent = args[1] ;
				LispObject alternate ;
				
				//System.out.println(this +  args[0].toString()) ;
				
				if(argForms.length() > 2) {
					alternate = args[2] ;
				} else {
					alternate = new NilAtom() ;
				} 
				
				//for reentrancy
				ChildEnvironment env = new ChildEnvironment(argEnv) ;
				
				//evaluating the condition sets argEnv with its own bindings, clobbering the earlier binding
				if (!condition.eval(env).isNull()) {
					return consequent.eval(env);
				} else {
					return alternate.eval(env);
				}
			}
		});


		/**
		 * progn(CL) / begin(Scheme) - evaluate the forms in sequence, 
		 * and return the last form. If processArguments were guaranteed to execute
		 * from left to right, this could be a conventional procedure, that just
		 * returns the last argument.
		 */
		specials.put("begin".toUpperCase(), new SpecialOperation() {
			@Override
			public LispObject applyNonNil(ConsCell argForms) {
				return argForms.evalSequence(argEnv) ;
			}
		});
		specials.put("progn".toUpperCase(), specials.get("begin".toUpperCase())) ;
		

		specials.put("quote".toUpperCase(), new SpecialOperation() {
			/**
			 * Special case to allow '()
			 */
			@Override
			public LispObject apply(LispList argForms) {
				requireArgumentCount(1, argForms, "quote") ;
				if(argForms.isNull()) {
					return NilAtom.nil ;
				} else {
					return applyNonNil((ConsCell)argForms) ;
				}
			}
			
			@Override
			public LispObject applyNonNil(ConsCell argForms) {
/*				if(argForms.isNull()) {
					return NilAtom.nil ;	//(QUOTE)
				}
				if(!argForms.cdr().isNull()) {
					new LispRestarter().offerRestarts(";Ill-formed special form") ;
				}
*/				return argForms.car() ;//unevaluated
			}
		});

		specials.put("lambda".toUpperCase(), new SpecialOperation() {
			/**
			 * Second parameter is list-bound
			 */
			@Override
			public LispObject applyNonNil(ConsCell argForms) {
				ConsCell argFormsCell = argForms ; //assume argForms is not empty list
				LispList bindings = (LispList) argFormsCell.car() ;
				ConsCell body = (ConsCell) argFormsCell.cdr() ;
				return new CompoundProcedure(bindings, body, argEnv);
			}
		});

		specials.put("define".toUpperCase(), new SpecialOperation() {
			public LispObject applyNonNil(ConsCell argForms) {
				//System.out.println(this + " " + argForms) ;
				//extract components
				LispObject varNameForm = argForms.car();	//variable name, or list of function name and parameters
				ConsCell definition = (ConsCell) argForms.cdrList() ;
				
				SymbolAtom nameSymbol ;
				LispObject valueObject ;
				
				if(varNameForm.isAtom()) {	//defining a variable, not a function
					nameSymbol = (SymbolAtom) varNameForm ;
					valueObject = definition.car().eval(argEnv);
				} else {
					ConsCell varNameAndParams = (ConsCell)varNameForm ;
					nameSymbol = (SymbolAtom)varNameAndParams.car() ;
					LispObject params = varNameAndParams.cdr() ;

					LispList procBody = (LispList) argForms.cdr() ;

					if(params.isAtom()) {
						SymbolAtom symbolName = (SymbolAtom)params ;
						SymbolAtom procParams = new ParameterSymbol(symbolName) ;
						valueObject = new CompoundProcedure(procParams, procBody, argEnv) ;
					} else {
						ConsCell procParams = new ParameterList((ConsCell)params) ;						
						valueObject = new CompoundProcedure(procParams, procBody, argEnv) ;
					}
				}
				argEnv.intern(nameSymbol.toString(), valueObject) ;
				return nameSymbol ;
			}
		}) ;
		
		/**
		 * LET: The first argument is a list of bindings, which may be atoms which will be bound to NIL,
		 * or two-elements lists containing the symbol and value. The remaining arguments (the body) 
		 * are evaluated in sequence with the new bindings, and the last value is returned.
		 */
		specials.put("let".toUpperCase(), new SpecialOperation() {
			public LispObject applyNonNil(ConsCell argForms) {
				LispList bindingList = (LispList)argForms.car();
				LispList body = (LispList) argForms.cdr();
				
				ChildEnvironment letEnv = new ChildEnvironment(argEnv);

				Iterator<LispObject> itrBindings = bindingList.iterator();
				
				/*
				 * TODO: Simplify; just create a new list with the null bindings filled in,
				 * then bindToParams
				 */
				while(itrBindings.hasNext()) {
					LispObject bindingExp = itrBindings.next() ;
					
					LispObject[] bindingArr = null ;
					
					if(bindingExp.isAtom()) {
						bindingArr = new LispObject[] { bindingExp, NilAtom.nil } ;
					} else {
						bindingArr = ((LispList) bindingExp).toArray() ;
					}

					SymbolAtom symName = (SymbolAtom) bindingArr[0];
					LispObject value = bindingArr[1].eval(argEnv);

					letEnv.intern(symName.toString(), value);
				}
				
				LispObject result = null;
				result = body.evalSequence(letEnv);

				return result;
			}

		});

		specials.put("apply".toUpperCase(), new SpecialOperation() {
			public LispObject applyNonNil(ConsCell argForms) {
				LispProcedure proc = (LispProcedure)argForms.car().eval(argEnv) ;
				LispList values = ((ConsCell) argForms.cdr()).listOfValues(argEnv) ;
				
				return proc.apply(values) ;
			}
		});

		// ///////////////////
		return specials;
	}
}