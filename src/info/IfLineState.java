package info;

import java.util.Iterator;
import java.util.LinkedList;

import abs.frontend.ast.ASTNode;

public class IfLineState extends LineState {

	public IfLineState(ASTNode<ASTNode> line, String name) {
		super(line, name);
//		System.out.println("IfLineState");
	}

	@Override
	public void extend() {
		super.extend();

		Iterator<ASTNode> iterator = this.statement.astChildIterator();
		iterator.next();// skipping the first element as it is empty
		ASTNode<ASTNode> booleanEquation = iterator.next();
		ASTNode<ASTNode> thenBlock = iterator.next().getChild(1);
		ASTNode<ASTNode> elseBlock = iterator.next();


		java.util.List<LineState> thenStates = new LinkedList<LineState>();

		iterator = thenBlock.astChildIterator();

		while (iterator.hasNext()) {
			ASTNode<ASTNode> element = iterator.next();
			thenStates.add(LineStateFactory.getLineState(element, element.value.toString()));
		}

		LineStateFactory.connectStates(thenStates);

		java.util.List<LineState> elseStates = new LinkedList<LineState>();

		iterator = elseBlock.astChildIterator();

		while (iterator.hasNext()) {
			ASTNode<ASTNode> element = iterator.next();
			elseStates.add(LineStateFactory.getLineState(element, element.value.toString()));
		}

		LineStateFactory.connectStates(elseStates);
		
		LineState out = this.getNext().get(0);
		
		this.removeNext(out);
		this.addNext(thenStates.get(0));
		this.addNext(elseStates.get(0));
		thenStates.get(thenStates.size() - 1).addNext(out);
		elseStates.get(elseStates.size() - 1).addNext(out);
		/*
		 * StringWriter stringWriter = new StringWriter(); try (PrintWriter printWriter
		 * = new PrintWriter(stringWriter)) { if(statement != null)
		 * statement.doPrettyPrint(printWriter, new DefaultABSFormatter(printWriter));
		 * }catch (Exception e) { e.printStackTrace(); }
		 * System.out.println(stringWriter.toString()); System.out.println("---");
		 */
	}

}
