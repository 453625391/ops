package com.meitianhui.finance.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.MD5Util;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.finance.constant.Constant;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.service.TradeService;
import com.meitianhui.finance.util.WechatRequest;

/**
 * 微信异步结果通知
 * 
 * @author Tiny
 *
 */
@Controller
public class WechatNotifyControler {

	private static Logger logger = Logger.getLogger(WechatNotifyControler.class);

	@Autowired
	private TradeService tradeService;

	@RequestMapping(value = "/consumerWechatNotify")
	public void consumerWechatNotity(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "本地生活-";
		// 返回给微信的结果
		String returnCode = "SUCCESS";
		String returnMsg = "成功";
		try {
			String wechat_notify_data = request.getParameter("wechat_notify_data");
			logger.info(type + "微信异步接口:接受的数据" + wechat_notify_data);
			if (StringUtils.isEmpty(wechat_notify_data)) {
				logger.error(type + "微信异步接口:数据丢失");
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据丢失");
			}
			final Map<String, Object> params = FastJsonUtil.jsonToMap(wechat_notify_data);
			String app_key = PropertiesConfigUtil.getProperty("wechat.consumer_app_key");
			if (!checkIsSignValidFromResponseString(params, app_key)) {
				returnMsg = type + "数据签名非法，有可能被第三方篡改";
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据签名非法，有可能被第三方篡改");
			}

			// 判断交易是否成功
			String result_code = params.get("result_code").toString();
			String return_code = params.get("return_code").toString();
			if (result_code.equals("SUCCESS") && return_code.equals("SUCCESS")) {
				logger.info(type + "微信异步结果通知:调用交易确认接口");
				String transaction_no = (String) params.get("out_trade_no");
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.clear();
				resultMap.put("transaction_no", transaction_no);
				// 查询数据只要不抛出异常,
				boolean flag = false;
				try {
					tradeService.transactionStatusFind(resultMap, new ResultData());
				} catch (Exception e) {
					// 如果交易结果表中不存在此数据,则可以进行交易确认操作
					flag = true;
				}
				if (flag) {
					
					resultMap.clear();
					resultMap.put("transaction_status", Constant.TRANSACTION_STATUS_CONFIRMED);
					resultMap.put("transaction_no", transaction_no);
					resultMap.put("transaction_body", FastJsonUtil.toJson(params));
					ResultData result = new ResultData();
					tradeService.transactionConfirmed(resultMap, result);
				}
			}
		} catch (BusinessException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(e.getMsg());
		} catch (SystemException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} catch (Exception e) {
			returnCode = "FAIL";
			returnMsg = e.getMessage();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} finally {
			PrintWriter out = null;
			try {
				out = response.getWriter();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("return_code", returnCode);
				resultMap.put("return_msg", returnMsg);
				logger.info("return data:" + resultMap.toString() + ",耗时" + (System.currentTimeMillis() - startTime)
						+ "毫秒");
				out.println(WechatRequest.mapToXMLWeChat(resultMap));
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				if (null != out) {
					out.close();
				}
			}
		}
	}

	@RequestMapping(value = "/storeWechatNotify")
	public void storeWechatNotity(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "店东助手-";
		// 返回给微信的结果
		String returnCode = "SUCCESS";
		String returnMsg = "成功";
		try {
			String wechat_notify_data = request.getParameter("wechat_notify_data");
			logger.info(type + "微信异步接口:接受的数据" + wechat_notify_data);
			if (StringUtils.isEmpty(wechat_notify_data)) {
				logger.error(type + "微信异步接口:数据丢失");
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据丢失");
			}
			final Map<String, Object> params = FastJsonUtil.jsonToMap(wechat_notify_data);
			String app_key = PropertiesConfigUtil.getProperty("wechat.store_app_key");
			if (!checkIsSignValidFromResponseString(params, app_key)) {
				returnMsg = type + "数据签名非法，有可能被第三方篡改";
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据签名非法，有可能被第三方篡改");
			}
			// 判断交易是否成功
			String result_code = params.get("result_code").toString();
			String return_code = params.get("return_code").toString();
			if (result_code.equals("SUCCESS") && return_code.equals("SUCCESS")) {

				logger.info(type + "微信异步结果通知:调用交易确认接口");
				String transaction_no = (String) params.get("out_trade_no");
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.clear();
				resultMap.put("transaction_no", transaction_no);
				// 查询数据只要不抛出异常,
				boolean flag = false;
				try {
					tradeService.transactionStatusFind(resultMap, new ResultData());
				} catch (Exception e) {
					// 如果交易结果表中不存在此数据,则可以进行交易确认操作
					flag = true;
				}
				if (flag) {
					resultMap.put("transaction_status", Constant.TRANSACTION_STATUS_CONFIRMED);
					resultMap.put("transaction_no", transaction_no);
					resultMap.put("transaction_body", FastJsonUtil.toJson(params));
					ResultData result = new ResultData();
					tradeService.transactionConfirmed(resultMap, result);
				}
			}
		} catch (BusinessException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(e.getMsg());
		} catch (SystemException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} catch (Exception e) {
			returnCode = "FAIL";
			returnMsg = e.getMessage();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} finally {
			PrintWriter out = null;
			try {
				out = response.getWriter();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("return_code", returnCode);
				resultMap.put("return_msg", returnMsg);
				logger.info("return data:" + resultMap.toString() + ",耗时" + (System.currentTimeMillis() - startTime)
						+ "毫秒");
				out.println(WechatRequest.mapToXMLWeChat(resultMap));
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				if (null != out) {
					out.close();
				}
			}
		}
	}

