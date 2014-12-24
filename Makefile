JAVAC=javac
JAVAC_FLAGS=-d build -cp build:$(CLASSPATH)
PACKAGEPATH=org/riktov/prlisp/

CLASSES=\
	$(PACKAGEPATH)PRLisp.class \
	$(PACKAGEPATH)LispObject.class \
	$(PACKAGEPATH)LispReader.class \
	$(PACKAGEPATH)NilAtomTest.class \
	$(PACKAGEPATH)StringAtomTest.class

all: $(CLASSES)

$(PACKAGEPATH)LispObject.class: src/LispObject.java
	$(JAVAC) $(JAVAC_FLAGS) src/LispObject.java

#Atom.class: Atom.java
#	$(JAVAC) $(JAVAC_FLAGS) Atom.java

#ConsCell.class: ConsCell.java
#	$(JAVAC) $(JAVAC_FLAGS) ConsCell.java

$(PACKAGEPATH)LispReader.class: src/LispReader.java
	$(JAVAC) $(JAVAC_FLAGS) src/LispReader.java

$(PACKAGEPATH)PRLisp.class: src/PRLisp.java
	$(JAVAC) $(JAVAC_FLAGS) src/PRLisp.java

$(PACKAGEPATH)NilAtomTest.class: src/NilAtomTest.java
	$(JAVAC) $(JAVAC_FLAGS) src/NilAtomTest.java

$(PACKAGEPATH)StringAtomTest.class: src/StringAtomTest.java
	$(JAVAC) $(JAVAC_FLAGS) src/StringAtomTest.java

.PHONY: clean

clean:
	rm *.class

run:
	java -cp .:build:$(CLASSPATH) org.riktov.prlisp.PRLisp

runtest:
	java -cp .:build:$(CLASSPATH) org.junit.runner.JUnitCore org.riktov.prlisp.NilAtomTest
	java -cp .:build:$(CLASSPATH) org.junit.runner.JUnitCore org.riktov.prlisp.StringAtomTest
