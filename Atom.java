class Atom extends LispObject {
    //member data
    protected Object data ;

    //constructor
    public Atom() { this.data = new NilObject() ; }
    public Atom(Object o) { this.data = o ; }

    //methods
    /*
    public void print() {
	System.out.print(this.data) ;
    }
    public void printAsCdr() {
	System.out.print(" . ") ;
	print() ;
    }
    */
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
	return " . " + this.toString() ;
    }
    
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
