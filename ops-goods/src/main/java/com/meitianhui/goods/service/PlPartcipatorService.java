package com.meitianhui.goods.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/**
 * 抽奖活动
 * @author Tiny
 *
 */
public interface PlPartcipatorService {

	/**
	 * 发布抽奖活动
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plActivityCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 查询抽奖活动（运营平台）
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plActivityListForOpFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	/**
	 * 查询抽奖活动（消费者端）
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plActivityListForConsumerFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 查询抽奖活动详情（消费者端）
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plActivityListForConsumerDetailFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 抽奖活动取消
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plActivityCancel(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 用户参加抽奖活动
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plPartcipatorAdd(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 查询中奖结果（运营平台）
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plLuckyDeliveryListForOpFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 查询中奖纪录（消费者端）
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plLuckyDeliveryListForConsumerFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 查询参与活动的会员列表（运营端）
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plPartcipatorListForOpFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 查询参与的活动的详情列表（消费者端）
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plPartcipatorListForConsumerFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 中奖表发货
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void plLuckyDeliveryDelivered(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	

}

