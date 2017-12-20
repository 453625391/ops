package com.meitianhui.finance.dao;

import java.util.Map;

import com.meitianhui.finance.entity.FDCashDailyAccountMember;
import com.meitianhui.finance.entity.FDGoldDailyAccountMember;
import com.meitianhui.finance.entity.FDMemberCashLog;
import com.meitianhui.finance.entity.FDMemberGoldLog;
import com.meitianhui.finance.entity.FDMemberPointLog;
import com.meitianhui.finance.entity.FDMemberVoucherLog;
import com.meitianhui.finance.entity.FDPointDailyAccountMember;
import com.meitianhui.finance.entity.FDVoucherDailyAccountMember;

public interface TransactionDao {
	
	/**
	 * 会员现金资产操作日志
	 * @param map
	 * @throws Exception
	 */
	public void insertFDMemberPointLog(FDMemberPointLog operateLog) throws Exception;
	
	/**
	 * 会员现金资产操作日志
	 * @param map
	 * @throws Exception
	 */
	public void insertFDMemberCashLog(FDMemberCashLog operateLog) throws Exception;
	
	/**
	 * 会员金币资产操作日志
	 * @param map
	 * @throws Exception
	 */
	public void insertFDMemberGoldLog(FDMemberGoldLog operateLog) throws Exception;
	
	/**
	 * 会员礼券资产操作日志
	 * @param map
	 * @throws Exception
	 */
	public void insertFDMemberVoucherLog(FDMemberVoucherLog operateLog) throws Exception;
	
	/**
	 * 	公司现金日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDCashDailyAccount(FDCashDailyAccountMember fDCashDailyAccountMember) throws Exception;
	
	/**
	 * 记录消费者现金日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDCashDailyAccountConsumer(FDCashDailyAccountMember fDCashDailyAccountMember) throws Exception;
	
	/**
	 * 记录加盟店现金日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDCashDailyAccountStore(FDCashDailyAccountMember fDCashDailyAccountMember) throws Exception;
	
	/**
	 * 供应商现金日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDCashDailyAccountSupplier(FDCashDailyAccountMember fDCashDailyAccountMember) throws Exception;
	
	/**
	 * 	公司礼券日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDVoucherDailyAccount(FDVoucherDailyAccountMember fDVoucherDailyAccountMember) throws Exception;
	
	/**
	 * 消费者礼券日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDVoucherDailyAccountConsumer(FDVoucherDailyAccountMember fDVoucherDailyAccountMember) throws Exception;
	
	/**
	 * 加盟店礼券日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDVoucherDailyAccountStore(FDVoucherDailyAccountMember fDVoucherDailyAccountMember) throws Exception;
	
	/**
	 * 供应商礼券日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDVoucherDailyAccountSupplier(FDVoucherDailyAccountMember fDVoucherDailyAccountMember) throws Exception;
	
	/**
	 * 公司金币日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDGoldDailyAccount(FDGoldDailyAccountMember fDGoldDailyAccountMember) throws Exception;
	
	/**
	 * 加盟店金币日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDGoldDailyAccountStore(FDGoldDailyAccountMember fDGoldDailyAccountMember) throws Exception;
	
	/**
	 * 加盟店金币日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDGoldDailyAccountConsumer(FDGoldDailyAccountMember fDGoldDailyAccountMember) throws Exception;
	
	/**
	 * 交易流程步骤
	 * @param map
	 * @throws Exception
	 */
	public void insertFDTransactionFlow(Map<String,Object> map) throws Exception;
	
	/**
	 * 交易日志
	 * @param map
	 * @throws Exception
	 */
	public void insertFDTransactionLog(Map<String,Object> map) throws Exception;
	
	/**
	 * 交易日志
	 * @param map
	 * @throws Exception
	 */
	public void insertFDTransactionErrorLog(Map<String,Object> map) throws Exception;
	
	/**
	 * 更新交易结果
	 * @param map
	 * @throws Exception
	 */
	public void updateFDTransactionsResult(Map<String,Object> map) throws Exception;
	
	
	/**
	 * 	公司积分日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDPointDailyAccount(FDPointDailyAccountMember fDPointDailyAccountMember) throws Exception;
	
	/**
	 * 记录消费者积分日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDPointDailyAccountConsumer(FDPointDailyAccountMember fDPointDailyAccountMember) throws Exception;
	
	/**
	 * 记录加盟店积分日记账
	 * @param map
	 * @throws Exception
	 */
	public void insertFDPointDailyAccountStore(FDPointDailyAccountMember fDPointDailyAccountMember) throws Exception;
}
