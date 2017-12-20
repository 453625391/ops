package com.meitianhui.order.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.order.entity.CsOrder;

public interface CsOrderDao {

	
	/**
	 * 创建增值服务订单
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertCsOrder(CsOrder order) throws Exception;
	
	/**
	 * app话费充值订单列表订单查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<CsOrder> selectPhoneBillOrderForAppList(Map<String, Object> map) throws Exception;
	
	/**
	 * 运营话费充值订单列表订单查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<CsOrder> selectCsOrderForOpList(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 订单详情接口
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public CsOrder selectCsOrderDetail(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新增值服务订单信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateCsOrder(Map<String, Object> map) throws Exception;
	
	/***
	 * 查询增值订单结算统计
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月21日
	 */
	public Map<String, Object> selectCsOrderSettlementCount(Map<String, Object> map) throws Exception;
	
	/***
	 * 查询结算订单
	 * @param params
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月21日
	 */
	public List<Map<String, Object>> selectCsOrderForSettlement(Map<String, Object> params) throws Exception;

	
}
