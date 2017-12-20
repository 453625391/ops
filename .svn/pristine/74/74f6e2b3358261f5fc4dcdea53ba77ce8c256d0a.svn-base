package com.meitianhui.member.dao;

import java.util.List;
import java.util.Map;

import com.meitianhui.member.entity.MDSupplier;

public interface SupplierDao {


	/**
	 * 供应商信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertMDSupplier(MDSupplier mDSupplier) throws Exception;

	/**
	 * 供应商信息日志
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertMDSupplierLog(Map<String, Object> map) throws Exception;

	
	/**
	 * 查询供应商会员信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<MDSupplier> selectMDSupplier(Map<String, Object> map) throws Exception;

	
	/**
	 * 更新供应商信息,用于运营管理系统的供应商信息更新
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	void updateMDSupplierSync(Map<String, Object> map) throws Exception;

}
