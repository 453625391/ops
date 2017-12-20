package com.meitianhui.schedule.task;

import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface GoodsTask {

	
	/**
	 * 刷新失效优惠券状态
	 */
	public void couponStatusRefresh() throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 活动揭晓
	 */
	public void ldActivitiesStatusRefresh() throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 商品浏览量保存到数据库
	 */
	public void goodsViewCountSave() throws BusinessException, SystemException, Exception;
	
	/**
	 * 刷新抽奖活动状态
	 */
	public void plActivityStatusRefresh() throws BusinessException, SystemException, Exception;
	
	/**
	 * 处理过期红包
	 */
	public void updateDisabledGcActivity() throws BusinessException, SystemException, Exception;
	
	/**
	 * 处理过期权益卷
	 */
	public void gdBenefitStatusRefresh() throws BusinessException, SystemException, Exception;
	
	/**
	 * 处理过期权益活动
	 */
	public void gdActivityStatusRefresh() throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 领了么商品码自动下架
	 */
	public void psGoodsAutoOffline() throws BusinessException, SystemException, Exception;
	
}
