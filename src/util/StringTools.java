package util;

import java.io.PrintWriter;
import java.io.StringWriter;

import abs.backend.prettyprint.DefaultABSFormatter;
import abs.frontend.ast.ASTNode;

public class StringTools {
	
	/* Checks given String[] array for occurrences of element.
	 * @return true only if element matches one in array (caseSensitive)
	 */
	public static boolean containsCaseSensitive(String[] array, String element) {
		for (String e : array)
			if (e.equals(element))
				return true;
		return false;
	}
	
	/* Checks given String[] array for occurrences of element.
	 * @return true only if element matches one in array (not caseSensitive)
	 */
	public static boolean containsNotCaseSensitive(String[] array, String element) {
		for (String e : array)
			if (e.equalsIgnoreCase(element))
				return true;
		return false;
	}

	/* Finds target string in String[].
	 * @return index of that element if present, Otherwise -1.
	 */
	public static int find(String[] list, String target) {
		for (int i = 0; i < list.length; i++)
			if (list[i].equalsIgnoreCase(target))
				return i;
		return -1;
	}

	/* Converts given ASTNode statement to String
	 * This is done by the DefaultABSFormatter.
	 * @Note: This process usually ads brackets.
	 */
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

	/* Converts given String in to XML equivalent
	 * Conversion:
	 * this. -> removed
	 * <= -> &lt;=
	 * >= -> &gt;=
	 * && -> &amp;
	 * <  -> &lt;
	 * >  -> &gt;			
	 */
	public static String parseToXML(String in) {
		String workingCopy = new String(in);
		
		String[][] replacementVector = { 
				{"this.", ""},
				{"<=", "&lt;="},
				{">=", "&gt;="},
				{"&&", "&amp;"},
				{"<", "&lt;"},
				{">", "&gt;"}
				
		};
		for(String[] rep : replacementVector)
			workingCopy = workingCopy.replace(rep[0],rep[1]);

		return workingCopy;
	}

	/* Converts given String equation into its complement
	 * Example:
	 * (i <= j) -> i > j
	 * 
	 * It only supports: <,<=,>,>=,==,!=			
	 */
	public static String invertEquation(String equation) {
		String result = new String(equation);
		String[][] replacementVector = { {"<=","_le_",">"}, {">=","_ge_","<"}, {"<","_l_",">="}, {">","_g_","<="},{"==","_e_","!="},{"!=","_ne_","=="}};
		
		for(String[] rep: replacementVector)
			result = result.replaceAll(rep[0],rep[1]);
		
		for(String[] rep: replacementVector)
			result = result.replaceAll(rep[1],rep[2]);
		
		
		return result;
	}
	
	/* Converts given String statement into its spaceex-transition equivalent
	 * Example:
	 * var = this.42; -> var := 42
	 * 
	 * It only supports: =,this.,;
	 */
	public static String convertToTransition(String statement) {
		String assignment = new String(statement);
		String[][] replacementVector = { {"=",":="},{"this.",""},{";",""}};
		
		for(String[] rep: replacementVector)
			assignment = assignment.replaceAll(rep[0],rep[1]);
		
		return assignment;
	}
	
	/* Adds an XML-Tag based on given tag name tag, its parameters and the content
	 * XML-Tag for reference:
	 * 
	 * <tagName, parameters>
	 * 		content
	 * </tagName>
	 * 
	 * @Note: The tab is also added.
	 */
	public static String tag(String tag, String parameters, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(tag);
		sb.append(" ");
		sb.append(parameters);
		sb.append(">\n");
		String[] contentLines = content.split("\n");
		for(String line : contentLines) {
			sb.append("\t");
			sb.append(line);
			sb.append("\n");
			
		}
		sb.append("</");
		sb.append(tag);
		sb.append(">\n");
		return sb.toString();
	}
}
