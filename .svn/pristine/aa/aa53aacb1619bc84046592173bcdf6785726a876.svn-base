package com.meitianhui.goods.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface LdActivityService {
	

	/**
	 * 定时开活动创建
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void ldActivityCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 摇一摇抽奖活动创建
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月9日
	 */
	void ldYyyActivityCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 刮刮乐抽奖活动创建
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月9日
	 */
	void ldGglActivityCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 摇一摇抽奖机会获取，系统返回中奖号码与随机列表
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月10日
	 */
	void ldYyyActivityLuckDrawFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 刮刮乐抽奖机会获取
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月10日
	 */
	void ldGglActivityLuckDrawFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 抽奖活动中奖
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月10日
	 */
	void ldActivtiyWinning(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 查询附近有抽奖活动的门店
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月10日
	 */
	void nearbyLdActivityStoreFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 定时开商品查询(会员服务调用)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void ldActivityForMemberFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 定时开活动详情查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void ldActivityFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 定时开商品列表查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void ldActivityListFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 店东发起的活动列表
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月10日
	 */
	void ldActivityForStoreListFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 定时开商品抽奖记录查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void ldActivityProcessForStoreFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 定时开商品抽奖记录查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void ldActivityProcessForConsumerFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 消费者端获取门店正在进行活动的列表
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月15日
	 */
	void ldActivityListByStoresForConsumerFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/***
	 * 抽奖活动参与的消费者列表
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date   2016年12月10日
	 */
	void ldActivityConsumerListFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 一元抽奖活动信息验证查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void ldActivityValidateFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 一元抽奖活动验证
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleLdActivityValidate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 定时开活动取消
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handleLdActivityCancel(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 一元抽奖获取支付信息
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void ldActivityPayInfoFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 创建定时开抽奖号码
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void dskActivityProcessCreateNotify(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 创建摇一摇抽奖号码
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void yyyActivityProcessCreateNotify(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 创建刮刮乐抽奖号码
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void gglActivityProcessCreateNotify(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	
	
}

