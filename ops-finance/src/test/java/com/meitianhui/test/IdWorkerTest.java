package com.meitianhui.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.meitianhui.common.util.IdWorker;

public class IdWorkerTest {

	public static Set<Long> setAll = new HashSet<>();
	public static long size = 0;

	public static void testProductIdByMoreThread(final Map<String, IdWorker> idFactories, final int n)
			throws InterruptedException {
		List<Thread> tlist = new ArrayList<>();
		CountDownLatch cdLatch = new CountDownLatch(10);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			Thread temp = new Thread(new Runnable() {
				@Override
				public void run() {
					Set<Long> setId = new HashSet<>();
					IdWorker idWorker = idFactories.get(Thread.currentThread().getName());
					for (int j = 0; j < n; j++) {
						long id = idWorker.nextId();
						setId.add(id);
						// System.out.println(Thread.currentThread().getName() +
						// ",id:" + id);
					}
					addSetId(Thread.currentThread().getName(), n, setId);
					// synchronized (setAll) {
					// setAll.addAll(setId);
					// addSetId(Thread.currentThread().getName(), n, setId);
					// System.out.println(Thread.currentThread().getName() +
					// "预计产生" + n + "个id,实际产生了" + setId.size()
					// + "个id,并成功加入到setAll中.目前中条数为:" + setAll.size());
					// }
				}
			}, "snowflake" + i);
			tlist.add(temp);
		}
		for (int j = 0; j < 10; j++) {
			tlist.get(j).start();
		}
		cdLatch.await();
		long end1 = System.currentTimeMillis() - start;

		System.out.println("共耗时:" + end1 + "毫秒,预期应该生产" + 10 * n + "个id, 实际合并总计生成ID个数:" + setAll.size());
	}

	/**
	 * 统计重复率
	 * 
	 * @Title: addSetId
	 * @param threadName
	 * @param n
	 * @param setId
	 * @author tiny
	 */
	public static void addSetId(String threadName, int n, Set<Long> setId) {
		setAll.addAll(setId);
		size += setId.size();
		System.out.println(threadName + "预计产生" + n + "个id,实际产生了" + setId.size() + "个id,并成功加入到setAll中.setAll中总条数为:"
				+ setAll.size() + ",总生成id数量为:" + size);
	}

	public static void testProductId(int dataCenterId, int workerId, int n) {
		IdWorker idWorker = new IdWorker(workerId, dataCenterId);
		IdWorker idWorker2 = new IdWorker(workerId + 1, dataCenterId);
		Set<Long> setOne = new HashSet<>();
		Set<Long> setTow = new HashSet<>();
		long start = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			setOne.add(idWorker.nextId());// 加入set
		}
		long end1 = System.currentTimeMillis() - start;
		System.out.println("第一批ID预计生成" + n + "个,实际生成" + setOne.size() + "个<<<<*>>>>共耗时:" + end1 + "毫秒");

		start = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			setTow.add(idWorker2.nextId());// 加入set
		}
		long end2 = System.currentTimeMillis() - start;
		System.out.println("第一批ID预计生成" + n + "个,实际生成" + setTow.size() + "个<<<<*>>>>共耗时:" + end2);
		setOne.addAll(setTow);
		System.out.println("合并总计生成ID个数:" + setOne.size());

	}

	public static void testPerSecondProductIdNums() {
		IdWorker idWorker = new IdWorker(1, 2);
		long start = System.currentTimeMillis();
		int count = 0;
		for (int i = 0; System.currentTimeMillis() - start < 1000; i++, count = i) {
			/** 测试方法一: 此用法纯粹的生产ID,每秒生产ID个数为300w+ */
			idWorker.nextId();
			/**
			 * 测试方法二: 在log中打印,同时获取ID,此用法生产ID的能力受限于log.error()的吞吐能力. 每秒徘徊在10万左右.
			 */
			System.out.println(idWorker.nextId());
		}
		long end = System.currentTimeMillis() - start;
		System.out.println(end);
		System.out.println(count);
	}

	public static void main(String[] args) {
		/**
		 * case1: 测试每秒生产id个数? 结论: 每秒生产id个数300w+
		 */
		// testPerSecondProductIdNums();

		/**
		 * case2: 单线程-测试多个生产者同时生产N个id,验证id是否有重复? 结论: 验证通过,没有重复.
		 */
//		 testProductId(1, 2, 100000);// 验证通过!
		// testProductId(1,2,20000);//验证通过!

		/**
		 * case3: 多线程-测试多个生产者同时生产N个id, 全部id在全局范围内是否会重复? 结论: 验证通过,没有重复.
		 */
		try {
			Map<String, IdWorker> idFactories = new HashMap<>();
			int threadNo = 1;
			for (int i = 0; i < 10; i++) {
				// 用线程名称做map key.
				idFactories.put("snowflake" + i, new IdWorker(threadNo, 1));
			}
			testProductIdByMoreThread(idFactories, 100);// 单机测试此场景,性能损失至少折半!
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 
	}
	
	
}
