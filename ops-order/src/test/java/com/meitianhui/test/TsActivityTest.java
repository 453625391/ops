package com.meitianhui.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

public class TsActivityTest {

	@Test
	@Ignore
	public void tsActivityCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsActivity.consumer.tsActivityCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "20000001");
			params.put("member_type_key", "consumer");
			params.put("member_mobile", "13018089936");
			params.put("goods_id", "60945e6e5cb94a0da0ad73ee83c654d1");
			params.put("min_num", "2");
			params.put("amount", "56");
			params.put("expiried_hour", "24");
			params.put("stores_id", "5fa0b7ba-f252-11e5-8f52-00163e0009c6");
			params.put("received_mode", "pick_up");
			params.put("payment_way_key", "ZFFS_01");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void tsOrderCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsOrder.consumer.ladderTsOrderCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "20000001");
			params.put("member_type_key", "consumer");
			params.put("member_mobile", "13018089936");
			params.put("activity_id", "9c84aee02477442cab1c2e79f6667c54");
			params.put("amount", "3.00");
			params.put("payment_way_key", "ZFFS_01");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void nearbyTsActivityListFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsActivity.consumer.nearbyTsActivityListFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("goods_id", "3ed2251c32c846389f5cbf64e1af4d55");
			params.put("longitude", "114.035136");
			params.put("latitude", "22.539934");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void tsOrderCancel() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsOrder.consumer.tsOrderCancel");
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_id", "68b2d7cd3e5c4c00bd48e798a8345230");
			params.put("remark", "不要了");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void tsActivityDetail() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsActivity.consumer.tsActivityDetail");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "b48810241b6c499a84d3f54e91629508");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void qualificationValidate() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsActivity.consumer.qualificationValidate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("conusmer_id", "20000001");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void tsOrderListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsOrder.operate.tsOrderListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "20000001");
			params.put("member_type_key", "consumer");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void tsActivityCountForH5() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsActivity.consumer.tsActivityCountForH5");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "f1b55b45d4da4069b94da85701bade85");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test
	@Ignore
	public void tsActivityListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsActivity.operate.tsActivityListPageFind");
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

	@Test
	@Ignore
	public void joinListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsActivity.consumer.joinListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "9032e52733824f7d93da9b2d744fe491");
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
	public void sponsorTsActivityListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsActivity.consumer.sponsorTsActivityListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("activity_id", "9032e52733824f7d93da9b2d744fe491");
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
	public void tsOrderSettle() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsOrder.operate.tsOrderSettle");
			Map<String, String> params = new HashMap<String, String>();
			params.put("operator", "刘涛");
			params.put("order_date", "2017-03-07");
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
	public void freeCouponPay() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsOrder.consumer.freeCouponPay");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "20000001");
			params.put("operator", "刘涛");
			params.put("order_date", "2017-03-07");
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
	
//	@Test
	public void ladderTsOrderValidate() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsOrder.consumer.ladderTsOrderValidate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "20000001");
			params.put("goods_id", "0d48a675bf4f4b75a78b194098f0caab");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void tsOrderCountForStoresSale() {
		try {
			String url = "http://127.0.0.1:8080/ops-order/order";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "tsOrder.stores.tsOrderCountForStoresSale");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_id", "e08de1e5bbd3475fa87483305e33a4e8");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
