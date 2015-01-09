package org.riktov.prlisp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 */
class LispReader {
	public LispObject read(String sExp) {
		Reader r = new BufferedReader(new StringReader(sExp));
		StreamTokenizer st = new StreamTokenizer(r);
		st.whitespaceChars(' ', ' ');
		st.commentChar(';');

		st.resetSyntax();
		st.whitespaceChars(' ', ' ');
		st.wordChars('a', 'z');
		st.wordChars('A', 'Z');
		st.wordChars('0', '9');
		st.wordChars('.', '.');
		st.wordChars('?', '?');	//examples: NULL? zero?
		st.wordChars('*', '*');	//examples: LET*
		//st.ordinaryChar('+');
		st.quoteChar('"');
		// st.parseNumbers() ;
		// st.ordinaryChars('0', '9') ;

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
		// Atom a = new Atom() ;
		// ConsCell c = new ConsCell() ;
		//LispObject theCar, theCdr;
		//ArrayList<LispObject> cdrList = new ArrayList<LispObject>() ;
		//theCar = null;
		//theCdr = null;

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
				return new SymbolAtom(sym);
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
		System.out.println("Finishing reading list") ;
		return new NilAtom() ;
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

abstract class ReaderMacro {
	abstract String process(String st) ;
	
	static HashMap<String, ReaderMacro> initialReaderMacros() {
		HashMap<String, ReaderMacro> macros = new HashMap<String, ReaderMacro>() ;
		
		return macros;
	}
}
