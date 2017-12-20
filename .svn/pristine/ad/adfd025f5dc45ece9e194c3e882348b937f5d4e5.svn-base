package com.meitianhui.common.util;

import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesCbcSecurity {
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	public static final String DES_KEY = "82543243";

	/**
	 * DES算法，加密
	 *
	 * @param data
	 *            待加密字符串
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * @throws CryptException
	 *             异常
	 */
	public static String encode(String data) throws Exception {
		return encode(data.getBytes());
	}

	/**
	 * DES算法，加密
	 *
	 * @param data
	 *            待加密字符串
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * @throws CryptException
	 *             异常
	 */
	public static String encode(byte[] data) throws Exception {
		try {
			DESKeySpec dks = new DESKeySpec(DES_KEY.getBytes());

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec(DES_KEY.getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

			byte[] bytes = cipher.doFinal(data);

			return asHex(bytes);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public static String md5(String input) throws NoSuchAlgorithmException {
	    String result = input;
	    if(input != null) {
	        MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
	        md.update(input.getBytes());
	        BigInteger hash = new BigInteger(1, md.digest());
	        result = hash.toString(16);
	        while(result.length() < 32) { //40 for SHA-1
	            result = "0" + result;
	        }
	    }
	    return result;
	}
	
	public static String asHex (byte buf[]) {  
        StringBuffer strbuf = new StringBuffer(buf.length * 2);  
        int i;  
        for (i = 0; i < buf.length; i++) {  
            if (((int) buf[i] & 0xff) < 0x10)  
                strbuf.append("0");  
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));  
        }  
        return strbuf.toString();  
    } 
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(md5("default" + "sessions" + "8b8e04ba3630c11fc4e17894089d759fcbd1c69d"));
	}
}