package com.meitianhui.community.easemob.api.impl;

import org.apache.commons.lang3.StringUtils;

import com.meitianhui.community.easemob.api.ChatRoomAPI;
import com.meitianhui.community.easemob.body.ChatRoomBody;
import com.meitianhui.community.easemob.body.IMUsersBody;
import com.meitianhui.community.easemob.constant.EasemobConstant;
import com.meitianhui.community.easemob.constant.HttpMethod;
import com.meitianhui.community.easemob.utils.HeaderHelper;
import com.meitianhui.community.easemob.utils.HttpClientRestApiInvoker;
import com.meitianhui.community.easemob.wrapper.HeaderWrapper;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

/***
 * 环信聊天室请求接口实现类
 * 
 * @author 丁硕
 * @date 2016年7月22日
 */
public class EasemobChatRoomApi implements ChatRoomAPI {
	
	private static final String ROOT_URI = EasemobConstant.EASEMOB_API_SERVICE_URL + "/chatrooms";

	@Override
	public ResponseWrapper createChatRoom(ChatRoomBody body) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, ROOT_URI, header, body);
	}

	@Override
	public ResponseWrapper modifyChatRoom(String roomId, ChatRoomBody body) {
		 String url = ROOT_URI + "/" + roomId;
		 HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		 return HttpClientRestApiInvoker.sendRequest(HttpMethod.PUT, url, header, body);
	}

	@Override
	public ResponseWrapper deleteChatRoom(String roomId) {
		 String url = ROOT_URI + "/" + roomId;
		 HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		 return HttpClientRestApiInvoker.sendRequest(HttpMethod.DELETE, url, header, null);
	}

	@Override
	public ResponseWrapper getAllChatRooms() {
		 HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		 return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, ROOT_URI, header, null);
	}

	@Override
	public ResponseWrapper getChatRoomDetail(String roomId) {
		 String url = ROOT_URI + "/" + roomId;
		 HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		 return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper addSingleUserToChatRoom(String roomId, String userName) {
		 String url = ROOT_URI + "/" + roomId + "/users/" + userName;
		 HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		 return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, url, header, null);
	}

	@Override
	public ResponseWrapper addBatchUsersToChatRoom(String roomId, IMUsersBody body) {
		 String url = ROOT_URI + "/" + roomId + "/users";
		 HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		 return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, url, header, null);
	}

	@Override
	public ResponseWrapper removeSingleUserFromChatRoom(String roomId, String userName) {
		String url = ROOT_URI + "/" + roomId + "/users/" + userName;
		 HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		 return HttpClientRestApiInvoker.sendRequest(HttpMethod.DELETE, url, header, null);
	}

	@Override
	public ResponseWrapper removeBatchUsersFromChatRoom(String roomId, String[] userNames) {
		 String url = ROOT_URI + "/" + roomId + "/users/" + StringUtils.join(userNames, ",");
		 HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		 return HttpClientRestApiInvoker.sendRequest(HttpMethod.PUT, url, header, null);
	}

}
