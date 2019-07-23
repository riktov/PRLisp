package org.riktov.spinja;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;

public class RegressionTest {
	private Environment env ;
	private LispObject result = null ;
	
	@Before
	public void setUp() throws Exception {
		env = new Environment() ;
		env.initialize() ;
	}

	/**
	 * This was something about the environment, and values weren't being properly interned
	 * in a re-entrant environment
	 */
	@Test
	public void testRegression1() {
		try {
			LispReader.load("ch3.scm", env) ;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//env.dump() ;
		
		LispObject []exp2 = {
				new SymbolAtom("withdraw"),
				new ObjectAtom(10)
		} ;
		
		ConsCell form = new ConsCell(exp2) ;
		result = form.eval(env) ;
		
		//System.out.println(new SymbolAtom("withdraw").eval(env)) ;
		System.out.println(result) ;
		assertTrue(result.toString().equals("90")) ;
	}


	@Test
	public void testRegression2() {	
		LispReader lr = new LispReader("") ;
			
		//manually create a procedure from a param list, body, and env.
		LispObject []paramArray = {
				new SymbolAtom("amount")
		} ;
		
		ConsCell params = new ConsCell(paramArray) ;
		LispList body = (LispList)lr.read("(if (>= 100 1) amount)") ;
		
		LispProcedure proc =  new CompoundProcedure(params, body, env) ;

		//manually apply it with arguments
		LispObject []argsArray = {
				new ObjectAtom(12)
		} ;

		ConsCell args = new ConsCell(argsArray) ;
		result = proc.apply(args) ;
		
		assertTrue(result.toString().equals("12")) ;

	}
	
	@Test
	public void testRegression3() {	
		String str = "(define (withdraw amount)(if (>= 100 1) amount))" ;

		LispReader lr = new LispReader("") ;
		LispObject exp ;
		
		exp = lr.read(str);
		result = exp.eval(env) ;

		LispProcedure proc = (LispProcedure)env.lookup("WITHDRAW") ;

		LispObject []argsArray = {
				new ObjectAtom(12)
		} ;

		ConsCell args = new ConsCell(argsArray) ;
		result = proc.apply(args) ;
		
		assertTrue(result.toString().equals("12")) ;

	}
	
}
