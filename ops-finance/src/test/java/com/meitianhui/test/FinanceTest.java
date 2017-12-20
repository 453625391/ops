package com.meitianhui.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class FinanceTest {

	
	
	@Test
	public void memberPointEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.consumer.memberPointEdit");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("member_id", "12105561");
			params.put("point_values", "5");
			params.put("booking_mark", "income");
			params.put("member_type_key", "consumer");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
