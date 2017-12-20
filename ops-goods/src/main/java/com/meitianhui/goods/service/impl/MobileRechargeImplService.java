package com.meitianhui.goods.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.dao.MobileRechargeDao;
import com.meitianhui.goods.service.MobileRechargeService;

/**
 * 话费充值商品
 * 
 * @author Tiny
 *
 */
@Service
public class MobileRechargeImplService implements MobileRechargeService {

	@Autowired
	public MobileRechargeDao mobileRechargeDao;

	/**
	 * 话费充值类型
	 * 
	 * @param mobiles
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public void mobileRechargeTypeListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "label"});
			List<Map<String, Object>> list = mobileRechargeDao.selectMobileRechargeType(paramsMap);
			for(Map<String, Object> map : list){
				map.put("market_price", map.get("market_price") + "");
				map.put("discount_price", map.get("discount_price") + "");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", list);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 话费充值类型
	 * 
	 * @param mobiles
	 * @param msg
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@Override
	public void mobileRechargeTypeDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "goods_id"});
			List<Map<String, Object>> list = mobileRechargeDao.selectMobileRechargeType(paramsMap);
			if(list.size() == 0){
				throw new BusinessException(RspCode.PS_GOODS_NOT_EXIST, "充值商品不存在");
			}
			Map<String, Object> detail = list.get(0);
			detail.put("market_price", detail.get("market_price") + "");
			detail.put("discount_price", detail.get("discount_price") + "");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("detail", detail);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	
	
}
