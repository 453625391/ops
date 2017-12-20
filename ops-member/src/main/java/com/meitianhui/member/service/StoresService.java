package com.meitianhui.member.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface StoresService {


	/**
	 * 门店信息同步
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleStoreSyncRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 惠易定门店信息同步(2.0)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleStoreSyncRegisterForHYD(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 惠易定门店信息同步(3.0)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleStoreSyncRegisterForHYD3(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 熟么门店信息同步
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleStoreSyncRegisterForShume(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店信息同步
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeSyncEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店资料更新
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店状态更新
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 惠商驿站查询
	 * @Title: stageStoresFind  
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void stageStoresFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 惠商驿站编辑
	* @Title: stageStoresEdit  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	public void stageStoresEdit(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 门店活动编辑
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleStoreActivityPaymentEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 获取门店的活动信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeActivityPaymentFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店登陆校验
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeLoginValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店查询(店东端查询)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店信息查询(所有门店信息)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 门店基本信息查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesBaseInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店查询(订单模块)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeForOrderFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店详情
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店详情查询(消费终端)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeDetailForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店List详情查询(门店优惠券活动查询)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeListForGoodsFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店列表查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询门店id
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeIdFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 通过手机号查询门店信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeFindByMobile(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	
	/**
	 * 附近惠商驿站
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyHSPostListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/***
	 * 门店营业信息查询(掌上超市信息)
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月28日
	 */
	public void storesBusinessInfoFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	

	
	/**
	 * 附近所有的门店信息（搞掂APP）
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyAllStoreForSalesassistantFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 附近所有的门店信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyAllStoreFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 附近加盟店信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyStoreFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询附近有一元购商品的门店
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyLDStoreFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询附近有团购预售商品的门店
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyPsGroupGoodsListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 查询门店附近优惠券信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyAllPropFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 通过登陆手机号熟么入住商户信息查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void shumeStoresFindByMobile(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 熟么入住商户信息查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void shumeStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 附近熟么入住商户信息查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyShumeStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	
	/**
	 * 推荐店东查询(本地生活首页)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesRecommendFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 推荐店东查询(for stores)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void recommendStoresListForGoodsFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 推荐门店查询(运营)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesRecommendForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 推荐联盟商同步
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleStoresRecommendSync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店助手信息更新
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeAssistantInfoSync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店助手信息查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeAssistantFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 门店助手信息清空
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeAssistantClear(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	
	
	/**
	 * 门店会员关系创建
	 * @Title: storesMemberRelCreate  
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void storesMemberRelCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 	业务员对应的拓店，助教门店数量查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void salesmanStoresNumFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
	/**
	 * 门店对应的会员资产列表分页查询
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void storesForConsumerAssetListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;
	
}
