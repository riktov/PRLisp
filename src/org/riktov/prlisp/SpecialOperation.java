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

		specials.put("setq".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argVals) {
				String symbolName = argVals[0].toString().toUpperCase() ;
				env.put(symbolName, argVals[1]) ;
				return argVals[0] ;
			}

			/**
			 * Unevaluated: 0
			 * Evaluated: 1
			 */
			@Override
			public LispObject[] ProcessArguments(LispObject[] unevaluatedArgs,
					Environment argEnv) {
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
		
		specials.put("if".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argVals) {
				if(!argVals[0].isNull()) {
					return argVals[1].eval(argEnv) ;
				} else {
					return argVals[2].eval(argEnv) ;
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
			public LispObject apply(LispObject[] argVals) {
				int i ;
				LispObject result = new NilAtom();
				for(i = 0 ; i < argVals.length ; i++) {
					result = argVals[i].eval(argEnv) ;
				}
				return result ;
			}
		});

		specials.put("quote".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argVals) {
				return new ConsCell(argVals) ;
			}
		});
		
		specials.put("lambda".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argVals) {
				LispObject[] argList = ((ConsCell)argVals[0]).toArray() ;
				
				String[] argNames = new String[argList.length] ;
				int i ;
				for(i = 0 ; i < argList.length ; i++) {
					argNames[i] = argList[0].toString() ;
				}
				return new CompoundProcedure(argNames, argVals[1], argEnv);
			}
		});

		specials.put("let".toUpperCase(), new SpecialOperation() {
			public LispObject apply(LispObject[] argVals) {
				ConsCell bindingList = (ConsCell)argVals[0] ;
				LispObject[] bindings = bindingList.toArray() ;
				
				ChildEnvironment letEnv = new ChildEnvironment(argEnv) ;
				int i ;
				for(i = 0 ; i < bindings.length ; i++) {
					ConsCell binding = (ConsCell)bindings[i] ;
					SymbolAtom symName = (SymbolAtom)binding.car() ;
					LispObject value = binding.cdr().car() ;
					
					letEnv.intern(symName.toString(), value) ;
				}
				
				LispObject result = argVals[1].eval(letEnv) ;
				
				return result ; 
			}
		});
	
		/////////////////////
		return specials;
	}
}