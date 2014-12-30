package org.riktov.prlisp;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
*
* @author riktov@freeshell.org (Paul Richter)
*/

public class EvalTest {
    private ConsCell c ;
    NilAtom nil = new NilAtom() ;
    String helloString = "Hello, I am foo." ;
    
    Environment e = new Environment() ;

    @Before
    public void setUp() {
	/**
	 * We create a cons cell "by hand", without relying on the reader. 
	 */
        ConsCell ct2 = new ConsCell(DataAtom.make(23), new NilAtom()) ;
        ConsCell ct1 = new ConsCell(DataAtom.make(47), ct2) ;
        c = new ConsCell(new SymbolAtom("+"), ct1) ;
        
	// c -> (+ 47 23)
    }

    @Test public void testEvalLookup() {
    	e.intern("foo", new StringAtom(helloString)) ;
    	  	
    	LispObject sexp = new SymbolAtom("foo") ;
    	ObjectDataAtom value = (ObjectDataAtom)sexp.eval(e) ;
    	
    	
    	assertTrue(value.data() == helloString) ;
    }
    
    
    @Test public void testEvalApply() {
    	ObjectDataAtom sum = (ObjectDataAtom)c.eval(e) ;

    	System.out.println(sum) ;
    	
       assertTrue(sum.dataEquals(new Float(70))) ;
    }
 
}
