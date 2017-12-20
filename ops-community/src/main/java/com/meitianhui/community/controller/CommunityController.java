package com.meitianhui.community.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.community.constant.RspCode;
import com.meitianhui.community.service.IMGroupService;
import com.meitianhui.community.service.IMUserService;
import com.meitianhui.community.service.LiveRoomService;

@Controller
@RequestMapping("community")
public class CommunityController extends MyBaseController {

	@Autowired
	private IMUserService imUserService;

	@Autowired
	private IMGroupService imGroupService;
	
	@Autowired
	private LiveRoomService liveroomService;

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException {
		String operateName = request.getParameter("service");
		if("chat.getIMUserLoginInfo".equals(operateName)){	//获取用户登录信息,如果没有相关信息，则自动注册
			imUserService.getIMUserLoginInfo(paramsMap, result);
		} else if("chat.getIMUserAccount".equals(operateName)) { // 根据会员名与会员类型查询IM用户信息
			imUserService.getIMUserAccount(paramsMap, result);
		} else if("chat.getIMUserDetail".equals(operateName)){	//im_user_id获取用户详情信息
			imUserService.getIMUserDetail(paramsMap, result);
		} else if("chat.modifyIMUserInfo".equals(operateName)){	//修改用户信息
			imUserService.modifyIMUserInfo(paramsMap, result);
		} else if ("chat.getIMUserGroupList".equals(operateName)) { // 获取用户群组信息
			imUserService.getIMUserGroupList(paramsMap, result);
		} else if ("chat.createGroup".equals(operateName)) { // 创建一个群组
			imGroupService.createChatGroup(paramsMap, result);
		} else if("chat.createChatGroupWithMembers".equals(operateName)){	//将会员拉入进创建一个群
			imGroupService.createChatGroupWithMembers(paramsMap, result);
		} else if ("chat.getChatGroupUsers".equals(operateName)) { // 获取某个群组下的用户
			imGroupService.getChatGroupUsers(paramsMap, result);
		} else if("chat.getChatGroupLocationUsers".equals(operateName)){	//获取群组中位置信息成员列表
			imGroupService.getChatGroupLocationUsers(paramsMap, result);
		} else if ("chat.getChatGroupDetails".equals(operateName)) { // 获取群组详细信息
			imGroupService.getChatGroupDetails(paramsMap, result);
		} else if ("chat.modifyChatGroupInfo".equals(operateName)) { // 修改群组信息
			imGroupService.modifyChatGroupInfo(paramsMap, result);
		} else if ("chat.deleteChatGroup".equals(operateName)) { // 删除群组信息
			imGroupService.deleteChatGroup(paramsMap, result);
		} else if ("chat.addSingleUserToChatGroup".equals(operateName)) { // 添加单个用户到群组
			imGroupService.addSingleUserToChatGroup(paramsMap, result);
		} else if ("chat.addBatchUsersToChatGroup".equals(operateName)) { // 批量将用户添加到群组中
			imGroupService.addBatchUsersToChatGroup(paramsMap, result);
		} else if ("chat.removeSingleUserFromChatGroup".equals(operateName)) { // 将单个用户从群组中移除，主要用户自动退出
			imGroupService.removeSingleUserFromChatGroup(paramsMap, result);
		} else if ("chat.removeBatchUsersFromChatGroup".equals(operateName)) { // 将多个用户从群组中移除，主要用户管理员批量移除
			imGroupService.removeBatchUsersFromChatGroup(paramsMap, result);
		} else if(operateName.startsWith("live.")){	//进入直播业务模块
			this.liveBusiness(operateName, paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}
	
	/***
	 * 直播业务相关
	 * @param operateName
	 * @param paramsMap
	 * @param result
	 * @return
	 * @author 丁硕
	 * @date   2016年8月26日
	 */
	private void liveBusiness(String operateName, Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		if("live.openOneLiveRoom".equals(operateName)){	//开始一个直播室
			liveroomService.openOneLiveRoom(paramsMap, result);
		} else if("live.closeOneLiveRoom".equals(operateName)){	//关闭直播室
			liveroomService.closeOneLiveRoom(paramsMap, result);
		} else if("live.getLiveRoomDetails".equals(operateName)) {	//获取直播室详情
			liveroomService.getLiveRoomDetails(paramsMap, result);
		} else if("live.addUserToLiveRoom".equals(operateName)) {	//用户加入直播室
			liveroomService.addUserToLiveRoom(paramsMap, result);
		} else if("live.removeUserFromLiveRoom".equals(operateName)) {	//用户退出直播室
			liveroomService.removeUserFromLiveRoom(paramsMap, result);
		} else if("live.getLiveRoomUsers".equals(operateName)) {	//查询直播室的成员列表
			liveroomService.getLiveRoomUsers(paramsMap, result);
		} else if ("live.queryLiveRoomList".equals(operateName)) {	//查询正在直播的直播室列表
			liveroomService.queryLiveRoomList(paramsMap, result);
		} else if ("live.queryUserLiveRoom".equals(operateName)) {	//查询用户开播中的直播室
			liveroomService.queryUserLiveRoom(paramsMap, result);
		} else if ("live.queryUserLiveRoomHistoryList".equals(operateName)) {	//查询用户历史直播室列表
			liveroomService.queryUserLiveRoomHistoryList(paramsMap, result);
		} else if ("live.queryLiveStoresList".equals(operateName)) {	//查询正在直播中的门店列表
			liveroomService.queryLiveStoresList(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}

}
