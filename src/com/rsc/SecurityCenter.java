package com.rsc;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class SecurityCenter {
	public static SecurityCenter getInstance(){
		if(unique_instance==null)
			unique_instance=new SecurityCenter();
		return unique_instance;
	}

	 public String  encrypt(String source, String key){
		 String result="";
		 try{
		     byte[] bt = this.encrypt(source.getBytes(), key.getBytes());
		     BASE64Encoder encoder = new BASE64Encoder();
		     result =encoder.encode(bt);
		 }
		 catch(Exception e){
			 result=null;
			 //Logger.getInstance().error("exception at encrypting:"+e.getMessage());
		 }
	        return result;
	 }
	 
	 public String decrypt(String encryptStr,String key){
		 String result="";
		 try{
		 if (encryptStr == null)
	            return null;
	        BASE64Decoder decoder = new BASE64Decoder();
	        byte[] buf = decoder.decodeBuffer(encryptStr);
	        byte[] bt = this.decrypt(buf,key.getBytes());
	        result= new String(bt);
		 }
		 catch(Exception e){
			 result=null;
			 //Logger.getInstance().error("exception at decrypting:"+e.getMessage());
		 }
	        return result;
	 }
	 
	 
	private SecurityCenter(){};
	private final static String DES = "DES";
	private static SecurityCenter unique_instance;
	 private byte[] encrypt(byte[] data, byte[] key)throws Exception {
		   // ����һ�������ε������Դ
	        SecureRandom sr = new SecureRandom();
	        // ��ԭʼ��Կ���ݴ���DESKeySpec����
	        DESKeySpec dks = new DESKeySpec(key);
	        // ����һ����Կ������Ȼ��������DESKeySpecת����SecretKey����
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
	        SecretKey securekey = keyFactory.generateSecret(dks);
	        // Cipher����ʵ����ɼ��ܲ���
	        Cipher cipher = Cipher.getInstance(DES);
	        // ����Կ��ʼ��Cipher����
	        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
	        return cipher.doFinal(data);
	 }
	 
	 private  byte[]  decrypt(byte[] data, byte[] key)throws Exception{
		    // ����һ�������ε������Դ
	        SecureRandom sr = new SecureRandom();
	        // ��ԭʼ��Կ���ݴ���DESKeySpec����
	        DESKeySpec dks = new DESKeySpec(key);
	        // ����һ����Կ������Ȼ��������DESKeySpecת����SecretKey����
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
	        SecretKey securekey = keyFactory.generateSecret(dks);
	        // Cipher����ʵ����ɽ��ܲ���
	        Cipher cipher = Cipher.getInstance(DES);
	        // ����Կ��ʼ��Cipher����
	        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
	        return cipher.doFinal(data);
	    }
}
