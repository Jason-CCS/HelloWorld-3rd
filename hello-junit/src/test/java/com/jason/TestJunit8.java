package com.jason;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit provides an option of tracing the exception handling of code.
 * You can test whether the code throws a desired exception or not.
 */
public class TestJunit8 {
    MessageUtil messageUtil = new MessageUtil("123");

    @Test(expected = ArithmeticException.class)
    public void testThrowException() {
        System.out.println("Inside testThrowException()");
        assertEquals(1L, messageUtil.throwException());
    }

}
