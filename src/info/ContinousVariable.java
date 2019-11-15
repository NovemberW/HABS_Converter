package info;

import java.util.Iterator;

import abs.frontend.ast.ASTNode;
import util.NodeUtil;
import util.StringTools;

public class ContinousVariable {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	private String initialValue;

	private String formula;

	private ASTNode<ASTNode> physicalBlock;

	public ContinousVariable(ASTNode<ASTNode> physicalBlock) {
		this.physicalBlock = physicalBlock;
		extractContinousVariable();

	}

	public ContinousVariable(String globalTime) {
		name = globalTime;

		initialValue = "0";
		//System.out.println(help.getChild(1).getChild(0) + "'");
		formula = globalTime + " == 0";
	}

	private void extractContinousVariable() {
		ASTNode<ASTNode> var = physicalBlock.getChild(1);
		String full = physicalBlock.value.toString();
		name = full.substring(full.indexOf("Real ") + 5, full.indexOf("=")).trim();

		ASTNode<ASTNode> help = var.getChild(0);

		initialValue = StringTools.getWithPrettyPrint(help.getChild(0));
		//System.out.println(help.getChild(1).getChild(0) + "'");
		formula = help.getChild(1).getChild(0) + "' == " + StringTools.getWithPrettyPrint(help.getChild(2));

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
