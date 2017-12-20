package com.meitianhui.notification.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.notification.constant.Constant;
import com.meitianhui.notification.constant.RspCode;
import com.meitianhui.notification.dao.NotificationDao;
import com.meitianhui.notification.entity.IdMessageQueue;
import com.meitianhui.notification.service.JpushService;
import com.meitianhui.notification.service.MessagesService;
import com.meitianhui.notification.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	public NotificationDao notificationDao;

	@Autowired
	public RedisUtil redisUtil;

	@Resource(name = "aliyunService")
	public MessagesService aliyunService;

	@Autowired
	public JpushService jpushService;

	@Override
	public void sendCheckCode(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "sms_source", "mobile" });
			
			//加入安全验证码验证 对应的验证码是在接口： infrastructure.securityVerify 加入到redis的
			//根据当前的日期生成时间戳(2017-09-08) 来获取钥匙KEY
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date date = simpleDateFormat.parse(DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMdd));
	        String timeStamp = String.valueOf(date.getTime());
	        String lockValue = redisUtil.getStr("[safetyCodeVerify]_meitianhui"+timeStamp);
//	        if(!lockValue.equals(paramsMap.get("lock_value").toString())){
//	        	throw new BusinessException(RspCode.CHECK_CODE_DISABLED, RspCode.MSG.get(RspCode.CHECK_CODE_DISABLED));
//	        }
	        
			String check_code = IDUtil.random(4);
			redisUtil.setStr((String) paramsMap.get("mobile") + "_" + check_code, check_code, 120);
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("sms_source", (String) paramsMap.get("sms_source"));
			tempMap.put("mobiles", (String) paramsMap.get("mobile"));
			tempMap.put("code", check_code);
			aliyunService.sendCheckCode(tempMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 发送信息接口
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
			aliyunService.sendMsg(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void validateCheckCode(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "mobile", "check_code" });
			String mobile = (String) paramsMap.get("mobile");
			String check_code = (String) paramsMap.get("check_code");
			String key = mobile + "_" + check_code;
			String redis_check_code = redisUtil.getStr(key);
			if (StringUtils.isEmpty(redis_check_code)) {
				throw new BusinessException(RspCode.CHECK_CODE_DISABLED, RspCode.MSG.get(RspCode.CHECK_CODE_DISABLED));
			}
			redisUtil.del(key);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void appMsgNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "receiver", "message" });

			IdMessageQueue idMessageQueue = new IdMessageQueue();
			BeanConvertUtil.mapToBean(idMessageQueue, paramsMap);
			idMessageQueue.setQueue_id(IDUtil.getUUID());
			idMessageQueue.setSend_date(new Date());
			notificationDao.insertIdMessageQueue(idMessageQueue);

			String member_id = (String) paramsMap.get("receiver");
			String key = "app_notify_" + member_id;
			redisUtil.setList(key, (String) paramsMap.get("message"));
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
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("title", paramsMap.get("title"));
			tempMap.put("alert", paramsMap.get("alert"));
			tempMap.put("extrasparam", paramsMap.get("extrasparam"));
			jpushService.pushStoresNotification(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void pushConsumerNotification(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "title", "alert", "extrasparam" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("title", paramsMap.get("title"));
			tempMap.put("alert", paramsMap.get("alert"));
			tempMap.put("extrasparam", paramsMap.get("extrasparam"));
			jpushService.pushConsumerNotification(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void pushNotificationByAlias(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "title", "alert", "extrasparam" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("member_id", paramsMap.get("member_id"));
			List<Map<String, Object>> memberLoginLogList = notificationDao.selectMemberLoginLog(tempMap);
			for (Map<String, Object> memberLoginLog : memberLoginLogList) {
				// 会员类型
				String member_type_key = memberLoginLog.get("member_type_key") + "";
				// 注册id
				if (member_type_key.equals(Constant.MEMBER_TYPE_CONSUMER)) {
					// 本地生活
					tempMap.clear();
					tempMap.put("alias", memberLoginLog.get("alias"));
					tempMap.put("device_type", memberLoginLog.get("device_type"));
					tempMap.put("title", paramsMap.get("title"));
					tempMap.put("alert", paramsMap.get("alert"));
					tempMap.put("extrasparam", paramsMap.get("extrasparam"));
					jpushService.pushConsumerNotificationByAlias(tempMap, result);
				} else if (member_type_key.equals(Constant.MEMBER_TYPE_STORES)) {
					// 店东助手
					tempMap.clear();
					tempMap.put("alias", memberLoginLog.get("alias"));
					tempMap.put("device_type", memberLoginLog.get("device_type"));
					tempMap.put("title", paramsMap.get("title"));
					tempMap.put("alert", paramsMap.get("alert"));
					tempMap.put("extrasparam", paramsMap.get("extrasparam"));
					jpushService.pushStoresNotificationByAlias(tempMap, result);
				}
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
	public void pushNotificationByTag(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "tag", "title", "alert", "extrasparam" });
			String tag = paramsMap.get("tag") + "";
			if (tag.equals("stores")) {
				jpushService.pushStoresNotificationByTag(paramsMap, result);
			} else if (tag.equals("consumer")) {
				jpushService.pushConsumerNotificationByTag(paramsMap, result);
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
	public void pushMessage(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "member_id", "msg_title", "msg_content", "extrasparam" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("member_id", paramsMap.get("member_id"));
			List<Map<String, Object>> memberLoginLogList = notificationDao.selectMemberLoginLog(tempMap);
			for (Map<String, Object> memberLoginLog : memberLoginLogList) {
				// 会员类型
				String member_type_key = memberLoginLog.get("member_type_key") + "";
				// 注册id
				if (member_type_key.equals(Constant.MEMBER_TYPE_CONSUMER)) {
					// 本地生活
					tempMap.clear();
					tempMap.put("alias", memberLoginLog.get("alias"));
					tempMap.put("device_type", memberLoginLog.get("device_type"));
					tempMap.put("msg_title", paramsMap.get("msg_title"));
					tempMap.put("msg_content", paramsMap.get("msg_content"));
					tempMap.put("extrasparam", paramsMap.get("extrasparam"));
					jpushService.pushConsumerMessage(tempMap, result);
				} else if (member_type_key.equals(Constant.MEMBER_TYPE_STORES)) {
					// 店东助手
					tempMap.clear();
					tempMap.put("alias", memberLoginLog.get("alias"));
					tempMap.put("device_type", memberLoginLog.get("device_type"));
					tempMap.put("msg_title", paramsMap.get("msg_title"));
					tempMap.put("msg_content", paramsMap.get("msg_content"));
					tempMap.put("extrasparam", paramsMap.get("extrasparam"));
					jpushService.pushStoresMessage(tempMap, result);
				}
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
