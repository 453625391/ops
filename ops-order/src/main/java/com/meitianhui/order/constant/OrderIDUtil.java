package com.meitianhui.order.constant;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.meitianhui.common.util.IdWorker;
import com.meitianhui.common.util.PropertiesConfigUtil;

/**
 * 订单号生成工具类
 * 
 * @author Tiny
 *
 */
public class OrderIDUtil {

	private static final Logger logger = Logger.getLogger(OrderIDUtil.class);

	public static IdWorker idWorker = null;
	public final static String datacenter_id = PropertiesConfigUtil.getProperty("datacenter_id", "1");
	public final static String worker_id = PropertiesConfigUtil.getProperty("worker_id", "0");

	static {
		initialIdWorker();
	}

	/**
	 * 初始化IdWorker
	 * 
	 * @Title: initial
	 * @author tiny
	 */
	public static void initialIdWorker() {
		logger.info("datacenter_id=" + datacenter_id + ";worker_id=" + worker_id);
		long workerId = 0;
		long datacenterId = 0;
		if (!StringUtils.isEmpty(worker_id)) {
			workerId = Long.parseLong(worker_id);
		}
		if (!StringUtils.isEmpty(datacenter_id)) {
			datacenterId = Long.parseLong(datacenter_id);
		}
		idWorker = new IdWorker(workerId, datacenterId);
	}

	/**
	 * 获取Id
	 * 
	 * @return
	 */
	public synchronized static String getOrderNo() {
		return idWorker.nextId() + datacenter_id + worker_id;
	}

}
