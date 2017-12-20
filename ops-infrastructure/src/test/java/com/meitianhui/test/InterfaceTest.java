package com.meitianhui.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class InterfaceTest {

	@Test
		public void salesmanLogin() {
			try {
				String url = "http://127.0.0.1:8080/ops-infrastructure/user";
				Map<String, String> requestData = new HashMap<String, String>();
				requestData.put("service", "infrastructure.salesmanLogin");
				Map<String, Object> params = new LinkedHashMap<String, Object>();
				params.put("mobile", "14795781694");
				params.put("request_info", "127.0.0.1");
				params.put("data_source", "SJLY_01");
				requestData.put("params", FastJsonUtil.toJson(params));
				String result = HttpClientUtil.post(url, requestData);
				System.out.println(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
//	@Test
	public void appValidate() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.appValidate");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("app_token", "04fefd7e-ad6b-11e5-9ba7-fcaa1490ccaf");
			params.put("request_info", "127.0.0.1");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void appTokenAuth() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.appTokenAuth");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("app_id", "04fefd7e-ad6b-11e5-9ba7-fcaa1490ccaf");
			params.put("private_key", "meitianhui");
			params.put("request_info", "127.0.0.1");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void payPasswordSetting() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.payPasswordSetting");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("user_id", "10531921");
			params.put("payment_password", "test");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void paySecurityOptions() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.paySecurityOptions");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("user_id", "10531921");
			params.put("small_direct", "Y");
			params.put("sms_notify", "Y");
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
			String url = "http://127.0.0.1:8080/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.validateCheckCode");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("mobile", "18676794743");
			params.put("check_code", "4379");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void appInfo() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.appInfo");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("app_id", "04fefd7e-ad6b-11e5-9ba7-fcaa1490ccaf");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void consumerUserRegister() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.consumerUserRegister");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("mobile", "13870190189");
			params.put("password", "e10adc3949ba59abbe56e057f20f883e");
			params.put("user_id", "12024518");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void shumeLoginRegister() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.shumeLoginRegister");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("mobile", "13018089936");
			params.put("device_id", "e10adc3949ba59abbe56e057f20f883e");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void shumeLoginVerify() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.shumeLoginVerify");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("mobile", "13018089936");
			params.put("device_id", "e10adc3949ba59abbe56e057f20f883e");
			params.put("code", "e10adc3949ba59abbe56e057f20f883e");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void consumerLogin() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.consumerLogin");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("mobile", "18818706542");
			params.put("request_info", "127.0.0.1");
			params.put("data_source", "SJLY_01");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	// @Test
	public void userLoginForOAuth() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.consumerLoginForOAuth");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("user_account", "18818706542");
			params.put("password", "96e79218965eb72c92a549dd5a330112");
			params.put("request_info", "127.0.0.1");
			params.put("member_type_key", "consumer");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void consumerInfoFindForOAuth() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.consumerInfoFindForOAuth");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("user_id", "10192099");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void storeLogin() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.storeLogin");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("user_account", "13018089936");
			params.put("request_info", "127.0.0.1");
			params.put("data_source", "SJLY_02");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void userFindByMobile() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.userFindByMobile");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("mobile", "13018089936");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void memberTypeValidateByMobile() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.memberTypeValidateByMobile");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("mobile", "13018089936");
			params.put("member_type_key", "stores");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void memberTypeValidate() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.memberTypeValidate");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("user_account", "13018089936");
			params.put("member_type_key", "stores");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void memberTypeFindByMobile() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.memberTypeFindByMobile");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("mobile", "13018089936");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void userEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.userEdit");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			// params.put("user_id", "12f27687-a7c3-11e5-bcf8-fcaa1490ccaf");
			params.put("user_token", "eff20f2c6b605fe2df0fb31a3bc21ba5");
			params.put("mobile", "1256454");
			params.put("im1", "44444444444444");
			params.put("im2", "1111111111111");
			params.put("im3", "333333333333333");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void userFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.userFind");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("token", "eff20f2c6b605fe2df0fb31a3bc21ba5");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void userPasswordChange() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.userPasswordChange");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("token", "eff20f2c6b605fe2df0fb31a3bc21ba5");
			params.put("old_password", "654321");
			params.put("new_password", "123456");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void userPasswordReset() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.userPasswordReset");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("mobile", "13135636400");
			// 123456
			params.put("password", "e10adc3949ba59abbe56e057f20f883e");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void transactionRegister() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.transactionRegister");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void userValidate() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/user";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.userValidateNoRequestInfo");
			Map<String, String> params = new HashMap<String, String>();
			params.put("user_token",
					"12153d5e2c901e64ed717531c92f591f7dd98482aa2eccaaddb4172d47d655b6e335796b5ecd2413a47cad87dcb233a6");
			params.put("request_info", "FHSnnm1j");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void transactionVerify() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.transactionVerify");
			Map<String, String> params = new HashMap<String, String>();
			params.put("flow_no", "741124020195");
			params.put("security_code", "FHSnnm1j");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void feedback() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.feedback");
			Map<String, String> params = new HashMap<String, String>();
			params.put("category", "意见");
			params.put("desc1", "youbug");
			params.put("data_source", "SJLY_01");
			params.put("contact", "张三");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void feedbackPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-infrastructure/infrastructure";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "infrastructure.feedbackPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("category", "意见");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "5");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
