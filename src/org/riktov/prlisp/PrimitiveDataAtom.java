package org.riktov.prlisp;

/** A PrimitiveDataAtom is a DataAtom whose data member is a primitive
 * All Java primitives except boolean can be treated as numbers, and we deal with
 * Lisp booleans separately, so a Primitive data holds a single numerical value
 */
abstract class PrimitiveDataAtom extends DataAtom {
    abstract DataAtom subtract(PrimitiveDataAtom other) ;
	abstract DataAtom add(PrimitiveDataAtom other) ;
    public boolean isPrimitive() { return true ;}
}

class IntAtom extends PrimitiveDataAtom {
    int intData ;

    //constructor
    public IntAtom(int i) { this.intData = i ; }

    IntAtom addThisClass(IntAtom other) { return new IntAtom(this.intData + other.intData) ; }
    DataAtom add(PrimitiveDataAtom other) { return addThisClass((IntAtom)other) ; } 

    IntAtom subtractThisClass(IntAtom other) { return new IntAtom(this.intData - other.intData) ; }
    DataAtom subtract(PrimitiveDataAtom other) { return subtractThisClass((IntAtom)other) ; } 
    
    boolean equals(IntAtom other) {
    	return intData == other.intData ;
    }
    
    boolean equals(float f) { return intData == f ; }
}

class FloatAtom extends PrimitiveDataAtom {
    float floatData ;
    
    //constructor
    public FloatAtom(float f) { this.floatData = f ; }

    DataAtom add(PrimitiveDataAtom n) {
        return (FloatAtom)make(this.floatData + ((FloatAtom)n).floatData) ;
    } 
    DataAtom subtract(PrimitiveDataAtom n) {
        return (FloatAtom)make(this.floatData - ((FloatAtom)n).floatData) ;
    }    
    
    boolean equals(float f) { return floatData == f ; }
    public String toString() { return Float.toString(floatData) ; }
}

class DoubleAtom extends PrimitiveDataAtom {
    double doubleData ;
    
    //constructor
    public DoubleAtom(double d) { this.doubleData = d ; }

    DataAtom add(PrimitiveDataAtom n) {
        return (DoubleAtom)make(this.doubleData + ((DoubleAtom)n).doubleData) ;
    } 
    DataAtom subtract(PrimitiveDataAtom n) {
        return (DoubleAtom)make(this.doubleData - ((DoubleAtom)n).doubleData) ;
    }    
    
    boolean equals(double d) { return doubleData == d ; }
    public String toString() { return Double.toString(doubleData) ; }
}