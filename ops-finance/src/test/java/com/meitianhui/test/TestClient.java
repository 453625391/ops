package com.meitianhui.test;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.MD5Util;
import com.meitianhui.finance.util.BestPayHttpUtil;

public class TestClient {
	public static void main(String[] args) throws Exception {
		reverse();
	}

	public static void reverse(){
		try {
			String reverseUrl = "https://webpaywg.bestpay.com.cn/reverse/reverse";
			String merchantId = "02440107050050223";
			String merchantPwd = "140261";
			String oldOrderNo = "201604081415412849461669";
			String oldOrderReqNo = "201604081415412849461669";
			String refundReqNo = "2016040800000483969920001";
			String transAmt = "1";
			String key = "A8BE9C38358274A292703C18C3145DD00BE5CDD620E8E8D3";
			String refundReqDate = DateUtil.getFormatDate("yyyyMMdd");
			StringBuilder sb = new StringBuilder();
			sb.append("MERCHANTID=").append(merchantId);
			sb.append("&MERCHANTPWD=").append(merchantPwd);
			sb.append("&OLDORDERNO=").append(oldOrderNo);
			sb.append("&OLDORDERREQNO=").append(oldOrderReqNo);
			sb.append("&REFUNDREQNO=").append(refundReqNo);
			sb.append("&REFUNDREQDATE=").append(refundReqDate);
			sb.append("&TRANSAMT=").append(transAmt);
			sb.append("&KEY=").append(key);
			
			Map<String, String> queryParam = new HashMap<String, String>();
			queryParam.put("merchantId", merchantId);
			queryParam.put("merchantPwd", merchantPwd);
			queryParam.put("oldOrderNo", oldOrderNo);
			queryParam.put("oldOrderReqNo", oldOrderReqNo);
			queryParam.put("refundReqNo", refundReqNo);
			queryParam.put("refundReqDate", refundReqDate);
			queryParam.put("transAmt", transAmt);
			queryParam.put("channel", "05");
			queryParam.put("mac",  MD5Util.MD5Encode(sb.toString()).toUpperCase());
			System.out.println("翼支付扫码支付-冲正,request->" + queryParam.toString());
			BestPayHttpUtil bastPayHttpUtil = new BestPayHttpUtil();
			String resultStr = bastPayHttpUtil.httpPost(reverseUrl, queryParam);
			System.out.println("翼支付扫码支付-冲正,response->" + resultStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void placeOrder() {
		String merchantId = "043101180050000";
		String orderNo = "1433734609522";
		String orderReqNo = "14337346095221";
		String orderDate = "20150608113649";
		String barcode = "512582454851204002";
		String orderAmt = "1";

		StringBuilder sb = new StringBuilder();// 组装mac加密明文串
		sb.append("MERCHANTID=").append(merchantId);
		sb.append("&ORDERNO=").append(orderNo);
		sb.append("&ORDERREQNO=").append(orderReqNo);
		sb.append("&ORDERDATE=").append(orderDate);
		sb.append("&BARCODE=").append(barcode);
		sb.append("&ORDERAMT=").append(orderAmt);
		sb.append("&KEY=").append("111");// 此处是商户的key

		String mac = MD5Util.MD5Encode(sb.toString());// 进行md5加密(商户自己封装MD5加密工具类，此处只提供参考)

		Map<String, String> param = new HashMap<String, String>();// 组装请求参数

		param.put("merchantId", merchantId);
		param.put("subMerchantId", merchantId);
		param.put("barcode", barcode);
		param.put("orderNo", orderNo);
		param.put("orderReqNo", orderReqNo);
		param.put("orderDate", orderDate);
		param.put("channel", "05");
		param.put("busiType", "0001");
		param.put("TransType", "B");
		param.put("orderAmt", orderAmt);
		param.put("productAmt", "1");
		param.put("attachAmt", "0");
		param.put("goodsName", "条码支付");
		param.put("storeId", "201231");
		param.put("ledgerDetail", "");
		param.put("attach", "");
		param.put("mac", mac);

		// 创建信任证书
		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = createSSLClientDefault();
			httpPost = new HttpPost("https://webpaywg.bestpay.com.cn/barcode/placeOrder");
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
			if (HttpStatus.SC_OK == statusCode) {
				// 如果响应码是200
				System.out.println(EntityUtils.toString(entity));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpPost != null) {
					httpPost.releaseConnection();
				}
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 创建链接
	public static CloseableHttpClient createSSLClientDefault() throws Exception {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new AllTrustStrategy()).build();
		SSLConnectionSocketFactory sslSf = new SSLConnectionSocketFactory(sslContext);
		return HttpClients.custom().setSSLSocketFactory(sslSf).build();
	}

	// 加载证书
	private static class AllTrustStrategy implements TrustStrategy {
		public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			return true;
		}
	}

}