package info;

import abs.frontend.ast.ASTNode;
import util.NodeUtil;
import util.StringTools;

public class ContinousVariable {
	private String name;

	private String initialValue;

	private String formula;

	public ContinousVariable(String DifferentialExp) {
		extractContinousVariable(DifferentialExp);
	}

	private void extractContinousVariable(String diffExp) {
		name = diffExp.substring(0, diffExp.indexOf("="));

		int z = diffExp.indexOf("(", diffExp.indexOf("(") + 1);
		int initFormula_Komma = StringTools.getClosingBracket(diffExp, z )+2;
		initialValue = diffExp.substring(z + 1, StringTools.getClosingBracket(diffExp, z));
		formula = diffExp.substring(initFormula_Komma,diffExp.length()-1);
//		System.out.println(formula);
//		System.out.println(diffExp);

	
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Name: ");
		sb.append(name);
		sb.append(" ini: ");
		sb.append(initialValue);
		sb.append(" formula: ");
		sb.append(formula);

		return sb.toString();
	}

}
