package com.meitianhui.goods.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/**
 * 店东收银商品
 * @author Tiny
 *
 */
public interface ItemStoreService {
	
	/**
	 * 商品新增
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 商品新增（快捷）
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreQuickCreate(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	

	/**
	 * 我要批商品导入到店东商品列表中
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void handlePsGoodsImport(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 商品编辑
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreEdit(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 标准商品查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 门店商品信息(订单)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreForOrderFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 门店商品信息(本地生活)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreForConsumerListFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;

	/**
	 * 门店商品信息(店东助手)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreForStoresListFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;

	
	/**
	 * 门店商品分组类别查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreGroupTypeFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;

	
	/**
	 * 门店商品分组类别查询(本地生活)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreGroupTypeForConsumerFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 便利店商品详情查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreDetailFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 精选特卖可销售数量恢复
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreSaleQtyRestore(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 精选特卖商品可销售数量扣减
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreSaleQtyDeduction(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	/**
	 * 查询门店是否有本地兑商品
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void storesExchangeFind(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 删除商品
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	void itemStoreDeleted(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception;
	
}

