package com.meitianhui.common.component;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.meitianhui.common.constant.CommonRspCode;
import com.meitianhui.common.exception.SystemException;

/**
 * 连接池管理类，支持https协议
 * 
 * @author Tiny
 *
 */
@Component
public class HttpConnectionManager {

	public final int TIMEOUT_LONG = 60000;

	public final int TIMEOUT_SHORT = 30000;

	public PoolingHttpClientConnectionManager cm = null;

	@PostConstruct
	public void init() {
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
		cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		// 设置最大连接数增加
		cm.setMaxTotal(10);
		// 设置每个路由基础的连接增加
		cm.setDefaultMaxPerRoute(5);
	}

	public CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		return httpClient;
	}

	/**
	 * 长连接Http请求
	 * 
	 * @param reqParams
	 *            请求的参数键值表
	 * @param url
	 *            请求的Url地址
	 * @return 请求响应字串
	 */
	public String post(CloseableHttpClient httpClient,String url, Map<String, String> reqParams) throws SystemException, Exception {
		String responseStr = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			HttpPost post = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_LONG)
					.setConnectTimeout(TIMEOUT_LONG).setConnectionRequestTimeout(TIMEOUT_LONG)
					.setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : reqParams.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);

			post.setEntity(uefEntity);

			response = httpClient.execute(post);

			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				entity = response.getEntity();
				responseStr = EntityUtils.toString(entity);
			} else {
				throw new Exception("http request fail,statusCode:" + status);
			}
		} catch (ClientProtocolException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (IOException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(entity);
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseStr;
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
	public String postShort(CloseableHttpClient httpClient,String url, Map<String, String> reqParams) throws Exception {
		String responseStr = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			HttpPost post = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SHORT)
					.setConnectTimeout(TIMEOUT_SHORT).setConnectionRequestTimeout(TIMEOUT_SHORT)
					.setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : reqParams.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);

			post.setEntity(uefEntity);

			response = httpClient.execute(post);

			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				entity = response.getEntity();
				responseStr = EntityUtils.toString(entity);
			} else {
				throw new Exception("http request fail,statusCode:" + status);
			}
		} catch (ClientProtocolException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (IOException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(entity);
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseStr;
	}

	/**
	 * 短连接 Http请求
	 * 
	 * @param json
	 *            请求的字符串
	 * @param url
	 *            请求的Url地址
	 * @return 请求响应字串
	 */
	public String post(CloseableHttpClient httpClient,String url, String json) throws Exception {
		String responseStr = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			HttpPost post = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_LONG)
					.setConnectTimeout(TIMEOUT_LONG).setConnectionRequestTimeout(TIMEOUT_LONG)
					.setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
			post.setHeader("Content-Type", "application/json; charset=UTF-8");
			StringEntity jsonEntity = new StringEntity(json.toString(), "UTF-8");
			post.setEntity(jsonEntity);

			response = httpClient.execute(post);

			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				entity = response.getEntity();
				responseStr = EntityUtils.toString(entity);
			} else {
				throw new Exception("http request fail,statusCode:" + status);
			}
		} catch (ClientProtocolException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (IOException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(entity);
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseStr;
	}

	/**
	 * 长连接 Http请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String get(CloseableHttpClient httpClient,String url, Map<String, String> params) throws SystemException, Exception {
		String responseStr = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			StringBuffer strBuf = new StringBuffer();

			for (Map.Entry<String, String> entry : params.entrySet()) {
				strBuf.append("&" + entry.getKey() + "=" + entry.getValue());
			}
			// 实例化HTTP方法
			HttpGet get = new HttpGet();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_LONG)
					.setConnectTimeout(TIMEOUT_LONG).setConnectionRequestTimeout(TIMEOUT_LONG)
					.setExpectContinueEnabled(false).build();
			get.setConfig(requestConfig);
			get.setURI(new URI(url + "?" + strBuf.substring(1)));
			response = httpClient.execute(get);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				entity = response.getEntity();
				responseStr = EntityUtils.toString(entity);
			} else {
				throw new Exception("http request fail,statusCode:" + status);
			}
		} catch (ClientProtocolException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (IOException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(entity);
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseStr;
	}

	/**
	 * 短连接 Http请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getShort(CloseableHttpClient httpClient,String url, Map<String, String> params) throws Exception {
		String responseStr = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			StringBuffer strBuf = new StringBuffer();

			for (Map.Entry<String, String> entry : params.entrySet()) {
				strBuf.append("&" + entry.getKey() + "=" + entry.getValue());
			}
			// 实例化HTTP方法
			HttpGet get = new HttpGet();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SHORT)
					.setConnectTimeout(TIMEOUT_SHORT).setConnectionRequestTimeout(TIMEOUT_SHORT)
					.setExpectContinueEnabled(false).build();
			get.setConfig(requestConfig);
			get.setURI(new URI(url + "?" + strBuf.substring(1)));
			response = httpClient.execute(get);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				entity = response.getEntity();
				responseStr = EntityUtils.toString(entity);
			} else {
				throw new Exception("http request fail,statusCode:" + status);
			}
		} catch (ClientProtocolException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (IOException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(entity);
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseStr;
	}

	/**
	 * http post 请求 json格式参数
	 * 
	 * @param url
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public String postWithJSON(CloseableHttpClient httpClient,String url, String json) throws Exception {
		String responseStr = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SHORT)
					.setConnectTimeout(TIMEOUT_SHORT).setConnectionRequestTimeout(TIMEOUT_SHORT)
					.setExpectContinueEnabled(false).build();
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			post.setHeader("Content-Type", "application/json;charset=utf-8");
			StringEntity strEnt = new StringEntity(json, "utf-8");
			strEnt.setContentType("text/json");
			strEnt.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(strEnt);
			response = httpClient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				entity = response.getEntity();
				responseStr = EntityUtils.toString(entity);
			} else {
				throw new Exception("http request fail,statusCode:" + status);
			}
		} catch (ClientProtocolException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (IOException e) {
			throw new SystemException(CommonRspCode.HTTP_ERROR, e.getMessage(), e);
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(entity);
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseStr;
	}
}
