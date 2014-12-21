interface LispObject {
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
    String toStringCdr() ;
}

