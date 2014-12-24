package org.riktov.prlisp ;

import java.util.List ;
import java.util.ArrayList ;

interface LispObject {
    public boolean isNull() ;
    public String toStringCdr() ;
    public LispObject eval(Environment env) ;
    //    int length() ;
}

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

    //implementation of LispObject
    public boolean isNull() { return false ; }
    public String toString() { return this.data.toString() ; }
    public LispObject eval(Environment env) { return this ; }
    
    public String toStringCdr() { return " . " + this.toString() ; }
}

/**
* NilAtom does not extend Atom because it requires no data. 
* It could also be a singleton class
*/
class NilAtom implements LispObject {
    public String toString() { return "NIL" ; }
    public String toStringCdr() { return "" ; }
    public boolean isNull() { return true ; }
    public LispObject eval(Environment env) { return this ; }
}

/**
 *@ class StringAtom
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

class ConsCell implements LispObject {
    private LispObject car ;
    private LispObject cdr ;

    //constructors
    /*
    public ConsCell() {
	this.car = new NilAtom() ;
	this.cdr = new NilAtom() ;
    }
    */
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
    
    public boolean isNull() { return false ; }
    public LispObject eval (Environment env) {
        return new StringAtom("TODO") ;
    }

    int length() {
        if(cdr.isNull()) {
            return 1 ;
        } else {
            return 1 + cdr.length() ;
        }
    }
}

class Condition implements LispObject {
    public String toStringCdr() { return "Condition" ; }
    public boolean isNull() { return false ; } 
    public LispObject eval(Environment env) { return this ;}
}

class LispProcedure extends Atom {
    private LispObject body ;
    private String formalParamList[] ;
    private Environment env ;
    
    //constructors
    public LispProcedure(LispObject body) {
        super(body) ;
        this.body = body ;
    }
    public LispProcedure(LispObject body, ConsCell params) {
        super(body) ;
        //        this.formalParamList = params ;
    }
    
    //implementation of LispObject
    public String toString() { return "#<FUNCTION :LAMBDA>" ;}

    /**
     *APPLY
     * Evaluate the procedure body in an environment created by 
     * extending the procedure's initial environment with the argument
     * bindings.
     */
    public LispObject apply(ConsCell args) {
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
*/
