package flags;

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

public class FileStoreFlag extends FlagInterface {

	private String fileName = null;

	private String xmlFileContent = "";

	private String cfgFileContent = "";

	public FileStoreFlag(java.util.List<FlagInterface> flagList) {
		super(flagList);
		name = "s";
		manual = "-s takes a filename and uses it to store the results in it. The file extension .xml is added automatically. "
				+ "If no filename is given the default value out is used.";
		before = false;
	}

	@Override
	public void readArgList(String[] argList) {
		super.readArgList(argList);
		int index = StringTools.find(argList, "-" + name);
		if (index == -1) {
			fileName = "out";
		}
		fileName = argList[index + 1];

	}

	@Override
	public void performe() {
		if (!isActive)
			return;

		PrintWriter writer;

		File XMLout = new File(fileName + ".xml");
		if (!XMLout.exists()) {

			try {
				writer = new PrintWriter(XMLout);
				writer.write(getXMLFileContent());
				writer.flush();
			} catch (IOException e) {
				System.out.println("Error while writing xml file!");
				e.printStackTrace();
			}
			System.out.println("Storing XML file");
		}else {
			System.out.println("XML file already exists");
			System.out.println("Dumping to stdout:\n");
			System.out.println(getXMLFileContent());
		}

		File CFGout = new File(fileName + ".cfg");

		if (!CFGout.exists()) {

			try {
				writer = new PrintWriter(CFGout);
				writer.write(getCFGFileContent());
				writer.flush();
			} catch (IOException e) {
				System.out.println("Error while writing cfgwriting!");
				e.printStackTrace();
			}

			System.out.println("Storing file");

		} else {
			System.out.println("Cfg file already exists");
			System.out.println("Dumping to stdout:\n");
			System.out.println(getCFGFileContent());
		}
	}

	public String getXMLFileContent() {
		return xmlFileContent;
	}

	public void setXMLFileContent(String fileContent) {
		this.xmlFileContent = fileContent;
	}

	public String getCFGFileContent() {
		return cfgFileContent;
	}

	public void setCFGFileContent(String cfgFileContent) {
		this.cfgFileContent = cfgFileContent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}