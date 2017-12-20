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
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.report.dao.StoresDao;
import com.meitianhui.report.service.StoresService;


/**
 * 门店统计报表
 * 
 * @ClassName: StoresServiceImpl
 * @author 丁忍
 * @date 
 *
 */
@Service
public class StoresServiceImpl implements StoresService {

	@Autowired
	public StoresDao storesDao;

	@Autowired
	public RedisUtil redisUtil;


	@Override
	public void storesTransactionTypeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> consumerIdList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("p_stores_id", paramsMap.get("stores_id"));
		consumerIdList = storesDao.selectStoresTransactionType(tempMap);
		result.setResultData(consumerIdList);
	}


}
