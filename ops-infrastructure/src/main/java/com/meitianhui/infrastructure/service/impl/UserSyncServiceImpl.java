package com.meitianhui.infrastructure.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.infrastructure.constant.Constant;
import com.meitianhui.infrastructure.constant.RspCode;
import com.meitianhui.infrastructure.dao.UserDao;
import com.meitianhui.infrastructure.entity.User;
import com.meitianhui.infrastructure.entity.UserLog;
import com.meitianhui.infrastructure.service.UserSyncService;

/**
 * 用户信息同步的服务层
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
@Service
public class UserSyncServiceImpl implements UserSyncService {
	private static final Logger logger = Logger.getLogger(UserSyncServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Override
	public void handleConsumerInfoSync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "data_map" });
			String date_str = (String) paramsMap.get("data_map");
			Map<String, Object> dataMap = FastJsonUtil.jsonToMap(date_str);
			for (String key : dataMap.keySet()) {
				Map<String, Object> tempMap = (Map<String, Object>) dataMap.get(key);
				consumerSyncRegister(tempMap);
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
	 * 消费者信息同步
	 * 
	 * @param paramsMap
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void consumerSyncRegister(Map<String, Object> paramsMap)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "user_id", "mobile", "password", "slat", "registered_date" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String member_id = paramsMap.get("user_id") + "";
			paramsMap.remove("user_id");
			String mobile = paramsMap.get("mobile") + "";
			User user = null;
			tempMap.put("mobile", mobile);
			user = userDao.selectUser(tempMap);

			String user_id = "";
			// 如果对应的用户已存在,则更新密码或手机号信息，如果不存在，则创建用户
			if (null == user) {
				logger.info("用户" + mobile + "不存在,创建用户");
				user_id = IDUtil.getUUID();
				// 注册用户
				user = new User();
				BeanConvertUtil.mapToBean(user, paramsMap);
				user.setStatus(Constant.USER_STATUS_NORMAL);
				user.setUser_id(user_id);
				userDao.insertUser(user);
				userLogAdd(user_id, "消费者同步", (String) paramsMap.get("mobile") + "注册成功");
			} else {
				logger.info("用户" + mobile + "已存在,获取用户id");
				user_id = user.getUser_id();
				tempMap.clear();
				tempMap.put("use_id", user_id);
				tempMap.put("password", (String) paramsMap.get("password"));
				tempMap.put("slat", (String) paramsMap.get("slat"));
				if (!mobile.equals(user.getMobile())) {
					tempMap.put("mobile", mobile);
				}
				userDao.updateUser(tempMap);
			}

			// 注册消费者会员信息
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("service", "member.consumerSync");
			tempMap.clear();
			tempMap.put("user_id", user_id);
			tempMap.put("member_id", member_id);
			tempMap.put("mobile", mobile);
			tempMap.put("registered_date", paramsMap.get("registered_date"));
			tempMap.put("status", Constant.USER_STATUS_NORMAL);
			reqParams.put("params", FastJsonUtil.toJson(tempMap));
			String resultStr = HttpClientUtil.post(member_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			// 初始化会员资产信息
			reqParams.clear();
			tempMap.clear();
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			reqParams.put("service", "finance.memberAssetInit");
			tempMap.put("member_id", member_id);
			tempMap.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
			reqParams.put("params", FastJsonUtil.toJson(tempMap));
			HttpClientUtil.post(finance_service_url, reqParams);
			reqParams.clear();
			resultMap.clear();
			tempMap.clear();
			reqParams = null;
			resultMap = null;
			tempMap = null;
		} catch (BusinessException e) {
			throw e;
		} catch (NoSuchAlgorithmException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	private void userLogAdd(String user_id, String category, String event)
			throws BusinessException, SystemException, Exception {
		try {
			UserLog userLog = new UserLog();
			userLog.setLog_id(IDUtil.getUUID());
			userLog.setUser_id(user_id);
			userLog.setCategory(category);
			userLog.setEvent(event);
			userLog.setTracked_date(new Date());
			userDao.insertUserLog(userLog);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

}
