package com.meitianhui.finance.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.service.GoldService;


/**
 * 金币业务层
* @ClassName: GoldServiceImpl  
* @author tiny 
* @date 2017年2月24日 上午11:56:21  
*
 */
@Service
public class GoldServiceImpl implements GoldService {

	@Override
	public void handleGoldExchangeCash(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "data_source", "member_id", "member_type_key", "mobile", "gold" });
		if (true) {
			throw new BusinessException(RspCode.EXCHANGE_FAIL, "暂未开放");
		}
	}

}
