package org.riktov.prlisp;

import java.util.HashMap;

abstract class PrimitiveProcedure extends LispProcedure {
    //String symbol ;
    //public String symbol() { return this.symbol ; } 
//    public LispObject apply(LispObject argForms) { return n ; } //
   // public LispObject apply() { return new NilAtom(); } //
   // public LispObject apply() { return new NilAtom() ; }
        
    static HashMap<String, PrimitiveProcedure> initialPrimitives() {
    	HashMap<String, PrimitiveProcedure> primitives = new HashMap<String, PrimitiveProcedure>() ;
    	
		primitives.put("cons".toUpperCase(), 
			new PrimitiveProcedure() {
				public LispObject apply(LispList argForms) {
					return new ConsCell(argForms.car(), argForms.cdr().car());
				}
			});
		
		primitives.put("car".toUpperCase(), 
				new PrimitiveProcedure() {
					public LispObject apply(LispList argForms) {
						return argForms.car();
					}
				});
			
		primitives.put("cdr".toUpperCase(), 
				new PrimitiveProcedure() {
					public LispObject apply(LispList argForms) {
						return argForms.cdr();
					}
				});

		primitives.put("null?".toUpperCase(), 
				new PrimitiveProcedure() {
					public LispObject apply(LispList argForms) {
						if(argForms.car().isNull()) {
							return new SymbolAtom("t") ;
						} else {
							return NilAtom.nil ;
						}
					}
				});
	
		primitives.put("+".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispList argForms) {
//						System.out.println("primitive + apply():" + argForms) ;
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
					public LispObject apply(LispList argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return new ObjectAtom(numericalArgs[0].floatValue()
								- numericalArgs[1].floatValue());
					}
				});
			
		primitives.put("*".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispList argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return new ObjectAtom(numericalArgs[0].floatValue()
								* numericalArgs[1].floatValue());
					}
				});
		
		primitives.put("/".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispList argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return new ObjectAtom(numericalArgs[0].floatValue()
								/ numericalArgs[1].floatValue());
					}
				});
		
		primitives.put("=".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispList argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return Atom.make(numericalArgs[0].floatValue() == numericalArgs[1].floatValue()) ;
					}
				});

		primitives.put(">".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispList argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return Atom.make(numericalArgs[0].floatValue() > numericalArgs[1].floatValue()) ;
					}
				});

		primitives.put("<".toUpperCase(), 
				new PrimitiveNumericalProcedure() {
					public LispObject apply(LispList argForms) {
						Number[] numericalArgs = numericalArgs(argForms.toArray());
						return Atom.make(numericalArgs[0].floatValue() < numericalArgs[1].floatValue()) ;
					}
				});

		return primitives ;
    }
}

abstract class PrimitiveNumericalProcedure extends PrimitiveProcedure {
	/**
	 * Returns an array of Number objects
	 * @param args Array of ObjectAtoms
	 * @return Array of Numbers
	 */
	Number[] numericalArgs(LispObject[] args) {
		System.out.println("numericalArgs(LispObject[]):") ;
		Number[] numArgs = new Number[args.length] ; 
		int i ;
		for (i = 0 ; i < args.length ; i++) {
			ObjectAtom nda = (ObjectAtom)args[i] ;
			//System.out.println(nda.toString()) ;
			numArgs[i] = (Number)(nda.data) ;
		}
		return numArgs ;
	}
}