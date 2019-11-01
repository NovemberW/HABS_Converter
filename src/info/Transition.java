package info;

import abs.frontend.ast.ASTNode;
import abs.frontend.ast.ExpressionStmt;
import util.StringTools;

public class Transition{
	
	private LineState target;
	
	private String guard;
	
	
	public Transition(String guard, LineState target) {
		this.target = target;
		this.guard = guard;
		
		if(this.guard == null)
			this.guard = "true";
	}
	
	public Transition(LineState target) {
		this.target = target;
		this.guard = "true";
	}

	public String getGuard() {
		return guard;
	}

	public void setGuard(String guard) {
		this.guard = guard;
	}

	public LineState getTarget() {
		return target;
	}

	public void setTarget(LineState target) {
		this.target = target;
	}
	
	public String getGuardValue() {
		return guard;
	}
}