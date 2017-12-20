package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.goods.entity.GcActivity;
import com.meitianhui.goods.entity.GcActivityDetail;
import com.meitianhui.goods.entity.GcMemberFaceGift;
import com.meitianhui.goods.entity.GcMemberFaceGiftLog;

/***
 * 红包、见面礼数据操作接口
 * 
 * @author 丁硕
 * @date 2017年3月1日
 */
public interface GcActivityDao {

	/**
	 * 活动发放记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void insertGcActivity(GcActivity gcActivity) throws Exception;
	
	/**
	 * 查询红包发放记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectGcActivity(Map<String, Object> map) throws Exception;

	/**
	 * 查询红包记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<GcActivityDetail> selectGcActivityDetail(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询红包个数
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> selectGcActivityDetailCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新红包信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updateGcActivityDetail(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新失效红包信息
	 * 
	 * @param plActivity
	 * @throws Exception
	 */
	void updateDisabledGcActivity(Map<String, Object> map) throws Exception;
	
	//============================================见面礼==================================================================
	
	/***
	 * 根据条件查询红包记录数
	 * @param params
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月1日
	 */
	long selectActivityDetailCount(Map<String, Object> params) throws Exception;
	
	/***
	 * 新增红包记录
	 * @param gcActivityDetail
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月1日
	 */
	void insertGcActivityDetail(GcActivityDetail gcActivityDetail) throws Exception;
	
	/***
	 * 新增会员见面礼记录
	 * @param gcMemberFaceGift
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月1日
	 */
	void insertGcMemberFaceGift(GcMemberFaceGift gcMemberFaceGift) throws Exception;
	
	/***
	 * 根据条件查询会员见面礼统计
	 * @param params
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月1日
	 */
	List<GcMemberFaceGift> selectGcMemberFaceGift(Map<String, Object> params) throws Exception;
	
	/***
	 * 更新会员见面礼红包统计信息,用于送红包与开红包
	 * @param params
	 * @return
	 * @author 丁硕
	 * @date   2017年3月1日
	 */
	int updateGcMemberFaceGift(Map<String, Object> params) throws Exception;
	
	/***
	 * 新增见面礼红包领取日志
	 * @param memberFaceGiftLog
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月1日
	 */
	void insertGcMemberFaceGiftLog(GcMemberFaceGiftLog memberFaceGiftLog) throws Exception;
	
	/***
	 * 统计会员已到帐的见面礼金额
	 * @param params
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月1日
	 */
	List<Map<String, Object>> selectGcMemberFaceGiftTotalAmount(Map<String, Object> params) throws Exception;
	
}
