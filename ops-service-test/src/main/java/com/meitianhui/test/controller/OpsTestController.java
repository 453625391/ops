package com.meitianhui.test.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.meitianhui.util.HttpClientUtil;

@Controller
@RequestMapping(value = "/opsTest")
public class OpsTestController {

	private static Logger logger = Logger.getLogger(OpsTestController.class);

	@RequestMapping(value = "/execute")
	public void queryRewardTypeByPage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("url") String url, @RequestParam("service") String service,
			@RequestParam("params") String params) {
		String result = "";
		try {
			Map<String, String> reqParams = new LinkedHashMap<String, String>();
			reqParams.put("service", service);
			reqParams.put("params", params);
			logger.info("request--> url:" + url + ", requestParam:" + reqParams.toString());
			result = HttpClientUtil.post(url, reqParams);
			logger.info("response--> " + result);
		} catch (Exception e) {
			result = e.getMessage();
		} finally {
			retrunResult(response, result);
		}
	}

	@RequestMapping(value = "/token")
	public String tokenCallBack(HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		Map<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("code", code);
		reqParams.put("grant_type", "authorization_code");
		reqParams.put("app_id", "2efae0c1-e74c-11e5-8f52-00163e0009c6");
		reqParams.put("private_key", "490eee76e74c11e58f5200163e0009c650b573ffe74c11e58f5200163e0009c6");
		reqParams.put("redirect_uri", "http://192.168.16.200:9090/ops-service-test/opsTest/token.do");
		String url = "http://192.168.16.88/oauth/getUserToken";
		try {
			String resultStr = HttpClientUtil.post(url, reqParams);
			System.out.println("user_token" + resultStr);
			request.getSession().setAttribute("user_token", resultStr);
			String resultStr1 = HttpClientUtil.post(url, reqParams);
			System.out.println("user_token" + resultStr1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
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
}
