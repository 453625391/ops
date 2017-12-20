package com.meitianhui.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.CommonConstant;
import com.meitianhui.common.constant.PageParam;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.MoneyUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.finance.constant.Constant;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.dao.FinanceDao;
import com.meitianhui.finance.entity.FDCashCommissionStore;
import com.meitianhui.finance.entity.FDMemberAsset;
import com.meitianhui.finance.entity.FDMemberCashLog;
import com.meitianhui.finance.entity.FDMemberGoldLog;
import com.meitianhui.finance.entity.FDMemberPointLog;
import com.meitianhui.finance.entity.FDMemberVoucherLog;
import com.meitianhui.finance.entity.FDStoresCashier;
import com.meitianhui.finance.entity.FDTransactionsResult;
import com.meitianhui.finance.entity.FDVoucherDailyAccountMember;
import com.meitianhui.finance.entity.FdMemberAssetCoupon;
import com.meitianhui.finance.service.FinanceService;
import com.meitianhui.finance.service.FinanceSyncService;
import com.meitianhui.finance.service.NotifyService;
import com.meitianhui.finance.service.TradeService;

/**
 * 金融服务的服务层
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
@Service
public class FinanceServiceImpl implements FinanceService {

	@Autowired
	public FinanceDao financeDao;
	@Autowired
	private TradeService tradeService;
	@Autowired
	public NotifyService notifyService;
	@Autowired
	private FinanceSyncService financeSyncService;

	@Autowired
	private DocUtil docUtil;

	@Override
	public void handleInitMemberAsset(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("member_id", paramsMap.get("member_id"));
			queryMap.put("member_type_key", paramsMap.get("member_type_key"));
			FDMemberAsset fDMemberAsset = financeDao.selectFDMemberAsset(queryMap);
			if (fDMemberAsset == null) {
				fDMemberAsset = new FDMemberAsset();
				BeanConvertUtil.mapToBean(fDMemberAsset, paramsMap);
				fDMemberAsset.setAsset_id(IDUtil.getUUID());
				fDMemberAsset.setCreated_date(new Date());
				financeDao.insertFDMemberAsset(fDMemberAsset);
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storeCashierPromotion(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "data_source", "payment_way_key", "buyer_id", "seller_id", "rebate_cash",
							"pay_amount", "reward_gold", "out_trade_no", "out_trade_body", "detail" });
			String payment_way_key = paramsMap.get("payment_way_key") + "";

			// 检测公司是否入账
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("buyer_id", paramsMap.get("buyer_id"));
			tempMap.put("seller_id", Constant.MEMBER_ID_MTH);
			tempMap.put("out_trade_no", paramsMap.get("out_trade_no"));
			tempMap.put("business_type_key", Constant.BUSINESS_TYPE_BALANCEPAY);
			tempMap.put("payment_way_key", Constant.PAYMENT_WAY_05);
			List<FDTransactionsResult> fDTransactionsResultList = financeDao.selectFDTransactionsResult(tempMap);
			// 验证消费者是否支付金额到平台账号上去
			if (fDTransactionsResultList.size() <= 0) {
				return;
			}

			// 如果支付方式不为零钱，则不进行返现操作
			if (!payment_way_key.equals(Constant.PAYMENT_WAY_05)) {
				return;
			}
			BigDecimal pay_amount = new BigDecimal(paramsMap.get("pay_amount") + "");
			BigDecimal rebate_cash = new BigDecimal(paramsMap.get("rebate_cash") + "");
			// 店东应得的钱
			BigDecimal stores_amount = MoneyUtil.moneySub(pay_amount, rebate_cash);
			// 支付店东应该得到的钱
			if (stores_amount.compareTo(BigDecimal.ZERO) > 0) {
				Map<String, Object> balanceParams = new HashMap<String, Object>();
				balanceParams.put("data_source", paramsMap.get("data_source"));
				balanceParams.put("out_member_id", Constant.MEMBER_ID_MTH);
				balanceParams.put("in_member_id", paramsMap.get("seller_id"));
				balanceParams.put("payment_way_key", "ZFFS_05");
				balanceParams.put("amount", stores_amount + "");
				balanceParams.put("out_trade_no", paramsMap.get("out_trade_no"));
				balanceParams.put("out_trade_body", paramsMap.get("out_trade_body"));
				balanceParams.put("detail", paramsMap.get("detail"));
				tradeService.balancePay(balanceParams, result);
			}

			// 对消费者进行返现,支付金额要大于返现金额且返现金额不小于0
			if (pay_amount.compareTo(rebate_cash) > 0 && rebate_cash.compareTo(BigDecimal.ZERO) > 0) {
				Map<String, Object> balanceParams = new HashMap<String, Object>();
				balanceParams.put("data_source", paramsMap.get("data_source"));
				balanceParams.put("out_member_id", Constant.MEMBER_ID_MTH);
				balanceParams.put("in_member_id", paramsMap.get("buyer_id"));
				balanceParams.put("payment_way_key", "ZFFS_05");
				balanceParams.put("amount", rebate_cash + "");
				balanceParams.put("out_trade_no", paramsMap.get("out_trade_no"));
				balanceParams.put("out_trade_body", paramsMap.get("out_trade_body"));
				balanceParams.put("detail", "商家返利");
				tradeService.balancePay(balanceParams, result);
			}

			// 对消费者进行赠送金币
			BigDecimal reward_gold = new BigDecimal(paramsMap.get("reward_gold") + "");
			if (reward_gold.compareTo(BigDecimal.ZERO) > 0) {
				Map<String, Object> balanceParams = new HashMap<String, Object>();
				balanceParams.put("data_source", paramsMap.get("data_source"));
				balanceParams.put("out_member_id", Constant.MEMBER_ID_MTH);
				balanceParams.put("in_member_id", paramsMap.get("buyer_id"));
				balanceParams.put("payment_way_key", "ZFFS_08");
				balanceParams.put("amount", reward_gold + "");
				balanceParams.put("out_trade_no", paramsMap.get("out_trade_no"));
				balanceParams.put("out_trade_body", paramsMap.get("out_trade_body"));
				balanceParams.put("detail", "平台返利");
				tradeService.balancePay(balanceParams, result);
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberAssetFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			String member_id = StringUtil.formatStr(paramsMap.get("member_id"));
			List<String> list = StringUtil.str2List(member_id, ",");
			if (list.size() > 1) {
				paramsMap.remove("member_id");
				paramsMap.put("member_id_in", list);
			}
			FDMemberAsset fDMemberAsset = financeDao.selectFDMemberAsset(paramsMap);
			if (fDMemberAsset == null) {
				throw new BusinessException(RspCode.MEMBER_NO_EXIST, RspCode.MSG.get(RspCode.MEMBER_NO_EXIST));
			}
			Map<String, Object> resultData = new HashMap<String, Object>();
			resultData.put("member_id", fDMemberAsset.getMember_id());
			resultData.put("cash_balance", fDMemberAsset.getCash_balance() + "");
			resultData.put("cash_froze", fDMemberAsset.getCash_froze() + "");
			// 消费者计算实际的礼券余额
			BigDecimal voucher_balance = fDMemberAsset.getVoucher_balance();
			if (fDMemberAsset.getMember_type_key().equals(Constant.MEMBER_TYPE_CONSUMER)) {
				voucher_balance = new BigDecimal("0");
				/*
				 * String member_service_url =
				 * PropertiesConfigUtil.getProperty("member_service_url");
				 * Map<String, String> reqParams = new HashMap<String,
				 * String>(); Map<String, Object> bizParams = new
				 * HashMap<String, Object>(); reqParams.put("service",
				 * "member.memberInfoFindByMemberId");
				 * bizParams.put("member_id", member_id);
				 * bizParams.put("member_type_key", "consumer");
				 * reqParams.put("params", FastJsonUtil.toJson(bizParams));
				 * String resultStr = HttpClientUtil.post(member_service_url,
				 * reqParams); Map<String, Object> resultMap =
				 * FastJsonUtil.jsonToMap(resultStr); if (((String)
				 * resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				 * Map<String, Object> dateMap = (Map<String, Object>)
				 * resultMap.get("data"); String mobile =
				 * (StringUtil.formatStr(dateMap.get("mobile"))); if
				 * (StringUtils.isNotEmpty(mobile)) { voucher_balance = new
				 * BigDecimal(financeSyncService.giftRechargeBalanceFind(mobile)
				 * ); } } else { voucher_balance = new BigDecimal("0"); }
				 */

			} else if (fDMemberAsset.getMember_type_key().equals(Constant.MEMBER_TYPE_STORES)) {
				// 因为只有门店类型的会员才会有佣金 所以另外写这个逻辑。
//				Map<String, Object> tempMap = new HashMap<String, Object>();
//				tempMap.put("store_id", member_id);
//				Map<String, Object> tempCashCommissionMap = financeDao.selectStoresCashCommission(tempMap);
//				if (null == tempCashCommissionMap) {
//					resultData.put("commission_cash", "0");// 佣金金额
//				} else {
//					resultData.put("commission_cash", tempCashCommissionMap.get("commission_cash"));// 佣金金额
//				}
			}
			// modify by dingshuo 2016-10-11 添加信用余额，数据库暂时没有值
			resultData.put("credit_balance", "0");
			resultData.put("voucher_balance", voucher_balance.intValue() + "");
			resultData.put("bonus", fDMemberAsset.getBonus() + "");
			resultData.put("gold", fDMemberAsset.getGold() + "");
			resultData.put("experience", fDMemberAsset.getExperience() + "");
			result.setResultData(resultData);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberAssetListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			String member_id = StringUtil.formatStr(paramsMap.get("member_id"));
			List<String> list = StringUtil.str2List(member_id, ",");
			if (list.size() > 1) {
				paramsMap.remove("member_id");
				paramsMap.put("member_id_in", list);
			}
			List<FDMemberAsset> fDMemberAssetList = financeDao.selectFDMemberAssetList(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (FDMemberAsset fDMemberAsset : fDMemberAssetList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("member_id", fDMemberAsset.getMember_id());
				tempMap.put("cash_balance", fDMemberAsset.getCash_balance() + "");
				tempMap.put("gold", fDMemberAsset.getGold() + "");
				resultList.add(tempMap);
			}
			Map<String, Object> resultData = new HashMap<String, Object>();
			resultData.put("list", resultList);
			result.setResultData(resultData);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberUsableCashBalanceFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			FDMemberAsset fDMemberAsset = financeDao.selectFDMemberAsset(paramsMap);
			if (fDMemberAsset == null) {
				throw new BusinessException(RspCode.MEMBER_NO_EXIST, RspCode.MSG.get(RspCode.MEMBER_NO_EXIST));
			}
			Map<String, Object> resultData = new HashMap<String, Object>();
			BigDecimal cash_balance = fDMemberAsset.getCash_balance();
			BigDecimal cash_froze = fDMemberAsset.getCash_froze();
			resultData.put("usable_cash_balance", MoneyUtil.moneySub(cash_balance, cash_froze) + "");
			result.setResultData(resultData);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCashLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			List<FDMemberCashLog> list = financeDao.selectFDMemberCashLog(paramsMap);
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for (FDMemberCashLog log : list) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("category", log.getCategory());
				tempMap.put("tracked_date", DateUtil.date2Str(log.getTracked_date(), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("amount", log.getAmount() + "");
				// 解析备注信息
				String remark = log.getRemark();
				if (!StringUtils.isEmpty(remark)) {
					Map<String, Object> remarkMap = FastJsonUtil.jsonToMap(remark);
					tempMap.put("transaction_no", StringUtil.formatStr(remarkMap.get("transaction_no")));
					tempMap.put("detail", StringUtil.formatStr(remarkMap.get("detail")));
					tempMap.put("payment_way_key", StringUtil.formatStr(remarkMap.get("payment_way_key")));
					tempMap.put("business_type_key", StringUtil.formatStr(remarkMap.get("business_type_key")));
					tempMap.put("transaction_member_name",
							StringUtil.formatStr(remarkMap.get("transaction_member_name")));
					tempMap.put("transaction_member_contact",
							StringUtil.formatStr(remarkMap.get("transaction_member_contact")));
				} else {
					tempMap.put("transaction_no", "");
					tempMap.put("detail", "");
					tempMap.put("payment_way_key", "");
					tempMap.put("business_type_key", "");
					tempMap.put("transaction_member_name", "");
					tempMap.put("transaction_member_contact", "");
				}
				resultList.add(tempMap);
			}
			Map<String, Object> resultDateMap = new HashMap<String, Object>();
			resultDateMap.put("list", resultList);
			result.setResultData(resultDateMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	public void memberPointLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			List<FDMemberPointLog> list = financeDao.selectFDMemberPointLog(paramsMap);
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for (FDMemberPointLog log : list) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("category", log.getCategory());
				tempMap.put("tracked_date", DateUtil.date2Str(log.getTracked_date(), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("amount", log.getAmount() + "");
				// 解析备注信息
				String remark = log.getRemark();
				if (!StringUtils.isEmpty(remark)) {
					Map<String, Object> remarkMap = FastJsonUtil.jsonToMap(remark);
					tempMap.put("transaction_no", StringUtil.formatStr(remarkMap.get("transaction_no")));
					tempMap.put("detail", StringUtil.formatStr(remarkMap.get("detail")));
					tempMap.put("payment_way_key", StringUtil.formatStr(remarkMap.get("payment_way_key")));
					tempMap.put("business_type_key", StringUtil.formatStr(remarkMap.get("business_type_key")));
					tempMap.put("transaction_member_name",
							StringUtil.formatStr(remarkMap.get("transaction_member_name")));
					tempMap.put("transaction_member_contact",
							StringUtil.formatStr(remarkMap.get("transaction_member_contact")));
				} else {
					tempMap.put("transaction_no", "");
					tempMap.put("detail", "");
					tempMap.put("payment_way_key", "");
					tempMap.put("business_type_key", "");
					tempMap.put("transaction_member_name", "");
					tempMap.put("transaction_member_contact", "");
				}
				resultList.add(tempMap);
			}
			Map<String, Object> resultDateMap = new HashMap<String, Object>();
			resultDateMap.put("list", resultList);
			result.setResultData(resultDateMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@Override
	public void memberGoldLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			List<FDMemberGoldLog> list = financeDao.selectFDMemberGoldLog(paramsMap);
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for (FDMemberGoldLog log : list) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("category", log.getCategory());
				tempMap.put("tracked_date", DateUtil.date2Str(log.getTracked_date(), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("amount", log.getAmount() + "");
				// 解析备注信息
				String remark = log.getRemark();
				if (!StringUtils.isEmpty(remark)) {
					Map<String, Object> remarkMap = FastJsonUtil.jsonToMap(remark);
					tempMap.put("transaction_no", StringUtil.formatStr(remarkMap.get("transaction_no")));
					tempMap.put("detail", StringUtil.formatStr(remarkMap.get("detail")));
					tempMap.put("payment_way_key", StringUtil.formatStr(remarkMap.get("payment_way_key")));
					tempMap.put("business_type_key", StringUtil.formatStr(remarkMap.get("business_type_key")));
					tempMap.put("transaction_member_name",
							StringUtil.formatStr(remarkMap.get("transaction_member_name")));
					tempMap.put("transaction_member_contact",
							StringUtil.formatStr(remarkMap.get("transaction_member_contact")));
				} else {
					tempMap.put("transaction_no", "");
					tempMap.put("detail", "");
					tempMap.put("payment_way_key", "");
					tempMap.put("business_type_key", "");
					tempMap.put("transaction_member_name", "");
					tempMap.put("transaction_member_contact", "");
				}
				resultList.add(tempMap);
			}
			Map<String, Object> resultDateMap = new HashMap<String, Object>();
			resultDateMap.put("list", resultList);
			result.setResultData(resultDateMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberVoucherLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			List<FDMemberVoucherLog> list = financeDao.selectFDMemberVoucherLog(paramsMap);
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for (FDMemberVoucherLog log : list) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("category", log.getCategory());
				tempMap.put("tracked_date", DateUtil.date2Str(log.getTracked_date(), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("amount", log.getAmount() + "");
				// 解析备注信息
				String remark = log.getRemark();
				if (!StringUtils.isEmpty(remark)) {
					Map<String, Object> remarkMap = FastJsonUtil.jsonToMap(remark);
					tempMap.put("transaction_no", StringUtil.formatStr(remarkMap.get("transaction_no")));
					tempMap.put("detail", StringUtil.formatStr(remarkMap.get("detail")));
					tempMap.put("payment_way_key", StringUtil.formatStr(remarkMap.get("payment_way_key")));
					tempMap.put("business_type_key", StringUtil.formatStr(remarkMap.get("business_type_key")));
					tempMap.put("transaction_member_name",
							StringUtil.formatStr(remarkMap.get("transaction_member_name")));
					tempMap.put("transaction_member_contact",
							StringUtil.formatStr(remarkMap.get("transaction_member_contact")));
				} else {
					tempMap.put("transaction_no", "");
					tempMap.put("detail", "");
					tempMap.put("payment_way_key", "");
					tempMap.put("business_type_key", "");
					tempMap.put("transaction_member_name", "");
					tempMap.put("transaction_member_contact", "");
				}
				resultList.add(tempMap);
			}
			Map<String, Object> resultDateMap = new HashMap<String, Object>();
			resultDateMap.put("list", resultList);
			result.setResultData(resultDateMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberPointEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 如果存在则加5分 如果不存在则在其基础上加5分
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id","member_type_key", "point_values", "booking_mark" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String member_id = paramsMap.get("member_id") + "";
			int point_values = Integer.parseInt(paramsMap.get("point_values").toString());

			// 向资产表加积分
			tempMap.put("member_id", member_id);
			tempMap.put("trade_bonus", point_values);
			financeDao.updateFDMemberAsset(tempMap);
			
			// 把记录添加到一条记录
			tempMap.clear();
			tempMap.put("log_id", IDUtil.getUUID());
			tempMap.put("member_type_key", paramsMap.get("member_type_key") + "");
			tempMap.put("member_id", member_id);
			tempMap.put("category", paramsMap.get("booking_mark") + "");
			
			tempMap.put("pre_balance", "0");
			tempMap.put("amount", point_values);
			tempMap.put("balance", "0");
			
			tempMap.put("tracked_date", new Date());
			tempMap.put("remark", paramsMap.get("remark") + "");
			financeDao.insertFdMemberPointLog(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberPointFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
		FDMemberAsset memberPoint = financeDao.selectFDMemberAsset(paramsMap);
		if (memberPoint != null) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("point_values", memberPoint.getBonus());
			result.setResultData(tempMap);
		}
	}

	@Override
	public void storeCashCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "transaction_date" });
			Map<String, Object> resultMap = financeDao.selectStoreCashierCount(paramsMap);
			resultMap.put("in_sum", resultMap.get("in_sum") == null ? "0.00" : resultMap.get("in_sum") + "");
			resultMap.put("in_case_sum",
					resultMap.get("in_case_sum") == null ? "0.00" : resultMap.get("in_case_sum") + "");
			resultMap.put("num_count", resultMap.get("num_count") + "");
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storeVoucherBillCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });

			String business_type_key = StringUtil.formatStr(paramsMap.get("business_type_key"));
			if (!"".equals(business_type_key)) {
				List<String> list = StringUtil.str2List(business_type_key, ",");
				if (list.size() > 1) {
					paramsMap.remove("business_type_key");
					paramsMap.put("business_type_in", list);
				}
			}
			Map<String, Object> countsMap = financeDao.selectFDVoucherDailyAccountStoreCount(paramsMap);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("count_num", "0");
			if (null != countsMap) {
				resultMap.put("count_num", countsMap.get("count_num") + "");
			}
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storeVoucherRewardAccountCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			paramsMap.put("transaction_date_start", DateUtil.getFormatDate(DateUtil.fmt_yyyyMM) + "-01");
			paramsMap.put("transaction_date_end", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMdd));
			Map<String, Object> countsMap = financeDao.selectFDStoreVoucherRewardAccountCount(paramsMap);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("count_num", "0");
			if (null != countsMap) {
				resultMap.put("count_num", countsMap.get("count_num") + "");
			}
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storesCashierCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "flow_no", "stores_id", "amount", "discount_amount",
					"pay_amount", "payment_way_key", "cashier_id" });
			FDStoresCashier fDStoreCashier = new FDStoresCashier();
			BeanConvertUtil.mapToBean(fDStoreCashier, paramsMap);
			fDStoreCashier.setFlow_id(IDUtil.getUUID());
			fDStoreCashier.setCreated_date(new Date());
			if (paramsMap.get("reduce_amount") == null) {
				fDStoreCashier.setReduce_amount(new BigDecimal("0.00"));
			}
			if (paramsMap.get("rebate_cash") == null) {
				fDStoreCashier.setRebate_cash(new BigDecimal("0.00"));
			}
			if (paramsMap.get("reward_gold") == null) {
				fDStoreCashier.setReward_gold(0);
			}
			financeDao.insertFDStoresCashier(fDStoreCashier);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storesCashierFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			List<FDStoresCashier> storeCashierList = financeDao.selectFDStoresCashier(paramsMap);
			List<Map<String, Object>> resultlist = new LinkedList<Map<String, Object>>();
			for (FDStoresCashier storeCashier : storeCashierList) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("flow_no", storeCashier.getFlow_no());
				resultMap.put("amount", storeCashier.getAmount() + "");
				resultMap.put("discount_amount", storeCashier.getDiscount_amount() + "");
				resultMap.put("reduce_amount", storeCashier.getReduce_amount() + "");
				resultMap.put("reward_voucher", "0.00");
				resultMap.put("rebate_cash", storeCashier.getRebate_cash() + "");
				resultMap.put("reward_gold", storeCashier.getReward_gold() + "");
				resultMap.put("pay_amount", storeCashier.getPay_amount() + "");
				resultMap.put("payment_way_key", storeCashier.getPayment_way_key());
				resultMap.put("json_data", StringUtil.formatStr(storeCashier.getJson_data()));
				resultMap.put("cashier_id", storeCashier.getCashier_id());
				resultMap.put("created_date",
						DateUtil.date2Str(storeCashier.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
				resultlist.add(resultMap);
			}
			result.setResultData(resultlist);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCouponCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "sku_id", "item_id",
					"sku_code", "title", "expired_date", "status" });
			FdMemberAssetCoupon fdMemberAssetCoupon = new FdMemberAssetCoupon();
			BeanConvertUtil.mapToBean(fdMemberAssetCoupon, paramsMap);
			fdMemberAssetCoupon.setAsset_id(IDUtil.getUUID());
			fdMemberAssetCoupon.setCreated_date(new Date());
			financeDao.insertFdMemberAssetCoupon(fdMemberAssetCoupon);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCouponStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "sku_code", "status" });
			financeDao.updateFdMemberAssetCoupon(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCouponFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<FdMemberAssetCoupon> fdMemberAssetCouponList = financeDao.selectFdMemberAssetCoupon(paramsMap);
			List<String> doc_ids = new ArrayList<String>();
			for (FdMemberAssetCoupon fdMemberAssetCoupon : fdMemberAssetCouponList) {
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("item_id", fdMemberAssetCoupon.getItem_id());
				temp.put("sku_id", fdMemberAssetCoupon.getSku_id());
				temp.put("sku_code", fdMemberAssetCoupon.getSku_code());
				temp.put("title", fdMemberAssetCoupon.getTitle());
				temp.put("status", fdMemberAssetCoupon.getStatus());
				temp.put("expired_date",
						DateUtil.date2Str(fdMemberAssetCoupon.getExpired_date(), DateUtil.fmt_yyyyMMddHHmmss));
				temp.put("pic_path", fdMemberAssetCoupon.getPic_path());
				temp.put("created_date",
						DateUtil.date2Str(fdMemberAssetCoupon.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
				temp.put("remark", StringUtil.formatStr(fdMemberAssetCoupon.getRemark()));
				// 查询商品新
				String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
				Map<String, String> reqParams = new HashMap<String, String>();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				reqParams.put("service", "goods.couponDetailFind");
				paramMap.put("sku_id", fdMemberAssetCoupon.getSku_id());
				reqParams.put("params", FastJsonUtil.toJson(paramMap));
				String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
				Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				Map<String, Object> data = (Map<String, Object>) resultMap.get("data");
				temp.put("category_id", StringUtil.formatStr(data.get("category_id")));
				temp.put("coupon_prop", StringUtil.formatStr(data.get("coupon_prop")));
				temp.put("stores_name", StringUtil.formatStr(data.get("stores_name")));
				temp.put("logo_pic_path", StringUtil.formatStr(data.get("logo_pic_path")));
				temp.put("limit_amount", StringUtil.formatStr(data.get("limit_amount")));
				resultList.add(temp);
				doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(data.get("logo_pic_path")), ","));
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberIdBySkuCodeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "sku_code" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("sku_code", paramsMap.get("sku_code"));
			List<FdMemberAssetCoupon> fdMemberAssetCouponList = financeDao.selectFdMemberAssetCoupon(tempMap);
			if (fdMemberAssetCouponList.size() == 0) {
				throw new BusinessException(RspCode.COUPON_NO_EXIST, RspCode.MSG.get(RspCode.COUPON_NO_EXIST));
			}
			tempMap.clear();
			FdMemberAssetCoupon fdMemberAssetCoupon = fdMemberAssetCouponList.get(0);
			tempMap.put("member_id", fdMemberAssetCoupon.getMember_id());
			result.setResultData(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCouponCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "status" });
			Map<String, Object> countsMap = financeDao.selectFdMemberAssetCouponCount(paramsMap);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("count_num", "0");
			if (null != countsMap) {
				resultMap.put("count_num", countsMap.get("count_num") + "");
			}
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void disabledCouponStatusUpdate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("gt_date", DateUtil.addDate(DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss),
					DateUtil.fmt_yyyyMMddHHmmss, 3, -1));
			tempMap.put("lt_date", new Date());
			financeDao.updateDisabledCouponAssetStatus(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void tradeConsumerListForStores(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			Map<String, Object> tempConmuserMap = new HashMap<String, Object>();
			paramsMap.put("transaction_date_start", DateUtil.getFormatDate(DateUtil.fmt_yyyyMM) + "-01");
			paramsMap.put("transaction_date_end", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMdd));
			List<Map<String, Object>> tradeConsumerList = financeDao.selectTradeConsumerListForStores(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> tm : tradeConsumerList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("member_id", tm.get("member_id"));
				map.put("name", StringUtil.formatStr(tm.get("name")));
				map.put("contact", StringUtil.formatStr(tm.get("contact")));
				map.put("date", DateUtil.date2Str((Date) tm.get("date"), DateUtil.fmt_yyyyMMddHHmmss));
				map.put("type", "收银转帐");
				map.put("amount", StringUtil.formatStr(tm.get("amount")));
				resultList.add(map);
				tempConmuserMap.put(tm.get("member_id") + "", map);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("list", resultList);
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void tradeConsumerListForMemberList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> tempConmuserMap = new HashMap<String, Object>();
			List<Map<String, Object>> tradeConsumerList = financeDao.selectTradeConsumerListForStores(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> tm : tradeConsumerList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("member_id", tm.get("member_id"));
				map.put("name", StringUtil.formatStr(tm.get("name")));
				map.put("contact", StringUtil.formatStr(tm.get("contact")));
				map.put("date", DateUtil.date2Str((Date) tm.get("date"), DateUtil.fmt_yyyyMMddHHmmss));
				map.put("type", "收银转帐");
				map.put("amount", StringUtil.formatStr(tm.get("amount")));
				resultList.add(map);
				tempConmuserMap.put(tm.get("member_id") + "", map);
			}
			resultMap.clear();
			resultMap.put("list", resultList);
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerVoucherBill(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = new PageParam();
			pageParam.setPage_size(100);
			financeSyncService.giftRechargeLogFind(paramsMap, pageParam, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storeVoucherBill(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			List<FDVoucherDailyAccountMember> list = financeDao.selectFDVoucherDailyAccountStore(paramsMap);
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for (FDVoucherDailyAccountMember daily : list) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("transaction_no", daily.getTransaction_no());
				tempMap.put("transaction_date",
						DateUtil.date2Str(daily.getTransaction_date(), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("detail", daily.getDetail());
				tempMap.put("amount", daily.getAmount().toString());
				tempMap.put("booking_mark", daily.getBooking_mark());
				tempMap.put("payment_way_key", daily.getPayment_way_key());
				tempMap.put("business_type_key", daily.getBusiness_type_key());
				resultList.add(tempMap);
			}
			result.setResultData(resultList);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleStoresUnfreezeBalanceForConsumer(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "data_source", "stores_id", "stores_name", "consumer_id" });
			// 查询消费者余额
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("member_id", paramsMap.get("consumer_id"));
			tempMap.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
			FDMemberAsset consumer_memberAsset = financeDao.selectFDMemberAsset(tempMap);
			BigDecimal consumer_cash_froze = consumer_memberAsset.getCash_froze();
			if (consumer_cash_froze.compareTo(BigDecimal.ZERO) > 0) {
				// 检测店东可用余额是否足够
				tempMap.clear();
				tempMap.put("member_id", paramsMap.get("stores_id"));
				tempMap.put("member_type_key", Constant.MEMBER_TYPE_STORES);
				FDMemberAsset stores_memberAsset = financeDao.selectFDMemberAsset(tempMap);
				BigDecimal stores_cash_froze = stores_memberAsset.getCash_froze();
				BigDecimal stores_cash_balance = stores_memberAsset.getCash_balance();
				// 店东可用余额
				BigDecimal usable_cash_balance = MoneyUtil.moneySub(stores_cash_balance, stores_cash_froze);
				if (MoneyUtil.moneySub(usable_cash_balance, consumer_cash_froze).compareTo(BigDecimal.ZERO) < 0) {
					throw new BusinessException(RspCode.TRADE_AMOUNT_ERROR, "您的可用余额不足以解冻消费者的冻结金额");
				}

				// 冻结店东的余额
				tempMap.clear();
				tempMap.put("data_source", paramsMap.get("data_source"));
				tempMap.put("member_id", paramsMap.get("stores_id"));
				tempMap.put("member_type_key", Constant.MEMBER_TYPE_STORES);
				tempMap.put("amount", consumer_cash_froze + "");
				tempMap.put("detail", "余额冻结");
				tradeService.balanceFreeze(tempMap, result);

				// 解冻消费者冻结余额
				tempMap.clear();
				tempMap.put("data_source", paramsMap.get("data_source"));
				tempMap.put("member_id", paramsMap.get("consumer_id"));
				tempMap.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
				tempMap.put("amount", consumer_cash_froze + "");
				tempMap.put("detail", "余额解冻,操作门店【" + paramsMap.get("stores_name") + "】");
				tempMap.put("operator", paramsMap.get("stores_id"));
				tempMap.put("operator_type", "stores");
				tradeService.balanceUnFreeze(tempMap, result);

				// 推送消息
				Map<String, String> extrasMap = new HashMap<String, String>();
				extrasMap.put("type", Constant.PUSH_MESSAGE_TYPE_02);
				extrasMap.put("consumer_id", paramsMap.get("consumer_id") + "");
				String msg = "您已成功解冻" + consumer_cash_froze + "元\n操作门店【" + paramsMap.get("stores_name") + "】";
				String member_id = paramsMap.get("consumer_id") + "";
				notifyService.pushAppMessage(member_id, msg, extrasMap);
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void billCheckLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "bill_type", "bill_date" });
		List<Map<String, Object>> list = financeDao.selectBillCheckLog(paramsMap);
		for (Map<String, Object> log : list) {
			log.put("transaction_date",
					DateUtil.date2Str((Date) log.get("transaction_date"), DateUtil.fmt_yyyyMMddHHmmss));
			log.put("buyer_member_type", StringUtil.formatStr(log.get("buyer_member_type")));
			log.put("buyer_name", StringUtil.formatStr(log.get("buyer_name")));
			log.put("buyer_contact", StringUtil.formatStr(log.get("buyer_contact")));

			log.put("seller_member_type", StringUtil.formatStr(log.get("seller_member_type")));
			log.put("seller_name", StringUtil.formatStr(log.get("seller_name")));
			log.put("seller_contact", StringUtil.formatStr(log.get("seller_contact")));
		}
		Map<String, Object> resultDateMap = new HashMap<String, Object>();
		resultDateMap.put("list", list);
		result.setResultData(resultDateMap);
	}

	@Override
	public void storesCashCommissionLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("store_id", paramsMap.get("member_id"));
		// List<Map<String, Object>> list =
		// financeDao.selectStoresCashCommissionLogList(paramsMap);
		// for (Map<String, Object> log : list) {
		// log.put("created_date", DateUtil.date2Str((Date)
		// log.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
		// log.put("commission_cash",
		// StringUtil.formatStr(log.get("commission_cash")));
		// log.put("consumer_mobile",
		// StringUtil.formatStr(log.get("consumer_moblie")));
		// }
		Map<String, Object> resultDateMap = new HashMap<String, Object>();
		// resultDateMap.put("list", list);
		result.setResultData(resultDateMap);
	}

	@Override
	public void storesCashCommissionCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "store_id", "consumer_mobile", "consumer_id" });

			// String store_id = paramsMap.get("store_id") + "";
			// String remark = paramsMap.get("remark") + "";
			// String consumer_id = paramsMap.get("consumer_id") + "";
			// String consumer_mobile = paramsMap.get("consumer_mobile") + "";
			// BigDecimal commission_cash = new
			// BigDecimal(paramsMap.get("commission_cash").toString());
			// // 1.验证门店是否存在
			// Map<String, Object> tempMap = new HashMap<String, Object>();
			// tempMap.put("store_id", store_id);
			// Map<String, Object> tempSalesman =
			// financeDao.selectStoresCashCommission(tempMap);
			/*
			 * 这里有两种情况：一种是当门店佣金表中没有对应的门店佣金记录就进行新增操作 一种是当门店佣金表中有了对应的门店佣金记录就进行修改操作
			 * 新增操作：创建一条门店佣金记录，并且把当前传递过来的佣金加入到冻结金额中（这里只会出现正数的佣金），再新增一条日志
			 * 修改操作：根据门店ID来对门店佣金记录进行修改，把传递过来的佣金直接加入到冻结金额中（这里只会出现正数的佣金），再新增一条日志
			 */
			// if (null == tempSalesman) {
			// System.out.println("门店佣金信息不存在");
			// // 2.添加门店佣金记录
			// FDCashCommissionStore fdCashCommissionStore = new
			// FDCashCommissionStore();
			// BeanConvertUtil.mapToBean(fdCashCommissionStore, paramsMap);
			// fdCashCommissionStore.setStore_id(store_id);
			// //fdCashCommissionStore.setUsable_cash(new BigDecimal(0));//可提金额
			// //fdCashCommissionStore.setFroze_cash(commission_cash);//冻结金额
			// fdCashCommissionStore.setUpdated_date(new Date());
			// fdCashCommissionStore.setRemark(remark);
			// financeDao.insertStoresCashCommission(fdCashCommissionStore);
			// // 3.添加门店佣金记录日记
			// Map<String, Object> logMap = new HashMap<String, Object>();
			// logMap.put("log_id", IDUtil.getUUID());
			// logMap.put("store_id", store_id);
			// logMap.put("consumer_id", consumer_id);
			// logMap.put("consumer_mobile", consumer_mobile);
			// logMap.put("commission_cash", commission_cash);
			// logMap.put("created_date", new Date());
			// logMap.put("remark",
			// "remark:新增门店佣金记录,store_id:"+store_id+",consumer_mobile:"+consumer_mobile+",commission_cash:"+commission_cash);
			// financeDao.insertStoresCashCommissionLog(logMap);
			// } else {
			// ValidateUtil.validateParams(paramsMap, new String[] {
			// "commission_cash" });
			// System.out.println("门店佣金信息已存在");
			// // 2.修改门店佣金记录
			// Map<String, Object> fdCashCommissionStore = new HashMap<String,
			// Object>();
			// fdCashCommissionStore.put("store_id", store_id);
			// fdCashCommissionStore.put("commission_cash", commission_cash);
			// financeDao.updateStoresCashCommission(fdCashCommissionStore);
			// // 3.添加门店佣金记录日记
			// Map<String, Object> logMap = new HashMap<String, Object>();
			// logMap.put("log_id", IDUtil.getUUID());
			// logMap.put("store_id", store_id);
			// logMap.put("consumer_id", consumer_id);
			// logMap.put("consumer_mobile", consumer_mobile);
			// logMap.put("commission_cash", commission_cash);
			// logMap.put("created_date", new Date());
			// logMap.put("remark",
			// "remark:修改门店佣金记录,store_id:"+store_id+",consumer_mobile:"+consumer_mobile+",commission_cash:"+commission_cash);
			// financeDao.insertStoresCashCommissionLog(logMap);
			// }

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storesCashCommissionEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "store_id", "consumer_mobile", "consumer_id", "commission_cash" });
			String store_id = paramsMap.get("store_id") + "";
			String consumer_id = paramsMap.get("consumer_id") + "";
			String consumer_mobile = paramsMap.get("consumer_mobile") + "";
			BigDecimal commission_cash = new BigDecimal(paramsMap.get("commission_cash").toString());
			/*
			 * 因为是撤销返款 所以这里传递进来的佣金是负数的 这个一定要注意
			 */
			// 1.验证门店是否存在
			// Map<String, Object> tempMap = new HashMap<String, Object>();
			// tempMap.put("store_id", store_id);
			// Map<String, Object> tempSalesman =
			// financeDao.selectStoresCashCommission(tempMap);
			// if (null == tempSalesman) {
			// throw new BusinessException(RspCode.ALIPAY_ERROR, "门店佣金不存在");
			// }
			// // 2.修改门店佣金记录
			// Map<String, Object> fdCashCommissionStore = new HashMap<String,
			// Object>();
			// fdCashCommissionStore.put("store_id", store_id);
			// fdCashCommissionStore.put("commission_cash", commission_cash);
			// financeDao.updateStoresCashCommission(fdCashCommissionStore);
			// // 3.添加门店佣金记录日记
			// Map<String, Object> logMap = new HashMap<String, Object>();
			// logMap.put("log_id", IDUtil.getUUID());
			// logMap.put("store_id", store_id);
			// logMap.put("consumer_id", consumer_id);
			// logMap.put("consumer_mobile", consumer_mobile);
			// logMap.put("commission_cash", commission_cash);
			// logMap.put("created_date", new Date());
			// logMap.put("remark",
			// "remark:修改门店佣金记录,store_id:"+store_id+",consumer_mobile:"+consumer_mobile+",commission_cash:"+commission_cash);
			// financeDao.insertStoresCashCommissionLog(logMap);
			//
		} catch (Exception e) {
			throw e;
		}
	}


}
