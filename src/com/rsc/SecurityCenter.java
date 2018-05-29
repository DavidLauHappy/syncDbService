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
		   // 生成一个可信任的随机数源
	        SecureRandom sr = new SecureRandom();
	        // 从原始密钥数据创建DESKeySpec对象
	        DESKeySpec dks = new DESKeySpec(key);
	        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
	        SecretKey securekey = keyFactory.generateSecret(dks);
	        // Cipher对象实际完成加密操作
	        Cipher cipher = Cipher.getInstance(DES);
	        // 用密钥初始化Cipher对象
	        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
	        return cipher.doFinal(data);
	 }
	 
	 private  byte[]  decrypt(byte[] data, byte[] key)throws Exception{
		    // 生成一个可信任的随机数源
	        SecureRandom sr = new SecureRandom();
	        // 从原始密钥数据创建DESKeySpec对象
	        DESKeySpec dks = new DESKeySpec(key);
	        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
	        SecretKey securekey = keyFactory.generateSecret(dks);
	        // Cipher对象实际完成解密操作
	        Cipher cipher = Cipher.getInstance(DES);
	        // 用密钥初始化Cipher对象
	        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
	        return cipher.doFinal(data);
	    }
}
