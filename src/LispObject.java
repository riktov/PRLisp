package org.riktov.prlisp ;

import java.util.List ;
import java.util.ArrayList ;

abstract class LispObject {
    public boolean isNull() { return false ; }
    //    int length() ;
}

/** 
@class ValueObject - an object whose value can be taken; an atom or pair
procedures and conditions are not ValueObjects
 */
abstract class ValueObject extends LispObject {
    /* most atoms are self-evaluating */
    public LispObject eval(Environment env) { return this ;}
    abstract public String toStringCdr() ;
}

/** Atom HAS a data member, rather than IS a data member because the box classes like
 * Integer and String are final.
 */
class Atom extends ValueObject {
    //member data
    protected Object data ;
    
    //constructor
    //public Atom() { this.data = new NilAtom() ; }
    public Atom(Object o) { this.data = o ; }
    
    //accessors
    public Object data() { return data ; }
    
    //factory methods, dispatch on argument type
    public static Atom make(Object o) {
        return new Atom(o) ;
    }
    
    public static Atom make(String s) {
        return new StringAtom(s) ;
    }

    //implementation of LispObject
    public String toString() { return data.toString() ; }
    public String toStringCdr() { return " . " + this.toString() ; }
}

/**
* NilAtom does not extend Atom because it requires no data. 
* It could also be a singleton class
*/
class NilAtom extends ValueObject {
    public String toString() { return "NIL" ; }
    public String toStringCdr() { return "" ; }
    public boolean isNull() { return true ; }
}

/**
 *@class StringAtom
 */
class StringAtom extends Atom {
    public StringAtom(String s) { super(s) ; }
    public String toString() {
        return '"' + super.toString() + '"' ;
    }
}

class SymbolAtom extends Atom {
    public SymbolAtom(String s) { super(s.toUpperCase()) ; }
    public LispObject eval(Environment env) {
        return env.lookup(this.toString()) ;
    }
}

class ConsCell extends ValueObject {
    private ValueObject car ;
    private ValueObject cdr ;

    //constructors
    /*
    public ConsCell() {
	this.car = new NilAtom() ;
	this.cdr = new NilAtom() ;
    }
    */
    public ConsCell(ValueObject car, ValueObject cdr) {
        this.car = car ;
        this.cdr = cdr ;
    }
    
    //accessors
    public ValueObject car() { return this.car ;}
    public ValueObject cdr() { return this.cdr ;}
    //public void setCar(LispObject o) { this.car = o ; }
    //public void setCdr(LispObject o) { this.cdr = o ; }
    
    //methods
    public String toString() {
        return '(' + this.car.toString() + this.cdr.toStringCdr() + ')' ;
    }

    public String toStringCdr() {
        return ' ' + this.car.toString() + this.cdr.toStringCdr() ;
    }
    

    /** EVAL
     * 
     */
    public LispObject eval (Environment env) {
	/* eval() returns a LispObject.
	 * It can be an Atom or a LispProcedure
	 */
	LispProcedure proc = (LispProcedure)car.eval(env) ;
	return proc.apply(new Atom[0]) ;
	
	//        return new StringAtom("TODO") ;
    }

    int length() {
        if(cdr.isNull()) {
            return 1 ;
        } else {
            return 1 ; //+ cdr.length() ;
        }
    }
}

abstract class LispProcedure extends LispObject {
    abstract public LispObject apply(Atom []argVals) ;
}

abstract class PrimitiveProcedure extends LispProcedure {
}

class PrimitiveAdditionProcedure extends PrimitiveProcedure {
    public LispObject apply(Atom []argVals) {
	return argVals[0].data() + argVals[1].data() ;
    }
}

class CompoundProcedure extends LispProcedure {
    private LispObject body ;
    private String formalParamList[] ;
    private Environment env ;
    
    //constructors
    public CompoundProcedure(LispObject body, String []params) {
        //super(body) ;
        this.formalParamList = params ;
    }
    
    //implementation of LispObject
    public String toString() { return "#<FUNCTION :LAMBDA>" ;}

    /**
     *APPLY
     * Evaluate the procedure body in an environment created by 
     * extending the procedure's initial environment with the argument
     * bindings.
     */
    public LispObject apply(Atom []argVals) {
        return new NilAtom();
    }

    public static String[] paramStringArray(ConsCell params) {
        if(params.cdr().isNull()) {
            return new String[] { params.car().toString() } ;
        } else {
            List<String> l = new ArrayList<String>() ;
            return l.toArray(new String[l.size()]) ;
            
            //            l.add(params.car()) ;
            //        l.
        }
    }
}

/***
   ____
  /    \
 / EVAL|\
 |  __/ |
 | /    |
 \|APPLY/
  \____/      

    ______
  _/_     \_
 /    \     \
/      |     \
| EVAL |     |
|     /      |
|    | APPLY |
\    |       /
 \_   \_____/
   \______/      
*/
