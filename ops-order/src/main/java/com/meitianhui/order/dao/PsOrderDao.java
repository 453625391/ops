package com.meitianhui.order.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.order.entity.PsOrder;
import com.meitianhui.order.entity.PsOrderExtra;
import com.meitianhui.order.entity.PsOrderItem;
import com.meitianhui.order.entity.PsSubOrder;

public interface PsOrderDao {

	/**
	 * 新增自营商贸订单
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertPsOrder(PsOrder psOrder) throws Exception;

	/**
	 * 订单商品列表
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertPsOrderItem(PsOrderItem psOrderItem) throws Exception;

	/**
	 * 我要批订单扩展信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertPsOrderExtra(PsOrderExtra psOrderExtra) throws Exception;

	/**
	 * 我要批子订单信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertPsSubOrder(PsSubOrder psSubOrder) throws Exception;

	/**
	 * 店东我要批订单统计(可以按订单类型统计)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectPsOrderCount(Map<String, Object> map) throws Exception;

	/**
	 * 自营商贸订单信息查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PsOrder> selectPsOrder(Map<String, Object> map) throws Exception;

	/**
	 * 自营商贸订单商品信息查询(后期废除)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectPsOrderItem(Map<String, Object> map) throws Exception;

	/**
	 * 自营商贸订单信息查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PsOrder> selectWypOrderForOp(Map<String, Object> map) throws Exception;

	/**
	 * 我要批订单商品信息查询(店东)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectWypOrderForStores(Map<String, Object> map) throws Exception;

	/**
	 * 我要批订单商品列表信息查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<PsOrderItem> selectWypOrderItem(Map<String, Object> map) throws Exception;

	/**
	 * 我要批订单商品查询(订单结算)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectWypOrderItemForSettlement(Map<String, Object> map) throws Exception;

	/**
	 * 团购预售订单商品信息查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectPsGroupGoods(Map<String, Object> map) throws Exception;

	/**
	 * 团购预售子订单商品信息查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectPsGroupOrderItemForStores(Map<String, Object> map) throws Exception;

	/**
	 * 团购预售子订单商品信息查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectPsGroupSubOrder(Map<String, Object> map) throws Exception;

	/**
	 * 团购预售订单商品信息查询(运营系统)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectPsSubOrderForOp(Map<String, Object> map) throws Exception;

	/**
	 * 我要批结算订单统计
	 * 
	 * @param map
	 * @throws Exception
	 */
	public Map<String, Object> selectPsOrderSettlementCount(Map<String, Object> map) throws Exception;

	/**
	 * 更新团购预售到时间的订单的状态
	 * 
	 * @param map
	 * @throws Exception
	 */
	public int updateExpirePsGroupOrder(Map<String, Object> map) throws Exception;

	/**
	 * 更新团购预售子订单信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public int updatePsSubOrder(Map<String, Object> map) throws Exception;

	/**
	 * 运营我要批订单列表查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectPsOrderForOp(Map<String, Object> map) throws Exception;

	/**
	 * 我要批订单统计
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectPsOrderGoodsCount(Map<String, Object> map) throws Exception;

	/**
	 * 查询到期的团购预售懂订单
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectExpirePsSubOrder(Map<String, Object> map) throws Exception;

	/**
	 * 查询超时未确认收货的的我要批订单
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectTimeoutDeliveredPsOrder(Map<String, Object> map) throws Exception;

	/**
	 * 更新自营商贸订单信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updatePsOrder(Map<String, Object> map) throws Exception;

	/**
	 * 更新团购预售订单信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public int updatePsOrderExtra(Map<String, Object> map) throws Exception;

}
