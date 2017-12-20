package com.meitianhui.community.util;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.community.constant.IMRspCode;
import com.meitianhui.community.constant.RspCode;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

public class IMServiceUtil {

	private static Logger logger = Logger.getLogger(IMServiceUtil.class);
	
	// 验证第三方响应结果
	public static JSONObject validResponse(ResponseWrapper response) throws BusinessException {
		if (response == null) {
			throw new BusinessException(IMRspCode.IM_NO_RESPONSE, RspCode.MSG.get(IMRspCode.IM_NO_RESPONSE));
		} else if (response.hasError()) {
			logger.error("IM REQUEST FAIL: " + response.toString());
			throw new BusinessException(IMRspCode.IM_REQUEST_BODY_INVALID, RspCode.MSG.get(IMRspCode.IM_REQUEST_BODY_INVALID));
		} else if (200 == response.getResponseStatus()) { // 请求成功
			JSONObject json = JSONObject.parseObject(response.getResponseBody().toString());
			return json;
		} else {
			logger.error("IM REQUEST FAIL: " + response.toString());
			JSONObject json = JSONObject.parseObject(response.getResponseBody().toString());
			String error_msg = json.containsKey("error_description") ? json.getString("error_description") : RspCode.MSG.get(IMRspCode.IM_REQUEST_FAIL);
			throw new BusinessException(IMRspCode.IM_REQUEST_FAIL, error_msg);
		}
	}
}
