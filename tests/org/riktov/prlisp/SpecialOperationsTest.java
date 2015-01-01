package org.riktov.prlisp;

import static org.junit.Assert.*;

import org.junit.Before;
//import org.junit.Before;
import org.junit.Test;

public class SpecialOperationsTest {
	private Environment e;
	private NilAtom nil = new NilAtom() ;
	@Before
	public void setUp() {
		e = new Environment();
		SpecialOperation.initialSpecials(e) ;
	}

	/**
	 * SETQ modifies the environment, so we check whether the environment has received the new key-value pair
	 */
	@Test
	public void testSetq() {
		LispObject varName = new SymbolAtom("foobar") ;
		LispObject assignedValue = DataAtom.make(32) ;
		
		LispObject[] forms = new LispObject[] {
				new SymbolAtom("setq"),
				varName,
				assignedValue
		} ;
		//System.out.println(forms.length) ;
		
		System.out.println("Just after installing specials: " + e.keySet()) ;
		
		ConsCell c = new ConsCell(forms) ;
		
		System.out.println("testSetq() evaluating: " + c.toString()) ;
		c.eval(e) ;
		System.out.println("Just after calling SETQ: " + e.keySet()) ;
		
		assertTrue(e.containsKey("FOOBAR")) ;
		assertTrue(e.get("FOOBAR") == assignedValue) ;
	}

	/**
	 * IF evaluates one of two forms conditionally, so we check the returned value with various conditions
	 * (if 5 14 2)
	 */
	@Test
	public void testIfTrue() {
		LispObject conditionObject = DataAtom.make(5) ;
		LispObject consequentObject = DataAtom.make(14) ;
		LispObject alternateObject = DataAtom.make(2) ;
		
		LispObject[] forms = new LispObject[] {
				new SymbolAtom("if"),
				conditionObject,
				consequentObject,
				alternateObject
		} ;
		
		ConsCell c = new ConsCell(forms) ;
		System.out.println("testIfTrue() evaluating: " + c.toString()) ;

		LispObject result = c.eval(e) ;
		
		assertTrue(result == consequentObject) ;
	}

	@Test
	public void testIfFalse() {
		LispObject conditionObject = new NilAtom() ;
		LispObject consequentObject = DataAtom.make(14) ;
		LispObject alternateObject = DataAtom.make(2) ;
		
		LispObject[] forms = new LispObject[] {
				new SymbolAtom("if"),
				conditionObject,
				consequentObject,
				alternateObject
		} ;
		
		ConsCell c = new ConsCell(forms) ;
		LispObject result = c.eval(e) ;
		
		assertTrue(result == alternateObject) ;
	}
	
	/**
	 * PROGN evaluates a sequence of forms, so we check that it returns the value of the last form
	 */
	@Test
	public void testPrognResult() {
		LispObject exp1 = new NilAtom() ;
		LispObject exp2 = new StringAtom("Bob Seger") ;
		LispObject exp3 = DataAtom.make(221) ;
		
		LispObject[] forms = new LispObject[] {
				new SymbolAtom("progn"),
				exp1,
				exp2,
				exp3
		} ;
				
		ConsCell c = new ConsCell(forms) ;
		LispObject result = c.eval(e) ;
		
		assertTrue(result == exp3) ;
	}
	
	/**
	 * PROGN evaluates a sequence of forms, so we check that the intermediate forms are evaluated, 
	 * even though we do not get their results
	 */
	@Test
	public void testPrognIntermediate() {
		LispObject exp1 = new NilAtom() ;
		LispObject exp2 = new ConsCell(new LispObject[] { new SymbolAtom("setq"), new SymbolAtom("singer"), new StringAtom("Bob Seger") }) ;
		LispObject exp3 = DataAtom.make(221) ;
		
		LispObject[] forms = new LispObject[] {
				new SymbolAtom("progn"),
				exp1,
				exp2,
				exp3
		} ;
				
		ConsCell c = new ConsCell(forms) ;
		
		assertFalse(e.containsKey("SINGER")) ;
		
		ObjectAtom result = (ObjectAtom)c.eval(e) ;
		
		assertTrue(result.data.equals(221)) ;	//we got the value of the last form
		assertTrue(e.containsKey("SINGER")) ; //did the intermediate form get evaluated?
		String assignedVal = e.get("SINGER").toString() ;
		System.out.println(assignedVal) ;
		assertTrue(assignedVal.equals("Bob Seger")) ;
		//assertTrue(result == exp3) ;
	}
	
