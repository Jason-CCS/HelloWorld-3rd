package com.jason;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestResult;


/**
 * JUnit Example 3.
 * Show TestResult function.
 * TestResult is a class to collects information produced from tests, and it will be shown in {@link TestJunit4}.
 */
public class TestJunit3 extends TestResult {
    // add the error
    public synchronized void addError(Test test, Throwable t) {
        super.addError(test, t);
    }

    // add the failure
    public synchronized void addFailure(Test test, AssertionFailedError t) {
        super.addFailure(test, t);
    }

    @org.junit.Test
    public void testAdd() {
        // add any test
    }

    // Marks that the test run should stop.
    public synchronized void stop() {
        //stop the test here
    }
}
