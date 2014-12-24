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
        ConsCell ct2 = new ConsCell(Atom.make("bar"), new NilAtom()) ;
        ConsCell ct1 = new ConsCell(Atom.make("foo"), ct2) ;
        c = new ConsCell(Atom.make("hello"), ct1) ;
    }

    /*
    @Test public void testLength() {
            assertTrue(c.length() == 3) ;
    }
    */
    
}

