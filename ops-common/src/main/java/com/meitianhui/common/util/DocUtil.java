package com.meitianhui.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meitianhui.common.constant.CommonRspCode;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

@SuppressWarnings("unchecked")
@Component("docUtil")
public class DocUtil {

	private static final Logger logger = Logger.getLogger(DocUtil.class);

	/** 单个图片URl缓存标示 **/
	public static String CACHE_DOC_ID = "[cache_doc_id]_";
	/** 多个图片URl缓存标示 **/
	public static String CACHE_DOC_ID_LIST = "[cache_doc_id_list]_";
	/** 图片url地址 **/
	public static String OSS_ACCESS_URL = "https://oss-img.meitianhui.com/";

	@Autowired
	public RedisUtil redisUtil;

	/**
	 * 获取图片
	 * 
	 * @param list
	 * @return
	 */
	public Map<String, Object> imageUrlFind(List<String> list) {
		Map<String, Object> url_map = new HashMap<String, Object>();
		try {
			// doc_id列表，去除list中为空串的数据
			List<String> docIdList = new ArrayList<String>();
			for (String docId : list) {
				if (StringUtil.isNotEmpty(docId)) {
					docIdList.add(docId);
				}
			}
			if (docIdList.size() > 0) {
				String storage_service_url = PropertiesConfigUtil.getProperty("storage_service_url") + "/previewOps";
				Map<String, String> reqParams = new HashMap<String, String>();
				// 解析图片
				String doc_ids = StringUtil.list2Str(docIdList);
				reqParams.put("doc_ids", doc_ids);
				String resultStr = HttpClientUtil.postShort(storage_service_url, reqParams);
				Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
				String rsp_code = (String) resultMap.get("rsp_code");
				if (!rsp_code.equals(CommonRspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				url_map = (Map<String, Object>) resultMap.get("data");
			}
		} catch (BusinessException e) {
			logger.error("获取图片url异常->" + e.getMsg());
		} catch (SystemException e) {
			logger.error("获取图片url异常", e);
		} catch (Exception e) {
			logger.error("获取图片url异常", e);
		}
		return url_map;
	}

	/**
	 * 获取图片
	 * 
	 * @param doc_ids
	 * @return
	 */
	public String imageUrlFind(String doc_id) {
		String doc_path = null;
		try {
			if (StringUtil.isNotEmpty(doc_id)) {
				// 查询没有在缓存不存在的图片url
				String key = CACHE_DOC_ID + doc_id;
				try {
					doc_path = redisUtil.getStr(key);
				} catch (Exception e) {
					logger.error("获取缓存图片异常", e);
				}
				if (StringUtil.isEmpty(doc_path)) {
					String storage_service_url = PropertiesConfigUtil.getProperty("storage_service_url")
							+ "/previewOps";
					Map<String, String> reqParams = new HashMap<String, String>();
					// 解析图片
					reqParams.put("doc_ids", doc_id);
					String resultStr = HttpClientUtil.postShort(storage_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					String rsp_code = (String) resultMap.get("rsp_code");
					if (!rsp_code.equals(CommonRspCode.RESPONSE_SUCC)) {
						throw new BusinessException((String) resultMap.get("error_code"),
								(String) resultMap.get("error_msg"));
					}
					Map<String, Object> qry_map = (Map<String, Object>) resultMap.get("data");
					// 将查询的图片url追加到返回的结果集中
					doc_path = StringUtil.formatStr(qry_map.get(doc_id));
				}
			}
		} catch (BusinessException e) {
			logger.error("获取图片url异常->" + e.getMsg());
		} catch (SystemException e) {
			logger.error("获取图片url异常", e);
		} catch (Exception e) {
			logger.error("获取图片url异常", e);
		}
		return doc_path;
	}

	/**
	 * 获取图片url
	 * 
	 * @param path
	 * @return
	 */
	public String picPathUrlFind(String path) {
		return OSS_ACCESS_URL + path;
	}

	/**
	 * 解析json格式的图片
	 * 
	 * @Title: parseJsonPicPath
	 * @param jsonStr
	 * @param pathName
	 * @return
	 * @author tiny
	 */
	public String parseJsonPicPath(String jsonStr, String pathName) {
		List<Map<String, Object>> picList = new ArrayList<Map<String, Object>>();
		if (StringUtils.isNotEmpty(jsonStr)) {
			try {
				List<Map<String, Object>> jsonList = FastJsonUtil.jsonToList(jsonStr);
				for (Map<String, Object> pic : jsonList) {
					String path = StringUtil.formatStr(pic.get(pathName));
					if (StringUtil.isNotEmpty(path)) {
						pic.put(pathName, OSS_ACCESS_URL + path);
					}
					picList.add(pic);
				}
			} catch (Exception e) {
				logger.error("图片解析异常", e);
			}
		}
		return FastJsonUtil.toJson(picList);
	}

	/**
	 * 解析json格式的图片(获取第一张图片)
	 * 
	 * @Title: parseJsonFirstPicPath
	 * @param jsonStr
	 * @param pathName
	 * @return
	 * @author tiny
	 */
	public String parseJsonFirstPicPath(String jsonStr, String pathName) {
		String path_url = "";
		if (StringUtils.isNotEmpty(jsonStr)) {
			try {
				List<Map<String, Object>> jsonList = FastJsonUtil.jsonToList(jsonStr);
				for (Map<String, Object> pic : jsonList) {
					String path = StringUtil.formatStr(pic.get(pathName));
					if (StringUtil.isNotEmpty(path)) {
						path_url = OSS_ACCESS_URL + path;
						break;
					}
				}
			} catch (Exception e) {
				logger.error("图片解析异常", e);
			}
		}
		return path_url;
	}

}
