package info;

import java.util.Iterator;
import java.util.LinkedList;

import abs.frontend.ast.ASTNode;
import abs.frontend.ast.ClassDecl;
import abs.frontend.ast.MethodImpl;
import util.NodeUtil;

public class ClassContent {
	private java.util.List<Method> methods;

	private java.util.List<String> parameter;

	private String name;

	private java.util.List<ContinousVariable> physicalBlock;

	public ClassContent(ClassDecl classDecl) {
		int firstKomma = classDecl.value.toString().indexOf(",");
		name = classDecl.value.toString().substring("ClassDecl".length() + 1, firstKomma);

		parameter = new LinkedList<String>();

		this.physicalBlock = new LinkedList<ContinousVariable>();

		ASTNode<ASTNode> physicalBlock = NodeUtil.recursiveFind_PhysicalImpl(classDecl);
		Iterator<ASTNode> it = physicalBlock.getChild(1).astChildIterator();

		while (it.hasNext())
			this.physicalBlock.add(new ContinousVariable(it.next()));

		StringBuffer sb = new StringBuffer();

		for (int i = 0;i < this.physicalBlock.size() - 1;i++){
			sb.append(this.physicalBlock.get(i).getFormula());
			sb.append(" &amp; ");
		}
		sb.append(this.physicalBlock.get(this.physicalBlock.size() - 1).getFormula());

		String flow = sb.toString().replaceAll("this.","");
		
		java.util.List<ASTNode<ASTNode>> list = new LinkedList<ASTNode<ASTNode>>();
		NodeUtil.recursiveFind_MethodImpl(classDecl, list);

		methods = new LinkedList<Method>();

		for (ASTNode<ASTNode> methodImpl : list) {
			methods.add(new Method((MethodImpl) methodImpl,flow));
		}
	}

	public java.util.List<Method> getMethods() {
		return methods;
	}

	public void setMethods(java.util.List<Method> methods) {
		this.methods = methods;
	}

	public java.util.List<String> getParameter() {
		return parameter;
	}

	public void setParameter(java.util.List<String> parameter) {
		this.parameter = parameter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (Method m : methods)
			sb.append(m.toString());

		return sb.toString();
	}
}
