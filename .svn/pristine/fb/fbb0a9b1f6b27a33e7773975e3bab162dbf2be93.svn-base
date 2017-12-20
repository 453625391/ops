package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class TestGdActivity {
	
	
	 @Test
	public void gdActivityCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "gdActivity.operate.gdActivityCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("title", "222");
			params.put("goods_id", "449cd350839143bb9b9ff81d023bf56f");
			params.put("limited_grade", "2");
			params.put("limited_benefit", "2");
			params.put("expired_date", "2017-03-03 13:52:17");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
//	 @Test
	 public void gdActivityGet() {
		 try {
			 String url = "http://127.0.0.1:8080/ops-goods/goods";
			 Map<String, String> requestData = new HashMap<String, String>();
			 requestData.put("service", "gdActivity.consumer.gdActivityGet");
			 Map<String, String> params = new HashMap<String, String>();
			 params.put("activity_id", "7db424ad62f84cd38f94cf09f7dc8545");
			 params.put("goods_id", "449cd350839143bb9b9ff81d023bf56f");
			 params.put("member_type_key", "consumer");
			 params.put("member_id", "11603129");
			 params.put("member_mobile", "18618419799");
			 params.put("amount", "2");
			 params.put("benefit_id", "1");
			 params.put("benefit_type", "free_coupon");
			 params.put("contact_person", "18618419799");
			 params.put("contact_tel", "18618419799");
			 params.put("delivery_area_id", "440304");
			 params.put("delivery_address", "车公庙");
			 requestData.put("params", FastJsonUtil.toJson(params));
			 String result = HttpClientUtil.post(url, requestData);
			 System.out.println(result);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }
	 
//	 @Test
	public void gdActivityDeliveryDeliver() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "gdActivity.operate.gdActivityDeliveryDeliver");
			Map<String, String> params = new HashMap<String, String>();
			params.put("delivery_id", "285f0a4a27244206b27f237a346f0f76");
			params.put("logistics", "2017-03-03 13:52:17");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
}
