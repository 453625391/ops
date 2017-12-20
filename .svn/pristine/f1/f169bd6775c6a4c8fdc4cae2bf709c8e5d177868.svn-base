package com.meitianhui.order.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/**
 * 伙拼团活动
 * 
 * @ClassName: TsActivityService
 * @author tiny
 * @date 2017年2月27日 下午7:05:45
 *
 */
public interface TsActivityService {

	/**
	 * 资格验证
	 * 
	 * @Title: qualificationValidate
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void qualificationValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;


	/**
	 * 阶梯价伙拼团活动创建(消费者直接创建,不需要支付)
	 * 
	 * @Title: ladderTsActivityForConsumerCreate
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void ladderTsActivityForConsumerCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 阶梯价伙拼团活动创建
	 * 
	 * @Title: tsActivityCreate
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void ladderTsActivityCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 滑梯价伙拼团列表
	 * 
	 * @Title: tsActivityForConsumerListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsActivityListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 附近的伙拼团活动
	 * 
	 * @Title: nearbyTsActivityListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void nearbyTsActivityListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团活动编辑
	 * 
	 * @Title: tsActivityEdit
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsActivityEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团活动取消(已成团才能取消)
	 * 
	 * @Title: handleTsActivityCancelForOp
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void handleTsActivityCancelForOp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团活动取消
	 * 
	 * @Title: handleTsActivityCancelForConsumer
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void handleTsActivityCancelForConsumer(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团活动发货
	 * 
	 * @Title: handleTsActivityDeliver
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void handleTsActivityDeliver(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团活动收货
	 * 
	 * @Title: handleTsActivityDeliver
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void handleTsActivityReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 活动详情查询
	 * 
	 * @Title: tsActivityDetail
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsActivityDetail(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 参团时显示此活动的参与情况
	 * 
	 * @Title: tsActivityCountForH5
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsActivityCountForH5(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团活动列表(店东端)
	 * 
	 * @Title: tsActivityListForStoresPageFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsActivityListForStoresPageFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 发起的伙拼团列表
	 *
	 * @Title: sponsorTsActivityListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void sponsorTsActivityListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团参加列表
	 * 
	 * @Title: joinListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void joinListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 团购活动成功
	 * 
	 * @Title: activitySuccessForLadder
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void activitySuccessForLadder(String activity_id) throws BusinessException, SystemException, Exception;

	/**
	 * 团购活动失败
	 * 
	 * @Title: handleActivityFail
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void activityFailForLadder(String activity_id, String activityStatus, String activityRemark,
			String orderStatus, String orderRemark) throws BusinessException, SystemException, Exception;

	
	/**
	 * 赠送权益券
	 * 
	 * @Title: rewardGdBenefit
	 * @param member_id
	 * @param amount
	 * @author tiny
	 */
	public void rewardGdBenefit(final String member_id);

	
}
