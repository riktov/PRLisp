JAVAC=javac
JAVAC_FLAGS=-d build -sourcepath src
JAVAC_CLASSPATH=build:$(CLASSPATH)
PACKAGEPATH=build/classes/org/riktov/prlisp/

CLASSES=\
	$(PACKAGEPATH)LispObject.class \
	$(PACKAGEPATH)LispReader.class \
	$(PACKAGEPATH)Environment.class \
	$(PACKAGEPATH)Procedure.class \
	$(PACKAGEPATH)PRLisp.class \
	$(PACKAGEPATH)Primitives.class

TESTCLASSES=\
	$(PACKAGEPATH)NilAtomTest.class \
	$(PACKAGEPATH)StringAtomTest.class \
	$(PACKAGEPATH)ConsCellTest.class

all: $(CLASSES)
test: $(TESTCLASSES)

#LispObject and Environment are circularly dependent, so they must be compiled simultaneously
$(PACKAGEPATH)LispObject.class: src/LispObject.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/LispObject.java src/Environment.java

#$(PACKAGEPATH)ConsCell.class: src/ConsCell.java
#	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/ConsCell.java src/Environment.java

$(PACKAGEPATH)Environment.class: src/Environment.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/Environment.java src/LispObject.java

$(PACKAGEPATH)Procedure.class: src/Procedure.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/Procedure.java src/LispObject.java

$(PACKAGEPATH)Primitives.class: src/Primitives.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/Primitives.java src/Procedure.java src/LispObject.java

#Atom.class: Atom.java
#	$(JAVAC) $(JAVAC_FLAGS) Atom.java

#ConsCell.class: ConsCell.java
#	$(JAVAC) $(JAVAC_FLAGS) ConsCell.java

$(PACKAGEPATH)LispReader.class: src/LispReader.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/LispReader.java

$(PACKAGEPATH)PRLisp.class: src/PRLisp.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/PRLisp.java

#Test classes
$(PACKAGEPATH)NilAtomTest.class: src/test/NilAtomTest.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/test/NilAtomTest.java

$(PACKAGEPATH)StringAtomTest.class: src/test/StringAtomTest.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/tets/StringAtomTest.java

$(PACKAGEPATH)ConsCellTest.class: src/test/ConsCellTest.java
	$(JAVAC) -cp $(JAVAC_CLASSPATH) $(JAVAC_FLAGS) src/test/ConsCellTest.java

.PHONY: clean

clean:
	rm $(PACKAGEPATH)*.class

run:
	java -cp .:build:$(CLASSPATH) org.riktov.prlisp.PRLisp

runtest:
	java -cp .:build:$(CLASSPATH) org.junit.runner.JUnitCore org.riktov.prlisp.NilAtomTest
	java -cp .:build:$(CLASSPATH) org.junit.runner.JUnitCore org.riktov.prlisp.StringAtomTest
	java -cp .:build:$(CLASSPATH) org.junit.runner.JUnitCore org.riktov.prlisp.ConsCellTest
# java -cp .:build/classes:$CLASSPATH org.junit.runner.JUnitCore org.riktov.prlisp.NilAtomTest
