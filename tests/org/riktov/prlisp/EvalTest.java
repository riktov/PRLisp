package org.riktov.prlisp;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
* Test
* 
* @author riktov@freeshell.org (Paul Richter)
*/

public class EvalTest {
    private ConsCell c ;
    NilAtom nil = new NilAtom() ;
    String helloString = "Hello, I am foo." ;
    
    private Environment e ;

    @Before
    public void setUp() {
	/**
	 * We create a cons cell "by hand", without relying on the reader. 
	 */
    	e = new Environment() ;

        ConsCell ct2 = new ConsCell(DataAtom.make(23), new NilAtom()) ;
        ConsCell ct1 = new ConsCell(DataAtom.make(47), ct2) ;
        c = new ConsCell(new SymbolAtom("+"), ct1) ;
        
	// c -> (+ 47 23)
    }

    @Test public void testEvalSimpleLookup() {
    	e.intern("foo", new StringAtom(helloString)) ;
    	  	
    	LispObject sexp = new SymbolAtom("foo") ;
    	ObjectAtom value = (ObjectAtom)sexp.eval(e) ;
    	
    	
    	assertTrue(value.data() == helloString) ;
    }
    
    
    @Test public void testEvalApplyPrimitive() {
    	// The primitive + converts the result to float
    	ObjectAtom sum = (ObjectAtom)c.eval(e) ;

    	//System.out.println(sum) ;
    	
       assertTrue(sum.dataEquals(new Float(70))) ;
    }
 
    @Test public void testEvalApplyCompoundNoArgs() {
    	// a procedure which takes no arguments and returns 42.
    	// DataAtom.make(42) makes the data have the Integer run-time type. 
    	// Since we do not call the primitive +, the result is not converted to float
    	ObjectAtom body = (ObjectAtom)DataAtom.make(42) ;	//the body is a self-evaluating atom
    	String[] formalParams = new String[0] ;
    	
    	System.out.println(body.toString()) ;

    	CompoundProcedure proc = new CompoundProcedure(formalParams, body, e) ;
    	
    	ObjectAtom result = (ObjectAtom)proc.apply(nil) ;

    	//System.out.println("[" + result + "]") ;

        assertTrue(result.toString().equals("42")) ;
        assertTrue(result.dataEquals(new Integer(42))) ;
    }    

	@Test public void testEvalApplyCompoundWithArgs() {
		SymbolAtom plus = new SymbolAtom("+") ;
		ObjectAtom four = new ObjectAtom(3) ;
		SymbolAtom x = new SymbolAtom("x") ;
		LispObject[] args = {
				plus, four, x  
		} ;
		
		ConsCell body = new ConsCell(args) ;
    	String[] formalParams = new String[] { "x" } ;

    	CompoundProcedure proc = new CompoundProcedure(formalParams, body, e) ;
    	
    	ObjectAtom result = (ObjectAtom)proc.apply(new LispObject[] { new ObjectAtom(5)} ) ;

    	System.out.println("[" + result + "]") ;

        assertTrue(result.toString().equals("8.0")) ;
        assertTrue(result.dataEquals(new Float(8.0))) ;
	}
}