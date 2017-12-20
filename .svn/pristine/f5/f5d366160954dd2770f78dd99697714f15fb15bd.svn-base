package com.meitianhui.common.util;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;

/***
 * Redis distributed lock
 * 
 * @author 丁硕
 * @date 2017年3月2日
 */
public class RedisLock {

	//重新获取锁的间隔时间
	private static final long DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 5;
	
	private RedisUtil redisUtil;

	/**
	 * Lock key path.
	 */
	private String lockKey;

	/**
	 * 锁超时时间，防止线程在入锁以后，无限的执行等待
	 */
	private int expireMills = 60 * 1000;

	/**
	 * 锁等待时间，防止线程饥饿
	 */
	private int timeoutMills = 10 * 1000;
	
	private volatile boolean locked = false;

	public RedisLock(RedisUtil redisUtil, String lockKey, int timeoutMills) {
		this.redisUtil = redisUtil;
		this.lockKey = lockKey + "_redis_lock";
		this.timeoutMills = timeoutMills;
	}

	public RedisLock(RedisUtil redisUtil, String lockKey, int timeoutMills, int expireMills) {
		this(redisUtil, lockKey, timeoutMills);
		this.expireMills = expireMills;
	}

	/**
	 * @return lock key
	 */
	public String getLockKey() {
		return lockKey;
	}

	/**
	 * 获得 lock. 实现思路: 主要是使用了redis 的setnx命令,缓存了锁. reids缓存的key是锁的key,所有的共享,
	 * value是锁的到期时间(注意:这里把过期时间放在value了,没有时间上设置其超时时间) 执行过程:
	 * 1.通过setnx尝试设置某个key的值,成功(当前没有这个锁)则返回,成功获得锁
	 * 2.锁已经存在则获取锁的到期时间,和当前时间比较,超时的话,则设置新的值
	 *
	 * @return true if lock is acquired, false acquire timeouted
	 * @throws InterruptedException
	 *             in case of thread interruption
	 */
	public synchronized boolean lock() throws Exception{
		long timeout = TimeUnit.MILLISECONDS.toNanos(timeoutMills);
		long nanoTime = System.nanoTime();
		while (System.nanoTime() - nanoTime < timeout) {
			long expires = System.currentTimeMillis() + expireMills + 1;
			String expiresStr = String.valueOf(expires); // 锁到期时间
			if (redisUtil.setNxStr(lockKey, expiresStr)) {
				locked = true;
				return true;
			}

			String currentValueStr = redisUtil.getStr(lockKey); // redis里的时间
			if (currentValueStr != null && Long.parseLong(currentValueStr + "") < System.currentTimeMillis()) {
				// 判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
				// lock is expired

				Object oldValueStr = redisUtil.getSetStr(lockKey, expiresStr);
				// 获取上一个锁到期时间，并设置现在的锁到期时间，
				// 只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
				if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
					// 防止误删（覆盖，因为key是相同的）了他人的锁——这里达不到效果，这里值会被覆盖，但是因为什么相差了很少的时间，所以可以接受
					// [分布式的情况下]:如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
					// lock acquired
					locked = true;
					return true;
				}
			}
			/***
			 * 延迟100 毫秒，这里使用随机时间可能会好一点,可以防止饥饿进程的出现,即当同时到达多个进程,只会有一个进程获得锁,
			 * 使用随机的等待时间可以一定程度上保证公平性
			 */
			Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS, RandomUtils.nextInt(999));
		}
		return false;
	}

	/**
	 * Acqurired lock release.
	 * @throws Exception 
	 */
	public synchronized void unlock() throws Exception {
		if (locked) {
			redisUtil.del(lockKey);
			locked = false;
		}
	}

}