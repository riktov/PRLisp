package org.riktov.prlisp ;


//import java.util.List ;
//import java.util.ArrayList ;

abstract class LispProcedure extends LispObject {
    abstract public LispObject apply(LispObject []argVals) ;
}

abstract class PrimitiveProcedure extends LispProcedure {
    String symbol ;
    public String symbol() { return this.symbol ; }
}

abstract class PrimitiveNumericalProcedure extends PrimitiveProcedure {
	Number[] numericalArgs(LispObject[] args) {
		Number[] numArgs = new Number[args.length] ; 
		int i ;
		for (i = 0 ; i < args.length ; i++) {
			ObjectDataAtom nda = (ObjectDataAtom)args[i] ;
			numArgs[i] = (Number)(nda.data) ;
		}
		return numArgs ;
	}
}
abstract class SpecialOperation extends LispProcedure {
    private String formalParams[] ;
    private Environment env ;    
}

class CompoundProcedure extends LispProcedure {
    private LispObject body ;
    private String formalParams[] ;
    private Environment env ;
    
    //constructors
    public CompoundProcedure(LispObject body, String []params) {
        this.body = body ;
        this.formalParams = params ;
    }
    
    //implementation of LispObject
    public String toString() { return "#<FUNCTION :LAMBDA>" ;}

    /**
     * APPLY
     * Evaluate the procedure body in an environment created by 
     * extending the procedure's initial environment with the argument
     * bindings.
     * @param argVals This is an array of Atoms (evaluated) that is passed to the method.
     * @return LispObject This is either an Atom or a list
     */

   public LispObject apply(LispObject []argVals) {
        ChildEnvironment newEnv = new ChildEnvironment(env) ;
        int i ;
        for(i = 0 ; i < argVals.length ; i++) {
            newEnv.intern(formalParams[i], argVals[i]) ;
        }
        return body.eval(newEnv) ;
    }
}

/***
   ____
  /    \
 / EVAL|\
 |  __/ |
 | /    |
 \|APPLY/
  \____/      

    ______
  _/_     \_
 /    \     \
/      |     \
| EVAL |     |
|     /      |
|    | APPLY |
\    |       /
 \_   \_____/
   \______/      
*/
