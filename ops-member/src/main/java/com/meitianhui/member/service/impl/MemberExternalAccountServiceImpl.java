package com.meitianhui.member.service.impl;

import java.util.ArrayList;
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
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.dao.MemberExternalAccountDao;
import com.meitianhui.member.dao.MemberExternalAccountHistoryDao;
import com.meitianhui.member.entity.MDMemberExternalAccount;
import com.meitianhui.member.entity.MDMemberExternalAccountHistory;
import com.meitianhui.member.service.MemberExternalAccountService;

/**
 * 账号绑定信息
 * 
 * @author Tiny
 *
 */
@Service
public class MemberExternalAccountServiceImpl implements MemberExternalAccountService {

	@Autowired
	public MemberExternalAccountDao memberExternalAccountDao;
	@Autowired
	public MemberExternalAccountHistoryDao memberExternalAccountHistoryDao;

	@Autowired
	public RedisUtil redisUtil;

	@Override
	public void memberExternalAccountCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "user_id", "mobile", "member_type_key", "member_id", "account_type_key", "account_no" });
		String member_id = paramsMap.get("member_id") + "";
		String account_no = paramsMap.get("account_no") + "";
		String account_type_key = paramsMap.get("account_type_key") + "";
		String member_type_key = paramsMap.get("member_type_key") + "";
		// 验证是否可以绑定
		bindValiDate(account_no, account_type_key, member_type_key, member_id, "create");
		// 创建绑定信息
		MDMemberExternalAccount account = new MDMemberExternalAccount();
		BeanConvertUtil.mapToBean(account, paramsMap);
		account.setAccount_id(IDUtil.getUUID());
		Date date = new Date();
		account.setCreated_date(date);
		account.setModified_date(date);
		memberExternalAccountDao.insertMemberExternalAccount(account);
		// 创建历史
		MDMemberExternalAccountHistory accountHistory = new MDMemberExternalAccountHistory();
		BeanConvertUtil.mapToBean(accountHistory, paramsMap);
		accountHistory.setHistory_id(IDUtil.getUUID());
		accountHistory.setAccount_id(account.getAccount_id());
		accountHistory.setCreated_date(date);
		accountHistory.setModified_date(date);
		Map<String, String> remarkMap = new HashMap<String, String>();
		remarkMap.put("operator", paramsMap.get("mobile") + "");
		remarkMap.put("desc", "新增");
		accountHistory.setRemark(FastJsonUtil.toJson(remarkMap));
		memberExternalAccountHistoryDao.insertMemberExternalAccountHistory(accountHistory);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("account_id", account.getAccount_id());
		resultMap.put("account_no", account.getAccount_no());
		resultMap.put("account_type_key", account.getAccount_type_key());
		result.setResultData(resultMap);
	}

	@Override
	public void memberExternalAccountListForAppFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> qryList = memberExternalAccountDao.selectMemberExternalAccountList(paramsMap);
		for (Map<String, Object> map : qryList) {
			Map<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("account_id", StringUtil.formatStr(map.get("account_id")));
			tempMap.put("account_no", StringUtil.formatStr(map.get("account_no")));
			tempMap.put("account_type_key", StringUtil.formatStr(map.get("account_type_key")));
			resultList.add(tempMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		result.setResultData(resultMap);
	}

	@Override
	public void memberExternalAccountListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> qryList = memberExternalAccountDao.selectMemberExternalAccountList(paramsMap);
		for (Map<String, Object> map : qryList) {
			Map<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("account_id", StringUtil.formatStr(map.get("account_id")));
			tempMap.put("user_id", StringUtil.formatStr(map.get("user_id")));
			tempMap.put("account_no", StringUtil.formatStr(map.get("account_no")));
			tempMap.put("account_type_key", StringUtil.formatStr(map.get("account_type_key")));
			tempMap.put("mobile", StringUtil.formatStr(map.get("mobile")));
			tempMap.put("member_type_key", StringUtil.formatStr(map.get("member_type_key")));
			tempMap.put("member_id", StringUtil.formatStr(map.get("member_id")));
			resultList.add(tempMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		result.setResultData(resultMap);
	}

	@Override
	public void memberExternalAccountEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "account_id", "account_type_key", "account_no", "user_id",
				"mobile", "member_id", "member_type_key" });
		String account_id = paramsMap.get("account_id") + "";
		String member_id = paramsMap.get("member_id") + "";
		String account_no = paramsMap.get("account_no") + "";
		String account_type_key = paramsMap.get("account_type_key") + "";
		String member_type_key = paramsMap.get("member_type_key") + "";
		// 验证是否可以绑定
		bindValiDate(account_no, account_type_key, member_type_key, member_id, "edit");
		// 更新账号信息
		memberExternalAccountDao.updateMemberExternalAccount(paramsMap);

		Date date = new Date();
		// 创建历史
		MDMemberExternalAccountHistory accountHistory = new MDMemberExternalAccountHistory();
		BeanConvertUtil.mapToBean(accountHistory, paramsMap);
		accountHistory.setHistory_id(IDUtil.getUUID());
		accountHistory.setAccount_id(account_id);
		accountHistory.setCreated_date(date);
		accountHistory.setModified_date(date);
		Map<String, String> remarkMap = new HashMap<String, String>();
		remarkMap.put("operator", paramsMap.get("mobile") + "");
		remarkMap.put("desc", "修改");
		accountHistory.setRemark(FastJsonUtil.toJson(remarkMap));
		memberExternalAccountHistoryDao.insertMemberExternalAccountHistory(accountHistory);
	}

	@Override
	public void memberExternalAccountForOpEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "account_id", "account_type_key", "account_no",
				"member_id", "member_type_key", "user_id", "mobile", "operator" });
		String account_id = paramsMap.get("account_id") + "";
		String member_id = paramsMap.get("member_id") + "";
		String member_type_key = paramsMap.get("member_type_key") + "";
		String account_no = paramsMap.get("account_no") + "";
		String account_type_key = paramsMap.get("account_type_key") + "";
		// 验证是否可以绑定
		bindValiDate(account_no, account_type_key, member_type_key, member_id, "edit");
		// 更新账号信息
		memberExternalAccountDao.updateMemberExternalAccount(paramsMap);

		Date date = new Date();
		// 创建历史
		MDMemberExternalAccountHistory accountHistory = new MDMemberExternalAccountHistory();
		BeanConvertUtil.mapToBean(accountHistory, paramsMap);
		accountHistory.setHistory_id(IDUtil.getUUID());
		accountHistory.setAccount_id(account_id);
		accountHistory.setCreated_date(date);
		accountHistory.setModified_date(date);
		Map<String, String> remarkMap = new HashMap<String, String>();
		remarkMap.put("operator", paramsMap.get("operator") + "");
		remarkMap.put("desc", "修改");
		accountHistory.setRemark(FastJsonUtil.toJson(remarkMap));
		memberExternalAccountHistoryDao.insertMemberExternalAccountHistory(accountHistory);
	}

	@Override
	public void memberExternalAccountDelete(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "account_id", "operator" });
		MDMemberExternalAccount account = memberExternalAccountDao.selectMemberExternalAccount(paramsMap);
		if (null == account) {
			throw new BusinessException(RspCode.MEMBER_EXTERNAL_ACCOUNT_ERROR, "绑定账号信息不存在");
		}
		// 更新账号信息
		memberExternalAccountDao.deleteMemberExternalAccount(paramsMap);

		// 创建历史记录
		Date date = new Date();
		MDMemberExternalAccountHistory accountHistory = new MDMemberExternalAccountHistory();
		accountHistory.setHistory_id(IDUtil.getUUID());
		accountHistory.setAccount_id(account.getAccount_id());
		accountHistory.setAccount_no(account.getAccount_no());
		accountHistory.setAccount_type_key(account.getAccount_type_key());
		accountHistory.setMember_id(account.getMember_id());
		accountHistory.setMember_type_key(account.getAccount_type_key());
		accountHistory.setMobile(account.getMobile());
		accountHistory.setUser_id(account.getUser_id());
		accountHistory.setCreated_date(date);
		accountHistory.setModified_date(date);
		Map<String, String> remarkMap = new HashMap<String, String>();
		remarkMap.put("operator", paramsMap.get("operator") + "");
		remarkMap.put("desc", "删除");
		accountHistory.setRemark(FastJsonUtil.toJson(remarkMap));
		memberExternalAccountHistoryDao.insertMemberExternalAccountHistory(accountHistory);
	}

	/**
	 * 绑定账号验证
	 * 
	 * @Title: bindValiDate
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	private void bindValiDate(String account_no, String account_type_key, String member_type_key, String member_id,
			String add_or_update) throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = null;
		try {
			lockKey = "[bindValiDate]_" + account_type_key + account_no;
			lock = new RedisLock(redisUtil, lockKey, 30 * 1000);
			lock.lock();
			// 检测账号是否已经绑定了
			Map<String, Object> tempMap = new HashMap<String, Object>();
			if (add_or_update.equals("create")) {
				tempMap.put("member_id", member_id);
				tempMap.put("member_type_key", member_type_key);
				List<Map<String, Object>> qryList = memberExternalAccountDao.selectMemberExternalAccountList(tempMap);
				if (qryList.size() > 0) {
					throw new BusinessException(RspCode.MEMBER_EXTERNAL_ACCOUNT_ERROR, "会员已经绑定,请勿重复绑定");
				}
			}
			// 查询账号是否已经被其他会员绑定了
			tempMap.clear();
			tempMap.put("account_no", account_no);
			tempMap.put("account_type_key", account_type_key);
			tempMap.put("member_type_key", member_type_key);
			List<Map<String, Object>> qryList = memberExternalAccountDao.selectMemberExternalAccountList(tempMap);
			if (qryList.size() > 0) {
				if (add_or_update.equals("create")) {
					// 如果是新增,只要有记录，就标示账号已经被绑定了
					throw new BusinessException(RspCode.MEMBER_EXTERNAL_ACCOUNT_ERROR, "账号已经被其他会员绑定");
				} else {
					// 如果是修改，判断当前账号绑定的会员不是当前会员
					tempMap.clear();
					tempMap = qryList.get(0);
					String temp_member_id = tempMap.get("member_id") + "";
					if (!temp_member_id.equals(member_id)) {
						throw new BusinessException(RspCode.MEMBER_EXTERNAL_ACCOUNT_ERROR, "账号已经被其他会员绑定");
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (lock != null) {
				lock.unlock();
			}
			if (StringUtil.isNotEmpty(lockKey)) {
				redisUtil.del(lockKey);
			}
		}
	}

}
