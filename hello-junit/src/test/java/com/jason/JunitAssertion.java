package com.jason;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by jc6t on 2015/12/2.
 */
public class JunitAssertion extends TestCase {

    private int count;

    protected void setUp() {
        System.out.println("in set up");
        count++;
    }

    protected void tearDown() {
        System.out.println("in tear down~~~");
    }

    @Test
    public void testAssertions() {
        String str1 = "abc";
        String str2 = "abc";
        String str3 = new String("abc");
        String str4 = new String("abc");

        // below three asserts are true
        assertEquals(str1, str2);
        assertEquals(str1, str3);
        assertEquals(str3, str4);

//        below three asserts are false
//        assertSame(str1, str2);
//        assertSame(str1, str3);
//        assertSame(str3, str4);
    }

    @Test
    public void test2() {
        // note: the number here is 1 bcuz setUp() function is independent to each other @Test function
        System.out.println("method:" + getMName() + ", number is " + count);
    }

    @Test
    public void test3() {
        // note: the number here is 1 bcuz setUp() function is independent to each other @Test function
        System.out.println("method:" + getMName() + ", number is " + this.count);
    }

    /**
     * get the method name in current running thread
     *
     * @return
     */
    public static String getMName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
