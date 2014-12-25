JAVAC=javac
JAVAC_FLAGS=-d build
JAVAC_CLASSPATH=build:$(CLASSPATH)
PACKAGEPATH=build/org/riktov/prlisp/

CLASSES=\
	$(PACKAGEPATH)LispObject.class \
	$(PACKAGEPATH)LispReader.class \
	$(PACKAGEPATH)Environment.class \
	$(PACKAGEPATH)PRLisp.class \
	$(PACKAGEPATH)Primitives.class \
	$(PACKAGEPATH)NilAtomTest.class \
	$(PACKAGEPATH)StringAtomTest.class \
	$(PACKAGEPATH)ConsCellTest.class

all: $(CLASSES)

#LispObject and Environment are circularly dependent, so they must be compiled simultaneously
$(PACKAGEPATH)LispObject.class: src/LispObject.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/LispObject.java src/Environment.java src/Primitives.java

$(PACKAGEPATH)ConsCell.class: src/LispObject.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/LispObject.java src/Environment.java

$(PACKAGEPATH)Environment.class: src/Environment.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/Environment.java

$(PACKAGEPATH)Primitives.class: src/Primitives.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/Primitives.java

#Atom.class: Atom.java
#	$(JAVAC) $(JAVAC_FLAGS) Atom.java

#ConsCell.class: ConsCell.java
#	$(JAVAC) $(JAVAC_FLAGS) ConsCell.java

$(PACKAGEPATH)LispReader.class: src/LispReader.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/LispReader.java

$(PACKAGEPATH)PRLisp.class: src/PRLisp.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/PRLisp.java

$(PACKAGEPATH)NilAtomTest.class: src/NilAtomTest.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/NilAtomTest.java

$(PACKAGEPATH)StringAtomTest.class: src/StringAtomTest.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/StringAtomTest.java

$(PACKAGEPATH)ConsCellTest.class: src/ConsCellTest.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/ConsCellTest.java

.PHONY: clean

clean:
	rm $(PACKAGEPATH)*.class

run:
	java -cp .:build:$(CLASSPATH) org.riktov.prlisp.PRLisp

runtest:
	java -cp .:build:$(CLASSPATH) org.junit.runner.JUnitCore org.riktov.prlisp.NilAtomTest
	java -cp .:build:$(CLASSPATH) org.junit.runner.JUnitCore org.riktov.prlisp.StringAtomTest
	java -cp .:build:$(CLASSPATH) org.junit.runner.JUnitCore org.riktov.prlisp.ConsCellTest
