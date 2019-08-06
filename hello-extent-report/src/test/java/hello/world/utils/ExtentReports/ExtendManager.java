package hello.world.utils.ExtentReports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class ExtendManager {
    private static ExtentReports extent;

    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            //Set HTML reporting file location
            String workingDir = System.getProperty("user.dir");
            extent = new ExtentReports();
            extent.attachReporter(new ExtentHtmlReporter(workingDir + "\\hello-extent-report\\extent-report.html"));
        }
        return extent;
    }

}
