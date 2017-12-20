package com.meitianhui.sync.dao;

import java.util.Map;

public interface SyncDao {

	/**
	 * 消费者礼券同步记录保存
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertConsumerVoucherLog(Map<String, Object> map) throws Exception;

	/**
	 * 密码同步记录保存
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertUserPasswordLog(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询消费者最新一条同步记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectConsumerVoucherLastLog() throws Exception;

	/**
	 * 查询密码最新一条同步记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectUserPasswordLogLastLog() throws Exception;

}
