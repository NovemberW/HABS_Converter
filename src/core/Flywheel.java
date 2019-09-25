package core;

import java.io.File;

import util.NodeUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import abs.frontend.antlr.parser.ABSParserWrapper;
import abs.frontend.ast.ASTNode;
import abs.frontend.ast.CompilationUnit;
import abs.frontend.ast.List;
import abs.frontend.ast.Model;
import flags.FileFlag;
import flags.FilePrintFlag;
import flags.FlagInterface;
import flags.HelpFlag;
import util.ParseHelper;

public class Flywheel {

	java.util.List<FlagInterface> availableFlags;
	

	private static HashMap<String, String> call;

	private String fileContent;
	
	public Flywheel() {
		availableFlags = new LinkedList<FlagInterface>();
		availableFlags.add(new HelpFlag(availableFlags));
		availableFlags.add(new FileFlag(availableFlags));
		availableFlags.add(new FilePrintFlag(availableFlags));
			
	}

	public void work(String[] args) {

		System.out.println("Starting other Tool...");

		call = new HashMap<String, String>();
		readArguments(args);

//		evaluateArguments();

//		loadFile();

//		System.out.println(this.fileContent);

		try {
			String filename = "exampleFiles/BouncingBall.abs";
			File file = new File(filename);

			ABSParserWrapper parser = new ABSParserWrapper(new File(filename), true, true);
			CompilationUnit cUnit = parser.parse(new FileReader(filename));

			List l = new List<>();
			l.add(cUnit);
			Model model = new Model(l);
			
			Iterator<ASTNode> it = model.astChildIterator();
			
			ASTNode<ASTNode> x = it.next();
//			System.out.println(x);
//			System.out.println(NodeUtil.recursiveFind_PhysicalImpl(x));
//			System.out.println(NodeUtil.recursiveFind_PhysicalImpl(x).value);
			String fullPhysical = NodeUtil.recursiveFind_PhysicalImpl(x).toString();
			String[] parts = fullPhysical.split("DifferentialExp");
			java.util.List<String> continousVariables = new LinkedList<String>();
			for(String s : parts) {
//				System.out.println(s);
				int ratIndex = s.indexOf("Rat");
				if(ratIndex != -1) {
					int eqIndex = s.indexOf("=", ratIndex);
					continousVariables.add(s.substring(ratIndex +3,eqIndex).trim());
				}
			}
			
//			it = x.astChildIterator();
//			while(it.hasNext())
//				System.out.println(NodeUtil.recursiveFind_DifferentialExp(it.next()));
			
//			System.out.println(model.getDecls());
//			System.out.println(model.getUpdateDecls());
		/*	
			Iterator<ASTNode> it = model.astChildIterator();
			
			ASTNode<ASTNode> x = it.next();
			ASTNode<ASTNode> physicalBlock = recursiveTraverserFind(x,"PhysicalImpl");
			
//			System.out.println(physicalBlock.value.toString());
			Iterator<ASTNode> g = physicalBlock.astChildren().iterator();
//			physicalBlock.
			System.out.println(physicalBlock.getNumChild());
			String[] splitted = physicalBlock.value.toString().split("Rat");
			
			for(String s : splitted) {

				if(s.contains("DifferentialExp")) {

					System.out.println(s);
					System.out.println(ParseHelper.extractBlock(s,8));
				}
			}
			*/
//			System.out.println(x);
//			physicalBlock = recursiveTraverserFind(x,"DifferentialExp");
//			System.out.println(physicalBlock.value);
			
			/*while (g.hasNext()) {
				System.out.println(g.next());
				System.out.println("It");
			}
			*/

			/*
			String parsed = model.value.toString();
			int startOfPhysical = parsed.indexOf("Physical");
			String part = ParseHelper.extractBlock(parsed,startOfPhysical);
			
			System.out.println(part);
			*/
			/*
			 * Iterator<ASTNode> it = model.astChildIterator(); recursivePrinter(it.next());
			 * System.out.println(it.next().value);
			 * 
			 * System.out.println(cUnit.getDeltaDeclList());
			 * System.out.println(cUnit.getNumChild());
			 * 
			 * 
			 * while (it.hasNext()) { it.next().astChildIterator();
			 * System.out.println(it.next()); System.out.println("-"); }
			 * 
			 * 
			 */
		} catch (Exception e) {
			System.out.println(e);
			System.exit(-1);
		}
	}

	private void loadFile() {
		String k = null;
		System.out.println(call.keySet());
		Iterator<String> iterator = call.keySet().iterator();
		while (iterator.hasNext())
			if ((k = iterator.next()).equals("-f")) {
				FileReader fr = null;
				try {
					File file = new File(call.get(k));
					FileInputStream fis = new FileInputStream(file);
					byte[] data = new byte[(int) file.length()];
					fis.read(data);
					fis.close();

					fileContent = new String(data, "UTF-8");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
	}

	private void evaluateArguments() {
		String k = null;
		System.out.println(call.keySet());
		Iterator<String> iterator = call.keySet().iterator();
		while (iterator.hasNext())
			k = iterator.next();
		if (k.equals("-help"))
			;

	}

	private void readArguments(String[] args) {
		/*for (int i = 0; i < args.length; i++) {
				if (contains(flags, args[i].toLowerCase()))
					if (i + 1 != args.length)
						call.put(args[i], args[i + 1]);
			}
		*/
	}

	private boolean contains(String[] array, String element) {
		for (String e : array)
			if (e.equals(element))
				return true;
		return false;
	}

	public static void recursivePrinter(ASTNode node) {
		if (node == null)
			return;
		System.out.println(node.value);
		Iterator<ASTNode> it = node.astChildIterator();
//		if(!it.hasNext())
//			System.out.println(node.value);

		while (it.hasNext()) {
			recursivePrinter(it.next());
		}
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

	public void extract() {

	}

}
