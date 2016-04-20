package org.riktov.spinja;

import static org.junit.Assert.* ;

import java.io.IOException;

//import org.junit.Test ;
//import org.junit.Ignore;
import org.junit.* ;
import org.riktov.spinja.ConsCell;
import org.riktov.spinja.LispObject;
import org.riktov.spinja.LispReader;
import org.riktov.spinja.ObjectAtom;

public class LispReaderTest {
	private LispReader lr ;
	private LispObject result ;
	
	@Before
	
    public void setUp() {
        lr = new LispReader("Foo") ;
    }

    @Test public void testReadAtoms() {
        Atom fiftyfour = (Atom)lr.read("54") ;
        //System.out.println("Does read(\"54\") create a primitive? " + fiftyfour.isPrimitive()) ;
        //assertTrue(fiftyfour.equals(54.0)) ;
        System.out.println("testReadAtoms() : read 54 as " + fiftyfour.toString()) ;
        assertTrue(fiftyfour.toString().equals("54")) ;
        
        Atom threePointOneFour = (Atom)lr.read("3.14") ;
        System.out.println("testReadAtoms() : read 3.14 as " + threePointOneFour.toString()) ;
        assertTrue(threePointOneFour.toString().equals("3.14")) ;

        LispObject foosym = lr.read("foosym") ;
        System.out.println("testReadAtoms() : read foosym as " + foosym.toString()) ;
        assertTrue(foosym.toString().equals("FOOSYM")) ;
        
        LispObject plus = lr.read("+") ;
        System.out.println("testReadAtoms() : read + as " + plus.toString()) ;
        assertTrue(plus.toString().equals("+")) ;
        
        LispObject minus = lr.read("-") ;
        System.out.println("testReadAtoms() : read + as " + minus.toString()) ;
        assertTrue(minus.toString().equals("-")) ;
        
        LispObject hello = lr.read("\"Hello\"") ;
        System.out.println("testReadAtoms() : read \"Hello\" as " + hello.toString()) ;
        assertTrue(hello.toString().equals("\"Hello\"")) ;
        
        /*
        LispObject nilly = r.read("nil") ;
        //System.out.println("Is nil primitive? " + nilly.isPrimitive()) ;
        assertTrue(nilly.isNull()) ;
        */
    }
    
    @Test public void testReadNumbers() {
        ObjectAtom fiftyfour = (ObjectAtom)lr.read("54") ;
        System.out.println("testReadNumbers() : read 54 as " + fiftyfour.data) ;
        assertTrue(fiftyfour.data.equals(54)) ;
        
        ObjectAtom pointFourOneFour = (ObjectAtom)lr.read(".414") ;
        System.out.println("testReadNumbers() : read .414 as " + pointFourOneFour.data) ;
        assertTrue(pointFourOneFour.data.equals((float)0.414)) ;    	

        ObjectAtom threePointOneFour = (ObjectAtom)lr.read("3.14") ;
        System.out.println("testReadNumbers() : read 3.14 as " + threePointOneFour.data) ;
        assertTrue(threePointOneFour.data.equals((float)3.14)) ;    	
    }
    
    @Test public void testReadCons() {
        LispObject aCons = lr.read("(12 . foo)") ;
        System.out.println("testReadCons(): \"(12 . foo)\" as " + aCons.toString()) ;
        LispObject target = ((ConsCell) aCons).cdr();
        assertTrue(target.toString().equals("FOO")) ;
    }
    
    @Test public void testReadConsNested() {
        LispObject aCons = lr.read("(12 . (foo . (\"Steve Miller\" . nil)))") ;
        System.out.println("testReadConsNested(): " + aCons.toString()) ;
        LispObject target = ((ConsCell) ((ConsCell) aCons).cdr()).car();
        assertTrue(target.toString().equals("FOO")) ;
    }
    
    /**
    @Test public void testReadBadCons() {
        LispObject aCons = r.read("(12 . foo") ;
        System.out.println("testReadBadCons(): " + aCons.toString()) ;
        LispObject target = aCons.cdr();
        assertTrue(target.toString().equals("FOO")) ;
    }
    */
    @Test public void testReadSequence() {
    	LispObject exp = lr.read("1 2 3 4") ;
    	System.out.println("testReadSequence(): " + exp) ;
       assertTrue(exp.toString().equals("1")) ;
    }
    
    @Test public void testReadListEmpty() {
        LispObject aCons = lr.read("()") ;
        System.out.println("testEmptyListAsNIl(): Read in \"()\" as " + aCons.toString()) ;
        assertTrue(aCons.isNull()) ;       
    }


    @Test public void testReadListBare() throws IOException {
    	String exp = "3 5 foo)" ;
    	
    	/*
		Reader r = new BufferedReader(new StringReader(exp));
		StreamTokenizer st = new StreamTokenizer(r);
		st.whitespaceChars(' ', ' ');
		st.commentChar(';');

		st.resetSyntax();
		st.whitespaceChars(' ', ' ');
		st.wordChars('a', 'z');
		st.wordChars('A', 'Z');
		st.wordChars('0', '9');
		st.wordChars('.', '.');
		st.quoteChar('"');
    	 */
    	
    	LispReader lr_local = new LispReader(exp) ;
    	
    	ConsCell result = (ConsCell) lr_local.readList() ;
  
    	ObjectAtom expectedFive = (ObjectAtom)((ConsCell) result.cdr()).car();
    	
    	assertTrue(expectedFive.data().equals(5)) ;
  	
    }
    
    @Test public void testReadList() {
    	String exp = "(+ 4 5)" ;
    	ConsCell result = (ConsCell) lr.read(exp) ;
    	
    	LispObject expectedFive = ((ConsCell) result.cdr()).car();
    	
    	ObjectAtom o = (ObjectAtom)expectedFive ;
    	
    	assertTrue(o.data.equals(4)) ;
    }
    
    @Test public void testReadListWithPair() {
    	String exp = "(4 5 . 2)" ;
    	ConsCell result = (ConsCell) lr.read(exp) ;
    	
    	ObjectAtom expectedFive = (ObjectAtom)((ConsCell) result.cdr()).car();
    	
    	assertTrue(expectedFive.data().equals(5)) ;
    }
    
    @Test public void testReadSymbolWithSpecialChar() {
    	String exp = "(let* ((x 5)))" ;
    	ConsCell result = (ConsCell) lr.read(exp) ;
    	ObjectAtom expectedLetStar = (ObjectAtom)result.car();
    	assertTrue(expectedLetStar.data().toString().equals("LET*")) ;
    }
    @Test public void testReadSymbolWithSpecialChar2() {
    	String exp = "hyphenated-variable-name" ;
    	LispObject result = lr.read(exp) ;
    	System.out.println("testReadSymbolWithSpecialChar2() : read " + exp + " as " + result) ;
    	assertTrue(result.toString().equals("HYPHENATED-VARIABLE-NAME")) ;
    }
    
    @Test public void testReadSymbolWithOtherOperator() {
    	String exp = "<=" ;
    	LispObject result = lr.read(exp) ;
    	System.out.println("testReadSymbolWithOtherOperator() : read " + exp + " as " + result) ;
    	assertTrue(result.toString().equals("<=")) ;
    }
}
