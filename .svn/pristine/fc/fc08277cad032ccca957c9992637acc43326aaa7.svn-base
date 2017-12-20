package com.meitianhui.goods.service.impl;

import java.util.ArrayList;
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
import com.meitianhui.goods.dao.GdBenefitDao;
import com.meitianhui.goods.dao.GoodsDao;
import com.meitianhui.goods.dao.PsGoodsDao;
import com.meitianhui.goods.entity.GdActivity;
import com.meitianhui.goods.entity.GdActivityDelivery;
import com.meitianhui.goods.entity.PsGoods;
import com.meitianhui.goods.service.GdActivityService;

/**
 * 会员权益服务层
 * 
 * @ClassName: GdActivityServiceImpl
 * @Description:
 * @author tiny
 * @date 2017年2月20日 下午3:55:47
 *
 */
@SuppressWarnings("unchecked")
@Service
public class GdActivityServiceImpl implements GdActivityService {

	@Autowired
	public GdActivityDao gdActivityDao;
	@Autowired
	public GdActivityDeliveryDao gdActivityDeliveryDao;
	@Autowired
	private DocUtil docUtil;
	@Autowired
	public PsGoodsDao psGoodsDao;
	@Autowired
	public GoodsDao goodsDao;
	@Autowired
	public GdBenefitDao gdBenefitDao;

