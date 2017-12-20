package com.meitianhui.member.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.member.entity.MDAssistantEvaluation;
import com.meitianhui.member.entity.MDMemberRecommend;
import com.meitianhui.member.entity.MDStoresActivityPromotion;
import com.meitianhui.member.entity.MDStoresServiceFee;
import com.meitianhui.member.entity.MDUserMember;

public interface MemberDao {


	/**
	 * 消费者和用户关系信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertMDUserMember(MDUserMember mDUserMember) throws Exception;

	/**
	 * 新增便利店优惠信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void insertMDStoresActivityPromotion(MDStoresActivityPromotion mDStoresActivityPromotion) throws Exception;

	/**
	 * 店东信息反馈
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void insertMDStoreFeedback(Map<String, Object> map) throws Exception;

	/**
	 * 推荐用户注册
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	void insertMDMemberRecommend(MDMemberRecommend mDMemberRecommend) throws Exception;
	
	/**
	 * 熟么商户入住
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void insertMDMerchant(Map<String, Object> map) throws Exception;
	
	/**
	 * 熟么商户入住日志
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void insertMDMerchantLog(Map<String, Object> map) throws Exception;

	/**
	 * 会员费记录
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertMDStoresServiceFee(MDStoresServiceFee mdStoresServiceFee) throws Exception;
	
	/**
	 * 会员费调度日志记录
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertMDStoresScheduleLog(Map<String, Object> map) throws Exception;
	
	/**
	 * 店东助手等级评估
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertMDAssistantEvaluation(MDAssistantEvaluation assistantEvaluation) throws Exception;

	/**
	 * 查询区域经纬度
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectMDArea(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 查询区域信息(区域信息导出)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectMDAreaCode(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询外部区域编码对应的每天惠的区域编码
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectMDAreaMapping(Map<String, Object> map) throws Exception;

	/**
	 * 查询消费者和会员信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MDUserMember> selectMDUserMember(Map<String, Object> map) throws Exception;

	
	
	/**
	 * 查询便利店的优惠信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MDStoresActivityPromotion> selectMDStoresActivityPromotion(Map<String, Object> map) throws Exception;
	
	/**
	 * 根据手机号查询是否是消费者
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectMDMemberRecommendByPhone(Map<String, Object> map) throws Exception;
	
	/**
	 * 推荐注册明细查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectMDMemberRecommend(Map<String, Object> map) throws Exception;
	
	/**
	 * 统计推荐人推荐注册的会员数
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectMDMemberRecommendCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询会员费记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<MDStoresServiceFee> selectMDStoresServiceFee(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询会员费记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> selectMDStoresScheduleLog(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询店东助手评价
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<MDAssistantEvaluation> selectMDAssistantEvaluation(Map<String, Object> map) throws Exception;
	
	/**
	 * 店东助手评价统计
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> selectMDAssistantEvaluationCount(Map<String, Object> map) throws Exception;

	/**
	 * 更新便利店优惠信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void updateMDStoresActivityPromotion(Map<String, Object> map) throws Exception;

	
	/**
	 * 更新熟么入住商户信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updateMDMerchant(Map<String, Object> map) throws Exception;
	
	/**
	 * 修改会员对应的用户id关系
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void deleteMDUserMember(Map<String, Object> map) throws Exception;


	/**
	 * 通过手机号和会员类型查询会员信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> callQueryMemberInfoByMobile(Map<String, Object> map) throws Exception;

}
