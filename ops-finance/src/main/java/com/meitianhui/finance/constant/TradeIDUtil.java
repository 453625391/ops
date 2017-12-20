package com.meitianhui.finance.constant;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.IdWorker;
import com.meitianhui.common.util.PropertiesConfigUtil;

/**
 * 交易号生成工具类
 * 
 * @author Tiny
 *
 */

public class TradeIDUtil {

	private static final Logger logger = Logger.getLogger(TradeIDUtil.class);

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

	public synchronized static String getTradeNo() {
		return idWorker.nextId() + IDUtil.random(6) + datacenter_id + worker_id;
	}

	/**
	 * 获取交易码
	 * 
	 * @return
	 */
	public synchronized static String getSecurityCode() {
		return idWorker.nextId() + "";
	}
}
