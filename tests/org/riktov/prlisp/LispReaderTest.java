package org.riktov.prlisp;

import static org.junit.Assert.* ;

//import org.junit.Test ;
//import org.junit.Ignore;
import org.junit.* ;

public class LispReaderTest {
	LispReader r ;
	@Before
	
    public void setUp() {
        r = new LispReader() ;
    }

    @Test public void testReadAtoms() {
        DataAtom fiftyfour = (DataAtom)r.read("54") ;
        //System.out.println("Does read(\"54\") create a primitive? " + fiftyfour.isPrimitive()) ;
        //assertTrue(fiftyfour.equals(54.0)) ;
        System.out.println("testReadAtoms() : read 54 as " + fiftyfour.toString()) ;
        assertTrue(fiftyfour.toString().equals("54")) ;
        
        DataAtom threePointOneFour = (DataAtom)r.read("3.14") ;
        System.out.println("testReadAtoms() : read 3.14 as " + threePointOneFour.toString()) ;
        assertTrue(threePointOneFour.toString().equals("3.14")) ;

        LispObject foosym = r.read("foosym") ;
        System.out.println("testReadAtoms() : read foosym as " + foosym.toString()) ;
        assertTrue(foosym.toString().equals("FOOSYM")) ;
        
        LispObject hello = r.read("\"Hello\"") ;
        assertTrue(hello.toString().equals("\"Hello\"")) ;
        
        LispObject nilly = r.read("nil") ;
        //System.out.println("Is nil primitive? " + nilly.isPrimitive()) ;
        assertTrue(nilly.isNull()) ;
    }
    
    @Test public void testReadNumbers() {
        ObjectAtom fiftyfour = (ObjectAtom)r.read("54") ;
        System.out.println("testReadNumbers() : read 54 as " + fiftyfour.data) ;
        assertTrue(fiftyfour.data.equals(54)) ;
        
        ObjectAtom pointFourOneFour = (ObjectAtom)r.read(".414") ;
        System.out.println("testReadNumbers() : read .414 as " + pointFourOneFour.data) ;
        assertTrue(pointFourOneFour.data.equals((float)0.414)) ;    	

        ObjectAtom threePointOneFour = (ObjectAtom)r.read("3.14") ;
        System.out.println("testReadNumbers() : read 3.14 as " + threePointOneFour.data) ;
        assertTrue(threePointOneFour.data.equals((float)3.14)) ;    	
    }
    
    @Test public void testReadCons() {
        LispObject aCons = r.read("(12 . foo)") ;
        System.out.println("testReadCons(): " + aCons.toString()) ;
        LispObject target = aCons.cdr();
        assertTrue(target.toString().equals("FOO")) ;
    }
    
    @Test public void testReadConsNested() {
        LispObject aCons = r.read("(12 . (foo . (\"Steve Miller\" . nil)))") ;
        System.out.println("testReadConsNested(): " + aCons.toString()) ;
        LispObject target = aCons.cdr().car();
        assertTrue(target.toString().equals("FOO")) ;
    }
    
    @Test public void testReadBadCons() {
        LispObject aCons = r.read("(12 . foo") ;
        System.out.println("testReadBadCons(): " + aCons.toString()) ;
        LispObject target = aCons.cdr();
        assertTrue(target.toString().equals("FOO")) ;
    }
    
    @Test public void testEmptyListAsNil() {
        LispObject aCons = r.read("()") ;
        System.out.println("testEmptyListAsNIl(): Read in \"()\" as " + aCons.toString()) ;
        assertTrue(aCons.isNull()) ;       
    }

    @Test public void testReadList() {
    	String exp = "(4 5)" ;
    	LispObject result = r.read(exp) ;
    	
    	ObjectAtom expectedFive = (ObjectAtom)result.cdr().car();
    	
    	assertTrue(expectedFive.data().equals(5)) ;
    }
    
    @Test public void testReadListWithPair() {
    	String exp = "(4 5 . 2)" ;
    	LispObject result = r.read(exp) ;
    	
    	ObjectAtom expectedFive = (ObjectAtom)result.cdr().car();
    	
    	assertTrue(expectedFive.data().equals(5)) ;
    }
}
