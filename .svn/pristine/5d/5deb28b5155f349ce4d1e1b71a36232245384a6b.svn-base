package com.meitianhui.goods.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring.xml"})
public class GoodsSercieImplTest {
	@Autowired
	PsGoodsService psGoodsService;
	
	@Test
	public void testSaveUser(){
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
			psGoodsService.psGoodsSync(params,null);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	@Test
	public void psGoodsSync2s() {
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
	public void psGoodsSync2() {
		String url = "http://127.0.0.1:8080/omp-mth-freeGet/goods/editGoods";
		Map<String, String> requestData = new HashMap<String, String>();
		//requestData.put("service", "editGoods");
		System.out.println("22222222222222222222222222");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("area_id", "440304");
		params.put("contact_tel", "17097217172");
		params.put("delivery_area", "100000");
		params.put("discount_end", "2017-11-09 23:59:59");
		params.put("pic_info", "[{'title':'','path_id':'0bdb5df01b4647db8bf892aabfb33f28'}]");
		params.put("data_source", "hsrj");
		params.put("min_buy_qty", "0");
		params.put("desc1", "经常穿高跟鞋的姑娘，脚上都有老茧，基本上用了一天脚垫就范白了，不刺激皮肤~");
		params.put("title", "【云南本草】祛脚底鸡眼手脚老茧膏");
		params.put("max_buy_qty", "0");
		params.put("token", "9f7b23aa7f164ce68fa58697f30c9804");
		params.put("discount_price", "0.00");
		params.put("goods_code", "550928554796");
		params.put("payment_way", "online");
		params.put("taobao_price", "35.90");
		params.put("ladder_price", "0");
		params.put("cost_price", "0.00");
		params.put("status", "normal");
		params.put("contact_person", "厉先生");
		params.put("commission_rate", "90.50");
		params.put("voucher_link", "http://shop.m.taobao.com/shop/coupon.htm?seller_id=2336283349&activity_id=0d39b3f435fc49bfa3477eef073cc707");
		params.put("shipping_fee", "0.00");
		params.put("category", "试样");
		params.put("data_source_type", "0");
		params.put("market_price", "5.90");
		params.put(" manufacturer", "硕果旗舰店");
		params.put("goods_id", "6c0397907d804e4ea1c37de7778a0f88");
		params.put("display_area", "家饰家纺,百货,餐厨,家庭保健");
		params.put("quan_id", "0d39b3f435fc49bfa3477eef073cc707");
		params.put("pic_detail_info", "481f5db06af24bec9b9caf5rta45d8gf");
			try {
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
