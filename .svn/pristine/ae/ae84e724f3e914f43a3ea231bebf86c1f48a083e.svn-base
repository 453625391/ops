package com.meitianhui.order.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/***
 * 积分订单逻辑处理接口
 * 
 * @author 丁硕
 * @date 2016年12月28日
 */
public interface PeOrderService {

	/**
	 * 积分订单创建
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePeOrderPayCreateNotify(Map<String, Object> paramsMap, ResultData result)throws BusinessException, SystemException, Exception;
	
	/**
	 * 积分订单列表查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void peOrderListFindForOp(Map<String, Object> paramsMap, ResultData result)throws BusinessException, SystemException, Exception;
	
	/**
	 * 积分订单查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void peOrderDetailFind(Map<String, Object> paramsMap, ResultData result)throws BusinessException, SystemException, Exception;
	
	/**
	 * 积分订单发货
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePeOrderDelivered(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 积分订单收货
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePeOrderReceived(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 积分订单编辑
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void peOrderEdit(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 积分结算订单统计
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月28日
	 */
	public void peOrderSettlementCount(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 积分订单结算，只按现金结算
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月28日
	 */
	public void handlePeOrderSettlement(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
}
