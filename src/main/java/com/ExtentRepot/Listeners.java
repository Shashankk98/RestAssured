package com.ExtentRepot;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class Listeners implements ITestListener {
	ExtentReports extent = ExtentReportBase.ExtentReportGenerator();
	ExtentTest test;
	// This is the concept of ThreadSafe
	// make it private static, so no other class will have access and when you are
	// dealing with ThreadSafe, its good to have private
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();

	public void onFinish(ITestContext context) {
		extent.flush();
		
	}

	public void onStart(ITestContext context) {

	}

	public void onTestFailure(ITestResult result) {

//		
//		try {
//			extentTest.get().fail(result.getThrowable().getMessage(), 
//					MediaEntityBuilder.createScreenCaptureFromPath(BaseClass.getScreenshotfailure(((WebDriver) (result.getTestContext()).getAttribute("WebDriver")), result.getName())).build());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		WebDriver driver;
		ITestContext context = result.getTestContext();
		driver = (WebDriver) context.getAttribute("WebDriver");
		extentTest.get().getModel();
		
		
		try {
			String screenshot = BaseClass.getScreenshotfailure(driver, result.getName());
			extentTest.get().fail(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void onTestSkipped(ITestResult result) {

	}

	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);
	}

	public void onTestSuccess(ITestResult result) {
		extentTest.get().log(Status.PASS, "Test Successful");

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

}


