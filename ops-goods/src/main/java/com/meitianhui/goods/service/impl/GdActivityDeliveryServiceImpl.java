package com.meitianhui.goods.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.OrderIDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.goods.constant.Constant;
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.dao.GdActivityDao;
import com.meitianhui.goods.dao.GdActivityDeliveryDao;
import com.meitianhui.goods.dao.PsGoodsDao;
import com.meitianhui.goods.entity.GdActivityDelivery;
import com.meitianhui.goods.service.GdActivityDeliveryService;

/**
 * 权益活动配送信息服务层
 * 
 * @ClassName: GdActivityDeliveryServiceImpl
 * @Description:
 * @author tiny
 * @date 2017年2月20日 下午3:55:47
 *
 */
@SuppressWarnings("unchecked")
@Service
public class GdActivityDeliveryServiceImpl implements GdActivityDeliveryService {

	@Autowired
	public GdActivityDeliveryDao gdActivityDeliveryDao;

	@Autowired
	public GdActivityDao gdActivityDao;
	
	@Autowired
	public PsGoodsDao psGoodsDao;
	
	@Autowired
	private DocUtil docUtil;
	/**
	 * 权益活动配送信息列表查询
	 * 
	 * @Title: GdActivityDeliveryListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void gdActivityDeliveryListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String status = StringUtil.formatStr(paramsMap.get("status"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		List<Map<String, Object>> GdActivityDeliveryList = gdActivityDeliveryDao
				.selectGdActivityDeliveryList(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : GdActivityDeliveryList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("delivery_id", map.get("delivery_id"));
			tempMap.put("benefit_type", map.get("benefit_type"));
			tempMap.put("amount", map.get("amount"));
			tempMap.put("member_mobile", map.get("member_mobile"));
			tempMap.put("contact_tel", map.get("contact_tel"));
			tempMap.put("contact_person", map.get("contact_person"));
			tempMap.put("logistics", StringUtil.formatStr(map.get("logistics")));
			tempMap.put("delivery_address", StringUtil.formatStr(map.get("delivery_address")));
			tempMap.put("status", map.get("status"));
			tempMap.put("created_date", DateUtil.date2Str((Date) map.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("title", map.get("title"));
			tempMap.put("goods_title", map.get("goods_title"));
			tempMap.put("order_no", map.get("order_no"));
			tempMap.put("limited_point", StringUtil.formatStr(map.get("limited_point")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	/**
	 * 权益活动配送信息列表查询(消费者)
	 * 
	 * @Title: GdActivityDeliveryListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void gdActivityDeliveryListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
		List<Map<String, Object>> GdActivityDeliveryList = gdActivityDeliveryDao
				.selectGdActivityDeliveryList(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : GdActivityDeliveryList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("delivery_id", map.get("delivery_id"));
			tempMap.put("benefit_type", map.get("benefit_type"));
			tempMap.put("amount", map.get("amount"));
			tempMap.put("benefit_type", map.get("benefit_type"));
			tempMap.put("contact_tel", map.get("contact_tel"));
			tempMap.put("contact_person", map.get("contact_person"));
			tempMap.put("logistics", StringUtil.formatStr(map.get("logistics")));
			tempMap.put("delivery_address", StringUtil.formatStr(map.get("delivery_address")));
			tempMap.put("status", map.get("status"));
			tempMap.put("created_date", DateUtil.date2Str((Date) map.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("title", map.get("title"));
			tempMap.put("goods_title", map.get("goods_title"));
			tempMap.put("order_no", map.get("order_no"));
			tempMap.put("json_data", StringUtil.formatStr(map.get("json_data")));
			String json_data = StringUtil.formatStr(map.get("json_data"));
			if (StringUtils.isNotEmpty(json_data)) {
				List<Map<String, Object>> jsonList = FastJsonUtil.jsonToList(json_data);
				for (Map<String, Object> json : jsonList) {
					if (!StringUtil.formatStr(json.get("path_id")).equals("")) {
						doc_ids.add(json.get("path_id") + "");
					}
				}
			}
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	/**
	 * 权益活动配送(发货)
	 * 
	 * @Title: handleGdActivityDeliver
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void handleGdActivityDeliver(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "delivery_id", "logistics" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("delivery_id", paramsMap.get("delivery_id"));
		tempMap.put("status", Constant.STATUS_UNDELIVERED);
		Map<String, Object> gdActivityDeliveryMap = gdActivityDeliveryDao.selectGdActivityDelivery(paramsMap);
		if (null == gdActivityDeliveryMap) {
			throw new BusinessException(RspCode.GD_ACTIVITY_ERROR, "待配送的活动商品不存在");
		}
		paramsMap.put("modified_date",
				DateUtil.date2Str((Date) gdActivityDeliveryMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
		paramsMap.put("status", Constant.STATUS_DELIVERED);
		int updateFlag = gdActivityDeliveryDao.updateGdActivityDelivery(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
		}
	}

	@Override
	public void gdActivityDeliveryCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "goods_id", "activity_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		
		//加多一个限制
		tempMap.clear();
		tempMap.put("activity_id", paramsMap.get("activity_id"));
		tempMap.put("status", Constant.STATUS_NORMAL);
		// 检测活动是否存在
		Map<String, Object> gdActivityMap = gdActivityDao.selectGdActivity(tempMap);
		if (null == gdActivityMap) {
			throw new BusinessException(RspCode.GD_ACTIVITY_ERROR, "权益活动不存在");
		}
		
		String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "member.consumerFind");
		bizParams.put("member_id", paramsMap.get("member_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> memberMap = (Map<String, Object>) resultMap.get("data");
		
		// 活动限制等级
		Integer limited_grade = (Integer) gdActivityMap.get("limited_grade");
		// 会员等级
		Integer member_grade = Integer.parseInt(memberMap.get("grade") + "");
		if (member_grade < limited_grade) {
			throw new BusinessException(RspCode.GD_BENEFIT_ERROR, "您的权益等级不足,无法领取此活动商品");
		}
		
		//兑换限制=积分兑换每月2次限制规则：针对的是人，每个人每月只能兑换两次，不论什么商品，不论什么活动。
		tempMap.clear();
		tempMap.put("member_id", paramsMap.get("member_id"));

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar cal=Calendar.getInstance();//获取当前日期   
        cal.add(Calendar.MONTH, 0);  
        cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天   
        String firstDay = format.format(cal.getTime()); 
    	tempMap.put("created_date_strat",firstDay);
    	
    	cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
    	String lastDay = format.format(cal.getTime());  
		tempMap.put("created_date_end", lastDay);
		
		tempMap.put("status", "undelivered,delivered");
		String status = StringUtil.formatStr(tempMap.get("status"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				tempMap.remove("status");
				tempMap.put("status_in", list);
			}
		}
		List<Map<String, Object>> GdActivityDeliveryList = gdActivityDeliveryDao.selectGdActivityDeliveryList(tempMap);
		
		if(GdActivityDeliveryList.size() >= 2){
			throw new BusinessException(RspCode.GD_BENEFIT_ERROR, "您每月兑换次数超过两次,无法兑换此活动商品");
		}
		
		tempMap.clear();
		tempMap.put("status", "true");
		result.setResultData(tempMap);
	}


	@Override
	public void handleGdActivityDeliveryCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		
		ValidateUtil.validateParams(paramsMap,
				new String[] { "member_id", "activity_id", "member_type_key", "member_mobile", "goods_id", "point"});
			
		Map<String, Object> tempMap = new HashMap<String, Object>();
		
		// 获取消费者的收货地址信息
		String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "member.consumerAddressFind");
		bizParams.put("consumer_id", paramsMap.get("member_id"));
		bizParams.put("is_major_addr", "Y");
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		List<Map<String, Object>> addressList = (List<Map<String, Object>>) resultMap.get("data");
		if (addressList.size() == 0) {
			throw new BusinessException(RspCode.GD_BENEFIT_ERROR, "您未设置收货地址");
		}
		Map<String, Object> addressMap = addressList.get(0);
		paramsMap.put("delivery_area_id", addressMap.get("area_id"));
		String area_desc = addressMap.get("area_desc") + "";
		String address = addressMap.get("address") + "";
		paramsMap.put("delivery_address", area_desc.replace("中国", "").replace(",", "") + address);
		paramsMap.put("contact_person", addressMap.get("consignee"));
		paramsMap.put("contact_tel", addressMap.get("mobile"));
		
		//创建活动订单
		Date created_date = new Date();
		GdActivityDelivery gdActivityDelivery = new GdActivityDelivery();
		BeanConvertUtil.mapToBean(gdActivityDelivery, paramsMap);
		gdActivityDelivery.setActivity_id(paramsMap.get("activity_id").toString());
		gdActivityDelivery.setMember_type_key(paramsMap.get("member_type_key").toString());
		gdActivityDelivery.setMember_mobile(paramsMap.get("member_mobile").toString());
		gdActivityDelivery.setAmount(new BigDecimal(paramsMap.get("point").toString()));
		gdActivityDelivery.setDelivery_id(IDUtil.getUUID());
		gdActivityDelivery.setStatus(Constant.STATUS_UNDELIVERED);
		gdActivityDelivery.setCreated_date(created_date);
		gdActivityDelivery.setModified_date(created_date);
		gdActivityDelivery.setOrder_no(OrderIDUtil.getOrderNo());
		gdActivityDeliveryDao.insertGdActivityDelivery(gdActivityDelivery);
		
		//扣库存
		tempMap.clear();
		tempMap.put("goods_id", paramsMap.get("goods_id"));
		tempMap.put("sell_qty", "1");
		int updateFlag = 0;
		updateFlag = psGoodsDao.updatePsGoodsSaleQtyDeduction(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, "可售库存不足");
		}
		
		//把订单号码传出去
		tempMap.clear();
		tempMap.put("order_no", gdActivityDelivery.getOrder_no());
		result.setResultData(tempMap);
	}

}
