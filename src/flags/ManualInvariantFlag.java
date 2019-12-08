package flags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import util.StringTools;

/**
 * Stores output of conversion into two files. spaceex XML file and standard cfg
 * file.
 * 
 * @author nicholas
 *
 */
public class ManualInvariantFlag extends FlagInterface {

	public static String invariantPlaceholder = "_Manual_Interaction_Invariant_";

	private String fileName = "";
	
	private String invariant;

	public ManualInvariantFlag(java.util.List<FlagInterface> flagList) {
		super(flagList);
		name = "i";
		manual = "-i Replaces invariantPlaceholder () with given argument invariant." + "\n" + "Requires -s.";
		before = false;
	}

	@Override
	public void readArgList(String[] argList) {
		super.readArgList(argList);
		int index = StringTools.find(argList, "-s");

		if (index == -1) {
			fileName = "out";
		}
		fileName = argList[index + 1];

		fileName = fileName + ".xml";
		
		int invariantIndex = StringTools.find(argList, "-" + name);
		
		if(invariantIndex + 1 >= argList.length) {
			System.out.println("Missing valui for parameter -i. Please provide an invariant.");
			System.exit(1);
		}
		
		invariant = argList[invariantIndex + 1];
	}

	/**
	 * Replaces
	 * 
	 * @throws FileNotFoundException
	 */
	@Override
	public void performe() {
		if (!isActive)
			return;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

			StringBuffer content = new StringBuffer();
			String line;
			do {
				line = reader.readLine();
				if(line != null) {
					content.append(line);
					content.append("\n");
				}
			} while (line!= null);
			
			String content_ = content.toString();
			
			content_ = content_.replace(invariantPlaceholder, invariant);
			
			FileWriter out = new FileWriter(new File(fileName));
			
			out.write(content_);
			
			out.close();

		} catch (Exception e) {
			System.out.println("Failed to replace " + invariantPlaceholder + "with given invariant.");
			e.printStackTrace();
		}
		/*
		 * 
		 * PrintWriter writer;
		 * 
		 * File XMLout = new File(fileName + ".xml");
		 * System.out.println(XMLout.exists()); if (!XMLout.exists()) { try { writer =
		 * new PrintWriter(XMLout); writer.write(getXMLFileContent()); writer.flush(); }
		 * catch (IOException e) { System.out.println("Error while writing xml file!");
		 * e.printStackTrace(); } System.out.println("Storing XML file"); } else {
		 * System.out.println("XML file already exists");
		 * System.out.println("Dumping to stdout:\n");
		 * System.out.println(getXMLFileContent()); }
		 * 
		 * File CFGout = new File(fileName + ".cfg");
		 * 
		 * if (!CFGout.exists()) {
		 * 
		 * try { writer = new PrintWriter(CFGout); writer.write(getCFGFileContent());
		 * writer.flush(); } catch (IOException e) {
		 * System.out.println("Error while writing cfg file!"); e.printStackTrace(); }
		 * 
		 * System.out.println("Storing file");
		 * 
		 * } else { System.out.println("Cfg file already exists");
		 * System.out.println("Dumping to stdout:\n");
		 * System.out.println(getCFGFileContent()); }
		 */
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}