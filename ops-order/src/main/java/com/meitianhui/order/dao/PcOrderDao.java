package com.meitianhui.order.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.order.entity.PcOrder;
import com.meitianhui.order.entity.PcOrderItem;

public interface PcOrderDao {

	/**
	 * 新增掌上便利店商品订单
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertPcOrder(PcOrder pcOrder) throws Exception;

	/**
	 * 掌上便利店订单商品列表
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertPcOrderItem(PcOrderItem pcOrderItem) throws Exception;

	/**
	 * 掌上便利店订单信息查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PcOrder> selectPcOrder(Map<String, Object> map) throws Exception;

	/**
	 * 掌上便利店订单统计
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PcOrderItem> selectPcOrderItem(Map<String, Object> map) throws Exception;

	/**
	 * 统计已支付的订单数
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectPcOrderPayedCount(Map<String, Object> map) throws Exception;

	/**
	 * 掌上便利店订单统计
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectPcOrderCount(Map<String, Object> map) throws Exception;

	/**
	 * 查询超时未确认收货的的精选特卖的订单
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectTimeoutDeliveredPcOrder(Map<String, Object> map) throws Exception;

	/**
	 * 更新掌上便利店订单信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updatePcOrder(Map<String, Object> map) throws Exception;

	/***
	 * 查询掌上便利店订单列表（运营）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date 2016年12月30日
	 */
	public List<Map<String, Object>> selectPcOrderListForOp(Map<String, Object> map) throws Exception;

}
