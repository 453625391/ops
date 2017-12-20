package com.meitianhui.demo.dao.mongodb;

import java.util.Map;

public interface LogDemoDao {
	
	/**
	 * 新增对象
	 * @param map
	 * @throws Exception
	 */
	void insertDemo(Map<String,Object> map) throws Exception;
	
	
}
