package com.meitianhui.order.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.MD5Util;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.service.CsOrderService;
import com.meitianhui.order.service.impl.MobileRechargeImplService;

/**
 * 话费充值回调接口
 * 
 * @author Tiny
 *
 */
@Controller
@RequestMapping("/daHanNotify")
public class MobileRechargeNotifyController {

	private static final Logger logger = Logger.getLogger(MobileRechargeNotifyController.class);

	@Autowired
	private CsOrderService csOrderService;

	@RequestMapping(value = "/mobileRechargeNotify")
	public void mobileRechargeNotify(HttpServletRequest request, HttpServletResponse response) {
		// 返回给支付的结果
		String resultCode = "1111";
		String resultMsg = "处理失败!";
		PrintWriter out = null;
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			//获取输出流
			out = response.getWriter();
			String notify_data_str = request.getParameter("dahan_notify_data") + "";
			logger.info("mobile_recharge_notify:" + notify_data_str);
			Map<String, Object> params = FastJsonUtil.jsonToMap(notify_data_str);
			
			// 打印参数
			String sign = params.get("sign") + "";
			String clientOrderId = params.get("clientOrderId") + "";
			String mobile = params.get("mobile") + "";
			String callBackTime = params.get("callBackTime") + "";
			String status = params.get("status") + "";
			String errorCode = params.get("errorCode") + "";
			String errorDesc = params.get("errorDesc") + "";
			String intervalTime = params.get("intervalTime") + "";
			String clientSubmitTime = params.get("clientSubmitTime") + "";
			String discount = params.get("discount") + "";
			String costMoney = params.get("costMoney") + "";
			Map<String, String> signMap = new LinkedHashMap<String, String>();
			signMap.put("clientOrderId", clientOrderId);
			signMap.put("mobile", mobile);
			signMap.put("callBackTime", callBackTime);
			signMap.put("status", status);
			if (StringUtils.isNotEmpty(errorCode)) {
				signMap.put("errorCode", errorCode);
			}
			if (StringUtils.isNotEmpty(errorDesc)) {
				signMap.put("errorDesc", errorDesc);
			}
			signMap.put("intervalTime", intervalTime);
			signMap.put("clientSubmitTime", clientSubmitTime);
			signMap.put("discount", discount);
			signMap.put("costMoney", costMoney);
			String md5Pwd = MD5Util.sign(MobileRechargeImplService.PASSWORD, "UTF-8");
			String originString = createLinkString(signMap) + "&" + md5Pwd;
			String req_sign = MD5Util.sign(URLEncoder.encode(originString, "UTF-8"), "UTF-8");
			if (!sign.equals(req_sign)) {
				throw new BusinessException(RspCode.MOBILE_RECHARGE_FAIL,
						"签名错误,sign=" + sign + ";req_sign=" + req_sign);
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("order_no", clientOrderId);
			paramsMap.put("status", status);
			if (!status.equals("0")) {
				paramsMap.put("errorDesc", errorDesc);
			}
			// 调用订单完成接口
			csOrderService.handlePhoneBillOrderFinishNotity(paramsMap);
			//通知成功
			resultCode = "0000";
			resultMsg = "处理成功！";
		} catch (BusinessException e) {
			resultMsg = e.getMsg();
			logger.error("话费充值通知异常," + e.getMsg());
		} catch (SystemException e) {
			resultMsg = e.getMsg();
			logger.error("话费充值通知异常," + e.getMsg(), e);
		} catch (Exception e) {
			resultMsg = e.getMessage();
			logger.error("话费充值通知异常," + e.getMessage(), e);
		} finally {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMsg", resultMsg);
			out.println(FastJsonUtil.toJson(resultMap));
		}
	}

	
	public void mobileRechargeNotify1(HttpServletRequest request, HttpServletResponse response) {
		// 返回给支付的结果
		String resultCode = "1111";
		String resultMsg = "处理失败!";
		PrintWriter out = null;
		BufferedReader br = null;
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			Map<String, Object> params = FastJsonUtil.jsonToMap(sb.toString());
			// 打印参数
			logger.info("mobile_recharge_notify data：" + params.toString());
			String sign = params.get("sign") + "";
			String clientOrderId = params.get("clientOrderId") + "";
			String mobile = params.get("mobile") + "";
			String callBackTime = params.get("callBackTime") + "";
			String status = params.get("status") + "";
			String errorCode = params.get("errorCode") + "";
			String errorDesc = params.get("errorDesc") + "";
			String intervalTime = params.get("intervalTime") + "";
			String clientSubmitTime = params.get("clientSubmitTime") + "";
			String discount = params.get("discount") + "";
			String costMoney = params.get("costMoney") + "";
			Map<String, String> signMap = new LinkedHashMap<String, String>();
			signMap.put("clientOrderId", clientOrderId);
			signMap.put("mobile", mobile);
			signMap.put("callBackTime", callBackTime);
			signMap.put("status", status);
			if (StringUtils.isNotEmpty(errorCode)) {
				signMap.put("errorCode", errorCode);
			}
			if (StringUtils.isNotEmpty(errorDesc)) {
				signMap.put("errorDesc", errorDesc);
			}
			signMap.put("intervalTime", intervalTime);
			signMap.put("clientSubmitTime", clientSubmitTime);
			signMap.put("discount", discount);
			signMap.put("costMoney", costMoney);
			String md5Pwd = MD5Util.sign(MobileRechargeImplService.PASSWORD, "UTF-8");
			String originString = createLinkString(signMap) + "&" + md5Pwd;
			String req_sign = MD5Util.sign(URLEncoder.encode(originString, "UTF-8"), "UTF-8");
			if (!sign.equals(req_sign)) {
				throw new BusinessException(RspCode.MOBILE_RECHARGE_FAIL,
						"签名错误,sign=" + sign + ";req_sign=" + req_sign);
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("order_no", clientOrderId);
			paramsMap.put("status", status);
			if (!status.equals("0")) {
				paramsMap.put("errorDesc", errorDesc);
			}
			// 调用订单完成接口
			csOrderService.handlePhoneBillOrderFinishNotity(paramsMap);
			resultCode = "0000";
			resultMsg = "处理成功！";
		} catch (BusinessException e) {
			resultMsg = e.getMsg();
			logger.error("话费充值通知异常," + e.getMsg());
		} catch (SystemException e) {
			resultCode = "1111";
			resultMsg = e.getMsg();
			logger.error("话费充值通知异常," + e.getMsg(), e);
		}  catch (Exception e) {
			resultCode = "1111";
			resultMsg = e.getMessage();
			logger.error("话费充值通知异常," + e.getMessage(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", resultCode);
			resultMap.put("resultMsg", resultMsg);
			out.println(FastJsonUtil.toJson(resultMap));
		}
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

}