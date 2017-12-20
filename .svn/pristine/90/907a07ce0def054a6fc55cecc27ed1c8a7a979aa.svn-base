package com.meitianhui.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class InterfaceTest {

	// @Test
	public void wpyGoodsSkuSync() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.wpyGoodsSkuSync");
			Map<String, String> params = new HashMap<String, String>();
			List<Map<String, Object>> goods_sku_list = new ArrayList<Map<String, Object>>();
			Map<String, Object> sku1 = new HashMap<String, Object>();
			sku1.put("goods_id", "416466f0b47b48c8b510d283dab64eb2");
			sku1.put("goods_stock_id", "fad4022a-bc66-11e6-a5d0-00163e000086");
			sku1.put("sku", "S");
			sku1.put("desc1", "小码");
			sku1.put("cost_price", "5.00");
			sku1.put("sales_price", "5.00");
			sku1.put("sale_qty", "10");
			goods_sku_list.add(sku1);
			Map<String, Object> sku2 = new HashMap<String, Object>();
			sku2.put("goods_id", "416466f0b47b48c8b510d283dab64eb2");
			sku2.put("goods_stock_id", "");
			sku2.put("sku", "M");
			sku2.put("desc1", "中码");
			sku2.put("cost_price", "5.00");
			sku2.put("sales_price", "5.00");
			sku2.put("sale_qty", "10");
			goods_sku_list.add(sku2);
			params.put("goods_sku_list", FastJsonUtil.toJson(goods_sku_list));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void psGoodsStockFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.psGoodsStockFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_id", "1ac5e0d8fe9c4dbe8b1a361ce841e60f");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void itemStoreGroupTypeForConsumerFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.itemStoreGroupTypeForConsumerFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("store_id", "f7ce488b78a64209b82d560ef1bf4667");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void psGoodsFindForH5() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.wypGoodsFindForH5");
			Map<String, String> params = new HashMap<String, String>();
			params.put("area_id", "440304");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "10");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void psGoodsListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.psGoodsListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			//params.put("data_source_type", "丁辉");
			params.put("data_source", "淘宝");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "10");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void goldExchangeGoodsDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.goldExchangeGoodsDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_id", "2957d632f1a449a4acae31bb9df65ce1");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 //@Test
	public void psGoodsDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.psGoodsDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_code", "558638806064");
			//params.put("area_id", "2531c0f36d6e466390b5380f18cc3b5f");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void psGoodsPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.psGoodsPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("category_in", "预售,新品");
			params.put("status_in", "normal,on_shelf,off_shelf");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "10");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void itemStoreListFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.itemStoreListFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("store_id", "123");
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

	// @Test
	public void itemStoreFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.itemStorePageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("store_id", "4709acb9b7aa40c894d938d001f07c13");
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

	// @Test
	public void itemStoreDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.itemStoreDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("item_name", "RIO蓝莓3.8度");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void itemStoreAdd() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.itemStoreAdd");
		Map<String, String> params = new HashMap<String, String>();
		params.put("store_id", "4709acb9b7aa40c894d938d001f07c13");
		params.put("item_name", "RIO蓝莓3.8度");
		params.put("category_id", "酒水饮料");
		params.put("brand_id", "自营");
		params.put("weight", "0");
		params.put("barcode", "7565");
		params.put("image_info", "51869b29d56d4a0483dc465738450f09");
		params.put("specification", "275ml");
		params.put("vip_price", "1.00");
		params.put("cost_price", "2.00");
		params.put("market_price", "1.50");
		params.put("durability_period", "180");
		params.put("production_date", "2015-01-30");
		params.put("stock_qty", "100");
		params.put("is_show", "Y");
		params.put("is_sell", "Y");
		params.put("is_exchange", "Y");
		params.put("manufacturer", "未知");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void psGoodsImport() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.psGoodsImport");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_code", "FS149017563881");
			params.put("product_source", "lingleme");
			params.put("qty", "1");
			params.put("remark", "领了么商品导入");
			params.put("store_id", "e08de1e5bbd3475fa87483305e33a4e8");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Test
	public void psGoodsSync() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.psGoodsSync");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("goods_id", "ac5b8df7bba542da8deb1107e1278a59");
		params.put("goods_code", "558638806064");
		params.put("title", "【家居必备】晾衣架");  
		params.put("desc1", "工厂直批，一 卡 10 个，20卡起批包邮");
		params.put("area_id", "440000");
		params.put("display_area", "440000");
		params.put("data_source", "淘宝");
		params.put("label", "文艺");
		params.put("category", "新品");
		params.put("contact_person", "骆天");
		params.put("contact_tel", "15014314649");
		params.put("pic_info", "[{'path_id':'https://img.alicdn.com/imgextra/i2/1129998532/TB2ujU3dHsTMeJjy1zbXXchlVXa_!!1129998532.jpg','title':''},{'path_id':'https://img.alicdn.com/imgextra/i2/1129998532/TB2ujU3dHsTMeJjy1zbXXchlVXa_!!1129998532.jpg_120x120q90.jpg','title':''}]");
		params.put("pic_detail_info","[{'path_id':'https://img.alicdn.com/imgextra/i2/1129998532/TB2ujU3dHsTMeJjy1zbXXchlVXa_!!1129998532.jpg','title':''},{'path_id':'https://img.alicdn.com/imgextra/i2/1129998532/TB2ujU3dHsTMeJjy1zbXXchlVXa_!!1129998532.jpg_120x120q90.jpg','title':''}]");
		params.put("data_source_type", false);
		params.put("specification", "5*10");
		params.put("pack", "包");
		params.put("cost_price", "0.50");
		params.put("market_price", "1.00");
		params.put("discount_price", "0.00");
		params.put("shipping_fee", "0.00");
		params.put("min_buy_qty", "2");
		params.put("max_buy_qty", "20");
		params.put("sale_qty", "1000");
		params.put("stock_qty", "1000");
		params.put("goods_unit", "个");
		params.put("valid_thru", "2016-08-11");
		params.put("delivery_area", "440300");
		params.put("payment_way", "online");
		params.put("status", "normal");
		params.put("created_date", "2016-06-11 18:30:30");
		params.put("ladder_price", "{}");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void psGoodsActivitySync() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.psGoodsActivitySync");
			Map<String, Object> params = new HashMap<String, Object>();
			List<Map<String, Object>> paramsList = new ArrayList<Map<String, Object>>();
			Map<String, Object> temp1 = new HashMap<String, Object>();
			temp1.put("goods_id", "1ac5e0d8fe9c4dbe8b1a361ce841e60f");
			temp1.put("activity_type", "HDMD_01");
			temp1.put("order_no", "1");
			paramsList.add(temp1);
			Map<String, Object> temp2 = new HashMap<String, Object>();
			temp2.put("goods_id", "1ac5e0d8fe9c4dbe8b1a361ce841e60f");
			temp2.put("activity_type", "HDMD_01");
			temp2.put("order_no", "2");
			paramsList.add(temp2);
			params.put("activity_params", FastJsonUtil.toJson(paramsList));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void couponIssueApply() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.couponIssueApply");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("category_id", "1");
		params.put("title", "1234元代金券");
		params.put("member_type_key", "stroes");
		params.put("member_id", "29f124b7166d4437881c277136247ab0");
		params.put("coupon_prop", "100元代金券");
		params.put("voucher_amount", "10");
		params.put("settle_price", "90");
		params.put("limit_amount", "100");
		// params.put("per_limit", "10");
		params.put("cost_price", "100");
		params.put("market_price", "100");
		params.put("vip_price", "100");
		params.put("market_price_voucher", "0");
		params.put("market_price_gold", "0");
		params.put("market_price_bonus", "0");
		params.put("issued_num", "100");
		params.put("expired_date", "2016-04-20 23:59:59");
		params.put("is_refund_anytime", "Y");
		params.put("is_refund_expired", "Y");
		params.put("pic_path", "05967dc42a614a29bee899abaa68f480");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void couponFind() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.couponFind");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("member_id", "29f124b7166d4437881c277136247ab0");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test
	public void couponDetailFind() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.couponDetailFind");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sku_id", "7e27a69214004021b40a4ae5c07b9fbb");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void couponPageFind() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.couponPageFind");
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, String> page = new HashMap<String, String>();
		page.put("page_no", "1");
		page.put("page_size", "10");
		requestData.put("page", FastJsonUtil.toJson(page));
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void couponFindForConsumer() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.couponFindForConsumer");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("member_id", "29f124b7166d4437881c277136247ab0");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void couponValidate() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.couponValidate");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("member_id", "29f124b7166d4437881c277136247ab0");
		params.put("sku_code", "4460511205835350");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void storesCcouponTotal() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.storesCouponTotal");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("member_id", "2c392db2-d9fd-11e5-a4b3-00163e0009c6");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void storesCouponCount() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.storesCouponCount");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("member_id", "2c392db2-d9fd-11e5-a4b3-00163e0009c6");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ldActivitiesCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ldActivitiesCreate");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "2c392db2-d9fd-11e5-a4b3-00163e0009c6");
			params.put("title", "小米max");
			params.put("end_date", "2016-05-12 20:11:20");
			params.put("award_name", "2c392db2-d9fd-11e5-a4b3-00163e0009c6");
			params.put("award_value", "100");
			params.put("person_num", "111");
			params.put("award_pic_path1", "2c392db2-d9fd-11e5-a4b3-00163e0009c6");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ldActivityProcessBuy() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ldActivityProcessCreate");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("activity_id", "a6edb3ab7d6542d8b038092aab15cd51");
			params.put("consumer_id", "11603134");
			params.put("qty", "4");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ldActivitiesFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ldActivitiesFind");
			Map<String, Object> params = new HashMap<String, Object>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void nearbyLdActivityStoreFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.nearbyLdActivityStoreFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("longitude", "114.034886");
			params.put("latitude", "22.540116");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "10");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ldActivitiesPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ldActivitiesPageFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "3295a61d-dece-11e5-8f52-00163e0009c6");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "10");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ldActivityProcessForStorePageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ldActivityProcessForStorePageFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("activity_id", "70f089d5b70c49d289b966011c94421f");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "10");
			requestData.put("page", FastJsonUtil.toJson(page));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ldActivityProcessForConsumerPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ldActivityProcessForConsumerPageFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("consumer_id", "10544084");
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

	// @Test
	public void ldActivityValidate() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.ldActivityValidate");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stores_id", "48a04ab78704455c967d83345878cc43");
		params.put("activity_id", "dcdc1793be7c402f8dd6db3f0e6888a7");
		params.put("consumer_id", "1c5f15236d344d9b96dd183bfa153975");
		params.put("draw_code", "10000001");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ldActivityValidateFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ldActivityValidateFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "48a04ab78704455c967d83345878cc43");
			params.put("process_id", "dcdc1793be7c402f8dd6db3f0e6888a71");
			params.put("activity_type", "dcdc1793be7c402f8dd6db3f0e6888a7");
			params.put("activity_id", "dcdc1793be7c402f8dd6db3f0e6888a7");
			params.put("consumer_id", "1c5f15236d344d9b96dd183bfa153975");
			params.put("luck_code", "10000001");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void storesExchangeIsExist() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.storesExchangeIsExist");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_id", "a745f450640445a68edd8427f9c7be4b,9654");
			requestData.put("params", FastJsonUtil.toJson(params));

			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ppActivityfind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ppActivityListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("start_date", "2016-10-26 14:49:23");
			params.put("end_date", "2016-10-31 14:49:23");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ppActivityAdd() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ppActiviyAdd");
			Map<String, String> params = new HashMap<String, String>();
			params.put("title", "绿箭活动");
			params.put("desc1", "新品活动");
			params.put("start_date", "2016-10-20 14:49:23");
			params.put("duration", "100");
			params.put("end_date", "2016-10-30 14:49:23");
			params.put("fee", "8.88");
			params.put("operator", "zhouming");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ppActivityUpdate() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ppActiviyEdit");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "559596e8cf4442e0a6d38fd7ecaeb405");
			params.put("operator", "zhouming1111");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ppActivityDetailUpdate() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ppActivityDetailEdit");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "559596e8cf4442e0a6d38fd7ecaeb405");
			params.put("detail_id", "f57e9cc86c09466a9bf48235c331d7ef");
			params.put("chosen", "Y");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ppActivityDetailAdd() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ppActiviyDetailAdd");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "559596e8cf4442e0a6d38fd7ecaeb405");
			params.put("stores_id", "1234");
			params.put("acreage", "50");
			params.put("dms", "12300");
			params.put("stores_name", "丁铭测试便利店");
			params.put("contact_person", "丁铭");
			params.put("contact_tel", "15019225349");
			params.put("address", "深圳市福田区泰然工业园");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ppActivityDetailListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ppActivityDetailListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			// params.put("detail_id", "303f39c451b342659f079dc50af3f66d");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void selectGoodsLog() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.selectGoodsLogs");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void goodsLogAdd() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.goodsLogAdd");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_id", "12312312321321321321");
			params.put("category", "下架");
			params.put("event", "商品被XXX下架了");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ppActivityListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ppActivityListForWebPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_id", "1234567");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void storesCouponProp() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.storesCouponProp");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id",
					"4f6fa5dc5e4a4f29aea11a97a08887ac,7b76259311214dc897a33744e8314e29,8474b382ec944a7fb6814a91d2e9d638");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void memberLotteryNumFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.memberLotteryNumFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "11603134");
			params.put("member_type_key", "consumer");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void memberGiftCardSend() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.memberGiftCardSend");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "11613920");
			params.put("member_type_key", "consumer");
			params.put("gift_type", "gold");
			params.put("gift_value", "100");
			params.put("operator", "摩乐");
			params.put("remark", "没事发个红包");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plActivityCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plActivityCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("title", "测试抽奖活动4");
			params.put("goods_id", "15bce8873a80453397cbfa89ea69cee7");
			params.put("prize_qty", "35");
			params.put("min_num", "15");
			params.put("lottery_time", "2016-11-18 14:49:23");
			params.put("remark", "测试4");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plActivityListForOpPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plActivityListForOpPageFind");
			Map<String, String> params = new HashMap<String, String>();
			// params.put("activity_like", "品");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plActivityListForConsumerPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plActivityListForConsumerPageFind");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plActivityListForConsumerDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plActivityListForConsumerDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "ecc6ab6a14b7454ab65ba68ad365c366");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plPartcipatorAdd() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plPartcipatorAdd");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "ecc6ab6a14b7454ab65ba68ad365c366");
			params.put("member_id", "11603136");
			params.put("member_type_key", "consumer");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plActivityCancel() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plActivityCancel");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "ecc6ab6a14b7454ab65ba68ad365c366");
			// params.put("activity_id", "11603134");
			params.put("remark", "取消了");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plLuckyDeliveryListForOpPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plLuckyDeliveryListForOpPageFind");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plLuckyDeliveryListForConsumerPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plLuckyDeliveryListForConsumerPageFind");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plPartcipatorListForOpPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plPartcipatorListForOpPageFind");
			Map<String, String> params = new HashMap<String, String>();
			// params.put("activity_id", "0ca20270739441d1a92498d9b239bf9c");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plPartcipatorListForConsumerPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plPartcipatorListForConsumerPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "11603129");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void plLuckyDeliveryDelivered() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.plLuckyDeliveryDelivered");
			Map<String, String> params = new HashMap<String, String>();
			params.put("lucky_delivery_id", "123");
			params.put("logistics", "丁铭快递");
			params.put("remark", "测试备注");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void ldActivityPayInfoFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.ldActivityPayInfoFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "0d3e6561d8794e25a3131f10026679b4");
			params.put("consumer_id", "11603129");
			params.put("activity_type", "YYY");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
