package com.meitianhui.sync.service;

public interface SyncService {

	/**
	 * 用户密码同步记录保存
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void userPasswordLogAdd(Integer last_process_point, String exception_desc) throws Exception;
	
	/**
	 * 消费者礼券同步记录保存
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void consumerVoucherLogAdd(Integer last_process_point, String exception_desc) throws Exception;
 

	/**
	 * 查询消费者最新一条同步记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer userPasswordLastLogFind() throws Exception;
	/**
	 * 查询消费者最新一条同步记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer consumerVoucherLastLogFind() throws Exception;

}
