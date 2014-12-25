package org.riktov.prlisp ;

class PrimitiveAdditionProcedure extends PrimitiveProcedure {
    public PrimitiveAdditionProcedure() {
        symbol = "+" ;
    }
    public LispObject apply(Atom []argVals) {
        return new Atom((int)argVals[0].data() + (int)argVals[1].data()) ;
    }
}

class PrimitiveSubtractionProcedure extends PrimitiveProcedure {
    public PrimitiveSubtractionProcedure() {
        symbol = "-" ;
    }
    public LispObject apply(Atom []argVals) {
        return new Atom((int)argVals[0].data() - (int)argVals[1].data()) ;
    }
}

class PrimitiveConsProcedure extends PrimitiveProcedure {
    public PrimitiveConsProcedure() {
        symbol = "cons" ;
    }
    public LispObject apply(Atom []argVals) {
        return new ConsCell(argVals[0], argVals[1]) ;
    }
}

