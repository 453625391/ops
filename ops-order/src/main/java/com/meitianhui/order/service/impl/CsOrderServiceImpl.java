package com.meitianhui.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.MoneyUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.order.constant.Constant;
import com.meitianhui.order.constant.OrderIDUtil;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.dao.CsOrderDao;
import com.meitianhui.order.dao.OrderDao;
import com.meitianhui.order.entity.CsOrder;
import com.meitianhui.order.entity.OdSettlement;
import com.meitianhui.order.entity.OdSettlementDetail;
import com.meitianhui.order.service.CsOrderService;
import com.meitianhui.order.service.MobileRechargeService;

/**
 * 增值服务接口实现类
 * 
 * @author 丁硕
 * @date 2016年12月14日
 */
@SuppressWarnings("unchecked")
@Service
public class CsOrderServiceImpl implements CsOrderService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private CsOrderDao csOrderDao;
	
	@Autowired
	private MobileRechargeService mobileRechargeService;

	@Override
	public void phoneBillOrderCreateNotity(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil
				.validateParams(paramsMap,
						new String[] { "member_id", "member_type_key", "member_info", "goods_id", "contact_tel",
								"sale_fee", "discount_fee", "total_fee", "payment_way_key", "goods_discount_price",
								"goods_market_price" });
		Date date = new Date();
		CsOrder order = new CsOrder();
		BeanConvertUtil.mapToBean(order, paramsMap);
		order.setOrder_id(IDUtil.getUUID());
		order.setOrder_no(OrderIDUtil.getOrderNo());
		order.setModified_date(date);
		order.setCreated_date(date);
		order.setOrder_date(date);
		order.setOrder_type("phone_bill");
		order.setStatus(Constant.ORDER_PAID);
		order.setSettle_status(Constant.ORDER_SETTLE_PENDING);
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
		reqParams.put("service", "goods.psGoodsFindForOrder");
		bizParams.put("goods_id", paramsMap.get("goods_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(goods_service_url, reqParams);
		resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
			order.setGoods_code(StringUtil.formatStr(dataMap.get("goods_code")));
			order.setGoods_title(StringUtil.formatStr(dataMap.get("title")));
			order.setGoods_pic_info(StringUtil.formatStr(dataMap.get("pic_info")));
			order.setGoods_unit(StringUtil.formatStr(dataMap.get("goods_unit")));
			order.setSupplier_id(StringUtil.formatStr(dataMap.get("supplier_id")));
			order.setSupplier(StringUtil.formatStr(dataMap.get("supplier")));
			order.setSettled_price(new BigDecimal(dataMap.get("settled_price") + ""));
			order.setService_fee(new BigDecimal(dataMap.get("settled_price") + ""));
			order.setQty(1);
		}
		csOrderDao.insertCsOrder(order);
		// 调用话费充值接口
		Integer price = new BigDecimal(paramsMap.get("goods_market_price") + "").intValue();
		String clientOrderId = order.getOrder_no();
		String mobile = order.getContact_tel();
		mobileRechargeService.mobileRecharge(mobile, price, clientOrderId);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_no", order.getOrder_no());
		result.setResultData(paramMap);
	}

	@Override
	public void handlePhoneBillOrderFinishNotity(Map<String, Object> paramsMap)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_no", "status" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_no", paramsMap.get("order_no"));
		CsOrder csOrder = csOrderDao.selectCsOrderDetail(tempMap);
		if (csOrder == null) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		if (!csOrder.getStatus().equals(Constant.ORDER_PAID)) {
			throw new BusinessException(RspCode.ORDER_ERROE, "订单已完成");
		}
		String status = paramsMap.get("status") + "";
		if (status.equals("0")) {
			// 更新状态
			tempMap.clear();
			tempMap.put("status", Constant.ORDER_CLOSED);
			tempMap.put("order_id", csOrder.getOrder_id());
			tempMap.put("modified_date", DateUtil.date2Str(csOrder.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
			csOrderDao.updateCsOrder(tempMap);
		} else {
			// 充值失败,进行退款操作
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			reqParams.put("service", "finance.orderRefund");
			bizParams.put("data_source", "SJLY_03");
			bizParams.put("buyer_id", csOrder.getMember_id());
			bizParams.put("seller_id", Constant.MEMBER_ID_MTH);
			bizParams.put("payment_way_key", "ZFFS_05");
			bizParams.put("detail", "充值失败退款");
			bizParams.put("amount", csOrder.getTotal_fee() + "");
			bizParams.put("out_trade_no", csOrder.getOrder_no());
			
			
			Map<String, Object> out_trade_body = new HashMap<String, Object>();
			out_trade_body.put("order_id", csOrder.getOrder_id());
			out_trade_body.put("goods_id", csOrder.getGoods_id());
			out_trade_body.put("goods_title", csOrder.getGoods_title());
			out_trade_body.put("member_id", csOrder.getMember_id());
			out_trade_body.put("member_type_key", csOrder.getMember_type_key());
			out_trade_body.put("total_fee", csOrder.getTotal_fee() + "");
			bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			// 更新状态
			tempMap.clear();
			tempMap.put("status", Constant.ORDER_CANCELLED);
			tempMap.put("order_id", csOrder.getOrder_id());
			tempMap.put("remark", StringUtil.formatStr(paramsMap.get("errorDesc")));
			tempMap.put("modified_date", DateUtil.date2Str(csOrder.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
			csOrderDao.updateCsOrder(tempMap);
		}
	}

	/***
	 * app订单列表查询
	 */
	@Override
	public void phoneBillOrderListForAppFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
		paramsMap.put("order_type ", "phone_bill");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<CsOrder> orderList = csOrderDao.selectPhoneBillOrderForAppList(paramsMap);
		for (CsOrder order : orderList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", order.getOrder_id());
			map.put("order_no", order.getOrder_no());
			map.put("contact_tel", order.getContact_tel());
			map.put("order_date", DateUtil.date2Str(order.getOrder_date(), DateUtil.fmt_yyyyMMddHHmmss));
			map.put("goods_title", order.getGoods_title());
			map.put("total_fee", order.getTotal_fee() + "");
			map.put("status", order.getStatus());
			resultList.add(map);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		result.setResultData(resultMap);
	}

	/***
	 * 运营订单列表查询
	 */
	@Override
	public void csOrderListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String status = StringUtil.formatStr(paramsMap.get("status"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		List<CsOrder> orderList = csOrderDao.selectCsOrderForOpList(paramsMap);
		for (CsOrder order : orderList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", order.getOrder_id());
			map.put("order_no", order.getOrder_no());
			map.put("order_type", order.getOrder_type());
			map.put("order_date", DateUtil.date2Str(order.getOrder_date(), DateUtil.fmt_yyyyMMddHHmmss));
			map.put("goods_code", order.getGoods_code());
			map.put("goods_title", order.getGoods_title());
			map.put("contact_tel", order.getContact_tel());
			map.put("member_type_key", order.getMember_type_key());
			String member_info = order.getMember_info();
			Map<String, Object> memberInfoMap = FastJsonUtil.jsonToMap(member_info);
			map.put("member_mobile", memberInfoMap.get("mobile"));
			map.put("member_name", memberInfoMap.get("name"));
			map.put("payment_way_key", order.getPayment_way_key());
			map.put("sale_fee", order.getSale_fee() + "");
			map.put("total_fee", order.getTotal_fee() + "");
			map.put("settled_price", order.getSettled_price() + "");
			map.put("qty", order.getQty() + "");
			map.put("supplier_id", order.getSupplier_id());
			map.put("supplier", order.getSupplier());
			map.put("status", order.getStatus());
			map.put("settle_status", order.getSettle_status());
			map.put("remark", StringUtil.formatStr(order.getRemark()));
			resultList.add(map);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		result.setResultData(resultMap);
	}

	/***
	 * 增值订单结算统计
	 */
	@Override
	public void csOrderSettlementCount(Map<String, Object> paramsMap, ResultData result)
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
		Map<String, Object> settlementCountMap = csOrderDao.selectCsOrderSettlementCount(paramsMap);
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

	/***
	 * 增值订单结算
	 */
	@Override
	public void handleCsOrderSettlement(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {

		ValidateUtil.validateParams(paramsMap, new String[] { "operator", "order_date" });
		String operator = paramsMap.get("operator") + "";
		Map<String, List<Map<String, Object>>> settlementMap = new HashMap<String, List<Map<String, Object>>>();
		// 查询需要结算的订单列表
		paramsMap.put("settle_status", Constant.ORDER_SETTLE_PENDING);
		paramsMap.put("status", Constant.ORDER_CLOSED);
		List<Map<String, Object>> settlementList = csOrderDao.selectCsOrderForSettlement(paramsMap);
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
		for (String supplier_id : settlementMap.keySet()) {
			Set<String> orderNumSet = new HashSet<String>(); // 按订单计算订单总数
			// 结算订单
			OdSettlement settlement = new OdSettlement();
			// 结算订单列表
			List<OdSettlementDetail> detailList = new ArrayList<OdSettlementDetail>();
			settlement.setSettlement_id(IDUtil.getTimestamp(7));
			Date date = new Date();
			String settlementAmount = "0"; // 结算金额
			// String orderAmount = "0"; //订单金额
			String supplier = "";
			List<Map<String, Object>> tempList = settlementMap.get(supplier_id);
			// 生成结算清单
			for (Map<String, Object> tempMap : tempList) {
				// 以订单金额为主
				String qty = tempMap.get("qty") + "";
				String total_fee = MoneyUtil.moneyMul(tempMap.get("settled_price") + "", qty);
				settlementAmount = MoneyUtil.moneyAdd(settlementAmount, total_fee);
				// orderAmount = MoneyUtil.moneyAdd(orderAmount, tempMap.get("total_fee") + "");
				supplier = StringUtil.formatStr(tempMap.get("supplier"));
				OdSettlementDetail detail = new OdSettlementDetail();
				detail.setData_source(Constant.ORDER_TYPE_APPRECIATION);
				detail.setDetail_id(IDUtil.getUUID());
				detail.setSettlement_id(settlement.getSettlement_id());
				detail.setOrder_id(tempMap.get("order_id") + "");
				detail.setOrder_no(tempMap.get("order_no") + "");
				detail.setGoods_id(tempMap.get("goods_id") + "");
				detail.setGoods_code(StringUtil.formatStr(tempMap.get("goods_code")));
				detail.setGoods_title(StringUtil.formatStr(tempMap.get("goods_title")));
				detail.setSupplier_id(supplier_id);
				detail.setSupplier(supplier);
				detail.setQty(Integer.parseInt(tempMap.get("qty") + ""));
				detail.setTotal_fee(new BigDecimal(total_fee));
				detail.setCreated_date(date);
				detail.setModified_date(date);
				detail.setStatus(Constant.ORDER_SETTLE_SETTLED);
				detailList.add(detail);
				bizParams.clear();
				bizParams.put("order_id", detail.getOrder_id());
				bizParams.put("settle_status", Constant.ORDER_SETTLE_SETTLED);
				bizParams.put("modified_date", DateUtil.date2Str((Date) tempMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
				int updateFlag = csOrderDao.updateCsOrder(bizParams);
				if (updateFlag == 0) {
					throw new BusinessException(RspCode.ORDER_PROCESSING, "请刷新订单后再进行结算操作");
				}
				orderNumSet.add(detail.getOrder_id());
			}
			settlement.setData_source(Constant.ORDER_TYPE_APPRECIATION);
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
			settlement.setOrder_qty(orderNumSet.size());
			succ_count += orderNumSet.size();
			orderDao.insertOdSettlement(settlement);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("fail_count", settlementList.size() - succ_count);
		resultMap.put("succ_count", succ_count);
		result.setResultData(resultMap);
	}
	
}
