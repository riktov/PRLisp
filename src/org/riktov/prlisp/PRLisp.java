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
	
			String previousLine = "" ;
			LispObject readObject = null ;
			
			while (in.hasNextLine()) {
				String input = previousLine + in.nextLine();
				if(input.isEmpty()) {
					System.out.println(";No value") ;
					lr.prompt();
					continue ;
				}
				
				try {
					readObject = lr.read(input);// READ					
				} catch (LispIncompleteFormException exc) {
					previousLine = input ;
					continue ;
				}
	
				LispObject evaluated = null ;
				try {
					evaluated = readObject.eval(env);// EVALUATE
					System.out.println(";Value: " + evaluated);// PRINT (evaluated)
				} catch (LispAbortEvaluationException exc) {
					System.out.println("Evaluation aborted.") ;
				}
				
				previousLine = "" ;
				
				//System.out.println(o.toString()) ; //print unevaluated
	
				lr.prompt();
			}// LOOP!
		}finally {
			if(in!=null)
				in.close();
		}
	}
}
