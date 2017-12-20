package com.meitianhui.community.easemob.api;

import com.meitianhui.community.easemob.body.IMUserBody;
import com.meitianhui.community.easemob.body.IMUsersBody;
import com.meitianhui.community.easemob.wrapper.BodyWrapper;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

/***
 * 环信用户模块操作接口
 * 
 * @author 丁硕
 * @date 2016年7月22日
 */
public interface IMUserAPI {

	/***
	 * 注册IM用户[单个]
	 * 
	 * @param body
	 *            <code>{"username":"${用户名}","password":"${密码}", "nickname":"${昵称值}"}</code>
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper createNewIMUserSingle(IMUserBody body);

	/***
	 * 注册IM用户[批量]
	 * 
	 * @param body
	 *            <code>[{"username":"${用户名1}","password":"${密码}"},…,{"username":"${用户名2}","password":"${密码}"}]</code>
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper createNewIMUserBatch(IMUsersBody body);

	/***
	 * 获取IM用户[单个]
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getIMUsersByUserName(String userName);

	/***
	 * 获取IM用户[批量]，参数为空时默认返回最早创建的10个用户
	 * 
	 * @param limit
	 *            单页获取数量
	 * @param cursor
	 *            游标，大于单页记录时会产生
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getIMUsersBatch(Long limit, String cursor);

	/***
	 * 删除IM用户[单个]
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper deleteIMUserByUserName(String userName);

	/***
	 * 删除IM用户[批量]，随机删除
	 * 
	 * @param limit
	 *            删除数量，建议100-500
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper deleteIMUserBatch(Long limit);

	/***
	 * 重置IM用户密码
	 * @param userName
	 * @param newpassword
	 * @return
	 * @author 丁硕
	 * @date   2016年8月30日
	 */
	ResponseWrapper modifyIMUserPasswordWithAdminToken(String userName, String newpassword);

	/***
	 * 修改用户昵称
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param body
	 *            <code>{"nickname" : "${昵称值}"}</code>
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper modifyIMUserNickNameWithAdminToken(String userName, String nickname);

	/***
	 * 给IM用户的添加好友
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param friendName
	 *            友用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper addFriendSingle(String userName, String friendName);

	/**
	 * 解除IM用户的好友关系 <br>
	 * DELETE
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param friendName
	 *            好友用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper deleteFriendSingle(String userName, String friendName);

	/***
	 * 查看某个IM用户的好友信息
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getFriends(String userName);

	/***
	 * 获取IM用户的黑名单
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getBlackList(String userName);

	/***
	 * 往IM用户的黑名单中加人
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param payload
	 *            <code>{"usernames":["5cxhactgdj", "mh2kbjyop1"]}</code>
	 * 
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper addToBlackList(String userName, BodyWrapper body);

	/***
	 * 从IM用户的黑名单中减人 <br>
	 * DELETE
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param blackListName
	 *            黑名单用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper removeFromBlackList(String userName, String blackListName);

	/***
	 * 查看用户在线状态
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getIMUserStatus(String userName);

	/***
	 * 查询离线消息数
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getOfflineMsgCount(String userName);

	/***
	 * 查询某条离线消息状态
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param msgId
	 *            消息ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getSpecifiedOfflineMsgStatus(String userName, String msgId);

	/***
	 * 用户账号禁用
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper deactivateIMUser(String userName);

	/***
	 * 用户账号解禁
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper activateIMUser(String userName);

	/***
	 * 用戶名或用戶ID
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper disconnectIMUser(String userName);

	/***
	 * 获取用户参与的群组
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getIMUserAllChatGroups(String userName);

	/***
	 * 获取用户所有参与的聊天室
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @author 丁硕
	 * @date 2016年7月22日
	 */
	ResponseWrapper getIMUserAllChatRooms(String userName);
}
