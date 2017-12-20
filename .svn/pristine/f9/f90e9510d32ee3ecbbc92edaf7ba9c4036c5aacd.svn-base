package com.meitianhui.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.MoneyUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.finance.constant.Constant;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.dao.VoucherDao;
import com.meitianhui.finance.entity.FdVoucherToGoldLog;
import com.meitianhui.finance.service.FinanceSyncService;
import com.meitianhui.finance.service.TradeService;
import com.meitianhui.finance.service.VoucherService;

/**
 * 金融服务的服务层
 * 
 * @author Tiny
 *
 */
@Service
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	public VoucherDao voucherDao;
	@Autowired
	public TradeService tradeService;
	@Autowired
	public FinanceSyncService financeSyncService;

	@Override
	public void handleVoucherExchangeLogFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("is_exchange", "N");
		ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id", "mobile" });
		// 查询消费者是否已经兑换过礼券
		List<FdVoucherToGoldLog> list = voucherDao.selectFdVoucherToGoldLog(paramsMap);
		if (list.size() == 0) {
			String mobile = paramsMap.get("mobile") + "";
			BigDecimal voucher_balance = new BigDecimal(financeSyncService.giftRechargeBalanceFind(mobile) + "");
			// 如果礼券余额大于0,前段才能调用礼券兑换接口
			if (voucher_balance.compareTo(BigDecimal.ZERO) > 0) {
				resultMap.put("is_exchange", "Y");
				resultMap.put("voucher", voucher_balance.intValue() + ""); // 小数部分直接进位
				BigDecimal gold = MoneyUtil.moneydiv(voucher_balance, new BigDecimal("10")).setScale(0,
						BigDecimal.ROUND_UP);
				resultMap.put("gold", gold.intValue() + "");
			} else {
				// 如果礼券余额等于0,则自动创建一个兑换记录
				FdVoucherToGoldLog log = new FdVoucherToGoldLog();
				log.setLog_id(IDUtil.getUUID());
				log.setGold(0);
				log.setVoucher(0);
				log.setMember_id(paramsMap.get("consumer_id") + "");
				log.setMember_type_key(Constant.MEMBER_TYPE_CONSUMER);
				log.setTracked_date(new Date());
				log.setConsumer_id(paramsMap.get("consumer_id") + "");
				voucherDao.insertFdVoucherToGoldLog(log);
			}
		}
		result.setResultData(resultMap);
	}

	@Override
	public void handleVoucherExchangeGold(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "data_source", "consumer_id", "mobile", "voucher", "gold" });
		String consumer_id = paramsMap.get("consumer_id") + "";
		String mobile = paramsMap.get("mobile") + "";
		String voucher = paramsMap.get("voucher") + "";
		String gold = paramsMap.get("gold") + "";
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("consumer_id", consumer_id);
		List<FdVoucherToGoldLog> list = voucherDao.selectFdVoucherToGoldLog(tempMap);
		if (list.size() > 0) {
			throw new BusinessException(RspCode.EXCHANGE_FAIL, "您已经兑换过礼券,请勿重复兑换");
		}
		// 写入兑换记录
		FdVoucherToGoldLog log = new FdVoucherToGoldLog();
		log.setLog_id(IDUtil.getUUID());
		log.setConsumer_id(consumer_id);
		log.setGold(Integer.parseInt(gold));
		log.setVoucher(Integer.parseInt(voucher));
		log.setMember_id(consumer_id);
		log.setMember_type_key(Constant.MEMBER_TYPE_CONSUMER);
		log.setTracked_date(new Date());
		voucherDao.insertFdVoucherToGoldLog(log);
		// 扣除礼券
		financeSyncService.voucherExchange(mobile, voucher);
		// 金币充值
		Map<String, Object> bizParams = new HashMap<String, Object>();
		bizParams.put("data_source", paramsMap.get("data_source"));
		bizParams.put("payment_way_key", "ZFFS_08");
		bizParams.put("detail", "礼券兑换金币");
		bizParams.put("amount", new BigDecimal(gold).intValue() + "");
		bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
		bizParams.put("in_member_id", consumer_id);
		bizParams.put("out_trade_no", IDUtil.getTimestamp(5));
		Map<String, Object> out_trade_body = new HashMap<String, Object>();
		out_trade_body.put("mobile", mobile);
		out_trade_body.put("gold", gold);
		out_trade_body.put("voucher", voucher);
		bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
		tradeService.balancePay(bizParams, new ResultData());
	}

	@Override
	public void handleVoucherExchange(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "consumer_id", "mobile", "voucher", "stores_id", "stores_name" });
		// 写入兑换记录
		String consumer_id = paramsMap.get("consumer_id") + "";
		String mobile = paramsMap.get("mobile") + "";
		String voucher = paramsMap.get("voucher") + "";
		BigDecimal voucher_balance = new BigDecimal(financeSyncService.giftRechargeBalanceFind(mobile) + "");
		if (voucher_balance.intValue() < Integer.parseInt(voucher)) {
			throw new BusinessException(RspCode.EXCHANGE_FAIL, "兑换金额超过当前用户礼券余额");
		}
		// 扣除礼券
		financeSyncService.voucherExchange(mobile, voucher);
		// 写入日志
		FdVoucherToGoldLog log = new FdVoucherToGoldLog();
		log.setLog_id(IDUtil.getUUID());
		log.setConsumer_id(consumer_id);
		log.setGold(0);
		log.setVoucher(Integer.parseInt(voucher));
		log.setMember_id(paramsMap.get("stores_id") + "");
		log.setMember_type_key(Constant.MEMBER_TYPE_STORES);
		log.setTracked_date(new Date());
		Map<String, Object> remark = new HashMap<String, Object>();
		remark.put("stores_name", paramsMap.get("stores_name"));
		remark.put("consumer_mobile", paramsMap.get("consumer_mobile"));
		remark.put("desc", "兑换商品");
		log.setRemark(FastJsonUtil.toJson(remark));
		voucherDao.insertFdVoucherToGoldLog(log);
	}

	@Override
	public void voucherBalanceFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ValidateUtil.validateParams(paramsMap, new String[] { "mobile" });
		String mobile = paramsMap.get("mobile") + "";
		BigDecimal voucher_balance = new BigDecimal(financeSyncService.giftRechargeBalanceFind(mobile) + "");
		resultMap.put("voucher", voucher_balance.intValue() + "");
		result.setResultData(resultMap);
	}

	@Override
	public void voucherExchangeLogForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<FdVoucherToGoldLog> qryList = voucherDao.selectFdVoucherToGoldLog(paramsMap);
		for (FdVoucherToGoldLog log : qryList) {
			// 只查询兑换商品的记录
			if (log.getGold() == 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				String remark = log.getRemark();
				Map<String, Object> remarkMap = FastJsonUtil.jsonToMap(remark);
				String consumer_mobile = StringUtil.formatStr(remarkMap.get("consumer_mobile"));
				remark = StringUtil.formatStr(remarkMap.get("desc"));
				map.put("tracked_date", DateUtil.date2Str(log.getTracked_date(), DateUtil.fmt_yyyyMMddHHmmss));
				map.put("remark", "消费者" + consumer_mobile + "消耗" + log.getVoucher() + "礼券");
				list.add(map);
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);
		result.setResultData(resultMap);
	}

}
