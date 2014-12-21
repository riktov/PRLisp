abstract class LispObject {
    /*
    public enum Tag {
	CONS,
	INTEGER,
	STRING,
	SYMBOL,
	LAMBDA
    } ;
    protected Tag tag ;
    public abstract void print() ;
    public abstract void printAsCdr() ;
    */
    //public abstract Object data() ;
    public abstract String toStringCdr() ;
}

class NilObject extends LispObject {
    /*
    public void print() { System.out.print("NIL") ; }
    public void printAsCdr() {
    }
    */
    
    public String toString() { return "NIL" ; }
    public String toStringCdr() { return "" ; }
}
