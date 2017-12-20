package com.meitianhui.finance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.PageParam;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.finance.constant.Constant;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.service.FinanceSyncService;

/**
 * 交易同步的服务层
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
@Service
public class FinanceSyncServiceImpl implements FinanceSyncService {

	private static final Logger logger = Logger.getLogger(FinanceSyncServiceImpl.class);

	/**
	 * 礼券兑换
	 * 
	 * @param paramsMap
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public void voucherExchange(String mobile, String amount) throws BusinessException, SystemException, Exception {
		try {
			String huigujia_service_url = PropertiesConfigUtil.getProperty("huigujia_service_url")
					+ "giftAccountsAPI.json";
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("toUser", "1");
			reqParams.put("scenariosId", "5");
			reqParams.put("fromUser", mobile);
			reqParams.put("amount", amount);
			reqParams.put("message", "兑换金币");
			String resultStr = HttpClientUtil.postShort(huigujia_service_url, reqParams);
			logger.info("惠顾家-礼券兑换,request->" + reqParams.toString() + ",response->" + resultStr);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!(Boolean) resultMap.get("success")) {
				throw new BusinessException(RspCode.TRADE_FAIL, (String) resultMap.get("message"));
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 通过手机号直接送礼券
	 * 
	 * @param paramsMap
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public void giftAccountsAPI(Map<String, Object> paramsMap) throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "amount", "identity", "message", "scenariosId" });
			String huigujia_service_url = PropertiesConfigUtil.getProperty("huigujia_service_url")
					+ "giftAccountsAPI.json";
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("fromUser", StringUtil.formatStr(paramsMap.get("fromUser")));
			reqParams.put("toUser", StringUtil.formatStr(paramsMap.get("toUser")));
			reqParams.put("store_mobile", StringUtil.formatStr(paramsMap.get("store_mobile")));
			reqParams.put("tradeId", StringUtil.formatStr(paramsMap.get("tradeId")));
			reqParams.put("data_source", StringUtil.formatStr(paramsMap.get("data_source")));
			reqParams.put("amount", paramsMap.get("amount") + "");
			reqParams.put("identity", paramsMap.get("identity") + "");
			reqParams.put("message", paramsMap.get("message") + "");
			reqParams.put("scenariosId", paramsMap.get("scenariosId") + "");
			String resultStr = HttpClientUtil.postShort(huigujia_service_url, reqParams);
			logger.info("惠顾家-礼券交易,request->" + reqParams.toString() + ",response->" + resultStr);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!(Boolean) resultMap.get("success")) {
				throw new BusinessException(RspCode.TRADE_FAIL, (String) resultMap.get("message"));
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 通过手机号直接送礼券
	 * 
	 * @param paramsMap
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public void giftRechargeLogFind(Map<String, Object> paramsMap, PageParam pageParam, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {

			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			// 查询登陆手机号
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "member.userInfoFind");
			bizParams.put("member_id", paramsMap.get("member_id"));
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
			bizParams.put("is_admin", "Y");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> userInfoMap = (Map<String, Object>) resultMap.get("data");
			String userPhone = StringUtil.formatStr(userInfoMap.get("mobile"));

			String huigujia_service_url = PropertiesConfigUtil.getProperty("huigujia_service_url")
					+ "getGiftRechargeLog.json";
			reqParams.clear();
			resultMap.clear();
			reqParams.put("userPhone", userPhone);
			reqParams.put("pageSize", pageParam.getPage_size() + "");
			reqParams.put("currentPage", pageParam.getPage_no() + "");
			resultStr = HttpClientUtil.postShort(huigujia_service_url, reqParams);
			logger.info("惠顾家-记录查询,request->" + reqParams.toString() + ",response->" + resultStr);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if ((Integer) resultMap.get("status") != 200) {
				throw new BusinessException(RspCode.TRADE_FAIL, (String) resultMap.get("message"));
			}
			pageParam.setTotal_count((Integer) resultMap.get("count"));
			pageParam.setTotal_page((Integer) resultMap.get("totalPage"));
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultMap.get("data");
			for (Map<String, Object> logMap : dataList) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 交易流水号
				map.put("transaction_no", StringUtil.formatStr(logMap.get("identity")));
				// 交易时间
				map.put("transaction_date", StringUtil.formatStr(logMap.get("gmtCreate")));
				// 场景
				map.put("detail", StringUtil.formatStr(logMap.get("scenarios")));
				// 礼券数量
				map.put("amount", logMap.get("amount") + "");
				map.put("booking_mark", "income");
				map.put("payment_way_key", "ZFFS_06");
				map.put("business_type_key", "JYLX_09");
				resultList.add(map);
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

	/**
	 * 通过手机号查询礼券余额
	 * 
	 * @param paramsMap
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public String giftRechargeBalanceFind(String userPhone) {
		String giftAmount = "0";

		try {
			String huigujia_service_url = PropertiesConfigUtil.getProperty("huigujia_service_url")
					+ "getGiftRechargeLog.json";
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("userPhone", userPhone);
			String resultStr = HttpClientUtil.postShort(huigujia_service_url, reqParams);
			logger.info("惠顾家-余额查询,request->" + reqParams.toString() + ",response->" + resultStr);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if ((Integer) resultMap.get("status") != 200) {
				throw new BusinessException(RspCode.TRADE_FAIL, (String) resultMap.get("message"));
			}
			giftAmount = (Integer) resultMap.get("giftAmount") + "";
		} catch (BusinessException e) {
			logger.info(e.getMessage());
		} catch (SystemException e) {
			logger.info("消费者礼券余额查询异常" + e.getMessage());
		} catch (Exception e) {
			logger.info("消费者礼券余额查询异常" + e.getMessage());
		}
		return giftAmount;
	}

}
