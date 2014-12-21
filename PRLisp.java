import java.util.Scanner ;

public class PRLisp {
    public static void main(String args[]) {
	System.out.println("Starting PRLisp") ;
	
	LispReader r = new LispReader() ;
	r.prompt() ;

	Scanner in = new Scanner(System.in);
	while(in.hasNext()) {
	    String input = in.next() ;
	    LispObject o = r.read(input) ;
	    System.out.println(o.toString()) ;
	}

	
    }
    private void testFakeReader() {
	LispReader r = new LispReader() ;

    	LispObject fiftyfour = r.read("(54)") ;
	LispObject hello = r.read("Hello") ;
	
	//	LispObject s = Atom.make(13) ;
	//System.out.println(s.toString()) ;
	
	System.out.println(fiftyfour.toString()) ;
	//System.out.println(fiftyfour.data() + 1) ;
	System.out.println(hello.toString()) ;
    }
    
    private void testObjects () {
	Atom five = new Atom(5) ;
	Atom ninetynine = new Atom(99) ;
	Atom thirteen = new Atom(13) ;
	Atom hello = new SymbolAtom("Hello") ;
	
	//five.print() ;
	System.out.println(five.toString()) ;
	System.out.println(hello.toString()) ;

	ConsCell consFiveHello = new ConsCell(five, hello) ;
	System.out.println(consFiveHello.toString()) ;
	
	ConsCell listNinetynine = new ConsCell(ninetynine, consFiveHello) ;
	System.out.println(listNinetynine.toString()) ;
	
	ConsCell f = new ConsCell(thirteen, listNinetynine) ;
	System.out.println(f.toString()) ;

	System.out.println(f.cdr().toString()) ;

	ConsCell nilCdr = new ConsCell(thirteen, new NilObject()) ;
	ConsCell list = new ConsCell(five, nilCdr) ;
	System.out.println(list.toString()) ;

	ConsCell list2 = new ConsCell(thirteen, list) ;
	System.out.println(list2.toString()) ;
	
    }    
}
