package hello.world;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> currentThread = new ThreadLocal<>();

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @BeforeClass
    public synchronized void beforeClass() {
        ExtentTest parent = extent.createTest(getClass().getName());
        currentThread.set(parent);
    }

    @BeforeMethod
    public synchronized void beforeMethod(Method method) {
        ExtentTest child = currentThread.get().createNode(method.getName());


    }


}
