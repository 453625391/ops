package com.meitianhui.goods.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;


/**
 * 
* @ClassName: GdActivityService  
* @Description: 权益活动
* @author tiny 
* @date 2017年2月20日 下午4:40:41  
*
 */
public interface GdActivityService {
	
	/**
	 * 创建会员权益活动
	* @Title: gdActivityCreate  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	void gdActivityCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 会员权益列表查询(运营)
	* @Title: gdActivityListFind  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	void gdActivityListForOpFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 会员权益列表查询(消费者)
	 * @Title: GdActivityListFind  
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	void gdActivityListForConsumerFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 删除权益活动
	* @Title: deleteGdActivity  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	void gdActivityCancel(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 领取权益活动商品
	 * 
	 * @Title: GdActivityListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	void handleGdActivityGet(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;

	/**
	 * 查询权益活动商品数量
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	void gdActivityCountFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;

	/**
	 * 查询会员权益活动商品详情
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	void gdActivityDetailFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;

}
