package com.meitianhui.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

/**
 * 加密算法
 * 
 * @author 丁忍（神一般的男人-kk）
 *
 */

public class EncryptUtil {

	/**
	 * 传入文本内容，返回 MD5串
	 * 
	 * @param strText
	 * @return
	 */
	public static String MD5(final String strText) {
		return SHA(strText, "MD5");
	}

	/**
	 * 传入文本内容，返回 SHA-256 串
	 * 
	 * @param strText
	 * @return
	 */
	public static String SHA256(final String strText) {
		return SHA(strText, "SHA-256");
	}

	/**
	 * 传入文本内容，返回 SHA-512 串
	 * 
	 * @param strText
	 * @return
	 */
	public static String SHA512(final String strText) {
		return SHA(strText, "SHA-512");
	}

	/**
	 * 字符串 SHA 加密
	 * 
	 * @param strSourceText
	 * @return
	 */
	private static String SHA(final String strText, final String strType) {
		// 返回值
		String strResult = null;

		// 是否是有效字符串
		if (strText != null && strText.length() > 0) {
			try {
				// SHA 加密开始
				// 创建加密对象 并传入加密类型
				MessageDigest messageDigest = MessageDigest.getInstance(strType);
				// 传入要加密的字符串
				messageDigest.update(strText.getBytes());
				// 得到 byte 类型结果
				byte byteBuffer[] = messageDigest.digest();

				// 將 byte 转化为 string
				StringBuffer strHexString = new StringBuffer();
				// 遍历 byte buffer
				for (int i = 0; i < byteBuffer.length; i++) {
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1) {
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				// 得到返回结果
				strResult = strHexString.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return strResult;
	}
	
	//测试
	public static void main(String args[]) throws ParseException {
		
//		System.out.println(EncryptUtil.SHA512("333"));
		
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//      Date date = simpleDateFormat.parse(DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMdd));
//      String timeStamp = String.valueOf(date.getTime());
//      System.out.println(timeStamp);
    } 

	
	
}
