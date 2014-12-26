package org.riktov.prlisp ;

class PrimitiveAdditionProcedure extends PrimitiveProcedure {
    public PrimitiveAdditionProcedure() {
        symbol = "+" ;
    }
    public LispObject apply(LispObject []argVals) {
        Atom arg0 = (Atom)argVals[0] ;
        Atom arg1 = (Atom)argVals[1] ;
        
        return new Atom((int)arg0.data() + (int)arg1.data()) ;
    }
}

class PrimitiveSubtractionProcedure extends PrimitiveProcedure {
    public PrimitiveSubtractionProcedure() {
        symbol = "-" ;
    }
    public LispObject apply(LispObject []argVals) {
        Atom arg0 = (Atom)argVals[0] ;
        Atom arg1 = (Atom)argVals[1] ;
        
        return new Atom((int)arg0.data() - (int)arg1.data()) ;
    }
}

class PrimitiveConsProcedure extends PrimitiveProcedure {
    public PrimitiveConsProcedure() {
        symbol = "cons" ;
    }
    public LispObject apply(LispObject []argVals) {
        return new ConsCell(argVals[0], argVals[1]) ;
    }
}

