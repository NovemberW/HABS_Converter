package flags;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import util.StringTools;

public class FileFlag extends FlagInterface {
	
	private String fileName = null;
	
	private String fileContent = null;

	public FileFlag(java.util.List<FlagInterface> flagList) {
		super(flagList);
		name = "f";
		manual = "-f takes a filename to a *.abs file. Filename musst follow the flag directly";
	}

	@Override
	public void readArgList(String[] argList) {
		super.readArgList(argList);
		int index = StringTools.find(argList, "-f");
		if(index == -1) {
			System.out.println("Could not find file");
			System.exit(-1);
		}
		fileName = argList[index + 1];
		
	}
	@Override
	public void performe() {
		if (!isActive)
			return;

		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			
			fileContent = new String(data,"UTF-8");

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
	
	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
}