package com.meitianhui.order.service.impl;

import java.math.BigDecimal;
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
import com.meitianhui.common.util.MoneyUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.order.constant.Constant;
import com.meitianhui.order.constant.OrderIDUtil;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.dao.PcOrderDao;
import com.meitianhui.order.entity.PcOrder;
import com.meitianhui.order.entity.PcOrderItem;
import com.meitianhui.order.service.OrderService;
import com.meitianhui.order.service.PcOrderService;

/**
 * 精选特卖逻辑接口实现类
 * 
 * @author 丁硕
 * @date 2016年12月14日
 */
@SuppressWarnings("unchecked")
@Service
public class PcOrderServiceImpl implements PcOrderService {

	@Autowired
	private DocUtil docUtil;

	@Autowired
	private PcOrderDao pcOrderDao;

	@Autowired
	private OrderService orderService;

	@Override
	public void handlePcOrderPayCreateNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "stores_id", "stores_name", "consumer_id", "item_num", "payment_way_key", "total_fee",
						"discount_fee", "sale_fee", "contact_person", "contact_tel", "delivery_address",
						"store_item_list" });
		Date date = new Date();
		PcOrder order = new PcOrder();
		order.setRebate_fee(new BigDecimal(0)); // 设置默认返现金额
		BeanConvertUtil.mapToBean(order, paramsMap);
		order.setOrder_id(IDUtil.getUUID());
		order.setOrder_no(OrderIDUtil.getOrderNo());
		order.setStatus(Constant.ORDER_PAYED);
		order.setCreated_date(date);
		order.setOrder_date(date);
		order.setLoaded_code(IDUtil.random(4));
		order.setModified_date(date);
		pcOrderDao.insertPcOrder(order);
		// 解析商品信息
		List<Map<String, Object>> pc_goods_list = FastJsonUtil.jsonToList((String) paramsMap.get("store_item_list"));
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (Map<String, Object> pc_goods : pc_goods_list) {
			reqParams.clear();
			resultMap.clear();
			reqParams.put("service", "goods.itemStoreForOrderFind");
			bizParams.put("item_store_id", pc_goods.get("item_store_id"));
			bizParams.put("store_id", order.getStores_id());
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException(resultMap.get("error_code") + "", resultMap.get("error_msg") + "");
			}
			Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
			pc_goods.put("item_name", dataMap.get("item_name"));
			pc_goods.put("image_info", dataMap.get("image_info"));
			pc_goods.put("weight", dataMap.get("weight"));
			pc_goods.put("specification", dataMap.get("specification"));

			PcOrderItem pcOrderItem = new PcOrderItem();
			pcOrderItem.setRebate(new BigDecimal(0)); // 设置默认返现金额
			BeanConvertUtil.mapToBean(pcOrderItem, pc_goods);
			pcOrderItem.setOrder_item_id(IDUtil.getUUID());
			pcOrderItem.setOrder_id(order.getOrder_id());
			pcOrderItem.setCreated_date(date);
			pcOrderItem.setModified_date(date);
			pcOrderDao.insertPcOrderItem(pcOrderItem);

			itemStoreSaleQtyDeduction(pcOrderItem.getItem_store_id(), pcOrderItem.getQty() + "");
		}
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_no", order.getOrder_no());
		result.setResultData(tempMap);

	}

	@Override
	public void handlePcOrderCancelled(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id" });
		List<PcOrder> orderList = pcOrderDao.selectPcOrder(paramsMap);
		if (orderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		PcOrder order = orderList.get(0);
		if (!order.getStatus().equals(Constant.ORDER_PAYED)) {
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
		}
		// 更新订单状态
		paramsMap.put("status", Constant.ORDER_CANCELLED);
		paramsMap.put("modified_date", DateUtil.date2Str(order.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = pcOrderDao.updatePcOrder(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
		// 调用订单退款交易
		String data_source = StringUtil.formatStr(paramsMap.get("data_source"));
		if (data_source.equals("")) {
			data_source = "SJLY_01";
		}
		// 订单退款
		Map<String, Object> out_trade_body = new HashMap<String, Object>();
		out_trade_body.put("order_id", order.getOrder_id());
		out_trade_body.put("consumer_id", order.getConsumer_id());
		out_trade_body.put("order_desc", order.getDesc1());
		out_trade_body.put("total_fee", order.getTotal_fee() + "");
		orderService.orderRefund(data_source, order.getConsumer_id(), Constant.MEMBER_ID_MTH,
				order.getPayment_way_key(), order.getTotal_fee(), "DDLX_04", order.getOrder_no(), "精选特卖退款",
				out_trade_body);

		// 还原库存
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", order.getOrder_id());
		List<PcOrderItem> orderItemList = pcOrderDao.selectPcOrderItem(tempMap);
		if (orderItemList.size() == 0) {
			throw new BusinessException(RspCode.GOODS_NOT_EXIST, RspCode.MSG.get(RspCode.GOODS_NOT_EXIST));
		}

		for (PcOrderItem orderItem : orderItemList) {
			// 还原商品库存
			itemStoreSaleQtyRestore(orderItem.getItem_store_id(), orderItem.getQty() + "");
		}
	}

	@Override
	public void handlePcOrderDeliver(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id" });
		List<PcOrder> orderList = pcOrderDao.selectPcOrder(paramsMap);
		if (orderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		PcOrder pcOrder = orderList.get(0);
		if (!pcOrder.getStatus().equals(Constant.ORDER_PAYED)) {
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
		}
		paramsMap.put("status", Constant.ORDER_DELIVERED);
		paramsMap.put("delivery_date", DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMddHHmmss));
		paramsMap.put("modified_date", DateUtil.date2Str(pcOrder.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = pcOrderDao.updatePcOrder(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
	}

	/***
	 * 订单派工
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date 2016年12月30日
	 */
	@Override
	public void handlePcOrderAssigned(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id", "remark" });
		List<PcOrder> orderList = pcOrderDao.selectPcOrder(paramsMap);
		if (orderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		PcOrder order = orderList.get(0);
		if (!Constant.ORDER_PAYED.equals(order.getStatus())) {
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
		}
		paramsMap.put("status", Constant.ORDER_ASSIGNED);
		paramsMap.put("modified_date", DateUtil.date2Str(order.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = pcOrderDao.updatePcOrder(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
	}

	@Override
	public void handlePcOrderReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id" });
		List<PcOrder> orderList = pcOrderDao.selectPcOrder(paramsMap);
		if (orderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		PcOrder order = orderList.get(0);
		if (!order.getStatus().equals(Constant.ORDER_DELIVERED) && !order.getStatus().equals(Constant.ORDER_ASSIGNED)) { // 已发货跟已派工都可以进行收货
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
		}
		// 更新订单状态
		paramsMap.put("status", Constant.ORDER_RECEIVED);
		paramsMap.put("modified_date", DateUtil.date2Str(order.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = pcOrderDao.updatePcOrder(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
		String data_source = StringUtil.formatStr(paramsMap.get("data_source"));
		if (data_source.equals("")) {
			data_source = "SJLY_02";
		}
		// 将精选特卖的金额结算给门店
		BigDecimal stores_total_fee = MoneyUtil.moneySub(order.getTotal_fee(), order.getRebate_fee());
		if (stores_total_fee.compareTo(BigDecimal.ZERO) > 0) {
			Map<String, Object> out_trade_body = new HashMap<String, Object>();
			out_trade_body.put("order_id", order.getOrder_id());
			out_trade_body.put("stores_id", order.getStores_id());
			out_trade_body.put("order_desc", order.getDesc1());
			out_trade_body.put("total_fee", order.getTotal_fee() + "");
			out_trade_body.put("rebate_fee", order.getRebate_fee() + "");
			BigDecimal amount = MoneyUtil.moneySub(order.getTotal_fee(), order.getRebate_fee());
			orderService.balancePay(data_source, Constant.MEMBER_ID_MTH, order.getStores_id(), "ZFFS_05", amount,
					"DDLX_04", order.getOrder_no(), "精选特卖", out_trade_body);
		}

		// 返利给消费者
		if (order.getRebate_fee().compareTo(BigDecimal.ZERO) > 0) {
			Map<String, Object> out_trade_body = new HashMap<String, Object>();
			out_trade_body.put("order_id", order.getOrder_id());
			out_trade_body.put("consumer_id", order.getConsumer_id());
			out_trade_body.put("order_desc", order.getDesc1());
			out_trade_body.put("rebate_fee", order.getRebate_fee() + "");
			orderService.orderReward(data_source, order.getConsumer_id(), Constant.MEMBER_ID_MTH, "ZFFS_05",
					order.getRebate_fee(), "DDLX_04", order.getOrder_no(), "商家返利", out_trade_body);
		}
	}

	@Override
	public void pcOrderDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String status = StringUtil.formatStr(paramsMap.get("status"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		Map<String, Object> tempMap = new HashMap<String, Object>();
		List<PcOrder> orderList = pcOrderDao.selectPcOrder(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		for (PcOrder order : orderList) {
			Map<String, Object> orderDetailMap = new HashMap<String, Object>();
			orderDetailMap.put("order_id", order.getOrder_id());
			orderDetailMap.put("order_no", order.getOrder_no());
			orderDetailMap.put("order_date", DateUtil.date2Str(order.getOrder_date(), DateUtil.fmt_yyyyMMddHHmmss));
			orderDetailMap.put("stores_id", order.getStores_id());
			orderDetailMap.put("stores_name", order.getStores_name());
			orderDetailMap.put("consumer_id", order.getConsumer_id());
			orderDetailMap.put("desc1", StringUtil.formatStr(order.getDesc1()));
			orderDetailMap.put("item_num", order.getItem_num() + "");
			orderDetailMap.put("payment_way_key", order.getPayment_way_key());
			orderDetailMap.put("sale_fee", order.getSale_fee() + "");
			orderDetailMap.put("discount_fee", order.getDiscount_fee() + "");
			orderDetailMap.put("total_fee", order.getTotal_fee() + "");
			orderDetailMap.put("rebate_fee", order.getRebate_fee() + "");
			orderDetailMap.put("loaded_code", StringUtil.formatStr(order.getLoaded_code()));
			orderDetailMap.put("delivery_address", StringUtil.formatStr(order.getDelivery_address()));
			orderDetailMap.put("contact_person", StringUtil.formatStr(order.getContact_person()));
			orderDetailMap.put("contact_tel", StringUtil.formatStr(order.getContact_tel()));
			orderDetailMap.put("logistics", StringUtil.formatStr(order.getLogistics()));
			orderDetailMap.put("status", order.getStatus());
			orderDetailMap.put("remark", StringUtil.formatStr(order.getRemark()));
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			tempMap.clear();
			tempMap.put("order_id", order.getOrder_id());
			List<PcOrderItem> orderItemList = pcOrderDao.selectPcOrderItem(tempMap);
			for (PcOrderItem pcOrderItem : orderItemList) {
				Map<String, Object> itemDetailMap = new HashMap<String, Object>();
				itemDetailMap.put("item_store_id", pcOrderItem.getItem_store_id());
				itemDetailMap.put("item_name", pcOrderItem.getItem_name());
				itemDetailMap.put("item_image_info", StringUtil.formatStr(pcOrderItem.getImage_info()));
				itemDetailMap.put("item_qty", pcOrderItem.getQty() + "");
				itemDetailMap.put("item_weight", pcOrderItem.getWeight() + " ");
				itemDetailMap.put("item_specification", StringUtil.formatStr(pcOrderItem.getSpecification()));
				itemDetailMap.put("item_sale_price", pcOrderItem.getSale_price() + "");
				itemDetailMap.put("item_discount_fee", pcOrderItem.getDiscount_fee() + "");
				itemDetailMap.put("item_total_fee", pcOrderItem.getTotal_fee() + "");
				itemDetailMap.put("item_rebate", pcOrderItem.getRebate() + "");
				itemDetailMap.put("item_remark", pcOrderItem.getRemark());
				itemList.add(itemDetailMap);
				if (!StringUtil.formatStr(pcOrderItem.getImage_info()).equals("")) {
					doc_ids.add(pcOrderItem.getImage_info());
				}
			}
			orderDetailMap.put("itemList", itemList);
			resultList.add(orderDetailMap);
		}
		tempMap.clear();
		tempMap.put("list", resultList);
		tempMap.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(tempMap);
	}

	@Override
	public void pcOrderDetailForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String status = (String) paramsMap.get("status");
		if (null != status && !"".equals(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		Map<String, Object> tempMap = new HashMap<String, Object>();
		List<PcOrder> orderList = pcOrderDao.selectPcOrder(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (PcOrder order : orderList) {
			Map<String, Object> orderDetailMap = new HashMap<String, Object>();
			orderDetailMap.put("order_id", order.getOrder_id());
			orderDetailMap.put("order_no", order.getOrder_no());
			orderDetailMap.put("order_date", DateUtil.date2Str(order.getOrder_date(), DateUtil.fmt_yyyyMMddHHmmss));
			orderDetailMap.put("stores_id", order.getStores_id());
			orderDetailMap.put("stores_name", order.getStores_name());
			orderDetailMap.put("consumer_id", order.getConsumer_id());
			orderDetailMap.put("desc1", StringUtil.formatStr(order.getDesc1()));
			orderDetailMap.put("item_num", order.getItem_num() + "");
			orderDetailMap.put("payment_way_key", order.getPayment_way_key());
			orderDetailMap.put("sale_fee", order.getSale_fee() + "");
			orderDetailMap.put("discount_fee", order.getDiscount_fee() + "");
			orderDetailMap.put("total_fee", order.getTotal_fee() + "");
			orderDetailMap.put("rebate_fee", order.getRebate_fee() + "");
			orderDetailMap.put("delivery_address", order.getDelivery_address());
			orderDetailMap.put("contact_person", order.getContact_person());
			orderDetailMap.put("contact_tel", order.getContact_tel());
			orderDetailMap.put("logistics", StringUtil.formatStr(order.getLogistics()));
			orderDetailMap.put("status", order.getStatus());
			orderDetailMap.put("remark", StringUtil.formatStr(order.getRemark()));
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			tempMap.clear();
			tempMap.put("order_id", order.getOrder_id());
			List<PcOrderItem> orderItemList = pcOrderDao.selectPcOrderItem(tempMap);
			for (PcOrderItem pcOrderItem : orderItemList) {
				Map<String, Object> itemDetailMap = new HashMap<String, Object>();
				itemDetailMap.put("item_store_id", pcOrderItem.getItem_store_id());
				itemDetailMap.put("item_name", pcOrderItem.getItem_name());
				itemDetailMap.put("item_image_info", StringUtil.formatStr(pcOrderItem.getImage_info()));
				itemDetailMap.put("item_qty", pcOrderItem.getQty() + "");
				itemDetailMap.put("item_weight", pcOrderItem.getWeight() + " ");
				itemDetailMap.put("item_specification", StringUtil.formatStr(pcOrderItem.getSpecification()));
				itemDetailMap.put("item_sale_price", pcOrderItem.getSale_price() + "");
				itemDetailMap.put("item_discount_fee", pcOrderItem.getDiscount_fee() + "");
				itemDetailMap.put("item_total_fee", pcOrderItem.getTotal_fee() + "");
				itemDetailMap.put("item_rebate", pcOrderItem.getRebate() + "");
				itemDetailMap.put("item_remark", pcOrderItem.getRemark());
				itemList.add(itemDetailMap);
				if (!StringUtil.formatStr(pcOrderItem.getImage_info()).equals("")) {
					doc_ids.add(pcOrderItem.getImage_info());
				}
			}
			orderDetailMap.put("itemList", itemList);
			// 获取门店信息
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "member.storeForOrderFind");
			bizParams.put("stores_id", order.getStores_id());
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(OrderServiceImpl.MEMBER_SERVICE_URL, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				// 如果查询不到门店信息，就跳过此条记录
				continue;
			}
			Map<String, Object> storeDataMap = (Map<String, Object>) resultMap.get("data");
			orderDetailMap.put("stores_contact_person", StringUtil.formatStr(storeDataMap.get("contact_person")));
			orderDetailMap.put("stores_contact_tel", StringUtil.formatStr(storeDataMap.get("contact_tel")));
			resultList.add(orderDetailMap);
		}
		tempMap.clear();
		tempMap.put("list", resultList);
		tempMap.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(tempMap);
		reqParams.clear();
		bizParams.clear();
		reqParams = null;
		bizParams = null;
	}

	@Override
	public void pcOrderPayedCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("count_num", "0");
		Map<String, Object> countsMap = pcOrderDao.selectPcOrderPayedCount(paramsMap);
		if (null != countsMap) {
			resultMap.put("count_num", countsMap.get("count_num") + "");
		}
		result.setResultData(resultMap);
	}

	@Override
	public void pcOrderSellCountForStoresSaleFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("yesterday_sell_amount", "0.00");
		resultMap.put("month_sell_amount", "0.00");
		resultMap.put("totle_amount", "0.00");
		resultMap.put("wait_delivery", "0");
		String date = DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss);
		String stores_id = paramsMap.get("stores_id") + "";
		Map<String, Object> qryMap = new HashMap<String, Object>();
		// 昨天销售总金额
		qryMap.put("stores_id", stores_id);
		qryMap.put("status_in", new String[] { "payed", "delivered", "received" });
		String yesterday = DateUtil.addDate(date, DateUtil.fmt_yyyyMMdd, 3, -1);
		qryMap.put("order_date_start", yesterday + " 00:00:00");
		qryMap.put("order_date_end", yesterday + " 23:59:59");
		Map<String, Object> countsMap = pcOrderDao.selectPcOrderCount(qryMap);
		if (null != countsMap) {
			resultMap.put("yesterday_sell_amount", MoneyUtil.formatMoney(countsMap.get("total_fee")));
		}
		// 月销售金额
		qryMap.clear();
		countsMap.clear();
		qryMap.put("stores_id", stores_id);
		qryMap.put("status_in", new String[] { "payed", "delivered", "received" });
		String month = DateUtil.getFormatDate(DateUtil.fmt_yyyyMM);
		qryMap.put("order_date_start", month + "-01 00:00:00");
		qryMap.put("order_date_end", date);
		countsMap = pcOrderDao.selectPcOrderCount(qryMap);
		if (null != countsMap) {
			resultMap.put("month_sell_amount", MoneyUtil.formatMoney(countsMap.get("total_fee")));
		}

		// 总销售金额
		qryMap.clear();
		countsMap.clear();
		qryMap.put("stores_id", stores_id);
		qryMap.put("status_in", new String[] { "payed", "delivered", "received" });
		countsMap = pcOrderDao.selectPcOrderCount(qryMap);
		if (null != countsMap) {
			resultMap.put("totle_amount", MoneyUtil.formatMoney(countsMap.get("total_fee")));
		}

		// 待配送订单数
		qryMap.clear();
		countsMap.clear();
		qryMap.put("stores_id", stores_id);
		qryMap.put("status", "payed");
		countsMap = pcOrderDao.selectPcOrderCount(qryMap);
		if (null != countsMap) {
			resultMap.put("wait_delivery", countsMap.get("count_num") + "");
		}
		result.setResultData(resultMap);
	}

	/***
	 * 运营掌上便利店订单查询
	 */
	@Override
	public void pcOrderListFindForOp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String status = StringUtil.formatStr(paramsMap.get("status"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		String stores_id = StringUtil.formatStr(paramsMap.get("stores_id"));
		if (!StringUtils.isEmpty(stores_id)) {
			List<String> list = StringUtil.str2List(stores_id, ",");
			if (list.size() > 1) {
				paramsMap.remove("stores_id");
				paramsMap.put("stores_id_in", list);
			}
		}
		List<Map<String, Object>> orderList = pcOrderDao.selectPcOrderListForOp(paramsMap);
		Date now = new Date();
		for (Map<String, Object> map : orderList) {
			Date order_date = (Date) map.get("order_date");
			if (Constant.ORDER_PAYED.equals(map.get("status"))) {
				map.put("delay_time", (now.getTime() - order_date.getTime()) / (1000 * 60));
			} else if (Constant.ORDER_DELIVERED.equals(map.get("status"))) {
				if (map.get("delivery_date") != null) {
					Date delivery_date = (Date) map.get("delivery_date");
					map.put("delay_time", (delivery_date.getTime() - order_date.getTime()) / (1000 * 60));
				}
			} else {
				map.put("delay_time", ""); // 延时分钟数
			}
			map.put("order_id", StringUtil.formatStr(map.get("order_id")));
			map.put("order_no", StringUtil.formatStr(map.get("order_no")));
			map.put("item_num", StringUtil.formatStr(map.get("item_num")));
			map.put("total_fee", StringUtil.formatStr(map.get("total_fee")));
			map.put("delivery_address", StringUtil.formatStr(map.get("delivery_address")));
			map.put("contact_tel", StringUtil.formatStr(map.get("contact_tel")));
			map.put("contact_person", StringUtil.formatStr(map.get("contact_person")));
			map.put("logistics", StringUtil.formatStr(map.get("logistics")));
			map.put("status", StringUtil.formatStr(map.get("status")));
			map.put("remark", StringUtil.formatStr(map.get("remark")));
			map.put("item_specification", StringUtil.formatStr(map.get("item_specification")));
			map.put("order_date", DateUtil.date2Str(order_date, DateUtil.fmt_yyyyMMddHHmmss));
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", orderList);
		result.setResultData(resultMap);
	}

	/**
	 * 扣库存
	 * 
	 * @Title: itemStoreSaleQtyDeduction
	 * @param item_store_id
	 * @param sell_qty
	 * @throws BusinessException
	 * @throws Exception
	 * @author tiny
	 */
	private void itemStoreSaleQtyDeduction(String item_store_id, String sell_qty) throws BusinessException, Exception {
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "goods.itemStoreSaleQtyDeduction");
		bizParams.put("item_store_id", item_store_id);
		bizParams.put("sell_qty", sell_qty);
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
	}

	/**
	 * 还原库存
	 * 
	 * @Title: itemStoreSaleQtyRestore
	 * @param item_store_id
	 * @param restore_qty
	 * @throws BusinessException
	 * @throws Exception
	 * @author tiny
	 */
	private void itemStoreSaleQtyRestore(String item_store_id, String restore_qty) throws BusinessException, Exception {
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "goods.itemStoreSaleQtyRestore");
		bizParams.put("item_store_id", item_store_id);
		bizParams.put("restore_qty", restore_qty);
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
	}

}
