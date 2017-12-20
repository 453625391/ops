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
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.order.constant.Constant;
import com.meitianhui.order.constant.OrderIDUtil;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.controller.OrderController;
import com.meitianhui.order.dao.OrderDao;
import com.meitianhui.order.dao.TsActivityDao;
import com.meitianhui.order.dao.TsOrderDao;
import com.meitianhui.order.entity.OdSettlement;
import com.meitianhui.order.entity.OdSettlementDetail;
import com.meitianhui.order.entity.TsActivity;
import com.meitianhui.order.entity.TsOrder;
import com.meitianhui.order.service.OrderService;
import com.meitianhui.order.service.TsActivityService;
import com.meitianhui.order.service.TsOrderService;

/**
 * 伙拼团活动
 * 
 * @ClassName: TsOrderServiceImpl
 * @author tiny
 * @date 2017年2月27日 下午7:07:58
 *
 */
@SuppressWarnings("unchecked")
@Service
public class TsOrderServiceImpl implements TsOrderService {

	private static final Logger logger = Logger.getLogger(TsOrderServiceImpl.class);

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private DocUtil docUtil;
	@Autowired
	private TsActivityDao tsActivityDao;
	@Autowired
	private TsOrderDao tsOrderDao;
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderService orderService;
	@Autowired
	private TsActivityService tsActivityService;

