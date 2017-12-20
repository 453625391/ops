package com.meitianhui.order.dao;

import com.meitianhui.order.entity.TsActivitySettlement;

/**
 * 伙拼团活动结算
 * 
 * @ClassName: TsActivitySettlementDao
 * @author tiny
 * @date 2017年3月6日 下午5:48:38
 *
 */
public interface TsActivitySettlementDao {

	/**
	 * 创建结算记录
	 * 
	 * @Title: insertTsActivitySettlement
	 * @param settlement
	 * @throws Exception
	 * @author tiny
	 */
	public void insertTsActivitySettlement(TsActivitySettlement settlement) throws Exception;

}
