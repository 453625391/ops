package com.meitianhui.community.easemob.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meitianhui.community.easemob.constant.HttpMethod;
import com.meitianhui.community.easemob.constant.MessageTemplate;
import com.meitianhui.community.easemob.wrapper.BodyWrapper;
import com.meitianhui.community.easemob.wrapper.HeaderWrapper;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

public class HttpClientRestApiInvoker {

	private static Logger logger = Logger.getLogger(HttpClientRestApiInvoker.class);

	// private final static String PROTOCOL = "TLSv1.2"; // 传输协议
	private static final int HTTPREQTIMEOUT = 60000;

	/***
	 * 发送环信服务器请求
	 * @param url
	 * @param header
	 * @param body
	 * @return
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	public static ResponseWrapper sendRequest(HttpMethod method, String url, HeaderWrapper header, BodyWrapper body) {
		ResponseWrapper responseWrapper = new ResponseWrapper();
		responseWrapper.setResponseBody(JsonNodeFactory.instance.objectNode());
		if (StringUtils.isBlank(url)) {
			String msg = MessageTemplate.print(MessageTemplate.BLANK_OBJ_MSG, new String[] { "Parameter url" });
			responseWrapper.addError(msg);
		}
		if (null == header) {
			String msg = MessageTemplate.print(MessageTemplate.BLANK_OBJ_MSG, new String[] { "Parameter header" });
			responseWrapper.addError(msg);
		}
		if (null != body && !body.validate()) {
			responseWrapper.addError(MessageTemplate.INVALID_BODY_MSG);
		}
		if (responseWrapper.hasError()) {
			return responseWrapper;
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse response = null;
		try {
			HttpRequestBase request = null;
			if (HttpMethod.POST == method) {
	        	request = new HttpPost(url);
			} else if (HttpMethod.PUT == method) {
	        	request = new HttpPut(url);
			} else if (HttpMethod.GET == method) {
	        	request = new HttpGet(url);
			} else if (HttpMethod.DELETE == method) {
	        	request = new HttpDelete(url);
			}
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HTTPREQTIMEOUT)
					.setConnectTimeout(HTTPREQTIMEOUT).setConnectionRequestTimeout(HTTPREQTIMEOUT)
					.setExpectContinueEnabled(false).build();
			request.setConfig(requestConfig);
			if (null != body && null != body.getBody()) {
				((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(body.getBody().toString(), "UTF-8"));
			}
			buildHeader(request, header);
			response = httpClient.execute(request);
		} catch (IOException e) {
			responseWrapper.addError(e.getMessage());
			return responseWrapper;
		}
		responseWrapper = readResponse(responseWrapper, response, false);
		try {
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseWrapper;
	}
	
	/***
	 * 构建请求头信息
	 * @param request
	 * @param header
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	private static void buildHeader(HttpRequestBase request, HeaderWrapper header) {
		if (null != header && !header.getHeaders().isEmpty()) {
			for (NameValuePair nameValuePair : header.getHeaders()) {
				request.addHeader(nameValuePair.getName(), nameValuePair.getValue());
			}
		}
	}

	private static ResponseWrapper readResponse(ResponseWrapper responseWrapper, HttpResponse response,
			boolean isFile) {
		ObjectNode responseNode;
		HttpEntity entity = response.getEntity();
		if (null != entity) {
			responseWrapper.setResponseStatus(response.getStatusLine().getStatusCode());

			Object responseContent;
			try {
				if (isFile) {
					responseContent = entity.getContent();
				} else {
					responseContent = EntityUtils.toString(entity, "UTF-8");
					EntityUtils.consume(entity);
				}
			} catch (ParseException e) {
				responseWrapper.addError(e.getMessage());
				return responseWrapper;
			} catch (IOException e) {
				responseWrapper.addError(e.getMessage());
				return responseWrapper;
			}

			if (!isFile) {
				ObjectMapper mapper = new ObjectMapper();
				JsonFactory factory = mapper.getFactory();
				JsonParser jp;
				try {
					jp = factory.createParser(responseContent.toString());
					responseNode = mapper.readTree(jp);
					responseWrapper.setResponseBody(responseNode);
				} catch (IOException e) {
					logger.error(MessageTemplate.STR2JSON_ERROR_MSG, e);
					responseWrapper.addError(MessageTemplate.STR2JSON_ERROR_MSG);
				}
			} else {
				responseWrapper.setResponseBody(responseContent);
			}
		}
		return responseWrapper;
	}

}
