package com.meitianhui.finance.service;

import java.util.Map;

import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.finance.entity.FDTransactionsResult;

/**
 * 通知服务类
 * 
 * @author Tiny
 *
 */
public interface NotifyService {
	/**
	 * 财产变更短信通知
	 * 
	 * @param member_id
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void financeChangeSMSNotify(String member_id, String member_type_key, String msg);

	/**
	 * 财产变更App消息通知
	 * 
	 * @param member_id
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void financeChangeAppNotify(String member_id, String msg);

	/**
	 * 财产变更给app推送通知
	 * 
	 * @param member_id
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void pushAppNotify(final String data_source,String member_id, String msg);

	/**
	 * 财产变更给app推送消息
	 * 
	 * @param member_id
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void pushAppMessage(String member_id, String msg, Map<String, String> extrasMap);

	/**
	 * 财产变更给app推送消息
	 * 
	 * @param member_id
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void tradeNotify(FDTransactionsResult fDTransactionsResult);

}
