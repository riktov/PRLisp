class ConsCell extends LispObject {
    private LispObject car ;
    private LispObject cdr ;

    //constructors
    public ConsCell() {
	this.car = new NilObject() ;
	this.cdr = new NilObject() ;
    }
    public ConsCell(LispObject car, LispObject cdr) {
	this.car = car ;
	this.cdr = cdr ;
    }

    //accessors
    public LispObject car() { return this.car ;}
    public LispObject cdr() { return this.cdr ;}
    public void setCar(LispObject o) { this.car = o ; }
    public void setCdr(LispObject o) { this.cdr = o ; }
    //methods
    /*
    public void print() {
	System.out.print("(") ;
	this.car.print() ;
	this.cdr.printAsCdr() ;
	System.out.print(")") ;
    }

    public void printAsCdr() {
	System.out.print(" ") ;
	this.car.print() ;
	this.cdr.printAsCdr() ;
    }
    */
    public String toString() {
	return '(' + this.car.toString() + this.cdr.toStringCdr() + ')' ;
    }

    public String toStringCdr() {
	return ' ' + this.car.toString() + this.cdr.toStringCdr() ;
    }
}
