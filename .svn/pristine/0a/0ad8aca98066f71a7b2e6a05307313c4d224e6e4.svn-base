package com.meitianhui.community.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.meitianhui.community.entity.IMLiveroom;

/***
 * 直播室数据为操作接口
 * 
 * @author 丁硕
 * @date 2016年8月29日
 */
public interface IMLiveroomMapper {

	/***
	 * 创建一个直播室
	 * @param liveroom
	 * @author 丁硕
	 * @date   2016年8月29日
	 */
	public void createLiveroom(IMLiveroom liveroom);
	
	/***
	 * 查询直播室详情
	 * @param room_id
	 * @return
	 * @author 丁硕
	 * @date   2016年8月29日
	 */
	public IMLiveroom queryLiveroomDetail(@Param("id")String room_id);
	
	/***
	 * 根据URL与状态查询对应的直播室信息
	 * @param status
	 * @param url
	 * @return
	 * @author 丁硕
	 * @date   2016年9月12日
	 */
	public Map<String, String> queryLiveroomByUrl(@Param("status")String status, @Param("url") String url);
	
	/***
	 * 更新直播室成员列表
	 * @param room_id 房间ID
	 * @param affiliations 成员列表
	 * @param old_affiliations 旧的成员列表
	 * @return
	 * @author 丁硕
	 * @date   2016年9月12日
	 */
	public int updateLiveroomUsers(@Param("room_id")String room_id, @Param("affiliations")String affiliations, @Param("old_affiliations") String old_affiliations);
	
	/***
	 * 查询直播室列表
	 * @return
	 * @author 丁硕
	 * @date   2016年8月29日
	 */
	public List<Map<String, String>> queryLiveroomList(Map<String, Object> params);
	
	/***
	 * 更新直播室状态
	 * @param params
	 * @author 丁硕
	 * @date   2016年8月29日
	 */
	public void updateLiveRoomStatus(Map<String, Object> params);
	
	/***
	 * 查询正在直播中的门店列表
	 * @param params
	 * @return
	 * @author 丁硕
	 * @date   2016年9月1日
	 */
	public List<Map<String, String>> queryLiveStoresList(Map<String, Object> params);
}
