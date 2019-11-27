package info;

import java.util.Iterator;
import java.util.LinkedList;

import abs.frontend.ast.ASTNode;
import core.MainDefs;
import util.StringTools;


/*
 * IfLineState
 * 
 * @see LineState
 * 
 * Represents an if statement (ASTNode). It has two outgoing @see Transition.
 * 
 * Attribute text is "if".
 * 
 */
public class IfLineState extends LineState implements XMLPrinter{

	public IfLineState(ASTNode<ASTNode> line, String name,String invariant,String flow) {
		super(line, name,invariant,flow);
		text = "if";
	}

	/*
	 * Recursively called to extends state to multiple states.
	 */
	@Override
	public void extend() {
		super.extend();

		Iterator<ASTNode> iterator = this.statement.astChildIterator();
		iterator.next();// skipping the first element as it is empty
		ASTNode<ASTNode> booleanEquation = iterator.next();
		ASTNode<ASTNode> thenBlock = iterator.next().getChild(1);//Skip first entry as it is empty
		ASTNode<ASTNode> elseBlock = iterator.next().getChild(0);//explicitly 0 --- somehow needed

		java.util.List<LineState> thenStates = expandBlock(thenBlock);

		java.util.List<LineState> elseStates = expandBlock(elseBlock);

		LineState out = this.getNext().get(0).getTarget();
		
		
		String positive = StringTools.getWithPrettyPrint(booleanEquation);
		
		String negative = StringTools.invertEquation(positive);
		
		this.removeNext(out);
		this.addNext(new Transition(positive, thenStates.get(0)));
		this.addNext(new Transition(negative,elseStates.get(0)));
		thenStates.get(thenStates.size() - 1).addNext(out);
		elseStates.get(elseStates.size() - 1).addNext(out);
	}

	private java.util.List<LineState> expandBlock(ASTNode<ASTNode> block) {
		Iterator<ASTNode> iterator;
		java.util.List<LineState> statesResult = new LinkedList<LineState>();

		iterator = block.astChildIterator();

		while (iterator.hasNext()) {
			ASTNode<ASTNode> element = iterator.next();
			statesResult.add(LineStateFactory.getLineState(element, element.value.toString(),MainDefs.globalTimeInvariant,flow));
		}

		LineStateFactory.connectStates(statesResult);
		return statesResult;
	}
	
	@Override
	public java.util.List<String> getAsXML() {
		return super.getAsXML();
	}
	@Override
	protected String getAssignmentString() {
		return "";
	}

}
