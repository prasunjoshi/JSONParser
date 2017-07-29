package com.driver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.parser.JSONParser;

public class MyMain {
	public static void main(String args[])
	{
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(args[0]));
			String line,jsonString = "";
			System.out.println(args[0]);
			try {
				while((line = br.readLine()) != null)
				{
					jsonString += line;
				}
				jsonString = jsonString.replaceAll("\\s+", "");
				JSONParser jsonParser = new JSONParser(jsonString);				
				boolean valid = jsonParser.validate();
				if(valid) System.out.println("Json file is valid");
				else System.out.println("Json file is invalid");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
