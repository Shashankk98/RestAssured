package com.notes;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class GET_Soap_Request {

	@Test
	public void post_Add_Method() {

		File input = new File(".\\TestData\\SoapRequestFile.xml");
		RestAssured.baseURI = "http://www.dneonline.com";

		Response response = given().header("Content-Type", "text/xml").and().body(input).when().post("/calculator.asmx")
				.then().statusCode(200).and().log().all().extract().response();

		XmlPath xmlpath = new XmlPath(response.asString());// Converting string into xml path to assert
		String rate = xmlpath.getString("AddResult");
		Assert.assertEquals("10", rate);
		System.out.println("Add value returned is: " + rate);
	}

}
