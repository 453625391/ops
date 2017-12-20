package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class InterfaceTest {

	public static void main(String[] args) {
		// appValidate();
		getArea();
	}

	public static void appAccredit() {
		try {
			String url = "http://127.0.0.1:9090/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.appAccredit");
			Map<String, String> bizparam = new HashMap<String, String>();
			bizparam.put("app_id", "2015122212000001");
			bizparam.put("private_key", "123456");
			requestData.put("params", FastJsonUtil.toJson(bizparam));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void appValidate() {
		try {
			String url = "http://127.0.0.1:9090/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.appValidate");
			Map<String, String> bizparam = new HashMap<String, String>();
			bizparam.put("token", "42e3cdd5da661812f2032d8a493bae95276456de3745256488f22a5224fd9759");
			bizparam.put("security_code", "R7JqQysO");
			requestData.put("params", FastJsonUtil.toJson(bizparam));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void eventLog() {
		try {
			String url = "http://127.0.0.1:9090/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.eventLog");
			Map<String, String> bizparam = new HashMap<String, String>();
			bizparam.put("event_info", "测试数据");
			requestData.put("params", FastJsonUtil.toJson(bizparam));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addDemo() {
		String url = "http://127.0.0.1:9090/ops-template/demo";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "addDemo");
		Map<String, String> bizparam = new HashMap<String, String>();
		bizparam.put("id", "20");
		bizparam.put("name", "测试数据");
		requestData.put("params", FastJsonUtil.toJson(bizparam));
		try {
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void queryDemo() {
		String url = "http://127.0.0.1:9090/ops-template/demo";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "queryDemo");
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "测试数据");
		Map<String, String> page = new HashMap<String, String>();
		page.put("page_no", "1");
		requestData.put("params", FastJsonUtil.toJson(params));
		requestData.put("page", FastJsonUtil.toJson(page));
		try {
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	public static void getArea() {
		String url = "http://127.0.0.1:9090/ops-template/demo";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "getArea");
		Map<String, String> params = new HashMap<String, String>();
		requestData.put("params", FastJsonUtil.toJson(params));
		try {
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
