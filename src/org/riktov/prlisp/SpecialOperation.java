package org.riktov.prlisp;

import java.util.HashMap;
import java.util.Iterator;

abstract class SpecialOperation extends LispProcedure {
	/**
	 * A special operation may choose to evaluate some arguments conditionally,
	 * during execution, rather than receiving them evaluated. So it may need the environment in
	 * which the arguments are to be evaluated,
	 * 
	 */
	protected Environment argEnv;

	//public LispObject apply() { return new NilAtom()  ; }

	public LispObject apply(LispList argList) {
		return applyNonNil((ConsCell)argList) ;
	}	
	
	/**
	 * A special operation with no arguments would be meaningless. So we can assume that all special operations
	 * will be called with a non-zero number of arguments. So the arg list can be a ConsCell, not just
	 * a LispList (which could be a NilAtom).
	 *
	 * @param argList
	 * @return
	 */
	abstract LispObject applyNonNil(ConsCell argList) ;
	
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
		 * setq - Treat the first argument as a symbol(unevaluated), and intern
		 * it with the value of the second argument
		 * TODO: Implement the paired-setq
		 */
		specials.put("setq".toUpperCase(), new SpecialOperation() {
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

		/**
		 * if - Treat the first argument as a boolean(evaluated), and depending
		 * on its truth value return the value of the second or third argument.
		 */
		specials.put("if".toUpperCase(), new SpecialOperation() {
			@Override
			public LispObject applyNonNil(ConsCell argForms) {
				LispObject[] args = argForms.toArray() ;

				LispObject condition = args[0] ;
				LispObject consequent = args[1] ;
				LispObject alternate = args[2] ;

				if (!condition.eval(argEnv).isNull()) {
					return consequent.eval(argEnv);
				} else {
					return alternate.eval(argEnv);
				}
			}
		});


		specials.put("progn".toUpperCase(), new SpecialOperation() {
			@Override
			public LispObject applyNonNil(ConsCell argForms) {
				return argForms.evalSequence(argEnv) ;
			}
		});

		specials.put("quote".toUpperCase(), new SpecialOperation() {
			@Override
			public LispObject applyNonNil(ConsCell argForms) {
				if(argForms.isNull()) {
					return NilAtom.nil ;	//(QUOTE)
				}
				if(!argForms.cdr().isNull()) {
					new LispRestarter().offerRestarts(";Ill-formed special form") ;
				}
				return argForms.car() ;//unevaluated
			}
		});

		specials.put("lambda".toUpperCase(), new SpecialOperation() {
			/**
			 * Second parameter is list-bound
			 */
			@Override
			public LispObject applyNonNil(ConsCell argForms) {
				ConsCell argFormsCell = argForms ; //assume argForms is not empty list
				ConsCell bindings = (ConsCell) argFormsCell.car() ;
				ConsCell body = (ConsCell) argFormsCell.cdr() ;
				return new CompoundProcedure(bindings, body, argEnv);
			}
		});

		specials.put("define".toUpperCase(), new SpecialOperation() {
			public LispObject applyNonNil(ConsCell argForms) {
				//extract components
				LispObject varNameForm = argForms.car();
				ConsCell definition = (ConsCell)argForms.cdr() ;
				
				if(varNameForm.isAtom()) {
					LispObject value = null;
					value = definition.car().eval(argEnv);
					argEnv.intern(varNameForm.toString(), value) ;
					return varNameForm;
				} else {
					ConsCell varNameAndParams = (ConsCell)varNameForm ;
					SymbolAtom procName = (SymbolAtom) varNameAndParams.car() ;
					
					LispObject procParamList = varNameAndParams.cdr() ;
					
					LispList procBody = (LispList) argForms.cdr() ;
					argEnv.intern(procName.toString(),
							new CompoundProcedure(procParamList, procBody, argEnv)) ;
					return procName ;
				}
			}
		}) ;
		
		specials.put("let".toUpperCase(), new SpecialOperation() {
			public LispObject applyNonNil(ConsCell argForms) {
				LispList bindingList = (LispList)argForms.car();
				LispList body = (LispList) argForms.cdr();
				
//				LispObject[] bindings = bindingList.toArray();

				ChildEnvironment letEnv = new ChildEnvironment(argEnv);

				Iterator<LispObject> itrBindings = bindingList.iterator();
				
				while(itrBindings.hasNext()) {
					LispObject bindingExp = itrBindings.next() ;
					ConsCell bindingPair = null ;
					
					if(bindingExp.isAtom()) {
						bindingPair = new ConsCell(bindingExp, NilAtom.nil) ;
					} else {
						bindingPair = (ConsCell)bindingExp ;
					}

					LispObject[] bindingArr = bindingPair.toArray() ;
					
					SymbolAtom symName = (SymbolAtom) bindingArr[0];
					LispObject value = bindingArr[1].eval(argEnv);

					letEnv.intern(symName.toString(), value);
				}
				
				LispObject result = null;
				result = body.evalSequence(letEnv);

				return result;
			}
		});

		// ///////////////////
		return specials;
	}
}