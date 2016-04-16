package org.riktov.spinja;

import java.io.BufferedReader;
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
			return this.read() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return NilAtom.nil ;
		}
	}
	
	/**
	 * read() - Read a single object from the associated stream
	 * @return a LispObject
	 * @throws IOException
	 */
	public LispObject read() throws IOException {
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
					return macros.get(sym).process(read().toString()) ;
				} else {
					return new SymbolAtom(sym);					
				}
			} 
		}	//end of while loop, exited by no more input
		this.can_continue_reading = false ;
		return new NilAtom();
	}
	
	/**
	 * readFrom(StreamTokenizer st)
	 * @param st a StreamTokenizer
	 */
	/*
	public LispObject readFrom(StreamTokenizer st) throws IOException {
		while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			//System.out.println(st.toString());

			if (st.ttype == '(') {					
				return readListFrom(st) ;
			} else if (tokenizer.ttype == '"') {
				System.out.println("Read quoted string") ;
				return new StringAtom(st.sval);
			} else if (tokenizer.ttype == StreamTokenizer.TT_WORD) {
				///We parse numbers ourselves instead of using StreamTokenizer's
				//number parsing, because it will read a solitary dot as 0.0
				Atom a ; 

				try {
					a = parseNumber(st.sval);
				} catch (NumberFormatException e) {
					a = new SymbolAtom(st.sval);
				}
				return a ;
			} else {
				String sym = Character.toString((char) tokenizer.ttype);
				
				if(macros.containsKey(sym)) {
					return macros.get(sym).process(readTextFrom(st)) ;
				} else {
					return new SymbolAtom(sym);					
				}
			} 
		}	//end of while loop
		return new NilAtom();
	}
	*/

	/**
	 * readTextFrom(StreamTokenizer st)
	 * @param st a StreamTokenizer
	 * @return the next entire string enclosed in parentheses (possible nested), spaces, or quotes
	 * @throws IOException
	 */
	/*
	public String readTextFrom(StreamTokenizer st) throws IOException {
		while (st.nextToken() != StreamTokenizer.TT_EOF) {
			//System.out.println(st.toString());

			if (st.ttype == '(') {					
				return readTextListFrom(tokenizer) ;
			} else if (st.ttype == '"') {
				// System.out.println("Read quoted string") ;
				return tokenizer.sval;
			} else if (tokenizer.ttype == StreamTokenizer.TT_WORD) {
				return tokenizer.sval ;
			} else {
				return Character.toString((char) st.ttype);
			} 
		}	//end of while loop
		return "" ;
	}
	

	/**
	 * Reads everything up to a closing paren as elements of a list or dotted-pair
	 * @param st the StreamTokenizer
	 * @return the read LispObject
	 * @throws IOException
	 */
	/*
	public LispObject readListFrom(StreamTokenizer st) throws IOException {
		ArrayList<LispObject> items = new ArrayList<LispObject>() ;

		//System.out.println("Starting reading list") ;
		while (st.nextToken() != StreamTokenizer.TT_EOF) {
			if (st.ttype == ')') {
				//System.out.println("Closing paren to list") ;
				LispObject[] arr = new LispObject[items.size()];
				items.toArray(arr);
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
				items.add(readFrom(st)) ;
			}
		}
		
		throw new LispIncompleteFormException() ;
	}
	*/
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
				items.toArray(arr);

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
	
	public boolean canContinueReading() {
		return this.can_continue_reading ;
	}
	
	public static DataAtom parseNumber(String st) {
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
		wordChars('?', '?');	//examples: NULL? zero?
		wordChars('*', '*');	//examples: LET*
		wordChars('-', '-');	//examples: VARIABLE-WITH-HYPHENS
		wordChars('>', '>');	//examples: STR->LIS
		wordChars(';', ';');	//handle lines that start with?? ;
		//ordinaryChar('+');
		quoteChar('"');
		// st.parseNumbers() ;
		// st.ordinaryChars('0', '9') ;
		commentChar(';') ;
	}
}

abstract class ReaderMacro {
	abstract LispObject process(String string) ;
	
	static String quoteString(String s) {
		return s ;
	}
	
	static HashMap<String, ReaderMacro> initialReaderMacros() {
		HashMap<String, ReaderMacro> macros = new HashMap<String, ReaderMacro>() ;
		
		macros.put("'", new ReaderMacro() {
			@Override
			LispObject process(String formString) {
				//System.out.println("String fed to reader macro: "+ formString) ;
				String processedString = "(quote " + formString + ")" ;
				//System.out.println("in reader macro process(): " + processedString);
				try {
					return new LispReader(processedString).read() ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return NilAtom.nil ;
				}
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
