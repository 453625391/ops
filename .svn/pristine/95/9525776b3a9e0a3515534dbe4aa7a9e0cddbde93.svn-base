package com.meitianhui.community.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.meitianhui.community.entity.IMGroup;

/***
 * IM群组数据库操作接口
 * 
 * @author 丁硕
 * @date 2016年8月24日
 */
public interface IMGroupMapper {

	/***
	 * 根据群组编号，查询群组详细信息
	 * @param groupId
	 * @return
	 * @author 丁硕
	 * @date   2016年8月24日
	 */
	public IMGroup queryIMGroupDetail(@Param("group_id")String group_id);
	
	/***
	 * 创建IM聊天群组信息
	 * @param IMGroup
	 * @author 丁硕
	 * @date   2016年8月24日
	 */
	public void createIMGroup(IMGroup IMGroup);
	
	/***
	 * 修改IM聊天群组信息
	 * @param IMGroup
	 * @author 丁硕
	 * @date   2016年8月24日
	 */
	public int modifyIMGroup(Map<String, Object> imGroup);
	
	/***
	 * 删除群组信息
	 * @param group_id
	 * @param owner
	 * @author 丁硕
	 * @date   2016年8月30日
	 */
	public int deleteIMGroup(@Param("group_id")String group_id, @Param("owner")String owner);
	
	/***
	 * 更新群组成员信息
	 * @param group_id	群组ID
	 * @param affiliations 群组成员
	 * @param affiliations_count 群组数量
	 * @param old_affiliations 旧的群组信息
	 * @return
	 * @author 丁硕
	 * @date   2016年9月12日
	 */
	public int updateIMGroupUsers(@Param("group_id")String group_id, @Param("affiliations")String affiliations,
			@Param("affiliations_count")int affiliations_count, @Param("old_affiliations") String old_affiliations);
	
	/****
	 * 查询单个用户的群组列表
	 * @param im_user_id
	 * @return
	 * @author 丁硕
	 * @date   2016年8月25日
	 */
	public List<Map<String, String>> getIMUserGroupList(@Param("im_user_id") String im_user_id);
}
