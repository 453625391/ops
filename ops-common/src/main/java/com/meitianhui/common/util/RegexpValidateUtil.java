package com.meitianhui.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串的校验工具类
 * 
 * @author Tiny
 * 
 */
public class RegexpValidateUtil {
	
	/**
	 * 邮箱校验
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String rule = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		return validate(rule,email);
	}
	
	/**
	 * 数字类型校验
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		String rule = "[0-9]*";
		return validate(rule,str);
	}
	
	
	
	/**
	 * 手机号码校验
	 * @param str
	 * @return
	 */
	public static boolean isPhone(String str){
		String rule = "^[1][34578][0-9]{9}$";
		return validate(rule,str);
	}
	
	/**
	 * 对字段串进行正则规则校验
	 * @param rule
	 * @param str
	 * @return
	 */
	public static boolean validate(String rule,String str){
		boolean isExist = false;
		Pattern pattern = Pattern.compile(rule);
		Matcher matcher = pattern.matcher(str);
		boolean b = matcher.matches();
		if (b) {
			isExist = true;
		}
		return isExist;
	}
	
}
