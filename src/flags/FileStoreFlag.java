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
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private String fileContent = null;

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
		if(index == -1) {
			fileName = "out";
		}
		fileName = argList[index + 1];
		
	}
	@Override
	public void performe() {
		if (!isActive)
			return;
		File out = new File(fileName + ".xml");
		if(!out.exists()) {

		PrintWriter writer;
		try {
			writer = new PrintWriter(out);
			writer.write(getFileContent());
			writer.flush();
		} catch (IOException e) {
			System.out.println("Error while writing!");
			e.printStackTrace();
		}
		
		System.out.println("Storing file");
		
		}else {
			System.out.println("File already exists");
			System.out.println("Dumping to stdout:\n");
			System.out.println(getFileContent());
		}
	}
	
	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
}