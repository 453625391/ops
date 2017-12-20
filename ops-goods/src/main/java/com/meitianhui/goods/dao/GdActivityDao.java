package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.goods.entity.GdActivity;

/**
 * 权益活动
* @ClassName: GdActivityDao  
* @author tiny 
* @date 2017年3月6日 上午11:43:58  
*
 */
public interface GdActivityDao {

	/**
	 * 添加权益活动 
	* @Title: insertGdActivity  
	* @param GdActivity
	* @throws Exception
	* @author tiny
	 */
	void insertGdActivity(GdActivity gdActivity) throws Exception;

	/**
	 * 查询权益活动
	 * @Title: selectGdActivity  
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	Map<String, Object> selectGdActivity(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询会员权益活动详情（APP）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectGdActivityDetail(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 查询权益活动 列表
	* @Title: selectGdActivityListForOp  
	* @param map
	* @return
	* @throws Exception
	* @author tiny
	 */
	List<Map<String, Object>> selectGdActivityListForOp(Map<String, Object> map) throws Exception;
	
	
	
	/**
	 * 查询权益活动 列表
	 * @Title: selectGdActivityListForConsumer  
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	List<Map<String, Object>> selectGdActivityListForConsumer(Map<String, Object> map) throws Exception;

	/**
	 * 更新权益活动 
	* @Title: updateGdActivity  
	* @param map
	* @return
	* @throws Exception
	* @author tiny
	 */
	int updateGdActivity(Map<String, Object> map) throws Exception;
	
	/**
	 * 权益活动过期自动取消 
	 * @Title: updateExpiredGdActivity  
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	int updateExpiredGdActivity(Map<String, Object> map) throws Exception;

}
