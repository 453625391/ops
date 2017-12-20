package com.meitianhui.member.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.member.constant.Constant;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.dao.MemberDao;
import com.meitianhui.member.dao.SupplierDao;
import com.meitianhui.member.entity.MDSupplier;
import com.meitianhui.member.entity.MDUserMember;
import com.meitianhui.member.service.SupplierService;

/**
 * 会员管理服务层
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
@Service
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	public RedisUtil redisUtil;

	@Autowired
	public SupplierDao supplierDao;

	@Autowired
	public MemberDao memberDao;
	
	@Override
	public void handleSupplierSyncRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "supplier_id", "supplier_no", "supplier_name",
					"area_id", "address", "contact_person", "contact_tel", "business_type_key", "registered_date" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String supplier_id = (String) paramsMap.get("supplier_id");
			String contact_person = (String) paramsMap.get("contact_person");
			String mobile = (String) paramsMap.get("contact_tel");
			tempMap.put("supplier_id", supplier_id);
			List<MDSupplier> mDSupplierList = supplierDao.selectMDSupplier(tempMap);
			// 如果供应商不存在,则注册供应商,如果存在,则更新供应商信息
			if (mDSupplierList.size() > 0) {
				throw new BusinessException(RspCode.MEMBER_EXIST, RspCode.MSG.get(RspCode.MEMBER_EXIST));
			}

			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> bizParams = new HashMap<String, String>();

			/**
			 * 检查手机号是否已经注册成了供应商， 如果手机号已经成为用户，但是不是供应商,则获取用户id,
			 * 如果手机号不存在,则直接注册手机号,并获取用户id 如果供应商类型存在,则直接抛出异常
			 */
			reqParams.put("service", "infrastructure.memberTypeValidateByMobile");
			bizParams.put("mobile", mobile);
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_SUPPLIER);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			String user_flag = (String) dateMap.get("user");
			if (((String) dateMap.get("member_type")).equals("exist")) {
				throw new BusinessException(RspCode.MEMBER_EXIST, RspCode.MSG.get(RspCode.MEMBER_EXIST));
			}
			String user_id = null;
			if (user_flag.equals("exist")) {
				user_id = (String) dateMap.get("user_id");
			} else {
				// 如果用户不存在,则注册用户
				reqParams.clear();
				bizParams.clear();
				resultMap.clear();
				reqParams.put("service", "infrastructure.syncUserRegister");
				bizParams.put("mobile", mobile);
				bizParams.put("user_name", contact_person);
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				dateMap = (Map<String, Object>) resultMap.get("data");
				user_id = (String) dateMap.get("user_id");
			}
			// 新增供应商
			MDSupplier mDSupplier = new MDSupplier();
			BeanConvertUtil.mapToBean(mDSupplier, paramsMap);
			mDSupplier.setSys_status(Constant.STATUS_NORMAL);
			mDSupplier.setCreated_date(new Date());
			supplierDao.insertMDSupplier(mDSupplier);
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("supplier_id", supplier_id);
			logMap.put("category", "运营系统数据同步");
			logMap.put("tracked_date", new Date());
			logMap.put("event", "供应商注册");
			supplierDao.insertMDSupplierLog(logMap);
			// 初始化会员资产信息
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "finance.memberAssetInit");
			bizParams.put("member_id", supplier_id);
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_SUPPLIER);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			// 构建供应商和用户之间的关系
			MDUserMember mDUserMember = new MDUserMember();
			mDUserMember.setIs_admin("Y");
			mDUserMember.setMember_id(supplier_id);
			mDUserMember.setUser_id(user_id);
			mDUserMember.setMember_type_key(Constant.MEMBER_TYPE_SUPPLIER);
			memberDao.insertMDUserMember(mDUserMember);
			reqParams.clear();
			bizParams.clear();
			tempMap.clear();
			reqParams = null;
			bizParams = null;
			tempMap = null;
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleSupplierSyncRegisterForHYD(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "supplier_no", "supplier_name", "area_id", "address",
					"contact_person", "contact_tel", "registered_date" });
			String mobile = paramsMap.get("contact_tel") + "";
			String user_name = paramsMap.get("contact_person") + "";
			String supplier_id = IDUtil.getUUID();

			Map<String, Object> tempMap = new HashMap<String, Object>();
			paramsMap.put("supplier_id", supplier_id);
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			/**
			 * 检查手机号是否已经注册成了供应商， 如果手机号已经成为用户，但是不是供应商,则获取用户id,
			 * 如果手机号不存在,则直接注册手机号,并获取用户id 如果供应商类型存在,则直接抛出异常
			 */
			reqParams.put("service", "infrastructure.memberTypeValidateByMobile");
			bizParams.put("mobile", mobile);
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_SUPPLIER);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			String user_flag = (String) dateMap.get("user");
			if (((String) dateMap.get("member_type")).equals("exist")) {
				throw new BusinessException(RspCode.MEMBER_EXIST, RspCode.MSG.get(RspCode.MEMBER_EXIST));
			}
			String user_id = null;
			if (user_flag.equals("exist")) {
				user_id = (String) dateMap.get("user_id");
			} else {
				// 如果用户不存在,则注册用户
				reqParams.clear();
				bizParams.clear();
				resultMap.clear();
				reqParams.put("service", "infrastructure.syncUserRegister");
				bizParams.put("mobile", mobile);
				bizParams.put("user_name", user_name);
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				dateMap = (Map<String, Object>) resultMap.get("data");
				user_id = (String) dateMap.get("user_id");
			}
			// 转换区域id
			bizParams.clear();
			bizParams.put("code", paramsMap.get("area_id"));
			Map<String, Object> areaMapping = memberDao.selectMDAreaMapping(bizParams);
			if (null != areaMapping) {
				String mth_code = StringUtil.formatStr(areaMapping.get("mth_code"));
				paramsMap.put("area_id", mth_code);
			}

			// 新增供应商
			MDSupplier mDSupplier = new MDSupplier();
			BeanConvertUtil.mapToBean(mDSupplier, paramsMap);
			mDSupplier.setSys_status(Constant.STATUS_NORMAL);
			mDSupplier.setBusiness_type_key("GYSLB_01");
			mDSupplier.setCreated_date(new Date());
			supplierDao.insertMDSupplier(mDSupplier);
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("supplier_id", supplier_id);
			logMap.put("category", "惠易定数据同步");
			logMap.put("tracked_date", new Date());
			logMap.put("event", "惠易定供应商信息同步");
			supplierDao.insertMDSupplierLog(logMap);
			// 初始化会员资产信息
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "finance.memberAssetInit");
			bizParams.put("member_id", supplier_id);
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_SUPPLIER);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			// 构建供应商和用户之间的关系
			MDUserMember mDUserMember = new MDUserMember();
			mDUserMember.setIs_admin("Y");
			mDUserMember.setMember_id(supplier_id);
			mDUserMember.setUser_id(user_id);
			mDUserMember.setMember_type_key(Constant.MEMBER_TYPE_SUPPLIER);
			memberDao.insertMDUserMember(mDUserMember);
			reqParams.clear();
			bizParams.clear();
			tempMap.clear();
			reqParams = null;
			bizParams = null;
			tempMap = null;
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleSupplierSyncRegisterForHYD3(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "supplier_id", "supplier_no", "supplier_name",
					"area_id", "address", "contact_person", "contact_tel", "registered_date" });
			String mobile = paramsMap.get("contact_tel") + "";
			String user_name = paramsMap.get("contact_person") + "";
			String supplier_id = paramsMap.get("supplier_id") + "";
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			bizParams.put("supplier_id", supplier_id);
			List<MDSupplier> mDSupplierList = supplierDao.selectMDSupplier(bizParams);
			if (mDSupplierList.size() > 0) {
				throw new BusinessException(RspCode.MEMBER_EXIST, RspCode.MSG.get(RspCode.MEMBER_EXIST));
			}
			/**
			 * 检查手机号是否已经注册成了供应商， 如果手机号已经成为用户，但是不是供应商,则获取用户id,
			 * 如果手机号不存在,则直接注册手机号,并获取用户id 如果供应商类型存在,则直接抛出异常
			 */
			reqParams.put("service", "infrastructure.memberTypeValidateByMobile");
			bizParams.put("mobile", mobile);
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_SUPPLIER);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			String user_flag = (String) dateMap.get("user");
			if (((String) dateMap.get("member_type")).equals("exist")) {
				throw new BusinessException(RspCode.MEMBER_EXIST, RspCode.MSG.get(RspCode.MEMBER_EXIST));
			}
			String user_id = null;
			if (user_flag.equals("exist")) {
				user_id = (String) dateMap.get("user_id");
			} else {
				// 如果用户不存在,则注册用户
				reqParams.clear();
				bizParams.clear();
				resultMap.clear();
				reqParams.put("service", "infrastructure.syncUserRegister");
				bizParams.put("mobile", mobile);
				bizParams.put("user_name", user_name);
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				dateMap = (Map<String, Object>) resultMap.get("data");
				user_id = (String) dateMap.get("user_id");
			}
			// 转换区域id
			bizParams.clear();
			bizParams.put("code", paramsMap.get("area_id"));
			Map<String, Object> areaMapping = memberDao.selectMDAreaMapping(bizParams);
			if (null != areaMapping) {
				String mth_code = StringUtil.formatStr(areaMapping.get("mth_code"));
				paramsMap.put("area_id", mth_code);
			}

			// 新增供应商
			MDSupplier mDSupplier = new MDSupplier();
			BeanConvertUtil.mapToBean(mDSupplier, paramsMap);
			mDSupplier.setSys_status(Constant.STATUS_NORMAL);
			mDSupplier.setBusiness_type_key("GYSLB_01");
			mDSupplier.setCreated_date(new Date());
			supplierDao.insertMDSupplier(mDSupplier);
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("supplier_id", supplier_id);
			logMap.put("category", "惠易定数据同步");
			logMap.put("tracked_date", new Date());
			logMap.put("event", "惠易定供应商信息同步");
			supplierDao.insertMDSupplierLog(logMap);
			// 初始化会员资产信息
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "finance.memberAssetInit");
			bizParams.put("member_id", supplier_id);
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_SUPPLIER);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			// 构建供应商和用户之间的关系
			MDUserMember mDUserMember = new MDUserMember();
			mDUserMember.setIs_admin("Y");
			mDUserMember.setMember_id(supplier_id);
			mDUserMember.setUser_id(user_id);
			mDUserMember.setMember_type_key(Constant.MEMBER_TYPE_SUPPLIER);
			memberDao.insertMDUserMember(mDUserMember);
			reqParams.clear();
			bizParams.clear();
			reqParams = null;
			bizParams = null;
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleSupplierSyncEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "supplier_id" });
			if (paramsMap.size() == 1) {
				throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE,
						RspCode.MSG.get(RspCode.SYSTEM_NO_PARAMS_UPDATE));
			}
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String supplier_id = (String) paramsMap.get("supplier_id");
			tempMap.put("supplier_id", supplier_id);
			List<MDSupplier> mDSupplierList = supplierDao.selectMDSupplier(tempMap);
			if (mDSupplierList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			supplierDao.updateMDSupplierSync(paramsMap);
			tempMap.clear();
			tempMap = null;
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void supplierFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "supplier_id" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("supplier_id", paramsMap.get("supplier_id"));
			List<MDSupplier> supplierList = supplierDao.selectMDSupplier(tempMap);
			if (supplierList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDSupplier supplier = supplierList.get(0);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("supplier_id", StringUtil.formatStr(supplier.getSupplier_id()));
			resultMap.put("supplier_no", StringUtil.formatStr(supplier.getSupplier_no()));
			resultMap.put("supplier_name", StringUtil.formatStr(supplier.getSupplier_name()));
			resultMap.put("business_type_key", supplier.getBusiness_type_key());
			resultMap.put("legal_person", supplier.getLegal_person());
			resultMap.put("legal_person_tel", supplier.getLegal_person_tel());
			resultMap.put("contact_person", supplier.getContact_person());
			resultMap.put("contact_tel", supplier.getContact_tel());
			resultMap.put("area_id", supplier.getArea_id());
			resultMap.put("area_desc", supplier.getArea_desc());
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void supplierListPromotionFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "supplier_id" });
			String supplier_id = StringUtil.formatStr(paramsMap.get("supplier_id"));
			Map<String, Object> promotion = new HashMap<String, Object>();
			List<String> supplierList = new ArrayList<String>();
			if (!StringUtils.isEmpty(supplier_id)) {
				supplierList = StringUtil.str2List(supplier_id, ",");
				if (supplierList.size() > 1) {
					paramsMap.remove("supplier_id");
					paramsMap.put("supplier_id_in", supplierList);
				}
			}
			for (String supplier : supplierList) {
				Map<String, Object> promotionMap = new HashMap<String, Object>();
				promotionMap.put("supplier_id", supplier);
				// 起送价格
				promotionMap.put("starting_price", "50");
				// 邮费
				promotionMap.put("shipping_fee", "15");

				promotion.put(supplier, promotionMap);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("promotion", promotion);
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void supplierLoginValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "user_id" });
			paramsMap.put("member_type_key", Constant.MEMBER_TYPE_SUPPLIER);
			List<MDUserMember> mDUserMemberList = memberDao.selectMDUserMember(paramsMap);
			if (mDUserMemberList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDUserMember mDUserMember = mDUserMemberList.get(0);

			// 验证门店状态
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("supplier_id", mDUserMember.getMember_id());
			List<MDSupplier> mDSupplierList = supplierDao.selectMDSupplier(tempMap);
			if (mDSupplierList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDSupplier mDSupplier = mDSupplierList.get(0);

			String sys_status = mDSupplier.getSys_status();
			if (!sys_status.equals(Constant.STATUS_NORMAL)) {
				throw new BusinessException(RspCode.MEMBER_STATUS_ERROR, "会员被禁用");
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("member_id", mDUserMember.getMember_id());
			resultMap.put("is_admin", mDUserMember.getIs_admin());
			resultMap.put("area_id", mDSupplier.getArea_id());
			resultMap.put("supplier_no", mDSupplier.getSupplier_no());
			resultMap.put("supplier_name", mDSupplier.getSupplier_name());
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	public void supplierStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "supplier_id", "sys_status" });
			String sys_status = paramsMap.get("sys_status") + "";
			String supplier_id = paramsMap.get("supplier_id") + "";
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			bizParams.put("supplier_id", supplier_id);
			bizParams.put("sys_status", sys_status);
			supplierDao.updateMDSupplierSync(bizParams);
			// 如果状态是禁用，则清除会员登陆授权信息
			if (sys_status.equals(Constant.STATUS_DISABLED)) {
				bizParams.clear();
				bizParams.put("member_id", supplier_id);
				bizParams.put("member_type_key", Constant.MEMBER_TYPE_SUPPLIER);
				List<MDUserMember> userMemberList = memberDao.selectMDUserMember(bizParams);
				String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
				for (MDUserMember um : userMemberList) {
					// 删除授权token信息
					reqParams.clear();
					bizParams.clear();
					reqParams.put("service", "infrastructure.userTokenClear");
					bizParams.put("member_id", um.getMember_id());
					bizParams.put("user_id", um.getUser_id());
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					HttpClientUtil.postShort(user_service_url, reqParams);
				}
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	

}
