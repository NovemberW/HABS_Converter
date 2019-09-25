package core;

import java.io.File;
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
import info.ContentModel;
import util.ParseHelper;

public class Flywheel2 {

	java.util.List<FlagInterface> availableFlags;
	

	private static HashMap<String, String> call;

	private String fileContent;
	
	public Flywheel2() {
		availableFlags = new LinkedList<FlagInterface>();
		availableFlags.add(new HelpFlag(availableFlags));
		availableFlags.add(new FileFlag(availableFlags));
		availableFlags.add(new FilePrintFlag(availableFlags));
			
	}

	public void work(String[] args) {

		System.out.println("Starting Tool...");

		call = new HashMap<String, String>();
		readArguments(args);

		evaluateArguments();

		loadFile();

		ContentModel contentModel = new ContentModel();
		String fileName = null;
		
		for(FlagInterface fI : availableFlags)
			if(fI instanceof FileFlag)
				fileName = ((FileFlag) fI).getFileName();
		
		try {
			contentModel.extractInfo(fileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	}

	private void loadFile() {
		String k = null;
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
		for(FlagInterface flag: availableFlags) {
			flag.performe();
		}

	}

	private void readArguments(String[] args) {
		for(FlagInterface flag : availableFlags)
			flag.readArgList(args);
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
