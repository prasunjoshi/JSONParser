package com.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.object.JSONObject;

public class JSONParser {
	
	public static final char EMPTY_CHAR = '\u0000';
	
	public String jsonString;
	public JSONObject jobj;
	public int index;
	public char ch;
	
	public JSONParser(String jsonString) {
		// TODO Auto-generated constructor stub
		this.jsonString = jsonString;
		this.index = 0;
		this.ch = jsonString.charAt(index);
	}
	
	public void next()
	{
		
		if(index < jsonString.length()-1) {
			ch = jsonString.charAt(++index);
			System.out.print(""+ch);
			
		}
		else ch = EMPTY_CHAR;
	}

	public boolean validate() {
		
		return isValidValue();
	}
	
	private boolean isValidString() {
		// TODO Auto-generated method stub
		System.out.println("in isVAlidString " + ch);
		
		HashMap<Character, Character> escapes = new HashMap<Character, Character>(){{
													put('b','\b');
													put('n','\n');
													put('t','\t');
													put('r','\r');
													put('f','\f');
													put('\"','\"');
													put('\\','\\');
												}};
		
		System.out.println("in isVAlidString " + ch);
		String str = "";
		if(ch == '\"')
		{
			System.out.println("in isVAlidString2 " + ch);
			next();
			while(index < jsonString.length() && ch != EMPTY_CHAR){
				if(ch == '\"'){
					next();
					return true;
				}
				if(ch == '\\'){
					next();
					if(escapes.containsKey(ch))
					{
						str += escapes.get(ch);
					}
					else
					{
						str += ch;
					}
				}
				else
				{
					str += ch;
				}
				next();
		  }
			System.out.println("returning from isValidString");
			return false;
	  }
		else
		{
			System.out.println("returning from else of isValidString");
			return false;
		}
	}

	private String getDigits()
	{
		String num = "";
		while(ch>='0' && ch<='9')
		{
			num += ch;
			next();
		}
		return num;
	}
	
	private boolean isValidNumber() {
		String num = "";
		if(ch == '-')
		{
			num += '-';
			next();
		}
		num = getDigits();
		if(ch == '.')
		{
			num += '.';
			num += getDigits();
		}
		if(ch == 'e' || ch == 'E')
		{
			num += ch;
			next();
		}
		num += getDigits();
		try {
			Double d = Double.parseDouble(num);
			if(d.isNaN())
			{
				System.out.println("returning from isValidNumber NAN");
				return false;
			}
			return true;			
		} catch (Exception e) {
			// TODO: handle exception
			//error
			System.out.println("returning from isValidNumber nan excpetion");
			return false;
		}
	}

	private boolean isValidNull() {

/*		
 * 			We have encountered n till here now  
 * 		the next 3 chars should be 'ull' for valid json
 */
		
		String nully = "";
		int tmp=3;
		while(tmp-- > 0)
		{
			next();
			nully += ch;
		}
		if(nully.equals("ull")){
			next();
			return true;
		}
		System.out.println("returning from isValidNull");
		return false;
	}

	private boolean isValidBool() {
		String truly = "t";
		int tmp=3;
		char i = ch;
		while(i == 't' && tmp-- > 0)
		{
			next();
			truly += ch;
		}
		if(truly.equals("true"))
		{
			next();
			return true;
		}
		String falsy = "f";		
		tmp = 4;
		while(i =='f' && tmp-- > 0)
		{
			next();
			falsy += ch;
		}
		if(falsy.equals("false")){
			next();
			return true;
		}

			System.out.println("returning from isValidBool" + falsy);
		return false;
	}

	private boolean isValidArray() {
		// TODO Auto-generated method stub
		ArrayList<Character> arr = new ArrayList<Character>();
		if(ch == '[')
		{
			next();
			
			if(ch == ']')
				return true;
			
			do{
				if(ch == ',')	next();
				if(isValidValue()){
				arr.add(ch);
				if(ch == ']')
				{
					next();
					return true;
				}
				}
//				next();
			}while(ch == ',');
			System.out.println("returning from isValidArray if");
			return false;
		}
		else
		{
			System.out.println("returning from isValidArray else");
			return false;			
		}
	}

	private boolean isValidObject() {
		// TODO Auto-generated method stub
		if(ch == '{')
		{
			next();
			if(ch == '}')
			{
				next();
				return true;				
			}
			do
			{
				if(ch == ',') next(); //new
				System.out.println("in object do ka , wala next"+ch);
				if(isValidString()){
					if(ch != ':'){
						//error
						System.out.println("returning from isValidObject ch ':' case");
						return false;
					}
					next();
					if(isValidValue()){
						if(ch == '}')
						{
							next();
							return true;
						}
					}
					else{
							System.out.println("returning from isValidObject isValidVAlue else"+ch);
												//error
						return false;
					}
				}
				else{
					System.out.println("returning from isValidObject isValidString else");
					return false;
				}
//				next();
				System.out.println("next before while of object " + ch);
			}while((Character)ch != null && ch == ',');
			System.out.println("returning from isValidObject after do while"+ch);
			return false;
		}
		else
		{
			System.out.println("returning from isValidObject last else");
			return false;
		}
	}

	private boolean isValidValue() {
		// TODO Auto-generated method stub
		switch(ch){
		case '{':{
			if(!isValidObject())
				return false;
			break;
		}
			
		case '[':{
			if(!isValidArray())
				return false;
			break;
		}
		
		case '\"':{
			if(!isValidString())
				return false;
			break;
		}
		case 't':
		case 'f':{
			if(!isValidBool())
				return false;
			break;
		}
		case 'n':
		{
			if(!isValidNull())
				return false;
			break;
		}
		default:{
			System.out.println("in default of is valid value1");
			
			if(ch == '-' || ch>='0' && ch <= '9')
			{
				System.out.println("in default of is valid value");
				if(!isValidNumber())
					return false;
				else return true;
			}
		}
	}
	return true;
	}

	public JSONObject parseJSON() {
		return null;
	}
}
