package com.meitianhui.order.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.AESUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.MD5Util;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.order.constant.Constant;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.service.MobileRechargeService;

/**
 * 话费充值接口
 * 
 * @author Tiny
 *
 */
@Service
public class MobileRechargeImplService implements MobileRechargeService {

	private static final Logger logger = Logger.getLogger(MobileRechargeImplService.class);

	@Autowired
	public RedisUtil redisUtil;

	/** 大汉三通url **/
	public static String DAHAN3TONG_URL = PropertiesConfigUtil.getProperty("dahan3tong_url");
	/** 账号 **/
	public static String ACCOUNT = PropertiesConfigUtil.getProperty("dahan3tong_account");
	/** 密码 **/
	public static String PASSWORD = PropertiesConfigUtil.getProperty("dahan3tong_password");

	/**
	 * 手机归属地
	 * 
	 * @param mobiles
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public void mobileAttribution(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "mobile" });
		String url = PropertiesConfigUtil.getProperty("dahan3tong_url") + "FCGetAttribution";
		Map<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("mobile", paramsMap.get("mobile") + "");
		String resultStr = HttpClientUtil.getShort(url, reqParams);
		logger.info("话费充值-归属地查询,request->" + reqParams.toString() + ",response->" + resultStr);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		String resultCode = StringUtil.formatStr(resultMap.get("resultCode"));
		if (StringUtils.isNotEmpty(resultCode)) {
			throw new BusinessException(RspCode.MOBILE_RECHARGE_FAIL, resultMap.get("resultMsg") + "");
		}
		String yysTypeID = resultMap.get("yysTypeID") + "";
		String provinceID = resultMap.get("provinceID") + "";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yysType", Constant.YYS_TYPE_MAP.get(yysTypeID));
		map.put("province", Constant.PROVINCE_MAP.get(provinceID));
		result.setResultData(map);
	}

	/**
	 * 话费充值
	 * 
	 * @param mobiles
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public void mobileRecharge(String mobiles, Integer price, String clientOrderId)
			throws BusinessException, SystemException, Exception {
		String url = DAHAN3TONG_URL + "FCPhoneBillOrderNewServlet";
		String md5Pwd = MD5Util.sign(PASSWORD, "UTF-8");
		// 加密key
		String secretKey = md5Pwd.substring(0, 16);
		// 向量
		String vector = md5Pwd.substring(16, md5Pwd.length());

		String timestamp = System.currentTimeMillis() + "";
		String sign = MD5Util.sign(ACCOUNT, md5Pwd + timestamp + mobiles + price + clientOrderId, "UTF-8");
		Map<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("mobiles", AESUtil.encrypt(mobiles, secretKey, vector));
		reqParams.put("timestamp", timestamp);
		reqParams.put("account", ACCOUNT);
		reqParams.put("sign", sign);
		reqParams.put("price", price + "");
		reqParams.put("clientOrderId", clientOrderId);
		String resultStr = HttpClientUtil.post(url, FastJsonUtil.toJson(reqParams));
		logger.info("话费充值-提交充值请求,request->" + reqParams.toString() + ",response->" + resultStr);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		String resultCode = resultMap.get("resultCode") + "";
		if (!resultCode.equals("00")) {
			throw new BusinessException(RspCode.MOBILE_RECHARGE_FAIL, resultMap.get("resultMsg") + "");
		}
	}

}
