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
	public static int getClosingBracket(String full, int openingBracket) {
		int open = 0;
		for(int index = openingBracket;full.length() != index;index++) {
			if(full.charAt(index) == '(')
				open++;
			else if(full.charAt(index) == ')')
				open--;
			if(open== 0)
				return index;
		}
		
		return -1;
	}
	public static String removeSemicolon(String in) {
		if(in == null)
			return null;
		
		return in.replaceAll(";","");
	}
}
