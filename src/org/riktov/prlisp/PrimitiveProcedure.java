package org.riktov.prlisp;

import java.util.HashMap;

abstract class PrimitiveProcedure extends LispProcedure {
	// String symbol ;
	// public String symbol() { return this.symbol ; }
	// public LispObject apply(LispObject argForms) { return n ; } //
	// public LispObject apply() { return new NilAtom(); } //
	// public LispObject apply() { return new NilAtom() ; }

	static HashMap<String, PrimitiveProcedure> initialPrimitives(final Environment env) {
		HashMap<String, PrimitiveProcedure> primitives = new HashMap<String, PrimitiveProcedure>();

		primitives.put("cons".toUpperCase(), new PrimitiveProcedure() {
			public LispObject apply(LispList argForms) {
				requireArgumentCount(2, argForms, "cons") ;
				LispObject[] args = argForms.toArray() ;
				return new ConsCell(args[0], args[1]);
			}
		});

		primitives.put("car".toUpperCase(), new PrimitiveProcedure() {
			public LispObject apply(LispList argForms) {
				requireArgumentCount(1, argForms, "car") ;
				LispObject[] args = argForms.toArray() ;
				ConsCell theCons = (ConsCell)(args[0]) ;
				return ((ConsCell) theCons).car();
			}
		});


		primitives.put("cdr".toUpperCase(), new PrimitiveProcedure() {
			public LispObject apply(LispList argForms) {
				requireArgumentCount(1, argForms, "cdr") ;
				LispObject[] args = argForms.toArray() ;
				ConsCell theCons = (ConsCell)(args[0]) ;
				return ((ConsCell) theCons).cdr();
			}
		});

		primitives.put("env".toUpperCase(), new PrimitiveProcedure() {
			public LispObject apply(LispList argForms) {
				Object[] envKeyNames = env.keySet().toArray() ;
				LispObject[] envKeys = new LispObject[envKeyNames.length] ;
				int i ;
				for (i = 0 ; i < envKeyNames.length ; i++) {
					envKeys[i] = new SymbolAtom((String)envKeyNames[i]) ;
				}
				return new ConsCell(envKeys) ;
			}
		});

		primitives.put("read".toUpperCase(), new PrimitiveProcedure() {
			public LispObject apply(LispList argForms) {
				requireArgumentCount(1, argForms, "read") ;
				LispObject[] args = argForms.toArray() ;
				LispReader lr = new LispReader() ;
				return lr.read(((StringAtom)args[0]).toStringUnquoted()) ;
			}
		});

		primitives.put("null?".toUpperCase(), new PrimitiveProcedure() {
			public LispObject apply(LispList argForms) {
				requireArgumentCount(1, argForms, "null?") ;
				LispObject[] args = argForms.toArray() ;

				if (args[0].isNull()) {
					return new SymbolAtom("t");
				} else {
					return NilAtom.nil;
				}
			}
		});

		/**
		 * This could have been built-in in Lisp, but since it's useful to have a Java function to use
		 * in the interpreter, we will just let it be used as a primitive.
		 */
		primitives.put("length".toUpperCase(), new PrimitiveProcedure() {
			public LispObject apply(LispList argForms) {
				requireArgumentCount(1, argForms, "length") ;
				LispObject[] args = argForms.toArray() ;

				LispList lis = null ;
				try {
					lis = (LispList)args[0] ;
				} catch (ClassCastException exc) {
					new LispRestarter().offerRestarts("The object " + args[0] + ", passed as an argument to length, is not a list") ;
					throw new LispAbortEvaluationException() ;
				}
				return DataAtom.make(lis.length()) ;
			}
		});

		primitives.put("+".toUpperCase(), new PrimitiveNumericalProcedure() {
			public LispObject apply(LispList argForms) {
				
				Number[] numericalArgs = numericalArgs(argForms.toArray());					

				if(PrimitiveNumericalProcedure.isAllIntegers(numericalArgs)) {
					Integer accumulator = new Integer(0) ;
				
					int i;
					for(i = 0 ; i < numericalArgs.length ; i++) {
						accumulator = accumulator + numericalArgs[i].intValue() ;
					}
					return new ObjectAtom(accumulator) ;
				} else {
					Float accumulator = new Float(0.0) ;
					
					int i;
					for(i = 0 ; i < numericalArgs.length ; i++) {
						accumulator = accumulator + numericalArgs[i].floatValue() ;
					}
					return new ObjectAtom(accumulator) ;					
				}				
			}
		});

		primitives.put("-".toUpperCase(), new PrimitiveNumericalProcedure() {
			public LispObject apply(LispList argForms) {
				Number[] numericalArgs = numericalArgs(argForms.toArray());

				if(PrimitiveNumericalProcedure.isAllIntegers(numericalArgs)) {
					Integer accumulator = new Integer(numericalArgs[0].intValue()) ;
				
					int i;
					for(i = 1 ; i < numericalArgs.length ; i++) {
						accumulator = accumulator - numericalArgs[i].intValue() ;
					}
					return new ObjectAtom(accumulator) ;
				} else {
					Float accumulator = new Float(numericalArgs[0].floatValue()) ;
					
					int i;
					for(i = 1 ; i < numericalArgs.length ; i++) {
						accumulator = accumulator - numericalArgs[i].floatValue() ;
					}
					return new ObjectAtom(accumulator) ;					
				}				
			}
		});

		primitives.put("*".toUpperCase(), new PrimitiveNumericalProcedure() {
			public LispObject apply(LispList argForms) {
				Number[] numericalArgs = numericalArgs(argForms.toArray());

				if(PrimitiveNumericalProcedure.isAllIntegers(numericalArgs)) {
					Integer accumulator = new Integer(1) ;
				
					int i;
					for(i = 0 ; i < numericalArgs.length ; i++) {
						accumulator = accumulator * numericalArgs[i].intValue() ;
					}
					return new ObjectAtom(accumulator) ;
				} else {
					Float accumulator = new Float(1.0) ;
					
					int i;
					for(i = 0 ; i < numericalArgs.length ; i++) {
						accumulator = accumulator * numericalArgs[i].floatValue() ;
					}
					return new ObjectAtom(accumulator) ;					
				}				
			}
		});

		primitives.put("/".toUpperCase(), new PrimitiveNumericalProcedure() {
			public LispObject apply(LispList argForms) {
				Number[] numericalArgs = numericalArgs(argForms.toArray());
				return new ObjectAtom(numericalArgs[0].floatValue()
						/ numericalArgs[1].floatValue());
			}
		});

		primitives.put("=".toUpperCase(), new PrimitiveNumericalProcedure() {
			public LispObject apply(LispList argForms) {
				Number[] numericalArgs = numericalArgs(argForms.toArray());
				return Atom.make(numericalArgs[0].floatValue() == numericalArgs[1]
						.floatValue());
			}
		});

		primitives.put(">".toUpperCase(), new PrimitiveNumericalProcedure() {
			public LispObject apply(LispList argForms) {
				Number[] numericalArgs = numericalArgs(argForms.toArray());
				return Atom.make(numericalArgs[0].floatValue() > numericalArgs[1]
						.floatValue());
			}
		});

		primitives.put("<".toUpperCase(), new PrimitiveNumericalProcedure() {
			public LispObject apply(LispList argForms) {
				Number[] numericalArgs = numericalArgs(argForms.toArray());
				return Atom.make(numericalArgs[0].floatValue() < numericalArgs[1]
						.floatValue());
			}
		});

		return primitives;
	}
}

abstract class PrimitiveNumericalProcedure extends PrimitiveProcedure {
	/**
	 * Returns an array of Number objects
	 * 
	 * @param args
	 *		Array of ObjectAtoms
	 * @return Array of Numbers
	 */
	Number[] numericalArgs(LispObject[] args) {
		// System.out.println("numericalArgs(LispObject[]):") ;
		Number[] nums = new Number[args.length];
		int i;
		ObjectAtom nda = null ;
		for (i = 0; i < args.length; i++) {
			try {
				nda = (ObjectAtom) args[i];
				// System.out.println(nda.toString()) ;
				nums[i] = (Number) (nda.data);				
			} catch(ClassCastException exc) {
				new LispRestarter().offerRestarts(";The object " + args[i] + " is not a numeric type.") ;
				throw new LispAbortEvaluationException() ;
			}
		}
		return nums;
	}
	
	static boolean isAllIntegers(Number[] nums) {
		int i ;
		for(i = 0 ; i < nums.length ; i++) {
			if(nums[i].getClass() != Integer.class) {
				return false ;
			}
		}
		return true ;
	}
}