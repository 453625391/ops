package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.goods.entity.PsGoodsFavorites;

/**
 * 商品收藏
 * 
 * @ClassName: PsGoodsFavoritesDao
 * @author tiny
 * @date 2017年4月5日 下午6:14:33
 *
 */
public interface PsGoodsFavoritesDao {

	/**
	 * 添加收藏的商品
	 * 
	 * @Title: insertPsGoodsFavorites
	 * @param GdActivity
	 * @throws Exception
	 * @author tiny
	 */
	void insertPsGoodsFavorites(PsGoodsFavorites psGoodsFavorites) throws Exception;

	/**
	 * 删除收藏的商品
	 * 
	 * @Title: deletePsGoodsFavorites
	 * @param GdActivity
	 * @throws Exception
	 * @author tiny
	 */
	void deletePsGoodsFavorites(Map<String, Object> map) throws Exception;

	/**
	 * 查询领了么收藏的商品
	 * 
	 * @Title: selectPsGoodsFavorites
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	PsGoodsFavorites selectPsGoodsFavorites(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 查询领了么收藏的商品列表
	 * 
	 * @Title: selectFgGoodsFavoritesListForApp
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	List<Map<String, Object>> selectFgGoodsFavoritesListForApp(Map<String, Object> map) throws Exception;

	
	/**
	 * 查询伙拼团收藏的商品列表
	 * 
	 * @Title: selectTsGoodsFavoritesListForApp
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	List<Map<String, Object>> selectTsGoodsFavoritesListForApp(Map<String, Object> map) throws Exception;

}
