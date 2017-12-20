package com.meitianhui.member.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.member.entity.MDSaleAssistant;

/**
 * 社区导购
 * @author Tiny
 *
 */
public interface SaleAssistantDao {
	
	/**
	 * 增加社区导购信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertMDSaleAssistant(MDSaleAssistant mDSaleAssistant) throws Exception;

	
	/**
	 * 查询社区导购信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectMDSaleAssistant(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询消费者的社区导购信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectMDSaleAssistantForConsumerList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询店东的社区导购信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectMDSaleAssistantForStoresList(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 更新社区导购信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer updateMDSaleAssistant(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 删除社区导购信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer deleteMDSaleAssistant(Map<String, Object> map) throws Exception;
	
	
}
