package org.riktov.prlisp;

import java.util.HashMap;

public class BuiltIn {
	static HashMap<String, SpecialOperation> initialBuiltIns(final Environment env) {
		HashMap<String, SpecialOperation> builtIns = new HashMap<String, SpecialOperation>() ;
		
		LispReader lr = new LispReader();
			
		String[] builtInDefs = new String[] {
			"(define (list . x) x)",
			"(define (not x) (if (null? x) t nil))",
			"(define (first x) (car x))",
			"(define (cadr x) (car (cdr x)))"
		} ;
		
		int i ;
		for(i = 0 ; i < builtInDefs.length ; i++) {
			lr.read(builtInDefs[i]).eval(env) ;
		}
		
		return builtIns;
	}
}
