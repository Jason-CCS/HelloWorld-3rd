package com.jason;

import org.junit.*;

/**
 * JUnit Example 5.
 * Show annotation logic.
 * @Before and @After are for each test instance.
 * @BeforeClass and @AfterClass are class method ran once for a class.
 */
public class TestJunit5 {
    private int numberForBefore = 0;
    private int numberForAfter = 0;
    private static int numberForClass = 0;

    /**
     * Seems like the tests will be run in dictionary order.
     * That is, run test1, test2, then test3.
     * And test4 will be ignore.
     * Reference: https://garygregory.wordpress.com/2011/09/25/understaning-junit-method-order-execution/
     */
    @Test
    public void test3() {
        System.out.println("in test3");
        System.out.println("numberForBefore:" + numberForBefore); // will be 1
        System.out.println("numberForAfter:" + numberForAfter); // will be 0
    }

    @Test
    public void test2() {
        System.out.println("in test2");
        System.out.println("numberForBefore:" + numberForBefore); // will be 1
        System.out.println("numberForAfter:" + numberForAfter); // will be 0
    }

    @Test
    public void test1() {
        System.out.println("in test1");
        System.out.println("numberForBefore:" + numberForBefore); // will be 1
        System.out.println("numberForAfter:" + numberForAfter); // will be 0
    }

    /**
     * ignore this test.
     * Ignore annotation can be used on class, then, all tests inside will be ignored.
     */
    @Ignore
    public void test4() {
        System.out.println("in ignore test");
        System.out.println("numberForBefore:" + numberForBefore);
        System.out.println("numberForAfter:" + numberForAfter);
    }

    /**
     * expect this test will throw ArithmeticException out
     */
    @Test(expected = ArithmeticException.class)
    public void test5() {
        System.out.println("in test5");
        // it will throw ArithmeticException
        int a = 1 / 0;
    }

    /**
     * for each test run once
     */
    @Before
    public void beforeTest() {
        numberForBefore++;
    }

    /**
     * for each test run once
     */
    @After
    public void afterTest() {
        numberForAfter--;
    }

    /**
     * run before this class
     */
    @BeforeClass
    public static void beforeClass() {
        numberForClass++;
        System.out.println("numberForClass:" + numberForClass);
    }

    /**
     * run after this class
     */
    @AfterClass
    public static void afterClass() {
        numberForClass++;
        System.out.println("numberForClass:" + numberForClass);
    }
}
