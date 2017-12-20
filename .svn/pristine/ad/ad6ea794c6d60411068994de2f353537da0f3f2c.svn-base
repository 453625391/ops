package com.meitianhui.member.dao;


import java.util.List;
import java.util.Map;

import com.meitianhui.member.entity.MDSalesman;
import com.meitianhui.member.entity.MDUserMember;

public interface SalesmanDao {
	
	/**
	 * 新增业务员
	 * 
	 * @param mdSalesman
	 * @throws Exception
	 */
	public void insertSalesman(MDSalesman mdSalesman) throws Exception; 
	
	/**
	 * 业务员信息日志
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void insertSalesmanLog(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询业务员列表
	 * @param mdSalesman
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectSalesmanList(Map<String, Object> mdSalesman) throws Exception;
	
	
	
	/**
	 * 查询业务员日志列表
	 * @param mdSalesman
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectSalesmanLogList(Map<String, Object> mdSalesman) throws Exception;
	
	/**
	 * 更新业务员
	 * 
	 * @param mdSalesman
	 * @throws Exception
	 */
	public int updateSalesman(Map<String, Object> mdSalesman) throws Exception;
	
	/**
	 * 查询业务员详情
	 * 
	 * @param mdSalesman
	 * @throws Exception
	 */
	public Map<String, Object> selectSalesman(Map<String, Object> mdSalesman) throws Exception;
	
	
	/**
	 * 添加会员与用户关系
	 * 
	 * @param mdUserMember
	 * @throws Exception
	 */
	public void insertUserMember(MDUserMember mdUserMember) throws Exception;
	
	/**
	 * 查询会员与用户关系
	 * 
	 * @param mdUserMember
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectUserMember(Map<String, Object> mdUserMember) throws Exception;
	
	/**
	 * 删除会员与用户关系
	 * 
	 * @param mdUserMember
	 * @return
	 * @throws Exception
	 */
	public int deleteUserMember(Map<String, Object> mdUserMember) throws Exception;
	
	
	/**
	 * 查询业务员总数 
	 * 
	 * @param mdSalesman
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectSalesmanNum(Map<String, Object> mdSalesman) throws Exception;
	
	/**
	 * 查询业务员本月新增数
	 * 
	 * @param mdSalesman
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectSalesmanMonthNum(Map<String, Object> mdSalesman) throws Exception;
	
}
