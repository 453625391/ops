package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class StoresGoodsTest {

	// @Test
	public void goodsListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "storesGoods.stores.goodsListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_id", "e68bbc95a188468ea7dcaa0b690b1bfc");
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

//	 @Test
	public void stockReplenish() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "storesGoods.stores.stockReplenish");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_goods_id", "dabb3ad4b8ae4e35a8c21445c466935e");
			params.put("qty", "1");
			params.put("cost_price", "10.00");
			//params.put("production_date", "2017-05-17 18:39:18");
//			params.put("supplier", "测试供应商");
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

//	@Test
	public void storesGoodsDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "storesGoods.stores.goodsDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_goods_id", "dabb3ad4b8ae4e35a8c21445c466935e");
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

	
	@Test
	public void storesGoodsEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "storesGoods.stores.goodsEdit");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_goods_id", "f9f90cb41b534a69a75fde0c1d236e01");
			params.put("stores_id", "3ab0eb265f564e0ba34f7ad4ed971bf3");
			params.put("goods_code", "TS149144906860");
			params.put("goods_name", "测试伙拼团价格");
			params.put("category", "其它");
			params.put("barcode", "7866794");
			params.put("quick_code", "66");
			params.put("market_price", "8.00");
			params.put("sale_unit", "包");
			params.put("purchase_unit", "箱");
			params.put("min_pack_unit", "支");
			params.put("conversion_relation", "{}");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
