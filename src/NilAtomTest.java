package org.riktov.prlisp ;

import static org.junit.Assert.* ;

//import org.junit.Test ;
//import org.junit.Ignore;
import org.junit.* ;

//import org.junit.runners.JUnit4;

/**
 * Tests for {@link Foo}.
 *
 * @author riktov@freeshell.org (Paul Richter)
 */
public class NilAtomTest {
    private NilAtom n ;
    private StringAtom str ;
    private SymbolAtom sym ;
    private Atom num ;
    
    @Before
    public void setUp() {
        n = new NilAtom() ;
        str = new StringAtom("I am a StringAtom.") ;
        sym = new SymbolAtom("sym") ;
        num = new IntAtom(45) ;
    }
    
    @Test public void testNil() { assertTrue(n.isNull()) ; }
    @Test public void testNilAsString() { assertTrue(n.toString() == "NIL") ; }
    @Test public void testStringNotNil () { assertFalse(str.isNull()) ; }
    @Test public void testSymbolNotNil () { assertFalse(sym.isNull()) ; }
    @Test public void testNumNotNil () { assertFalse(num.isNull()) ; }
}
