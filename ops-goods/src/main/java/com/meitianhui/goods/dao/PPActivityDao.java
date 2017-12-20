package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.goods.entity.PPActivity;
import com.meitianhui.goods.entity.PPActivityDetail;

public interface PPActivityDao {


	/**
	 * 添加新品秀活动
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void insertPPActivity(PPActivity ppActivity) throws Exception;

	/**
	 * 报名参加新品秀活动
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void insertPPActivityDetail(PPActivityDetail ppActivityDetail) throws Exception;

	
	/**
	 * 查询新品秀列表/详情（运营接口）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPPActivity(Map<String, Object> map) throws Exception;

	/**
	 * 查询新品秀列表/详情(h5接口)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPPActivityForWeb(Map<String, Object> map) throws Exception;


	/**
	 * 查询新品秀报名列表/详情
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectPPActivityDetail(Map<String, Object> map) throws Exception;


	/**
	 * 修改新品秀活动
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updatePPActivity(Map<String, Object> map) throws Exception;

	/**
	 * 修改新品秀活动数量
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updatePPActivityNum(Map<String, Object> map) throws Exception;

	/**
	 * 修改新品秀活动报名
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updatePPActivityDetail(Map<String, Object> map) throws Exception;

	/**
	 * 删除新品秀活动
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void deletePPActivity(Map<String, Object> map) throws Exception;

	/**
	 * 删除新品秀活动报名
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void deletePPActivityDetail(Map<String, Object> map) throws Exception;

}
