package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class PsGoodsFavoritesTest {

//	@Test
	public void fgGoodsFavoritesListFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "psGoodsFavorites.app.fgGoodsFavoritesListFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_type_key", "consumer");
			params.put("member_id", "11613920");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
//	@Test
	public void psGoodsFavoritesCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goods";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "psGoodsFavorites.app.psGoodsFavoritesCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("favorites_type", "llm");
			params.put("goods_id", "022c728051574511bd252e3e2a742b22");
			params.put("member_type_key", "consumer");
			params.put("member_id", "11613920");
			params.put("valid_thru", "2017-04-11");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
