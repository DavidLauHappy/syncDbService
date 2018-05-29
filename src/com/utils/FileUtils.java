package com.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.db.DBConnectionManager;

public class FileUtils {
	 public static String formatPath(String path)
		{
		  if (path != null && !"".equals(path)) {
				path = path.replace('/', File.separatorChar);
				path = path.replace('\\', File.separatorChar);
				while (path.endsWith(String.valueOf(File.separatorChar))) {
					path = path.substring(0, path.length() - 1);
				}
			} else {
				return "";
			}
			return path;
		}
	 
	 //生成文件
	 public  static boolean writeFile(String path,String content){
	    	try {
				File file = new File(path);
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.close();
				fw.close();
				return true;
			} catch (Exception e) {
				 Logger.error("生成文件"+path+"异常:"+e.toString());
				 return false;
			}
	    }
	 //读取文件行
	 public static List<String> getFileLines(String path){
		 List<String> result=new ArrayList<String>();
		 try {
			 	File file = new File(path);
			 	FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line = "";
				while ((line = br.readLine()) != null) {
					result.add(line);
				}
				fr.close();
				br.close();
			} catch (Exception e) {
				 Logger.error("生成文件"+path+"异常:"+e.toString());
			}
		 return result;
	 }
	 
	 //获取文件的md5值
	 public static String getMd5ByPath(String filePath){
		  FileInputStream fis = null;
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				File file=new File(filePath);
					if(file!=null&&file.isFile()){
						fis = new FileInputStream(file);
						FileChannel fChannel = fis.getChannel();
						ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
						int length = -1;
						while ((length = fChannel.read(buffer)) != -1) {
							buffer.flip();
							md.update(buffer);
							buffer.compact();
						}
						byte[] b = md.digest();
						return byteToHexString(b);
				}else{
					return "";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	  }
	  
	  public static String getMd5ByFile(File file) {
			FileInputStream fis = null;
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				fis = new FileInputStream(file);
				FileChannel fChannel = fis.getChannel();
				ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
				int length = -1;
				while ((length = fChannel.read(buffer)) != -1) {
					buffer.flip();
					md.update(buffer);
					buffer.compact();
				}
				byte[] b = md.digest();
				return byteToHexString(b);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	  private static char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'a', 'b', 'c', 'd', 'e', 'f' };
	  private static String byteToHexString(byte[] tmp) {
			String s;// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串
			return s;
		}
	  
	  //判断文件实体释放存在
	  public static boolean fileExist(String md5){
			Connection conn = null;
			int count=0;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
			  String sql="select COUNT(*) AS COUNT from FILES where MD5='@MD5'";
				sql=sql.replace("@MD5", md5);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
					Map line=(Map)result.get(0);
					String exist=line.get("COUNT")+"";
					count=Integer.parseInt(exist);
					if(count>0)
						return true;
				}
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return false;
	  }
	  
}
