package com.meitianhui.order.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.order.entity.OdTask;
import com.meitianhui.order.entity.OdTaskProcessing;
import com.meitianhui.order.entity.OdTaskProcessingLog;

/**
 * 任务发布
* @ClassName: OdTaskDao  
* @author tiny 
* @date 2017年3月8日 下午2:20:27  
*
 */
public interface OdTaskDao {
	/**
	 * 新增任务
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertOdTask(OdTask OdTask) throws Exception;
	
	/**
	 * 更新任务状态
	* @Title: updateOdTask  
	* @param map
	* @return
	* @throws Exception
	* @author tiny
	 */
	public int updateOdTask(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询任务(运营)
	* @Title: selectOdTaskListForOp  
	* @param map
	* @return
	* @throws Exception
	* @author tiny
	 */
	public List<Map<String,Object>> selectOdTaskListForOp(Map<String, Object> map) throws Exception;
	
	/***
	 * 子任务列表查询（运营）
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月9日
	 */
	public List<OdTaskProcessing> selectOdTaskProcessingListForOp(Map<String, Object> map) throws Exception;
	
	/***
	 * 更新子任务信息
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月8日
	 */
	public int updateOdTaskProcessing(Map<String, Object> map) throws Exception;
	
	/***
	 * 新增子任务
	 * @param odTaskProcessing
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月8日
	 */
	public void insertOdTaskProcessing(OdTaskProcessing odTaskProcessing) throws Exception;
	
	
	/***
	 * 查询任务详情列表
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月8日
	 */
	public List<OdTask> selectOdTaskDetailList(Map<String, Object> map) throws Exception;
	
	/***
	 * 子任务详情信息查询
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月8日
	 */
	public List<OdTaskProcessing> selectOdTaskProcessingDetailList(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询会员可领取的任务列表(新任务)
	* @Title: selectOdTaskListForMember  
	* @param map
	* @return
	* @throws Exception
	* @author tiny
	 */
	public List<OdTask> selectNewOdTaskListForMember(Map<String, Object> map) throws Exception;
	
	
	/***
	 * 查询子任务信息列表
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月8日
	 */
	public List<OdTaskProcessing> selectOdTaskProcessingList(Map<String, Object> map) throws Exception;
	
	/***
	 * 查询会员可领取的任务总数
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月8日
	 */
	public long odTaskNewCountForMemberFind(Map<String, Object> map) throws Exception;
	
	/**
	 * 新增任务执行日志
	 * @param processingLog
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月8日
	 */
	public void insertOdTaskProcessingLog(OdTaskProcessingLog processingLog) throws Exception;
	
	/***
	 * 查询任务执行日志
	 * @param map
	 * @return
	 * @throws Exception
	 * @author 丁硕
	 * @date   2017年3月8日
	 */
	public List<Map<String,Object>> selectOdTaskProcessingLogList(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新超时的任务
	* @Title: updateTimeoutOdTask  
	* @param map
	* @throws Exception
	* @author tiny
	 */
	public void updateTimeoutOdTask(Map<String, Object> map) throws Exception;
	
	/**
	* @Title: updateTimeoutOdTask  
	* 更新超时的会员接受任务
	* @param map
	* @throws Exception
	* @author tiny
	 */
	public void updateTimeoutOdTaskProcessing(Map<String, Object> map) throws Exception;
	
}
