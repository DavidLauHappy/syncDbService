package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
		//yyyyMMddHHmmssSSS
		public static String getCurrentDate(String form) {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat(form);
			String staticsDate= format.format(calendar.getTime());
			return staticsDate;
		}
		
		//获取当前时间的数字
		public static long getCurrentTimeLong(String form) {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat(form);
			String staticsDate= format.format(calendar.getTime());
			staticsDate=staticsDate.replace("-", "");
			staticsDate=staticsDate.replace(":", "");
			staticsDate=staticsDate.replace(".", "");
			return Long.parseLong(staticsDate);
		}
		
		//文件最后修改时间转出日期字符串
		public static String getTimeFormLong(long inputLong){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(inputLong);
			String time=sdf.format(cal.getTime());
			return time;
		}
		
		public static long getTimeLongFromString(String timeString){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
			Date date=null;
			long result=0;
			try{
				date = sdf.parse(timeString);  
				  result=date.getTime();
			}catch (ParseException e) {
				e.printStackTrace();
			}
			 return result;
		}
		
		public static String getDateFromDateStr(String workDate){
			  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
			  SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd"); //加上时间
			  try {
		            Date date=simpleDateFormat.parse(workDate);
		           return sDateFormat.format(date);
		        } catch(ParseException px) {
		            px.printStackTrace();
		        }
			  return workDate;
		}
}
