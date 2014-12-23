package org.riktov.prlisp ;

import java.util.Scanner ;

public class PRLisp {
    public static void main(String args[]) {
        testFakeReader() ;
    }
    
    private void repl() {
        System.out.println("Starting PRLisp") ;
        
        LispReader r = new LispReader() ;
        r.prompt() ;
        
        Scanner in = new Scanner(System.in);
        
        while(in.hasNext()) {
            String input = in.next() ;
            LispObject o = r.read(input) ;
            System.out.println(o.toString()) ;
            r.prompt() ;
        }
    }

    static private void testFakeReader() {
        LispReader r = new LispReader() ;
        
        LispObject fiftyfour = r.read("(54)") ;
        System.out.println(fiftyfour.toString()) ;	
        
        LispObject hello = r.read("Hello") ;
        System.out.println(hello.toString()) ;
        
        LispObject nilly = r.read("nil") ;
        System.out.println(nilly.toString()) ;
        
        LispObject cons = r.read("(12 , (foo , (34, nil)))") ;
        System.out.println(cons.toString()) ;
        
        
        //	LispObject s = Atom.make(13) ;
        //System.out.println(s.toString()) ;
        
        
        //System.out.println(fiftyfour.data() + 1) ;
        
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
        
        ConsCell nilCdr = new ConsCell(thirteen, new NilAtom()) ;
        ConsCell list = new ConsCell(five, nilCdr) ;
        System.out.println(list.toString()) ;
        
        ConsCell list2 = new ConsCell(thirteen, list) ;
        System.out.println(list2.toString()) ;
        
    }    
}
