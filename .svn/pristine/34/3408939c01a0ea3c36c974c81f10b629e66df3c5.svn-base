package com.meitianhui.notification.controller;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meitianhui.base.controller.BaseController;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.notification.constant.RspCode;
import com.meitianhui.notification.service.NotificationService;

/**
 * 信息通知服务
 * 
 * @author mole
 *
 */
@Controller
@RequestMapping("/notification")
public class NotificationController extends BaseController {

	@Autowired
	private NotificationService notificationService;

	public static ExecutorService threadExecutor = Executors.newFixedThreadPool(20);

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			String operateName = request.getParameter("service");
			if ("notification.SMSSend".equals(operateName)) {
				notificationService.sendMsg(paramsMap, result);
			} else if ("notification.sendCheckCode".equals(operateName)) {
				notificationService.sendCheckCode(paramsMap, result);
			} else if ("notification.validateCheckCode".equals(operateName)) {
				notificationService.validateCheckCode(paramsMap, result);
			} else if ("notification.appMsgNotify".equals(operateName)) {
				notificationService.appMsgNotify(paramsMap, result);
			} else if ("notification.pushConsumerNotification".equals(operateName)) {
				notificationService.pushConsumerNotification(paramsMap, result);
			} else if ("notification.pushStoresNotification".equals(operateName)) {
				notificationService.pushStoresNotification(paramsMap, result);
			} else if ("notification.pushNotificationByAlias".equals(operateName)) {
				notificationService.pushNotificationByAlias(paramsMap, result);
			} else if ("notification.pushNotificationByTag".equals(operateName)) {
				notificationService.pushNotificationByTag(paramsMap, result);
			} else if ("notification.pushMessage".equals(operateName)) {
				notificationService.pushMessage(paramsMap, result);
			} else {
				throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR,
						RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
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
