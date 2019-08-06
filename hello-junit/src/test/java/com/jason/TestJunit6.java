package com.jason;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * JUnit Example 6.
 * JUnit provides a handy option of Timeout.
 * If a test case takes more time than the specified number of milliseconds,
 * then JUnit will automatically mark it as failed.
 */
public class TestJunit6 {
    String msg = "Robert";
    MessageUtil messageUtil = new MessageUtil(msg);

    // This test will show timeout.
    @Test(timeout = 100)
    public void testPrintMessage() throws InterruptedException {
        System.out.println("Inside testPrintMessage()");
        TimeUnit.MILLISECONDS.sleep(1000);
        messageUtil.printMessage();
    }

    @Test
    public void testSalutationMessage() {
        System.out.println("Inside testSalutationMessage()");
        msg = "Hi!" + "Robert";
        Assert.assertEquals(msg, messageUtil.salutationMessage());
    }
}
