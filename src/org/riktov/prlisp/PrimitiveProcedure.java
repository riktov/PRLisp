package org.riktov.prlisp;

import java.util.HashMap;

abstract class PrimitiveProcedure extends LispProcedure {
    String symbol ;
    public String symbol() { return this.symbol ; }
    
    /**
     * @param unevaluatedArgs an Array of LispObjects, which may be symbols or forms which have not been evaluated
     * @return array of LispObject, which are the evaluated results of all the unevaluatedArgs
     */
    public LispObject[] ProcessArguments(LispObject[] unevaluatedArgs, Environment evalEnv) {
        int numArgs = unevaluatedArgs.length ;
        
        LispObject[] evaluatedArgs = new LispObject[numArgs] ;

        int i ;
        for(i = 0 ; i < numArgs ; i++) {
            evaluatedArgs[i] = unevaluatedArgs[i].eval(evalEnv) ;
        }
        return evaluatedArgs ;
    }
    
    static HashMap<String, PrimitiveProcedure> initialPrimitives() {
    	HashMap<String, PrimitiveProcedure> primitives = new HashMap<String, PrimitiveProcedure>() ;
    	
		primitives.put("cons".toUpperCase(), 
			new PrimitiveProcedure() {
				public LispObject apply(LispObject[] argVals) {
					return new ConsCell(argVals[0], argVals[1]);
				}
			});
		
		primitives.put("car".toUpperCase(), 
				new PrimitiveProcedure() {
					public LispObject apply(LispObject[] argVals) {
						return argVals[0].car();
					}
				});
			
		primitives.put("cdr".toUpperCase(), 
				new PrimitiveProcedure() {
					public LispObject apply(LispObject[] argVals) {
						return argVals[0].cdr();
					}
				});

		primitives.put("null?".toUpperCase(), 
				new PrimitiveProcedure() {
					public LispObject apply(LispObject[] argVals) {
						if(argVals[0].isNull()) {
							return new SymbolAtom("t") ;
						} else {
							return new NilAtom() ;
						}
					}
				});
	
		primitives.put("+".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispObject[] argVals) {
						Number[] numericalArgs = numericalArgs(argVals);
						if(numericalArgs[0].getClass() == numericalArgs[1].getClass()) {
							numericalArgs[0] = numericalArgs[0].floatValue() + numericalArgs[1].floatValue() ;
							return new ObjectAtom(numericalArgs[0]) ;
						}
						return new ObjectAtom(numericalArgs[0].floatValue()
								+ numericalArgs[1].floatValue());
					}
				});
			
		primitives.put("-".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispObject[] argVals) {
						Number[] numericalArgs = numericalArgs(argVals);
						return new ObjectAtom(numericalArgs[0].floatValue()
								- numericalArgs[1].floatValue());
					}
				});
			
		primitives.put("*".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispObject[] argVals) {
						Number[] numericalArgs = numericalArgs(argVals);
						return new ObjectAtom(numericalArgs[0].floatValue()
								* numericalArgs[1].floatValue());
					}
				});
		
		primitives.put("/".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispObject[] argVals) {
						Number[] numericalArgs = numericalArgs(argVals);
						return new ObjectAtom(numericalArgs[0].floatValue()
								/ numericalArgs[1].floatValue());
					}
				});
		
		primitives.put(">".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispObject[] argVals) {
						Number[] numericalArgs = numericalArgs(argVals);
						if(numericalArgs[0].floatValue() > numericalArgs[1].floatValue()) {
							return new SymbolAtom("t") ;
						} else {
							return new NilAtom() ;
						}
					}
				});

		return primitives ;
    }
}

abstract class PrimitiveNumericalProcedure extends PrimitiveProcedure {
	Number[] numericalArgs(LispObject[] args) {
		Number[] numArgs = new Number[args.length] ; 
		int i ;
		for (i = 0 ; i < args.length ; i++) {
			ObjectAtom nda = (ObjectAtom)args[i] ;
			numArgs[i] = (Number)(nda.data) ;
		}
		return numArgs ;
	}
}