package com.meitianhui.goods.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;


/**
 * 
* @ClassName: GdActivityDeliveryService  
* @Description: 权益活动配送列表
* @author tiny 
* @date 2017年2月20日 下午4:40:41  
*
 */
public interface GdActivityDeliveryService {
	
	/**
	 * 
	* @Title: gdActivityDeliveryListFind  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	void gdActivityDeliveryListForOpFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 权益活动配送列表查询(消费者)
	 * @Title: GdActivityDeliveryListFind  
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	void gdActivityDeliveryListForConsumerFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 权益活动配送(发货)
	 * @Title: gdActivityDeliveryUpdate  
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	void handleGdActivityDeliver(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 权益活动订单数量查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	void gdActivityDeliveryCountFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 新增会员权益活动商品订单
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */	
	void handleGdActivityDeliveryCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
}
