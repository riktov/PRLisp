package org.riktov.prlisp ;

//import java.util.List ;
import java.util.ArrayList ;

abstract class LispObject {
    public boolean isNull() { return false ; }
    /**
     * Most objects are self-evaluating. The two exceptions are symbols, 
     * whose values are looked up in the environment,
     * and (lists) cons cells, which are evaluated by applying their first 
     * element to the remainders
     */
    public LispObject eval(LispEnvironment env) { return this ;}
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

/*
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
    public static DataAtom make(int i) { return new IntAtom(i) ; }
    public static DataAtom make(float f) { return new FloatAtom(f) ; }
    public static DataAtom make(double d) { return new DoubleAtom(d) ; }
    abstract public boolean isPrimitive() ;
}

/** A PrimitiveDataAtom is a DataAtom whose data member is a primitive
 * All Java primitives except boolean can be treated as numbers, and we deal with
 * Lisp booleans separately, so a Primitive data holds a single numerical value
 */
abstract class PrimitiveDataAtom extends DataAtom {
    abstract DataAtom subtract(PrimitiveDataAtom other) ;
	abstract DataAtom add(PrimitiveDataAtom other) ;
    public boolean isPrimitive() { return true ;}
}

class IntAtom extends PrimitiveDataAtom {
    int intData ;

    //constructor
    public IntAtom(int i) { this.intData = i ; }

    IntAtom addThisClass(IntAtom other) { return new IntAtom(this.intData + other.intData) ; }
    DataAtom add(PrimitiveDataAtom other) { return addThisClass((IntAtom)other) ; } 

    IntAtom subtractThisClass(IntAtom other) { return new IntAtom(this.intData - other.intData) ; }
    DataAtom subtract(PrimitiveDataAtom other) { return subtractThisClass((IntAtom)other) ; } 
    
    boolean equals(IntAtom other) {
    	return intData == other.intData ;
    }
    
    boolean equals(float f) { return intData == f ; }
}


class FloatAtom extends PrimitiveDataAtom {
    float floatData ;
    
    //constructor
    public FloatAtom(float f) { this.floatData = f ; }

    DataAtom add(PrimitiveDataAtom n) {
        return (FloatAtom)make(this.floatData + ((FloatAtom)n).floatData) ;
    } 
    DataAtom subtract(PrimitiveDataAtom n) {
        return (FloatAtom)make(this.floatData - ((FloatAtom)n).floatData) ;
    }    
    
    boolean equals(float f) { return floatData == f ; }
    public String toString() { return Float.toString(floatData) ; }
}

class DoubleAtom extends PrimitiveDataAtom {
    double doubleData ;
    
    //constructor
    public DoubleAtom(double d) { this.doubleData = d ; }

    DataAtom add(PrimitiveDataAtom n) {
        return (DoubleAtom)make(this.doubleData + ((DoubleAtom)n).doubleData) ;
    } 
    DataAtom subtract(PrimitiveDataAtom n) {
        return (DoubleAtom)make(this.doubleData - ((DoubleAtom)n).doubleData) ;
    }    
    
    boolean equals(double d) { return doubleData == d ; }
    public String toString() { return Double.toString(doubleData) ; }
}


class ObjectDataAtom extends DataAtom {
    Object data ;

    public Object data() { return data ; }    
    public ObjectDataAtom(Object o) { this.data = o ; }
    public String toString() { return data.toString() ; }
    public boolean equals(int i) { return ((Integer)data).intValue() == i ;}
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
    public LispObject eval(LispEnvironment env) {
        return env.lookup(this.toString()) ;
    }
}

class ConsCell extends LispObject {
    private LispObject car ;
    private LispObject cdr ;

    public ConsCell(LispObject car, LispObject cdr) {
        this.car = car ;
        this.cdr = cdr ;
    }
    
    //accessors
    public LispObject car() { return this.car ;}
    public LispObject cdr() { return this.cdr ;}
    //public void setCar(LispObject o) { this.car = o ; }
    //public void setCdr(LispObject o) { this.cdr = o ; }
    
    //methods
    public String toString() {
        return '(' + this.car.toString() + this.cdr.toStringCdr() + ')' ;
    }

    public String toStringCdr() {
        return ' ' + this.car.toString() + this.cdr.toStringCdr() ;
    }
    
    /** EVAL returns a LispObject.
     * It can be an Atom or a LispProcedure
     */
    public LispObject eval (LispEnvironment env) {
        LispProcedure proc = (LispProcedure)car.eval(env) ;//this can return null...
        ConsCell rest = (ConsCell)cdr ;
        LispObject[] unevaluatedArgs =  rest.toArray() ;
        int numArgs = unevaluatedArgs.length ;
        
        LispObject[] evaluatedArgs = new LispObject[numArgs] ;

        int i ;
        for(i = 0 ; i < numArgs ; i++) {
            evaluatedArgs[i] = unevaluatedArgs[i].eval(env) ;
        }
        
        return proc.apply(evaluatedArgs) ;//...which will raise a NullPointerException here
    }

    LispObject[] toArray() {
        ArrayList<LispObject> al = new ArrayList<LispObject>() ;

        LispObject c = this ;

        while(!c.cdr().isNull()) {
            al.add(c.car()) ;
            c = c.cdr() ;
        }

	al.add(c.car()) ;

        LispObject[] arr = new LispObject[al.size()] ; 
        al.toArray(arr) ;

        //System.out.println("Arguments: " + arr) ;
        return arr ;
        //return new LispObject [] { new Atom(5), new Atom(13) } ;
    }

    
    int length() {
        if(cdr.isNull()) {
            return 1 ;
        } else {
            return 1 ; //+ cdr.length() ;
        }
    }
}
