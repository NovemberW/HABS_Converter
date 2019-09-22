package flags;

import util.StringTools;
public class FilePrintFlag extends FlagInterface {

	public FilePrintFlag(java.util.List<FlagInterface> flagList) {
		super(flagList);
		name = "print";
		manual = "-print to print the file given to -f to be printed";
	}

	@Override
	public void performe() {
		if(!isActive)
			return;
		if(StringTools.containsNotCaseSensitive(argList,"-f")){
			for(FlagInterface flag : flagList)
				if(flag instanceof FileFlag)
					System.out.println(((FileFlag) flag).getFileContent());
					
		}else {
			System.out.println("Nothing to print!");
			System.exit(-1);
		}
	}
}