package com.meitianhui.infrastructure.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/**
 * 用户管理
 * 
 * @author mole.wang
 *
 */
public interface UserService {

	/**
	 * 消费者登陆
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException,SystemException
	 */
	void consumerLoginForOAuth(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 兑换中消费者信息查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException,SystemException
	 */
	void consumerInfoForOAuthFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员登陆信息更新
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException,SystemException
	 */
	void memberLoginLogUpdate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 手机号登陆
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException,SystemException
	 */
	void mobileLogin(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 消费者登陆
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException,SystemException
	 */
	void consumerLogin(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 业务员登陆
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	void salesmanLogin(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	/**
	 * 店东登陆
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException,SystemException
	 */
	void storeLogin(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 供应商登陆
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException,SystemException
	 */
	void supplierLogin(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 用户登出
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	void userLogout(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 用户校验
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void userValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 用户授权信息清除
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void userTokenClear(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 用户校验(无request_info)
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void userValidateNoRequestInfo(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	/**
	 * 用户查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void userFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 通过手机号查询用户和会员列表
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void memberTypeFindByMobile(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	/**
	 * 通过手机号或者用户账号校验用户的会员类型是否存在
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void memberTypeValidateByMobile(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	/**
	 * 通过手机号查询用户
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void userFindByMobile(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 通过手机号查询会员id
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void memberFindByMobile(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 消费者注册
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleConsumerUserRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	/**
	 * 同步用户注册
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleSyncUserRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	/**
	 * 用户密码修改
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleUserPasswordChange(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	/**
	 * 用户密码重置
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleUserPasswordReset(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	/**
	 * 用户信息编辑
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void userEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 支付密码设置
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handlePayPasswordSetting(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	/**
	 * 支付选项设置
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handlePaySecurityOptions(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	/**
	 * 支付密码验证
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void payPasswordValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	/**
	 * 用户登陆手机号变更
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleLoginMobileChange(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;
}
