package org.riktov.prlisp;

import java.util.HashMap;
import java.util.Iterator;

abstract class SpecialOperation extends LispProcedure {
	/**
	 * A special operation may choose to evaluate some arguments conditionally,
	 * rather than receiving them evaluated. So it may need the environment in
	 * which the arguments are to be evaluated,
	 */
	protected Environment argEnv;

	//public LispObject apply() { return new NilAtom()  ; }

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
			public LispObject apply(LispList argForms) {
				LispObject assignedValue = null ;

				ConsCell current = (ConsCell)argForms ;
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
			public LispObject apply(LispList argForms) {
				LispObject condition = argForms.car();
				LispObject consequent = argForms.cdr().car();
				LispObject alternate = argForms.cdr().cdr().car();

				if (!condition.eval(argEnv).isNull()) {
					return consequent.eval(argEnv);
				} else {
					return alternate.eval(argEnv);
				}
			}
		});


		specials.put("progn".toUpperCase(), new SpecialOperation() {
			@Override
			public LispObject apply(LispList argForms) {
				return argForms.evalSequence(argEnv) ;
			}
		});

		specials.put("quote".toUpperCase(), new SpecialOperation() {
			@Override
			public LispObject apply(LispList argForms) {
				if(!argForms.cdr().isNull()) {
					new LispRestarter(";Ill-formed special form", env) ;
				}
				return argForms.car() ;//unevaluated
			}
		});

		specials.put("lambda".toUpperCase(), new SpecialOperation() {
			@Override
			public LispObject apply(LispList argForms) {
				return new CompoundProcedure(argForms.car(), (ConsCell)argForms.cdr(), argEnv);
			}
		});

		specials.put("define".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispList argForms) {
				//extract components
				LispObject varNameForm = argForms.car();

				if(varNameForm.isAtom()) {
					LispObject value = null;
					value = argForms.cdr().car().eval(argEnv);
					argEnv.intern(varNameForm.toString(), value) ;
				} else {
					SymbolAtom procName = (SymbolAtom) varNameForm.car() ;
					
					LispObject procParamList = varNameForm.cdr() ;
					
					LispList procBody = (LispList) argForms.cdr() ;
					argEnv.intern(procName.toString(),
							new CompoundProcedure(procParamList, procBody, argEnv)) ;
				}
				return varNameForm;
			}
		}) ;
		
		specials.put("let".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispList argForms) {
				LispList bindingList = (LispList)argForms.car();
				LispList body = (LispList) argForms.cdr();
				
//				LispObject[] bindings = bindingList.toArray();

				ChildEnvironment letEnv = new ChildEnvironment(argEnv);

				Iterator<LispObject> itrBindings = bindingList.iterator();
				
				while(itrBindings.hasNext()) {
					LispObject binding = itrBindings.next() ;
					
					if(binding.isAtom()) {
						binding = new ConsCell(binding, NilAtom.nil) ;
					}
					SymbolAtom symName = (SymbolAtom) binding.car();
					LispObject value = null;
					value = binding.cdr().car().eval(argEnv);

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