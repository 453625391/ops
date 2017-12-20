package com.meitianhui.notification.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.RegexpValidateUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.notification.constant.RspCode;
import com.meitianhui.notification.controller.NotificationController;
import com.meitianhui.notification.dao.NotificationDao;
import com.meitianhui.notification.entity.IdSmsStatistics;
import com.meitianhui.notification.service.MessagesService;

@Service("aliyunService")
public class AliyunServiceImpl implements MessagesService {

	private static final Logger logger = Logger.getLogger(AliyunServiceImpl.class);

	@Autowired
	public NotificationDao notificationDao;

	@Autowired
	public RedisUtil redisUtil;

	public final static String ACCESSKEY = PropertiesConfigUtil.getProperty("aliyuncs_accessKey");

	public final static String ACCESSSECRET = PropertiesConfigUtil.getProperty("aliyuncs_accessSecret");

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
			ValidateUtil.validateParams(paramsMap, new String[] { "sms_source", "mobiles", "code" });
			final String sms_source = (String) paramsMap.get("sms_source");
			final String mobiles = (String) paramsMap.get("mobiles");
			final String code = (String) paramsMap.get("code");
			NotificationController.threadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						String[] mobilesStr = mobiles.split(",");
						for (String phone : mobilesStr) {
							if (!RegexpValidateUtil.isPhone(phone)) {
								throw new BusinessException(RspCode.PHONE_ERROR, RspCode.MSG.get(RspCode.PHONE_ERROR));
							}
						}

						IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEY, ACCESSSECRET);
						DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
						IAcsClient client = new DefaultAcsClient(profile);
						SingleSendSmsRequest request = new SingleSendSmsRequest();
						// 控制台创建的签名名称
						request.setSignName("每天惠");
						// 控制台创建的模板CODE
						request.setTemplateCode("SMS_47520040");
						// 短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。"
						Map<String, String> paramString = new HashMap<String, String>();
						paramString.put("code", code);
						request.setParamString(FastJsonUtil.toJson(paramString));
						// 接收号码
						request.setRecNum(mobiles);
						SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
						// 成功则记录日志
						List<IdSmsStatistics> smsStatisticsList = new ArrayList<IdSmsStatistics>();
						for (String mobile : mobilesStr) {
							Date tracked_date = new Date();
							IdSmsStatistics idSmsStatistics = new IdSmsStatistics();
							idSmsStatistics.setStatistics_id(IDUtil.getUUID());
							idSmsStatistics.setSms_source(sms_source);
							idSmsStatistics.setMobile(mobile);
							idSmsStatistics.setSms("短信验证码:" + code + "【RequestId:" + httpResponse.getRequestId() + "】");
							idSmsStatistics.setTracked_date(tracked_date);
							smsStatisticsList.add(idSmsStatistics);
						}
						// 发送短信的记录需要记录到数据
						notificationDao.insertIdSmsStatistics(smsStatisticsList);
					} catch (Exception e) {
						logger.error("发送验证短信异常;mobile:" + mobiles, e);
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
	 * 发送信息
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
			final String mobiles = (String) paramsMap.get("mobiles");
			final String msg = (String) paramsMap.get("msg");
			final String sms_source = (String) paramsMap.get("sms_source");
			NotificationController.threadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						String[] mobilesStr = mobiles.split(",");
						for (String phone : mobilesStr) {
							if (!RegexpValidateUtil.isPhone(phone)) {
								throw new BusinessException(RspCode.PHONE_ERROR, RspCode.MSG.get(RspCode.PHONE_ERROR));
							}
						}
						if (mobilesStr.length > 100) {
							throw new BusinessException(RspCode.PHONES_TOO_MUCH,
									RspCode.MSG.get(RspCode.PHONES_TOO_MUCH));
						}

						IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEY, ACCESSSECRET);
						DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
						IAcsClient client = new DefaultAcsClient(profile);
						SingleSendSmsRequest request = new SingleSendSmsRequest();
						// 控制台创建的签名名称
						request.setSignName("每天惠");
						// 控制台创建的模板CODE
						request.setTemplateCode("SMS_47145164");
						// 短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。"
						Map<String, String> paramString = new HashMap<String, String>();
						paramString.put("msg", msg);
						request.setParamString(FastJsonUtil.toJson(paramString));
						// 接收号码
						request.setRecNum(mobiles);
						SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
						// 成功则记录日志
						List<IdSmsStatistics> smsStatisticsList = new ArrayList<IdSmsStatistics>();
						for (String mobile : mobilesStr) {
							Date tracked_date = new Date();
							IdSmsStatistics idSmsStatistics = new IdSmsStatistics();
							idSmsStatistics.setStatistics_id(IDUtil.getUUID());
							idSmsStatistics.setSms_source(sms_source);
							idSmsStatistics.setMobile(mobile);
							idSmsStatistics.setSms(msg + "【RequestId:" + httpResponse.getRequestId() + "】");
							idSmsStatistics.setTracked_date(tracked_date);
							smsStatisticsList.add(idSmsStatistics);
						}
						// 发送短信的记录需要记录到数据
						notificationDao.insertIdSmsStatistics(smsStatisticsList);
					} catch (Exception e) {
						logger.error("发送短信信息异常;mobile:" + mobiles, e);
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

}
