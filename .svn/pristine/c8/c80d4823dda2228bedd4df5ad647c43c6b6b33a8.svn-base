package com.meitianhui.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class RedisTest {

	public static void main(String[] args) {
		for(int i = 1;i<2;i++){
			new Thread(){
				@Override  
				public void run() {
					test();
				}
			}.start();
		}
	}

	public static void test() {
		try {
			for(int i = 0;i<1;i++){
				freeGetGoodsPageFind();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void freeGetGoodsPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.freeGetGoodsPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("area_id", "440300");
			requestData.put("params", FastJsonUtil.toJson(params));
			HttpClientUtil.post(url, requestData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void storeLogin() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.storeLogin");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("user_account", "13018089936");
			params.put("request_info", "127.0.0.1");
			params.put("data_source", "SJLY_02");
			requestData.put("params", FastJsonUtil.toJson(params));
			HttpClientUtil.post(url, requestData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
