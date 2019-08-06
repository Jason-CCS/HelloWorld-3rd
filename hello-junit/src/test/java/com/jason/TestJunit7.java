package com.jason;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * JUnit Example 7.
 * Show JUnit Suite class function.
 * This is second way to run suite which could contain many test cases as you wish.
 * The first way is {@link TestJunit4}
 */
public class TestJunit7 {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(JunitSuite.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
