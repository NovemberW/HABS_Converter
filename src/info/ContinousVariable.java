package info;

import abs.frontend.ast.ASTNode;
import util.NodeUtil;
import util.StringTools;

public class ContinousVariable {
	private String name;

	private String initialValue;

	private String formula;

	String filecontent;

	public ContinousVariable(String var, String filecontent) {
		this.filecontent = filecontent;
		name = var;
		extractContinousVariable();

	}

	private void extractContinousVariable() {
		

		String[] lines = filecontent.split(System.lineSeparator());

		for (int i = 0; i < lines.length; i++)
			lines[i] = lines[i].replaceAll(" ", " ").trim();

		for (int i = 0; i < lines.length; i++)
			if (lines[i].startsWith("Rat " + name)) {
				String[] exp = lines[i].substring(("Rat " + name).length() + 2).trim().split(":");
				initialValue = exp[0];
				if(exp.length == 2)
					formula = StringTools.removeSemicolon(exp[1]);
				else
					formula = "";
				break;
			}
//			System.out.println(lines[i]);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Name: ");
		sb.append(name);
		sb.append(" init: ");
		sb.append(initialValue);
		sb.append(" formula: ");
		sb.append(formula);

		return sb.toString();
	}

}
