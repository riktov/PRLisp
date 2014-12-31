package org.riktov.prlisp ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

/**
 */
class LispReader {
    public LispObject read(String sExp) {
        Reader r = new BufferedReader(new StringReader(sExp)) ;
        StreamTokenizer st = new StreamTokenizer(r) ;
        st.whitespaceChars(' ', ' ');
        st.commentChar(';') ;
        
        st.resetSyntax();
        st.whitespaceChars(' ', ' ');
        st.wordChars('a', 'z') ;
        st.wordChars('A', 'Z') ;
        st.wordChars('0', '9') ;
        st.wordChars('.', '.') ;
        st.quoteChar('"') ;
        //st.parseNumbers() ;
        //st.ordinaryChars('0', '9') ;
        
        //StringTokenizer st = new StringTokenizer(s, "( )", true) ;
        try {
            return this.readFrom(st) ;
        } catch (IOException e) {
            System.out.println("error: bad input string in LispReader.read()") ;
        }
        return new NilAtom() ;
    }

    /**
     * readFrom(StreamTokenizer st)
     */
    public LispObject readFrom(StreamTokenizer st) throws IOException {
        //	Atom a = new Atom() ;
        //ConsCell c = new ConsCell() ;
        
        while(st.nextToken() != StreamTokenizer.TT_EOF) {
            //System.out.println(st.toString()) ;
            
            if (st.ttype == '(') {
                //System.out.println("Read opening paren") ;
            		st.nextToken() ;
            		if(st.ttype == ')') {
            			return new NilAtom() ;
            		} else {
            			st.pushBack();
                     return new ConsCell(readFrom(st), readFrom(st)) ;            			
            		}
            } else if (st.ttype == ')') {
                //System.out.println("Read closing paren") ;
                return readFrom(st) ;
            } else if (st.ttype == '.') {//assumes enclosed within spaces
                return readFrom(st);
            } else if (st.ttype == '"') {
                //System.out.println("Read quoted string") ;
                return new StringAtom(st.sval);
                /** We parse numbers ourselves instead of using StreamTokenizer's number parsing, 
                 * because it will read a solitary dot as 0.0
                 	*/
            } else if (st.ttype == StreamTokenizer.TT_WORD) {
				if (st.sval.equals(".")) {
					return readFrom(st);
				} else {
					SymbolAtom s = new SymbolAtom(st.sval);
					if (s.toString().equals("NIL")) {
						return new NilAtom();
					} else {
						try {
							return parseNumber(st.sval) ;							
						} catch (NumberFormatException e) {
							return s ;
						}
					}
					// System.out.println("Read a symbol named " + s.toString())
					// ;
					//return s;
				}
            } else {
                String sym = Character.toString((char)st.ttype) ;
                
                //System.out.println("Read a single-character symbol named " + sym) ; 
                return new SymbolAtom(sym) ;
                //return new NilAtom() ;
            }
            
        }
        System.out.println("; No value") ;
        return new NilAtom() ;
    }
    
    public static DataAtom parseNumber(String st) {
    	try {
        	return new ObjectAtom(Integer.valueOf(st)) ;    		
    	} catch (NumberFormatException e) {
			return new ObjectAtom(Float.valueOf(st)) ;
		}
    }
    public void prompt() {
        System.out.print("PR-USER> ") ;
    }
    
}
