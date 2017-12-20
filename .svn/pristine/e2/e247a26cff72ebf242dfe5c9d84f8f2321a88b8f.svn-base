package com.meitianhui.finance.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface AlipayPayService {

	/**
	 * 条码支付(商家扫码用户支付宝的付款码)
	 * 
	 * @param paramsMap
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void barCodePay(Map<String, Object> paramsMap, String scan_flag)
			throws BusinessException, SystemException, Exception;

	/**
	 * 扫码支付(用户扫码商家的收款码)
	 * 
	 * @param paramsMap
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public String scanCodePay(Map<String, Object> paramsMap) throws BusinessException, SystemException, Exception;

	/**
	 * 交易查询
	 * 
	 * @Title: orderQuery
	 * @param app_id
	 * @param private_key
	 * @param out_trade_no
	 * @param query_response
	 *            查询返回的对象
	 * @return
	 * @author tiny
	 */
	public String orderQuery(String app_id, String private_key, String out_trade_no,
			Map<String, Object> query_response);

	/**
	 * 订单取消
	 * 
	 * @param paramsMap
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void orderCancel(String app_id, String private_key, String out_trade_no)
			throws BusinessException, SystemException, Exception;

	/**
	 * 本地生活-支付宝信息
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	public Map<String, String> consumerAlipayInfoGet(Map<String, Object> paramsMap)
			throws BusinessException, SystemException, Exception;

	/**
	 * 店东助手-支付宝信息
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 */
	public Map<String, String> storeAlipayInfoGet(Map<String, Object> paramsMap)
			throws BusinessException, SystemException, Exception;

	/**
	 * 熟么获取支付宝支付信息
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	Map<String, String> shumeAlipayInfoGet(Map<String, Object> paramsMap)
			throws BusinessException, SystemException, Exception;

	/**
	 * 惠点收银机获取支付宝支付信息
	 * 
	 * @Title: cashierAlipayInfoGet
	 * @param paramsMap
	 * @return
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	Map<String, String> cashierAlipayInfoGet(Map<String, Object> paramsMap)
			throws BusinessException, SystemException, Exception;

	/**
	 * 惠易定3.0支付宝Wap支付
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void hydAliPayWapPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 惠易定3.0支付宝Wap支付
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void hydAliPayPcPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询对账单
	 * 
	 * @Title: billDownloadUrlQuery
	 * @param app_id
	 * @param bill_type
	 * @param bill_date
	 * @return
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public String billDownloadUrlQuery(String app_id, String private_key, String bill_date)
			throws BusinessException, SystemException, Exception;

	/**
	 * 交易对账
	 * 
	 * @Title: billCheck
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void billCheck(String bill_date) throws BusinessException, SystemException, Exception;

	/**
	 * 扫码支付对账单下载
	 * 
	 * @Title: alipayBillImport
	 * @param app_id
	 * @param bill_type
	 * @param bill_date
	 * @return
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void alipayBillImport(String bill_date, String app_id, String private_key)
			throws BusinessException, SystemException, Exception;

}
