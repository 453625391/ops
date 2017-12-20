package com.meitianhui.notification.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.meitianhui.common.util.MD5Util;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.RegexpValidateUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.common.util.XmlUtil;
import com.meitianhui.notification.constant.RspCode;
import com.meitianhui.notification.controller.NotificationController;
import com.meitianhui.notification.dao.NotificationDao;
import com.meitianhui.notification.entity.IdSmsStatistics;
import com.meitianhui.notification.service.MessagesService;

/**
 * 移动梦网短信接口
 * 
 * @author Tiny
 *
 */
@Service("monternetService")
public class MonternetService implements MessagesService {

	private static final Logger logger = Logger.getLogger(MonternetService.class);

	@Autowired
	public NotificationDao notificationDao;

	@Autowired
	public RedisUtil redisUtil;

	/**
	 * 移动梦网短信接口
	 * 
	 * @param mobiles
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public void sendCheckCode(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			sendMsg(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 移动梦网短信接口
	 * 
	 * @param mobiles
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public void sendMsg(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "sms_source", "mobiles", "msg" });
			final String sms_source = (String) paramsMap.get("sms_source");
			final String pszMobis = (String) paramsMap.get("mobiles");
			final String pszMsg = (String) paramsMap.get("msg");
			final String[] mobilesStr = pszMobis.split(",");
			for (String phone : mobilesStr) {
				if (!RegexpValidateUtil.isPhone(phone)) {
					throw new BusinessException(RspCode.PHONE_ERROR, RspCode.MSG.get(RspCode.PHONE_ERROR));
				}
			}
			if (mobilesStr.length > 100) {
				throw new BusinessException(RspCode.PHONES_TOO_MUCH, RspCode.MSG.get(RspCode.PHONES_TOO_MUCH));
			}
			// 短信修改为线程去执行
			NotificationController.threadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// 发送短信
						String url = PropertiesConfigUtil.getProperty("msg_url");
						String msgUserId = PropertiesConfigUtil.getProperty("msg_user_id");
						String msgPassword = PropertiesConfigUtil.getProperty("msg_password");
						String iMobiCount = mobilesStr.length + "";
						Map<String, String> reqParams = new HashMap<String, String>();
						reqParams.put("userId", msgUserId);
						reqParams.put("password", msgPassword);
						reqParams.put("pszMobis", pszMobis);
						reqParams.put("pszMsg", pszMsg);
						reqParams.put("iMobiCount", iMobiCount);
						reqParams.put("pszSubPort", "*");
						reqParams.put("MsgId", new Date().getTime() + "");
						String xmlStr = HttpClientUtil.postShort(url, reqParams);
						String str = XmlUtil.getNodeValFromXml(xmlStr, "utf-8", "string");
						if (str.equals("-1") || str.equals("-12") || str.equals("-14") || str.equals("-999")) {
							throw new BusinessException(RspCode.SMS_SEND_ERROR, str);
						}
						// 成功则记录日志
						List<IdSmsStatistics> smsStatisticsList = new ArrayList<IdSmsStatistics>();
						for (String mobile : mobilesStr) {
							Date tracked_date = new Date();
							IdSmsStatistics idSmsStatistics = new IdSmsStatistics();
							idSmsStatistics.setStatistics_id(IDUtil.getUUID());
							idSmsStatistics.setSms_source(sms_source);
							idSmsStatistics.setMobile(mobile);
							idSmsStatistics.setSms(pszMsg);
							idSmsStatistics.setTracked_date(tracked_date);
							smsStatisticsList.add(idSmsStatistics);
						}
						notificationDao.insertIdSmsStatistics(smsStatisticsList);
					} catch (BusinessException e) {
						logger.error("短信发送失败;" + e.getMsg());
					} catch (SystemException e) {
						logger.error("短信发送失败;" + e.getMsg());
					} catch (Exception e) {
						logger.error("短信发送失败;", e);
					}
				}
			});
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	

	/**
	 * 移动梦网短信接口
	 * 
	 * @param mobiles
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@SuppressWarnings("unused")
	private void monternet(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "sms_source", "mobiles", "msg" });
			String url = PropertiesConfigUtil.getProperty("msg_url");
			String msgUserId = PropertiesConfigUtil.getProperty("msg_user_id");
			String msgPassword = PropertiesConfigUtil.getProperty("msg_password");
			String pszMobis = (String) paramsMap.get("mobiles");
			String pszMsg = (String) paramsMap.get("msg");
			String[] mobilesStr = pszMobis.split(",");
			for (String phone : mobilesStr) {
				if (!RegexpValidateUtil.isPhone(phone)) {
					throw new BusinessException(RspCode.PHONE_ERROR, RspCode.MSG.get(RspCode.PHONE_ERROR));
				}
			}
			if (mobilesStr.length > 100) {
				throw new BusinessException(RspCode.PHONES_TOO_MUCH, RspCode.MSG.get(RspCode.PHONES_TOO_MUCH));
			}
			// 发送短信
			String iMobiCount = mobilesStr.length + "";
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("userId", msgUserId);
			reqParams.put("password", msgPassword);
			reqParams.put("pszMobis", pszMobis);
			reqParams.put("pszMsg", pszMsg);
			reqParams.put("iMobiCount", iMobiCount);
			reqParams.put("pszSubPort", "*");
			reqParams.put("MsgId", new Date().getTime() + "");
			String xmlStr = HttpClientUtil.post(url, reqParams);
			String str = XmlUtil.getNodeValFromXml(xmlStr, "utf-8", "string");
			if (str.equals("-1") || str.equals("-12") || str.equals("-14") || str.equals("-999")) {
				throw new BusinessException(RspCode.SMS_SEND_ERROR, str);
			}
			// 成功则记录日志
			List<IdSmsStatistics> smsStatisticsList = new ArrayList<IdSmsStatistics>();
			for (String mobile : mobilesStr) {
				Date tracked_date = new Date();
				IdSmsStatistics idSmsStatistics = new IdSmsStatistics();
				idSmsStatistics.setStatistics_id(IDUtil.getUUID());
				idSmsStatistics.setSms_source((String) paramsMap.get("sms_source"));
				idSmsStatistics.setMobile(mobile);
				idSmsStatistics.setSms(pszMsg);
				idSmsStatistics.setTracked_date(tracked_date);
				smsStatisticsList.add(idSmsStatistics);
			}
			// 发送短信的记录需要记录到数据
			notificationDao.insertIdSmsStatistics(smsStatisticsList);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	

	/**
	 * 大汉三通短信接口
	 * 
	 * @param mobiles
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@SuppressWarnings("unused")
	private void daHan3Tong(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "sms_source", "mobiles", "msg" });
			String mobiles = (String) paramsMap.get("mobiles");
			String msg = (String) paramsMap.get("msg");
			String url = PropertiesConfigUtil.getProperty("msg_dahan3tong_url");
			String account = PropertiesConfigUtil.getProperty("msg_dahan3tong_account");
			String password = PropertiesConfigUtil.getProperty("msg_dahan3tong_password");
			for (String phone : mobiles.split(",")) {
				if (!RegexpValidateUtil.isPhone(phone)) {
					throw new BusinessException(RspCode.PHONE_ERROR, RspCode.MSG.get(RspCode.PHONE_ERROR));
				}
			}
			if (mobiles.split(",").length > 100) {
				throw new BusinessException(RspCode.PHONES_TOO_MUCH, RspCode.MSG.get(RspCode.PHONES_TOO_MUCH));
			}
			if (msg.length() > 300) {
				throw new BusinessException(RspCode.MSG_TOO_LONG, RspCode.MSG.get(RspCode.MSG_TOO_LONG));
			}
			// 发送短信
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("account", account);
			reqParams.put("password", MD5Util.MD5Encode(password).toLowerCase());
			reqParams.put("msgid", IDUtil.getUUID());
			reqParams.put("phones", mobiles);
			reqParams.put("content", msg);
			reqParams.put("sign", "【8528】");
			reqParams.put("subcode", "8528");
			reqParams.put("sendtime", "");
			String resultStr = HttpClientUtil.postWithJSON(url, FastJsonUtil.toJson(reqParams));
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			String failPhones = "";
			if (!resultMap.equals("0")) {
				if (StringUtil.formatStr(resultMap.get("failPhones")).equals("")) {
					throw new BusinessException(RspCode.SMS_SEND_ERROR, resultMap.get("desc") + "");
				} else {
					failPhones = StringUtil.formatStr(resultMap.get("failPhones"));
				}
			}
			List<IdSmsStatistics> smsStatisticsList = new ArrayList<IdSmsStatistics>();
			for (String mobile : mobiles.split(",")) {
				if (failPhones.contains(mobile)) {
					continue;
				}
				Date tracked_date = new Date();
				IdSmsStatistics idSmsStatistics = new IdSmsStatistics();
				idSmsStatistics.setStatistics_id(IDUtil.getUUID());
				idSmsStatistics.setSms_source((String) paramsMap.get("sms_source"));
				idSmsStatistics.setMobile(mobile);
				idSmsStatistics.setSms(msg);
				idSmsStatistics.setTracked_date(tracked_date);
				smsStatisticsList.add(idSmsStatistics);
			}
			// 发送短信的记录需要记录到数据
			notificationDao.insertIdSmsStatistics(smsStatisticsList);
			// 返回失败的条目数
			Map<String, Object> resultData = new HashMap<String, Object>();
			if (!failPhones.equals("")) {
				resultData.put("fail", failPhones);
			}
			result.setResultData(resultData);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}


}
