package app;

import http.SimpleHTTPServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import svc.FileOutService;
import svc.FilesInService;

import bean.Task;

import com.db.DBConnectionManager;
import com.rsc.Context;
import com.utils.DateUtil;
import com.utils.Logger;
import com.utils.Paths;
import com.utils.SqlServerUtil;
import com.utils.StringUtils;


public class App {
	
	private  static App app=null;
	
	public static void main(String[] args){
		if("start".equalsIgnoreCase(args[0])){
			App.getInstance().start();
		}else if("stop".equalsIgnoreCase(args[0])){
			App.getInstance().stop();
			System.exit(0);
		}
	}
	
	
	
	public  void start(){
		//初始化路径
		App.getInstance().setStartPath();
		//加载配置
		App.getInstance().loadConfig();
		App.getInstance().loadFtpFromDb();
		//启动运行删除锁文件
		App.getInstance().unlock();
		//启动运行
		App.getInstance().run();
	}
	
	
	private void stop(){
		App.getInstance().setStartPath();
		App.getInstance().lock();
		//停止httpserver服务线程
		
	}
	
	private void run(){
		while(Context.isRunable()){
			if("yes".equalsIgnoreCase(Context.httpServerEnable)){
				SimpleHTTPServer.getInstance().run();
			}
			//日切时间段判断
			String nowDate=DateUtil.getCurrentDate("yyyyMMdd");
			String currTime=DateUtil.getCurrentDate("HH:mm:ss");
			currTime=currTime.replace(":", "");
			String startTime=Context.startTime.replace(":", "");
			if(nowDate.compareTo(App.getInstance().systemDate)>0){
				Logger.log("系统日期发生跳转，准备数据同步服务……");
			   //发起文件导出
				FileOutService.getInstance().setWorkDate(App.getInstance().systemDate);
				FileOutService.getInstance().run();
				//发起文件加载(控制时间点)
				FilesInService.getInstance().setWorkDate(App.getInstance().systemDate);
				FilesInService.getInstance().run();
				//守护结果后进行日切
				App.getInstance().systemDate=nowDate;
			}else if(nowDate.compareTo(App.getInstance().systemDate)==0&&currTime.compareTo(startTime)>0){
				    Logger.log("满足当日服务启动时间，准备数据同步服务……");
				   //发起文件导出
					FileOutService.getInstance().setWorkDate(App.getInstance().systemDate);
					FileOutService.getInstance().run();
					//发起文件加载(控制时间点)
					FilesInService.getInstance().setWorkDate(App.getInstance().systemDate);
					FilesInService.getInstance().run();
					//守护结果后进行日切
					String nextDate=getNextDate(nowDate);
					App.getInstance().systemDate=nextDate;
			}else{
				
			}
			try {
				TimeUnit.SECONDS.sleep(Context.sleepSecond);
			} catch (InterruptedException e) {
				 Logger.log("syncDbService睡眠等待发生异常……");
			}
		}
		//服务结束是释放连接池
		DBConnectionManager.getInstance().release();//释放连接池
	}
	
	
	public static String getNextDate(String date){
		DateFormat format = new SimpleDateFormat("yyyyMMdd");      
		Calendar   calendar=Calendar.getInstance();    
		Date dateObj=null;
		String nextDate=date;
		try {
			dateObj = format.parse(date);
			calendar.setTime(dateObj);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			nextDate= format.format(calendar.getTime());
		}catch (ParseException e) {
			e.printStackTrace();
		} 
		return nextDate;
	}
	
