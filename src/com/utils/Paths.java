package com.utils;

import java.io.File;

import com.rsc.Context;

public class Paths {
	  private static Paths uniqueInstance;
	  public static Paths getInstance(){
		  	if(uniqueInstance==null)
		  		uniqueInstance=new Paths();
		  	return uniqueInstance;
	  }
	  
	  public String getLogPath() {
			return logPath;
		}

		public String getWorkDir() {
			return workDir;
		}

		public String getConfigPath() {
			return configPath;
		}
		
	  private Paths(){
		  this.basePath=FileUtils.formatPath(Context.StartPath)+File.separator;
		  this.init();
	  } 
	  

	  
	  private void init(){
		  this.logPath=this.basePath+"log"+File.separator;
		  this.configPath=this.basePath+"config"+File.separator;
		  this.workDir=this.basePath+"workDir"+File.separator;
		  File logDir=new File(this.logPath);
		  if(!logDir.exists())
			  logDir.mkdirs();
		  File cfgDir=new File(this.configPath);
		  if(!cfgDir.exists())
			  cfgDir.mkdirs();
		  File workDir=new File(this.workDir);
		  if(!workDir.exists())
			  workDir.mkdirs();
	  }
	  
	  private String basePath;
	  private String logPath;
	  private String workDir;
	  private String configPath;
	  
}
