package com.meitianhui.test;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.meitianhui.common.util.HttpClientUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	private static final String base_url = "http://localhost:8080/ops-community/";
	
	public static void main(String[] args) {
//		addSingleUserToChatGroup();
		getChatGroupUsers();
//		getChatGroupDetails();
//		System.out.println(getIMUserLoginInfo());
//		createChatGroup("8e172d61a1894abb86b41416c83733e6");
//		System.out.println(getIMUserDetail());
//		getUserGroupList();
//		modifyIMChatGroupInfo();
//		openOneLiveRoom();
//		removeUserFromLiveRoom();
//		createChatGroupWithMembers();
//		queryLiveStoresList();
//		getLiveRoomDetails();
//		closeLiveroom();
	}
	
	public static String getIMUserLoginInfo(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("member_id", "11693335");
		params.put("member_type_key", "consumer");
		params.put("nick_name", "测试账号");
		params.put("head_pic_path", "2828.jpg");
		String result = requestUrl("community", "chat.getIMUserLoginInfo", params);
		JSONObject json = JSONObject.fromObject(result);
		return json.toString();
	}
	
	public static String modifyIMChatGroupInfo(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("group_id", "234694784478871980");
		params.put("owner", "悟饭");
		params.put("group_name", "每天惠测试群");
		params.put("head_pic_path", "每天惠测试群");
		String result = requestUrl("community", "chat.modifyChatGroupInfo", params);
		JSONObject json = JSONObject.fromObject(result);
		return json.toString();
	}
	
	public static String getIMUserAccount(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("member_id", "11934963");
		params.put("member_type_key", "consumer");
		String result = requestUrl("community", "chat.getIMUserAccount", params);
		JSONObject json = JSONObject.fromObject(result);
		return json.toString();
	}
	
	public static String getIMUserDetail(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("im_user_id", "6f2464d67bb04213969d25700a8d5a4c");
		String result = requestUrl("community", "chat.getIMUserDetail", params);
		JSONObject json = JSONObject.fromObject(result);
		return json.toString();
	}
	
	
	public static void createChatGroup(String created_by){
		Map<String, String> params = new HashMap<String, String>();
		params.put("group_name", "技术讨论组");
		params.put("desc", "这是一个技术交流组");
		params.put("created_by", created_by);
		String result = requestUrl("community", "chat.createGroup", params);
		System.out.println(result);
	}
	
	public static void createChatGroupWithMembers(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("group_name", "技术讨论组");
		params.put("desc", "这是一个技术交流组");
		params.put("created_by", "768d0e2e1c2a41b49b0b1f6c6535d598");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("member_id", "11694028");
		jsonObject.put("nickname", "丁戈1111");
		jsonObject.put("head_pic_path", "head.jpg");
		jsonObject.put("member_type_key", "consumer");
		JSONArray array = new JSONArray();
		array.add(jsonObject);
		params.put("member_info", array.toString());
		String result = requestUrl("community", "chat.createChatGroupWithMembers", params);
		System.out.println(result);
	}
	
	public static void getUserGroupList(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("im_user_id", "8e172d61a1894abb86b41416c83733e6");
		String result = requestUrl("community", "chat.getIMUserGroupList", params);
		System.out.println(result);
	}
	
	//获取群组所有信息
	public static void getChatGroupUsers(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("group_id", "237000121693245876");
		String result = requestUrl("community", "chat.getChatGroupUsers", params);
		System.out.println(result);
	}
	
	//获取群组所有信息
	public static void getChatGroupDetails(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("group_id", "234615081835430324");
		String result = requestUrl("community", "chat.getChatGroupDetails", params);
		System.out.println(result);
	}
	
	public static void addSingleUserToChatGroup(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("group_id", "233932490408985008");
		params.put("im_user_id", "nba14");
		String result = requestUrl("community", "chat.addSingleUserToChatGroup", params);
		System.out.println(result);
	}
	
	public static void openOneLiveRoom(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "开播啦");
		params.put("cover", "xxx.png");
		params.put("url", "http://www.baidu.com");
		params.put("desc", "这是我的第一个直播室");
		params.put("label", "娱乐");
		params.put("created_by", "f80200e4e9744d3bac2dd24d7500beac");
		String result = requestUrl("community", "live.openOneLiveRoom", params);
		System.out.println(result);
	}
	
	public static void getLiveRoomDetails(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("room_id", "236137469676880296");
		String result = requestUrl("community", "live.getLiveRoomUsers", params);
		System.out.println(result);
	}
	
	public static void removeUserFromLiveRoom(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("room_id", "236137469676880296");
		params.put("im_user_id", "3342272e78ee4a52b6116a3ab6efa84a");
		String result = requestUrl("community", "live.removeUserFromLiveRoom", params);
		System.out.println(result);
	}
	
	public static void queryLiveStoresList(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("longitude", "114.034954");
		params.put("latitude", "22.540183");
		String result = requestUrl("community", "live.queryLiveStoresList", params);
		System.out.println(result);
	}
	
	public static void closeLiveroom(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("room_id", "236965860273553844");
		params.put("im_user_id", "b12ef3edd321421fa46c524f6e487d44");
		String result = requestUrl("community", "live.closeOneLiveRoom", params);
		System.out.println(result);
	}
	
	private static String requestUrl(String url, String service, Map<String, String> params){
		try{
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("service", service);
			reqParams.put("token", "55075e58476f47b0a25a58277dc41f24");
			reqParams.put("params", JSONObject.fromObject(params).toString());
			JSONObject result = JSONObject.fromObject(HttpClientUtil.post(base_url + url, reqParams));
			if (result == null) {
				throw new Exception(result == null ? "" : result.toString());
			}
			if ("succ".equals(result.get("rsp_code"))) {
				String data = result.getString("data");
				if (StringUtils.isNotEmpty(data)) {
					return data;
				}
			} else {
				throw new Exception(result.getString("error_msg"));
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
