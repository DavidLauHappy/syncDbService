package com.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Logger {
	
	//记录通用日志的方法
	public static void log(String info){
		try{
			String logpath=Paths.getInstance().getLogPath()+File.separator+DateUtil.getCurrentDate("yyyy-MM-dd")+".log";
			String currentTime=DateUtil.getCurrentDate("yyyyMMdd-HH:mm:ss.SSS");
			 File file=new File(logpath);
			   FileWriter fw = new FileWriter(file,true);
			   BufferedWriter bw = new BufferedWriter(fw);
			   bw.write(currentTime);
			   bw.write(":");
			   bw.write(info);
			   bw.write("\r\n");
			   bw.close();
			   fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void logService(String serviceName,String info){
			try{
				String logDir=Paths.getInstance().getLogPath()+File.separator+DateUtil.getCurrentDate("yyyy-MM-dd");
				File dir=new File(logDir);
			    if(!dir.exists())
			    	dir.mkdirs();
			    String logPath=logDir+File.separator+ serviceName+".log";
			    String currentTime=DateUtil.getCurrentDate("yyyyMMdd-HH:mm:ss.SSS");
				 File file=new File(logPath);
				   FileWriter fw = new FileWriter(file,true);
				   BufferedWriter bw = new BufferedWriter(fw);
				   bw.write(currentTime);
				   bw.write(":");
				   bw.write(info);
				   bw.write("\r\n");
				   bw.close();
				   fw.close();
			}catch(Exception e){
				e.printStackTrace();
			}
	  }
	
	
		public static void error(String info){
			try{
				String logpath=Paths.getInstance().getLogPath()+File.separator+DateUtil.getCurrentDate("yyyy-MM-dd")+".error";
				String currentTime=DateUtil.getCurrentDate("yyyyMMdd-HH:mm:ss.SSS");
				 File file=new File(logpath);
				   FileWriter fw = new FileWriter(file,true);
				   BufferedWriter bw = new BufferedWriter(fw);
				   bw.write(currentTime);
				   bw.write(":");
				   bw.write(info);
				   bw.write("\r\n");
				   bw.close();
				   fw.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
}
