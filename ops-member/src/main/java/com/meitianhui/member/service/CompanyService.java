package com.meitianhui.member.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface CompanyService {

	/**
	 * 公司信息查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void companyFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;


	/**
	 * 公司信息同步
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleMdCompanySync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	
}
