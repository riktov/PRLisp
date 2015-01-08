package org.riktov.prlisp;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Iterator;

import java.util.Iterator;

import org.junit.Test;

interface LispList extends Iterable {
	public LispList evalList(Environment env) ;
	public LispObject evalSequence(Environment env) ;
	boolean isNull();
	public LispObject car() ;
	public LispObject cdr() ;
	public LispObject[] toArray();

}

class ConsCell extends LispObject implements LispList {
	LispObject car;
	LispObject cdr;

	/**
	 * Constructor 
	 * @param car
	 * @param cdr
	 */
	public ConsCell(LispObject car, LispObject cdr) {
		this.car = car;
		this.cdr = cdr;
	}

	/**
	 * Constructor
	 * @param argObjects
	 *            An array of LispObjects, which are built into a list
	 */
	public ConsCell(LispObject[] argObjects) {
		this.car = argObjects[0];
		if (argObjects.length > 1) {
			this.cdr = new ConsCell(Arrays.copyOfRange(argObjects, 1,
					argObjects.length));
		} else {
			this.cdr = new NilAtom();
		}
	}

	/**
	 * public ConsCell(ArrayList<LispObject> argList) { Iterator<LispObject> it
	 * = argList.iterator() ;
	 * 
	 * ConsCell current = this ; while(it.hasNext()) { current.car = it.next() ;
	 * current.cdr = new ConsCell)current.cdr ; }
	 * 
	 * LispObject[] arr = new LispObject[argList.size()];
	 * 
	 * 
	 * argList.toArray(arr);
	 * 
	 * }
	 */

    public boolean isAtom() { return false ; }

	// accessors
	public LispObject car() { return this.car; }
	public LispObject cdr() { return this.cdr; }

	// public void setCar(LispObject o) { this.car = o ; }
	// public void setCdr(LispObject o) { this.cdr = o ; }

	// factory methods
	/**
	 * Factory Method
	 * @param argList An array of LispObjects, which are built into a list
	 * @return A newly created LispObject, of class NilAtom or ConsCell
	 */
	public static LispObject make(LispObject[] argList) {
		if (argList.length == 0) {
			return new NilAtom();
		} else {
			return new ConsCell(argList);
		}
	}

	/**
	 * Factory method
	 * @param n
	 * @return
	 */
	public static LispObject make(NilAtom n) {
		return n;
	}

	/**
	 * EVAL
	 * Take the value of the first form as a procedure, and APPLY it to the evaluated results 
	 * of the remaining forms, in the environment env.
	 * 
	 * @param env
	 *            The Environment in which this object is evaluated
	 * @return The LispObject resulting from the evaluation
	 */
	@Override LispObject eval(Environment env) {
		//System.out.println("eval(Environment): " + this.toString()) ;
		LispProcedure proc = (LispProcedure)car.eval(env);
				
		LispList argsToApply = proc.processArguments((LispList)cdr, env);
	
		//System.out.println("eval(Environment): argsToApply: " + argsToApply.toString()) ;		
		return proc.apply(argsToApply);// ...which will raise a										// NullPointerException here
		}

	// methods
	@Override public String toString() {
		return '(' + this.car.toString() + this.cdr.toStringCdr() + ')';
	}

	@Override public String toStringCdr() {
		return ' ' + this.car.toString() + this.cdr.toStringCdr();
	}

	/**
	 * Converts the ConsCell to an array of LispObject
	 * @return an Array of LispObject
	 */
	public LispObject[] toArray() {
		ArrayList<LispObject> al = new ArrayList<LispObject>();

		LispObject c = this;

		while (!c.cdr().isNull()) {
			al.add(c.car());
			c = c.cdr();
		}

		al.add(c.car());

		LispObject[] arr = new LispObject[al.size()];
		return al.toArray(arr);

		// System.out.println("Arguments: " + arr) ;
		// return arr;
		// return new LispObject [] { new Atom(5), new Atom(13) } ;
	}
	
	/**
	 * Treat this as a list of forms, and return a list comprising the values of forms, evaluated in env
	 * @return A list of the same length as this, comprising the evaluated subforms
	 */
	@Override public LispList evalList(Environment env) {
		ConsCell current = this ;
		
		if(current.cdr().isNull()) {
			return (LispList) new ConsCell(current.car.eval(env), current.cdr) ;
		} else {
			return (LispList) new ConsCell(current.car.eval(env), (LispObject) ((ConsCell)current.cdr).evalList(env)) ;
		}
	}

	/**
	 * Treat this as a list of forms, and evaluate them in turn in env, returning the last one
	 * @return The value of the last subform
	 */
	@Override public LispObject evalSequence(Environment env) {
		ConsCell current = this ;
		
		System.out.println("evalSequence(Environment): form: " + current.car.toString()) ;
		
		if(current.cdr().isNull()) {
			return current.car.eval(env) ;
		} else {
			current.car.eval(env) ;
			return ((ConsCell)current.cdr).evalSequence(env) ;
		}
	}

	@Override
	public Iterator<LispObject> iterator() {
		final ConsCell origin = this ;
		return new Iterator<LispObject>() {
			LispObject current = origin ;
			@Override
			public boolean hasNext() {
				return !current.cdr().isNull() ;
			}

			@Override
			public LispObject next() {
				LispObject currentCar = current.car() ;
				current = current.cdr() ;
				return currentCar ;
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
				
			}
			
		} ;
	}
}

