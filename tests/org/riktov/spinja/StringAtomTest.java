package org.riktov.spinja ;

import static org.junit.Assert.assertTrue;


//import org.junit.Test ;
//import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;
import org.riktov.spinja.LispObject;
import org.riktov.spinja.StringAtom;

//import org.junit.runners.JUnit4;

/**
 * Tests for {@link Foo}.
 *
 * @author riktov@freeshell.org (Paul Richter)
 */

public class StringAtomTest {
    private LispObject str ;

    @Before
    public void setUp() {
        str = new StringAtom("I am a StringAtom.") ;
    }

    @Test public void testStringAtomOutputIsQuoted() {
        //System.out.println(str.toString()) ;
        assertTrue(str.toString().equals("\"I am a StringAtom.\"")) ;
    }
    
}

