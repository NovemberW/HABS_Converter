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

/**
 * 
 */
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

		ASTNode<ASTNode> root = it.next();// Base of ast

		java.util.List<ASTNode<ASTNode>> list = new LinkedList<ASTNode<ASTNode>>();
		NodeUtil.recursiveFind_ClassDecl(root, list);

		for (ASTNode<ASTNode> classDecl : list) {
			classes.add(new ClassContent((ClassDecl) classDecl));
		}

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

	public String getXML() {
		StringBuffer sb = new StringBuffer();

		for (ClassContent c : classes)
			sb.append(c.getXMLString());

		return sb.toString();
	}

	public String getCFG() {
		StringBuffer sb = new StringBuffer();

		sb.append(makeCFGString());

		return sb.toString();
	}

	public String makeCFGString() {
		String defaultConfig = "scenario = supp\n" + "directions = oct\n"
				+ "set-aggregation = chull\n" + "sampling-time = 0.001\n"
				+ "time-horizon = 25\n" + "iter-max = 1000\n"
				+ "output-format = GEN\n" + "rel-err = 1.0E-12\n"
				+ "abs-err = 1.0E-13\n" + "flowpipe-tolerance = 0.001\n"
				+ "system = sys1\n";
		StringBuffer sb = new StringBuffer();

		sb.append(defaultConfig);

		// TODO accommodate for more classes -> Variables
		ClassContent c = classes.get(0);
		
		java.util.List<ContinousVariable> physicalBlock = c.getPhysicalBlock();
		
		sb.append("initially =");
		sb.append("\"");
		for(ContinousVariable var : physicalBlock) {
			sb.append(var.getName());
			sb.append("==");
			sb.append(var.getInitialValue());
			sb.append(" & ");
		}
		sb.append(" loc(main)==_0");//main and _0 are default names in HABS_Converter
		sb.append("\"");
		sb.append("\n");
		
		sb.append("output-variables = \"");
		String x, y;
		int length = physicalBlock.size();
		y = physicalBlock.get(length - 2).getName();
		x = physicalBlock.get(length - 1).getName();//last one is global Time
		sb.append(x + ", " + y);
		sb.append("\"");

		return sb.toString();
	}
}