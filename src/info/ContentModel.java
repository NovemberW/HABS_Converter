package info;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import abs.frontend.antlr.parser.ABSParserWrapper;
import abs.frontend.ast.ASTNode;
import abs.frontend.ast.AwaitStmt;
import abs.frontend.ast.ClassDecl;
import abs.frontend.ast.CompilationUnit;
import abs.frontend.ast.FieldDecl;
import abs.frontend.ast.List;
import abs.frontend.ast.Model;
import abs.frontend.ast.PhysicalImpl;
import flags.FileFlag;
import flags.FlagInterface;
import util.NodeUtil;
import util.StringTools;

public class ContentModel {


	private java.util.List<FlagInterface> availableFlags;

	
	private java.util.List<ClassContent> classes;

	public ContentModel(java.util.List<FlagInterface> availableFlags) {
		this.availableFlags = availableFlags;
		this.classes = new LinkedList<ClassContent>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void extract(String filename) throws FileNotFoundException, IOException {

		String fileContent = "";
		for (FlagInterface flag : availableFlags)
			if (flag instanceof FileFlag)
				fileContent = ((FileFlag) flag).getFileContent();

		ABSParserWrapper parser = new ABSParserWrapper(new File(filename), true, true);
		CompilationUnit cUnit = parser.parse(new FileReader(filename));

		List l = new List<>();
		l.add(cUnit);
		Model model = new Model(l);

		Iterator<ASTNode> it = model.astChildIterator();

		ASTNode<ASTNode> root = it.next();//Base of ast

		
		java.util.List<ASTNode<ASTNode>> list = new LinkedList<ASTNode<ASTNode>>();
		NodeUtil.recursiveFind_ClassDecl(root, list);
		
		for (ASTNode<ASTNode> classDecl : list) {
			classes.add(new ClassContent((ClassDecl) classDecl));
		}
		
	}

	private void extractFunctions(String[] lines) {
		
	}


	public static void recursivePrinter(ASTNode node) {
		if (node == null)
			return;
		System.out.println(node.value);
		Iterator<ASTNode> it = node.astChildIterator();

		while (it.hasNext()) {
			recursivePrinter(it.next());
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for(ClassContent c : classes)
			sb.append(c.toString());

		return sb.toString();
	}
}