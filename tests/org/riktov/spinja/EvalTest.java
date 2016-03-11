package org.riktov.spinja;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.riktov.spinja.Atom;
import org.riktov.spinja.ConsCell;
import org.riktov.spinja.Environment;
import org.riktov.spinja.LispObject;
import org.riktov.spinja.NilAtom;
import org.riktov.spinja.ObjectAtom;
import org.riktov.spinja.StringAtom;
import org.riktov.spinja.SymbolAtom;

/**
* Test
* 
* @author riktov@freeshell.org (Paul Richter)
*/

public class EvalTest {
    private ConsCell c ;
    String helloString = "Hello, I am foo." ;
    
    public Environment env ;

    @Before
    public void setUp() {
	/**
	 * We create a cons cell "by hand", without relying on the reader. 
	 */
    	env = new Environment() ;
    	env.initialize();

        ConsCell ct2 = new ConsCell(Atom.make(23), NilAtom.nil) ;
        ConsCell ct1 = new ConsCell(Atom.make(47), ct2) ;
        c = new ConsCell(new SymbolAtom("+"), ct1) ;
        
	// c -> (+ 47 23)
    }

    @Test public void testEvalSymbol() {
    	env.intern("foo", new StringAtom(helloString)) ;
    	  	
    	LispObject sexp = new SymbolAtom("foo") ;
    	ObjectAtom value = (ObjectAtom)sexp.eval(env) ;
    	
    	assertTrue(value.data() == helloString) ;
    }
    
    /**
    @Test public void testEvalApplyPrimitive() {
    	// The primitive + converts the result to float
    	ObjectAtom sum = (ObjectAtom)c.eval(env) ;

    	//System.out.println(sum) ;
    	
       assertTrue(sum.data.equals(new Float(70))) ;
    }
    */
}