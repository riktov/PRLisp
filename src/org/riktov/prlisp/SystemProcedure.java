package org.riktov.prlisp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A system procedure uses system calls like file I/O, that are not part of the Lisp interpreter, or primitives.
 * @author paul
 *
 */
abstract class SystemProcedure extends LispProcedure {
	static HashMap<String, SystemProcedure> initialSystemProcedures(final Environment env) {
		HashMap<String, SystemProcedure> systemProcs = new HashMap<String, SystemProcedure>();

		systemProcs.put("load".toUpperCase(), new SystemProcedure() {
			public LispObject apply(LispList argForms) {
				requireArgumentCount(1, argForms, "load") ;
				LispObject[] args = argForms.toArray() ;
				StringAtom nameAtom = (StringAtom)args[0] ;
				String filename = nameAtom.toStringUnquoted() ;

			    String input;
	
			    System.out.println("Working Directory = " +
			              System.getProperty("user.dir"));
			    
				String previousFragment = "" ;
				LispObject readObject = null ;
				LispObject lastReadValue = null ;
				
				FileReader fr = null ;
				try{
					fr = new FileReader(filename) ;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				BufferedReader reader = new BufferedReader(fr);
				LispReader lr = new LispReader();

				try {
					while ((input = reader.readLine()) != null) {
						System.out.println(input) ;
						if(input.isEmpty()) { continue ; }
						
						String expression = previousFragment + input ;
						try {
							readObject = lr.read(expression);// READ					
							System.out.println("READ: " + readObject) ;
						} catch (LispIncompleteFormException exc) {
							previousFragment = previousFragment + expression ;
							continue ;
						}

						LispObject evaluated = null ;
						try {
							evaluated = readObject.eval(env);// EVALUATE
							lastReadValue = evaluated ;
							//System.out.println(evaluated) ;
						} catch (LispAbortEvaluationException exc) {
							System.out.println("Evaluation aborted.") ;
						}
						
						previousFragment = "" ;
						
						//System.out.println(o.toString()) ; //print unevaluated
					} //Loop next line
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(";Value: " + lastReadValue);// PRINT (evaluated)
				lr.prompt();
				return NilAtom.nil ;
			}
		}) ;
		
		return systemProcs;		
	}
}
