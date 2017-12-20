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
import com.meitianhui.infrastructure.service.UserSyncService;

/**
 * 用户密码
 * 
 * @author Tiny
 *
 */
@Controller
@RequestMapping("/userSync")
public class UserSyncController extends BaseController {

	@Autowired
	private UserSyncService userSyncService;

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception{
		try {
			String operateName = request.getParameter("service");
			if ("sync.consumerInfoSync".equals(operateName)) {
				userSyncService.handleConsumerInfoSync(paramsMap, result);
			} else {
				throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
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
