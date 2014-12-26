package org.riktov.prlisp ;

import java.util.HashMap ;

/**
 * Environment
 */
class Environment extends HashMap<String, LispObject> {
    /** 
     */
    Environment() {    
        installPrimitive(new PrimitiveAdditionProcedure()) ;
        installPrimitive(new PrimitiveSubtractionProcedure()) ;
        installPrimitive(new PrimitiveConsProcedure()) ;
        installPrimitive(new PrimitiveCarProcedure()) ;
        
        System.out.println(keySet()) ;
    }
    
    LispObject lookup(String str) {
        LispObject o = this.get(str) ;
        
        if (o == null) {
            System.out.println("EVAL: variable " + str + " has no value") ;
        }
        
        return o ;
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
