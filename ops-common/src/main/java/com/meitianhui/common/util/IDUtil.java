package com.meitianhui.common.util;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class IDUtil {

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	/**
	 * 短八位UUID
	 * 
	 * @return
	 */
	public static synchronized String getShortUUID() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = getUUID();
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}

	/**
	 * 随机生成UUID 32位字符串
	 * 
	 * @return
	 */
	public static synchronized String getUUID() {
		return (UUID.randomUUID().toString()).replace("-", "");
	}

	/**
	 * 获取时间戳 
	 * 后面跟指定长度的随机数
	 * @param length
	 * @return
	 */
	public static synchronized String getTimestamp(int length) {
		return DateUtil.date2Str(new Date(), "yyMMddHHmmssSSS") + random(length);
	}

	
	/**
	 * 生成制定位数的随机数
	 * 
	 * @param length
	 * @return
	 */
	public static String random(int length) {
		Random random = new Random();
		String randomNum = "";
		for (int i = 1; i <= length; i++) {
			String num = random.nextInt(10) + "";
			randomNum += num;
		}
		return randomNum;
	}

	/**
	 * 为手机app生成登陆token
	 * 
	 * @param password
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public static synchronized String generateToken(String uuId) throws Exception {
		Long curTime = System.currentTimeMillis();
		String token = DesCbcSecurity.encode(uuId + curTime);
		return token;
	}

	/**
	 * 获取code
	 * 
	 * @param password
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public static synchronized String generateCode(int length) {
		return random(length);
	}

}
