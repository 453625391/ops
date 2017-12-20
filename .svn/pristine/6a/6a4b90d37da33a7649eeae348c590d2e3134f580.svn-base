package com.meitianhui.member.controller;

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
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.service.MemberTaskService;

/**
 * 会员任务管理
 * 
 * @author Tiny
 *
 */
@Controller
@RequestMapping("/memberTask")
public class MemberTaskController extends BaseController {

	@Autowired
	private MemberTaskService memberTaskService;

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception{
		try {
			String operateName = request.getParameter("service");
			if ("member.freezeServiceFree".equals(operateName)) {
				memberTaskService.handleFreezeServiceFree(paramsMap, result);
			} else if ("member.proServiceFree".equals(operateName)) {
				memberTaskService.handleProServiceFree(paramsMap, result);
			} else if ("member.assistantServiceFree".equals(operateName)) {
				memberTaskService.handleAssistantServiceFree(paramsMap, result);
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
