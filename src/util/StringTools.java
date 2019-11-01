package util;

import java.io.PrintWriter;
import java.io.StringWriter;

import abs.backend.prettyprint.DefaultABSFormatter;
import abs.frontend.ast.ASTNode;

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
		for (int i = 0; i < list.length; i++)
			if (list[i].equalsIgnoreCase(target))
				return i;
		return -1;
	}

	public static int getClosingBracket(String full, int openingBracket) {
		int open = 0;
		for (int index = openingBracket; full.length() != index; index++) {
			if (full.charAt(index) == '(')
				open++;
			else if (full.charAt(index) == ')')
				open--;
			if (open == 0)
				return index;
		}

		return -1;
	}

	public static String removeSemicolon(String in) {
		if (in == null)
			return null;

		return in.replaceAll(";", "");
	}

	public static String getWithPrettyPrint(ASTNode<ASTNode> statement) {

		StringWriter stringWriter = new StringWriter();
		try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
			if (statement != null)
				statement.doPrettyPrint(printWriter, new DefaultABSFormatter(printWriter));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringWriter.toString();
	}

	public static String parseToXML(String in) {
		String workingCopy = new String(in);
		
		String[][] replacementVector = { 
				{"this.", ""},
				{"<=", "&lt;"},
				{">=", "&gt;"},
				{"&&", "&amp"},
				{"<", "&l;"},
				{">", "&g;"}
				
		};
		for(String[] rep : replacementVector)
			workingCopy = workingCopy.replace(rep[0],rep[1]);

		return workingCopy;
	}

	public static String invertEquation(String equation) {
		String result = new String(equation);
		String[][] replacementVector = { {"<=","_le_",">"}, {">=","_ge_","<"}, {"<","_l_",">="}, {">","_g_","<="},{"==","_e_","!="}};
		
		for(String[] rep: replacementVector)
			result = result.replaceAll(rep[0],rep[1]);
		
		for(String[] rep: replacementVector)
			result = result.replaceAll(rep[1],rep[2]);
		
		
		return result;
	}
}
