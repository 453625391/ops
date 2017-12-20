package com.meitianhui.goods.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.goods.constant.Constant;
import com.meitianhui.goods.constant.GoodsIDUtil;
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.dao.ItemStoreDao;
import com.meitianhui.goods.entity.GdItemStore;
import com.meitianhui.goods.service.StoresGoodsService;

/**
 * 门店商品服务
 * 
 * @author Tiny
 *
 */
@Service
public class StoresGoodsServiceImpl implements StoresGoodsService {

	@Autowired
	private DocUtil docUtil;
	@Autowired
	public ItemStoreDao itemStoreDao;

	@Autowired
	public RedisUtil redisUtil;

	@Override
	public void goodsCodeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> resultDateMap = new HashMap<String, Object>();
		String goods_code = GoodsIDUtil.getGoodsCode(redisUtil);
		resultDateMap.put("goods_code", goods_code);
		result.setResultData(resultDateMap);
	}

	@Override
	public void storesGoodsCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "stores_id", "stores_name", "stores_service_tel", "goods_code", "barcode", "quick_code",
						"goods_name", "pic_detail_info", "category", "sale_unit", "purchase_unit", "min_pack_unit",
						"conversion_relation", "is_weighed", "cost_price", "market_price", "rebate", "discount",
						"gross_profit", "expired_warning", "stock_warning", "is_show", "is_activity", "is_sell",
						"is_recommend" });
		paramsMap.put("item_code", paramsMap.get("barcode"));
		paramsMap.put("item_name", paramsMap.get("stores_name"));
		paramsMap.put("store_id", paramsMap.get("stores_id"));
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("barcode", paramsMap.get("barcode"));
		tempMap.put("store_id", paramsMap.get("store_id"));
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(tempMap);
		if (gdItemStoreList.size() > 0) {
			throw new BusinessException(RspCode.ITEM_STORE_EXIST, "商品条码已存在");
		}
		Date date = new Date();
		GdItemStore gdItemStore = new GdItemStore();
		gdItemStore.setRebate(new BigDecimal(0)); // 返现金额默认设置为零
		BeanConvertUtil.mapToBean(gdItemStore, paramsMap);
		gdItemStore.setItem_store_id(IDUtil.getUUID());
		gdItemStore.setCategory_id(paramsMap.get("category") + "");
		gdItemStore.setImage_detail(paramsMap.get("pic_detail_info") + "");
		gdItemStore.setItem_code(paramsMap.get("goods_code") + "");
		gdItemStore.setItem_name(paramsMap.get("goods_name") + "");
		gdItemStore.setSpecification(paramsMap.get("sale_unit") + "");
		gdItemStore.setStore_info(paramsMap.get("stores_name") + "");
		gdItemStore.setWeight("0.00");
		gdItemStore.setSale_qty(0);
		gdItemStore.setStock_qty(0);
		gdItemStore.setStatus(Constant.STATUS_NORMAL);
		gdItemStore.setCreated_date(date);
		gdItemStore.setModified_date(date);
		gdItemStore.setProduct_source(Constant.GOODS_PRODUCT_SOURCE_ZIDINGYI);
		gdItemStore.setIs_exchange("N");
		gdItemStore.setIs_offline("N");
		gdItemStore.setIs_discount("Y");
		gdItemStore.setIs_track_stock("N");
		// TODO 临时数据
		gdItemStore.setImage_info(paramsMap.get("image_info") + "");

		itemStoreDao.insertGdItemStore(gdItemStore);
	}

	@Override
	public void storesGoodsDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_goods_id" });
		paramsMap.put("item_store_id", paramsMap.get("stores_goods_id") + "");
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(paramsMap);
		if (gdItemStoreList.size() == 0) {
			throw new BusinessException(RspCode.STORES_GOODS_ERROR, "商品不存在");
		}
		GdItemStore gdItemStore = gdItemStoreList.get(0);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("stores_goods_id", gdItemStore.getItem_store_id());
		tempMap.put("goods_code", gdItemStore.getItem_code());
		tempMap.put("goods_name", gdItemStore.getItem_name());
		tempMap.put("category", StringUtil.formatStr(gdItemStore.getCategory_id()));
		tempMap.put("barcode", gdItemStore.getBarcode());
		tempMap.put("sale_unit", StringUtil.formatStr(gdItemStore.getSpecification()));
		tempMap.put("rebate", gdItemStore.getRebate() + "");
		tempMap.put("manufacturer", StringUtil.formatStr(gdItemStore.getManufacturer()));
		tempMap.put("durability_period", StringUtil.formatStr(gdItemStore.getDurability_period()));
		tempMap.put("is_show", StringUtil.formatStr(gdItemStore.getIs_show()));
		tempMap.put("is_sell", StringUtil.formatStr(gdItemStore.getIs_sell()));
		tempMap.put("is_recommend", StringUtil.formatStr(gdItemStore.getIs_recommend()));
		tempMap.put("is_activity", StringUtil.formatStr(gdItemStore.getIs_activity()));
		tempMap.put("is_offline", StringUtil.formatStr(gdItemStore.getIs_offline()));
		tempMap.put("is_weighed", StringUtil.formatStr(gdItemStore.getIs_weighed()));
		tempMap.put("stock_warning", gdItemStore.getStock_warning() + "");
		tempMap.put("expired_warning", gdItemStore.getExpired_warning() + "");
		// TODO 临时解析数据
		if (StringUtil.isEmpty(gdItemStore.getImage_detail())) {
			List<Map<String, Object>> pic_detail_info_url = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> pic_detail_info = new ArrayList<Map<String, Object>>();
			if (StringUtil.isNotEmpty(gdItemStore.getImage_info())) {
				// 取原图片
				String path_url = docUtil.imageUrlFind(gdItemStore.getImage_info());
				Map<String, Object> pathUrlMap = new HashMap<String, Object>();
				pathUrlMap.put("path", path_url);
				pic_detail_info_url.add(pathUrlMap);
				Map<String, Object> pathMap = new HashMap<String, Object>();
				pathMap.put("path", path_url.replace(DocUtil.OSS_ACCESS_URL, ""));
				pic_detail_info.add(pathMap);
			}
			tempMap.put("pic_detail_info_url", FastJsonUtil.toJson(pic_detail_info_url));
			tempMap.put("pic_detail_info", FastJsonUtil.toJson(pic_detail_info));
		} else {
			tempMap.put("pic_detail_info", gdItemStore.getImage_detail());
			tempMap.put("pic_detail_info_url", docUtil.parseJsonPicPath(gdItemStore.getImage_detail(), "path"));
		}
		tempMap.put("cost_price", gdItemStore.getCost_price() + "");
		tempMap.put("market_price", gdItemStore.getMarket_price() + "");
		tempMap.put("sale_qty", gdItemStore.getSale_qty() + "");
		// TODO 未保存数据
		tempMap.put("gross_profit", "0.00");
		tempMap.put("discount", "0.00");
		if (gdItemStore.getItem_code().length() == 16) {
			tempMap.put("quick_code", gdItemStore.getItem_code().substring(gdItemStore.getItem_code().length() - 4));
		} else {
			tempMap.put("quick_code", "");
		}
		tempMap.put("purchase_unit", "");
		tempMap.put("min_pack_unit", "");
		tempMap.put("conversion_relation", "");
		tempMap.put("warehouse", "");

		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("detail", tempMap);
		result.setResultData(resultDataMap);
	}

	@Override
	public void storesGoodsSaleStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_goods_id", "is_offline" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("stores_goods_id", paramsMap.get("stores_goods_id"));
		tempMap.put("is_offline", paramsMap.get("is_offline"));
		itemStoreDao.updateGdItemStore(tempMap);
	}

	@Override
	public void storesGoodsEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_goods_id", "goods_name", "category", "barcode",
				"quick_code", "market_price", "sale_unit", "purchase_unit", "min_pack_unit", "conversion_relation" });
		String barcode = paramsMap.get("barcode") + "";
		String stores_id = paramsMap.get("stores_id") + "";
		String stores_goods_id = paramsMap.get("stores_goods_id") + "";
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("barcode", barcode);
		tempMap.put("store_id", stores_id);
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(tempMap);
		if (gdItemStoreList.size() > 0) {
			GdItemStore gdItemStore = gdItemStoreList.get(0);
			if (gdItemStore.getItem_store_id().equals(stores_goods_id)) {
				// 如果存在的条码对应的商品是当前商品,则不更新条码
				paramsMap.remove("barcode");
			} else {
				throw new BusinessException(RspCode.ITEM_STORE_EXIST, "商品条码已存在");
			}
		}
		paramsMap.put("item_store_id", stores_goods_id);
		paramsMap.put("image_detail", paramsMap.get("pic_detail_info") + "");
		paramsMap.put("item_name", paramsMap.get("goods_name") + "");
		paramsMap.put("category_id", paramsMap.get("category") + "");
		paramsMap.put("specification", paramsMap.get("sale_unit") + "");
		itemStoreDao.updateGdItemStore(paramsMap);
	}

	@Override
	public void stockReplenish(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_goods_id", "cost_price", "qty" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("item_store_id", paramsMap.get("stores_goods_id") + "");
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(tempMap);
		if (gdItemStoreList.size() == 0) {
			throw new BusinessException(RspCode.STORES_GOODS_ERROR, "商品不存在");
		}
		// 更新商品信息
		tempMap.clear();
		tempMap.put("item_store_id", paramsMap.get("stores_goods_id") + "");
		tempMap.put("cost_price", paramsMap.get("cost_price") + "");
		tempMap.put("production_date", StringUtil.formatStr(paramsMap.get("production_date")));
		tempMap.put("supplier", StringUtil.formatStr(paramsMap.get("supplier")));
		tempMap.put("category_id", StringUtil.formatStr(paramsMap.get("category")));
		Integer qty = Integer.parseInt(paramsMap.get("qty") + "");
		GdItemStore gdItemStore = gdItemStoreList.get(0);
		Integer sale_qty = gdItemStore.getSale_qty();
		Integer stock_qty = gdItemStore.getStock_qty();
		tempMap.put("stock_qty", sale_qty + qty);
		tempMap.put("sale_qty", stock_qty + qty);
		itemStoreDao.updateGdItemStore(tempMap);
	}

	@Override
	public void storesGoodsListForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		paramsMap.put("store_id", paramsMap.get("stores_id"));
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStoreList(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (GdItemStore gdItemStore : gdItemStoreList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("stores_goods_id", gdItemStore.getItem_store_id());
			tempMap.put("goods_name", gdItemStore.getItem_name());
			tempMap.put("category", StringUtil.formatStr(gdItemStore.getCategory_id()));
			tempMap.put("barcode", StringUtil.formatStr(gdItemStore.getBarcode()));
			tempMap.put("sale_unit", StringUtil.formatStr(gdItemStore.getSpecification()));
			tempMap.put("cost_price", gdItemStore.getCost_price() + "");
			tempMap.put("market_price", gdItemStore.getMarket_price() + "");
			tempMap.put("rebate", gdItemStore.getRebate() + "");
			tempMap.put("sale_qty", gdItemStore.getSale_qty() + "");
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void cashierGoodsListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		paramsMap.put("store_id", paramsMap.get("stores_id"));
		paramsMap.put("is_show", "Y");
		paramsMap.put("is_offline", "Y");
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStoreList(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (GdItemStore gdItemStore : gdItemStoreList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("stores_goods_id", gdItemStore.getItem_store_id());
			tempMap.put("goods_name", gdItemStore.getItem_name());
			tempMap.put("market_price", gdItemStore.getMarket_price() + "");
			if (StringUtil.isEmpty(gdItemStore.getImage_detail())) {
				// 取原图片
				String path_url = docUtil.imageUrlFind(gdItemStore.getImage_info());
				tempMap.put("path_url", path_url);
			} else {
				tempMap.put("path_url", docUtil.parseJsonFirstPicPath(gdItemStore.getImage_detail(), "path"));
			}
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void isSellGoodsCountForStoresSaleFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("is_sell_goods_count", "0");
		Map<String, Object> countsMap = itemStoreDao.selectIsSellGoodsCount(paramsMap);
		if (null != countsMap) {
			resultMap.put("is_sell_goods_count", countsMap.get("count_num") + "");
		}
		result.setResultData(resultMap);
	}

	@Override
	public void storesGoodsDelete(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_goods_id" });
		paramsMap.put("item_store_id", paramsMap.get("stores_goods_id"));
		itemStoreDao.deletedItemStore(paramsMap);
	}

}
