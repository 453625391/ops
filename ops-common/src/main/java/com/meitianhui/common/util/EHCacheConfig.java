package com.meitianhui.common.util;

public class EHCacheConfig {

	/**
	 * 元素最大数量
	 */

	public static int MAXELEMENTSINMEMORY = 2000;

	/**
	 * 
	 * 是否把溢出数据持久化到硬盘
	 */

	public static boolean OVERFLOWTODISK = false;

	/**
	 * 
	 * 是否会死亡
	 */

	public static boolean ETERNAL = false;

	/**
	 * 
	 * 缓存的间歇时间
	 */

	public static int TIMETOIDLESECONDS = 3600;

	/**
	 * 
	 * 存活时间(默认一天)
	 */

	public static int TIMETOlIVESECONDS = 3600;

	/**
	 * 
	 * 需要持久化到硬盘否
	 */

	public static boolean DISKPERSISTENT = false;

	/**
	 * 
	 * 内存存取策略
	 */

	public static String MEMORYSTOREEVICTIONPOLICY = "LFU";
}
