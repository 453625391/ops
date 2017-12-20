package com.meitianhui.community.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/***
 * IM群组逻辑处理接口
 * 
 * @author 丁硕
 * @date 2016年8月24日
 */
public interface IMGroupService {

	/***
	 * 获取群组详情信息
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月9日
	 */
	public void getChatGroupDetails(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 创建一个群组
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月9日
	 */
	public void createChatGroup(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 将会员批量加入一个群聊,并创建一个群组
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月31日
	 */
	public void createChatGroupWithMembers(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 更新群组信息
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月29日
	 */
	public void modifyChatGroupInfo(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/*****
	 * 删除群组信息
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月29日
	 */
	public void deleteChatGroup(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 获取群组下的所有的成员信息
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月9日
	 */
	public void getChatGroupUsers(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 获取群组中位置信息成员列表
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2017年2月27日
	 */
	public void getChatGroupLocationUsers(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 添加单个用户到群组内
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月9日
	 */
	public void addSingleUserToChatGroup(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 批量添加用户到群组内
	 * @param ResultData
	 * @author 丁硕
	 * @date   2016年10月27日
	 */
	public void addBatchUsersToChatGroup(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/****
	 * 将某个用户从群组中移除
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月9日
	 */
	public void removeSingleUserFromChatGroup(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 批量移除用户
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年11月2日
	 */
	public void removeBatchUsersFromChatGroup(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
}
