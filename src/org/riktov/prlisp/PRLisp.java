package org.riktov.prlisp;

import java.util.Scanner;

/**
 * <h1>PRLisp</h1> A Lisp interpreter.
 *
 * @author Paul Richter &lt;riktov@freeshell.org&gt;
 * @version 0.1
 */
public class PRLisp {
	public static void main(String args[]) {
		Environment env = new Environment();
		env.initialize();
		// env.put("FOO", DataAtom.make(45)) ;
		// env.put("BAR", DataAtom.make("Bargle")) ;

		// LispObject o = env.lookup("FOO") ;
		// System.out.println(o) ;

		PRLisp lisp = new PRLisp();
		lisp.repl(env);
		// testFakeReader() ;
	}

	/**
	 * Runs a REPL(Read-Evaluate-Print Loop)
	 * 
	 * @param env
	 *            The initial environment
	 * @return None
	 */
	public void repl(Environment env) {
		System.out.println("Starting PRLisp");

		LispReader lr = new LispReader();
		lr.prompt();

		Scanner in = null ;
		try{		
			in = new Scanner(System.in);
	
			while (in.hasNextLine()) {
				String input = in.nextLine();
				if(input.isEmpty()) {
					System.out.println(";No value") ;
					lr.prompt();
					continue ;
				}
				LispObject o = lr.read(input);// READ
	
				try {
					LispObject evaluated = o.eval(env);// EVALUATE
					System.out.println(evaluated);// PRINT (evaluated)
				} catch(ClassCastException exc) {
					new LispDebugger(";The object " + o + " is not applicable.  " + exc.toString(), env) ;
				}

				//System.out.println(o.toString()) ; //print unevaluated
	
				lr.prompt();
			}// LOOP!
		}finally {
			if(in!=null)
				in.close();
		}
	}
}
