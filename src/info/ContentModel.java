package info;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import abs.frontend.antlr.parser.ABSParserWrapper;
import abs.frontend.ast.ASTNode;
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

	public ContentModel(java.util.List<FlagInterface> availableFlags) {
		continousVariables = new LinkedList<ContinousVariable>();
		this.availableFlags = availableFlags;
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
			continousVariables.add(new ContinousVariable(fD.getName(),fileContent));
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (ContinousVariable cV : continousVariables) {
			sb.append(cV.toString());
			sb.append(System.lineSeparator());
		}

		return sb.toString();
	}

}
