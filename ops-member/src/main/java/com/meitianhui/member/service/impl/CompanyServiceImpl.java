package com.meitianhui.member.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.member.constant.Constant;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.dao.CompanyDao;
import com.meitianhui.member.entity.MDCompany;
import com.meitianhui.member.service.CompanyService;

/**
 * 会员管理服务层
 * 
 * @author Tiny
 *
 */
@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	public CompanyDao companyDao;

	@Override
	public void companyFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			MDCompany mDCompany = companyDao.selectMDCompany(paramsMap);
			if (mDCompany == null) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("company_id", StringUtil.formatStr(mDCompany.getCompany_id()));
			resultMap.put("company_code", StringUtil.formatStr(mDCompany.getCompany_code()));
			resultMap.put("company_name", StringUtil.formatStr(mDCompany.getCompany_name()));
			resultMap.put("contact_person", StringUtil.formatStr(mDCompany.getContact_person()));
			resultMap.put("contact_tel", StringUtil.formatStr(mDCompany.getContact_tel()));
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
	public void handleMdCompanySync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "company_id", "company_name", "registered_date",
					"area_id", "address", "contact_person", "contact_tel" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("company_id", paramsMap.get("company_id"));
			MDCompany mDCompany = companyDao.selectMDCompany(tempMap);
			if (null == mDCompany) {
				mDCompany = new MDCompany();
				Date date = new Date();
				BeanConvertUtil.mapToBean(mDCompany, paramsMap);
				mDCompany.setCreated_date(date);
				mDCompany.setModified_date(date);
				mDCompany.setBusiness_status("opening");
				mDCompany.setStatus(Constant.STATUS_NORMAL);
				companyDao.insertMDCompany(mDCompany);
				// 初始化会员资产信息
				Map<String, String> reqParams = new HashMap<String, String>();
				Map<String, Object> bizParams = new HashMap<String, Object>();
				String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
				reqParams.clear();
				bizParams.clear();
				reqParams.put("service", "finance.memberAssetInit");
				bizParams.put("member_id", paramsMap.get("company_id"));
				bizParams.put("member_type_key", Constant.MEMBER_TYPE_COMPANY);
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
				Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
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
