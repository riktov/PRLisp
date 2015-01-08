package org.riktov.prlisp;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ApplyTest {
    Environment e ;

    @Before
    public void setUp() {
    	e = new Environment() ;
    }

    @Test public void testApplyCompoundNoArgs() {
    	// a procedure which takes no arguments and returns 42.
    	// DataAtom.make(42) makes the data have the Integer run-time type. 
    	// Since we do not call the primitive +, the result is not converted to float
    	ConsCell body = new ConsCell(new LispObject[] { DataAtom.make(42) }) ;
    	System.out.println("testEvalApplyCompoundNoArgs(): body is " + body.toString()) ;

    	CompoundProcedure proc = new CompoundProcedure(NilAtom.nil, body, e) ;
    	
    	ObjectAtom result = (ObjectAtom)proc.apply(NilAtom.nil) ;

        assertTrue(result.toString().equals("42")) ;
        assertTrue(result.data.equals(new Integer(42))) ;
    }
    
	/**
	 * We manually build the equivalent of (lambda (x) x)
	 * then apply it to (5)
	 * @param evalTest TODO
	 */
	@Test public void testApplyCompoundWithArgs() {
//		String[] formalParams = new String[] { "X" } ;
		LispList formalParams = new ConsCell(new SymbolAtom("x"), new NilAtom()) ;
		LispList body = new ConsCell(new SymbolAtom("x"), new NilAtom()) ;
	
		CompoundProcedure proc = new CompoundProcedure(formalParams, body, e) ;
		System.out.println("testEvalApplyCompoundWithArgs(): proc: " + proc) ;
		
		LispList argForms = new ConsCell(new ObjectAtom(5), new NilAtom()) ;
		LispList argValues = proc.processArguments(argForms, e) ;
		
		System.out.println("testEvalApplyCompoundWithArgs(): argValues: " + argValues) ;
		
		LispObject result = proc.apply(argValues) ;
	
		System.out.println("testEvalApplyCompoundWithArgs(): result :  " + result) ;
		assertTrue(result.toString().equals("5")) ;
		//assertTrue(((ObjectAtom)result).data.equals(new Float(5.0))) ;
	}
}
