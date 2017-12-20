package com.meitianhui.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class TradeTest {

	// @Test
	public void orderReward() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.orderReward");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_02");
			params.put("payment_way_key", "ZFFS_06");
			params.put("detail", "赠送礼券");
			params.put("transaction_date", "2016-04-01 17:10:30");
			params.put("amount", "10");
			params.put("out_trade_no", "20160707000000002");
			params.put("mobile", "18665371520");
			params.put("buyer_id", "10000001");
			params.put("seller_id", "5fa0b7ba-f252-11e5-8f52-00163e0009c6");
			params.put("out_trade_body", FastJsonUtil.toJson(params));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void assetClear() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.assetClear");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_01");
			params.put("payment_way_key", "ZFFS_06");
			params.put("detail", "礼券清零");
			params.put("transaction_date", "2016-3-29 17:10:30");
			params.put("out_trade_no", "20160307000000002");
			params.put("buyer_id", "10000001");
			params.put("seller_id", "5fa0b7ba-f252-11e5-8f52-00163e0009c6");
			params.put("out_trade_body", FastJsonUtil.toJson(params));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void wechatCodeOrderPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.balancePay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_02");
			params.put("payment_way_key", "ZFFS_11");
			params.put("detail", "微信条码支付测试");
			params.put("transaction_date", "2017-04-22 10:10:30");
			params.put("amount", "0.01");
			params.put("out_trade_no", "20170409120020001");
			params.put("seller_id", "9654");
			params.put("buyer_id", "10000001");
			params.put("goods_name", "扫码支付");
			params.put("auth_code", "130362411755028166");
			params.put("out_trade_body", FastJsonUtil.toJson(params));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void alipayCodeOrderPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.balancePay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_02");
			params.put("payment_way_key", "ZFFS_10");
			params.put("detail", "支付宝条码支付测试");
			params.put("amount", "5000");
			params.put("out_trade_no", "20170515120020003");
			params.put("seller_id", "9654");
			params.put("buyer_id", "10000001");
			params.put("goods_name", "扫码支付");
			params.put("auth_code", "286035353555197367");
			params.put("out_trade_body", FastJsonUtil.toJson(params));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void barCodeCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "trade.barCodeCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "9654");
			params.put("auth_code_type", "P");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void barCodePay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "trade.barCodePay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_15");
			params.put("payment_way_key", "ZFFS_05");
			params.put("detail", "扫码支付(张鹤)");
			params.put("amount", "0.01");
			params.put("out_trade_no", "20170411120020002");
			params.put("member_id", "1000001");
			params.put("auth_code", "853886869834186752");
			params.put("out_trade_body", FastJsonUtil.toJson(params));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void aliPayBarCodePay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.aliPayBarCodePay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("out_trade_no", "20160307000000002");
			params.put("auth_code", "288711017751021796");
			params.put("total_amount", "0.01");
			params.put("subject", "扫码");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void wechatPayBarCodePay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.wechatPayBarCodePay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("out_trade_no", "20160307141435005");
			params.put("auth_code", "130012954333129403");
			params.put("total_fee", "0.01");
			params.put("spbill_create_ip", "127.0.0.1");
			params.put("body", "测试扫码");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void bastCodeOrderPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.balancePay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_02");
			params.put("payment_way_key", "ZFFS_14");
			params.put("detail", "翼支付测试,翼支付测试,翼支付测试,翼支付测试");
			params.put("transaction_date", "2016-05-26 10:10:30");
			params.put("amount", "0.01");
			params.put("out_trade_no", "20160225120020007");
			params.put("seller_id", "29f124b7166d4437881c277136247ab0");
			params.put("buyer_id", "10000001");
			params.put("goods_name", "测试商品");
			params.put("auth_code", "51086945610618011");
			params.put("out_trade_body", FastJsonUtil.toJson(params));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void orderPayWechat() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.orderPayWechat");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_02");
			params.put("payment_way_key", "ZFFS_02");
			params.put("detail", "兑换团购预售商品");
			params.put("transaction_date", "2016-03-30 02:10:30");
			params.put("amount", "0.01");
			params.put("out_trade_no", "20160330120000001");
			params.put("seller_id", "10000001");
			params.put("buyer_id", "29f124b7166d4437881c277136247ab0");
			params.put("out_trade_body", FastJsonUtil.toJson(params));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void transactionReverse() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.transactionReverse");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_01");
			params.put("detail", "交易冲正");
			params.put("transaction_no", "201604291733506126255813");
			params.put("out_trade_body", "20160330120000001");
			params.put("transaction_date", "2016-3-07 12:10:30");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void balanceWithdraw() {
		try {
			String url = "http://121.43.59.219:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.balanceWithdraw");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_01");
			params.put("detail", "提现");
			params.put("amount", "100");
			params.put("buyer_id", "9654");
			params.put("seller_id", "10000001");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void balancePay1() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
