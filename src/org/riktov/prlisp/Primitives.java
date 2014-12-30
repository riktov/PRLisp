package org.riktov.prlisp ;
/*
class PrimitiveAdditionProcedure extends PrimitiveProcedure {
    public PrimitiveAdditionProcedure() {
        symbol = "+" ;
    }
    public LispObject apply(LispObject []argVals) {
        Number num0 = (Number)((Atom)argVals[0]).data() ;
        Number num1 = (Number)((Atom)argVals[1]).data() ;
        
        return new Atom(num0.floatValue() + num1.floatValue()) ;
    }
}
*/
/*
class PrimitiveSubtractionProcedure extends PrimitiveProcedure {
    public PrimitiveSubtractionProcedure() {
        symbol = "-" ;
    }
    public LispObject apply(LispObject []argVals) {
        NumberAtom n0 = (NumberAtom)argVals[0] ;
        NumberAtom n1 = (NumberAtom)argVals[1] ;
        
        return n0.subtract(n1) ;
    }
}
*/

class PrimitiveConsProcedure extends PrimitiveProcedure {
    public PrimitiveConsProcedure() {
        symbol = "cons" ;
    }
    public LispObject apply(LispObject []argVals) {
        return new ConsCell(argVals[0], argVals[1]) ;
    }
}

class PrimitiveCarProcedure extends PrimitiveProcedure {
    public PrimitiveCarProcedure() {
        symbol = "car" ;
    }
    public LispObject apply(LispObject []argVals) {
        return argVals[0].car() ;
    }
}
