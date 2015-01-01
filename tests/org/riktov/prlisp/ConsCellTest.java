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
        ConsCell ct2 = new ConsCell(Atom.make(23), new NilAtom()) ;
        ConsCell ct1 = new ConsCell(Atom.make(147), ct2) ;
        c = new ConsCell(Atom.make(55), ct1) ;
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
        assertTrue(arr.length == 3) ;
    }
    
    @Test public void testArrayElements() {
        LispObject[] arr = c.toArray() ;
        
        ObjectAtom zeroth = (ObjectAtom)arr[0] ;
        //System.out.println("[" + zeroth + "]") ;
        assertTrue(zeroth.data.equals(new Integer(55))) ;
    }
    
    @Test public void testMakeListFromArray() {
    	SymbolAtom plus = new SymbolAtom("+") ;
    	ObjectAtom four = new ObjectAtom(4) ;
    	SymbolAtom x = new SymbolAtom("x") ;
    	LispObject[] args = {
    			plus, four, x  
    	} ;
    	ConsCell c = new ConsCell(args) ;
    	
    	System.out.println("testMakeListFromArray(): List made from array:" + c.toString()) ;
    	
    	assertTrue(c.car() == plus) ;
    	assertTrue(c.cdr().car() == four) ;
    	assertTrue(c.cdr().cdr().car() == x) ;
    	assertTrue(c.cdr().cdr().cdr().isNull()) ;
    }
    
    @Test public void testMakeListFromArrayShort() {
    	SymbolAtom plus = new SymbolAtom("+") ;
    	LispObject[] args = { plus } ;
    	ConsCell c = new ConsCell(args) ;
    	
    	System.out.println("testMakeListFromArrayShort(): List made from array:" + c.toString()) ;
    	
    	assertTrue(c.car() == plus) ;
    	assertTrue(c.cdr().isNull()) ;
    }
    

    @Test public void testMakeListFromArrayEmpty() {
    	LispObject[] args = {} ;
    	LispObject c = ConsCell.make(args) ;
    	
    	System.out.println("testMakeListFromEmptyArray(): List made from array:" + c.toString()) ;
    	
    	assertTrue(c.isNull()) ;
    }
}

