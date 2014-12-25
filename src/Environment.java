package org.riktov.prlisp ;

import java.util.HashMap ;

/**
 * Environment
 */
class Environment extends HashMap<String, LispObject> {
    /** 
     */
    public Environment() {
        installPrimitive(new PrimitiveAdditionProcedure()) ;
        installPrimitive(new PrimitiveSubtractionProcedure()) ;
        installPrimitive(new PrimitiveConsProcedure()) ;
    }
    
    public LispObject lookup(String str) {
        return this.get(str) ;
    }

    public boolean installPrimitive(PrimitiveProcedure proc) {
        put(proc.symbol(), proc) ;
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
    public ChildEnvironment(Environment parent) {
        this.parent = parent ;
    }

    public LispObject lookup(String str) {
        if (this.containsKey(str)) {
            return this.get(str) ;
        }
        return parent.lookup(str) ;
    }

}

