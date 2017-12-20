package com.meitianhui.demo.dao.mysql;

import java.util.List;
import java.util.Map;

import com.meitianhui.demo.entity.Demo;

public interface DemoDao {
	
	/**
	 * 新增对象
	 * @param map
	 * @throws Exception
	 */
	void insertDemo(Demo demo) throws Exception;
	
	/**
	 * 查询对象列表信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Demo> selectDemo(Map<String,Object> paramMap)throws Exception;
	
	/**
	 * 查询区域代码
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectMdArea()throws Exception;
	
	
}
