package com.meitianhui.order.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/***
 * 我要批
 * 
 * @author 丁硕
 * @date 2016年12月14日
 */
public interface PsOrderService {

	/**
	 * 我要批订单创建
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void wypOrderCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单支付
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void wypOrderPayInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单支付通知
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleWypOrderPayNotity(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单信息编辑
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psOrderEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单发货
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePsOrderDelivered(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单取消(店东助手)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePsOrderCancelled(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单确认收货
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePsOrderReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单退款
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePsOrderRefund(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单信息编辑
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psOrderStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单列表查询(运营系统)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psOrderListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单列表查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psOrderListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单列表查询(运营)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void wypOrderListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单商品列表查询(运营)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void wypOrderItemForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单列表查询(运营)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void wypOrderListForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单商品列表查询(运营)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void wypOrderItemForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 发起团购预售
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleInitiateGroup(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 团购预售商品列表查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupGoodsListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 团购预售商品明细查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupGoodsDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 团购预售商品信息查询(消费者)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupSubOrderForConsumerListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 团购预售主订单信息查询(店东)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupOrderForStoresListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 团购预售商品信息查询(店东)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupSubOrderForStoresListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 团购预售商品信息查询(运营系统)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupSubOrderForOpListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 团购预售子订单
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupSubOrderCreateNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 团购预售子订单取消
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePsSubOrderCanCelled(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单结算
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePsOrderSettlement(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单统计
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psOrderCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批订单商品统计
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Deprecated
	public void psOrderGoodsCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 团购预售子订单确认
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePsSubOrderReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 我要批结算订单统计查询(运营)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psOrderSettlementCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 运营我要批列表查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @author 丁硕
	 * @date 2016年12月14日
	 */
	public void selectPsOrderForOp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 导入商品到门店商品库
	 * 
	 * @Title: psGoodsImport
	 * @param store_id
	 * @param goods_code
	 * @param qty
	 * @param remark
	 * @param product_source
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void psGoodsImport(final String store_id, final String goods_code, final Integer qty, final String remark,
			final String product_source) throws BusinessException, SystemException, Exception;

}
