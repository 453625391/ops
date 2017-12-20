package com.meitianhui.community.easemob.utils;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meitianhui.community.easemob.body.AuthTokenBody;
import com.meitianhui.community.easemob.constant.EasemobConstant;
import com.meitianhui.community.easemob.constant.HttpMethod;
import com.meitianhui.community.easemob.constant.MessageTemplate;
import com.meitianhui.community.easemob.wrapper.BodyWrapper;
import com.meitianhui.community.easemob.wrapper.HeaderWrapper;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

/***
 * 环信token工厂
 * 
 * @author 丁硕
 * @date 2016年7月21日
 */
public class EasemobTokenFactory {

	private Logger logger = Logger.getLogger(EasemobTokenFactory.class);

	public final String ROOT_URI = "/token";

	// 缓存的token
	private String accessToken;

	// token过期时间
	private Long expiredAt = -1L;

	private static EasemobTokenFactory tokenFactory;

	private EasemobTokenFactory() {

	}

	public static EasemobTokenFactory getInstance() {
		if (null == tokenFactory) {
			tokenFactory = new EasemobTokenFactory();
		}
		return tokenFactory;
	}

	/***
	 * 获取环信认证token信息
	 * 
	 * @param force
	 *            是否强制从服务器上获取token
	 * @return
	 * @author 丁硕
	 * @date 2016年7月21日
	 */
	public String getAuthToken(Boolean force) {
		force = (null == force) ? Boolean.FALSE : force;
		if (isExpired() || force) {
			final String url = EasemobConstant.EASEMOB_API_SERVICE_URL + ROOT_URI;
			BodyWrapper body = new AuthTokenBody(EasemobConstant.EASEMOB_APP_CLIENT_ID, EasemobConstant.EASEMOB_APP_CLIENT_SECRET);
			HeaderWrapper header = HeaderHelper.getDefaultHeader();
			ResponseWrapper response = HttpClientRestApiInvoker.sendRequest(HttpMethod.POST,url, header, body);
			if (response != null && response.getResponseStatus() == 200 && response.getResponseBody() != null) {
				ObjectNode responseBody = (ObjectNode) response.getResponseBody();
				String newToken = responseBody.get("access_token").asText();
				Long newTokenExpire = responseBody.get("expires_in").asLong() * 1000;

				logger.debug("New token: " + newToken);
				logger.debug("New token expires: " + newTokenExpire);

				this.accessToken = newToken;
				this.expiredAt = System.currentTimeMillis() + newTokenExpire;
				logger.info(MessageTemplate.print(MessageTemplate.REFRESH_TOKEN_MSG, new String[] { accessToken, expiredAt.toString() }));
			} else {
				logger.error(MessageTemplate.REFRESH_TOKEN_ERROR_MSG);
			}
		}

		return accessToken;
	}

	/***
	 * 检查token是否过期
	 * 
	 * @return
	 * @author 丁硕
	 * @date 2016年7月21日
	 */
	public Boolean isExpired() {
		return System.currentTimeMillis() > expiredAt;
	}
}
