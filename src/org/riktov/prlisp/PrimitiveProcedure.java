package org.riktov.prlisp;

import java.util.HashMap;

abstract class PrimitiveProcedure extends LispProcedure {
    //String symbol ;
    //public String symbol() { return this.symbol ; } 
//    public LispObject apply(LispObject argForms) { return n ; } //
    public LispObject apply() { return new NilAtom(); } //
   // public LispObject apply() { return new NilAtom() ; }
        
    static HashMap<String, PrimitiveProcedure> initialPrimitives() {
    	HashMap<String, PrimitiveProcedure> primitives = new HashMap<String, PrimitiveProcedure>() ;
    	
		primitives.put("cons".toUpperCase(), 
			new PrimitiveProcedure() {
				public LispObject apply(ConsCell argForms) {
					return new ConsCell(argForms.car, argForms.cdr.car());
				}
			});
		
		primitives.put("car".toUpperCase(), 
				new PrimitiveProcedure() {
					public LispObject apply(ConsCell argForms) {
						return argForms.car();
					}
				});
			
		primitives.put("cdr".toUpperCase(), 
				new PrimitiveProcedure() {
					public LispObject apply(ConsCell argForms) {
						return argForms.cdr;
					}
				});

		primitives.put("null?".toUpperCase(), 
				new PrimitiveProcedure() {
					public LispObject apply(ConsCell argForms) {
						if(argForms.car.isNull()) {
							return new SymbolAtom("t") ;
						} else {
							return new NilAtom() ;
						}
					}
				});
	
		primitives.put("+".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(ConsCell argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
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
					public LispObject apply(ConsCell argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return new ObjectAtom(numericalArgs[0].floatValue()
								- numericalArgs[1].floatValue());
					}
				});
			
		primitives.put("*".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(ConsCell argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return new ObjectAtom(numericalArgs[0].floatValue()
								* numericalArgs[1].floatValue());
					}
				});
		
		primitives.put("/".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(ConsCell argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return new ObjectAtom(numericalArgs[0].floatValue()
								/ numericalArgs[1].floatValue());
					}
				});
		
		primitives.put("=".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(ConsCell argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return Atom.make(numericalArgs[0].floatValue() == numericalArgs[1].floatValue()) ;
					}
				});

		primitives.put(">".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(ConsCell argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return Atom.make(numericalArgs[0].floatValue() > numericalArgs[1].floatValue()) ;
					}
				});

		primitives.put("<".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(ConsCell argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return Atom.make(numericalArgs[0].floatValue() < numericalArgs[1].floatValue()) ;
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