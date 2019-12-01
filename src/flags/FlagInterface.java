package flags;

/**
 * FlagInterface
 * 
 * Interface to represent command line flags and there behavior.
 * 
 * @author nicholas
 *
 */
public abstract class FlagInterface {
	
	/**
	 * Name - The name of the flag, without "-"
	 */
	String name = null;
	
	/**
	 * Arguments - String[] to store all arguments of the flag
	 */
	String[] arguments = null;
	
	/**
	 * Manual - Short description used when -help is called
	 */
	String manual = null;
	
	/**
	 * ArgList - List of all Flags (and there arguments).
	 * Used when behavior of one flag depends on presence or absence
	 * of other flags
	 */
	String[] argList = null;
	
	/**
	 * True if flag is present, false otherwise
	 */
	public boolean isActive = false;
	
	/**
	 * True if flag is activated before the conversion is performed, otherwise false
	 */
	protected boolean before = true;
	
	public boolean isBefore() {
		return before;
	}
	
	public boolean isAfter() {
		return !before;
	}

	public void setBefore(boolean before) {
		this.before = before;
	}

	protected java.util.List<FlagInterface> flagList;
	
	public FlagInterface(java.util.List<FlagInterface> flagList) {
		 this.flagList = flagList;
	
	}
	
	private void setArgList(String[] argList) {
		this.argList = argList;
	}
	
	/**
	 * Sets isActive true if this argument is present in the call, otherwise false.
	 */
	public void isActive() {
		boolean a = false;
		for (String e : argList) {
			if (e.toLowerCase().equals("-" + name)) {
				a = true;
				break;
			}
		}
		isActive = a;
	}
	
	/**
	 *Performs primary function of flag.
	 *
	 *@Note: Can terminate execution of if flag finds an error.
	 * 
	 */
	public abstract void performe();
	
	/**
	 * Initializes argList and sets isActive.
	 * @param argList
	 */
	public void readArgList(String[] argList) {
		setArgList(argList);
		isActive();
	}

	public String getManual() {
		return manual;
	}
	
}
