CLASSES=PRLisp.class LispObject.class Atom.class ConsCell.class LispReader.class
JAVAC=javac
JAVAC_FLAGS=-d .


all: $(CLASSES)

LispObject.class: LispObject.java
	$(JAVAC) $(JAVAC_FLAGS) LispObject.java

#Atom.class: Atom.java
#	$(JAVAC) $(JAVAC_FLAGS) Atom.java

#ConsCell.class: ConsCell.java
#	$(JAVAC) $(JAVAC_FLAGS) ConsCell.java

LispReader.class: LispReader.java
	$(JAVAC) $(JAVAC_FLAGS) LispReader.java

PRLisp.class: PRLisp.java
	$(JAVAC) $(JAVAC_FLAGS) PRLisp.java

.PHONY: clean

clean:
	rm *.class
