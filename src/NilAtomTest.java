package org.riktov.prlisp ;

import static org.junit.Assert.* ;

import org.junit.Test ;
import org.junit.Ignore;
import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;

/**
 * Tests for {@link Foo}.
 *
 * @author riktov@freeshell.org (Paul Richter)
 */
public class NilAtomTest {
    @Test public void testNil() {
        NilAtom n = new NilAtom() ;
        assertTrue(n.isNull()) ;

    }

    @Test public void testString() {
        NilAtom n = new NilAtom() ;
	assertTrue(n.toString() == "NIL") ;
    }
}

