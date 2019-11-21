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
		String[][] testVector = {{"d = 42;","d := 42"},{"this.x = 13;","x := 13"}};
		
		for(String[] v : testVector)
			assertEquals(v[1], StringTools.convertToTransition(v[0]));
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
	public void containCaseSensitivetTest() {
		String element = "Dummy";
		String[] toTest = { "A", "b", "heslfsjfd", element, element.toLowerCase() };
		String[] toTest2 = { "A", "b", "heslfsjfd", element.toLowerCase() };

		boolean answer = StringTools.containsCaseSensitive(toTest, element);
		assertEquals(true, answer);
		
		boolean answer2 = StringTools.containsCaseSensitive(toTest2, element);
		assertEquals(false, answer2);
		
	}

	@Test
	public void containNotCaseSensitivetTest() {
		String element = "Dummy";
		String[] toTest = { "A", "b", "heslfsjfd", element, element.toLowerCase() };
		String[] toTest2 = { "A", "b", "heslfsjfd", element.toLowerCase() };

		boolean answer = StringTools.containsNotCaseSensitive(toTest, element);
		assertEquals(true, answer);
		
		boolean answer2 = StringTools.containsNotCaseSensitive(toTest2, element);
		assertEquals(true, answer2);

	}
}