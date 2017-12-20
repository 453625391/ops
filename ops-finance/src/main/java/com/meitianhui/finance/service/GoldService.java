package com.meitianhui.finance.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface GoldService {

	/**
	 * 金币兑换现金
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleGoldExchangeCash(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
}
