package com.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.exception.JSONValidatorException;
import com.object.JSONObject;

public class JSONParser {
	
	//contents of the JSON file in a string
	String jsonString;
	
	//index of the jsonString
	static int i = 0;
	
	//read jsonString character by character
	private char ch;
	
	//invalid character or end of file
	private static final char EMPTY_CHAR = '\u0000';
	
	public JSONParser(String s) {
		jsonString = s;
		ch = jsonString.charAt(0);
	}
	
	/**
	 * 
	 * @return true : jsonString is correct
	 */
	public boolean validate() {
		try {
			return isValidValue();
		}catch(JSONValidatorException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Go to next character in the jsonString
	 */
	private void next()
	{
		
		if(i < jsonString.length()-1) {
			ch = jsonString.charAt(++i);
			
		}
		else ch = EMPTY_CHAR;
	}
	
	/**
	 * Checks whether the string is a valid value conforming to JSON Grammar
	 * 
	 * @return true: Valid value in json grammar
	 * @throws JSONValidatorException
	 */
	private boolean isValidValue() throws JSONValidatorException {

		switch(ch) {
			case '{': {
				if(!isValidObject())
					return false;
				break;
			}
			case '[': {
				if(!isValidArray())
					return false;
				break;
			}
			case '\"': {
				if(!isValidString())
					return false;
				break;
			}
			case 't': 
			case 'f': {
				if(!isValidBool()) 
					return false;
				break;
			}
			case 'n': {
				if(!isValidNUll()) 
					return false;
				break;
			}
			default: { 
				if(ch == '-' || (ch >= '0' && ch <= '9')) { 
					if(!isValidNumber())
						return false;
					return true;
				} else {
					throw new JSONValidatorException("Something wrong at position: " + (i+1));
				}
			}
		}
		return true;
	}
	
	
	/**
	 * Checks whether the string is a valid object conforming to JSON Grammar
	 * 
	 * @return true: Valid object in json grammar
	 * @throws JSONValidatorException
	 */
	private boolean isValidObject() throws JSONValidatorException {
		
		
		if(ch == '{') {
			next();
			
			if(ch == '}') {
				next();
				return true; 
			}
		
			do {
				if(ch == ',') 
					next();
				if(isValidString()) {
					if(ch != ':'){
						throw new JSONValidatorException("Object property expecting \":\" at position: " + (i+1));
					}
					next();
					if(isValidValue()) { 
						if(ch == '}') {  
							next();
							return true;
						}
					}
					else {
						throw new JSONValidatorException("Invalid value of object at position: " + (i+1));
					}
				}
				else {
					throw new JSONValidatorException("Invalid string of object at position: " + (i+1));
				}
			} while(ch == ','); 

			return false;
		}
		else
			return false;
	}
	
	/**
	 * Checks whether the string is a valid array conforming to JSON Grammar
	 * 
	 * @return true: Valid array in json grammar
	 * @throws JSONValidatorException
	 */
	private boolean isValidArray() throws JSONValidatorException {
		ArrayList<Character> arr = new ArrayList<Character>();
		
		if(ch == '[') {
			next();
			if(ch == ']')
				return true;
			
			do {
				if(ch == ',')	next();
				if(isValidValue()){
				arr.add(ch);
				if(ch == ']')
				{
					next();
					return true;
				}
				}
			}while(ch == ',');
			
			throw new JSONValidatorException("Invalid array at position: " + (i+1));
		}
		else
			return false;
	}
	
	/**
	 * Checks whether the string is a valid string conforming to JSON Grammar
	 * 
	 * @return true: Valid string in json grammar
	 * @throws JSONValidatorException
	 */
	private boolean isValidString() throws JSONValidatorException {
		@SuppressWarnings("serial")
		HashMap<Character, Character> escapes = new HashMap<Character, Character>(){{
												put('b', '\b');
												put('n', '\n');
												put('t', '\t');
												put('r', '\r');
												put('f', '\f');
												put('\"', '\"');
												put('\\', '\\');
												}};
		
		@SuppressWarnings("unused")
		String s = "";
		
		if(ch == '\"') {
			next();
			
			while(i < jsonString.length() && ch != EMPTY_CHAR){
				if(ch == '\"'){
					next();
					return true;
				}
				if(ch == '\\'){
					next();
					if(escapes.containsKey(ch))
					{
						s += escapes.get(ch);
					}
					else
					{
						s += ch;
					}
				}
				else
				{
					s += ch;
				}
				next();
			}
			
			throw new JSONValidatorException("Invalid string at position: " + (i+1));
		}
		else
			throw new JSONValidatorException("Invalid string at position: " + (i+1));
	}
	
	/**
	 * Checks whether the string is a valid boolean conforming to JSON Grammar
	 * 
	 * @return true: Valid boolean in json grammar
	 * @throws JSONValidatorException
	 */
	private boolean isValidBool() throws JSONValidatorException {
		if(ch == 't') {
			next();
			if(ch == 'r') {
				next();
				if(ch == 'u') {
					next();
					if(ch == 'e') {
						next();
						return true;
					}
					else
						throw new JSONValidatorException("Invalid Boolean at position: " + (i+1));
				}
				else
					throw new JSONValidatorException("Invalid Boolean at position: " + (i+1));
			}
			else
				throw new JSONValidatorException("Invalid Boolean at position: " + (i+1));
		}
		else if(ch == 'f') {
			next();
			if(ch == 'a') {
				next();
				if(ch == 'l') {
					next();
					if(ch == 's') {
						next();
						if(ch == 'e') {
							next();
							return true;
						}
						else
							throw new JSONValidatorException("Invalid Boolean at position: " + (i+1));
					}
					else
						throw new JSONValidatorException("Invalid Boolean at position: " + (i+1));
				}
				else
					throw new JSONValidatorException("Invalid Boolean at position: " + (i+1));
			}
			else
				throw new JSONValidatorException("Invalid Boolean at position: " + (i+1));
		}
		
		return false;
	}
	
	/**
	 * Checks whether the string is a valid null conforming to JSON Grammar
	 * 
	 * @return true: Valid null in json grammar
	 * @throws JSONValidatorException
	 */
	private boolean isValidNUll() throws JSONValidatorException{
		next();
		if(ch == 'u') {
			next();
			if(ch == 'l') {
				next();
				if(ch == 'l') {
					next();
					return true;
				}
				else 
					throw new JSONValidatorException("Invalid NULL at position: " + (i+1));
					
			}
			else
				throw new JSONValidatorException("Invalid NULL at position: " + (i+1));
		}
		else
			throw new JSONValidatorException("Invalid NULL at position: " + (i+1));
	}
	
	/**
	 * Helper method for isValidNumber()
	 * @return: digits in String format
	 */
	private String getDigits() {
		String num = "";
		while(ch>='0' && ch<='9')
		{
			num += ch;
			next();
		}
		return num;
		
	}
	
	/**
	 * Checks whether the string is a valid number conforming to JSON Grammar
	 * 
	 * @return true: Valid number in json grammar
	 * @throws JSONValidatorException
	 */
	private boolean isValidNumber() {
		String num = "";
		
		//If a number starts with negative sign
		if(ch == '-') {
			num += ch;
			next();
		}
		
		//get digits of the number
		num += getDigits();
		
		//get decimal point if there
		if(ch == '.') {
			num += ch;
			next();
			getDigits();
		}

		//get exponential if there
		if(ch == 'e' || ch == 'E') {
			num += ch;
			next();
			
			// required - get sign of exponent
			if(ch == '-' || ch == '+') {
				num += ch;
				next();
			}
			getDigits(); // exponent
		}
		
		try {
			Double d = Double.parseDouble(num);
			if(d.isNaN())
				return false;
			else
				return true;
		}catch(NumberFormatException e) {
			System.out.println("Invalid Number at position: " + (i+1));
			return false;
		}
	}
	
	/**
	 * parse the string and create a json object if the string is valid
	 * 
	 * @return: valid JSON object
	 */
	public JSONObject parseJson() {
		//TODO
		return null;
	}	
}