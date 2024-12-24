package com.notes;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create_Notes_Using_File {
	String outh_token;
	String notes_id;
	// String rand_title;
	// WebDriver driver;

	@BeforeTest
	public void getToken() {

		outh_token = BaseClass.GetToken("shashank@abc.com", "shashank");
	}

	@Test
	public void createNotes() throws IOException, ParseException {

		JSONParser jsonparser = new JSONParser();
		// Create object for FileReader class, which help to load and read JSON file
		FileReader reader = new FileReader(".\\TestData\\Create_Notes.json");
		// Returning/assigning to Java Object
		Object obj = jsonparser.parse(reader);
		// Convert Java Object to JSON Object, JSONObject is type cast here
		JSONObject prodjsonobj = (JSONObject) obj;

		RestAssured.baseURI = "https://practice.expandtesting.com";
		RequestSpecification request = RestAssured.given();

		// Add a header stating the Request body is a JSON
		request.header("Content-Type", "application/json");
		request.header("x-auth-token", outh_token);
		request.body(prodjsonobj.toJSONString());
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
		String fname = jsonPathEvaluator.get("data.title").toString();
		System.out.println("Title is =>  " + fname);
		Assert.assertEquals("Data_File", fname);

	}

}
