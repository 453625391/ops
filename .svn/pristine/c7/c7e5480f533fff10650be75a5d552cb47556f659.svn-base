package com.meitianhui.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class InterfaceTest {
	
	@Test
	public void sendCheckCode() {
		try {
			String url = "http://127.0.0.1:8080/ops-notification/notification";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "notification.sendCheckCode");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("sms_source", "SJLY_03");
			params.put("mobile", "14795781689");
			params.put("lock_value", "33f90305d81f02c4fff70fecce0c13da85c9363aaf1d27f3938c9badaa675cedfb48092ef2ba42136b8f5477dcb2c23ddd62c28fcd78ea2db9b6ddf6ba788a6a7f");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void validateCheckCode() {
		try {
			String url = "http://127.0.0.1:8080/ops-notification/notification";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "notification.validateCheckCode");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("mobile", "13018089936");
			params.put("check_code", "0369");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void SMSSend() {
		try {
			String url = "http://127.0.0.1:8080/ops-notification/notification";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "notification.SMSSend");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("sms_source", "test");
			params.put("mobiles", "");
			params.put("msg", "您的验证码为:635102,请到验证页面输入验证码验证！");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void pushNotificationByAlias() {
		try {
			String url = "http://127.0.0.1:8080/ops-notification/notification";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "notification.pushNotificationByAlias");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("title", "通知");
			params.put("alert", "测试群体推送");
			params.put("alias", "10494374");
			Map<String, Object> extrasparam = new HashMap<String, Object>();
			params.put("extrasparam", FastJsonUtil.toJson(extrasparam));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
