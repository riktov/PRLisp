package org.riktov.prlisp;

import java.util.ArrayList;

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
}