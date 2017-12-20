package com.meitianhui.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.meitianhui.common.util.MoneyUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.order.constant.Constant;
import com.meitianhui.order.constant.OrderIDUtil;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.controller.OrderController;
import com.meitianhui.order.dao.FgBlacklistDao;
import com.meitianhui.order.dao.TsActivityDao;
import com.meitianhui.order.dao.TsActivitySettlementDao;
import com.meitianhui.order.dao.TsOrderDao;
import com.meitianhui.order.entity.TsActivity;
import com.meitianhui.order.entity.TsActivitySettlement;
import com.meitianhui.order.entity.TsOrder;
import com.meitianhui.order.service.OrderService;
import com.meitianhui.order.service.PsOrderService;
import com.meitianhui.order.service.TsActivityService;

/**
 * 伙拼团活动
 * 
 * @ClassName: TsActivityServiceImpl
 * @author tiny
 * @date 2017年2月27日 下午7:06:06
 *
 */
@SuppressWarnings("unchecked")
@Service
public class TsActivityServiceImpl implements TsActivityService {

	private static final Logger logger = Logger.getLogger(TsActivityServiceImpl.class);

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private DocUtil docUtil;

	@Autowired
	private TsActivityDao tsActivityDao;
	@Autowired
	private TsOrderDao tsOrderDao;
	@Autowired
	private FgBlacklistDao fgBlacklistDao;
	@Autowired
	private TsActivitySettlementDao tsActivitySettlementDao;

	@Autowired
	private OrderService orderService;
	@Autowired
	private PsOrderService psOrderService;

