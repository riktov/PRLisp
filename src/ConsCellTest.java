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
        ConsCell ct2 = new ConsCell(Atom.make(23), new NilAtom()) ;
        ConsCell ct1 = new ConsCell(Atom.make(147), ct2) ;
        c = new ConsCell(Atom.make(55), ct1) ;
    }

    
    @Test public void testSize() {
	LispObject[] arr = c.toArray() ;
	
	System.out.println(arr.length) ;
        assertTrue(arr.length == 3) ;
    }

    @Test public void testToArray() {
	LispObject[] arr = c.toArray() ;

	Atom zeroth = (Atom)arr[0] ;
	//	System.out.println("[" + zeroth + "]") ;
        assertTrue(zeroth.data().equals(55)) ;
    }
}

