package flags;

public abstract class FlagInterface {
	
	String name = null;
	
	String[] arguments = null;
	
	String manual = null;
	
	String[] argList = null;
	
	public boolean isActive = false;
	
	protected java.util.List<FlagInterface> flagList;
	
	public FlagInterface(java.util.List<FlagInterface> flagList) {
		 this.flagList = flagList;
	
	}
	
	private void setArgList(String[] argList) {
		this.argList = argList;
	}
	public void isActive() {
		boolean a = false;
		for (String e : argList)
			if (e.toLowerCase().equals("-" + name)) {
				a = true;
				break;
			}
		isActive = a;
	}
	/**
	 *Primary function of flag.
	 *
	 *@Note: Can terminate execution of if flag finds an error.
	 * 
	 */
	public abstract void performe();
	
	public void readArgList(String[] argList) {
		setArgList(argList);
		isActive();
	}

	public String getManual() {
		return manual;
	}
	
}
