package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class GdBenefitTest {

	// @Test
	public void gdBenefitCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "gdBenefit.operate.gdBenefitCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("benefit_type", "free_coupon");
			params.put("member_id", "20000001");
			params.put("limited_price", "10.00");
			params.put("amount", "10.00");
			params.put("event", "测试赠送");
			params.put("expired_date", "2017-03-03 13:52:17");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void gcActivityFaceGiftPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "gcActivity.platform.gcActivityFaceGiftPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "11603134");
			params.put("member_type_key", "consumer");
			params.put("order_date", "2017-04-07");
			params.put("order_no", "85025559107495936011");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void memberPointPay() {
		try { 
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "gdBenefit.consumer.MemberPointPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "10010005"); 
			params.put("goods_id", "10010021"); 
			//params.put("member_type_key", "consumer");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
