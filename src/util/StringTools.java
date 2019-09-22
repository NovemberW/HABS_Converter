package util;

public class StringTools {

	public static boolean containsCaseSensitive(String[] array, String element) {
		for (String e : array)
			if (e.equals(element))
				return true;
		return false;
	}
	
	public static boolean containsNotCaseSensitive(String[] array, String element) {
		for (String e : array)
			if (e.equalsIgnoreCase(element))
				return true;
		return false;
	}
	
	public static int find(String[] list, String target) {
		for(int i = 0;i < list.length;i++)
			if(list[i].equalsIgnoreCase(target))
				return i;
		return -1;
	}
}
