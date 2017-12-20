package com.meitianhui.sync.util;

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
	
	public static final int TIMEOUT_LONG = 240000;
	
	public static final int TIMEOUT_SHORT = 60000;

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
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_LONG)
				.setConnectTimeout(TIMEOUT_LONG).setConnectionRequestTimeout(TIMEOUT_LONG)
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
	 * 短连接 Http请求
	 * 
	 * @param reqParams
	 *            请求的参数键值表
	 * @param url
	 *            请求的Url地址
	 * @return 请求响应字串
	 */
	public static String postShort(String url, Map<String, String> reqParams) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_LONG)
				.setConnectTimeout(TIMEOUT_LONG).setConnectionRequestTimeout(TIMEOUT_LONG)
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
	 * 长连接 Http请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> params) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		StringBuffer strBuf = new StringBuffer();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			strBuf.append("&"+entry.getKey() + "=" + entry.getValue());
		}
		// 实例化HTTP方法
		HttpGet get = new HttpGet();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_LONG)
				.setConnectTimeout(TIMEOUT_LONG).setConnectionRequestTimeout(TIMEOUT_LONG)
				.setExpectContinueEnabled(false).build();
		get.setConfig(requestConfig);
		get.setURI(new URI(url + "?" + strBuf.substring(1)));
		HttpResponse response = httpclient.execute(get);
		int status = response.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} else {
			return null;
		}
	}
	
	/**
	 * 短连接 Http请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String getShort(String url, Map<String, String> params) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		StringBuffer strBuf = new StringBuffer();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			strBuf.append("&"+entry.getKey() + "=" + entry.getValue());
		}
		// 实例化HTTP方法
		HttpGet get = new HttpGet();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SHORT)
				.setConnectTimeout(TIMEOUT_SHORT).setConnectionRequestTimeout(TIMEOUT_SHORT)
				.setExpectContinueEnabled(false).build();
		get.setConfig(requestConfig);
		get.setURI(new URI(url + "?" + strBuf.substring(1)));
		HttpResponse response = httpclient.execute(get);
		int status = response.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} else {
			return null;
		}
	}
	
}
