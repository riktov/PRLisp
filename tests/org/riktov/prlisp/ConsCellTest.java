package org.riktov.prlisp;

import static org.junit.Assert.assertFalse;
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
	private ConsCell c, d ;
	private Environment env ;
	ObjectAtom sevenFortySeven = new ObjectAtom(747);
	
	@Before
	public void setUp() {
		env = new Environment();
		/**
		 * We create a cons cell "by hand", without relying on the reader.
		 */
		ConsCell ct2 = new ConsCell(Atom.make(23), NilAtom.nil);
		ConsCell ct1 = new ConsCell(Atom.make(147), ct2);
		c = new ConsCell(Atom.make(55), ct1);
		// c -> (55 147 23)		
		env.intern("JUMBO", sevenFortySeven);
		
		d = new ConsCell(Atom.make(13), Atom.make(89)) ;
	}

	@Test
	public void testToString() {
		String s = c.toString();
		assertTrue(s.equals("(55 147 23)"));
	}

	 @Test public void testCar() { 
		LispObject car = c.car() ;
        assertTrue(car.toString().equals("55")) ; }
	 
	 @Test public void testCarCons() {
		 System.out.println("testCarCons(): car of " + d) ;
		String s = (d.car()).toString();
		assertTrue(s.equals("13")) ;
	 }

	 @Test public void testCdrCons() {
		 System.out.println("testCdrCons(): cdr of " + d) ;
		String s = (d.cdr()).toString();
		assertTrue(s.equals("89")) ;
	 }

	 @Test public void testCdr() { LispObject cd = c.cdr() ;
	        assertTrue(cd.toString().equals("(147 23)")) ; }

	@Test
	public void testArraySize() {
		LispObject[] arr = c.toArray();
		assertTrue(arr.length == 3);
	}

	@Test
	public void testArrayElements() {
		LispObject[] arr = c.toArray();

		ObjectAtom zeroth = (ObjectAtom) arr[0];
		// System.out.println("[" + zeroth + "]") ;
		assertTrue(zeroth.data.equals(new Integer(55)));
	}

	@Test
	public void testMakeListFromArray() {
		SymbolAtom plus = new SymbolAtom("+");
		ObjectAtom four = new ObjectAtom(4);
		SymbolAtom x = new SymbolAtom("x");
		LispObject[] args = { plus, four, x };
		ConsCell c = new ConsCell(args);

		System.out.println("testMakeListFromArray(): List made from array:"
				+ c.toString());

		assertTrue(c.car() == plus);
		assertTrue(c.cdr().car() == four);
		assertTrue(c.cdr().cdr().car() == x);
		assertTrue(c.cdr().cdr().cdr().isNull());
	}

	@Test
	public void testMakeListFromArrayShort() {
		SymbolAtom plus = new SymbolAtom("+");
		LispObject[] args = { plus };
		ConsCell c = new ConsCell(args);

		System.out
				.println("testMakeListFromArrayShort(): List made from array:"
						+ c.toString());

		assertTrue(c.car() == plus);
		assertTrue(c.cdr().isNull());
	}

	@Test
	public void testMakeListFromArrayEmpty() {
		LispObject[] args = {};
		LispObject c = ConsCell.make(args);

		System.out
				.println("testMakeListFromEmptyArray(): List made from array:"
						+ c.toString());

		assertTrue(c.isNull());
	}

	@Test
	public void testEvalList() {
		ObjectAtom sevenFortySeven = new ObjectAtom(747);
		env.intern("JUMBO", sevenFortySeven);

		ConsCell ct2 = new ConsCell(Atom.make(23), NilAtom.nil);
		ConsCell ct1 = new ConsCell(new SymbolAtom("jumbo"), ct2);
		LispList ct0 = new ConsCell(Atom.make(55), ct1);

		//ct0 => (55 JUMBO 23)
		
		System.out.println("testEvalList(): " + ct0.toString());
		LispList evaluated = ct0.evalList(env);
		System.out.println("testEvalList() result: " + evaluated.toString());

		LispObject evaluatedSecondElement = evaluated.cdr().car();
		assertTrue(evaluatedSecondElement.equals(sevenFortySeven));
	}

	/** This tests something under a situation that should never occur: the argsList not being a proper list.
	 * Thus is is disabled.
	 * However it might be useful to test for that situation itself. It should raise a ClassCastException.
	@Test
	public void testEvalListPair() {
		ConsCell c = new ConsCell(Atom.make(55), Atom.make(13)) ;
		
		System.out.println("testEvalListPair(): " + c.toString());
		LispList evaluated = c.evalList(env);
		System.out.println("testEvalListPair() result: " + evaluated.toString());

		ObjectAtom evaluatedCdr = (ObjectAtom)evaluated.cdr();
		assertTrue(evaluatedCdr.data.equals(13));
	}
	*/
	
	@Test
	public void testEvalListNil() {
		LispList ct0 = NilAtom.nil ;
		
		System.out.println("testEvalList(): " + ct0.toString());
		LispList evaluated = ct0.evalList(env);
		System.out.println("testEvalList() result: " + evaluated.toString());

		assertTrue(evaluated.isNull()) ;
	}
	
	/**
	 * Test that the return value of evalSequence is the last form in the list
	 */
	@Test
	public void testEvalSequenceValue() {
		LispObject exp1 = NilAtom.nil ;
		LispObject exp2 = new StringAtom("Bob Seger") ;
		LispObject exp3 = new SymbolAtom("JUMBO") ;
		
		LispObject[] forms = new LispObject[] {
				exp1,
				exp2,
				exp3
		} ;
				
		LispList c = new ConsCell(forms) ;
		System.out.println("testEvalSequenceResult(): " + c.toString()) ;
		LispObject result = c.evalSequence(env) ;
		System.out.println("testEvalSequenceResult(): result: " + result.toString()) ;
		
		assertTrue(result == sevenFortySeven) ;
	}
	
	/**
	 * Test that evalSequence evaluates all intermediate forms
	 */
	@Test
	public void testEvalSequenceIntermediate() {
		LispObject sevenEightySeven = new ObjectAtom(787) ;
		LispObject exp1 = NilAtom.nil ;
		LispObject exp2 = new ConsCell(new LispObject[] { 
			new SymbolAtom("setq"),
			new SymbolAtom("dreamliner"),
			sevenEightySeven
			}) ;
		LispObject exp3 = DataAtom.make(221) ;
		
		LispObject[] forms = new LispObject[] {
				exp1,
				exp2,
				exp3
		} ;
				
		LispList c = new ConsCell(forms) ;
		System.out.println("testEvalSequenceIntermediate(): " + c.toString()) ;

		assertFalse(env.containsKey("DREAMLINER"));

		c.evalSequence(env);

		assertTrue(env.containsKey("DREAMLINER"));
		assertTrue(env.get("DREAMLINER").equals(sevenEightySeven)) ;
	}
	
	/**
	 * Test that a list of LispObject values gets correctly bound to a list of parameter names.
	 * The list of parameters can have list-binding.
	 */
	@Test
	public void testBindToParamsProperList() {
		ConsCell params = new ConsCell(new SymbolAtom[] {
				new SymbolAtom("foo"),
				new SymbolAtom("bar"),
				new SymbolAtom("baz"),
		} ) ;
	
		ConsCell vals = new ConsCell(new ObjectAtom[] {
				new ObjectAtom(5),
				new ObjectAtom(6),
				new ObjectAtom(7),
		} ) ;
		
		vals.bindToParams(params, env);
		
		assertTrue(env.containsKey("BAR"));
		LispObject six = env.get("BAR") ;
		assertTrue(((ObjectAtom)six).data.equals(6)) ;
	}

	@Test
	public void testBindToParamsImproperList() {
		SymbolAtom foo = new SymbolAtom("foo");
		SymbolAtom bar = new SymbolAtom("bar");
		SymbolAtom baz = new SymbolAtom("baz");

		ConsCell params = new ConsCell(foo, new ConsCell(bar, baz)) ;
		
		ConsCell vals = new ConsCell(new ObjectAtom[] {
				new ObjectAtom(5),
				new ObjectAtom(6),
				new ObjectAtom(7),
				new ObjectAtom(8),
				new ObjectAtom(9),
		} ) ;
		
		vals.bindToParams(params, env);
		
		env.printKeys();
		assertTrue(env.containsKey("BAZ"));
		LispObject sevenEightNine = env.get("BAZ") ;
		
		System.out.println(sevenEightNine) ;
		ObjectAtom eight = (ObjectAtom)sevenEightNine.cdr().car() ;
		assertTrue(eight.data.equals(8)) ;
	}}
