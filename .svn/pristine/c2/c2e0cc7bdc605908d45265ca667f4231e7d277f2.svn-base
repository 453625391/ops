package com.meitianhui.member.service;

import java.math.BigDecimal;
import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface MemberService {

	/**
	 * 区域信息导出
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void areaExport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 推荐用户注册并成功下单获5积分
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleCustomerRecommendMemberRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 会员推荐用户注册成每天惠的会员记录
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleMemberRegisterRecommend(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员推荐注册查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberRegisterRecommendFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 根据member_id查
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberRegisterRecommendFindByMemberId(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 查询推荐人是否是消费者
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberRegisterRecommendFindIsConsumer(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 会员推荐注册统计
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberRegisterRecommendCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 推荐注册的会员的资产信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void recommendmemberAsssetList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	
	/**
	 * 通过手机号查询会员信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberInfoFindByMobile(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 通过会员id和会员类型查询会员信息(交易接口)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberInfoFindByMemberId(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 店东信息反馈
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleStoreFeedback(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员登陆账号查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberUserFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 清楚店东和会员之间的关系
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleMemberUserRemove(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员登陆账号创建
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleMemberUserCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 获取会员ID
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberIdFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 获取会员类型列表
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberTypeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 获取用户ID
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void userIdFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 获取用户信息获取
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void userInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * App消息通知保存入库
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void appMsgNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 获取会员通知消息
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void appMsgNotifyFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 和店东有关系的消费者列表(店东助手)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesRelConsumerList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 和店东有关的消费者的资产列表
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesRelConsumerAssetList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店会员费列表查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesServiceFeeListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 店东助手等级评估
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void assistantEvaluationCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 店东助手评估统计
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void assistantEvaluationCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 区域code查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleAreaCodeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 解除原有会员关系
	 * 
	 * @Title: userMemberRelRemove
	 * @param paramsMap
	 * @author tiny
	 */
	public void userMemberRelRemove(Map<String, Object> paramsMap);

	/**
	 * 清除缓存信息
	 * 
	 * @Title: clearCacheMemberInfo
	 * @param member_id
	 * @param mobile
	 * @param member_type_key
	 * @throws Exception
	 * @author tiny
	 */
	public void clearCacheMemberInfo(String member_id, String mobile, String member_type_key) throws Exception;

	/**
	 * 余额支付
	 * 
	 * @Title: balancePay
	 * @param member_id
	 * @param amount
	 * @param order_no
	 * @param detail
	 * @param out_trade_body
	 * @throws Exception
	 * @author tiny
	 */
	public void balancePay(String buyer_id, String seller_id, String payment_way_key, BigDecimal amount,
			String out_trade_no, String detail, Map<String, Object> out_trade_body) throws Exception;

}
