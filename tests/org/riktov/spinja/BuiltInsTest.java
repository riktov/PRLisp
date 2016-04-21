package org.riktov.spinja;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BuiltInsTest {
	private Environment env ;
	private LispObject result ;
	
	@Before
	public void setUp() throws Exception {
		env = new Environment() ;
		env.initialize() ;
	}

	@Test
	public void AppendTestEmpty() {
		LispProcedure proc = (LispProcedure)new SymbolAtom("append").eval(env) ;
		
		LispList args = NilAtom.nil ;
		result = proc.apply(args) ;
		
		assertTrue(result.isNull()) ;
	}

	@Test
	public void AppendTestSingleArg() {
		LispProcedure proc = (LispProcedure)new SymbolAtom("append").eval(env) ;
		
		LispObject arg1Objects[] = { ObjectAtom.make(1), ObjectAtom.make(2) } ;
		
		LispList arg1 = new ConsCell(arg1Objects) ;
		LispList args = new ConsCell((LispObject) arg1, NilAtom.nil) ;
		result = proc.apply(args) ;
		
		String expected = "(1 2)" ;
		System.out.println("Expecting: " + expected + " Got: " + result) ;
		assertTrue(result.toString().equals(expected)) ;
	}


	@Test
	public void AppendTestTwoArgs() {
		LispProcedure proc = (LispProcedure)new SymbolAtom("append").eval(env) ;
		
		LispObject arg1Objects[] = { ObjectAtom.make(1) } ;
		LispObject arg2Objects[] = { ObjectAtom.make(2) } ;
		/*
		LispObject arg1Objects[] = { ObjectAtom.make(1), ObjectAtom.make(2) } ;
		LispObject arg2Objects[] = { ObjectAtom.make(3), ObjectAtom.make(4) } ;
		 */
		
		LispList arg1 = new ConsCell(arg1Objects) ;
		LispList arg2 = new ConsCell(arg2Objects) ;

		LispObject []argsObjects = {(LispObject) arg1, (LispObject) arg2} ;
		
		LispList args = new ConsCell(argsObjects) ;
		result = proc.apply(args) ;
		
		String expected = "(1 2)" ;
		System.out.println("Expecting: " + expected + " Got: " + result) ;
		assertTrue(result.toString().equals(expected)) ;
	}
}
