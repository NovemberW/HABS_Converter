package info;

import org.eclipse.jdt.internal.compiler.ast.IfStatement;

import abs.frontend.ast.ASTNode;
import abs.frontend.ast.AwaitStmt;
import abs.frontend.ast.IfStmt;

public class LineStateFactory {
	
	public static LineState getLineState(ASTNode<ASTNode> node, String name) {
		if(node instanceof AwaitStmt)
			return new AwaitLineState(node, name);
		
		if(node instanceof IfStmt)
			return new IfLineState(node, name);
		
		return new LineState(node,name);
	}

	public static void connectStates(java.util.List<LineState> states) {
		for(int i = 0;i < states.size() - 1;i++) {
			states.get(i).addNext(states.get(i  + 1));
		}
	}
}
