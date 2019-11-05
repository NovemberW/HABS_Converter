package info;

import java.util.LinkedList;

import abs.frontend.ast.ASTNode;
import core.MainDefs;
import util.StringTools;

public class AwaitDurationLineState extends LineState {

	public AwaitDurationLineState(ASTNode<ASTNode> line, String name, String flow) {
		super(line, name, "true", flow);
		// As this is the state containing the diff. guard its invariant is true

	}

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
		
		
		String guard = MainDefs.globalTimeName 
				+ " == " 
				+ StringTools.getWithPrettyPrint(statement.getChild(1).getChild(0));
		//Note: As statet during meetings both values are the same 
		//- using only the first one
	
		for (Transition trans : nexts) {
			sb = new StringBuffer();
			sb.append("<transition source=\"");
			// "2" target="3"
			sb.append(this.getId());
			sb.append("\" target=\"");
			sb.append(trans.getTarget().getId());
			sb.append("\">\n");
			sb.append("<guard>");
			sb.append(guard);
			sb.append(" </guard>\n");
			sb.append(transitionAssignment);
			sb.append("<labelposition x=\"-31.0\" y=\"3.0\" width=\"76.0\" height=\"50.0\" />\n");
			sb.append("<middlepoint x=\"579.0\" y=\"381.5\" />\n");
			sb.append("</transition>\n");

			akku.add(sb.toString());
		}
		return akku;
	}
	
	@Override
	protected String getAssignmentString() {
		return "";
	}
}
