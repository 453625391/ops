package com.meitianhui.community.easemob.api.impl;

import org.apache.commons.lang.StringUtils;

import com.meitianhui.community.easemob.api.ChatGroupAPI;
import com.meitianhui.community.easemob.body.ChatGroupBody;
import com.meitianhui.community.easemob.body.CommonBody;
import com.meitianhui.community.easemob.body.IMUserNamesBody;
import com.meitianhui.community.easemob.constant.EasemobConstant;
import com.meitianhui.community.easemob.constant.HttpMethod;
import com.meitianhui.community.easemob.utils.HeaderHelper;
import com.meitianhui.community.easemob.utils.HttpClientRestApiInvoker;
import com.meitianhui.community.easemob.wrapper.HeaderWrapper;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

public class EasemobChatGroupApi implements ChatGroupAPI {

	private static final String ROOT_URI = EasemobConstant.EASEMOB_API_SERVICE_URL + "/chatgroups";

	@Override
	public ResponseWrapper getChatGroups(Long limit, String cursor) {
		return null;
	}

	@Override
	public ResponseWrapper getChatGroupDetails(String[] groupIds) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + StringUtils.join(groupIds, ",");
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper createChatGroup(ChatGroupBody body) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, ROOT_URI, header, body);
	}

	@Override
	public ResponseWrapper modifyChatGroup(String groupId, CommonBody body) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId;
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.PUT, url, header, body);
	}

	@Override
	public ResponseWrapper deleteChatGroup(String groupId) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId;
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.DELETE, url, header, null);
	}

	@Override
	public ResponseWrapper getChatGroupUsers(String groupId) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId + "/users";
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper addSingleUserToChatGroup(String groupId, String userId) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId + "/users/" + userId;
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, url, header, null);
	}

	@Override
	public ResponseWrapper addBatchUsersToChatGroup(String groupId, IMUserNamesBody body) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId + "/users";
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, url, header, body);
	}

	@Override
	public ResponseWrapper removeSingleUserFromChatGroup(String groupId, String userId) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId + "/users/" + userId;
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.DELETE, url, header, null);
	}

	@Override
	public ResponseWrapper removeBatchUsersFromChatGroup(String groupId, String[] userIds) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId + "/users/" + StringUtils.join(userIds, ",");
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.DELETE, url, header, null);
	}

	@Override
	//将群组 Owner 转让给其他用户后，原 Owner 可能被删除。如果希望原 Owner 作为成员继续留在该群组中，需要再次将该用户添加至群组。
	public ResponseWrapper transferChatGroupOwner(String groupId, ChatGroupBody body) {
		return null;
	}

	@Override
	public ResponseWrapper getChatGroupBlockUsers(String groupId) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId + "/blocks/users";
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.GET, url, header, null);
	}

	@Override
	public ResponseWrapper addSingleBlockUserToChatGroup(String groupId, String userId) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId + "/blocks/users/" + userId;
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, url, header, null);
	}

	@Override
	public ResponseWrapper addBatchBlockUsersToChatGroup(String groupId, IMUserNamesBody body) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId + "/blocks/users";
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, url, header, body);
	}

	@Override
	public ResponseWrapper removeSingleBlockUserFromChatGroup(String groupId, String userId) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId + "/blocks/users/" + userId;
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.DELETE, url, header, null);
	}

	@Override
	public ResponseWrapper removeBatchBlockUsersFromChatGroup(String groupId, String[] userIds) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		String url = ROOT_URI + "/" + groupId + "/blocks/users/" + StringUtils.join(userIds,",");
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.DELETE, url, header, null);
	}

}
