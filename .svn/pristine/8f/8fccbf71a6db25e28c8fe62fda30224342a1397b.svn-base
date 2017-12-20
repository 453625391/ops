package com.meitianhui.test;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkAdzoneCreateRequest;
import com.taobao.api.request.TbkCouponConvertRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkCouponConvertResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;


public class GdAppAdvertTest {
	
	
	
	
	
	//@Test
	public void testCreateTKLByTaobaoAPI() {
		
		try {
			String command = null;
			TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "24635795", "b8709ffa33efaff9a7d4813ba77de28d");
			TbkTpwdCreateRequest  req = new TbkTpwdCreateRequest();
			req.setUserId("");
			req.setText("丁胖很漂亮");
			req.setUrl("https://uland.taobao.com/coupon/edetail?e=dJxOOMLAOYwGQASttHIRqSAhdOH5kHrEMABzzGcTJXQ5WH9WBO1MDwS4i3j9SEzntxOPgoVOL2jkSi0Futlyq2jp926RHPMNXjJhgAGfjiyuX5JkpVUf%2FA%3D%3D&traceId=0ab22d6215094406725701507e");
			req.setLogo("");
			req.setExt("");
			TbkTpwdCreateResponse rsp = client.execute(req);
			command = (((Map<String, Object>)((Map<String, Object>) FastJsonUtil.jsonToMap(rsp.getBody()).get("tbk_tpwd_create_response")).get("data")).get("model")).toString();
			System.out.println(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testCreateLongUrlByTaobaoAPI() {
		try {
			String longUrl = createLongUrlByTaobaoAPI("547799748985","6e5841cfc4d74426bd613c48a571df02");
			String user_service_url="http://api.t.sina.com.cn/short_url/shorten.json";
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("source", "896267270");
			reqParams.put("url_long", longUrl);
			String resultStr = HttpClientUtil.getShort(user_service_url, reqParams);
			String getSignInfo = resultStr.substring(resultStr.indexOf("[") + 1, resultStr.indexOf("]"));
			Map<String, Object> bizParams = new HashMap<String, Object>();
			bizParams = FastJsonUtil.jsonToMap(getSignInfo);
			String url_long = bizParams.get("url_long").toString();
			String url_short = bizParams.get("url_short").toString();
			System.out.println(url_short);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String createLongUrlByTaobaoAPI(String ItemId,String Quan_id) {
		String tempURL = null;
		try {
			TaobaoClient client_android = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "24635795", "b8709ffa33efaff9a7d4813ba77de28d");
			TbkCouponConvertRequest req = new TbkCouponConvertRequest();   
			req.setItemId(Long.parseLong(ItemId));
			String numAdzoneId = "146130492";
			req.setAdzoneId(Long.parseLong(numAdzoneId));
			TbkCouponConvertResponse rsp = client_android.execute(req);
			tempURL = (((Map<String, Object>)((Map<String, Object>)((Map<String, Object>) FastJsonUtil.jsonToMap(rsp.getBody()).get("tbk_coupon_convert_response")).get("result")).get("results")).get("coupon_click_url")).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempURL+"&activityId="+Quan_id;
	}
	
	//@Test
	public void gdActivityDeliveryCountFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "gdActivity.consumer.gdActivityDeliveryCountFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "ded6f1503a9044e7a55c93c1de4d8d3d");
			params.put("goods_id", "2de7f45575d44e84ab0b277f6a13fabd");
			params.put("member_id", "20000001");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void gdActivityDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "gdActivity.consumer.gdActivityDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_id", "e85cb6101a5c4f2eacc1c49cff56a15e");
			params.put("activity_id", "00645c3891744bb18a826579aed19800");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void gdAdvertCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "gdAppAdvert.operate.gdAppAdvertCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("category", "wyp_app");
			params.put("json_data",
					"[{\"path_id\":\"a18c51ce1c2c46d399a0393b73167f41\",\"title\":\"\",\"url\":\"www.baidu.com\"}]");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
//	@Test
	public void gdAdvertEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "gdAppAdvert.operate.gdAppAdvertEdit");
			Map<String, String> params = new HashMap<String, String>();
			params.put("advert_id", "969426304bd649948e4e0027384c6cb6");
			params.put("category", "c_app_sign");
			params.put("json_data",
					"[{\"path_id\":\"489906b44b9c48518f358c64ab2bdb38\",\"title\":\"签到广告\",\"url\":\"www.baidu.com\"}]");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	@Test
	public void fgGoodsActivityHomeForConsumerFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.fgGoodsActivityHomeForConsumerFind");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//@Test
	public void gdAppAdvertFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "gdAppAdvert.app.gdAppAdvertFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("category", "c_app_sign");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
