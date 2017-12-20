package com.meitianhui.report.dao;

import java.util.List;
import java.util.Map;

public interface OrderDao {

	/**
	 * 订单数和金额统计
	 * 
	 * @param map
	 * @throws Exception
	 */
	public Map<String, Object> selectOrderCountAndMoney(Map<String, Object> map) throws Exception;

	/**
	 * 	订单参与门店和顾客统计
	 * 
	 * @param map
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectStoreCountAndConsumerCount(Map<String, Object> map) throws Exception;

}
