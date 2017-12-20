package com.meitianhui.infrastructure.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.infrastructure.entity.IdMessageQueue;
import com.meitianhui.infrastructure.entity.IdSmsStatistics;

public interface InfrastructureDao {
	
	
	/**
	 * 新增短信统计
	 * @param user
	 * @throws Exception
	 */
	void insertIdSmsStatistics(List<IdSmsStatistics> list)throws Exception;
	
	/**
	 * app消息通知
	 * @param user
	 * @throws Exception
	 */
	void insertIdMessageQueue(IdMessageQueue idMessageQueue)throws Exception;
	
	
	/**
	 * 用户意见反馈
	 * @param user
	 * @throws Exception
	 */
	void insertIdUserFeedback(Map<String,Object> map)throws Exception;
	
	
	/**
	 * 用户意见反馈查询
	 * @param user
	 * @throws Exception
	 */
	List<Map<String,Object>> selectUserFeedback(Map<String,Object> map)throws Exception;
	
	/**
	 * 更新用户意见反馈
	 * @param user
	 * @throws Exception
	 */
	void updateUserFeedback(Map<String,Object> map)throws Exception;
	
	
}
