package com.rsc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.utils.Paths;

import bean.Task;

public class Context {
	//数据库连接池最小连接数
	 public static int ServerDbMinConns=2;
	 //数据库配置
	 public static String SqlServerIp="";
     public static String ServerSqlPort="1433";
     public static String ServerDbName="";
     public static String ServerDbUser="sa";
     public static String ServerDbPasswd="";
     public static String outShareFloder="";
     public static String outShareUser="";
     //ftp服务器信息
     public static String FTPServerIp="";
     public static String FTPServerPort="";
     public static String ServerFTPUser="";
     public static String ServerFTPasswd="";
     
     //固定密钥
     public static String EncryptKey="Gorgeous";
     //起始工作目录
     public static String StartPath="";
     //安渡配置信息
     public static String UniNXG_URL="";//安渡分派的用户名
     public static String UniNXG_ShareUser="";//安都分配的对方的用户名
     public static String apiKey="";	//安渡分配给系统的key
     //public static String securityKey="";
     public static String UniNXG_IP_OUT="";//安渡交换网络的设备外网ip
     public static String UniNXG_IP_IN="";//安渡交换网络的设备内网ip
     public static String UniNXG_DIR="";//安渡服务器上的共享目录，
     //加载文件列表
     public static List<Task> FilesIn=new ArrayList<Task>();
     //导出文件列表
     public static List<Task> FilesOut=new ArrayList<Task>();
     //工作日期
     public static String workDate="";
     //工作时间
     public static String startTime="";
     //HTTP服务启动标识
     public  static String httpServerEnable="No";
     //服务轮询频率
     public static int sleepSecond=0;
     //是否可以运行
     public static boolean isRunable(){
    	 String lockFile=Paths.getInstance().getConfigPath()+"lock.ini";
    	 File file=new File(lockFile);
    	 if(file.exists()){
    		 return false;
    	 }else{
    		 return true;
    	 }
     }
}
