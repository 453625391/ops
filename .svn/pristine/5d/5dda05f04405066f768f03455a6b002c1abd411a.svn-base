package com.meitianhui.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * http工具
 * 
 * @author Tiny
 *
 */
public class HttpClientUtil {
	public static final int HTTPREQTIMEOUT = 60000;

	/**
	 * 长连接Http请求
	 * 
	 * @param reqParams
	 *            请求的参数键值表
	 * @param url
	 *            请求的Url地址
	 * @return 请求响应字串
	 */
	public static String post(String url, Map<String, String> reqParams) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HTTPREQTIMEOUT)
				.setConnectTimeout(HTTPREQTIMEOUT).setConnectionRequestTimeout(HTTPREQTIMEOUT)
				.setExpectContinueEnabled(false).build();
		post.setConfig(requestConfig);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : reqParams.entrySet()) {
			formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);

		post.setEntity(uefEntity);

		HttpResponse response = httpclient.execute(post);

		int status = response.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} else {
			return null;
		}
	}

	/**
	 * http get 请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String get(String url, String params) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 实例化HTTP方法
		HttpGet request = new HttpGet();
		request.setURI(new URI(url + "?" + params));
		HttpResponse response = httpclient.execute(request);
		int status = response.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} else {
			return null;
		}
	}

}
