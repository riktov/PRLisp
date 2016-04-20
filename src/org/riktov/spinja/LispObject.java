package org.riktov.spinja ;

import java.util.Iterator;

/**
 * A LispObject is the base of all lisp objects.
 * 
 * @author riktov@freeshell.org (Paul Richter)
 *
 */

interface LispObject {
	public boolean isNull() ;
	public boolean isAtom() ;
	LispObject eval(Environment env) ;
	public String toStringCdr() ;	//String representation as a cdr of a list,
}

/*
abstract class LispObject {
    public boolean isNull() { return false ; }
    boolean isAtom() { return true ; }
    /**
     * Most objects are self-evaluating. The two exceptions are symbols, 
     * whose values are looked up in the environment,
     * and (lists) cons cells, which are evaluated by applying their first 
     * element to the remainders
     * @throws LispAbortEvaluationException 
     */
/*
	LispObject eval(Environment env) { return this ;}
    public String toStringCdr() { return " . " + this.toString() ; }

}
*/    

/** Atom HAS a data member, rather than IS a data member because the box classes like
 * Integer and String are final.
 */

//abstract class Atom implements LispObject {
abstract class Atom implements LispObject {
    //constructor
    //public Atom() { this.data = new NilAtom() ; }
    /* factory methods */
    public static Atom make(Object o) { return new ObjectAtom(o) ; }
    public static Atom make(String s) { return new StringAtom(s) ; }
    public static Atom make(int i) { return new ObjectAtom(i) ; }
    public static Atom make(boolean b) { return b ? new SymbolAtom("t") : NilAtom.nil ; }
    //accessors
    //implementation of LispObject
    //public String toString() { return data.toString() ; }
//	abstract public boolean isPrimitive() ;
	@Override public boolean isAtom() { return true; }
	@Override public LispObject eval(Environment env) { return this; }
	@Override public boolean isNull() { return false; }
	public String toStringCdr() { return " . " + this ; }
}

/** A DataAtom is an atom that holds some value as data. The Atoms that are not DataAtoms
 * are NilAtom and Procedure. DataAtom is abstract because different primitive data subclasses have different 
 * types for the data member.
 * @author: Paul Richter
 */
abstract class DataAtom extends Atom {
    //factory methods, dispatch on argument type
    //public static DataAtom make(float f) { return new FloatAtom(f) ; }
    //public static DataAtom make(double d) { return new DoubleAtom(d) ; }
    
    abstract public boolean isPrimitive() ;
}

/**
 * An ObjectAtom is an atom that holds its data in an Object object, rather than a numerical primitive.
 * @author paul
 *
 */
class ObjectAtom extends DataAtom {
    Object data ;

    public Object data() { return data ; }    
    public ObjectAtom(Object o) { this.data = o ; }
    @Override public String toString() { return data.toString() ; }
    //public boolean dataEquals(Object o) { return data.equals(o);}
    @Override public boolean isPrimitive() { return false ; }
}

/**
* NilAtom does not extend DataAtom because it requires no data. 
* It could also be a singleton class
* NilAtom implements the LispList interface because it is equivalent to an empty list
*/

final class NilAtom extends Atom implements LispList {
	public static final NilAtom nil = new NilAtom() ;
    @Override public String toString() { return "NIL" ; }
    @Override public String toStringCdr() { return "" ; }
    //If the cdr of a cons is nil, then the cons is the last element of a list,
    //so we just finish printing
    @Override public boolean isNull() { return true ; }
    @Override public LispList listOfValues(Environment env) { return this ; }
    @Override public LispObject evalSequence(Environment env) { return this ; }
    @Override public LispObject[] toArray() { return new LispObject[0] ; }
    
    @Override
    public Iterator<LispObject> iterator() {
		return new Iterator<LispObject>() {
			@Override public boolean hasNext() { return false; }
			@Override public LispObject next() { return nil ; }
			@Override public void remove() {}			
		};
	}
	@Override public int length() { return 0; }
	@Override public LispList cdrList() { return this; }
}


class StringAtom extends ObjectAtom {
	/*
	private String value ;
    public StringAtom(String s) { this.value = s ; }
    @Override public String toString() { return '"' + value + '"' ; }
    public String toStringUnquoted() { return value ; }
	 */
    public StringAtom(String s) { super(s) ; }
    @Override public String toString() { return '"' + super.toString() + '"' ; }
    public String toStringUnquoted() { return super.toString(); }
}

/**
 * A SymbolAtom can be bound to a value and interned in an environment.
 * @author paul
 *
 */
class SymbolAtom extends ObjectAtom {
    public SymbolAtom(String s) { super(s.toUpperCase()) ; }
    @Override public LispObject eval(Environment env) {
        LispObject o = env.lookup(this.toString()) ;

		if(o == null) {
			LispRestarter restarter = new LispRestarter() ;
			int choice = restarter.offerRestarts(";Unbound variable: " + this);
			System.out.println(choice) ;
			
			switch(choice) {
			case 1:
				break ;
			case 4:
				System.out.println("The restart is 4") ;
//				o = this.get("FIB") ;
				break ;
			default:
				throw new LispAbortEvaluationException() ;
				//System.out.println("The restart is default") ;
			}
		} 
		return o ;
    }
}
