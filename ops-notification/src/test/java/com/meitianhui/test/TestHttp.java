package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class TestHttp {

	public static void main(String[] args) {
		for (int i = 1; i < 10; i++) {
			new Thread() {
				@Override
				public void run() {
					test();
				}
			}.start();
		}
	}

	public static void test() {
		try {
			for (int i = 1; i < 2; i++) {
				testHttp(i + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testHttp(String flag) {
		try {
			String url = "http://127.0.0.1:8080/ops-notification/notification";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "notification.testHttp");
			Map<String, String> params = new HashMap<String, String>();
			params.put("flag", flag);
			requestData.put("params", FastJsonUtil.toJson(params));
			HttpClientUtil.post(url, requestData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
