package org.riktov.spinja;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.riktov.spinja.LispReader;

public class ReaderMacroTest {
	private LispReader lr ;
	private Environment env ;
	private LispObject result ;
	
	@Before
	public void setUp() throws Exception {
    	env = new Environment() ;
    	env.initialize();
    	
    	lr = new LispReader("") ;
    }

	@Test
	public void testSimpleQuote() {
		result = lr.read("'foo").eval(env) ;
		System.out.println(result.toString()) ;

		assertTrue(result.toString().equals("FOO")) ;
	}

	@Test
	public void testQuoteString() {
		result = lr.read("'\"Farmer\"").eval(env) ;
		System.out.println(result.toString()) ;
		
		assertTrue(result.toString().equals("\"Farmer\"")) ;
	}


	@Test
	public void testQuoteList() {		
		result = lr.read("'(\"Farmer\")").eval(env) ;
		System.out.println(result.toString()) ;
			
		assertTrue(result.toString().equals("(\"Farmer\")")) ;
	}

	@Test
	public void testQuoteListMultiple() {		
		result = lr.read("'(foo \"Farmer\")").eval(env) ;
		System.out.println(result.toString()) ;

		assertTrue(result.toString().equals("(FOO \"Farmer\")")) ;
	}
}
