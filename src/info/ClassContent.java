package info;

import java.util.LinkedList;

import abs.frontend.ast.ASTNode;
import abs.frontend.ast.ClassDecl;
import abs.frontend.ast.MethodImpl;
import util.NodeUtil;

public class ClassContent {
	java.util.List<Method> methods;
	
	java.util.List<String> parameter;
	
	String name;
	
	public ClassContent(ClassDecl classDecl) {
		System.out.println(classDecl.toString());
		int firstKomma = classDecl.value.toString().indexOf(",");
		name = classDecl.value.toString().substring("ClassDecl".length() + 1,firstKomma);
		
		parameter = new LinkedList<String>();
		
		String[] vars = classDecl.value.toString().substring(firstKomma,classDecl.value.toString().indexOf("Opt()")).replaceAll(",,",",").replaceAll(",,",",").split(",");
		
		for(String var : vars)
			if(var.contains(" "))
				parameter.add(var);
		
		java.util.List<ASTNode<ASTNode>> list = new LinkedList<ASTNode<ASTNode>>();
		NodeUtil.recursiveFind_MethodImpl(classDecl, list);
		
		methods = new LinkedList<Method>();
		
		for (ASTNode<ASTNode> methodImpl : list) {
			methods.add(new Method((MethodImpl) methodImpl));
		}
		System.out.println(name);
		System.out.println(parameter);
	}
}
