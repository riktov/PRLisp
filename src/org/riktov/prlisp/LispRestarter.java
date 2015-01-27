package org.riktov.prlisp;

/**
 * 
 * @author paul
 *
 */
class LispRestarter {
	/**
	 * constructor 
	 * @param errorMessage
	 * @param env
	 */
	LispRestarter(String errorMessage, Environment env) {
	 
		offerRestarts(errorMessage) ;
	}
	
	int offerRestarts(String errorMessage) {
		System.out.println(errorMessage) ;
		return 2 ;
	}
}

class LispAbortEvaluationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}