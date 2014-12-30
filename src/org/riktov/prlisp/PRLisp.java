package org.riktov.prlisp ;

import java.util.Scanner;


/**
 * <h1>PRLisp</h1>
 * A Lisp interpreter.
 *
 * @author Paul Richter <riktov@freeshell.org>
 * @version 0.1
 */
public class PRLisp {
    public static void main(String args[]) {
        Environment env = new Environment() ;
        env.put("FOO", DataAtom.make(45)) ;
        env.put("BAR", DataAtom.make("Bargle")) ;

        LispObject o = env.lookup("FOO") ;
        System.out.println(o) ;
        
        PRLisp lisp = new PRLisp() ;
        lisp.repl(env) ;
        //     testFakeReader() ;
    }

    /**
     * Runs a REPL(Read-Eval-Print Loop)
     * @param env The initial environment
     * @return None
     */
    public void repl(Environment env) {
        System.out.println("Starting PRLisp") ;
        
        LispReader lr = new LispReader() ;
        lr.prompt() ;
        
        Scanner in = new Scanner(System.in);
        
        while(in.hasNextLine()) {
            String input = in.nextLine() ;
            LispObject o = lr.read(input) ;
            
            LispObject evaluated = o.eval(env) ;
            
            System.out.println(evaluated.toString()) ;

            //System.out.println(o.toString()) ;
            lr.prompt() ;
        }
    }

    
    /**
   private void testObjects () {
        Atom five = Atom.make(5) ;
        Atom ninetynine = Atom.make(99) ;
        Atom thirteen = Atom.make(13) ;
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
*/
}
