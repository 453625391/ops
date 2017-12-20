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
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.order.constant.Constant;
import com.meitianhui.order.constant.OrderIDUtil;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.controller.OrderController;
import com.meitianhui.order.dao.OrderDao;
import com.meitianhui.order.dao.PsOrderDao;
import com.meitianhui.order.entity.OdSettlement;
import com.meitianhui.order.entity.OdSettlementDetail;
import com.meitianhui.order.entity.PsOrder;
import com.meitianhui.order.entity.PsOrderExtra;
import com.meitianhui.order.entity.PsOrderItem;
import com.meitianhui.order.entity.PsSubOrder;
import com.meitianhui.order.service.OrderService;
import com.meitianhui.order.service.PsOrderService;

/**
 * 我要批
 * 
 * @author 丁硕
 * @date 2016年12月14日
 */
@SuppressWarnings("unchecked")
@Service
public class PsOrderServiceImpl implements PsOrderService {
	private static final Logger logger = Logger.getLogger(PsOrderService.class);

	@Autowired
	public RedisUtil redisUtil;
	@Autowired
	private DocUtil docUtil;

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private PsOrderDao psOrderDao;

	@Autowired
	private OrderService orderService;

	/***
	 * 运营我要批列表查询
	 */
	@Override
	public void selectPsOrderForOp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String status = StringUtil.formatStr(paramsMap.get("status"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		List<Map<String, Object>> orderList = psOrderDao.selectPsOrderForOp(paramsMap);
		for (Map<String, Object> map : orderList) {
			map.put("order_id", StringUtil.formatStr(map.get("order_id")));
			map.put("order_no", StringUtil.formatStr(map.get("order_no")));
			map.put("order_date", DateUtil.date2Str((Date) map.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			map.put("item_num", StringUtil.formatStr(map.get("item_num")));
			map.put("total_fee", StringUtil.formatStr(map.get("total_fee")));
			map.put("delivery_address", StringUtil.formatStr(map.get("delivery_address")));
			map.put("contact_tel", StringUtil.formatStr(map.get("contact_tel")));
			map.put("contact_person", StringUtil.formatStr(map.get("contact_person")));
			map.put("logistics", StringUtil.formatStr(map.get("logistics")));
			map.put("remark", StringUtil.formatStr(map.get("remark")));
			map.put("biz_remark", StringUtil.formatStr(map.get("biz_remark")));
			map.put("goods_qty", StringUtil.formatStr(map.get("goods_qty")));
			map.put("goods_sale_price", StringUtil.formatStr(map.get("goods_sale_price")));
			map.put("goods_manufacturer", StringUtil.formatStr(map.get("goods_manufacturer")));
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", orderList);
		result.setResultData(resultMap);
	}

	@Override
	public void wypOrderCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "order_list" });
		String member_id = StringUtil.formatStr(paramsMap.get("member_id"));
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Map<String, Object> storeInfo = null;
		// 检测缓存中是否有数据
		String key = "[wypOrderCreate_storesBaseInfoFind]_" + member_id;
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			storeInfo = (Map<String, Object>) obj;
		} else {
			reqParams.put("service", "stores.stores.storesBaseInfoFind");
			bizParams.put("stores_id", member_id);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(OrderServiceImpl.MEMBER_SERVICE_URL, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			storeInfo = (Map<String, Object>) resultMap.get("data");
			// 缓存数据到reds中
			redisUtil.setObj(key, storeInfo, 600);
		}

		String stores_name = StringUtil.formatStr(storeInfo.get("stores_name"));
		String contact_person = StringUtil.formatStr(storeInfo.get("contact_person"));
		String contact_tel = StringUtil.formatStr(storeInfo.get("contact_tel"));
		String area_desc = StringUtil.formatStr(storeInfo.get("area_desc"));
		String address = StringUtil.formatStr(storeInfo.get("address"));
		String delivery_address = area_desc.replace("中国", "").replace(",", "") + address;
		// 组装会员信息信息
		Map<String, Object> memberInfo = new HashMap<String, Object>();
		memberInfo.put("stores_name", stores_name);
		memberInfo.put("contact_person", contact_person);
		memberInfo.put("contact_tel", contact_tel);
		// 门店我要批订单
		List<Map<String, Object>> order_list = FastJsonUtil.jsonToList((String) paramsMap.get("order_list"));
		for (Map<String, Object> orderMap : order_list) {
			ValidateUtil.validateParams(orderMap,
					new String[] { "desc1", "item_num", "total_fee", "delivery_fee", "discount_fee", "sale_fee",
							"supplier_id", "supplier", "warehouse_id", "warehouse", "wyp_goods_list" });
			String supplier_id = orderMap.get("supplier_id") + "";
			String supplier = orderMap.get("supplier") + "";
			orderMap.put("member_info", FastJsonUtil.toJson(memberInfo));
			Date date = new Date();
			PsOrder order = new PsOrder();
			BeanConvertUtil.mapToBean(order, orderMap);
			order.setOrder_id(IDUtil.getUUID());
			order.setMember_id(member_id);
			order.setMember_type_key(Constant.MEMBER_TYPE_STORES);
			order.setContact_tel(contact_tel);
			order.setContact_person(contact_person);
			order.setDelivery_address(delivery_address);
			order.setOrder_no(OrderIDUtil.getOrderNo());
			order.setOrder_type(Constant.PS_ORDER_TYPE_NORMAL);
			order.setStatus(Constant.ORDER_NONPAID);
			order.setSettle_status(Constant.ORDER_SETTLE_PENDING);
			order.setOrder_date(date);
			order.setLoaded_code(IDUtil.random(4));
			order.setCreated_date(date);
			order.setModified_date(date);
			// 累计商品数量
			Integer item_num = 0;
			// 解析商品信息
			List<Map<String, Object>> ps_goods_list = FastJsonUtil.jsonToList((String) orderMap.get("wyp_goods_list"));
			for (Map<String, Object> wypGoodsMap : ps_goods_list) {
				ValidateUtil.validateParams(wypGoodsMap, new String[] { "goods_id", "goods_title", "goods_stock_id",
						"sku_desc1", "qty", "sale_price", "discount_fee", "total_fee" });
				PsOrderItem orderItem = new PsOrderItem();
				BeanConvertUtil.mapToBean(orderItem, wypGoodsMap);
				orderItem.setOrder_item_id(IDUtil.getUUID());
				orderItem.setOrder_id(order.getOrder_id());
				orderItem.setCreated_date(date);
				orderItem.setModified_date(date);
				String sku_desc1 = StringUtil.formatStr(wypGoodsMap.get("sku_desc1"));
				if (sku_desc1.length() > 200) {
					sku_desc1 = sku_desc1.substring(0, 199);
				}
				orderItem.setSpecification(sku_desc1);
				orderItem.setSupplier_id(supplier_id);

				Map<String, Object> goodsDataMap = orderService.psGoodsFindForOrder(orderItem.getGoods_id(), null);
				String status = StringUtil.formatStr(goodsDataMap.get("status"));
				if (!status.equals("on_shelf")) {
					throw new BusinessException(RspCode.ORDER_ERROE, "商品已下架");
				}
				// 验证库存
				reqParams.clear();
				bizParams.clear();
				resultMap.clear();
				reqParams.put("service", "goods.wypGoodsSaleQtyValidate");
				List<Map<String, Object>> goods_list = new ArrayList<Map<String, Object>>();
				Map<String, Object> goodsMap = new HashMap<String, Object>();
				goodsMap.put("goods_id", orderItem.getGoods_id());
				goodsMap.put("goods_stock_id", orderItem.getGoods_stock_id());
				goodsMap.put("goods_title", orderItem.getGoods_title());
				goodsMap.put("sell_qty", orderItem.getQty() + "");
				goods_list.add(goodsMap);
				bizParams.put("goods_list", FastJsonUtil.toJson(goods_list));
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException(resultMap.get("error_code") + "", resultMap.get("error_msg") + "");
				}
				// 设置订单数商品属性

				orderItem.setGoods_code(StringUtil.formatStr(goodsDataMap.get("goods_code")));
				orderItem.setGoods_desc1(StringUtil.formatStr(goodsDataMap.get("desc1")));
				orderItem.setGoods_title(StringUtil.formatStr(goodsDataMap.get("title")));
				orderItem.setGoods_pic_detail_info(StringUtil.formatStr(goodsDataMap.get("pic_detail_info")));
				orderItem.setContact_person(StringUtil.formatStr(goodsDataMap.get("contact_person")));
				orderItem.setContact_tel(StringUtil.formatStr(goodsDataMap.get("contact_tel")));
				orderItem.setGoods_unit(StringUtil.formatStr(goodsDataMap.get("goods_unit")));
				orderItem.setManufacturer(StringUtil.formatStr(goodsDataMap.get("manufacturer")));
				orderItem.setSettled_price(new BigDecimal(goodsDataMap.get("settled_price") + ""));
				orderItem.setService_fee(new BigDecimal(goodsDataMap.get("service_fee") + ""));
				String pic_info = StringUtil.formatStr(goodsDataMap.get("pic_info"));
				// 只取商品的第一张图片
				List<Map<String, Object>> goods_pic_info = FastJsonUtil.jsonToList(pic_info);
				Map<String, Object> picMap = goods_pic_info.get(0);
				goods_pic_info.clear();
				goods_pic_info.add(picMap);
				orderItem.setGoods_pic_info(FastJsonUtil.toJson(goods_pic_info));
				// 设置供应商信息
				orderItem.setSupplier_id(supplier_id);
				orderItem.setSupplier(supplier);
				item_num += orderItem.getQty();
				psOrderDao.insertPsOrderItem(orderItem);
			}
			order.setItem_num(item_num);
			psOrderDao.insertPsOrder(order);
		}
		reqParams.clear();
		bizParams.clear();
		reqParams = null;
		bizParams = null;
	}

