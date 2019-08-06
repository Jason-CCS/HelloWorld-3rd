package com.jason;

import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * JUnit Example 4.
 * Show TestSuite class function.
 * 從第一個範例到第五個範例只是簡單闡釋了JUnit的使用，
 * 但其測試邏輯並沒有很正確，所以可以說只是練習JUnit，而不是練習Unit Test怎麼寫。
 */
public class TestJunit4 {
    public static void main(String[] args) {
        TestSuite testSuite = new TestSuite(TestJunit1.class, TestJunit2.class, TestJunit3.class);
        TestResult testResult = new TestResult();
        testSuite.run(testResult);
        System.out.println("Number of test cases = " + testResult.runCount());
    }
}
