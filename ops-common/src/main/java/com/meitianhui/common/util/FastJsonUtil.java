package com.meitianhui.common.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.meitianhui.common.constant.CommonRspCode;
import com.meitianhui.common.exception.SystemException;

/**
 * 阿里巴巴json工具类
 * 
 * @author Tiny
 *
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class FastJsonUtil {
	
	public static Map<String, Object> jsonToMap(String str) throws SystemException {
		try {
			return JSONObject.parseObject(str, Map.class);
		} catch (Exception e) {
			throw new SystemException(CommonRspCode.JSON_ERROR, e.getMessage(), e);
		}
	}

	public static List jsonToList(String str) throws SystemException {
		try {
			return JSONObject.parseObject(str, List.class);
		} catch (Exception e) {
			throw new SystemException(CommonRspCode.JSON_ERROR, "info->"+e.getMessage()+";str->"+str, e);
		}
	}

	public static String toJson(Object obj) {
		return JSONObject.toJSONString(obj);
	}

}
