package test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import util.StringTools;

public class StringUtilTest {

	@Test
	public void tagTest() {
		String content = "i am content";
		String tagName = "funny";
		String parameter = "very=True veryvery=extremeTrue";

		String result = StringTools.tag(tagName, parameter, content);
		String correctResult = "<funny " + parameter + ">\n\t" + content + "\n</funny>\n";
		assertEquals(correctResult, result);
	}

	@Test
	public void convertToTransitionTest() {

	}

	@Test
	public void invertEquationTest() {

		String equation = "3 <= pi";
		String solution = "3 > pi";
		
		String result = StringTools.invertEquation(equation);
		assertEquals(result, solution);
	}

	@Test
	public void parseToXMLTest() {
		String statement = "42 <= this.my42 && pi >= 3";
		String solution = "42 &lt;= my42 &amp; pi &gt;= 3";
		String result = StringTools.parseToXML(statement);

		assertEquals(result, solution);
	}

	@Test
	public void getWithPrettyPrintTest() {
	}

	@Test
	public void removeSemicolonTest() {
	}

}