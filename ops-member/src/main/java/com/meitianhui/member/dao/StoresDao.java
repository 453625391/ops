package com.meitianhui.member.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.member.entity.MDStores;
import com.meitianhui.member.entity.MDStoresRecommend;

public interface StoresDao {

	/**
	 * 便利店信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertMDStores(MDStores mDStores) throws Exception;

	/**
	 * 便利店信息日志
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertMDStoresLog(Map<String, Object> map) throws Exception;

	/**
	 * 联盟商门店推荐
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void insertMDStoresRecommend(MDStoresRecommend mDStoresRecommend) throws Exception;

	/**
	 * 查询门店基本信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectMDStoresBaseInfo(Map<String, Object> map) throws Exception;

	/**
	 * 查询便利店信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MDStores> selectMDStores(Map<String, Object> map) throws Exception;

	/**
	 *  根据门店电话来查询门店
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public MDStores selectMDStoresForDefault(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询便利店列表信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MDStores> selectMDStoresList(Map<String, Object> map) throws Exception;

	/**
	 * 查询状态正常的店东
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MDStores> selectNormalStores(Map<String, Object> map) throws Exception;

	/**
	 * 查询便利店信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectMDStoresDetailForConsumer(Map<String, Object> map) throws Exception;

	/**
	 * 查询附近的便利店
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectNearbyMDStores(Map<String, Object> map) throws Exception;

	
	/**
	 * 查询附近的便利店(搞掂APP)附加5公里和申请助教
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectNearbyMDStoresForSalesassistant(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 查询附近的便利店(搞掂APP)待审批
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectNearbyMDStoresForSalesassistantByApproval(Map<String, Object> map) throws Exception;
	/**
	 * 查询附近的便利店(搞掂APP)总查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectNearbyMDStoresForSalesassistantByMain(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询附近的便利店(搞掂APP)拓店和助教查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectNearbyMDStoresForSalesassistantBySpecialist(Map<String, Object> map) throws Exception;
	
	
	
	/**
	 * 查询熟么入住商家信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectShumeStores(Map<String, Object> map) throws Exception;

	/**
	 * 查询附近熟么入住商家信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectNearbyShumeStores(Map<String, Object> map) throws Exception;

	/**
	 * 查询店东助手服务门店列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectStoresAssistant(Map<String, Object> map) throws Exception;

	/**
	 * 查询推荐商家信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectMDStoresRecommendInfo(Map<String, Object> map) throws Exception;

	/**
	 * 查询推荐商家
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MDStoresRecommend> selectMDStoresRecommend(Map<String, Object> map) throws Exception;

	/**
	 * 查询便利店的会员费信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MDStores> selectMDStoresForServiceFree(Map<String, Object> map) throws Exception;

	/***
	 * 门店营业信息查询(掌上超市信息)
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date 2017年3月28日
	 */
	public Map<String, Object> selectMDStoresBusinessInfoFind(Map<String, Object> map) throws Exception;

	/**
	 * 门店惠商驿站信息查询
	 * 
	 * @Title: selectStageStores
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	public Map<String, Object> selectStageStores(Map<String, Object> map) throws Exception;

	/**
	 * 更新便利店信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void updateMDStores(Map<String, Object> map) throws Exception;

	/**
	 * 更新便利店信息,用于运营管理系统的便利店信息更新
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void updateMDStoresSync(Map<String, Object> map) throws Exception;

	/**
	 * 更新便利店信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void updateMDStoresAssistant(Map<String, Object> map) throws Exception;

	/**
	 * 删除推荐门店信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void deleteMDStoresRecommend(Map<String, Object> map) throws Exception;

	/**
	 * 查询业务员对应的拓店，助教门店数
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectSalesmanStoresNum(Map<String, Object> map) throws Exception;
	
	
}