	@Override
	public void wypOrderPayInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id" });
		// 查询订单信息
		paramsMap.put("status", Constant.ORDER_NONPAID);
		List<PsOrder> orderList = psOrderDao.selectPsOrder(paramsMap);
		if (orderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, "操作失败,请刷新后重试");
		}

		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		String order_id = orderList.get(0).getOrder_id();
		bizParams.put("order_id", order_id);
		List<Map<String, Object>> goods_list = new ArrayList<Map<String, Object>>();
		List<PsOrderItem> orderItemList = psOrderDao.selectWypOrderItem(bizParams);
		// 批量进行商品库存验证
		for (PsOrderItem orderItem : orderItemList) {
			Map<String, Object> goodsMap = new HashMap<String, Object>();
			goodsMap.put("goods_id", orderItem.getGoods_id());
			goodsMap.put("goods_stock_id", orderItem.getGoods_stock_id());
			goodsMap.put("goods_title", orderItem.getGoods_title());
			goodsMap.put("sell_qty", orderItem.getQty() + "");
			goods_list.add(goodsMap);
		}

		// 验证商品库存
		bizParams.clear();
		reqParams.put("service", "goods.wypGoodsSaleQtyValidate");
		bizParams.put("goods_list", FastJsonUtil.toJson(goods_list));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException(resultMap.get("error_code") + "", resultMap.get("error_msg") + "");
		}
		bizParams.clear();
		bizParams.put("order_id", orderList.get(0).getOrder_id());
		bizParams.put("order_no", orderList.get(0).getOrder_no());
		bizParams.put("total_fee", orderList.get(0).getTotal_fee() + "");
		bizParams.put("member_id", orderList.get(0).getMember_id() + "");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pay_info", bizParams);
		result.setResultData(paramMap);
		reqParams.clear();
		reqParams = null;
	}

	@Override
	public void handleWypOrderPayNotity(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id", "payment_way_key" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", paramsMap.get("order_id"));
		tempMap.put("status", Constant.ORDER_NONPAID);
		List<PsOrder> orderList = psOrderDao.selectPsOrder(tempMap);
		if (orderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		tempMap.clear();
		tempMap.put("order_id", paramsMap.get("order_id"));
		List<PsOrderItem> orderItemList = psOrderDao.selectWypOrderItem(tempMap);
		List<Map<String, Object>> goods_list = new ArrayList<Map<String, Object>>();
		for (PsOrderItem orderItem : orderItemList) {
			// 批量进行商品库存验证
			Map<String, Object> goodsMap = new HashMap<String, Object>();
			goodsMap.put("goods_id", orderItem.getGoods_id());
			goodsMap.put("goods_title", orderItem.getGoods_title());
			goodsMap.put("goods_stock_id", orderItem.getGoods_stock_id());
			goodsMap.put("sell_qty", orderItem.getQty() + "");
			goods_list.add(goodsMap);
		}
		// 更新订单状态
		tempMap.clear();
		tempMap.put("order_id", paramsMap.get("order_id"));
		tempMap.put("status", Constant.ORDER_PAYED);
		tempMap.put("payment_way_key", paramsMap.get("payment_way_key"));
		tempMap.put("modified_date",
				DateUtil.date2Str(orderList.get(0).getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = psOrderDao.updatePsOrder(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
		// 扣减商品库存
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		reqParams.put("service", "goods.wypGoodsSaleQtyDeduction");
		bizParams.put("goods_list", FastJsonUtil.toJson(goods_list));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
		resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		reqParams.clear();
		bizParams.clear();
		reqParams = null;
		bizParams = null;
	}

	@Override
	public void psOrderEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id" });
		if (paramsMap.size() == 1) {
			throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE,
					RspCode.MSG.get(RspCode.SYSTEM_NO_PARAMS_UPDATE));
		}
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", paramsMap.get("order_id"));
		List<PsOrder> orderList = psOrderDao.selectPsOrder(tempMap);
		if (orderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		tempMap.put("biz_remark", paramsMap.get("biz_remark"));
		psOrderDao.updatePsOrder(paramsMap);
		// 如果是发货,需要发送短信
		String status = StringUtil.formatStr(paramsMap.get("status"));
		if (!status.equals("") && status.equals(Constant.ORDER_DELIVERED)) {
			String mobile = orderList.get(0).getContact_tel();
			String logistics = paramsMap.get("logistics") + "";
			Map<String, Object> logisticsMap = FastJsonUtil.jsonToMap(logistics);
			String company = logisticsMap.get("company") + "";
			String number = logisticsMap.get("number") + "";
			String msg = "您在【我要批】购买的商品【" + orderList.get(0).getDesc1() + "】已经发货，物流信息【" + company + ":" + number
					+ "】，请确认收货，谢谢合作。";
			// 发送短信
			OrderServiceImpl.sendMsg(mobile, msg);
			// 发送通知
			OrderServiceImpl.appMsgNotify(orderList.get(0).getMember_id(), msg);

		}
	}

	@Override
	public void psOrderStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id", "status" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", paramsMap.get("order_id"));
		List<PsOrder> psOrderList = psOrderDao.selectPsOrder(tempMap);
		if (psOrderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		psOrderDao.updatePsOrder(paramsMap);
	}

	@Override
	public void handlePsOrderDelivered(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id", "logistics" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", paramsMap.get("order_id"));
		List<PsOrder> orderList = psOrderDao.selectPsOrder(tempMap);
		if (orderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		PsOrder order = orderList.get(0);
		if (!order.getStatus().equals(Constant.ORDER_PAYED)) {
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
		}
		paramsMap.put("modified_date", DateUtil.date2Str(order.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		paramsMap.put("status", Constant.ORDER_DELIVERED);
		int updateFlag = psOrderDao.updatePsOrder(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
		String mobile = orderList.get(0).getContact_tel();
		String logistics = paramsMap.get("logistics") + "";
		Map<String, Object> logisticsMap = FastJsonUtil.jsonToMap(logistics);
		String company = logisticsMap.get("company") + "";
		String number = logisticsMap.get("number") + "";
		String msg = "您在【我要批】购买的商品【" + orderList.get(0).getDesc1() + "】已经发货，物流信息【" + company + ":" + number
				+ "】，请确认收货，谢谢合作。";
		// 发送短信
		OrderServiceImpl.sendMsg(mobile, msg);
		// 发送通知
		OrderServiceImpl.appMsgNotify(orderList.get(0).getMember_id(), msg);

	}

	@Override
	public void handlePsOrderReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id" });
		String order_id = paramsMap.get("order_id") + "";
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", order_id);
		List<PsOrder> orderList = psOrderDao.selectPsOrder(tempMap);
		if (orderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		PsOrder order = orderList.get(0);
		if (!order.getStatus().equals(Constant.ORDER_DELIVERED)) {
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
		}
		tempMap.clear();
		tempMap.put("order_id", order_id);
		tempMap.put("modified_date", DateUtil.date2Str(order.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		tempMap.put("status", Constant.ORDER_RECEIVED);
		int updateFlag = psOrderDao.updatePsOrder(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
		String total_fee = order.getTotal_fee() + "";
		String member_id = order.getMember_id();
		// 店东获取赠送的金币
		Integer gold_value = new BigDecimal(total_fee).intValue();
		if (gold_value > 0) {
			Map<String, Object> out_trade_body = new HashMap<String, Object>();
			out_trade_body.put("order_no", order.getOrder_no());
			out_trade_body.put("total_fee", total_fee);
			orderService.orderReward("SJLY_03", member_id, Constant.MEMBER_ID_MTH, "ZFFS_08",
					new BigDecimal(gold_value), "DDLX_01", order.getOrder_no(), "我要批", out_trade_body);
		}
		String order_type = order.getOrder_type();
		if (order_type.equals("normal")) {
			String store_id = order.getMember_id();
			tempMap.clear();
			tempMap.put("order_id", order_id);
			List<PsOrderItem> orderItemList = psOrderDao.selectWypOrderItem(tempMap);
			for (PsOrderItem orderItem : orderItemList) {
				psGoodsImport(store_id, orderItem.getGoods_code(), orderItem.getQty(), "我要批商品导入", "woyaopi");
			}
		} else if (order_type.equals("activity")) {
			// 修改团购预售子订单为已到货
			tempMap.clear();
			tempMap.put("parent_order_id", order_id);
			tempMap.put("param_status", Constant.ORDER_DELIVERED);
			tempMap.put("status", Constant.ORDER_ARRIVED);
			psOrderDao.updatePsSubOrder(tempMap);
		}
	}

	@Override
	public void handlePsOrderCancelled(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "data_source", "order_id", "remark" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", paramsMap.get("order_id"));
		List<PsOrder> psOrderList = psOrderDao.selectPsOrder(tempMap);
		if (psOrderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		PsOrder order = psOrderList.get(0);
		String order_type = order.getOrder_type();
		if (order_type.equals(Constant.PS_ORDER_TYPE_ACTIVITY)) {
			// 团购预售订单
			if (!order.getStatus().equals(Constant.ORDER_ACTIVING)) {
				throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
			}
		} else {
			// 正常订单
			if (!order.getStatus().equals(Constant.ORDER_NONPAID)) {
				throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
			}
		}
		// 更新主订单状态
		tempMap.clear();
		tempMap.put("order_id", order.getOrder_id());
		tempMap.put("status", Constant.ORDER_CANCELLED);
		tempMap.put("remark", paramsMap.get("remark"));
		tempMap.put("modified_date", DateUtil.date2Str(order.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = psOrderDao.updatePsOrder(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
		// 如果是团购订单
		if (order_type.equals(Constant.PS_ORDER_TYPE_ACTIVITY)) {
			psSubOrderListRefund(paramsMap.get("data_source") + "", order.getOrder_id(), Constant.ORDER_PAID, "活动取消");
		}
	}

	@Override
	public void handlePsOrderRefund(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "data_source", "order_id", "amount", "remark" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", paramsMap.get("order_id"));
		List<PsOrder> psOrderList = psOrderDao.selectPsOrder(tempMap);
		if (psOrderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		PsOrder order = psOrderList.get(0);
		if (!order.getStatus().equals(Constant.ORDER_PAYED)) {
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
		}
		// 更新订单状态
		tempMap.clear();
		tempMap.put("order_id", order.getOrder_id());
		tempMap.put("status", Constant.ORDER_CANCELLED);
		tempMap.put("remark", paramsMap.get("remark"));
		tempMap.put("modified_date", DateUtil.date2Str(order.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = psOrderDao.updatePsOrder(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 如果是团购预售的订单,不需要退店东钱
		if (order.getOrder_type().equals("activity")) {
			psSubOrderListRefund(paramsMap.get("data_source") + "", order.getOrder_id(), Constant.ORDER_DELIVERED,
					"活动取消");
		} else {
			// 正常我要批订单,则对店东进行退款
			if (null != order.getPayment_way_key()) {
				// 如果支付方式不为货到付款,调用订单退款交易
				Map<String, Object> out_trade_body = new HashMap<String, Object>();
				out_trade_body.put("order_no", order.getOrder_no());
				out_trade_body.put("total_fee", order.getTotal_fee());
				out_trade_body.put("amount", paramsMap.get("amount"));
				orderService.orderRefund(paramsMap.get("data_source") + "", order.getMember_id(),
						Constant.MEMBER_ID_MTH, order.getPayment_way_key(),
						new BigDecimal(paramsMap.get("amount") + ""), "DDLX_01", order.getOrder_no(), "我要批退款",
						out_trade_body);
			}
		}
		// 还原库存
		tempMap.clear();
		tempMap.put("order_id", order.getOrder_id());
		List<PsOrderItem> orderItemList = psOrderDao.selectWypOrderItem(tempMap);
		if (psOrderList.size() == 0) {
			throw new BusinessException(RspCode.GOODS_NOT_EXIST, RspCode.MSG.get(RspCode.GOODS_NOT_EXIST));
		}
		List<Map<String, Object>> goods_list = new ArrayList<Map<String, Object>>();
		for (PsOrderItem orderItem : orderItemList) {
			Map<String, Object> goodsMap = new HashMap<String, Object>();
			goodsMap.put("goods_id", orderItem.getGoods_id());
			goodsMap.put("goods_stock_id", orderItem.getGoods_stock_id());
			goodsMap.put("restore_qty", orderItem.getQty() + "");
			goods_list.add(goodsMap);
		}
		// 还原自营商品库存
		reqParams.clear();
		bizParams.clear();
		resultMap.clear();
		reqParams.put("service", "goods.wypGoodsSaleQtyRestore");
		bizParams.put("goods_list", FastJsonUtil.toJson(goods_list));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
		resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			logger.error(resultMap.get("error_msg"));
		}
	}

	/**
	 * 子订单退款
	 * 
	 * @param data_source
	 * @param parent_order_id
	 * @param param_status
	 *            前置状态
	 * @param remark
	 * @throws BusinessException
	 * @throws SystemException
	 */
	private void psSubOrderListRefund(String data_source, String parent_order_id, String param_status, String remark)
			throws BusinessException, SystemException, Exception {
		// 子订单退款,先修改状态，然后再逐笔退款
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("parent_order_id", parent_order_id);
		if (StringUtils.isNotEmpty(param_status)) {
			tempMap.put("status", param_status);
		}
		List<Map<String, Object>> psSubOrderList = psOrderDao.selectPsGroupSubOrder(tempMap);
		// 修改订单状态
		for (Map<String, Object> subOrder : psSubOrderList) {
			String order_id = subOrder.get("order_id") + "";
			tempMap.clear();
			tempMap.put("order_id", order_id);
			tempMap.put("status", Constant.ORDER_CANCELLED);
			tempMap.put("modified_date",
					DateUtil.date2Str((Date) subOrder.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("remark", remark);
			Integer updateFlag = psOrderDao.updatePsSubOrder(tempMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
			}
		}

		// 进行退款操作
		for (Map<String, Object> subOrder : psSubOrderList) {
			String member_id = subOrder.get("member_id") + "";
			String order_no = subOrder.get("order_no") + "";
			Map<String, Object> out_trade_body = new HashMap<String, Object>();
			out_trade_body.put("parent_order_id", parent_order_id);
			out_trade_body.put("order_no", subOrder.get("order_no"));
			out_trade_body.put("total_fee", subOrder.get("total_fee") + "");
			orderService.orderRefund(data_source, member_id, Constant.MEMBER_ID_MTH, "ZFFS_05",
					new BigDecimal(subOrder.get("total_fee") + ""), "DDLX_09", order_no, "团购预售订单退款", out_trade_body);
		}
	}

	@Override
	public void psOrderListFind(Map<String, Object> paramsMap, ResultData result)
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
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> psOrderItemList = psOrderDao.selectPsOrderItem(paramsMap);
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		for (Map<String, Object> map : psOrderItemList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_id", map.get("order_id"));
			tempMap.put("order_no", map.get("order_no"));
			tempMap.put("order_type", map.get("order_type"));
			tempMap.put("order_date", DateUtil.date2Str((Date) map.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("desc1", map.get("desc1"));
			tempMap.put("item_num", map.get("item_num") + "");
			tempMap.put("payment_way_key", StringUtil.formatStr(map.get("payment_way_key")));
			tempMap.put("total_fee", map.get("total_fee") + "");
			tempMap.put("delivery_address", map.get("delivery_address") + "");
			tempMap.put("contact_person", map.get("contact_person"));
			tempMap.put("contact_tel", map.get("contact_tel"));
			tempMap.put("logistics", StringUtil.formatStr(map.get("logistics")));
			tempMap.put("remark", StringUtil.formatStr(map.get("remark")));
			tempMap.put("status", map.get("status"));
			tempMap.put("item_qty", map.get("item_qty") + "");
			tempMap.put("item_goods_id", map.get("item_goods_id"));
			tempMap.put("item_goods_title", StringUtil.formatStr(map.get("item_goods_title")) + "");
			tempMap.put("item_contact_person", map.get("item_contact_person"));
			tempMap.put("item_contact_tel", map.get("item_contact_tel"));
			tempMap.put("item_total_fee", map.get("item_total_fee") + "");
			tempMap.put("item_discount_fee", map.get("item_discount_fee") + "");
			if (map.get("order_type").equals("activity")) {
				tempMap.put("closing_time",
						DateUtil.date2Str((Date) map.get("closing_time"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("retail_price", StringUtil.formatStr(map.get("retail_price")));
				tempMap.put("qty_limit", StringUtil.formatStr(map.get("qty_limit")));
				tempMap.put("sub_order_qty", StringUtil.formatStr(map.get("sub_order_qty")));
			}
			tempMap.put("item_goods_pic_info", map.get("item_goods_pic_info") + "");
			String goods_pic_info = (String) map.get("item_goods_pic_info");
			if (!StringUtils.isEmpty(goods_pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(goods_pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(map);
		reqParams.clear();
		bizParams.clear();
		reqParams = null;
		bizParams = null;
	}

	@Override
	public void psOrderListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			String status = StringUtil.formatStr(paramsMap.get("status"));
			if (!StringUtils.isEmpty(status)) {
				List<String> list = StringUtil.str2List(status, ",");
				if (list.size() > 1) {
					paramsMap.remove("status");
					paramsMap.put("status_in", list);
				}
			}
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> psOrderItemList = psOrderDao.selectPsOrderItem(paramsMap);
			for (Map<String, Object> map : psOrderItemList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("order_id", map.get("order_id"));
				tempMap.put("order_no", map.get("order_no"));
				tempMap.put("order_date", DateUtil.date2Str((Date) map.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("desc1", map.get("desc1"));
				tempMap.put("item_num", map.get("item_num") + "");
				tempMap.put("payment_way_key", StringUtil.formatStr(map.get("payment_way_key")));
				tempMap.put("delivery_address", map.get("delivery_address") + "");
				tempMap.put("total_fee", map.get("total_fee") + "");
				tempMap.put("discount_fee", map.get("discount_fee") + "");
				tempMap.put("sale_fee", map.get("sale_fee") + "");
				tempMap.put("delivery_address", map.get("delivery_address") + "");
				tempMap.put("contact_person", map.get("contact_person"));
				tempMap.put("contact_tel", map.get("contact_tel"));
				tempMap.put("logistics", StringUtil.formatStr(map.get("logistics")));
				tempMap.put("remark", StringUtil.formatStr(map.get("remark")));
				tempMap.put("biz_remark", StringUtil.formatStr(map.get("biz_remark")));
				tempMap.put("status", map.get("status"));
				tempMap.put("settle_status", map.get("settle_status"));
				tempMap.put("item_qty", map.get("item_qty") + "");
				tempMap.put("item_goods_id", map.get("item_goods_id"));
				tempMap.put("item_goods_title", map.get("item_goods_title") + "");
				tempMap.put("item_goods_code", StringUtil.formatStr(map.get("item_goods_code")));
				tempMap.put("item_supplier", StringUtil.formatStr(map.get("item_supplier")));
				tempMap.put("item_manufacturer", StringUtil.formatStr(map.get("item_manufacturer")));
				tempMap.put("item_goods_unit", map.get("item_goods_unit") + "");
				tempMap.put("item_sale_price", map.get("item_sale_price") + "");
				tempMap.put("item_total_fee", map.get("item_total_fee") + "");
				tempMap.put("item_discount_fee", map.get("item_discount_fee") + "");
				tempMap.put("item_service_fee", map.get("item_service_fee") + "");
				tempMap.put("item_settled_price", map.get("item_settled_price") + "");
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void wypOrderListForOpFind(Map<String, Object> paramsMap, ResultData result)
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
		List<PsOrder> orderList = psOrderDao.selectWypOrderForOp(paramsMap);
		for (PsOrder order : orderList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_id", order.getOrder_id());
			tempMap.put("order_no", order.getOrder_no());
			tempMap.put("order_date", DateUtil.date2Str(order.getOrder_date(), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("desc1", order.getDesc1());
			tempMap.put("item_num", order.getItem_num() + "");
			tempMap.put("payment_way_key", StringUtil.formatStr(order.getPayment_way_key()));
			tempMap.put("delivery_address", order.getDelivery_address() + "");
			tempMap.put("total_fee", order.getTotal_fee() + "");
			tempMap.put("discount_fee", order.getDiscount_fee() + "");
			tempMap.put("sale_fee", order.getSale_fee() + "");
			tempMap.put("delivery_fee", order.getDelivery_fee() + "");
			tempMap.put("supplier", order.getSupplier() + "");
			tempMap.put("warehouse", order.getWarehouse() + "");
			tempMap.put("delivery_address", order.getDelivery_address() + "");
			tempMap.put("contact_person", order.getContact_person());
			tempMap.put("contact_tel", order.getContact_tel());
			tempMap.put("logistics", StringUtil.formatStr(order.getLogistics()));
			tempMap.put("remark", StringUtil.formatStr(order.getRemark()));
			tempMap.put("biz_remark", StringUtil.formatStr(order.getBiz_remark()));
			tempMap.put("status", order.getStatus());
			tempMap.put("settle_status", order.getSettle_status());
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void wypOrderItemForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id" });
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<PsOrderItem> orderItemList = psOrderDao.selectWypOrderItem(paramsMap);
		for (PsOrderItem item : orderItemList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", item.getGoods_id());
			tempMap.put("goods_title", item.getGoods_title() + "");
			tempMap.put("goods_code", StringUtil.formatStr(item.getGoods_code()));
			tempMap.put("goods_pic_info", item.getGoods_pic_info());
			tempMap.put("sku", StringUtil.formatStr(item.getSku()));
			tempMap.put("specification", StringUtil.formatStr(item.getSpecification()));
			tempMap.put("supplier", StringUtil.formatStr(item.getSupplier()));
			tempMap.put("qty", item.getQty() + "");
			tempMap.put("sale_price", item.getSale_price() + "");
			tempMap.put("discount_fee", item.getDiscount_fee() + "");
			tempMap.put("total_fee", item.getTotal_fee() + "");
			tempMap.put("service_fee", item.getService_fee() + "");
			tempMap.put("settled_price", item.getSettled_price() + "");
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void wypOrderListForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> orderList = psOrderDao.selectWypOrderForStores(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		for (Map<String, Object> order : orderList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_id", order.get("order_id"));
			tempMap.put("order_no", order.get("order_no"));
			tempMap.put("order_type", order.get("order_type"));
			tempMap.put("order_date", DateUtil.date2Str((Date) order.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("desc1", order.get("desc1"));
			tempMap.put("item_num", order.get("item_num") + "");
			tempMap.put("payment_way_key", StringUtil.formatStr(order.get("payment_way_key")));
			tempMap.put("supplier", order.get("supplier") + "");
			tempMap.put("warehouse", order.get("warehouse") + "");
			tempMap.put("delivery_fee", order.get("delivery_fee") + "");
			tempMap.put("sale_fee", order.get("sale_fee") + "");
			tempMap.put("discount_fee", order.get("discount_fee") + "");
			tempMap.put("total_fee", order.get("total_fee") + "");
			tempMap.put("contact_person", StringUtil.formatStr(order.get("contact_person")));
			tempMap.put("contact_tel", StringUtil.formatStr(order.get("contact_tel")));
			tempMap.put("delivery_address", StringUtil.formatStr(order.get("delivery_address")));
			tempMap.put("logistics", StringUtil.formatStr(order.get("logistics")));
			tempMap.put("status", order.get("status"));
			tempMap.put("remark", StringUtil.formatStr(order.get("remark")));
			tempMap.put("biz_remark", order.get("biz_remark"));
			List<Map<String, Object>> pic_list = new ArrayList<Map<String, Object>>();
			String item_info = StringUtil.formatStr(order.get("item_info"));
			if (StringUtils.isNotEmpty(item_info)) {
				String[] item_info_str = item_info.split("\\|");
				for (String item : item_info_str) {
					if (pic_list.size() > 5) {
						break;
					}
					List<Map<String, Object>> goods_pic_list = FastJsonUtil.jsonToList(item);
					Map<String, Object> picMap = goods_pic_list.get(0);
					if (picMap != null) {
						String path_id = StringUtil.formatStr(picMap.get("path_id"));
						if (StringUtils.isNotEmpty(path_id)) {
							doc_ids.add(path_id);
							pic_list.add(picMap);
						}
					}
				}
			}
			tempMap.put("goods_pic_list", FastJsonUtil.toJson(pic_list));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void wypOrderItemForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "order_id" });
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<PsOrderItem> orderItemList = psOrderDao.selectWypOrderItem(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		for (PsOrderItem item : orderItemList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", item.getGoods_id());
			tempMap.put("qty", item.getQty() + "");
			tempMap.put("goods_title", item.getGoods_title() + "");
			tempMap.put("goods_code", StringUtil.formatStr(item.getGoods_code()));
			tempMap.put("supplier", StringUtil.formatStr(item.getSupplier()));
			tempMap.put("specification", StringUtil.formatStr(item.getSpecification()));
			tempMap.put("goods_unit", StringUtil.formatStr(item.getGoods_unit()));
			tempMap.put("sale_price", item.getSale_price() + "");
			tempMap.put("total_fee", item.getTotal_fee() + "");
			tempMap.put("discount_fee", item.getDiscount_fee() + "");
			tempMap.put("service_fee", item.getService_fee() + "");
			tempMap.put("settled_price", item.getSettled_price() + "");
			tempMap.put("goods_pic_info", item.getGoods_pic_info() + "");
			String goods_pic_info = item.getGoods_pic_info();
			if (StringUtils.isNotEmpty(goods_pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(goods_pic_info);
				// 去除多余的图片
				Map<String, Object> m = tempList.get(0);
				tempList.clear();
				tempList.add(m);
				tempMap.put("goods_pic_info", FastJsonUtil.toJson(tempList));
				if (StringUtils.isNotBlank(m.get("path_id") + "")) {
					doc_ids.add(m.get("path_id") + "");
				}
			}
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void psOrderCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "order_type" });
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("count_num", "0");
		paramsMap.put("status", Constant.ORDER_ACTIVING);
		Map<String, Object> countsMap = psOrderDao.selectPsOrderCount(paramsMap);
		if (null != countsMap) {
			resultMap.put("count_num", countsMap.get("count_num") + "");
		}
		result.setResultData(resultMap);
	}

	@Override
	public void psOrderGoodsCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total_fee", "0.00");
		Map<String, Object> countsMap = psOrderDao.selectPsOrderGoodsCount(paramsMap);
		if (null != countsMap) {
			resultMap.put("total_fee", countsMap.get("total_fee") + "");
		}
		result.setResultData(resultMap);
	}

	@Override
	public void psOrderSettlementCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String status = StringUtil.formatStr(paramsMap.get("status"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("status");
				paramsMap.put("status_in", list);
			}
		}
		Map<String, Object> settlementCountMap = psOrderDao.selectPsOrderSettlementCount(paramsMap);
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
	public void handlePsOrderSettlement(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "operator", "order_date" });
		String operator = paramsMap.get("operator") + "";
		Map<String, List<Map<String, Object>>> settlementMap = new HashMap<String, List<Map<String, Object>>>();
		// 查询需要结算的订单列表
		paramsMap.put("settle_status", Constant.ORDER_SETTLE_PENDING);
		paramsMap.put("status", Constant.ORDER_RECEIVED);
		List<Map<String, Object>> settlementList = psOrderDao.selectWypOrderItemForSettlement(paramsMap);
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
			String settlementAmount = "0"; // 结算金额
			// String orderAmount = "0"; //订单金额
			String supplier = "";
			List<Map<String, Object>> tempList = settlementMap.get(supplier_id);
			// 生成结算清单
			for (Map<String, Object> tempMap : tempList) {
				// 以商品的结算价格金额为主
				String qty = tempMap.get("qty") + "";
				String total_fee = MoneyUtil.moneyMul(tempMap.get("settled_price") + "", qty);
				settlementAmount = MoneyUtil.moneyAdd(settlementAmount, total_fee);
				supplier = StringUtil.formatStr(tempMap.get("supplier"));
				OdSettlementDetail detail = new OdSettlementDetail();
				detail.setData_source(Constant.ORDER_TYPE_PROPRIETARY);
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
				bizParams.put("modified_date",
						DateUtil.date2Str((Date) tempMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
				int updateFlag = psOrderDao.updatePsOrder(bizParams);
				if (updateFlag == 0) {
					throw new BusinessException(RspCode.ORDER_PROCESSING, "请刷新订单后再进行结算操作");
				}
				orderNum++;
			}
			settlement.setData_source(Constant.ORDER_TYPE_PROPRIETARY);
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
			// 设置供应商的结算的子订单数量
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
	public void psGroupGoodsListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String status = StringUtil.formatStr(paramsMap.get("member_id"));
		if (!StringUtils.isEmpty(status)) {
			List<String> list = StringUtil.str2List(status, ",");
			if (list.size() > 1) {
				paramsMap.remove("member_id");
				paramsMap.put("member_id_in", list);
			}
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> groupGoodsList = psOrderDao.selectPsGroupGoods(paramsMap);
		for (Map<String, Object> map : groupGoodsList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("invitation_code", map.get("invitation_code"));
			tempMap.put("member_type_key", map.get("member_type_key"));
			tempMap.put("member_id", map.get("member_id"));
			tempMap.put("goods_id", map.get("goods_id"));
			tempMap.put("goods_title", map.get("goods_title") + "");
			tempMap.put("retail_price", map.get("retail_price") + "");
			tempMap.put("qty_limit", map.get("qty_limit") + "");
			tempMap.put("sub_order_qty", map.get("sub_order_qty") + "");
			tempMap.put("goods_pic_info", map.get("goods_pic_info") + "");
			String goods_pic_info = (String) map.get("goods_pic_info");
			if (!StringUtils.isEmpty(goods_pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(goods_pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(map);
	}

	@Override
	public void psGroupGoodsDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "invitation_code" });
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> groupGoodsList = psOrderDao.selectPsGroupGoods(paramsMap);
		if (groupGoodsList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_ERROE, "活动商品不存在");
		}
		Map<String, Object> groupGoods = groupGoodsList.get(0);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("invitation_code", groupGoods.get("invitation_code"));
		tempMap.put("member_type_key", groupGoods.get("member_type_key"));
		tempMap.put("member_id", groupGoods.get("member_id"));
		// 解析会员信息
		String member_info = groupGoods.get("member_info") + "";
		Map<String, Object> memberMap = FastJsonUtil.jsonToMap(member_info);
		tempMap.put("stores_name", memberMap.get("stores_name"));
		tempMap.put("stores_contact_tel", groupGoods.get("stores_contact_tel"));
		tempMap.put("stores_contact_person", groupGoods.get("stores_contact_person"));
		tempMap.put("stores_delivery_address", groupGoods.get("stores_delivery_address"));
		tempMap.put("goods_id", groupGoods.get("goods_id"));
		tempMap.put("goods_code", groupGoods.get("goods_code"));
		tempMap.put("goods_title", groupGoods.get("goods_title") + "");
		tempMap.put("goods_desc1", StringUtil.formatStr(groupGoods.get("goods_desc1")));
		tempMap.put("goods_unit", StringUtil.formatStr(groupGoods.get("goods_unit")));
		tempMap.put("specification", StringUtil.formatStr(groupGoods.get("specification")));

		tempMap.put("closing_time",
				DateUtil.date2Str((Date) groupGoods.get("closing_time"), DateUtil.fmt_yyyyMMddHHmmss));
		tempMap.put("retail_price", groupGoods.get("retail_price") + "");
		tempMap.put("qty_limit", groupGoods.get("qty_limit") + "");
		tempMap.put("sub_order_qty", groupGoods.get("sub_order_qty") + "");
		tempMap.put("pic_info", groupGoods.get("goods_pic_info") + "");
		tempMap.put("pic_detail_info", groupGoods.get("goods_pic_detail_info") + "");
		String goods_pic_info = (String) groupGoods.get("goods_pic_info");
		if (!StringUtils.isEmpty(goods_pic_info)) {
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(goods_pic_info);
			for (Map<String, Object> m : tempList) {
				if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
					doc_ids.add(m.get("path_id") + "");
				}
			}
		}
		String goods_pic_detail_info = (String) groupGoods.get("goods_pic_detail_info");
		if (!StringUtils.isEmpty(goods_pic_detail_info)) {
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(goods_pic_detail_info);
			for (Map<String, Object> m : tempList) {
				if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
					doc_ids.add(m.get("path_id") + "");
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detail", tempMap);
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(map);
	}

	@Override
	public void handleInitiateGroup(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil
				.validateParams(paramsMap,
						new String[] { "desc1", "member_id", "contact_tel", "contact_person", "member_type_key",
								"stores_name", "delivery_address", "retail_price", "qty_limit", "closing_time",
								"ps_goods" });
		Map<String, Object> memberInfo = new HashMap<String, Object>();
		memberInfo.put("stores_name", paramsMap.get("stores_name"));
		paramsMap.put("member_info", FastJsonUtil.toJson(memberInfo));
		Date date = new Date();
		PsOrder order = new PsOrder();
		BeanConvertUtil.mapToBean(order, paramsMap);
		order.setCreated_date(date);
		order.setModified_date(date);
		order.setOrder_id(IDUtil.getUUID());
		order.setOrder_no(OrderIDUtil.getOrderNo());
		order.setOrder_type(Constant.PS_ORDER_TYPE_ACTIVITY);
		order.setStatus(Constant.ORDER_ACTIVING);
		order.setSettle_status(Constant.ORDER_SETTLE_PENDING);
		order.setItem_num(1);
		order.setSale_fee(new BigDecimal("0.00"));
		order.setDiscount_fee(new BigDecimal("0.00"));
		order.setTotal_fee(new BigDecimal("0.00"));
		order.setDelivery_fee(new BigDecimal("0.00"));
		order.setPayment_way_key("ZFFS_05");
		order.setOrder_date(date);

		// 解析商品信息
		Map<String, Object> ps_goods = FastJsonUtil.jsonToMap((String) paramsMap.get("ps_goods"));

		PsOrderItem orderItem = new PsOrderItem();
		BeanConvertUtil.mapToBean(orderItem, ps_goods);
		orderItem.setOrder_item_id(IDUtil.getUUID());
		orderItem.setOrder_id(order.getOrder_id());
		orderItem.setCreated_date(date);
		orderItem.setModified_date(date);
		orderItem.setQty(0);
		orderItem.setTotal_fee(new BigDecimal("0.00"));
		// 获取商品详情
		Map<String, Object> dataMap = orderService.psGoodsFindForOrder(orderItem.getGoods_id(), null);
		String status = StringUtil.formatStr(dataMap.get("status"));
		if (!status.equals("on_shelf")) {
			throw new BusinessException(RspCode.ORDER_ERROE, "商品已下架");
		}
		orderItem.setGoods_desc1(StringUtil.formatStr(dataMap.get("desc1")));
		orderItem.setGoods_pic_info(StringUtil.formatStr(dataMap.get("pic_info")));
		orderItem.setGoods_pic_detail_info(StringUtil.formatStr(dataMap.get("pic_detail_info")));
		orderItem.setContact_person(StringUtil.formatStr(dataMap.get("contact_person")));
		orderItem.setContact_tel(StringUtil.formatStr(dataMap.get("contact_tel")));
		orderItem.setGoods_unit(StringUtil.formatStr(dataMap.get("goods_unit")));
		orderItem.setSupplier_id(StringUtil.formatStr(dataMap.get("supplier_id")));
		orderItem.setSupplier(StringUtil.formatStr(dataMap.get("supplier")));
		orderItem.setSpecification(StringUtil.formatStr(dataMap.get("specification")));
		orderItem.setManufacturer(StringUtil.formatStr(dataMap.get("manufacturer")));
		orderItem.setSettled_price(new BigDecimal(dataMap.get("settled_price") + ""));
		orderItem.setService_fee(new BigDecimal(dataMap.get("service_fee") + ""));
		// 设置订单的供应商和仓位
		order.setSupplier_id(StringUtil.formatStr(dataMap.get("supplier_id")));
		order.setSupplier(StringUtil.formatStr(dataMap.get("supplier")));
		order.setWarehouse(StringUtil.formatStr(dataMap.get("warehouse")));
		order.setWarehouse_id(StringUtil.formatStr(dataMap.get("warehouse_id")));
		// 订单主表
		psOrderDao.insertPsOrder(order);
		// 订单商品列表
		psOrderDao.insertPsOrderItem(orderItem);
		// 创建团购商品
		PsOrderExtra extra = new PsOrderExtra();
		BeanConvertUtil.mapToBean(extra, paramsMap);
		extra.setOrder_id(order.getOrder_id());
		extra.setActivity_no(order.getOrder_no());
		extra.setInvitation_code(order.getOrder_no());
		extra.setSub_order_qty(0);
		extra.setCreated_date(date);
		extra.setModified_date(date);
		psOrderDao.insertPsOrderExtra(extra);
	}

	@Override
	public void psGroupSubOrderCreateNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "invitation_code", "member_id", "member_type_key", "retail_price", "goods_id",
						"goods_code", "stores_id", "stores_name", "payment_way_key", "qty", "retail_price", "total_fee",
						"contact_tel" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("invitation_code", paramsMap.get("invitation_code"));
		tempMap.put("status", Constant.ORDER_ACTIVING);
		List<Map<String, Object>> groupOrderList = psOrderDao.selectPsGroupGoods(tempMap);
		if (groupOrderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_ERROE, "活动已结束");
		}
		Map<String, Object> groupOrder = groupOrderList.get(0);
		Date closing_time = (Date) groupOrder.get("closing_time");
		if (closing_time.getTime() < new Date().getTime()) {
			throw new BusinessException(RspCode.ORDER_ERROE, "活动已结束");
		}

		tempMap.clear();
		tempMap.put("order_id", groupOrder.get("order_id"));
		tempMap.put("modified_date",
				DateUtil.date2Str((Date) groupOrder.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
		tempMap.put("qty", paramsMap.get("qty") + "");
		int updateFlag = psOrderDao.updatePsOrderExtra(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}

		Map<String, Object> memberInfo = new HashMap<String, Object>();
		memberInfo.put("nick_name", StringUtil.formatStr(paramsMap.get("nick_name")));
		paramsMap.put("member_info", FastJsonUtil.toJson(memberInfo));
		paramsMap.put("parent_order_id", groupOrder.get("order_id"));

		Date date = new Date();
		PsSubOrder order = new PsSubOrder();
		BeanConvertUtil.mapToBean(order, paramsMap);
		order.setCreated_date(date);
		order.setModified_date(date);
		order.setOrder_id(IDUtil.getUUID());
		order.setOrder_no(OrderIDUtil.getOrderNo());
		order.setStatus(Constant.ORDER_PAID);
		order.setOrder_date(date);
		order.setLoaded_code(IDUtil.random(4));
		psOrderDao.insertPsSubOrder(order);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_no", order.getOrder_no());
		result.setResultData(paramMap);
	}

	@Override
	public void psGroupSubOrderForConsumerListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> psSubOrderList = psOrderDao.selectPsGroupSubOrder(paramsMap);
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		for (Map<String, Object> map : psSubOrderList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_id", map.get("order_id"));
			tempMap.put("order_no", map.get("order_no"));
			tempMap.put("order_date", DateUtil.date2Str((Date) map.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("desc1", map.get("desc1"));
			tempMap.put("desc2", StringUtil.formatStr(map.get("desc2")));
			tempMap.put("payment_way_key", StringUtil.formatStr(map.get("payment_way_key")));
			tempMap.put("goods_title", StringUtil.formatStr(map.get("goods_title")));
			tempMap.put("stores_name", StringUtil.formatStr(map.get("stores_name")));
			tempMap.put("stores_delivery_address", StringUtil.formatStr(map.get("stores_delivery_address")));
			tempMap.put("stores_contact_person", StringUtil.formatStr(map.get("stores_contact_person")));
			tempMap.put("stores_contact_tel", StringUtil.formatStr(map.get("stores_contact_tel")));
			tempMap.put("qty", map.get("qty") + "");
			tempMap.put("total_fee", map.get("total_fee") + "");
			tempMap.put("retail_price", map.get("retail_price") + "");
			tempMap.put("remark", StringUtil.formatStr(map.get("remark")));
			tempMap.put("status", map.get("status"));
			tempMap.put("goods_pic_info", map.get("goods_pic_info") + "");
			String goods_pic_info = (String) map.get("goods_pic_info");
			if (!StringUtils.isEmpty(goods_pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(goods_pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(map);
		reqParams.clear();
		bizParams.clear();
		reqParams = null;
		bizParams = null;
	}

	@Override
	public void psGroupOrderForStoresListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
		paramsMap.put("order_type", Constant.PS_ORDER_TYPE_ACTIVITY);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> orderItemList = psOrderDao.selectPsGroupOrderItemForStores(paramsMap);
		for (Map<String, Object> map : orderItemList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_id", map.get("order_id"));
			tempMap.put("order_no", map.get("order_no"));
			tempMap.put("order_date", DateUtil.date2Str((Date) map.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("desc1", map.get("desc1"));
			tempMap.put("total_fee", map.get("total_fee") + "");
			tempMap.put("contact_person", map.get("contact_person"));
			tempMap.put("contact_tel", map.get("contact_tel"));
			tempMap.put("logistics", StringUtil.formatStr(map.get("logistics")));
			tempMap.put("remark", StringUtil.formatStr(map.get("remark")));
			tempMap.put("status", map.get("status"));
			tempMap.put("item_qty", map.get("item_qty") + "");
			tempMap.put("item_goods_id", map.get("item_goods_id"));
			tempMap.put("item_goods_title", StringUtil.formatStr(map.get("item_goods_title")) + "");
			tempMap.put("item_discount_fee", map.get("item_discount_fee") + "");
			tempMap.put("closing_time", DateUtil.date2Str((Date) map.get("closing_time"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("retail_price", StringUtil.formatStr(map.get("retail_price")));
			tempMap.put("qty_limit", StringUtil.formatStr(map.get("qty_limit")));
			tempMap.put("sub_order_qty", StringUtil.formatStr(map.get("sub_order_qty")));
			tempMap.put("item_goods_pic_info", map.get("item_goods_pic_info") + "");
			String goods_pic_info = (String) map.get("item_goods_pic_info");
			if (!StringUtils.isEmpty(goods_pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(goods_pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		result.setResultData(map);
	}

	@Override
	public void psGroupSubOrderForStoresListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "parent_order_id" });
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> psSubOrderList = psOrderDao.selectPsGroupSubOrder(paramsMap);
		for (Map<String, Object> map : psSubOrderList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("order_id", map.get("order_id"));
			tempMap.put("order_no", map.get("order_no"));
			tempMap.put("order_date", DateUtil.date2Str((Date) map.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("desc1", map.get("desc1"));
			tempMap.put("desc2", StringUtil.formatStr(map.get("desc2")));
			tempMap.put("payment_way_key", StringUtil.formatStr(map.get("payment_way_key")));
			tempMap.put("goods_title", StringUtil.formatStr(map.get("goods_title")));
			tempMap.put("contact_person", StringUtil.formatStr(map.get("contact_person")));
			tempMap.put("contact_tel", StringUtil.formatStr(map.get("contact_tel")));
			tempMap.put("qty", map.get("qty") + "");
			tempMap.put("total_fee", map.get("total_fee") + "");
			tempMap.put("retail_price", map.get("retail_price") + "");
			tempMap.put("remark", StringUtil.formatStr(map.get("remark")));
			tempMap.put("status", map.get("status"));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void psGroupSubOrderForOpListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> psSubOrderList = psOrderDao.selectPsSubOrderForOp(paramsMap);
		for (Map<String, Object> map : psSubOrderList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("parent_order_no", map.get("parent_order_no"));
			tempMap.put("parent_order_date",
					DateUtil.date2Str((Date) map.get("parent_order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("parent_delivery_address", StringUtil.formatStr(map.get("parent_delivery_address")));
			tempMap.put("parent_stores_name", StringUtil.formatStr(map.get("parent_stores_name")));
			tempMap.put("parent_contact_person", StringUtil.formatStr(map.get("parent_contact_person")));
			tempMap.put("parent_contact_tel", StringUtil.formatStr(map.get("parent_contact_tel")));
			tempMap.put("parent_status", StringUtil.formatStr(map.get("parent_status")));
			tempMap.put("parent_goods_code", StringUtil.formatStr(map.get("parent_goods_code")));
			tempMap.put("parent_goods_title", StringUtil.formatStr(map.get("parent_goods_title")));
			tempMap.put("goods_discount_fee", StringUtil.formatStr(map.get("goods_discount_fee")));
			tempMap.put("parent_qty_limit", StringUtil.formatStr(map.get("parent_qty_limit")));
			tempMap.put("order_date", DateUtil.date2Str((Date) map.get("order_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("order_no", map.get("order_no"));
			tempMap.put("payment_way_key", StringUtil.formatStr(map.get("payment_way_key")));
			tempMap.put("desc1", StringUtil.formatStr(map.get("desc1")));
			tempMap.put("desc2", StringUtil.formatStr(map.get("desc2")));
			tempMap.put("qty", map.get("qty") + "");
			tempMap.put("retail_price", map.get("retail_price") + "");
			tempMap.put("total_fee", map.get("total_fee") + "");
			tempMap.put("contact_person", StringUtil.formatStr(map.get("contact_person")));
			tempMap.put("contact_tel", StringUtil.formatStr(map.get("contact_tel")));
			tempMap.put("qty", map.get("qty") + "");
			tempMap.put("status", map.get("status") + "");
			tempMap.put("retail_price", map.get("retail_price") + "");
			tempMap.put("remark", StringUtil.formatStr(map.get("remark")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void handlePsSubOrderCanCelled(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "data_source", "order_id", "remark" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", paramsMap.get("order_id"));
		List<Map<String, Object>> psSubOrderList = psOrderDao.selectPsGroupSubOrder(tempMap);
		if (psSubOrderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		Map<String, Object> subOrder = psSubOrderList.get(0);
		if (!subOrder.get("status").equals(Constant.ORDER_PAID)) {
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
		}
		// 还原团购预售订单商品数量
		tempMap.clear();
		tempMap.put("order_id", subOrder.get("parent_order_id"));
		tempMap.put("qty", "-" + subOrder.get("qty"));
		int updateFlag = psOrderDao.updatePsOrderExtra(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}

		// 更新子订单状态
		tempMap.clear();
		tempMap.put("order_id", subOrder.get("order_id"));
		tempMap.put("status", Constant.ORDER_CANCELLED);
		tempMap.put("remark", paramsMap.get("remark"));
		tempMap.put("modified_date",
				DateUtil.date2Str((Date) subOrder.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
		updateFlag = psOrderDao.updatePsSubOrder(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
		// 订单退款
		Map<String, Object> out_trade_body = new HashMap<String, Object>();
		out_trade_body.put("parent_order_id", subOrder.get("parent_order_id"));
		out_trade_body.put("order_no", subOrder.get("order_no"));
		out_trade_body.put("total_fee", subOrder.get("total_fee"));
		orderService.orderRefund(paramsMap.get("data_source") + "", subOrder.get("member_id") + "",
				Constant.MEMBER_ID_MTH, "ZFFS_05", new BigDecimal(subOrder.get("total_fee") + ""), "DDLX_09",
				subOrder.get("order_no") + "", "团购预售订单退款", out_trade_body);
	}

	@Override
	public void handlePsSubOrderReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "data_source", "order_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("order_id", paramsMap.get("order_id"));
		List<Map<String, Object>> psOrderList = psOrderDao.selectPsGroupSubOrder(tempMap);
		if (psOrderList.size() == 0) {
			throw new BusinessException(RspCode.ORDER_NOT_EXIST, RspCode.MSG.get(RspCode.ORDER_NOT_EXIST));
		}
		Map<String, Object> order = psOrderList.get(0);
		if (!order.get("status").equals(Constant.ORDER_ARRIVED)) {
			throw new BusinessException(RspCode.ORDER_STATUS_ERROR, "操作失败,请刷新后重试");
		}
		// 更新订单状态
		tempMap.clear();
		tempMap.put("order_id", order.get("order_id"));
		tempMap.put("status", Constant.ORDER_RECEIVED);
		tempMap.put("modified_date", DateUtil.date2Str((Date) order.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
		Integer updateFlag = psOrderDao.updatePsSubOrder(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.ORDER_PROCESSING, "操作失败,请刷新后重试");
		}
		// 数量
		String qty = order.get("qty") + "";
		// 团购价格
		String retail_price = order.get("retail_price") + "";
		// 商品优惠后单价
		String goods_discount_fee = order.get("goods_discount_fee") + "";
		// 差价
		String diff_price = MoneyUtil.moneySub(retail_price, goods_discount_fee);
		if (!MoneyUtil.moneyComp("0.00", diff_price)) {
			// 店东获取的佣金
			String diff_totle_fee = MoneyUtil.moneyMul(diff_price, qty);
			Map<String, Object> out_trade_body = new HashMap<String, Object>();
			out_trade_body.put("stores_id", order.get("stores_member_id"));
			out_trade_body.put("member_id", order.get("member_id"));
			out_trade_body.put("member_info", order.get("member_info"));
			out_trade_body.put("diff_price", diff_price);
			out_trade_body.put("qty", qty);
			out_trade_body.put("diff_totle_fee", diff_totle_fee);
			out_trade_body.put("goods_title", order.get("goods_title"));
			orderService.balancePay(paramsMap.get("data_source") + "", Constant.MEMBER_ID_MTH,
					order.get("stores_member_id") + "", "ZFFS_05", new BigDecimal(diff_totle_fee), "DDLX_09",
					order.get("order_no") + "", "团购预售", out_trade_body);
		}
	}

	@Override
	public void psGoodsImport(final String store_id, final String goods_code, final Integer qty, final String remark,
			final String product_source) throws BusinessException, SystemException, Exception {
		OrderController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					// 导入商品到门店的商品库中
					Map<String, String> reqParams = new HashMap<String, String>();
					Map<String, Object> bizParams = new HashMap<String, Object>();
					reqParams.put("service", "goods.psGoodsImport");
					bizParams.put("store_id", store_id);
					bizParams.put("goods_code", goods_code);
					bizParams.put("qty", qty + "");
					bizParams.put("remark", remark);
					bizParams.put("product_source", product_source);
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					HttpClientUtil.postShort(OrderServiceImpl.GOODS_SERVICE_URL, reqParams);
				} catch (BusinessException e) {
					logger.warn("商品导入失败," + e.getMsg());
				} catch (SystemException e) {
					logger.error("商品导入异常", e);
				} catch (Exception e) {
					logger.error("商品导入异常", e);
				}
			}
		});
	}

}
