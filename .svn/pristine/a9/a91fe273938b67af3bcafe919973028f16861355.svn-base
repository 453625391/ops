package com.meitianhui.finance.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csvreader.CsvReader;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.FileUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.common.util.ZipUtil;
import com.meitianhui.finance.constant.Constant;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.controller.FinanceController;
import com.meitianhui.finance.dao.AlipayBillLogDao;
import com.meitianhui.finance.entity.FDAlipayBillLog;
import com.meitianhui.finance.service.AlipayPayService;
import com.meitianhui.finance.service.TradeService;
import com.meitianhui.finance.util.AliPayUtil;
import com.meitianhui.finance.util.RSAUtil;
import com.meitianhui.finance.util.WebUtils;

@SuppressWarnings("unchecked")
@Service
public class AlipayPayServiceImpl implements AlipayPayService {
	private static final Logger logger = Logger.getLogger(AlipayPayServiceImpl.class);

	private final String rootPath = System.getProperty("mth.ops.finance.root");

	private final String basePath = rootPath.substring(0, rootPath.indexOf("ops-finance")) + "bill" + File.separator
			+ "alipay" + File.separator;

	/** 交易等待 **/
	public static String ALIPAY_TRADE_WAIT = "TRADE_WAIT";
	/** 交易成功 **/
	public static String ALIPAY_TRADE_SUCCESS = "TRADE_SUCCESS";
	/** 交易关闭 **/
	public static String ALIPAY_TRADE_CLOSED = "TRADE_CLOSED";

	@Autowired
	private TradeService tradeService;

	@Autowired
	private AlipayBillLogDao fDAlipayBillLogDao;