	private void lock(){
		try{
			String lockFile=Paths.getInstance().getConfigPath()+"lock.ini";
			String currentTime=DateUtil.getCurrentDate("yyyyMMdd-HH:mm:ss.SSS");
		   File file=new File(lockFile);
		   FileWriter fw = new FileWriter(file,true);
		   BufferedWriter bw = new BufferedWriter(fw);
		   bw.write(currentTime);
		   bw.write("\r\n");
		   bw.close();
		   fw.close();
		   Logger.log("syncDbService发起外部退出指令完成……");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void unlock(){
		try{
			String lockFile=Paths.getInstance().getConfigPath()+"lock.ini";
		    File file=new File(lockFile);
		    if(file.exists()){
		    	file.delete();
		    }
		   Logger.log("syncDbService启动前解除外部退出指令完成……");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private  void setStartPath(){
		 String path="";
		 try {
				path= System.getProperty("user.dir");//这种不支持中文路径吧
				String pathClass= URLDecoder.decode(this.getClass().getClassLoader().getResource("app/App.class").getPath(),"UTF-8");
				Context.StartPath=path;
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	private boolean loadConfig(){
		    SAXBuilder builder = new SAXBuilder();
			Document doc;
			String initPath="";
			boolean result=true;
			try {
				initPath=Paths.getInstance().getConfigPath()+"start.xml";
				doc = builder.build(initPath);
				Element sysParams=doc.getRootElement();
				Element dataIn=sysParams.getChild("dataIn");
				if(dataIn!=null){
					//安渡信息
					Attribute attrUniNXG_URL=	dataIn.getAttribute("UniNXG_URL");
					if(attrUniNXG_URL!=null){
						String UniNXG_URL=attrUniNXG_URL.getValue();
						Context.UniNXG_URL=UniNXG_URL;
					}
					Attribute attrUniNXG_SharedUser=	dataIn.getAttribute("UniNXG_SharedUser");
					if(attrUniNXG_SharedUser!=null){
						String UniNXG_ShareUser=attrUniNXG_SharedUser.getValue();
						Context.UniNXG_ShareUser=UniNXG_ShareUser;
					}	
					Attribute attrapiKey=	dataIn.getAttribute("apiKey");
					if(attrapiKey!=null){
						String apiKey=attrapiKey.getValue();
						Context.apiKey=apiKey;
					}
					/*Attribute attrsecurityKey=	dataIn.getAttribute("securityKey");
					if(attrsecurityKey!=null){
						String securityKey=attrsecurityKey.getValue();
						Context.securityKey=securityKey;
					}*/
					Attribute attrUniNXG_IP_OUT=	dataIn.getAttribute("UniNXG_IP_OUT");
					if(attrUniNXG_IP_OUT!=null){
						String UniNXG_IP_OUT=attrUniNXG_IP_OUT.getValue();
						Context.UniNXG_IP_OUT=UniNXG_IP_OUT;
					}
					
					Attribute attrUniNXG_IP_IN=	dataIn.getAttribute("UniNXG_IP_IN");
					if(attrUniNXG_IP_IN!=null){
						String UniNXG_IP_IN=attrUniNXG_IP_IN.getValue();
						Context.UniNXG_IP_IN=UniNXG_IP_IN;
					}
					Attribute attrUniNXG_DIR=	dataIn.getAttribute("UniNXG_DIR");
					if(attrUniNXG_DIR!=null){
						String UniNXG_DIR=attrUniNXG_DIR.getValue();
						Context.UniNXG_DIR=UniNXG_DIR;
					}
					//加载文件信息
					List<Element> inFiles=dataIn.getChildren();
					if(inFiles!=null){
						for(Element ele:inFiles){
							Task file=new Task();
							file.setId(ele.getAttributeValue("id"));
							file.setName(ele.getAttributeValue("fileName"));
							file.setDesc(ele.getAttributeValue("desc"));
							file.setPath(ele.getAttributeValue("path"));
							file.setClassID(ele.getAttributeValue("functionClass"));
							Context.FilesIn.add(file);
						}
					}
				}
				Element dataOut=sysParams.getChild("dataOut");
				if(dataOut!=null){
					//数据库信息
					Attribute attrdbServer=	dataOut.getAttribute("dbServer");
					if(attrdbServer!=null){
						String SqlServerIp=attrdbServer.getValue();
						Context.SqlServerIp=SqlServerIp;
					}
					Attribute attrport=dataOut.getAttribute("port");
					if(attrport!=null){
						String ServerSqlPort=attrport.getValue();
						Context.ServerSqlPort=ServerSqlPort;
					}
					Attribute attruser=dataOut.getAttribute("user");
					if(attruser!=null){
						String ServerDbUser=attruser.getValue();
						Context.ServerDbUser=ServerDbUser;
					}
					Attribute attrpasswd=dataOut.getAttribute("passwd");
					if(attrpasswd!=null){
						String ServerDbPasswd=attrpasswd.getValue();
						Context.ServerDbPasswd=ServerDbPasswd;
					}
					Attribute attrdbname=dataOut.getAttribute("dbname");
					if(attrdbname!=null){
						String ServerDbName=attrdbname.getValue();
						Context.ServerDbName=ServerDbName;
					}
					Attribute attrshareFolder=dataOut.getAttribute("shareFolder");
					if(attrshareFolder!=null){
						String outShareFloder=attrshareFolder.getValue();
						Context.outShareFloder=outShareFloder;
					}
					Attribute attrshareUser=dataOut.getAttribute("shareUser");
					if(attrshareUser!=null){
						String outShareUser=attrshareUser.getValue();
						Context.outShareUser=outShareUser;
					}
					//加载文件信息
					List<Element> outFiles=dataOut.getChildren();
					if(outFiles!=null){
						for(Element ele:outFiles){
							Task file=new Task();
							file.setId(ele.getAttributeValue("id"));
							file.setName(ele.getAttributeValue("fileName"));
							file.setDesc(ele.getAttributeValue("desc"));
							file.setPath(ele.getAttributeValue("path"));
							file.setClassID(ele.getAttributeValue("functionClass"));
							Context.FilesOut.add(file);
						}
					}
				}
				Element http=sysParams.getChild("httpserver");
				if(http!=null){
					Attribute attrenable=http.getAttribute("enable");
					if(attrenable!=null){
						String enable=attrenable.getValue();
						Context.httpServerEnable=enable;
					}
				}
				Element service=sysParams.getChild("service");
				if(service!=null){
					Attribute frequency=service.getAttribute("enable");
					if(frequency!=null){
						String fre=frequency.getValue();
						int seconds=Integer.parseInt(fre);
						Context.sleepSecond=seconds;
					}
					Attribute attrWorkdate=service.getAttribute("workdate");
					if(attrWorkdate!=null){
						String workdate=attrWorkdate.getValue();
						Context.workDate=workdate;
					}
					Attribute attrStartTime=service.getAttribute("startTime");
					if(attrStartTime!=null){
						String startTime=attrStartTime.getValue();
						Context.startTime=startTime;
					}
				}
				if(StringUtils.isNullOrEmpty(Context.workDate)){
					this.systemDate=DateUtil.getCurrentDate("yyyyMMdd");
				}
				else{
					this.systemDate=Context.workDate;
				}
				if(StringUtils.isNullOrEmpty(Context.startTime)){
					Context.startTime="23:59:00";
				}
			}  
		  catch(Exception e){
			  result=false;
			}
		return result;	
	 }
	
	private boolean loadFtpFromDb(){
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String query="select  NAME,VALUE from SYS_PARAMETER";
			List result=SqlServerUtil.executeQuery(query, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> parameters=(Map)result.get(w);
					String name=parameters.get("NAME");
					String value=parameters.get("VALUE");
					value=value.trim();
					 if(("FTPServerIp").equals(name)){
						   String FTPServerIp=value;
						   Context.FTPServerIp=FTPServerIp;
						} 
					   if(("FTPServerPort").equals(name)){
						   String FTPServerPort=value;
						   Context.FTPServerPort=FTPServerPort;
						} 
					   if(("ServerFTPUser").equals(name)){
						   String ServerFTPUser=value;
						   Context.ServerFTPUser=ServerFTPUser;
						}   
					   if(("ServerFTPasswd").equals(name)){
							   String ServerFTPasswd=value;
							   Context.ServerFTPasswd=ServerFTPasswd;
						} 
				}
			}
		}catch(Exception e){
			 return false;
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return true;
	}
	
	
	
	private App(){
		
	}
	
	public static App getInstance(){
		if(app==null)
			app=new App();
		return app;
	}
	
	public String systemDate;
}
