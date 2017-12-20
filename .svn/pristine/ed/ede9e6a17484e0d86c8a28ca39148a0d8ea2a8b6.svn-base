package com.meitianhui.goods.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.goods.constant.Constant;
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.dao.GoodsDao;
import com.meitianhui.goods.dao.ItemStoreDao;
import com.meitianhui.goods.dao.PsGoodsDao;
import com.meitianhui.goods.entity.GdItem;
import com.meitianhui.goods.entity.GdItemStore;
import com.meitianhui.goods.entity.PsGoods;
import com.meitianhui.goods.service.ItemStoreService;

/**
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
@Service
public class ItemStoreServiceImpl implements ItemStoreService {

	@Autowired
	private DocUtil docUtil;
	@Autowired
	public ItemStoreDao itemStoreDao;

	@Autowired
	public GoodsDao goodsDao;

	@Autowired
	public PsGoodsDao psGoodsDao;

	@Override
	public void itemStoreCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "store_id", "stores_name", "item_name", "barcode",
				"cost_price", "market_price", "vip_price", "is_show", "is_sell" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("barcode", paramsMap.get("barcode"));
		tempMap.put("store_id", paramsMap.get("store_id"));
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(tempMap);
		if (gdItemStoreList.size() > 0) {
			throw new BusinessException(RspCode.ITEM_STORE_EXIST, "商品条码已存在");
		}
		String stock_qty = StringUtil.formatStr(paramsMap.get("stock_qty"));
		String sale_qty = StringUtil.formatStr(paramsMap.get("sale_qty"));
		if (!StringUtils.isEmpty(sale_qty)) {
			paramsMap.put("stock_qty", sale_qty);
		} else {
			paramsMap.put("sale_qty", stock_qty);
		}
		String expired_warning = paramsMap.get("expired_warning") + "";
		if (StringUtils.isEmpty(expired_warning)) {
			paramsMap.put("expired_warning", "0");
		} else {
			if (expired_warning.equals("Y") || expired_warning.equals("N")) {
				paramsMap.put("expired_warning", "0");
			}
		}

		Date date = new Date();
		GdItemStore gdItemStore = new GdItemStore();
		gdItemStore.setRebate(new BigDecimal(0)); // 返现金额默认设置为零
		BeanConvertUtil.mapToBean(gdItemStore, paramsMap);
		gdItemStore.setItem_store_id(IDUtil.getUUID());
		gdItemStore.setStatus(Constant.STATUS_NORMAL);
		gdItemStore.setCreated_date(date);
		gdItemStore.setModified_date(date);
		gdItemStore.setProduct_source(Constant.GOODS_PRODUCT_SOURCE_ZIDINGYI);
		gdItemStore.setIs_exchange("N");
		if (null == gdItemStore.getItem_code()) {
			gdItemStore.setItem_code(gdItemStore.getBarcode());
		}
		if (null == gdItemStore.getIs_activity()) {
			gdItemStore.setIs_activity("N");
		}
		if (null == gdItemStore.getIs_track_stock()) {
			gdItemStore.setIs_track_stock("N");
		}
		if (null == gdItemStore.getStock_warning()) {
			gdItemStore.setStock_warning(0);
		}
		if (null == gdItemStore.getIs_discount()) {
			gdItemStore.setIs_discount("N");
		}
		if (null == gdItemStore.getIs_offline()) {
			gdItemStore.setIs_offline("N");
		}
		if (null == gdItemStore.getIs_weighed()) {
			gdItemStore.setIs_weighed("N");
		}
		if (null == gdItemStore.getIs_recommend()) {
			gdItemStore.setIs_recommend("N");
		}
		itemStoreDao.insertGdItemStore(gdItemStore);
	}

	@Override
	public void itemStoreQuickCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "store_id", "stores_name", "item_name", "barcode", "market_price", "stock_qty" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("barcode", paramsMap.get("barcode"));
		tempMap.put("store_id", paramsMap.get("store_id"));
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(tempMap);
		if (gdItemStoreList.size() > 0) {
			throw new BusinessException(RspCode.ITEM_STORE_EXIST, "商品条码已存在");
		}
		paramsMap.put("sale_qty", paramsMap.get("stock_qty"));
		paramsMap.put("cost_price", paramsMap.get("market_price"));
		paramsMap.put("vip_price", paramsMap.get("market_price"));
		Date date = new Date();
		GdItemStore gdItemStore = new GdItemStore();
		BeanConvertUtil.mapToBean(gdItemStore, paramsMap);
		gdItemStore.setItem_store_id(IDUtil.getUUID());
		gdItemStore.setStatus(Constant.STATUS_NORMAL);
		gdItemStore.setCreated_date(date);
		gdItemStore.setModified_date(date);
		gdItemStore.setIs_show("N");
		gdItemStore.setIs_sell("N");
		gdItemStore.setIs_exchange("N");
		gdItemStore.setItem_code(gdItemStore.getBarcode());
		gdItemStore.setIs_activity("N");
		gdItemStore.setIs_track_stock("N");
		gdItemStore.setStock_warning(0);
		gdItemStore.setIs_discount("N");
		gdItemStore.setIs_offline("N");
		gdItemStore.setIs_weighed("N");
		gdItemStore.setIs_recommend("N");
		gdItemStore.setExpired_warning("0");
		gdItemStore.setProduct_source(Constant.GOODS_PRODUCT_SOURCE_ZIDINGYI);
		gdItemStore.setCategory_id("其它");
		gdItemStore.setRebate(new BigDecimal(0));
		gdItemStore.setStore_info(StringUtil.formatStr(paramsMap.get("stores_name")));
		itemStoreDao.insertGdItemStore(gdItemStore);
	}

	@Override
	public void itemFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "barcode" });
		GdItem gdItem = itemStoreDao.selectGdItem(paramsMap);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (gdItem != null) {
			resultMap.put("item_id", gdItem.getItem_id());
			resultMap.put("item_code", gdItem.getItem_code());
			resultMap.put("item_name", gdItem.getItem_name());
			resultMap.put("keywords", StringUtil.formatStr(gdItem.getKeywords()));
			resultMap.put("category_id", gdItem.getCategory_id());
			resultMap.put("brand_id", gdItem.getBrand_id());
			resultMap.put("barcode", gdItem.getBarcode());
			resultMap.put("desc1", StringUtil.formatStr(gdItem.getDesc1()));
			resultMap.put("specification", StringUtil.formatStr(gdItem.getSpecification()));
			resultMap.put("pack", StringUtil.formatStr(gdItem.getPack()));
			resultMap.put("cost_price", gdItem.getCost_price().toString());
			resultMap.put("market_price", gdItem.getMarket_price().toString());
			resultMap.put("vip_price", gdItem.getVip_price().toString());
			resultMap.put("producer", StringUtil.formatStr(gdItem.getProducer()));
			resultMap.put("supplier", StringUtil.formatStr(gdItem.getSupplier()));
			resultMap.put("manufacturer", StringUtil.formatStr(gdItem.getManufacturer()));
		}
		result.setResultData(resultMap);
	}

	@Override
	public void handlePsGoodsImport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "store_id", "goods_code", "qty", "product_source", "remark" });
		String goods_code = paramsMap.get("goods_code") + "";
		String store_id = paramsMap.get("store_id") + "";
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("barcode", goods_code);
		tempMap.put("store_id", store_id);
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(tempMap);
		Integer qty = Integer.parseInt(paramsMap.get("qty") + "");
		if (gdItemStoreList.size() > 0) {
			tempMap.clear();
			// 更新商品库存
			Integer new_sale_qty = gdItemStoreList.get(0).getSale_qty() + qty;
			Integer new_stock_qty = gdItemStoreList.get(0).getStock_qty() + qty;
			tempMap.put("sale_qty", new_sale_qty);
			tempMap.put("stock_qty", new_stock_qty);
			tempMap.put("item_store_id", gdItemStoreList.get(0).getItem_store_id());
			itemStoreDao.updateGdItemStore(tempMap);
		} else {
			GdItemStore gdItemStore = new GdItemStore();
			gdItemStore.setItem_store_id(IDUtil.getUUID());
			// 手动补全门店信息
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			reqParams.put("service", "member.storeForOrderFind");
			paramMap.put("stores_id", store_id);
			reqParams.put("params", FastJsonUtil.toJson(paramMap));
			String resultStr = HttpClientUtil.post(member_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> storeInfo = (Map<String, Object>) resultMap.get("data");
			gdItemStore.setStore_info(StringUtil.formatStr(storeInfo.get("stores_name")));
			Date date = new Date();
			// 设置商品信息
			tempMap.clear();
			tempMap.put("goods_code", goods_code);
			List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(tempMap);
			PsGoods psGoods = psGoodsList.get(0);
			gdItemStore.setStore_id(store_id);
			gdItemStore.setBarcode(psGoods.getGoods_code());
			gdItemStore.setItem_name(psGoods.getTitle());
			gdItemStore.setItem_code(psGoods.getGoods_code());
			gdItemStore.setCost_price(psGoods.getMarket_price());
			gdItemStore.setMarket_price(psGoods.getMarket_price());
			gdItemStore.setVip_price(psGoods.getMarket_price());
			gdItemStore.setRebate(new BigDecimal(0));
			gdItemStore.setSpecification(psGoods.getSpecification());
			gdItemStore.setPack(psGoods.getPack());
			gdItemStore.setSupplier(psGoods.getSupplier());
			gdItemStore.setManufacturer(psGoods.getManufacturer());
			gdItemStore.setProducer(psGoods.getProducer());
			gdItemStore.setSale_qty(qty);
			gdItemStore.setStock_qty(qty);
			gdItemStore.setStatus(Constant.STATUS_NORMAL);
			gdItemStore.setCreated_date(date);
			gdItemStore.setProduction_date(date);
			gdItemStore.setModified_date(date);
			gdItemStore.setCategory_id("其它");
			gdItemStore.setIs_show("Y");
			gdItemStore.setIs_sell("N");
			gdItemStore.setIs_exchange("N");
			gdItemStore.setIs_activity("N");
			gdItemStore.setIs_track_stock("N");
			gdItemStore.setIs_discount("N");
			gdItemStore.setIs_offline("N");
			gdItemStore.setIs_weighed("N");
			gdItemStore.setIs_recommend("N");
			gdItemStore.setIs_track_stock("N");
			gdItemStore.setExpired_warning("0");
			gdItemStore.setStock_warning(0);
			gdItemStore.setProduct_source(paramsMap.get("product_source") + "");
			gdItemStore.setRemark(paramsMap.get("remark") + "");
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			String image_info = "";
			if (!StringUtils.isEmpty(pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				Map<String, Object> picMap = null;
				if (tempList.size() > 0) {
					picMap = tempList.get(0);
				}
				if (null != picMap) {
					if (!StringUtil.formatStr(picMap.get("path_id")).equals("")) {
						image_info = picMap.get("path_id") + "";
					}
				}
			}
			gdItemStore.setImage_info(image_info);
			itemStoreDao.insertGdItemStore(gdItemStore);
		}
	}

	@Override
	public void itemStoreEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "item_store_id" });
		if (paramsMap.size() == 1) {
			throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE,
					RspCode.MSG.get(RspCode.SYSTEM_NO_PARAMS_UPDATE));
		}
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("item_store_id", paramsMap.get("item_store_id"));
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(tempMap);
		if (gdItemStoreList.size() == 0) {
			throw new BusinessException(RspCode.ITEM_STORE_NOT_EXIST, RspCode.MSG.get(RspCode.ITEM_STORE_NOT_EXIST));
		}
		GdItemStore gdItemStore = gdItemStoreList.get(0);
		String barcode = StringUtil.formatStr(paramsMap.get("barcode"));
		// 如果商品码不为空，且商品码不等于数据库中的商品码，验证商品是否唯一
		if (StringUtils.isNotBlank(barcode) && !barcode.equals(gdItemStore.getBarcode())) {
			tempMap.clear();
			tempMap.put("barcode", paramsMap.get("barcode"));
			tempMap.put("store_id", paramsMap.get("store_id"));
			gdItemStoreList = itemStoreDao.selectGdItemStore(tempMap);
			if (gdItemStoreList.size() > 0) {
				throw new BusinessException(RspCode.ITEM_STORE_EXIST, "商品条码已存在");
			}
		}
		String sale_qty = StringUtil.formatStr(paramsMap.get("sale_qty"));
		String stock_qty = StringUtil.formatStr(paramsMap.get("stock_qty"));
		if (!StringUtils.isEmpty(sale_qty)) {
			// 计算差值
			Integer num = Integer.parseInt(sale_qty) - gdItemStore.getSale_qty();
			// 计算总库存量
			Integer new_stock_qty = gdItemStore.getStock_qty() + num;
			paramsMap.put("stock_qty", new_stock_qty);
		} else {
			// 计算差值
			Integer num = Integer.parseInt(stock_qty) - gdItemStore.getStock_qty();
			// 计算可销售存量
			Integer new_sale_qty = gdItemStore.getSale_qty() + num;
			paramsMap.put("sale_qty", new_sale_qty);
		}

		String expired_warning = StringUtil.formatStr(paramsMap.get("expired_warning"));
		if (StringUtils.isEmpty(expired_warning)) {
			paramsMap.put("expired_warning", "0");
		} else {
			if (expired_warning.equals("Y") || expired_warning.equals("N")) {
				paramsMap.put("expired_warning", "0");
			}
		}
		itemStoreDao.updateGdItemStore(paramsMap);
	}

	@Override
	public void itemStoreSaleQtyDeduction(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "item_store_id", "sell_qty" });
		List<GdItemStore> itemStoreList = itemStoreDao.selectGdItemStore(paramsMap);
		if (itemStoreList.size() == 0) {
			throw new BusinessException(RspCode.ITEM_STORE_NOT_EXIST, RspCode.MSG.get(RspCode.ITEM_STORE_NOT_EXIST));
		}
		int updateFlag = itemStoreDao.updateGdItemStoreSaleQtyDeduction(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, "可销售库存不足");
		}
	}

	@Override
	public void itemStoreSaleQtyRestore(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "item_store_id", "restore_qty" });
		List<GdItemStore> itemStoreList = itemStoreDao.selectGdItemStore(paramsMap);
		if (itemStoreList.size() == 0) {
			throw new BusinessException(RspCode.ITEM_STORE_NOT_EXIST, RspCode.MSG.get(RspCode.ITEM_STORE_NOT_EXIST));
		}
		int updateFlag = itemStoreDao.updateGdItemStoreSaleQtyRestore(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, "系统繁忙,请重新操作");
		}
	}

	@Override
	public void itemStoreGroupTypeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> groupMap = new LinkedHashMap<String, Object>();
		groupMap.put("工艺", "工艺");
		groupMap.put("美食", "美食");
		groupMap.put("日用", "日用");
		groupMap.put("家居", "家居");
		groupMap.put("饮料", "饮料");
		groupMap.put("酒水", "酒水");
		groupMap.put("文体", "文体");
		groupMap.put("母婴", "母婴");
		groupMap.put("玩具", "玩具");
		groupMap.put("数码", "数码");
		groupMap.put("箱包", "箱包");
		groupMap.put("服饰", "服饰");
		groupMap.put("建材", "建材");
		groupMap.put("五金", "五金");
		groupMap.put("健康", "健康");
		groupMap.put("滋补", "滋补");
		groupMap.put("计生", "计生");
		groupMap.put("桶装水", "桶装水");
		groupMap.put("瓶装水", "瓶装水");
		groupMap.put("水设备", "水设备");
		groupMap.put("粮食", "粮食");
		groupMap.put("生鲜", "生鲜");
		groupMap.put("干货", "干货");
		groupMap.put("种苗", "种苗");
		groupMap.put("花卉", "花卉");
		groupMap.put("中药材", "中药材");
		groupMap.put("农机", "农机");
		groupMap.put("农药", "农药");
		groupMap.put("化肥", "化肥");
		groupMap.put("其它", "其它");
		result.setResultData(groupMap);
	}

	@Override
	public void itemStoreGroupTypeForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "store_id" });
		List<Map<String, Object>> groupTypeList = itemStoreDao.selectGdItemStoreTypeGroup(paramsMap);
		Map<String, Object> groupTypeMap = new LinkedHashMap<String, Object>();
		for (Map<String, Object> map : groupTypeList) {
			groupTypeMap.put(map.get("category_id") + "", map.get("category_id"));
		}
		result.setResultData(groupTypeMap);
	}

	@Override
	public void itemStoreDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParamsNum(paramsMap, new String[] { "item_store_id", "store_id"}, 1);
		
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(paramsMap);
		if (gdItemStoreList.size() == 0) {
			throw new BusinessException(RspCode.ITEM_STORE_NOT_EXIST, RspCode.MSG.get(RspCode.ITEM_STORE_NOT_EXIST));
		}
		List<String> doc_ids = new ArrayList<String>();
		GdItemStore gdItemStore = gdItemStoreList.get(0);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("item_store_id", gdItemStore.getItem_store_id());
		tempMap.put("item_id", StringUtil.formatStr(gdItemStore.getItem_id()));
		tempMap.put("item_code", gdItemStore.getItem_code());
		tempMap.put("item_name", gdItemStore.getItem_name());
		tempMap.put("category_id", StringUtil.formatStr(gdItemStore.getCategory_id()));
		tempMap.put("brand_id", StringUtil.formatStr(gdItemStore.getBrand_id()));
		tempMap.put("barcode", gdItemStore.getBarcode());
		tempMap.put("desc1", StringUtil.formatStr(gdItemStore.getDesc1()));
		tempMap.put("image_info", StringUtil.formatStr(gdItemStore.getImage_info()));
		tempMap.put("image_detail", StringUtil.formatStr(gdItemStore.getImage_detail()));
		tempMap.put("specification", StringUtil.formatStr(gdItemStore.getSpecification()));
		tempMap.put("pack", StringUtil.formatStr(gdItemStore.getPack()));
		tempMap.put("weight", StringUtil.formatStr(gdItemStore.getWeight()));
		tempMap.put("cost_price", gdItemStore.getCost_price() + "");
		tempMap.put("market_price", gdItemStore.getMarket_price() + "");
		tempMap.put("vip_price", gdItemStore.getVip_price() + "");
		tempMap.put("rebate", gdItemStore.getRebate() + "");
		tempMap.put("sale_qty", gdItemStore.getSale_qty() + "");
		tempMap.put("stock_qty", gdItemStore.getStock_qty() + "");
		tempMap.put("supplier", StringUtil.formatStr(gdItemStore.getSupplier()));
		tempMap.put("manufacturer", StringUtil.formatStr(gdItemStore.getManufacturer()));
		tempMap.put("production_date", DateUtil.date2Str(gdItemStore.getProduction_date(), DateUtil.fmt_yyyyMMdd));
		tempMap.put("durability_period", StringUtil.formatStr(gdItemStore.getDurability_period()));
		tempMap.put("is_show", StringUtil.formatStr(gdItemStore.getIs_show()));
		tempMap.put("is_sell", StringUtil.formatStr(gdItemStore.getIs_sell()));
		tempMap.put("is_exchange", StringUtil.formatStr(gdItemStore.getIs_exchange()));
		tempMap.put("is_virtual", StringUtil.formatStr(gdItemStore.getIs_virtual()));
		tempMap.put("is_best", StringUtil.formatStr(gdItemStore.getIs_best()));
		tempMap.put("is_new", StringUtil.formatStr(gdItemStore.getIs_new()));
		tempMap.put("is_hot", StringUtil.formatStr(gdItemStore.getIs_hot()));
		tempMap.put("is_activity", StringUtil.formatStr(gdItemStore.getIs_activity()));
		tempMap.put("is_track_stock", StringUtil.formatStr(gdItemStore.getIs_track_stock()));
		tempMap.put("stock_warning", gdItemStore.getStock_warning() + "");
		tempMap.put("is_discount", StringUtil.formatStr(gdItemStore.getIs_discount()));
		tempMap.put("is_offline", StringUtil.formatStr(gdItemStore.getIs_offline()));
		tempMap.put("is_weighed", StringUtil.formatStr(gdItemStore.getIs_weighed()));
		tempMap.put("is_recommend", StringUtil.formatStr(gdItemStore.getIs_recommend()));
		tempMap.put("expired_warning", StringUtil.formatStr(gdItemStore.getExpired_warning()));
		tempMap.put("sum_sale", StringUtil.formatStr(gdItemStore.getStock_qty() - gdItemStore.getSale_qty()));
		String image_info = StringUtil.formatStr(gdItemStore.getImage_info());
		if (StringUtils.isNotEmpty(image_info)) {
			doc_ids.add(image_info);
		}
		// 解析商品详情图片
		String image_detail = StringUtil.formatStr(gdItemStore.getImage_detail());
		if (StringUtils.isNotEmpty(image_detail)) {
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(image_detail);
			for (Map<String, Object> m : tempList) {
				if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
					doc_ids.add(m.get("path_id") + "");
				}
			}
		}
		String product_source = StringUtil.formatStr(gdItemStore.getProduct_source());
		if (product_source.equals(Constant.GOODS_PRODUCT_SOURCE_WOYAOPI)) {
			product_source = "我要批";
		} else if (product_source.equals(Constant.GOODS_PRODUCT_SOURCE_HUIYIDING)) {
			product_source = "惠易定";
		} else if (product_source.equals(Constant.GOODS_PRODUCT_SOURCE_ZIDINGYI)) {
			product_source = "自定义";
		} else if (product_source.equals(Constant.GOODS_PRODUCT_SOURCE_LINGLEME)) {
			product_source = "淘淘领";
		} else if (product_source.equals(Constant.GOODS_PRODUCT_SOURCE_HUOPINTUAN)) {
			product_source = "拼团领";
		} else {
			product_source = "自定义";
		}
		tempMap.put("product_source", product_source);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("detail", tempMap);
		result.setResultData(map);
	}

	@Override
	public void itemStoreForOrderFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "store_id", "item_store_id" });
		Map<String, Object> gdItemStoreMap = itemStoreDao.selectGdItemStoreForOrder(paramsMap);
		if (null == gdItemStoreMap) {
			throw new BusinessException(RspCode.ITEM_STORE_NOT_EXIST, RspCode.MSG.get(RspCode.ITEM_STORE_NOT_EXIST));
		}
		result.setResultData(gdItemStoreMap);
	}

	@Override
	public void itemStoreForConsumerListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "store_id" });
		paramsMap.put("is_sell", "Y");
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		for (GdItemStore gdItemStore : gdItemStoreList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("item_store_id", gdItemStore.getItem_store_id());
			tempMap.put("item_name", gdItemStore.getItem_name());
			tempMap.put("category_id", StringUtil.formatStr(gdItemStore.getCategory_id()));
			tempMap.put("desc1", StringUtil.formatStr(gdItemStore.getDesc1()));
			tempMap.put("image_info", StringUtil.formatStr(gdItemStore.getImage_info()));
			tempMap.put("specification", StringUtil.formatStr(gdItemStore.getSpecification()));
			tempMap.put("pack", StringUtil.formatStr(gdItemStore.getPack()));
			tempMap.put("weight", StringUtil.formatStr(gdItemStore.getWeight()));
			tempMap.put("cost_price", gdItemStore.getCost_price() + "");
			tempMap.put("market_price", gdItemStore.getMarket_price() + "");
			tempMap.put("vip_price", gdItemStore.getVip_price() + "");
			tempMap.put("rebate", gdItemStore.getRebate() + "");
			tempMap.put("product_source", StringUtil.formatStr(gdItemStore.getProduct_source()));
			tempMap.put("sale_qty", gdItemStore.getSale_qty() + "");
			String image_info = StringUtil.formatStr(gdItemStore.getImage_info());
			if (!StringUtils.isEmpty(image_info)) {
				doc_ids.add(image_info);
			}
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void itemStoreForStoresListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "store_id" });
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStoreList(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		for (GdItemStore gdItemStore : gdItemStoreList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("item_store_id", gdItemStore.getItem_store_id());
			tempMap.put("item_name", gdItemStore.getItem_name());
			tempMap.put("category_id", StringUtil.formatStr(gdItemStore.getCategory_id()));
			tempMap.put("barcode", StringUtil.formatStr(gdItemStore.getBarcode()));
			tempMap.put("desc1", StringUtil.formatStr(gdItemStore.getDesc1()));
			tempMap.put("image_info", StringUtil.formatStr(gdItemStore.getImage_info()));
			tempMap.put("specification", StringUtil.formatStr(gdItemStore.getSpecification()));
			tempMap.put("pack", StringUtil.formatStr(gdItemStore.getPack()));
			tempMap.put("weight", StringUtil.formatStr(gdItemStore.getWeight()));
			tempMap.put("cost_price", gdItemStore.getCost_price() + "");
			tempMap.put("market_price", gdItemStore.getMarket_price() + "");
			tempMap.put("vip_price", gdItemStore.getVip_price() + "");
			tempMap.put("rebate", gdItemStore.getRebate() + "");
			tempMap.put("product_source", StringUtil.formatStr(gdItemStore.getProduct_source()));
			tempMap.put("sale_qty", gdItemStore.getSale_qty() + "");
			String image_info = StringUtil.formatStr(gdItemStore.getImage_info());
			if (!StringUtils.isEmpty(image_info)) {
				doc_ids.add(image_info);
			}
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void itemStoreDeleted(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "item_store_id" });
		itemStoreDao.deletedItemStore(paramsMap);
	}

	@Override
	public void storesExchangeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "store_id" });
		paramsMap.put("is_exchange", "Y");
		List<GdItemStore> gdItemStoreList = itemStoreDao.selectGdItemStore(paramsMap);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (GdItemStore gdItemStore : gdItemStoreList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("item_store_id", gdItemStore.getItem_store_id());
			tempMap.put("store_id", gdItemStore.getStore_id());
			tempMap.put("item_name", gdItemStore.getItem_name());
			tempMap.put("category_id", StringUtil.formatStr(gdItemStore.getCategory_id()));
			tempMap.put("brand_id", StringUtil.formatStr(gdItemStore.getBrand_id()));
			tempMap.put("barcode", gdItemStore.getBarcode());
			tempMap.put("desc1", StringUtil.formatStr(gdItemStore.getDesc1()));
			tempMap.put("image_info", StringUtil.formatStr(gdItemStore.getImage_info()));
			tempMap.put("specification", StringUtil.formatStr(gdItemStore.getSpecification()));
			tempMap.put("pack", StringUtil.formatStr(gdItemStore.getPack()));
			tempMap.put("weight", StringUtil.formatStr(gdItemStore.getWeight()));
			tempMap.put("market_price", gdItemStore.getMarket_price() + "");
			tempMap.put("vip_price", gdItemStore.getVip_price() + "");
			tempMap.put("sale_qty", gdItemStore.getSale_qty() + "");
			tempMap.put("stock_qty", gdItemStore.getStock_qty() + "");
			resultList.add(tempMap);
		}
		result.setResultData(resultList);
	}

}
