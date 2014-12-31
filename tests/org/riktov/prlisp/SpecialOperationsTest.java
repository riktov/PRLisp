package org.riktov.prlisp;

import static org.junit.Assert.assertTrue;

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

	@Test
	public void testSetq() {
		LispObject varName = new SymbolAtom("foobar") ;
		LispObject assignedValue = DataAtom.make(32) ;
		
		LispObject[] elements = new LispObject[] {
				new SymbolAtom("setq"),
				varName,
				assignedValue
		} ;
		//System.out.println(elements.length) ;
		
		System.out.println("Just after installing specials: " + e.keySet()) ;
		
		ConsCell c = new ConsCell(elements) ;
		
		System.out.println("testSetq() evaluating: " + c.toString()) ;
		c.eval(e) ;
		System.out.println("Just after calling SETQ: " + e.keySet()) ;
		
		assertTrue(e.containsKey("FOOBAR")) ;
		assertTrue(e.get("FOOBAR") == assignedValue) ;
	}

	/**
	 * (if 5 14 2)
	 */
	@Test
	public void testIfTrue() {
		LispObject conditionObject = DataAtom.make(5) ;
		LispObject consequentObject = DataAtom.make(14) ;
		LispObject alternateObject = DataAtom.make(2) ;
		
		LispObject[] elements = new LispObject[] {
				new SymbolAtom("if"),
				conditionObject,
				consequentObject,
				alternateObject
		} ;
		
		ConsCell c = new ConsCell(elements) ;
		System.out.println("testIfTrue() evaluating: " + c.toString()) ;

		LispObject result = c.eval(e) ;
		
		assertTrue(result == consequentObject) ;
	}

	@Test
	public void testIfFalse() {
		LispObject conditionObject = new NilAtom() ;
		LispObject consequentObject = DataAtom.make(14) ;
		LispObject alternateObject = DataAtom.make(2) ;
		
		LispObject[] elements = new LispObject[] {
				new SymbolAtom("if"),
				conditionObject,
				consequentObject,
				alternateObject
		} ;
		
		ConsCell c = new ConsCell(elements) ;
		LispObject result = c.eval(e) ;
		
		assertTrue(result == alternateObject) ;
	}
	
	@Test
	public void testProgn() {
		LispObject exp1 = new NilAtom() ;
		LispObject exp2 = new StringAtom("Bob Seger") ;
		LispObject exp3 = DataAtom.make(221) ;
		
		LispObject[] elements = new LispObject[] {
				new SymbolAtom("progn"),
				exp1,
				exp2,
				exp3
		} ;
				
		ConsCell c = new ConsCell(elements) ;
		LispObject result = c.eval(e) ;
		
		assertTrue(result == exp3) ;
	}
	
	@Test
	public void testQuote() {
		LispObject exp1 = new SymbolAtom("foo") ;
		LispObject exp2 = new SymbolAtom("bar") ;
		LispObject exp3 = new SymbolAtom("baz") ;
		
		LispObject[] elements = new LispObject[] {
				new SymbolAtom("quote"),
				exp1,
				exp2,
				exp3
		} ;
				
		ConsCell c = new ConsCell(elements) ;

		System.out.println("testQuote() evaluating: " + c.toString()) ;

		LispObject result = c.eval(e) ;
		
		System.out.println("testQuote() result: " + result) ;
		assertTrue(result.car() == exp1) ;
	}
	
	@Test
	public void testLambda() {
		LispObject arglist = new ConsCell( new LispObject[] { new SymbolAtom("x") });
		LispObject body = new ConsCell(new LispObject[] { 
				new SymbolAtom("+"),
				DataAtom.make(3),
				new SymbolAtom("x")
				}) ;
		
		LispObject[] elements = new LispObject[] {
				new SymbolAtom("lambda"),
				arglist,
				body
		} ;
				
		ConsCell c = new ConsCell(elements) ;
		System.out.println("testLambda() evaluating: " + c.toString()) ;

		CompoundProcedure theLambda = (CompoundProcedure)c.eval(e) ;
		
		System.out.println(theLambda) ;
		
		LispObject result = theLambda.apply(new LispObject[] { DataAtom.make(9)} ) ;
		
		assertTrue(result.toString().equals("12.0")) ;
	}
	
	/**
	 * (let ((x 5)) x)
	 */
	@Test
	public void testLet() {
		LispObject binding = new ConsCell( new LispObject[] { new SymbolAtom("x"), DataAtom.make(5) });
		LispObject bindingList = new ConsCell(binding, nil) ;
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
