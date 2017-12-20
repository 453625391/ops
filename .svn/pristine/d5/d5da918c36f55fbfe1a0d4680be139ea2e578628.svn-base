package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class TestStores {

	// @Test
	public void storeListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "stores.consumer.storesTypeGroupFind");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	 @Test
	public void nearbyHSPostListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "stores.consumer.nearbyHSPostListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("longitude", "122.035096");
			params.put("latitude", "37.399296");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void stageStoresListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "stores.operate.stageStoresListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "20");
			requestData.put("page", FastJsonUtil.toJson(page));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void stageStoresEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "stores.operate.stageStoresEdit");
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String> page = new HashMap<String, String>();
			params.put("stores_id", "68aada35208c414097465471f501c888");
			params.put("is_stage_hpt", "Y");
			requestData.put("page", FastJsonUtil.toJson(page));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
