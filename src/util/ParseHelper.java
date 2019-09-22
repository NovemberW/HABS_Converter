package util;

public class ParseHelper {
	/**
	 * Return a block (=xy(..()...())) from given string whole.
	 * 
	 * @param whole the full string - expected to be well formated
	 * @param start starting point before the opening bracket
	 */
	public static String extractBlock(String whole, int start) {
		if (whole == null || whole.length() <= 2)
			throw new IllegalArgumentException("Text is invalid.");
		if (start < 0 || start >= whole.length())
			throw new IllegalArgumentException("Index out of range:" + start);

		int pos = start;
		int bracketDepth = 0;
		int beginn = 0;
		int end = 0;

//		System.out.println(whole.length());
		try {
			while (whole.charAt(pos) != '(')// Set starting point of counting to first opening bracket
				pos++;
			
			pos++;
			bracketDepth = 1;
			while (bracketDepth != 0 ) {// Find end of block
				if (whole.charAt(pos) == '(')
					bracketDepth++;
				else if (whole.charAt(pos) == ')')
					bracketDepth--;
				pos++;
			}
			if(pos == whole.length())
				pos--;
//			System.out.println(pos + " " + whole.charAt(pos));
			end = pos;
			pos = start-1;
//			System.out.println(pos + " " + whole.charAt(pos));
			while (pos != -1 && (whole.charAt(pos) != '(' && whole.charAt(pos) != ',')) {// Find start of block
//				System.out.println(whole.charAt(pos));
				pos--;
			}
			beginn = pos +1;
			
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Given String is not well formated", e);
		}

//		System.out.println(end + " " + whole.charAt(end)); 
		if(end == whole.length() - 1)
			return whole.substring(beginn);
		
		return whole.substring(beginn, end );
	}

}