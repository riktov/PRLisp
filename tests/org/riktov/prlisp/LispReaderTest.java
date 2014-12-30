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
        System.out.println("Does read(\"54\") create a primitive? " + fiftyfour.isPrimitive()) ;
        //assertTrue(fiftyfour.equals(54.0)) ;
        System.out.println(fiftyfour.toString()) ;
        assertTrue(fiftyfour.toString().equals("54.0")) ;
        
        LispObject foosym = r.read("foosym") ;
        assertTrue(foosym.toString().equals("FOOSYM")) ;
        
        LispObject hello = r.read("\"Hello\"") ;
        assertTrue(hello.toString().equals("\"Hello\"")) ;
        
        LispObject nilly = r.read("nil") ;
        //System.out.println("Is nil primitive? " + nilly.isPrimitive()) ;
        assertTrue(nilly.isNull()) ;
    }
    
    @Test public void testReadCons() {
        LispObject aCons = r.read("(12 , (foo , (34, nil)))") ;
        LispObject target = aCons.cdr().car();
        
        assertTrue(target.toString().equals("FOO")) ;
        
        
        //	LispObject s = Atom.make(13) ;
        //System.out.println(s.toString()) ;
        
        
        //System.out.println(fiftyfour.data() + 1) ;
    }

}
