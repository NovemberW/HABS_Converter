package core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import abs.frontend.ast.ASTNode;
import flags.FileFlag;
import flags.FilePrintFlag;
import flags.FileStoreFlag;
import flags.FlagInterface;
import flags.HelpFlag;
import info.ContentModel;

public class Flywheel2 {

	java.util.List<FlagInterface> availableFlags;
	

	private static HashMap<String, String> call;

	
	public Flywheel2() {
		availableFlags = new LinkedList<FlagInterface>();
		availableFlags.add(new HelpFlag(availableFlags));
		availableFlags.add(new FileFlag(availableFlags));
		availableFlags.add(new FilePrintFlag(availableFlags));
		availableFlags.add(new FileStoreFlag(availableFlags));
			
	}

	public void work(String[] args) {

		System.out.println("Starting Tool...");

		readArguments(args);

		executeArguments(true); //execute all flag that should run before the conversion is performed

		ContentModel contentModel = new ContentModel(availableFlags);
		String fileName = null;
		
		for(FlagInterface fI : availableFlags)
			if(fI instanceof FileFlag)
				fileName = ((FileFlag) fI).getFileName();
		
		try {
			contentModel.extract(fileName);
			
			for(FlagInterface fI : availableFlags)
				if(fI instanceof FileStoreFlag) {
					FileStoreFlag storage = (FileStoreFlag) fI;
					storage.setXMLFileContent(contentModel.getXML());
					storage.setCFGFileContent(contentModel.getCFG());
//					System.out.println(((FileStoreFlag) fI).getFileName());
				}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		executeArguments(false);
	}

	private void executeArguments(boolean before) {
		for(FlagInterface flag: availableFlags) {
			if(flag.isBefore() == before) {
				flag.performe();
			}else {//after
				flag.performe();
			}
		}

	}

	private void readArguments(String[] args) {
		for(FlagInterface flag : availableFlags)
			flag.readArgList(args);
	}
	
	public static ASTNode<ASTNode> recursiveTraverserFind(@SuppressWarnings("rawtypes") ASTNode node, String startText) {
		if (node == null)
			return null;
		if(node.value.toString().startsWith(startText))
			return node;
		System.out.println(node.value);
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
