package info;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import abs.frontend.ast.ASTNode;
import abs.frontend.ast.ClassDecl;
import abs.frontend.ast.ExpressionStmt;
import abs.frontend.ast.MethodImpl;
import core.MainDefs;
import flags.ManualInvariantFlag;
import util.NodeUtil;
import util.StringTools;

/**
 * ClassContent
 * 
 * Representation of a HABS class.
 * 
 * Methods: List of {@link Method} representing the methods of the class. Name:
 * String name of the class PhysicalBlock: List of {@link ContinousVariable}
 * present in the HABS file. This also contains a global time variable
 * (globalTim).
 * 
 * @Note: For now this only supports one class implementation.
 * @author nicholas
 *
 */
public class ClassContent {
	private java.util.List<Method> methods;

	private String name;

	private java.util.List<ContinousVariable> physicalBlock;

	public ClassContent(ClassDecl classDecl) {
		int firstKomma = classDecl.value.toString().indexOf(",");
		name = classDecl.value.toString().substring("ClassDecl".length() + 1, firstKomma).trim();

		this.physicalBlock = new LinkedList<ContinousVariable>();

		ASTNode<ASTNode> physicalBlock = NodeUtil.recursiveFind_PhysicalImpl(classDecl);
		Iterator<ASTNode> it = physicalBlock.getChild(1).astChildIterator();

		while (it.hasNext())
			this.physicalBlock.add(new ContinousVariable(it.next()));

		this.physicalBlock.add(new ContinousVariable(MainDefs.globalTimeName));

		// Assemble flow out of all continousVariables present in HABS file
		// and globalTime.

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < this.physicalBlock.size() - 1; i++) {
			sb.append(this.physicalBlock.get(i).getFormula());
			sb.append(" &amp; ");
		}
		sb.append(this.physicalBlock.get(this.physicalBlock.size() - 1).getFormula());

		String flow = sb.toString().replaceAll("this.", "");

		// Extract methods
		java.util.List<ASTNode<ASTNode>> methodList = new LinkedList<ASTNode<ASTNode>>();
		NodeUtil.recursiveFind_MethodImpl(classDecl, methodList);

		methods = new LinkedList<Method>();

		int base = 0;
		for (ASTNode<ASTNode> methodImpl : methodList) {
			Method current = new Method((MethodImpl) methodImpl, flow);
			// uniquely name LineStates of each method (support up to 99 states per method)
			current.renameToIDs(base);
			methods.add(current);
			base += 100;
		}

		combineMethods();

		Method run = null;

