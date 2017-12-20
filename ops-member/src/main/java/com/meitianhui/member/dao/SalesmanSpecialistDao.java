package com.meitianhui.member.dao;


import java.util.List;
import java.util.Map;

import com.meitianhui.member.entity.MDSalesmanSpecialist;


public interface SalesmanSpecialistDao {
	
	/**
	 * 查询业务员地服申请列表
	 * 
	 * @param mdSalesmanSpecialist
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectSalesmanSpecialistList(Map<String, Object> mdSalesmanSpecialist) 
			throws Exception;
	
	/**
	 * 查询业务员地服申请日志列表
	 * 
	 * @param mdSalesmanSpecialistLog
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectSalesmanSpecialistLogList(Map<String, Object> mdSalesmanSpecialistLog) 
			throws Exception;
	
	/**
	 * 查询业务员地服申请详情
	 * 
	 * @param mdSalesmanSpecialist
	 * @throws Exception
	 */
	public Map<String, Object> selectSalesmanSpecialist(Map<String, Object> mdSalesmanSpecialist) throws Exception;
	
	/**
	 * 更新业务员地服申请
	 * 
	 * @param mdSalesmanSpecialist
	 * @return
	 * @throws Exception
	 */
	public int updateSalesmanSpecialist(Map<String, Object> mdSalesmanSpecialist) throws Exception;
	
	/**
	 * 新增业务员地服申请
	 * 
	 * @param mdSalesmanSpecialist
	 * @throws Exception
	 */
	public void insertSalesmanSpecialist(MDSalesmanSpecialist mdSalesmanSpecialist) throws Exception; 
	
	/**
	 * 新增业务员地服申请日志
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertSalesmanSpecialistLog(Map<String, Object> map) throws Exception;
	
	
}