	@Override
	public void handleFreeCouponPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "activity_id",
				"member_mobile", "amount", "benefit_id" });
		String activity_id = paramsMap.get("activity_id") + "";
		String cache_activity_id = redisUtil.getStr(activity_id + "_ts_order_create");
		if (StringUtils.isNotEmpty(cache_activity_id)) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已结束");
		}
		Map<String, Object> bizParams = new HashMap<String, Object>();
		bizParams.put("activity_id", activity_id);
		bizParams.put("status", Constant.ORDER_ACTIVING);
		TsActivity tsActivity = tsActivityDao.selectTsActivity(bizParams);
		if (null == tsActivity) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已结束");
		}
		// 使用免单券支付
		bizParams.clear();
		Map<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("service", "gdBenefit.consumer.freeCouponPay");
		bizParams.put("member_id", paramsMap.get("member_id"));
		bizParams.put("benefit_id", paramsMap.get("benefit_id"));
		bizParams.put("pay_amount",
				MoneyUtil.moneyAdd(tsActivity.getCost_allocation(), tsActivity.getDiscount_price()));
		bizParams.put("event", "拼团领");
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		paramsMap.put("payment_way_key", "ZFFS_19");
		ladderTsOrderCreate(paramsMap, result);
	}

	@Override
	public void ladderTsOrderValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "goods_id" });
		// 检测商品是否有限购要求
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "goods.psGoodsFindForTsActivity");
		bizParams.put("goods_id", paramsMap.get("goods_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> psGoodsMap = (Map<String, Object>) resultMap.get("data");
		Integer max_buy_qty = (Integer) psGoodsMap.get("max_buy_qty");
		// 如果max_buy_qty大于0,则表明活动商品有限购条件
		if (max_buy_qty > 0) {
			// 检测会员是否已经参与过此活动
			paramsMap.put("status_in", new String[] { "paid", "refunded", "delivered", "received" });
			Map<String, Object> countMap = tsOrderDao.selectTsOrderCount(paramsMap);
			if (countMap != null) {
				Integer count_num = Integer.parseInt(countMap.get("count_num") + "");
				if (count_num > 0) {
					throw new BusinessException(RspCode.ORDER_ERROE, "此商品一个用户只能参与一次");
				}
			}
		}
	}

	@Override
	public void ladderTsOrderCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "activity_id",
				"member_mobile", "payment_way_key", "amount" });
		RedisLock lock = null;
		try {
			String member_mobile = paramsMap.get("member_mobile") + "";
			BigDecimal amount = new BigDecimal(paramsMap.get("amount") + "");
			String activity_id = paramsMap.get("activity_id") + "";
			String cache_activity_id = redisUtil.getStr(activity_id + "_ts_order_create");
			if (StringUtils.isNotEmpty(cache_activity_id)) {
				throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已成团");
			}
			lock = new RedisLock(redisUtil, activity_id, 10 * 1000);
			lock.lock();
			paramsMap.put("deposit_fee", amount);
			paramsMap.put("total_fee", "0.00");
			paramsMap.put("sale_price", "0.00");
			paramsMap.put("member_info", paramsMap.get("member_mobile"));
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			bizParams.put("activity_id", activity_id);
			bizParams.put("status", Constant.ORDER_ACTIVING);
			TsActivity tsActivity = tsActivityDao.selectTsActivity(bizParams);
			if (null == tsActivity) {
				throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已结束");
			}
			if (amount.compareTo(tsActivity.getDiscount_price()) < 0) {
				throw new BusinessException(RspCode.ORDER_ERROE, "订单金额被篡改");
			}
			// 消费者头像
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "consumer.consumer.consumerBaseInfoFind");
			bizParams.put("consumer_id", paramsMap.get("member_id"));
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(OrderServiceImpl.MEMBER_SERVICE_URL, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> consuemrMap = (Map<String, Object>) resultMap.get("data");
			if (null == consuemrMap) {
				throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "会员信息查询失败");
			}

			// 收货地址信息
			Map<String, Object> consigneeJsonData = new HashMap<String, Object>();
			// 消费者头像
			consigneeJsonData.put("head_pic_path", consuemrMap.get("head_pic_path") + "");
			consigneeJsonData.put("contact_person", StringUtil.formatStr(consuemrMap.get("nick_name")));
			consigneeJsonData.put("contact_tel", member_mobile);
			consigneeJsonData.put("address", "未知");
			// 获取消费者默认收货地址
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "member.consumerAddressFind");
			bizParams.put("consumer_id", paramsMap.get("member_id"));
			bizParams.put("is_major_addr", "Y");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(OrderServiceImpl.MEMBER_SERVICE_URL, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				List<Map<String, Object>> addressList = (List<Map<String, Object>>) resultMap.get("data");
				if (addressList.size() > 0) {
					Map<String, Object> addressMap = addressList.get(0);
					consigneeJsonData.put("contact_person", addressMap.get("consignee") + "");
					consigneeJsonData.put("contact_tel", addressMap.get("mobile") + "");
					String area_desc = addressMap.get("area_desc") + "";
					String address = addressMap.get("address") + "";
					consigneeJsonData.put("address", area_desc.replace("中国", "").replace(",", "") + address);
				}
			}

			// 创建订单
			Date date = new Date();
			TsOrder order = new TsOrder();
			BeanConvertUtil.mapToBean(order, paramsMap);
			order.setOrder_id(IDUtil.getUUID());
			order.setOrder_no(OrderIDUtil.getOrderNo());
			order.setActivity_id(tsActivity.getActivity_id());
			order.setOrder_date(date);
			order.setLoaded_code(IDUtil.random(4));
			order.setDesc1(tsActivity.getTitle());
			order.setItem_num(1);
			order.setPayment_way_key(paramsMap.get("payment_way_key") + "");
			order.setMember_type_key(Constant.MEMBER_TYPE_CONSUMER);
			order.setConsignee_json_data(FastJsonUtil.toJson(consigneeJsonData));
			order.setStatus(Constant.ORDER_PAID);
			order.setSettle_status(Constant.ORDER_SETTLE_PENDING);
			order.setCreated_date(date);
			order.setModified_date(date);
			tsOrderDao.insertTsOrder(order);

			bizParams.clear();
			bizParams.put("activity_id", activity_id);
			bizParams.put("order_qty_add", "1");
			int update_flag = tsActivityDao.updateTsActivity(bizParams);
			if (update_flag == 0) {
				throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动已成团");
			}
			resultMap.clear();
			resultMap.put("order_no", order.getOrder_no());
			result.setResultData(resultMap);
			// 检测是否已经成团
			checkActivityOrderNum(activity_id);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != lock) {
				lock.unlock();
			}
		}
	}

	@Override
	public void tsOrderListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = tsOrderDao.selectTsOrderList(paramsMap);
		for (Map<String, Object> order : list) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_id", order.get("order_id"));
			tempMap.put("activity_id", order.get("activity_id"));
			tempMap.put("order_no", order.get("order_no"));
			tempMap.put("order_date", DateUtil.date2Str((Date) order.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("loaded_code", StringUtil.formatStr(order.get("loaded_code")));
			tempMap.put("status", order.get("status"));
			Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(order.get("consignee_json_data") + "");
			// 如果是门店自提,则收货地址换成门店的地址
			tempMap.put("contact_person", consigneeMap.get("contact_person") + "");
			tempMap.put("contact_tel", consigneeMap.get("contact_tel") + "");
			tempMap.put("address", consigneeMap.get("address") + "");
			resultList.add(tempMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		result.setResultData(resultMap);
	}

	@Override
	public void tsOrderListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = tsOrderDao.selectTsOrderListForConsumer(paramsMap);
		for (Map<String, Object> order : list) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_id", order.get("order_id"));
			tempMap.put("activity_id", order.get("activity_id"));
			tempMap.put("order_no", order.get("order_no"));
			tempMap.put("order_date", DateUtil.date2Str((Date) order.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("loaded_code", StringUtil.formatStr(order.get("loaded_code")));
			tempMap.put("desc1", order.get("desc1"));
			tempMap.put("deposit_fee", order.get("deposit_fee") + "");
			tempMap.put("status", order.get("status"));
			// 解析图片
			tempMap.put("goods_json_data", order.get("goods_json_data") + "");
			String pic_info = order.get("goods_json_data") + "";
			if (StringUtils.isNotEmpty(pic_info)) {
				Map<String, Object> picMap = FastJsonUtil.jsonToMap(pic_info);
				if (!StringUtil.formatStr(picMap.get("path_id")).equals("")) {
					doc_ids.add(picMap.get("path_id") + "");
				}
			}
			// 物流信息
			tempMap.put("logistics", StringUtil.formatStr(order.get("logistics")));
			// 解析收货信息
			tempMap.put("received_mode", order.get("received_mode"));
			String received_mode = order.get("received_mode") + "";
			Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(order.get("consumer_consignee") + "");
			// 如果是门店自提,则收货地址换成门店的地址
			if (received_mode.equals("pick_up")) {
				consigneeMap = FastJsonUtil.jsonToMap(order.get("stores_consignee") + "");
				Map<String, Object> storesInfoMap = FastJsonUtil.jsonToMap(order.get("stores_info") + "");
				tempMap.put("stores_name", storesInfoMap.get("stores_name") + "");
			}
			tempMap.put("address", consigneeMap.get("address") + "");
			resultList.add(tempMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		resultMap.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(resultMap);
	}

	@Override
	public void tsOrderDetailForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id" });
		List<String> doc_ids = new ArrayList<String>();
		Map<String, Object> orderMap = tsOrderDao.selectTsOrderDetail(paramsMap);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", orderMap.get("order_id"));
		tempMap.put("order_no", orderMap.get("order_no"));
		tempMap.put("activity_id", orderMap.get("activity_id"));
		tempMap.put("order_date", DateUtil.date2Str((Date) orderMap.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
		tempMap.put("desc1", orderMap.get("desc1"));
		tempMap.put("payment_way_key", orderMap.get("payment_way_key"));
		tempMap.put("item_num", orderMap.get("item_num") + "");
		tempMap.put("activity_ladder_price", orderMap.get("activity_ladder_price") + "");
		tempMap.put("deposit_fee", orderMap.get("deposit_fee") + "");
		// 去除
		tempMap.put("total_fee", orderMap.get("total_fee") + "");
		tempMap.put("activity_sale_price", orderMap.get("activity_sale_price") + "");

		tempMap.put("status", orderMap.get("status"));
		tempMap.put("expiried_date",
				DateUtil.date2Str((Date) orderMap.get("expiried_date"), DateUtil.fmt_yyyyMMddHHmmss));
		tempMap.put("received_mode", orderMap.get("received_mode"));
		tempMap.put("member_mobile", orderMap.get("member_info"));
		// 解析收货信息
		String received_mode = orderMap.get("received_mode") + "";
		Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(orderMap.get("consumer_consignee") + "");
		// 如果是门店自提,则收货地址换成门店的地址
		if (received_mode.equals("pick_up")) {
			consigneeMap = FastJsonUtil.jsonToMap(orderMap.get("stores_consignee") + "");
			Map<String, Object> storesInfoMap = FastJsonUtil.jsonToMap(orderMap.get("stores_info") + "");
			tempMap.put("stores_name", storesInfoMap.get("stores_name") + "");
		}
		tempMap.put("address", consigneeMap.get("address") + "");
		tempMap.put("contact_person", consigneeMap.get("contact_person") + "");
		tempMap.put("contact_tel", consigneeMap.get("contact_tel") + "");
		// 解析图片
		tempMap.put("goods_json_data", orderMap.get("goods_json_data") + "");
		String pic_info = orderMap.get("goods_json_data") + "";
		if (StringUtils.isNotEmpty(pic_info)) {
			Map<String, Object> picMap = FastJsonUtil.jsonToMap(pic_info);
			if (!StringUtil.formatStr(picMap.get("path_id")).equals("")) {
				doc_ids.add(picMap.get("path_id") + "");
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("detail", tempMap);
		resultMap.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(resultMap);
	}

	@Override
	public void tsOrderListForOpFind(Map<String, Object> paramsMap, ResultData result)
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
		List<Map<String, Object>> list = tsOrderDao.selectTsOrderListForOp(paramsMap);
		for (Map<String, Object> order : list) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_id", order.get("order_id"));
			tempMap.put("order_no", order.get("order_no"));
			tempMap.put("activity_no", order.get("activity_no"));
			tempMap.put("order_date", DateUtil.date2Str((Date) order.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("desc1", order.get("desc1"));
			tempMap.put("desc2", StringUtil.formatStr(order.get("desc2")));
			tempMap.put("payment_way_key", order.get("payment_way_key") + "");
			tempMap.put("deposit_fee", order.get("deposit_fee") + "");
			tempMap.put("total_fee", order.get("total_fee") + "");
			tempMap.put("member_info", order.get("member_info") + "");
			tempMap.put("status", order.get("status"));
			tempMap.put("settle_status", order.get("settle_status") + "");
			tempMap.put("remark", StringUtil.formatStr(order.get("remark")));
			tempMap.put("biz_remark", StringUtil.formatStr(order.get("biz_remark")));
			tempMap.put("settled_price", order.get("settled_price") + "");
			tempMap.put("supplier", order.get("supplier") + "");
			tempMap.put("manufacturer", StringUtil.formatStr(order.get("manufacturer")));
			tempMap.put("goods_code", order.get("goods_code") + "");
			// 解析收货地址信息
			Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(order.get("consignee_json_data") + "");
			tempMap.put("address", consigneeMap.get("address") + "");
			tempMap.put("contact_person", consigneeMap.get("contact_person") + "");
			tempMap.put("contact_tel", consigneeMap.get("contact_tel") + "");
			resultList.add(tempMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		result.setResultData(resultMap);
	}

	@Override
	public void handleTsOrderCancelForConsumer(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id", "remark" });
		List<TsOrder> tsOrderList = tsOrderDao.selectTsOrder(paramsMap);
		if (tsOrderList.size() == 0) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ORDER_ERROR, "订单不存在");
		}
		TsOrder tsOrder = tsOrderList.get(0);
		if (!tsOrder.getStatus().equals(Constant.ORDER_SETTLE_PAID)) {
			throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
		}
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", tsOrder.getOrder_id());
		tempMap.put("status", Constant.ORDER_CANCELLED);
		tempMap.put("remark", paramsMap.get("remark"));
		tempMap.put("modified_date", DateUtil.date2Str(tsOrder.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = tsOrderDao.updateTsOrder(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
		}
		if (!tsOrder.getPayment_way_key().equals("ZFFS_19")) {
			// 退还 预付款
			Map<String, Object> out_trade_body = new HashMap<String, Object>();
			out_trade_body.put("order_no", tsOrder.getOrder_no());
			out_trade_body.put("activity_id", tsOrder.getActivity_id());
			out_trade_body.put("amount", tsOrder.getDeposit_fee());
			orderService.orderRefund("SJLY_03", tsOrder.getMember_id(), Constant.MEMBER_ID_MTH, "ZFFS_05",
					tsOrder.getDeposit_fee(), "DDLX_15", tsOrder.getOrder_no(), "拼团领订单退款", out_trade_body);
		}
		tempMap.clear();
		tempMap.put("activity_id", tsOrder.getActivity_id());
		tempMap.put("order_qty_add", "-1");
		int update_flag = tsActivityDao.updateTsActivity(tempMap);
		if (update_flag == 0) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "操作失败,请刷新后重试");
		}
	}

	@Override
	public void tsOrderCancelForOp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id", "biz_remark" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", paramsMap.get("order_id"));
		tempMap.put("status", Constant.ORDER_RECEIVED);
		List<TsOrder> tsOrderList = tsOrderDao.selectTsOrder(paramsMap);
		if (tsOrderList.size() == 0) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ORDER_ERROR, "订单不存在");
		}
		TsOrder tsOrder = tsOrderList.get(0);
		tempMap.clear();
		tempMap.put("order_id", tsOrder.getOrder_id());
		tempMap.put("status", Constant.ORDER_CANCELLED);
		tempMap.put("biz_remark", paramsMap.get("biz_remark"));
		tempMap.put("modified_date", DateUtil.date2Str(tsOrder.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = tsOrderDao.updateTsOrder(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
		}
		if (!tsOrder.getPayment_way_key().equals("ZFFS_19")) {
			// 退货成团价格
			Map<String, Object> out_trade_body = new HashMap<String, Object>();
			out_trade_body.put("order_no", tsOrder.getOrder_no());
			out_trade_body.put("activity_id", tsOrder.getActivity_id());
			out_trade_body.put("total_fee", tsOrder.getTotal_fee());
			orderService.orderRefund("SJLY_03", tsOrder.getMember_id(), Constant.MEMBER_ID_MTH, "ZFFS_05",
					tsOrder.getTotal_fee(), "DDLX_15", tsOrder.getOrder_no(), "拼团领订单退款", out_trade_body);
		}
		// 更新订单数量
		tempMap.clear();
		tempMap.put("activity_id", tsOrder.getActivity_id());
		tempMap.put("order_qty_add", "-1");
		int update_flag = tsActivityDao.updateTsActivity(tempMap);
		if (update_flag == 0) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "操作失败,请刷新后重试");
		}

		tempMap.clear();
		tempMap.put("activity_id", tsOrder.getActivity_id());
		TsActivity tsActivity = tsActivityDao.selectTsActivity(tempMap);
		// 还原商品库存
		orderService.psGoodsSaleQtyRestore(tsActivity.getGoods_id(), "1");
	}

	@Override
	public void handleTsOrderReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id" });
		String order_id = paramsMap.get("order_id") + "";
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", order_id);
		tempMap.put("status", Constant.ORDER_DELIVERED);
		List<TsOrder> tsOrderList = tsOrderDao.selectTsOrder(tempMap);
		if (tsOrderList.size() == 0) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ORDER_ERROR, "订单不存在");
		}
		TsOrder tsOrder = tsOrderList.get(0);
		// 查询活动
		tempMap.clear();
		tempMap.put("activity_id", tsOrder.getActivity_id());
		tempMap.put("status", Constant.ORDER_RECEIVED);
		TsActivity tsActivity = tsActivityDao.selectTsActivityDetail(tempMap);
		if (null == tsActivity) {
			throw new BusinessException(RspCode.TS_ACTIVITY_ERROR, "活动不存在");
		}
		// 更新订单状态
		tempMap.clear();
		tempMap.put("order_id", tsOrder.getOrder_id());
		tempMap.put("status", Constant.ORDER_RECEIVED);
		tempMap.put("modified_date", DateUtil.date2Str(tsOrder.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = tsOrderDao.updateTsOrder(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
		}
		// 结算 给店东返钱
		Map<String, Object> out_trade_body = new HashMap<String, Object>();
		out_trade_body.put("activity_id", tsActivity.getActivity_id());
		out_trade_body.put("order_no", tsOrder.getOrder_no());
		orderService.balancePay("SJLY_03", Constant.MEMBER_ID_MTH, tsActivity.getStores_id(), "ZFFS_05",
				new BigDecimal("1.00"), "DDLX_15", tsOrder.getOrder_no(), "开团收益", out_trade_body);
	}

	@Override
	public void tsOrderSettleListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String order_date = paramsMap.get("order_date") + "";
		if (StringUtils.isNotEmpty(order_date)) {
			paramsMap.remove("order_date");
			paramsMap.put("order_date_start", order_date);
			paramsMap.put("order_date_end", order_date);
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = tsOrderDao.selectSettleTsOrderListForOp(paramsMap);
		for (Map<String, Object> order : list) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_id", order.get("order_id"));
			tempMap.put("order_no", order.get("order_no"));
			tempMap.put("activity_no", order.get("activity_no"));
			tempMap.put("order_date", DateUtil.date2Str((Date) order.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("desc1", order.get("desc1"));
			tempMap.put("member_info", order.get("member_info") + "");
			tempMap.put("settle_status", order.get("settle_status"));
			tempMap.put("settled_price", order.get("settled_price") + "");
			tempMap.put("supplier", order.get("supplier") + "");
			// 解析收货地址信息
			Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(order.get("consignee_json_data") + "");
			tempMap.put("address", consigneeMap.get("address") + "");
			tempMap.put("contact_person", consigneeMap.get("contact_person") + "");
			tempMap.put("contact_tel", consigneeMap.get("contact_tel") + "");
			resultList.add(tempMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		result.setResultData(resultMap);
	}

	@Override
	public void tsOrderSettleCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String order_date = paramsMap.get("order_date") + "";
		if (StringUtils.isNotEmpty(order_date)) {
			paramsMap.remove("order_date");
			paramsMap.put("order_date_start", order_date);
			paramsMap.put("order_date_end", order_date);
		}
		Map<String, Object> settlementCountMap = tsOrderDao.selectSettleTsOrderCount(paramsMap);
		Map<String, Object> countMap = new HashMap<String, Object>();
		countMap.put("total_fee", "0.00");
		countMap.put("count_num", "0");
		if (null != settlementCountMap) {
			if (null != settlementCountMap.get("total_fee")) {
				countMap.put("total_fee", settlementCountMap.get("total_fee"));
			}
			countMap.put("count_num", settlementCountMap.get("count_num") + "");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", countMap);
		result.setResultData(map);
	}

	@Override
	public void handleTsOrderSettle(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "operator", "order_date" });
		String data_source = Constant.ORDER_TYPE_TSACTIVITY;
		Map<String, Object> qryMap = new HashMap<String, Object>();
		String operator = paramsMap.get("operator") + "";
		String order_date = paramsMap.get("order_date") + "";
		qryMap.put("order_date_start", order_date);
		qryMap.put("order_date_end", order_date);
		List<Map<String, Object>> settlementList = tsOrderDao.selectSettleTsOrderList(qryMap);
		Map<String, List<Map<String, Object>>> settlementMap = new HashMap<String, List<Map<String, Object>>>();

		// 统计供应商数量
		Map<String, String> orderTemp = new HashMap<String, String>();
		for (Map<String, Object> map : settlementList) {
			String supplier_id = map.get("supplier_id") + "";
			if (null != settlementMap.get(supplier_id)) {
				List<Map<String, Object>> orderList = settlementMap.get(supplier_id);
				orderList.add(map);
				// 计算供应商的订单总数
				String order_id = map.get("order_id") + "";
				// 查询订单集合中是否存在此订单，如果不存在,则说明这是新的订单列表
				String order_id_temp = orderTemp.get(order_id);
				if (null == order_id_temp) {
					orderTemp.put(order_id, order_id);
				}
			} else {
				List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
				orderList.add(map);
				settlementMap.put(supplier_id, orderList);
			}
		}
		int succ_count = 0;
		// 按供应商进行订单结算
		Map<String, Object> bizParams = new HashMap<String, Object>();
		Date date = new Date();
		for (String supplier_id : settlementMap.keySet()) {
			Integer orderNum = 0; // 按订单计算订单总数
			// 结算订单
			OdSettlement settlement = new OdSettlement();
			// 结算订单列表
			List<OdSettlementDetail> detailList = new ArrayList<OdSettlementDetail>();
			settlement.setSettlement_id(IDUtil.getTimestamp(7));
			// 结算金额
			String settlementAmount = "0.00";
			String supplier = "";
			List<Map<String, Object>> tempList = settlementMap.get(supplier_id);
			// 生成结算清单
			for (Map<String, Object> tempMap : tempList) {
				// 以商品的结算价格金额为主
				String total_fee = tempMap.get("settled_price") + "";
				settlementAmount = MoneyUtil.moneyAdd(settlementAmount, total_fee);
				supplier = StringUtil.formatStr(tempMap.get("supplier"));
				OdSettlementDetail detail = new OdSettlementDetail();
				detail.setData_source(data_source);
				detail.setDetail_id(IDUtil.getUUID());
				detail.setSettlement_id(settlement.getSettlement_id());
				detail.setOrder_id(tempMap.get("order_id") + "");
				detail.setOrder_no(tempMap.get("order_no") + "");
				detail.setGoods_id(tempMap.get("goods_id") + "");
				detail.setGoods_code(tempMap.get("goods_code") + "");
				detail.setGoods_title(tempMap.get("goods_title") + "");
				detail.setSupplier_id(supplier_id);
				detail.setSupplier(supplier);
				detail.setQty(1);
				detail.setTotal_fee(new BigDecimal(total_fee));
				detail.setCreated_date(date);
				detail.setModified_date(date);
				detail.setStatus(Constant.ORDER_SETTLE_SETTLED);
				detailList.add(detail);
				bizParams.clear();
				bizParams.put("order_id", detail.getOrder_id());
				bizParams.put("settle_status", Constant.ORDER_SETTLE_SETTLED);
				bizParams.put("modified_date",
						DateUtil.date2Str((Date) tempMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
				int updateFlag = tsOrderDao.updateTsOrder(bizParams);
				if (updateFlag == 0) {
					throw new BusinessException(RspCode.ORDER_PROCESSING, "请刷新订单后再进行结算操作");
				}
				orderNum++;
			}
			settlement.setData_source(data_source);
			// 订单金额
			settlement.setOrder_amount(new BigDecimal(settlementAmount));
			// 平台服务费
			settlement.setCommission_fee(new BigDecimal("0.00"));
			// 结算金额
			settlement.setAmount(new BigDecimal(settlementAmount));
			settlement.setOperator(operator);
			settlement.setSettled_date(DateUtil.str2Date(paramsMap.get("order_date") + "", DateUtil.fmt_yyyyMMdd));
			settlement.setSupplier_id(supplier_id);
			settlement.setSupplier(supplier);
			settlement.setCreated_date(date);
			settlement.setModified_date(date);
			settlement.setStatus(Constant.ORDER_SETTLE_SETTLED);
			orderDao.insertOdSettlementDetailList(detailList);
			// 设置供应商的结算的订单数量
			settlement.setOrder_qty(orderNum);
			succ_count += orderNum;
			orderDao.insertOdSettlement(settlement);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("fail_count", settlementList.size() - succ_count);
		resultMap.put("succ_count", succ_count);
		result.setResultData(resultMap);
	}

	@Override
	public void tsOrderForStoresSaleCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 参于会员数
		resultMap.put("participate_member", "0");
		// 总订单数
		resultMap.put("order_num", "0.00");
		// 总金额
		resultMap.put("totle_amount", "0.00");
		// 累计收益
		resultMap.put("totle_earnings", "0.00");

		paramsMap.put("status_in", new String[] { "delivered", "received", "refunded" });
		Map<String, Object> countsMap = tsOrderDao.selectTsOrderForStoresCount(paramsMap);
		if (null != countsMap) {
			resultMap.put("participate_member",
					countsMap.get("participate_member") == null ? 0 : countsMap.get("participate_member") + "");
			resultMap.put("order_num", countsMap.get("order_num") + "");
			resultMap.put("totle_amount", MoneyUtil.formatMoney(countsMap.get("totle_amount")));
			resultMap.put("totle_earnings", MoneyUtil.formatMoney(countsMap.get("totle_earnings")));
		}
		result.setResultData(resultMap);
	}

	/**
	 * 检测活动订单数(未成团)
	 * 
	 * @Title: caclPrice
	 * @param activity_id
	 * @author tiny
	 */
	@Override
	public void checkActivityOrderNum(final String activity_id) {
		OrderController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					// 获取活动信息
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("activity_id", activity_id);
					tempMap.put("status", Constant.ORDER_PAID);
					Map<String, Object> orderCount = tsActivityDao.selectTsActivityOrderCount(tempMap);
					tempMap.clear();
					// 达标人数
					Integer min_num = Integer.parseInt(orderCount.get("min_num") + "");
					/** 当前订单数 **/
					Integer order_num = Integer.parseInt(orderCount.get("order_num") + "");
					// 是否成团
					boolean flag = false;
					// 活动成团
					if (order_num == min_num) {
						// 增加 redis 缓存验证,如果已成团,就不让用户再继续购买
						redisUtil.setStr(activity_id + "_ts_order_create",
								activity_id + "_" + order_num + "/" + min_num, 20);
						/** 阶梯价格 **/
						BigDecimal ladder_price = new BigDecimal(orderCount.get("ladder_price") + "");
						// 更新价格
						tempMap.put("sale_price", ladder_price);
						flag = true;
						// 更新订单数量
						tempMap.put("activity_id", activity_id);
						tsActivityDao.updateTsActivity(tempMap);
					}
					// 如果成团,则进行开团逻辑
					if (flag) {
						tsActivityService.activitySuccessForLadder(activity_id);
					}
				} catch (Exception e) {
					logger.error("检测活动是否成团异常", e);
				}
			}
		});
	}

}
