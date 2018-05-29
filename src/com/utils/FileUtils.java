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
	 
	 //�����ļ�
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
				 Logger.error("�����ļ�"+path+"�쳣:"+e.toString());
				 return false;
			}
	    }
	 //��ȡ�ļ���
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
				 Logger.error("�����ļ�"+path+"�쳣:"+e.toString());
			}
		 return result;
	 }
	 
	 //��ȡ�ļ���md5ֵ
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
			String s;// ���ֽڱ�ʾ���� 16 ���ֽ�
			char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
			// ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ� ת���� 16 �����ַ���ת��
				byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
				str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��, >>> Ϊ�߼����ƣ�������λһ������
				str[k++] = hexdigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
			}
			s = new String(str); // ����Ľ��ת��Ϊ�ַ���
			return s;
		}
	  
	  //�ж��ļ�ʵ���ͷŴ���
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
