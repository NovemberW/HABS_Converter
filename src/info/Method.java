package info;

import java.util.Iterator;
import java.util.LinkedList;
import abs.frontend.ast.ASTNode;
import abs.frontend.ast.ExpressionStmt;
import abs.frontend.ast.MethodImpl;
import core.MainDefs;
import util.StringTools;

public class Method {

	private String name;
	
	private String flow;

	private java.util.List<LineState> states;
	
	private LineState root;
	
	private LineState end;

	public Method(MethodImpl methodImpl, String flow) {
		String full = methodImpl.getChild(0).value.toString();
		name = full.substring(full.indexOf(" "), full.indexOf("(")).trim();

		this.flow = flow;
		
		states = new LinkedList<LineState>();

		Iterator<ASTNode<ASTNode>> blockIterator = methodImpl.getChild(1).getChild(1).astChildIterator();// ==
																											// individual
																											// statements
																											// of method
		
		root = LineStateFactory.getLineState(null, name,MainDefs.globalTimeInvariant,flow);

		states.add(root); // state to start is method declaration
		int i = 0;
		while (blockIterator.hasNext()) {// creating one state for each line
			ASTNode<ASTNode> element = blockIterator.next();
			states.add(LineStateFactory.getLineState(element, String.valueOf(i++),MainDefs.globalTimeInvariant, flow));
		}
		
		end = LineStateFactory.getLineState(null, "end",MainDefs.globalTimeInvariant,flow);
		// final state for method is implicit "}" state

		states.add(end); 

		LineStateFactory.connectStates(states);

		expandStates();
		
		collectStates();
		
		renameToIDs(0);
		
		
	}

	private void collectStates() {
		for(int i = 0;i < states.size();i++) {
			for(Transition trans: states.get(i).getNext())
				if(!states.contains(trans.getTarget()))
					states.add(trans.getTarget());
		}
	}

	public void renameToIDs(int base) {
		int i = base;
		for(LineState state : states) {
			state.setName(String.valueOf(i));
			i++;
		}
	}

	private void expandStates() {
		for (int i = 0; i < states.size(); i++) {
			states.get(i).extend();
		}
	}

	public LineState getState(String name) {
		if (states == null)
			return null;

		for (LineState state : states)
			if (state.getName().equals(name))
				return state;

		return null;
	}
	
	public java.util.List<LineState> getStates(){
		return states;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for(LineState state : states) {
			java.util.List<String> xmlList = state.getAsXML();
			for(String s : xmlList) {
				sb.append(s);
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public LineState getRoot() {
		return root;
	}

	public void setRoot(LineState root) {
		this.root = root;
	}

	public LineState getEnd() {
		return end;
	}

	public void setEnd(LineState end) {
		this.end = end;
	}
}
