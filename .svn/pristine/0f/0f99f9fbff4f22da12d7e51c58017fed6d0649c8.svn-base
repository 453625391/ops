package com.meitianhui.community.easemob.api;

import com.meitianhui.community.easemob.body.ChatRoomBody;
import com.meitianhui.community.easemob.body.IMUsersBody;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

/***
 * 环信聊天室请求接口
 * 
 * @author 丁硕
 * @date 2016年7月22日
 */
public interface ChatRoomAPI {
	
	/***
	 * 创建聊天室
	 * @param body	<code>{name":"testchatroom","description":"server create chatroom","maxusers":300,"owner":"jma1","members":["jma2","jma3"]}</code>
	 * @return
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	ResponseWrapper createChatRoom(ChatRoomBody body);

	/***
	 * 修改聊天室信息
	 * @param roomId	聊天室标识
	 * @param body	<code>{"name":"test chatroom","description":"update chatroominfo","maxusers":200}</code>
	 *            
	 * @return
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	ResponseWrapper modifyChatRoom(String roomId, ChatRoomBody body);
	
	/***
	 * 删除聊天室
	 * @param roomId	聊天室标识
	 * @return
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	ResponseWrapper deleteChatRoom(String roomId);

	/***
	 * 获取app中所有的聊天室
	 * @return
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	ResponseWrapper getAllChatRooms();

	/***
	 * 获取一个聊天室详情 
	 * @param roomId	聊天室标识
	 * @return
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	ResponseWrapper getChatRoomDetail(String roomId);

	/***
	 * 聊天室成员添加[单个]
	 * @param roomId	聊天室标识
	 * @param userName	用户ID或用户名
	 * @return
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	ResponseWrapper addSingleUserToChatRoom(String roomId, String userName);

	/***
	 * 聊天室成员添加[批量] <br>
	 * @param roomId	 聊天室标识
	 * @param body	用户ID或用户名，数组形式
	 * @return
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	ResponseWrapper addBatchUsersToChatRoom(String roomId,IMUsersBody body);

	/**
	 * 将用户从某个聊天室移除
	 * @param roomId	 聊天室标识
	 * @param userName	用户ID或用户名
	 * @return
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	ResponseWrapper removeSingleUserFromChatRoom(String roomId, String userName);

	/**
	 * 批量将用户移出聊天室
	 * @param roomId	聊天室标识
	 * @param userNames	用户ID或用户名
	 * @return
	 * @author 丁硕
	 * @date   2016年7月22日
	 */
	ResponseWrapper removeBatchUsersFromChatRoom(String roomId, String[] userNames);
}
