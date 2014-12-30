package org.riktov.prlisp ;

abstract class LispObject {
    public boolean isNull() { return false ; }
    /**
     * Most objects are self-evaluating. The two exceptions are symbols, 
     * whose values are looked up in the environment,
     * and (lists) cons cells, which are evaluated by applying their first 
     * element to the remainders
     */
    public LispObject eval(Environment env) { return this ;}
    public String toStringCdr() { return " . " + this.toString() ; }

    /**
     * The methods below here are the base error implementations of the 
     * fundamental methods that can be called on 
     * a LispObject. Some of these might be unnecessary with judicious use of 
     * downcasting and handling of the resulting ClassCastException
     */
    /*
    public LispObject apply(LispObject[] argVals) {
        System.out.println("APPLY: " + this.toString() + " is not a function name; try using a symbol instead") ;
        return new NilAtom() ;
    }
    */

    public LispObject car() {
        System.out.println("CAR: " + this.toString() + " is not a list") ;
        return new NilAtom() ;
    }
    
    public LispObject cdr() {
        System.out.println("CDR: " + this.toString() + " is not a list") ;
        return new NilAtom() ;
    }
}

/**
abstract class ObsoleteValueObject extends LispObject {
    abstract public String toStringCdr() ;
}
*/

/** Atom HAS a data member, rather than IS a data member because the box classes like
 * Integer and String are final.
 */

class Atom extends LispObject {
    //constructor
    //public Atom() { this.data = new NilAtom() ; }
    /* factory methods */
    //accessors
    //implementation of LispObject
    //public String toString() { return data.toString() ; }
//	abstract public boolean isPrimitive() ;
}

/** A DataAtom is an atom that holds some value as data. The Atoms that are not DataAtoms
 * are NilAtom and Procedure. DataAtom is abstract because different subclasses have different 
 * types for the data member.
 */
abstract class DataAtom extends Atom {
    //factory methods, dispatch on argument type
    public static DataAtom make(Object o) { return new ObjectDataAtom(o) ; }
    public static DataAtom make(String s) { return new StringAtom(s) ; }
    //public static DataAtom make(int i) { return new IntAtom(i) ; }
    //public static DataAtom make(float f) { return new FloatAtom(f) ; }
    //public static DataAtom make(double d) { return new DoubleAtom(d) ; }
    
    abstract public boolean isPrimitive() ;
}


class ObjectDataAtom extends DataAtom {
    Object data ;

    public Object data() { return data ; }    
    public ObjectDataAtom(Object o) { this.data = o ; }
    public String toString() { return data.toString() ; }
    public boolean dataEquals(Object o) { return data.equals(o);}
    public boolean isPrimitive() { return false ; }
}

/**
* NilAtom does not extend Atom because it requires no data. 
* It could also be a singleton class
*/

final class NilAtom extends Atom {
    public String toString() { return "NIL" ; }
    public String toStringCdr() { return "" ; }
    //If the cdr of a cons is nil, then the cons is the last element of a list,
    //so we just finish printing
    public boolean isNull() { return true ; }
}

class StringAtom extends ObjectDataAtom {
    public StringAtom(String s) { super(s) ; }
    public String toString() { return '"' + super.toString() + '"' ; }
}

class SymbolAtom extends ObjectDataAtom {
    public SymbolAtom(String s) { super(s.toUpperCase()) ; }
    public LispObject eval(Environment env) {
        return env.lookup(this.toString()) ;
    }
}
