package com.meitianhui.member.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/**
 * 
 * @author mole
 * @date 2016年12月19日 下午2:59:01
 */
public interface MemberExternalAccountService {

	/**
	 * 创建账号绑定信息
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void memberExternalAccountCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询绑定账号信息(app)
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void memberExternalAccountListForAppFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询绑定账号信息(运营系统)
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void memberExternalAccountListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 更新绑定账号信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberExternalAccountEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 更新绑定账号信息(运营系统)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberExternalAccountForOpEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 删除绑定账号信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberExternalAccountDelete(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

}
