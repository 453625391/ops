package com.meitianhui.notification.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface JpushService {

	
	/**
	 * 发送通知给所有的会员(本地生活)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushConsumerNotification(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 发送通知给所有的会员(店东助手)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushStoresNotification(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 给本地生活发送通知(根据别名)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushConsumerNotificationByAlias(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 给店东助手发送通知(根据别名)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushStoresNotificationByAlias(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 给本地生活发送通知(根据标签)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushConsumerNotificationByTag(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 给店东助手发送通知(根据标签)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushStoresNotificationByTag(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	
	
	/**
	 * 发送消息(本地生活)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushConsumerMessage(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 发送消息(店东助手)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushStoresMessage(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	

}
