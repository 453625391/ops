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
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.member.constant.Constant;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.dao.SaleAssistantDao;
import com.meitianhui.member.dao.SaleAssistantHistoryDao;
import com.meitianhui.member.entity.MDSaleAssistant;
import com.meitianhui.member.entity.MDSaleAssistantHistory;
import com.meitianhui.member.service.SaleAssistantService;

/**
 * 社区导购服务层
 * 
 * @author Tiny
 *
 */
@Service
public class SaleAssistantServiceImpl implements SaleAssistantService {

	@Autowired
	public SaleAssistantDao saleAssistantDao;
	@Autowired
	public SaleAssistantHistoryDao saleAssistantHistoryDao;

	@Override
	public void handleSaleAssistantApply(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "consumer_id", "consumer_mobile", "stores_id", "stores_name" });
		List<Map<String, Object>> list = saleAssistantDao.selectMDSaleAssistant(paramsMap);
		if (list.size() > 0) {
			throw new BusinessException(RspCode.SALE_ASSISTANT_ERROR, "您已经申请过此门店的导购,请等待店东审核");
		}
		Date date = new Date();
		MDSaleAssistant saleAssistant = new MDSaleAssistant();
		BeanConvertUtil.mapToBean(saleAssistant, paramsMap);
		saleAssistant.setStatus("applied");
		saleAssistant.setCreated_date(date);
		saleAssistant.setModified_date(date);
		saleAssistantDao.insertMDSaleAssistant(saleAssistant);

		// 记录日志
		MDSaleAssistantHistory consumerHistory = new MDSaleAssistantHistory();
		consumerHistory.setMember_id(paramsMap.get("consumer_id") + "");
		consumerHistory.setMember_type_key(Constant.MEMBER_TYPE_CONSUMER);
		consumerHistory.setHistory_id(IDUtil.getUUID());
		consumerHistory.setEvent_desc("您已申请成为【" + paramsMap.get("stores_name") + "】的社区导购");
		consumerHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(consumerHistory);
	}

	@Override
	public void handleSaleAssistantInvite(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "consumer_id", "consumer_mobile", "stores_id", "stores_name" });
		List<Map<String, Object>> list = saleAssistantDao.selectMDSaleAssistant(paramsMap);
		if (list.size() > 0) {
			throw new BusinessException(RspCode.SALE_ASSISTANT_ERROR, "消费者已经是您的社区导购!");
		}
		Date date = new Date();
		MDSaleAssistant saleAssistant = new MDSaleAssistant();
		BeanConvertUtil.mapToBean(saleAssistant, paramsMap);
		saleAssistant.setStatus("approved");
		saleAssistant.setCreated_date(date);
		saleAssistant.setModified_date(date);
		saleAssistantDao.insertMDSaleAssistant(saleAssistant);

		// 记录日志
		MDSaleAssistantHistory consumerHistory = new MDSaleAssistantHistory();
		consumerHistory.setMember_id(paramsMap.get("consumer_id") + "");
		consumerHistory.setMember_type_key(Constant.MEMBER_TYPE_CONSUMER);
		consumerHistory.setHistory_id(IDUtil.getUUID());
		consumerHistory.setEvent_desc("【" + paramsMap.get("stores_name") + "】邀请您成为社区导购");
		consumerHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(consumerHistory);

		// 记录日志
		MDSaleAssistantHistory storesHistory = new MDSaleAssistantHistory();
		storesHistory.setMember_id(paramsMap.get("stores_id") + "");
		storesHistory.setMember_type_key(Constant.MEMBER_TYPE_STORES);
		storesHistory.setHistory_id(IDUtil.getUUID());
		storesHistory.setEvent_desc("您已邀请【" + paramsMap.get("consumer_mobile") + "】成为您的社区导购");
		storesHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(storesHistory);
	}

	@Override
	public void handleSaleAssistantApprove(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "consumer_id", "consumer_mobile", "stores_id", "stores_name" });
		// 是否存在申请中的记录
		paramsMap.put("status", "applied");
		List<Map<String, Object>> list = saleAssistantDao.selectMDSaleAssistant(paramsMap);
		if (list.size() == 0) {
			throw new BusinessException(RspCode.SALE_ASSISTANT_ERROR, "申请记录不存在");
		}
		paramsMap.put("status", "approved");
		saleAssistantDao.updateMDSaleAssistant(paramsMap);
		// 记录日志
		Date date = new Date();
		MDSaleAssistantHistory consumerHistory = new MDSaleAssistantHistory();
		consumerHistory.setMember_id(paramsMap.get("consumer_id") + "");
		consumerHistory.setMember_type_key(Constant.MEMBER_TYPE_CONSUMER);
		consumerHistory.setHistory_id(IDUtil.getUUID());
		consumerHistory.setEvent_desc("【" + paramsMap.get("stores_name") + "】已同意您的社区导购申请");
		consumerHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(consumerHistory);

		// 记录日志
		MDSaleAssistantHistory storesHistory = new MDSaleAssistantHistory();
		storesHistory.setMember_id(paramsMap.get("stores_id") + "");
		storesHistory.setMember_type_key(Constant.MEMBER_TYPE_STORES);
		storesHistory.setHistory_id(IDUtil.getUUID());
		storesHistory.setEvent_desc("您已同意【" + paramsMap.get("consumer_mobile") + "】成为您的社区导购");
		storesHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(storesHistory);
	}

	@Override
	public void handleSaleAssistantReject(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "consumer_id", "consumer_mobile", "stores_id", "stores_name" });
		// 是否存在申请中的记录
		paramsMap.put("status", "applied");
		List<Map<String, Object>> list = saleAssistantDao.selectMDSaleAssistant(paramsMap);
		if (list.size() == 0) {
			throw new BusinessException(RspCode.SALE_ASSISTANT_ERROR, "申请记录不存在");
		}
		saleAssistantDao.deleteMDSaleAssistant(paramsMap);

		// 记录日志
		Date date = new Date();
		MDSaleAssistantHistory consumerHistory = new MDSaleAssistantHistory();
		consumerHistory.setMember_id(paramsMap.get("consumer_id") + "");
		consumerHistory.setMember_type_key(Constant.MEMBER_TYPE_CONSUMER);
		consumerHistory.setHistory_id(IDUtil.getUUID());
		consumerHistory.setEvent_desc("【" + paramsMap.get("stores_name") + "】已拒绝您的社区导购申请");
		consumerHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(consumerHistory);

		// 记录日志
		MDSaleAssistantHistory storesHistory = new MDSaleAssistantHistory();
		storesHistory.setMember_id(paramsMap.get("stores_id") + "");
		storesHistory.setMember_type_key(Constant.MEMBER_TYPE_STORES);
		storesHistory.setHistory_id(IDUtil.getUUID());
		storesHistory.setEvent_desc("您已拒绝【" + paramsMap.get("consumer_mobile") + "】成为您的社区导购");
		storesHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(storesHistory);
	}

	@Override
	public void saleAssistantHistoryFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
		List<Map<String, Object>> qryList = saleAssistantHistoryDao.selectMDSaleAssistantHistoryList(paramsMap);
		for (Map<String, Object> m : qryList) {
			m.put("tracked_date", DateUtil.date2Str((Date) m.get("tracked_date"), DateUtil.fmt_yyyyMMddHHmmss));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", qryList);
		result.setResultData(map);
	}

	@Override
	public void saleAssistantForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id" });
		paramsMap.put("status", "approved");
		List<Map<String, Object>> qryList = saleAssistantDao.selectMDSaleAssistantForConsumerList(paramsMap);
		for (Map<String, Object> m : qryList) {
			m.put("address", StringUtil.formatStr(m.get("address")));
			//contact_tel 后期废除
			m.put("contact_tel", StringUtil.formatStr(m.get("service_tel")));
			
			m.put("service_tel", StringUtil.formatStr(m.get("service_tel")));
			m.put("stores_name", StringUtil.formatStr(m.get("stores_name")));
			m.put("created_date", DateUtil.date2Str((Date) m.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", qryList);
		result.setResultData(map);
	}

	@Override
	public void saleAssistantAppliedForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		paramsMap.put("status_in", new String[] { "applied", "approved"});
		List<Map<String, Object>> qryList = saleAssistantDao.selectMDSaleAssistantForStoresList(paramsMap);
		for (Map<String, Object> m : qryList) {
			m.put("address", StringUtil.formatStr(m.get("address")));
			m.put("mobile", StringUtil.formatStr(m.get("mobile")));
			m.put("nick_name", StringUtil.formatStr(m.get("nick_name")));
			m.put("status", StringUtil.formatStr(m.get("status")));
			m.put("created_date", DateUtil.date2Str((Date) m.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", qryList);
		result.setResultData(map);
	}

	@Override
	public void saleAssistantApprovedForStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		paramsMap.put("status", "approved");
		List<Map<String, Object>> qryList = saleAssistantDao.selectMDSaleAssistantForStoresList(paramsMap);
		for (Map<String, Object> m : qryList) {
			m.put("address", StringUtil.formatStr(m.get("address")));
			m.put("mobile", StringUtil.formatStr(m.get("mobile")));
			m.put("nick_name", StringUtil.formatStr(m.get("nick_name")));
			m.put("created_date", DateUtil.date2Str((Date) m.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", qryList);
		result.setResultData(map);
	}

	@Override
	public void handleSaleAssistantForConsumerCancel(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "consumer_id", "consumer_mobile", "stores_id", "stores_name" });
		// 是否存在同意记录
		paramsMap.put("status", "approved");
		List<Map<String, Object>> list = saleAssistantDao.selectMDSaleAssistant(paramsMap);
		if (list.size() == 0) {
			throw new BusinessException(RspCode.SALE_ASSISTANT_ERROR, "记录不存在");
		}
		saleAssistantDao.deleteMDSaleAssistant(paramsMap);
		Date date = new Date();
		// 记录日志
		MDSaleAssistantHistory consumerHistory = new MDSaleAssistantHistory();
		consumerHistory.setMember_id(paramsMap.get("consumer_id") + "");
		consumerHistory.setMember_type_key(Constant.MEMBER_TYPE_CONSUMER);
		consumerHistory.setHistory_id(IDUtil.getUUID());
		consumerHistory.setEvent_desc("您已取消【" + paramsMap.get("stores_name") + "】的社区导购资格");
		consumerHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(consumerHistory);

		// 记录日志
		MDSaleAssistantHistory storesHistory = new MDSaleAssistantHistory();
		storesHistory.setMember_id(paramsMap.get("stores_id") + "");
		storesHistory.setMember_type_key(Constant.MEMBER_TYPE_STORES);
		storesHistory.setHistory_id(IDUtil.getUUID());
		storesHistory.setEvent_desc(paramsMap.get("consumer_mobile") + "取消了社区导购资格");
		storesHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(storesHistory);
	}

	@Override
	public void handleSaleAssistantForStoresCancel(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "consumer_id", "consumer_mobile", "stores_id", "stores_name" });
		// 是否存在同意记录
		paramsMap.put("status", "approved");
		List<Map<String, Object>> list = saleAssistantDao.selectMDSaleAssistant(paramsMap);
		if (list.size() == 0) {
			throw new BusinessException(RspCode.SALE_ASSISTANT_ERROR, "记录不存在");
		}
		saleAssistantDao.deleteMDSaleAssistant(paramsMap);
		Date date = new Date();
		// 记录日志
		MDSaleAssistantHistory consumerHistory = new MDSaleAssistantHistory();
		consumerHistory.setMember_id(paramsMap.get("consumer_id") + "");
		consumerHistory.setMember_type_key(Constant.MEMBER_TYPE_CONSUMER);
		consumerHistory.setHistory_id(IDUtil.getUUID());
		consumerHistory.setEvent_desc("【" + paramsMap.get("stores_name") + "】取消了您的社区导购资格");
		consumerHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(consumerHistory);

		// 记录日志
		MDSaleAssistantHistory storesHistory = new MDSaleAssistantHistory();
		storesHistory.setMember_id(paramsMap.get("stores_id") + "");
		storesHistory.setMember_type_key(Constant.MEMBER_TYPE_STORES);
		storesHistory.setHistory_id(IDUtil.getUUID());
		storesHistory.setEvent_desc("您已取消【" + paramsMap.get("consumer_mobile") + "】的社区导购资格");
		storesHistory.setTracked_date(date);
		saleAssistantHistoryDao.insertMDSaleAssistantHistory(storesHistory);
	}

}
