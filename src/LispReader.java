package org.riktov.prlisp ;

import java.io.* ;

class LispReader {
    public LispObject read(String sExp) {
        Reader r = new BufferedReader(new StringReader(sExp)) ;
        StreamTokenizer st = new StreamTokenizer(r) ;
        st.whitespaceChars(' ', ' ');
        st.commentChar(';') ;
        st.parseNumbers() ;
        
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
     * StreamTokenizer is not very flexible, and lisp syntax is kind of tough.
     * We can't use dots for dotted-pairs because StreamTokenizer will treat 
     * a single dot surrounded by spaces as the number 0.0 . So for this Lisp
     * we use commas instead of dots.
     */
    public LispObject readFrom(StreamTokenizer st) throws IOException {
        //	Atom a = new Atom() ;
        //ConsCell c = new ConsCell() ;
        
        while(st.nextToken() != st.TT_EOF) {
            //System.out.println(st.toString()) ;
            
            if (st.ttype == '(') {
                //System.out.println("Read opening paren") ;
                return new ConsCell(readFrom(st), readFrom(st)) ;
            } else if (st.ttype == ')') {
                //System.out.println("Read closing paren") ;
                return readFrom(st) ;
                //return c ;
            } else if (st.ttype == ',') {//assumes enclosed within spaces
                //return new SymbolAtom(st.sval) ;
                return readFrom(st);
            } else if (st.ttype == '"') {
                //System.out.println("Read quoted string") ;
                return new StringAtom(st.sval); 
            } else if (st.ttype == st.TT_NUMBER) {
                //                System.out.println("I think we got a number") ;
                return new Atom(st.nval) ;
            } else if (st.ttype == st.TT_WORD) {
                SymbolAtom s = new SymbolAtom(st.sval) ;
                if(s.toString() == "NIL") {
                    System.out.println("Read a NIL") ;
                    return new NilAtom() ;
                } else {
                    System.out.println("Read a symbol named " + s.toString()) ;
                    return s ;
                }
            } else {
                //a = Atom.make() ;
                //return new NilAtom() ;
            }
            
        }
        return new NilAtom() ;
    }
    
    public void prompt() {
        System.out.print("PR-USER> ") ;
    }
    
}