	@RequestMapping(value = "/sdjWechatNotify")
	public void sdjWechatNotity(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "颂到家-";
		// 返回给微信的结果
		String returnCode = "SUCCESS";
		String returnMsg = "成功";
		try {
			String wechat_notify_data = request.getParameter("wechat_notify_data");
			logger.info(type + "微信异步接口:接受的数据" + wechat_notify_data);
			if (StringUtils.isEmpty(wechat_notify_data)) {
				logger.error(type + "微信异步接口:数据丢失");
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据丢失");
			}
			final Map<String, Object> params = FastJsonUtil.jsonToMap(wechat_notify_data);
			String app_key = PropertiesConfigUtil.getProperty("wechat.sdj_app_key");
			if (!checkIsSignValidFromResponseString(params, app_key)) {
				returnMsg = type + "数据签名非法，有可能被第三方篡改";
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据签名非法，有可能被第三方篡改");
			}
			// 判断交易是否成功
			String result_code = params.get("result_code").toString();
			String return_code = params.get("return_code").toString();
			if (result_code.equals("SUCCESS") && return_code.equals("SUCCESS")) {
				logger.info(type + "微信异步结果通知:调用交易确认接口");
				String transaction_no = (String) params.get("out_trade_no"); 
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.clear();
				resultMap.put("transaction_no", transaction_no);
				// 查询数据只要不抛出异常,
				boolean flag = false;
				try {
					tradeService.transactionStatusFind(resultMap, new ResultData());
				} catch (Exception e) {
					// 如果交易结果表中不存在此数据,则可以进行交易确认操作
					flag = true;
				}
				if (flag) {
					resultMap.put("transaction_status", Constant.TRANSACTION_STATUS_CONFIRMED);
					resultMap.put("transaction_no", transaction_no);
					resultMap.put("transaction_body", FastJsonUtil.toJson(params));
					ResultData result = new ResultData();
					tradeService.transactionConfirmed(resultMap, result);
				}
			}
		} catch (BusinessException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(e.getMsg());
		} catch (SystemException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} catch (Exception e) {
			returnCode = "FAIL";
			returnMsg = e.getMessage();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} finally {
			PrintWriter out = null;
			try {
				out = response.getWriter();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("return_code", returnCode);
				resultMap.put("return_msg", returnMsg);
				logger.info("return data:" + resultMap.toString() + ",耗时" + (System.currentTimeMillis() - startTime)
						+ "毫秒");
				out.println(WechatRequest.mapToXMLWeChat(resultMap));
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				if (null != out) {
					out.close();
				}
			}
		}
	}

