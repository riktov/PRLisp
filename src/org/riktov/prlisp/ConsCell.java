package org.riktov.prlisp;

import java.util.ArrayList;
import java.util.Arrays;

class ConsCell extends LispObject {
    private LispObject car ;
    private LispObject cdr ;

    public ConsCell(LispObject car, LispObject cdr) {
        this.car = car ;
        this.cdr = cdr ;
    }
    
    /**
     * 
     * 
    * @param argObjects An array of LispObjects, which are built into a list
     */
    public ConsCell(LispObject[] argObjects) {
    	this.car = argObjects[0] ;
    	if (argObjects.length > 1) {
        	this.cdr = new ConsCell(Arrays.copyOfRange(argObjects, 1, argObjects.length)) ;
    	} else {
    		this.cdr = new NilAtom() ;
    	}
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
     * @param env The Enviroment in which this object is evaluated
     * @return The LispObject resulting from the evaluation
     */
    LispObject eval (Environment env) {
        LispProcedure proc = (LispProcedure)car.eval(env) ;//this can return null...
        ConsCell rest = (ConsCell)cdr ;
        LispObject[] unevaluatedArgs =  rest.toArray() ;
        
        LispObject[] evaluatedArgs = proc.ProcessArguments(unevaluatedArgs, env) ;
        
        return proc.apply(evaluatedArgs) ;//...which will raise a NullPointerException here
    }

	LispObject[] toArray() {
		ArrayList<LispObject> al = new ArrayList<LispObject>();

		LispObject c = this;

		while (!c.cdr().isNull()) {
			al.add(c.car());
			c = c.cdr();
		}

		al.add(c.car());

		LispObject[] arr = new LispObject[al.size()];
		al.toArray(arr);

		// System.out.println("Arguments: " + arr) ;
		return arr;
		// return new LispObject [] { new Atom(5), new Atom(13) } ;
	}
}