package org.riktov.prlisp ;

import java.util.List ;
import java.util.ArrayList ;

abstract class LispObject {
    public boolean isNull() { return false ; }
    /**
     * Most objects are self-evaluating. The two exceptions are symbols, whos values are looked up in the environment,
     * and (lists) cons cells, which are evaluated by applying their first element to the remainders
     */
    public LispObject eval(Environment env) { return this ;}
    public LispObject apply(LispObject[] argVals) {
        System.out.println("APPLY: " + this.toString() + " is not a function name; try using a symbol instead") ;
        return new NilAtom() ;
    }
    public String toStringCdr() { return " . " + this.toString() ; }
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
}

/**
* NilAtom does not extend Atom because it requires no data. 
* It could also be a singleton class
*/
final class NilAtom extends LispObject {
    public String toString() { return "NIL" ; }
    public String toStringCdr() { return "" ; } //If the cdr of a cons is nil, then the cons is a list
    public boolean isNull() { return true ; }
}

class StringAtom extends Atom {
    public StringAtom(String s) { super(s) ; }
    public String toString() { return '"' + super.toString() + '"' ; }
}

class SymbolAtom extends Atom {
    public SymbolAtom(String s) { super(s.toUpperCase()) ; }
    public LispObject eval(Environment env) {
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
    public LispObject eval (Environment env) {
        LispObject proc = car.eval(env) ;//this can return null...
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

        LispObject[] arr = new LispObject[al.size()] ; 
        al.toArray(arr) ;

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
