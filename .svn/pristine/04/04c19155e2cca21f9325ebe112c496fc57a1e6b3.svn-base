package com.meitianhui.community.easemob.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/***
 * 配置文件缓存工具类
 * 
 * @author 丁硕
 * @date 2015年12月18日
 */
public class PropertiesHelper {

	private static PropertiesHelper INSTANCE = null;
	private static final String CONFIG_PATH = "/properties/easemob.properties";
	private Properties configCache = new Properties();  //系统配置
	
	private PropertiesHelper() {
	}
	
	/***
	 * 获取配置实例
	 * @return
	 */
	public static PropertiesHelper getInstance() {
		if (INSTANCE == null){
			synchronized(PropertiesHelper.class){
				INSTANCE = new PropertiesHelper();
				INSTANCE.reload();
			}
		}
		return INSTANCE;
	} 
	
	/***
	 * 重新加载资源
	 */
	public void reload(){
		InputStream is = null;
		try {
			is = PropertiesHelper.class.getResourceAsStream(CONFIG_PATH);
			configCache.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/***
	 * 获取配置文件值
	 * @param key
	 */
	public String getValue(String key){
		return configCache.getProperty(key);
	}
	
	public static void main(String[] args) {
		System.out.println(PropertiesHelper.getInstance().getValue("base_server"));
	}
	
	
}
