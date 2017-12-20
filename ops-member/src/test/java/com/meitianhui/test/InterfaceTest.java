package com.meitianhui.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.meitianhui.common.constant.CommonConstant;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;

@SuppressWarnings("unchecked")
public class InterfaceTest {
	

	
	//@Test
	public void userRecommendFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "consumer.consumer.userRecommendFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "10010041");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void userRecommendCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "consumer.consumer.userRecommendCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("recommend_mobile", "15817247920");
			params.put("member_id", "10010041");
			params.put("member_mobile", "15817247920");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void storesBySpecialistListFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.storesBySpecialistListFind");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void storesBySpecialistClear() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.storesBySpecialistClear");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_id", "000d800ef8fc487b8cfa6a4c4a97d718");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void storesBySpecialistSync() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.storesBySpecialistSync");
			Map<String, String> params = new HashMap<String, String>();
			params.put("assistant", "KK");
			params.put("assistant_id", "KK");
			params.put("stores_id", "000d800ef8fc487b8cfa6a4c4a97d718");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void storesForConsumerAssetListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "stores.salesassistant.storesForConsumerAssetListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_id", "3ab0eb265f564e0ba34f7ad4ed971bf3");
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "2");
			requestData.put("page", FastJsonUtil.toJson(page));	
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void salesmanDataFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.salesmanDataFind");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void assistantApplicationApplyDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.assistantApplicationApplyDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("application_id", "1269ebc8e88e41d6a88144113ed98bb9");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void salesmanStoresNumFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "stores.salesassistant.salesmanStoresNumFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "62241fbeb2ea41698869407f1edd65ff");
			params.put("contact_tel", "15814138658");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void assistantApplicationApplyListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.assistantApplicationApplyListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("search_input", "联兴");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void nearbyStoresTypeListPageFindForConsumer() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "stores.consumer.nearbyStoresTypeListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("longitude", "114.035121");
			params.put("latitude", "22.540123");	
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "2");
			requestData.put("page", FastJsonUtil.toJson(page));	
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void nearbyStoresTypeListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "stores.salesassistant.nearbyStoresTypeListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("find_type", "CXLX_6");
			params.put("longitude", "114.035121");
			params.put("latitude", "22.540123");
			params.put("assistant_id", "62241fbeb2ea41698869407f1edd65ff");
			params.put("search_like", "信");
			params.put("business_developer", "62241fbeb2ea41698869407f1edd65ff");
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "2");
			requestData.put("page", FastJsonUtil.toJson(page));	
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void assistantApplicationPass() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.assistantApplicationPass");
			Map<String, String> params = new HashMap<String, String>();
			params.put("application_id", "62241fbeb2ea41698869407f1edd65ff");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void assistantApplicationApplyReject() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.assistantApplicationApplyReject");
			Map<String, String> params = new HashMap<String, String>();
			params.put("application_id", "62241fbeb2ea41698869407f1edd65ff");
			params.put("remark", "1KK");
			params.put("logRemark", "1KK");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void systemInform() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.systemInform");
			Map<String, String> params = new HashMap<String, String>();
			params.put("cre_by", "7ce661139df34a8b8c59cee19be7eba2");	
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "2");
			requestData.put("page", FastJsonUtil.toJson(page));	
			
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void assistantApplicationApply() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.assistantApplicationApply");
			Map<String, String> params = new HashMap<String, String>();
			params.put("assistant_id", "7547251b5ea940ee8ee09800ea2c5f61");
			params.put("stores_id", "001209a9ce9843058843380b5bebd885");
			params.put("remark", "remark");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void specialistApplyDetailForSalesassistantFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.specialistApplyDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "7547251b5ea940ee8ee09800ea2c5f61");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void driverApplyDetailForSalesassistantFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.driverApplyDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "7547251b5ea940ee8ee09800ea2c5f61");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
		public void authApplyDetailForSalesassistantFind() {
			try {
				String url = "http://127.0.0.1:8080/ops-member/member";
				Map<String, String> requestData = new HashMap<String, String>();
				requestData.put("service", "salesman.salesassistant.authApplyDetailFind");
				Map<String, String> params = new HashMap<String, String>();
				params.put("salesman_id", "7547251b5ea940ee8ee09800ea2c5f61");
				requestData.put("params", FastJsonUtil.toJson(params));
				String result = HttpClientUtil.post(url, requestData);
				System.out.println("--------------------丁忍那一年青春的分割线-------------------");
				System.out.println(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	
	//@Test
	public void salesmanLogListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.salesmanLogListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "dae9660bc5b34a26bd636ef882c842bf");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void feedback() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.feedback");
			Map<String, String> params = new HashMap<String, String>();
			params.put("desc1", "丁忍测试搞掂APP用户反馈1111");
			params.put("contact", "14795781689");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void specialistApply() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.specialistApply");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "2d03689521de4cb1b1d7a73b4fddd9a3");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void headlines() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.messageHeadlines");
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "2");
			page.put("page_size", "2");
			requestData.put("page", FastJsonUtil.toJson(page));
			requestData.put("params", FastJsonUtil.toJson(params));	
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void salesmanRoleDelete() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.salesmanRoleDelete");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "7547251b5ea940ee8ee09800ea2c5f61");
			params.put("is_driver", "N");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void specialistApplyListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.specialistApplyListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("audit_status", "reject");
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "1");
			params.put("page", FastJsonUtil.toJson(page));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
		public void specialistApplyLogListPageFind() {
			try {
				String url = "http://127.0.0.1:8080/ops-member/member";
				Map<String, String> requestData = new HashMap<String, String>();
				requestData.put("service", "salesman.operate.specialistApplyLogListPageFind");
				Map<String, String> params = new HashMap<String, String>();
				params.put("salesman_id", "d78895b893d04358bd26adf1b5054606");
				requestData.put("params", FastJsonUtil.toJson(params));
				String result = HttpClientUtil.post(url, requestData);
				System.out.println("--------------------丁忍那一年青春的分割线-------------------");
				System.out.println(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	
	//@Test
	public void salesmanDetailForSalesassistantFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.salesmanDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "7547251b5ea940ee8ee09800ea2c5f61");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void driverApply() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.driverApply");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "7547251b5ea940ee8ee09800ea2c5f61");
			params.put("car_model", "12");
			params.put("car_number", "1243");
			params.put("capacity", "01");
			params.put("appearance_pic_path", "1111");
			params.put("driving_license_pic_path", "450802199206124351");
			params.put("driving_permit_pic_path", "1111");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void driverApplyLogListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.driverApplyLogListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "46f226ec3bf24930804d4ef0790fcf44");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void driverPass() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.handleDriverPass");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "440e0cc21b4b47e685cfa3d715517319");
			params.put("logRemark", "440e0cc21b4b47e685cfa3d715517319");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void driverReject() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.driverReject");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "440e0cc21b4b47e685cfa3d715517319");
			params.put("remark", "丁忍reject88");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void driverApplyDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.driverApplyDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "440e0cc21b4b47e685cfa3d715517319");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void driverApplyListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.driverApplyListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("audit_status", "reject");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void authApplyListLogPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.authApplyLogListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("history_id", "e1f15802c9634804bf7f8d257c68b512");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void authApply() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.authApply");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "46f226ec3bf24930804d4ef0790fcfc4");
			params.put("sex_key", "XBDM_01");
			params.put("name", "覃杨凯1");
			params.put("id_card", "123456789012345600");
			params.put("id_card_pic_path", "22222");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void authPass() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.authPass");
			Map<String, String> params = new HashMap<String, String>();
			params.put("history_id", "ff5a9eded787444fabc0ea4f5967eafe");
			params.put("event_desc", "KKK丁忍2232332");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void authReject() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.authReject");
			Map<String, String> params = new HashMap<String, String>();
			params.put("history_id", "ff5a9eded787444fabc0ea4f5967eafe");
			params.put("event_desc", "丁忍reject888888888888888888");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void authApplyDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.authApplyDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("history_id", "013926e93033525250afca6285e6e67");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void authApplyListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.authApplyListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void salesmanJurisdictionForOperate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.salesmanJurisdictionForOperate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "440e0cc21b4b47e685cfa3d715517319");
			params.put("is_operator", "S");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void salesmanDetailFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.salesmanDetailFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "4a220e0c3d5340cc8a1becf9f62fa98f");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void salesmanForSalesassistantEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.salesassistant.salesmanEdit");
			Map<String, String> params = new HashMap<String, String>();
			params.put("nick_name", "丁忍绝对牛逼888");
			//params.put("head_pic_path", "丁忍11111111111111111");
			params.put("salesman_id", "7547251b5ea940ee8ee09800ea2c5f61");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void salesmanEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.salesmanEdit");
			Map<String, String> params = new HashMap<String, String>();
			params.put("salesman_id", "01063a302b1c4e6583a196682653f885");
			params.put("nick_name", "BKB");
			params.put("remark", "14795781694");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void salesmanListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.salesmanListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			requestData.put("params", FastJsonUtil.toJson(params));
			Map<String, String> page = new HashMap<String, String>();
			page.put("page_no", "1");
			page.put("page_size", "2");
			requestData.put("page", FastJsonUtil.toJson(page));	
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//@Test
	public void salesmanLoginValidate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.salesmanLoginValidate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("user_id", "f0758e86129e489b8ed0263462953c79");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void salesmanCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "salesman.operate.salesmanCreate");
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", "丁忍");
			params.put("contact_tel", "14798888888");
			params.put("sex_key", "XBDM_01");
			params.put("area_id", "ebb41d1a-bb51-11e5-a4b3-00163e0009c6");
			params.put("nick_name", "丁忍测试");
			params.put("head_pic_path", "1111111111111111111111111111111");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println("--------------------丁忍那一年青春的分割线-------------------");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// @Test
	public void storeListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeListPageFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_id", "1231");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storeFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeListForGoodsFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("stores_id", "f92a580c10304814a6e63a4752ccd3c5");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storeDetailForConsumerFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeDetailForConsumerFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("consumer_id", "20000001");
			params.put("stores_id", "3295a61d-dece-11e5-8f52-00163e0009c6");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storeIdFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeIdFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("device_no", "3492");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void memberInfoFindByMobile() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.memberInfoFindByMobile");
			Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", "13018089936");
			params.put("member_type_key", "stores");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storeFindByMobile() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeFindByMobile");
			Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", "13700000000");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void memberRegisterRecommend() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "consumer.customer.memberRegisterRecommend");
			Map<String, String> params = new HashMap<String, String>();
			params.put("reference_id", "10036422");
			//params.put("reference_type_key", CommonConstant.MEMBER_TYPE_CONSUMER);
			params.put("member_id", "10036420");
			//params.put("member_type_key",CommonConstant.MEMBER_TYPE_CONSUMER ); 
			params.put("member_mobile","13682382056");  
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void consumerFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-mem/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.consumerFind");
			Map<String, String> params = new HashMap<String, String>();
			params.put("member_id", "10010041");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void appNotifyQuery() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.appMsgNotifyQuery");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("member_id", "11907772");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(result);
			List<String> rsp_code_list = (List<String>) resultMap.get("data");
			for (String str : rsp_code_list) {
				System.out.println(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void nearbyStoreFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.nearbyDrugstoreFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("longitude", 114.034954);
			params.put("latitude", 22.540183);
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

	@Test
	@Ignore
	public void nearbyPartnerFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.nearbyPartnerFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("longitude", 114.034954);
			params.put("latitude", 22.540183);
			params.put("business_group_type", "MDLXFZ_01");
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

	@Test
	@Ignore
	public void nearbyGrouponFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.nearbyGrouponFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("longitude", 114.034954);
			params.put("latitude", 22.540183);
			params.put("range", 800000);
			params.put("business_type_key", "MDLX_03");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void grouponTypeFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.grouponTypeFind");
			Map<String, Object> params = new HashMap<String, Object>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void nearbyLDStoreFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.nearbyLDStoreFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("area_id", "440304");
			params.put("longitude", 114.034954);
			params.put("latitude", 22.540183);
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void storeActivityPaymentEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeActivityPaymentEdit");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("activity", "reduce");
			params.put("member_id", "6986f013f2d147329c1a30d71a391b95");
			params.put("amount", 100.00);
			params.put("promotion", 10.00);
			params.put("remark", "满100减10");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void storeActivityPaymentQuery() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeActivityPaymentQuery");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("member_id", "6986f013f2d147329c1a30d71a391b95");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void storeSyncRegister() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeSyncRegister");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "29f124b7166d4437881c277136247ab2");
			params.put("stores_no", "300000002");
			params.put("stores_name", "1号加盟店");
			params.put("contact_person", "李四");
			params.put("contact_tel", "18665371521");
			params.put("stores_type_key", "HYLX_04");
			params.put("area_id", "440103");
			params.put("address", "泰然科技园204");
			params.put("sys_status", "1");
			params.put("registered_date", "2016-03-11 17:20:03");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storesUserCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storesUserCreate");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "29f124b7166d4437881c277136247ab0");
			params.put("mobile", "13018089936");
			params.put("is_admin", "Y");
			params.put("user_name", "李四");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storesUserFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storesUserFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("member_id", "3e6431e5891449489da97fe287f5027d");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storesCashierRegister() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storesCashierRegister");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "29f124b7166d4437881c277136247ab0");
			params.put("mobile", "13800000000");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storeSyncEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeSyncEdit");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "19f124b7166d4437881c277136247ab0");
			params.put("stores_no", "300000001");
			params.put("stores_name", "1号加盟店");
			params.put("contact_person", "张三");
			params.put("contact_tel", "13018089936");
			params.put("stores_type_key", "type");
			params.put("area_id", "110032");
			params.put("address", "泰然科技园204");
			params.put("sys_status", "1");
			params.put("registered_date", "2016-03-11 17:20:03");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storesCashierCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storesCashierCreate");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "123456789");
			params.put("flow_no", new Date().getTime());
			params.put("amount", "50.00");
			params.put("discount_amount", "1.00");
			params.put("pay_amount", "49.00");
			params.put("payment_way_key", "ZFFS_07");
			params.put("cashier_id", "123456");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storeSyncRegisterForHYD() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeSyncRegisterForHYD");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_no", "hyd300000009");
			params.put("stores_name", "9号加盟店");
			params.put("contact_person", "李四");
			params.put("contact_tel", "1300000003");
			params.put("stores_type_key", "HYLX_01");
			params.put("area_id", "341423");
			params.put("address", "泰然科技园204");
			params.put("registered_date", "2016-09-11 17:20:03");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void supplierSyncRegisterForHYD() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.supplierSyncRegisterForHYD3");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("area_id", "440030");
			params.put("supplier_id", "hyd300000001");
			params.put("supplier_no", "hyd300000001");
			params.put("supplier_name", "1号供应商");
			params.put("contact_person", "李四");
			params.put("contact_tel", "13018089936");
			params.put("address", "内饰阿斯蒂芬");
			params.put("registered_date", "2016-03-11 17:20:03");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void partnerSyncRegister() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.partnerSyncRegister");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("partner_id", "65b932b3-c799-11e5-a4b3-00163e0009c6");
			params.put("partner_name", "1号联盟商");
			params.put("contact", "李四");
			params.put("mobile", "130000000");
			params.put("category_key", "LMSLX_01");
			params.put("area_id", "110033");
			params.put("address", "泰然科技园204");
			params.put("registered_date", "2016-04-11 17:20:03");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storesCashierFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storesCashierFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("member_id", "123456789");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void consumerInfoUpdate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.consumerInfoUpdate");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("member_id", "10949545");
			params.put("nick_name", "好窟窿了");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void consumerAddressEdit() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.consumerAddressEdit");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("address_id", "0c84406c596a4aca8b0fe3066bb66b6e");
			params.put("consumer_id", "20000001");
			params.put("is_major_addr", "Y");
			params.put("address", "车公庙");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storeFeedback() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeFeedback");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("device_type", "iso");
			params.put("stores_name", "测试");
			params.put("contact_person", "老王");
			params.put("mobile", "18665371520");
			params.put("old_account", "13018089936");
			params.put("device_no", "1025");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storesRecommendSync() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storesRecommendSync");
			Map<String, Object> params = new HashMap<String, Object>();

			List<Map<String, Object>> paramsList = new ArrayList<Map<String, Object>>();
			Map<String, Object> temp1 = new HashMap<String, Object>();
			temp1.put("area_id", "100024");
			temp1.put("order_no", "1");
			temp1.put("stores_id", "10949545");
			paramsList.add(temp1);
			Map<String, Object> temp2 = new HashMap<String, Object>();
			temp2.put("area_id", "100024");
			temp2.put("order_no", "2");
			temp2.put("stores_id", "01637a1b58e84731bd893c4e576b5674");
			paramsList.add(temp2);
			Map<String, Object> temp3 = new HashMap<String, Object>();
			temp3.put("area_id", "100024");
			temp3.put("order_no", "3");
			temp3.put("stores_id", "1965");
			paramsList.add(temp3);
			params.put("recommend_params", FastJsonUtil.toJson(paramsList));
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void partnerTypeFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.partnerTypeFind");
			Map<String, Object> params = new HashMap<String, Object>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void nearbyExchangeFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.nearbyLDStoreFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("longitude", -122.035096);
			params.put("latitude", 37.399296);
			// params.put("longitude", 114.034954);
			// params.put("latitude", 22.540183);
			params.put("range", 300000);
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storesStatusUpdate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storesStatusUpdate");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "3295a61d-dece-11e5-8f52-00163e0009c6");
			params.put("sys_status", "disabled");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void consumerStatusUpdate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.consumerStatusUpdate");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("consumer_id", "11603129");
			params.put("status", "disabled");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storesRelConsumerList() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storesRelConsumerList");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "e68bbc95a188468ea7dcaa0b690b1bfc");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storesRelConsumerAssetList() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storesRelConsumerAssetList");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "9a54bf15109d460eabf5c7d8a7392373");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void getBaiduAddress() {
		try {
			String url = "http://api.map.baidu.com/geocoder/v2/";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("ak", "HWbsmkkkYrorNB5She5Y4yQpcU6bY920");
			requestData.put("callback", "renderReverse");
			requestData.put("location", "28.696117,115.958458");
			requestData.put("output", "json");
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void nearbyAllPropFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.nearbyAllPropFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("longitude", 114.034954);
			params.put("latitude", 22.540183);
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storeAssistantFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.storeAssistantFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("stores_id", "9654");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storeAssistantListFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "assistant.operate.storesAssistantListPageFind");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assistant_id", "9654");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void assistantServiceStoresListPageFind() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "assistant.operate.storesAssistantListPageFind");
			Map<String, Object> params = new HashMap<String, Object>();
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	//@Ignore
	public void storesAssistantCreate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "assistant.operate.storesAssistantCreate");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assistant_id", "9654");
			params.put("assistant_name", "test");
			params.put("contact_tel", "9654");
			params.put("status", "normal");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void storesAssistantUpdate() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "assistant.operate.storesAssistantUpdate");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("assistant_id", "9654");
			params.put("assistant_name", "test11");
			params.put("contact_tel", "13597845126");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void favoriteStore() {
		try {
			String url = "http://127.0.0.1:8080/ops-member/member";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("service", "member.favoriteStore");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("is_llm_stores", "Y");
			params.put("consumer_id", "10494374");
			params.put("stores_id", "021b4ba1c5aa45f69bd71f3944266860");
			requestData.put("params", FastJsonUtil.toJson(params));
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
