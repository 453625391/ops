package com.meitianhui.sync.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertiesConfigUtil extends PropertyPlaceholderConfigurer {
	private static Map<String, String> ctxPropertiesMap;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		ctxPropertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
		}
	}
	/**
	 * 获取属性文件
	 * 
	 * @param name
	 * @return
	 */
	public static String getProperty(String name) {
		return ctxPropertiesMap.get(name);
	}

	/**
	 * 获取属性文件
	 * 如果属性文件中不存在此参数值,则返回默认值
	 * @param name
	 * @param default_value 
	 * @return
	 */
	public static String getProperty(String name, String default_value) {
		String value = ctxPropertiesMap.get(name);
		return value == null||value.equals("") ? default_value : value;
	}
}