package org.riktov.prlisp ;

import java.util.HashMap;

/**
 * Environment
 */
class Environment extends HashMap<String, LispObject> {
    /** 
     */
    Environment() {
	installConstants() ;
	installPrimitives() ;
        System.out.println(keySet()) ;
    }

    LispObject lookup(String str) {
        LispObject o = this.get(str) ;
        
        if (o == null) {
            System.out.println("EVAL: variable " + str + " has no value") ;
        }
        
        return o ;
    }
    
    boolean installConstants() {
	NilAtom n = new NilAtom() ;//the singleton in the environment
	intern(n.toString(), n) ;
	
	SymbolAtom t = new SymbolAtom("t") ;//the singleton in the environment
	intern(t.toString(), t) ;
	
	return true ;
    }
    
    boolean installPrimitives() {
	        //installPrimitive(new PrimitiveAdditionProcedure()) ;
        //installPrimitive(new PrimitiveSubtractionProcedure()) ;
        //installPrimitive(new PrimitiveConsProcedure()) ;
        installPrimitive(new PrimitiveCarProcedure()) ;

	intern("cons",
	       new PrimitiveProcedure() {
		   public LispObject apply(LispObject []argVals) {
		       return new ConsCell(argVals[0], argVals[1]) ;
		   }
	       }) ;

	intern("car",
	       new PrimitiveProcedure() {
		   public LispObject apply(LispObject []argVals) {
		       return argVals[0].car() ;
		   }
	       }) ;

	intern("cdr",
	       new PrimitiveProcedure() {
		   public LispObject apply(LispObject []argVals) {
		       return argVals[0].cdr() ;
		   }
	       }) ;

	/*addition*/
	intern("+",
		new PrimitiveNumericalProcedure() {
		public LispObject apply(LispObject []argVals) {
		   Number[] numericalArgs = numericalArgs(argVals) ;
		   return new ObjectDataAtom(numericalArgs[0].floatValue() + numericalArgs[1].floatValue()) ;
		   }
	  }) ;

	intern("-",
			new PrimitiveNumericalProcedure() {
			public LispObject apply(LispObject []argVals) {
			   Number[] numericalArgs = numericalArgs(argVals) ;
			   return new ObjectDataAtom(numericalArgs[0].floatValue() - numericalArgs[1].floatValue()) ;
			   }
		  }) ;

	return true ;
    }
  
    boolean installPrimitive(PrimitiveProcedure proc) {
        return intern(proc.symbol(), proc) ;
    }

    
    boolean intern(String symName, LispObject o) {
        put(symName.toUpperCase(), o) ;
        return true ;
    }
}

/**
 * Environment
 */
class ChildEnvironment extends Environment{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//members
    private Environment parent ;
    
    /** constructor
     * @param parent Parent environment.
     */
    ChildEnvironment(Environment parent) {
        this.parent = parent ;
    }
    
    LispObject lookup(String str) {
        if (this.containsKey(str)) {
            return this.get(str) ;
        }
        return parent.lookup(str) ;
    }
}
