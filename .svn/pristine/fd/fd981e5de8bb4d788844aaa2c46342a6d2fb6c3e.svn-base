package com.meitianhui.order.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/***
 * 精选特卖订单相关逻辑处理接口
 * 
 * @author 丁硕
 * @date 2016年12月14日
 */
public interface PcOrderService {

	/**
	 * 掌上便利店订单创建通知
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePcOrderPayCreateNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 掌上便利店订单查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void pcOrderDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 掌上便利店订单消费者端查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void pcOrderDetailForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 掌上便利店订单取消
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePcOrderCancelled(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 掌上便利店订单发货
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePcOrderDeliver(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/***
	 * 掌上便利店派工
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date 2016年12月30日
	 */
	public void handlePcOrderAssigned(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 掌上便利店订单收货
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePcOrderReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 掌上便利店已支付的订单数量统计
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void pcOrderPayedCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 掌上便利店订单销售(店东特卖)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void pcOrderSellCountForStoresSaleFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/***
	 * 运营掌上便利店订单列表查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date 2016年12月30日
	 */
	public void pcOrderListFindForOp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

}
