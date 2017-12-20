package com.meitianhui.goods.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/**
 * 红包、见面礼业务接口
 * 
 * @author 丁硕
 * @date 2017年3月1日
 */
public interface GcActivityService {

	/**
	 * 活动发放记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void gcActivityCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 红包发放记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void gcActivityListFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 红包列表查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void gcActivityDetailListFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 红包个数统计
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void gcActivityDetailCountFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 打开红包
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void handleGcActivityOpen(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	
	//=======================================================见面礼===========================================================================
	/***
	 * 扫描二维码得见面礼
	 * @author 丁硕
	 * @date   2017年3月1日
	 */
	void handleGcActivityScanQRCode(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 红包见面礼支付到账
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月1日
	 */
	void handleGcActivityFaceGiftPay(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
}
