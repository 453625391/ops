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
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.order.constant.Constant;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.controller.OrderController;
import com.meitianhui.order.dao.FgOrderDao;
import com.meitianhui.order.dao.GeOrderDao;
import com.meitianhui.order.dao.OrderDao;
import com.meitianhui.order.dao.PsOrderDao;
import com.meitianhui.order.entity.GeOrder;
import com.meitianhui.order.entity.PsOrder;
import com.meitianhui.order.service.OrderService;

/**
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);

	public static String GOODS_SERVICE_URL = PropertiesConfigUtil.getProperty("goods_service_url");

	public static String FINANCE_SERVICE_URL = PropertiesConfigUtil.getProperty("finance_service_url");

	public static String MEMBER_SERVICE_URL = PropertiesConfigUtil.getProperty("member_service_url");

	public static String NOTIFICATION_SERVICE_URL = PropertiesConfigUtil.getProperty("notification_service_url");

	@Autowired
	private DocUtil docUtil;
	@Autowired
	public RedisUtil redisUtil;

	@Autowired
	public OrderDao orderDao;
	@Autowired
	public PsOrderDao psOrderDao;
	@Autowired
	public FgOrderDao fgOrderDao;
	@Autowired
	public GeOrderDao geOrderDao;

	@Override
	public void orderLogisticsFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id", "order_type" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		String order_type = paramsMap.get("order_type") + "";
		paramsMap.remove("order_type");
		if (order_type.equals("WYP")) {
			// 我要批
			List<PsOrder> orderList = psOrderDao.selectPsOrder(paramsMap);
			if (orderList.size() == 0) {
				throw new BusinessException(RspCode.ORDER_NOT_EXIST, "订单不存在");
			}
			PsOrder order = orderList.get(0);
			tempMap.put("logistics", StringUtil.formatStr(order.getLogistics()));
		} else if (order_type.equals("LLM")) {
			// 领了么
			List<Map<String, Object>> psOrderList = fgOrderDao.selectFgOrder(paramsMap);
			if (psOrderList.size() == 0) {
				throw new BusinessException(RspCode.ORDER_NOT_EXIST, "订单不存在");
			}
			Map<String, Object> map = psOrderList.get(0);
			tempMap.put("logistics", StringUtil.formatStr(map.get("logistics")));
		} else if (order_type.equals("JBD")) {
			// 名品汇
			List<GeOrder> orderList = geOrderDao.selectGeOrder(paramsMap);
			if (orderList.size() == 0) {
				throw new BusinessException(RspCode.ORDER_NOT_EXIST, "订单不存在");
			}
			GeOrder order = orderList.get(0);
			tempMap.put("logistics", StringUtil.formatStr(order.getLogistics()));
		}

		Map<String, Object> resultDateMap = new HashMap<String, Object>();
		resultDateMap.put("detail", tempMap);
		result.setResultData(resultDateMap);
	}

	@Override
	public void orderSettlementList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> OrderSettlementList = orderDao.selectOrderSettlement(paramsMap);
		for (Map<String, Object> map : OrderSettlementList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("settlement_id", map.get("settlement_id"));
			tempMap.put("settled_date", DateUtil.date2Str((Date) map.get("settled_date"), DateUtil.fmt_yyyyMMdd));
			tempMap.put("created_date", DateUtil.date2Str((Date) map.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("modified_date",
					DateUtil.date2Str((Date) map.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("supplier", map.get("supplier"));
			tempMap.put("order_qty", map.get("order_qty"));
			tempMap.put("amount", map.get("amount") + "");
			tempMap.put("order_amount", map.get("order_amount") + "");
			tempMap.put("commission_fee", map.get("commission_fee") + "");
			tempMap.put("operator", map.get("operator"));
			tempMap.put("status", map.get("status"));
			tempMap.put("remark", StringUtil.formatStr(map.get("remark")));
			resultList.add(tempMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		result.setResultData(resultMap);
	}

	/***
	 * 订单结算付款
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date 2016年12月15日
	 */
	@Override
	public void handleOrderSettlementPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "settlement_id", "operator" });
		String settlement_id = paramsMap.get("settlement_id") + "";
		Map<String, Object> bizParams = new HashMap<String, Object>();
		bizParams.put("settlement_id", settlement_id);
		bizParams.put("status", Constant.ORDER_SETTLE_SETTLED);
		List<Map<String, Object>> settlementList = orderDao.selectOrderSettlement(bizParams);
		if (settlementList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_ERROE, "结算历史不存在");
		}
		Map<String, Object> settlementMap = settlementList.get(0); // 结算信息
		bizParams.clear();
		bizParams.put("settlement_id", settlement_id);
		bizParams.put("operator", paramsMap.get("operator"));
		bizParams.put("modified_date",
				DateUtil.date2Str((Date) settlementMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
		bizParams.put("status", Constant.ORDER_SETTLE_PAID);
		int updateFlag = orderDao.updateOrderSettlement(bizParams);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_SETTLEMENT_PROCESSING, "请刷新后再进行结算支付操作");
		}

		// 调用支付接口
		bizParams.clear();
		bizParams.put("data_source", "SJLY_03");
		bizParams.put("payment_way_key", "ZFFS_05");
		bizParams.put("detail", "订单结算支付");
		bizParams.put("amount", settlementMap.get("amount") + "");
		bizParams.put("out_trade_no", settlementMap.get("settlement_id"));
		bizParams.put("buyer_id", Constant.MEMBER_ID_MTH);
		bizParams.put("seller_id", settlementMap.get("supplier_id"));

		Map<String, Object> out_trade_body = new HashMap<String, Object>();
		out_trade_body.put("amount", settlementMap.get("amount") + "");
		out_trade_body.put("supplier_id", settlementMap.get("supplier_id"));
		bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));

		Map<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("service", "finance.orderSettlement");
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.post(OrderServiceImpl.FINANCE_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}

	}

	@Override
	public void zjsOrderListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id" });
		List<Map<String, Object>> orderItemList = orderDao.selectZJSOrder(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		for (Map<String, Object> orderMap : orderItemList) {
			orderMap.put("order_date",
					DateUtil.date2Str((Date) orderMap.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			orderMap.put("remark", StringUtil.formatStr(orderMap.get("remark")));
			orderMap.put("logistics", StringUtil.formatStr(orderMap.get("logistics")));
			orderMap.put("loaded_code", StringUtil.formatStr(orderMap.get("loaded_code")));
			// type 1:精选特卖，2:团购预售
			String type = orderMap.get("type") + "";
			List<Map<String, Object>> goodsList = new ArrayList<Map<String, Object>>();
			String goods_pic_info = orderMap.get("goods_pic_info") + "";
			if (type.equals("1")) {
				// 解析本特特卖订单的商品信息
				String[] item = goods_pic_info.split("\\|");
				for (String itemStr : item) {
					Map<String, Object> goodsMap = new HashMap<String, Object>();
					String[] goods = itemStr.split("~_~");
					goodsMap.put("goods_title", goods[0]);
					goodsMap.put("path_id", goods[1]);
					doc_ids.add(goods[1]);
					goodsMap.put("qty", goods[2]);
					goodsMap.put("sale_price", goods[3]);
					goodsList.add(goodsMap);
				}
			} else {
				Map<String, Object> goodsMap = new HashMap<String, Object>();
				String goods_title = orderMap.get("goods_title") + "";
				goodsMap.put("goods_title", goods_title);

				String qty = orderMap.get("qty") + "";
				goodsMap.put("qty", qty);

				String sale_price = orderMap.get("sale_price") + "";
				goodsMap.put("sale_price", sale_price);
				goodsMap.put("path_id", "");
				if (StringUtils.isNotEmpty(goods_pic_info)) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(goods_pic_info);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
							goodsMap.put("path_id", m.get("path_id") + "");
						}
					}
				}
				goodsList.add(goodsMap);
			}
			// 清除已经重新组装的字段
			orderMap.remove("goods_title");
			orderMap.remove("qty");
			orderMap.remove("sale_price");
			orderMap.remove("goods_pic_info");
			orderMap.put("goods_list", FastJsonUtil.toJson(goodsList));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", orderItemList);
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(map);
	}

	@Override
	public void orderByloadedCodeForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "loaded_code", "stores_id" });
		List<Map<String, Object>> orderItemList = orderDao.selectOrderByLoadedCode(paramsMap);
		for (Map<String, Object> orderMap : orderItemList) {
			orderMap.put("order_date",
					DateUtil.date2Str((Date) orderMap.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			orderMap.put("loaded_code", StringUtil.formatStr(orderMap.get("loaded_code")));
			// type 1:精选特卖，2:团购预售
			String type = orderMap.get("type") + "";
			String goods_list = orderMap.get("goods_list") + "";
			orderMap.remove("goods_list");
			List<Map<String, Object>> goodsList = new ArrayList<Map<String, Object>>();
			if (type.equals("精选特卖")) {
				// 解析本特特卖订单的商品信息
				String[] item = goods_list.split("\\|");
				for (String itemStr : item) {
					Map<String, Object> goodsMap = new HashMap<String, Object>();
					String[] goods = itemStr.split("~_~");
					goodsMap.put("goods_title", goods[0]);
					goodsMap.put("qty", goods[1]);
					goodsList.add(goodsMap);
				}
			} else if (type.equals("团购预售")) {
				Map<String, Object> goodsMap = new HashMap<String, Object>();
				String[] goods = goods_list.split("~_~");
				goodsMap.put("goods_title", goods[0]);
				goodsMap.put("qty", goods[1]);
				goodsList.add(goodsMap);
			} else if (type.equals("拼团领")) {
				Map<String, Object> goodsMap = new HashMap<String, Object>();
				String[] goods = goods_list.split("~_~");
				goodsMap.put("goods_title", goods[0]);
				goodsMap.put("qty", goods[1]);
				goodsList.add(goodsMap);
				// 解析收货信息
				String address = orderMap.get("address") + "";
				orderMap.remove("address");
				Map<String, Object> consigneeMap = FastJsonUtil.jsonToMap(address);
				orderMap.put("address", consigneeMap.get("address") + "");
				orderMap.put("contact_person", consigneeMap.get("contact_person") + "");
				orderMap.put("contact_tel", consigneeMap.get("contact_tel") + "");
			}
			// 重新组装的字段
			orderMap.put("goods_list", FastJsonUtil.toJson(goodsList));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", orderItemList);
		result.setResultData(map);
	}

	/**
	 * 扣库存
	 * 
	 * @Title: psGoodsSaleQtyDeduction
	 * @param goods_code
	 * @param status
	 * @return
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void psGoodsSaleQtyRestore(String goods_id, String restore_qty) throws BusinessException, Exception {
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "goods.psGoodsSaleQtyRestore");
		bizParams.put("goods_id", goods_id);
		bizParams.put("restore_qty", restore_qty);
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(GOODS_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
	}

	/**
	 * 扣库存
	 * 
	 * @Title: psGoodsSaleQtyDeduction
	 * @param goods_code
	 * @param status
	 * @return
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void psGoodsSaleQtyDeduction(String goods_id, String sell_qty) throws BusinessException, Exception {
		// TODO 后期去掉 redis判断库存是否已经不足
		String goodsQtyKey = "[psOrder_psGoodsSaleQtyDeduction]_" + goods_id;
		String goodsQtyErrorDesc = redisUtil.getStr(goodsQtyKey);
		if (StringUtil.isNotEmpty(goodsQtyErrorDesc)) {
			throw new BusinessException("goods_sale_error", goodsQtyErrorDesc);
		}
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "goods.psGoodsSaleQtyDeduction");
		bizParams.put("goods_id", goods_id);
		bizParams.put("sell_qty", sell_qty);
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(GOODS_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			redisUtil.setStr(goodsQtyKey, (String) resultMap.get("error_msg"), 15);
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
	}

	/**
	 * 获取商品信息
	 * 
	 * @Title: psGoodsFindForOrder
	 * @param goods_id
	 * @param goods_code
	 * @param status
	 * @return
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public Map<String, Object> psGoodsFindForOrder(String goods_id, String goods_code)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> goodsDataMap = null;
		String psGoodsFindForOrder_key = "[order_psGoodsFindForOrder]_";
		// 组装查询参数
		Map<String, Object> bizParams = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(goods_id)) {
			psGoodsFindForOrder_key += goods_id;
			bizParams.put("goods_id", goods_id);
		}
		if (StringUtil.isNotEmpty(goods_code)) {
			psGoodsFindForOrder_key += goods_code;
			bizParams.put("goods_code", goods_code);
		}
		Object obj_goodsData = redisUtil.getObj(psGoodsFindForOrder_key);
		if (obj_goodsData != null) {
			goodsDataMap = (Map<String, Object>) obj_goodsData;
		} else {
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("service", "goods.psGoodsFindForOrder");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(GOODS_SERVICE_URL, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), "商品信息不存在");
			}
			goodsDataMap = (Map<String, Object>) resultMap.get("data");
			redisUtil.setObj(psGoodsFindForOrder_key, goodsDataMap, 60);
		}
		return goodsDataMap;
	}

	/**
	 * 余额支付
	 * 
	 * @Title: balancePay
	 * @param buyer_id
	 * @param seller_id
	 * @param payment_way_key
	 * @param amount
	 * @param out_trade_no
	 * @param detail
	 * @param out_trade_body
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void balancePay(String data_source, String buyer_id, String seller_id, String payment_way_key,
			BigDecimal amount, String order_type_key, String out_trade_no, String detail,
			Map<String, Object> out_trade_body) throws Exception {

		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "finance.balancePay");
		bizParams.put("data_source", data_source);
		bizParams.put("buyer_id", buyer_id);
		bizParams.put("seller_id", seller_id);
		bizParams.put("order_type_key", order_type_key);
		bizParams.put("payment_way_key", payment_way_key);
		bizParams.put("amount", amount + "");
		bizParams.put("detail", detail);
		bizParams.put("out_trade_no", out_trade_no);
		bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(FINANCE_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
	}

	/**
	 * 订单赠送
	 * 
	 * @Title: orderReward
	 * @param buyer_id
	 * @param seller_id
	 * @param payment_way_key
	 * @param amount
	 * @param out_trade_no
	 * @param detail
	 * @param out_trade_body
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void orderReward(String data_source, String buyer_id, String seller_id, String payment_way_key,
			BigDecimal amount, String order_type_key, String out_trade_no, String detail,
			Map<String, Object> out_trade_body) throws Exception {
		if (amount.compareTo(BigDecimal.ZERO) > 0) {
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			reqParams.put("service", "finance.orderReward");
			bizParams.put("data_source", data_source);
			bizParams.put("buyer_id", buyer_id);
			bizParams.put("seller_id", seller_id);
			bizParams.put("order_type_key", order_type_key);
			bizParams.put("payment_way_key", payment_way_key);
			bizParams.put("amount", amount + "");
			bizParams.put("detail", detail);
			bizParams.put("out_trade_no", out_trade_no);
			bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(OrderServiceImpl.FINANCE_SERVICE_URL, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		}
	}

	/**
	 * 订单退款
	 * 
	 * @Title: orderRefund
	 * @param buyer_id
	 * @param seller_id
	 * @param payment_way_key
	 * @param amount
	 * @param out_trade_no
	 * @param detail
	 * @param out_trade_body
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void orderRefund(String data_source, String buyer_id, String seller_id, String payment_way_key,
			BigDecimal amount, String order_type_key, String out_trade_no, String detail,
			Map<String, Object> out_trade_body) throws Exception {
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "finance.orderRefund");
		bizParams.put("data_source", data_source);
		bizParams.put("buyer_id", buyer_id);
		bizParams.put("seller_id", seller_id);
		bizParams.put("order_type_key", order_type_key);
		bizParams.put("payment_way_key", payment_way_key);
		bizParams.put("amount", amount + "");
		bizParams.put("detail", detail);
		bizParams.put("out_trade_no", out_trade_no);
		bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.FINANCE_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
	}

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 * @param msg
	 */
	public static void sendMsg(final String mobiles, final String msg) {
		OrderController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> reqParams = new HashMap<String, String>();
					Map<String, Object> bizParams = new HashMap<String, Object>();
					// 发送短信
					bizParams.put("sms_source", Constant.DATA_SOURCE_SJLY_03);
					bizParams.put("mobiles", mobiles);
					bizParams.put("msg", msg);
					reqParams.put("service", "notification.SMSSend");
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					HttpClientUtil.postShort(NOTIFICATION_SERVICE_URL, reqParams);
				} catch (Exception e) {
					logger.error("发送短信通知异常", e);
				}
			}
		});
	}

	/**
	 * 发送消息通知
	 * 
	 * @Title: appMsgNotify
	 * @param receiver
	 * @param message
	 * @author tiny
	 */
	public static void appMsgNotify(final String receiver, final String message) {
		OrderController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					// 发送APP消息通知
					Map<String, String> reqParams = new HashMap<String, String>();
					Map<String, Object> bizParams = new HashMap<String, Object>();
					bizParams.put("receiver", receiver);
					bizParams.put("message", message);
					reqParams.put("service", "member.appMsgNotify");
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					String resultStr = HttpClientUtil.postShort(MEMBER_SERVICE_URL, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						logger.info(receiver + "的消息通知失败," + (String) resultMap.get("error_msg"));
					}
				} catch (Exception e) {
					logger.error("发送app消息通知异常", e);
				}
			}
		});
	}

	@Override
	public void storesMemberRelCreate(final String stores_id, final String consumer_id) {
		OrderController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> reqParams = new HashMap<String, String>();
					Map<String, Object> bizParams = new HashMap<String, Object>();
					reqParams.put("service", "stores.app.storesMemberRelCreate");
					bizParams.put("stores_id", stores_id);
					bizParams.put("consumer_id", consumer_id);
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					HttpClientUtil.postShort(OrderServiceImpl.MEMBER_SERVICE_URL, reqParams);
				} catch (Exception e) {
					logger.error("门店会员关系创建异常", e);
				}
			}
		});
	}

}
