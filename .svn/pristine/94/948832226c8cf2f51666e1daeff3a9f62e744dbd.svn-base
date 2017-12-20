package com.meitianhui.notification.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface NotificationService {

	/**
	 * 发送短信校验码
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void sendCheckCode(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 发送信息
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void sendMsg(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 校验短信验证码
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void validateCheckCode(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * App消息通知保存入库
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void appMsgNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 发送通知(店东)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushStoresNotification(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 发送通知(消费者)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushConsumerNotification(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 发送通知(别名)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushNotificationByAlias(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 发送通知(标签)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushNotificationByTag(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 发送消息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void pushMessage(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

}
