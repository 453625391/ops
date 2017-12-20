package com.meitianhui.community.service;

import java.util.Map;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;

/***
 * 关于IM逻辑处理接口
 * 
 * @author 丁硕
 * @date 2016年7月21日
 */
public interface IMUserService {
	
	/***
	 * 获取IM用户登录信息，如果没有注册过，会自动注册相关信息
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月26日
	 */
	public void getIMUserLoginInfo(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 获取IM用户详细信息
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月26日
	 */
	public void getIMUserDetail(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 修改用户资料
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月30日
	 */
	public void modifyIMUserInfo(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
	/***
	 * 根据member_id与member_type获取IM用户详细信息
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月9日
	 */
	public void getIMUserAccount(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;

	/***
	 * 查询单个用户的群组列表
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @author 丁硕
	 * @date   2016年8月25日
	 */
	public void getIMUserGroupList(Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException;
	
}
