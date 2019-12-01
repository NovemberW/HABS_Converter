package flags;

import java.util.*;

/**
 * HelpFlag ({@link FlagInterface}
 * 
 * Prints all manuals of all available flags ({@link FlagInterface}) and
 * terminates the application.
 * 
 * @author nicholas
 *
 */
public class HelpFlag extends FlagInterface {

	public HelpFlag(java.util.List<FlagInterface> flagList) {
		super(flagList);
		name = "help";
		manual = "Manual:\n" + "\tAll flags are not case sensitive. For help use -help or -HeLp";
	}

	/**
	 * Prints all manuals to the standard output with indentation.
	 * 
	 */
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