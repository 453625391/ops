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
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.MoneyUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.order.constant.Constant;
import com.meitianhui.order.constant.OrderIDUtil;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.dao.GeOrderDao;
import com.meitianhui.order.dao.OrderDao;
import com.meitianhui.order.dao.PeOrderDao;
import com.meitianhui.order.entity.GeOrder;
import com.meitianhui.order.entity.GeOrderItem;
import com.meitianhui.order.entity.OdSettlement;
import com.meitianhui.order.entity.OdSettlementDetail;
import com.meitianhui.order.entity.PeOrder;
import com.meitianhui.order.entity.PeOrderItem;
import com.meitianhui.order.service.GeOrderService;
import com.meitianhui.order.service.OrderService;
import com.meitianhui.order.service.PeOrderService;

/***
 * 品牌领订单逻辑处理实现接口
 * 
 * @author 丁硕
 * @date 2016年12月28日
 */
@SuppressWarnings("unchecked")
@Service
public class PeOrderServiceImpl implements PeOrderService {

	@Autowired
	private DocUtil docUtil;
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private PeOrderDao peOrderDao;

	@Autowired
	private OrderService orderService;

	@Override
	public void handlePeOrderPayCreateNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "member_type_key", "member_id", "member_info", "desc1", "item_num", "payment_way_key",
						"cash_amount", "gold_amount", "delivery_address", "contact_person", "contact_tel",
						"data_source", "order_item_list", "amount" });
		// 订单实际支付金额
		BigDecimal amount = new BigDecimal(paramsMap.get("amount") + "");
		// 订单支付金额
		BigDecimal cash_amount = new BigDecimal(paramsMap.get("cash_amount") + "");
		// 检测支付金额是否相同
		if (cash_amount.compareTo(amount) > 0) {
			throw new BusinessException(RspCode.ORDER_ERROE, "订单金额被篡改");
		}

		Date date = new Date();
		PeOrder order = new PeOrder();
		BeanConvertUtil.mapToBean(order, paramsMap);
		order.setOrder_id(IDUtil.getUUID());
		order.setOrder_no(OrderIDUtil.getOrderNo());
		order.setCreated_date(date);
		order.setModified_date(date);
		order.setOrder_date(date);
		order.setSettle_status(Constant.ORDER_SETTLE_PENDING);
		// 解析商品信息
		List<Map<String, Object>> store_item_list = FastJsonUtil.jsonToList((String) paramsMap.get("order_item_list"));

		if (store_item_list.size() > 1) {
			throw new BusinessException(RspCode.ORDER_ERROE, "品牌领商品不能批量购买");
		}
		Map<String, Object> store_item = store_item_list.get(0);
		ValidateUtil.validateParams(store_item, new String[] { "goods_code", "qty", "cash_paid", "gold_paid" });

		Map<String, Object> goodsDataMap = orderService.psGoodsFindForOrder(null, store_item.get("goods_code") + "");

		/** 检测商品金额是否被篡改 **/
		// 实时商品价格
		BigDecimal goods_discount_price = (BigDecimal) goodsDataMap.get("discount_price");
		// 商品支付金额
		BigDecimal cash_paid = new BigDecimal(store_item.get("cash_paid") + "");
		// 检测商品单价是否变动
		if (goods_discount_price.compareTo(cash_paid) > 0) {
			throw new BusinessException(RspCode.ORDER_ERROE, "商品价格变动,请重新下单");
		}
		if (goods_discount_price.compareTo(cash_amount) > 0) {
			throw new BusinessException(RspCode.ORDER_ERROE, "订单金额被篡改");
		}
		// 检测订单支付金额是等于商品的实际金额
		if (goods_discount_price.compareTo(amount) > 0) {
			throw new BusinessException(RspCode.ORDER_ERROE, "支付金额被篡改");
		}

		PeOrderItem orderItem = new PeOrderItem();
		BeanConvertUtil.mapToBean(orderItem, goodsDataMap);
		BeanConvertUtil.mapToBean(orderItem, store_item);
		orderItem.setOrder_item_id(IDUtil.getUUID());
		orderItem.setOrder_id(order.getOrder_id());
		orderItem.setCreated_date(date);
		orderItem.setModified_date(date);
		peOrderDao.insertPeOrderItem(orderItem);
		// 扣库存
		orderService.psGoodsSaleQtyDeduction(orderItem.getGoods_id(), orderItem.getQty() + "");
		// 扣金币
		Map<String, Object> out_trade_body = new HashMap<String, Object>();
		out_trade_body.put("order_no", order.getOrder_no());
