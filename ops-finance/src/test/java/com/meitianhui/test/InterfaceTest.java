package com.meitianhui.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class InterfaceTest {
	@Test
	public void barCodePay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "trade.salesassistant.barCodePay"); 
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("data_source", "SJLY_16");
			params.put("payment_way_key", "ZFFS_10");
			params.put("auth_code", "282927378466778832");
			params.put("amount", "0.01");
			params.put("member_id", "7ce661139df34a8b8c59cee19be7eba2");
			params.put("detail", "2");
			params.put("out_trade_body", "4444");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	@Test
	public void initMemberAsset() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.initMemberAsset");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("member_id", "2");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void balanceRecharge() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.balanceRecharge");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("out_trade_no", "2016120617353365462086");
			params.put("data_source", "SJLY_14");
			params.put("payment_way_key", "ZFFS_01");
			params.put("detail", "现金充值");
			params.put("amount", "10.00");
			params.put("member_id", "9654");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void balancePay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.balancePay");
			Map<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("data_source", "SJLY_02");
			params.put("payment_way_key", "ZFFS_05");
			params.put("detail", "送余额");
			params.put("amount", "1000000");
			params.put("security_code_type", "P");
			params.put("security_code", "803889152818941952");
			params.put("currency_code", "CNY");
			params.put("out_trade_no", "20160109133420006");
			params.put("buyer_id", "10000001");
			params.put("seller_id", "11603135");
			params.put("out_trade_body", "{'seller_id':'9654','buyer_id':'11603135','security_code':'Q3BmV8a4','security_code_type':'P'}");
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

	@Test
	public void transactionConfirmed() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.transactionConfirmed");
			Map<String, String> params = new HashMap<String, String>();
			params.put("transaction_no", "20160105140640466");
			params.put("transaction_status", "confirmed");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void consumerVoucherBill() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.consumerVoucherBill");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "12105503");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void memberCashLogPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.memberCashLogPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "11613920");
			params.put("member_type_key", "consumer");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void memberAssetQuery() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.memberAssetQuery");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "11603129");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void storeVoucherAssetClear() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.storeVoucherAssetClear");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_ids", "29f124b7166d4437881c277136247ab0,52971525-e445-11e5-a022-fcaa1490ccaf");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void stores_alipay_query() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.stores_alipay_query");
			Map<String, String> params = new HashMap<String, String>();
			params.put("out_trade_no", "16121680943946987890688011");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
//	@Test
	public void getAlipayInfo() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.getAlipayInfo");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void getWechatPayInfo() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.getWechatPayInfo");
			Map<String, String> params = new HashMap<String, String>();
			params.put("out_trade_no", "20160120000000005");
			params.put("amount", "0.01");
			params.put("body", "余额支付使用微信支付");
			params.put("spbill_create_ip", "127.0.0.1");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void alipayNotify() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/alipayNotify";
			Map<String, String> params = new HashMap<String, String>();
			params.put("out_trade_no", "20160124120459055");
			params.put("buyer_email", "2206703344@qq.com");
			params.put("buyer_id", "2088002860131672");
			params.put("discount", "0.00");
			params.put("gmt_create", "2016-01-20 15:30:20");
			params.put("gmt_payment", "2016-01-20 15:30:21");
			params.put("is_total_fee_adjust", "N");
			params.put("notify_id", "d5b1c613a3c174290c1f91f502ccbe9l64");
			params.put("notify_time", "2015-10-22 18:56:32");
			params.put("notify_type", "trade_status_sync");
			params.put("payment_type", "1");
			params.put("price", "0.01");
			params.put("quantity", "0.01");
			params.put("seller_email", "mhqc88@126.com");
			params.put("seller_id", "2088911388210931");
			params.put("sign", "BaLeJqJNMVKTJmb7kOUmLMwrp07f7eWLZm5oh");
			params.put("sign_type", "RSA");
			params.put("subject", "每天惠充值");
			params.put("total_fee", "0.01");
			params.put("trade_no", "2015102200001000770063135489");
			params.put("trade_status", "TRADE_SUCCESS");
			params.put("use_coupon", "N");
			String result = HttpClientUtil.post(url, params);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void weichatNotify() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/storeWechatNotify";
			Map<String, String> temp = new HashMap<String, String>();
			Map<String, String> params = new HashMap<String, String>();
			params.put("openid", "og6EqxJ8yFVmekHa7ZbrrHXMukj8");
			params.put("trade_type", "APP");
			params.put("return_code", "SUCCESS");
			params.put("time_end", "20160307102104");
			params.put("mch_id", "1301202501");
			params.put("cash_fee", "1");
			params.put("is_subscribe", "N");
			params.put("bank_type", "CFT");
			params.put("out_trade_no", "20160325130803256");
			params.put("sign", "53342F0105C4B6AF7EAEE62A215ADC29");
			params.put("transaction_id", "1004940998201603033702262623");
			params.put("total_fee", "1");
			params.put("appid", "wxe5ea205b603aff48");
			params.put("noncestr", "20150531233956996351");
			params.put("result_code", "SUCCESS");
			temp.put("wechat_notify_data", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, temp);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void prepayCardActivate() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.prepayCardActivate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "6986f013f2d147329c1a30d71a391b95");
			params.put("card_no", "MTH0000000000001102Z");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


