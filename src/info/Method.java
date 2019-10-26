package info;

import java.util.Iterator;
import java.util.LinkedList;
import abs.frontend.ast.ASTNode;
import abs.frontend.ast.ExpressionStmt;
import abs.frontend.ast.MethodImpl;
import util.StringTools;

public class Method {

	private String name;

	private java.util.List<LineState> states;

	public Method(MethodImpl methodImpl) {
		String full = methodImpl.getChild(0).value.toString();
		name = full.substring(full.indexOf(" "), full.indexOf("(")).trim();

		states = new LinkedList<LineState>();

		Iterator<ASTNode<ASTNode>> blockIterator = methodImpl.getChild(1).getChild(1).astChildIterator();// ==
																											// individual
																											// statements
																											// of method

		states.add(LineStateFactory.getLineState(null, name)); // state to start is method declaration
		int i = 0;
		while (blockIterator.hasNext()) {// creating one state for each line
			ASTNode<ASTNode> element = blockIterator.next();
			states.add(LineStateFactory.getLineState(element, String.valueOf(i++)));
		}

		states.add(LineStateFactory.getLineState(null, "end")); // final state for method is implicit "}" state

		LineStateFactory.connectStates(states);

		expandStates();
		
		collectStates();

//		states.get(0).traversePrint(System.out);
		
		renameToIDs();
		
		System.out.println("-");
		for(LineState state : states)
			System.out.println(state);
		
	}

	private void collectStates() {
		for(int i = 0;i < states.size();i++) {
			for(Transition trans: states.get(i).getNext())
				if(!states.contains(trans.getTarget()))
					states.add(trans.getTarget());
		}
	}

	private void renameToIDs() {
		int i = 0;
		for(LineState state : states)
			state.setName("s_" + i++);
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
}
