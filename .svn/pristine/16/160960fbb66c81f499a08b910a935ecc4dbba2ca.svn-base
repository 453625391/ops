package com.meitianhui.community.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.community.constant.IMRspCode;
import com.meitianhui.community.constant.RspCode;
import com.meitianhui.community.dao.IMGroupMapper;
import com.meitianhui.community.dao.IMUserMapper;
import com.meitianhui.community.easemob.api.IMUserAPI;
import com.meitianhui.community.easemob.api.impl.EasemobIMUsersApi;
import com.meitianhui.community.easemob.body.IMUserBody;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;
import com.meitianhui.community.entity.IMUser;
import com.meitianhui.community.entity.IMUserMember;
import com.meitianhui.community.service.IMUserService;
import com.meitianhui.community.util.IMServiceUtil;

/***
 * 关于IM逻辑处理接口
 * 
 * @author 丁硕
 * @date 2016年8月9日
 */
@Service
public class IMUserServiceImpl implements IMUserService{
	
	private IMUserAPI imUserApi = new EasemobIMUsersApi();
	
	@Autowired
	private IMUserMapper imUserMapper;
	
	@Autowired
	private IMGroupMapper imGroupMapper;
	
	@Autowired
	private DocUtil docUtil;
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void getIMUserLoginInfo(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"member_id", "member_type_key"});
		String member_id = paramsMap.get("member_id") + "";
		String member_type_key = paramsMap.get("member_type_key") + "";
		//查询数据是否存在指定的IM账号信息
		String im_user_id = imUserMapper.getIMUserId(member_id, member_type_key);
		if(StringUtils.isEmpty(im_user_id)){
			im_user_id = this.regIMUser(member_id, member_type_key, paramsMap);
		}
		paramsMap.clear();
		paramsMap.put("im_user_id", im_user_id);
		//获取IM用户详情
		this.getIMUserDetail(paramsMap, result);
	}
	
	@Override
	public void getIMUserDetail(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"im_user_id"});
		String im_user_id = (String) paramsMap.get("im_user_id");
		Map<String, String> imUser = imUserMapper.queryOneIMUser(im_user_id);
		if(imUser == null || imUser.isEmpty()){
			throw new BusinessException(IMRspCode.IM_USER_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_NOT_EXISTS));
		}
		//查询用户头像
		String head_pic_path = StringUtil.formatStr(imUser.get("head_pic_path"));
		if(StringUtils.isNotEmpty(head_pic_path)){
			imUser.put("head_pic_path",  docUtil.imageUrlFind(head_pic_path));
		}
		result.setResultData(imUser);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void modifyIMUserInfo(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"im_user_id"});
		String im_user_id = paramsMap.get("im_user_id") + "";
		String nickname = StringUtil.formatStr(paramsMap.get("nickname"));	
		Map<String, String> imUser = imUserMapper.queryOneIMUser(im_user_id);
		if(imUser == null || imUser.isEmpty()){
			throw new BusinessException(IMRspCode.IM_USER_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_NOT_EXISTS));
		}
		int i = imUserMapper.updateIMUser(paramsMap);
		if(i > 0 && StringUtils.isNotEmpty(nickname) && !nickname.equals(imUser.get("nickname"))){
			//更新环信上的信息
			imUserApi.modifyIMUserNickNameWithAdminToken(im_user_id, nickname);
		}
	}

	public void getIMUserAccount(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"member_id", "member_type_key"});
		String member_id = paramsMap.get("member_id") + "";
		String member_type_key = paramsMap.get("member_type_key") + "";
		//查询数据是否存在指定的IM账号信息
		String im_user_id = imUserMapper.getIMUserId(member_id, member_type_key);
		if(StringUtils.isEmpty(im_user_id)){
			throw new BusinessException(IMRspCode.IM_USER_NOT_EXISTS, IMRspCode.MSG.get(IMRspCode.IM_USER_NOT_EXISTS));
		} else{
			paramsMap.clear();
			paramsMap.put("im_user_id", im_user_id);
			//获取IM用户详情
			this.getIMUserDetail(paramsMap, result);
		}
	}
	
	/***
	 * 注册IM用户信息,加锁，一次只能注册一个
	 * @param member_id
	 * @param member_type_key
	 * @throws BusinessException
	 * @author 丁硕
	 * @date   2016年8月10日
	 */
	@Transactional(rollbackFor=Exception.class)
	private synchronized String regIMUser(final String member_id, final String member_type_key, Map<String, Object> paramsMap) throws BusinessException, SystemException{	//注册IM用户信息
		String im_user_id = imUserMapper.getIMUserId(member_id, member_type_key);
		if(StringUtils.isEmpty(im_user_id)){
			Map<String, Object> userMemberMap = imUserMapper.getUserMember(member_id, member_type_key);
			if(userMemberMap == null || userMemberMap.isEmpty()){
				throw new BusinessException(RspCode.USER_NOT_EXIST, RspCode.MSG.get(RspCode.USER_NOT_EXIST));
			}
			Date now = new Date();
			im_user_id = IDUtil.getUUID();
			IMUser imUser = new IMUser();
			imUser.setIm_user_id(im_user_id);
			imUser.setUsername(im_user_id);
			imUser.setPassword(imUser.getIm_user_id());
			imUser.setNickname(StringUtil.formatStr(paramsMap.get("nickname")));	//昵称
			imUser.setHead_pic_path(StringUtil.formatStr(paramsMap.get("head_pic_path")));	//头像
			imUser.setActivated("true");
			imUser.setCreated(now.getTime() + "");
			imUser.setModified(now.getTime() + "");
			//新增记录
			imUserMapper.insertIMUser(imUser);
			//保存对应的关系
			IMUserMember userMember = new IMUserMember();
			userMember.setUser_id(userMemberMap.get("user_id") + "");
			userMember.setIm_user_id(im_user_id);
			userMember.setMember_id(member_id);
			userMember.setMember_type_key(member_type_key);
			userMember.setCreated_date(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			imUserMapper.insertIMUserMember(userMember);
			//调用IM注册接口
			IMUserBody userBody = new IMUserBody(im_user_id, imUser.getPassword(), imUser.getNickname());
			ResponseWrapper response = imUserApi.createNewIMUserSingle(userBody);
			IMServiceUtil.validResponse(response);
		}
		return im_user_id;
	}

	@Override
	public void getIMUserGroupList(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException {
		ValidateUtil.validateParams(paramsMap, new String[]{"im_user_id"});
		String im_user_id = paramsMap.get("im_user_id") + "";
		List<Map<String, String>> groupList = imGroupMapper.getIMUserGroupList(im_user_id);
		if(groupList == null){
			groupList = new ArrayList<Map<String, String>>();
		}
		result.setResultData(groupList);
	}

}
