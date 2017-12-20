package com.meitianhui.community.controller;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.meitianhui.common.constant.CommonRspCode;
import com.meitianhui.common.constant.PageParam;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.StringUtil;

/**
 * 控制层基类
 * 
 * @author Tiny
 *
 */
public abstract class MyBaseController {

	private static final Logger logger = Logger.getLogger(MyBaseController.class);

	private ValueFilter filter = new ValueFilter() {
		@Override
		public Object process(Object obj, String s, Object v) {
			return v == null ? "" : v;
		}
	};

	/**
	 * 处理所有的请求
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "service")
	public void handleRequert(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		long startTime = System.currentTimeMillis();
		try {
			ResultData result = new ResultData();
			// 将param参数解析成map
			Map<String, Object> paramsMap = formatParameter(request);
			operate(request, response, paramsMap, result);
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_SUCC);
			// 如果返回对象为null,就将空字符串赋给返回对象
			resultMap.put("data", result.getResultData() == null ? new Object() : result.getResultData());
		} catch (BusinessException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.error("BusinessException->request:" + FastJsonUtil.toJson(request.getParameterMap()) + " , "
					+ e.getMsg());
		} catch (SystemException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.error("SystemException->request:" + FastJsonUtil.toJson(request.getParameterMap()), e);
		} catch (Exception e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", CommonRspCode.SYSTEM_ERROR);
			resultMap.put("error_msg", CommonRspCode.MSG.get(CommonRspCode.SYSTEM_ERROR));
			logger.error("Exception->request:" + FastJsonUtil.toJson(request.getParameterMap()), e);
		}
		// 将数据返回给请求方
		String resultJson = JSONObject.toJSONString(resultMap, filter);
		// 清空返回数据并置空,让GC更快的进行内存回收
		logger.info("request:" + FastJsonUtil.toJson(request.getParameterMap()) + ",耗时"
				+ (System.currentTimeMillis() - startTime) + "毫秒");
		resultMap.clear();
		resultMap = null;
		retrunResult(response, resultJson);
	}

	/**
	 * 返回结果
	 * 
	 * @param response
	 * @param result
	 */
	public void retrunResult(HttpServletResponse response, String result) {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			if (null != out) {
				out.close();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 将参数解析成map格式参数
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> formatParameter(HttpServletRequest request) throws BusinessException, SystemException {
		String requestDate = request.getParameter("params");
		if (null == requestDate) {
			throw new BusinessException(CommonRspCode.SYSTEM_PARAM_MISS, "业务参数params缺失");
		}
		// 将data的数据进行格式化
		Map<String, Object> paramsMap = FastJsonUtil.jsonToMap(requestDate);
		// 将user_id放入params中
		String user_id = request.getParameter("user_id");
		if (user_id != null && !user_id.equals("")) {
			paramsMap.put("user_id", user_id);
		}
		return paramsMap;
	}

	/**
	 * 获取分页参数
	 * 
	 * @param map
	 * @return
	 * @throws ControllerException
	 */
	public PageParam getPageParam(HttpServletRequest request) throws BusinessException, SystemException {
		PageParam pageParam = new PageParam();
		String page = request.getParameter("page");
		if (page == null) {
			return null;
		}
		Map<String, Object> pageMap = FastJsonUtil.jsonToMap(page);
		String page_no = StringUtil.formatStr(pageMap.get("page_no"));
		if (!"".equals(page_no)) {
			pageParam.setPage_no(Integer.parseInt(page_no));
		} else {
			pageParam.setPage_no(1);
		}
		String page_size = StringUtil.formatStr(pageMap.get("page_size"));
		if (!"".equals(page_size)) {
			pageParam.setPage_size(Integer.parseInt(page_size));
		}
		return pageParam;
	}

	/**
	 * 获取请求网络ip
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	public abstract void operate(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;

	/**
	 * 测试接口
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	public void interfaceTest(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {

		} catch (Exception e) {
			throw e;
		}
	}

}