	@Override
	public void ladderTsActivityCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "goods_id", "stores_id",
				"member_mobile", "activity_type", "received_mode", "payment_way_key", "amount" });
		BigDecimal amount = new BigDecimal(paramsMap.get("amount") + "");
		paramsMap.put("member_info", paramsMap.get("member_mobile"));
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		// 获得商品信息
		reqParams.put("service", "goods.psGoodsFindForTsActivity");
		bizParams.put("goods_id", paramsMap.get("goods_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> psGoodsMap = (Map<String, Object>) resultMap.get("data");
		// 只取第一张图片
		String pic_info = StringUtil.formatStr(psGoodsMap.get("pic_info"));
		if (StringUtils.isNotEmpty(pic_info)) {
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
			if (tempList.size() > 0) {
				Map<String, Object> pic = tempList.get(0);
				pic_info = FastJsonUtil.toJson(pic);
			}
		}
		paramsMap.put("goods_json_data", pic_info);

		paramsMap.put("title", psGoodsMap.get("title"));
		paramsMap.put("goods_code", psGoodsMap.get("goods_code"));
		paramsMap.put("supplier_id", psGoodsMap.get("supplier_id"));
		paramsMap.put("supplier", psGoodsMap.get("supplier"));
		paramsMap.put("manufacturer", StringUtil.formatStr(psGoodsMap.get("manufacturer")));
		paramsMap.put("discount_price", psGoodsMap.get("discount_price"));
		// 阶梯价活动中,未成团直接,当前售价为优惠价,成团后,当前售价等于阶梯价
		paramsMap.put("sale_price", psGoodsMap.get("discount_price"));
		paramsMap.put("cost_allocation", "0.00");
		paramsMap.put("ladder_price", psGoodsMap.get("ts_price"));
		paramsMap.put("settled_price", psGoodsMap.get("settled_price"));
		paramsMap.put("market_price", psGoodsMap.get("market_price"));
		paramsMap.put("min_num", psGoodsMap.get("ts_min_num"));

		Integer ts_date = Integer.parseInt(psGoodsMap.get("ts_date") + "");
		String expiried_date = DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss);
		expiried_date = DateUtil.addDate(expiried_date, DateUtil.fmt_yyyyMMddHHmmss, 4, ts_date);
		paramsMap.put("expiried_date", expiried_date);

		BigDecimal discount_price = new BigDecimal(psGoodsMap.get("discount_price") + "");

		if (amount.compareTo(discount_price) != 0) {
			throw new BusinessException(RspCode.ORDER_ERROE, "订单金额被篡改");
		}

		// 获取门店信息
		reqParams.clear();
		bizParams.clear();
		resultMap.clear();
		reqParams.put("service", "stores.stores.storesBaseInfoFind");
		bizParams.put("stores_id", paramsMap.get("stores_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		resultStr = HttpClientUtil.postShort(OrderServiceImpl.MEMBER_SERVICE_URL, reqParams);
		resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> storesMap = (Map<String, Object>) resultMap.get("data");
		paramsMap.put("stores_longitude", storesMap.get("longitude"));
		paramsMap.put("stores_latitude", storesMap.get("latitude"));
		// 门店信息
		Map<String, String> storesInfo = new HashMap<String, String>();
		storesInfo.put("stores_name", storesMap.get("stores_name") + "");
		storesInfo.put("neighbor_pic_path", storesMap.get("neighbor_pic_path") + "");
		paramsMap.put("stores_info", FastJsonUtil.toJson(storesInfo));

		// 收货地址信息
		Map<String, String> consigneeJsonData = new HashMap<String, String>();
		consigneeJsonData.put("contact_person", storesMap.get("contact_person") + "");
		consigneeJsonData.put("contact_tel", storesMap.get("contact_tel") + "");
		String area_desc = storesMap.get("area_desc") + "";
		String address = storesMap.get("address") + "";
		consigneeJsonData.put("address", area_desc.replace("中国", "").replace(",", "") + address);
		paramsMap.put("consignee_json_data", FastJsonUtil.toJson(consigneeJsonData));

		// 创建活动
		Date date = new Date();
		TsActivity tsActivity = new TsActivity();
		BeanConvertUtil.mapToBean(tsActivity, paramsMap);
		tsActivity.setActivity_id(IDUtil.getUUID());
		tsActivity.setActivity_no(OrderIDUtil.getOrderNo());
		tsActivity.setCreated_date(date);
		tsActivity.setModified_date(date);
		tsActivity.setStatus(Constant.ORDER_ACTIVING);
		tsActivity.setOrder_qty(1);
		tsActivityDao.insertTsActivity(tsActivity);
		// 创建第一笔订单
		// 获取消费者默认收货地址
		reqParams.clear();
		bizParams.clear();
		resultMap.clear();
		reqParams.put("service", "member.consumerAddressFind");
		bizParams.put("consumer_id", paramsMap.get("member_id"));
		bizParams.put("is_major_addr", "Y");
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		resultStr = HttpClientUtil.postShort(OrderServiceImpl.MEMBER_SERVICE_URL, reqParams);
		resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		List<Map<String, Object>> addressList = (List<Map<String, Object>>) resultMap.get("data");
		if (addressList.size() == 0) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "会员没有默认地址");
		}
		Map<String, Object> addMap = addressList.get(0);
		// 收货地址信息
		consigneeJsonData.clear();
		consigneeJsonData.put("contact_person", addMap.get("consignee") + "");
		consigneeJsonData.put("contact_tel", addMap.get("mobile") + "");
		area_desc = addMap.get("area_desc") + "";
		address = addMap.get("address") + "";
		consigneeJsonData.put("address", area_desc.replace("中国", "").replace(",", "") + address);

		// 消费者头像
		reqParams.clear();
		bizParams.clear();
		resultMap.clear();
		reqParams.put("service", "consumer.consumer.consumerBaseInfoFind");
		bizParams.put("consumer_id", paramsMap.get("member_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		resultStr = HttpClientUtil.postShort(OrderServiceImpl.MEMBER_SERVICE_URL, reqParams);
		resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> consuemrMap = (Map<String, Object>) resultMap.get("data");
		if (null == consuemrMap) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "会员没有默认地址");
		}
		// 消费者头像
		consigneeJsonData.put("head_pic_path", consuemrMap.get("head_pic_path") + "");
		// 创建订单
		TsOrder order = new TsOrder();
		order.setOrder_id(IDUtil.getUUID());
		order.setOrder_no(OrderIDUtil.getOrderNo());
		order.setActivity_id(tsActivity.getActivity_id());
		order.setOrder_date(date);
		order.setLoaded_code(IDUtil.random(4));
		order.setDesc1(tsActivity.getTitle());
		order.setItem_num(1);
		order.setSale_price(new BigDecimal("0.00"));
		order.setTotal_fee(new BigDecimal("0.00"));
		order.setDeposit_fee(tsActivity.getSale_price());
		order.setPayment_way_key(paramsMap.get("payment_way_key") + "");
		order.setMember_id(tsActivity.getMember_id());
		order.setMember_type_key(Constant.MEMBER_TYPE_CONSUMER);
		order.setMember_info(tsActivity.getMember_info());
		order.setConsignee_json_data(FastJsonUtil.toJson(consigneeJsonData));
		order.setSettle_status(Constant.ORDER_SETTLE_PENDING);
		order.setStatus(Constant.ORDER_PAID);
		order.setCreated_date(date);
		order.setModified_date(date);
		tsOrderDao.insertTsOrder(order);
		resultMap.clear();
		resultMap.put("order_no", order.getOrder_no());
		result.setResultData(resultMap);
	}

	@Override
	public void ladderTsActivityForConsumerCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "goods_id", "stores_id",
				"member_mobile", "activity_type", "received_mode" });
		paramsMap.put("member_info", paramsMap.get("member_mobile"));
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		// 获得商品信息
		reqParams.put("service", "goods.psGoodsFindForTsActivity");
		bizParams.put("goods_id", paramsMap.get("goods_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> psGoodsMap = (Map<String, Object>) resultMap.get("data");

		// 只取第一张图片
		String pic_info = StringUtil.formatStr(psGoodsMap.get("pic_info"));
		if (StringUtils.isNotEmpty(pic_info)) {
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
			if (tempList.size() > 0) {
				Map<String, Object> pic = tempList.get(0);
				pic_info = FastJsonUtil.toJson(pic);
			}
		}
		paramsMap.put("goods_json_data", pic_info);

		paramsMap.put("title", psGoodsMap.get("title"));
		paramsMap.put("goods_code", psGoodsMap.get("goods_code"));
		paramsMap.put("supplier_id", psGoodsMap.get("supplier_id"));
		paramsMap.put("supplier", psGoodsMap.get("supplier"));
		paramsMap.put("manufacturer", StringUtil.formatStr(psGoodsMap.get("manufacturer")));
		paramsMap.put("discount_price", psGoodsMap.get("discount_price"));
		// 阶梯价活动中,未成团直接,当前售价为优惠价,成团后,当前售价等于阶梯价
		paramsMap.put("sale_price", psGoodsMap.get("discount_price"));
		paramsMap.put("cost_allocation", "0.00");
		paramsMap.put("ladder_price", psGoodsMap.get("ts_price"));
		paramsMap.put("settled_price", psGoodsMap.get("settled_price"));
		paramsMap.put("market_price", psGoodsMap.get("market_price"));
		paramsMap.put("min_num", psGoodsMap.get("ts_min_num"));

		Integer ts_date = Integer.parseInt(psGoodsMap.get("ts_date") + "");
		String expiried_date = DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss);
		expiried_date = DateUtil.addDate(expiried_date, DateUtil.fmt_yyyyMMddHHmmss, 4, ts_date);
		paramsMap.put("expiried_date", expiried_date);

		// 获取门店信息
		reqParams.clear();
		bizParams.clear();
		resultMap.clear();
		reqParams.put("service", "stores.stores.storesBaseInfoFind");
		bizParams.put("stores_id", paramsMap.get("stores_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		resultStr = HttpClientUtil.postShort(OrderServiceImpl.MEMBER_SERVICE_URL, reqParams);
		resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> storesMap = (Map<String, Object>) resultMap.get("data");
		paramsMap.put("stores_longitude", storesMap.get("longitude"));
		paramsMap.put("stores_latitude", storesMap.get("latitude"));
		// 门店信息
		Map<String, String> storesInfo = new HashMap<String, String>();
		storesInfo.put("stores_name", storesMap.get("stores_name") + "");
		storesInfo.put("neighbor_pic_path", storesMap.get("neighbor_pic_path") + "");
		paramsMap.put("stores_info", FastJsonUtil.toJson(storesInfo));

		// 收货地址信息
		Map<String, String> consigneeJsonData = new HashMap<String, String>();
		consigneeJsonData.put("contact_person", storesMap.get("contact_person") + "");
		consigneeJsonData.put("contact_tel", storesMap.get("contact_tel") + "");
		String area_desc = storesMap.get("area_desc") + "";
		String address = storesMap.get("address") + "";
		consigneeJsonData.put("address", area_desc.replace("中国", "").replace(",", "") + address);
		paramsMap.put("consignee_json_data", FastJsonUtil.toJson(consigneeJsonData));

		// 创建活动
		Date date = new Date();
		TsActivity tsActivity = new TsActivity();
		BeanConvertUtil.mapToBean(tsActivity, paramsMap);
		tsActivity.setActivity_id(IDUtil.getUUID());
		tsActivity.setActivity_no(OrderIDUtil.getOrderNo());
		tsActivity.setCreated_date(date);
		tsActivity.setModified_date(date);
		tsActivity.setStatus(Constant.ORDER_ACTIVING);
		tsActivity.setOrder_qty(0);
		tsActivityDao.insertTsActivity(tsActivity);
	}

	@Override
	public void tsActivityListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String status = StringUtil.formatStr(paramsMap.get("status"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> qryList = tsActivityDao.selectTsActivityListForOp(paramsMap);
		for (Map<String, Object> tMap : qryList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", tMap.get("activity_id") + "");
			tempMap.put("activity_no", tMap.get("activity_no") + "");
			tempMap.put("activity_type", tMap.get("activity_type") + "");
			tempMap.put("title", tMap.get("title"));
			tempMap.put("min_num", tMap.get("min_num"));
			tempMap.put("supplier", tMap.get("supplier"));
			tempMap.put("goods_code", tMap.get("goods_code"));
			tempMap.put("expiried_date",
					DateUtil.date2Str((Date) tMap.get("expiried_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("discount_price", tMap.get("discount_price") + "");
			tempMap.put("cost_allocation", tMap.get("cost_allocation") + "");
			tempMap.put("ladder_price", tMap.get("ladder_price") + "");
			tempMap.put("sale_price", tMap.get("sale_price") + "");
			tempMap.put("order_qty", tMap.get("order_qty") + "");
			tempMap.put("member_info", tMap.get("member_info"));
			tempMap.put("received_mode", tMap.get("received_mode"));
			tempMap.put("created_date",
					DateUtil.date2Str((Date) tMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("status", tMap.get("status"));
			tempMap.put("logistics", StringUtil.formatStr(tMap.get("logistics")));
			tempMap.put("remark", StringUtil.formatStr(tMap.get("remark")));
			tempMap.put("biz_remark", StringUtil.formatStr(tMap.get("biz_remark")));
			// 解析门店信息
			Map<String, Object> storesInfoMap = FastJsonUtil.jsonToMap(tMap.get("stores_info") + "");
			tempMap.put("stores_name", storesInfoMap.get("stores_name") + "");
			// 解析收货地址信息
			Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(tMap.get("consignee_json_data") + "");
			tempMap.put("contact_person", consigneeMap.get("contact_person") + "");
			tempMap.put("contact_tel", consigneeMap.get("contact_tel") + "");
			tempMap.put("address", consigneeMap.get("address") + "");
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void nearbyTsActivityListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id", "longitude", "latitude" });
		String longitude = paramsMap.get("longitude") + "";
		String latitude = paramsMap.get("latitude") + "";
		Double longitude_gt = Double.parseDouble(longitude) - 1;
		Double latitude_gt = Double.parseDouble(latitude) - 1;
		Double longitude_lt = Double.parseDouble(longitude) + 1;
		Double latitude_lt = Double.parseDouble(latitude) + 1;
		paramsMap.put("longitude_gt", longitude_gt);
		paramsMap.put("latitude_gt", latitude_gt);
		paramsMap.put("longitude_lt", longitude_lt);
		paramsMap.put("latitude_lt", latitude_lt);
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> qryList = tsActivityDao.selectNearbyTsActivityListForConsumer(paramsMap);
		for (Map<String, Object> tMap : qryList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", tMap.get("activity_id") + "");
			Date expiried_date = (Date) tMap.get("expiried_date");
			tempMap.put("difftime", (expiried_date.getTime() - new Date().getTime()) / 1000);
			tempMap.put("order_qty", tMap.get("order_qty") + "");
			tempMap.put("stores_id", tMap.get("stores_id") + "");
			tempMap.put("distance", tMap.get("distance") + "");
			Map<String, Object> storesMap = FastJsonUtil.jsonToMap(tMap.get("stores_info") + "");
			tempMap.put("stores_name", storesMap.get("stores_name") + "");
			// 解析图片
			tempMap.put("neighbor_pic_path", "");
			String neighbor_pic_path = StringUtil.formatStr(storesMap.get("neighbor_pic_path"));
			if (StringUtils.isNotEmpty(neighbor_pic_path)) {
				String[] str = neighbor_pic_path.split(",");
				if (str.length > 0) {
					tempMap.put("neighbor_pic_path", str[0]);
					doc_ids.add(str[0]);
				}
			}
			Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(tMap.get("consignee_json_data") + "");
			tempMap.put("address", consigneeMap.get("address") + "");
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		map.put("order_num", "0");
		// 统计商品已经下了多少单
		Map<String, Object> qryMap = new HashMap<String, Object>();
		qryMap.put("goods_id", paramsMap.get("goods_id"));
		Map<String, Object> countMap = tsActivityDao.selectTsActivityOrderCount(qryMap);
		if (null != countMap) {
			map.put("order_num", countMap.get("order_num") + "");
		}
		result.setResultData(map);
	}

	@Override
	public void tsActivityDetail(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
		Map<String, String> activityMap = new HashMap<String, String>();
		List<String> doc_ids = new ArrayList<String>();
		TsActivity tsActivity = tsActivityDao.selectTsActivityDetail(paramsMap);
		activityMap.put("activity_id", tsActivity.getActivity_id() + "");
		activityMap.put("activity_no", tsActivity.getActivity_no() + "");
		activityMap.put("ladder_price", tsActivity.getLadder_price() + "");
		activityMap.put("sale_price", tsActivity.getSale_price() + "");
		activityMap.put("discount_price", tsActivity.getDiscount_price() + "");
		activityMap.put("cost_allocation", tsActivity.getCost_allocation() + "");
		activityMap.put("title", tsActivity.getTitle());
		activityMap.put("received_mode", tsActivity.getReceived_mode());
		activityMap.put("status", tsActivity.getStatus());
		activityMap.put("order_qty", tsActivity.getOrder_qty() + "");
		activityMap.put("min_num", tsActivity.getMin_num() + "");
		activityMap.put("member_info", tsActivity.getMember_info() + "");
		activityMap.put("created_date",
				DateUtil.date2Str((Date) tsActivity.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
		Date expiried_date = (Date) tsActivity.getExpiried_date();
		activityMap.put("expiried_date", DateUtil.date2Str(expiried_date, DateUtil.fmt_yyyyMMddHHmmss));
		activityMap.put("difftime", (expiried_date.getTime() - new Date().getTime()) / 1000 + "");
		activityMap.put("goods_json_data", tsActivity.getGoods_json_data());
		// 解析图片
		String pic_info = tsActivity.getGoods_json_data();
		if (StringUtils.isNotEmpty(pic_info)) {
			Map<String, Object> picMap = FastJsonUtil.jsonToMap(pic_info);
			if (!StringUtil.formatStr(picMap.get("path_id")).equals("")) {
				doc_ids.add(picMap.get("path_id") + "");
			}
		}
		// 解析门店信息
		Map<String, Object> storesInfoMap = FastJsonUtil.jsonToMap(tsActivity.getStores_info());
		activityMap.put("stores_name", storesInfoMap.get("stores_name") + "");
		// 解析收货地址信息
		Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(tsActivity.getConsignee_json_data());
		activityMap.put("contact_person", consigneeMap.get("contact_person") + "");
		activityMap.put("contact_tel", consigneeMap.get("contact_tel") + "");
		activityMap.put("address", consigneeMap.get("address") + "");
		// 返回数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("detail", activityMap);
		resultMap.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(resultMap);
	}

	@Override
	public void tsActivityCountForH5(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, String> resultDataMap = new HashMap<String, String>();
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
		TsActivity tsActivity = tsActivityDao.selectTsActivity(paramsMap);
		if (null == tsActivity) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动不存在");
		}
		if (!tsActivity.getStatus().equals(Constant.ORDER_ACTIVING)) {
			if (tsActivity.getStatus().equals(Constant.ORDER_SUCCEED)) {
				throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已成团");
			} else if (tsActivity.getStatus().equals(Constant.ORDER_CANCELLED)) {
				throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已取消");
			} else {
				throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已结束");
			}
		}
		resultDataMap.put("min_num", tsActivity.getMin_num() + "");
		resultDataMap.put("order_qty", tsActivity.getOrder_qty() + "");
		resultDataMap.put("sale_price", tsActivity.getSale_price() + "");
		resultDataMap.put("max_price",
				MoneyUtil.moneyAdd(tsActivity.getDiscount_price(), tsActivity.getCost_allocation()) + "");
		resultDataMap.put("min_price", tsActivity.getDiscount_price() + "");
		// 查询参观人头像列表
		List<String> doc_ids = new ArrayList<String>();
		List<String> head_pic_list = new ArrayList<String>();

		// 获取发起人头像
		String creater_pic_path = getConsumerHeadPicPath(tsActivity.getMember_id());
		head_pic_list.add(creater_pic_path);
		doc_ids.add(creater_pic_path);
		List<Map<String, Object>> joinList = tsOrderDao.selectTsOrderJoinList(paramsMap);
		for (Map<String, Object> joinInfo : joinList) {
			Map<String, Object> consignee_json_data = FastJsonUtil.jsonToMap(joinInfo.get("consignee_json_data") + "");
			String head_pic_path = consignee_json_data.get("head_pic_path") + "";
			head_pic_list.add(head_pic_path);
			if (StringUtils.isNotEmpty(head_pic_path)) {
				doc_ids.add(head_pic_path);
			}
		}
		Map<String, Object> docMap = docUtil.imageUrlFind(doc_ids);
		doc_ids.clear();
		for (String doc_id : head_pic_list) {
			Object url = docMap.get(doc_id);
			if (null != url) {
				doc_ids.add(url + "");
			} else {
				doc_ids.add("");
			}
		}
		resultDataMap.put("doc_url_list", FastJsonUtil.toJson(doc_ids));
		result.setResultData(resultDataMap);
	}

	@Override
	public void tsActivityListForStoresPageFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> qryList = tsActivityDao.selectTsActivityListForStores(paramsMap);
		for (Map<String, Object> tMap : qryList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", tMap.get("activity_id") + "");
			tempMap.put("activity_no", tMap.get("activity_no") + "");
			tempMap.put("member_info", tMap.get("member_info") + "");
			tempMap.put("title", tMap.get("title") + "");
			tempMap.put("min_num", tMap.get("min_num") + "");
			tempMap.put("ladder_price", tMap.get("ladder_price") + "");
			tempMap.put("expiried_date",
					DateUtil.date2Str((Date) tMap.get("expiried_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("status", tMap.get("status"));
			tempMap.put("received_mode", tMap.get("received_mode") + "");
			// 解析图片
			tempMap.put("goods_json_data", tMap.get("goods_json_data") + "");
			String pic_info = tMap.get("goods_json_data") + "";
			if (StringUtils.isNotEmpty(pic_info)) {
				Map<String, Object> picMap = FastJsonUtil.jsonToMap(pic_info);
				if (!StringUtil.formatStr(picMap.get("path_id")).equals("")) {
					doc_ids.add(picMap.get("path_id") + "");
				}
			}
			// 解析收货地址信息
			Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(tMap.get("consignee_json_data") + "");
			tempMap.put("contact_person", consigneeMap.get("contact_person") + "");
			tempMap.put("contact_tel", consigneeMap.get("contact_tel") + "");
			tempMap.put("address", consigneeMap.get("address") + "");
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void sponsorTsActivityListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> qryList = tsActivityDao.selectTsActivityListForConsumer(paramsMap);
		for (Map<String, Object> tMap : qryList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", tMap.get("activity_id") + "");
			tempMap.put("activity_no", tMap.get("activity_no") + "");
			tempMap.put("title", tMap.get("title") + "");
			tempMap.put("min_num", tMap.get("min_num") + "");
			tempMap.put("sale_price", tMap.get("sale_price") + "");
			tempMap.put("expiried_date",
					DateUtil.date2Str((Date) tMap.get("expiried_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("status", tMap.get("status"));
			tempMap.put("received_mode", tMap.get("received_mode") + "");
			// 解析图片
			tempMap.put("goods_json_data", tMap.get("goods_json_data") + "");
			String pic_info = tMap.get("goods_json_data") + "";
			if (StringUtils.isNotEmpty(pic_info)) {
				Map<String, Object> picMap = FastJsonUtil.jsonToMap(pic_info);
				if (!StringUtil.formatStr(picMap.get("path_id")).equals("")) {
					doc_ids.add(picMap.get("path_id") + "");
				}
			}
			// 解析门店信息
			Map<String, Object> storesInfoMap = FastJsonUtil.jsonToMap(tMap.get("stores_info") + "");
			tempMap.put("stores_name", storesInfoMap.get("stores_name") + "");
			// 解析收货地址信息
			Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(tMap.get("consignee_json_data") + "");
			tempMap.put("contact_person", consigneeMap.get("contact_person") + "");
			tempMap.put("contact_tel", consigneeMap.get("contact_tel") + "");
			tempMap.put("address", consigneeMap.get("address") + "");
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void joinListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> joinList = tsOrderDao.selectTsOrderJoinList(paramsMap);
		for (Map<String, Object> joinInfo : joinList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_date",
					DateUtil.date2Str((Date) joinInfo.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("member_mobile", joinInfo.get("member_info"));
			Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(joinInfo.get("consignee_json_data") + "");
			String head_pic_path = consigneeMap.get("head_pic_path") + "";
			tempMap.put("head_pic_path", head_pic_path);
			if (StringUtils.isNotEmpty(head_pic_path)) {
				doc_ids.add(head_pic_path);
			}
			tempMap.put("contact_person", consigneeMap.get("contact_person") + "");
			tempMap.put("contact_tel", consigneeMap.get("contact_tel") + "");
			tempMap.put("address", consigneeMap.get("address") + "");
			resultList.add(tempMap);
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		resultMap.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(resultMap);
	}

	@Override
	public void tsActivityEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "biz_remark" });
		tsActivityDao.updateTsActivity(paramsMap);
	}

	@Override
	public void handleTsActivityCancelForOp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = null;
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "remark" });
			String activity_id = paramsMap.get("activity_id") + "";
			// 手机号商品加锁
			lockKey = "[tsActivityCancelForOp]_" + activity_id;
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();

			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				throw new BusinessException(RspCode.ORDER_ERROE, "活动正在取消中,请稍后重试");
			}
			redisUtil.setStr(lockKey, activity_id, 180);

			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.ORDER_SUCCEED);
			TsActivity tsActivity = tsActivityDao.selectTsActivity(tempMap);
			if (null == tsActivity) {
				throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "操作失败,请刷新后重试");
			}
			Integer order_qty = tsActivity.getOrder_qty();
			// 更新活动状态
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("order_qty", "0");
			tempMap.put("status", Constant.ORDER_CANCELLED);
			tempMap.put("remark", paramsMap.get("remark"));
			int update_flag = tsActivityDao.updateTsActivity(tempMap);
			if (update_flag == 0) {
				throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "操作失败,请刷新后重试");
			}
			// 查询出已返款的订单列表
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.ORDER_REFUNDED);
			List<TsOrder> tsOrderList = tsOrderDao.selectTsOrder(tempMap);
			for (TsOrder tsOrder : tsOrderList) {
				tempMap.clear();
				tempMap.put("order_id", tsOrder.getOrder_id());
				tempMap.put("status", Constant.ORDER_CANCELLED);
				tempMap.put("biz_remark", "运营人员取消");
				tempMap.put("modified_date",
						DateUtil.date2Str(tsOrder.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
				int updateFlag = tsOrderDao.updateTsOrder(tempMap);
				// 更新订单状态成功,则进行退还成员价格操作
				if (updateFlag == 1) {
					if (!tsOrder.getPayment_way_key().equals("ZFFS_19")) {
						// 退货成团价格
						Map<String, Object> out_trade_body = new HashMap<String, Object>();
						out_trade_body.put("order_no", tsOrder.getOrder_no());
						out_trade_body.put("activity_id", tsOrder.getActivity_id());
						out_trade_body.put("total_fee", tsOrder.getTotal_fee());
						orderService.orderRefund("SJLY_03", tsOrder.getMember_id(), Constant.MEMBER_ID_MTH, "ZFFS_05",
								tsOrder.getTotal_fee(), "DDLX_15", tsOrder.getOrder_no(), "拼团领订单退款", out_trade_body);
					}
				}
			}
			// 还原商品库存
			orderService.psGoodsSaleQtyRestore(tsActivity.getGoods_id(), order_qty + "");
		} catch (Exception e) {
			throw e;
		} finally {
			if (lock != null) {
				lock.unlock();
			}
			if (StringUtil.isNotEmpty(lockKey)) {
				redisUtil.del(lockKey);
			}
		}

	}

	@Override
	public void handleTsActivityCancelForConsumer(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
		activityFailForLadder(paramsMap.get("activity_id") + "", Constant.ORDER_CANCELLED,
				StringUtil.formatStr(paramsMap.get("remark")), Constant.ORDER_CANCELLED, "发起人取消活动");
	}

	@Override
	public void handleTsActivityDeliver(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "logistics" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("activity_id", paramsMap.get("activity_id"));
		tempMap.put("status", Constant.ORDER_SUCCEED);
		TsActivity tsActivity = tsActivityDao.selectTsActivity(paramsMap);
		if (null == tsActivity) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已结束");
		}
		// 发送短信
		String logistics = paramsMap.get("logistics") + "";
		Map<String, Object> logisticsMap = FastJsonUtil.jsonToMap(logistics);
		String company = logisticsMap.get("company") + "";
		String number = logisticsMap.get("number") + "";
		String msg = "您在【拼团领】发起的团购商品【" + tsActivity.getTitle() + "】已经发货，物流信息【" + company + ":" + number
				+ "】，请注意物流信息，谢谢合作。";

		tempMap.clear();
		tempMap.put("activity_id", tsActivity.getActivity_id());
		tempMap.put("status", Constant.ORDER_DELIVERED);
		tempMap.put("logistics", paramsMap.get("logistics"));
		tempMap.put("status_where", Constant.ORDER_SUCCEED);
		tempMap.put("modified_date", DateUtil.date2Str(tsActivity.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = tsActivityDao.updateTsActivity(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
		}
		// 发送短信
		OrderServiceImpl.sendMsg(tsActivity.getMember_info(), msg);
	}

	@Override
	public void handleTsActivityReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("activity_id", paramsMap.get("activity_id"));
		tempMap.put("status", Constant.ORDER_DELIVERED);
		TsActivity tsActivity = tsActivityDao.selectTsActivityDetail(paramsMap);
		if (null == tsActivity) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动未发货");
		}

		tempMap.clear();
		tempMap.put("activity_id", tsActivity.getActivity_id());
		tempMap.put("status", Constant.ORDER_RECEIVED);
		tempMap.put("status_where", Constant.ORDER_DELIVERED);
		tempMap.put("modified_date", DateUtil.date2Str(tsActivity.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = tsActivityDao.updateTsActivity(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
		}
		// 订单变成已发货
		tempMap.clear();
		tempMap.clear();
		tempMap.put("activity_id", tsActivity.getActivity_id());
		tempMap.put("status", Constant.ORDER_DELIVERED);
		tempMap.put("status_where", Constant.ORDER_REFUNDED);
		tsOrderDao.updateTsOrder(tempMap);

		// 订单导入店东的商品库
		psOrderService.psGoodsImport(tsActivity.getStores_id(), tsActivity.getGoods_code(), 1, "拼团领商品导入",
				"huopintuan");

	}

	@Override
	public void activitySuccessForLadder(String activity_id) throws BusinessException, SystemException, Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("activity_id", activity_id);
		tempMap.put("status", Constant.ORDER_ACTIVING);
		TsActivity tsActivity = tsActivityDao.selectTsActivityDetail(tempMap);
		if (null == tsActivity) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已结束");
		}
		// 扣库存标示(成功/失败)
		boolean deduction_flag = true;
		// 扣除商品库存
		try {
			orderService.psGoodsSaleQtyDeduction(tsActivity.getGoods_id(), tsActivity.getOrder_qty() + "");
		} catch (Exception e) {
			deduction_flag = false;
		}

		if (!deduction_flag) {
			// 扣库存失败,活动失败
			activityFailForLadder(activity_id, Constant.ORDER_FAIL, "库存不足,成团失败", Constant.ORDER_CANCELLED, "库存不足,订单取消");
		} else {
			// 更新订单状态
			tempMap.clear();
			tempMap.put("activity_id", tsActivity.getActivity_id());
			tempMap.put("status", Constant.ORDER_REFUNDED);
			tempMap.put("sale_price", tsActivity.getLadder_price() + "");
			tempMap.put("total_fee", tsActivity.getLadder_price() + "");
			tempMap.put("status_where", Constant.ORDER_PAID);
			tsOrderDao.updateTsOrder(tempMap);

			// 更新活动状态
			tempMap.clear();
			tempMap.put("activity_id", tsActivity.getActivity_id());
			tempMap.put("status", Constant.ORDER_SUCCEED);
			tempMap.put("status_where", Constant.ORDER_ACTIVING);
			tempMap.put("remark", "拼团领已成团");
			tempMap.put("sale_price", tsActivity.getLadder_price());
			tsActivityDao.updateTsActivity(tempMap);

			BigDecimal activity_sale_price = tsActivity.getSale_price();
			// 进行订单退款
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.ORDER_REFUNDED);
			List<TsOrder> tsOrderList = tsOrderDao.selectTsOrder(tempMap);
			// 逐笔给消费者退款
			for (TsOrder order : tsOrderList) {
				// 预付款
				BigDecimal deposit_fee = order.getDeposit_fee();
				// 差额
				BigDecimal diff_fee = MoneyUtil.moneySub(deposit_fee, activity_sale_price);

				// 差额大于0,才进行退款
				if (diff_fee.compareTo(BigDecimal.ZERO) > 0) {
					// 如果支付方式为权益券,则不退款
					if (!order.getPayment_way_key().equals("ZFFS_19")) {
						Map<String, Object> out_trade_body = new HashMap<String, Object>();
						out_trade_body.put("activity_id", order.getActivity_id());
						out_trade_body.put("order_no", order.getOrder_no());
						out_trade_body.put("deposit_fee", order.getDeposit_fee());
						out_trade_body.put("activity_sale_price", activity_sale_price);
						out_trade_body.put("diff_fee", diff_fee);
						orderService.orderRefund("SJLY_03", order.getMember_id(), Constant.MEMBER_ID_MTH, "ZFFS_05",
								diff_fee, "DDLX_15", order.getOrder_no(), "溢价支付", out_trade_body);

						// 记录结算日志
						tsActivitySettlementCreate(tsActivity, diff_fee, "溢价支付", order.getOrder_no());
					}
				}
			}
			// 给开团人送免单券
			rewardGdBenefit(tsActivity.getMember_id());
		}
	}

	@Override
	public void activityFailForLadder(String activity_id, String activityStatus, String activityRemark,
			String orderStatus, String orderRemark) throws BusinessException, SystemException, Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("activity_id", activity_id);
		tempMap.put("status", Constant.ORDER_ACTIVING);
		TsActivity tsActivity = tsActivityDao.selectTsActivity(tempMap);
		if (null == tsActivity) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已结束");
		}
		tempMap.clear();
		tempMap.put("activity_id", activity_id);
		tempMap.put("status", Constant.ORDER_PAID);
		List<TsOrder> tsOrderList = tsOrderDao.selectTsOrder(tempMap);
		for (TsOrder tsOrder : tsOrderList) {
			tempMap.clear();
			// 预付款
			BigDecimal deposit_fee = tsOrder.getDeposit_fee();
			tempMap.put("order_id", tsOrder.getOrder_id());
			tempMap.put("remark", orderRemark);
			tempMap.put("status", orderStatus);
			tempMap.put("status_where", Constant.ORDER_PAID);
			int updateFlag = tsOrderDao.updateTsOrder(tempMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
			}
			if (!tsOrder.getPayment_way_key().equals("ZFFS_19")) {
				Map<String, Object> out_trade_body = new HashMap<String, Object>();
				out_trade_body.put("activity_id", activity_id);
				out_trade_body.put("order_no", tsOrder.getOrder_no());
				out_trade_body.put("deposit_fee", deposit_fee);
				orderService.orderRefund("SJLY_03", tsOrder.getMember_id(), Constant.MEMBER_ID_MTH, "ZFFS_05",
						deposit_fee, "DDLX_15", tsOrder.getOrder_no(), "拼团领订单退款", out_trade_body);
			}
		}
		// 更新活动状态
		tempMap.clear();
		tempMap.put("activity_id", tsActivity.getActivity_id());
		tempMap.put("status", activityStatus);
		tempMap.put("remark", activityRemark);
		tempMap.put("status_where", Constant.ORDER_ACTIVING);
		tempMap.put("modified_date", DateUtil.date2Str(tsActivity.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = tsActivityDao.updateTsActivity(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
		}
	}

	/**
	 * 记录结算日志
	 * 
	 * @Title: tsActivitySettlementCreate
	 * @param tsActivity
	 * @param voucher_desc
	 * @author tiny
	 */
	private void tsActivitySettlementCreate(TsActivity tsActivity, BigDecimal amount, String desc1, String voucher_desc)
			throws BusinessException, SystemException, Exception {
		Date date = new Date();
		TsActivitySettlement settlement = new TsActivitySettlement();
		settlement.setSettlement_id(IDUtil.getUUID());
		settlement.setActivity_id(tsActivity.getActivity_id());
		settlement.setActivity_no(tsActivity.getActivity_no());
		settlement.setGoods_id(tsActivity.getGoods_id());
		settlement.setGoods_code(tsActivity.getGoods_code());
		settlement.setGoods_title(tsActivity.getTitle());
		settlement.setMarket_price(tsActivity.getMarket_price());
		settlement.setDiscount_price(tsActivity.getDiscount_price());
		settlement.setSettled_price(tsActivity.getSettled_price());
		settlement.setSale_price(tsActivity.getSale_price());
		settlement.setAmount(amount);
		settlement.setDesc1(desc1);
		settlement.setMember_mobile(tsActivity.getMember_info());
		settlement.setVoucher_desc(voucher_desc);
		settlement.setCreated_date(date);
		settlement.setModified_date(date);
		settlement.setSettled_date(date);
		tsActivitySettlementDao.insertTsActivitySettlement(settlement);
	}

	@Override
	public void qualificationValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id" });
		List<Map<String, Object>> validateList = new ArrayList<Map<String, Object>>();

		Map<String, Object> violation = new HashMap<String, Object>();
		violation.put("title", "没有违规记录,口碑及信用良好");
		violation.put("flag", "true");
		// 查询黑名单
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "member.memberInfoFindByMemberId");
		bizParams.put("member_id", paramsMap.get("consumer_id"));
		bizParams.put("member_type_key", "consumer");
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.MEMBER_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		bizParams.clear();
		bizParams.put("mobile", dataMap.get("mobile"));
		List<Map<String, Object>> fgBlacklist = fgBlacklistDao.selectFgBlacklist(bizParams);
		if (fgBlacklist.size() > 0) {
			violation.put("flag", "false");
		}
		Map<String, Object> wallet = new HashMap<String, Object>();
		wallet.put("title", "成功消费零钱,钱包功能已激活");
		wallet.put("flag", "true");

		Map<String, Object> invitation = new HashMap<String, Object>();
		invitation.put("title", "成功邀请好友,权益活动已激活");
		invitation.put("flag", "true");

		validateList.add(violation);
		validateList.add(wallet);
		validateList.add(invitation);
		result.setResultData(validateList);
	}

	@Override
	public void rewardGdBenefit(final String member_id) {
		OrderController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					Map<String, Object> bizParams = new HashMap<String, Object>();
					reqParams.put("service", "gdBenefit.operate.gdBenefitCreate");
					bizParams.put("benefit_type", "free_coupon");
					String date = DateUtil.getFormatDate(DateUtil.fmt_yyyyMMdd);
					date = DateUtil.addDate(date, DateUtil.fmt_yyyyMMdd, 2, 1);
					bizParams.put("expired_date", date + " 23:59:59");
					bizParams.put("member_id", member_id);
					bizParams.put("limited_price", "10.00");
					bizParams.put("amount", "10.00");
					bizParams.put("event", "完成拼团领活动系统赠送");
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					String resultStr = HttpClientUtil.postShort(goods_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						throw new BusinessException((String) resultMap.get("error_code"),
								(String) resultMap.get("error_msg"));
					}
				} catch (BusinessException e) {
					logger.warn("团购订单平台返利失败," + e.getMsg());
				} catch (SystemException e) {
					logger.error("团购订单平台返利异常", e);
				} catch (Exception e) {
					logger.error("团购订单平台返利异常", e);
				}
			}
		});
	}

	/**
	 * 获取消费者头像
	 * 
	 * @Title: getConsumerHeadPicPath
	 * @param consumer_id
	 * @return
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	private String getConsumerHeadPicPath(String consumer_id) {
		String head_pic_path = "";
		try {
			// 增加缓存
			String cacheKey = "[getConsumerHeadPicPath]" + consumer_id;
			head_pic_path = redisUtil.getStr(cacheKey);
			if (StringUtils.isNotEmpty(head_pic_path)) {
				return head_pic_path;
			}
			// 消费者头像
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			reqParams.put("service", "consumer.consumer.consumerBaseInfoFind");
			bizParams.put("consumer_id", consumer_id);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> consuemrMap = (Map<String, Object>) resultMap.get("data");
			if (null == consuemrMap) {
				throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "会员信息不存在");
			}
			// 设置缓存
			head_pic_path = consuemrMap.get("head_pic_path") + "";
			if (StringUtils.isNotEmpty(head_pic_path)) {
				redisUtil.setStr(cacheKey, head_pic_path, 604800);
			}
		} catch (Exception e) {
			logger.error("获取会员头像异常", e);
		}
		return head_pic_path;
	}

}
