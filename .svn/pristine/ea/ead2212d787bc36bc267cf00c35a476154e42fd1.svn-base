package com.meitianhui.infrastructure.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.infrastructure.constant.Constant;
import com.meitianhui.infrastructure.constant.RspCode;
import com.meitianhui.infrastructure.dao.AppStoreDao;
import com.meitianhui.infrastructure.entity.AppStore;
import com.meitianhui.infrastructure.service.AppStoreService;

@SuppressWarnings("unchecked")
@Service
public class AppStoreServiceImpl implements AppStoreService {

	private static final Logger logger = Logger.getLogger(AppStoreServiceImpl.class);

	@Autowired
	public AppStoreDao appStoreDao;
	@Autowired
	public RedisUtil redisUtil;

	@Override
	public void appTokenAuth(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "app_id", "private_key", "request_info" });
			String app_id = (String) paramsMap.get("app_id");
			String private_key = (String) paramsMap.get("private_key");
			String request_info = (String) paramsMap.get("request_info");

			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("app_id", app_id);
			tempMap.put("private_key", private_key);
			// 验证token是否存在
			List<AppStore> appStoreList = appStoreDao.selectAppStore(tempMap);
			if (appStoreList.size() == 0) {
				throw new BusinessException(RspCode.APP_NOT_EXIST, RspCode.MSG.get(RspCode.APP_NOT_EXIST));
			}
			AppStore appStore = appStoreList.get(0);
			// 验证app状态是否正常
			if (!appStore.getStatus().equals(Constant.APPSTORE_STATUS_ONLINE)) {
				throw new BusinessException(RspCode.APP_STATUS_ERROR, RspCode.MSG.get(RspCode.APP_STATUS_ERROR));
			}
			Map<String, Object> appTokenMap = new HashMap<String, Object>();
			String app_token_temp = (String) redisUtil.getStr(app_id + private_key);
			if (null != app_token_temp && !app_token_temp.equals("") ) {
				tempMap.clear();
				tempMap = (Map<String, Object>) redisUtil.getObj(app_token_temp);	
				appTokenMap.put("app_token", app_token_temp);
				appTokenMap.put("security_code", tempMap.get("security_code"));
			} else {
				tempMap.clear();
				String app_token = IDUtil.generateToken(IDUtil.getUUID());
				// 8位唯一码
				String security_code = IDUtil.getShortUUID();
				tempMap.put("app_id", appStore.getApp_id());
				tempMap.put("app_name", appStore.getApp_name());
				tempMap.put("developer", appStore.getDeveloper());
				tempMap.put("private_key", appStore.getPrivate_key());
				tempMap.put("security_code", security_code);
				tempMap.put("request_info", request_info);
				redisUtil.setObj(app_token, tempMap);
				// 保存app对应的token信息
				redisUtil.setStr(app_id + private_key, app_token);

				appTokenMap.put("app_token", StringUtil.formatStr(app_token));
				appTokenMap.put("security_code", StringUtil.formatStr(security_code));
				logger.info("app_id:" + app_id + ",app_name:" + appStore.getApp_name() + ",app_token:" + app_token
						+ ",security_code:" + security_code);
			}
			result.setResultData(appTokenMap);
			appStore = null;
		} catch (BusinessException e) {
			logger.info(e.getMessage());
			throw e;
		} catch (SystemException e) {
			logger.info(e.getMessage());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			throw e;
		}
	}

	@Override
	public void appTokenAuthForEx(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "app_id", "private_key", "request_info" });
			String app_id = (String) paramsMap.get("app_id");
			String private_key = (String) paramsMap.get("private_key");
			String request_info = (String) paramsMap.get("request_info");

			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("app_id", app_id);
			tempMap.put("private_key", private_key);
			// 验证token是否存在
			List<AppStore> appStoreList = appStoreDao.selectAppStore(tempMap);
			if (appStoreList.size() == 0) {
				throw new BusinessException(RspCode.APP_NOT_EXIST, RspCode.MSG.get(RspCode.APP_NOT_EXIST));
			}
			AppStore appStore = appStoreList.get(0);
			// 验证app状态是否正常
			if (!appStore.getStatus().equals(Constant.APPSTORE_STATUS_ONLINE)) {
				throw new BusinessException(RspCode.APP_STATUS_ERROR, RspCode.MSG.get(RspCode.APP_STATUS_ERROR));
			}
			tempMap.clear();
			String app_token = "";
			Map<String, String> reqParams = new LinkedHashMap<String, String>();
			String php_consumer_service_url = PropertiesConfigUtil.getProperty("php_consumer_service_url");
			reqParams.put("appid", "mth");
			reqParams.put("secret", "mth123");
			logger.info("获取兑换中心apptoken->request:" + reqParams.toString());
			String resultStr = HttpClientUtil.post(php_consumer_service_url + "/token.html", reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			logger.info("获取兑换中心apptoken->response:" + resultMap.toString());
			Map<String, Object> resultDate = (Map<String, Object>) resultMap.get("data");
			if (null == resultDate || resultDate.size() == 0) {
				throw new BusinessException(RspCode.APP_TOKEN_ERROR, (String) resultMap.get("errmsg"));
			}
			app_token = (String) resultDate.get("access_token");

			// 8位唯一码
			String security_code = IDUtil.getShortUUID();
			tempMap.put("app_id", appStore.getApp_id());
			tempMap.put("app_name", appStore.getApp_name());
			tempMap.put("developer", appStore.getDeveloper());
			tempMap.put("private_key", appStore.getPrivate_key());
			tempMap.put("security_code", security_code);
			tempMap.put("request_info", request_info);
			redisUtil.setObj(app_token, tempMap);
			// 将token和校验码返给请求房
			tempMap.clear();
			tempMap.put("app_token", StringUtil.formatStr(app_token));
			tempMap.put("security_code", StringUtil.formatStr(security_code));
			result.setResultData(tempMap);
			logger.info("app_id:" + app_id + ",app_name:" + appStore.getApp_name() + ",app_token:" + app_token
					+ ",security_code:" + security_code);
			appStore = null;
		} catch (BusinessException e) {
			logger.info(e.getMessage());
			throw e;
		} catch (SystemException e) {
			logger.info(e.getMessage());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			throw e;
		}
	}

	@Override
	public void appValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "app_token", "request_info" });
			String app_token = (String) paramsMap.get("app_token");
			Map<String, Object> redisMap = null;
			Object obj = redisUtil.getObj(app_token);
			if (obj == null) {
				logger.error("app_token:" + app_token + "无效");
				throw new BusinessException(RspCode.APP_TOKEN_ERROR, RspCode.MSG.get(RspCode.APP_TOKEN_ERROR));
			}
			redisMap = (Map<String, Object>) obj;
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("app_id", StringUtil.formatStr(redisMap.get("app_id")));
			resultMap.put("security_code", StringUtil.formatStr(redisMap.get("security_code")));
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取app信息
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void appInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "app_id" });
			String app_id = (String) paramsMap.get("app_id");
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("app_id", app_id);
			// 验证token是否存在
			List<AppStore> appStoreList = appStoreDao.selectAppStore(tempMap);
			if (appStoreList.size() == 0) {
				throw new BusinessException(RspCode.APP_NOT_EXIST, RspCode.MSG.get(RspCode.APP_NOT_EXIST));
			}
			AppStore appStore = appStoreList.get(0);
			// 验证app状态是否正常
			if (!appStore.getStatus().equals(Constant.APPSTORE_STATUS_ONLINE)) {
				throw new BusinessException(RspCode.APP_STATUS_ERROR, RspCode.MSG.get(RspCode.APP_STATUS_ERROR));
			}
			// 8位唯一码
			tempMap.put("app_name", StringUtil.formatStr(appStore.getApp_name()));
			tempMap.put("private_key", StringUtil.formatStr(appStore.getPrivate_key()));
			tempMap.put("developer", StringUtil.formatStr(appStore.getDeveloper()));
			tempMap.put("callback_url", StringUtil.formatStr(appStore.getCallback_url()));
			tempMap.put("tag", StringUtil.formatStr(appStore.getTag()));
			tempMap.put("desc1", StringUtil.formatStr(appStore.getDesc1()));
			logger.info(appStore.getApp_name());
			// 将token和校验码返给请求房
			result.setResultData(tempMap);
			appStore = null;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
}
