package com.meitianhui.infrastructure.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.EncryptUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.infrastructure.constant.RspCode;
import com.meitianhui.infrastructure.dao.InfrastructureDao;
import com.meitianhui.infrastructure.dao.UserDao;
import com.meitianhui.infrastructure.service.InfrastructureService;

/**
 * 基础服务的服务层
 * 
 * @author mole
 *
 */
@Service
public class InfrastructureServiceImpl implements InfrastructureService {

	@Autowired
	public InfrastructureDao infrastructureDao;

	@Autowired
	public UserDao userDao;

	@Autowired
	public RedisUtil redisUtil;

	@Override
	public void handleTransactionRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			String flow_no = IDUtil.random(12);
			String security_code = IDUtil.getShortUUID();
			redisUtil.setStr(flow_no, security_code, 120);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("flow_no", flow_no);
			resultMap.put("security_code", security_code);
			result.setResultData(resultMap);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleTransactionVerify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "flow_no", "security_code" });
			String redis_security_code = redisUtil.getStr((String) paramsMap.get("flow_no"));
			if (redis_security_code == null || !redis_security_code.equals((String) paramsMap.get("security_code"))) {
				throw new BusinessException(RspCode.SECURITY_CODE_ERROR, "二维码失效或不存在");
			}
			redisUtil.del((String) paramsMap.get("flow_no"));
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleMobileLoginRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "mobile" });
			String security_code = IDUtil.getShortUUID() + IDUtil.random(16);
			redisUtil.setStr(security_code, paramsMap.get("mobile") + "", 60);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("code", security_code);
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
	public void handleMobileLoginVerify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "code" });
			String code = (String) paramsMap.get("code");
			String mobile = StringUtil.formatStr(redisUtil.getStr(code));
			if (mobile.equals("")) {
				throw new BusinessException(RspCode.SECURITY_CODE_ERROR, RspCode.MSG.get(RspCode.SECURITY_CODE_ERROR));
			}
			redisUtil.del(code);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("mobile", mobile);
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
	public void handleShumeLoginRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "mobile", "device_id" });
			String mobile = (String) paramsMap.get("mobile");
			String device_id = (String) paramsMap.get("device_id");
			String security_code = IDUtil.getShortUUID() + IDUtil.random(16);
			redisUtil.setStr(security_code, mobile + "_" + device_id, 60);
			Map<String, Object> resultDataMap = new HashMap<String, Object>();
			resultDataMap.put("code", security_code);
			result.setResultData(resultDataMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleShumeLoginVerify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "code", "mobile", "device_id" });
			String request_code = (String) paramsMap.get("code");
			String security_info = StringUtil.formatStr(redisUtil.getStr(request_code));
			if (security_info.equals("")) {
				throw new BusinessException(RspCode.SECURITY_CODE_ERROR, RspCode.MSG.get(RspCode.SECURITY_CODE_ERROR));
			}
			if (!security_info.equals(paramsMap.get("mobile") + "_" + paramsMap.get("device_id"))) {
				throw new BusinessException(RspCode.SECURITY_CODE_ERROR, RspCode.MSG.get(RspCode.SECURITY_CODE_ERROR));
			}
			redisUtil.del(request_code);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void userFeedbackCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "data_source", "category", "desc1", "contact" });
			paramsMap.put("feedback_id", IDUtil.getUUID());
			paramsMap.put("created_date", new Date());
			infrastructureDao.insertIdUserFeedback(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void userFeedbackEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "feedback_id" });
			if (paramsMap.size() == 1) {
				throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE,
						RspCode.MSG.get(RspCode.SYSTEM_NO_PARAMS_UPDATE));
			}
			infrastructureDao.updateUserFeedback(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void userFeedbackFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> list = infrastructureDao.selectUserFeedback(paramsMap);
			for (Map<String, Object> map : list) {
				String attachment = StringUtil.formatStr(map.get("attachment"));
				map.put("attachment", attachment);
				map.put("created_date", DateUtil.date2Str((Date) map.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				map.put("remark", StringUtil.formatStr(map.get("remark")));
			}
			result.setResultData(list);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void safetyCodeVerify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = null;
		try {
			//1.调用加密算法加密明文，把生成的密文放到readis里面去，保存时间设为86400S=24H
			//根据当前的日期生成时间戳(2017-09-08)
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date date = simpleDateFormat.parse(DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMdd));
	        String timeStamp = String.valueOf(date.getTime());
	        
			lockKey = "[safetyCodeVerify]_meitianhui"+timeStamp;
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();

			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				result.setResultData(redisUtil.getStr(lockKey));
			}
			String ciphertext= EncryptUtil.SHA512(lockKey);
			redisUtil.setStr(lockKey, ciphertext, 86400);
			result.setResultData(redisUtil.getStr(lockKey));
		} catch (Exception e) {
			throw e;
		}finally {
			if (lock != null) {
				lock.unlock();
			}
		}
	}

}
