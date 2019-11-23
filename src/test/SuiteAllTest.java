package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		StringUtilTest.class, 
		NodeUtilTest.class,
		MethodTest.class,
		ClassTest.class
})

public class SuiteAllTest {
}