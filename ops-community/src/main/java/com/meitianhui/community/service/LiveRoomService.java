package com.meitianhui.community.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/***
 * 视频逻辑处理接口，关于视频业务处理
 * 
 * @author 丁硕
 * @date 2016年7月21日
 */
public interface LiveRoomService {
	
	final String LIVEROOM_STATUS_LIVE = "live";	//直播中
	final String LIVEROOM_STATUS_FINISHED = "finished";	//已结束

	/***
	 * 开始一个直播室
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月25日
	 */
	public void openOneLiveRoom(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 关闭直播室
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月29日
	 */
	public void closeOneLiveRoom(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 获取直接室详细信息
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月25日
	 */
	public void getLiveRoomDetails(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 添加用户到直播室中
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月25日
	 */
	public void addUserToLiveRoom(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 获取直播室用户列表
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月29日
	 */
	public void getLiveRoomUsers(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 将单个用户从直播室中移除
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月25日
	 */
	public void removeUserFromLiveRoom(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 查询正在直播中的直播室
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月29日
	 */
	public void queryLiveRoomList(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 查询用户正在直播的直播室
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月31日
	 */
	public void queryUserLiveRoom(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 查询IM用户的直播历史记录
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月29日
	 */
	public void queryUserLiveRoomHistoryList(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 查询正在直播中的门店列表
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年9月1日
	 */
	public void queryLiveStoresList(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 根据URL与状态查询对应的直播室信息
	 * @param status
	 * @param url
	 * @return
	 * @author 丁硕
	 * @date   2016年9月12日
	 */
	public Map<String, String> queryLiveroomByUrl(String status, String url);
	
}
