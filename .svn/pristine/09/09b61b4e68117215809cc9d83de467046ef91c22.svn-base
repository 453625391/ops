package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class TaskTest {

	@Test
	public void plActivityStatusRefresh() {
		try {
			String url = "http://127.0.0.1:8080/ops-goods/goodsTask";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "goods.updateDisabledGcActivity");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
