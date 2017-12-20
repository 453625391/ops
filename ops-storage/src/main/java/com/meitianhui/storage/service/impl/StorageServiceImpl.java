package com.meitianhui.storage.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.storage.constant.Constant;
import com.meitianhui.storage.dao.StorageDao;
import com.meitianhui.storage.entity.IdDocument;
import com.meitianhui.storage.service.StorageService;
import com.meitianhui.storage.util.OSSUtils;

/**
 * 文档服务的服务层
 * 
 * @author mole
 *
 */
@Service
public class StorageServiceImpl implements StorageService {
	private static final Logger logger = Logger.getLogger(StorageServiceImpl.class);

	@Autowired
	public StorageDao storageDao;

	@Autowired
	public RedisUtil redisUtil;

	@Override
	public void handleStorageUpload(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 文件后缀名
			String subffix = (String) paramsMap.get("subffix");
			String category = (String) paramsMap.get("category");
			String contentType = (String) paramsMap.get("contentType");
			InputStream inputStream = (InputStream) paramsMap.get("inputStream");
			String doc_id = IDUtil.getUUID();
			// 获取文档存储地址
			String dir = PropertiesConfigUtil.getProperty(category, Constant.OSS_DEFAULT_DIR);
			dir += DateUtil.date2Str(new Date(), "yyyyMM") + "/";
			String path = dir + doc_id + subffix;
			// 上传文件
			OSSUtils.OSSUpLoadFile(path, contentType, inputStream);
			// 上传成功后保存到数据库中
			IdDocument document = new IdDocument();
			document.setDoc_id(doc_id);
			document.setDoc_name((String) paramsMap.get("doc_name"));
			document.setDoc_type(category);
			document.setPath(path);
			document.setStatus("normal");
			document.setCreated_date(new Date());
			storageDao.insertDocument(document);
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("doc_id", doc_id);
			tempMap.put("path", path);
			tempMap.put("doc_path", OSSUtils.OSS_ACCESS_URL + path);
			result.setResultData(tempMap);
		} catch (Exception e) {
			throw e;
		}
	}

	
	@Override
	public void previewCache(List<String> list, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			for (String doc_id : list) {
				if (StringUtils.isEmpty(doc_id)) {
					continue;
				}
				//图片URL缓存key
				String key = DocUtil.CACHE_DOC_ID + doc_id;
				String doc_path = null;
				try {
					doc_path = redisUtil.getStr(key);
				} catch (Exception e) {
					logger.error("获取图片缓存异常", e);
				}
				if (StringUtils.isEmpty(doc_path)) {
					Map<String, Object> docMap = storageDao.selectDocumentById(doc_id);
					if (docMap == null) {
						continue;
					}
					doc_path = OSSUtils.OSS_ACCESS_URL + docMap.get("path") + "";
					try {
						redisUtil.setStr(key, doc_path, 604800);
					} catch (Exception e) {
						logger.error("缓存图片异常", e);
					}
					logger.info("init cache date; key->" + key + ",value->" + doc_path);
				}
				Map<String, String> tempMap = new HashMap<String, String>();
				if(StringUtil.isNotEmpty(doc_path)){
					tempMap.put("doc_id", doc_id);
					tempMap.put("doc_path", doc_path);
					resultList.add(tempMap);
				}
			}
			result.setResultData(resultList);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storagePreviewCache(List<String> list, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			Map<String, String> resultMap = new HashMap<String, String>();
			for (String doc_id : list) {
				if (StringUtils.isEmpty(doc_id)) {
					continue;
				}
				//图片URL缓存key
				String key = DocUtil.CACHE_DOC_ID + doc_id;
				String doc_path = null;
				try {
					doc_path = redisUtil.getStr(key);
				} catch (Exception e) {
					logger.error("获取图片缓存异常", e);
				}
				if (StringUtils.isEmpty(doc_path)) {
					Map<String, Object> docMap = storageDao.selectDocumentById(doc_id);
					if (docMap == null) {
						continue;
					}
					doc_path = OSSUtils.OSS_ACCESS_URL + docMap.get("path") + "";
					try {
						redisUtil.setStr(key, doc_path, 604800);
					} catch (Exception e) {
						logger.error("缓存图片异常", e);
					}
					logger.info("init cache date; key->" + key + ",value->" + doc_path);
				}
				if(StringUtil.isNotEmpty(doc_path)){
					resultMap.put(doc_id, doc_path);
				}
			}
			result.setResultData(resultMap);
		} catch (Exception e) {
			throw e;
		}
	}

}
