package com.meitianhui.finance.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.finance.constant.Constant;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.service.TradeService;
import com.meitianhui.finance.util.AliPayUtil;

/**
 * 支付宝异步结果通知控制层
 * 
 * @author Tiny
 *
 */
@Controller
public class AlipayNotifyControler {

	private static final Logger logger = Logger.getLogger(AlipayNotifyControler.class);

	@Autowired
	private TradeService tradeService;

	@RequestMapping(value = "/consumerAlipayNotify")
	public void consumerAlipayNotity(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "本地生活-";
		// 返回给支付的结果
		String returnAlipay = "success";
		final Map<String, Object> params = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			// 打印参数
			logger.info(type + "支付宝异步接口:接受的数据：" + StringUtil.createLinkString(params));
			// 获取支付宝的通知返回参数
			String alipay_partner_public_key = PropertiesConfigUtil.getProperty("alipay.alipay_partner_public_key");
			if (AliPayUtil.verify(params, alipay_partner_public_key, "utf-8")) {
				/** 交易完成,处理对应的业务逻辑 **/
				// 交易状态
				String trade_status = (String) params.get("trade_status");
				if (trade_status.equals("TRADE_SUCCESS")) {
					// 商户订单号
					logger.info(type + "支付宝异步结果通知:调用交易确认接口");
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
			}
		} catch (BusinessException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (SystemException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (Exception e) {
			logger.error(type + "支付宝确认异常", e);
		} finally {
			logger.info("return data:" + returnAlipay + ",耗时" + (System.currentTimeMillis() - startTime) + "毫秒");
			out.println(returnAlipay);
		}
	}

	@RequestMapping(value = "/storeAlipayNotify")
	public void storeAlipayNotity(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "店东助手-";
		// 返回给支付的结果
		String returnAlipay = "success";

		final Map<String, Object> params = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			// 打印参数
			logger.info(type + "支付宝异步接口:接受的数据：" + StringUtil.createLinkString(params));

			// 获取支付宝的通知返回参数
			String alipay_partner_public_key = PropertiesConfigUtil.getProperty("alipay.alipay_partner_public_key");
			if (AliPayUtil.verify(params, alipay_partner_public_key, "utf-8")) {
				// 交易状态
				String trade_status = (String) params.get("trade_status");
				if (trade_status.equals("TRADE_SUCCESS")) {
					// 商户订单号
					logger.info(type + "支付宝异步结果通知:调用交易确认接口");
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
			}
		} catch (BusinessException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (SystemException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (Exception e) {
			logger.error(type + "支付宝确认异常", e);
		} finally {
			logger.info("return data:" + returnAlipay + ",耗时" + (System.currentTimeMillis() - startTime) + "毫秒");
			out.println(returnAlipay);
		}
	}

	@RequestMapping(value = "/sdjAlipayNotify")
	public void sdjAlipayNotify(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "颂到家-";
		// 返回给支付的结果
		String returnAlipay = "success";
		final Map<String, Object> params = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			// 打印参数
			logger.info(type + "支付宝异步接口:接受的数据：" + StringUtil.createLinkString(params));
			// 交易状态
			String trade_status = (String) params.get("trade_status");
			// 获取支付宝的通知返回参数
			String alipay_partner_public_key = PropertiesConfigUtil.getProperty("alipay.alipay_partner_public_key");
			if (AliPayUtil.verify(params, alipay_partner_public_key, "utf-8")) {
				/** 交易完成,处理对应的业务逻辑 **/
				if (trade_status.equals("TRADE_SUCCESS")) {
					// 商户订单号
					logger.info(type + "支付宝异步结果通知:调用交易确认接口");
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
			}
		} catch (BusinessException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (SystemException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (Exception e) {
			logger.error(type + "支付宝确认异常", e);
		} finally {
			logger.info("return data:" + returnAlipay + ",耗时" + (System.currentTimeMillis() - startTime) + "毫秒");
			out.println(returnAlipay);
		}
	}

	@RequestMapping(value = "/faceAlipayNotify")
	public void faceAlipayNotify(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "收款码支付";
		// 返回给支付的结果
		String returnAlipay = "success";
		final Map<String, Object> params = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			// 打印参数
			logger.info(type + "支付宝异步接口:接受的数据：" + StringUtil.createLinkString(params));
			if (params.size() == 0) {
				throw new BusinessException(RspCode.ALIPAY_ERROR, "参数为空");
			}
			String input_charset = "utf-8";
			String charset = StringUtil.formatStr(params.get("charset"));
			if (StringUtils.isNotEmpty(charset)) {
				input_charset = charset;
			}
			// 交易状态
			String trade_status = (String) params.get("trade_status");
			// 获取支付宝的通知返回参数
			String alipay_partner_public_key = PropertiesConfigUtil
					.getProperty("alipay.face_pay_alipay_open_api_public_key");
			if (AliPayUtil.verify(params, alipay_partner_public_key, input_charset)) {
				/** 交易完成,处理对应的业务逻辑 **/
				if (trade_status.equals("TRADE_SUCCESS")) {
					// 商户订单号
					logger.info(type + "支付宝异步结果通知:调用交易确认接口");
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
			}
		} catch (BusinessException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (SystemException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (Exception e) {
			logger.error(type + "支付宝确认异常", e);
		} finally {
			logger.info("return data:" + returnAlipay + ",耗时" + (System.currentTimeMillis() - startTime) + "毫秒");
			out.println(returnAlipay);
		}
	}

	@RequestMapping(value = "/shumeAlipayNotify")
	public void shumeAlipayNotify(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "熟么-";
		// 返回给支付的结果
		String returnAlipay = "success";
		final Map<String, Object> params = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			// 打印参数
			logger.info(type + "支付宝异步接口:接受的数据：" + StringUtil.createLinkString(params));
			// 交易状态
			String trade_status = (String) params.get("trade_status");
			// 获取支付宝的通知返回参数
			String alipay_partner_public_key = PropertiesConfigUtil.getProperty("alipay.alipay_partner_public_key");
			if (AliPayUtil.verify(params, alipay_partner_public_key, "utf-8")) {
				/** 交易完成,处理对应的业务逻辑 **/
				if (trade_status.equals("TRADE_SUCCESS")) {
					// 商户订单号
					logger.info(type + "支付宝异步结果通知:调用交易确认接口");
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
			}
		} catch (BusinessException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (SystemException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (Exception e) {
			logger.error(type + "支付宝确认异常", e);
		} finally {
			logger.info("return data:" + returnAlipay + ",耗时" + (System.currentTimeMillis() - startTime) + "毫秒");
			out.println(returnAlipay);
		}
	}

	@RequestMapping(value = "/hydAlipayNotify")
	public void hydAlipayNotify(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "惠易定";
		// 返回给支付的结果
		String returnAlipay = "success";
		final Map<String, Object> params = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			// 打印参数
			logger.info(type + "支付宝异步接口:接受的数据：" + StringUtil.createLinkString(params));
			if (params.size() == 0) {
				throw new BusinessException(RspCode.ALIPAY_ERROR, "参数为空");
			}
			String input_charset = "utf-8";
			String charset = StringUtil.formatStr(params.get("charset"));
			if (StringUtils.isNotEmpty(charset)) {
				input_charset = charset;
			}
			// 交易状态
			String trade_status = (String) params.get("trade_status");
			// 获取支付宝的通知返回参数
			String alipay_partner_public_key = PropertiesConfigUtil.getProperty("alipay.alipay_partner_public_key");
			if (AliPayUtil.verify(params, alipay_partner_public_key, input_charset)) {
				/** 交易完成,处理对应的业务逻辑 **/
				if (trade_status.equals("TRADE_SUCCESS")) {
					// 商户订单号
					logger.info(type + "支付宝异步结果通知:调用交易确认接口");
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
			}
		} catch (BusinessException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (SystemException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (Exception e) {
			logger.error(type + "支付宝确认异常", e);
		} finally {
			logger.info("return data:" + returnAlipay + ",耗时" + (System.currentTimeMillis() - startTime) + "毫秒");
			out.println(returnAlipay);
		}
	}

	@RequestMapping(value = "/cashierAlipayNotify")
	public void cashierAlipayNotify(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String type = "惠点收银";
		// 返回给支付的结果
		String returnAlipay = "success";
		final Map<String, Object> params = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			// 打印参数
			logger.info(type + "支付宝异步接口:接受的数据：" + StringUtil.createLinkString(params));
			if (params.size() == 0) {
				throw new BusinessException(RspCode.ALIPAY_ERROR, "参数为空");
			}
			String input_charset = "utf-8";
			String charset = StringUtil.formatStr(params.get("charset"));
			if (StringUtils.isNotEmpty(charset)) {
				input_charset = charset;
			}
			// 交易状态
			String trade_status = (String) params.get("trade_status");
			// 获取支付宝的通知返回参数
			String alipay_partner_public_key = PropertiesConfigUtil.getProperty("alipay.alipay_partner_public_key");
			if (AliPayUtil.verify(params, alipay_partner_public_key, input_charset)) {
				/** 交易完成,处理对应的业务逻辑 **/
				if (trade_status.equals("TRADE_SUCCESS")) {
					// 商户订单号
					logger.info(type + "支付宝异步结果通知:调用交易确认接口");
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
			}
		} catch (BusinessException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (SystemException e) {
			returnAlipay = e.getMsg();
			logger.error(type + "支付宝确认失败," + e.getMsg());
		} catch (Exception e) {
			logger.error(type + "支付宝确认异常", e);
		} finally {
			logger.info("return data:" + returnAlipay + ",耗时" + (System.currentTimeMillis() - startTime) + "毫秒");
			out.println(returnAlipay);
		}
	}
}
