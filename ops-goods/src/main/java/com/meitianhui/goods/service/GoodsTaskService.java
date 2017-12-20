package com.meitianhui.goods.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface GoodsTaskService {
	/**
	 * 刷新失效优惠券状态
	 */
	public void couponStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;;

	/**
	 * 活动揭晓
	 */
	public void ldActivitiesStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;;

	/**
	 * 同步商品流量数量
	 */
	public void goodsViewCountSave(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;;

	/**
	 * 处理过期红包
	 */
	public void updateDisabledGcActivity(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;;

	/**
	 * 抽奖活动状态更新
	 */
	public void plActivityStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;;

	/**
	 * 权益劵状态刷新
	 * 
	 * @Title: gdBenefitStatusRefresh
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void gdBenefitStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;;

	/**
	 * 权益活动状态刷新
	 * 
	 * @Title: gdBenefitStatusRefresh
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void gdActivityStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;;

	/**
	 * 商品自动下架
	 * 
	 * @Title: psGoodsAutoOffline
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	void psGoodsAutoOffline(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

}
