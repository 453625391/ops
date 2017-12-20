package com.meitianhui.member.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface SupplierService {

	
	/**
	 * 供应商信息同步
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleSupplierSyncRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 供应商资料更新
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleSupplierSyncEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 供应商信息同步(惠易定2.0)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleSupplierSyncRegisterForHYD(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 供应商信息同步(惠易定3.0)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleSupplierSyncRegisterForHYD3(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	

	/**
	 * 供应商查询
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void supplierFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 供应商促销信息(多个供应商)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void supplierListPromotionFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 供应商登陆校验
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void supplierLoginValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

	/**
	 * 供应商状态更新
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void supplierStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

}
