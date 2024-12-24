package com.notes;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.simple.JSONObject;

public class Create_Unique_Notes {

	String outh_token;
	String notes_id;
	String rand_title;
	// WebDriver driver;

	@BeforeTest
	public void getToken() {

		outh_token = BaseClass.GetToken("shashank@abc.com", "shashank");
	}

	@Test
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
		Assert.assertEquals(success_msg, "Note successfully created");

		// Capture Title and Verify that Title got generated
		String act_title = jsonPathEvaluator.get("data.title").toString();
		Assert.assertEquals(act_title, rand_title);

	}

}
