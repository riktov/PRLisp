package org.riktov.prlisp;

//import java.util.List ;
//import java.util.ArrayList ;

/**
 * A LispProcedure can be a compound procedure (lambda), a primitive procedure, or a special operation
 * @author Paul RICHTER &lt;riktov@freeshell.de&gt;
 *
 */
abstract class LispProcedure extends LispObject {
	public abstract LispObject apply(LispList argsToApply) ;
    public LispList ProcessArguments(LispList unevaluatedArgForms,
			Environment evalEnv) {
		return unevaluatedArgForms.evalList(evalEnv) ;
	}

}

class CompoundProcedure extends LispProcedure {
	private String formalParams[];
	private LispList body;
	private Environment env;

	// constructors
	public CompoundProcedure(String[] formalParams, LispList body, Environment env) {
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
	 * accessors
	 */
	String[] formalParams() { return formalParams ; }
	LispList body() { return body ; }
	
	/**
	 * @param unevaluatedArgs
	 *            an Array of LispObjects, which may be symbols or forms which
	 *            have not been evaluated
	 * @return array of LispObject, which are the evaluated results of all the
	 *         unevaluatedArgs
	 */

	
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
		ChildEnvironment newEnv = new ChildEnvironment(env);
		
		if(!argForms.isNull()) {
			int i = 0 ;

			ConsCell currentCell = (ConsCell) argForms ;

			//iterate through the arg forms and formal params, interning them in turn
			while(!currentCell.cdr().isNull()) {
				String symbolName = formalParams[i++] ;
				System.out.println("apply(ConsCell): interning " + symbolName) ;
				newEnv.intern(symbolName, currentCell.car().eval(env)) ;
				currentCell = (ConsCell) currentCell.cdr();
			}					
		}
		return body.evalSequence(newEnv) ;
	}	
}
