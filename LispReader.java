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
	return new NilObject() ;
    }

    public LispObject readFrom(StreamTokenizer st) throws IOException {
	Atom a = new Atom() ;
	ConsCell c = new ConsCell() ;
	
	while(st.nextToken() != st.TT_EOF) {
	    System.out.println(st.toString()) ;
	    
	    if (st.ttype == '(') {
		System.out.println("Read opening paren") ;
		c.setCar(readFrom(st)) ;
	    } else if (st.ttype == ')') {
		System.out.println("Read closing paren") ;
		return c ;
	    } else if (st.ttype == '.') {//assumes enclosed within spaces
		c.setCdr(readFrom(st));
	    } else if (st.ttype == '"') {
		System.out.println("Read quoted string") ;
		a = Atom.make(st.sval) ;
		return a; 
	    } else if (st.ttype == st.TT_NUMBER) {
	       	a = Atom.make(st.nval) ;
		return a ;
	    } else if (st.ttype == st.TT_WORD) {
		a = new SymbolAtom(st.sval) ;
		return a ;
	    } else {
		//a = Atom.make() ;
		return a ;
	    }

	}
	return a ;
    }

    public void prompt() {
	System.out.print("PR-USER> ") ;
    }

}
