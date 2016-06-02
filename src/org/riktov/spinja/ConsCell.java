package org.riktov.spinja;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Iterator;

import java.util.Iterator;

/**
 * The LispList interface allows both ConsCells, which have data members, and NilAtoms,
 * to be treated as lists, the latter as the empty list. In Common Lisp, NIL is both an atom and
 * a list, so it essentially has multiple inheritance, which is just the appropriate use for
 * an interface
 * 
 * In scheme it is an error to call CAR or CDR on '(), but in CL, both calls return NIL.
 * 
 * @author riktov@freeshell.org (Paul Richter)
 *
 */
interface LispList extends Iterable<LispObject>, LispObject {
	LispList listOfValues(Environment env) ;
	LispObject evalSequence(Environment env) ;
	public LispList cdrList() ;
	LispObject[] toArray();
	int length() ;
}

class ConsCell implements LispList {
	LispObject car;
	LispObject cdr;	//the next cell, or nil for the last cell in a proper list, or an atom if this is a dotted-pair

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
	 *            An array of LispObjects, which are built into a proper list
	 */
	public ConsCell(LispObject[] argObjects) {
		this.car = argObjects[0];
		if (argObjects.length > 1) {
			this.cdr = new ConsCell(Arrays.copyOfRange(argObjects, 1,
					argObjects.length));
		} else {
			this.cdr = NilAtom.nil;
		}
	}

	/*
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

	@Override public boolean isAtom() { return false ; }
	@Override public boolean isNull() { return false; }

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
	 * @throws LispAbortEvaluationException 
	 */
	@Override
	public LispObject eval(Environment env) {
		//System.out.println("ConsCell.eval(): " + this) ;
		
		LispObject firstVal = car.eval(env) ;
		
		LispProcedure proc = null ;
		try {
			proc = (LispProcedure)firstVal ;	
		} catch (ClassCastException exc) {
			new LispRestarter().offerRestarts("The object " + firstVal + " is not applicable.") ;
			throw new LispAbortEvaluationException() ;
		}
		
		//System.out.println(proc.toString()) ;
		//proc could be a compound procedure, a primitive, or a special form
		LispList argsToApply = proc.processArguments((LispList)cdr, env);			
		return proc.apply(argsToApply);
	}

	// methods
	@Override public String toString() {
		return toString(false) ;
	}
	
	public String toString(boolean omitParentheses) {
		String openParen = "" ;
		String closeParen = "";
		if(!omitParentheses) {
			openParen ="(" ;
			closeParen = ")" ;
		}
		return openParen + this.car.toString() + this.cdr.toStringCdr() + closeParen;
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

		ConsCell c = this;

		while (!c.cdr().isNull()) {
			al.add(c.car());
			c = (ConsCell) c.cdr();
		}

		al.add(c.car());

		LispObject[] arr = new LispObject[al.size()];
		return al.toArray(arr);
	}
	
	/**
	 * Treat this as a list of forms, and return a list comprising the values of the forms, evaluated in env.
	 * @return A list of the same length as this, comprising the evaluated forms
	 * 
	 * TODO: change this to evalAllAtoms
	 * @throws LispAbortEvaluationException 
	 */

	@Override public LispList listOfValues(Environment env)  {
		ConsCell current = this ;
		
		if(current.cdr().isNull()) {
			return new ConsCell(current.car.eval(env), current.cdr) ;
		} else {
			return new ConsCell(current.car.eval(env), ((LispList)current.cdr).listOfValues(env)) ;				
		}
	}

	/**
	 * Treat this as a list of forms, and evaluate them in turn in env, returning the last one
	 * @return The value of the last subform
	 * @throws LispAbortEvaluationException 
	 */
	@Override public LispObject evalSequence(Environment env) {
		ConsCell current = this ;
		
		if(current.cdr().isNull()) {
			return current.car.eval(env) ;
		} else {
			current.car.eval(env) ;
			return ((LispList)current.cdr).evalSequence(env) ;
		}
	}

	@Override
	public Iterator<LispObject> iterator() {
		final ConsCell origin = this ;
//		System.out.println("Creating iterator from " + this) ;
		return new Iterator<LispObject>() {
			LispList current = origin ;
			@Override
			public boolean hasNext() {
//				System.out.println("Iterator.hasNext(): " + current) ;
				return !current.isNull() ;
			}

			@Override
			public LispObject next() {//current is assumed to be a ConsCell
				ConsCell currentCell = (ConsCell) current ;
				LispObject currentCar = currentCell.car() ;
				current = (LispList)currentCell.cdr() ;
				return currentCar ;
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
			}			
		} ;
	}
	
	@Override
	public int length() {
		if(this.isNull()) {
			return 0 ;
		} else {
			return ((LispList)this.cdr()).length() + 1 ;
		}
	}

	@Override
	public LispList cdrList() {
		return (LispList) cdr ;
	}
}

