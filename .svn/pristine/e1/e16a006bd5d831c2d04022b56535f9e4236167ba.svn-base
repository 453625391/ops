package com.meitianhui.community.easemob.api;

import com.meitianhui.community.easemob.body.ChatGroupBody;
import com.meitianhui.community.easemob.body.CommonBody;
import com.meitianhui.community.easemob.body.IMUserNamesBody;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

/***
 * 环信群组请求接口
 * 
 * @author 丁硕
 * @date 2016年7月22日
 */
public interface ChatGroupAPI {

	/***
	 * 获取群组，参数为空时获取所有群组
	 * 
	 * @param limit
	 *            单页数量
	 * @param cursor
	 *            游标，存在更多记录时产生
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getChatGroups(Long limit, String cursor);

	/***
	 * 获取一个或者多个群组的详情
	 * 
	 * @param groupIds
	 *            群组ID数组
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getChatGroupDetails(String[] groupIds);

	/***
	 * 创建一个群组
	 * 
	 * @param body
	 *            <code>{"groupname":"testrestgrp12","desc":"server create group","public":true,"maxusers":300,"approval":true,"owner":"jma1","members":["jma2","jma3"]}</code>
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper createChatGroup(ChatGroupBody body);

	/***
	 * 修改群组信息
	 * 
	 * @param groupId
	 *            群组标识
	 * @param body
	 *            <code>{"groupname":"testrestgrp12",description":"update groupinfo","maxusers":300}</code>
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper modifyChatGroup(String groupId, CommonBody body);

	/***
	 * 删除群组 <br>
	 * 
	 * @param groupId
	 *            群组标识
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper deleteChatGroup(String groupId);

	/***
	 * 获取群组所有用户 <br>
	 * 
	 * @param groupId
	 *            群组标识
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getChatGroupUsers(String groupId);

	/***
	 * 群组加人[单个]
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper addSingleUserToChatGroup(String groupId, String userId);

	/***
	 * 群组加人[批量] <br>
	 * 
	 * @param groupId
	 *            群组标识
	 * @param body
	 *            用户ID或用户名，数组形式
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper addBatchUsersToChatGroup(String groupId, IMUserNamesBody body);

	/**
	 * 将用户从群组中移除
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper removeSingleUserFromChatGroup(String groupId, String userId);

	/***
	 * 群组减人[批量]
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userIds
	 *            用户ID或用户名，数组形式
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper removeBatchUsersFromChatGroup(String groupId, String[] userIds);

	/***
	 * 群组转让 <br>
	 * 
	 * @param groupId
	 *            群组标识
	 * @param body
	 *            新群主ID或用户名
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper transferChatGroupOwner(String groupId, ChatGroupBody body);

	/***
	 * 查询群组黑名单
	 * 
	 * @param groupId
	 *            群组标识
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getChatGroupBlockUsers(String groupId);

	/***
	 * 群组黑名单个添加 <br>
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper addSingleBlockUserToChatGroup(String groupId, String userId);

	/***
	 * 群组黑名单批量添加
	 * 
	 * @param groupId
	 *            群组标识
	 * @param body
	 *            用户ID或用户名，数组形式
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper addBatchBlockUsersToChatGroup(String groupId, IMUserNamesBody body);

	/***
	 * 群组黑名单单个删除 <br>
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper removeSingleBlockUserFromChatGroup(String groupId, String userId);

	/**
	 * 群组黑名单批量删除
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userIds
	 *            用户ID或用户名，数组形式
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper removeBatchBlockUsersFromChatGroup(String groupId, String[] userIds);
}
