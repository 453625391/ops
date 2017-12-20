package com.meitianhui.goods.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.goods.entity.GdBenefit;

/**
 * 权益券
* @ClassName: GdBenefitDao  
* @author tiny 
* @date 2017年3月6日 上午11:44:20  
*
 */
public interface GdBenefitDao {

	/**
	 * 添加会员权益
	* @Title: insertGdBenefit  
	* @param gdBenefit
	* @throws Exception
	* @author tiny
	 */
	void insertGdBenefit(GdBenefit gdBenefit) throws Exception;

	/**
	 * 查询会员权益列表
	* @Title: selectGdBenefitList  
	* @param map
	* @return
	* @throws Exception
	* @author tiny
	 */
	List<Map<String, Object>> selectGdBenefitListForConsumer(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询会员权益信息
	 * @Title: selectGdBenefitList  
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	Map<String, Object> selectGdBenefit(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询会员权益统计
	 * @Title: selectGdBenefitCount  
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	Map<String, Object> selectGdBenefitCount(Map<String, Object> map) throws Exception;

	/**
	 * updateGdBenefit 
	* @Title: updateGdBenefit  
	* @param map
	* @return
	* @throws Exception
	* @author tiny
	 */
	int updateGdBenefit(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 更新过期权益卷
	* @Title: updateExpiredGdBenefit  
	* @param map
	* @return
	* @throws Exception
	* @author tiny
	 */
	int updateExpiredGdBenefit(Map<String,Object> map) throws Exception;


	/**
	 * 新增会员权益日志
	* @Title: insertGdBenefitLog  
	* @param gdBenefit
	* @throws Exception
	* @author tiny
	 */
	void insertGdBenefitLog(Map<String,Object> map) throws Exception;

	/**
	 * 查询会员权益日志
	 * 
	* @Title: selectGdBenefitLog  
	* @param map
	* @return
	* @throws Exception
	* @author tiny
	 */
	List<Map<String, Object>> selectGdBenefitLog(Map<String, Object> map) throws Exception;
	

}
