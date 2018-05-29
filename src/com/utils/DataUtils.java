package com.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class DataUtils {
	 public static Map<String,String> getData(String input){
		 Map<String,String> result=new HashMap<String,String>();
		 if(input== null || input.trim().length() == 0) {
	            return result;
	      }
		 String[] items=input.split("&");
		 for(String item:items){
			 String[] kv=item.split("=");
			 if(kv.length==2){
				 try{
					 String key=URLDecoder.decode( kv[0],"utf8");
					 String value=URLDecoder.decode( kv[1],"utf8");
					 result.put(key,value);
				 }catch (UnsupportedEncodingException e) {
					 
				 }
			 }
		 }
		 return result;
	 }
}
