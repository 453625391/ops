package com.meitianhui.sync.util;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.SystemException;

import com.alibaba.fastjson.JSONObject;

/**
 * 阿里巴巴json工具类
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
public class FastJsonUtil {

	public static Map<String, Object> jsonToMap(String str) throws SystemException {
		return JSONObject.parseObject(str, Map.class);
	}

	public static List<Map<String, Object>> jsonToList(String str) {
		return JSONObject.parseObject(str, List.class);
	}

	public static String toJson(Object obj) {
		return JSONObject.toJSONString(obj);
	}

}