	@Override
	public void barCodePay(Map<String, Object> paramsMap, String scan_flag)
			throws BusinessException, SystemException, Exception {
		try {
			paramsMap.put("scene", "bar_code");
			paramsMap.put("timeout_express", "1m");
			// 限制使用信用卡支付
			// paramsMap.put("disable_pay_channels", "credit_group");
			String out_trade_no = (String) paramsMap.get("out_trade_no");
			String app_id = PropertiesConfigUtil.getProperty("alipay.face_pay_app_id");
			String private_key = PropertiesConfigUtil.getProperty("alipay.face_pay_open_api_private_key");
			Map<String, String> reqParams = getBaseParam(app_id, "alipay.trade.pay");
			reqParams.put("biz_content", FastJsonUtil.toJson(paramsMap));
			reqParams.put("sign", createSign(reqParams, private_key));
			logger.info("支付宝条码支付->request:" + reqParams.toString());
			String resultStr = HttpClientUtil.post(AliPayUtil.HTTPS_GATEWAY_OPENAPI_URL, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			Map<String, Object> alipay_trade_pay_response = (Map<String, Object>) resultMap
					.get("alipay_trade_pay_response");
			logger.info("支付宝条码支付->response:" + alipay_trade_pay_response.toString());
			String code = StringUtil.formatStr(alipay_trade_pay_response.get("code"));
			String sub_code = StringUtil.formatStr(alipay_trade_pay_response.get("sub_code"));

			// 特定的错误直接返回失败,不执行查询操作
			if (code.equals("40004")) {
				if (sub_code.equals("ACQ.PAYMENT_AUTH_CODE_INVALID")) {
					// ACQ.PAYMENT_AUTH_CODE_INVALID 支付授权码无效 用户刷新条码后，重新扫码发起请求
					throw new BusinessException(RspCode.ALIPAY_ERROR, "支付授权码无效");
				} else if (sub_code.equals("ACQ.USER_FACE_PAYMENT_SWITCH_OFF")) {
					// ACQ.USER_FACE_PAYMENT_SWITCH_OFF 用户当面付付款开关关闭
					throw new BusinessException(RspCode.ALIPAY_ERROR,
							StringUtil.formatStr(alipay_trade_pay_response.get("sub_msg")));
				}
			}
			Map<String, Object> query_response = new HashMap<String, Object>();

			// 无论成功与否,都进行交易查询
			String status = null;
			if (StringUtil.isEmpty(scan_flag)) {
				// TODO 2.9.4后的版本废除
				// (10次轮询，每次轮询间隔5秒)
				status = barCodeOrderQuery(app_id, private_key, out_trade_no, 10, 5000, query_response);
			} else {
				// (6次轮询，每次轮询间隔5秒)
				status = barCodeOrderQuery(app_id, private_key, out_trade_no, 5, 4000, query_response);
			}
			// 如果交易成功,则进行交易确认操作,失败则进行交易取消
			if (status.equals(ALIPAY_TRADE_SUCCESS)) {
				// 交易确定
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("transaction_no", out_trade_no);
				tempMap.put("transaction_status", Constant.TRANSACTION_STATUS_CONFIRMED);
				tempMap.put("transaction_body", FastJsonUtil.toJson(query_response));
				tradeService.transactionConfirmed(tempMap, new ResultData());
			} else {
				if (StringUtil.isEmpty(scan_flag)) {
					// TODO 2.9.4后的版本废除
					// 交易取消
					orderCancel(app_id, private_key, out_trade_no);
					throw new BusinessException(RspCode.ALIPAY_ERROR, "条码支付失败");
				} else {
					asyncLoopQuery(app_id, private_key, out_trade_no);
				}
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	private void asyncLoopQuery(final String app_id, final String private_key, final String out_trade_no) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, Object> query_response = new HashMap<String, Object>();
					// 5次轮询，每次轮询间隔6秒
					String status = barCodeOrderQuery(app_id, private_key, out_trade_no, 8, 5000, query_response);
					String transaction_status = Constant.TRANSACTION_STATUS_ERROR;
					// 交易成功,进行交易确认,否则进行交易取消
					if (status.equals(ALIPAY_TRADE_SUCCESS)) {
						transaction_status = Constant.TRANSACTION_STATUS_CONFIRMED;
					} else {
						// 交易取消
						orderCancel(app_id, private_key, out_trade_no);
					}
					// 交易确定
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("transaction_no", out_trade_no);
					tempMap.put("transaction_status", transaction_status);
					tempMap.put("transaction_body", FastJsonUtil.toJson(query_response));
					tradeService.transactionConfirmed(tempMap, new ResultData());
				} catch (Exception e) {
					logger.error("支付宝条码支付异常", e);
				}
			}
		});
	}

	/**
	 * 订单查询
	 * 
	 * @param app_id
	 * @param private_key
	 * @param biz_params
	 * @throws BusinessException
	 * @throws SystemException
	 */
	private String barCodeOrderQuery(String app_id, String private_key, String out_trade_no, Integer qry_num,
			long sleep_time, Map<String, Object> query_response) {
		String status = null;
		try {
			// 调用查询交易的接口
			for (int i = 1; i <= qry_num; i++) {
				logger.info("支付宝条码支付查询,第" + i + "次查询");
				status = orderQuery(app_id, private_key, out_trade_no, query_response);
				if (status.equals(ALIPAY_TRADE_SUCCESS) || status.equals(ALIPAY_TRADE_CLOSED)) {
					break;
				}
				Thread.sleep(sleep_time);
			}
		} catch (Exception e) {
			logger.error("支付宝条码支付查询异常", e);
		}
		return status;
	}

	@Override
	public String scanCodePay(Map<String, Object> paramsMap) throws BusinessException, SystemException, Exception {
		String qr_code = "";
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "out_trade_no", "total_amount", "subject", "timeout_express" });
			String out_trade_no = (String) paramsMap.get("out_trade_no");
			String notify_url = PropertiesConfigUtil.getProperty("alipay.face_notify_url");
			String app_id = PropertiesConfigUtil.getProperty("alipay.face_pay_app_id");
			String private_key = PropertiesConfigUtil.getProperty("alipay.face_pay_open_api_private_key");
			Map<String, String> reqParams = getBaseParam(app_id, "alipay.trade.precreate");
			// 异步通知地址
			reqParams.put("notify_url", notify_url);
			// 限制使用信用卡支付
			// paramsMap.put("disable_pay_channels", "credit_group");
			reqParams.put("biz_content", FastJsonUtil.toJson(paramsMap));
			reqParams.put("sign", createSign(reqParams, private_key));
			logger.info("创建支付宝收款码->request:" + reqParams.toString());
			String resultStr = HttpClientUtil.post(AliPayUtil.HTTPS_GATEWAY_OPENAPI_URL, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			Map<String, Object> alipay_trade_precreate_response = (Map<String, Object>) resultMap
					.get("alipay_trade_precreate_response");
			logger.info("创建支付宝收款码->response:" + alipay_trade_precreate_response.toString());
			String code = StringUtil.formatStr(alipay_trade_precreate_response.get("code"));
			if (code.equals("10000")) {
				String response_out_trade_no = StringUtil
						.formatStr(alipay_trade_precreate_response.get("out_trade_no"));
				if (!response_out_trade_no.equals(out_trade_no)) {
					throw new BusinessException(RspCode.ALIPAY_ERROR, "创建付款码失败,请重试");
				}
				// 获取付款码
				qr_code = StringUtil.formatStr(alipay_trade_precreate_response.get("qr_code"));
			} else {
				throw new BusinessException(RspCode.ALIPAY_ERROR,
						StringUtil.formatStr(alipay_trade_precreate_response.get("sub_msg")));
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return qr_code;
	}

	/**
	 * 交易查询
	 * 
	 * @param app_id
	 * @param private_key
	 * @param biz_params
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public String orderQuery(String app_id, String private_key, String out_trade_no,
			Map<String, Object> query_response) {
		String status = ALIPAY_TRADE_WAIT;
		try {
			// 调用查询交易的接口
			Map<String, String> reqParams = getBaseParam(app_id, "alipay.trade.query");
			Map<String, Object> biz_params = new HashMap<String, Object>();
			biz_params.put("out_trade_no", out_trade_no);
			reqParams.put("biz_content", FastJsonUtil.toJson(biz_params));
			reqParams.put("sign", createSign(reqParams, private_key));
			logger.info("支付宝交易查询->request:" + reqParams.toString());
			String resultStr = HttpClientUtil.post(AliPayUtil.HTTPS_GATEWAY_OPENAPI_URL, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			Map<String, Object> alipay_trade_query_response = (Map<String, Object>) resultMap
					.get("alipay_trade_query_response");
			logger.info("支付宝交易查询->response:" + alipay_trade_query_response.toString());
			String code = StringUtil.formatStr(alipay_trade_query_response.get("code"));
			query_response.putAll(alipay_trade_query_response);
			if (code.equals("10000")) {
				String trade_status = StringUtil.formatStr(alipay_trade_query_response.get("trade_status"));
				if (trade_status.equals("TRADE_SUCCESS")) {
					status = ALIPAY_TRADE_SUCCESS;
				} else if (trade_status.equals("TRADE_CLOSED")) {
					status = ALIPAY_TRADE_CLOSED;
				}
			} else if (code.equals("40004")) {
				status = ALIPAY_TRADE_CLOSED;
			}
		} catch (Exception e) {
			logger.error("支付宝交易查询异常", e);
		}
		return status;
	}

	/**
	 * 订单取消
	 * 
	 * @param app_id
	 * @param private_key
	 * @param biz_params
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public void orderCancel(final String app_id, final String private_key, final String out_trade_no)
			throws BusinessException, SystemException, Exception {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				int i = 3;
				while (i > 0) {
					try {
						Thread.sleep(1000);
						i--;
						// 调用查询交易的接口
						Map<String, String> reqParams = getBaseParam(app_id, "alipay.trade.cancel");
						Map<String, Object> biz_params = new HashMap<String, Object>();
						biz_params.put("out_trade_no", out_trade_no);
						reqParams.put("biz_content", FastJsonUtil.toJson(biz_params));
						reqParams.put("sign", createSign(reqParams, private_key));
						logger.info("支付宝交易取消->request:" + reqParams.toString());
						String resultStr = HttpClientUtil.post(AliPayUtil.HTTPS_GATEWAY_OPENAPI_URL, reqParams);
						Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
						Map<String, Object> alipay_trade_cancel_response = (Map<String, Object>) resultMap
								.get("alipay_trade_cancel_response");
						logger.info("支付宝交易取消->response:" + alipay_trade_cancel_response.toString());
						String code = StringUtil.formatStr(alipay_trade_cancel_response.get("code"));
						if (code.equals("10000")) {
							i = 0;
						}
					} catch (Exception e) {
						logger.error("支付宝交易取消异常", e);
					}
				}
			}
		});
	}

	/**
	 * 获取支付宝基础参数
	 * 
	 * @return
	 */
	public Map<String, String> getBaseParam(String app_id, String method) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("app_id", app_id);
		map.put("method", method);
		map.put("charset", "UTF-8");
		map.put("version", "1.0");
		map.put("sign_type", "RSA");
		map.put("timestamp", DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMddHHmmss));
		return map;
	}

	/**
	 * 服务器签名
	 * 
	 * @param params
	 * @return
	 */
	public static String createSign(Map<String, String> params, String private_key) {
		// 获取待签名字符串
		String preSignStr = createLinkString(params);
		return RSAUtil.sign(preSignStr, private_key, "UTF-8");
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */

	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			// 拼接时，不包括最后一个&字符
			if (i == keys.size() - 1) {
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

	/**
	 * 惠易定3.0支付宝Wap支付
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	@Override
	public void hydAliPayWapPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			try {
				String charset = "utf-8";
				ValidateUtil.validateParams(paramsMap, new String[] { "return_url", "subject", "body", "out_trade_no",
						"timeout_express", "total_fee" });
				String return_url = paramsMap.get("return_url") + "";
				String baseUrl = AliPayUtil.HTTPS_GATEWAY_MAPI_URL + "?_input_charset=" + charset;
				// 惠易定回调url
				String notify_url = PropertiesConfigUtil.getProperty("alipay.hyd_notify_url");
				// 合作者身份ID
				String partner = PropertiesConfigUtil.getProperty("alipay.partner");
				String private_key = PropertiesConfigUtil.getProperty("alipay.partner_private_key");
				Map<String, String> reqParams = new HashMap<String, String>();
				reqParams.put("service", "alipay.wap.create.direct.pay.by.user");
				reqParams.put("partner", partner);
				reqParams.put("seller_id", partner);
				reqParams.put("_input_charset", charset);
				reqParams.put("notify_url", notify_url);
				reqParams.put("return_url", return_url);

				// 业务参数
				reqParams.put("subject", paramsMap.get("subject") + "");
				reqParams.put("body", paramsMap.get("body") + "");
				reqParams.put("payment_type", "1");
				reqParams.put("out_trade_no", paramsMap.get("out_trade_no") + "");
				reqParams.put("it_b_pay", paramsMap.get("timeout_express") + "");
				reqParams.put("total_fee", paramsMap.get("total_fee") + "");

				// 扩展字段
				String passback_params = paramsMap.get("extra_common_param") + "";
				if (StringUtils.isNotBlank(passback_params)) {
					reqParams.put("extra_common_param", passback_params);
				}
				// 签名
				reqParams.put("sign", AliPayUtil.createSign(reqParams, private_key, charset));
				reqParams.put("sign_type", "RSA");
				String alipay_str = WebUtils.buildForm(baseUrl, reqParams);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("alipay_str", alipay_str);
				result.setResultData(map);
			} catch (BusinessException e) {
				throw e;
			} catch (SystemException e) {
				throw e;
			} catch (Exception e) {
				throw e;
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
	 * 惠易定3.0支付宝Wap支付(新接口,有问题)
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void hydAliPayWapPayNew(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "return_url", "subject", "body", "out_trade_no",
					"timeout_express", "total_amount" });
			String charset = "UTF-8";
			String baseUrl = AliPayUtil.HTTPS_GATEWAY_OPENAPI_URL;
			String return_url = paramsMap.get("return_url") + "";
			// 惠易定appid
			String app_id = PropertiesConfigUtil.getProperty("alipay.hyd_app_id");
			// 惠易定回调url
			String notify_url = PropertiesConfigUtil.getProperty("alipay.hyd_notify_url");
			// 商家信息
			String seller_id = PropertiesConfigUtil.getProperty("alipay.seller_id");
			String private_key = PropertiesConfigUtil.getProperty("alipay.partner_private_key");
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("app_id", app_id);
			reqParams.put("method", "alipay.trade.wap.pay");
			reqParams.put("return_url", return_url);
			reqParams.put("charset", charset);
			reqParams.put("sign_type", "RSA");
			reqParams.put("timestamp", DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMddHHmmss));
			reqParams.put("version", "1.0");
			reqParams.put("notify_url", notify_url);
			// 业务参数
			Map<String, String> biz_content = new HashMap<String, String>();
			biz_content.put("subject", paramsMap.get("subject") + "");
			biz_content.put("body", paramsMap.get("body") + "");
			biz_content.put("out_trade_no", paramsMap.get("out_trade_no") + "");
			biz_content.put("timeout_express", paramsMap.get("timeout_express") + "");
			biz_content.put("total_amount", paramsMap.get("total_amount") + "");
			biz_content.put("product_code", "QUICK_WAP_PAY");
			biz_content.put("seller_id", seller_id);
			String passback_params = paramsMap.get("passback_params") + "";
			if (StringUtils.isNotBlank(passback_params)) {
				biz_content.put("passback_params", URLEncoder.encode(passback_params, charset));
			}
			reqParams.put("biz_content", FastJsonUtil.toJson(biz_content));
			// 签名
			reqParams.put("sign", AliPayUtil.createSign(reqParams, private_key, charset));
			String alipay_str = WebUtils.buildForm(baseUrl, reqParams);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("alipay_str", alipay_str);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 惠易定3.0支付宝Wap支付
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	@Override
	public void hydAliPayPcPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			String charset = "utf-8";
			ValidateUtil.validateParams(paramsMap,
					new String[] { "return_url", "subject", "body", "out_trade_no", "timeout_express", "total_fee" });
			String return_url = paramsMap.get("return_url") + "";
			String baseUrl = AliPayUtil.HTTPS_GATEWAY_MAPI_URL + "?_input_charset=" + charset;
			// 惠易定回调url
			String notify_url = PropertiesConfigUtil.getProperty("alipay.hyd_notify_url");
			// 合作者身份ID
			String partner = PropertiesConfigUtil.getProperty("alipay.partner");
			String private_key = PropertiesConfigUtil.getProperty("alipay.partner_private_key");
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("service", "create_direct_pay_by_user");
			reqParams.put("partner", partner);
			reqParams.put("seller_id", partner);
			reqParams.put("_input_charset", charset);
			reqParams.put("notify_url", notify_url);
			reqParams.put("return_url", return_url);

			// 业务参数
			reqParams.put("subject", paramsMap.get("subject") + "");
			reqParams.put("body", paramsMap.get("body") + "");
			reqParams.put("payment_type", "1");
			reqParams.put("out_trade_no", paramsMap.get("out_trade_no") + "");
			reqParams.put("it_b_pay", paramsMap.get("timeout_express") + "");
			reqParams.put("total_fee", paramsMap.get("total_fee") + "");

			// 扩展字段
			String passback_params = paramsMap.get("extra_common_param") + "";
			if (StringUtils.isNotBlank(passback_params)) {
				reqParams.put("extra_common_param", passback_params);
			}
			// 签名
			reqParams.put("sign", AliPayUtil.createSign(reqParams, private_key, charset));
			reqParams.put("sign_type", "RSA");
			String alipay_str = WebUtils.buildForm(baseUrl, reqParams);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("alipay_str", alipay_str);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 本地生活-支付宝信息
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	@Override
	public Map<String, String> consumerAlipayInfoGet(Map<String, Object> paramsMap)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "out_trade_no", "amount", "subject" });
			String out_trade_no = paramsMap.get("out_trade_no") + "";
			String amount = paramsMap.get("amount") + "";
			String subject = paramsMap.get("subject") + "";

			// 私钥
			String private_key = PropertiesConfigUtil.getProperty("alipay.partner_private_key");
			// 合作身份者ID
			String partner = PropertiesConfigUtil.getProperty("alipay.partner");
			// 卖家账号
			String seller_id = PropertiesConfigUtil.getProperty("alipay.seller_id");
			String notify_url = PropertiesConfigUtil.getProperty("alipay.consumer_notify_url");
			return appPayInfoGet(out_trade_no, amount, subject, private_key, partner, seller_id, notify_url);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 店东助手-支付宝信息
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	@Override
	public Map<String, String> storeAlipayInfoGet(Map<String, Object> paramsMap)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "out_trade_no", "amount", "subject" });
			String out_trade_no = paramsMap.get("out_trade_no") + "";
			String amount = paramsMap.get("amount") + "";
			String subject = paramsMap.get("subject") + "";

			// 私钥
			String private_key = PropertiesConfigUtil.getProperty("alipay.partner_private_key");
			// 合作身份者ID
			String partner = PropertiesConfigUtil.getProperty("alipay.partner");
			// 卖家账号
			String seller_id = PropertiesConfigUtil.getProperty("alipay.seller_id");
			String notify_url = PropertiesConfigUtil.getProperty("alipay.store_notify_url");
			return appPayInfoGet(out_trade_no, amount, subject, private_key, partner, seller_id, notify_url);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 熟么-支付宝信息
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	@Override
	public Map<String, String> shumeAlipayInfoGet(Map<String, Object> paramsMap)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "out_trade_no", "amount", "subject" });
			String out_trade_no = paramsMap.get("out_trade_no") + "";
			String amount = paramsMap.get("amount") + "";
			String subject = paramsMap.get("subject") + "";

			// 私钥
			String private_key = PropertiesConfigUtil.getProperty("alipay.partner_private_key");
			// 合作身份者ID
			String partner = PropertiesConfigUtil.getProperty("alipay.partner");
			// 卖家账号
			String seller_id = PropertiesConfigUtil.getProperty("alipay.seller_id");
			String notify_url = PropertiesConfigUtil.getProperty("alipay.shume_notify_url");
			return appPayInfoGet(out_trade_no, amount, subject, private_key, partner, seller_id, notify_url);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 惠点收银-支付宝信息
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	@Override
	public Map<String, String> cashierAlipayInfoGet(Map<String, Object> paramsMap)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "out_trade_no", "amount", "subject" });
			String out_trade_no = paramsMap.get("out_trade_no") + "";
			String amount = paramsMap.get("amount") + "";
			String subject = paramsMap.get("subject") + "";
			// 私钥
			String private_key = PropertiesConfigUtil.getProperty("alipay.partner_private_key");
			// 合作身份者ID
			String partner = PropertiesConfigUtil.getProperty("alipay.partner");
			// 卖家账号
			String seller_id = PropertiesConfigUtil.getProperty("alipay.seller_id");
			String notify_url = PropertiesConfigUtil.getProperty("alipay.cashier_notify_url");
			return appPayInfoGet(out_trade_no, amount, subject, private_key, partner, seller_id, notify_url);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取支付宝支付信息(老接口)
	 * 
	 * @Title: alipayInfoGet
	 * @param out_trade_no
	 * @param amount
	 * @param subject
	 * @param private_key
	 * @param partner
	 * @param seller_id
	 * @param notify_url
	 * @return
	 * @author tiny
	 */
	private Map<String, String> appPayInfoGet(String out_trade_no, String amount, String subject, String private_key,
			String partner, String seller_id, String notify_url) throws Exception {
		String charset = "UTF-8";
		Map<String, String> signMap = new HashMap<String, String>();
		// 支付方法
		signMap.put("service", "mobile.securitypay.pay");
		// 合作身份者ID // 私钥(老接口字段)
		signMap.put("partner", partner);
		// 参数编码字符集
		signMap.put("_input_charset", charset);
		// 卖家账号 // 私钥(老接口字段)
		signMap.put("seller_id", seller_id);
		// 异步通知url // 私钥(老接口字段)
		signMap.put("notify_url", notify_url);
		// 商户订单号
		signMap.put("out_trade_no", out_trade_no);
		// 商品名称
		signMap.put("subject", subject);
		// 支付类型(1:商品购买)
		signMap.put("payment_type", "1");
		// 未付款交易的超时时间(10分钟)
		signMap.put("total_fee", amount);
		// 未付款交易的超时时间
		signMap.put("it_b_pay", "10m");
		// 拼接参数
		String linkString = createLinkString(signMap);
		// 签名(签名URLEncoder.encode)
		String sign = URLEncoder.encode(AliPayUtil.createSign(signMap, private_key, charset), charset);
		// 组装支付参数
		Map<String, String> alipayMap = new HashMap<String, String>();
		alipayMap.put("alipay_params", linkString + "&sign_type=RSA&sign=" + sign);
		// 合作身份者ID
		alipayMap.put("partner", partner);
		// 卖家账号
		alipayMap.put("seller_id", seller_id);
		// 异步通知url
		alipayMap.put("notify_url", notify_url);
		// 私钥
		alipayMap.put("private_key", private_key);
		return alipayMap;
	}

	/**
	 * 1 对账单URl查询
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	@Override
	public String billDownloadUrlQuery(String app_id, String private_key, String bill_date)
			throws BusinessException, SystemException, Exception {
		String bill_download_url = null;
		Map<String, String> biz_content = new HashMap<String, String>();
		biz_content.put("bill_type", "trade");
		biz_content.put("bill_date", bill_date);
		Map<String, String> reqParams = getBaseParam(app_id, "alipay.data.dataservice.bill.downloadurl.query");
		reqParams.put("biz_content", FastJsonUtil.toJson(biz_content));
		reqParams.put("sign", createSign(reqParams, private_key));
		logger.info("下载对账单->request:" + reqParams.toString());
		String resultStr = HttpClientUtil.post(AliPayUtil.HTTPS_GATEWAY_OPENAPI_URL, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		Map<String, Object> responseMap = (Map<String, Object>) resultMap
				.get("alipay_data_dataservice_bill_downloadurl_query_response");
		logger.info("下载对账单->response:" + responseMap.toString());
		String code = StringUtil.formatStr(responseMap.get("code"));
		if (code.equals("10000")) {
			bill_download_url = StringUtil.formatStr(responseMap.get("bill_download_url"));
		}
		return bill_download_url;
	}

	/**
	 * 2 下载文件
	 * 
	 * @Title: downloadFile
	 * @param url
	 * @param bill_type
	 * @param bill_date
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	private String downloadFile(String url, String bill_date) throws Exception {
		// 下载对账单到本地
		String billPath = basePath + bill_date.replace("-", "") + File.separator;
		File billFileDir = new File(billPath);
		if (!billFileDir.exists()) {
			billFileDir.mkdirs();
		}
		String uploadFilePath = billPath + bill_date.replace("-", "") + "_bill";
		File uploadFile = new File(uploadFilePath);
		if (uploadFile.exists()) {
			FileUtil.delListFile(uploadFilePath);
		}
		uploadFile.mkdirs();
		// zip压缩文件名称
		String zipPath = uploadFilePath + ".zip";
		// 下载zip文件
		HttpClientUtil.download(url, zipPath);
		// 解压zip文件
		ZipUtil.unzip(zipPath, uploadFilePath);
		return uploadFilePath;
	}

	/**
	 * 3 导入数据入库
	 * 
	 * @Title: importDate
	 * @param importFile
	 * @param bill_date
	 * @throws Exception
	 * @author tiny
	 */
	private void importDate(File importFile, String bill_date) throws Exception {
		CsvReader reader = null;
		Date date = new Date();
		try {
			reader = new CsvReader(importFile.getAbsolutePath(), ',', Charset.forName("GBK"));
			List<FDAlipayBillLog> datalist = new ArrayList<FDAlipayBillLog>();
			boolean isStart = false;
			while (reader.readRecord()) {
				String record = reader.getRawRecord();
				if (StringUtils.isNotEmpty(record)) {
					String[] strs = record.split(",");
					if (strs[0].contains("业务明细列表结束")) {
						break;
					}
					if (isStart) {
						FDAlipayBillLog billLog = new FDAlipayBillLog();
						billLog.setLog_id(IDUtil.getUUID());
						billLog.setTrade_no(strs[0].trim());
						billLog.setOut_trade_no(strs[1].trim());
						billLog.setBill_date(bill_date);
						billLog.setDesc1(strs[3].trim());
						billLog.setTrade_create_date(DateUtil.str2Date(strs[4].trim(), DateUtil.fmt_yyyyMMddHHmmss));
						billLog.setTrade_finish_date(DateUtil.str2Date(strs[5].trim(), DateUtil.fmt_yyyyMMddHHmmss));
						billLog.setTrade_account(strs[10].trim());
						billLog.setOrder_amount(new BigDecimal(strs[11].trim()));
						billLog.setStatus(Constant.STATUS_NORMAL);
						billLog.setCreated_date(date);
						billLog.setModified_date(date);
						datalist.add(billLog);
						if (datalist.size() > 6000) {
							fDAlipayBillLogDao.insertFDAlipayBillLog(datalist);
							datalist.clear();
						}
					}
					if (isStart == false && "支付宝交易号".equals(strs[0])) {
						isStart = true;
					}
				}
			}
			if (datalist.size() > 0) {
				fDAlipayBillLogDao.insertFDAlipayBillLog(datalist);
				datalist.clear();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != reader) {
				reader.close();
			}
		}
	}

	/**
	 * 扫码支付对账单下载
	 * 
	 * @Title: consumerBillDownloadUrlQuery
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void alipayBillImport(String bill_date, String app_id, String private_key)
			throws BusinessException, SystemException, Exception {
		String uploadFilePath = null;
		Map<String, Object> tempMap = new HashMap<String, Object>();
		try {
			tempMap.put("bill_date", bill_date);
			Integer count_num = fDAlipayBillLogDao.selectAlipayBillLogCount(tempMap);
			if (count_num > 0) {
				return;
			}
			// 获取对账单地址
			String url = billDownloadUrlQuery(app_id, private_key, bill_date);
			if (StringUtils.isNotEmpty(url)) {
				// 下载文件
				uploadFilePath = downloadFile(url, bill_date);

				// 得到需要导入的文件
				File unZipFile = new File(uploadFilePath);
				File importFile = null;
				File[] tempFiles = unZipFile.listFiles();
				for (File file : tempFiles) {
					if (file.getName().endsWith("_业务明细.csv")) {
						importFile = file;
						break;
					}
				}
				// 解析文件并导入数据
				if (importFile != null) {
					importDate(importFile, bill_date);
				} else {
					logger.warn("未找到需要导入的CSV文件");
				}
			} else {
				logger.warn("获取账单url异常");
			}
		} catch (Exception e) {
			/** 如果出现异常，删除数据并且删除下载的文件 **/
			// 删除数据
			fDAlipayBillLogDao.deleteAlipayBillLog(tempMap);
			new File(uploadFilePath + ".zip").delete();
			throw e;
		} finally {
			// 删除文件
			if (uploadFilePath != null) {
				FileUtil.delListFile(uploadFilePath);
			}
		}
	}

	@Override
	public void billCheck(String bill_date) throws BusinessException, SystemException, Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("bill_date", bill_date);
		fDAlipayBillLogDao.deleteAlipayBillCheckLog(tempMap);
		fDAlipayBillLogDao.insertOpsBillCheckLog(tempMap);
		fDAlipayBillLogDao.insertAlipayBillCheckLog(tempMap);
	}

}
