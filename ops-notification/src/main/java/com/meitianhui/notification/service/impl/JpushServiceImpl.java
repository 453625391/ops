package com.meitianhui.notification.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.notification.constant.RspCode;
import com.meitianhui.notification.service.JpushService;
import com.meitianhui.notification.util.JPushClientUtil;

import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.audience.Audience;

@Service
@SuppressWarnings("unchecked")
public class JpushServiceImpl implements JpushService {

	/**
	 * 本地生活推送
	 */
	public JPushClientUtil JPushClientConsumerUtil = new JPushClientUtil(
			PropertiesConfigUtil.getProperty("jpush_consumer_appKey"),
			PropertiesConfigUtil.getProperty("jpush_consumer_masterSecret"));
	/**
	 * 店东助手推送
	 */
	public JPushClientUtil JPushClientStoresUtil = new JPushClientUtil(
			PropertiesConfigUtil.getProperty("jpush_stores_appKey"),
			PropertiesConfigUtil.getProperty("jpush_stores_masterSecret"));
	
	@Override
	public void pushConsumerNotification(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "title", "alert", "extrasparam" });
			// 消息标题
			String title = paramsMap.get("title") + "";
			// 消息内容
			String alert = paramsMap.get("alert") + "";
			// 扩展字段
			Map<String, String> extrasparam = JSONObject.parseObject(paramsMap.get("extrasparam") + "", Map.class);
			// 本地生活通知
			JPushClientConsumerUtil.pushNotificationToAll(title, alert, extrasparam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void pushStoresNotification(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "title", "alert", "extrasparam" });
			// 消息标题
			String title = paramsMap.get("title") + "";
			// 消息内容
			String alert = paramsMap.get("alert") + "";
			// 扩展字段
			Map<String, String> extrasparam = JSONObject.parseObject(paramsMap.get("extrasparam") + "", Map.class);
			// 店东通知
			JPushClientStoresUtil.pushNotificationToAll(title, alert, extrasparam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void pushConsumerNotificationByAlias(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "device_type", "alias", "title", "alert", "extrasparam" });
			// 登陆设备类型
			String device_type = paramsMap.get("device_type") + "";
			// 接受对象别名
			String alias = paramsMap.get("alias") + "";
			// 消息标题
			String title = paramsMap.get("title") + "";
			// 消息内容
			String alert = paramsMap.get("alert") + "";
			// 扩展字段
			Map<String, String> extrasparam = JSONObject.parseObject(paramsMap.get("extrasparam") + "", Map.class);

			// 本地生活
			if (device_type.equals("IOS")) {
				// 苹果设备
				JPushClientConsumerUtil.pushNotificationToIosAudience(Audience.alias(alias), title, alert, extrasparam);
			} else if (device_type.equals("ANDROID")) {
				// 安卓设备
				JPushClientConsumerUtil.pushNotificationToAndroidAudience(Audience.alias(alias), title, alert,
						extrasparam);
			} else {
				throw new BusinessException(RspCode.DEVICE_TYPE_ERROR, "不支持此机型推送");
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void pushStoresNotificationByAlias(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "device_type", "alias", "title", "alert", "extrasparam" });
			// 登陆设备类型
			String device_type = paramsMap.get("device_type") + "";
			// 接受对象别名
			String alias = paramsMap.get("alias") + "";
			// 消息标题
			String title = paramsMap.get("title") + "";
			// 消息内容
			String alert = paramsMap.get("alert") + "";
			// 扩展字段
			Map<String, String> extrasparam = JSONObject.parseObject(paramsMap.get("extrasparam") + "", Map.class);

			// 本地生活
			if (device_type.equals("IOS")) {
				// 苹果设备
				JPushClientStoresUtil.pushNotificationToIosAudience(Audience.alias(alias), title, alert, extrasparam);
			} else if (device_type.equals("ANDROID")) {
				// 安卓设备
				JPushClientStoresUtil.pushNotificationToAndroidAudience(Audience.alias(alias), title, alert,
						extrasparam);
			} else {
				throw new BusinessException(RspCode.DEVICE_TYPE_ERROR, "不支持此机型推送");
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void pushConsumerNotificationByTag(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "title", "alert", "extrasparam" });
			// 消息标题
			String title = paramsMap.get("title") + "";
			// 消息内容
			String alert = paramsMap.get("alert") + "";
			// 扩展字段
			Map<String, String> extrasparam = JSONObject.parseObject(paramsMap.get("extrasparam") + "", Map.class);
			JPushClientConsumerUtil.pushNotificationToIosAudience(Audience.tag("consumer"), title, alert, extrasparam);
			JPushClientConsumerUtil.pushNotificationToAndroidAudience(Audience.tag("consumer"), title, alert,
					extrasparam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void pushStoresNotificationByTag(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "alert", "extrasparam" });
			// 消息标题
			String title = paramsMap.get("title") + "";
			// 消息内容
			String alert = paramsMap.get("alert") + "";
			// 扩展字段
			Map<String, String> extrasparam = JSONObject.parseObject(paramsMap.get("extrasparam") + "", Map.class);
			JPushClientStoresUtil.pushNotificationToIosAudience(Audience.tag("stores"), title, alert, extrasparam);
			JPushClientStoresUtil.pushNotificationToAndroidAudience(Audience.tag("stores"), title, alert, extrasparam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void pushConsumerMessage(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "device_type", "alias", "msg_title", "msg_content", "extrasparam" });
			// 登陆设备类型
			String device_type = paramsMap.get("device_type") + "";
			// 接受对象别名
			String alias = paramsMap.get("alias") + "";
			// 消息标题
			String msg_title = paramsMap.get("msg_title") + "";
			// 消息内容
			String msg_content = paramsMap.get("msg_content") + "";
			// 扩展字段
			Map<String, String> extrasparam = JSONObject.parseObject(paramsMap.get("extrasparam") + "", Map.class);

			// 本地生活
			if (device_type.equals("IOS")) {
				// 苹果设备
				JPushClientConsumerUtil.pushMessageToAudience(Platform.ios(), Audience.alias(alias), msg_title,
						msg_content, extrasparam);
			} else if (device_type.equals("ANDROID")) {
				// 安卓设备
				JPushClientConsumerUtil.pushMessageToAudience(Platform.android(), Audience.alias(alias), msg_title,
						msg_content, extrasparam);
			} else {
				throw new BusinessException(RspCode.DEVICE_TYPE_ERROR, "不支持此机型推送");
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void pushStoresMessage(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "device_type", "alias", "msg_title", "msg_content", "extrasparam" });
			// 接受对象别名
			String alias = paramsMap.get("alias") + "";
			// 登陆设备类型
			String device_type = paramsMap.get("device_type") + "";
			// 消息标题
			String msg_title = paramsMap.get("msg_title") + "";
			// 消息内容
			String msg_content = paramsMap.get("msg_content") + "";
			// 扩展字段
			Map<String, String> extrasparam = JSONObject.parseObject(paramsMap.get("extrasparam") + "", Map.class);

			// 本地生活
			if (device_type.equals("IOS")) {
				// 苹果设备
				JPushClientStoresUtil.pushMessageToAudience(Platform.ios(), Audience.alias(alias), msg_title,
						msg_content, extrasparam);
			} else if (device_type.equals("ANDROID")) {
				// 安卓设备
				JPushClientStoresUtil.pushMessageToAudience(Platform.android(), Audience.alias(alias), msg_title,
						msg_content, extrasparam);
			} else {
				throw new BusinessException(RspCode.DEVICE_TYPE_ERROR, "不支持此机型推送");
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

}
