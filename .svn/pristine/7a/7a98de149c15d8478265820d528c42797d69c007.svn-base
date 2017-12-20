package com.meitianhui.goods.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.goods.dao.PsGoodsFavoritesDao;
import com.meitianhui.goods.entity.PsGoodsFavorites;
import com.meitianhui.goods.service.PsGoodsFavoritesService;

/**
 * 商品收藏
 * 
 * @ClassName: PsGoodsFavoritesServiceImpl
 * @author tiny
 * @date 2017年4月6日 下午5:01:57
 *
 */
@SuppressWarnings("unchecked")
@Service
public class PsGoodsFavoritesServiceImpl implements PsGoodsFavoritesService {

	@Autowired
	private DocUtil docUtil;
	@Autowired
	private PsGoodsFavoritesDao psGoodsFavoritesDao;

	@Override
	public void psGoodsFavoritesCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "favorites_type", "goods_id", "member_type_key", "member_id" });
			PsGoodsFavorites psGoodsFavorites = psGoodsFavoritesDao.selectPsGoodsFavorites(paramsMap);
			if (psGoodsFavorites == null) {
				psGoodsFavorites = new PsGoodsFavorites();
				BeanConvertUtil.mapToBean(psGoodsFavorites, paramsMap);
				Date date = new Date();
				psGoodsFavorites.setCreated_date(date);
				psGoodsFavoritesDao.insertPsGoodsFavorites(psGoodsFavorites);
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void psGoodsFavoritesCancel(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "goods_id", "member_id", "member_type_key" });
			psGoodsFavoritesDao.deletePsGoodsFavorites(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void fgGoodsFavoritesListForAppFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			List<Map<String, Object>> fgGoodsList = psGoodsFavoritesDao.selectFgGoodsFavoritesListForApp(paramsMap);
			for (Map<String, Object> map : fgGoodsList) {
				map.put("goods_id", map.get("goods_id") + "");
				map.put("market_price", map.get("market_price") + "");
				map.put("discount_price", map.get("discount_price") + "");
				map.put("sale_qty", map.get("sale_qty") + "");
				String pic_info = map.get("pic_info") + "";
				if (StringUtils.isNotEmpty(pic_info)) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							String path_id = m.get("path_id") + "";
							String pic_info_url = docUtil.imageUrlFind(path_id);
							map.put("pic_info_url", pic_info_url);
							break;
						}
					}
				}
			}
			Map<String, Object> resultDataMap = new HashMap<String, Object>();
			resultDataMap.put("list", fgGoodsList);
			result.setResultData(resultDataMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void tsGoodsFavoritesListForAppFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			List<String> doc_ids = new ArrayList<String>();
			List<Map<String, Object>> fgGoodsList = psGoodsFavoritesDao.selectTsGoodsFavoritesListForApp(paramsMap);
			for (Map<String, Object> map : fgGoodsList) {
				map.put("goods_id", map.get("goods_id") + "");
				map.put("title", map.get("title") + "");
				map.put("label", map.get("label") + "");
				map.put("market_price", map.get("market_price") + "");
				map.put("discount_price", map.get("discount_price") + "");
				map.put("ts_min_num", map.get("ts_min_num") + "");
				map.put("ts_price", map.get("ts_price") + "");
				map.put("sale_qty", map.get("sale_qty") + "");
				String pic_info = map.get("pic_info") + "";
				if (StringUtils.isNotEmpty(pic_info)) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
					}
				}
			}
			Map<String, Object> resultDataMap = new HashMap<String, Object>();
			resultDataMap.put("list", fgGoodsList);
			resultDataMap.put("doc_url", docUtil.imageUrlFind(doc_ids));
			result.setResultData(resultDataMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
