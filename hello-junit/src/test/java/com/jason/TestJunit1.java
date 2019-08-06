package com.jason;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * JUnit Example 1.
 * Show Assert class function.
 */
public class TestJunit1 {
    @Test
    public void testAdd() {
        //test data
        int num = 5;
        String temp = "al;sdkjf";
        String str = "Junit is working fine";

        //check for equality
        assertEquals("Junit is working fine", str);

        //check for false condition
        assertFalse(num > 6);

        //check for not null value
        assertNotNull(temp);
    }
}
