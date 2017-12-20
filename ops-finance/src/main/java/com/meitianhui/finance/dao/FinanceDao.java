package com.meitianhui.finance.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.finance.entity.FDCashCommissionStore;
import com.meitianhui.finance.entity.FDCashDailyAccountMember;
import com.meitianhui.finance.entity.FDGoldDailyAccountMember;
import com.meitianhui.finance.entity.FDMemberAsset;
import com.meitianhui.finance.entity.FDMemberCashLog;
import com.meitianhui.finance.entity.FDMemberGoldLog;
import com.meitianhui.finance.entity.FDMemberPointLog;
import com.meitianhui.finance.entity.FDMemberVoucherLog;
import com.meitianhui.finance.entity.FDStoresCashier;
import com.meitianhui.finance.entity.FDTransactions;
import com.meitianhui.finance.entity.FDTransactionsResult;
import com.meitianhui.finance.entity.FDVoucher;
import com.meitianhui.finance.entity.FDVoucherDailyAccountMember;
import com.meitianhui.finance.entity.FdMemberAssetCoupon;

public interface FinanceDao {

	/**
	 * 新增交易信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertFDTransactions(FDTransactions fDTransactions) throws Exception;

	/**
	 * 新增交易结果信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertFDTransactionsResult(FDTransactionsResult fDTransactionsResult) throws Exception;

	/**
	 * 新增会员资产信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertFDMemberAsset(FDMemberAsset fDMemberAsset) throws Exception;

	/**
	 * 新增门店收银流水
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertFDStoresCashier(FDStoresCashier fDStoresCashier) throws Exception;

	/**
	 * 新增会员优惠券信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertFdMemberAssetCoupon(FdMemberAssetCoupon fdMemberAssetCoupon) throws Exception;

	
	/**
	 * 添加会员积分日志
	 * 
	 * @param tempMap
	 * @throws Exception
	 */
	public void insertFdMemberPointLog(Map<String, Object> tempMap) throws Exception;

	/**
	 * 查询交易信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public FDTransactions selectFDTransactions(Map<String, Object> paramMap) throws Exception;
	/**
	 * 查询交易信息(返回对象集合)
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<FDTransactions> selectListFDTransactions(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询交易结果
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<FDTransactionsResult> selectFDTransactionsResult(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询消费者礼券交易流水
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<FDVoucherDailyAccountMember> selectFDVoucherDailyAccountConsumer(Map<String, Object> paramMap)
			throws Exception;

	/**
	 * 查询会员现金变更日志
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<FDMemberCashLog> selectFDMemberCashLog(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询会员金币变更日志
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<FDMemberGoldLog> selectFDMemberGoldLog(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询会员积分变更日志
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<FDMemberPointLog> selectFDMemberPointLog(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 查询会员礼券变更日志
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<FDMemberVoucherLog> selectFDMemberVoucherLog(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询门店收银交易统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectStoreCashierCount(Map<String, Object> paramMap) throws Exception;

	/**
	 * 门店礼券交易统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectFDVoucherDailyAccountStoreCount(Map<String, Object> paramMap) throws Exception;

	/**
	 * 门店赠送礼券人数统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectFDStoreVoucherRewardAccountCount(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询门店礼券交易流水
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectFDVoucherAmountCount(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询会员资产
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public FDMemberAsset selectFDMemberAsset(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询会员资产列表
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<FDMemberAsset> selectFDMemberAssetList(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询礼券信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public FDVoucher selectFDVoucher(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询门店的收银流水
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<FDStoresCashier> selectFDStoresCashier(Map<String, Object> map) throws Exception;

	/**
	 * 查询会员银行卡信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<FdMemberAssetCoupon> selectFdMemberAssetCoupon(Map<String, Object> map) throws Exception;

	/**
	 * 会员优惠券统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectFdMemberAssetCouponCount(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询和店东有交易的消费者信息列表
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectTradeConsumerListForStores(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询门店礼券交易流水
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<FDVoucherDailyAccountMember> selectFDVoucherDailyAccountStore(Map<String, Object> paramMap)
			throws Exception;

	/**
	 * 记录会员资产信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public Integer updateFDMemberAsset(Map<String, Object> map) throws Exception;

	/**
	 * 更新礼券信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void updateFDVoucher(Map<String, Object> map) throws Exception;

	/**
	 * 更新会员优惠券信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void updateFdMemberAssetCoupon(Map<String, Object> map) throws Exception;

	/**
	 * 会员过期的优惠券设置为作废
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void updateDisabledCouponAssetStatus(Map<String, Object> map) throws Exception;

	/**
	 * 查询消费者现金交易流水
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public List<FDCashDailyAccountMember> selectFDCashDailyAccountConsumer(Map<String, Object> paramMap)
			throws Exception;

	/**
	 * 查询门店现金交易流水
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public List<FDCashDailyAccountMember> selectFDCashDailyAccountStore(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询门店金币交易流水
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public List<FDGoldDailyAccountMember> selectFDGoldDailyAccountStore(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询消费者金币交易流水
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public List<FDGoldDailyAccountMember> selectFDGoldDailyAccountConsumer(Map<String, Object> paramMap)
			throws Exception;

	/**
	 * 查询交易对账日志
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectBillCheckLog(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 查询门店佣金
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectStoresCashCommission(Map<String, Object> map) throws Exception;
	
	/**
	 * 	添加门店佣金
	 * 
	 * @param fDTransactions
	 * @throws Exception
	 */
	public void insertStoresCashCommission(FDCashCommissionStore fdCashCommissionStore) throws Exception;
	
	/**
	 * 修改门店佣金
	 * 
	 * @param fdCashCommissionStore
	 * @throws Exception
	 */
	public void updateStoresCashCommission(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询门店佣金详细列表（日志表）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectStoresCashCommissionLogList(Map<String, Object> map) throws Exception;
	
	/**
	 * 	添加门店佣金日志
	 * 
	 * @param fDTransactions
	 * @throws Exception
	 */
	public void insertStoresCashCommissionLog(Map<String, Object> map) throws Exception;
	
}
