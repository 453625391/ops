package com.meitianhui.report.dao;

import java.util.List;
import java.util.Map;


public interface StoresDao {

	/**
	 * 查询门店类型
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectStoresTransactionType(Map<String, Object> map) throws Exception;
}
