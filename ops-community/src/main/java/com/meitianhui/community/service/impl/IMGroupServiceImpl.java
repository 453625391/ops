package com.meitianhui.community.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.community.constant.IMRspCode;
import com.meitianhui.community.dao.IMGroupMapper;
import com.meitianhui.community.dao.IMUserMapper;
import com.meitianhui.community.easemob.api.ChatGroupAPI;
import com.meitianhui.community.easemob.api.impl.EasemobChatGroupApi;
import com.meitianhui.community.easemob.body.ChatGroupBody;
import com.meitianhui.community.easemob.body.CommonBody;
import com.meitianhui.community.easemob.body.IMUserNamesBody;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;
import com.meitianhui.community.entity.IMGroup;
import com.meitianhui.community.service.IMGroupService;
import com.meitianhui.community.service.IMUserService;
import com.meitianhui.community.util.IMServiceUtil;

/***
 * IM群组逻辑处理实现类
 * 
 * @author 丁硕
 * @date 2016年8月24日
 */
@Service
public class IMGroupServiceImpl implements IMGroupService {

	private ChatGroupAPI chatGroupApi = new EasemobChatGroupApi();

	@Autowired
	private IMGroupMapper imGroupMapper;

	@Autowired
	private IMUserMapper imUserMapper;

	@Autowired
	private DocUtil docUtil;

	@Autowired
	private IMUserService imUserService;

