package com.meitianhui.goods.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
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
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.goods.constant.Constant;
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.dao.GoodsDao;
import com.meitianhui.goods.dao.PsGoodsDao;
import com.meitianhui.goods.entity.GdViewSell;
import com.meitianhui.goods.entity.PsGoods;
import com.meitianhui.goods.entity.PsGoodsActivity;
import com.meitianhui.goods.entity.PsGoodsLog;
import com.meitianhui.goods.entity.PsGoodsSku;
import com.meitianhui.goods.service.PsGoodsService;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkCouponConvertRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkCouponConvertResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;

/**
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
@Service
public class PsGoodsServiceImpl implements PsGoodsService {

	@Autowired
	public RedisUtil redisUtil;
	@Autowired
	private DocUtil docUtil;
	@Autowired
	public GoodsDao goodsDao;
	@Autowired
	public PsGoodsDao psGoodsDao;

	/** 免费领商品列表缓存key **/
	public static Set<String> freeGetGoodsListCacheKeySet = new HashSet<String>();

	@Override
	public void psGoodsSync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "goods_id", "title", "area_id", "category", "display_area", "desc1", "contact_person",
						"contact_tel", "pic_info", "cost_price", "market_price", "discount_price", "ladder_price",
						"min_buy_qty", "max_buy_qty", "payment_way", "status", "shipping_fee" });
		RedisLock lock = null;
		String lockKey = null;
		try {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String goods_code = StringUtil.formatStr(paramsMap.get("goods_code"));

			lockKey = "[psGoodsSync]_" + goods_code;
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();

			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				throw new BusinessException(RspCode.GOODS_CODE_ERROR, "您的操作过于频繁,请稍后重试");
			}
			redisUtil.setStr(lockKey, goods_code, 180);

			// 根据商品id查询商品，存在则更新，不存在新增
			tempMap.clear();
			tempMap.put("goods_id", paramsMap.get("goods_id"));
			List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(tempMap);
           
			if (psGoodsList.size() > 0) {
				PsGoods psGoods = psGoodsList.get(0);
				String sale_qty = StringUtil.formatStr(paramsMap.get("sale_qty"));
				if (!StringUtils.isEmpty(sale_qty)) {
					// 计算差值
					Integer num = Integer.parseInt(sale_qty) - psGoods.getSale_qty();
					// 计算总库存量
					Integer new_stock_qty = psGoods.getStock_qty() + num;
					paramsMap.put("stock_qty", new_stock_qty);
				}
				if ("hsrj".equals(paramsMap.get("data_source"))) {
					if (!StringUtil.isEmpty(StringUtil.formatStr(paramsMap.get("quan_id")))) {
						Map<String, Object> tempTaobaoMap = createCommandAndUrlByTaobaoAPI(
								paramsMap.get("goods_code").toString(), paramsMap.get("quan_id").toString());
						if (tempTaobaoMap != null) {
							String url_short = tempTaobaoMap.get("url_short").toString();
							if (url_short == null) {
								String url_long = tempTaobaoMap.get("url_long").toString();
								paramsMap.put("taobao_link", url_long);
							} else {
								paramsMap.put("taobao_link", url_short);
							}
							String command = tempTaobaoMap.get("command").toString();
							if (command != null) {
								paramsMap.put("product_source", command);
							}
						}
					}
				}
				psGoodsDao.updatePsGoods(paramsMap);
				tempMap.clear();
				tempMap.put("goods_id", paramsMap.get("goods_id"));
				tempMap.put("category", paramsMap.get("category"));
				Map<String, Object> eventTempMap = new HashMap<String, Object>();
				eventTempMap.put("remark","修改商品:" + paramsMap.get("title") + ",商品ID：" + paramsMap.get("goods_id") + ",商品码：" + goods_code);
				tempMap.put("event", FastJsonUtil.toJson(eventTempMap));
				goodsLogAdd(tempMap, result);
			} else {
				String cost_allocation = StringUtil.formatStr(paramsMap.get("cost_allocation"));
				if (StringUtils.isEmpty(cost_allocation)) {
					paramsMap.put("cost_allocation", "0.00");
				}
				String sale_qty = StringUtil.formatStr(paramsMap.get("sale_qty"));
				if (StringUtils.isBlank(sale_qty)) {
					paramsMap.put("sale_qty", "0");
				}
				if (!StringUtils.isEmpty(goods_code)) {
					// 验证商品码是否已存在
					tempMap.clear();
					tempMap.put("goods_code", paramsMap.get("goods_code"));
					List<PsGoods> psGoodsTempList = psGoodsDao.selectPsGoods(tempMap);
					if (psGoodsTempList.size() > 0) {
						throw new BusinessException(RspCode.GOODS_CODE_EXIST,
								RspCode.MSG.get(RspCode.GOODS_CODE_EXIST));
					}
				}
				Date date = new Date();
				PsGoods psGoods = new PsGoods();
				BeanConvertUtil.mapToBean(psGoods, paramsMap);
				psGoods.setStock_qty(psGoods.getSale_qty());
				psGoods.setModified_date(date);
				psGoods.setCreated_date(date);
				if (psGoods.getSettled_price() == null) {
					psGoods.setSettled_price(new BigDecimal("0.00"));
				}
				if (psGoods.getService_fee() == null) {
					psGoods.setService_fee(new BigDecimal("0.00"));
				}

				if ("hsrj".equals(paramsMap.get("data_source"))) {
					if (!StringUtil.isEmpty(StringUtil.formatStr(paramsMap.get("quan_id")))) {

						Map<String, Object> tempTaobaoMap = createCommandAndUrlByTaobaoAPI(
								paramsMap.get("goods_code").toString(), paramsMap.get("quan_id").toString());
						if (tempTaobaoMap != null) {
							String url_short = tempTaobaoMap.get("url_short").toString();
							if (url_short == null) {
								String url_long = tempTaobaoMap.get("url_long").toString();
								psGoods.setTaobao_link(url_long);
							} else {
								psGoods.setTaobao_link(url_short);
							}
							String command = tempTaobaoMap.get("command").toString();
							if (command != null) {
								psGoods.setProduct_source(command);
							}
						}
					}
				}
				psGoodsDao.insertPsGoods(psGoods);

				tempMap.clear();
				tempMap.put("goods_id", paramsMap.get("goods_id"));
				tempMap.put("category", paramsMap.get("category"));
				Map<String, Object> eventTempMap = new HashMap<String, Object>();
				eventTempMap.put("remark","新增商品:" + paramsMap.get("title") + ",商品ID：" + paramsMap.get("goods_id") + ",商品码：" + goods_code);
				tempMap.put("event", FastJsonUtil.toJson(eventTempMap));
				goodsLogAdd(tempMap, result);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (lock != null) {
				lock.unlock();
			}
			if (StringUtil.isNotEmpty(lockKey)) {
				redisUtil.del(lockKey);
			}
		}
	}

	
	 
	public Map<String, Object> createCommandAndUrlByTaobaoAPI(String itemId, String quanId)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> tempParams = new HashMap<String, Object>();
		try {
			TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "24635795",
					"b8709ffa33efaff9a7d4813ba77de28d");
			TbkCouponConvertRequest tbkCouponConvertRequest = new TbkCouponConvertRequest();
			tbkCouponConvertRequest.setItemId(Long.parseLong(itemId));
			tbkCouponConvertRequest.setAdzoneId(Long.parseLong("146130492"));
			TbkCouponConvertResponse tbkCouponConvertResponse = client.execute(tbkCouponConvertRequest);
			if (tbkCouponConvertResponse != null) {
				if (StringUtils.isNotBlank(tbkCouponConvertResponse.getErrorCode())) {
					throw new BusinessException(RspCode.PS_GOODS_NOT_EXIST,
							RspCode.MSG.get(RspCode.SYSTEM_NO_PARAMS_UPDATE));
				}
				String originalURL = (((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) FastJsonUtil
						.jsonToMap(tbkCouponConvertResponse.getBody()).get("tbk_coupon_convert_response"))
								.get("result")).get("results")).get("coupon_click_url")).toString();
				Map<String, String> reqParams = new HashMap<String, String>();
				reqParams.put("source", "896267270");
				String longUrl = URLEncoder.encode(originalURL + "&activityId=" + quanId ,"UTF-8");
				reqParams.put("url_long", longUrl);
				String weibo_service_url = "https://api.weibo.com/2/short_url/shorten.json";
				String resultStr = HttpClientUtil.getShort(weibo_service_url, reqParams);
				String getSignInfo = resultStr.substring(resultStr.indexOf("[") + 1, resultStr.indexOf("]"));
				Map<String, Object> bizParams = new HashMap<String, Object>();
				bizParams = FastJsonUtil.jsonToMap(getSignInfo);
				tempParams.put("url_long", bizParams.get("url_long").toString());
				tempParams.put("url_short", bizParams.get("url_short").toString());

				TbkTpwdCreateRequest tbkTpwdCreateRequest = new TbkTpwdCreateRequest();
				tbkTpwdCreateRequest.setUserId("");
				tbkTpwdCreateRequest.setText("收到一条优惠信息");
				tbkTpwdCreateRequest.setUrl(originalURL);
				tbkTpwdCreateRequest.setLogo("");
				tbkTpwdCreateRequest.setExt("");
				TbkTpwdCreateResponse tbkTpwdCreateResponse = client.execute(tbkTpwdCreateRequest);
				String command = (((Map<String, Object>) ((Map<String, Object>) FastJsonUtil
						.jsonToMap(tbkTpwdCreateResponse.getBody()).get("tbk_tpwd_create_response")).get("data"))
								.get("model")).toString();
				tempParams.put("command", command);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		return tempParams;
	}

	@Override
	public void wpyGoodsSkuSync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_sku_list" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		List<Map<String, Object>> goods_sku_list = FastJsonUtil.jsonToList(paramsMap.get("goods_sku_list") + "");
		String goods_id = null;
		// 总库存量的变更
		Integer total_sale_qty = 0;
		// 总可销售库存
		Integer total_stock_qty = 0;
		for (Map<String, Object> skuMap : goods_sku_list) {
			ValidateUtil.validateParams(skuMap,
					new String[] { "goods_id", "sku", "desc1", "cost_price", "sales_price", "sale_qty" });
			String goods_stock_id = StringUtil.formatStr(skuMap.get("goods_stock_id"));
			goods_id = StringUtil.formatStr(skuMap.get("goods_id"));
			if (StringUtils.isNotBlank(goods_stock_id)) {
				// 根据商品库存id查询商品，存在则更新，不存在新增
				tempMap.clear();
				tempMap.put("goods_stock_id", goods_stock_id);
				List<PsGoodsSku> psGoodsSkuList = psGoodsDao.selectPsGoodsSku(tempMap);
				PsGoodsSku psGoodsSku = psGoodsSkuList.get(0);
				// 如果可销售库存数量和数据的数量不相等,则更新库存,否则不更新库存
				Integer sale_qty = Integer.parseInt(skuMap.get("sale_qty") + "");
				// 计算差值
				Integer modify_num = sale_qty - psGoodsSku.getSale_qty();
				// 计算总库存
				Integer new_stock_qty = psGoodsSku.getStock_qty() + modify_num;
				// 累加可销售库存量
				total_sale_qty += sale_qty;
				// 累加总销售库存量
				total_stock_qty += new_stock_qty;
				skuMap.put("stock_qty_modify", modify_num);
				skuMap.put("sale_qty_modify", modify_num);
				psGoodsDao.updatePsGoodsSku(skuMap);
			} else {
				Date date = new Date();
				PsGoodsSku psGoodsSku = new PsGoodsSku();
				BeanConvertUtil.mapToBean(psGoodsSku, skuMap);
				psGoodsSku.setGoods_stock_id(IDUtil.getUUID());
				psGoodsSku.setStock_qty(psGoodsSku.getSale_qty());
				psGoodsSku.setModified_date(date);
				psGoodsSku.setCreated_date(date);
				psGoodsSku.setStatus(Constant.STATUS_NORMAL);
				psGoodsDao.insertPsGoodsSku(psGoodsSku);
				// 累加可销售库存量
				total_sale_qty += psGoodsSku.getSale_qty();
				// 累加总销售库存量
				total_stock_qty += psGoodsSku.getSale_qty();
			}
		}
		if (StringUtils.isNotBlank(goods_id)) {
			tempMap.clear();
			tempMap.put("goods_id", goods_id);
			// 修改主商品的商品库存
			tempMap.put("stock_qty", total_stock_qty);
			tempMap.put("sale_qty", total_sale_qty);
			psGoodsDao.updatePsGoods(tempMap);
		}
	}

	@Override
	public void psGoodsSaleQtyDeduction(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id", "sell_qty" });
		int updateFlag = psGoodsDao.updatePsGoodsSaleQtyDeduction(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, "可售库存不足");
		}
		gdSellAdd(paramsMap, new ResultData());
	}

	@Override
	public void psGoodsSaleQtyRestore(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id", "restore_qty" });
		int updateFlag = psGoodsDao.updatePsGoodsSaleQtyRestore(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PS_GOODS_NOT_EXIST, "系统繁忙,请重新操作");
		}
		paramsMap.put("sell_qty", "-" + paramsMap.get("restore_qty"));
		gdSellAdd(paramsMap, new ResultData());
	}

	@Override
	public void handleWypGoodsSaleQtyRestore(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_list" });
		List<Map<String, Object>> goods_list = FastJsonUtil.jsonToList(paramsMap.get("goods_list") + "");
		for (Map<String, Object> goodsMap : goods_list) {
			ValidateUtil.validateParams(goodsMap, new String[] { "goods_id", "goods_stock_id", "restore_qty" });
			// 修改sku库存表
			psGoodsDao.updateWypGoodsSkuSaleQtyRestore(goodsMap);

			// 修改主表库存
			psGoodsDao.updateWypGoodsSaleQtyRestore(goodsMap);
			goodsMap.put("sell_qty", "-" + goodsMap.get("restore_qty"));
			gdSellAdd(goodsMap, new ResultData());
		}
	}

	@Override
	public void wypGoodsSaleQtyDeduction(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_list" });
		List<Map<String, Object>> goods_list = FastJsonUtil.jsonToList(paramsMap.get("goods_list") + "");
		for (Map<String, Object> goodsMap : goods_list) {
			ValidateUtil.validateParams(goodsMap,
					new String[] { "goods_id", "goods_title", "goods_stock_id", "sell_qty" });
			// 修改sku库存表
			psGoodsDao.updateWypGoodsSkuSaleQtyDeduction(goodsMap);
			// 修改主表库存
			psGoodsDao.updateWypGoodsSaleQtyDeduction(goodsMap);
			gdSellAdd(goodsMap, new ResultData());
		}
	}

	@Override
	public void wypGoodsSaleQtyValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_list" });
		List<Map<String, Object>> goods_list = FastJsonUtil.jsonToList(paramsMap.get("goods_list") + "");
		for (Map<String, Object> goodsMap : goods_list) {
			ValidateUtil.validateParams(goodsMap, new String[] { "goods_id", "goods_title", "sell_qty" });
			List<PsGoodsSku> psGoodsSkuList = psGoodsDao.selectPsGoodsSku(goodsMap);
			if (psGoodsSkuList.size() == 0) {
				throw new BusinessException(RspCode.PS_GOODS_NOT_EXIST, "商品的规格信息不存在");
			}
			PsGoodsSku psGoodsSku = psGoodsSkuList.get(0);
			String sell_qty = StringUtil.formatStr(goodsMap.get("sell_qty"));
			// 计算差值
			if (psGoodsSku.getSale_qty() - Integer.parseInt(sell_qty) < 0) {
				throw new BusinessException(RspCode.GOODS_SALE_ERROR, goodsMap.get("goods_title") + "库存不足");
			}
		}
	}

	@Override
	public void psGoodsOrderBottom(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id" });
		psGoodsDao.updatePsGoodsBottom(paramsMap);
	}

	@Override
	public void psGoodsEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id" });
		if (paramsMap.size() == 1) {
			throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE,
					RspCode.MSG.get(RspCode.SYSTEM_NO_PARAMS_UPDATE));
		}
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("goods_id", paramsMap.get("goods_id"));
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(tempMap);
		if (psGoodsList.size() == 0) {
			throw new BusinessException(RspCode.PS_GOODS_NOT_EXIST, RspCode.MSG.get(RspCode.PS_GOODS_NOT_EXIST));
		}
		PsGoods psGoods = psGoodsList.get(0);
		String sale_qty = StringUtil.formatStr(paramsMap.get("sale_qty"));
		if (!StringUtils.isEmpty(sale_qty)) {
			// 计算差值
			Integer num = Integer.parseInt(sale_qty) - psGoods.getSale_qty();
			// 计算总库存量
			Integer new_stock_qty = psGoods.getStock_qty() + num;
			paramsMap.put("stock_qty", new_stock_qty);
		}
		psGoodsDao.updatePsGoods(paramsMap);
	}

	@Override
	public void psGoodsNewEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id", "title", "market_price", "discount_price",
				"desc1", "category", "data_source", "goods_code", "pic_info" });
		if (paramsMap.size() == 1) {
			throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE,
					RspCode.MSG.get(RspCode.SYSTEM_NO_PARAMS_UPDATE));
		}
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("goods_id", paramsMap.get("goods_id"));
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(tempMap);
		if (psGoodsList.size() == 0) {
			throw new BusinessException(RspCode.PS_GOODS_NOT_EXIST, RspCode.MSG.get(RspCode.PS_GOODS_NOT_EXIST));
		}
		tempMap.put("title", paramsMap.get("title"));
		tempMap.put("market_price", paramsMap.get("market_price"));
		tempMap.put("discount_price", paramsMap.get("discount_price"));
		tempMap.put("desc1", paramsMap.get("desc1"));
		tempMap.put("category", paramsMap.get("category"));
		tempMap.put("data_source", paramsMap.get("data_source"));
		tempMap.put("goods_code", paramsMap.get("goods_code"));
		tempMap.put("pic_info", paramsMap.get("pic_info"));
		Integer i = psGoodsDao.updatePsGoods(tempMap);
		if (i > 0) {
			tempMap.clear();
			tempMap.put("msg", "修改成功");
			result.setResultData(tempMap);
		}
	}

	@Override
	public void psGoodsStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id", "status" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("goods_id", paramsMap.get("goods_id"));
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(tempMap);
		if (psGoodsList.size() == 0) {
			throw new BusinessException(RspCode.PS_GOODS_NOT_EXIST, RspCode.MSG.get(RspCode.PS_GOODS_NOT_EXIST));
		}
		tempMap.put("status", paramsMap.get("status"));
		psGoodsDao.updatePsGoods(tempMap);
	}

	@Override
	public void psGoodsDelete(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id" });
		Integer i = psGoodsDao.updatePsGoodsStatus(paramsMap);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		if (i > 0) {
			tempMap.put("msg", "删除成功");
			result.setResultData(tempMap);
		}
	}

	@Override
	public void freeGetGoodsStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "status" });
		// 商品id和商品编码必须有一个
		ValidateUtil.validateParamsNum(paramsMap, new String[] { "goods_id", "goods_code" }, 1);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("goods_id", StringUtil.formatStr(paramsMap.get("goods_id")));
		tempMap.put("goods_code", StringUtil.formatStr(paramsMap.get("goods_code")));
		tempMap.put("category", "试样");
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(tempMap);
		if (psGoodsList.size() == 0) {
			throw new BusinessException(RspCode.PS_GOODS_NOT_EXIST, RspCode.MSG.get(RspCode.PS_GOODS_NOT_EXIST));
		}
		PsGoods psGoods = psGoodsList.get(0);
		// 将更新前商品信息返回
		result.setResultData(psGoods);

		paramsMap.put("goods_id", psGoods.getGoods_id());
		String sale_qty = StringUtil.formatStr(paramsMap.get("sale_qty"));
		if (StringUtils.isNotEmpty(sale_qty)) {
			// 计算差值
			Integer num = Integer.parseInt(sale_qty) - psGoods.getSale_qty();
			// 计算总库存量
			Integer new_stock_qty = psGoods.getStock_qty() + num;
			paramsMap.put("stock_qty", new_stock_qty);
		}
		psGoodsDao.updatePsGoods(paramsMap);
		// 清除缓存
		boolean flag = false;
		for (String key : freeGetGoodsListCacheKeySet) {
			redisUtil.delObj(key);
			flag = true;
		}
		if (flag) {
			freeGetGoodsListCacheKeySet.clear();
		}
	}

	@Override
	public void psGoodsFindForWeb(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "area_id" });
		List<String> areaIdList = new ArrayList<String>();
		areaIdList.add((String) paramsMap.get("area_id"));
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("area_code", paramsMap.get("area_id"));
		Map<String, Object> areaMap = goodsDao.selectAreaCodeTree(tempMap);
		if (areaMap != null) {
			if (null != areaMap.get("s_code") && !"".equals(areaMap.get("s_code"))) {
				areaIdList.add((String) areaMap.get("s_code"));
			}
			if (null != areaMap.get("t_code") && !"".equals(areaMap.get("t_code"))) {
				areaIdList.add((String) areaMap.get("t_code"));
			}
		}
		areaIdList.add("100000");
		paramsMap.put("areaList", areaIdList);
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoodsForWeb(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("goods_code", StringUtil.formatStr(psGoods.getGoods_code()));
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("label", StringUtil.formatStr(psGoods.getLabel()));
			psGoodsMap.put("label_promotion", StringUtil.formatStr(psGoods.getLabel_promotion()));
			psGoodsMap.put("category", psGoods.getCategory());
			psGoodsMap.put("display_area", psGoods.getDisplay_area());
			psGoodsMap.put("specification", StringUtil.formatStr(psGoods.getSpecification()));
			psGoodsMap.put("pack", StringUtil.formatStr(psGoods.getPack()));
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
			psGoodsMap.put("min_buy_qty", psGoods.getMin_buy_qty() + "");
			psGoodsMap.put("max_buy_qty", psGoods.getMax_buy_qty() + "");
			psGoodsMap.put("valid_thru", StringUtil.formatStr(psGoods.getValid_thru()));
			psGoodsMap.put("payment_way", StringUtil.formatStr(psGoods.getPayment_way()));
			psGoodsMap.put("status", psGoods.getStatus());
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
			psGoodsMap.put("pic_info", StringUtil.formatStr(psGoods.getPic_info()));
			// 解析区域
			String[] areaStr = psGoods.getDelivery_area().split(",");
			tempMap.clear();
			tempMap.put("areaStr", areaStr);
			List<Map<String, Object>> areaList = goodsDao.selectMDArea(tempMap);
			String delivery_desc = "";
			for (Map<String, Object> area : areaList) {
				delivery_desc = delivery_desc + "|" + area.get("path");
			}
			if (delivery_desc.trim().length() > 0) {
				delivery_desc = delivery_desc.substring(1);
			}
			psGoodsMap.put("delivery_area", StringUtil.formatStr(psGoods.getDelivery_area()));
			psGoodsMap.put("delivery_desc", delivery_desc);
			resultList.add(psGoodsMap);
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (!pic_info.equals("")) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void tsGoodsListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "area_id" });
		List<String> areaIdList = new ArrayList<String>();
		String area_id = paramsMap.get("area_id") + "";
		areaIdList.add(area_id);
		areaIdList.add(area_id.substring(0, area_id.length() - 2) + "00");
		areaIdList.add(area_id.substring(0, area_id.length() - 3) + "000");
		areaIdList.add(area_id.substring(0, area_id.length() - 4) + "0000");
		areaIdList.add("100000");
		paramsMap.put("areaList", areaIdList);
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoodsForH5(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("label", psGoods.getLabel());
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			// 修复ios版本 621后的版本可以废除
			psGoodsMap.put("discount_price", psGoods.getTs_price() + "");
			psGoodsMap.put("ts_min_num", psGoods.getTs_min_num());
			psGoodsMap.put("ts_price", psGoods.getTs_price() + "");
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
			psGoodsMap.put("pic_info", StringUtil.formatStr(psGoods.getPic_info()));
			psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
			resultList.add(psGoodsMap);
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (!pic_info.equals("")) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void psGoodsFindForH5(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "area_id" });
		List<String> areaIdList = new ArrayList<String>();
		String area_id = paramsMap.get("area_id") + "";
		areaIdList.add(area_id);
		areaIdList.add(area_id.substring(0, area_id.length() - 2) + "00");
		areaIdList.add(area_id.substring(0, area_id.length() - 3) + "000");
		areaIdList.add(area_id.substring(0, area_id.length() - 4) + "0000");
		areaIdList.add("100000");
		paramsMap.put("areaList", areaIdList);
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoodsForH5(paramsMap);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("desc1", StringUtil.formatStr(psGoods.getDesc1()));
			psGoodsMap.put("label", StringUtil.formatStr(psGoods.getLabel()));
			psGoodsMap.put("label_promotion", StringUtil.formatStr(psGoods.getLabel_promotion()));
			psGoodsMap.put("category", psGoods.getCategory());
			psGoodsMap.put("display_area", psGoods.getDisplay_area());
			psGoodsMap.put("specification", StringUtil.formatStr(psGoods.getSpecification()));
			psGoodsMap.put("supplier", StringUtil.formatStr(psGoods.getSupplier()));
			psGoodsMap.put("pack", StringUtil.formatStr(psGoods.getPack()));
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
			psGoodsMap.put("min_buy_qty", psGoods.getMin_buy_qty() + "");
			psGoodsMap.put("max_buy_qty", psGoods.getMax_buy_qty() + "");
			psGoodsMap.put("valid_thru", StringUtil.formatStr(psGoods.getValid_thru()));
			psGoodsMap.put("status", psGoods.getStatus());
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
			psGoodsMap.put("pic_info", StringUtil.formatStr(psGoods.getPic_info()));
			// 解析区域
			String[] areaStr = psGoods.getDelivery_area().split(",");
			tempMap.clear();
			tempMap.put("areaStr", areaStr);
			List<Map<String, Object>> areaList = goodsDao.selectMDArea(tempMap);
			String delivery_desc = "";
			for (Map<String, Object> area : areaList) {
				delivery_desc = delivery_desc + "|" + area.get("path");
			}
			if (delivery_desc.trim().length() > 0) {
				delivery_desc = delivery_desc.substring(1);
			}
			psGoodsMap.put("delivery_area", StringUtil.formatStr(psGoods.getDelivery_area()));
			psGoodsMap.put("delivery_desc", delivery_desc);
			resultList.add(psGoodsMap);
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (!pic_info.equals("")) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void wypGoodsDetailFindForH5(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id" });
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(paramsMap);
		if (psGoodsList.size() == 0) {
			throw new BusinessException(RspCode.PS_GOODS_EXIST, "我要批商品不存在");
		}
		List<String> doc_ids = new ArrayList<String>();
		Map<String, Object> goodsMap = new HashMap<String, Object>();
		PsGoods psGoods = psGoodsList.get(0);
		goodsMap.put("goods_id", psGoods.getGoods_id());
		goodsMap.put("goods_code", StringUtil.formatStr(psGoods.getGoods_code()));
		goodsMap.put("title", psGoods.getTitle());
		goodsMap.put("desc1", StringUtil.formatStr(psGoods.getDesc1()));
		goodsMap.put("area_id", psGoods.getArea_id());
		goodsMap.put("label", StringUtil.formatStr(psGoods.getLabel()));
		goodsMap.put("label_promotion", StringUtil.formatStr(psGoods.getLabel_promotion()));
		goodsMap.put("category", psGoods.getCategory());
		goodsMap.put("data_source", StringUtil.formatStr(psGoods.getData_source()));
		goodsMap.put("pack", StringUtil.formatStr(psGoods.getPack()));
		goodsMap.put("supplier", StringUtil.formatStr(psGoods.getSupplier()));
		goodsMap.put("supplier_id", StringUtil.formatStr(psGoods.getSupplier_id()));
		goodsMap.put("warehouse", StringUtil.formatStr(psGoods.getWarehouse()));
		goodsMap.put("warehouse_id", StringUtil.formatStr(psGoods.getWarehouse_id()));
		goodsMap.put("min_buy_qty", psGoods.getMin_buy_qty() + "");
		goodsMap.put("max_buy_qty", psGoods.getMax_buy_qty() + "");
		goodsMap.put("goods_unit", StringUtil.formatStr(psGoods.getGoods_unit()));
		goodsMap.put("delivery", StringUtil.formatStr(psGoods.getDelivery()));
		goodsMap.put("valid_thru", StringUtil.formatStr(psGoods.getValid_thru()));
		goodsMap.put("payment_way", StringUtil.formatStr(psGoods.getPayment_way()));
		goodsMap.put("pic_info", StringUtil.formatStr(psGoods.getPic_info()));
		goodsMap.put("pic_detail_info", StringUtil.formatStr(psGoods.getPic_detail_info()));
		goodsMap.put("online_date", DateUtil.date2Str(psGoods.getOnline_date(), DateUtil.fmt_yyyyMMddHHmmss));
		goodsMap.put("sale_qty", psGoods.getSale_qty() + "");
		goodsMap.put("stock_qty", psGoods.getStock_qty() + "");
		goodsMap.put("discount_price", psGoods.getDiscount_price() + "");
		goodsMap.put("market_price", psGoods.getMarket_price() + "");
		goodsMap.put("specification", StringUtil.formatStr(psGoods.getSpecification()));
		goodsMap.put("remark", StringUtil.formatStr(psGoods.getRemark()));
		// 解析区域
		String[] areaStr = psGoods.getDelivery_area().split(",");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("areaStr", areaStr);
		List<Map<String, Object>> areaList = goodsDao.selectMDArea(tempMap);
		String delivery_desc = "";
		for (Map<String, Object> area : areaList) {
			delivery_desc = delivery_desc + "|" + area.get("path");
		}
		if (delivery_desc.trim().length() > 0) {
			delivery_desc = delivery_desc.substring(1);
		}
		goodsMap.put("delivery_area", StringUtil.formatStr(psGoods.getDelivery_area()));
		goodsMap.put("delivery_desc", delivery_desc);
		// 解析图片
		String pic_info = StringUtil.formatStr(psGoods.getPic_info());
		if (!pic_info.equals("")) {
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
			for (Map<String, Object> m : tempList) {
				if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
					doc_ids.add(m.get("path_id") + "");
				}
			}
		}
		// 解析详情图片
		String pic_detail_info = StringUtil.formatStr(psGoods.getPic_detail_info());
		if (!pic_detail_info.equals("")) {
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_detail_info);
			for (Map<String, Object> m : tempList) {
				if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
					doc_ids.add(m.get("path_id") + "");
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("detail", goodsMap);
		result.setResultData(map);
	}

	@Override
	public void psGoodsLevelAndPointFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id" });
		List<PsGoods> list = psGoodsDao.selectPsGoods(paramsMap);
		if (CollectionUtils.isNotEmpty(list)) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// tempMap.put("good_level", list.get(0).getGood_level());
			// tempMap.put("good_point", list.get(0).getGood_point());
			result.setResultData(tempMap);
		}
	}

	@Override
	public void freeGetGoodsListFindForSmallProgram(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String key = "[freeGetGoodsListFindForSmallProgram]";
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}
		// 查询可以购买的商品
		paramsMap.put("valid_thru_end", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		List<PsGoods> psGoodsList = psGoodsDao.selectFgGoodsListForSmallProgram(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
			psGoodsMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
			psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
			psGoodsMap.put("data_source", StringUtil.formatStr(psGoods.getData_source()));
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (StringUtil.isNotEmpty(pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						String path_id = m.get("path_id") + "";
						String pic_info_url = docUtil.imageUrlFind(path_id);
						psGoodsMap.put("pic_info_url", pic_info_url);
						break;
					}
				}
			}
			resultList.add(psGoodsMap);
		}
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("list", resultList);
		result.setResultData(resultDataMap);
		// 设置缓存
		redisUtil.setObj(key, resultDataMap, 300);
		freeGetGoodsListCacheKeySet.add(key);
	}
	
	@Override
	public void freeGetGoodsListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String key = "[freeGetGoodsListFind]";
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}
		// 查询可以购买的商品
		paramsMap.put("valid_thru_end", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		List<PsGoods> psGoodsList = psGoodsDao.selectFgGoodsListForH5(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
			psGoodsMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
			psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (StringUtil.isNotEmpty(pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						String path_id = m.get("path_id") + "";
						String pic_info_url = docUtil.imageUrlFind(path_id);
						psGoodsMap.put("pic_info_url", pic_info_url);
						break;
					}
				}
			}
			resultList.add(psGoodsMap);
		}
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("list", resultList);
		result.setResultData(resultDataMap);
		// 设置缓存
		redisUtil.setObj(key, resultDataMap, 300);
		freeGetGoodsListCacheKeySet.add(key);
	}
	
	
	@Override
	public void freeGetGoodsPreSaleListFindForSmallProgram(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String key = "[freeGetGoodsPreSaleListFindForSmallProgram]";
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}
		// 有效期大于当前时间的都是预售
		String valid_thru = DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss);
		paramsMap.put("valid_thru_start", valid_thru);
		// 有效期大于当前时间的都是预售
		// paramsMap.put("valid_thru_end", DateUtil.addDate(valid_thru,
		// DateUtil.fmt_yyyyMMddHHmmss, 3, 1));
		List<PsGoods> psGoodsList = psGoodsDao.selectfreeGetGoodsPreSaleForH5(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("valid_thru", StringUtil.formatStr(psGoods.getValid_thru()));
			psGoodsMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
			psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
			psGoodsMap.put("data_source", StringUtil.formatStr(psGoods.getData_source()));
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (StringUtil.isNotEmpty(pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						String path_id = m.get("path_id") + "";
						String pic_info_url = docUtil.imageUrlFind(path_id);
						psGoodsMap.put("pic_info_url", pic_info_url);
						break;
					}
				}
			}
			resultList.add(psGoodsMap);
		}
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("list", resultList);
		result.setResultData(resultDataMap);
		// 设置缓存
		redisUtil.setObj(key, resultDataMap, 300);
		freeGetGoodsListCacheKeySet.add(key);
		
	}
	
	
	
	@Override
	public void freeGetGoodsPreSaleListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String key = "[freeGetGoodsPreSaleListFind]";
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}
		// 有效期大于当前时间的都是预售
		String valid_thru = DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss);
		paramsMap.put("valid_thru_start", valid_thru);
		// 有效期大于当前时间的都是预售
		// paramsMap.put("valid_thru_end", DateUtil.addDate(valid_thru,
		// DateUtil.fmt_yyyyMMddHHmmss, 3, 1));
		List<PsGoods> psGoodsList = psGoodsDao.selectfreeGetGoodsPreSaleForH5(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("valid_thru", StringUtil.formatStr(psGoods.getValid_thru()));
			psGoodsMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
			psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (StringUtil.isNotEmpty(pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						String path_id = m.get("path_id") + "";
						String pic_info_url = docUtil.imageUrlFind(path_id);
						psGoodsMap.put("pic_info_url", pic_info_url);
						break;
					}
				}
			}
			resultList.add(psGoodsMap);
		}
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("list", resultList);
		result.setResultData(resultDataMap);
		// 设置缓存
		redisUtil.setObj(key, resultDataMap, 300);
		freeGetGoodsListCacheKeySet.add(key);
	}

	@Override
	public void freeGetGoodsNewestListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String label_promotion = StringUtil.formatStr(paramsMap.get("label_promotion"));
		// 如果有标签的话,缓存的key就要增加标签
		String key = "[freeGetGoodsNewestListFind]" + label_promotion;

		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}

		// 上架时间在1天以内的
		String online_date_end = DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss);
		String online_date_start = DateUtil.addDate(online_date_end, DateUtil.fmt_yyyyMMddHHmmss, 3, -1);
		paramsMap.put("online_date_start", online_date_start);
		String valid_thru = DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss);
		paramsMap.put("valid_thru_end", valid_thru);

		List<PsGoods> psGoodsList = psGoodsDao.selectFgGoodsListForH5(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
			psGoodsMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
			psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (StringUtil.isNotEmpty(pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						String path_id = m.get("path_id") + "";
						String pic_info_url = docUtil.imageUrlFind(path_id);
						psGoodsMap.put("pic_info_url", pic_info_url);
						break;
					}
				}
			}
			resultList.add(psGoodsMap);
		}
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("list", resultList);
		result.setResultData(resultDataMap);
		// 设置缓存
		redisUtil.setObj(key, resultDataMap, 300);
		freeGetGoodsListCacheKeySet.add(key);
	}
	
	
	@Override
	public void freeGetGoodsByLabelListFindForSmallProgram(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "label_promotion" });
		String label_promotion = paramsMap.get("label_promotion") + "";
		String key = "[freeGetGoodsByLabelListFindForSmallProgram]" + label_promotion;
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}
		// 查询可以购买的商品
		paramsMap.put("valid_thru_end", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		List<PsGoods> psGoodsList = psGoodsDao.selectfreeGetGoodsListByLabel(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
			psGoodsMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
			psGoodsMap.put("online_date", DateUtil.date2Str(psGoods.getOnline_date(), DateUtil.fmt_yyyyMMddHHmmss));
			psGoodsMap.put("valid_thru", StringUtil.formatStr(psGoods.getValid_thru()));
			psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
			psGoodsMap.put("data_source", StringUtil.formatStr(psGoods.getData_source()));
		
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (StringUtil.isNotEmpty(pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						String path_id = m.get("path_id") + "";
						String pic_info_url = docUtil.imageUrlFind(path_id);
						psGoodsMap.put("pic_info_url", pic_info_url);
						break;
					}
				}
			}
			resultList.add(psGoodsMap);
		}
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("list", resultList);
		result.setResultData(resultDataMap);
		// 设置缓存
		redisUtil.setObj(key, resultDataMap, 10);
		freeGetGoodsListCacheKeySet.add(key);
	}
	
	@Override
	public void freeGetGoodsByLabelListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "label_promotion" });
		String label_promotion = paramsMap.get("label_promotion") + "";
		String key = "[freeGetGoodsByLabelListFind]" + label_promotion;
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}
		// 查询可以购买的商品
		paramsMap.put("valid_thru_end", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		List<PsGoods> psGoodsList = psGoodsDao.selectfreeGetGoodsListByLabel(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
			psGoodsMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
			psGoodsMap.put("online_date", DateUtil.date2Str(psGoods.getOnline_date(), DateUtil.fmt_yyyyMMddHHmmss));
			psGoodsMap.put("valid_thru", StringUtil.formatStr(psGoods.getValid_thru()));
			psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
			psGoodsMap.put("data_source", StringUtil.formatStr(psGoods.getData_source()));
			
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (StringUtil.isNotEmpty(pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						String path_id = m.get("path_id") + "";
						String pic_info_url = docUtil.imageUrlFind(path_id);
						psGoodsMap.put("pic_info_url", pic_info_url);
						break;
					}
				}
			}
			resultList.add(psGoodsMap);
		}
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("list", resultList);
		result.setResultData(resultDataMap);
		// 设置缓存
		redisUtil.setObj(key, resultDataMap, 10);
		freeGetGoodsListCacheKeySet.add(key);
	}

	@Override
	public void goldExchangeGoodsDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id" });
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", psGoods.getGoods_id());
			tempMap.put("goods_code", StringUtil.formatStr(psGoods.getGoods_code()));
			tempMap.put("title", psGoods.getTitle());
			tempMap.put("desc1", StringUtil.formatStr(psGoods.getDesc1()));
			tempMap.put("area_id", psGoods.getArea_id());
			tempMap.put("label", StringUtil.formatStr(psGoods.getLabel()));
			tempMap.put("category", psGoods.getCategory());
			tempMap.put("display_area", psGoods.getDisplay_area());
			tempMap.put("contact_person", psGoods.getContact_person());
			tempMap.put("contact_tel", psGoods.getContact_tel());
			tempMap.put("specification", StringUtil.formatStr(psGoods.getSpecification()));
			tempMap.put("pack", StringUtil.formatStr(psGoods.getPack()));
			tempMap.put("market_price", psGoods.getMarket_price() + "");
			tempMap.put("discount_price", psGoods.getDiscount_price() + "");
			tempMap.put("product_source", StringUtil.formatStr(psGoods.getProduct_source()));
			tempMap.put("shipping_fee", psGoods.getShipping_fee() + "");
			tempMap.put("producer", StringUtil.formatStr(psGoods.getProducer()));
			tempMap.put("supplier", StringUtil.formatStr(psGoods.getSupplier()));
			tempMap.put("manufacturer", StringUtil.formatStr(psGoods.getManufacturer()));
			tempMap.put("min_buy_qty", psGoods.getMin_buy_qty() + "");
			tempMap.put("max_buy_qty", psGoods.getMax_buy_qty() + "");
			tempMap.put("goods_unit", StringUtil.formatStr(psGoods.getGoods_unit()));
			tempMap.put("payment_way", StringUtil.formatStr(psGoods.getPayment_way()));
			tempMap.put("pic_info", StringUtil.formatStr(psGoods.getPic_info()));
			tempMap.put("pic_detail_info", StringUtil.formatStr(psGoods.getPic_detail_info()));
			tempMap.put("sale_qty", psGoods.getSale_qty() + "");
			tempMap.put("stock_qty", psGoods.getStock_qty() + "");
			String deliver_date = DateUtil.getDistanceDays(psGoods.getValid_thru(),
					DateUtil.getFormatDate(DateUtil.fmt_yyyyMMdd));
			tempMap.put("deliver_date", deliver_date);
			// 解析区域
			String[] deliveryAreaStr = psGoods.getDelivery_area().split(",");
			queryMap.clear();
			queryMap.put("areaStr", deliveryAreaStr);
			List<Map<String, Object>> deliveryAreaList = goodsDao.selectMDArea(queryMap);
			String delivery_desc = "";
			for (Map<String, Object> area : deliveryAreaList) {
				delivery_desc = delivery_desc + "|" + area.get("path");
			}
			if (delivery_desc.trim().length() > 0) {
				delivery_desc = delivery_desc.substring(1);
			}
			tempMap.put("delivery_area", StringUtil.formatStr(psGoods.getDelivery_area()));
			tempMap.put("delivery_desc", delivery_desc);
			resultList.add(tempMap);
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (!StringUtils.isEmpty(pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
			// 解析详情图片
			String pic_detail_info = StringUtil.formatStr(psGoods.getPic_detail_info());
			if (!StringUtils.isEmpty(pic_detail_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_detail_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void goldExchangeGoodsFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "area_id" });
		List<String> areaList = new ArrayList<String>();
		areaList.add((String) paramsMap.get("area_id"));
		areaList.add("100000");
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("area_code", paramsMap.get("area_id"));
		Map<String, Object> areaMap = goodsDao.selectAreaCodeTree(queryMap);
		if (areaMap != null) {
			if (null != areaMap.get("s_code") && !"".equals(areaMap.get("s_code"))) {
				areaList.add((String) areaMap.get("s_code"));
			}
			if (null != areaMap.get("t_code") && !"".equals(areaMap.get("t_code"))) {
				areaList.add((String) areaMap.get("t_code"));
			}
		}
		paramsMap.put("areaList", areaList);
		paramsMap.put("status", Constant.STATUS_ON_SHELF);
		paramsMap.put("category", "兑换");
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", psGoods.getGoods_id());
			tempMap.put("goods_code", StringUtil.formatStr(psGoods.getGoods_code()));
			tempMap.put("title", psGoods.getTitle());
			tempMap.put("desc1", StringUtil.formatStr(psGoods.getDesc1()));
			tempMap.put("area_id", psGoods.getArea_id());
			tempMap.put("label", StringUtil.formatStr(psGoods.getLabel()));
			tempMap.put("category", psGoods.getCategory());
			tempMap.put("display_area", psGoods.getDisplay_area());
			tempMap.put("contact_person", psGoods.getContact_person());
			tempMap.put("contact_tel", psGoods.getContact_tel());
			tempMap.put("specification", StringUtil.formatStr(psGoods.getSpecification()));
			tempMap.put("pack", StringUtil.formatStr(psGoods.getPack()));
			tempMap.put("market_price", psGoods.getMarket_price() + "");
			tempMap.put("discount_price", psGoods.getDiscount_price() + "");
			tempMap.put("product_source", StringUtil.formatStr(psGoods.getProduct_source()));
			tempMap.put("shipping_fee", psGoods.getShipping_fee() + "");
			tempMap.put("producer", StringUtil.formatStr(psGoods.getProducer()));
			tempMap.put("supplier", StringUtil.formatStr(psGoods.getSupplier()));
			tempMap.put("manufacturer", StringUtil.formatStr(psGoods.getManufacturer()));
			tempMap.put("min_buy_qty", psGoods.getMin_buy_qty() + "");
			tempMap.put("max_buy_qty", psGoods.getMax_buy_qty() + "");
			tempMap.put("goods_unit", StringUtil.formatStr(psGoods.getGoods_unit()));
			tempMap.put("payment_way", StringUtil.formatStr(psGoods.getPayment_way()));
			tempMap.put("pic_info", StringUtil.formatStr(psGoods.getPic_info()));
			tempMap.put("pic_detail_info", StringUtil.formatStr(psGoods.getPic_detail_info()));
			tempMap.put("sale_qty", psGoods.getSale_qty() + "");
			tempMap.put("stock_qty", psGoods.getStock_qty() + "");
			String deliver_date = DateUtil.getDistanceDays(psGoods.getValid_thru(),
					DateUtil.getFormatDate(DateUtil.fmt_yyyyMMdd));
			tempMap.put("deliver_date", deliver_date);
			// 解析区域
			String[] deliveryAreaStr = psGoods.getDelivery_area().split(",");
			queryMap.clear();
			queryMap.put("areaStr", deliveryAreaStr);
			List<Map<String, Object>> deliveryAreaList = goodsDao.selectMDArea(queryMap);
			String delivery_desc = "";
			for (Map<String, Object> area : deliveryAreaList) {
				delivery_desc = delivery_desc + "|" + area.get("path");
			}
			if (delivery_desc.trim().length() > 0) {
				delivery_desc = delivery_desc.substring(1);
			}
			tempMap.put("delivery_area", StringUtil.formatStr(psGoods.getDelivery_area()));
			tempMap.put("delivery_desc", delivery_desc);
			resultList.add(tempMap);
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (!pic_info.equals("")) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
			// 解析详情图片
			String pic_detail_info = StringUtil.formatStr(psGoods.getPic_detail_info());
			if (!pic_detail_info.equals("")) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_detail_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void psGoodsSkuForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id" });
		List<PsGoodsSku> psGoodsSkuList = psGoodsDao.selectPsGoodsSku(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoodsSku psGoodsSku : psGoodsSkuList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_stock_id", psGoodsSku.getGoods_stock_id());
			tempMap.put("sku", StringUtil.formatStr(psGoodsSku.getSku()));
			tempMap.put("desc1", StringUtil.formatStr(psGoodsSku.getDesc1()));
			tempMap.put("sales_price", psGoodsSku.getSales_price() + "");
			tempMap.put("sale_qty", psGoodsSku.getSale_qty() + "");
			tempMap.put("stock_qty", psGoodsSku.getStock_qty() + "");
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void psGoodsSkuForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id" });
		List<PsGoodsSku> psGoodsSkuList = psGoodsDao.selectPsGoodsSku(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoodsSku psGoodsSku : psGoodsSkuList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_stock_id", psGoodsSku.getGoods_stock_id());
			tempMap.put("sku", StringUtil.formatStr(psGoodsSku.getSku()));
			tempMap.put("desc1", StringUtil.formatStr(psGoodsSku.getDesc1()));
			tempMap.put("sales_price", psGoodsSku.getSales_price() + "");
			tempMap.put("sale_qty", psGoodsSku.getSale_qty() + "");
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void psGoodsDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		if (paramsMap.size() == 0) {
			throw new BusinessException(RspCode.SYSTEM_PARAM_MISS, "参数缺失");
		}
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(paramsMap);
		List<String> doc_ids = new ArrayList<String>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		// Map<String, Object> imgMap = new HashMap<String, Object>();
		// String data_source = "";
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", psGoods.getGoods_id());
			tempMap.put("goods_code", StringUtil.formatStr(psGoods.getGoods_code()));
			tempMap.put("title", psGoods.getTitle());
			tempMap.put("desc1", StringUtil.formatStr(psGoods.getDesc1()));
			tempMap.put("area_id", psGoods.getArea_id());
			tempMap.put("label", StringUtil.formatStr(psGoods.getLabel()));
			tempMap.put("label_promotion", StringUtil.formatStr(psGoods.getLabel_promotion()));
			tempMap.put("category", psGoods.getCategory());
			tempMap.put("data_source", StringUtil.formatStr(psGoods.getData_source()));
			tempMap.put("display_area", psGoods.getDisplay_area());
			tempMap.put("contact_person", psGoods.getContact_person());
			tempMap.put("contact_tel", psGoods.getContact_tel());
			tempMap.put("specification", StringUtil.formatStr(psGoods.getSpecification()));
			tempMap.put("agent", StringUtil.formatStr(psGoods.getAgent()));
			tempMap.put("pack", StringUtil.formatStr(psGoods.getPack()));
			tempMap.put("cost_price", psGoods.getCost_price() + "");
			tempMap.put("market_price", psGoods.getMarket_price() + "");
			tempMap.put("discount_price", psGoods.getDiscount_price() + "");
			tempMap.put("ladder_price", StringUtil.formatStr(psGoods.getLadder_price()));
			tempMap.put("product_source", StringUtil.formatStr(psGoods.getProduct_source()));
			tempMap.put("shipping_fee", psGoods.getShipping_fee() + "");
			tempMap.put("cost_allocation", psGoods.getCost_allocation() + "");
			tempMap.put("settled_price", psGoods.getSettled_price() + "");
			tempMap.put("ts_min_num", psGoods.getTs_min_num() + "");
			tempMap.put("ts_price", psGoods.getTs_price() + "");
			tempMap.put("ts_date", psGoods.getTs_date() + "");
			tempMap.put("service_fee", psGoods.getService_fee() + "");
			tempMap.put("producer", StringUtil.formatStr(psGoods.getProducer()));
			tempMap.put("supplier", StringUtil.formatStr(psGoods.getSupplier()));
			tempMap.put("supplier_id", StringUtil.formatStr(psGoods.getSupplier_id()));
			tempMap.put("warehouse", StringUtil.formatStr(psGoods.getWarehouse()));
			tempMap.put("warehouse_id", StringUtil.formatStr(psGoods.getWarehouse_id()));
			tempMap.put("manufacturer", StringUtil.formatStr(psGoods.getManufacturer()));
			tempMap.put("min_buy_qty", psGoods.getMin_buy_qty() + "");
			tempMap.put("max_buy_qty", psGoods.getMax_buy_qty() + "");
			tempMap.put("goods_unit", StringUtil.formatStr(psGoods.getGoods_unit()));
			tempMap.put("delivery", StringUtil.formatStr(psGoods.getDelivery()));
			tempMap.put("delivery_id", StringUtil.formatStr(psGoods.getDelivery_id()));
			tempMap.put("valid_thru", StringUtil.formatStr(psGoods.getValid_thru()));
			tempMap.put("payment_way", StringUtil.formatStr(psGoods.getPayment_way()));
			tempMap.put("status", psGoods.getStatus());
			tempMap.put("remark", psGoods.getRemark());
			tempMap.put("pic_info", StringUtil.formatStr(psGoods.getPic_info()));
			tempMap.put("pic_detail_info", StringUtil.formatStr(psGoods.getPic_detail_info()));
			tempMap.put("online_date", DateUtil.date2Str(psGoods.getOnline_date(), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("sale_qty", psGoods.getSale_qty() + "");
			tempMap.put("stock_qty", psGoods.getStock_qty() + "");
			tempMap.put("data_source_type", psGoods.getData_source_type());
			tempMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
			tempMap.put("voucher_link", StringUtil.formatStr(psGoods.getVoucher_link()));
			tempMap.put("discount_end", DateUtil.date2Str(psGoods.getDiscount_end(), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			tempMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
			// data_source = psGoods.getData_source();
			// tempMap.put("good_level", psGoods.getGood_level() + "");
			// tempMap.put("good_points", psGoods.getGood_point() + "");
			// 解析区域
			String[] areaStr = psGoods.getDelivery_area().split(",");
			params.clear();
			params.put("areaStr", areaStr);
			List<Map<String, Object>> areaList = goodsDao.selectMDArea(params);
			String delivery_desc = "";
			for (Map<String, Object> area : areaList) {
				delivery_desc = delivery_desc + "|" + area.get("path");
			}
			if (delivery_desc.trim().length() > 0) {
				delivery_desc = delivery_desc.substring(1);
			}
			tempMap.put("delivery_area", StringUtil.formatStr(psGoods.getDelivery_area()));
			tempMap.put("delivery_desc", delivery_desc);
			resultList.add(tempMap);
			// 解析图片
			// List<String> strList = new ArrayList<String>();
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			try {
				if (!pic_info.equals("")) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
					for (Map<String, Object> m : tempList) {
						// if(!StringUtil.isEmpty(psGoods.getData_source()) &&
						// psGoods.getData_source().equals("hsrj")){
						// strList.add(
						// StringUtil.formatStr(m.get("path_id"))+"");
						// }else{
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
						// }
					}
				}
				// tempMap.put("pic_info_url",strList);
				// 解析详情图片
				String pic_detail_info = StringUtil.formatStr(psGoods.getPic_detail_info());
				if (!pic_detail_info.equals("")) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_detail_info);
					for (Map<String, Object> m : tempList) {
						// if(!StringUtil.isEmpty(psGoods.getData_source()) &&
						// psGoods.getData_source().equals("hsrj")){
						// strList.add(
						// StringUtil.formatStr(m.get("path_id"))+"");
						// }else{
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
						// }
					}
				}
			} catch (Exception e) {
				map.put("doc_url", "");
				e.printStackTrace();
			}
			// imgMap.put("img_url",strList);
		}
		// if(!StringUtil.isEmpty(data_source) && data_source.equals("hsrj")){
		// map.put("doc_url",imgMap);
		// }else{
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		// }
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void psGoodsSupplyCount(Map<String, Object> paramsMap, ResultData result) throws Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "supplier_id" });
		Map<String, Object> map = psGoodsDao.selectPsGoodsSupplyCount(paramsMap);
		Map<String, Object> countMap = new HashMap<String, Object>();
		if (null != map) {
			countMap.put("lingleme", map.get("count_num") + "");
		}
		result.setResultData(countMap);
	}

	@Override
	public void psGoodsListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<PsGoods> list = psGoodsDao.selectPsGoods(paramsMap);
		psGoodlistFindResultSetup(result, list);
	}

	@Override
	public void selectPsGoodsAndFiterOffDay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<PsGoods> list = psGoodsDao.selectPsGoodsAndFiterOffDay(paramsMap);
		psGoodlistFindResultSetup(result, list);
	}

	private void psGoodlistFindResultSetup(ResultData result, List<PsGoods> list) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : list) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", psGoods.getGoods_id());
			tempMap.put("goods_code", StringUtil.formatStr(psGoods.getGoods_code()));
			tempMap.put("title", psGoods.getTitle());
			tempMap.put("desc1", StringUtil.formatStr(psGoods.getDesc1()));
			tempMap.put("label", StringUtil.formatStr(psGoods.getLabel()));
			tempMap.put("label_promotion", StringUtil.formatStr(psGoods.getLabel_promotion()));
			tempMap.put("category", psGoods.getCategory());
			tempMap.put("data_source", StringUtil.formatStr(psGoods.getData_source()));
			tempMap.put("display_area", psGoods.getDisplay_area());
			tempMap.put("contact_person", psGoods.getContact_person());
			tempMap.put("contact_tel", psGoods.getContact_tel());
			tempMap.put("specification", StringUtil.formatStr(psGoods.getSpecification()));
			tempMap.put("cost_price", psGoods.getCost_price() + "");
			tempMap.put("market_price", psGoods.getMarket_price() + "");
			tempMap.put("discount_price", psGoods.getDiscount_price() + "");
			tempMap.put("settled_price", psGoods.getSettled_price() + "");
			tempMap.put("ts_min_num", psGoods.getTs_min_num() + "");
			tempMap.put("ts_price", psGoods.getTs_price() + "");
			tempMap.put("ts_date", psGoods.getTs_date() + "");
			tempMap.put("service_fee", psGoods.getService_fee() + "");
			tempMap.put("product_source", StringUtil.formatStr(psGoods.getProduct_source()));
			tempMap.put("shipping_fee", psGoods.getShipping_fee() + "");
			tempMap.put("cost_allocation", psGoods.getCost_allocation() + "");
			tempMap.put("producer", StringUtil.formatStr(psGoods.getProducer()));
			tempMap.put("supplier", StringUtil.formatStr(psGoods.getSupplier()));
			tempMap.put("supplier_id", StringUtil.formatStr(psGoods.getSupplier_id()));
			tempMap.put("manufacturer", StringUtil.formatStr(psGoods.getManufacturer()));
			tempMap.put("min_buy_qty", psGoods.getMin_buy_qty() + "");
			tempMap.put("max_buy_qty", psGoods.getMax_buy_qty() + "");
			tempMap.put("goods_unit", StringUtil.formatStr(psGoods.getGoods_unit()));
			tempMap.put("warehouse", StringUtil.formatStr(psGoods.getWarehouse()));
			tempMap.put("delivery", StringUtil.formatStr(psGoods.getDelivery()));
			tempMap.put("valid_thru", StringUtil.formatStr(psGoods.getValid_thru()));
			tempMap.put("offline_date", DateUtil.date2Str(psGoods.getOffline_date(), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("payment_way", StringUtil.formatStr(psGoods.getPayment_way()));
			tempMap.put("status", psGoods.getStatus());
			tempMap.put("remark", StringUtil.formatStr(psGoods.getRemark()));
			tempMap.put("pic_info", StringUtil.formatStr(psGoods.getPic_info()));
			tempMap.put("sale_qty", psGoods.getSale_qty() + "");
			tempMap.put("stock_qty", psGoods.getStock_qty() + "");
			tempMap.put("data_source_type", psGoods.getData_source_type() + "");
			tempMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
			tempMap.put("created_date", DateUtil.date2Str(psGoods.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("voucher_link", StringUtil.formatStr(psGoods.getVoucher_link()));
			tempMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			tempMap.put("store_name", StringUtil.formatStr(psGoods.getTaobao_link()));
			tempMap.put("nowdiffOffline_date", StringUtil.formatStr(psGoods.getNowdiffOffline_date()));

			// 解析区域
			String[] areaStr = psGoods.getDelivery_area().split(",");
			params.clear();
			params.put("areaStr", areaStr);
			List<Map<String, Object>> areaList = goodsDao.selectMDArea(params);
			String delivery_desc = "";
			for (Map<String, Object> area : areaList) {
				delivery_desc = delivery_desc + "|" + area.get("path");
			}
			if (delivery_desc.trim().length() > 0) {
				delivery_desc = delivery_desc.substring(1);
			}
			tempMap.put("delivery_area", StringUtil.formatStr(psGoods.getDelivery_area()));
			tempMap.put("delivery_desc", delivery_desc);
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void newestPsGoodsListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<PsGoods> list = psGoodsDao.selectNewestPsGoodsForOp(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : list) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", psGoods.getGoods_id());
			tempMap.put("goods_code", StringUtil.formatStr(psGoods.getGoods_code()));
			tempMap.put("title", psGoods.getTitle());
			tempMap.put("desc1", StringUtil.formatStr(psGoods.getDesc1()));
			tempMap.put("label", StringUtil.formatStr(psGoods.getLabel()));
			tempMap.put("label_promotion", StringUtil.formatStr(psGoods.getLabel_promotion()));
			tempMap.put("category", psGoods.getCategory());
			tempMap.put("data_source", StringUtil.formatStr(psGoods.getData_source()));
			tempMap.put("display_area", psGoods.getDisplay_area());
			tempMap.put("contact_person", psGoods.getContact_person());
			tempMap.put("contact_tel", psGoods.getContact_tel());
			tempMap.put("specification", StringUtil.formatStr(psGoods.getSpecification()));
			tempMap.put("cost_price", psGoods.getCost_price() + "");
			tempMap.put("market_price", psGoods.getMarket_price() + "");
			tempMap.put("discount_price", psGoods.getDiscount_price() + "");
			tempMap.put("settled_price", psGoods.getSettled_price() + "");
			tempMap.put("service_fee", psGoods.getService_fee() + "");
			tempMap.put("product_source", StringUtil.formatStr(psGoods.getProduct_source()));
			tempMap.put("shipping_fee", psGoods.getShipping_fee() + "");
			tempMap.put("producer", StringUtil.formatStr(psGoods.getProducer()));
			tempMap.put("supplier", StringUtil.formatStr(psGoods.getSupplier()));
			tempMap.put("supplier_id", StringUtil.formatStr(psGoods.getSupplier_id()));
			tempMap.put("manufacturer", StringUtil.formatStr(psGoods.getManufacturer()));
			tempMap.put("min_buy_qty", psGoods.getMin_buy_qty() + "");
			tempMap.put("max_buy_qty", psGoods.getMax_buy_qty() + "");
			tempMap.put("goods_unit", StringUtil.formatStr(psGoods.getGoods_unit()));
			tempMap.put("warehouse", StringUtil.formatStr(psGoods.getWarehouse()));
			tempMap.put("delivery", StringUtil.formatStr(psGoods.getDelivery()));
			tempMap.put("valid_thru", StringUtil.formatStr(psGoods.getValid_thru()));
			tempMap.put("status", psGoods.getStatus());
			tempMap.put("remark", StringUtil.formatStr(psGoods.getRemark()));
			tempMap.put("pic_info", StringUtil.formatStr(psGoods.getPic_info()));
			tempMap.put("sale_qty", psGoods.getSale_qty() + "");
			tempMap.put("stock_qty", psGoods.getStock_qty() + "");
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void psGoodsFindForOrder(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		// goods_id 和 goods_code 必须有一个
		ValidateUtil.validateParamsNum(paramsMap, new String[] { "goods_id", "goods_code" }, 1);
		Map<String, Object> psGoodsMap = psGoodsDao.selectPsGoodsForOrder(paramsMap);
		if (null == psGoodsMap) {
			throw new BusinessException(RspCode.PS_GOODS_NOT_EXIST, RspCode.MSG.get(RspCode.PS_GOODS_NOT_EXIST));
		}
		result.setResultData(psGoodsMap);
	}

	@Override
	public void psGoodsFindForTsActivity(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "goods_id" });
		Map<String, Object> psGoodsMap = psGoodsDao.selectPsGoodsForTsActivity(paramsMap);
		if (null == psGoodsMap) {
			throw new BusinessException(RspCode.PS_GOODS_NOT_EXIST, RspCode.MSG.get(RspCode.PS_GOODS_NOT_EXIST));
		}
		result.setResultData(psGoodsMap);
	}

	@Override
	public void psGoodsActivityForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_type" });
		List<Map<String, Object>> goodsActivityList = psGoodsDao.selectPsGoodsActivityForOpList(paramsMap);
		List<Map<String, Object>> activityList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> activityMap : goodsActivityList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", activityMap.get("goods_id"));
			tempMap.put("goods_code", activityMap.get("goods_code"));
			tempMap.put("title", activityMap.get("title"));
			tempMap.put("pic_info", activityMap.get("pic_info"));
			activityList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", activityList);
		result.setResultData(map);
	}

	/**
	 * 推荐商品创建
	 * 
	 * @param consumer_id
	 * @param seller_id
	 * @param voucher_amount
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handlePsGoodsActivitySync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_params", "activity_type" });
		String activity_type = StringUtil.formatStr(paramsMap.get("activity_type"));

		// 清除推荐商品对应的缓存数据
		String key = "[psGoodsActivityListFind]" + activity_type;
		redisUtil.del(key);
		key = "[psGoodsActivityHomeForConsumerFind]" + activity_type;
		redisUtil.del(key);

		List<Map<String, Object>> activityParamsList = FastJsonUtil
				.jsonToList((String) paramsMap.get("activity_params"));
		if(activityParamsList.size() > 0){
			// 删除推荐商品类型
			psGoodsDao.deletePsGoodsActivity(activity_type);
			for (Map<String, Object> activity : activityParamsList) {
				ValidateUtil.validateParams(activity, new String[] { "order_no", "goods_id", "pic_info" });
				PsGoodsActivity psGoodsActivity = new PsGoodsActivity();
				BeanConvertUtil.mapToBean(psGoodsActivity, activity);
				psGoodsActivity.setActivity_type(activity_type);
				psGoodsActivity.setGoods_activity_id(IDUtil.getUUID());
				psGoodsActivity.setStart_date(new Date());
				psGoodsActivity.setCreated_date(new Date());
				psGoodsActivity.setIs_finished("N");
				psGoodsDao.insertPsGoodsActivity(psGoodsActivity);
			}
		}else {
			psGoodsDao.deletePsGoodsActivity(activity_type);
		}
	}

	@Override
	public void psGoodsActivityListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_type" });
		String activity_type = StringUtil.formatStr(paramsMap.get("activity_type"));
		String key = "[psGoodsActivityListFind]" + activity_type;
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}

		List<Map<String, Object>> goodsActivityList = psGoodsDao.selectPsGoodsActivityList(paramsMap);
		List<Map<String, Object>> activityList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> activityMap : goodsActivityList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", activityMap.get("goods_id"));
			tempMap.put("goods_code", activityMap.get("goods_code"));
			tempMap.put("label_promotion", StringUtil.formatStr(activityMap.get("label_promotion")));
			tempMap.put("data_source", StringUtil.formatStr(activityMap.get("data_source")));
			tempMap.put("title", activityMap.get("title"));
			tempMap.put("market_price", activityMap.get("market_price") + "");
			// 如果是伙拼团推荐,优惠价取成团价格
			if (activity_type.equals("HDMS_06")) {
				tempMap.put("discount_price", activityMap.get("ts_price") + "");
			} else {
				tempMap.put("discount_price", activityMap.get("discount_price") + "");
			}
			tempMap.put("specification", StringUtil.formatStr(activityMap.get("specification")));
			tempMap.put("supplier", StringUtil.formatStr(activityMap.get("supplier")));
			tempMap.put("min_buy_qty", StringUtil.formatStr(activityMap.get("min_buy_qty")));
			tempMap.put("valid_thru", StringUtil.formatStr(activityMap.get("valid_thru")));
			tempMap.put("sale_qty", activityMap.get("sale_qty") + "");
			tempMap.put("stock_qty", activityMap.get("stock_qty") + "");

			// 解析图片
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList((String) activityMap.get("pic_info"));
			for (Map<String, Object> m : tempList) {
				if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
					String path_id = m.get("path_id") + "";
					String pic_info_url = docUtil.imageUrlFind(path_id);
					tempMap.put("pic_info_url", pic_info_url);
					break;
				}
			}
			activityList.add(tempMap);
		}
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("list", activityList);
		result.setResultData(resultDataMap);

		redisUtil.setObj(key, resultDataMap, 3600);
	}

	@Deprecated
	@Override
	public void psGoodsActivityFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_type" });
		String activity_type = StringUtil.formatStr(paramsMap.get("activity_type"));
		String key = "[psGoodsActivityFind]" + activity_type;
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}

		List<Map<String, Object>> goodsActivityList = psGoodsDao.selectPsGoodsActivityList(paramsMap);
		List<Map<String, Object>> activityList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		for (Map<String, Object> activityMap : goodsActivityList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", activityMap.get("goods_id"));
			tempMap.put("goods_code", activityMap.get("goods_code"));
			tempMap.put("label_promotion", StringUtil.formatStr(activityMap.get("label_promotion")));
			tempMap.put("data_source", StringUtil.formatStr(activityMap.get("data_source")));
			tempMap.put("title", activityMap.get("title"));
			tempMap.put("pic_info", activityMap.get("pic_info"));
			tempMap.put("market_price", activityMap.get("market_price") + "");
			// 如果是伙拼团推荐,优惠价取成团价格
			if (activity_type.equals("HDMS_06")) {
				tempMap.put("discount_price", activityMap.get("ts_price") + "");
			} else {
				tempMap.put("discount_price", activityMap.get("discount_price") + "");
			}
			tempMap.put("specification", StringUtil.formatStr(activityMap.get("specification")));
			tempMap.put("supplier", StringUtil.formatStr(activityMap.get("supplier")));
			tempMap.put("min_buy_qty", StringUtil.formatStr(activityMap.get("min_buy_qty")));
			tempMap.put("valid_thru", StringUtil.formatStr(activityMap.get("valid_thru")));
			tempMap.put("sale_qty", activityMap.get("sale_qty") + "");
			tempMap.put("stock_qty", activityMap.get("stock_qty") + "");

			// 解析图片
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList((String) tempMap.get("pic_info"));
			for (Map<String, Object> m : tempList) {
				if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
					doc_ids.add(m.get("path_id") + "");
					break;
				}
			}
			activityList.add(tempMap);
		}
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		Map<String, Object> doc_ids_url = docUtil.imageUrlFind(doc_ids);
		resultDataMap.put("doc_url", doc_ids_url);
		resultDataMap.put("list", activityList);
		result.setResultData(resultDataMap);

		// 设置缓存
		if (doc_ids_url.size() > 0) {
			redisUtil.setObj(key, resultDataMap, 3600);
		}
	}

	
	@Override
	public void freeGetGoodsActivityHomeListFindForSmallProgram(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_type" });
		String activity_type = StringUtil.formatStr(paramsMap.get("activity_type"));
		String key = "[freeGetGoodsActivityHomeListFindForSmallProgram]" + activity_type;
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}

		List<Map<String, Object>> goodsActivityList = psGoodsDao.selectPsGoodsActivityHomeListForSmallProgram(paramsMap);
		List<Map<String, Object>> activityList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		for (Map<String, Object> activityMap : goodsActivityList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", activityMap.get("goods_id"));
			tempMap.put("title", activityMap.get("title"));
			tempMap.put("desc1", StringUtil.formatStr(activityMap.get("desc1")));
			tempMap.put("pic_info", activityMap.get("pic_info"));
			tempMap.put("market_price", activityMap.get("market_price") + "");
			tempMap.put("data_source", activityMap.get("data_source") + "");

			// 如果是伙拼团推荐,优惠价取成团价格
			if (activity_type.equals("HDMS_06")) {
				tempMap.put("ts_min_num", activityMap.get("ts_min_num"));
				tempMap.put("discount_price", activityMap.get("ts_price") + "");
			} else {
				tempMap.put("discount_price", activityMap.get("discount_price") + "");
			}
			tempMap.put("valid_thru", StringUtil.formatStr(activityMap.get("valid_thru")));
			// 解析图片
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList((String) tempMap.get("pic_info"));
			for (Map<String, Object> m : tempList) {
				if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
					doc_ids.add(m.get("path_id") + "");
				}
			}
			activityList.add(tempMap);
		}

		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		Map<String, Object> doc_ids_url = docUtil.imageUrlFind(doc_ids);
		resultDataMap.put("doc_url", doc_ids_url);
		resultDataMap.put("list", activityList);
		result.setResultData(resultDataMap);

		// 设置缓存
		if (doc_ids_url.size() > 0) {
			redisUtil.setObj(key, resultDataMap, 3600);
		}
	}
	
	
	@Override
	public void psGoodsActivityHomeForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_type" });
		String activity_type = StringUtil.formatStr(paramsMap.get("activity_type"));
		String key = "[psGoodsActivityHomeForConsumerFind]" + activity_type;
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}

		List<Map<String, Object>> goodsActivityList = psGoodsDao.selectPsGoodsActivityHomeListForConsumer(paramsMap);
		List<Map<String, Object>> activityList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		for (Map<String, Object> activityMap : goodsActivityList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", activityMap.get("goods_id"));
			tempMap.put("title", activityMap.get("title"));
			tempMap.put("desc1", StringUtil.formatStr(activityMap.get("desc1")));
			tempMap.put("pic_info", activityMap.get("pic_info"));
			tempMap.put("market_price", activityMap.get("market_price") + "");
			// 如果是伙拼团推荐,优惠价取成团价格
			if (activity_type.equals("HDMS_06")) {
				tempMap.put("ts_min_num", activityMap.get("ts_min_num"));
				tempMap.put("discount_price", activityMap.get("ts_price") + "");
			} else {
				tempMap.put("discount_price", activityMap.get("discount_price") + "");
			}
			tempMap.put("valid_thru", StringUtil.formatStr(activityMap.get("valid_thru")));
			// 解析图片
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList((String) tempMap.get("pic_info"));
			for (Map<String, Object> m : tempList) {
				if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
					doc_ids.add(m.get("path_id") + "");
				}
			}
			activityList.add(tempMap);
		}

		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		Map<String, Object> doc_ids_url = docUtil.imageUrlFind(doc_ids);
		resultDataMap.put("doc_url", doc_ids_url);
		resultDataMap.put("list", activityList);
		result.setResultData(resultDataMap);

		// 设置缓存
		if (doc_ids_url.size() > 0) {
			redisUtil.setObj(key, resultDataMap, 3600);
		}
	}

	@Override
	public void goodsLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> goodsLogList = psGoodsDao.selectGoodsLog(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> goodsLogMap : goodsLogList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("log_id", StringUtil.formatStr(goodsLogMap.get("log_id")));
				tempMap.put("goods_id", StringUtil.formatStr(goodsLogMap.get("goods_id")));
				tempMap.put("category", StringUtil.formatStr(goodsLogMap.get("category")));
				tempMap.put("tracked_date",
						DateUtil.date2Str((Date) goodsLogMap.get("tracked_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("event", StringUtil.formatStr(goodsLogMap.get("event")));
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void goodsLogAdd(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "goods_id", "category", "event" });
			PsGoodsLog psGoodsLog = new PsGoodsLog();
			Date date = new Date();
			BeanConvertUtil.mapToBean(psGoodsLog, paramsMap);
			psGoodsLog.setLog_id(IDUtil.getUUID());
			psGoodsLog.setTracked_date(date);
			psGoodsDao.insertGoodsLog(psGoodsLog);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void gdViewSellDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> viewSellDetailList = psGoodsDao.selectGdViewSellDetail(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> viewSellDetailMap : viewSellDetailList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("goods_id", StringUtil.formatStr(viewSellDetailMap.get("goods_id")));
				tempMap.put("goods_code", StringUtil.formatStr(viewSellDetailMap.get("goods_code")));
				tempMap.put("title", StringUtil.formatStr(viewSellDetailMap.get("title")));
				tempMap.put("desc1", StringUtil.formatStr(viewSellDetailMap.get("desc1")));
				tempMap.put("label", StringUtil.formatStr(viewSellDetailMap.get("label")));
				tempMap.put("specification", StringUtil.formatStr(viewSellDetailMap.get("specification")));
				tempMap.put("pack", StringUtil.formatStr(viewSellDetailMap.get("pack")));
				tempMap.put("market_price", viewSellDetailMap.get("market_price") + "");
				tempMap.put("discount_price", viewSellDetailMap.get("discount_price") + "");
				tempMap.put("product_source", StringUtil.formatStr(viewSellDetailMap.get("product_source") + ""));
				tempMap.put("shipping_fee", viewSellDetailMap.get("shipping_fee") + "");
				tempMap.put("producer", StringUtil.formatStr(viewSellDetailMap.get("producer")));
				tempMap.put("supplier", StringUtil.formatStr(viewSellDetailMap.get("supplier")));
				tempMap.put("manufacturer", StringUtil.formatStr(viewSellDetailMap.get("manufacturer")));
				tempMap.put("min_buy_qty", viewSellDetailMap.get("min_buy_qty") + "");
				tempMap.put("max_buy_qty", viewSellDetailMap.get("max_buy_qty") + "");
				tempMap.put("goods_unit", StringUtil.formatStr(viewSellDetailMap.get("goods_unit")));
				tempMap.put("payment_way", StringUtil.formatStr(viewSellDetailMap.get("payment_way")));
				tempMap.put("pic_info", StringUtil.formatStr(viewSellDetailMap.get("pic_info")));
				tempMap.put("sale_qty", viewSellDetailMap.get("sale_qty") + "");
				tempMap.put("stock_qty", viewSellDetailMap.get("stock_qty") + "");
				tempMap.put("view", viewSellDetailMap.get("view") == null ? '0' : viewSellDetailMap.get("view") + "");
				tempMap.put("sell", viewSellDetailMap.get("sell") == null ? '0' : viewSellDetailMap.get("sell") + "");
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void gdViewAdd(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "goods_id" });
			String goods_id = paramsMap.get("goods_id") + "";
			Map<String, Integer> viewMap = new HashMap<String, Integer>();
			Object obj = redisUtil.getObj("ps_goods_view");
			if (obj != null) {
				viewMap = (Map<String, Integer>) redisUtil.getObj("ps_goods_view");
			}
			Integer view_count = viewMap.get(goods_id) == null ? 0 : viewMap.get(goods_id);
			viewMap.put(goods_id, view_count + 1);
			redisUtil.setObj("ps_goods_view", viewMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void gdSellAdd(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "goods_id", "sell_qty" });
			int updateFlag = psGoodsDao.updateGdViewSell(paramsMap);
			if (updateFlag == 0) { // 更新失败
				// 插入商品记录
				GdViewSell gdViewSell = new GdViewSell();
				gdViewSell.setGoods_id((String) paramsMap.get("goods_id"));
				gdViewSell.setView(1);
				gdViewSell.setSell(1);
				psGoodsDao.insertGdViewSell(gdViewSell);
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
	public void freeGetGoodsListBySearchFindForSmallProgram(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// ValidateUtil.validateParams(paramsMap, new String[] {"search"});

			// 后端暂时不返回字段给前段 当search字段为空时 等下次前端发版之后改回来 2017-09-18 丁忍
			Object tempObj = paramsMap.get("search");
			if (tempObj == null) {
				return;
			}

			String key = "[freeGetGoodsListBySearchFindForSmallProgram]" + paramsMap.get("search");
			Object obj = redisUtil.getObj(key);
			if (null != obj) {
				result.setResultData(obj);
				return;
			}
			// 查询可以购买的商品
			paramsMap.put("valid_thru_end", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
			List<PsGoods> psGoodsList = psGoodsDao.selectfreeGetGoodsListBySearchForSmallProgram(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (PsGoods psGoods : psGoodsList) {
				Map<String, Object> psGoodsMap = new HashMap<String, Object>();
				psGoodsMap.put("goods_id", psGoods.getGoods_id());
				psGoodsMap.put("title", psGoods.getTitle());
				psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
				psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
				psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
				psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
				psGoodsMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
				psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
				psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
				psGoodsMap.put("data_source", StringUtil.formatStr(psGoods.getData_source()));
				// 解析图片
				String pic_info = StringUtil.formatStr(psGoods.getPic_info());
				if (StringUtil.isNotEmpty(pic_info)) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							String path_id = m.get("path_id") + "";
							String pic_info_url = docUtil.imageUrlFind(path_id);
							psGoodsMap.put("pic_info_url", pic_info_url);
							break;
						}
					}
				}
				resultList.add(psGoodsMap);
			}
			Map<String, Object> resultDataMap = new HashMap<String, Object>();
			resultDataMap.put("list", resultList);
			result.setResultData(resultDataMap);
			// 设置缓存
			redisUtil.setObj(key, resultDataMap, 300);
			freeGetGoodsListCacheKeySet.add(key);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Override
	public void freeGetGoodsListBySearchFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// ValidateUtil.validateParams(paramsMap, new String[] {"search"});

			// 后端暂时不返回字段给前段 当search字段为空时 等下次前端发版之后改回来 2017-09-18 丁忍
			Object tempObj = paramsMap.get("search");
			if (tempObj == null) {
				return;
			}

			String key = "[freeGetGoodsListBySearchFind]" + paramsMap.get("search");
			Object obj = redisUtil.getObj(key);
			if (null != obj) {
				result.setResultData(obj);
				return;
			}
			// 查询可以购买的商品
			paramsMap.put("valid_thru_end", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
			List<PsGoods> psGoodsList = psGoodsDao.selectfreeGetGoodsListBySearch(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (PsGoods psGoods : psGoodsList) {
				Map<String, Object> psGoodsMap = new HashMap<String, Object>();
				psGoodsMap.put("goods_id", psGoods.getGoods_id());
				psGoodsMap.put("title", psGoods.getTitle());
				psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
				psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
				psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
				psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
				psGoodsMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
				psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
				psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
				// 解析图片
				String pic_info = StringUtil.formatStr(psGoods.getPic_info());
				if (StringUtil.isNotEmpty(pic_info)) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							String path_id = m.get("path_id") + "";
							String pic_info_url = docUtil.imageUrlFind(path_id);
							psGoodsMap.put("pic_info_url", pic_info_url);
							break;
						}
					}
				}
				resultList.add(psGoodsMap);
			}
			Map<String, Object> resultDataMap = new HashMap<String, Object>();
			resultDataMap.put("list", resultList);
			result.setResultData(resultDataMap);
			// 设置缓存
			redisUtil.setObj(key, resultDataMap, 300);
			freeGetGoodsListCacheKeySet.add(key);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void freeGetGoodsForOperateListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		String key = "[freeGetGoodsForOperateListFind]";
		Object obj = redisUtil.getObj(key);
		if (null != obj) {
			result.setResultData(obj);
			return;
		}
		// 查询来源是花生日记
		paramsMap.put("data_source", "hsrj");
		// 商品状态 上架的
		paramsMap.put("status", "on_shelf");

		List<PsGoods> psGoodsList = psGoodsDao.selectFgGoodsListForOperate(paramsMap);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (PsGoods psGoods : psGoodsList) {
			Map<String, Object> psGoodsMap = new HashMap<String, Object>();
			psGoodsMap.put("goods_id", psGoods.getGoods_id());
			psGoodsMap.put("title", psGoods.getTitle());
			psGoodsMap.put("market_price", psGoods.getMarket_price() + "");
			psGoodsMap.put("discount_price", psGoods.getDiscount_price() + "");
			psGoodsMap.put("sale_qty", psGoods.getSale_qty() + "");
			psGoodsMap.put("stock_qty", psGoods.getStock_qty() + "");
			psGoodsMap.put("taobao_price", StringUtil.formatStr(psGoods.getTaobao_price()));
			psGoodsMap.put("data_source", StringUtil.formatStr(psGoods.getData_source()));
			psGoodsMap.put("status", StringUtil.formatStr(psGoods.getStatus()));
			psGoodsMap.put("goods_code", StringUtil.formatStr(psGoods.getGoods_code()));
			psGoodsMap.put("commission_rate", StringUtil.formatStr(psGoods.getCommission_rate()));
			psGoodsMap.put("taobao_link", StringUtil.formatStr(psGoods.getTaobao_link()));
			psGoodsMap.put("taobao_sales", StringUtil.formatStr(psGoods.getTaobao_sales()));
			psGoodsMap.put("discount_end",
					StringUtil.formatStr(DateUtil.date2Str(psGoods.getDiscount_end(), DateUtil.fmt_yyyyMMddHHmmss)));
			// 解析图片
			String pic_info = StringUtil.formatStr(psGoods.getPic_info());
			if (StringUtil.isNotEmpty(pic_info)) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						String path_id = m.get("path_id") + "";
						String pic_info_url = docUtil.imageUrlFind(path_id);
						psGoodsMap.put("pic_info_url", pic_info_url);
						break;
					}
				}
			}
			resultList.add(psGoodsMap);
		}
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("list", resultList);
		result.setResultData(resultDataMap);
		// 设置缓存
		redisUtil.setObj(key, resultDataMap, 300);
	}



	



	
}
