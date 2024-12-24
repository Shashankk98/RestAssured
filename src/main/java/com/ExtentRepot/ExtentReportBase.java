package com.ExtentRepot;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportBase {

	static ExtentReports extent;
	static ExtentSparkReporter reporter;

	public static ExtentReports ExtentReportGenerator() {

		reporter = new ExtentSparkReporter("./ExtentReport/FinalReport.html");
		extent = new ExtentReports();
		extent.attachReporter(reporter);

		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("QA", "Abhi");

		reporter.config().setDocumentTitle("Consolidate ExtentReport Result");
		reporter.config().setTheme(Theme.DARK);
		reporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
		return extent;
	}

}
