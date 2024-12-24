package com.notes;

import org.testng.annotations.DataProvider;

public class Notes_TestData {
	
	@DataProvider(name="create_note")
	public Object[][] createNotes(){
		// Two dimensional object
		// 2X2 , 2X3, 3X4
	    return new Object[][] {
	            {"Home","Home_API","Done for Home-Sha"},
	            {"Work","Work_API","Done for Work-Sha"},
	            {"Personal","Personal_API","Done for Personal-Sha"}
	            };
	}
	
	@DataProvider(name="create_note_negative_tcs")
	public Object[][] createNotes_negative(){
		// Two dimensional object
	    return new Object[][] {
	            {"Home","Hom","Done for Home","Title must be between 4 and 100 characters"},
	            {"Work","Work_API","","Description must be between 4 and 1000 characters"},
	            {"Work","Work_API","Des","Description must be between 4 and 1000 characters"},
	            {"Personal","","Done for Personal","Title must be between 4 and 100 characters"},
	            {"","Title","Done for Personal","Category must be one of the categories: Home, Work, Personal"}
	            };
	}
	
	@DataProvider(name="LogInTestData")
	public Object[][] LogInTestData(){
		// Two dimensional object
	    return new Object[][] {
	    		{"shashank@abc.com","shashank",200,"Login successful"},
	    		{"","shashank",400,"A valid email address is required"},
	    		{"shashank@abc.com","",400,"Password must be between 6 and 30 characters"},
	    		{"shashank@abccom","shashank",400,"A valid email address is required"},
	    		{"shashank@abc.com","asd",400,"Password must be between 6 and 30 characters"}
	            };
       
	}
	
	@DataProvider(name="All_Soap_Requests_tcs")
	public Object[][] allSoapRequestData(){
		// Two dimensional object
	    return new Object[][] {
	            {"AddResult","SoapAddRequestFile.xml","10"},
	            {"SubtractResult","SoapSubtractRequestFile.xml","5"},
	            {"MultiplyResult","SoapMultiplyRequestFile.xml","25"},
	            {"DivideResult","SoapDivideRequestFile.xml","2"}
	    };
	}

}
