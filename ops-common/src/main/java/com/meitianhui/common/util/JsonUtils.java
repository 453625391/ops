package com.meitianhui.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.json.JSONObject;

@SuppressWarnings("rawtypes")
public class JsonUtils {

	/**
	 * 将json格式的数据格式化成map
	 * @param jsonString
	 * @return
	 * @throws JSONException
	 */
	public static Map<String,Object> jsonFormatMap(String jsonString){
		Map<String,Object> result = new HashMap<String,Object>();
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator iterator = jsonObject.keys();
		String key = null;
		Object obj = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			obj = jsonObject.get(key);
			result.put(key, obj);
		}
        return result;
    }
}
