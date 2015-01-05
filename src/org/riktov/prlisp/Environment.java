package org.riktov.prlisp;

import java.util.HashMap;

/**
 * Environment
 */
class Environment extends HashMap<String, LispObject> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
     */
	Environment() {
		installConstants();
		installPrimitives();
		installSpecials();
	}

	LispObject lookup(String str) {
		LispObject o = this.get(str);

		if (o == null) {
			System.out.println("EVAL: variable " + str + " has no value");
		}

		return o;
	}

	void printKeys() {
		System.out.println(keySet());	
	}
	
	boolean installConstants() {
		NilAtom nil = new NilAtom();// the singleton in the environment
		intern(nil.toString(), nil);

		SymbolAtom t = new SymbolAtom("t");// the singleton in the environment
		intern(t.toString(), t);

		return true;
	}

	boolean installPrimitives() {		
		putAll(PrimitiveProcedure.initialPrimitives()) ;
		return true;
	}

	boolean installSpecials() {		
		putAll(SpecialOperation.initialSpecials(this)) ;
		return true;
	}
	/**
	boolean installPrimitive(PrimitiveProcedure proc) {
		return intern(proc.symbol(), proc);
	}
*/
	boolean intern(String symName, LispObject o) {
		put(symName.toUpperCase(), o);
		return true;
	}
}

/**
 * Environment
 */
class ChildEnvironment extends Environment {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// members
	private Environment parent;

	/**
	 * constructor
	 * 
	 * @param parent
	 *            Parent environment.
	 */
	ChildEnvironment(Environment parent) {
		this.parent = parent;
	}

	LispObject lookup(String str) {
		if (this.containsKey(str)) {
			return this.get(str);
		}
		return parent.lookup(str);
	}
}
