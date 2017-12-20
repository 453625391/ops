package com.meitianhui.report.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.report.dao.OrderDao;
import com.meitianhui.report.service.OrderService;

/**
 * 订单统计报表接口
 * 
 * @author 丁忍
 * @date 2017年7月25日
 */
@SuppressWarnings("unchecked")
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	
	@Override
	public void statisticsOrderCountAndMoneyFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap = orderDao.selectOrderCountAndMoney(paramsMap);
		result.setResultData(tempMap);
	}

	@Override
	public void statisticsStoreCountAndConsumerCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> countList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		countList = orderDao.selectStoreCountAndConsumerCount(paramsMap);
		Integer storesCount = 0;
		Integer consumerCount = 0;
		for (int i = 0; i < countList.size(); i++) {
			storesCount += Integer.parseInt(countList.get(i).get("store_qty").toString());
			consumerCount += Integer.parseInt(countList.get(i).get("consumer_qty").toString());
		}
		tempMap.put("storesCount", storesCount);
		tempMap.put("consumerCount", consumerCount);
		result.setResultData(tempMap);
	}

}
