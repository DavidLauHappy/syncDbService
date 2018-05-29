package svc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.rsc.Context;
import com.utils.Logger;
import com.utils.Paths;


public class UniExApi {
		public static final String serviceName="UniExService";
		//上传文件到安渡文档服务器
		 public static boolean uplaodFile(String filePath){
			 	String url="http://@IP:81/UniExServices/openAPI/file/uploadFile.htm";
			 	url=url.replace("@IP", Context.UniNXG_IP_OUT);
			 	String spath=filePath.substring(filePath.indexOf(":"));
			 	spath=spath.replace(":", "");
			 	spath=Context.outShareUser+spath;
			 	spath=spath.replace('\\','/');
			 	spath=spath.replace("//", "/");
			 	//参数拼装
			 	JSONObject json=new JSONObject();
			 	json.put("spath", spath);
			 	json.put("stype", "1");//共享文件夹
				json.put("uname", Context.UniNXG_URL);//用户名
				json.put("tpath", "/"+Context.UniNXG_DIR+"/");//文件存放的目标目录
				json.put("tshare", Context.UniNXG_ShareUser);//用户名
				json.put("apikey", Context.apiKey);
				Logger.logService(serviceName, "调用接口："+url+",参数："+json.toString());
				String result=UniExApi.doHttpPost(json.toString(), url);
				Logger.logService(serviceName, "调用接口："+url+",参数："+json.toString()+"，返回："+result);
				if(!StringUtils.isEmpty(result)){
					JSONObject retJson=JSONObject.fromObject(result);
					String status=(String)retJson.get("status");
					String msg=(String)retJson.get("msg");
					if("SUCCESS".equalsIgnoreCase(status)){
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
		 }
		 
		 //从安渡下载文件到哪里
		 public static boolean downloadFile(String fileName){
			    String uniExURL="http://@IP:81/UniExServices/openAPI/file/downloadFile.htm";
			    uniExURL=uniExURL.replace("@IP", Context.UniNXG_IP_IN);
			 	String fname=Context.UniNXG_DIR+File.separator+fileName;
			 	fname=fname.replace('\\', '/');
			 	fname=fname.replace("//", "/");
			 	fname="/"+fname;
			 	String params="uname=@uname&suname=@suname&fname=@fname&apikey=@apikey";
			 	params=params.replace("@uname", Context.UniNXG_ShareUser);
			 	params=params.replace("@suname",  Context.UniNXG_URL);
			 	//params=params.replace("@suname", "");
			 	params=params.replace("@fname", fname);
			 	params=params.replace("@apikey", Context.apiKey);
				Logger.logService(serviceName, "调用接口："+uniExURL+",参数："+params);
				 HttpURLConnection urlCon=null;
				 try{
					 	URL url = new URL(uniExURL);
			            urlCon = (HttpURLConnection)url.openConnection();
			            urlCon.setDoOutput(true);
			            urlCon.setDoInput(true);
			            urlCon.setRequestMethod("POST");
			            urlCon.setUseCaches(false);
			            urlCon.setRequestProperty("content-Type", "application/x-www-form-urlencoded");
			            urlCon.setRequestProperty("Accpet-charset", "UTF-8");
			            urlCon.setRequestProperty("Content-length",String.valueOf(params.length()));
			            PrintStream ps=new PrintStream(urlCon.getOutputStream());
			            ps.print(params);
			            ps.close();
			            if(urlCon.getResponseCode()!=200){
			            	Logger.error("安渡接口调用："+uniExURL+"参数："+params+"发生异常：http error code "+urlCon.getResponseCode());
			            	return false;
			            }
			            else{
			            	Map<String,List<String>> map=urlCon.getHeaderFields();
			            	String result=map.get("Content-Result").get(0);
			            	if("SUCCESS".equalsIgnoreCase(result)){
			            		Logger.logService(serviceName, "调用接口："+uniExURL+",参数："+params+"返回成功");
			            		 InputStream instream= urlCon.getInputStream();//文件内容
			            		 if(instream!=null){
			            			 BufferedOutputStream out=null;
				            		 try{
				            			BufferedInputStream responseBuffer=new BufferedInputStream(instream);
				            			 File file=new File(Paths.getInstance().getWorkDir()+fileName);
				            			 FileOutputStream  fout=new FileOutputStream(file);
				            			out=new BufferedOutputStream(fout);
				            			 byte[] buffer=new byte[1024];
				            			 int len=0;
				            			 while((len=responseBuffer.read(buffer))>0){
				            				 out.write(buffer,0,len);
				            				 out.flush();
				            			 }
				            			 //文件写入本地成功
				            			 responseBuffer.close();
				            		 }catch(Exception e){
				            			 e.printStackTrace();
				            			 return false;
				            		 }finally{
				            			 try {
											if(out!=null)
												out.close();
										} catch (Exception e) {
											Logger.error("安渡接口调用："+uniExURL+"参数："+params+"关闭文件流发生异常："+e.toString());
										}
				            		 }
				            		 return true;
			            		 }
			            	}else{
			            		String resultMsg=map.get("Content-ResultMsg").get(0);
			            		resultMsg=URLDecoder.decode(resultMsg, "UTF-8");
			            		Logger.logService(serviceName, "调用接口："+uniExURL+",参数："+params+"返回:"+resultMsg);
			            		Logger.error("安渡接口调用："+uniExURL+"参数："+params+"不成功，返回："+resultMsg);
			            		return false;
			            	}
			            }
			            return true;
				 }catch(Exception e){
					 	Logger.error("安渡接口调用："+uniExURL+"参数："+params+"发生异常："+e.toString());
						return false;
			    }
		 }
		 
		 public static String doHttpPost(String jsonStr,String uniExURL){
			 byte[] jsonData=jsonStr.getBytes();
			 InputStream instream=null;
			 HttpURLConnection urlCon=null;
			 try{
				 	URL url = new URL(uniExURL);
		             urlCon = (HttpURLConnection)url.openConnection();
		            urlCon.setDoOutput(true);
		            urlCon.setDoInput(true);
		            urlCon.setRequestMethod("POST");
		            urlCon.setUseCaches(false);
		            urlCon.setRequestProperty("content-Type", "application/json");
		            urlCon.setRequestProperty("Accpet-charset", "UTF-8");
		            urlCon.setRequestProperty("Content-length",String.valueOf(jsonData.length));
		            DataOutputStream printout = new DataOutputStream(urlCon.getOutputStream());
		            printout.write(jsonData);
		            printout.flush();
		            printout.close();
		            if(urlCon.getResponseCode()!=200){
		            	Logger.error("安渡接口调用："+uniExURL+"参数："+jsonStr+"发生异常：http error code "+urlCon.getResponseCode());
		            	return "";
		            }else{
			            instream=urlCon.getInputStream();
			            byte[] bis = IOUtils.toByteArray(instream);
			            String ResponseJSon= new String(bis, "UTF-8");
			            return ResponseJSon;
		            }
			 }catch(Exception e){
				 	Logger.error("安渡接口调用："+uniExURL+"参数："+jsonStr+"发生异常："+e.toString());
				 return "";
			 }
			 finally{
				 try {
					if(instream!=null)
						 instream.close();
					if(urlCon!=null)
						urlCon.disconnect();
				} catch (IOException e) {
					Logger.error("安渡接口调用："+uniExURL+"参数："+jsonStr+"关闭流发生异常："+e.toString());
				}
			 }
		 }
}