	@Override
	public void getChatGroupDetails(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "group_id" });
		String group_id = paramsMap.get("group_id") + "";
		IMGroup imGroup = imGroupMapper.queryIMGroupDetail(group_id);
		if (imGroup == null) {
			throw new BusinessException(IMRspCode.IM_GROUP_NOT_EXISTS,
					IMRspCode.MSG.get(IMRspCode.IM_GROUP_NOT_EXISTS));
		}
		result.setResultData(imGroup);
	}

	/***
	 * 创建群组，需要先调用环信创建接口，然后将数据进行保存，如果出错，需调用接口将环信中的数据进行删除
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createChatGroup(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "group_name", "desc", "created_by" });
		String created_by = paramsMap.get("created_by") + ""; // 环信用户ID
		// 检查用户是否存在
		Map<String, String> imUser = imUserMapper.queryOneIMUser(created_by);
		if (imUser == null || imUser.isEmpty()) {
			throw new BusinessException(IMRspCode.IM_USER_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_NOT_EXISTS));
		}
		// 成员列表
		String[] members = {};
		String member_ids = StringUtil.formatStr(paramsMap.get("member_ids"));
		if (StringUtils.isNotEmpty(member_ids)) {
			members = member_ids.split(",");
		}
		Date now = new Date();
		IMGroup group = new IMGroup();
		group.setName(StringUtil.formatStr(paramsMap.get("group_name")));
		group.setDescription(StringUtil.formatStr(paramsMap.get("desc")));
		group.setIs_public("false".equals(paramsMap.get("public")) ? "0" : "1"); // 是否公开
		group.setMaxusers(200 + ""); // 200人
		group.setMembersonly("1");
		group.setAllowinvites("1"); // 默认允许群成员邀请别人加入此群
		group.setAffiliations(JSONArray.toJSONString(members));
		group.setAffiliations_count(members.length + "");
		group.setModified(now.getTime() + "");
		group.setCreated(now.getTime() + "");
		group.setOwner(created_by);
		// 加入群不需要批准，可直接进入
		ChatGroupBody groupBody = new ChatGroupBody(group.getName(), group.getDescription(),
				"1".equals(group.getIs_public()), Long.parseLong(group.getMaxusers()), false, group.getOwner(),
				members);
		ResponseWrapper response = chatGroupApi.createChatGroup(groupBody);
		JSONObject data = IMServiceUtil.validResponse(response).getJSONObject("data");

		// 设置groupId,并将数据保存到数据库中
		group.setId(data.getString("groupid"));
		try {
			imGroupMapper.createIMGroup(group);
		} catch (Exception e) {
			// 删除创建的群组,还原操作
			chatGroupApi.deleteChatGroup(group.getId());
			throw e;
		}

		result.setResultData(group);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createChatGroupWithMembers(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "group_name", "desc", "created_by", "member_info" });
		String created_by = paramsMap.get("created_by") + ""; // 环信用户ID
		// 检查用户是否存在
		Map<String, String> imUser = imUserMapper.queryOneIMUser(created_by);
		if (imUser == null || imUser.isEmpty()) {
			throw new BusinessException(IMRspCode.IM_USER_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_NOT_EXISTS));
		}
		List<String> imUserIdList = new ArrayList<String>();
		ResultData newResult = new ResultData();
		// 会员数组
		JSONArray memberInfoList = JSONArray.parseArray(paramsMap.get("member_info").toString());
		for (int i = 0; i < memberInfoList.size(); i++) {
			JSONObject memberInfo = memberInfoList.getJSONObject(i);
			imUserService.getIMUserLoginInfo(memberInfo, newResult);
			JSONObject memberLoginInfo = JSONObject.parseObject(JSON.toJSONString(newResult.getResultData()));
			String im_user_id = memberLoginInfo.getString("im_user_id");
			imUserIdList.add(im_user_id);
		}
		// 创建群组
		paramsMap.put("member_ids", StringUtils.join(imUserIdList, ","));
		this.createChatGroup(paramsMap, result);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void modifyChatGroupInfo(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "group_id" });
		paramsMap.put("modified", (new Date()).getTime() + "");
		imGroupMapper.modifyIMGroup(paramsMap); // 更新群组

		String group_id = StringUtil.formatStr(paramsMap.get("group_id"));
		String group_name = StringUtil.formatStr(paramsMap.get("group_name"));
		String desc = StringUtil.formatStr(paramsMap.get("desc"));
		String maxusers = StringUtil.formatStr(paramsMap.get("maxusers"));
		CommonBody body = new CommonBody();
		if (StringUtils.isNotEmpty(group_name)) {
			body.put("groupname", group_name);
		}
		if (StringUtils.isNotEmpty(desc)) {
			body.put("description", desc);
		}
		if (StringUtils.isNotEmpty(maxusers)) {
			body.put("maxusers", Integer.parseInt(maxusers));
		}
		if (body.getBody().size() > 0) { // 更新环信信息
			IMServiceUtil.validResponse(chatGroupApi.modifyChatGroup(group_id, body));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteChatGroup(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "group_id", "im_user_id" });
		String group_id = paramsMap.get("group_id") + "";
		String im_user_id = paramsMap.get("im_user_id") + "";
		int i = imGroupMapper.deleteIMGroup(group_id, im_user_id);
		if (i > 0) {
			// 删除环信上群组
			IMServiceUtil.validResponse(chatGroupApi.deleteChatGroup(group_id));
		}
	}

	@Override
	public void getChatGroupUsers(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "group_id" });
		String group_id = paramsMap.get("group_id") + "";
		IMGroup imGroup = imGroupMapper.queryIMGroupDetail(group_id);
		if (imGroup == null) {
			throw new BusinessException(IMRspCode.IM_GROUP_NOT_EXISTS,
					IMRspCode.MSG.get(IMRspCode.IM_GROUP_NOT_EXISTS));
		}
		JSONArray memberList = JSONArray.parseArray(imGroup.getAffiliations());
		// 将owner加入进来
		memberList.add(0, imGroup.getOwner());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userIdList", memberList);
		List<Map<String, String>> imUserList = imUserMapper.getIMUserBaseInfoList(params);
		for (Map<String, String> imUser : imUserList) {
			// 查询用户头像
			String head_pic_path = imUser.get("head_pic_path");
			if (StringUtils.isNotEmpty(head_pic_path)) {
				imUser.put("head_pic_path", docUtil.imageUrlFind(head_pic_path));
			}
		}
		result.setResultData(imUserList);
	}

	@Override
	public void getChatGroupLocationUsers(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "group_id" });
		String group_id = paramsMap.get("group_id") + "";
		IMGroup imGroup = imGroupMapper.queryIMGroupDetail(group_id);
		if (imGroup == null) {
			throw new BusinessException(IMRspCode.IM_GROUP_NOT_EXISTS,
					IMRspCode.MSG.get(IMRspCode.IM_GROUP_NOT_EXISTS));
		}
		JSONArray memberList = JSONArray.parseArray(imGroup.getAffiliations());
		// 将owner加入进来
		memberList.add(0, imGroup.getOwner());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userIdList", memberList);
		List<Map<String, String>> imUserList = imUserMapper.getIMUserLocationInfoList(params);
		for (Map<String, String> imUser : imUserList) {
			// 查询用户头像
			String head_pic_path = imUser.get("head_pic_path");
			if (StringUtils.isNotEmpty(head_pic_path)) {
				imUser.put("head_pic_path",  docUtil.imageUrlFind(head_pic_path));
			}
		}
		result.setResultData(imUserList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addSingleUserToChatGroup(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "im_user_id", "group_id" });
		String im_user_id = paramsMap.get("im_user_id") + "";
		String group_id = paramsMap.get("group_id") + "";
		IMGroup imGroup = imGroupMapper.queryIMGroupDetail(group_id);
		if (imGroup == null) {
			throw new BusinessException(IMRspCode.IM_GROUP_NOT_EXISTS,
					IMRspCode.MSG.get(IMRspCode.IM_GROUP_NOT_EXISTS));
		}
		JSONArray affiliations = JSONArray.parseArray(imGroup.getAffiliations());
		// 1、检查用户成员是否存在
		if (affiliations.contains(im_user_id) || im_user_id.equals(imGroup.getOwner())) {
			throw new BusinessException(IMRspCode.IM_USER_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_EXISTS));
		}
		// 2、查询用户是否存在
		Map<String, String> imUser = imUserMapper.queryOneIMUser(im_user_id);
		if (imUser == null || imUser.isEmpty()) {
			throw new BusinessException(IMRspCode.IM_USER_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_NOT_EXISTS));
		}
		affiliations.add(im_user_id);
		// 更新数据到数据库中
		int i = imGroupMapper.updateIMGroupUsers(group_id, affiliations.toString(), affiliations.size(),
				imGroup.getAffiliations());
		if (i > 0) {
			// 调用环信接口
			IMServiceUtil.validResponse(chatGroupApi.addSingleUserToChatGroup(group_id, im_user_id));
		} else {
			throw new BusinessException(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL,
					IMRspCode.MSG.get(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addBatchUsersToChatGroup(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "group_id", "member_info" });
		List<String> imUserIdList = new ArrayList<String>();
		ResultData newResult = new ResultData();
		// 会员数组
		JSONArray memberInfoList = JSONArray.parseArray(paramsMap.get("member_info").toString());
		for (int i = 0; i < memberInfoList.size(); i++) {
			JSONObject memberInfo = memberInfoList.getJSONObject(i);
			imUserService.getIMUserLoginInfo(memberInfo, newResult);
			JSONObject memberLoginInfo = JSONObject.parseObject(JSONObject.toJSONString(newResult.getResultData()));
			String im_user_id = memberLoginInfo.getString("im_user_id");
			imUserIdList.add(im_user_id);
		}

		String group_id = paramsMap.get("group_id") + "";
		IMGroup imGroup = imGroupMapper.queryIMGroupDetail(group_id);
		if (imGroup == null) {
			throw new BusinessException(IMRspCode.IM_GROUP_NOT_EXISTS,
					IMRspCode.MSG.get(IMRspCode.IM_GROUP_NOT_EXISTS));
		}

		JSONArray affiliations = JSONArray.parseArray(imGroup.getAffiliations());
		List<String> addUserList = new ArrayList<String>();
		for (String im_user_id : imUserIdList) {
			if (!(im_user_id.equals(imGroup) || affiliations.contains(im_user_id))) {
				affiliations.add(im_user_id);
				addUserList.add(im_user_id);
			}
		}
		if (addUserList.size() > 0) {
			// 更新数据到数据库中
			int i = imGroupMapper.updateIMGroupUsers(group_id, affiliations.toString(), affiliations.size(),
					imGroup.getAffiliations());
			if (i > 0) {
				// 调用环信接口
				IMUserNamesBody body = new IMUserNamesBody(addUserList.toArray(new String[] {}));
				IMServiceUtil.validResponse(chatGroupApi.addBatchUsersToChatGroup(group_id, body));
			} else {
				throw new BusinessException(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL,
						IMRspCode.MSG.get(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL));
			}
		}
	}

	/****
	 * 先加入锁，到时候改成数据库更新,主要用于用户自己退出群组
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void removeSingleUserFromChatGroup(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "im_user_id", "group_id" });
		String im_user_id = paramsMap.get("im_user_id") + "";
		String group_id = paramsMap.get("group_id") + "";
		IMGroup imGroup = imGroupMapper.queryIMGroupDetail(group_id);
		if (imGroup == null) {
			throw new BusinessException(IMRspCode.IM_GROUP_NOT_EXISTS,
					IMRspCode.MSG.get(IMRspCode.IM_GROUP_NOT_EXISTS));
		}
		JSONArray affiliations = JSONArray.parseArray(imGroup.getAffiliations());
		if (!affiliations.contains(im_user_id)) { // 用户未在群列表中
			throw new BusinessException(IMRspCode.IM_USER_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_NOT_EXISTS));
		}
		affiliations.remove(im_user_id);
		// 更新数据到数据库中
		int i = imGroupMapper.updateIMGroupUsers(group_id, affiliations.toJSONString(), affiliations.size(),
				imGroup.getAffiliations());
		if (i > 0) {
			// 调用环信接口
			IMServiceUtil.validResponse(chatGroupApi.removeSingleUserFromChatGroup(group_id, im_user_id));
		} else {
			throw new BusinessException(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL,
					IMRspCode.MSG.get(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL));
		}
	}

	/***
	 * 批量移除用户，主要场景用于管理员减人 im_user_ids,聊天数组
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void removeBatchUsersFromChatGroup(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[] { "im_user_ids", "group_id" });
		String im_user_ids = paramsMap.get("im_user_ids") + "";
		String group_id = paramsMap.get("group_id") + "";
		IMGroup imGroup = imGroupMapper.queryIMGroupDetail(group_id);
		if (imGroup == null) {
			throw new BusinessException(IMRspCode.IM_GROUP_NOT_EXISTS,
					IMRspCode.MSG.get(IMRspCode.IM_GROUP_NOT_EXISTS));
		}
		JSONArray affiliations = JSONArray.parseArray(imGroup.getAffiliations());
		String[] userIds = im_user_ids.split(",");
		for (int i = 0; i < userIds.length; i++) {
			affiliations.remove(userIds[i]);
		}
		// 更新数据到数据库中
		int i = imGroupMapper.updateIMGroupUsers(group_id, affiliations.toJSONString(), affiliations.size(),
				imGroup.getAffiliations());
		if (i > 0) {
			// 调用环信接口
			IMServiceUtil.validResponse(chatGroupApi.removeBatchUsersFromChatGroup(group_id, userIds));
		} else {
			throw new BusinessException(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL,
					IMRspCode.MSG.get(IMRspCode.IM_UPDATE_MEMBER_LIST_FAIL));
		}
	}

}
