package com.meitianhui.test;

import java.util.Iterator;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.meitianhui.common.util.RedisUtil;

/**
 * redis spring 简单例子
 * 
 * @author hk
 *
 *         2012-12-22 上午10:40:15
 */
@SuppressWarnings({ "resource", "rawtypes" })
public class TestRedis {

	public static void main(String[] args) throws InterruptedException {
		testRedis();
		//clear();
	}

	public static void testRedis() {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:config/spring.xml");
		// 这里已经配置好,属于一个redis的服务接口
		RedisUtil redisUtil = (RedisUtil) app.getBean("redisUtil");

		String ping = redisUtil.ping();// 测试是否连接成功,连接成功输出PONG
		System.out.println(ping);

		// 首先,我们看下redis服务里是否有数据
		long dbSizeStart = redisUtil.dbSize();
		System.out.println(dbSizeStart);
		Set<String> s = redisUtil.keys("*");
		Iterator it = s.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			System.out.println(key);
			if(key.contains("ec6134c0243563f80b96bf44cf89fb124c0b3d6098887029fe6d059e107df7c2377286e9d48421314deefd0de096cbfa")){
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+redisUtil.getStr(key));
			}
		}

	}

	public static void clear() {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:config/spring.xml");
		// 这里已经配置好,属于一个redis的服务接口
		RedisUtil redisUtil = (RedisUtil) app.getBean("redisUtil");
		redisUtil.flushDB();
		System.out.println("清除redis缓存成功");
	}
}