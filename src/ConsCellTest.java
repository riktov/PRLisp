package org.riktov.prlisp ;

import static org.junit.Assert.* ;

//import org.junit.Test ;
//import org.junit.Ignore;
import org.junit.* ;
import org.junit.runner.RunWith;

//import org.junit.runners.JUnit4;

/**
 * Tests for {@link Foo}.
 *
 * @author riktov@freeshell.org (Paul Richter)
 */

public class ConsCellTest {
    private ConsCell c ;

    @Before
    public void setUp() {
	/**
	 * We create a cons cell "by hand", without relying on the reader. 
	 */
        ConsCell ct2 = new ConsCell(Atom.make(23), new NilAtom()) ;
        ConsCell ct1 = new ConsCell(Atom.make(147), ct2) ;
        c = new ConsCell(Atom.make(55), ct1) ;
	// c -> (55 147 23)
    }

    @Test public void testToString() {
	String s = c.toString() ;
	assertTrue(s.equals("(55 147 23)")) ;
    }
    
    @Test public void testCdr() {
	LispObject cd = c.cdr() ;
	assertTrue(cd.toString().equals("(147 23)")) ;
    }
    
    @Test public void testArraySize() {
	LispObject[] arr = c.toArray() ;
	
	System.out.println(arr.length) ;
        assertTrue(arr.length == 3) ;
    }

    @Test public void testArrayElements() {
	LispObject[] arr = c.toArray() ;

	Atom zeroth = (Atom)arr[0] ;
	//	System.out.println("[" + zeroth + "]") ;
        assertTrue(zeroth.data().equals(55)) ;
    }
}

