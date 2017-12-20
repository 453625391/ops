package com.meitianhui.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author Tiny
 *
 */
public class DateUtil {

	/** 格式yyyy-MM **/
	public static String fmt_yyyyMM = "yyyy-MM";
	/** 格式yyyy-MM-dd **/
	public static String fmt_yyyyMMdd = "yyyy-MM-dd";
	/** 格式HH:mm:ss **/
	public static String fmt_HHmmss = "HH:mm:ss";
	/** 格式yyyy-MM-dd HH:mm:ss **/
	public static String fmt_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
	/** 格式yyyy-MM-dd HH:mm **/
	public static String fmt_yyyyMMddHHmm = "yyyy/MM/dd HH:mm";
	/** 格式yyyy-MM-dd HH:mm:ss **/
	public static String fmt_yyyyMMddHHmmssSSS = "yyyy-MM-dd HH:mm:ss SSS";

	/**
	 * 日期格式转换
	 * 
	 * @param date
	 * @param convertFmt
	 * @param convertFmt
	 * @return
	 * @throws Exception
	 */
	public static String formateConvert(String dateStr, String fmt, String convertFmt) {
		try {
			if (dateStr == null) {
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat(fmt);
			Date dateTemp = format.parse(dateStr);
			SimpleDateFormat convertFormat = new SimpleDateFormat(convertFmt);
			return convertFormat.format(dateTemp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @param fmt
	 * @return
	 * @throws Exception
	 */
	public static String date2Str(Date date, String fmt) {
		try {
			if (date == null) {
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat(fmt);
			return format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	   public static void main(String[] args) throws ParseException {  
	        Calendar now = Calendar.getInstance();  
	        System.out.println("年: " + now.get(Calendar.YEAR));  
	        System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");  
	        System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));  
//	        System.out.println("时: " + now.get(Calendar.HOUR_OF_DAY));  
//	        System.out.println("分: " + now.get(Calendar.MINUTE));  
//	        System.out.println("秒: " + now.get(Calendar.SECOND));  
//	        System.out.println("当前时间毫秒数：" + now.getTimeInMillis());  
//	        System.out.println(now.getTime());  
//	  
//	        Date d = new Date();  
//	        System.out.println(d);  
//	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//	        String dateNowStr = sdf.format(d);  
//	        System.out.println("格式化后的日期：" + dateNowStr);  
//	          
//	        String str = "2012-1-13 17:26:33";  //要跟上面sdf定义的格式一样  
//	        Date today = sdf.parse(str);  
//	        System.out.println("字符串转成日期：" + today);  
	    }  

	/**
	 * 字符串转换成日期
	 * 
	 * @param date
	 * @param fmt
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	public static Date str2Date(String str, String fmt) {
		try {
			if (str == null || str.equals("")) {
				return null;
			}
			SimpleDateFormat format = new SimpleDateFormat(fmt);
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 转换成时间 字符串格式必须为 yyyy-MM-dd HH:mm:ss SSS 、yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd
	 * 
	 * @param val
	 * @return
	 * @throws ParseException
	 */
	public static Date parseToDate(String val) {
		Date date = null;
		try {
			if (val != null && val.trim().length() != 0 && !val.trim().toLowerCase().equals("null")) {
				val = val.trim();
				if (val.length() == 19) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					date = sdf.parse(val);
				}
				if (val.length() == 23) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
					date = sdf.parse(val);
				}
				if (val.length() == 10) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					date = sdf.parse(val);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取制定格式的时间
	 * 
	 * @param format
	 * @return
	 */
	public static String getFormatDate(String fmt) {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar calendar = Calendar.getInstance();
		return format.format(calendar.getTime());
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static int dayForWeek(String date) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(DateUtil.fmt_yyyyMMdd);
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(date));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 获取月开始日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthStart(String fmt, String date) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(fmt);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(format.parse(date));
			int index = calendar.get(Calendar.DAY_OF_MONTH);
			calendar.add(Calendar.DATE, (1 - index));
			return format.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取月末日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthEnd(String fmt, String date) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(fmt);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(format.parse(date));

			calendar.add(Calendar.MONTH, 1);

			int index = calendar.get(Calendar.DAY_OF_MONTH);

			calendar.add(Calendar.DATE, (-index));

			return format.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 增加时间
	 * 
	 * @param date
	 * @param flag
	 *            1:年,2:月,3:日,4:时,5:分,6:秒,
	 * @param value
	 * @return
	 */
	public static String addDate(String date, String fmt, int flag, int value) {
		String resultDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(fmt);
			Calendar cal = Calendar.getInstance();
			cal.setTime(format.parse(date));
			// 默认值为
			int addType = 0;
			switch (flag) {
			case 1:
				addType = Calendar.YEAR;
				break;
			case 2:
				addType = Calendar.MONTH;
				break;
			case 3:
				addType = Calendar.DAY_OF_YEAR;
				break;
			case 4:
				addType = Calendar.HOUR_OF_DAY;
				break;
			case 5:
				addType = Calendar.MINUTE;
				break;
			case 6:
				addType = Calendar.MILLISECOND;
				break;
			default:
				addType = Calendar.YEAR;
				break;
			}
			cal.add(addType, value);
			resultDate = format.format(cal.getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultDate;
	}

	/**
	 * 时间格式转换(秒-->转化成时分秒)
	 * 
	 * @param String
	 * @return
	 */
	public static String formateS2Hms(String sec) {
		Integer second = Integer.parseInt(sec);
		String resultDate = "";
		if ((second / (60 * 60)) > 0) {
			resultDate = resultDate + (second / (60 * 60)) + ":";
			second = second % (60 * 60);
		} else {
			resultDate = resultDate + "00:";
		}

		if ((second / 60) > 0) {
			resultDate = resultDate + (second / 60) + ":";
			second = second % 60;
		} else {
			resultDate = resultDate + "00:";
		}

		if (second > 0) {
			resultDate = resultDate + (second);
		} else {
			resultDate = resultDate + "00";
		}
		return resultDate;
	}

	/**
	 * 两个时间之间相差距离多少天
	 * 
	 * @param str1
	 *            时间参数 1：
	 * @param str2
	 *            时间参数 2：
	 * @return 相差天数
	 */
	public static String getDistanceDays(String str1, String str2) {
		Date date1;
		Date date2;
		long days = 0;
		date1 = str2Date(str1, fmt_yyyyMMdd);
		date2 = str2Date(str2, fmt_yyyyMMdd);
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}
		days = diff / (1000 * 60 * 60 * 24);
		return days + "";
	}
	

	/**
	 * 两个时间之间相差距多少秒
	 * 
	 * @param str1
	 *            时间参数 1：
	 * @param str2
	 *            时间参数 2：
	 * @return 相差天数
	 */
	public static String differSecond(String str1, String str2) {
		Date date1;
		Date date2;
		date1 = str2Date(str1, fmt_yyyyMMddHHmmss);
		date2 = str2Date(str2, fmt_yyyyMMddHHmmss);
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}
		return diff / (1000) + "";
	}
	
}
