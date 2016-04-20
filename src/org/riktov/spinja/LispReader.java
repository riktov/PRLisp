package org.riktov.spinja;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Converts strings into lisp forms. Also maintains a reader macro table
 * to convert input in special ways
 * @author riktov@freeshell.org (Paul Richter)
 *
 */
class LispReader {
	private HashMap<String, ReaderMacro> macros;
	private LispStreamTokenizer tokenizer ;
	private boolean can_continue_reading = true ;

	/**
	 * constructor
	 * @param r - a BufferedReader
	 */
	public LispReader(BufferedReader r) {
		macros = ReaderMacro.initialReaderMacros() ;
		tokenizer = new LispStreamTokenizer(r) ;
	}
	
	public LispReader(String s) {
		this(new BufferedReader(new StringReader(s))) ;
	}
	
	/**
	 * Converts a string into a LispObject
	 * @param sExp - a String
	 * @return a LispObject
	 * @throws IOException 
	 * 
	 * This will discard the current tokenizer for a new one built on the string.
	 */
	public LispObject read(String sExp) {
		Reader r = new BufferedReader(new StringReader(sExp));
		tokenizer = new LispStreamTokenizer(r);
		try {
			return read() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	/**
	 * readObject() - Read a single object from the associated stream
	 * @return a LispObject
	 * @throws IOException
	 */
	public LispObject read() throws IOException  {
		while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			this.can_continue_reading = true ;
			//System.out.println(tokenizer.sval);

			if (tokenizer.ttype == '(') {					
				return readList() ;
			} else if (tokenizer.ttype == '"') {
				//System.out.println("Read quoted string") ;
				return new StringAtom(tokenizer.sval);
			} else if (tokenizer.ttype == StreamTokenizer.TT_WORD) {
				/**
				 * We parse numbers ourselves instead of using StreamTokenizer's
				 * number parsing, because it will read a solitary dot as 0.0
				 */
				Atom a ; 

				try {
					a = parseNumber(tokenizer.sval);
				} catch (NumberFormatException e) {
					a = new SymbolAtom(tokenizer.sval);
				}
				return a ;
			} else {
				String sym = Character.toString((char) tokenizer.ttype);
				
				if((null != macros) && macros.containsKey(sym)) {
					//return macros.get(sym).process(readTextFrom(tokenizer)) ;
					//return macros.get(sym).process(read().toString()) ;
					return macros.get(sym).expand(read()) ;
				} else {
					return new SymbolAtom(sym);					
				}
			} 
		}	//end of while loop, exited by no more input
		this.can_continue_reading = false ;
		return null ;
		//return new NilAtom();
	}
	
	/**
	 * Reads everything up to a closing parenthesis as elements of a list or dotted-pair
	 * @return the read LispObject
	 * @throws IOException
	 */
	public LispObject readList() throws IOException {
		ArrayList<LispObject> items = new ArrayList<LispObject>() ;

		//System.out.println("Starting reading list") ;
		while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			if (tokenizer.ttype == ')') {
				//System.out.println("Closing paren to list") ;
				LispObject[] arr = new LispObject[items.size()];
				items.toArray(arr);	//writes into arr

				if(arr.length == 0) {
					return new NilAtom() ;
				} else if(arr.length == 1) {
					return new ConsCell(arr[0], new NilAtom()) ;
				} else if(arr[1].toString().equals(".")) {
					return new ConsCell(arr[0], arr[2]) ;
				} else {
					return new ConsCell(arr) ;
				}
			} else {
				tokenizer.pushBack();
				items.add(read()) ;
			}
		}
		
		throw new LispIncompleteFormException() ;
	}
	
	public LispObject readSequence() { 
		LispObject lastReadObject = null ;
		LispObject o = null ;
		
		while(canContinueReading()) {
			try {
				o = read() ;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			if(o != null) {
				lastReadObject = o ;
			}
		}
		return lastReadObject ;
	}

	public static LispObject load(String filename, Environment env) throws FileNotFoundException{ 
		LispObject lastReadValue = null ;
		
		FileReader fr = null ;
		
		fr = new FileReader(filename) ;
		
		BufferedReader reader = new BufferedReader(fr);
		LispReader lr = new LispReader(reader);

		// read() will return as soon as it reads an expression. We need to keep going until
		// the end of the file (stream)
		
		while(lr.canContinueReading()) {
			//System.out.println("Can continue reading?: " + lr.canContinueReading()) ;
			LispObject o = null;
			try {
				o = lr.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //.eval(env) ;
			
			if(o != null) {
				lastReadValue = o.eval(env) ;
			}
		}

		return lastReadValue ;
	}
	
	public boolean canContinueReading() {
		return this.can_continue_reading ;
	}
	
	public static Atom parseNumber(String st) {
		try {
			return new ObjectAtom(Integer.valueOf(st));
		} catch (NumberFormatException e) {
			return new ObjectAtom(Float.valueOf(st));
		}
	}

	public void prompt() {
		System.out.print("SPINJA-USER> ");
	}
}

class LispStreamTokenizer extends StreamTokenizer {
	public LispStreamTokenizer(Reader r) {
		super(r);
		this.initializeSyntax();
	}
	
	void initializeSyntax() {
		whitespaceChars(' ', ' ');
		commentChar(';');

		resetSyntax();
		whitespaceChars(' ', ' ');
		whitespaceChars(9, 9) ;
		whitespaceChars('\n', '\n') ;
		wordChars('a', 'z');
		wordChars('A', 'Z');
		wordChars('0', '9');
		wordChars('.', '.');
		wordChars('!', '!');
		wordChars('*', '*');	//examples: LET*
		wordChars('-', '-');	//examples: VARIABLE-WITH-HYPHENS
		wordChars('<', '?');	// <=>? examples: STR->LIS
		wordChars(';', ';');	//handle lines that start with?? ;
		//ordinaryChar('+');
		quoteChar('"');
		// st.parseNumbers() ;
		// st.ordinaryChars('0', '9') ;
		commentChar(';') ;
	}
}

/**
 * 
 * @author paul
 *
 */
abstract class ReaderMacro {
	//abstract LispObject process(String string) ;
	abstract LispObject expand(LispObject o) ;
	
	static String quoteString(String s) {
		return s ;
	}
	
	static HashMap<String, ReaderMacro> initialReaderMacros() {
		HashMap<String, ReaderMacro> macros = new HashMap<String, ReaderMacro>() ;
		
		macros.put("'", new ReaderMacro() {
			@Override
			LispObject expand(LispObject lo) {
				LispObject[] parts = {
					new SymbolAtom("quote"), lo
				} ;
				ConsCell processedList = new ConsCell(parts) ;
				
				return processedList;
			}
		}) ;
		
		return macros;
	}
}

class LispIncompleteFormException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
