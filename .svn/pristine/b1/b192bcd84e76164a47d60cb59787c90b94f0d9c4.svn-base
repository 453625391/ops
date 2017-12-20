package com.meitianhui.goods.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface GoodsService {

	/**
	 * 优惠券发行申请
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleCouponIssueApply(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 优惠券状态编辑
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void sysitemItemStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询分享的优惠券信息和优惠券对应的门店信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void couponAndStoreFindForShare(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 消费者端查询自己购买的优惠券信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void couponDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询推荐商家的商品列表
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void recommendStoresGoodsListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询商家的优惠券详情列表(消费者端)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void couponListFindForConsumer(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询门店的优惠券列表(店东端)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void couponListFindForStores(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询商家的优惠券列表的详情(消费者端)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void couponDetailForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询商家的优惠券列表的详情(店东端)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void couponDetailForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 消费者免费领取优惠券
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void couponFree(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 给消费者派发优惠券
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleCouponPresented(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 消费者购买优惠券
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleCouponCreateNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 优惠券验证查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void couponValidateFindForStores(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	/**
	 * 优惠券验证
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleCouponValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询门店是否有优惠券商品
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void storesCouponIsExist(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 店东活动统计
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void storesActivityCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 店东优惠券总数统计
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void storesCouponTotalFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 店东发行的优惠券商品统计
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void storesCouponCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 广告信息编辑
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void gdAdvertEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 广告信息查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void gdAdvertFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 单人送红包
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void memberGiftCardSend(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 批量送红包
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void memberGiftCardBatchSend(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询会员可抽奖的次数
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void handleMemberLotteryNumFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询门店优惠券信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void storesCouponProp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

}
