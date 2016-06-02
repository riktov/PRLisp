package org.riktov.spinja;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MacroTest {

	Environment env ;
	
	@Before
	public void setUp() throws Exception {
		env = new Environment() ;
	}

	@Test
	public void testNullMacro() {
		// a macro with no args and body that is a scalar
		Macro m = new Macro((LispList)NilAtom.nil, (LispList)(NilAtom.nil), env) ;
	}

}
