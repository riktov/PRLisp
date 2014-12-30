package org.riktov.prlisp ;

import static org.junit.Assert.assertTrue;

//import org.junit.Test ;
//import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;

//import org.junit.runners.JUnit4;

/**
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
        ConsCell ct2 = new ConsCell(DataAtom.make(23), new NilAtom()) ;
        ConsCell ct1 = new ConsCell(DataAtom.make(147), ct2) ;
        c = new ConsCell(DataAtom.make(55), ct1) ;
	// c -> (55 147 23)
    }

    @Test public void testToString() {
        String s = c.toString() ;
        assertTrue(s.equals("(55 147 23)")) ;
    }
    
    /** too simple to test
    @Test public void testCar() {
        LispObject car = c.car() ;
        assertTrue(car.toString().equals("55")) ;
    }
    
    @Test public void testCdr() {
        LispObject cd = c.cdr() ;
        assertTrue(cd.toString().equals("(147 23)")) ;
    }
    */
    
    @Test public void testArraySize() {
        LispObject[] arr = c.toArray() ;
        
        System.out.println(arr.length) ;
        assertTrue(arr.length == 3) ;
    }
    
    @Test public void testArrayElements() {
        LispObject[] arr = c.toArray() ;
        
        ObjectDataAtom zeroth = (ObjectDataAtom)arr[0] ;
        System.out.println("[" + zeroth + "]") ;
        assertTrue(zeroth.dataEquals(new Integer(55))) ;
    }
}

