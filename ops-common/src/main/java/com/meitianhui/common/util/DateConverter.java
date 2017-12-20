package com.meitianhui.common.util;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;

/**
 * 对转换器进行修改,使其支持日期格式的数据
 * 
 * @author Tiny
 *
 */
@SuppressWarnings({ "rawtypes" })
public class DateConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			return null;
		} else if (type == Timestamp.class) {
			return convertToDate(type, value, "yyyy-MM-dd HH:mm:ss");
		} else if (type == Date.class) {
			if (StringUtil.formatStr(value).length() == DateUtil.fmt_yyyyMMdd.length()) {
				return convertToDate(type, value, DateUtil.fmt_yyyyMMdd);
			} else if (StringUtil.formatStr(value).length() == DateUtil.fmt_yyyyMMddHHmmss.length()) {
				return convertToDate(type, value, DateUtil.fmt_yyyyMMddHHmmss);
			} else {
				return convertToDate(type, value, DateUtil.fmt_yyyyMMddHHmmssSSS);
			}
		} else if (type == String.class) {
			return value.toString();
		}
		throw new ConversionException("不能转换 " + value.getClass().getName() + " 为 " + type.getName());
	}

	protected Object convertToDate(Class type, Object value, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		if (value instanceof String) {
			try {
				if (StringUtils.isEmpty(value.toString())) {
					return null;
				}
				Date date = sdf.parse((String) value);
				if (type.equals(Timestamp.class)) {
					return new java.sql.Timestamp(date.getTime());
				}
				return date;
			} catch (Exception pe) {
				return null;
			}
		} else if (value instanceof Date) {
			return value;
		}
		throw new ConversionException("不能转换 " + value.getClass().getName() + " 为 " + type.getName());
	}
	
}