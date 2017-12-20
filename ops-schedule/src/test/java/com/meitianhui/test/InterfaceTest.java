package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class InterfaceTest {


	@Test
	public void psOrderFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderFind");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void psOrderDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_like", "201603");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void psOrderDetailPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderDetailPageFind");
			Map<String, String> params = new HashMap<String, String>();
			// params.put("order_like", "2016032412001");
			params.put("member_id", "8dcee1d07ede487dab730a90ba595b49");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "50");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void consumerFreeGetRecordCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.consumerFreeGetRecordCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_code", "5674576");
			params.put("consumer_id", "12244057");
			params.put("recommend_stores_id", "29f124b7166d4437881c277136247ab0");
			params.put("amount", "10.00");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void freeGetOrderImport() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.freeGetOrderImport");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_code", "WYPG2016071322");
			params.put("qty", "1");
			params.put("contact_person", "张三");
			params.put("contact_tel", "18718688437");
			params.put("delivery_address", "深圳市福田区车公庙泰然科技园");
			params.put("logistics", "物流信息");
			params.put("amount", "100");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void fgOrderDetailPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.fgOrderDetailPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "29f124b7166d4437881c277136247ab0");
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

	//@Test
	public void pcOrderDetailPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.pcOrderDetailPageFind");
			Map<String, String> params = new HashMap<String, String>();
			// params.put("member_id", "e68bbc95a188468ea7dcaa0b690b1bfc");
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

	//@Test
	public void psOrderAdd() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderAdd");
			Map<String, String> params = new HashMap<String, String>();
			params.put("desc1", "下订单");
			params.put("item_num", "2");
			params.put("total_fee", "20.00");
			params.put("discount_fee", "10.00");
			params.put("sale_fee", "30.00");
			params.put("weight", "50");
			params.put("member_id", "132456");
			params.put("member_type_key", "132456");
			params.put("delivery_address", "深圳市福田区车公庙泰然科技园");
			Map<String, Object> ps_goods = new HashMap<String, Object>();
			ps_goods.put("goods_id", "ec835573-5ecd-412c-8b9b-9fd5ae7318dd");
			ps_goods.put("qty", "30");
			ps_goods.put("weight", "20");
			ps_goods.put("goods_unit", "件");
			ps_goods.put("sale_price", "30.00");
			ps_goods.put("total_fee", "20.00");
			ps_goods.put("discount_fee", "10.00");
			ps_goods.put("remark", "商品一号");
			params.put("ps_goods", FastJsonUtil.toJson(ps_goods));
			requestData.put("params", FastJsonUtil.toJson(params));

			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void psOrderRefund() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderRefund");
			Map<String, String> params = new HashMap<String, String>();
			params.put("data_source", "SJLY_03");
			params.put("order_no", "201605281528331656407085");
			params.put("amount", "0.02");
			params.put("remark", "退款0.02元");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void psOrderReceived() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.psOrderReceived");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "6c2829afd7ad4506bb5e6418385ede91");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
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

	//@Test
	public void leOrderDetailPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.leOrderDetailPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_like", "2016032412001");
			params.put("status", "received,cancelled,payed");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "50");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void leOrderDetailPageForConsumerFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.leOrderDetailPageForConsumerFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_like", "2016032412001");
			params.put("status", "received,cancelled,payed");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "50");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void leOrderDeliver() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.leOrderDeliver");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "2131f99babb74638ad06c3eb789e5058");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void leOrderReceived() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.leOrderReceived");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "2131f99babb74638ad06c3eb789e5058");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void leOrderCancelled() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.leOrderCancelled");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "2131f99babb74638ad06c3eb789e5058");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void consumerFreeGetOrderCancelled() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.consumerFreeGetOrderCancelled");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "10e7acea136f4d5aa4657b648e38cb91");
			params.put("remark", "不想领取了");
			params.put("data_source", "SJLY_02");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void fgOrderSettlement() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "order.fgOrderSettlement");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_ids", "6fa9296c949e422280429895ed948535,f16f5dc635164f1384b37e05ea299ed3");
			params.put("operator", "广东省公司|刘涛");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
