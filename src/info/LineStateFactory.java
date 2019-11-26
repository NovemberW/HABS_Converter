package info;

import org.eclipse.jdt.internal.compiler.ast.IfStatement;

import abs.frontend.ast.ASTNode;
import abs.frontend.ast.AwaitStmt;
import abs.frontend.ast.DurationGuard;
import abs.frontend.ast.IfStmt;
import exception.UnknownASTNodeException;

/*
 * Factory for Linestate.
 * 
 * Determines correct subtype of LineState by ASTNode type.
 * 
 * Supported:
 * 
 * DurationGuard -> AwaitDurationLineState
 * DurationGuard -> AwaitDiffLineState
 * IfStmt 		 -> IfLineState
 * All other than above -> LineState
 * 
 */
public class LineStateFactory {

	/*
	 * Returns a (subtype) of LineStat.
	 * @param node: The ASTNode to be used
	 * @param name: the name of the LineStat
	 * @param invariant: The invariant of the LineState
	 * @param flow: The flow of the LineState
	 * @see LineState
	 * Supported:
	 * 
	 * DurationGuard -> AwaitDurationLineState
	 * DurationGuard -> AwaitDiffLineState
	 * IfStmt 		 -> IfLineState
	 */
	public static LineState getLineState(ASTNode<ASTNode> node, String name, String invariant, String flow) {
		try {
			if (node instanceof AwaitStmt) {
				if (node.getChild(1) instanceof DurationGuard)
					return new AwaitDurationLineState(node, name, flow);

				return new AwaitDiffLineState(node, name, flow);
			}
			if (node instanceof IfStmt)
				return new IfLineState(node, name, invariant, flow);
		} catch (Exception e) {
			throw new UnknownASTNodeException(e);
		}

		return new LineState(node, name, invariant, flow);
	}

	/*
	 * Utility function to connect given list of states
	 * @param states: That List
	 */
	public static void connectStates(java.util.List<LineState> states) {
		for (int i = 0; i < states.size() - 1; i++) {
			states.get(i).addNext(states.get(i + 1));
		}
	}
}
