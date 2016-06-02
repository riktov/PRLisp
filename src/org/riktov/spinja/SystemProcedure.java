package org.riktov.spinja;

import java.util.HashMap;
import java.io.FileNotFoundException;

/**
 * A system procedure uses system calls like file I/O, that are not part of the Lisp interpreter, 
 * or primitives.
 * @author Paul Richter &lt;riktov@freeshell.org&gt;
 *
 */
abstract class SystemProcedure extends PrimitiveProcedure {
	static HashMap<String, SystemProcedure> initialSystemProcedures(final Environment env) {
		HashMap<String, SystemProcedure> systemProcs = new HashMap<String, SystemProcedure>();
		
		systemProcs.put("load".toUpperCase(), new SystemProcedure() {
			/**
			 * load a file
			 */
			public LispObject apply(LispList argForms) {
				requireArgumentCount(1, argForms, "load") ;
				LispObject[] args = argForms.toArray() ;
				StringAtom nameAtom = (StringAtom)args[0] ;
				String filename = nameAtom.toStringUnquoted() ;

				try {
					return LispReader.load(filename, env) ;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new NilAtom() ;
				}
			}
		}) ;

		return systemProcs;		
	}
}