	/**
	 * QUOTE returns all the forms unevaluated, so we check that forms are not evaluated
	 */
	@Test
	public void testQuote() {
		LispObject exp1 = new SymbolAtom("foo") ;
		LispObject exp2 = new ConsCell(new LispObject[] { new SymbolAtom("+"), Atom.make(5), Atom.make(7) }) ;
		LispObject exp3 = new SymbolAtom("baz") ;
		
		LispObject[] forms = new LispObject[] {
				new SymbolAtom("quote"),
				exp1,
				exp2,
				exp3
		} ;
				
		ConsCell c = new ConsCell(forms) ;

		System.out.println("testQuote() evaluating: " + c.toString()) ;

		LispObject result = c.eval(e) ;
		
		System.out.println("testQuote() result: " + result) ;
		assertTrue(result.car() == exp1) ;
		assertTrue(result.cdr().car() == exp2) ;
		
		LispObject secondForm = result.cdr().car() ;
		
		assertFalse(secondForm.toString().equals("12")) ;
	}
	
	/**
	 * LAMBDA returns a CompoundProcedure.
	 * We should not be testing APPLY here, those will go in a separate set of tests, LambdaTest.
	 * We only test that the resulting CompoundProcedure is properly created.
	 * Invoke the lambda special operation with a single parameter and a single form in its body.
	 * Assert that when evaluated, it returns a CompoundProcedure with the parameter list and the body.
	 */
	@Test
	public void testLambda() {
		LispObject arglist = new ConsCell( new LispObject[] { new SymbolAtom("x") });
		LispObject form1 = new ConsCell(new LispObject[] { 
				new SymbolAtom("+"),
				DataAtom.make(3),
				new SymbolAtom("x")
				}) ;
		
		LispObject[] forms = new LispObject[] {
				new SymbolAtom("lambda"),
				arglist,
				form1
		} ;
				
		ConsCell c = new ConsCell(forms) ;
		System.out.println("testLambda() evaluating: " + c.toString()) ;

		CompoundProcedure theLambda = (CompoundProcedure)c.eval(e) ;
		System.out.println(theLambda) ;
		
		//LispObject result = theLambda.apply(new LispObject[] { DataAtom.make(9)} ) ;
		
		assertTrue(theLambda.body() == c.cdr().cdr().car()) ;
	}
	
	@Test
	public void testLambdaListBody() {
		LispObject arglist = new ConsCell( new LispObject[] { new SymbolAtom("x") });
		LispObject form1 = new ConsCell(new LispObject[] { 
				new SymbolAtom("*"),
				DataAtom.make(5),
				new SymbolAtom("x")
				}) ;
		LispObject form2 = new ConsCell(new LispObject[] { 
				new SymbolAtom("+"),
				DataAtom.make(3),
				new SymbolAtom("x")
				}) ;
		
		//LispObject body = new ConsCell(new LispObject[] {form1, form2}) ;
		
		LispObject[] forms = new LispObject[] {
				new SymbolAtom("lambda"),
				arglist,
				form1, form2
		} ;
				
		ConsCell c = new ConsCell(forms) ;
		System.out.println("testLambdaListBody() evaluating: " + c.toString()) ;

		CompoundProcedure theLambda = (CompoundProcedure)c.eval(e) ;
		
		System.out.println(theLambda) ;
		
		LispObject result = theLambda.apply(new LispObject[] { DataAtom.make(9)} ) ;
		
		assertTrue(result.toString().equals("12.0")) ;
	}

	@Test public void testLambdaNoArgs() {
		LispObject arglist = new NilAtom();
		LispObject body = DataAtom.make(3) ;
		
		LispObject[] exp = new LispObject[] {
				new SymbolAtom("lambda"),
				arglist,
				body
		} ;
	
		ConsCell c = new ConsCell(exp) ;
		System.out.println("testLambdaNoArgs() evaluating: " + c.toString()) ;

		CompoundProcedure theLambda = (CompoundProcedure)c.eval(e) ;
		
		System.out.println(theLambda) ;
		
		ObjectAtom result = (ObjectAtom)theLambda.apply(new LispObject[0]) ;
		
		assertTrue(result.data.equals(3)) ;
	}
	
	/**
	 * LET adds bindings under which the form is evaluated.
	 * Check whether the bindings are in effect when evaluating the form.
	 * (let ((x 5)) x)
	 */
	@Test
	public void testLet() {
		LispObject binding1 = new ConsCell( new LispObject[] { new SymbolAtom("x"), DataAtom.make(5) });
		LispObject bindingList = new ConsCell(binding1, nil) ;
		LispObject body = new SymbolAtom("x") ;
		
		LispObject[] formAsArray = new LispObject[] {
				new SymbolAtom("let"),
				bindingList,
				body
		} ;

		ConsCell c = new ConsCell(formAsArray) ;
		System.out.println("testLet() evaluating: " + c.toString()) ;

		LispObject result = c.eval(e) ;
				
		assertTrue(result.toString().equals("5")) ;
	}
}
