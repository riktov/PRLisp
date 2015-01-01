package org.riktov.prlisp;

import java.util.HashMap;

abstract class SpecialOperation extends LispProcedure {
	/**
	 * A special operation may choose to evaluate some arguments conditionally, rather than receiving them evaluated.
	 * So it may need the environment in which the arguments are to be evaluated,
	 */
	protected Environment argEnv;	
	
	/**
	 * Override this if the procedure wants some arguments to be evaluated in the calling environment beforehand
	 */
	public LispObject[] ProcessArguments(LispObject[] unevaluatedArgs, Environment argEnv) {
		this.argEnv = argEnv ;	//always necessary if any args need to be evaluated within apply()
		return unevaluatedArgs ;
	}

    //constructors


	static HashMap<String, SpecialOperation> initialSpecials(Environment env) {
		HashMap<String, SpecialOperation> specials = new HashMap<String, SpecialOperation>();

		/**
		 * setq - Treat the first argument as a symbol(unevaluated), and intern it with the value of the second argument
		 */
		specials.put("setq".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argForms) {
				String symbolName = argForms[0].toString().toUpperCase() ;
				LispObject assignedValue = argForms[1] ;
				
				env.put(symbolName, assignedValue) ;
				return assignedValue ;
			}

			/**
			 * Unevaluated: 0
			 * Evaluated: 1
			 */
			@Override
			public LispObject[] ProcessArguments(LispObject[] unevaluatedArgs, Environment argEnv) {
				this.argEnv = argEnv ;	//always necessary if any args need to be evaluated within apply()
				int numArgs = unevaluatedArgs.length;
				//System.out.println(unevaluatedArgs) ;
				
				LispObject[] evaluatedArgs = new LispObject[numArgs];

				evaluatedArgs[0] = unevaluatedArgs[0] ;
				int i;
				for (i = 1; i < numArgs; i++) {
					evaluatedArgs[i] = unevaluatedArgs[i].eval(argEnv);
				}
				//System.out.println(evaluatedArgs) ;
				return evaluatedArgs;
			}
		});
		
		/**
		 * if - Treat the first argument as a boolean(evaluated), and depending on its truth value
		 * return the value of the second or third argument.
		 */
		specials.put("if".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argForms) {
				LispObject condition = argForms[0] ;
				LispObject consequent = argForms[1] ;
				LispObject alternate = argForms[2] ;
				
				if(!condition.isNull()) {
					return consequent.eval(argEnv) ;
				} else {
					return alternate.eval(argEnv) ;
				}
			}

			/**
			 * Evaluated: 0
			 * Unevaluated: 1, 2
			 */
			@Override
			public LispObject[] ProcessArguments(LispObject[] unevaluatedArgs, Environment argEnv) {
				this.argEnv = argEnv ;	//always necessary if any args need to be evaluated within apply()
				int numArgs = unevaluatedArgs.length;
				
				LispObject[] evaluatedArgs = new LispObject[numArgs];

				evaluatedArgs[0] = unevaluatedArgs[0].eval(argEnv) ;
				
				int i;
				for (i = 1; i < numArgs; i++) {
					evaluatedArgs[i] = unevaluatedArgs[i];
				}
				return evaluatedArgs;
			}
		});

		specials.put("progn".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argForms) {
				int i ;
				LispObject result = new NilAtom();
				for(i = 0 ; i < argForms.length ; i++) {
					result = argForms[i].eval(argEnv) ;
				}
				return result ;
			}
		});

		specials.put("quote".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argForms) {
				return new ConsCell(argForms) ;
			}
		});
		
		specials.put("lambda".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argForms) {
				LispObject formalParamList = argForms[0] ;
				LispObject body = argForms[1] ; // TODO: gather the rest not just [1]

				String[] paramNames ;
				
				if(formalParamList.isNull()) {
					paramNames = new String[0] ;
				} else {
					LispObject[] argList = ((ConsCell)formalParamList).toArray() ;
					
					paramNames = new String[argList.length] ;
					
					int i ;
					for(i = 0 ; i < argList.length ; i++) {
						paramNames[i] = argList[i].toString() ;
					}
				}
				return new CompoundProcedure(paramNames, body, argEnv);
			}
		});

		specials.put("let".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argForms) {
				ConsCell bindingList = (ConsCell)argForms[0] ;
				LispObject body = argForms[1] ;

				LispObject[] bindings = bindingList.toArray() ;
				
				ChildEnvironment letEnv = new ChildEnvironment(argEnv) ;
				int i ;
				for(i = 0 ; i < bindings.length ; i++) {
					ConsCell binding = (ConsCell)bindings[i] ;
					SymbolAtom symName = (SymbolAtom)binding.car() ;
					LispObject value = binding.cdr().car() ;
					
					letEnv.intern(symName.toString(), value) ;
				}
				
				LispObject result = body.eval(letEnv) ;
				
				return result ; 
			}
		});
	
		/////////////////////
		return specials;
	}
}