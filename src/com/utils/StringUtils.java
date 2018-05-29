package com.utils;

public class StringUtils {
	 public static boolean isNullOrEmpty(String str){
	   	  if( str!=null&&!"".equals(str.trim()))
	   		  return false;
	   	  return true;
    }
	 
	  public static String leftpad(String source,int len,String padding){		
			if(source==null||source.length()>=len||padding==null||padding.length()==0)
				return source;
			String target=source;
			for(int w=len-source.length();w>0;w=w-padding.length()){
				target=padding+target;
			}
			return target;
		}
}
