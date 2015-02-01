package org.riktov.prlisp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 */
class LispReader {
	private HashMap<String, ReaderMacro> macros;

	public LispObject read(String sExp) {
		Reader r = new BufferedReader(new StringReader(sExp));
		macros = ReaderMacro.initialReaderMacros() ;
		
		LispStreamTokenizer st = new LispStreamTokenizer(r);
		st.initializeSyntax();
		
		// StringTokenizer st = new StringTokenizer(s, "( )", true) ;
		try {
			return this.readFrom(st);
		} catch (IOException e) {
			System.out.println("error: bad input string in LispReader.read()");
		}
		return new NilAtom();
	}

	/**
	 * readFrom(StreamTokenizer st)
	 */
	public LispObject readFrom(StreamTokenizer st) throws IOException {
		while (st.nextToken() != StreamTokenizer.TT_EOF) {
			//System.out.println(st.toString());

			if (st.ttype == '(') {					
				return readListFrom(st) ;
			} else if (st.ttype == '"') {
				// System.out.println("Read quoted string") ;
				return new StringAtom(st.sval);
				/**
				 * We parse numbers ourselves instead of using StreamTokenizer's
				 * number parsing, because it will read a solitary dot as 0.0
				 */
			} else if (st.ttype == StreamTokenizer.TT_WORD) {
				Atom a ; 

				try {
					a = parseNumber(st.sval);
				} catch (NumberFormatException e) {
					a = new SymbolAtom(st.sval);
				}
				return a ;
			} else {
				String sym = Character.toString((char) st.ttype);
				
				if(macros.containsKey(sym)) {
					return macros.get(sym).process(readTextFrom(st)) ;
				} else {
					return new SymbolAtom(sym);					
				}
			} 
		}	//end of while loop
		return new NilAtom();
	}

	/**
	 * Reads everything up to a closing paren as elements of a list or dotted-pair
	 * @param st the StreamTokenizer
	 * @return the read LispObject
	 * @throws IOException
	 */
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
				st.pushBack();
				items.add(readFrom(st)) ;
			}
		}
		
		throw new LispIncompleteFormException() ;
		/*
		System.out.println("Premature end of list") ;
		return new NilAtom() ;
		*/
	}
	
	/**
	 * 
	 * @param st a StreamTokenizer
	 * @return the next entire string enclosed in parentheses (possible nested), spaces, or quotes
	 * @throws IOException
	 */
	public String readTextFrom(StreamTokenizer st) throws IOException {
		while (st.nextToken() != StreamTokenizer.TT_EOF) {
			//System.out.println(st.toString());

			if (st.ttype == '(') {					
				return readTextListFrom(st) ;
			} else if (st.ttype == '"') {
				// System.out.println("Read quoted string") ;
				return st.sval;
			} else if (st.ttype == StreamTokenizer.TT_WORD) {
				return st.sval ;
			} else {
				return Character.toString((char) st.ttype);
			} 
		}	//end of while loop
		return "" ;
	}
	
	public String readTextListFrom(StreamTokenizer st) throws IOException {
		String forms = "" ;

		//System.out.println("Starting reading list") ;
		while (st.nextToken() != StreamTokenizer.TT_EOF) {
			if (st.ttype == ')') {
				return forms + ")";
			} else {
				st.pushBack();
				String nextForm = readTextFrom(st) ;
				if(forms.equals("")) {
					forms = "(" + nextForm;
				} else {
					forms = forms + " " + nextForm ;					
				}
			}
		}
		System.out.println("Premature end of list string") ;
		return "" ;
	}
	
	public static DataAtom parseNumber(String st) {
		try {
			return new ObjectAtom(Integer.valueOf(st));
		} catch (NumberFormatException e) {
			return new ObjectAtom(Float.valueOf(st));
		}
	}

	public void prompt() {
		System.out.print("PR-USER> ");
	}
}

class LispStreamTokenizer extends StreamTokenizer {
	public LispStreamTokenizer(Reader r) {
		super(r);
	}
	
	void initializeSyntax() {
		whitespaceChars(' ', ' ');
		commentChar(';');

		resetSyntax();
		whitespaceChars(' ', ' ');
		wordChars('a', 'z');
		wordChars('A', 'Z');
		wordChars('0', '9');
		wordChars('.', '.');
		wordChars('?', '?');	//examples: NULL? zero?
		wordChars('*', '*');	//examples: LET*
		wordChars('-', '-');	//examples: VARIABLE-WITH-HYPHENS
		wordChars('>', '>');	//examples: STR->LIS
		//ordinaryChar('+');
		quoteChar('"');
		// st.parseNumbers() ;
		// st.ordinaryChars('0', '9') ;
	}
}

abstract class ReaderMacro {
	abstract LispObject process(String string) ;
	
	static HashMap<String, ReaderMacro> initialReaderMacros() {
		HashMap<String, ReaderMacro> macros = new HashMap<String, ReaderMacro>() ;
		
		macros.put("'", new ReaderMacro() {
			@Override
			LispObject process(String formString) {
				String processedString = "(quote " + formString + ")" ;
				return new LispReader().read(processedString) ;
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
