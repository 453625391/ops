package com.meitianhui.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class FinanceTaskTest {

	
	

	
//	 @Test
	public void alipayBillImport() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/financeTask";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "facePay.alipay.billImport");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("bill_date", "2017-03-23");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	 @Test
	public void wechatBillImport() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/financeTask";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "consumer.wechat.billImport");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("bill_date", "2017-03-23");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void transactionStatusConfirmed() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "trade.transactionStatusConfirmed");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("data_source", "SJLY_02");
			params.put("payment_way_key", "ZFFS_01");
			params.put("transaction_no", "17033084743002746939392011");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


//	@Test
	public void alipayBillCheck() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/financeTask";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "alipay.billCheck");
			Map<String, String> params = new HashMap<String, String>();
			params.put("bill_date", "2017-03-22");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

//	@Test
	public void wechatBillCheck() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/financeTask";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "wechat.billCheck");
			Map<String, String> params = new HashMap<String, String>();
			params.put("bill_date", "2017-03-22");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
