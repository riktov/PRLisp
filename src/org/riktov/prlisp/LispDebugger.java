package org.riktov.prlisp;

class LispDebugger {
	LispDebugger(String errorMessage, Environment env) {
		offerRestarts(errorMessage) ;
	}
	
	void offerRestarts(String errorMessage) {
		System.out.println(errorMessage) ;
	}
}
