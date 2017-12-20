package com.meitianhui.goods.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/**
 * 
* @ClassName: GdBenefitService  
* @Description: 会员权益服务层
* @author tiny 
* @date 2017年2月20日 下午3:53:49  
*
 */
public interface GdBenefitService {
	
	/**
	 * 创建会员权益
	* @Title: gdBenefitCreate  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	void gdBenefitCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 会员权益列表查询(消费者)
	 * @Title: gdBenefitListFind  
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	void usableGdBenefitListForConsumerFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 免单券支付
	 * @Title: handleFreeCouponPay  
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	void handleFreeCouponPay(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 会员积分兑换商品
	 * @Title: handleMemberPointPay  
	 * @param paramsMap  
	 * @param result    
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	void handleMemberPointPay(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;

	/**
	 * 会员权益日志查询(消费者)
	 * @Title: gdBenefitListFind  
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	void gdBenefitLogListForConsumerFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 
	* @Title: gdBenefitNumCount  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	void usableGdBenefitCount(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	

	/**
	 * 会员权益转让
	 * 
	 * @Title: gdBenefitListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	void handleGdBenefitTransfer(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
}
