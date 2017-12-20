package com.meitianhui.order.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/**
 * 伙拼团
 * 
 * @ClassName: TsOrderService
 * @author tiny
 * @date 2017年2月27日 下午7:08:13
 *
 */
public interface TsOrderService {

	/**
	 * 免单券支付
	 * 
	 * @Title: handleFreeCouponPay
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void handleFreeCouponPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团订单创建验证(一个人只能参加一次)
	* @Title: ladderTsOrderValidate  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	public void ladderTsOrderValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 
	 * 伙拼团订单创建(阶梯价格)
	 * 
	 * @Title: ladderTsOrderCreate
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void ladderTsOrderCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 参加的伙拼团列表
	 * 
	 * @Title: tsOrderListForOpFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 活动的的订单列表
	 * 
	 * @Title: tsOrderListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 参加的伙拼团列表
	 * 
	 * @Title: tsOrderListForConsumerFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 参加的伙拼团列表
	 * 
	 * @Title: joinTsActivityListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderDetailForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团订单取消
	 * 
	 * @Title: handleTsOrderCancelForConsumer
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void handleTsOrderCancelForConsumer(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团订单取消
	 * 
	 * @Title: handleTsOrderCancelForOp
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderCancelForOp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团订单收货
	 * 
	 * @Title: handleTsOrderReceived
	 * @param order_id
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void handleTsOrderReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团订单结算统计
	 * 
	 * @Title: tsOrderSettlementCount
	 * @param order_id
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderSettleCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 伙拼团订单结算列表查询
	 * 
	 * @Title: tsOrderSettleListForOpFind
	 * @param order_id
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderSettleListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 伙拼团订单结算
	 * 
	 * @Title: handleTsOrderSettle
	 * @param order_id
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void handleTsOrderSettle(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/***
	 * 检测活动订单数量
	 * 
	 * @Title: checkActivityOrderNum
	 * @param activity_id
	 * @author tiny
	 */
	public void checkActivityOrderNum(final String activity_id);
	
	/**
	 * 便利店自动销售机销售量统计(店东特卖)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void tsOrderForStoresSaleCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

}
