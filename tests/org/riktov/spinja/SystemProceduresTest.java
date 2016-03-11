package org.riktov.spinja;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;
import org.riktov.spinja.CompoundProcedure;
import org.riktov.spinja.ConsCell;
import org.riktov.spinja.Environment;
import org.riktov.spinja.LispObject;
import org.riktov.spinja.ObjectAtom;
import org.riktov.spinja.StringAtom;
import org.riktov.spinja.SymbolAtom;

public class SystemProceduresTest {
	private Environment env;
	@Before
	public void setUp() {
		env = new Environment();
		env.initialize();
	}

	@Test
	public void testLoad() {
		//first write the temporary file
		Writer writer = null ;
		
		String sourceFilePath = "testLoad.lisp" ;
		
		//create the test source file
		try {
			writer = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(sourceFilePath), "utf-8")) ;
			writer.write("(define foo 37)\n");
			writer.write("(define bar 95)\n");
		} catch (IOException except){
			
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}
		
		//build a form
		LispObject[] forms = new LispObject[] {
				new SymbolAtom("load"),
				new StringAtom(sourceFilePath)
		} ;
		
		//convert to a list
		ConsCell c = new ConsCell(forms) ;
		
		//evaluate it
		System.out.println("testLoad() evaluating: " + c.toString()) ;
		c.eval(env) ;
	
		//check that the environment contains the bindings as stated in the source file.
		
		System.out.println("testLoad(): got " + env.get("BAR")) ;

		assertTrue(env.get("BAR").toString().equals("95"));
	}

	@Test
	public void testLoadMultiline() {
		//first write the temporary file
		Writer writer = null ;
		
		String sourceFilePath = "testLoad2.lisp" ;
		
		//create the test source file
		try {
			writer = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(sourceFilePath), "utf-8")) ;
			writer.write("(define (add-thirteen x)\n");
			writer.write("(+ x 13))\n");
		} catch (IOException except){
			
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}
		
		//build a form
		LispObject[] forms = new LispObject[] {
				new SymbolAtom("load"),
				new StringAtom(sourceFilePath)
		} ;
		
		//convert to a list
		ConsCell c = new ConsCell(forms) ;
		
		//evaluate it
		System.out.println("testLoad() evaluating: " + c.toString()) ;
		c.eval(env) ;
	
		//check that the environment contains the bindings as stated in the source file.
		
		CompoundProcedure theProc = (CompoundProcedure) env.get("ADD-THIRTEEN") ;

		forms = new LispObject[] { new ObjectAtom(4) } ;
		c = new ConsCell(forms) ;
		
		LispObject result = theProc.apply(c) ;
		
		assertTrue(result.toString().equals("17"));
	}
}
