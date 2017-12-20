package com.meitianhui.report.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/***
 * 订单统计报表接口
 * 
 * @author 丁忍
 * @date 2017年7月25日
 */
public interface OrderService {

	public void statisticsOrderCountAndMoneyFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	public void statisticsStoreCountAndConsumerCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
}
