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
	}

	@Test
	public void classTest() {
		java.util.List<ASTNode<ASTNode>> classes = new LinkedList<ASTNode<ASTNode>>();
		NodeUtil.recursiveFind_ClassDecl(root, classes);
		
		ClassContent toTest = new ClassContent((ClassDecl) classes.get(0));
		
		assertEquals(1,toTest.getMethods().size());
		assertEquals("Ball",toTest.getName());
		
		assertTrue(toTest.getPhysicalBlock().size() == 4);
		//3 in the file + 1 for global time
	}
}