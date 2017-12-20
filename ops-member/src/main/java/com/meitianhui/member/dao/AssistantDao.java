package com.meitianhui.member.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.member.entity.MDAssistant;

public interface AssistantDao {

	/**
	 * 查询店东助手列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectStoresAssistantList(Map<String, Object> map) throws Exception;

	/**
	 * 查询店东助手服务门店列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectAssistantServiceStoresList(Map<String, Object> map) throws Exception;

	/**
	 * 店东助手信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertAssistant(MDAssistant mdAssistant) throws Exception;

	/**
	 * 更新店东助手信息
	 * 
	 * @param mdAssistant
	 * @throws Exception
	 */
	public int updateAssistant(Map<String, Object> mdAssistant) throws Exception;

	/**
	 * 删除店东助手信息
	 * 
	 * @param mdAssistant
	 * @throws Exception
	 */
	public int deleteAssistant(Map<String, Object> mdAssistant) throws Exception;

	/**
	 * 查询店东助手信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectAssistant(Map<String, Object> map) throws Exception;
}
