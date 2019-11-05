package util;

import java.util.Iterator;
import java.util.LinkedList;

import abs.frontend.ast.ASTNode;
import abs.frontend.ast.AwaitStmt;
import abs.frontend.ast.ClassDecl;
import abs.frontend.ast.DifferentialExp;
import abs.frontend.ast.MethodImpl;
import abs.frontend.ast.PhysicalImpl;

public class NodeUtil {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void  recursiveFind_ClassDecl(ASTNode<ASTNode> node, java.util.List<ASTNode<ASTNode>> akku ) {
		if (node == null)
			return;
		if (node instanceof ClassDecl) {
			akku.add(node);
			return;
		}

		Iterator<ASTNode> it = node.astChildIterator();

		ASTNode<ASTNode> akku2 = null;
		while (it.hasNext() && akku2 == null) {
			recursiveFind_ClassDecl(it.next(),akku);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void  recursiveFind_MethodImpl(ASTNode<ASTNode> node, java.util.List<ASTNode<ASTNode>> akku ) {
		if (node == null)
			return;
		if (node instanceof MethodImpl) {
			akku.add(node);
		}

		Iterator<ASTNode> it = node.astChildIterator();

		ASTNode<ASTNode> akku2 = null;
		while (it.hasNext() && akku2 == null) {
			recursiveFind_MethodImpl(it.next(),akku);
		}
	}
	
	public static java.util.List<ASTNode<ASTNode>> getAllPhysicalImpl(ASTNode<ASTNode> classDecl){
		java.util.List<ASTNode<ASTNode>> akku = new LinkedList<ASTNode<ASTNode>>();
		
		getAllPhysicalImplHelper(classDecl, akku);

		return akku;
		
	}

	private static java.util.List<ASTNode<ASTNode>> getAllPhysicalImplHelper(ASTNode<ASTNode> node,java.util.List<ASTNode<ASTNode>> akku){

		if (node == null)
			return akku;
		if (node instanceof PhysicalImpl) {
			akku.add(node);
			return akku;
		}

		Iterator<ASTNode> it = node.astChildIterator();

		ASTNode<ASTNode> ret = null;
		while (it.hasNext() && ret == null) {
			getAllPhysicalImplHelper(it.next(),akku);
		}
		
		
		return akku;
		
	}
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
			akku = recursiveFind_DifferentialExp(it.next());
		}
		return akku;
	}
	
	public static ASTNode<ASTNode> recursiveTraverserFind(@SuppressWarnings("rawtypes") ASTNode node, String startText) {
		if (node == null)
			return null;
		if(node.value.toString().startsWith(startText))
			return node;
//		System.out.println(node.value);
		Iterator<ASTNode> it = node.astChildIterator();

		ASTNode<ASTNode> akku = null;
		while (it.hasNext() && akku == null) {
			akku = recursiveTraverserFind(it.next(),startText);
		}
		return akku;
	}

}
