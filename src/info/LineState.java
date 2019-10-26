package info;

import java.io.PrintStream;
import java.util.LinkedList;

import abs.frontend.ast.ASTNode;

public class LineState {

	String name;

	ASTNode<ASTNode> statement;

	java.util.List<LineState> nexts;


	public java.util.List<LineState> getNext() {
		return nexts;
	}

	public void addNext(LineState next) {
		nexts.add(next);
	}
	public void removeNext(LineState next) {
		nexts.remove(next);
	}

	public LineState(ASTNode<ASTNode> line, String name) {
		this.statement = line;
		this.setName(name);
		
		nexts = new LinkedList<LineState>();
	}

	public ASTNode<ASTNode> getStatement() {
		return statement;
	}

	public void setStatement(ASTNode<ASTNode> statement) {
		this.statement = statement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void extend() {

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("STATE: ");
		sb.append(name);
		sb.append("\n");
		sb.append("Statement: ");
		sb.append("\n");
		sb.append(statement);
		sb.append("\n");
		
		for(LineState state : nexts) {
			sb.append("Next: ");
			sb.append(state.getName());
			sb.append("\n");
		}
		
		return sb.toString();
	}
	public void traversePrint(PrintStream drain) {
		drain.println(this);
		drain.print("\n");
		for(LineState state : nexts)
			state.traversePrint(drain);
	}
}