	@RequestMapping(value = "/shumeWechatNotify")
	public void shumeWechatNotity(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "熟么-";
		// 返回给微信的结果
		String returnCode = "SUCCESS";
		String returnMsg = "成功";
		try {
			String wechat_notify_data = request.getParameter("wechat_notify_data");
			logger.info(type + "微信异步接口:接受的数据" + wechat_notify_data);
			if (StringUtils.isEmpty(wechat_notify_data)) {
				logger.error(type + "微信异步接口:数据丢失");
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据丢失");
			}
			final Map<String, Object> params = FastJsonUtil.jsonToMap(wechat_notify_data);
			String app_key = PropertiesConfigUtil.getProperty("wechat.shume_app_key");
			if (!checkIsSignValidFromResponseString(params, app_key)) {
				returnMsg = type + "数据签名非法，有可能被第三方篡改";
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据签名非法，有可能被第三方篡改");
			}

			// 判断交易是否成功
			String result_code = params.get("result_code").toString();
			String return_code = params.get("return_code").toString();
			returnCode = "SUCCESS";
			returnMsg = "成功";
			if (result_code.equals("SUCCESS") && return_code.equals("SUCCESS")) {
				logger.info(type + "微信异步结果通知:调用交易确认接口");
				String transaction_no = (String) params.get("out_trade_no");
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.clear();
				resultMap.put("transaction_no", transaction_no);
				// 查询数据只要不抛出异常,
				boolean flag = false;
				try {
					tradeService.transactionStatusFind(resultMap, new ResultData());
				} catch (Exception e) {
					// 如果交易结果表中不存在此数据,则可以进行交易确认操作
					flag = true;
				}
				if (flag) {
					resultMap.put("transaction_status", Constant.TRANSACTION_STATUS_CONFIRMED);
					resultMap.put("transaction_no", transaction_no);
					resultMap.put("transaction_body", FastJsonUtil.toJson(params));
					ResultData result = new ResultData();
					tradeService.transactionConfirmed(resultMap, result);
				}
			}
		} catch (BusinessException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(e.getMsg());
		} catch (SystemException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} catch (Exception e) {
			returnCode = "FAIL";
			returnMsg = e.getMessage();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} finally {
			PrintWriter out = null;
			try {
				out = response.getWriter();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("return_code", returnCode);
				resultMap.put("return_msg", returnMsg);
				logger.info("return data:" + resultMap.toString() + ",耗时" + (System.currentTimeMillis() - startTime)
						+ "毫秒");
				out.println(WechatRequest.mapToXMLWeChat(resultMap));
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				if (null != out) {
					out.close();
				}
			}
		}
	}

