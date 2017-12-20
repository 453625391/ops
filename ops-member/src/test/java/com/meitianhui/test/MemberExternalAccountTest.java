package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class MemberExternalAccountTest {


	 @Test
	public void memberExternalAccountCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "memberExternalAccount.app.memberExternalAccountCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("user_id", "12235096");
			params.put("mobile", "13001010101");
			params.put("member_type_key", "stores");
			params.put("member_id", "9654");
			params.put("account_type_key", "taobao");
			params.put("account_no", "13001010101");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 
//	 @Test
	public void memberExternalAccountListFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "memberExternalAccount.app.memberExternalAccountListFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_type_key", "stores");
			params.put("member_id", "9654");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	 @Test
	public void memberExternalAccountEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "memberExternalAccount.app.memberExternalAccountEdit");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_type_key", "stores");
			params.put("member_id", "9654");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	 
}
