package com.meitianhui.test;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.MD5Util;

public class InterfaceTest {

	@Test
	@Ignore
	public void wypOrderCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("service", "order.wypOrderCreate");
			List<Map<String, Object>> order_list = new ArrayList<Map<String, Object>>();
			Map<String, Object> order1 = new HashMap<String, Object>();
			order1.put("desc1", "下订单");
			order1.put("item_num", "2");
			order1.put("sale_fee", "20.00");
			order1.put("discount_fee", "0.00");
			order1.put("delivery_fee", "0.00");
			order1.put("total_fee", "40.00");
			order1.put("warehouse", "若梅供应商");
			order1.put("warehouse_id", "e1efdab43f2c42049973436a6e8af051");
			order1.put("supplier", "摩乐测617");
			order1.put("supplier_id", "0826b4b860f94063901a93481e01dfa5");
			List<Map<String, Object>> goods_list = new ArrayList<Map<String, Object>>();

			Map<String, Object> ps_goods = new HashMap<String, Object>();
			ps_goods.put("goods_id", "93e42eb573634b6587b008ab99c60d4d");
			ps_goods.put("goods_title", "若梅0.01");
			ps_goods.put("goods_stock_id", "2634993a-bf82-11e6-a5d0-00163e000086");
			ps_goods.put("sku", "0250");
			ps_goods.put("sku_desc1", "0250");
			ps_goods.put("qty", "4");
			ps_goods.put("sale_price", "10.00");
			ps_goods.put("discount_fee", "5.00");
			ps_goods.put("total_fee", "20.00");
			goods_list.add(ps_goods);
			Map<String, Object> ps_goods2 = new HashMap<String, Object>();
			ps_goods2.put("goods_id", "93e42eb573634b6587b008ab99c60d4d");
			ps_goods2.put("goods_title", "若梅0.01");
			ps_goods2.put("goods_stock_id", "2634993a-bf82-11e6-a5d0-00163e000086");
			ps_goods2.put("sku", "0251");
			ps_goods2.put("sku_desc1", "0251");
			ps_goods2.put("qty", "4");
			ps_goods2.put("sale_price", "10.00");
			ps_goods2.put("discount_fee", "5.00");
			ps_goods2.put("total_fee", "20.00");
			goods_list.add(ps_goods2);
			order1.put("wyp_goods_list", FastJsonUtil.toJson(goods_list));
			order_list.add(order1);
			params.put("order_list", FastJsonUtil.toJson(order_list));
			params.put("member_id", "9654");
			params.put("member_type_key", "stores");
			params.put("stores_name", "凌度的便利店");
			params.put("contact_person", "逸亮");
			params.put("contact_tel", "13000000000");
			params.put("delivery_address", "深圳市福田区车公庙泰然科技园");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	@Ignore
	public void psOrderDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("status", "closed,cancelled");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void wypOrderListForStoresPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.wypOrderListForStoresPageFind");
			Map<String, String> params = new HashMap<String, String>();
			// params.put("member_id", "9654");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "15");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void wypOrderItemForStoresFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.wypOrderItemForStoresFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "0e708fa43a1349eaa00f00f0f8e7092b");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "15");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore 
	public void psOrderDetailPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderDetailPageFind");
			Map<String, String> params = new HashMap<String, String>();
			// params.put("order_like", "2016032412001");
			// params.put("member_id", "8dcee1d07ede487dab730a90ba595b49");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "100");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void consumerFreeGetRecordCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.consumerFreeGetRecordCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_code", "WYPG2016071360");
			params.put("consumer_id", "12244057");
			params.put("recommend_stores_id", "29f124b7166d4437881c277136247ab0");
			params.put("amount", "10.00");
			params.put("mobile", "13018089936");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void psOrderListForOpPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderListPageFindForOp");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_date_start", "2016-12-25");
			params.put("order_date_end", "2016-12-26");
			params.put("status", "normal,delete");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "20");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	@Ignore
	public void zjsOrderForConsumerPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.zjsOrderForConsumerPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("consumer_id", "10494374");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "20");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void orderByloadedCodeForStoresFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.orderByloadedCodeForStoresFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("loaded_code", "10494374");
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	@Ignore
	public void pcOrderDetailPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.pcOrderDetailPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "f93ef3c7e6e04d48b40bec1406fd55bb");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "20");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void psOrderRefund() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderRefund");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_03");
			params.put("order_id", "6a1a1eb652384132b3c2409bbe0ca2a3");
			params.put("amount", "12");
			params.put("remark", "退款12元");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void psOrderDelivered() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderDelivered");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_03");
			Map<String, String> logistics = new HashMap<String, String>();
			logistics.put("company", "顺丰");
			logistics.put("number", "20160923001");
			params.put("logistics", FastJsonUtil.toJson(logistics));
			params.put("order_id", "0198464e94e84501b2d0813ac32aab0c");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void psOrderReceived() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderReceived");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "017c2d12ec6347fdb8cc98b1d58ef2eb");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void pcOrderCancelled() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.pcOrderCancelled");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "2131f99babb74638ad06c3eb789e5058");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	 @Test
	public void psOrderSettlement() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderSettlement");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_date", "2017-05-11");
			params.put("operator", "广东省公司|刘涛");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void orderSettlementListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.orderSettlementListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "DDLX_01");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void initiateGroup() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.initiateGroup");
			Map<String, String> params = new HashMap<String, String>();
			params.put("desc1", "测试商品");
			params.put("member_id", "9654");
			params.put("member_type_key", "stores");
			params.put("stores_name", "凌度测试便利店");
			params.put("contact_tel", "13510531358");
			params.put("stores_name", "凌度测试便利店");
			params.put("delivery_address", "广东省深圳市福田区那个屯");
			params.put("retail_price", "8.00");
			params.put("qty_limit", "40");
			params.put("closing_time", "2016-09-20 17:20:00");
			Map<String, String> ps_goods = new HashMap<String, String>();
			ps_goods.put("goods_id", "d2a39d3082f54ebeaf7dab986bd52127");
			ps_goods.put("goods_code", "WYPG2016071397");
			ps_goods.put("goods_title", "小饼干");
			ps_goods.put("goods_pic_info", "[{'path_id':'25382254750d4918be2cd1b11f90cb91','title':''}]");
			ps_goods.put("goods_pic_detail_info", "[]");
			ps_goods.put("specification", "件");
			ps_goods.put("contact_person", "张三");
			ps_goods.put("contact_tel", "13890013800");
			ps_goods.put("supplier", "若梅老板");
			ps_goods.put("supplier_id", "1f0236ce8f22485187ea13de3b8e5ef8");
			ps_goods.put("manufacturer", "联想制造厂");
			ps_goods.put("goods_unit", "");
			ps_goods.put("sale_price", "6.00");
			ps_goods.put("discount_fee", "6.00");
			params.put("ps_goods", FastJsonUtil.toJson(ps_goods));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void psSubOrderCanCelled() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psSubOrderCanCelled");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "894f25709d844643aa6c17f5e7130b7f");
			params.put("data_source", "SJLY_01");
			params.put("remark", "广东省公司|刘涛");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void geOrderEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.geOrderEdit");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "f8b94d8a8a29414ab28c594dc75f2194");
			params.put("biz_remark", "丁铭测试");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void psOrderEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderEdit");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "f93ef3c7e6e04d48b40bec1406fd55bb");
			params.put("biz_remark", "丁铭测试yaya");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void mobileAttribution() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "recharger.app.mobileAttribution");
			Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", "13018089936");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void mobileRecharge() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "recharger.app.mobileRecharge");
			Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", "13018089936");
			params.put("price", "10");
			params.put("order_no", "201609141310141593655214");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore
	public void mobileRechargeNotify() {
		try {
			String url = "http://t-openapi.meitianhui.com/openapi/dahan/mobileRechargeNotify";
			Map<String, String> params = new LinkedHashMap<String, String>();
			params.put("clientOrderId", "81121593172427161611");
			params.put("mobile", "13162762655");
			params.put("callBackTime", "2016-05-13 10:21:09");
			params.put("status", "3");
			params.put("errorCode", "10010");
			params.put("errorDesc", "欠费停机");
			params.put("intervalTime", "30");
			params.put("clientSubmitTime", "2016-05-13 10:20:39");
			params.put("discount", "10.0");
			params.put("costMoney", "0.0");
			String md5Pwd = MD5Util.sign("Mth1688", "UTF-8");
			String originString = createLinkString(params) + "&" + md5Pwd;

			String req_sign = MD5Util.sign(URLEncoder.encode(originString, "UTF-8"), "UTF-8");
			params.put("sign", req_sign);
			params.put("reportTime", "May 13, 2016 10:21:09 AM");
			String result = HttpClientUtil.post(url, FastJsonUtil.toJson(params));
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	 @Ignore 
	public void mobileRechargeNotify1() {
		try {
			String url = "http://192.168.16.18:3001/openapi/daHanNotify/mobileRechargeNotify1";
			// String url =
			// "http://127.0.0.1:8080/ops-order/daHanNotify/mobileRechargeNotify";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "recharger.app.mobileRecharge");
			Map<String, String> params = new HashMap<String, String>();
			params.put("clientOrderId", "81120350476068864011");
			params.put("mobile", "13018089936");
			params.put("callBackTime", "2016-12-20 17:32:43");
			params.put("status", "0");
			params.put("errorCode", "");
			params.put("errorDesc", "");
			params.put("intervalTime", "49");
			params.put("clientSubmitTime", "2016-12-20 17:31:54");
			params.put("discount", "0");
			params.put("costMoney", "0.0");
			String md5Pwd = MD5Util.sign("Mth1688", "UTF-8");
			String originString = createLinkString(params) + "&" + md5Pwd;

			String req_sign = MD5Util.sign(URLEncoder.encode(originString, "UTF-8"), "UTF-8");
			params.put("sign", req_sign);
			params.put("reportTime", "May 13, 2016 10:21:09 AM");
			String result = HttpClientUtil.post(url, FastJsonUtil.toJson(params));
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void geOrderPayCreateNotify() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("service", "order.geOrderPayCreateNotify");
			List<Map<String, Object>> order_item_list = new ArrayList<Map<String, Object>>();
			Map<String, Object> ps_goods2 = new HashMap<String, Object>();
			ps_goods2.put("goods_id", "c9aeba074872492bbe723a1d4599329a");
			ps_goods2.put("goods_code", "XS148298084306");
			ps_goods2.put("retail_price", "4");
			ps_goods2.put("qty", "1");
			ps_goods2.put("discount_price", "0.01");
			ps_goods2.put("cash_paid", "0.01");
			ps_goods2.put("gold_paid", "1");
			order_item_list.add(ps_goods2);

			params.put("order_item_list", FastJsonUtil.toJson(order_item_list));
			params.put("member_id", "9654");
			params.put("member_type_key", "stores");
			params.put("member_info", "13000000000");
			params.put("desc1", "12312131");
			params.put("item_num", "2");
			params.put("payment_way_key", "ZFFS_01");
			params.put("cash_amount", "0.01");
			params.put("gold_amount", "1");
			params.put("data_source", "SJLY_01");
			params.put("amount", "0.01");
			params.put("contact_person", "逸亮");
			params.put("contact_tel", "13000000000");
			params.put("delivery_address", "深圳市福田区车公庙泰然科技园");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			// 拼接时，不包括最后一个&字符
			if (i == keys.size() - 1) {
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}
}
