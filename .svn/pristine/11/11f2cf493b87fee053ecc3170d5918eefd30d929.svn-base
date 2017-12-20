package com.meitianhui.finance.service;

import java.util.Map;

import com.meitianhui.common.constant.PageParam;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface FinanceSyncService {

	/**
	 * 消费者礼券兑换
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void voucherExchange(String userPhone,String amount) throws BusinessException, SystemException, Exception;
	
	/**
	 * 消费者礼券充值
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void giftAccountsAPI(Map<String, Object> paramsMap) throws BusinessException, SystemException, Exception;
	
	/**
	 * 消费者礼券余额查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	String giftRechargeBalanceFind(String userPhone);
	
	/**
	 * 消费者礼券查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void giftRechargeLogFind(Map<String, Object> paramsMap,PageParam pageParam,ResultData result) throws BusinessException, SystemException, Exception;

}
