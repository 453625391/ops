package com.meitianhui.test;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class RedisTest2 {

	public static void main(String[] args) {
		for (int i = 1; i < 100; i++) {
			new Thread() {
				@Override
				public void run() {
					test1();
					test2();
				}
			}.start();
		}
	}

	public static void test1() {
		try {
			boolean flag = true;
			Date date = DateUtil.str2Date("2017-05-24 13:25:00", DateUtil.fmt_yyyyMMddHHmmss);
			while (flag) {
				if (new Date().getTime() >= date.getTime()) {
					flag = false;
				}
				if (!flag) {
					consumerFreeGetRecordCreate1();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void test2() {
		try {
			boolean flag = true;
			Date date = DateUtil.str2Date("2017-05-24 13:25:00", DateUtil.fmt_yyyyMMddHHmmss);
			while (flag) {
				if (new Date().getTime() >= date.getTime()) {
					flag = false;
				}
				if (!flag) {
					consumerFreeGetRecordCreate2();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void consumerFreeGetRecordCreate1() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.consumerFreeGetRecordCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_code", "FS149249522430");
			params.put("consumer_id", "11603134");
			params.put("recommend_stores_id", "9654");
			params.put("mobile", "13018089936");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void consumerFreeGetRecordCreate2() {
		try {
			String url = "http://121.43.59.219:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.consumerFreeGetRecordCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_code", "FS149249522430");
			params.put("consumer_id", "11171510");
			params.put("recommend_stores_id", "9654");
			params.put("mobile", "18618419799");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void balancePay1() {
		try {
			String url = "http://121.43.59.219:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.balancePay");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("data_source", "SJLY_02");
			params.put("payment_way_key", "ZFFS_05");
			params.put("detail", "测试交易");
			params.put("amount", "1");
			params.put("out_trade_no", "20170109133420006");
			params.put("buyer_id", "9654");
			params.put("seller_id", "10000001");
			params.put("out_trade_body", "{'seller_id':'10000001','buyer_id':'9654'}");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		}
		catch (SystemException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void balancePay2() {
		try {
			String url = "http://121.43.59.219:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.balancePay");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("data_source", "SJLY_02");
			params.put("payment_way_key", "ZFFS_05");
			params.put("detail", "测试交易");
			params.put("amount", "1");
			params.put("out_trade_no", "20170109133420006");
			params.put("buyer_id", "9a54bf15109d460eabf5c7d8a7392373");
			params.put("seller_id", "10000001");
			params.put("out_trade_body", "{'seller_id':'10000001','buyer_id':'9a54bf15109d460eabf5c7d8a7392373'}");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
