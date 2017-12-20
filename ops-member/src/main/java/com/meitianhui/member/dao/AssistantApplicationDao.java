package com.meitianhui.member.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.member.entity.MDAssistantApplication;

public interface AssistantApplicationDao {
	/**
	 * 查询业务员助教申请列表
	 * 
	 * @param mdAssistantApplication
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectAssistantApplicationList(Map<String, Object> mdAssistantApplication) 
			throws Exception;
	
	/**
	 * 查询业务员助教申请日志列表
	 * 
	 * @param mdAssistantApplicationLog
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectAssistantApplicationLogList(Map<String, Object> mdAssistantApplicationLog) 
			throws Exception;
	
	/**
	 * 查询业务员助教申请详情
	 * 
	 * @param mdAssistantApplication
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectAssistantApplication(Map<String, Object> mdAssistantApplication) throws Exception;
	
	/**
	 * 更新业务员助教申请
	 * 
	 * @param mdAssistantApplication
	 * @return
	 * @throws Exception
	 */
	public int updateAssistantApplication(Map<String, Object> mdAssistantApplication) throws Exception;
	
	/**
	 * 新增业务员助教申请
	 * 
	 * @param mdAssistantApplication
	 * @throws Exception
	 */
	public void insertAssistantApplication(MDAssistantApplication mdAssistantApplication) throws Exception; 
	
	/**
	 * 新增业务员助教申请日志
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertAssistantApplicationLog(Map<String, Object> map) throws Exception;
}
