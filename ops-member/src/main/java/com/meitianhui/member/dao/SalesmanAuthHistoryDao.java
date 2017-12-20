package com.meitianhui.member.dao;


import java.util.List;
import java.util.Map;

import com.meitianhui.member.entity.MDSalesmanAuthHistory;

public interface SalesmanAuthHistoryDao {
	
	/**
	 * 查询业务员认证申请列表
	 * 
	 * @param mdSalesmanAuthHistory
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectSalesmanAuthHistoryList(Map<String, Object> mdSalesmanAuthHistory) 
			throws Exception;
	
	/**
	 * 查询业务员认证申请日志列表
	 * 
	 * @param mdSalesmanAuthHistoryLog
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectSalesmanAuthHistoryLogList(Map<String, Object> mdSalesmanAuthHistoryLog) 
			throws Exception;
	
	/**
	 * 查询业务员认证申请详情
	 * 
	 * @param mdSalesmanAuthHistory
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectSalesmanAuthHistory(Map<String, Object> mdSalesmanAuthHistory) throws Exception;
	
	/**
	 * 更新业务员认证申请
	 * 
	 * @param mdSalesmanAuthHistory
	 * @return
	 * @throws Exception
	 */
	public int updateSalesmanAuthHistory(Map<String, Object> mdSalesmanAuthHistory) throws Exception;
	
	/**
	 * 新增业务员认证申请
	 * 
	 * @param mdSalesmanAuthHistory
	 * @throws Exception
	 */
	public void insertSalesmanAuthHistory(MDSalesmanAuthHistory mdSalesmanAuthHistory) throws Exception; 
	
	/**
	 * 新增业务员认证申请日志
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertSalesmanAuthLog(Map<String, Object> map) throws Exception;

}
