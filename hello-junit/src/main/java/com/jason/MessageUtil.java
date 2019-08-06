package com.jason;

/**
 * JUnit Example 1.
 * This is the first example to show JUnit function.
 * We write a class {@link MessageUtil} to print a message, and use cmd+shift+T to generate a unit test TestJunit
 * in test source root.
 * Each method in MessageUtilTest has a @Test annotation to notify JUnit to run this unit test.
 * With assertEquals statement, we can assertSomething(expected, knownInput).
 * If true, test is passed; otherwise, is failed.
 *
 * Because I write theses tests in IDE Intellij, I don't need to use JUnit Runner to run by myself manually like below:
 * <code>
 * Result result = JUnitCore.runClasses(TestJunit.class);
 *
 * for (Failure failure : result.getFailures()) {
 *          System.out.println(failure.toString());
 *       }
 *
 * System.out.println(result.wasSuccessful());
 * </code>
 *
 * @author Jason Chang
 */
public class MessageUtil {
    private String message;

    //Constructor
    //@param message to be printed
    public MessageUtil(String message) {
        this.message = message;
    }

    // prints the message
    public String printMessage() {
        System.out.println(message);
        return message;
    }

    // add "Hi!" to the message
    public String salutationMessage(){
        message = "Hi!" + message;
        System.out.println(message);
        return message;
    }

    public long throwException(){
        return 1/0;
    }
}
