package flags;

import java.util.*;

public class HelpFlag extends FlagInterface {

	public HelpFlag(java.util.List<FlagInterface> flagList) {
		super(flagList);
		name = "help";
		manual = "Manual:\n" + "\tAll flags are not case sensitive. For help use -help or -HeLp";
	}

	@Override
	public void performe() {
		if (!isActive)
			return;
		
		StringBuffer akku = new StringBuffer();
		for (FlagInterface flag : flagList) {
			akku.append("\t");
			akku.append(flag.getManual());
			akku.append("\n");
		}
		System.out.println(akku.toString());

		System.exit(0);
	}
}