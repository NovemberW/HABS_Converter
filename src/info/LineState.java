package info;

import java.io.PrintStream;
import java.util.LinkedList;

import abs.frontend.ast.ASTNode;
import util.StringTools;

public class LineState implements XMLPrinter{

	String name;

	ASTNode<ASTNode> statement;

	java.util.List<Transition> nexts;
	
	String text;


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public java.util.List<Transition> getNext() {
		return nexts;
	}

	public void addNext(LineState next) {
		nexts.add(new Transition(next));
	}
	
	public void addNext(Transition next) {
		nexts.add(next);
	}
	
	public void removeNext(LineState next) {
		for(Transition trans : nexts)
			if(trans.getTarget() == next)
				nexts.remove(trans);
	}

	public LineState(ASTNode<ASTNode> line, String name) {
		this.statement = line;
		if(line != null)
			this.text = StringTools.getWithPrettyPrint(line);
		else
			this.text = name;
		
		this.setName(name);
		
		nexts = new LinkedList<Transition>();
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
		sb.append("Text: ");
		sb.append(text);
		sb.append("\n");
		
		for(Transition next : nexts) {
			sb.append("\tNext: ");
			sb.append(next.getTarget().getName());
			sb.append("\t");
			sb.append(next.getGuardValue());
			sb.append("\n");
		}
		
		return sb.toString();
	}
	public void traversePrint(PrintStream drain) {
		drain.println(this);
		drain.print("\n");
		for(Transition next : nexts)
			next.getTarget().traversePrint(drain);
	}

	@Override
	public java.util.List<String> getAsXML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<transition source=\"");
//		"2" target="3"
		sb.append(this.getName());
		sb.append("\" target=\"");
		sb.append();
		sb.append(">\n");
//	      <guard>level &lt;= 3 </guard>
//	      <labelposition x="-31.0" y="3.0" width="76.0" height="50.0" />
	      sb.append("<middlepoint x=\"579.0\" y=\"381.5\" />\n");
//	    </transition>
	    		return null;
	}
}
