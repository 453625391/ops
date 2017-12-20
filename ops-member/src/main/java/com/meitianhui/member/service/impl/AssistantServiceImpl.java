package com.meitianhui.member.service.impl;

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
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.member.constant.Constant;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.dao.AssistantDao;
import com.meitianhui.member.entity.MDAssistant;
import com.meitianhui.member.service.AssistantService;

/**
 * 店东助手
 * 
 * @author Tiny
 *
 */
@Service
public class AssistantServiceImpl implements AssistantService {

	@Autowired
	public AssistantDao assistantDao;

	@Override
	public void storesAssistantCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "assistant_id", "assistant_name", "contact_tel" });
		MDAssistant assistant = new MDAssistant();
		BeanConvertUtil.mapToBean(assistant, paramsMap);
		assistant.setStatus(Constant.STATUS_NORMAL);
		assistantDao.insertAssistant(assistant);
		// 初始化会员资产信息
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
		reqParams.clear();
		bizParams.clear();
		reqParams.put("service", "finance.memberAssetInit");
		bizParams.put("member_id", paramsMap.get("assistant_id"));
		bizParams.put("member_type_key", paramsMap.get("member_type_key"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
	}

	@Override
	public void storesAssistantListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = assistantDao.selectStoresAssistantList(paramsMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void storesAssistantDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> assistant = assistantDao.selectAssistant(paramsMap);
		if (null == assistant) {
			throw new BusinessException(RspCode.STORES_ASSISTANT_ERROR, "店东助手信息不存在");
		}
		assistant.put("registered_date",
				DateUtil.date2Str((Date) assistant.get("registered_date"), DateUtil.fmt_yyyyMMddHHmmss));
		assistant.put("desc1", StringUtil.formatStr(assistant.get("desc1")));
		assistant.put("id_card", StringUtil.formatStr(assistant.get("id_card")));
		assistant.put("head_pic_path", StringUtil.formatStr(assistant.get("head_pic_path")));
		assistant.put("sex_key", StringUtil.formatStr(assistant.get("sex_key")));
		assistant.put("area_id", StringUtil.formatStr(assistant.get("area_id")));
		assistant.put("address", StringUtil.formatStr(assistant.get("address")));
		assistant.put("contact_tel", StringUtil.formatStr(assistant.get("contact_tel")));
		result.setResultData(assistant);
	}

	@Override
	public void assistantServiceStoresListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = assistantDao.selectAssistantServiceStoresList(paramsMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void storesAssistantEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "assistant_id", "assistant_name", "contact_tel" });
		assistantDao.updateAssistant(paramsMap);
	}

	
	@Override
	public void storesAssistantDelete(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "assistant_id" });
		assistantDao.deleteAssistant(paramsMap);
	}
	
	

}
