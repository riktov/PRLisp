package org.riktov.prlisp;

import java.util.HashMap;

public class BuiltIn {
	static HashMap<String, SpecialOperation> initialBuiltIns(final Environment env) {
		HashMap<String, SpecialOperation> builtIns = new HashMap<String, SpecialOperation>() ;
		
		LispReader lr = new LispReader();
			
		String[] builtInDefs = new String[] {
			"(define (list . x) x)"
		} ;

		(lr.read("(define (list . rest) rest)")).eval(env) ;
		(lr.read("(define (not x) (if (null? x) t nil))")).eval(env) ;
		(lr.read("(define (first x) (car x))")).eval(env) ;
//		(lr.read("(define (cadr x) (car (cdr x)))")).eval(env) ;
		
		return builtIns;
	}
}
