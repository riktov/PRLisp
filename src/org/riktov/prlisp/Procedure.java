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
<<<<<<< Updated upstream
    public LispList ProcessArguments(LispList unevaluatedArgForms,
=======
    public LispList processArguments(LispList unevaluatedArgForms,
>>>>>>> Stashed changes
			Environment evalEnv) {
    	System.out.println("processArguments() : unevaluated: " + unevaluatedArgForms) ;
		return unevaluatedArgForms.evalList(evalEnv) ;
	}

}

class CompoundProcedure extends LispProcedure {
	private String formalParams[];
	private LispList body;
	private Environment env;

	// constructors
<<<<<<< Updated upstream
=======
	/**
	 * Constructor
	 * @param formalParams
	 * @param body
	 * @param env
	 */
>>>>>>> Stashed changes
	public CompoundProcedure(String[] formalParams, LispList body, Environment env) {
		this.formalParams = formalParams;
		this.body = body;
		this.env = env;
	}

	// implementation of LispObject
	/**
	 * Copying CLISP
	 */
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
<<<<<<< Updated upstream
	LispList body() { return body ; }
	
	/**
	 * @param unevaluatedArgs
	 *            an Array of LispObjects, which may be symbols or forms which
	 *            have not been evaluated
	 * @return array of LispObject, which are the evaluated results of all the
	 *         unevaluatedArgs
	 */
=======
	LispList body() { return body ; }	
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
=======
		System.out.println("apply() : argForms: " + argForms) ;
>>>>>>> Stashed changes
		ChildEnvironment newEnv = new ChildEnvironment(env);
		
		if(!argForms.isNull()) {
			int i = 0 ;

<<<<<<< Updated upstream
			ConsCell currentCell = (ConsCell) argForms ;

			//iterate through the arg forms and formal params, interning them in turn
			while(!currentCell.cdr().isNull()) {
				String symbolName = formalParams[i++] ;
				System.out.println("apply(ConsCell): interning " + symbolName) ;
				newEnv.intern(symbolName, currentCell.car().eval(env)) ;
				currentCell = (ConsCell) currentCell.cdr();
			}					
=======
			LispObject currentCell = (LispObject)argForms ;
			String symbolName ;
			LispObject val ;
			
			System.out.println("apply(): currentCell : " + currentCell.toString()) ;
			
			//TODO: Refactor this with LET for pairing formalParams and values
			//iterate through the arg forms and formal params, interning them in turn
			while(!currentCell.isNull()) {
				symbolName = formalParams[i++] ;
				val = currentCell.car().eval(env) ;
				System.out.println("apply(ConsCell): interning " + symbolName + " with value: " + val ) ;
				newEnv.intern(symbolName, val) ;
				currentCell = currentCell.cdr();
			}
/*			symbolName = formalParams[i++] ;
			val = currentCell.car().eval(env) ;
			System.out.println("apply(ConsCell): interning " + symbolName + " with value: " + val ) ;
			newEnv.intern(symbolName, val) ;			
*/
>>>>>>> Stashed changes
		}
		return body.evalSequence(newEnv) ;
	}	
}
