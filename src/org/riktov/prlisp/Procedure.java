package org.riktov.prlisp;

//import java.util.List ;
//import java.util.ArrayList ;

abstract class LispProcedure extends LispObject {
	public abstract LispObject apply(LispObject[] argVals);
	public abstract LispObject[] ProcessArguments(LispObject[] unevaluatedArgs,
			Environment evalEnv);
}

class CompoundProcedure extends LispProcedure {
	private String formalParams[];
	private LispObject body;
	private Environment env;

	// constructors
	public CompoundProcedure(String[] formalParams, LispObject body, Environment env) {
		this.formalParams = formalParams;
		this.body = body;
		this.env = env;
	}

	// implementation of LispObject
	public String toString() {
		String paramList = new String() ;
		String sep = "" ;
		int i ;
		for(i = 0 ; i < formalParams.length ; i++) {
			if(i > 0) { sep = " " ; }
			paramList = paramList + formalParams[i] + sep ;
		}
		return "#<FUNCTION :LAMBDA> (" + paramList + ") " + body.toString() + ">"; 
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
	public LispObject[] ProcessArguments(LispObject[] unevaluatedArgs,
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
	 * APPLY Evaluate the procedure body in an environment created by extending
	 * the procedure's initial environment with the argument bindings.
	 * 
	 * @param argVals
	 *            This is an array of objects (evaluated) that is passed to the
	 *            method.
	 * @return LispObject The result of the application
	 */

	public LispObject apply(LispObject[] argVals) {
		ChildEnvironment newEnv = new ChildEnvironment(env);
		int i;
		
		System.out.println("apply(LispObject[]): argVals.length: " + argVals.length) ;
		
		for (i = 0; i < argVals.length; i++) {
			newEnv.intern(formalParams[i], argVals[i]);
		}
		return body.eval(newEnv);	//TODO: body is a list of forms which need to be PROGNd.
	}

	public LispObject apply() {	return apply(new LispObject[0]) ; }
	public LispObject apply(NilAtom n) { return apply(new LispObject[0]) ; }
}
