package org.riktov.spinja;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.riktov.spinja.LispReader;

public class ReaderMacroTest {
	LispReader lr ;
	Environment env ;
	
	@Before
	public void setUp() throws Exception {
    	env = new Environment() ;
    	env.initialize();
    }

	@Test
	public void testSimpleQuote() {
		lr = new LispReader("'foo") ;

		LispObject o ;
		try {
			o = lr.read().eval(env) ;
			System.out.println(o.toString()) ;
			assertTrue(o.toString().equals("FOO")) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//fail("Not yet implemented");
	}

	@Test
	public void testQuoteString() {
		lr = new LispReader("'\"Farmer\"") ;
		LispObject o ;
		
		try {
			o = lr.read().eval(env) ;
			System.out.println(o.toString()) ;
			assertTrue(o.toString().equals("\"Farmer\"")) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testQuoteList() {
		lr = new LispReader("'(\"Farmer\")") ;
		LispObject o ;
		
		try {
			o = lr.read().eval(env) ;
			System.out.println(o.toString()) ;
			assertTrue(o.toString().equals("(\"Farmer\")")) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testQuoteListMultiple() {
		lr = new LispReader("'(foo \"Farmer\")") ;
		LispObject o ;
		
		try {
			o = lr.read().eval(env) ;
			System.out.println(o.toString()) ;
			assertTrue(o.toString().equals("(FOO \"Farmer\")")) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
