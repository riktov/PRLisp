package org.riktov.prlisp;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ApplyTest {
    Environment env ;

    @Before
    public void setUp() {
    	/* No constants, primitives, or built-ins are necessary for these tests
    	 * so the environment does not need to be initialized
    	 * 
    	 */
    	env = new Environment() ;
    }

    @Test public void testApplyCompoundNoArgs() {
    	// a procedure which takes no arguments and returns 42.
    	// DataAtom.make(42) makes the data have the Integer run-time type. 
    	// Since we do not call the primitive +, the result is not converted to float
    	LispList body = new ConsCell(new LispObject[] { DataAtom.make(42) }) ;
    	System.out.println("testEvalApplyCompoundNoArgs(): body is " + body.toString()) ;

    	CompoundProcedure proc = new CompoundProcedure(NilAtom.nil, body, env) ;
    	
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
		LispObject formalParams = new ConsCell(new SymbolAtom("x"), new NilAtom()) ;
		LispList body = new ConsCell(new SymbolAtom("x"), new NilAtom()) ;
	
		CompoundProcedure proc = new CompoundProcedure(formalParams, body, env) ;
		System.out.println("testEvalApplyCompoundWithArgs(): proc: " + proc) ;
		
		LispList argForms = new ConsCell(new ObjectAtom(5), new NilAtom()) ;
		LispList argValues = proc.processArguments(argForms, env) ;
		
		System.out.println("testEvalApplyCompoundWithArgs(): argValues: " + argValues) ;
		
		LispObject result = proc.apply(argValues) ;
	
		System.out.println("testEvalApplyCompoundWithArgs(): result :  " + result) ;
		assertTrue(result.toString().equals("5")) ;
		//assertTrue(((ObjectAtom)result).data.equals(new Float(5.0))) ;
	}
	
	@Test public void testApplyCompoundWithListBoundArg() {
		/* We make formalParams an atom instead of a list so that it will be bound
		to the entire list argParams */
		LispObject formalParams = new SymbolAtom("rest") ;
		LispList body = new ConsCell(new SymbolAtom("rest"), new NilAtom()) ;
		
		CompoundProcedure proc = new CompoundProcedure(formalParams, body, env) ;
		System.out.println("testApplyCompoundWithListBoundArg(): proc: " + proc) ;
		
		ConsCell argForms = new ConsCell(new ObjectAtom(5), new NilAtom()) ;
		argForms = new ConsCell(new ObjectAtom(13), argForms) ;
		LispList argValues = proc.processArguments(argForms, env) ;
		
		System.out.println("testApplyCompoundWithListBoundArg(): argValues: " + argValues) ;
		
		LispObject result = proc.apply(argValues) ;
	
		System.out.println("testApplyCompoundWithListBoundArg(): result :  " + result) ;
		assertTrue(result.toString().equals("(13 5)")) ;
	}
	
	@Test public void testApplyCompoundWithListArg() {
		/* It should be possible to apply a procedure where one of the arguments is a list,
		 * without the list being evaluated as a procedure application at some intermediate poing
		 * */
		LispObject formalParams = new ConsCell(new SymbolAtom("alist"), new NilAtom()) ;
		LispList body = new ConsCell(new SymbolAtom("alist"), new NilAtom()) ;
		
		CompoundProcedure proc = new CompoundProcedure(formalParams, body, env) ;
		System.out.println("testApplyCompoundWithListArg(): proc: " + proc) ;
		
		ConsCell argForms = new ConsCell(new ObjectAtom(5), NilAtom.nil) ;
		argForms = new ConsCell(new ObjectAtom(13), argForms) ;
		argForms = new ConsCell(argForms, NilAtom.nil) ;
		//argForms is a list containing one item, the list (13 5)
		
		//LispList argValues = proc.processArguments(argForms, env) ;
		
		System.out.println("testApplyCompoundWithListArg(): argForms: " + argForms) ;
		
		LispObject result = proc.apply(argForms) ;
	
		System.out.println("testApplyCompoundWithListBoundArg(): result :  " + result) ;
		assertTrue(result.toString().equals("(13 5)")) ;		
	}
}
