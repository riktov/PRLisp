package org.riktov.prlisp;

//import java.util.List ;
//import java.util.ArrayList ;

/**
 * A LispProcedure can be a compound procedure (lambda), a primitive procedure, or a special operation
 * @author paul
 *
 */
abstract class LispProcedure extends LispObject {
//	public abstract LispObject apply(LispObject[] argVals);
	public LispObject apply(LispObject argForms) {
		if(argForms.isNull()) {
			return apply(new NilAtom()) ;
		} else {
			return apply((ConsCell)argForms) ;
		}
	}
	public abstract LispObject apply(ConsCell argForms) ;
	public abstract LispObject apply(NilAtom n) ;
	public abstract LispObject[] ProcessArguments(LispObject[] unevaluatedArgs,
			Environment evalEnv);
}

class CompoundProcedure extends LispProcedure {
	private String formalParams[];
	private ConsCell body;
	private Environment env;

	// constructors
	public CompoundProcedure(String[] formalParams, ConsCell body, Environment env) {
		this.formalParams = formalParams;
		this.body = body;
		this.env = env;
	}

	// implementation of LispObject
	@Override public String toString() {
		String paramList = new String() ;
		String sep = "" ;
		int i ;
		for(i = 0 ; i < formalParams.length ; i++) {
			if(i > 0) { sep = " " ; }
			paramList = paramList + formalParams[i] + sep ;
		}
		
		LispObject[] bodyForms = body.toArray() ;
		String bodyFormsString = new String() ;
		
		sep = "" ;
		for(i = 0 ; i < bodyForms.length ; i++) {
			if(i > 0) { sep = " " ; }			
			bodyFormsString = bodyFormsString + sep + bodyForms[i].toString() ;
		}
		
		return "#<FUNCTION :LAMBDA (" + paramList + ") " + bodyFormsString + ">"; 
		}

	/**
	 * 
	 */
	String[] formalParams() { return formalParams ; }
	LispObject body() { return body ; }
	
	/**
	 * @param unevaluatedArgs
	 *            an Array of LispObjects, which may be symbols or forms which
	 *            have not been evaluated
	 * @return array of LispObject, which are the evaluated results of all the
	 *         unevaluatedArgs
	 */
	@Override public LispObject[] ProcessArguments(LispObject[] unevaluatedArgs,
			Environment evalEnv) {
		int numArgs = unevaluatedArgs.length;

		LispObject[] evaluatedArgs = new LispObject[numArgs];

		int i;
		for (i = 0; i < numArgs; i++) {
			evaluatedArgs[i] = unevaluatedArgs[i].eval(evalEnv);
		}
		return evaluatedArgs;
	}

	/**
	 * APPLY Evaluate in turn each subform in the procedure body, in an environment created by extending
	 * the procedure's initial environment with the argument bindings.
	 * 
	 * @param argVals
	 *            This is an array of objects (evaluated) that is passed to the
	 *            method.
	 * @return LispObject The value of the last form in the procedure body
	 */
	public LispObject apply(LispObject argForms) {
		if(argForms.isNull()) {
			return apply(new NilAtom()) ;
		} else {
			return apply((ConsCell)argForms) ;
		}
	}
	
	public LispObject apply(ConsCell argForms) {
		ChildEnvironment newEnv = new ChildEnvironment(env);
		
		int i = 0 ;
		//System.out.println("apply(LispObject[]): argVals.length: " + argVals.length) ;
/*
		for (i = 0; i < argVals.length; i++) {
			newEnv.intern(formalParams[i], argVals[i]);
		}
*/
		LispObject currentForm ;

		//iterate through the arg forms and formal params, interning them in turn
		currentForm = argForms ;
		while(!currentForm.cdr().isNull()) {
			newEnv.intern(formalParams[i++], currentForm.car().eval(env)) ;
			currentForm = currentForm.cdr();
		}		
		newEnv.intern(formalParams[i], currentForm.car().eval(env)) ;
		
		//iterate through the body forms, evaluating them in turn
		currentForm = body ;
		while(!currentForm.cdr().isNull()) {
			currentForm.car().eval(newEnv) ;
			currentForm = currentForm.cdr();
		}		
		return currentForm.car().eval(newEnv);	
	}
	
	/**
	 * If there are no arguments, there is no need to extend the environment with new bindings. 
	 * Just evaluate the forms
	 * @param n Dummy
	 * @return
	 */
	public LispObject apply(NilAtom n) { 
		LispObject currentForm ;

		//iterate through the body forms, evaluating them in turn
		currentForm = body ;
		while(!currentForm.cdr().isNull()) {
			currentForm.car().eval(env) ;
			currentForm = currentForm.cdr();
		}		
		return currentForm.car().eval(env);	
	}

}
