package com.meitianhui.infrastructure.dao;

import java.util.Map;

import com.meitianhui.infrastructure.entity.User;
import com.meitianhui.infrastructure.entity.UserLog;

/**
 * @author mole.wang 2015年12月20日
 *
 */
public interface UserDao {

	/**
	 * 新增用户信息
	 * @param user
	 * @throws Exception
	 */
	public void insertUser(User user)throws Exception;
	

	/**
	 * 用户日志
	 * @param userLog
	 * @throws Exception
	 */
	public void insertUserLog(UserLog userLog) throws Exception;
	
	/**
	 * 会员登陆日志
	 * @param userLog
	 * @throws Exception
	 */
	public void insertMemberLoginLog(Map<String,Object> memberLoginLog) throws Exception;
	
	/**
	 * 更新用户信息
	 * @param map
	 * @throws Exception
	 */
	public void updateUser(Map<String,Object> map)throws Exception;
	
	/**
	 * 更新用户登陆手机号
	 * @param map
	 * @throws Exception
	 */
	public void updateUserMobile(Map<String,Object> map)throws Exception;
	
	/**
	 * 查询用户信息
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	public User selectUser(Map<String,Object> map)throws Exception;
	
	/**
	 * 删除用户
	 * @param userAccount
	 * @return
	 */
	public void deleteUser(Map<String,Object> map)throws Exception;
	
	
	/**
	 * 删除会员登陆信息
	 * @param userAccount
	 * @return
	 */
	public void deleteMemberLoginInfo(Map<String,Object> map)throws Exception;
	
	
}
