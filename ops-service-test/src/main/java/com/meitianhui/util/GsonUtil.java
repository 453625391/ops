package com.meitianhui.util;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Gson工具类
 * @author Tiny
 *
 */
public class GsonUtil {
	private static Gson gson = new Gson();

	
	/**
	 * 对象格式化成Json
	 * @param o
	 * @return
	 */
	public static String toJson(Object o) {
		return gson.toJson(o);
	}
	
	/**
	 * Map格式的Json数据格式话成Map
	 * @param str
	 * @return
	 */
	public static Map<String, Object> jsonToMap(String str) {
		return gson.fromJson(str, new TypeToken<Map<String, Object>>() {}.getType());
	}
}
