package info;

import java.util.Iterator;
import java.util.LinkedList;
import abs.frontend.ast.ASTNode;
import abs.frontend.ast.ExpressionStmt;
import abs.frontend.ast.MethodImpl;
import core.MainDefs;
import util.StringTools;

/**
 * Method
 * 
 * Method representation of a method in an HABS class.
 * 
 * Name: Name of the method. Used for combining method calls.
 * Flow: The flow to be used for all LineStates.
 * States: List of all the LineStates that will represent the method.
 * Root: The root LineState. Its test is the method name.
 * End:  Explicit LineState to model the end of the method.
 */
public class Method {

	private String name;
	
	private String flow;

	private java.util.List<LineState> states;
	
	private LineState root;
	
	private LineState end;

	/*
	 * Constructor
	 * 
	 * Used to build a @see LineState and @see Transition state machine out of 
	 * given method.
	 * 
	 * name is set as name if the methodImpl.
	 * 
	 * The states are expanded recursively and renamed with a numerical name starting 
	 * with 0 as root. 
	 * 
	 * 
	 * @param methodImpl: The ASTNode of type MethodImpl that represents the method.
	 * @param flow: The flow that will be copied for all LineStates.
	 */
	public Method(MethodImpl methodImpl, String flow) {
		String full = methodImpl.getChild(0).value.toString();
		name = full.substring(full.indexOf(" "), full.indexOf("(")).trim();

		this.flow = flow;
		
		states = new LinkedList<LineState>();

		Iterator<ASTNode<ASTNode>> blockIterator = methodImpl.getChild(1).getChild(1).astChildIterator();// ==
																											// individual
		
		root = LineStateFactory.getLineState(null, name,MainDefs.globalTimeInvariant,this.flow);

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
	
	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

}