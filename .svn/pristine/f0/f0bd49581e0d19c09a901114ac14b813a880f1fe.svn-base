package com.meitianhui.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class OrderPayTest {

//	@Test
	public void orderPayPsGoods() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.orderPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_12");
			params.put("payment_way_key", "ZFFS_05");
			// params.put("order_type_key", "DDLX_01");
			params.put("detail", "兑换商品");
			params.put("transaction_date", "2016-03-25 02:10:30");
			params.put("amount", "100");
			params.put("seller_id", "10000001");
			params.put("buyer_id", "9654");
			Map<String, String> orderParams = new HashMap<String, String>();
			orderParams.put("desc1", "兑换商品");
			orderParams.put("item_num", "1");
			orderParams.put("payment_way_key", "2");
			orderParams.put("total_fee", "50.00");
			orderParams.put("discount_fee", "0.00");
			orderParams.put("sale_fee", "50.00");
			orderParams.put("weight", "50");
			orderParams.put("member_id", "9654");
			orderParams.put("member_type_key", "stores");
			orderParams.put("delivery_address", "深圳市福田区车公庙泰然科技园");
			orderParams.put("contact_person", "大哥");
			orderParams.put("contact_tel", "13018089936");
			Map<String, Object> ps_goods = new HashMap<String, Object>();
			ps_goods.put("goods_id", "5cca6a57bb1248ef9eb92e3bf2e26932");
			ps_goods.put("qty", "1");
			ps_goods.put("weight", "20");
			ps_goods.put("goods_unit", "件");
			ps_goods.put("sale_price", "99.00");
			ps_goods.put("total_fee", "79.00");
			ps_goods.put("discount_fee", "79.00");
			ps_goods.put("remark", "商品一号");
			orderParams.put("ps_goods", FastJsonUtil.toJson(ps_goods));
			params.put("out_trade_body", FastJsonUtil.toJson(orderParams));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void orderPaycoupon() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.orderPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_01");
			params.put("payment_way_key", "ZFFS_05");
			params.put("detail", "购买优惠券");
			params.put("transaction_date", "2016-04-25 02:10:30");
			params.put("amount", "100");
			params.put("seller_id", "10000001");
			params.put("buyer_id", "40a48e81a0eb42829f6b48f69bab0ce2");
			params.put("order_type_key", "DDLX_02");
			Map<String, String> couponParams = new HashMap<String, String>();
			couponParams.put("member_id", "40a48e81a0eb42829f6b48f69bab0ce2");
			couponParams.put("member_type_key", "consumer");
			couponParams.put("item_id", "8a8921a460d64cbf93751b9849a160a6");
			params.put("out_trade_body", FastJsonUtil.toJson(couponParams));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void orderPayLdActivities() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.orderPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_01");
			params.put("payment_way_key", "ZFFS_05");
			params.put("detail", "一元购");
			params.put("transaction_date", "2016-05-15 02:10:30");
			params.put("amount", "2");
			params.put("seller_id", "10000001");
			params.put("buyer_id", "11696190");
			params.put("order_type_key", "DDLX_03");
			Map<String, String> couponParams = new HashMap<String, String>();
			couponParams.put("consumer_id", "11696190");
			couponParams.put("activity_id", "040756860dbb4b61b6d630ecf447d714");
			couponParams.put("stores_id", "e68bbc95a188468ea7dcaa0b690b1bfc");
			couponParams.put("price", "1");
			couponParams.put("total_fee", "2");
			couponParams.put("qty", "2");
			params.put("out_trade_body", FastJsonUtil.toJson(couponParams));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void orderPayPcOrder() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.orderPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_01");
			params.put("payment_way_key", "ZFFS_05");
			params.put("detail", "掌上便利店订单");
			params.put("transaction_date", "2016-05-20 02:10:30");
			params.put("amount", "3");
			params.put("seller_id", "10000001");
			params.put("buyer_id", "13d8fbf1c0864c4da46ade3109a3d87b");
			params.put("order_type_key", "DDLX_04");

			Map<String, Object> order = new HashMap<String, Object>();
			List<Map<String, Object>> store_item_list = new ArrayList<Map<String, Object>>();
			Map<String, Object> item1 = new HashMap<String, Object>();
			item1.put("item_store_id", "7cd703b9153b491b98269b50f113e37c");
			item1.put("item_name", "RIO蓝莓3.8度");
			item1.put("image_info", "51869b29d56d4a0483dc465738450f09");
			item1.put("weight", "1");
			item1.put("qty", "4");
			item1.put("sale_price", "1.50");
			item1.put("discount_fee", "0");
			item1.put("total_fee", "6.00");
			store_item_list.add(item1);
			Map<String, Object> item2 = new HashMap<String, Object>();
			item2.put("item_store_id", "7cd703b9153b491b98269b50f113e37c");
			item2.put("item_name", "RIO蓝莓3.8度");
			item2.put("image_info", "51869b29d56d4a0483dc465738450f09");
			item2.put("weight", "1");
			item2.put("qty", "4");
			item2.put("sale_price", "1.50");
			item2.put("discount_fee", "0");
			item2.put("total_fee", "6.00");
			store_item_list.add(item2);

			order.put("stores_id", "08665b80-ed03-11e5-a022-fcaa1490ccaf");
			order.put("stores_name", "测试门店");
			order.put("consumer_id", "13d8fbf1c0864c4da46ade3109a3d87b");
			order.put("desc1", "测试商品");
			order.put("item_num", 2);
			order.put("sale_fee", "12.00");
			order.put("discount_fee", "0");
			order.put("total_fee", "12.00");
			order.put("delivery_address", "测试地址");
			order.put("contact_person", "张三");
			order.put("contact_tel", "13018089936");
			order.put("store_item_list", FastJsonUtil.toJson(store_item_list));
			params.put("out_trade_body", FastJsonUtil.toJson(order));
			requestData.put("params", FastJsonUtil.toJson(params));
			System.out.println(requestData.toString());
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void orderPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.orderPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_06");
			params.put("payment_way_key", "ZFFS_17");
			params.put("detail", "收款");
			params.put("amount", "10");
			params.put("out_trade_no", "20160725120020007");
			params.put("seller_id", "5fa0b7ba-f252-11e5-8f52-00163e0009c6");
			params.put("buyer_id", "11603129");
			params.put("out_trade_body", FastJsonUtil.toJson(params));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void huidianWechatPcPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.huidianWechatPcPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_06");
			params.put("req_source", "PC");
			params.put("member_id", "9654");
			params.put("return_url", "http://domain.com/CallBack/return_url.jsp");
			params.put("price", "0.01");
			params.put("payment_id", "20160818171234253");
			params.put("tid", "20160818171234253");
			params.put("time_expire", "20170116191210");
			params.put("detail", "南方黑芝麻");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void hydOrderWebPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.hyd2OrderWebPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_06");
			params.put("req_source", "PC");
			params.put("member_id", "9654");
			params.put("return_url", "http://domain.com/CallBack/return_url.jsp");
			params.put("price", "0.01");
			params.put("payment_id", "20160818171234251");
			params.put("tid", "20160818171234251");
			params.put("timeout", "10m");
			params.put("detail", "南方黑芝麻");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void hyd2OrderPerCodePay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.hyd2OrderPerCodePay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_06");
			params.put("member_id", "9654");
			params.put("price", "0.01");
			params.put("payment_id", "20160818171234251");
			params.put("tid", "20160818171234251");
			params.put("timeout", "10m");
			params.put("detail", "南方黑芝麻");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void wypOrderPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.wypOrderPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_06");
			params.put("total_fee", "0.01");
			params.put("order_no", "80805660221715660811");
			params.put("order_id", "013f6314a34f4838960dc0c7d25808be");
			params.put("payment_way_key", "ZFFS_15");
			params.put("member_id", "9654");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ldOrderPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.ldOrderPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_02");
			params.put("payment_way_key", "ZFFS_05");
			params.put("activity_id", "0d3e6561d8794e25a3131f10026679b4");
			params.put("activity_type", "YYY");
			params.put("award_name", "我的人");
			params.put("total_fee", "1");
			params.put("consumer_id", "11603129");
			params.put("stores_id", "9a54bf15109d460eabf5c7d8a7392373");
			params.put("draw_token", "fEkd4rIi");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void orderPayMiniApp() {
		try {
			String url = "http://127.0.0.1:8080/ops-finance/finance";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "finance.miniAppWechatPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_17");
			params.put("payment_way_key", "ZFFS_02");
			// params.put("order_type_key", "DDLX_01");
			params.put("detail", "名品汇");
			params.put("transaction_date", "2016-03-25 02:10:30");
			params.put("amount", "100");
			params.put("seller_id", "10000001");
			params.put("buyer_id", "9654");
			params.put("openid", "oA9D20EZogOYpVNKYEjfAVUWfGaQ");
			params.put("out_trade_no", "");
			params.put("item_num", "1");
			params.put("business_type_key", "JYLX_07");
			params.put("order_type_key", "DDLX_17");
			params.put("out_trade_body", "{\\\"contact_tel\\\":\\\"18771786450\\\",\\\"remark\\\":\\\" 是的法国红酒\\\",\\\"contact_person\\\":\\\"\\\",\\\"member_type_key\\\":\\\"consumer\\\",\\\"cash_amount\\\":\\\"298.00\\\",\\\"member_id\\\":\\\"0bc323a0d01811e7ac35c39610748aaf\\\",\\\"data_source\\\":\\\"SJLY_17\\\",\\\"payment_way_key\\\":\\\"ZFFS_02\\\",\\\"item_num\\\":1}\",\"business_type_key\":\"JYLX_07\",\"order_type_key\":\"DDLX_17\"}");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
