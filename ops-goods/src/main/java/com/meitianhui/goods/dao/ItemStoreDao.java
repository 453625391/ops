package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.goods.entity.GdItem;
import com.meitianhui.goods.entity.GdItemStore;

public interface ItemStoreDao {

	/**
	 * 加盟店商品新增
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertGdItemStore(GdItemStore gdItem) throws Exception;

	/**
	 * 查询标准商品信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	GdItem selectGdItem(Map<String, Object> map) throws Exception;

	/**
	 * 查询商品信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> selectGdItemStoreForOrder(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询商品信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<GdItemStore> selectGdItemStore(Map<String, Object> map) throws Exception;
	

	/**
	 * 查询商品列表信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<GdItemStore> selectGdItemStoreList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询门店商品类型分组信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> selectGdItemStoreTypeGroup(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询可以兑换的商品列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectGdItemStoreForNearByExchange(Map<String, Object> map) throws Exception;

	/**
	 * 查询门店开通网店销售的商品数量
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectIsSellGoodsCount(Map<String, Object> map) throws Exception;

	
	/**
	 * 更新加盟店商品
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void updateGdItemStore(Map<String, Object> map) throws Exception;

	/**
	 * 精选特卖库存恢复
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updateGdItemStoreSaleQtyRestore(Map<String, Object> map) throws Exception;

	/**
	 * 精选特卖商品库存扣减
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer updateGdItemStoreSaleQtyDeduction(Map<String, Object> map) throws Exception;

	
	/**
	 * 删除加盟店商品
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void deletedItemStore(Map<String, Object> map) throws Exception;


}
