package com.meitianhui.finance.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.finance.entity.FDConsumerPrepayCard;
import com.meitianhui.finance.entity.FDPrepayCard;

public interface PrepayCardDao {


	/**
	 * 亲情卡激活
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertFDPrepayCard(FDPrepayCard fDPrepayCard) throws Exception;

	/**
	 * 亲情卡绑定
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertFDConsumerPrepayCard(FDConsumerPrepayCard fDConsumerPrepayCard) throws Exception;


	/**
	 * 新增消费者亲情卡交易记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertTransPrepayCard(Map<String, Object> paramMap) throws Exception;


	/**
	 * 查询亲情卡是否激活
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<FDPrepayCard> selectFDPrepayCard(Map<String, Object> paramMap) throws Exception;

	/**
	 * 查询亲情卡绑定信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<FDConsumerPrepayCard> selectFDConsumerPrepayCard(Map<String, Object> paramMap) throws Exception;

	/**
	 * 消费者亲情卡统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectFDConsumerPrepayCardCount(Map<String, Object> paramMap) throws Exception;

	/**
	 * 店东激活亲情卡统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectFDPrepayCardActivateCount(Map<String, Object> paramMap) throws Exception;

	/**
	 * 店东激活并绑定亲情卡列表
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectPrepayCardActivateAndBind(Map<String, Object> paramMap) throws Exception;

	/**
	 * 会员预付卡
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectTransPrepayCard(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 店东帮忙激活亲情卡的会员列表
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectStoresActivatePrepayCard(Map<String, Object> paramMap) throws Exception;

	
	/**
	 * 更新亲情卡绑定信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void updateFDConsumerPrepayCard(Map<String, Object> paramMap) throws Exception;

}
