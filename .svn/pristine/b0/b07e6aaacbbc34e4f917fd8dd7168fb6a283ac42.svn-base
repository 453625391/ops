package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.goods.entity.LdActivity;
import com.meitianhui.goods.entity.LdActivityProcess;

/**
 * 一元抽奖
 * 
 * @author Tiny
 *
 */
public interface LdActivityDao {

	/**
	 * 一元购商品创建
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertLdActivity(LdActivity ldActivity) throws Exception;

	/**
	 * 一元购商品抽奖号码
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertLdActivityProcess(LdActivityProcess ldActivityProcess) throws Exception;

	/**
	 * 一元购商品抽奖号码(定时开)
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertDskActivityProcess(LdActivityProcess ldActivityProcess) throws Exception;

	/**
	 * 查询一元购商品
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<LdActivity> selectLdActivity(Map<String, Object> map) throws Exception;
	

	/**
	 * 一元抽奖活动统计
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectLdActivityCount(Map<String, Object> map) throws Exception;

	/**
	 * 查询一元购商品
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectLdActivityDetail(Map<String, Object> map) throws Exception;

	/**
	 * 查询一元购购买记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<LdActivityProcess> selectLdActivityProcess(Map<String, Object> map) throws Exception;

	/**
	 * 店东查询一元购购买记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<LdActivityProcess> selectLdActivityProcessForStore(Map<String, Object> map) throws Exception;

	/**
	 * 查询单个一元购商品每个人的购买次数
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectLdActivityProcessCount(Map<String, Object> map) throws Exception;

	/***
	 * 查询附近的活动门店ID列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date 2016年12月10日
	 */
	List<Map<String, Object>> selectNearbyActivityStores(Map<String, Object> map) throws Exception;

	/**
	 * 一元抽奖验证查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> selectLdActivityForValidate(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新一元购活动状态
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updateLdActivity(Map<String, Object> map) throws Exception;

	/**
	 * 更新一元购抽奖码状态
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updateLdActivityProcess(Map<String, Object> map) throws Exception;

}
