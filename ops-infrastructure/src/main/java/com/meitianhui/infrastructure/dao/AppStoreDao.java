package com.meitianhui.infrastructure.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.infrastructure.entity.AppStore;

/**
 * @author mole.wang 2015年12月20日
 *
 */
public interface AppStoreDao {
	/**
	 * 查询app信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<AppStore> selectAppStore(Map<String, Object> params) throws Exception;
}
