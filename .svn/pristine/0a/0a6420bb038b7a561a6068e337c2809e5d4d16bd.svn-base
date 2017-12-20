package com.meitianhui.finance.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.finance.entity.FDMemberCapitalAccount;
import com.meitianhui.finance.entity.FDMemberCapitalAccountApplication;

public interface MemberCapitalAccountDao {

	/**
	 * 新增会员银行卡信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertFDMemberCapitalAccount(FDMemberCapitalAccount fDMemberCapitalAccount) throws Exception;

	/**
	 * 新增会员银行卡日志
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertFDMemberCapitalAccountLog(Map<String, Object> paramsMap) throws Exception;

	/**
	 * 新增会员绑定银行卡申请记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertFdMemberCapitalAccountApplication(
			FDMemberCapitalAccountApplication fDMemberCapitalAccountApplication) throws Exception;

	

	/**
	 * 查询会员银行卡信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public FDMemberCapitalAccount selectFDMemberCapitalAccount(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询会员绑定银行卡申请记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectFDMemberCapitalAccountApplication(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询会员绑定银行卡申请待审核记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectFDMemberCapitalAccountApplicationApply(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 查询银行列表信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> selectFDBank(Map<String, Object> map) throws Exception;

	/**
	 * 更新会员资金信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void updateFDMemberCapitalAccount(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 更新会员绑定银行卡申请记录状态
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void updateFDMemberCapitalAccountApplication(Map<String, Object> paramMap) throws Exception;

	
	/**
	 * 删除会员绑定的银行卡信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void deleteFdMemberCapitalAccount(Map<String, Object> map) throws Exception;

}
