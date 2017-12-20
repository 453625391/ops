package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

public interface MobileRechargeDao {

	/**
	 * 查询话费充值类型
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectMobileRechargeType(Map<String, Object> map) throws Exception;

}
