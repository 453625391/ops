package com.meitianhui.member.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/**
 * 社区导购
 * @author Tiny
 *
 */
public interface SaleAssistantService {
	/**
	 * 申请成为社区导购
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void handleSaleAssistantApply(Map<String, Object> paramsMap, ResultData result)
	 		throws BusinessException, SystemException, Exception;
	
	/**
	 * 店东邀请消费者成为社区导购
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void handleSaleAssistantInvite(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	

	/**
	 * 批准成为社区导购
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void handleSaleAssistantApprove(Map<String, Object> paramsMap, ResultData result)
	 		throws BusinessException, SystemException, Exception;
	

	/**
	 * 拒绝成为社区导购
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void handleSaleAssistantReject(Map<String, Object> paramsMap, ResultData result)
	 		throws BusinessException, SystemException, Exception;
	
	/**
	 * 消费者社区导购信息查询
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void saleAssistantForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 店东社区导购信息查询(已申请)
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void saleAssistantAppliedForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 店东社区导购信息查询(已批准)
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void saleAssistantApprovedForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 消费者取消社区导购身份
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void handleSaleAssistantForConsumerCancel(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 店东取消社区导购身份
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void handleSaleAssistantForStoresCancel(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 社区导购操作日志
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void saleAssistantHistoryFind(Map<String, Object> paramsMap, ResultData result)
	 		throws BusinessException, SystemException, Exception;


}
