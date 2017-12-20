package com.meitianhui.community.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.community.constant.IMRspCode;
import com.meitianhui.community.dao.IMLiveroomMapper;
import com.meitianhui.community.dao.IMUserMapper;
import com.meitianhui.community.easemob.api.ChatRoomAPI;
import com.meitianhui.community.easemob.api.impl.EasemobChatRoomApi;
import com.meitianhui.community.easemob.api.impl.EasemobIMSendMessageApi;
import com.meitianhui.community.easemob.body.ChatRoomBody;
import com.meitianhui.community.easemob.body.CommandMessageBody;
import com.meitianhui.community.easemob.body.MessageBody;
import com.meitianhui.community.easemob.constant.TargetType;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;
import com.meitianhui.community.entity.IMLiveroom;
import com.meitianhui.community.service.LiveRoomService;
import com.meitianhui.community.util.IMServiceUtil;

@Service
public class LiveroomServiceImpl implements LiveRoomService{
	
	private Logger logger = Logger.getLogger(LiveroomServiceImpl.class);
	
	@Autowired
	private IMLiveroomMapper imLiveroomMapper;
	
	@Autowired
	private IMUserMapper imUserMapper;
	
	@Autowired
	private DocUtil docUtil;
	
	private ChatRoomAPI chatRoomApi = new EasemobChatRoomApi();
	private static EasemobIMSendMessageApi msgApi = new EasemobIMSendMessageApi();

