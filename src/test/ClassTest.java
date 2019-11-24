package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import abs.frontend.antlr.parser.ABSParserWrapper;
import abs.frontend.ast.ASTNode;
import abs.frontend.ast.ClassDecl;
import abs.frontend.ast.CompilationUnit;
import abs.frontend.ast.List;
import abs.frontend.ast.MethodImpl;
import abs.frontend.ast.Model;
import info.ClassContent;
import info.LineState;
import info.Method;
import util.NodeUtil;
import util.StringTools;

public class ClassTest {

	public ASTNode<ASTNode> root;

	public ClassContent toTest;

	@Before
	public void startup() throws IOException {
		String filename = "/home/nicholas/Schreibtisch/Uni/BA/HABS_Converter/src/test/test.abs";

		File testInput = new File(filename);
		FileReader reader = new FileReader(testInput);

		ABSParserWrapper parser = new ABSParserWrapper(testInput, true, true);
		CompilationUnit cUnit = parser.parse(reader);

		List l = new List<>();
		l.add(cUnit);
		Model model = new Model(l);

		Iterator<ASTNode> it = model.astChildIterator();

		root = it.next();// Base of ast

		java.util.List<ASTNode<ASTNode>> classes = new LinkedList<ASTNode<ASTNode>>();
		NodeUtil.recursiveFind_ClassDecl(root, classes);

		toTest = new ClassContent((ClassDecl) classes.get(0));
	}

	@Test
	public void classTest() {
		assertEquals(1, toTest.getMethods().size());
		assertEquals("Ball", toTest.getName());

		assertTrue(toTest.getPhysicalBlock().size() == 4);
		// 3 in the file + 1 for global time
	}

	@Test
	public void classXMLTests() {
		String classXMLfirst = toTest.getUpperParameterBlock();
		String classXMLsecond = toTest.getStateMachineXML();
		String classXMLthird = toTest.getLowerParameterBlock();

		assertEquals(4, countInString(classXMLfirst,"param name"));
		assertTrue(classXMLfirst.contains("\"a\""));
		assertTrue(classXMLfirst.contains("\"v\""));
		assertTrue(classXMLfirst.contains("\"x\""));
		assertTrue(classXMLfirst.contains("\"global_time\""));
		assertEquals(8, countInString(classXMLsecond,"/location"));
		assertEquals(8, countInString(classXMLsecond,"/transition"));
		assertTrue(classXMLthird.contains("\"a\""));
		assertTrue(classXMLthird.contains("\"v\""));
		assertTrue(classXMLthird.contains("\"x\""));
		assertTrue(classXMLthird.contains("\"global_time\""));
		assertEquals(4,countInString(classXMLthird,"/map"));
		
		//Note: Further testing of the correct xml structure 
		//do not give much trust in the correctness so further tests
		//are carried out by manually checking that the produced files
		//can be used by the tool chain.
	}

	public static int countInString(String in, String toCount) {
		return (in.length() - in.replaceAll(toCount,"").length())/toCount.length();
	}
}