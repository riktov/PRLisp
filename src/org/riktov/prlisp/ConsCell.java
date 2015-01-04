package org.riktov.prlisp;

import java.util.ArrayList;

import java.util.Arrays;
//import java.util.Iterator;


class ConsCell extends LispObject {
	private LispObject car;
	private LispObject cdr;

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

	// methods
	@Override public String toString() {
		return '(' + this.car.toString() + this.cdr.toStringCdr() + ')';
	}

	@Override public String toStringCdr() {
		return ' ' + this.car.toString() + this.cdr.toStringCdr();
	}

	/**
	 * EVAL
	 * Take the value of the first form as a procedure, and APPLY the evaluated results 
	 * of the remaining forms, in the environment env.
	 * 
	 * @param env
	 *            The Environment in which this object is evaluated
	 * @return The LispObject resulting from the evaluation
	 */
	@Override LispObject eval(Environment env) {
		LispProcedure proc = (LispProcedure) car.eval(env);// this can return
															// null...

		LispObject rest = cdr;
		if (rest.isNull()) {
			return proc.apply();
		} else {

			LispObject[] unevaluatedArgs = ((ConsCell) rest).toArray();
			LispObject[] evaluatedArgs = proc.ProcessArguments(unevaluatedArgs,
					env);

			return proc.apply(evaluatedArgs);// ...which will raise a
												// NullPointerException here
		}
	}

	/**
	 * Converts the ConsCell to an array of LispObject
	 * @return an Array of LispObject
	 */
	LispObject[] toArray() {
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
}