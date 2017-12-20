package com.meitianhui.order.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.order.entity.TsOrder;


/**
 * 伙拼团订单
* @ClassName: TsOrderDao  
* @author tiny 
* @date 2017年2月27日 下午6:58:21  
*
 */
public interface TsOrderDao {

	/**
	 * 创建伙拼团订单
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertTsOrder(TsOrder order) throws Exception;
	
	/**
	 * 查询活动的订单列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectTsOrderList(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 查询伙拼团订单(本地生活)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectTsOrderListForConsumer(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询伙拼团订单(运营系统)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectTsOrderListForOp(Map<String, Object> map) throws Exception;
	
	/**
	 * 伙拼团订单详情接口
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> selectTsOrderDetail(Map<String, Object> map) throws Exception;
	
	/**
	 * 伙拼团参团人信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectTsOrderJoinList(Map<String, Object> map) throws Exception;
	
	/**
	 * 伙拼团订单详情接口
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<TsOrder> selectTsOrder(Map<String, Object> map) throws Exception;
	
	/**
	 * 伙拼团订单结算统计
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> selectSettleTsOrderCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 伙拼团结算订单列表查询(运营)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectSettleTsOrderListForOp(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 伙拼团结算订单查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectSettleTsOrderList(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 伙拼团超时未确认收货的订单
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectTimeoutDeliveredTsOrder(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 伙拼团订单统计
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> selectTsOrderCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 伙拼团订单统计(店东)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> selectTsOrderForStoresCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新伙拼团订单信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateTsOrder(Map<String, Object> map) throws Exception;
	
	
}
