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
    
    public String toString() {
	return this.data.toString() ;
    }

    public String toStringCdr() {
	return "->" + this.toString() ;
    }
    
}

class NilAtom implements LispObject {
    public String toString() { return "NIL" ; }
    public String toStringCdr() { return "" ; }
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
