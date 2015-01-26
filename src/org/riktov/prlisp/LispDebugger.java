package org.riktov.prlisp;

class LispDebugger {
	LispDebugger(String errorMessage, Environment env) {
		offerRestarts(errorMessage) ;
	}
	
	int offerRestarts(String errorMessage) {
		System.out.println(errorMessage) ;
		return 4 ;
	}
}
