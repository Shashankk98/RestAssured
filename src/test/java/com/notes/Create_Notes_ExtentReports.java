package com.notes;

import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

import com.aventstack.extentreports.Status;

public class Create_Notes_ExtentReports {
	String outh_token;
	String NotesId;
	String rand_title;
	WebDriver driver;

	// builds a new report using the html template
	ExtentSparkReporter htmlReporter;
	ExtentReports extent;
	// helps to generate the logs in test report.
	ExtentTest test;

	@BeforeClass()
	public void startReport() {
		// initialize the HtmlReporter
		htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/ExtentReport/CreateNotesResult.html");
		// initialize ExtentReports and attach the HtmlReporter
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		// To add system or environment info by using the setSystemInfo method.
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("QA Name", "Abhi");

		// configuration items to change the look and feel
		// add content, manage tests etc
		// htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle("Create Notes Report for API's Test");
		htmlReporter.config().setReportName("Smoke Test");
		// htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

	}

	@AfterClass
	public void CloseExtentReport()

	{
		// to write or update test information to reporter
		extent.flush();
	}

	@BeforeTest
	public void getToken() {

		outh_token = BaseClass.GetToken("shashank@abc.com", "shashank");

	}

	@Test(priority = 1)
	public void createNote() {
		
		test = extent.createTest("Test Case 1", "Create Notes using File");

		
		RestAssured.baseURI = "https://practice.expandtesting.com";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", "TestNG_APIByShashank");
		requestParams.put("description", "Done via RestAssured");
		requestParams.put("category", "Home");

		// Add a header stating the Request body is a JSON
		request.header("Content-Type", "application/json");
		request.header("x-auth-token", outh_token);
		request.body(requestParams.toJSONString());

		// POST the Response
		Response response = request.request(Method.POST, "/notes/api/notes");
		// Response response = request.request(Method.POST,"/spree_oauth/token");
		response.prettyPrint();
		
		int statusCode = response.getStatusCode();
		// System.out.println(response.asString());
		Assert.assertEquals(statusCode, 200);
		
		// To get the Token from JSON Response
		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		NotesId = jsonPathEvaluator.get("data.id").toString();		
		System.out.println("id is =>  " + NotesId);

		// To get the Token from JSON Response
		//JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		String success_msg = jsonPathEvaluator.get("message").toString();
		Assert.assertEquals(success_msg, "Note successfully created");

		String act_title = jsonPathEvaluator.get("data.title").toString();
		Assert.assertEquals(act_title, "TestNG_APIByShashank");
	}

	@Test(priority = 2)
	public void updateNote() {
		
		test = extent.createTest("Test Case 2", "Update Notes using File");


		RestAssured.baseURI = "https://practice.expandtesting.com";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", "TestNG_APIByShashank-updated");
		requestParams.put("description", "Done via RestAssured");
		requestParams.put("category", "Home");
		requestParams.put("completed", "true");

		// Add a header stating the Request body is a JSON
		request.header("Content-Type", "application/json");
		request.header("x-auth-token", outh_token);
		request.body(requestParams.toJSONString());

		// POST the Response
		Response response = request.request(Method.PUT, "/notes/api/notes/"+NotesId);
		
		// Response response = request.request(Method.POST,"/spree_oauth/token");
		response.prettyPrint();
		int statusCode = response.getStatusCode();
		
		// System.out.println(response.asString());
		Assert.assertEquals(statusCode, 200);

		// To get the Token from JSON Response
		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		String success_msg = jsonPathEvaluator.get("message").toString();
		Assert.assertEquals(success_msg, "Note successfully Updated");

		String act_title = jsonPathEvaluator.get("data.title").toString();
		Assert.assertEquals(act_title, "TestNG_APIByShashank-updated");

	}

	@Test(priority = 3)
	public void deleteNotes() {
		
		test = extent.createTest("Test Case 3", "Delete Notes After Create Notes");

		
		RestAssured.baseURI = "https://practice.expandtesting.com";
		RequestSpecification request = RestAssured.given();

		request.header("x-auth-token", outh_token);

		Response response = request.request(Method.DELETE, "/notes/api/notes/" + NotesId);
		response.prettyPrint();

		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);

		// To get the Token from JSON Response
		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		String success_msg = jsonPathEvaluator.get("message").toString();
		Assert.assertEquals(success_msg, "Note successfully deleted");

	}

	@AfterMethod
	public void getResult(ITestResult result) throws Exception {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " = FAILED ", ExtentColor.RED));
			test.fail(result.getThrowable());

		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " = PASSED ", ExtentColor.GREEN));
		} else {
			test.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " = SKIPPED ", ExtentColor.ORANGE));
			test.skip(result.getThrowable());
		}
	}
}
