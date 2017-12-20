package com.meitianhui.order.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.order.entity.OdSettlement;
import com.meitianhui.order.entity.OdSettlementDetail;

public interface OrderDao {

	/**
	 * 订单结算
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertOdSettlement(OdSettlement odSettlement) throws Exception;

	/**
	 * 订单结算商品列表
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertOdSettlementDetailList(List<OdSettlementDetail> list) throws Exception;
	/**
	 * 结算订单查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectOrderSettlement(Map<String, Object> map) throws Exception;
	
	/***
	 * 更新订单结算历史
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月15日
	 */
	public int updateOrderSettlement(Map<String, Object> map) throws Exception;

	/**
	 * 精选特卖订单详情
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectZJSOrder(Map<String, Object> map) throws Exception;
	
	/**
	 * 通过提货码查询订单
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectOrderByLoadedCode(Map<String, Object> map) throws Exception;

}
