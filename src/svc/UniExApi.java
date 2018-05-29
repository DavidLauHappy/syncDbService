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
		//�ϴ��ļ��������ĵ�������
		 public static boolean uplaodFile(String filePath){
			 	String url="http://@IP:81/UniExServices/openAPI/file/uploadFile.htm";
			 	url=url.replace("@IP", Context.UniNXG_IP_OUT);
			 	String spath=filePath.substring(filePath.indexOf(":"));
			 	spath=spath.replace(":", "");
			 	spath=Context.outShareUser+spath;
			 	spath=spath.replace('\\','/');
			 	spath=spath.replace("//", "/");
			 	//����ƴװ
			 	JSONObject json=new JSONObject();
			 	json.put("spath", spath);
			 	json.put("stype", "1");//�����ļ���
				json.put("uname", Context.UniNXG_URL);//�û���
				json.put("tpath", "/"+Context.UniNXG_DIR+"/");//�ļ���ŵ�Ŀ��Ŀ¼
				json.put("tshare", Context.UniNXG_ShareUser);//�û���
				json.put("apikey", Context.apiKey);
				Logger.logService(serviceName, "���ýӿڣ�"+url+",������"+json.toString());
				String result=UniExApi.doHttpPost(json.toString(), url);
				Logger.logService(serviceName, "���ýӿڣ�"+url+",������"+json.toString()+"�����أ�"+result);
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
		 
		 //�Ӱ��������ļ�������
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
				Logger.logService(serviceName, "���ýӿڣ�"+uniExURL+",������"+params);
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
			            	Logger.error("���ɽӿڵ��ã�"+uniExURL+"������"+params+"�����쳣��http error code "+urlCon.getResponseCode());
			            	return false;
			            }
			            else{
			            	Map<String,List<String>> map=urlCon.getHeaderFields();
			            	String result=map.get("Content-Result").get(0);
			            	if("SUCCESS".equalsIgnoreCase(result)){
			            		Logger.logService(serviceName, "���ýӿڣ�"+uniExURL+",������"+params+"���سɹ�");
			            		 InputStream instream= urlCon.getInputStream();//�ļ�����
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
				            			 //�ļ�д�뱾�سɹ�
				            			 responseBuffer.close();
				            		 }catch(Exception e){
				            			 e.printStackTrace();
				            			 return false;
				            		 }finally{
				            			 try {
											if(out!=null)
												out.close();
										} catch (Exception e) {
											Logger.error("���ɽӿڵ��ã�"+uniExURL+"������"+params+"�ر��ļ��������쳣��"+e.toString());
										}
				            		 }
				            		 return true;
			            		 }
			            	}else{
			            		String resultMsg=map.get("Content-ResultMsg").get(0);
			            		resultMsg=URLDecoder.decode(resultMsg, "UTF-8");
			            		Logger.logService(serviceName, "���ýӿڣ�"+uniExURL+",������"+params+"����:"+resultMsg);
			            		Logger.error("���ɽӿڵ��ã�"+uniExURL+"������"+params+"���ɹ������أ�"+resultMsg);
			            		return false;
			            	}
			            }
			            return true;
				 }catch(Exception e){
					 	Logger.error("���ɽӿڵ��ã�"+uniExURL+"������"+params+"�����쳣��"+e.toString());
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
		            	Logger.error("���ɽӿڵ��ã�"+uniExURL+"������"+jsonStr+"�����쳣��http error code "+urlCon.getResponseCode());
		            	return "";
		            }else{
			            instream=urlCon.getInputStream();
			            byte[] bis = IOUtils.toByteArray(instream);
			            String ResponseJSon= new String(bis, "UTF-8");
			            return ResponseJSon;
		            }
			 }catch(Exception e){
				 	Logger.error("���ɽӿڵ��ã�"+uniExURL+"������"+jsonStr+"�����쳣��"+e.toString());
				 return "";
			 }
			 finally{
				 try {
					if(instream!=null)
						 instream.close();
					if(urlCon!=null)
						urlCon.disconnect();
				} catch (IOException e) {
					Logger.error("���ɽӿڵ��ã�"+uniExURL+"������"+jsonStr+"�ر��������쳣��"+e.toString());
				}
			 }
		 }
}
