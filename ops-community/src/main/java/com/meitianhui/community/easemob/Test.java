package com.meitianhui.community.easemob;

import com.meitianhui.community.easemob.api.ChatRoomAPI;
import com.meitianhui.community.easemob.api.impl.EasemobChatRoomApi;
import com.meitianhui.community.easemob.utils.EasemobBaseApiUtil;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

public class Test {

	private static ChatRoomAPI chatRoomApi = new EasemobChatRoomApi();
	//private static IMUserAPI userApi = new EasemobIMUsersApi();
	
	public static void main(String[] args) {
		String token = EasemobBaseApiUtil.getAuthToken();
		System.out.println(token);
		//创建用户
		//IMUserBody body = new IMUserBody("liutao", "123456", "taotao");
		//ResponseWrapper response = userApi.createNewIMUserBatch(body);
		//System.out.println(response);
		//创建聊天室
		//ChatRoomBody roomBody = new ChatRoomBody("第一个聊天室", "测试", null, "liutao", null);
		//ResponseWrapper response = chatRoomApi.createChatRoom(roomBody);
		//System.out.println(response);
		ResponseWrapper response = chatRoomApi.getAllChatRooms();
		System.out.println(response);
		
	}
}