	@Override
	public void gdActivityCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "title", "goods_id", "expired_date", "limited_point", "limited_grade" });

		// 判断活动商品是否存在
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("goods_id", paramsMap.get("goods_id"));
		tempMap.put("category", "奖品");
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(tempMap);
		if (psGoodsList.size() == 0) {
			throw new BusinessException(RspCode.PS_GOODS_ERROR, "商品不存在");
		}
		PsGoods psGoods = psGoodsList.get(0);
		Date created_date = new Date();
		GdActivity gdActivity = new GdActivity();
		BeanConvertUtil.mapToBean(gdActivity, paramsMap);
		gdActivity.setActivity_id(IDUtil.getUUID());
		gdActivity.setGoods_title(psGoods.getTitle());
		gdActivity.setGoods_code(psGoods.getGoods_code());
		gdActivity.setJson_data(psGoods.getPic_info());
		gdActivity.setStatus(Constant.STATUS_NORMAL);
		gdActivity.setCreated_date(created_date);
		gdActivity.setModified_date(created_date);
		gdActivityDao.insertGdActivity(gdActivity);
	}

	/**
	 * 会员权益列表查询
	 * 
	 * @Title: gdActivityListForOpFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void gdActivityListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String status = StringUtil.formatStr(paramsMap.get("status"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		List<Map<String, Object>> GdActivityList = gdActivityDao.selectGdActivityListForOp(paramsMap);
		for (Map<String, Object> map : GdActivityList) {
			map.put("created_date", DateUtil.date2Str((Date) map.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
			map.put("expired_date", DateUtil.date2Str((Date) map.get("expired_date"), DateUtil.fmt_yyyyMMddHHmmss));
			map.put("limited_point", StringUtil.formatStr(map.get("limited_point")));
			map.put("limited_grade", StringUtil.formatStr(map.get("limited_grade")));
			map.put("limited_benefit", StringUtil.formatStr(map.get("limited_benefit")));
			map.put("remark", StringUtil.formatStr(map.get("remark")));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", GdActivityList);
		result.setResultData(map);
	}

	/**
	 * 权益活动列表查询(消费者)
	 * 
	 * @Title: GdActivityListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void gdActivityListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> GdActivityList = gdActivityDao.selectGdActivityListForConsumer(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : GdActivityList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", map.get("activity_id"));
			tempMap.put("title", map.get("title"));
			tempMap.put("limited_point", StringUtil.formatStr(map.get("limited_point")));
			tempMap.put("limited_grade", StringUtil.formatStr(map.get("limited_grade")));
			tempMap.put("limited_benefit", StringUtil.formatStr(map.get("limited_benefit")));
			tempMap.put("goods_id", map.get("goods_id"));
			tempMap.put("goods_title", map.get("goods_title"));
			tempMap.put("json_data", StringUtil.formatStr(map.get("json_data")));
			tempMap.put("sale_qty", map.get("sale_qty") + "");
			tempMap.put("stock_qty", map.get("stock_qty") + "");
			tempMap.put("discount_price", map.get("discount_price") + "");
			tempMap.put("market_price", map.get("market_price") + "");
			tempMap.put("good_level", map.get("good_level") + "");
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
	 * 权益活动删除
	 * 
	 * @Title: GdActivityListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void gdActivityCancel(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
		paramsMap.put("status", Constant.STATUS_CANCELLED);
		gdActivityDao.updateGdActivity(paramsMap);
	}

	/**
	 * 领取权益活动商品
	 * 
	 * @Title: GdActivityListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void handleGdActivityGet(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "goods_id", "member_type_key", "member_id",
				"member_grade", "member_mobile", "benefit_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("benefit_id", paramsMap.get("benefit_id"));
		tempMap.put("status", Constant.STATUS_NORMAL);
		Map<String, Object> gdBenefitMap = gdBenefitDao.selectGdBenefit(tempMap);
		if (null == gdBenefitMap) {
			throw new BusinessException(RspCode.GD_BENEFIT_ERROR, "权益券已失效");
		}
		paramsMap.put("amount", gdBenefitMap.get("amount"));

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

		tempMap.clear();
		tempMap.put("activity_id", paramsMap.get("activity_id"));
		tempMap.put("status", Constant.STATUS_NORMAL);
		// 检测会员等级和活动等级限制是否一直
		Map<String, Object> gdActivityMap = gdActivityDao.selectGdActivity(tempMap);
		if (null == gdActivityMap) {
			throw new BusinessException(RspCode.GD_ACTIVITY_ERROR, "权益活动不存在");
		}
		// 活动限制等级
		Integer limited_grade = (Integer) gdActivityMap.get("limited_grade");
		// 会员等级
		Integer member_grade = Integer.parseInt(paramsMap.get("member_grade") + "");
		if (member_grade < limited_grade) {
			throw new BusinessException(RspCode.GD_BENEFIT_ERROR, "您的权益等级不足,无法领取此活动商品");
		}

		// 更新会员权益信息
		tempMap.clear();
		tempMap.put("benefit_id", paramsMap.get("benefit_id"));
		tempMap.put("modified_date",
				DateUtil.date2Str((Date) gdBenefitMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
		tempMap.put("status", Constant.STATUS_USED);
		int updateFlag = gdBenefitDao.updateGdBenefit(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, "操作失败,请刷新后重试");
		}
		Date created_date = new Date();
		GdActivityDelivery gdActivityDelivery = new GdActivityDelivery();
		BeanConvertUtil.mapToBean(gdActivityDelivery, paramsMap);
		gdActivityDelivery.setDelivery_id(IDUtil.getUUID());
		gdActivityDelivery.setStatus(Constant.STATUS_UNDELIVERED);
		gdActivityDelivery.setCreated_date(created_date);
		gdActivityDelivery.setModified_date(created_date);
		gdActivityDelivery.setOrder_no(OrderIDUtil.getOrderNo());
		gdActivityDeliveryDao.insertGdActivityDelivery(gdActivityDelivery);

		tempMap.clear();
		tempMap.put("goods_id", paramsMap.get("goods_id"));
		tempMap.put("sell_qty", "1");
		updateFlag = 0;
		updateFlag = psGoodsDao.updatePsGoodsSaleQtyDeduction(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, "可售库存不足");
		}
		// 记录日志
		tempMap.clear();
		tempMap.put("log_id", IDUtil.getUUID());
		tempMap.put("member_id", paramsMap.get("member_id"));
		tempMap.put("benefit_id", paramsMap.get("benefit_id"));
		tempMap.put("category", "消费");
		tempMap.put("tracked_date", created_date);
		tempMap.put("event", "使用免单券领取了" + gdActivityMap.get("goods_title"));
		gdBenefitDao.insertGdBenefitLog(tempMap);
	}

	@Override
	public void gdActivityDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id", "activity_id" });
		if (paramsMap.size() == 0) {
			throw new BusinessException(RspCode.SYSTEM_PARAM_MISS, "参数缺失");
		}
		List<Map<String, Object>> psGoodsList = gdActivityDao.selectGdActivityDetail(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if(psGoodsList.size() > 0){
			
			Map<String, Object> tempMap = new HashMap<String, Object>();
			
			Map<String, Object> psGoods = psGoodsList.get(0);
			tempMap.put("activity_id", psGoods.get("activity_id"));
			tempMap.put("limited_point", StringUtil.formatStr(psGoods.get("limited_point")));
			tempMap.put("goods_id", psGoods.get("goods_id"));
			tempMap.put("goods_code", StringUtil.formatStr(psGoods.get("goods_code")));
			tempMap.put("title",  StringUtil.formatStr(psGoods.get("title")));
			tempMap.put("goods_title",  StringUtil.formatStr(psGoods.get("goods_title")));
			tempMap.put("desc1",  StringUtil.formatStr(psGoods.get("desc1")));
			tempMap.put("area_id",  StringUtil.formatStr(psGoods.get("area_id")));
			tempMap.put("label",  StringUtil.formatStr(psGoods.get("label")));
			tempMap.put("label_promotion",  StringUtil.formatStr(psGoods.get("label_promotion")));
			tempMap.put("category",  StringUtil.formatStr(psGoods.get("category")));
			tempMap.put("data_source",  StringUtil.formatStr(psGoods.get("data_source")));
			tempMap.put("display_area",  StringUtil.formatStr(psGoods.get("display_area")));
			tempMap.put("contact_person",  StringUtil.formatStr(psGoods.get("contact_person")));
			tempMap.put("contact_tel",  StringUtil.formatStr(psGoods.get("contact_tel")));
			tempMap.put("specification",  StringUtil.formatStr(psGoods.get("specification")));
			tempMap.put("agent",  StringUtil.formatStr(psGoods.get("agent")));
			tempMap.put("pack",  StringUtil.formatStr(psGoods.get("pack")));
			tempMap.put("cost_price",  StringUtil.formatStr(psGoods.get("cost_price")));
			tempMap.put("market_price",  StringUtil.formatStr(psGoods.get("market_price")));
			tempMap.put("discount_price",  StringUtil.formatStr(psGoods.get("discount_price")));
			tempMap.put("ladder_price",  StringUtil.formatStr(psGoods.get("ladder_price")));
			tempMap.put("product_source",  StringUtil.formatStr(psGoods.get("product_source")));
			tempMap.put("shipping_fee",  StringUtil.formatStr(psGoods.get("shipping_fee")));
			tempMap.put("cost_allocation",  StringUtil.formatStr(psGoods.get("cost_allocation")));
			tempMap.put("settled_price",  StringUtil.formatStr(psGoods.get("settled_price")));
			tempMap.put("ts_min_num",  StringUtil.formatStr(psGoods.get("ts_min_num")));
			tempMap.put("ts_price",  StringUtil.formatStr(psGoods.get("ts_price")));
			tempMap.put("ts_date",  StringUtil.formatStr(psGoods.get("ts_date")));
			tempMap.put("service_fee",  StringUtil.formatStr(psGoods.get("service_fee")));
			tempMap.put("producer",  StringUtil.formatStr(psGoods.get("producer")));
			tempMap.put("supplier",  StringUtil.formatStr(psGoods.get("supplier")));
			tempMap.put("supplier_id",  StringUtil.formatStr(psGoods.get("supplier_id")));
			tempMap.put("warehouse",  StringUtil.formatStr(psGoods.get("warehouse")));
			tempMap.put("warehouse_id",  StringUtil.formatStr(psGoods.get("warehouse_id")));
			tempMap.put("manufacturer",  StringUtil.formatStr(psGoods.get("manufacturer")));
			tempMap.put("min_buy_qty",  StringUtil.formatStr(psGoods.get("min_buy_qty")));
			tempMap.put("max_buy_qty",  StringUtil.formatStr(psGoods.get("max_buy_qty")));
			tempMap.put("goods_unit",  StringUtil.formatStr(psGoods.get("goods_unit")));
			tempMap.put("delivery",  StringUtil.formatStr(psGoods.get("delivery")));
			tempMap.put("delivery_id",  StringUtil.formatStr(psGoods.get("delivery_id")));
			tempMap.put("valid_thru",  StringUtil.formatStr(psGoods.get("valid_thru")));
			tempMap.put("payment_way",  StringUtil.formatStr(psGoods.get("payment_way")));
			tempMap.put("status",  StringUtil.formatStr(psGoods.get("status")));
			tempMap.put("remark",  StringUtil.formatStr(psGoods.get("remark")));
			tempMap.put("pic_info",  StringUtil.formatStr(psGoods.get("pic_info")));
			tempMap.put("pic_detail_info",  StringUtil.formatStr(psGoods.get("pic_detail_info")));
			tempMap.put("online_date", DateUtil.date2Str((Date) psGoods.get("online_date"), 
					DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("sale_qty",  StringUtil.formatStr(psGoods.get("sale_qty")));
			tempMap.put("stock_qty",  StringUtil.formatStr(psGoods.get("stock_qty")));

			// 解析区域  
			String[] areaStr = psGoods.get("delivery_area").toString().split(",");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("areaStr", areaStr);
			List<Map<String, Object>> areaList = goodsDao.selectMDArea(params);
			String delivery_desc = "";
			for (Map<String, Object> area : areaList) {
				delivery_desc = delivery_desc + "|" + area.get("path");
			}
			if (delivery_desc.trim().length() > 0) {
				delivery_desc = delivery_desc.substring(1);
			}
			tempMap.put("delivery_area", StringUtil.formatStr(psGoods.get("delivery_area")));
			tempMap.put("delivery_desc", delivery_desc);
			resultList.add(tempMap);
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.get("pic_info"));
			if (!pic_info.equals("")) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
			// 解析详情图片
			String pic_detail_info = StringUtil.formatStr(psGoods.get("pic_detail_info"));
			if (!pic_detail_info.equals("")) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_detail_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void gdActivityCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> GdActivityList = gdActivityDao.selectGdActivityListForConsumer(paramsMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count_num", GdActivityList.size()+"");
		result.setResultData(map);
	}
}
