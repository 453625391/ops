package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.goods.entity.PlActivity;
import com.meitianhui.goods.entity.PlLuckyDelivery;
import com.meitianhui.goods.entity.PlPartcipator;

public interface PlPartcipatorDao {

	/**
	 * 添加抽奖活动
	 * 
	 * @param plActivity
	 * @throws Exception
	 */
	void insertPlActivity(PlActivity plActivity) throws Exception;

	/**
	 * 添加中奖信息
	 * 
	 * @param plActivity
	 * @throws Exception
	 */
	void insertPlLuckyDelivery(List<PlLuckyDelivery> list) throws Exception;

	/**
	 * 添加活动参与
	 * 
	 * @param plActivity
	 * @throws Exception
	 */
	void insertPlPartcipator(PlPartcipator plPartcipator) throws Exception;

	/**
	 * 查询抽奖活动列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPlPartcipator(Map<String, Object> map) throws Exception;

	/**
	 * 查询中奖的用户以及中奖的次数
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectWinMemberGroupFind(Map<String, Object> map) throws Exception;

	/**
	 * 查询抽奖活动列表（运营平台）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPlActivityForOp(Map<String, Object> map) throws Exception;

	/**
	 * 查询抽奖活动列表（消费者端）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPlActivityForConsumer(Map<String, Object> map) throws Exception;

	/**
	 * 查询中奖信息列表(运营端)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPlLuckyDeliveryForOp(Map<String, Object> map) throws Exception;

	/**
	 * 查询活动中奖详情列表（消费者端）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPlLuckyDeliveryForConsumer(Map<String, Object> map) throws Exception;

	/**
	 * 查询活动参与列表(运营端)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPlPartcipatorForOp(Map<String, Object> map) throws Exception;

	/**
	 * 查询活动参与列表（消费者端）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPlPartcipatorForConsumer(Map<String, Object> map) throws Exception;

	/**
	 * 修改抽奖活动
	 * 
	 * @param plActivity
	 * @throws Exception
	 */
	int updatePlActivity(Map<String, Object> map) throws Exception;

	/**
	 * 修改中奖信息
	 * 
	 * @param plActivity
	 * @throws Exception
	 */
	int updatePlLuckyDelivery(Map<String, Object> map) throws Exception;

	/**
	 * 修改活动参与
	 * 
	 * @param plActivity
	 * @throws Exception
	 */
	int updatePlPartcipator(Map<String, Object> map) throws Exception;

	/**
	 * 删除参加活动
	 * 
	 * @param plActivity
	 * @throws Exception
	 */
	void deletePlPartcipatorByActivity(Map<String, Object> map) throws Exception;

}
