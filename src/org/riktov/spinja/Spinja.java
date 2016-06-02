package org.riktov.spinja;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * <h1>Spinja</h1> A Lisp interpreter written in Java -- "liSP IN JAva"
 * 
 * @author Paul Richter &lt;riktov@freeshell.org&gt;
 * @version 0.1
 */
public class Spinja {
	public static void main(String args[]) {
		Environment env = new Environment();
		env.initialize();

		Spinja lisp = new Spinja();
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
/*
	public void repl(Environment env) {
		System.out.println("Starting " + this.getClass().getSimpleName());

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
*/
	public void repl(Environment env) {
		System.out.println("Starting " + this.getClass().getSimpleName());

		LispReader lr = new LispReader(new BufferedReader(new InputStreamReader(System.in)));
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
