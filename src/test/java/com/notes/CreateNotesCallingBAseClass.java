package com.notes;

import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class CreateNotesCallingBAseClass {
String outh_token;
	
	@BeforeTest
	public void getToken() {

		outh_token=BaseClass.GetToken("shashank@abc.com", "shashank");

	}
	
	@Test
	public void createNotes() {

		RestAssured.baseURI = "https://practice.expandtesting.com";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", "TestNG_API-Base");
		requestParams.put("description", "Done via RestAssured");
		requestParams.put("category", "Home");
		
		// Add a header stating the Request body is a JSON
		request.header("Content-Type", "application/json");
		request.header("x-auth-token",outh_token);
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
		
		String act_title = jsonPathEvaluator.get("data.title").toString();
		Assert.assertEquals(act_title, "TestNG_API-Base");

	}

}
