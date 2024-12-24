package com.notes;

import java.util.Random;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

//import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class Delete_Notes_Using_UI {
	
	String outh_token;
	String notes_id;
	String rand_title;
	WebDriver driver;

	@BeforeTest
	public void getToken() {

		outh_token = BaseClass.GetToken("shashank@abc.com", "shashank");

	}

	@Test(priority = 1)
	public void createNotes() {

		// create instance of Random class
		Random rand = new Random();

		// Generate random integers in range 0 to 999
		int rand_int = rand.nextInt(1000);
		rand_title = "API" + rand_int;

		RestAssured.baseURI = "https://practice.expandtesting.com";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", rand_title);
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
		String success_msg = jsonPathEvaluator.get("message").toString();
		// Get the ID of the Notes
		notes_id = jsonPathEvaluator.get("data.id").toString();

	}

	@Test(priority = 2)
	public void DeleteNotes() throws InterruptedException {
		//WebDriverManager.chromedriver().setup();
		//driver = new ChromeDriver();
		driver = new EdgeDriver();
		//driver = new EdgeDriver();
		driver.manage().window().maximize();
		driver.get("https://practice.expandtesting.com/notes/app/login");
		driver.findElement(By.xpath("//input[@id='email']")).clear();
		driver.findElement(By.xpath("//input[@id='email']")).sendKeys("shashank@abc.com");
		driver.findElement(By.xpath("//input[@id='password']")).clear();
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys("shashank");
		driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
		Thread.sleep(5000);
		WebElement element = driver.findElement(By.xpath(
				"//div[text()='" + rand_title + "']//following-sibling::div/div/button[normalize-space()='Delete']"));
		//This will scroll the page Horizontally till the element is found
		//JavascriptExecutor js = (JavascriptExecutor) driver;
        //js.executeScript("arguments[0].scrollIntoView();", element);
		Actions action = new Actions(driver);
        action.moveToElement(element);
        element.click();
		driver.findElement(By.xpath("//button[@type='button'][normalize-space()='Delete']")).click();
		Thread.sleep(5000);
		boolean delted_title = driver.getPageSource().contains(rand_title);
		Assert.assertFalse(delted_title);
	}

	@AfterTest
	public void CloseBrowser() {
		driver.quit();

	}

}
