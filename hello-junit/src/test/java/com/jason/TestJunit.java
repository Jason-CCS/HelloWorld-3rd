package com.jason;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * JUnit Example 0.
 * Test for MessageUtil.
 */
public class TestJunit{

    @Test
    public void printMessage() {
        assertEquals("Hello World", new MessageUtil("Hello World").printMessage());
    }
}