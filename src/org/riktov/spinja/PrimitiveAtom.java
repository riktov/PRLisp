package org.riktov.spinja;

/** A PrimitiveAtom is a DataAtom whose data member is a primitive
 * All Java primitives except boolean can be treated as numbers, and we deal with
 * Lisp booleans separately, so a Primitive data holds a single numerical value
 */
abstract class PrimitiveAtom extends DataAtom {
   //abstract DataAtom subtract(PrimitiveDataAtom other) ;
	//abstract DataAtom add(PrimitiveDataAtom other) ;
    public boolean isPrimitive() { return true ;}
}

/**
 * A primitive atom which holds its value in an int element
 * @author riktov@freeshell.org (Paul Richter)
 *
 */
class IntAtom extends PrimitiveAtom {
    int intData ;

    //constructor
    public IntAtom(int i) { this.intData = i ; }

    IntAtom addThisClass(IntAtom other) { return new IntAtom(this.intData + other.intData) ; }
    DataAtom add(PrimitiveAtom other) { return addThisClass((IntAtom)other) ; } 

    IntAtom subtractThisClass(IntAtom other) { return new IntAtom(this.intData - other.intData) ; }
    DataAtom subtract(PrimitiveAtom other) { return subtractThisClass((IntAtom)other) ; } 
    
    boolean equals(IntAtom other) {
    	return intData == other.intData ;
    }
    
    boolean equals(float f) { return intData == f ; }
}

class FloatAtom extends PrimitiveAtom {
    float floatData ;
    
    //constructor
    public FloatAtom(float f) { this.floatData = f ; }

    DataAtom add(PrimitiveAtom n) {
        return (FloatAtom)make(this.floatData + ((FloatAtom)n).floatData) ;
    } 
    DataAtom subtract(PrimitiveAtom n) {
        return (FloatAtom)make(this.floatData - ((FloatAtom)n).floatData) ;
    }    
    
    boolean equals(float f) { return floatData == f ; }
    @Override public String toString() { return Float.toString(floatData) ; }
}

class DoubleAtom extends PrimitiveAtom {
    double doubleData ;
    
    //constructor
    public DoubleAtom(double d) { this.doubleData = d ; }

    DataAtom add(PrimitiveAtom n) {
        return (DoubleAtom)make(this.doubleData + ((DoubleAtom)n).doubleData) ;
    } 
    DataAtom subtract(PrimitiveAtom n) {
        return (DoubleAtom)make(this.doubleData - ((DoubleAtom)n).doubleData) ;
    }    
    
    boolean equals(double d) { return doubleData == d ; }
    @Override public String toString() { return Double.toString(doubleData) ; }
}