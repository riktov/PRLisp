package org.riktov.spinja;

//import java.util.List ;
//import java.util.ArrayList ;

/**
 * A LispProcedure can be a compound procedure (lambda), a primitive procedure, or a special operation
 * @author Paul RICHTER &lt;riktov@freeshell.de&gt;
 *
 */
abstract class LispProcedure extends LispObject {
	abstract LispObject apply(LispList argsToApply) ;
    public LispList processArguments(LispList argForms, Environment evalEnv) {
    	//System.out.println("LispProcedure.processArguments() : argForms: " + argForms) ;
		return argForms.listOfValues(evalEnv) ;
	}
	protected boolean requireArgumentCount(int count, LispList argForms, String procName) {
		int len = argForms.length() ;
		if(len != count) {
			new LispRestarter().offerRestarts("The procedure " + procName + " has been called with " + len + " arguments; it requires exactly " + count + " argument(s).") ;
			throw new LispAbortEvaluationException() ;
		}	
		return true ;
	}

	protected boolean requireArgumentCountAtLeast(int count, LispList argForms, String procName) {
		int len = argForms.length() ;
		if(len < count) {
			new LispRestarter().offerRestarts("The procedure " + procName + " has ben called with " + len + " arguments; it requires at least " + count + " argument(s).") ;
			throw new LispAbortEvaluationException() ;
		}	
		return true ;
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
	 * Copying CLISP notation
	 */
	@Override public String toString() {
		ConsCell paramsAndBody = new ConsCell((LispObject)formalParams, (LispObject) body) ;
		return "#<FUNCTION :LAMBDA " + paramsAndBody.toString(true) + ">"; 
		}

	/**
	 * accessors
	 */
	LispObject formalParams() { return formalParams ; }//can be a list, or nil, or a rest-bound atom
	LispList body() { return body ; }	

	/**
	 * APPLY Evaluate in turn each subform of the procedure body, in an environment created by extending
	 * the procedure's own environment with the argument bindings.
	 * 
	 * @param argVals
	 *            This is an array of objects (evaluated) that is passed to the
	 *            method.
	 * @return LispObject The value of the last form in the procedure body
	 * @throws LispAbortEvaluationException 
	 */
	public LispObject apply(LispList argForms) {
		//System.out.println("CompoundProcedure.apply() : argForms: " + argForms + " formalParams: " + formalParams) ;
		Environment newEnv ;

		if(!argForms.isNull()) {
			newEnv = new ChildEnvironment(env);
			((ConsCell)argForms).bindToParams(formalParams, newEnv) ;
		} else {
			newEnv = env ;
		}
		return body.evalSequence(newEnv) ;
	}	
}
