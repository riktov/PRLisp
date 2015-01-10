package org.riktov.prlisp;

import java.util.Iterator;

//import java.util.List ;
//import java.util.ArrayList ;

/**
 * A LispProcedure can be a compound procedure (lambda), a primitive procedure, or a special operation
 * @author Paul RICHTER &lt;riktov@freeshell.de&gt;
 *
 */
abstract class LispProcedure extends LispObject {
	public abstract LispObject apply(LispList argsToApply) ;
    public LispList processArguments(LispList argForms, Environment evalEnv) {
    	//System.out.println("processArguments() : unevaluated: " + unevaluatedArgForms) ;
		return argForms.evalList(evalEnv) ;
	}

}

class CompoundProcedure extends LispProcedure {
	private LispObject formalParams; //may be a list, or an Atom for list-binding
	private LispList body;
	private Environment env;

	// constructors
	/**
	 * Constructor
	 * @param formalParams
	 * @param body
	 * @param env
	 */
	public CompoundProcedure(LispObject formalParams, LispList body, Environment env) {
		this.formalParams = formalParams;
		this.body = body;
		this.env = env;
	}

	// implementation of LispObject
	/**
	 * Copying CLISP
	 */
	@Override public String toString() {
		ConsCell paramsAndBody = new ConsCell((LispObject)formalParams, (LispObject) body) ;
		return "#<FUNCTION :LAMBDA " + paramsAndBody.toString(true) + ">"; 
		}

	/**
	 * accessors
	 */
	LispObject formalParams() { return formalParams ; }
	LispList body() { return body ; }	

	/**
	 * APPLY Evaluate in turn each subform of the procedure body, in an environment created by extending
	 * the procedure's own environment with the argument bindings.
	 * 
	 * @param argVals
	 *            This is an array of objects (evaluated) that is passed to the
	 *            method.
	 * @return LispObject The value of the last form in the procedure body
	 */
	public LispObject apply(LispList argForms) {
		//System.out.println("apply() : argForms: " + argForms + " formalParams: " + formalParams) ;
		ChildEnvironment newEnv = new ChildEnvironment(env);
		
		if(!argForms.isNull()) {
			//int i = 0 ;

			LispObject currentCell = (LispObject)argForms ;

			/*
			 * String symbolName ;
			LispObject val ;
			
			System.out.println("apply(): currentCell : " + currentCell.toString()) ;
			
			//TODO: Refactor this with LET for pairing formalParams and values
			//iterate through the arg forms and formal params, interning them in turn
						
			Iterator<LispObject> itrParams = formalParams.iterator() ;
			Iterator<LispObject> itrForms  = argForms.iterator() ;

			while(itrParams.hasNext()) {
				LispObject next = itrParams.next() ;
				if(next.isAtom()) {
					symbolName = next.toString() ;
				} else {
					symbolName = next.car().toString() ;					
				}
				val = itrForms.next().eval(newEnv) ;

				System.out.println("apply(ConsCell): interning " + symbolName + " with value: " + val ) ;
				newEnv.intern(symbolName, val) ;
			}
			*/
			LispList argVals = argForms.evalList(env) ;
//			formalParams.bindParamsToValues(argVals, newEnv) ;
			((ConsCell)argVals).bindToParams(formalParams, newEnv) ;
			//newEnv.printKeys();
			/*
			while(!currentCell.isNull()) {
				symbolName = formalParams[i++] ;
				val = currentCell.car().eval(env) ;
				System.out.println("apply(ConsCell): interning " + symbolName + " with value: " + val ) ;
				newEnv.intern(symbolName, val) ;
				currentCell = currentCell.cdr();
			}
			*/
		}
		return body.evalSequence(newEnv) ;
	}	
}
