package useinsider.TestComponets;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import useinsider.Database.DatabaseController;

import java.io.IOException;

public class DatabaseListenersNG extends BaseTest implements ITestListener {

    private DatabaseController dbController;

    public DatabaseListenersNG() {
        this.dbController = new DatabaseController();
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Not used in this implementation
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            recordTestResult(result, "passed", null);
        } catch (IOException e) {
            handleException("Error occurred while recording test result.", e);
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            recordTestResult(result, "failed", result.getThrowable());
            captureScreenshot(result);
        } catch (IOException e) {
            handleException("Error occurred while recording test result.", e);
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try {
            recordTestResult(result, "skipped", result.getThrowable());
        } catch (IOException e) {
            handleException("Error occurred while recording test result.", e);
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not used in this implementation
    }

    @Override
    public void onStart(ITestContext context) {
        // Not used in this implementation
    }

    @Override
    public void onFinish(ITestContext context) {
        dbController.closeConnection();
    }

    private void recordTestResult(ITestResult result, String status, Throwable cause) throws IOException {
        String caseName = result.getMethod().getMethodName();
        String caseTestClass = result.getTestClass().getName();
        String caseStackTrace = cause != null ? cause.getMessage() : null;
        Long caseDuration = (result.getEndMillis() - result.getStartMillis())/1000; //seconds
        String caseScreenshot = captureScreenshot(result);
        // database controller to insert the test result into the database
        dbController.insertTestResult(caseName, caseTestClass, status, caseStackTrace, caseDuration, caseScreenshot);
    }

    private String captureScreenshot(ITestResult result) {
        if(result.getStatus()==ITestResult.FAILURE){
            try {
                driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
                return getScreenshotAbsPath(result.getMethod().getMethodName(), driver);
            } catch (Exception e) {
                handleException("Error occurred while capturing screenshot.", e);
                return null; // Return null if screenshot capturing fails
            }
        }else return null; // Return null if test case is not failed
    }

    private void handleException(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }

}
