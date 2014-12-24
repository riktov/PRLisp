package org.riktov.prlisp ;

import java.util.HashMap ;

/**
 * @classEnvironment
 */
class Environment extends HashMap<String, LispObject> {
    public LispObject lookup(String str) {
        if (this.containsKey(str)) {
            return this.get(str) ;
        }
        System.out.println("ERROR: no binding for " + str) ;
        return new Condition() ;
    }
}

/**
 * @classEnvironment
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
        } ;
        return parent.lookup(str) ;
    }

}