	@RequestMapping(value = "/huidianWechatNotify")
	public void huidianWechatNotify(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "惠点公众号-";
		// 返回给支付的结果
		// 返回给微信的结果
		String returnCode = "SUCCESS";
		String returnMsg = "成功";
		try {
			String wechat_notify_data = request.getParameter("wechat_notify_data");
			logger.info(type + "微信异步接口:接受的数据" + wechat_notify_data);
			if (StringUtils.isEmpty(wechat_notify_data)) {
				logger.error(type + "微信异步接口:数据丢失");
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据丢失");
			}
			final Map<String, Object> params = FastJsonUtil.jsonToMap(wechat_notify_data);
			String app_key = PropertiesConfigUtil.getProperty("wechat.huidian_app_key");
			if (!checkIsSignValidFromResponseString(params, app_key)) {
				returnMsg = type + "数据签名非法，有可能被第三方篡改";
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据签名非法，有可能被第三方篡改");
			}
			// 判断交易是否成功
			String result_code = params.get("result_code").toString();
			String return_code = params.get("return_code").toString();
			if (result_code.equals("SUCCESS") && return_code.equals("SUCCESS")) {
				logger.info(type + "微信异步结果通知:调用交易确认接口");
				String transaction_no = (String) params.get("out_trade_no");
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.clear();
				resultMap.put("transaction_no", transaction_no);
				// 查询数据只要不抛出异常,
				boolean flag = false;
				try {
					tradeService.transactionStatusFind(resultMap, new ResultData());
				} catch (Exception e) {
					// 如果交易结果表中不存在此数据,则可以进行交易确认操作
					flag = true;
				}
				if (flag) {
					resultMap.put("transaction_status", Constant.TRANSACTION_STATUS_CONFIRMED);
					resultMap.put("transaction_no", transaction_no);
					resultMap.put("transaction_body", FastJsonUtil.toJson(params));
					ResultData result = new ResultData();
					tradeService.transactionConfirmed(resultMap, result);
				}
			}
		} catch (BusinessException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(e.getMsg());
		} catch (SystemException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} catch (Exception e) {
			returnCode = "FAIL";
			returnMsg = e.getMessage();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} finally {
			PrintWriter out = null;
			try {
				out = response.getWriter();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("return_code", returnCode);
				resultMap.put("return_msg", returnMsg);
				logger.info("return data:" + resultMap.toString() + ",耗时" + (System.currentTimeMillis() - startTime)
						+ "毫秒");
				out.println(WechatRequest.mapToXMLWeChat(resultMap));
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				if (null != out) {
					out.close();
				}
			}
		}
	}
	@RequestMapping(value = "/cashierWechatNotify")
	public void cashierWechatNotify(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "惠点收银-";
		// 返回给支付的结果
		// 返回给微信的结果
		String returnCode = "SUCCESS";
		String returnMsg = "成功";
		try {
			String wechat_notify_data = request.getParameter("wechat_notify_data");
			logger.info(type + "微信异步接口:接受的数据" + wechat_notify_data);
			if (StringUtils.isEmpty(wechat_notify_data)) {
				logger.error(type + "微信异步接口:数据丢失");
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据丢失");
			}
			final Map<String, Object> params = FastJsonUtil.jsonToMap(wechat_notify_data);
			String app_key = PropertiesConfigUtil.getProperty("wechat.cashier_app_key");
			if (!checkIsSignValidFromResponseString(params, app_key)) {
				returnMsg = type + "数据签名非法，有可能被第三方篡改";
				throw new BusinessException(RspCode.WECHAT_ERROR, "数据签名非法，有可能被第三方篡改");
			}
			// 判断交易是否成功
			String result_code = params.get("result_code").toString();
			String return_code = params.get("return_code").toString();
			if (result_code.equals("SUCCESS") && return_code.equals("SUCCESS")) {
				logger.info(type + "微信异步结果通知:调用交易确认接口");
				String transaction_no = (String) params.get("out_trade_no");
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.clear();
				resultMap.put("transaction_no", transaction_no);
				// 查询数据只要不抛出异常,
				boolean flag = false;
				try {
					tradeService.transactionStatusFind(resultMap, new ResultData());
				} catch (Exception e) {
					// 如果交易结果表中不存在此数据,则可以进行交易确认操作
					flag = true;
				}
				if (flag) {
					resultMap.put("transaction_status", Constant.TRANSACTION_STATUS_CONFIRMED);
					resultMap.put("transaction_no", transaction_no);
					resultMap.put("transaction_body", FastJsonUtil.toJson(params));
					ResultData result = new ResultData();
					tradeService.transactionConfirmed(resultMap, result);
				}
			}
		} catch (BusinessException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(e.getMsg());
		} catch (SystemException e) {
			returnCode = "FAIL";
			returnMsg = e.getMsg();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} catch (Exception e) {
			returnCode = "FAIL";
			returnMsg = e.getMessage();
			logger.error(type + "微信异步接口:接受微信异步通知异常", e);
		} finally {
			PrintWriter out = null;
			try {
				out = response.getWriter();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("return_code", returnCode);
				resultMap.put("return_msg", returnMsg);
				logger.info("return data:" + resultMap.toString() + ",耗时" + (System.currentTimeMillis() - startTime)
						+ "毫秒");
				out.println(WechatRequest.mapToXMLWeChat(resultMap));
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				if (null != out) {
					out.close();
				}
			}
		}
	}

	/**
	 * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
	 * 
	 * @param responseString
	 *            API返回的XML数据字符串
	 * @return API签名是否合法
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static boolean checkIsSignValidFromResponseString(Map<String, Object> map, String app_key)
			throws ParserConfigurationException, IOException, SAXException {

		String signFromAPIResponse = map.get("sign").toString();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			logger.error("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
			return false;
		}
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		map.remove("sign");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		String signForAPIResponse = getSign(map, app_key);
		if (!signForAPIResponse.equals(signFromAPIResponse)) {
			// 签名验不过，表示这个API返回的数据有可能已经被篡改了
			logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
			logger.info("据根据用签名算法进行计算新的签名是:" + signForAPIResponse);
			logger.error("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			return false;
		}
		return true;
	}

	/**
	 * 数据根据用签名算法进行计算新的签名
	 * 
	 * @param responseString
	 *            API返回的数据
	 * @return 新的签名
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static String getSign(Map<String, Object> map, String app_key) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != "") {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + app_key;
		result = MD5Util.MD5Encode(result).toUpperCase();
		return result;
	}
}
