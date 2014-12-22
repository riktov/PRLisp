//package org.riktov.prlisp ;

/** Atom HAS a data member, rather than IS a data member because the box classes like
 * Integer and String are final.
 */
class Atom implements LispObject {
    //member data
    protected Object data ;
    
    //constructor
    //public Atom() { this.data = new NilAtom() ; }
    public Atom(Object o) { this.data = o ; }
    
    //accessors
    public Object data() { return data ; }
    
    //factory methods
    public static Atom make(Object o) {
        return new Atom(o) ;
    }
    
    public static Atom make(String s) {
        return new StringAtom(s) ;
    }

    //testing methods
    public boolean isNull() { return false ; }
    //methods
    public String toString() {
        return this.data.toString() ;
    }
    
    public String toStringCdr() {
        return " . " + this.toString() ;
    }
}

/**
* NilAtom does not extend Atom because it requires no data. 
* It could also be a singleton class
*/
class NilAtom implements LispObject {
    public String toString() { return "NIL" ; }
    public String toStringCdr() { return "" ; }
    public boolean isNull() { return true ; }
}

class StringAtom extends Atom {
    public StringAtom(String s) {
        super(s) ;
    }

    public String toString() {
        return '"' + super.toString() + '"' ;
    }
}

class SymbolAtom extends Atom {
    public SymbolAtom(String s) { super(s.toUpperCase()) ; }
}
