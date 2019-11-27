package info;

import java.io.PrintStream;
import java.util.LinkedList;

import abs.frontend.ast.ASTNode;
import core.MainDefs;
import util.StringTools;

/*
 * LineState
 * 
 * LineState represents a statement of an HABS-File
 * in the form of a continuous state.
 * 
 * Name: The name that will be used to identify the XML elements when printed
 * Id: The id that will be used to identify the XML element.
 * Flow: The flow that will be used when XML printing.
 * Statement: The ASTNode that is transformed.
 * Nexts: List of all outgoing transitions. A LineState can have multiple (@see IfStatement)
 * Test: String representation of Statement. Present in toString() for debugging.
 * Invariant: The invariant of the LineState.
 * 
 * @Note: name and id are connected by the following relation: name = "_" + id
 * This constrain was introduced to ease work with the output files and debugging.
 */
public class LineState implements XMLPrinter {

	protected String name;
	
	protected String id;
	
	protected String flow;

	protected ASTNode<ASTNode> statement;

	protected java.util.List<Transition> nexts;

	protected String text;
	
	protected String invariant;

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
		for (Transition trans : nexts)
			if (trans.getTarget() == next)
				nexts.remove(trans);
	}

	public LineState(ASTNode<ASTNode> line, String name, String invariant,String flow) {
		this.statement = line;
		if (line != null)
			this.text = StringTools.getWithPrettyPrint(line);
		else
			this.text = name;

		this.setName(name);
		this.setInvariant(invariant);
		
		this.flow = flow;

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
		this.name = "_" + name;
		this.id = name;
	}

	public String getId() {
		return id;
	}

	public void extend() {

	}

	/*
	 * Simple output of LineState and its outgoing
	 * transitions. 
	 * 
	 * Output format:
	 * 
	 * STATE: <name>
	 * Text: <text>
	 * 	Next: <targetState>	<guard>
	 * 	Next: <targetState>	<guard>
	 * 	...
	 * 
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("STATE: ");
		sb.append(name);
		sb.append("\n");
		sb.append("Text: ");
		sb.append(text);
		sb.append("\n");

		for (Transition next : nexts) {
			sb.append("\tNext: ");
			sb.append(next.getTarget().getName());
			sb.append("\t");
			sb.append(next.getGuardValue());
			sb.append("\n");
		}

		return sb.toString();
	}

	/*
	 * Debug function for printing the LineState (this) and its outgoing
	 * transition.
	 */
	public void traversePrint(PrintStream drain) {
		drain.println(this);
		drain.print("\n");
		for (Transition next : nexts)
			next.getTarget().traversePrint(drain);
	}

	/*
	 * Assembles the Linestate as a XML location and all its @Transitions.
	 */
	@Override
	public java.util.List<String> getAsXML() {
		java.util.List<String> akku = new LinkedList<String>();

		StringBuffer sb = new StringBuffer();
		
		sb.append("<location id=\"");
		sb.append(this.id);
		sb.append("\" name=\"");
		sb.append(this.name);
		sb.append("\" x=\"361.0\" y=\"314.0\" width=\"218.0\" height=\"128.0\">\n");
		sb.append("<invariant>");
		sb.append(MainDefs.globalTimeInvariant);
		sb.append("</invariant>\n");
		sb.append("<flow>");
		sb.append(this.flow);
		sb.append("</flow>\n");
		sb.append("</location>\n");
		
		akku.add(sb.toString());

		String transitionAssignment = getAssignmentString();
		
		for (Transition trans : nexts) {
			sb = new StringBuffer();
			sb.append("<transition source=\"");
			// "2" target="3"
			sb.append(this.getId());
			sb.append("\" target=\"");
			sb.append(trans.getTarget().getId());
			sb.append("\">\n");
			sb.append("<guard>");
			sb.append(StringTools.parseToXML(trans.getGuardValue()));
			sb.append(" </guard>\n");
			sb.append(transitionAssignment);
			sb.append("<labelposition x=\"-31.0\" y=\"3.0\" width=\"76.0\" height=\"50.0\" />\n");
			sb.append("<middlepoint x=\"579.0\" y=\"381.5\" />\n");
			sb.append("</transition>\n");

			akku.add(sb.toString());
		}
		return akku;
	}

	/*
	 * Converts the statement of the LineState to the XML assignment in eacht outgoing transition. 
	 */
	protected String getAssignmentString() {
		String assignment = StringTools.convertToTransition(StringTools.getWithPrettyPrint(statement));
		StringBuffer sb = new StringBuffer();
		
		sb.append("<assignment>");
		sb.append(assignment);
		sb.append(" </assignment>\n");
		return sb.toString();
	}

	public String getInvariant() {
		return invariant;
	}

	public void setInvariant(String invariant) {
		this.invariant = invariant;
	}
	
	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public java.util.List<Transition> getNexts() {
		return nexts;
	}

	public void setNexts(java.util.List<Transition> nexts) {
		this.nexts = nexts;
	}

	public void setId(String id) {
		this.id = id;
	}
}
