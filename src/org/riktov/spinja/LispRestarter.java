package org.riktov.spinja;

/**
 * 
 * @author paul
 *
 */
class LispRestarter {
	int offerRestarts(String errorMessage) {
		System.out.println("; ERROR: " + errorMessage) ;
		return 2 ;
	}
}

class LispAbortEvaluationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}