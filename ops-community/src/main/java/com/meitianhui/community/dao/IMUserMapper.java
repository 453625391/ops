package com.meitianhui.community.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.meitianhui.community.entity.IMUser;
import com.meitianhui.community.entity.IMUserMember;

/***
 * IM与会员体系数据库操作接口
 * 
 * @author 丁硕
 * @date 2016年8月10日
 */
public interface IMUserMapper {
	
	/***
	 * 根据会员编号与会员类型查询对应的会员账号信息
	 * @param member_id
	 * @param member_type_key
	 * @return
	 * @author 丁硕
	 * @date   2016年8月19日
	 */
	public Map<String, Object> getUserMember(@Param("member_id")String member_id, @Param("member_type_key")String member_type_key);

	/***
	 * 根据会员编号与会员类型查询数据库环信用户信息
	 * @param member_id
	 * @param member_type_key
	 * @return
	 * @author 丁硕
	 * @date   2016年8月10日
	 */
	public String getIMUserId(@Param("member_id")String member_id, @Param("member_type_key")String member_type_key);
	
	/**
	 * 查询单个IM用户信息
	 * @param im_user_id
	 * @return
	 * @author 丁硕
	 * @date   2016年8月26日
	 */
	public Map<String, String> queryOneIMUser(@Param("im_user_id")String im_user_id);
	
	/***
	 * 新增IM账号信息
	 * @param imUser
	 * @return
	 * @author 丁硕
	 * @date   2016年8月10日
	 */
	public int insertIMUser(IMUser imUser);
	
	/***
	 * 更新IM账号信息
	 * @param imUser
	 * @return
	 * @author 丁硕
	 * @date   2016年8月10日
	 */
	public int updateIMUser(Map<String,  Object> imUser);
	
	/***
	 * 新增IM与会员账号关系
	 * @param userMember
	 * @return
	 * @author 丁硕
	 * @date   2016年8月10日
	 */
	public int insertIMUserMember(IMUserMember userMember);
	
	/****
	 * 根据相关条件查询用户基本信息
	 * @param params
	 * @return
	 * @author 丁硕
	 * @date   2016年8月31日
	 */
	public List<Map<String, String>> getIMUserBaseInfoList(Map<String, Object> params);
	
	/***
	 * 获取带经纬度信息的用户信息
	 * @param params
	 * @return
	 * @author 丁硕
	 * @date   2017年2月27日
	 */
	public List<Map<String, String>> getIMUserLocationInfoList(Map<String, Object> params);
}