	/***
	 * 定义一个直播室，就是需要创建一个聊天室
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void openOneLiveRoom(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"name", "cover", "url", "desc", "label", "created_by"});
		String created_by = StringUtil.formatStr(paramsMap.get("created_by"));
		//检查用户是否存在
		Map<String, String> imUser = imUserMapper.queryOneIMUser(created_by);
		if(imUser == null || imUser.isEmpty()){
			throw new BusinessException(IMRspCode.IM_USER_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_NOT_EXISTS));
		}
		//加个逻辑，检查关闭之前所有用户已开播的直播室
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", LIVEROOM_STATUS_LIVE);
		params.put("owner", created_by);
		List<Map<String, String>> roomList = imLiveroomMapper.queryLiveroomList(params);
		if(roomList != null && roomList.size() > 0){	//循环关闭直播室
			Map<String, Object> closeParamsMap = new HashMap<String, Object>();
			for(Map<String, String> room : roomList){
				closeParamsMap.put("im_user_id", created_by);
				closeParamsMap.put("room_id", room.get("id"));
				this.closeOneLiveRoom(closeParamsMap, result);
			}
		}

		//创建一个新的直播室
		Date now = new Date();
		IMLiveroom liveroom = new IMLiveroom();
		liveroom.setName(StringUtil.formatStr(paramsMap.get("name")));
		liveroom.setCover(StringUtil.formatStr(paramsMap.get("cover")));
		liveroom.setLabel(StringUtil.formatStr(paramsMap.get("label")));
		liveroom.setUrl(StringUtil.formatStr(paramsMap.get("url")));
		liveroom.setDescription(StringUtil.formatStr(paramsMap.get("desc")));
		liveroom.setOwner(created_by);
		liveroom.setStatus(LIVEROOM_STATUS_LIVE); //直播中
		liveroom.setAffiliations("[]");
		liveroom.setCreated(now.getTime() + "");
		liveroom.setModified(now.getTime() + "");
		
		//调用接口，创建聊天室
		ChatRoomBody roomBody = new ChatRoomBody(liveroom.getName(), liveroom.getDescription(), 200l, liveroom.getOwner(), null);
		ResponseWrapper response = chatRoomApi.createChatRoom(roomBody);
		JSONObject data = IMServiceUtil.validResponse(response).getJSONObject("data");
		//设置groupId,并将数据保存到数据库中
		liveroom.setId(data.getString("id"));
		try{
			imLiveroomMapper.createLiveroom(liveroom);
		} catch(Exception e){
			//删除创建的聊天室,还原操作
			chatRoomApi.deleteChatRoom(liveroom.getId());
			throw e;
		}
		//返回结果
		result.setResultData(liveroom);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void closeOneLiveRoom(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"im_user_id", "room_id"});
		String im_user_id = paramsMap.get("im_user_id") + "";
		String room_id = paramsMap.get("room_id") + "";
		IMLiveroom liveroom = imLiveroomMapper.queryLiveroomDetail(room_id);
		if(liveroom == null){
			throw new BusinessException(IMRspCode.IM_LIVEROOM_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_LIVEROOM_NOT_EXISTS));
		}
		if(!im_user_id.equals(liveroom.getOwner())){
			throw new BusinessException(IMRspCode.IM_USER_AUTHORITY_NOT_ENOUGH, IMRspCode.MSG.get(IMRspCode.IM_USER_AUTHORITY_NOT_ENOUGH));
		}
		if(LIVEROOM_STATUS_LIVE.equals(liveroom.getStatus())){
			//更新状态，以后需要对状态进行检查更新
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("room_id", room_id);
			params.put("owner", im_user_id);
			params.put("status", LIVEROOM_STATUS_FINISHED);
			imLiveroomMapper.updateLiveRoomStatus(params);
			//发送透传消息
			MessageBody body = new CommandMessageBody(TargetType.CHATROOM, new String[]{room_id}, "admin", "closeRoom", null);
			IMServiceUtil.validResponse(msgApi.sendMessage(body));
			//删除聊天室
			IMServiceUtil.validResponse(chatRoomApi.deleteChatRoom(room_id));
		}
		logger.info("成功关闭直播室：" + room_id);
	}



	@Override
	public void getLiveRoomDetails(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"room_id"});
		String room_id = paramsMap.get("room_id") + "";
		IMLiveroom liveroom = imLiveroomMapper.queryLiveroomDetail(room_id);
		if(liveroom == null){
			throw new BusinessException(IMRspCode.IM_LIVEROOM_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_LIVEROOM_NOT_EXISTS));
		}
		result.setResultData(liveroom);
	}
	
	@Override
	public void getLiveRoomUsers(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"room_id"});
		String room_id = paramsMap.get("room_id") + "";
		IMLiveroom liveroom = imLiveroomMapper.queryLiveroomDetail(room_id);
		if(liveroom == null){
			throw new BusinessException(IMRspCode.IM_LIVEROOM_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_LIVEROOM_NOT_EXISTS));
		}
		JSONArray memberList = JSONArray.parseArray(liveroom.getAffiliations());
		//将owner加入进来
		memberList.add(0, liveroom.getOwner());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userIdList", memberList);
		List<Map<String, String>> imUserList = imUserMapper.getIMUserBaseInfoList(params);
		for(Map<String, String> imUser : imUserList){
			//查询用户头像
			String head_pic_path = imUser.get("head_pic_path");
			if(StringUtils.isNotEmpty(head_pic_path)){
				imUser.put("head_pic_path", docUtil.imageUrlFind(head_pic_path));
			}
		}
		result.setResultData(imUserList);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void addUserToLiveRoom(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"im_user_id", "room_id"});
		String im_user_id = paramsMap.get("im_user_id") + "";
		String room_id = paramsMap.get("room_id") + "";
		IMLiveroom liveroom = imLiveroomMapper.queryLiveroomDetail(room_id);
		if(liveroom == null){
			throw new BusinessException(IMRspCode.IM_LIVEROOM_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_LIVEROOM_NOT_EXISTS));
		}
		if(!LIVEROOM_STATUS_LIVE.equals(liveroom.getStatus())){
			throw new BusinessException(IMRspCode.LIVEROOM_CLOSED, IMRspCode.MSG.get(IMRspCode.LIVEROOM_CLOSED));
		}
		JSONArray affiliations = JSONArray.parseArray(liveroom.getAffiliations());
		//1、检查用户成员是否存在,如果存在直接返回成功
		if(!(affiliations.contains(im_user_id) || im_user_id.equals(liveroom.getOwner()))){
			//查询用户是否存在
			Map<String, String> imUser = imUserMapper.queryOneIMUser(im_user_id);
			if(imUser == null || imUser.isEmpty()){
				throw new BusinessException(IMRspCode.IM_USER_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_NOT_EXISTS));
			}
			affiliations.add(im_user_id);
			//更新数据到数据库中
			int i = imLiveroomMapper.updateLiveroomUsers(room_id, affiliations.toString(), liveroom.getAffiliations());
			if(i > 0){
				//调用环信接口
				IMServiceUtil.validResponse(chatRoomApi.addSingleUserToChatRoom(room_id, im_user_id));
			} else{
				throw new BusinessException(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL, IMRspCode.MSG.get(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL));
			}
		}
	}

	/****
	 * 先加入锁，到时候改成数据库更新
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void removeUserFromLiveRoom(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"im_user_id", "room_id"});
		String im_user_id = paramsMap.get("im_user_id") + "";
		String room_id = paramsMap.get("room_id") + "";
		IMLiveroom liveroom = imLiveroomMapper.queryLiveroomDetail(room_id);
		if(liveroom == null){
			throw new BusinessException(IMRspCode.IM_LIVEROOM_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_LIVEROOM_NOT_EXISTS));
		}
		if(!LIVEROOM_STATUS_LIVE.equals(liveroom.getStatus())){
			throw new BusinessException(IMRspCode.LIVEROOM_CLOSED, IMRspCode.MSG.get(IMRspCode.LIVEROOM_CLOSED));
		}
		JSONArray affiliations = JSONArray.parseArray(liveroom.getAffiliations());
		if(!affiliations.contains(im_user_id)){	//用户未在群列表中
			throw new BusinessException(IMRspCode.IM_USER_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_NOT_EXISTS));
		}
		affiliations.remove(im_user_id);
		//更新数据到数据库中
		int i = imLiveroomMapper.updateLiveroomUsers(room_id, affiliations.toString(), liveroom.getAffiliations());
		if(i > 0){
			//调用环信接口
			IMServiceUtil.validResponse(chatRoomApi.removeSingleUserFromChatRoom(room_id, im_user_id));
		} else{
			throw new BusinessException(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL, IMRspCode.MSG.get(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL));
		}
	}

	/***
	 * 查询正在直播的直播室
	 */
	@Override
	public void queryLiveRoomList(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", LIVEROOM_STATUS_LIVE);
		List<Map<String, String>> roomList = imLiveroomMapper.queryLiveroomList(params);
		result.setResultData(roomList);
	}
	
