package com.jason;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A way to run JUnit suite
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestJunit1.class, TestJunit2.class})
public class JunitSuite {
}
