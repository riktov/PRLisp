package org.riktov.spinja;

/**
 * 
 * @author riktov@freeshell.org (Paul Richter)
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

class LispQuitException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID =21L;
}