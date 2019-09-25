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
import abs.frontend.ast.List;
import abs.frontend.ast.Model;
import util.NodeUtil;
public class ContentModel {
	
	private java.util.List<ContinousVariable> continousVariables;
	
	
	public ContentModel() {
		continousVariables = new LinkedList<ContinousVariable>();
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public void extractInfo(String filename) throws FileNotFoundException, IOException {

		ABSParserWrapper parser = new ABSParserWrapper(new File(filename), true, true);
		CompilationUnit cUnit = parser.parse(new FileReader(filename));

		List l = new List<>();
		l.add(cUnit);
		Model model = new Model(l);
		
		Iterator<ASTNode> it = model.astChildIterator();
				
		ASTNode<ASTNode> root= it.next();
		
		String fullPhysical = NodeUtil.recursiveFind_PhysicalImpl(root).toString();
		
		String[] parts = fullPhysical.split("Rat");
				
		for(String s : parts) {
			if(s.contains("=")) {
				String reduced = (String) s.subSequence(0, s.length()-1);
				
//				System.out.println(reduced.trim());
				ContinousVariable c = new ContinousVariable(reduced.trim());
				System.out.println(c.toString());
				continousVariables.add(c);
			}
		}
	}



}
