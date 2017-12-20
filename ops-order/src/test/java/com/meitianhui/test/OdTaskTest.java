package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class OdTaskTest {

	 @Test
	 @Ignore
	public void odTaskProcessingSettle() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "odTask.operate.odTaskProcessingSettle");
			Map<String, String> params = new HashMap<String, String>();
			params.put("submitted_date", "2017-03-10");
			params.put("operator", "刘涛");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
