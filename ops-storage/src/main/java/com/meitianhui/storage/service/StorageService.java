package com.meitianhui.storage.service;

import java.util.List;
import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

public interface StorageService {

	/**
	 * 文档上传
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleStorageUpload(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception;

//	/**
//	 * 文档预览
//	 * 
//	 * @param paramMap
//	 * @param result
//	 * @throws BusinessException
//	 * @throws SystemException
//	 */
//	public void preview(List<String> list, ResultData result) throws BusinessException, SystemException, Exception;
//
//	/**
//	 * 文档预览
//	 * 
//	 * @param paramMap
//	 * @param result
//	 * @throws BusinessException
//	 * @throws SystemException
//	 */
//	public void storagePreview(List<String> list, ResultData result) throws BusinessException, SystemException, Exception;

	/**
	 * 文档预览(缓存)
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void previewCache(List<String> list, ResultData result) throws BusinessException, SystemException, Exception;
	
	
	/**
	 * 文档预览(缓存)
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storagePreviewCache(List<String> list, ResultData result) throws BusinessException, SystemException, Exception;

}
