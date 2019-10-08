package util;

import java.util.Iterator;

import abs.frontend.ast.ASTNode;
import abs.frontend.ast.AwaitStmt;
import abs.frontend.ast.DifferentialExp;
import abs.frontend.ast.PhysicalImpl;

public class NodeUtil {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ASTNode<ASTNode> recursiveFind_PhysicalImpl(ASTNode<ASTNode> node) {
		if (node == null)
			return null;
		if (node instanceof PhysicalImpl) {
			return node;
		}

		Iterator<ASTNode> it = node.astChildIterator();

		ASTNode<ASTNode> akku = null;
		while (it.hasNext() && akku == null) {
			akku = recursiveFind_PhysicalImpl(it.next());
		}
		return akku;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ASTNode<ASTNode> recursiveFind_AwaitStmt(ASTNode<ASTNode> node) {
		if (node == null)
			return null;
		if (node instanceof AwaitStmt) {
			return node;
		}

		Iterator<ASTNode> it = node.astChildIterator();

		ASTNode<ASTNode> akku = null;
		while (it.hasNext() && akku == null) {
			akku = recursiveFind_AwaitStmt(it.next());
		}
		return akku;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ASTNode<ASTNode> recursiveFind_DifferentialExp(ASTNode<ASTNode> node) {
		if (node == null)
			return null;
		if (node instanceof DifferentialExp)
			return node;

		Iterator<ASTNode> it = node.astChildIterator();

		ASTNode<ASTNode> akku = null;
		while (it.hasNext() && akku == null) {
			akku = recursiveFind_PhysicalImpl(it.next());
		}
		return akku;
	}

}
