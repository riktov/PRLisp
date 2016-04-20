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
}

class FloatAtom extends PrimitiveAtom {
    float floatData ;    
    //constructor
    public FloatAtom(float f) { this.floatData = f ; }

}

class DoubleAtom extends PrimitiveAtom {
    double doubleData ;
    //constructor
    public DoubleAtom(double d) { this.doubleData = d ; }
}