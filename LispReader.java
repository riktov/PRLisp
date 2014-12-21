import java.io.* ;

public class LispReader {
    public LispObject read(String s) {
	Reader r = new BufferedReader(new StringReader(s)) ;
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

    public LispObject readFrom(StreamTokenizer st) throws IOException {
	//	Atom a = new Atom() ;
	//ConsCell c = new ConsCell() ;
	
	while(st.nextToken() != st.TT_EOF) {
	    System.out.println(st.toString()) ;
	    
	    if (st.ttype == '(') {
		//System.out.println("Read opening paren") ;
		return new ConsCell(readFrom(st), readFrom(st)) ;
	    } else if (st.ttype == ')') {
		//System.out.println("Read closing paren") ;
		return readFrom(st) ;
		//return c ;
	    } else if (st.ttype == '.') {//assumes enclosed within spaces
		//return new SymbolAtom(st.sval) ;
		return readFrom(st);
	    } else if (st.ttype == '"') {
		//System.out.println("Read quoted string") ;
		return new StringAtom(st.sval); 
	    } else if (st.ttype == st.TT_NUMBER) {
		return new Atom(st.nval) ;
	    } else if (st.ttype == st.TT_WORD) {
		return new SymbolAtom(st.sval) ;
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
