package org.riktov.prlisp;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Iterator;

import java.util.Iterator;

interface LispList extends Iterable<LispObject> {
	public LispList listOfValues(Environment env) ;
	public LispObject evalSequence(Environment env) ;
	//public void bindParamsToValues(Iterable<LispObject> argForms, Environment env) ;
	boolean isNull();
	public LispObject car() ;
	public LispObject cdr() ;
	public LispObject[] toArray();
	public int length() ;
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
			this.cdr = NilAtom.nil;
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
	 * @throws LispAbortEvaluationException 
	 */
	@Override LispObject eval(Environment env) {
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

		LispObject c = this;

		while (!c.cdr().isNull()) {
			al.add(c.car());
			c = c.cdr();
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

		//System.out.println("ConsCell.listOfValues(): form: " + current) ;
		
		if(current.cdr().isNull()) {
			return (LispList) new ConsCell(current.car.eval(env), current.cdr) ;
		} else {
			/*
			if(current.cdr.isAtom()) {
				return (LispList) new ConsCell(current.car.eval(env), current.cdr.eval(env)) ;								
			} else { */
				return (LispList) new ConsCell(current.car.eval(env), (LispObject) ((LispList)current.cdr).listOfValues(env)) ;				
			//}
		}
	}

	/**
	 * Treat this as a list of forms, and evaluate them in turn in env, returning the last one
	 * @return The value of the last subform
	 * @throws LispAbortEvaluationException 
	 */
	@Override public LispObject evalSequence(Environment env) {
		ConsCell current = this ;
		//System.out.println("ConsCell.evalSequence(): form: " + current.car.toString()) ;
		
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
			LispObject current = origin ;
			@Override
			public boolean hasNext() {
//				System.out.println("Iterator.hasNext(): " + current) ;
				return !current.isNull() ;
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
	
	/**
	 * We cannot use iterators because we may need to bind the entire cdr instead of the car of a cell.
	 * @param formalParams
	 * @param env
	 */
	public void bindToParams(LispObject formalParams, Environment env) {
		//System.out.println("bindToParams(): formalParams: " + formalParams + " argVals : " + this) ;

		LispObject currentParam = formalParams ;
		LispObject currentValCell = this ;	//cell may be ConsCell or Atom
		LispObject val  ;
		
		while(!currentValCell.isNull()) {
			if (!currentParam.isAtom()) {
				val = currentValCell.car() ;
				//System.out.println("- binding cell key: " + currentParam + " val :" + val  ) ;
				env.intern(currentParam.car().toString(), val) ;
				currentValCell = currentValCell.cdr() ;
				currentParam = currentParam.cdr() ;
			} else {
				val = currentValCell ;
				//System.out.println("- binding atom key: " + currentParam + " val :" + val  ) ;
				env.intern(currentParam.toString(), val) ;
				//env.printKeys();
				return ;
			}
		}
		//env.printKeys();
	}

	@Override
	public int length() {
		if(this.isNull()) {
			return 0 ;
		} else {
			return ((LispList)this.cdr()).length() + 1 ;
		}
	}
	
	/*
	public void bindParamsToValues(Iterable<LispObject> argVals, Environment env) {
		Iterator<LispObject> itrParams = this.iterator() ;
		Iterator<LispObject> itrVals  = argVals.iterator() ;
		String symbolName ;
		LispObject val ;
		
		//System.out.println("bindParamsToValues(): formalParams: " + this + " argVals :" + argVals) ;
		//System.out.println(itrParams.hasNext()) ;
		while(itrParams.hasNext()) {
			LispObject param = itrParams.next() ;
			if(param.isAtom()) {	//rest-binding
				symbolName = param.toString() ;
			} else {
				symbolName = param.car().toString() ;					
			}
			val = itrVals.next() ;

			//System.out.println("bindParamsToValues(): interning " + symbolName + " with value: " + val ) ;
			env.intern(symbolName, val) ;
		}

	}
	*/
}