//		out_trade_body.put("gold_amount", order.getGold_amount() + "");
//		out_trade_body.put("cash_amount", order.getCash_amount() + "");
//		orderService.balancePay(paramsMap.get("data_source") + "", order.getMember_id(), Constant.MEMBER_ID_MTH,
//				"ZFFS_08", new BigDecimal(order.getGold_amount()), "DDLX_05", order.getOrder_no(), "品牌领",
//				out_trade_body);

		// 记录订单
		order.setSettle_status(Constant.ORDER_SETTLE_PENDING);
		order.setStatus(Constant.ORDER_PAYED);
		peOrderDao.insertPeOrder(order);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.clear();
		tempMap.put("order_no", order.getOrder_no());
		result.setResultData(tempMap);
	}

	@Override
	public void peOrderDetailFind(Map<String, Object> paramsMap, ResultData result)
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
		List<PeOrder> orderList = peOrderDao.selectPeOrder(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		for (PeOrder order : orderList) {
			Map<String, Object> orderDetailMap = new HashMap<String, Object>();
			orderDetailMap.put("order_id", order.getOrder_id());
			orderDetailMap.put("order_no", order.getOrder_no());
			orderDetailMap.put("order_date", DateUtil.date2Str(order.getOrder_date(), DateUtil.fmt_yyyyMMddHHmmss));
			orderDetailMap.put("member_id", order.getMember_id());
			orderDetailMap.put("member_info", order.getMember_info());
			orderDetailMap.put("desc1", StringUtil.formatStr(order.getDesc1()));
			orderDetailMap.put("item_num", order.getItem_num() + "");
			orderDetailMap.put("payment_way_key", order.getPayment_way_key());
//			orderDetailMap.put("cash_amount", order.getCash_amount() + "");
//			orderDetailMap.put("gold_amount", order.getGold_amount() + "");
			orderDetailMap.put("delivery_address", StringUtil.formatStr(order.getDelivery_address()));
			orderDetailMap.put("contact_person", order.getContact_person());
			orderDetailMap.put("contact_tel", order.getContact_tel());
			orderDetailMap.put("logistics", order.getLogistics());
			orderDetailMap.put("status", order.getStatus());
			orderDetailMap.put("remark", StringUtil.formatStr(order.getRemark()));
			orderDetailMap.put("biz_remark", StringUtil.formatStr(order.getBiz_remark()));
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			tempMap.clear();
			tempMap.put("order_id", order.getOrder_id());
			List<PeOrderItem> orderItemList = peOrderDao.selectPeOrderItem(tempMap);
			for (PeOrderItem orderItem : orderItemList) {
				Map<String, Object> itemDetailMap = new HashMap<String, Object>();
				itemDetailMap.put("item_goods_title", orderItem.getGoods_title());
				itemDetailMap.put("item_goods_pic_info", StringUtil.formatStr(orderItem.getGoods_pic_info()));
//				itemDetailMap.put("item_cash_paid", orderItem.getCash_paid() + "");
//				itemDetailMap.put("item_gold_paid", orderItem.getGold_paid() + "");
//				itemDetailMap.put("item_retail_price", orderItem.getRetail_price() + "");
//				itemDetailMap.put("item_discount_price", orderItem.getDiscount_price() + "");
				itemList.add(itemDetailMap);
				String goods_pic_info = StringUtil.formatStr(StringUtil.formatStr(orderItem.getGoods_pic_info()));
				if (!"".equals(goods_pic_info)) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(goods_pic_info);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
					}
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
	public void peOrderListFindForOp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String status = (String) paramsMap.get("status");
		if (null != status && !"".equals(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		List<Map<String, Object>> orderList = peOrderDao.selectPeOrderListForOp(paramsMap);
		for (Map<String, Object> map : orderList) {
			map.put("order_date", DateUtil.date2Str((Date) map.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			map.put("delivery_address", StringUtil.formatStr(map.get("delivery_address")));
			map.put("logistics", StringUtil.formatStr(map.get("logistics")));
			map.put("remark", StringUtil.formatStr(map.get("remark")));
			map.put("biz_remark", StringUtil.formatStr(map.get("biz_remark")));
			map.put("item_supplier", StringUtil.formatStr(map.get("item_supplier")));
			map.put("goods_code", StringUtil.formatStr(map.get("item_goods_code")));
		}
		resultMap.put("list", orderList);
		result.setResultData(resultMap);
	}

	@Override
	public void handlePeOrderDelivered(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id", "data_source" });
		List<PeOrder> PeOrderList = peOrderDao.selectPeOrder(paramsMap);
		if (PeOrderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		PeOrder order = PeOrderList.get(0);
		if (!order.getStatus().equals(Constant.ORDER_PAYED)) {
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
		}
		// 更新状态
		paramsMap.put("status", Constant.ORDER_DELIVERED);
		paramsMap.put("modified_date", DateUtil.date2Str(order.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = peOrderDao.updatePeOrder(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
	}

	@Override
	public void handlePeOrderReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id", "data_source" });
		List<PeOrder> PeOrderList = peOrderDao.selectPeOrder(paramsMap);
		if (PeOrderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		PeOrder order = PeOrderList.get(0);
		if (!order.getStatus().equals(Constant.ORDER_DELIVERED)) {
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "订单未发货");
		}
		// 更新状态
		paramsMap.put("status", Constant.ORDER_RECEIVED);
		paramsMap.put("modified_date", DateUtil.date2Str(order.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = peOrderDao.updatePeOrder(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
	}

	@Override
	public void peOrderEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "order_id", "biz_remark" });
			List<PeOrder> PeOrderList = peOrderDao.selectPeOrder(paramsMap);
			if (PeOrderList.size() == 0) {
				throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
			}
			// 更新运营备注字段
			paramsMap.put("biz_remark", paramsMap.get("biz_remark"));
			paramsMap.put("order_id", paramsMap.get("order_id"));
			peOrderDao.updatePeOrder(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void peOrderSettlementCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String status = StringUtil.formatStr(paramsMap.get("status"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		// 统计金额和数量
		Map<String, Object> settlementCountMap = peOrderDao.selectPeOrderSettlementCount(paramsMap);
		Map<String, Object> countMap = new HashMap<String, Object>();
		countMap.put("cash_total_fee", "0.00");
		countMap.put("gold_total_fee", "0.00");
		countMap.put("count_num", "0");
		if (null != settlementCountMap) {
			if (null != settlementCountMap.get("cash_total_fee")) {
				countMap.put("cash_total_fee", settlementCountMap.get("cash_total_fee"));
			}
			if (null != settlementCountMap.get("gold_total_fee")) {
				countMap.put("gold_total_fee", settlementCountMap.get("gold_total_fee"));
			}
			countMap.put("count_num", settlementCountMap.get("count_num") + "");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", countMap);
		result.setResultData(map);
	}

	/**
	 * 名品汇订单结算，只按现金结算
	 */
	@Override
	public void handlePeOrderSettlement(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "operator", "order_date" });
		String operator = paramsMap.get("operator") + "";
		Map<String, List<Map<String, Object>>> settlementMap = new HashMap<String, List<Map<String, Object>>>();
		// 查询需要结算的订单列表
		paramsMap.put("settle_status", Constant.ORDER_SETTLE_PENDING);
		paramsMap.put("status", Constant.ORDER_RECEIVED);
		List<Map<String, Object>> settlementList = peOrderDao.selectPeOrderDetailForSettlement(paramsMap);

		// 商品出结算清单
		for (Map<String, Object> map : settlementList) {
			String supplier_id = map.get("item_supplier_id") + "";
			if (null != settlementMap.get(supplier_id)) {
				List<Map<String, Object>> orderList = settlementMap.get(supplier_id);
				orderList.add(map);
			} else {
				List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
				orderList.add(map);
				settlementMap.put(supplier_id, orderList);
			}
		}
		int succ_count = 0;
		// 按供应商进行订单结算
		Map<String, Object> bizParams = new HashMap<String, Object>();
		for (String supplier_id : settlementMap.keySet()) {
			// 结算订单
			OdSettlement settlement = new OdSettlement();
			// 结算订单列表
			List<OdSettlementDetail> detailList = new ArrayList<OdSettlementDetail>();
			settlement.setSettlement_id(IDUtil.getTimestamp(7));
			Date date = new Date();
			String settlementAmount = "0"; // 结算金额
			// String orderAmount = "0"; //订单金额
			String supplier = "";
			Integer order_qty = 0;
			List<Map<String, Object>> tempList = settlementMap.get(supplier_id);
			// 生成结算清单
			for (Map<String, Object> tempMap : tempList) {
				// 以结算的金额为主
				String qty = tempMap.get("item_qty") + "";
				String total_fee = MoneyUtil.moneyMul(tempMap.get("item_settled_price") + "", qty);
				settlementAmount = MoneyUtil.moneyAdd(settlementAmount, total_fee); // 结算金额
				// orderAmount = MoneyUtil.moneyAdd(orderAmount,
				// tempMap.get("item_total_fee") + ""); //订单金额
				supplier = StringUtil.formatStr(tempMap.get("item_supplier"));
				OdSettlementDetail detail = new OdSettlementDetail();
				detail.setData_source(Constant.ORDER_TYPE_GOLD_EXCHANGE);
				detail.setDetail_id(IDUtil.getUUID());
				detail.setSettlement_id(settlement.getSettlement_id());
				detail.setOrder_id(tempMap.get("order_id") + "");
				detail.setOrder_no(tempMap.get("order_no") + "");
				detail.setGoods_id(tempMap.get("item_goods_id") + "");
				detail.setGoods_code(StringUtil.formatStr(tempMap.get("item_goods_code")));
				detail.setGoods_title(StringUtil.formatStr(tempMap.get("item_goods_title")));
				detail.setSupplier_id(supplier_id);
				detail.setSupplier(supplier);
				detail.setQty(Integer.parseInt(tempMap.get("item_qty") + ""));
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
				int updateFlag = peOrderDao.updatePeOrder(bizParams);
				if (updateFlag == 0) {
					throw new BusinessException(RspCode.ORDER_PROCESSING, "请刷新订单后再进行结算操作");
				}
				order_qty += 1;
			}
			settlement.setData_source(Constant.ORDER_TYPE_GOLD_EXCHANGE);
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
			// 计算成功数
			succ_count = succ_count + detailList.size();
			orderDao.insertOdSettlementDetailList(detailList);
			settlement.setOrder_qty(order_qty);
			orderDao.insertOdSettlement(settlement);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("fail_count", settlementList.size() - succ_count);
		resultMap.put("succ_count", succ_count);
		result.setResultData(resultMap);
	}

}
