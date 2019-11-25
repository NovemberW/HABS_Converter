package util;

import java.util.Iterator;
import java.util.LinkedList;

import abs.frontend.ast.ASTNode;
import abs.frontend.ast.AwaitStmt;
import abs.frontend.ast.ClassDecl;
import abs.frontend.ast.DifferentialExp;
import abs.frontend.ast.MethodImpl;
import abs.frontend.ast.PhysicalImpl;
/**
 * Collector Class for static functions to ease work with ASTNodes
 * @author nicholas
 *
 */
public class NodeUtil {
	/* Loads the given list akku with all instances of ClassDecl (@see absfrontend.jar)
	 * that are present in the subtree starting with given ASTNode node
	 * @param node - the root of the subtree to be searched through.
	 * @param akku - Will hold all such nodes
	 */
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
	
	/* Loads the given list akku with all instances of MethodlImpl (@see absfrontend.jar)
	 * that are present in the subtree starting with given ASTNode node
	 * @param node - the root of the subtree to be searched through.
	 * @param akku - Will hold all such nodes
	 */
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
	
	/* Returns a list of all instances of PhysicalImpl (@see absfrontend.jar)
	 * that are present in the subtree starting with given ASTNode classsDecl
	 * @param classDecl - the root of the subtree to be searched through.
	 * @return a list of all such nodes
	 */
	public static java.util.List<ASTNode<ASTNode>> getAllPhysicalImpl(ASTNode<ASTNode> classDecl){
		java.util.List<ASTNode<ASTNode>> akku = new LinkedList<ASTNode<ASTNode>>();
		
		getAllPhysicalImplHelper(classDecl, akku);

		return akku;
		
	}
	/* Recursive assembly of the list for getAllPhysicalImpl
	 * @param node the current node
	 * @param akku the list in its previous state
	 */
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
	
	/* Returns first ASTNode of type PhysicalImpl that can
	 * be found based on the given ASTNode node.
	 * @param node- The root of the subtree to be searched through.
	 * @return that node
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ASTNode<ASTNode> recursiveFind_PhysicalImpl(ASTNode<ASTNode> node) {
		if (node == null)
			return null;
		if (node instanceof PhysicalImpl) {
			return node;
		}

		Iterator<ASTNode> it = node.astChildIterator();

		ASTNode<ASTNode> temp = null;
		while (it.hasNext() && temp == null) {
			temp = recursiveFind_PhysicalImpl(it.next());
		}
		return temp;
	}
}