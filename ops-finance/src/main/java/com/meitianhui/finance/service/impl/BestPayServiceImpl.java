package com.meitianhui.finance.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.finance.constant.Constant;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.constant.TradeIDUtil;
import com.meitianhui.finance.controller.FinanceController;
import com.meitianhui.finance.service.BestPayService;
import com.meitianhui.finance.service.TradeService;
import com.meitianhui.finance.util.BestPayHttpUtil;

@SuppressWarnings("unchecked")
@Service
public class BestPayServiceImpl implements BestPayService {

	private final Logger logger = Logger.getLogger(BestPayServiceImpl.class);

	@Autowired
	private TradeService tradeService;

	private String url = PropertiesConfigUtil.getProperty("bestpay.service_url");

	/** 交易等待 **/
	public static String BESTPAY_TRADE_WAIT = "TRADE_WAIT";
	/** 交易成功 **/
	public static String BESTPAY_TRADE_SUCCESS = "TRADE_SUCCESS";
	/** 交易关闭 **/
	public static String BESTPAY_TRADE_CLOSED = "TRADE_CLOSED";

	@Override
	public void barCodePay(Map<String, Object> paramsMap, String scan_flag)
			throws BusinessException, SystemException, Exception {
		String merchantId = PropertiesConfigUtil.getProperty("bestpay.merchant_id");
		String key = PropertiesConfigUtil.getProperty("bestpay.date_key");
		String orderNo = (String) paramsMap.get("orderNo");
		String orderReqNo = (String) paramsMap.get("orderReqNo");
		String orderDate = DateUtil.getFormatDate("yyyyMMddHHmmss");
		String barcode = (String) paramsMap.get("barcode");
		String orderAmt = (String) paramsMap.get("amount");
		String attach = (String) paramsMap.get("attach");
		try {
			// 组装请求参数
			Map<String, String> param = new HashMap<String, String>();
			param.put("merchantId", merchantId);
			param.put("barcode", barcode);
			param.put("orderNo", orderNo);
			param.put("orderReqNo", orderReqNo);
			param.put("orderDate", orderDate);
			param.put("channel", "05");
			param.put("busiType", "0001");
			param.put("orderAmt", orderAmt);
			param.put("productAmt", orderAmt);
			param.put("attachAmt", "0");
			param.put("storeId", storesIdFind((String) paramsMap.get("member_id")));
			param.put("attach", attach);
			String orderMac = BestPayHttpUtil.createOrderMacStr(merchantId, orderNo, orderReqNo, orderDate, barcode,
					orderAmt, key);
			param.put("mac", orderMac);
			logger.info("翼支付扫码支付,request ->" + param.toString());
			String placeOrderUrl = url + "/barcode/placeOrder";
			BestPayHttpUtil bastPayHttpUtil = new BestPayHttpUtil();
			String resultStr = bastPayHttpUtil.httpPost(placeOrderUrl, param);
			logger.info("翼支付扫码支付,response->" + resultStr);
			Map<String, Object> query_response = new HashMap<String, Object>();
			String transStatus = BESTPAY_TRADE_CLOSED;
			// 无论成功与否,都进行交易查询
			if (scan_flag.equals("new2")) {
				// 6次轮询，每次轮询间隔5秒
				transStatus = queryTransStatus(merchantId, orderNo, orderReqNo, orderDate, 6, 5000, query_response);
			} else {
				// TODO 后期废除
				// (10次轮询，每次轮询间隔5秒)
				transStatus = queryTransStatus(merchantId, orderNo, orderReqNo, orderDate, 10, 5000, query_response);
			}

			if (transStatus.equals(BESTPAY_TRADE_SUCCESS)) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("transaction_no", orderReqNo);
				tempMap.put("transaction_status", Constant.TRANSACTION_STATUS_CONFIRMED);
				tempMap.put("transaction_body", FastJsonUtil.toJson(query_response));
				tradeService.transactionConfirmed(tempMap, new ResultData());
			} else {
				if (scan_flag.equals("new2")) {
					// 后台轮询交易
					asyncLoopQuery(merchantId, orderNo, orderReqNo, orderDate, orderAmt);
				} else {
					// TODO 2.9.5后的版本废除
					// 交易冲正
					reverse(merchantId, orderNo, orderReqNo, TradeIDUtil.getTradeNo(), orderAmt);
					throw new BusinessException(RspCode.ALIPAY_ERROR, "翼支付条码支付失败");
				}
			}
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 后台轮询
	 * 
	 * @Title: asyncLoopQuery
	 * @param out_trade_no
	 * @param merchantId
	 * @param orderNo
	 * @param orderReqNo
	 * @param orderDate
	 * @author tiny
	 */
	private void asyncLoopQuery(final String merchantId, final String orderNo, final String orderReqNo,
			final String orderDate, final String transAmt) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, Object> query_response = new HashMap<String, Object>();
					// 6次轮询，每次轮询间隔5秒
					String transaction_status = queryTransStatus(merchantId, orderNo, orderReqNo, orderDate, 6, 5000,
							query_response);
					// 交易成功,进行交易确认,否则进行交易取消
					if (transaction_status.equals(BESTPAY_TRADE_SUCCESS)) {
						transaction_status = Constant.TRANSACTION_STATUS_CONFIRMED;
					} else {
						transaction_status = Constant.TRANSACTION_STATUS_ERROR;
						// 交易冲正
						reverse(merchantId, orderNo, orderReqNo, TradeIDUtil.getTradeNo(), transAmt);
					}
					// 交易确定
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("transaction_no", orderNo);
					tempMap.put("transaction_status", transaction_status);
					tempMap.put("transaction_body", FastJsonUtil.toJson(query_response));
					tradeService.transactionConfirmed(tempMap, new ResultData());
				} catch (Exception e) {
					logger.error("翼支付条码支付异常", e);
				}
			}
		});
	}

	/**
	 * 交易结果查询
	 * 
	 * @param merchantId
	 * @param orderNo
	 * @param orderReqNo
	 * @param orderDate
	 * @param mac
	 */
	public String queryTransStatus(String merchantId, String orderNo, String orderReqNo, String orderDate,
			Integer qry_num, long sleep_time, Map<String, Object> query_response)
					throws BusinessException, SystemException, Exception {
		String transStatus = BESTPAY_TRADE_WAIT;
		try {
			// 轮询6次
			for (int i = 1; i <= qry_num; i++) {
				logger.info("翼支付-第" + i + "次交易查询");
				transStatus = queryOrder(merchantId, orderNo, orderReqNo, orderDate, query_response);
				if (transStatus.equals("B")) {
					transStatus = BESTPAY_TRADE_SUCCESS;
					break;
				} else if (transStatus.equals("C") || transStatus.equals("G")) {
					transStatus = BESTPAY_TRADE_CLOSED;
					break;
				}
				Thread.sleep(sleep_time);
			}
		} catch (Exception e) {
			logger.error("翼支付条码支付查询异常", e);
		}
		return transStatus;
	}

	/**
	 * 订单查询接口
	 * 
	 * @Title: queryOrder
	 * @author tiny
	 */
	public String queryOrder(String merchantId, String orderNo, String orderReqNo, String orderDate,
			Map<String, Object> query_response) {
		String status = "A";
		try {
			String key = PropertiesConfigUtil.getProperty("bestpay.date_key");
			String mac = BestPayHttpUtil.createOrderQueryMacStr(merchantId, orderNo, orderReqNo, orderDate, key);
			String queryOrderUrl = url + "/query/queryOrder";
			Map<String, String> queryParam = new HashMap<String, String>();
			queryParam.put("mac", mac);
			queryParam.put("merchantId", merchantId);
			queryParam.put("orderNo", orderNo);
			queryParam.put("orderReqNo", orderReqNo);
			queryParam.put("orderDate", orderDate);
			logger.info("翼支付-交易查询,request->" + queryParam.toString());
			BestPayHttpUtil bastPayHttpUtil = new BestPayHttpUtil();
			String responseStr = bastPayHttpUtil.httpPost(queryOrderUrl, queryParam);
			logger.info("翼支付-交易查询,response->" + responseStr);
			if (responseStr != null) {
				Map<String, Object> responseMap = FastJsonUtil.jsonToMap(responseStr);
				if ((Boolean) responseMap.get("success")) {
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(responseMap.get("result") + "");
					// 交易状态
					// A：请求（支付中）
					// B：成功（支付成功）
					// C：失败
					// G：订单作废
					status = (String) resultMap.get("transStatus");
					query_response.putAll(resultMap);
				}
			}
		} catch (Exception e) {
			logger.error("翼支付条码支付查询异常", e);
		}
		return status;
	}

	/**
	 * 冲正
	 * 
	 * @param merchantId
	 * @param orderNo
	 * @param orderReqNo
	 * @param orderDate
	 * @param mac
	 */
	public void reverse(final String merchantId, final String oldOrderNo, final String oldOrderReqNo,
			final String refundReqNo, final String transAmt) throws BusinessException, SystemException, Exception {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String reverseUrl = url + "/reverse/reverse";
				String key = PropertiesConfigUtil.getProperty("bestpay.date_key");
				String merchantPwd = PropertiesConfigUtil.getProperty("bestpay.trade_key");
				String refundReqDate = DateUtil.getFormatDate("yyyyMMdd");
				int i = 3;
				while (i > 0) {
					try {
						Thread.sleep(1000);
						i--;
						Map<String, String> queryParam = new HashMap<String, String>();
						queryParam.put("merchantId", merchantId);
						queryParam.put("merchantPwd", merchantPwd);
						queryParam.put("oldOrderNo", oldOrderNo);
						queryParam.put("oldOrderReqNo", oldOrderReqNo);
						queryParam.put("refundReqNo", refundReqNo);
						queryParam.put("refundReqDate", refundReqDate);
						queryParam.put("transAmt", transAmt);
						queryParam.put("channel", "05");
						String mac = BestPayHttpUtil.createReverseMacStr(merchantId, merchantPwd, oldOrderNo,
								oldOrderReqNo, refundReqNo, refundReqDate, transAmt, key);
						queryParam.put("mac", mac);
						logger.info("翼支付-冲正,request->" + queryParam.toString());
						BestPayHttpUtil bastPayHttpUtil = new BestPayHttpUtil();
						String resultStr = bastPayHttpUtil.httpPost(reverseUrl, queryParam);
						logger.info("翼支付-冲正,response->" + resultStr);
						if (null != resultStr) {
							Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
							if ((Boolean) resultMap.get("success")) {
								i = 0;
							}
						}
					} catch (Exception e) {
						logger.info("翼支付-交易冲正异常", e);
					}
				}
			}
		});
	}

	private String storesIdFind(String member_id) {
		String storeId = IDUtil.generateCode(10);
		try {
			Map<String, Object> storesMap = new HashMap<String, Object>();
			storesMap.put("contact_tel", member_id);
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> paramMap = new HashMap<String, String>();
			String resultStr = null;
			reqParams.clear();
			paramMap.clear();
			reqParams.put("service", "member.storeForOrderFind");
			paramMap.put("stores_id", member_id);
			reqParams.put("params", FastJsonUtil.toJson(paramMap));
			resultStr = HttpClientUtil.post(member_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
				String contact_tel = StringUtil.formatStr(dateMap.get("contact_tel"));
				if (!contact_tel.equals("")) {
					storeId = contact_tel.substring(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return storeId;
	}

}
