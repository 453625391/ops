package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.meitianhui.goods.entity.GdViewSell;
import com.meitianhui.goods.entity.PsGoods;
import com.meitianhui.goods.entity.PsGoodsActivity;
import com.meitianhui.goods.entity.PsGoodsLog;
import com.meitianhui.goods.entity.PsGoodsSku;

public interface PsGoodsDao {

	/**
	 * 自营商品新增
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertPsGoods(PsGoods psGoods) throws Exception;

	/**
	 * 自营商品SKU信息新增
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertPsGoodsSku(PsGoodsSku psGoodsSku) throws Exception;

	/**
	 * 每日抢活动
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertPsGoodsActivity(PsGoodsActivity psGoodsActivity) throws Exception;

	/**
	 * 新增商品查看销售统计记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void insertGdViewSell(GdViewSell gdViewSell) throws Exception;

	/**
	 * 添加商品操作日志
	 * 
	 * @param psGoodsLog
	 * @throws Exception
	 */
	void insertGoodsLog(PsGoodsLog psGoodsLog) throws Exception;

	
	/**
	 * 查询预售商品信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectPsGoods(Map<String, Object> map) throws Exception;


	/**
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectPsGoodsAndFiterOffDay(Map<String, Object> map) throws Exception;

	/**
	 * 查询供应商“已上架的“(领了么)商品总数
	 *    
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectPsGoodsSupplyCount(Map<String, Object> map) throws Exception;

	/**
	 * 查询预售商品SKU信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoodsSku> selectPsGoodsSku(Map<String, Object> map) throws Exception;

	/**
	 * 运营系统查询最新的商品信息，按创建时间倒序(去除状态为删除的)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectNewestPsGoodsForOp(Map<String, Object> map) throws Exception;


	/**
	 * 查售商品信息(app)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectPsGoodsForWeb(Map<String, Object> map) throws Exception;

	/**
	 * 查询领了么预售商品信息(app)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectfreeGetGoodsPreSaleForH5(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询领了么预售商品信息(小程序端)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectfreeGetGoodsPreSaleForSmallProgram(Map<String, Object> map) throws Exception;
	
	/**
	 * 根据标签查询淘淘领商品 小程序端
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectfreeGetGoodsListByLabelForSmallProgram(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询淘淘领商品（根据标签查询）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectfreeGetGoodsListByLabel(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询淘淘领商品（根据搜索条件查询） 小程序端
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectfreeGetGoodsListBySearchForSmallProgram(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询淘淘领商品（根据搜索条件查询）APP端
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectfreeGetGoodsListBySearch(Map<String, Object> map) throws Exception;
	
	/**
	 * 搜索查询淘淘领商品列表对应运营平台
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectFgGoodsListForOperate(Map<String, Object> map) throws Exception;
	
	/**
	 * 领了么商品信息H5
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectFgGoodsListForH5(Map<String, Object> map) throws Exception;
	
	/**
	 * 领了么商品查询 小程序端
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectFgGoodsListForSmallProgram(Map<String, Object> map) throws Exception;
	/**
	 * 查询商品信息(H5)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<PsGoods> selectPsGoodsForH5(Map<String, Object> map) throws Exception;

	/**
	 * 订单中goods_id对应的预售商品的基本信息查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectPsGoodsForOrder(Map<String, Object> map) throws Exception;
	
	/**
	 * 伙拼团查询商品信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectPsGoodsForTsActivity(Map<String, Object> map) throws Exception;

	/**
	 * 查询推荐商品
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPsGoodsActivityList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询首页推荐商品 小程序端
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPsGoodsActivityHomeListForSmallProgram(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询首页推荐商品 APP端
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPsGoodsActivityHomeListForConsumer(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询推荐商品列表(运营)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPsGoodsActivityForOpList(Map<String, Object> map) throws Exception;

	/**
	 * 查询商品查看销售统计记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectGdViewSellDetail(Map<String, Object> map) throws Exception;

	/**
	 * 查询商品操作日志
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectGoodsLog(Map<String, Object> map) throws Exception;

	/**
	 * 商品置底
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updatePsGoodsBottom(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新预售商品信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updatePsGoods(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新预售商品SKU信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updatePsGoodsSku(Map<String, Object> map) throws Exception;
	
	/**
	 * 逻辑删除商品，更新商品status属性
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updatePsGoodsStatus(Map<String, Object> map) throws Exception;

	/**
	 * 更新商品查看销售统计记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updateGdViewSell(Map<String, Object> map) throws Exception;

	/**
	 * 自营商品库存恢复
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updatePsGoodsSaleQtyRestore(Map<String, Object> map) throws Exception;

	/**
	 * 自营商品库存扣减
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updatePsGoodsSaleQtyDeduction(Map<String, Object> map) throws Exception;

	/**
	 * 我要批商品库存扣减
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updateWypGoodsSaleQtyDeduction(Map<String, Object> map) throws Exception;

	/**
	 * 我要批商品库存扣减
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updateWypGoodsSkuSaleQtyDeduction(Map<String, Object> map) throws Exception;

	/**
	 * 我要批商品库存恢复
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updateWypGoodsSaleQtyRestore(Map<String, Object> map) throws Exception;

	/**
	 * 我要批商品库存恢复
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updateWypGoodsSkuSaleQtyRestore(Map<String, Object> map) throws Exception;
	
	/**
	 * 推荐商品删除
	 * 
	 * @param activity_type
	 * @return
	 * @throws Exception
	 */
	Integer deletePsGoodsActivity(String activity_type) throws Exception;

}
