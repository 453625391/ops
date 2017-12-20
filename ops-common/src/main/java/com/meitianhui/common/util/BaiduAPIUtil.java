package com.meitianhui.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.meitianhui.common.constant.CommonRspCode;
import com.meitianhui.common.exception.SystemException;

/**
 * 百度API接口
 * 
 * @author zhouMing
 *
 */
public class BaiduAPIUtil {

	public static final int TIMEOUT_SHORT = 10000;
	
	// 百度API key
	private static final String KEY = "HWbsmkkkYrorNB5She5Y4yQpcU6bY920";

	/**
	 * 获取定位信息
	 * 
	 * @param lat
	 *            维度
	 * @param lng
	 *            经度
	 * @return
	 */
	public static Map<String, Object> getLocationInfo(String lat, String lng) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String url = "http://api.map.baidu.com/geocoder/v2/";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("ak", KEY);
			requestData.put("location", lat + "," + lng);
			requestData.put("output", "json");
			String resultStr = postShort(url, requestData);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			resultMap = FastJsonUtil.jsonToMap(resultMap.get("result") + "");
			resultMap = FastJsonUtil.jsonToMap(resultMap.get("addressComponent") + "");
		} catch (Exception e) {
			throw e;
		}
		return resultMap;
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
		CloseableHttpClient httpClient = null;
		HttpEntity entity = null;
		try {
			// 定制cookie策略
			BasicCookieStore cookieStore = new BasicCookieStore();
			CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
				public CookieSpec create(HttpContext context) {
					return new BrowserCompatSpec() {
						@Override
						public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
							// Oh, I am easy
						}
					};
				}
			};
			Registry<CookieSpecProvider> csf = RegistryBuilder.<CookieSpecProvider> create()
					.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
					.register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory())
					.register("easy", easySpecProvider).build();
			
			// 设置请求配置
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SHORT).setConnectTimeout(TIMEOUT_SHORT)
					.setConnectionRequestTimeout(TIMEOUT_SHORT).setCookieSpec("easy").setExpectContinueEnabled(false).build();
			// 创建httpClient
			httpClient = HttpClients.custom().setDefaultCookieSpecRegistry(csf).setDefaultRequestConfig(requestConfig)
					.setDefaultCookieStore(cookieStore).build();

			HttpPost post = new HttpPost(url);
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

}
