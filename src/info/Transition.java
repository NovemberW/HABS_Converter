package info;

import abs.frontend.ast.ASTNode;
import abs.frontend.ast.ExpressionStmt;
import util.StringTools;

public class Transition{
	
	private LineState target;
	
	private ASTNode<ASTNode> guard;
	
	
	public Transition(ASTNode<ASTNode> guard, LineState target) {
		this.target = target;
		this.guard = guard;
	}
	
	public Transition(LineState target) {
		this.target = target;
		this.guard = null;
	}

	public ASTNode<ASTNode> getGuard() {
		return guard;
	}

	public void setGuard(ASTNode<ASTNode> guard) {
		this.guard = guard;
	}

	public LineState getTarget() {
		return target;
	}

	public void setTarget(LineState target) {
		this.target = target;
	}
	
	public String getGuardValue() {
		if(guard == null)
			return "true";
		else
			return StringTools.getWithPrettyPrint(guard);
	}
}