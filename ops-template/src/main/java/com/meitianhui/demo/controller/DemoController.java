package com.meitianhui.demo.controller;

import java.util.HashMap;
import java.util.List;
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
import com.meitianhui.demo.entity.Demo;
import com.meitianhui.demo.service.DemoService;

/**
 * demo控制层
 * 
 * @author Tiny
 *
 */
@Controller
@RequestMapping("/demo")
public class DemoController extends BaseController {

	@Autowired
	private DemoService demoService;

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException,SystemException {
		try {
			String operateName = request.getParameter("service");
			if ("addDemo".equals(operateName)) {
				addDemo(paramsMap, result);
			} else if ("queryDemo".equals(operateName)) {
				queryDemo(request,paramsMap, result);
			} else if ("getArea".equals(operateName)) {
				getArea(request,paramsMap, result);
			}
			
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 新增demo
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	public void addDemo(Map<String, Object> paramsMap, ResultData result) throws BusinessException,SystemException {
		demoService.addDemo(paramsMap);
	}

	/**
	 * 查询demo
	 * @param request
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	public void queryDemo(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException,SystemException {
		// 引入分页查询
		PageParam pageParam = getPageParam(request);
		if (null != pageParam) {
			paramsMap.put("pageParam", pageParam);
		}
		List<Demo> resultMap = demoService.queryDemo(paramsMap);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", resultMap);
		data.put("page", pageParam);
		result.setResultData(data);
	}
	
	
	public void getArea(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException,SystemException {
		List<Map<String,Object>> resultMap = demoService.mdAreaFind();
		result.setResultData(resultMap);
	}
	
}
