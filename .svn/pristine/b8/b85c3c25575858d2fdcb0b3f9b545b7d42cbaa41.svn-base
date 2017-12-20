package com.meitianhui.order.service;

import java.math.BigDecimal;
import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface OrderService {

	/**
	 * 物流信息查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void orderLogisticsFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 超级返订单结算列表
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void orderSettlementList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

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
	public void handleOrderSettlementPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 精选特卖订单查询(本地生活)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void zjsOrderListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 提货码搜索
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void orderByloadedCodeForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 扣库存(主表)
	 * 
	 * @Title: psGoodsSaleQtyDeduction
	 * @param goods_id
	 * @param sell_qty
	 * @throws BusinessException
	 * @throws Exception
	 * @author tiny
	 */
	public void psGoodsSaleQtyDeduction(String goods_id, String sell_qty) throws BusinessException, Exception;

	/**
	 * 还原库存(主表)
	 * 
	 * @Title: psGoodsSaleQtyDeduction
	 * @param goods_id
	 * @param sell_qty
	 * @throws BusinessException
	 * @throws Exception
	 * @author tiny
	 */
	public void psGoodsSaleQtyRestore(String goods_id, String sell_qty) throws BusinessException, Exception;

	/**
	 * 查询商品信息
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
	public Map<String, Object> psGoodsFindForOrder(String goods_id, String goods_code)
			throws BusinessException, SystemException, Exception;

	/**
	 * 余额支付
	 * 
	 * @Title: balancePay
	 * @param data_source
	 * @param buyer_id
	 * @param seller_id
	 * @param payment_way_key
	 * @param amount
	 * @param order_type_key
	 * @param out_trade_no
	 * @param detail
	 * @param out_trade_body
	 * @throws Exception
	 * @author tiny
	 */
	public void balancePay(String data_source, String buyer_id, String seller_id, String payment_way_key,
			BigDecimal amount, String order_type_key, String out_trade_no, String detail,
			Map<String, Object> out_trade_body) throws Exception;

	/**
	 * 订单赠送
	 * 
	 * @Title: orderReward
	 * @param data_source
	 * @param buyer_id
	 * @param seller_id
	 * @param payment_way_key
	 * @param amount
	 * @param order_type_key
	 * @param out_trade_no
	 * @param detail
	 * @param out_trade_body
	 * @throws Exception
	 * @author tiny
	 */
	public void orderReward(String data_source, String buyer_id, String seller_id, String payment_way_key,
			BigDecimal amount, String order_type_key, String out_trade_no, String detail,
			Map<String, Object> out_trade_body) throws Exception;

	/**
	 * 订单退款
	 * 
	 * @Title: orderRefund
	 * @param data_source
	 * @param buyer_id
	 * @param seller_id
	 * @param payment_way_key
	 * @param amount
	 * @param order_type_key
	 * @param out_trade_no
	 * @param detail
	 * @param out_trade_body
	 * @throws Exception
	 * @author tiny
	 */
	public void orderRefund(String data_source, String buyer_id, String seller_id, String payment_way_key,
			BigDecimal amount, String order_type_key, String out_trade_no, String detail,
			Map<String, Object> out_trade_body) throws Exception;

	/**
	 * 门店会员关系创建
	 * 
	 * @Title: storesMemberRelCreate
	 * @param stores_id
	 * @param consumer_id
	 * @throws Exception
	 * @author tiny
	 */
	public void storesMemberRelCreate(final String stores_id, final String consumer_id);

}
