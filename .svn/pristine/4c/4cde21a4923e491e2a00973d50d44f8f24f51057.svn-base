package com.meitianhui.order.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/***
 * 名品汇订单逻辑处理接口
 * 
 * @author 丁硕
 * @date 2016年12月28日
 */
public interface GeOrderService {

	/**
	 * 名品汇订单创建
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleGeOrderPayCreateNotify(Map<String, Object> paramsMap, ResultData result)throws BusinessException, SystemException, Exception;
	
	/**
	 * 名品汇订单列表查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void geOrderListFindForOp(Map<String, Object> paramsMap, ResultData result)throws BusinessException, SystemException, Exception;
	
	/**
	 * 名品汇订单查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void geOrderDetailFind(Map<String, Object> paramsMap, ResultData result)throws BusinessException, SystemException, Exception;
	
	/**
	 * 名品汇订单发货
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleGeOrderDelivered(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 名品汇订单收货
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleGeOrderReceived(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 名品汇订单编辑
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void geOrderEdit(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 名品汇结算订单统计
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月28日
	 */
	public void geOrderSettlementCount(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 名品汇订单结算，只按现金结算
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月28日
	 */
	public void handleGeOrderSettlement(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
}
