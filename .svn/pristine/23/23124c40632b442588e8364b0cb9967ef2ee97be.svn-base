package com.meitianhui.infrastructure.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface InfrastructureService {

	/**
	 * 交易注册(付款码)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleTransactionRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 交易校验(付款码)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleTransactionVerify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 手机号授权注册
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleMobileLoginRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 手机号授权校验
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleMobileLoginVerify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 熟么授权注册
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleShumeLoginRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 熟么授权校验
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleShumeLoginVerify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 用户反馈
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void userFeedbackCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 用户信息反馈编辑
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void userFeedbackEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 用户信息反馈校验
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void userFeedbackFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 安全码验证
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	void safetyCodeVerify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	

}
