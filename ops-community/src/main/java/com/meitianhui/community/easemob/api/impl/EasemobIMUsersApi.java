package com.meitianhui.community.easemob.api.impl;

import com.meitianhui.community.easemob.api.IMUserAPI;
import com.meitianhui.community.easemob.body.CommonBody;
import com.meitianhui.community.easemob.body.IMUserBody;
import com.meitianhui.community.easemob.body.IMUsersBody;
import com.meitianhui.community.easemob.constant.EasemobConstant;
import com.meitianhui.community.easemob.constant.HttpMethod;
import com.meitianhui.community.easemob.utils.HeaderHelper;
import com.meitianhui.community.easemob.utils.HttpClientRestApiInvoker;
import com.meitianhui.community.easemob.wrapper.BodyWrapper;
import com.meitianhui.community.easemob.wrapper.HeaderWrapper;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

/***
 * 环信用户操作逻辑实现类
 * 
 * @author 丁硕
 * @date 2016年7月22日
 */
public class EasemobIMUsersApi implements IMUserAPI {

	private static final String ROOT_URI = EasemobConstant.EASEMOB_API_SERVICE_URL + "/users";

	@Override
	public ResponseWrapper createNewIMUserSingle(IMUserBody body) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, ROOT_URI, header, body);
	}

	@Override
	public ResponseWrapper createNewIMUserBatch(IMUsersBody body) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, ROOT_URI, header, body);
	}

	@Override
	public ResponseWrapper getIMUsersByUserName(String userName) {
		String url = ROOT_URI + "/" + userName;
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper getIMUsersBatch(Long limit, String cursor) {
		return null;
	}

	@Override
	public ResponseWrapper deleteIMUserByUserName(String userName) {
		String url = ROOT_URI + "/" + userName;
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.DELETE, url, header, null);
	}

	@Override
	public ResponseWrapper deleteIMUserBatch(Long limit) {
		return null;
	}

	@Override
	public ResponseWrapper modifyIMUserPasswordWithAdminToken(String userName, String newpassword) {
		String url = ROOT_URI + "/" + userName + "/password";
		CommonBody body = new CommonBody();
		body.put("newpassword", newpassword);
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.PUT, url, header, body);
	}

	@Override
	public ResponseWrapper modifyIMUserNickNameWithAdminToken(String userName, String nickname) {
		String url = ROOT_URI + "/" + userName;
		CommonBody body = new CommonBody();
		body.put("nickname", nickname);
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.PUT, url, header, body);
	}

	@Override
	public ResponseWrapper addFriendSingle(String userName, String friendName) {
		String url = ROOT_URI + "/" + userName + "/contacts/users/" + friendName;
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, url, header, null);
	}

	@Override
	public ResponseWrapper deleteFriendSingle(String userName, String friendName) {
		String url = ROOT_URI + "/" + userName + "/contacts/users/" + friendName;
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.DELETE, url, header, null);
	}

	@Override
	public ResponseWrapper getFriends(String userName) {
		String url = ROOT_URI + "/" + userName + "/contacts/users";
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper getBlackList(String userName) {
		String url = ROOT_URI + "/" + userName + "/blocks/users";
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper addToBlackList(String userName, BodyWrapper body) {
		String url = ROOT_URI + "/" + userName + "/blocks/users";
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, url, header, null);
	}

	@Override
	public ResponseWrapper removeFromBlackList(String userName, String blackListName) {
		String url = ROOT_URI + "/" + userName + "/blocks/users/" + blackListName;
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.DELETE, url, header, null);
	}

	@Override
	public ResponseWrapper getIMUserStatus(String userName) {
		String url = ROOT_URI + "/" + userName + "/status";
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper getOfflineMsgCount(String userName) {
		String url = ROOT_URI + "/" + userName + "/offline_msg_count";
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper getSpecifiedOfflineMsgStatus(String userName, String msgId) {
		String url = ROOT_URI + "/" + userName + "/offline_msg_status/" + msgId;
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper deactivateIMUser(String userName) {
		String url = ROOT_URI + "/" + userName + "/deactivate";
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, url, header, null);
	}

	@Override
	public ResponseWrapper activateIMUser(String userName) {
		String url = ROOT_URI + "/" + userName + "/activate";
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, url, header, null);
	}

	@Override
	public ResponseWrapper disconnectIMUser(String userName) {
		String url = ROOT_URI + "/" + userName + "/" + userName + "/disconnect";
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper getIMUserAllChatGroups(String userName) {
		String url = ROOT_URI + "/" + userName + "/joined_chatgroups";
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper getIMUserAllChatRooms(String userName) {
		String url = ROOT_URI + "/" + userName + "/joined_chatrooms";
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

}
