package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.goods.entity.GdActivityDelivery;

/**
 * 权益活动发货
* @ClassName: GdActivityDeliveryDao  
* @author tiny 
* @date 2017年3月6日 上午11:44:07  
*
 */
public interface GdActivityDeliveryDao {

	/**
	 * 添加权益活动发货
	 * 
	 * @Title: insertGdActivityDelivery
	 * @param GdActivityDelivery
	 * @throws Exception
	 * @author tiny
	 */
	void insertGdActivityDelivery(GdActivityDelivery gdActivityDelivery) throws Exception;

	/**
	 * 查询权益活动发货列表
	 * 
	 * @Title: selectGdActivityDeliveryList
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	List<Map<String, Object>> selectGdActivityDeliveryList(Map<String, Object> map) throws Exception;

	/**
	 * 查询权益活动发货信息
	 * 
	 * @Title: selectGdActivityDelivery
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	Map<String, Object> selectGdActivityDelivery(Map<String, Object> map) throws Exception;

	/**
	 * 更新权益活动发货信息
	 * 
	 * @Title: updateGdActivityDelivery
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	int updateGdActivityDelivery(Map<String, Object> map) throws Exception;

}