	@Override
	public void queryUserLiveRoom(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"im_user_id"});
		Map<String, Object> params = new HashMap<String, Object>();
		String im_user_id = paramsMap.get("im_user_id") + "";
		params.put("status", LIVEROOM_STATUS_LIVE);
		params.put("owner", im_user_id);
		List<Map<String, String>> roomList = imLiveroomMapper.queryLiveroomList(params);
		if(roomList == null || roomList.size() < 1){
			throw new BusinessException(IMRspCode.LIVEROOM_NOT_FOUND, IMRspCode.MSG.get(IMRspCode.LIVEROOM_NOT_FOUND));
		}
		result.setResultData(roomList.get(0));
	}

	@Override
	public void queryUserLiveRoomHistoryList(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"im_user_id"});
		String im_user_id = paramsMap.get("im_user_id") + "";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", LIVEROOM_STATUS_FINISHED);
		params.put("owner", im_user_id);
		List<Map<String, String>> roomList = imLiveroomMapper.queryLiveroomList(params);
		result.setResultData(roomList);
	}

	@Override
	public void queryLiveStoresList(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"latitude", "longitude"});
		String longitude = paramsMap.get("longitude") + "";
		String latitude = paramsMap.get("latitude") + "";
		Double longitude_gt = Double.parseDouble(longitude) - 1;
		Double latitude_gt = Double.parseDouble(latitude) - 1;
		Double longitude_lt = Double.parseDouble(longitude) + 1;
		Double latitude_lt = Double.parseDouble(latitude) + 1;
		paramsMap.put("longitude_gt", longitude_gt);
		paramsMap.put("latitude_gt", latitude_gt);
		paramsMap.put("longitude_lt", longitude_lt);
		paramsMap.put("latitude_lt", latitude_lt);
		paramsMap.put("live_status", LIVEROOM_STATUS_LIVE);
		List<Map<String, String>> list = imLiveroomMapper.queryLiveStoresList(paramsMap);
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		List<String> doc_ids = new ArrayList<String>();
		for(Map<String, String> mDStores :list ){
			Map<String,String> tempMap = new HashMap<String,String>();
			tempMap.put("stores_id", mDStores.get("stores_id"));
			tempMap.put("stores_name", mDStores.get("stores_name"));
			tempMap.put("stores_type_key", mDStores.get("stores_type_key"));
			tempMap.put("distance", StringUtil.formatStr(mDStores.get("distance")));
			String neighbor_pic_path = StringUtil.formatStr(mDStores.get("neighbor_pic_path"));
			if (neighbor_pic_path.equals("")) {
				List<String> tempList = new ArrayList<String>();
				String new_facade_pic_path = StringUtil.formatStr(mDStores.get("new_facade_pic_path"));
				if (!new_facade_pic_path.equals("")) {
					tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
				}
				String new_stores_pic_path = StringUtil.formatStr(mDStores.get("new_stores_pic_path"));
				if (!new_stores_pic_path.equals("")) {
					tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
				}
				if (tempList.size() > 0) {
					neighbor_pic_path = tempList.get(0);
				}
			}
			tempMap.put("neighbor_pic_path", neighbor_pic_path);
			tempMap.put("logo_pic_path", StringUtil.formatStr(mDStores.get("logo_pic_path")));
			resultList.add(tempMap);
			// 解析图片
			doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(mDStores.get("logo_pic_path")), ","));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public Map<String, String> queryLiveroomByUrl(String status, String url) {
		return imLiveroomMapper.queryLiveroomByUrl(status, url);
	}
	
}
