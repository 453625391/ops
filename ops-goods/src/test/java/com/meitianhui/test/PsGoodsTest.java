package com.meitianhui.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class PsGoodsTest {

	// @Test
	public void freeGetGoodsByLabelListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "psGoods.consumer.freeGetGoodsByLabelListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("label_promotion", "1018购物节");
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

	// @Test
	public void freeGetGoodsForOperateListFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "psGoods.operate.freeGetGoodsForOperateListFind");
			Map<String, String> params = new HashMap<String, String>();
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
	public void wypGoodsFindForH5() {
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

	// @Test
	public void freeGetGoodsListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "psGoods.consumer.freeGetGoodsListPageFind");
			Map<String, String> params = new HashMap<String, String>();
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

	// @Test
	public void freeGetGoodsPreSaleListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.freeGetGoodsPreSaleListPageFind");
			Map<String, String> params = new HashMap<String, String>();
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

	// @Test
	public void freeGetGoodsNewestPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.freeGetGoodsNewestPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("area_id", "440304");
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

	// @Test
	public void recommendStoresGoodsListFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.recommendStoresGoodsListFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_id", "2531c0f36d6e466390b5380f18cc3b5f");
			params.put("area_id", "2531c0f36d6e466390b5380f18cc3b5f");
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
		params.put("goods_id", "481f5db06af24bec9b9caf5rta45d8gf");
		params.put("goods_code", "16061309");
		params.put("title", "【家居必备】晾衣架");
		params.put("desc1", "工厂直批，一 卡 10 个，20卡起批包邮");
		params.put("area_id", "440000");
		params.put("display_area", "440000");
		params.put("label", "文艺");
		params.put("category", "新品");
		params.put("contact_person", "邢先生");
		params.put("contact_tel", "15014314649");
		params.put("pic_info", "[{'path_id':'4fb812d4696846189955d7914a5984a5','title':''}]");
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
	public void psGoodsEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.psGoodsEdit");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("goods_id", "3deab1ba676148b9b3cb776d5ddd0696");
			params.put("status", "off_shelf");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void psGoodsNewEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.psGoodsNewEdit");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("goods_id", "3deab1ba676148b9b3cb776d5ddd0696");
			// params : { "goods_id", "title", "market_price",
			// "discount_price","desc1","category","data_source","goods_code","pic_info"
			// })
			params.put("title", "11");
			params.put("market_price", "12");
			params.put("discount_price", "13");
			params.put("desc1", "14");
			params.put("category", "15");
			params.put("data_source", "16");
			params.put("goods_code", "17");
			params.put("pic_info", "18");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void psGoodsDelete() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.psGoodsDelete");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("goods_id", "0e4c8e8024ce4beca515bf57913038ef");
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
			temp1.put("pic_info", "[{\"path_id\":\"df6bf7da62ae4c34b9319b380000dc3e\",\"title\":\"\"}]");
			temp1.put("order_no", "1");
			paramsList.add(temp1);
			Map<String, Object> temp2 = new HashMap<String, Object>();
			temp2.put("goods_id", "1ac5e0d8fe9c4dbe8b1a361ce841e60f");
			temp2.put("pic_info", "[{\"path_id\":\"df6bf7da62ae4c34b9319b380000dc3e\",\"title\":\"\"}]");
			temp2.put("order_no", "2");
			paramsList.add(temp2);
			params.put("activity_params", FastJsonUtil.toJson(paramsList));
			params.put("activity_type", "HDMD_04");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void psGoodsSupplyCount() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "psGoods.supply.psGoodsSupplyCount");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("supplier_id", "86387627d39345a79dd5338681c27cbe");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void storesActivityCount() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.storesActivityCount");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "3295a61d-dece-11e5-8f52-00163e0009c6");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void psGoodsActivityFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.psGoodsActivityFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("activity_type", "HDMS_06");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void psGoodsSync2() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.psGoodsSync");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("goods_code", "560733813833");
		params.put("goods_id", "ca020cbeffe44bfaa41f1a149afb2d08");
		params.put("data_source", "hsrj");
		params.put("cost_price", "0.00");
		params.put("discount_price", "6.00");
		params.put("ladder_price", "0");
		params.put("min_buy_qty", "1");
		params.put("max_buy_qty", "0");
		params.put("status", "off_shelf");
		params.put("payment_way", "online");
		params.put("shipping_fee", "0.00");
		params.put("area_id", "440304");
		params.put("delivery_area", "100000");
		params.put("title", "测试6 - 天猫商品 - 花生日记");
		params.put("display_area", "工艺");
		params.put("display_area_ck", "工艺");
		params.put("specification", "个");
		params.put("sale_qty", "97");
		params.put("contact_person", "厉先生");
		params.put("contact_tel", "17097217172");
		params.put("market_price", "6.90");
		params.put("taobao_price", "11.90");
		params.put("category", "试样");
		params.put("desc1", "铅芯细滑，色彩鲜艳，手感舒适，无味无害，环保易上色，颜色丰富，素描画画必备【赠运费险】");
		params.put("pic_info", "[{\"title\":\"\",\"path_id\":\"ad9ad5880d8347558ad6cd56cccda616\"},{\"title\":\"\",\"path_id\":\"1b65a6241ece4dcc940c77f8ae91fc95\"}]");
		params.put("pic_detail_info", "[{\"title\":\"\",\"path_id\":\"7740cf2653194b98ae83f69ff5b5c67b\"}]");
		params.put("token", "44f8846a54f342348e5d98e59f8b1069");
		params.put("label", "");
		params.put("labels", "");
		params.put("remark", "");
		params.put("label", "");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void freeGetGoodsStatusEdit() {
		String url = "http://127.0.0.1:8080/ops-goods/goods";
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("service", "goods.freeGetGoodsStatusEdit");
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("goods_id", "8a486f1f787143babc38e3dc3db6d943");
		params.put("title", "【2盒】中华神皂手工洁面皂药皂");
		params.put("market_price", "9.90");
		params.put("discount_price", "0.00");
		params.put("specification", "件");
		params.put("sale_qty", "11000");
		params.put("min_buy_qty", "1");
		params.put("max_buy_qty", "0");
		params.put("valid_thru", "2017-11-18 17:00:00");
		params.put("offline_date", "2017-11-25 23:59:59");
		params.put("service_fee", "0.00");
		params.put("settled_price", "9.90");
		params.put("taobao_price", "19.90");
		params.put("supplier_id", "86387627d39345a79dd5338681c27cbe");
		params.put("warehouse_id", "671dc7b64cc344e5988823c36189fadb");
		params.put("data_source", "hsrj");
		params.put("product_source", "￥9i8a0hilS2d￥");
		params.put("taobao_link", "http://t.cn/RlgayIK ");
		params.put("delivery_area", "100000");
		params.put("label_promotion", "日用家居");
		params.put("status", "on_shelf");
		params.put("supplier", "深圳市金惠优选贸易有限公司");
		params.put("warehouse", "民治仓库");
		params.put("online_date", "2017-11-20 18:23:28");
		try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
