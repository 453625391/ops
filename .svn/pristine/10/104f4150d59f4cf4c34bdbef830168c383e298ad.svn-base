package com.meitianhui.infrastructure.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meitianhui.base.controller.BaseController;
import com.meitianhui.common.constant.PageParam;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.infrastructure.constant.RspCode;
import com.meitianhui.infrastructure.service.AppStoreService;
import com.meitianhui.infrastructure.service.InfrastructureService;

/**
 * 基础服务控制层
 * 
 * @author mole
 *
 */
@Controller
@RequestMapping("/infrastructure")
public class InfrastructureController extends BaseController {

	@Autowired
	private InfrastructureService infrastructureService;

	@Autowired
	private AppStoreService appStoreService;

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception{
		try {
			String operateName = request.getParameter("service");
			if ("infrastructure.transactionRegister".equals(operateName)) {
				infrastructureService.handleTransactionRegister(paramsMap, result);
			} else if ("infrastructure.transactionVerify".equals(operateName)) {
				infrastructureService.handleTransactionVerify(paramsMap, result);
			} else if ("infrastructure.appTokenAuthForEx".equals(operateName)) {
				appStoreService.appTokenAuthForEx(paramsMap, result);
			} else if ("infrastructure.appTokenAuth".equals(operateName)) {
				appStoreService.appTokenAuth(paramsMap, result);
			} else if ("infrastructure.appValidate".equals(operateName)) {
				appStoreService.appValidate(paramsMap, result);
			} else if ("infrastructure.findAppInfo".equals(operateName)) {
				appStoreService.appInfoFind(paramsMap, result);
			} else if ("infrastructure.feedback".equals(operateName)) {
				infrastructureService.userFeedbackCreate(paramsMap, result);
			} else if ("infrastructure.feedbackEdit".equals(operateName)) {
				infrastructureService.userFeedbackEdit(paramsMap, result);
			} else if ("infrastructure.feedbackPageFind".equals(operateName)) {
				feedbackPageFind(request, paramsMap, result);
			} else if ("infrastructure.mobileLoginRegister".equals(operateName)) {
				infrastructureService.handleMobileLoginRegister(paramsMap, result);
			} else if ("infrastructure.mobileLoginVerify".equals(operateName)) {
				infrastructureService.handleMobileLoginVerify(paramsMap, result);
			} else if ("infrastructure.shumeLoginRegister".equals(operateName)) {
				infrastructureService.handleShumeLoginRegister(paramsMap, result);
			} else if ("infrastructure.shumeLoginVerify".equals(operateName)) {
				infrastructureService.handleShumeLoginVerify(paramsMap, result);
			} else if ("infrastructure.securityVerify".equals(operateName)) {
				infrastructureService.safetyCodeVerify(paramsMap, result);//安全围墙-安全验证
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

	/**
	 * 意见反馈
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void feedbackPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception{
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			infrastructureService.userFeedbackFind(paramsMap, result);
			Map<String, Object> pageData = new HashMap<String, Object>();
			pageData.put("list", result.getResultData());
			pageData.put("page", pageParam);
			result.setResultData(pageData);
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

}
