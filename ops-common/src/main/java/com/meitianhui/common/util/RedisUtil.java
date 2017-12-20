package com.meitianhui.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

/**
 * 封装redis 缓存服务器服务接口
 * 
 * @author Tiny
 *
 */
@Component("redisUtil")
public class RedisUtil {

	private static final Logger logger = Logger.getLogger(RedisUtil.class);

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	public void del(String key) {
		Jedis jedis = JedisPoolUtil.getJedis();
		try {
			jedis.del(key);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
	}

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	public void delObj(String key) {
		Jedis jedis = JedisPoolUtil.getJedis();
		try {
			jedis.del(key.getBytes());
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
	}

	/**
	 * 添加key value 并且设置存活时间 liveTime的单位为秒
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public void setStr(String key, String value, int live_time) {
		Jedis jedis = JedisPoolUtil.getJedis();
		try {
			setStr(key, value);
			jedis.expire(key, live_time);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}

	}

	/**
	 * 添加key String
	 * 
	 * @param key
	 * @param value
	 */
	public void setStr(String key, String value) {
		Jedis jedis = JedisPoolUtil.getJedis();
		try {
			jedis.set(key, value);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
	}

	/***
	 * 添加key string 如果存在，返回false
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date 2017年3月2日
	 */
	public boolean setNxStr(String key, String value) {
		boolean flag = false;
		Jedis jedis = JedisPoolUtil.getJedis();
		try {
			flag = jedis.setnx(key, value) == 1;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
		return flag;
	}

	/***
	 * 设置新值，并返回旧值
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date 2017年3月2日
	 */
	public String getSetStr(String key, String value) {
		String str = null;
		Jedis jedis = JedisPoolUtil.getJedis();
		try {
			str = jedis.getSet(key, value);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
		return str;
	}

	/**
	 * 获取redis String
	 * 
	 * @param key
	 * @return
	 */
	public String getStr(String key) {
		Jedis jedis = JedisPoolUtil.getJedis();
		String value = null;
		try {
			value = jedis.get(key);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
		return value;
	}

	/**
	 * 添加key Object
	 * 
	 * @param key
	 * @param List
	 */
	public void setObj(String key, Object obj) {
		Jedis jedis = JedisPoolUtil.getJedis();
		try {
			jedis.set(key.getBytes(), serialize(obj));
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
	}

	/**
	 * 获取redis Object
	 * 
	 * @param key
	 * @return
	 */
	public Object getObj(String key) {
		Jedis jedis = JedisPoolUtil.getJedis();
		Object obj = null;
		try {
			byte[] bytes = jedis.get(key.getBytes());
			if (null != bytes) {
				obj = unserialize(bytes);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
		return obj;
	}

	/**
	 * 添加key Object,并设置有效时长
	 * 
	 * @param key
	 * @param obj
	 */
	public void setObj(String key, Object obj, int live_time) {
		Jedis jedis = JedisPoolUtil.getJedis();
		try {
			jedis.set(key.getBytes(), serialize(obj));
			jedis.expire(key, live_time);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
	}

	/**
	 * 获取redis Object,并设置有效时间
	 * 
	 * @param key
	 * @return
	 */
	public Object getObjAndDelay(String key, int live_time) {
		Jedis jedis = JedisPoolUtil.getJedis();
		Object obj = null;
		try {
			obj = unserialize(jedis.get(key.getBytes()));
			if (obj != null) {
				jedis.expire(key, live_time);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
		return obj;
	}

	/**
	 * 通过正则匹配keys
	 * 
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(String pattern) {
		Jedis jedis = JedisPoolUtil.getJedis();
		Set<String> set = null;
		try {
			set = jedis.keys(pattern);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
		return set;
	}

	/**
	 * 添加key map
	 * 
	 * @param key
	 * @param map
	 */
	public void setMap(String key, Map<String, String> map) {
		Jedis jedis = JedisPoolUtil.getJedis();
		try {
			jedis.hmset(key, map);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
	}

	/**
	 * 添加key value
	 * 
	 * @param key
	 * @param map
	 */
	public void setList(String key, String value) {
		Jedis jedis = JedisPoolUtil.getJedis();
		try {
			jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
	}

	/**
	 * 获取list中的数值
	 * 
	 * @param key
	 * @param map
	 * @return
	 */
	public List<String> getList(String key) {
		Jedis jedis = JedisPoolUtil.getJedis();
		List<String> list = null;
		try {
			list = jedis.lrange(key, 0, -1);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
		return list;
	}

	/**
	 * 检查key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(String key) {
		Jedis jedis = JedisPoolUtil.getJedis();
		boolean flag = false;
		try {
			flag = jedis.exists(key);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
		return flag;
	}

	/**
	 * 查看redis里有多少数据
	 */
	public long dbSize() {
		Jedis jedis = JedisPoolUtil.getJedis();
		long db_size = 0;
		try {
			db_size = jedis.dbSize();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
		return db_size;
	}

	/**
	 * 检查是否连接成功
	 * 
	 * @return
	 */
	public String ping() {
		Jedis jedis = JedisPoolUtil.getJedis();
		String ping = null;
		try {
			ping = jedis.ping();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			JedisPoolUtil.closeJedis(jedis);
		}
		return ping;
	}

	/**
	 * 序列化
	 * 
	 * @param object
	 * @return
	 */
	public byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}