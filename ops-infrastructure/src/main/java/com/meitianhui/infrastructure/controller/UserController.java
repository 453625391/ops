package com.meitianhui.infrastructure.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meitianhui.base.controller.BaseController;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.infrastructure.constant.RspCode;
import com.meitianhui.infrastructure.service.UserService;

/**
 * 基础服务控制层
 * 
 * @author mole
 *
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception{
		try {
			String operateName = request.getParameter("service");
			if ("infrastructure.mobileLogin".equals(operateName)) {
				userService.mobileLogin(paramsMap, result);
			} else if ("infrastructure.memberLoginLogUpdate".equals(operateName)) {
				userService.memberLoginLogUpdate(paramsMap, result);
			} else if ("infrastructure.consumerLogin".equals(operateName)) {
				userService.consumerLogin(paramsMap, result);
			} else if ("infrastructure.salesmanLogin".equals(operateName)){
				userService.salesmanLogin(paramsMap, result);
			} else if ("infrastructure.storeLogin".equals(operateName)) {
				userService.storeLogin(paramsMap, result);
			} else if ("infrastructure.supplierLogin".equals(operateName)) {
				userService.supplierLogin(paramsMap, result);
			} else if ("infrastructure.consumerLoginForOAuth".equals(operateName)) {
				userService.consumerLoginForOAuth(paramsMap, result);
			} else if ("infrastructure.consumerInfoFindForOAuth".equals(operateName)) {
				userService.consumerInfoForOAuthFind(paramsMap, result);
			} else if ("infrastructure.memberTypeValidateByMobile".equals(operateName)) {
				userService.memberTypeValidateByMobile(paramsMap, result);
			} else if ("infrastructure.memberFindByMobile".equals(operateName)) {
				userService.memberFindByMobile(paramsMap, result);
			} else if ("infrastructure.memberTypeFindByMobile".equals(operateName)) {
				userService.memberTypeFindByMobile(paramsMap, result);
			} else if ("infrastructure.userFindByMobile".equals(operateName)) {
				userService.userFindByMobile(paramsMap, result);
			} else if ("infrastructure.userLogout".equals(operateName)) {
				userService.userLogout(paramsMap, result);
			} else if ("infrastructure.userValidate".equals(operateName)) {
				userService.userValidate(paramsMap, result);
			} else if ("infrastructure.userTokenClear".equals(operateName)) {
				userService.userTokenClear(paramsMap, result);
			} else if ("infrastructure.userValidateNoRequestInfo".equals(operateName)) {
				userService.userValidateNoRequestInfo(paramsMap, result);
			} else if ("infrastructure.userFind".equals(operateName)) {
				userService.userFind(paramsMap, result);
			} else if ("infrastructure.consumerUserRegister".equals(operateName)) {
				userService.handleConsumerUserRegister(paramsMap, result);
			} else if ("infrastructure.syncUserRegister".equals(operateName)) {
				userService.handleSyncUserRegister(paramsMap, result);
			} else if ("infrastructure.userPasswordChange".equals(operateName)) {
				userService.handleUserPasswordChange(paramsMap, result);
			} else if ("infrastructure.userPasswordReset".equals(operateName)) {
				userService.handleUserPasswordReset(paramsMap, result);
			} else if ("infrastructure.userEdit".equals(operateName)) {
				userService.userEdit(paramsMap, result);
			} else if ("infrastructure.payPasswordSetting".equals(operateName)) {
				userService.handlePayPasswordSetting(paramsMap, result);
			} else if ("infrastructure.paySecurityOptions".equals(operateName)) {
				userService.handlePaySecurityOptions(paramsMap, result);
			} else if ("infrastructure.payPasswordValidate".equals(operateName)) {
				userService.payPasswordValidate(paramsMap, result);
			} else if ("infrastructure.loginMobileChange".equals(operateName)) {
				userService.handleLoginMobileChange(paramsMap, result);
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
