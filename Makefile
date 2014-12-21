CLASSES=PRLisp.class LispObject.class Atom.class ConsCell.class LispReader.class

all: $(CLASSES)

PRLisp.class: PRLisp.java
	javac PRLisp.java

LispObject.class: LispObject.java
	javac LispObject.java

Atom.class: Atom.java
	javac Atom.java

ConsCell.class: ConsCell.java
	javac ConsCell.java

LispReader.class: LispReader.java
	javac LispReader.java
.PHONY: clean

clean:
	rm *.class
