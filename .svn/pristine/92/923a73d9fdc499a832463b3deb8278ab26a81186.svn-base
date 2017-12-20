package com.meitianhui.common.util;

import org.apache.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EHCacheUtil {

	private static final Logger logger = Logger.getLogger(EHCacheUtil.class);

	private static CacheManager cacheManager = null;
	private static Cache cache = null;

	/**
	 * 
	 * 初始化缓存管理容器
	 */
	private static synchronized CacheManager initCacheManager() {
		try {
			if (cacheManager == null)
				cacheManager = CacheManager.getInstance();
		} catch (Exception e) {
			logger.error("缓存初始化异常", e);
		}
		return cacheManager;
	}

	/**
	 * 初始化cache
	 * 
	 * @param cacheName
	 *            cache的名字
	 * @param timeToLiveSeconds
	 *            有效时间
	 * @return cache 缓存
	 * @throws Exception
	 */
	private static synchronized void initCache(String cacheName) {
		checkCacheManager();
		initCache(cacheName, EHCacheConfig.MAXELEMENTSINMEMORY, EHCacheConfig.OVERFLOWTODISK, EHCacheConfig.ETERNAL,
				EHCacheConfig.TIMETOlIVESECONDS, EHCacheConfig.TIMETOIDLESECONDS);
	}

	/**
	 * 初始化缓存
	 * 
	 * @param cacheName
	 *            缓存名称
	 * @param maxElementsInMemory
	 *            元素最大数量
	 * @param overflowToDisk
	 *            是否持久化到硬盘
	 * @param eternal
	 *            是否会死亡
	 * @param timeToLiveSeconds
	 *            缓存存活时间
	 * @param timeToIdleSeconds
	 *            缓存的间隔时间
	 * @return 缓存
	 * @throws Exception
	 */
	public static void initCache(String cacheName, int maxElementsInMemory, boolean overflowToDisk, boolean eternal,
			long timeToLiveSeconds, long timeToIdleSeconds) {
		try {
			cache = cacheManager.getCache(cacheName);
			if (cache == null) {
				Cache memoryOnlyCache = new Cache(cacheName, maxElementsInMemory, overflowToDisk, eternal,
						timeToLiveSeconds, timeToIdleSeconds);
				cacheManager.addCache(memoryOnlyCache);
				cache = cacheManager.getCache(cacheName);
			}
		} catch (Exception e) {
			logger.error("缓存初始化异常", e);
		}
	}

	
	/**
	 * 
	 * 检测cacheManager
	 */

	private static void checkCacheManager() {
		if (null == cacheManager) {
			initCacheManager();
		}
	}

	private static void checkCache(String cacheName) {
		if (null == cache) {
			initCache(cacheName);
		}
	}

	/**
	 * 
	 * 添加缓存
	 * 
	 * @param key
	 *            关键字
	 * @param value
	 *            值
	 */
	public static void put(String cacheName, Object key, Object value) {
		checkCache(cacheName);
		// 创建Element,然后放入Cache对象中
		Element element = new Element(key, value);
		cache.put(element);
	}

	/**
	 * 获取cache
	 * 
	 * @param key
	 *            关键字
	 * @return
	 */
	public static Object get(String cacheName, Object key) {
		checkCache(cacheName);
		Element element = cache.get(key);
		if (null == element) {
			return null;
		}
		return element.getObjectValue();
	}

	
	/**
	 * 移除cache中的key
	 * 
	 * @param cacheName
	 */

	public static void remove(String cacheName, String key) {
		checkCache(cacheName);
		cache.remove(key);
	}

	/**
	 * 
	 * 移除所有Element
	 */

	public static void removeAllKey(String cacheName) {
		checkCache(cacheName);
		cache.removeAll();
	}

}