		for (Method m : methods)
			if ("run".equals(m.getName()))
				run = m;
		run.getRoot().nexts.get(0).setGuard("_FIRST_TRANSITION_ASSIGNMENT_");//CHANGE
		run.getRoot().setAssignInject(MainDefs.globalTimeTransition);
		run.getRoot().setInvariant(ManualInvariantFlag.invariantPlaceholder);
	}

	/**
	 * Assembles all methods in attribute methods into a state machine.
	 * 
	 * The process is based on calls of methods in LineStates.
	 * 
	 * This process does not allow to assign local variables, as this would require
	 * introducing local variable into the states.
	 */
	private void combineMethods() {
		java.util.List<LineState> allStates = new LinkedList<>();

		HashMap<String, LineState> methodRoots = new HashMap<>(methods.size() * 3);

		for (Method method : methods) {
			allStates.addAll(method.getStates());
			methodRoots.put(method.getName(), method.getRoot());
		}

		// Finding method calls

		HashMap<String, LineState> methodCalls = new HashMap<>(methods.size() * 3);

		for (LineState state : allStates) {
			ASTNode<ASTNode> statement = state.getStatement();
			if (statement != null) {
				if (statement instanceof ExpressionStmt) {
					// Question: Should we implement some mechanism to accommodate for
					// sync. and async. calls ?
					// Answer: Future Work will take a look into it.
					// But for now concurrent tasks are not modeled
					// This means that sync. and asyns. calls are mapped to sync. calls
					methodCalls.put(state.getText().replaceAll("this(.|!)|;|\\(|\\)", ""), state);
				}
			}
		}

		for (String key : methodCalls.keySet()) {
			LineState call = methodCalls.get(key);
			call.setStatement(null);

			List<Transition> newNexts = new LinkedList<Transition>();
			newNexts.add(new Transition(methodRoots.get(key)));
			call.setNexts(newNexts);
		}

	}

	/**
	 * @return the state machine part of a full XML file. Basically Locations and
	 *         transition.
	 */
	public String getStateMachineXML() {
		StringBuffer sb = new StringBuffer();

		sb.append(this.toString());

		return sb.toString();
	}

	/**
	 * Returns the lower parameter block for the spaceex output file.
	 * 
	 * @return That portion.
	 */
	public String getLowerParameterBlock() {

		StringBuffer sbBinding = new StringBuffer();
		StringBuffer sbParam = new StringBuffer();

		StringBuffer subSBmap;
		StringBuffer subSBParam;

		for (ContinousVariable cV : physicalBlock) {
			// param
			subSBParam = new StringBuffer();
			subSBParam.append("<param name=\"");
			subSBParam.append(cV.getName());
			subSBParam.append(
					"\" type=\"real\" local=\"false\" d1=\"1\" d2=\"1\" dynamics=\"any\" controlled=\"true\" />");
			sbParam.append(subSBParam.toString());
			sbParam.append("\n");

			// binding
			subSBmap = new StringBuffer();
			subSBmap.append(StringTools.tag("map", "key=\"" + cV.getName() + "\"", cV.getName()));
			sbBinding.append(subSBmap.toString());

		}
		String bindPart = StringTools.tag("bind", "component=\"main\" as=\"main\" x=\"238.0\" y=\"106.0\"",
				sbBinding.toString());

		return StringTools.tag("component", "id=\"sys1\"", sbParam.toString() + bindPart.toString());

	}

	/**
	 * Returns the upper parameter block for the spaceex output file.
	 * 
	 * @return That portion.
	 */
	public String getUpperParameterBlock() {

		StringBuffer sbParam = new StringBuffer();

		StringBuffer subSBParam;

		for (ContinousVariable cV : physicalBlock) {
			// param
			subSBParam = new StringBuffer();
			subSBParam.append("<param name=\"");
			subSBParam.append(cV.getName());
			subSBParam.append(
					"\" type=\"real\" local=\"false\" d1=\"1\" d2=\"1\" dynamics=\"any\" controlled=\"true\" />");
			sbParam.append(subSBParam.toString());
			sbParam.append("\n");

		}

		return sbParam.toString();

	}

	/**
	 * Returns the first component for the spaceec output file. Contains
	 * UpperParameterBlock and state machine.
	 * 
	 * @return That portion.
	 */
	public String getFirstComponentOfXML() {
		StringBuffer sb = new StringBuffer();

		sb.append(this.getUpperParameterBlock());
		sb.append(this.getStateMachineXML());

		return StringTools.tag("component", "id=\"main\"", sb.toString());

	}

	/**
	 * Returns the full content for the spaceex output file.
	 * 
	 * @return That contentcd.
	 */
	public String getXMLString() {
		StringBuffer sb = new StringBuffer();

		sb.append(this.getFirstComponentOfXML());
		sb.append(this.getLowerParameterBlock());

		String xmlPrefix = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n";

		return xmlPrefix + StringTools.tag("sspaceex",
				"xmlns=\"http://www-verimag.imag.fr/xml-namespaces/sspaceex\" version=\"0.2\" math=\"SpaceEx\"",
				sb.toString());
	}

	public java.util.List<Method> getMethods() {
		return methods;
	}

	public void setMethods(java.util.List<Method> methods) {
		this.methods = methods;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public java.util.List<ContinousVariable> getPhysicalBlock() {
		return physicalBlock;
	}

	public void setPhysicalBlock(java.util.List<ContinousVariable> physicalBlock) {
		this.physicalBlock = physicalBlock;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (Method m : methods)
			sb.append(m.toString());

		return sb.toString();
	}
}