//	@Test
	public void prepayCardScan() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.prepayCardScan");
			Map<String, String> params = new HashMap<String, String>();
			params.put("card_no", "MTH0000000000000100Z");
			// 0000000a
			// 00000006
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


//	@Test
	public void storeVoucherBill() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.storeVoucherBill");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "3f7accbd-e5e4-11e5-a022-fcaa1490ccaf");
			params.put("transaction_date_start", "2016-03-28");
			params.put("transaction_date_end", "2016-03-29");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void consumeCashBill() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.consumeCashBill");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "40a48e81a0eb42829f6b48f69bab0ce2");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void storeCashCount() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.cashCountStore");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "29f124b7166d4437881c277136247ab0");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void storeVoucherBillCount() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.storeVoucherBillCount");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "29f124b7166d4437881c277136247ab0");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void storeVoucherRewardAccountCount() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.storeVoucherRewardAccountCount");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "e68bbc95a188468ea7dcaa0b690b1bfc");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void storesCashierCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.storesCashierCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("flow_no", "20160126");
			params.put("stores_id", "29f124b7166d4437881c277136247ab0");
			params.put("amount", "20");
			params.put("discount_amount", "2.0");
			params.put("pay_amount", "18");
			params.put("payment_way_key", "ZFFS_01");
			params.put("cashier_id", "10001");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void storesCashierFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.storesCashierFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "29f124b7166d4437881c277136247ab0");
			params.put("created_date", "2016-03-03");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void memberCapitalAccountApplicationApplyListFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.memberCapitalAccountApplicationApplyListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_type_key", "stores");
			params.put("member_id", "f7ce488b78a64209b82d560ef1bf4667");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void memberCapitalAccountCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.memberCapitalAccountCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_type_key", "stores");
			params.put("member_id", "29f124b7166d4437881c277136247ab0");
			params.put("capital_account_type_key", "ZHFL_03");
			params.put("capital_account", "201603170001");
			params.put("cardholder", "张三");
			params.put("publishing_institutions", "招商");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void memberCapitalAccountEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.memberCapitalAccountEdit");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "29f124b7166d4437881c277136247ab0");
			params.put("capital_account", "138232654");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void memberCapitalAccountFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.memberCapitalAccountFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_type_key", "stores");
			params.put("member_id", "29f124b7166d4437881c277136247ab0");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void memberCouponFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.memberCouponFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "29f124b7166d4437881c277136247ab0");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void tradeConsumerListForStores() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.tradeConsumerListForStores");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_id", "e68bbc95a188468ea7dcaa0b690b1bfc");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void serviceFeeUnFreeze() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.serviceFeeUnFreeze");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_01");
			params.put("detail", "测试解绑");
			params.put("member_id", "3295a61d-dece-11e5-8f52-00163e0009c6");
			params.put("member_type_key", "stores");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	@Test
	public void voucherExchangeLogFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "voucher.app.voucherExchangeLogFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("consumer_id", "11603129");
			params.put("mobile", "13018089936");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	@Test
	public void voucherExchangeGold() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "voucher.app.voucherExchangeGold");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_01");
			params.put("consumer_id", "11603129");
			params.put("voucher", "100");
			params.put("gold", "1");
			params.put("mobile", "13018089936");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void voucherExchange() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "voucher.app.voucherExchange");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_01");
			params.put("consumer_id", "11603129");
			params.put("stores_id", "9654");
			params.put("voucher", "10");
			params.put("stores_name", "测试");
			params.put("mobile", "13018089936");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void mobileRechargeOrderPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "mobileRecharge.app.orderPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_01");
			params.put("member_id", "11603129");
			params.put("member_mobile", "13018089936");
			params.put("member_name", "13018089936");
			params.put("member_type_key", "consumer");
			params.put("mobile", "13419203677");
			params.put("goods_id", "e14640f9-c5fc-11e6-a18f-00163e000086");
			params.put("payment_way_key", "ZFFS_05");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
