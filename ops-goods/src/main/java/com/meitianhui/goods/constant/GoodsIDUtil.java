package com.meitianhui.goods.constant;

import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;

/**
 * 订单号生成工具类
 * 
 * @author Tiny
 *
 */
public class GoodsIDUtil {

	/**
	 * 获取code
	 * 
	 * @param password
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public synchronized static String getGoodsCode(RedisUtil redisUtil) throws Exception {
		RedisLock lock = null;
		try {
			String lockKey = "[getGoodsCode]";
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			return IDUtil.getTimestamp(1);
		} catch (Exception e) {
			throw new BusinessException(RspCode.GOODS_CODE_ERROR, "商品码获取失败,请重新操作");
		} finally {
			if (null != lock) {
				lock.unlock();
			}
		}
	}

}
