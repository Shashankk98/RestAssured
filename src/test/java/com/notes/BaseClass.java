package com.notes;

import org.json.simple.JSONObject;
import org.junit.Assert;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BaseClass {
	static String outh_token;

	public static void deleteAllNotes() throws IOException, ParseException {
		RestAssured.baseURI = "https://practice.expandtesting.com";
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.header("x-auth-token", outh_token);

		// Fetch existing notes
		Response response = request.request(Method.GET, "/notes/api/notes");
		response.prettyPrint();
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);

		// Get the list of notes
		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		List<String> notesIds = jsonPathEvaluator.getList("data.id");

		// Loop through each note ID and delete it
		for (String noteId : notesIds) {
			Response deleteResponse = request.request(Method.DELETE, "/notes/api/notes/" + noteId);
			deleteResponse.prettyPrint();
			int deleteStatusCode = deleteResponse.getStatusCode();
			Assert.assertEquals(deleteStatusCode, 200); // Assuming 200 is the success status for deletion
		}
	}

	public static String GetToken(String uname, String pass) {
		// System.out.println("LOGINBase");
		RestAssured.baseURI = "https://practice.expandtesting.com";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("email", uname);
		requestParams.put("password", pass);
		// Add a header stating the Request body is a JSON
		request.header("Content-Type", "application/json");
		request.body(requestParams.toJSONString());
		// POST the Response
		Response response = request.request(Method.POST, "/notes/api/users/login");
		// Response response = request.request(Method.POST,"/spree_oauth/token");
		response.prettyPrint();
		int statusCode = response.getStatusCode();
		// System.out.println(response.asString());
		Assert.assertEquals(statusCode, 200);
		// To get the Token from JSON Response
		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		outh_token = jsonPathEvaluator.get("data.token").toString();
		System.out.println("oAuth Token is =>  " + outh_token);
		return outh_token;

	}

	public static JSONObject readFile(String filename) throws IOException, ParseException {

		// Create json object of JSONParser class to parse the JSON data
		JSONParser jsonparser = new JSONParser();
		// Create object for FileReader class, which help to load and read JSON file
		FileReader reader = new FileReader(".\\TestData\\" + filename);
		// Returning/assigning to Java Object
		Object obj = jsonparser.parse(reader);
		// Convert Java Object to JSON Object, JSONObject is typeCast here
		JSONObject prodjsonobj = (JSONObject) obj;
		return prodjsonobj;

	}

}
