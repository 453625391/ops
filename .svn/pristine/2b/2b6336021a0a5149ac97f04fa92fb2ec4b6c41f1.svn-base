package com.meitianhui.finance.util;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.meitianhui.common.util.MD5Util;

public class BestPayHttpUtil {
	private static Logger log = Logger.getLogger(BestPayHttpUtil.class);

	public String httpPost(String url, Map<String, String> param) {
		String result = null;
		// 创建信任证书
		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = createSSLClientDefault();
			httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			for (String key : param.keySet()) {
				paramList.add(new BasicNameValuePair(key, param.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
			httpPost.setConfig(RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build());
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			// 如果响应码是200
			if (HttpStatus.SC_OK == statusCode) {
				result = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			log.error("翼支付请求异常", e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpPost != null) {
					httpPost.releaseConnection();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				log.error("翼支付关闭http异常", e);
			}
		}
		return result;
	}

	// 创建链接
	public CloseableHttpClient createSSLClientDefault() throws Exception {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new AllTrustStrategy()).build();
		SSLConnectionSocketFactory sslSf = new SSLConnectionSocketFactory(sslContext);
		return HttpClients.custom().setSSLSocketFactory(sslSf).build();
	}

	// 加载证书
	private class AllTrustStrategy implements TrustStrategy {
		public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			return true;
		}
	}
	
	/**
	 * 组装mac加密明文串
	 * 
	 * @param merchantId
	 * @param orderNo
	 * @param orderReqNo
	 * @param orderDate
	 * @param barcode
	 * @param orderAmt
	 * @param key
	 * @return
	 */
	public static String createOrderMacStr(String merchantId, String orderNo, String orderReqNo, String orderDate,
			String barcode, String orderAmt, String key) {
		StringBuilder sb = new StringBuilder();
		sb.append("MERCHANTID=").append(merchantId);
		sb.append("&ORDERNO=").append(orderNo);
		sb.append("&ORDERREQNO=").append(orderReqNo);
		sb.append("&ORDERDATE=").append(orderDate);
		sb.append("&BARCODE=").append(barcode);
		sb.append("&ORDERAMT=").append(orderAmt);
		sb.append("&KEY=").append(key);
		return MD5Util.MD5Encode(sb.toString()).toUpperCase();
	}

	
	/**
	 * 组装mac加密明文串
	 * 
	 * @param merchantId
	 * @param orderNo
	 * @param orderReqNo
	 * @param orderDate
	 * @param barcode
	 * @param orderAmt
	 * @param key
	 * @return
	 */
	public static String createOrderQueryMacStr(String merchantId, String orderNo, String orderReqNo, String orderDate,
			String key) {
		StringBuilder sb = new StringBuilder();
		sb.append("MERCHANTID=").append(merchantId);
		sb.append("&ORDERNO=").append(orderNo);
		sb.append("&ORDERREQNO=").append(orderReqNo);
		sb.append("&ORDERDATE=").append(orderDate);
		sb.append("&KEY=").append(key);
		return MD5Util.MD5Encode(sb.toString()).toUpperCase();
	}
	
	
	public static String createReverseMacStr(String merchantId, String merchantPwd, String oldOrderNo,
			String oldOrderReqNo, String refundReqNo, String refundReqDate, String transAmt, String key) {
		StringBuilder sb = new StringBuilder();
		sb.append("MERCHANTID=").append(merchantId);
		sb.append("&MERCHANTPWD=").append(merchantPwd);
		sb.append("&OLDORDERNO=").append(oldOrderNo);
		sb.append("&OLDORDERREQNO=").append(oldOrderReqNo);
		sb.append("&REFUNDREQNO=").append(refundReqNo);
		sb.append("&REFUNDREQDATE=").append(refundReqDate);
		sb.append("&TRANSAMT=").append(transAmt);
		sb.append("&KEY=").append(key);
		return MD5Util.MD5Encode(sb.toString()).toUpperCase();
	}

}
