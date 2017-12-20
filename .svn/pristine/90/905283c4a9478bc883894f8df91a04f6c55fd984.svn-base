package com.meitianhui.finance.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.finance.entity.FDAlipayBillLog;

/**
 * 支付宝对账单数据
 * 
 * @ClassName: FDAlipayBillLogDao
 * @author tiny
 * @date 2017年3月22日 下午5:19:05
 *
 */
public interface AlipayBillLogDao {

	/**
	 * 支付宝对账单
	 * 
	 * @Title: insertAlipayBillLog
	 * @param billLogList
	 * @throws Exception
	 * @author tiny
	 */
	public void insertFDAlipayBillLog(List<FDAlipayBillLog> list) throws Exception;

	/**
	 * 支付宝对账单统计查询
	 * 
	 * @Title: selectAlipayBillLogCount
	 * @param billLogList
	 * @throws Exception
	 * @author tiny
	 */
	public Integer selectAlipayBillLogCount(Map<String, Object> map) throws Exception;

	/**
	 * 删除支付宝对账单
	 * 
	 * @Title: deleteAlipayBillLog
	 * @param billLogList
	 * @throws Exception
	 * @author tiny
	 */
	public void deleteAlipayBillLog(Map<String, Object> map) throws Exception;

	/**
	 * 查询并保存开放平台异常交易数据(开放平台,支付宝异常)
	 * 
	 * @Title: insertAlipayBillLog
	 * @param billLogList
	 * @throws Exception
	 * @author tiny
	 */
	public void insertOpsBillCheckLog(Map<String, Object> map) throws Exception;

	/**
	 * 查询并保存支付宝异常交易数据(支付宝成功,开放平台异常)
	 * 
	 * @Title: insertAlipayBillLog
	 * @param billLogList
	 * @throws Exception
	 * @author tiny
	 */
	public void insertAlipayBillCheckLog(Map<String, Object> map) throws Exception;

	/**
	 * 删除对账记录，重新生成对账数据
	 * @Title: deleteAlipayBillCheckLog
	 * @param map
	 * @throws Exception
	 * @author tiny
	 */
	public void deleteAlipayBillCheckLog(Map<String, Object> map) throws Exception;

}
