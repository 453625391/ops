package com.meitianhui.notification.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.notification.entity.IdMessageQueue;
import com.meitianhui.notification.entity.IdSmsStatistics;

public interface NotificationDao {

	/**
	 * 新增短信统计
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void insertIdSmsStatistics(List<IdSmsStatistics> list) throws Exception;

	/**
	 * app消息通知
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void insertIdMessageQueue(IdMessageQueue idMessageQueue) throws Exception;

	/**
	 * 查询会员登陆信息
	 * 
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectMemberLoginLog(Map<String, Object> map) throws Exception;

}
