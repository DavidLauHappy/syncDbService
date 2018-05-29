package svc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rsc.Context;
import com.rsc.SecurityCenter;
import com.utils.Logger;
import com.utils.StringUtils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


/**
 * @author David
 *�ļ����䵽ftp������
 */
public class FtpFileService {
	private static FtpFileService unique_instance;
	public static FtpFileService getService(){
		if(unique_instance==null)
			unique_instance=new FtpFileService();
		return unique_instance;
	}
	
	private FtpFileService(){}
	public  FTPClient ftpUploader = new FTPClient(); 
	public boolean upLoad(String source,String target){
		int port=Integer.parseInt(Context.FTPServerPort);
		String realPasswd=SecurityCenter.getInstance().decrypt(Context.ServerFTPasswd, Context.EncryptKey);
		boolean result=this.connect(Context.FTPServerIp,port,Context.ServerFTPUser,realPasswd,target);
		if(result){
			File srcFile=new File(source);
			if(srcFile.exists()&&srcFile.isFile()){
			    return	this.uploadFile(srcFile,target);
			}
		}
		return false;
	}
	
	private boolean uploadFile(File inputFile,String path){
		  try{ 
			  String unicodeInputFilename=new String(inputFile.getName().getBytes("GBK"),"iso-8859-1");    
			  FileInputStream inputStream=new FileInputStream(inputFile);
			  boolean uptret=ftpUploader.storeFile(unicodeInputFilename, inputStream);
			  inputStream.close();
			  if(!uptret){
		    	   Logger.error("FtpFileService.uploadFile()�ϴ��ļ�["+unicodeInputFilename+"]δ�ɹ�");
		       }
			 
			  return true;
	    }
	  catch(Exception exp){
		  Logger.error("FtpFileService.uploadFile()�ϴ��ļ�["+inputFile.getPath()+"]�쳣"+exp.toString());
	  	}
		return false;
	}
	
	public boolean connect(String hostname,int port,String username,String password,String remotePath){   
		boolean result=false;	
		try{
				ftpUploader.connect(hostname, port);     
		        if(FTPReply.isPositiveCompletion(ftpUploader.getReplyCode())){   
		            if(ftpUploader.login(username, password)){   
		            	ftpUploader.enterLocalPassiveMode();   
		            	ftpUploader.setFileType(FTPClient.BINARY_FILE_TYPE);   
		            	//ftpUploader.setControlEncoding("GBK");  
		            	result=makeDirs(remotePath);//����Ŀ¼���л�Ŀ¼
		            return result;
		            }   
		        }else{
		        	disconnect();   
		        }
			}
			catch(Exception e){
		    	   Logger.error("FtpUpload.connect()�쳣��"+e.toString());
			}
	        return false;   
	}
	
	public void disconnect() throws IOException{   
        if(ftpUploader.isConnected()){   
        	ftpUploader.disconnect();   
        }   
    }  
	
	//�ж�Ŀ¼�Ƿ���ڣ���������ڣ��ʹ���Ŀ¼��������ڣ���Ҫ�ѵ�ǰ����Ŀ¼�л�����Ŀ¼
		private   boolean makeDirs(String path){
			boolean result=false;
			try{
					String[] dirs=path.split("\\\\");
					String currentDir="";
					List<String> Dirs=new ArrayList<String>();
					for(int w=0;w<dirs.length;w++){
						if(StringUtils.isNullOrEmpty(currentDir)){
							currentDir=dirs[w];
						}else{
							currentDir=currentDir+File.separator+dirs[w];
						}
						Dirs.add(currentDir);
					}
					for(String col:Dirs){
						ftpUploader.makeDirectory(col);//System.out.println(col);
					}
					result=ftpUploader.changeWorkingDirectory(path);
				return result;
			}catch(Exception e){
				 Logger.error("FtpUpload.makeDirs()�����л�Ŀ¼��"+path+"���쳣��"+e.toString());
			}
			return false;
		}
	
}
