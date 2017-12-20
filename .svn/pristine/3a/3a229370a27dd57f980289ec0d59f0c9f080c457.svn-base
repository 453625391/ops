package com.meitianhui.common.util;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

@SuppressWarnings("unchecked")
/**
 * 对象转换工具类
 * 
 * @author Tiny
 *
 */
public class BeanConvertUtil {

	/**
	 * map格式成javaBean
	 * 
	 * @param t
	 * @param map
	 */
	public static void mapToBean(Object bean, Map<String, Object> map) throws Exception {
		try {
			ConvertUtils.register(new DateConverter(), java.util.Date.class);
			BeanUtils.populate(bean, map);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * map格式成javaBean
	 * 
	 * @param t
	 * @param map
	 */
	public static Map<String, Object> beanToMap(Object bean) throws Exception {
		try {
			return BeanUtils.describe(bean);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 对象属性copy
	 * 
	 * @param to_obj
	 * @param from_obj
	 */
	public static void copyProperties(Object to_obj, Object from_obj) throws Exception {
		try {
			BeanUtils.copyProperties(to_obj, from_obj);
		} catch (Exception e) {
			throw e;
		}
	}
}
