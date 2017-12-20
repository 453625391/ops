package com.meitianhui.infrastructure.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface UserSyncService {

	/**
	 * 用户密码信息同步
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleConsumerInfoSync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException,Exception;

	
}
