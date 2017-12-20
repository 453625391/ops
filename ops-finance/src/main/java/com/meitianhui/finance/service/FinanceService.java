package com.meitianhui.finance.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface FinanceService {

	/**
	 * 初始化会员资产
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleInitMemberAsset(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员资产查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberAssetFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员资产列表查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberAssetListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员可用现金余额查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberUsableCashBalanceFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 店东收银
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeCashierPromotion(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员积分查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException  
	 * @throws SystemException  
	 */
	public void memberPointFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 会员积分创建  
	 * 
	 * @param paramMap    
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberPointEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	
	/**
	 * 会员积分操作日志
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberPointLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 会员现金操作日志
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberCashLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员金币操作日志
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberGoldLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员礼券操作日志
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberVoucherLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 加盟店现金交易统计
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeCashCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店礼券交易统计
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeVoucherBillCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店赠送礼券人数统计
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeVoucherRewardAccountCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 加盟店收银记录新增
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesCashierCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 加盟店收银记录查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesCashierFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员优惠券创建
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberCouponCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员优惠券信息查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberCouponFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员优惠券信息查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberIdBySkuCodeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员优惠券统计
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberCouponCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 会员优惠券状态编辑
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberCouponStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 过期的的优惠券状态更新
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void disabledCouponStatusUpdate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 和店东发生交易的消费者列表
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void tradeConsumerListForStores(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 和店东发生交易的消费者列表(会员类型)
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void tradeConsumerListForMemberList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 消费者礼券交易流水
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void consumerVoucherBill(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店礼券交易流水
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeVoucherBill(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 店东为消费者解冻余额
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleStoresUnfreezeBalanceForConsumer(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	
	/**
	 * 交易异常数据
	 * 
	 * @Title: billCheckLogListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void billCheckLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店的店东的佣金明细列表查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void storesCashCommissionLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 新增门店的店东佣金
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void storesCashCommissionCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 修改门店的店东佣金
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void storesCashCommissionEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
}
