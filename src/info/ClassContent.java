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
import util.NodeUtil;
import util.StringTools;

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

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < this.physicalBlock.size() - 1; i++) {
			sb.append(this.physicalBlock.get(i).getFormula());
			sb.append(" &amp; ");
		}
		sb.append(this.physicalBlock.get(this.physicalBlock.size() - 1).getFormula());

		String flow = sb.toString().replaceAll("this.", "");

		java.util.List<ASTNode<ASTNode>> list = new LinkedList<ASTNode<ASTNode>>();
		NodeUtil.recursiveFind_MethodImpl(classDecl, list);

		methods = new LinkedList<Method>();

		int base = 0;
		for (ASTNode<ASTNode> methodImpl : list) {
			Method current = new Method((MethodImpl) methodImpl, flow);
			current.renameToIDs(base);
			methods.add(current);
			base += 100;
		}
		combineMethods();

	}

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
					// Answer:Future Work will take a look into it.
					// But for now cocurrent tasks are not modeled
					// This means that sync. and asyns. calls are mapped to sync. calls
					methodCalls.put(state.getText().replaceAll("this(.|!)|;|\\(|\\)", ""), state);
				}
			}
		}
		
		for(String key : methodCalls.keySet()){
			LineState call = methodCalls.get(key);
			call.setStatement(null);
			
			List<Transition> newNexts = new LinkedList<Transition>();
			newNexts.add(new Transition(methodRoots.get(key)));
			call.setNexts(newNexts);
		}
		
	}

	public String getStateMachineXML() {
		StringBuffer sb = new StringBuffer();

		sb.append(this.toString());

		return sb.toString();
	}

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

	public String getFirstComponentOfXML() {
		StringBuffer sb = new StringBuffer();

		sb.append(this.getUpperParameterBlock());
		sb.append(this.getStateMachineXML());

		return StringTools.tag("component", "id=\"main\"", sb.toString());

	}

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
