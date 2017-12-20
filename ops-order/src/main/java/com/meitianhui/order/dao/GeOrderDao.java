package com.meitianhui.order.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.order.entity.GeOrder;
import com.meitianhui.order.entity.GeOrderItem;

/***
 * 名品汇数据操作接口
 * 
 * @author 丁硕
 * @date 2016年12月28日
 */
public interface GeOrderDao {

	/**
	 * 名品汇订单
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertGeOrder(GeOrder GeOrder) throws Exception;

	/**
	 * 名品汇订单商品列表
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertGeOrderItem(GeOrderItem GeOrderItem) throws Exception;
	
	/**
	 * 名品汇订单信息查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<GeOrder> selectGeOrder(Map<String, Object> map) throws Exception;

	/**
	 * 名品汇订单商品信息查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<GeOrderItem> selectGeOrderItem(Map<String, Object> map) throws Exception;
	
	/***
	 * 运营名品汇查询
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月28日
	 */
	public List<Map<String, Object>> selectGeOrderListForOp(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新名品汇订单信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public int updateGeOrder(Map<String, Object> map) throws Exception;
	
	/***
	 * 名品汇结算订单统计
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月28日
	 */
	public Map<String, Object> selectGeOrderSettlementCount(Map<String, Object> map) throws Exception;
	
	/***
	 * 查询名品汇订单明细（结算）
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月28日
	 */
	public List<Map<String, Object>> selectGeOrderDetailForSettlement(Map<String, Object> map) throws Exception;
	
	
}
