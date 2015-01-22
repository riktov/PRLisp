package org.riktov.prlisp;

import java.util.HashMap;

public class BuiltIn {
	static HashMap<String, SpecialOperation> initialBuiltIns(final Environment env) {
		HashMap<String, SpecialOperation> builtIns = new HashMap<String, SpecialOperation>() ;
		
		LispReader lr = new LispReader();
			
		String[] builtInDefs = new String[] {
			"(define (list . x) x)",
			"(define (not x) (if (null? x) t nil))",
			"(define (first alis) (car alis))",
			"(define (cadr alis) (car (cdr alis)))",
			"(define (append lis1 lis2) (if (null? lis1) lis2 (cons (car lis1) (append (cdr lis1) lis2))))",
			"(define (reverse alis) (if (null? alis) alis (append (reverse (cdr alis)) (list (car alis)))))",
			"(define (fib n)(if (< n 2)(list 1 1)(let ((fib-prev (fib (- n 1))))(cons (+ (car fib-prev)(cadr fib-prev)) fib-prev))))"
		} ;
		
		int i ;
		for(i = 0 ; i < builtInDefs.length ; i++) {
			lr.read(builtInDefs[i]).eval(env) ;
		}
		
		return builtIns;
	}
}
