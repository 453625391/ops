package com.meitianhui.common.util;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisPoolUtil线程池
 * 
 * @ClassName: JedisPoolUtil
 * @author tiny
 * @date 2017年3月20日 下午8:08:47
 *
 */
public class JedisPoolUtil {

	private static Logger logger = Logger.getLogger(JedisPoolUtil.class);

	protected static ReentrantLock lockPool = new ReentrantLock();

	// Redis服务器IP
	private static String HOSTNAME = PropertiesConfigUtil.getProperty("redis.hostname");
	// Redis的密码
	private static String PASSWORD = PropertiesConfigUtil.getProperty("redis.password");
	// Redis的端口号
	private static String PORT = PropertiesConfigUtil.getProperty("redis.port");
	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static String MAX_ACTIVE = PropertiesConfigUtil.getProperty("redis.maxActive");
	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static String MAX_IDLE = PropertiesConfigUtil.getProperty("redis.maxIdle");
	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = Integer.parseInt(PropertiesConfigUtil.getProperty("redis.maxWait", "3000"));

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;

	private static JedisPool jedisPool = null;

	/**
	 * 初始化Redis连接池
	 */
	private static void initialPool() {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(Integer.parseInt(MAX_ACTIVE));
			config.setMaxIdle(Integer.parseInt(MAX_IDLE));
			config.setMaxWaitMillis(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			if (StringUtil.isBlank(PASSWORD)) {
				// 无密码
				jedisPool = new JedisPool(config, HOSTNAME, Integer.parseInt(PORT));
				logger.info("初始化jedisPool连接池->地址:" + HOSTNAME + ",端口:" + PORT + ",最大连接数:" + MAX_ACTIVE);
			} else {
				// 有密码
				jedisPool = new JedisPool(config, HOSTNAME, Integer.parseInt(PORT), 2000, PASSWORD);
				logger.info(
						"初始化jedisPool连接池->地址:" + HOSTNAME + ",密码:" + PASSWORD + ",端口:" + PORT + ",最大连接数:" + MAX_ACTIVE);
			}
		} catch (Exception e) {
			logger.error("初始化jedisPool异常", e);
		}
	}

	/**
	 * 初始化连接池
	 * 
	 * @Title: poolInit
	 * @author tiny
	 */
	private static void poolInit() {
		lockPool.lock();
		try {
			if (jedisPool == null) {
				initialPool();
			}
		} catch (Exception e) {
			logger.error("初始化连接池", e);
		} finally {
			lockPool.unlock();
		}
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public static Jedis getJedis() {
		if (jedisPool == null) {
			poolInit();
		}
		try {
			if (jedisPool != null) {
				return jedisPool.getResource();
			}
		} catch (Exception e) {
			logger.error("获取Jedis异常", e);
		}
		return null;
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void closeJedis(Jedis jedis) {
		try {
			if (jedis != null) {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error("关闭Jedis异常", e);
		}

	}
}
