package com.meitianhui.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FileUploadTest {

	public static void main(String[] args) {
		String url = "http://127.0.0.1:8080/ops-storage/storage/upload";
		Map<String,Object> params = new HashMap<String,Object>();
		File file = new File("d://test.xml");
		params.put("subffix", "xml");
		params.put("app_token", "sdssadfjlwi54u");
		params.put("category", "oss_advert");
		params.put("up_load_file", file);
		httpPostFile(url, params);
		// upload(file, url);
	}

	/**
	 * @param fileName
	 *            图片路径
	 */
	public static void uploadFileWithHttpMime(File file, String url) {
		// 实例化http客户端
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 实例化post提交方式
		HttpPost post = new HttpPost(url);
		try {
			// 实例化参数对象
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

			multipartEntityBuilder.addTextBody("subffix", "xml");
			multipartEntityBuilder.addTextBody("app_token", "sdssadfjlwi54u");
			multipartEntityBuilder.addTextBody("category", "oss_advert");
			multipartEntityBuilder.addBinaryBody("up_load_file", file);
			// 设置 POST 请求的实体部分
			post.setEntity(multipartEntityBuilder.build());
			// 发送 HTTP 请求
			// 执行post请求并得到返回对象 [ 到这一步我们的请求就开始了 ]
			HttpResponse httpResponse = closeableHttpClient.execute(post);
			// 解析返回请求结果
			HttpEntity entity = httpResponse.getEntity();
			StatusLine statusLine = httpResponse.getStatusLine();
			System.out.println(statusLine.getStatusCode());
			if (null != entity) {
				System.out.println("contentEncoding:" + entity.getContentEncoding());
				System.out.println("response content:" + EntityUtils.toString(entity));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param fileName
	 *            图片路径
	 */
	public static void httpPostFile(String url, Map<String, Object> params) {
		// 实例化http客户端
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
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
			
			// 设置 POST 请求的实体部分
			post.setEntity(multipartEntityBuilder.build());
			// 发送 HTTP 请求
			// 执行post请求并得到返回对象 [ 到这一步我们的请求就开始了 ]
			HttpResponse httpResponse = closeableHttpClient.execute(post);
			// 解析返回请求结果
			HttpEntity entity = httpResponse.getEntity();
			StatusLine statusLine = httpResponse.getStatusLine();
			System.out.println(statusLine.getStatusCode());
			if (null != entity) {
				System.out.println("contentEncoding:" + entity.getContentEncoding());
				System.out.println("response content:" + EntityUtils.toString(entity));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void upload(File file, String url) {
		try {
			HttpPost httppost = new HttpPost(url);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpEntity reqEntity = MultipartEntityBuilder.create().addBinaryBody("up_load_file", file).build();
			httppost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				String str = EntityUtils.toString(entity);
				JSONObject json = JSONObject.parseObject(str);
				JSONArray array = json.getJSONArray("data");
				if (array != null && array.size() > 0) {
					String savePath = array.getString(0);
					System.out.println(savePath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
