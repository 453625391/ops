package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

@SuppressWarnings("unchecked")
public class InterfaceTest {
	
	//@Test
	public void storeCountAndConsumerCount() {
		try {
			String url = "http://127.0.0.1:8080/ops-report/report";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.salesassistant.storeCountAndConsumerCount");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void orderCountAndMoney() {
		try {
			String url = "http://127.0.0.1:8080/ops-report/report";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.salesassistant.orderCountAndMoney");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void storesTransactionTypeFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-report/report";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "stores.salesassistant.storesTransactionTypeFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_id", "3ab0eb265f564e0ba34f7ad4ed971bf3");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
