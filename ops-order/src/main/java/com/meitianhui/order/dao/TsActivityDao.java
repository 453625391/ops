package com.meitianhui.order.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.order.entity.TsActivity;
import com.meitianhui.order.entity.TsActivityLog;

/**
 * 伙拼团活动
 * 
 * @ClassName: TsActivityDao
 * @author tiny
 * @date 2017年2月27日 下午6:58:08
 *
 */
public interface TsActivityDao {

	/**
	 * 创建伙拼团活动
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertTsActivity(TsActivity tsActivity) throws Exception;

	/**
	 * 伙拼团活动日志
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertTsActivityLog(TsActivityLog tsActivityLog) throws Exception;

	/**
	 * 查询附近的伙拼团活动(本地生活)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectNearbyTsActivityListForConsumer(Map<String, Object> map) throws Exception;

	/**
	 * 查询伙拼团活动(运营系统)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectTsActivityListForOp(Map<String, Object> map) throws Exception;

	/**
	 * 伙拼团活动列表(本地生活)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectTsActivityListForConsumer(Map<String, Object> map) throws Exception;

	/**
	 * 伙拼团活动列表(店东助手)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectTsActivityListForStores(Map<String, Object> map) throws Exception;

	/**
	 * 伙拼团活动信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public TsActivity selectTsActivity(Map<String, Object> map) throws Exception;

	/**
	 * 伙拼团活动订单统计信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectTsActivityOrderCount(Map<String, Object> map) throws Exception;

	/**
	 * 伙拼团活动详情接口
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public TsActivity selectTsActivityDetail(Map<String, Object> map) throws Exception;

	/**
	 * 查询到时间的伙拼团活动
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectExpiriedActivity(Map<String, Object> map) throws Exception;

	/**
	 * 查询超过7天的活动,设置活动为自动为收货(阶梯价)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateTimeoutDeliveredActivityForLadder(Map<String, Object> map) throws Exception;

	/**
	 * 更新伙拼团活动信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateTsActivity(Map<String, Object> map) throws Exception;

	
}
