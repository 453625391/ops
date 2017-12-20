package com.meitianhui.finance.dao;

import java.util.List;
import java.util.Map;
import com.meitianhui.finance.entity.FDWechatBillLog;

/**
 * 微信对账单数据
 * 
 * @ClassName: FDWechatBillLogDao
 * @author tiny
 * @date 2017年3月22日 下午5:18:53
 *
 */
public interface WechatBillLogDao {

	/**
	 * 支付宝对账单
	 * 
	 * @Title: insertWechatBillLog
	 * @param billLogList
	 * @throws Exception
	 * @author tiny
	 */
	public void insertFDWechatBillLog(List<FDWechatBillLog> list) throws Exception;

	/**
	 * 支付宝对账单统计查询
	 * 
	 * @Title: selectWechatBillLogCount
	 * @param billLogList
	 * @throws Exception
	 * @author tiny
	 */
	public Integer selectWechatBillLogCount(Map<String, Object> map) throws Exception;

	/**
	 * 删除支付宝对账单
	 * 
	 * @Title: deleteWechatBillLog
	 * @param billLogList
	 * @throws Exception
	 * @author tiny
	 */
	public void deleteWechatBillLog(Map<String, Object> map) throws Exception;


	/**
	 * 查询并保存开放平台异常交易数据(开放平台,微信异常)
	 * 
	 * @Title: insertAlipayBillLog
	 * @param billLogList
	 * @throws Exception
	 * @author tiny
	 */
	public void insertOpsBillCheckLog(Map<String, Object> map) throws Exception;

	/**
	 * 查询并保存支付宝异常交易数据(微信成功,开放平台异常)
	 * 
	 * @Title: insertAlipayBillLog
	 * @param billLogList
	 * @throws Exception
	 * @author tiny
	 */
	public void insertWechatBillCheckLog(Map<String, Object> map) throws Exception;

	
	/**
	 * 删除对账记录，重新生成对账数据
	 * @Title: deleteWechatBillCheckLog
	 * @param map
	 * @throws Exception
	 * @author tiny
	 */
	public void deleteWechatBillCheckLog(Map<String, Object> map) throws Exception;

}
