package org.riktov.prlisp ;

import java.util.List ;
import java.util.ArrayList ;

abstract class LispObject {
    public boolean isNull() { return false ; }
    //    int length() ;
}

/** 
 * @class ValueObject - an object whose value can be taken; an atom or pair.
 * Procedures and conditions are not ValueObjects
 */
abstract class ValueObject extends LispObject {
    /* Atoms are by default self-evaluating */
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
    public static Atom make(Object o) { return new Atom(o) ; }
    public static Atom make(String s) { return new StringAtom(s) ; }

    //implementation of LispObject
    public String toString() { return data.toString() ; }
    public String toStringCdr() { return " . " + this.toString() ; }
}

/**
* NilAtom does not extend Atom because it requires no data. 
* It could also be a singleton class
*/
final class NilAtom extends ValueObject {
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
    
    /** EVAL returns a LispObject.
     * It can be an Atom or a LispProcedure
     */
    public LispObject eval (Environment env) {
        LispProcedure proc = (LispProcedure)car.eval(env) ;
        ConsCell arguments = (ConsCell)cdr ;

        Atom [] argvals = { new Atom(5), new Atom(13) } ;
        
            //return proc.apply(new Atom[0]) ;//TODO :
        return proc.apply(argvals) ;//TODO : 
                
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
    String symbol ;
    public String symbol() { return this.symbol ; }
}

class CompoundProcedure extends LispProcedure {
    private ValueObject body ;
    private String formalParamList[] ;
    private Environment env ;
    
    //constructors
    public CompoundProcedure(ValueObject body, String []params) {
        //super(body) ;
        this.formalParamList = params ;
    }
    
    //implementation of LispObject
    public String toString() { return "#<FUNCTION :LAMBDA>" ;}

    /**
     * APPLY
     * Evaluate the procedure body in an environment created by 
     * extending the procedure's initial environment with the argument
     * bindings.
     * @param argVals This is an array of Atoms (evaluated) that is passed to the method.
     * @return ValueObject This is either an Atom or a list
     */
    public ValueObject apply(Atom []argVals) {
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
