package com.meitianhui.finance.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.dao.MemberCapitalAccountDao;
import com.meitianhui.finance.entity.FDMemberCapitalAccount;
import com.meitianhui.finance.entity.FDMemberCapitalAccountApplication;
import com.meitianhui.finance.service.MemberCapitalAccountService;

/**
 * 会员账号的服务层
 * 
 * @author Tiny
 *
 */
@Service
public class MemberCapitalAccountImpl implements MemberCapitalAccountService {

	@Autowired
	public MemberCapitalAccountDao memberCapitalAccountDao;

	@Override
	public void memberCapitalAccountCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key",
					"capital_account_type_key", "cardholder", "capital_account", "publishing_institutions" });
			FDMemberCapitalAccount fDMemberCapitalAccount = null;
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("member_id", paramsMap.get("member_id"));
			fDMemberCapitalAccount = memberCapitalAccountDao.selectFDMemberCapitalAccount(tempMap);
			if (null != fDMemberCapitalAccount) {
				throw new BusinessException(RspCode.CAPITAL_ACCOUNT_EXIST,
						RspCode.MSG.get(RspCode.CAPITAL_ACCOUNT_EXIST));
			}
			fDMemberCapitalAccount = new FDMemberCapitalAccount();
			BeanConvertUtil.mapToBean(fDMemberCapitalAccount, paramsMap);
			fDMemberCapitalAccount.setCapital_account_id(IDUtil.getUUID());
			fDMemberCapitalAccount.setBinded_date(new Date());
			memberCapitalAccountDao.insertFDMemberCapitalAccount(fDMemberCapitalAccount);
			tempMap.clear();
			tempMap.put("log_id", IDUtil.getUUID());
			tempMap.put("capital_account_id", paramsMap.get("member_id"));
			tempMap.put("category", "绑定");
			tempMap.put("tracked_date", new Date());
			tempMap.put("event", "绑定资金账号:" + paramsMap.toString());
			memberCapitalAccountDao.insertFDMemberCapitalAccountLog(tempMap);

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCapitalAccountEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			memberCapitalAccountDao.updateFDMemberCapitalAccount(paramsMap);
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("log_id", IDUtil.getUUID());
			tempMap.put("capital_account_id", paramsMap.get("member_id"));
			tempMap.put("category", "修改");
			tempMap.put("tracked_date", new Date());
			tempMap.put("event", "修改资金信息:" + paramsMap.toString());
			memberCapitalAccountDao.insertFDMemberCapitalAccountLog(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCapitalAccountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			FDMemberCapitalAccount fDMemberCapitalAccount = memberCapitalAccountDao
					.selectFDMemberCapitalAccount(paramsMap);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (null != fDMemberCapitalAccount) {
				resultMap.put("capital_account_type_key", fDMemberCapitalAccount.getCapital_account_type_key());
				resultMap.put("capital_account", fDMemberCapitalAccount.getCapital_account());
				resultMap.put("cardholder", fDMemberCapitalAccount.getCardholder());
				resultMap.put("publishing_institutions", fDMemberCapitalAccount.getPublishing_institutions());
				resultMap.put("binded_date",
						DateUtil.date2Str(fDMemberCapitalAccount.getBinded_date(), DateUtil.fmt_yyyyMMddHHmmss));
				resultMap.put("remark", StringUtil.formatStr(fDMemberCapitalAccount.getRemark()));
			}
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCapitalAccountDelete(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			memberCapitalAccountDao.deleteFdMemberCapitalAccount(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCapitalAccountApplicationCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "member_id", "member_type_key", "capital_account_type_key", "cardholder",
							"organization_code", "capital_account", "publishing_institutions", "stores_name",
							"contact_tel", "contact_person" });
			FDMemberCapitalAccountApplication application = new FDMemberCapitalAccountApplication();
			Date date = new Date();
			BeanConvertUtil.mapToBean(application, paramsMap);
			application.setApplication_id(IDUtil.getUUID());
			application.setCreated_date(date);
			application.setModified_date(date);
			application.setStatus("unprocess");
			Map<String, Object> jsonDataMap = new HashMap<String, Object>();
			jsonDataMap.put("stores_name", paramsMap.get("stores_name"));
			jsonDataMap.put("contact_person", paramsMap.get("contact_person"));
			jsonDataMap.put("contact_tel", paramsMap.get("contact_tel"));
			application.setJson_data(FastJsonUtil.toJson(jsonDataMap));
			memberCapitalAccountDao.insertFdMemberCapitalAccountApplication(application);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCapitalAccountApplicationEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "application_id", "operator", "status" });
			memberCapitalAccountDao.updateFDMemberCapitalAccountApplication(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCapitalAccountApplicationApplyListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> applicationList = memberCapitalAccountDao
					.selectFDMemberCapitalAccountApplicationApply(paramsMap);
			for (Map<String, Object> application : applicationList) {
				application.put("created_date",
						DateUtil.date2Str((Date) application.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				application.put("remark", StringUtil.formatStr(application.get("remark")));
				Map<String, Object> jsonDataMap = FastJsonUtil.jsonToMap(application.get("json_data") + "");
				application.remove("json_data");
				application.put("stores_name", jsonDataMap.get("stores_name"));
				application.put("contact_person", jsonDataMap.get("contact_person"));
				application.put("contact_tel", jsonDataMap.get("contact_tel"));
			}
			Map<String, Object> resultDate = new HashMap<String, Object>();
			resultDate.put("list", applicationList);
			result.setResultData(resultDate);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberCapitalAccountApplicationListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> applicationList = memberCapitalAccountDao
					.selectFDMemberCapitalAccountApplication(paramsMap);
			for (Map<String, Object> application : applicationList) {
				application.put("created_date",
						DateUtil.date2Str((Date) application.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				application.put("remark", StringUtil.formatStr(application.get("remark")));
				Map<String, Object> jsonDataMap = FastJsonUtil.jsonToMap(application.get("json_data") + "");
				application.remove("json_data");
				application.put("stores_name", jsonDataMap.get("stores_name"));
				application.put("contact_person", jsonDataMap.get("contact_person"));
				application.put("contact_tel", jsonDataMap.get("contact_tel"));
			}
			Map<String, Object> resultDate = new HashMap<String, Object>();
			resultDate.put("list", applicationList);
			result.setResultData(resultDate);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void bankListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> bankList = memberCapitalAccountDao.selectFDBank(paramsMap);
			Map<String, Object> resultDate = new HashMap<String, Object>();
			resultDate.put("list", bankList);
			result.setResultData(resultDate);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

}
