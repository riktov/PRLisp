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
    
    public Environment e ;

    @Before
    public void setUp() {
	/**
	 * We create a cons cell "by hand", without relying on the reader. 
	 */
    	e = new Environment() ;

        ConsCell ct2 = new ConsCell(Atom.make(23), new NilAtom()) ;
        ConsCell ct1 = new ConsCell(Atom.make(47), ct2) ;
        c = new ConsCell(new SymbolAtom("+"), ct1) ;
        
	// c -> (+ 47 23)
    }

    @Test public void testEvalSymbol() {
    	e.intern("foo", new StringAtom(helloString)) ;
    	  	
    	LispObject sexp = new SymbolAtom("foo") ;
    	ObjectAtom value = (ObjectAtom)sexp.eval(e) ;
    	
    	assertTrue(value.data() == helloString) ;
    }
    
    
    @Test public void testEvalApplyPrimitive() {
    	// The primitive + converts the result to float
    	ObjectAtom sum = (ObjectAtom)c.eval(e) ;

    	//System.out.println(sum) ;
    	
       assertTrue(sum.data.equals(new Float(70))) ;
    }
<<<<<<< Updated upstream
 
    @Test public void testEvalApplyCompoundNoArgs() {
    	// a procedure which takes no arguments and returns 42.
    	// DataAtom.make(42) makes the data have the Integer run-time type. 
    	// Since we do not call the primitive +, the result is not converted to float
    	ConsCell body = new ConsCell(new LispObject[] { DataAtom.make(42) }) ;
    	String[] formalParams = new String[0] ;
    	
    	System.out.println("testEvalApplyCompoundNoArgs(): body is " + body.toString()) ;

    	CompoundProcedure proc = new CompoundProcedure(formalParams, body, e) ;
    	
    	ObjectAtom result = (ObjectAtom)proc.apply(nil) ;

    	//System.out.println("[" + result + "]") ;

        //assertTrue(result == body) ;
        assertTrue(result.toString().equals("42")) ;
        assertTrue(result.data.equals(new Integer(42))) ;
    }    

    /**
     * We manually build the equivalent of (lambda (x) (+ x 4))
     * then apply it to (5)
     */
	@Test public void testEvalApplyCompoundWithArgs() {
		SymbolAtom plus = new SymbolAtom("+") ;
		ObjectAtom four = new ObjectAtom(4) ;
		SymbolAtom x = new SymbolAtom("x") ;
		LispObject[] bodyForms = { plus, four, x } ;
		
		LispList body = new ConsCell(new ConsCell(bodyForms), new NilAtom()) ;
    	String[] formalParams = new String[] { "X" } ;	//TODO: lowercase or uppercase?

    	CompoundProcedure proc = new CompoundProcedure(formalParams, body, e) ;
    	System.out.println("testEvalApplyCompoundWithArgs(): " + proc) ;
    	
    	LispList argForms = (LispList) new ConsCell(new ObjectAtom(5), new NilAtom()) ;
    	LispList argValues = proc.ProcessArguments(argForms, e) ;
    	
    	ObjectAtom result = (ObjectAtom)proc.apply(argValues) ;

    	System.out.println("testEvalApplyCompoundWithArgs(): " + result) ;
       assertTrue(result.toString().equals("9.0")) ;
       assertTrue(result.data.equals(new Float(9.0))) ;
	}
=======
>>>>>>> Stashed changes
}