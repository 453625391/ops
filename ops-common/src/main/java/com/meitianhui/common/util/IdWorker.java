package com.meitianhui.common.util;

/**
 * 分布式唯一ID生成器Twitter
 * @author Tiny
 *
 */
public class IdWorker {
	private final long twepoch = 1288834974657L;
	 //机器标识位数
	private final long workerIdBits = 8L;
	//数据中心标识位数
	private final long datacenterIdBits = 2L;
	//机器ID最大值
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	//数据中心ID最大值
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	//毫秒内自增位
	private final long sequenceBits = 12L;
	//机器ID偏左移12位
	private final long workerIdShift = sequenceBits;
	//数据中心ID左移17位
	private final long datacenterIdShift = sequenceBits + workerIdBits;
	//时间毫秒左移22位
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long workerId;
	private long datacenterId;
	private long sequence = 0L;
	private long lastTimestamp = -1L;

	/**
	 * 
	 * @param workerId 机器id
	 * @param datacenterId 数据中心id
	 */
	public IdWorker(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(
					String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public synchronized long nextId() {
		long timestamp = timeGen();
		//时间错误
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		if (lastTimestamp == timestamp) {
			//当前毫秒内，则+1
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				//当前毫秒内计数满了，则等待下一秒
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;
		//ID偏移组合生成最终的ID，并返回ID   
		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
				| (workerId << workerIdShift) | sequence;
	}

	/**
	 * 等待下一个毫秒的到来 
	 * @param lastTimestamp
	 * @return
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}

}