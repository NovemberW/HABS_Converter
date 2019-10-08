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

	private java.util.List<ContinousVariable> continousVariables;

	private java.util.List<FlagInterface> availableFlags;

	private java.util.List<AwaitGroups> awaitGroups;

	public ContentModel(java.util.List<FlagInterface> availableFlags) {
		continousVariables = new LinkedList<ContinousVariable>();
		this.availableFlags = availableFlags;
		this.awaitGroups = new LinkedList<AwaitGroups>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void extractInfo(String filename) throws FileNotFoundException, IOException {

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

		ASTNode<ASTNode> root = it.next();

		ASTNode<ASTNode> physicalBlock = NodeUtil.recursiveFind_PhysicalImpl(root);

		Iterator<FieldDecl> fieldIterator = ((PhysicalImpl) physicalBlock).getFieldList().iterator();

		while (fieldIterator.hasNext()) {
			FieldDecl fD = fieldIterator.next();
			continousVariables.add(new ContinousVariable(fD.getName(), fileContent));
		}

		java.util.List<String> segment = new LinkedList<String>();

		String[] lines = fileContent.split(System.lineSeparator());

		extractAwaits(segment, lines);

	}

	private void extractAwaits(java.util.List<String> segment, String[] lines) {
		for (int i = 0; i < lines.length; i++)
			lines[i] = lines[i].replaceAll(" ", " ").trim();

		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("await")) {
				String awaitStatement = lines[i];
				i++;// Skipping the line with await
				boolean end = false;
				for (; i < lines.length && !end; i++) {
					if (lines[i].contains("!")) {
						awaitGroups.add(new AwaitGroups(awaitStatement, segment));
						segment = new LinkedList<String>();
						end = true;
					}
					segment.add(lines[i]);
				}
			}
		}
	}

	public static void recursivePrinter(ASTNode node) {
		if (node == null)
			return;
		System.out.println(node.value);
		Iterator<ASTNode> it = node.astChildIterator();
//		if(!it.hasNext())
//			System.out.println(node.value);

		while (it.hasNext()) {
			recursivePrinter(it.next());
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (ContinousVariable cV : continousVariables) {
			sb.append(cV.toString());
			sb.append(System.lineSeparator());
		}

		sb.append("\n");

		for (AwaitGroups aG : awaitGroups) {
			sb.append(aG.toString());
			sb.append("\n");
		}

		return sb.toString();
	}

	public java.util.List<AwaitGroups> getAwaitGroups() {
		return awaitGroups;
	}

	public void setAwaitGroups(java.util.List<AwaitGroups> awaitGroups) {
		this.awaitGroups = awaitGroups;
	}

}
