package com.meitianhui.order.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/***
 * 增值服务
 * 
 * @author 丁硕
 * @date 2016年12月14日
 */
public interface CsOrderService {
	
	/**
	 * 话费充值订单创建通知
	 * @param paramsMap
	 * @param result
	 * @author 丁硕
	 * @date   2016年12月14日
	 */
	public void phoneBillOrderCreateNotity(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 话费充值成功通知
	 * @param paramsMap
	 * @param result
	 * @author 丁硕
	 * @date   2016年12月14日
	 */
	public void handlePhoneBillOrderFinishNotity(Map<String, Object> paramsMap) throws BusinessException, SystemException, Exception;
	
	/**
	 * app查询话费充值订单列表
	 * @param paramsMap
	 * @param result
	 * @author 丁硕
	 * @date   2016年12月14日
	 */
	public void phoneBillOrderListForAppFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * app查询话费充值订单列表
	 * @param paramsMap
	 * @param result
	 * @author 丁硕
	 * @date   2016年12月14日
	 */
	public void csOrderListForOpFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 增值订单结算统计
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月21日
	 */
	public void csOrderSettlementCount(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 增值订单结算
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月21日
	 */
	public void handleCsOrderSettlement(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
}
