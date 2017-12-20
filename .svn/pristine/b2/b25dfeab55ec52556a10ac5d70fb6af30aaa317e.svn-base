package com.meitianhui.community.easemob.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/****
 * 环信常量类
 * 
 * @author 丁硕
 * @date 2016年7月21日
 */
public class EasemobConstant {

	private static Log log = LogFactory.getLog(EasemobConstant.class);
	private static Properties configCache = new Properties(); // 系统配置
	
	static {
		InputStream is = null;
		try {
			is = EasemobConstant.class.getResourceAsStream("/properties/easemob.properties");
			configCache.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
	}

	final static String EASEMOB_API_SERVER = configCache.getProperty("easemob_api_server");
	final static String EASEMOB_API_ORG = configCache.getProperty("easemob_api_org");
	final static String EASEMOB_API_APP = configCache.getProperty("easemob_api_app");
	/***
	 * 请求环信接口全地址
	 */
	public final static String EASEMOB_API_SERVICE_URL = EASEMOB_API_SERVER + "/" + EASEMOB_API_ORG + "/" + EASEMOB_API_APP;
	public final static String EASEMOB_APP_CLIENT_ID = configCache.getProperty("easemob_app_client_id");
	public final static String EASEMOB_APP_CLIENT_SECRET = configCache.getProperty("easemob_app_client_secret");

	public static void main(String[] args) {
		System.out.println(EasemobConstant.EASEMOB_API_SERVICE_URL);
	}

}
