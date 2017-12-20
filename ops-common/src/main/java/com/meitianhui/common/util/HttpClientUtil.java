package com.meitianhui.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.meitianhui.common.constant.CommonRspCode;
import com.meitianhui.common.exception.SystemException;

/**
 * http工具
 * 
 * @author Tiny
 *
 */
public class HttpClientUtil {

	public static final int TIMEOUT_LONG = 60000;

	public static final int TIMEOUT_SHORT = 30000;

	/**
	 * 长连接Http请求
	 * 
	 * @param reqParams
	 *            请求的参数键值表
	 * @param url
	 *            请求的Url地址
	 * @return 请求响应字串
	 */
	public static String post(String url, Map<String, String> reqParams) throws SystemException, Exception {
		String responseStr = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse response = null;
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
			EntityUtils.consume(entity);
			if (httpClient != null) {
				try {
					httpClient.close();
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
	public static String postShort(String url, Map<String, String> reqParams) throws Exception {
		String responseStr = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
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

			HttpResponse response = httpClient.execute(post);

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
			EntityUtils.consume(entity);
			if (httpClient != null) {
				try {
					httpClient.close();
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
	public static String post(String url, String json) throws Exception {
		String responseStr = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
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

			HttpResponse response = httpClient.execute(post);

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
			EntityUtils.consume(entity);
			if (httpClient != null) {
				try {
					httpClient.close();
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
	public static String get(String url, Map<String, String> params) throws SystemException, Exception {
		String responseStr = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
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
			HttpResponse response = httpClient.execute(get);
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
			EntityUtils.consume(entity);
			if (httpClient != null) {
				try {
					httpClient.close();
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
	public static String getShort(String url, Map<String, String> params) throws Exception {
		String responseStr = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
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
			HttpResponse response = httpClient.execute(get);
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
			EntityUtils.consume(entity);
			if (httpClient != null) {
				try {
					httpClient.close();
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
	public static String postWithJSON(String url, String json) throws Exception {
		String responseStr = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
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
			HttpResponse response = httpClient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
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
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseStr;
	}

	/**
	 * 文件上传
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String upload(String url, Map<String, Object> params) throws Exception {
		// 实例化http客户端
		RequestConfig config = RequestConfig.custom().setConnectTimeout(TIMEOUT_LONG).setSocketTimeout(TIMEOUT_LONG)
				.build();
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		// 实例化post提交方式
		HttpPost post = new HttpPost(url);
		try {
			// 实例化参数对象
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			for (String key : params.keySet()) {
				Object value = params.get(key);
				if (value instanceof File) {
					multipartEntityBuilder.addBinaryBody(key, (File) value);
				} else {
					multipartEntityBuilder.addTextBody(key, value + "");
				}
			}
			post.setEntity(multipartEntityBuilder.build());
			HttpResponse httpResponse = closeableHttpClient.execute(post);
			// 解析返回请求结果
			int status = httpResponse.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				return EntityUtils.toString(entity);
			} else {
				return null;
			}
		} finally {
			try {
				closeableHttpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据url下载文件，保存到filepath中
	 * 
	 * @param url
	 * @param filepath
	 * @return
	 */
	public static void download(String url, String filepath) {
		// 实例化http客户端
		RequestConfig config = RequestConfig.custom().setConnectTimeout(TIMEOUT_LONG).setSocketTimeout(TIMEOUT_LONG)
				.build();
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		InputStream is = null;
		FileOutputStream fileout = null;
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			File file = new File(filepath);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
			}
			fileout = new FileOutputStream(file);
			/**
			 * 根据实际运行效果 设置缓冲区大小
			 */
			final int cache = 10 * 1024;
			byte[] buffer = new byte[cache];
			int ch = 0;
			while ((ch = is.read(buffer)) != -1) {
				fileout.write(buffer, 0, ch);
			}
			fileout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is) {
					is.close();
				}
				if (null != fileout) {
					fileout.close();
				}
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
