package org.riktov.prlisp ;

import java.util.HashMap ;

/**
 * Environment
 */
class Environment extends HashMap<String, LispObject> {
    /** 
     */
    Environment() {
	installParameters() ;
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
    
    boolean installParameters() {
	intern("nil",
	       new LispObject() {
		   public String toString() { return "NIL" ; }
		   public String toStringCdr() { return "" ; }//If the cdr of a cons is nil, then the cons is a list
		   public boolean isNull() { return true ; }
	       }) ;

	intern("t", new SymbolAtom("t")) ;
	
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
	       new PrimitiveProcedure() {
		   public LispObject apply(LispObject []argVals) {
		       Number num0 = (Number)((Atom)argVals[0]).data() ;
		       Number num1 = (Number)((Atom)argVals[1]).data() ;
		       return new Atom(num0.floatValue() + num1.floatValue()) ;
		   }
	       }) ;

	intern("-",
	       new PrimitiveProcedure() {
		   public LispObject apply(LispObject []argVals) {
		       Number num0 = (Number)((Atom)argVals[0]).data() ;
		       Number num1 = (Number)((Atom)argVals[1]).data() ;
		       return new Atom(num0.floatValue() - num1.floatValue()) ;
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
