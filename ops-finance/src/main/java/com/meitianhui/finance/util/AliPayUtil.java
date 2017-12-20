package com.meitianhui.finance.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;

public class AliPayUtil {
	private static final Logger logger = Logger.getLogger(AliPayUtil.class);
	/**
	 * 支付宝消息验证地址
	 */
	public static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

	/**
	 * 支付宝开发平台接口地址
	 */
	public static final String HTTPS_GATEWAY_OPENAPI_URL = "https://openapi.alipay.com/gateway.do";
	/**
	 * 支付宝即使到账网关
	 */
	public static final String HTTPS_GATEWAY_MAPI_URL = "https://mapi.alipay.com/gateway.do";

	/**
	 * 根据反馈回来的信息，生成签名结果
	 * 
	 * @param params
	 *            通知返回来的参数数组
	 * @param public_key
	 *            支付宝公钥
	 * @param sign
	 *            比对的签名结果
	 * @return 生成的签名结果
	 */
	public static boolean getSignVeryfy(Map<String, Object> params, String public_key, String sign,
			String input_charset) throws Exception {
		// 过滤空值、sign与sign_type参数
		Map<String, Object> sParaNew = paraFilter(params);
		// 获取待签名字符串
		String preSignStr = StringUtil.createLinkString(sParaNew);
		// 获得签名验证结果
		boolean isSign = false;
		isSign = RSAUtil.verify(preSignStr, sign, public_key, input_charset);
		return isSign;
	}

	/**
	 * 获取远程服务器ATN结果,验证返回URL
	 * 
	 * @param notify_id
	 *            通知校验ID
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	public static String verifyResponse(String notify_id, String partner) throws Exception {
		// 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
		String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;
		return checkUrl(veryfy_url);
	}

	/**
	 * 获取远程服务器ATN结果
	 * 
	 * @param urlvalue
	 *            指定URL路径地址
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	public static String checkUrl(String urlvalue) throws Exception {
		String inputLine = "";
		try {
			URL url = new URL(urlvalue);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			inputLine = in.readLine().toString();
		} catch (Exception e) {
			e.printStackTrace();
			inputLine = "";
		}
		return inputLine;
	}

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, Object> paraFilter(Map<String, Object> sArray) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		for (String key : sArray.keySet()) {
			String value = (String) sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}

	/**
	 * 验证消息是否是支付宝发出的合法消息
	 * 
	 * @param params
	 *            通知返回来的参数数组
	 * @return 验证结果
	 */
	public static boolean verify(Map<String, Object> params, String public_key, String input_charset) throws Exception {
		// 判断responsetTxt是否为true，isSign是否为true
		// responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
		// isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
		String partner = PropertiesConfigUtil.getProperty("alipay.partner");
		String responseTxt = "true";
		if (params.get("notify_id") != null) {
			String notify_id = params.get("notify_id").toString();
			responseTxt = AliPayUtil.verifyResponse(notify_id, partner);
		}
		String sign = "";
		if (params.get("sign") != null) {
			sign = params.get("sign").toString();
		}
		boolean isSign = AliPayUtil.getSignVeryfy(params, public_key, sign, input_charset);
		if (isSign && responseTxt.equals("true")) {
			return true;
		} else {
			logger.info("responseTxt:" + responseTxt + ",isSign:" + isSign);
			return false;
		}
	}

	/**
	 * 服务器签名
	 * 
	 * @param params
	 * @return
	 */
	public static String createSign(Map<String, String> params, String private_key, String input_charset)
			throws Exception {
		// 获取待签名字符串
		String preSignStr = createLinkString(params);
		return RSAUtil.sign(preSignStr, private_key, input_charset);
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */

	public static String createLinkString(Map<String, String> params) throws Exception {
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

}
