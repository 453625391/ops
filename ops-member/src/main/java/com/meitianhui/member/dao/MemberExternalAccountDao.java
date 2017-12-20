package com.meitianhui.member.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.member.entity.MDMemberExternalAccount;

public interface MemberExternalAccountDao {

	/**
	 * 新增绑定信息
	 * 
	 * @Title: insertMemberExternalAccount
	 * @param memberExternalAccount
	 * @throws Exception
	 * @author tiny
	 */
	public void insertMemberExternalAccount(MDMemberExternalAccount memberExternalAccount) throws Exception;

	/**
	 * 查询绑定信息
	 * 
	 * @Title: selectMemberExternalAccount
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	public MDMemberExternalAccount selectMemberExternalAccount(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询绑定列表
	 * 
	 * @Title: selectMemberExternalAccountList
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	public List<Map<String, Object>> selectMemberExternalAccountList(Map<String, Object> map) throws Exception;

	/**
	 * 更新绑定信息
	 * 
	 * @Title: updateMemberExternalAccount
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	public int updateMemberExternalAccount(Map<String, Object> map) throws Exception;

	/**
	 * 删除绑定信息
	 * 
	 * @Title: deleteMemberExternalAccount
	 * @param map
	 * @return
	 * @throws Exception
	 * @author tiny
	 */
	public int deleteMemberExternalAccount(Map<String, Object> map) throws Exception;

}
