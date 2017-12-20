package com.meitianhui.member.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.meitianhui.common.constant.CommonConstant;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.member.constant.Constant;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.dao.AssistantApplicationDao;
import com.meitianhui.member.dao.MemberDao;
import com.meitianhui.member.dao.SalesmanAuthHistoryDao;
import com.meitianhui.member.dao.SalesmanDao;
import com.meitianhui.member.dao.SalesmanDriverDao;
import com.meitianhui.member.dao.SalesmanSpecialistDao;
import com.meitianhui.member.dao.StoresDao;
import com.meitianhui.member.dao.SystemInformDao;
import com.meitianhui.member.entity.MDAssistantApplication;
import com.meitianhui.member.entity.MDSalesman;
import com.meitianhui.member.entity.MDSalesmanAuthHistory;
import com.meitianhui.member.entity.MDSalesmanDriver;
import com.meitianhui.member.entity.MDSalesmanSpecialist;
import com.meitianhui.member.entity.MDStores;
import com.meitianhui.member.entity.MDSystemInform;
import com.meitianhui.member.entity.MDUserMember;
import com.meitianhui.member.service.SalesmanService;

@SuppressWarnings("unchecked")
@Service
public class SalesmanServiceImpl implements SalesmanService {

	@Autowired
	public RedisUtil redisUtil;

	@Autowired
	public SalesmanDao salesmanDao;
	
	@Autowired
	public SalesmanAuthHistoryDao salesmanAuthHistoryDao;
	
	@Autowired
	public SalesmanDriverDao salesmanDriverDao;
	
	@Autowired
	public SalesmanSpecialistDao salesmanSpecialistDao;
	
	@Autowired
	public AssistantApplicationDao assistantApplicationDao;
	
	@Autowired
	public MemberDao memberDao;
	
	@Autowired
	public SystemInformDao systemInformDao;
	
	@Autowired
	public StoresDao StoresDao;
	

	@Override
	public void salesmanCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = null;
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "name", "contact_tel", "sex_key", "area_id" });

			String mobile = paramsMap.get("contact_tel") + "";
			String salesman_name = paramsMap.get("name") + "";

			// 0.添加业务员加锁
			lockKey = "[salesmanCreate]_" + mobile;
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();

			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "您的操作过于频繁,请稍后重试");
			}
			redisUtil.setStr(lockKey, mobile, 180);

			// 1.验证手机号码是否存在(查询md_salesman表)
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("contact_tel", mobile);
			Map<String, Object> tempSalesman = salesmanDao.selectSalesman(tempMap);
			if (null != tempSalesman) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员信息已存在");
			}

			// 2.调用用户同步接口
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> bizParams = new HashMap<String, String>();
			reqParams.put("service", "infrastructure.syncUserRegister");
			bizParams.put("mobile", mobile);
			bizParams.put("user_name", salesman_name);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			String user_id = (String) dateMap.get("user_id");

			// 3. 把业务员信息到业务员表(md_salesman表)
			MDSalesman mdSalesman = new MDSalesman();
			BeanConvertUtil.mapToBean(mdSalesman, paramsMap);
			mdSalesman.setAuth_status(Constant.AUTH_STATUS_NON_AUTH);
			mdSalesman.setStatus(Constant.STATUS_NORMAL);
			mdSalesman.setSalesman_id(IDUtil.getUUID());
			mdSalesman.setIs_driver("N");
			mdSalesman.setIs_specialist("N");
			mdSalesman.setIs_guider("N");
			mdSalesman.setIs_buyer("N");
			mdSalesman.setIs_operator("N");
			Date now = new Date();
			mdSalesman.setRegistered_date(now);
			mdSalesman.setCreated_date(now);
			mdSalesman.setModified_date(now);
			salesmanDao.insertSalesman(mdSalesman);
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("salesman_id", mdSalesman.getSalesman_id());
			logMap.put("category", "新增");
			logMap.put("tracked_date", now);
			logMap.put("event_desc", paramsMap.get("event_desc") + "");
			salesmanDao.insertSalesmanLog(logMap);

			// 4. 把用户（id)和业务员(id)添加到账号会员关系表
			// (md_user_member表)(先检查是否存在，如果关系存在，先删除再添加)
			MDUserMember mdUserMember = new MDUserMember();
			mdUserMember.setUser_id(user_id);
			mdUserMember.setMember_id(mdSalesman.getSalesman_id());
			mdUserMember.setMember_type_key(CommonConstant.MEMBER_TYPE_SALESMAN);
			mdUserMember.setIs_admin("Y");
			tempMap.clear();
			tempMap.put("user_id", user_id);
			tempMap.put("member_type_key", CommonConstant.MEMBER_TYPE_SALESMAN);
			Map<String, Object> tempUserMember = salesmanDao.selectUserMember(tempMap);
			if (null != tempUserMember) {
				salesmanDao.deleteUserMember(tempMap);
			}
			salesmanDao.insertUserMember(mdUserMember);

			// 5.调用 资产接口
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			reqParams.put("service", "finance.memberAssetInit");
			bizParams.put("member_id", mdSalesman.getSalesman_id());
			bizParams.put("member_type_key", CommonConstant.MEMBER_TYPE_SALESMAN);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.post(finance_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
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

	@Override
	public void salesmanLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" });
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> mDSalesmanLogList = salesmanDao.selectSalesmanLogList(paramsMap);
		for (Map<String, Object> salesmanLogMap : mDSalesmanLogList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("log_id", StringUtil.formatStr(salesmanLogMap.get("log_id")));
			tempMap.put("salesman_id", StringUtil.formatStr(salesmanLogMap.get("salesman_id")));
			tempMap.put("category", StringUtil.formatStr(salesmanLogMap.get("category")));
			tempMap.put("tracked_date", 
					DateUtil.date2Str((Date) salesmanLogMap.get("tracked_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("event_desc", StringUtil.formatStr(salesmanLogMap.get("event_desc")));
			tempMap.put("name", StringUtil.formatStr(salesmanLogMap.get("name")));
			tempMap.put("nick_name", StringUtil.formatStr(salesmanLogMap.get("nick_name")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}
	
	@Override
	public void salesmanListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> mDSalesmanList = salesmanDao.selectSalesmanList(paramsMap);
		for (Map<String, Object> salesmanMap : mDSalesmanList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("salesman_id", StringUtil.formatStr(salesmanMap.get("salesman_id")));
			tempMap.put("name", StringUtil.formatStr(salesmanMap.get("name")));
			tempMap.put("nick_name", StringUtil.formatStr(salesmanMap.get("nick_name")));
			tempMap.put("contact_tel", StringUtil.formatStr(salesmanMap.get("contact_tel")));
			tempMap.put("auth_status", StringUtil.formatStr(salesmanMap.get("auth_status")));
			tempMap.put("path", StringUtil.formatStr(salesmanMap.get("path")));
			tempMap.put("is_driver", StringUtil.formatStr(salesmanMap.get("is_driver")));
			tempMap.put("is_specialist", StringUtil.formatStr(salesmanMap.get("is_specialist")));
			tempMap.put("is_guider", StringUtil.formatStr(salesmanMap.get("is_guider")));
			tempMap.put("is_buyer", StringUtil.formatStr(salesmanMap.get("is_buyer")));
			tempMap.put("is_operator", StringUtil.formatStr(salesmanMap.get("is_operator")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void salesmanEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" });
		ValidateUtil.validateParamsNum(paramsMap, new String[] { "nick_name", "sex_key", "area_id" }, 1);
		salesmanDao.updateSalesman(paramsMap);
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", paramsMap.get("salesman_id"));
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", paramsMap.get("event_desc") + "");
		salesmanDao.insertSalesmanLog(logMap);
	}
	
	@Override
	public void salesmanForSalesassistantEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" , "head_pic_path"});
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id")+"");
		tempMap.put("head_pic_path", paramsMap.get("head_pic_path"));
		salesmanDao.updateSalesman(tempMap);
		tempMap.clear();
		tempMap.put("log_id", IDUtil.getUUID());
		tempMap.put("salesman_id", paramsMap.get("salesman_id"));
		tempMap.put("category", "修改");
		tempMap.put("tracked_date", new Date());
		tempMap.put("event_desc", "修改头像");
		salesmanDao.insertSalesmanLog(tempMap);	
	}

	
	
	@Override
	public void handleSalesmanRoleDelete(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id","remark"});
		ValidateUtil.validateParamsNum(paramsMap, new String[] { "is_driver", "is_specialist", "is_buyer" , "is_guider", "is_operator" }, 1);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id"));
		String role = "";
		if(null != paramsMap.get("is_driver") ){
			tempMap.put("is_driver", "N");
			role = "司机";
			
			Map<String, Object> tempHashMap = new HashMap<String, Object>();
			tempHashMap.put("salesman_id", paramsMap.get("salesman_id"));
			tempHashMap.put("audit_status", Constant.AUDIT_STATUS_DELETED);
			int updateFlagSalesmanDriver = salesmanDriverDao.updateSalesmanDriver(tempHashMap);
			if (updateFlagSalesmanDriver == 0) {
				throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
			}
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", paramsMap.get("username")+"");
			params.put("team", paramsMap.get("team")+"");
			params.put("remark", paramsMap.get("remark")+"");		
			String logRemark = FastJsonUtil.toJson(params);
			
			//添加业务员司机审批日志信息
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("salesman_id", paramsMap.get("salesman_id"));
			logMap.put("category", "修改");
			logMap.put("tracked_date", new Date());
			logMap.put("event_desc", logRemark);
			salesmanDriverDao.insertSalesmanDriverLog(logMap);
		}
		if(null != paramsMap.get("is_specialist")){
			tempMap.put("is_specialist", "N");
			role = role==""?"地服":role+",地服";	
			
			Map<String, Object> tempHashMap = new HashMap<String, Object>();
			tempHashMap.put("salesman_id", paramsMap.get("salesman_id"));
			tempHashMap.put("audit_status", Constant.AUDIT_STATUS_DELETED);
			int updateFlagSalesmanSpecialist = salesmanSpecialistDao.updateSalesmanSpecialist(tempHashMap);
			if (updateFlagSalesmanSpecialist == 0) {
				throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
			}
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", paramsMap.get("username")+"");
			params.put("team", paramsMap.get("team")+"");
			params.put("remark", paramsMap.get("remark")+"");		
			String logRemark = FastJsonUtil.toJson(params);
			
			//添加业务员司机审批日志信息
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("salesman_id", paramsMap.get("salesman_id"));
			logMap.put("category", "修改");
			logMap.put("tracked_date", new Date());
			logMap.put("event_desc", logRemark);
			salesmanSpecialistDao.insertSalesmanSpecialistLog(logMap);
		}
		if(null != paramsMap.get("is_buyer")){
			tempMap.put("is_buyer", "N");
			role = role==""?"买手":role+",买手";	
		}
		if(null != paramsMap.get("is_guider")){
			tempMap.put("is_guider", "N");
			role = role==""?"导购":role+",导购";	
		}
		if(null != paramsMap.get("is_operator")){
			tempMap.put("is_operator", "N");
			role = role==""?"运营":role+",运营";	
		}
		
		salesmanDao.updateSalesman(tempMap);
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", paramsMap.get("salesman_id"));
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", "删除角色:"+role);
		salesmanDao.insertSalesmanLog(logMap);
		
		MDSystemInform mdSystemInform =new MDSystemInform();
		mdSystemInform.setInfo_id(IDUtil.getUUID());
		mdSystemInform.setInfo_comment("您的"+role+"角色已被清除。原因："+paramsMap.get("remark")+"");
		mdSystemInform.setInfo_result("fail");
		mdSystemInform.setInfo_type("XXLX_02");
		mdSystemInform.setCre_by(StringUtil.formatStr(paramsMap.get("salesman_id")));
		mdSystemInform.setCre_date(new Date());
		systemInformDao.insertSystemInform(mdSystemInform);
		
	}

	@Override
	public void salesmanDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> salesman = salesmanDao.selectSalesman(paramsMap);
		if (null != salesman) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("registered_date",
					DateUtil.date2Str((Date) salesman.get("registered_date"), 
							DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("name", StringUtil.formatStr(salesman.get("name")));
			tempMap.put("nick_name", StringUtil.formatStr(salesman.get("nick_name")));
			tempMap.put("desc1", StringUtil.formatStr(salesman.get("desc1")));
			tempMap.put("id_card", StringUtil.formatStr(salesman.get("id_card")));
			tempMap.put("id_card_pic_path", StringUtil.formatStr(salesman.get("id_card_pic_path")));
			tempMap.put("head_pic_path", StringUtil.formatStr(salesman.get("head_pic_path")));
			tempMap.put("sex_key", StringUtil.formatStr(salesman.get("sex_key")));
			tempMap.put("area_id", StringUtil.formatStr(salesman.get("area_id")));
			tempMap.put("path", StringUtil.formatStr(salesman.get("path")));
			tempMap.put("address", StringUtil.formatStr(salesman.get("address")));
			tempMap.put("contact_tel", StringUtil.formatStr(salesman.get("contact_tel")));
			tempMap.put("salesman_id", StringUtil.formatStr(salesman.get("salesman_id")));
			tempMap.put("auth_status", StringUtil.formatStr(salesman.get("auth_status")));
			tempMap.put("is_driver", StringUtil.formatStr(salesman.get("is_driver")));
			tempMap.put("is_specialist", StringUtil.formatStr(salesman.get("is_specialist")));
			tempMap.put("is_guider", StringUtil.formatStr(salesman.get("is_guider")));
			tempMap.put("is_buyer", StringUtil.formatStr(salesman.get("is_buyer")));
			tempMap.put("is_operator", StringUtil.formatStr(salesman.get("is_operator")));
			tempMap.put("remark", StringUtil.formatStr(salesman.get("remark")));
			result.setResultData(tempMap);
		}
	}
	
	@Override
	public void salesmanDetailForSalesassistantFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> salesman = salesmanDao.selectSalesman(paramsMap);
		if (null != salesman) {	
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("salesman_id", StringUtil.formatStr(salesman.get("salesman_id")));
			tempMap.put("auth_status", StringUtil.formatStr(salesman.get("auth_status")));
			tempMap.put("name", StringUtil.formatStr(salesman.get("name")));
			tempMap.put("nick_name", StringUtil.formatStr(salesman.get("nick_name")));
			tempMap.put("sex_key", StringUtil.formatStr(salesman.get("sex_key")));
			tempMap.put("contact_tel", StringUtil.formatStr(salesman.get("contact_tel")));
			tempMap.put("head_pic_path", StringUtil.formatStr(salesman.get("head_pic_path")));
			//运营
			tempMap.put("is_operator", StringUtil.formatStr(salesman.get("is_operator")));
			//司机
			Map<String, Object> mDSalesmanDriver = salesmanDriverDao.selectSalesmanDriver(tempMap);
			if(null == mDSalesmanDriver ){
				tempMap.put("is_driver", "unapproved");//未审批
			}else{
				tempMap.put("is_driver", StringUtil.formatStr(
						mDSalesmanDriver.get("audit_status")));//non-audit //audit //reject //deleted
			}
			//地服
			Map<String, Object> mDSalesmanSpecialist = salesmanSpecialistDao.selectSalesmanSpecialist(tempMap);
			if(null == mDSalesmanSpecialist ){
				tempMap.put("is_specialist", "unapproved");//未审批
			}else{
				tempMap.put("is_specialist", StringUtil.formatStr(
						mDSalesmanSpecialist.get("audit_status")));//non-audit //audit //reject //deleted
			}
			//买手
			tempMap.put("is_buyer", "unapproved");
			
			//导购
			tempMap.put("is_guider", "unapproved");
			result.setResultData(tempMap);
		}
	}

	@Override
	public void salesmanLoginValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "user_id" });
			paramsMap.put("member_type_key", Constant.MEMBER_TYPE_SALESMAN);
			List<MDUserMember> mDUserMemberList = memberDao.selectMDUserMember(paramsMap);
			if (mDUserMemberList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDUserMember mDUserMember = mDUserMemberList.get(0);
			// 验证会员状态
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("salesman_id", mDUserMember.getMember_id());
			Map<String, Object> salesman = salesmanDao.selectSalesman(tempMap);
			if (salesman == null) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			
			String status = StringUtil.formatStr(salesman.get("status"));
			if (!status.equals(Constant.STATUS_NORMAL)) {
				throw new BusinessException(RspCode.MEMBER_STATUS_ERROR, "会员被禁用");
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("member_id", mDUserMember.getMember_id());
			resultMap.put("name", StringUtil.formatStr(salesman.get("name")));
			resultMap.put("nick_name", StringUtil.formatStr(salesman.get("nick_name")));
			resultMap.put("head_pic_path", StringUtil.formatStr(salesman.get("head_pic_path")));
			resultMap.put("auth_status", StringUtil.formatStr(salesman.get("auth_status")));
			resultMap.put("is_driver", StringUtil.formatStr(salesman.get("is_driver")));
			resultMap.put("is_specialist", StringUtil.formatStr(salesman.get("is_specialist")));
			resultMap.put("is_guider", StringUtil.formatStr(salesman.get("is_guider")));
			resultMap.put("is_buyer", StringUtil.formatStr(salesman.get("is_buyer")));
			resultMap.put("is_operator", StringUtil.formatStr(salesman.get("is_operator")));
			resultMap.put("sex_key", StringUtil.formatStr(salesman.get("sex_key")));
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
	public void salesmanJurisdictionForOperate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id"));
		tempMap.put("is_operator", paramsMap.get("is_operator"));
		salesmanDao.updateSalesman(tempMap);
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", paramsMap.get("salesman_id"));
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", paramsMap.get("event_desc") + "");
		salesmanDao.insertSalesmanLog(logMap);
		
	}

	/**
	 ******************************************
	 * 业务员认证申请
	 ******************************************
	 */
	
	@Override
	public void authApplyListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> mDSalesmanAuthHistoryList = 
				salesmanAuthHistoryDao.selectSalesmanAuthHistoryList(paramsMap);
		for (Map<String, Object> authApplyMap : mDSalesmanAuthHistoryList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("name", StringUtil.formatStr(authApplyMap.get("name")));
			tempMap.put("nick_name", StringUtil.formatStr(authApplyMap.get("nick_name")));
			tempMap.put("contact_tel", StringUtil.formatStr(authApplyMap.get("contact_tel")));
			tempMap.put("path", StringUtil.formatStr(authApplyMap.get("path")));
			tempMap.put("auth_status", StringUtil.formatStr(authApplyMap.get("auth_status")));
			tempMap.put("created_date", 
					DateUtil.date2Str((Date) authApplyMap.get("created_date"), 
							DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("salesman_id", StringUtil.formatStr(authApplyMap.get("salesman_id")));
			tempMap.put("history_id", StringUtil.formatStr(authApplyMap.get("history_id")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}
	
	@Override
	public void authApplyLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "history_id" });
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> mDSalesmanAuthHistoryLogList = 
				salesmanAuthHistoryDao.selectSalesmanAuthHistoryLogList(paramsMap);
		for (Map<String, Object> authApplyLogMap : mDSalesmanAuthHistoryLogList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("log_id", StringUtil.formatStr(authApplyLogMap.get("log_id")));
			tempMap.put("history_id", StringUtil.formatStr(authApplyLogMap.get("history_id")));
			tempMap.put("category", StringUtil.formatStr(authApplyLogMap.get("category")));
			tempMap.put("tracked_date", 
					DateUtil.date2Str((Date) authApplyLogMap.get("tracked_date"), 
							DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("event_desc", StringUtil.formatStr(authApplyLogMap.get("event_desc")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}
	
	@Override
	public void authApplyDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "history_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("history_id", paramsMap.get("history_id"));
		List<Map<String, Object>> authApplyList = 
				salesmanAuthHistoryDao.selectSalesmanAuthHistory(tempMap);
		if (authApplyList.size() > 0) {
			Map<String, Object> authApply = authApplyList.get(0);
			tempMap.clear();
			tempMap.put("salesman_id", StringUtil.formatStr(authApply.get("salesman_id")));
			tempMap.put("history_id", StringUtil.formatStr(authApply.get("history_id")));
			tempMap.put("name", StringUtil.formatStr(authApply.get("name")));
			tempMap.put("sex_key", StringUtil.formatStr(authApply.get("sex_key")));
			tempMap.put("id_card", StringUtil.formatStr(authApply.get("id_card")));
			tempMap.put("id_card_pic_path", StringUtil.formatStr(authApply.get("id_card_pic_path")));
			tempMap.put("auth_status", StringUtil.formatStr(authApply.get("auth_status")));
			tempMap.put("remark", StringUtil.formatStr(authApply.get("remark")));
			result.setResultData(tempMap);
		}
	}
	
	@Override
	public void authApplyDetailForSalesassistantFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id"));
		List<Map<String, Object>> authApplyList = 
				salesmanAuthHistoryDao.selectSalesmanAuthHistory(tempMap);
		if (authApplyList.size() > 0) {
			Map<String, Object> authApply = authApplyList.get(0);
			tempMap.clear();
			tempMap.put("salesman_id", StringUtil.formatStr(authApply.get("salesman_id")));
			tempMap.put("history_id", StringUtil.formatStr(authApply.get("history_id")));
			tempMap.put("name", StringUtil.formatStr(authApply.get("name")));
			tempMap.put("sex_key", StringUtil.formatStr(authApply.get("sex_key")));
			tempMap.put("id_card", StringUtil.formatStr(authApply.get("id_card")));
			tempMap.put("id_card_pic_path", StringUtil.formatStr(authApply.get("id_card_pic_path")));
			tempMap.put("auth_status", StringUtil.formatStr(authApply.get("auth_status")));
			tempMap.put("remark", StringUtil.formatStr(authApply.get("remark")));
			result.setResultData(tempMap);
		}
	}

	@Override
	public void handleAuthPass(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "history_id"});
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("history_id", paramsMap.get("history_id"));
		List<Map<String, Object>> salesmanAuthHistoryList = 
				salesmanAuthHistoryDao.selectSalesmanAuthHistory(tempMap);
		if (salesmanAuthHistoryList.size() == 0 ){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员审批申请不存在");
		}
		Map<String, Object> salesmanAuthHistory = salesmanAuthHistoryList.get(0);
		if (!salesmanAuthHistory.get("auth_status").equals(Constant.AUTH_STATUS_NON_AUTH)){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员审批申请已操作");
		}
		//修改业务员认证审批表信息
		tempMap.put("auth_status", Constant.AUTH_STATUS_AUTH);
		int updateFlagSalesmanAuth = salesmanAuthHistoryDao.updateSalesmanAuthHistory(tempMap);
		if (updateFlagSalesmanAuth == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员认证日志
		Map<String, Object> logMapSalesmanAuth = new HashMap<String, Object>();
		logMapSalesmanAuth.put("log_id", IDUtil.getUUID());
		logMapSalesmanAuth.put("history_id", paramsMap.get("history_id"));
		logMapSalesmanAuth.put("category", "修改");
		logMapSalesmanAuth.put("tracked_date", new Date());
		logMapSalesmanAuth.put("event_desc", paramsMap.get("event_desc")+"");
		salesmanAuthHistoryDao.insertSalesmanAuthLog(logMapSalesmanAuth);
		
		//把认证信息 复制到 业务员表
		tempMap.clear();
		tempMap.put("salesman_id", StringUtil.formatStr(salesmanAuthHistory.get("salesman_id")));
		tempMap.put("sex_key", StringUtil.formatStr(salesmanAuthHistory.get("sex_key")));
		tempMap.put("name", StringUtil.formatStr(salesmanAuthHistory.get("name")));
		tempMap.put("id_card", StringUtil.formatStr(salesmanAuthHistory.get("id_card")));
		tempMap.put("id_card_pic_path", StringUtil.formatStr(salesmanAuthHistory.get("id_card_pic_path")));
		tempMap.put("auth_status", StringUtil.formatStr(Constant.AUTH_STATUS_AUTH));
		int updateFlagSalesman = salesmanDao.updateSalesman(tempMap);
		if (updateFlagSalesman == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员日志
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", StringUtil.formatStr(salesmanAuthHistory.get("salesman_id")));
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());	
		logMap.put("event_desc", paramsMap.get("event_desc")+"");
		salesmanDao.insertSalesmanLog(logMap);
		
		
		MDSystemInform mdSystemInform =new MDSystemInform();
		mdSystemInform.setInfo_id(IDUtil.getUUID());
		mdSystemInform.setInfo_comment("您提交的基本资料认证已审核通过。");
		mdSystemInform.setInfo_result("pass");
		mdSystemInform.setInfo_type("XXLX_01");
		mdSystemInform.setCre_by(StringUtil.formatStr(salesmanAuthHistory.get("salesman_id")));
		mdSystemInform.setCre_date(new Date());
		systemInformDao.insertSystemInform(mdSystemInform);
	}

	@Override
	public void handleAuthReject(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "history_id" });
		String event_desc = paramsMap.get("event_desc")+"";
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("history_id", paramsMap.get("history_id"));
		List<Map<String, Object>> salesmanAuthHistoryList = 
				salesmanAuthHistoryDao.selectSalesmanAuthHistory(tempMap);
		if (salesmanAuthHistoryList.size() == 0){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员审批申请不存在");
		}
		Map<String, Object> salesmanAuthHistory = salesmanAuthHistoryList.get(0);
		if (!salesmanAuthHistory.get("auth_status").equals(Constant.AUTH_STATUS_NON_AUTH)){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员审批申请已操作");
		}
		//修改业务员认证审批表信息
		tempMap.put("auth_status", Constant.AUTH_STATUS_REJECT);
		int updateFlagSalesmanAuthHistory = 
				salesmanAuthHistoryDao.updateSalesmanAuthHistory(tempMap);
		if (updateFlagSalesmanAuthHistory == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
	
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("history_id", paramsMap.get("history_id"));
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", event_desc);
		salesmanAuthHistoryDao.insertSalesmanAuthLog(logMap);
		
		//修改业务员信息
		tempMap.clear();
		tempMap.put("salesman_id", salesmanAuthHistory.get("salesman_id"));	
		tempMap.put("auth_status", StringUtil.formatStr(Constant.AUTH_STATUS_REJECT));
		int updateFlagSalesman = salesmanDao.updateSalesman(tempMap);
		if (updateFlagSalesman == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员日志
		logMap.clear();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", salesmanAuthHistory.get("salesman_id"));
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());	
		logMap.put("event_desc", event_desc);
		salesmanDao.insertSalesmanLog(logMap);
		
		MDSystemInform mdSystemInform =new MDSystemInform();
		mdSystemInform.setInfo_id(IDUtil.getUUID());
		mdSystemInform.setInfo_comment("您提交的基本资料认证审核未通过。原因："+event_desc.substring(event_desc.lastIndexOf("|")+1));
		mdSystemInform.setInfo_result("fail");
		mdSystemInform.setInfo_type("XXLX_01");
		mdSystemInform.setCre_by(StringUtil.formatStr(salesmanAuthHistory.get("salesman_id")));
		mdSystemInform.setCre_date(new Date());
		systemInformDao.insertSystemInform(mdSystemInform);
	}

	@Override
	public void handleAuthApply(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = null;
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "name", "id_card", "id_card_pic_path", "salesman_id" });
			String id_card = paramsMap.get("id_card") + "";
			String salesman_id = paramsMap.get("salesman_id") + "";
			
			// 1.添加业务员认证申请加锁
			lockKey = "[handleAuthApply]_" + salesman_id;
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();

			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "您的操作过于频繁,请稍后重试");
			}
			redisUtil.setStr(lockKey, salesman_id, 180); 

			// 2.验证身份证号(唯一)是否存在并且必须为已驳回的才能新增(查询md_salesman_auth_history表) 
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("salesman_id", salesman_id);
			Map<String, Object> tempSalesman = salesmanDao.selectSalesman(tempMap);
			if (tempSalesman == null) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员信息不存在");
			}
			
			tempMap.clear();
			tempMap.put("salesman_id", salesman_id);
			tempMap.put("auth_status_in", 
					new String[]{Constant.AUTH_STATUS_AUTH,Constant.AUTH_STATUS_NON_AUTH});
			List<Map<String, Object>> tempSalesmanAuthHistoryList = 
					salesmanAuthHistoryDao.selectSalesmanAuthHistory(tempMap);
			if (tempSalesmanAuthHistoryList.size() > 0) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员实名认证申请已存在");
			}
			
			tempMap.clear();
			tempMap.put("id_card", id_card);
			tempMap.put("auth_status_in", 
					new String[]{Constant.AUTH_STATUS_AUTH,Constant.AUTH_STATUS_NON_AUTH});
			List<Map<String, Object>> tempSalesmanAuthHistory = 
					salesmanAuthHistoryDao.selectSalesmanAuthHistory(tempMap);
			if (tempSalesmanAuthHistory.size() > 0) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员认证申请身份证账号已存在");
			}
			
			//3.把业务员认证申请信息添加到表(md_salesman_auth_history表)
			MDSalesmanAuthHistory mdSalesmanAuthHistory = new MDSalesmanAuthHistory();
			BeanConvertUtil.mapToBean(mdSalesmanAuthHistory, paramsMap);
			mdSalesmanAuthHistory.setHistory_id(IDUtil.getUUID());
			mdSalesmanAuthHistory.setAuth_status(Constant.AUTH_STATUS_NON_AUTH);
			Date now = new Date();
			mdSalesmanAuthHistory.setCreated_date(now);
			mdSalesmanAuthHistory.setModified_date(now);
			salesmanAuthHistoryDao.insertSalesmanAuthHistory(mdSalesmanAuthHistory);
			
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("history_id", mdSalesmanAuthHistory.getHistory_id());
			logMap.put("category", "新增");
			logMap.put("tracked_date", now);
			logMap.put("event_desc", "新增业务员认证申请信息");
			salesmanAuthHistoryDao.insertSalesmanAuthLog(logMap);
			
			//修改业务员信息
			tempMap.clear();
			tempMap.put("salesman_id", salesman_id);	
			tempMap.put("auth_status", StringUtil.formatStr(Constant.AUTH_STATUS_PROCESSING));
			int updateFlagSalesman = salesmanDao.updateSalesman(tempMap);
			if (updateFlagSalesman == 0) {
				throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
			}
			
			//添加业务员日志
			logMap.clear();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("salesman_id", salesman_id);
			logMap.put("category", "修改");
			logMap.put("tracked_date", new Date());	
			logMap.put("event_desc", "修改业务员认证状态改为审核中");
			salesmanDao.insertSalesmanLog(logMap);
			
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

	/**
	 ******************************************
	 * 业务员司机申请
	 ******************************************
	 */
	
	@Override
	public void driverApplyListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		String audit_status = (String) paramsMap.get("audit_status");
		if (null != audit_status && !"".equals(audit_status)) {
			List<String> list = StringUtil.str2List(audit_status, ",");
			if (list.size() >= 1) {
				paramsMap.remove("audit_status");
				paramsMap.put("audit_status_in", list);
			}
		}
		
		List<Map<String, Object>> mDSalesmanDriverList = 
				salesmanDriverDao.selectSalesmanDriverList(paramsMap);
		for (Map<String, Object> driverApplyMap : mDSalesmanDriverList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("name", StringUtil.formatStr(driverApplyMap.get("name")));
			tempMap.put("nick_name", StringUtil.formatStr(driverApplyMap.get("nick_name")));
			tempMap.put("sex_key", StringUtil.formatStr(driverApplyMap.get("sex_key")));
			tempMap.put("contact_tel", StringUtil.formatStr(driverApplyMap.get("contact_tel")));
			tempMap.put("path", StringUtil.formatStr(driverApplyMap.get("path")));
			tempMap.put("salesman_id", StringUtil.formatStr(driverApplyMap.get("salesman_id")));
			tempMap.put("audit_status", StringUtil.formatStr(driverApplyMap.get("audit_status")));
			tempMap.put("created_date", 
					DateUtil.date2Str((Date) driverApplyMap.get("created_date"), 
							DateUtil.fmt_yyyyMMddHHmmss));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}
	
	@Override
	public void handleDriverPass(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id", "logRemark"});
		
		String salesman_id = paramsMap.get("salesman_id")+"";
		
		//查询业务员司机信息
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id"));
		Map<String, Object> salesmanDriver = 
				salesmanDriverDao.selectSalesmanDriver(tempMap);
		
		if (!salesmanDriver.get("audit_status").equals(Constant.AUDIT_STATUS_NON_AUDIT)){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员司机审批申请已操作");
		}
		
		//修改业务员司机审批信息
		tempMap.clear();
		tempMap.put("salesman_id", salesman_id);
		tempMap.put("audit_status", Constant.AUDIT_STATUS_AUDIT);
		int updateFlagSalesmanDriver = salesmanDriverDao.updateSalesmanDriver(tempMap);
		if (updateFlagSalesmanDriver == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员司机审批日志信息
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", salesman_id);
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", paramsMap.get("logRemark"));
		salesmanDriverDao.insertSalesmanDriverLog(logMap);
		
		//修改业务员表司机审批信息
		tempMap.clear();
		tempMap.put("salesman_id", salesman_id);
		tempMap.put("is_driver", "Y");
		int updateSalesman = salesmanDao.updateSalesman(tempMap);
		if (updateSalesman == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员日志
		logMap.clear();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", salesman_id);
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", "新增角色：司机");
		salesmanDao.insertSalesmanLog(logMap);
		
		MDSystemInform mdSystemInform =new MDSystemInform();
		mdSystemInform.setInfo_id(IDUtil.getUUID());
		mdSystemInform.setInfo_comment("您提交的司机认证已审核通过。");
		mdSystemInform.setInfo_result("pass");
		mdSystemInform.setInfo_type("XXLX_02");
		mdSystemInform.setCre_by(salesman_id);
		mdSystemInform.setCre_date(new Date());
		systemInformDao.insertSystemInform(mdSystemInform);
	}

	@Override
	public void handleDriverReject(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id", "remark", "logRemark" });
		
		//查询业务员司机信息
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id"));
		Map<String, Object> salesmanDriver = salesmanDriverDao.selectSalesmanDriver(tempMap);
		if (!salesmanDriver.get("audit_status").equals(Constant.AUDIT_STATUS_NON_AUDIT)){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员审批申请已操作");
		}
		
		//修改业务员司机审批信息
		tempMap.put("audit_status", Constant.AUDIT_STATUS_REJECT);
		tempMap.put("remark", paramsMap.get("remark"));
		int  updateFlagSalesmanDriver = salesmanDriverDao.updateSalesmanDriver(tempMap);
		if (updateFlagSalesmanDriver == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员司机审批日志信息
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", paramsMap.get("salesman_id"));
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", paramsMap.get("logRemark"));
		salesmanDriverDao.insertSalesmanDriverLog(logMap);
		
		MDSystemInform mdSystemInform =new MDSystemInform();
		mdSystemInform.setInfo_id(IDUtil.getUUID());
		mdSystemInform.setInfo_comment("您提交的司机认证审核未通过。原因："+paramsMap.get("remark"));
		mdSystemInform.setInfo_result("fail");
		mdSystemInform.setInfo_type("XXLX_02");
		mdSystemInform.setCre_by(StringUtil.formatStr(paramsMap.get("salesman_id")));
		mdSystemInform.setCre_date(new Date());
		systemInformDao.insertSystemInform(mdSystemInform);
	}
	
	@Override
	public void driverApply(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = null;
		try {
		
			ValidateUtil.validateParams(paramsMap,
					new String[] { "salesman_id", "car_model", "car_number", "capacity",
							"appearance_pic_path", "driving_license_pic_path",
							"driving_permit_pic_path" });
			String salesman_id = paramsMap.get("salesman_id") + "";
			
			// 1.添加业务员认证申请加锁
			lockKey = "[driverApply]_" + salesman_id;
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();

			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "您的操作过于频繁,请稍后重试");
			}
			redisUtil.setStr(lockKey, salesman_id, 180);

			// 2.验证业务员ID已存在(查询md_salesman_auth_history表) 
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("salesman_id", salesman_id);
			tempMap.put("auth_status_in", new String[]{Constant.AUTH_STATUS_AUTH});
			List<Map<String, Object>> tempSalesmanAuthHistoryList = 
					salesmanAuthHistoryDao.selectSalesmanAuthHistory(tempMap);
			if (tempSalesmanAuthHistoryList.size() == 0) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员实名认证申请未通过");
			}
				
			tempMap.clear();
			tempMap.put("salesman_id", salesman_id);
			Map<String, Object> tempSalesmanDriver = 
					salesmanDriverDao.selectSalesmanDriver(tempMap);
			if (null != tempSalesmanDriver ) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员司机申请审批已存在");
			} 
			
			//3.把业务员司机申请信息添加到表(md_salesman_driver表)
			if(null == tempSalesmanDriver){
				MDSalesmanDriver mdSalesmanDriver = new MDSalesmanDriver();
				BeanConvertUtil.mapToBean(mdSalesmanDriver, paramsMap);
				mdSalesmanDriver.setAudit_status(Constant.AUDIT_STATUS_NON_AUDIT);
				Date now = new Date();
				mdSalesmanDriver.setCreated_date(now);
				mdSalesmanDriver.setModified_date(now);
				salesmanDriverDao.insertSalesmanDriver(mdSalesmanDriver);
				
				Map<String, Object> logMap = new HashMap<String, Object>();
				logMap.put("log_id", IDUtil.getUUID());
				logMap.put("salesman_id", salesman_id);
				logMap.put("category", "新增");
				logMap.put("tracked_date", now);
				logMap.put("event_desc", "新增业务员司机申请信息");
				salesmanDriverDao.insertSalesmanDriverLog(logMap);
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
	
	@Override
	public void handleDriverAgainApply(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = null;
		try {
		
			ValidateUtil.validateParams(paramsMap,
					new String[] { "salesman_id", "car_model", "car_number", "capacity",
							"appearance_pic_path", "driving_license_pic_path",
							"driving_permit_pic_path" });
			String salesman_id = paramsMap.get("salesman_id") + "";
			String car_model = paramsMap.get("car_model") + "";
			String car_number = paramsMap.get("car_number") + "";
			String capacity = paramsMap.get("capacity") + "";
			String appearance_pic_path = paramsMap.get("appearance_pic_path") + "";
			String driving_license_pic_path = paramsMap.get("driving_license_pic_path") + "";
			String driving_permit_pic_path = paramsMap.get("driving_permit_pic_path") + "";
			
			// 1.添加业务员认证申请加锁
			lockKey = "[handleDriverAgainApply]_" + salesman_id;
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();

			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "您的操作过于频繁,请稍后重试");
			}
			redisUtil.setStr(lockKey, salesman_id, 180);

			// 2.1 验证业务员是否实名认证通过(查询md_salesman_auth_history表) 
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("salesman_id", salesman_id);
			tempMap.put("auth_status_in", new String[]{Constant.AUTH_STATUS_AUTH});
			List<Map<String, Object>> tempSalesmanAuthHistoryList = 
					salesmanAuthHistoryDao.selectSalesmanAuthHistory(tempMap);
			if (tempSalesmanAuthHistoryList.size() == 0) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员认证申请未通过");
			}
			// 2.2验证业务员是否已经成为司机(查询md_salesman表) 
			tempMap.clear();
			tempMap.put("salesman_id", salesman_id);
			Map<String, Object> salesman = salesmanDao.selectSalesman(tempMap);
			if(salesman.get("is_driver").equals("Y")){
				throw new BusinessException(RspCode.SALESMAN_ERROR, "你已经是一名司机，不能再次提交申请");
			}
			// 2.2验证业务员司机申请是否已经提交过(查询md_salesman_driver表) 
			tempMap.put("salesman_id", salesman_id);
			Map<String, Object> tempSalesmanDriver = 
					salesmanDriverDao.selectSalesmanDriver(tempMap);
			if (null == tempSalesmanDriver ) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员司机申请未提交过");
			}
			
			//3.把业务员司机申请信息添加到表(md_salesman_driver表)
			//修改
			MDSalesmanDriver mdSalesmanDriver = new MDSalesmanDriver();
			BeanConvertUtil.mapToBean(mdSalesmanDriver, paramsMap);
			tempMap.put("salesman_id", salesman_id);
			tempMap.put("car_model", car_model);
			tempMap.put("car_number", car_number);
			tempMap.put("capacity", capacity);
			tempMap.put("appearance_pic_path", appearance_pic_path);
			tempMap.put("driving_license_pic_path", driving_license_pic_path);
			tempMap.put("driving_permit_pic_path", driving_permit_pic_path);
			tempMap.put("audit_status", Constant.AUDIT_STATUS_NON_AUDIT);
			int updateFlagSalesmanDriver = salesmanDriverDao.updateSalesmanDriver(tempMap);
			if (updateFlagSalesmanDriver == 0) {
				throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
			}
			 
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("salesman_id", paramsMap.get("salesman_id"));
			logMap.put("category", "修改");
			logMap.put("tracked_date", new Date());
			logMap.put("event_desc", "新增(修改)业务员司机再次申请信息");
			salesmanDriverDao.insertSalesmanDriverLog(logMap);
		
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
		
	@Override
	public void driverApplyDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id")+"");
		Map<String, Object> driverApply = salesmanDriverDao.selectSalesmanDriver(tempMap);
		if (null != driverApply) {
			tempMap.clear();
			tempMap.put("salesman_id", StringUtil.formatStr(driverApply.get("salesman_id")));
			tempMap.put("car_model", StringUtil.formatStr(driverApply.get("car_model")));
			tempMap.put("car_number", StringUtil.formatStr(driverApply.get("car_number")));
			tempMap.put("capacity", StringUtil.formatStr(driverApply.get("capacity")));
			tempMap.put("appearance_pic_path", 
					StringUtil.formatStr(driverApply.get("appearance_pic_path")));
			tempMap.put("driving_license_pic_path", 
					StringUtil.formatStr(driverApply.get("driving_license_pic_path")));
			tempMap.put("driving_permit_pic_path", 
					StringUtil.formatStr(driverApply.get("driving_permit_pic_path")));
			tempMap.put("audit_status", 
					StringUtil.formatStr(driverApply.get("audit_status")));
			tempMap.put("created_date", 
					DateUtil.date2Str((Date) driverApply.get("created_date"), 
							DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("remark", StringUtil.formatStr(driverApply.get("remark")));
			result.setResultData(tempMap);
		}
	}
	
	@Override
	public void driverApplyDetailForSalesassistantFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id")+"");
		Map<String, Object> driverApply = salesmanDriverDao.selectSalesmanDriver(tempMap);
		if (null != driverApply) {
			tempMap.clear();
			tempMap.put("salesman_id", StringUtil.formatStr(driverApply.get("salesman_id")));
			tempMap.put("car_model", StringUtil.formatStr(driverApply.get("car_model")));
			tempMap.put("car_number", StringUtil.formatStr(driverApply.get("car_number")));
			tempMap.put("capacity", StringUtil.formatStr(driverApply.get("capacity")));
			tempMap.put("appearance_pic_path", 
					StringUtil.formatStr(driverApply.get("appearance_pic_path")));
			tempMap.put("driving_license_pic_path", 
					StringUtil.formatStr(driverApply.get("driving_license_pic_path")));
			tempMap.put("driving_permit_pic_path", 
					StringUtil.formatStr(driverApply.get("driving_permit_pic_path")));
			tempMap.put("audit_status", 
					StringUtil.formatStr(driverApply.get("audit_status")));
			tempMap.put("created_date", 
					DateUtil.date2Str((Date) driverApply.get("created_date"), 
							DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("remark", StringUtil.formatStr(driverApply.get("remark")));
			result.setResultData(tempMap);
		}
		
	}

	@Override
	public void driverApplyLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" });
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> mDSalesmanDriverLogList = 
				salesmanDriverDao.selectSalesmanDriverLogList(paramsMap);
		for (Map<String, Object> driverApplyLogMap : mDSalesmanDriverLogList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("log_id", StringUtil.formatStr(driverApplyLogMap.get("log_id")));
			tempMap.put("salesman_id", 
					StringUtil.formatStr(driverApplyLogMap.get("msd_salesman_id")));
			tempMap.put("tracked_date", StringUtil.formatStr(
					DateUtil.date2Str((Date) driverApplyLogMap.get("tracked_date"), 
							DateUtil.fmt_yyyyMMddHHmmss)));
			tempMap.put("event_desc", StringUtil.formatStr(driverApplyLogMap.get("event_desc")));
			tempMap.put("nick_name", StringUtil.formatStr(driverApplyLogMap.get("nick_name")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	
	/**
	 ******************************************
	 * 业务员地服申请
	 ******************************************
	 */
	
	@Override
	public void specialistApplyListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		String audit_status = (String) paramsMap.get("audit_status");
		if (null != audit_status && !"".equals(audit_status)) {
			List<String> list = StringUtil.str2List(audit_status, ",");
			if (list.size() >= 1) {
				paramsMap.remove("audit_status");
				paramsMap.put("audit_status_in", list);
			}
		}
		
		List<Map<String, Object>> mDSalesmanSpecialistList = 
				salesmanSpecialistDao.selectSalesmanSpecialistList(paramsMap);
		for (Map<String, Object> specialistApplyMap : mDSalesmanSpecialistList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("name", 
					StringUtil.formatStr(specialistApplyMap.get("name")));
			tempMap.put("nick_name", 
					StringUtil.formatStr(specialistApplyMap.get("nick_name")));
			tempMap.put("sex_key", 
					StringUtil.formatStr(specialistApplyMap.get("sex_key")));
			tempMap.put("contact_tel", 
					StringUtil.formatStr(specialistApplyMap.get("contact_tel")));
			tempMap.put("path", 
					StringUtil.formatStr(specialistApplyMap.get("path")));
			tempMap.put("salesman_id", 
					StringUtil.formatStr(specialistApplyMap.get("salesman_id")));
			tempMap.put("audit_status", 
					StringUtil.formatStr(specialistApplyMap.get("audit_status")));
			tempMap.put("created_date", 
				DateUtil.date2Str((Date) specialistApplyMap.get("created_date"), 
						DateUtil.fmt_yyyyMMddHHmmss));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
		
	}

	@Override
	public void specialistApplyLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" });
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> mDSalesmanSpecialistLogList = 
				salesmanSpecialistDao.selectSalesmanSpecialistLogList(paramsMap);
		for (Map<String, Object> specialistApplyLogMap : mDSalesmanSpecialistLogList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("log_id", 
					StringUtil.formatStr(specialistApplyLogMap.get("log_id")));
			tempMap.put("salesman_id", 
					StringUtil.formatStr(specialistApplyLogMap.get("mssl_salesman_id")));
			tempMap.put("tracked_date", 
					DateUtil.date2Str((Date) specialistApplyLogMap.get("tracked_date"), 
							DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("event_desc", 
					StringUtil.formatStr(specialistApplyLogMap.get("event_desc")));
			tempMap.put("nick_name", 
					StringUtil.formatStr(specialistApplyLogMap.get("nick_name")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void specialistApplyDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id")+"");
		Map<String, Object> driverApply = salesmanSpecialistDao.selectSalesmanSpecialist(tempMap);
		if (null != driverApply) {
			tempMap.clear();
			tempMap.put("salesman_id", StringUtil.formatStr(driverApply.get("salesman_id")));
			tempMap.put("is_marketer", StringUtil.formatStr(driverApply.get("is_marketer")));
			tempMap.put("is_assistant", StringUtil.formatStr(driverApply.get("is_assistant")));
			tempMap.put("service_store_num", StringUtil.formatStr(driverApply.get("service_store_num")));
			tempMap.put("self_store_num", StringUtil.formatStr(driverApply.get("self_store_num")));
			tempMap.put("audit_status", StringUtil.formatStr(driverApply.get("audit_status")));
			tempMap.put("remark", StringUtil.formatStr(driverApply.get("remark")));
			result.setResultData(tempMap);
		}
	}
	
	@Override
	public void specialistApplyDetailForSalesassistantFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id")+"");
		Map<String, Object> driverApply = 
				salesmanSpecialistDao.selectSalesmanSpecialist(tempMap);
		if (null != driverApply) {
			tempMap.clear();
			tempMap.put("salesman_id", 
					StringUtil.formatStr(driverApply.get("salesman_id")));
			tempMap.put("is_marketer", 
					StringUtil.formatStr(driverApply.get("is_marketer")));
			tempMap.put("is_assistant", 
					StringUtil.formatStr(driverApply.get("is_assistant")));
			tempMap.put("service_store_num", 
					StringUtil.formatStr(driverApply.get("service_store_num")));
			tempMap.put("self_store_num", 
					StringUtil.formatStr(driverApply.get("self_store_num")));
			tempMap.put("audit_status", 
					StringUtil.formatStr(driverApply.get("audit_status")));
			tempMap.put("remark", 
					StringUtil.formatStr(driverApply.get("remark")));
			result.setResultData(tempMap);
		}
	}

	@Override
	public void handleSpecialistPass(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id", "logRemark"});
		
		String salesman_id = paramsMap.get("salesman_id")+"";
		
		//查询业务员地服信息
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id"));
		Map<String, Object> salesmanDriver = 
				salesmanSpecialistDao.selectSalesmanSpecialist(tempMap);
		
		if (!salesmanDriver.get("audit_status").equals(Constant.AUDIT_STATUS_NON_AUDIT)){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员地服审批申请已操作");
		}
		
		//修改业务员地服审批信息
		tempMap.clear();
		tempMap.put("salesman_id", salesman_id);
		tempMap.put("audit_status", Constant.AUDIT_STATUS_AUDIT);
		int updateFlagSalesmanSpecialist = 
				salesmanSpecialistDao.updateSalesmanSpecialist(tempMap);
		if (updateFlagSalesmanSpecialist == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员地服审批日志信息
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", salesman_id);
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", paramsMap.get("logRemark"));
		salesmanSpecialistDao.insertSalesmanSpecialistLog(logMap);
		
		//修改业务员表司机审批信息
		tempMap.clear();
		tempMap.put("salesman_id", salesman_id);
		tempMap.put("is_specialist", "Y");
		int updateSalesman = salesmanDao.updateSalesman(tempMap);
		if (updateSalesman == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员日志
		logMap.clear();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", salesman_id);
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", "新增角色：地服");
		salesmanDao.insertSalesmanLog(logMap);
		
		MDSystemInform mdSystemInform =new MDSystemInform();
		mdSystemInform.setInfo_id(IDUtil.getUUID());
		mdSystemInform.setInfo_comment("您提交的地服认证已审核通过。");
		mdSystemInform.setInfo_result("pass");
		mdSystemInform.setInfo_type("XXLX_02");
		mdSystemInform.setCre_by(salesman_id);
		mdSystemInform.setCre_date(new Date());
		systemInformDao.insertSystemInform(mdSystemInform);
	}

	@Override
	public void handleSpecialistReject(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id", "remark", "logRemark" });
		
		//查询业务员地服信息
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("salesman_id", paramsMap.get("salesman_id"));
		Map<String, Object> salesmanSpecialist = 
				salesmanSpecialistDao.selectSalesmanSpecialist(tempMap);
		if (!salesmanSpecialist.get("audit_status").equals(Constant.AUDIT_STATUS_NON_AUDIT)){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员地服审批申请已操作");
		}
		
		//修改业务员地服审批信息
		tempMap.put("audit_status", Constant.AUDIT_STATUS_REJECT);
		tempMap.put("remark", paramsMap.get("remark"));
		int  updateFlagSalesmanSpecialist = 
				salesmanSpecialistDao.updateSalesmanSpecialist(tempMap);
		if (updateFlagSalesmanSpecialist == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员地服审批日志信息
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", paramsMap.get("salesman_id"));
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", paramsMap.get("logRemark"));
		salesmanSpecialistDao.insertSalesmanSpecialistLog(logMap);
		
		MDSystemInform mdSystemInform =new MDSystemInform();
		mdSystemInform.setInfo_id(IDUtil.getUUID());
		mdSystemInform.setInfo_comment("您提交的地服认证审核未通过。原因："+paramsMap.get("remark"));
		mdSystemInform.setInfo_result("fail");
		mdSystemInform.setInfo_type("XXLX_02");
		mdSystemInform.setCre_by(StringUtil.formatStr(paramsMap.get("salesman_id")));
		mdSystemInform.setCre_date(new Date());
		systemInformDao.insertSystemInform(mdSystemInform);
	}

	@Override
	public void specialistApply(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = null;
		try {
		
			ValidateUtil.validateParams(paramsMap,
					new String[] { "salesman_id"});
			String salesman_id = paramsMap.get("salesman_id") + "";
		
			// 1.添加业务员认证申请加锁
			lockKey = "[specialistApply]_" + salesman_id;
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();

			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "您的操作过于频繁,请稍后重试");
			}
			redisUtil.setStr(lockKey, salesman_id, 180);

			// 2.验证(查询md_salesman_auth_history表) 
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("salesman_id", salesman_id);
			tempMap.put("auth_status_in", new String[]{Constant.AUTH_STATUS_AUTH});
			List<Map<String, Object>> tempSalesmanAuthHistoryList = 
					salesmanAuthHistoryDao.selectSalesmanAuthHistory(tempMap);
			if (tempSalesmanAuthHistoryList.size() == 0) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员实名认证申请未通过");
			}
				
			tempMap.clear();
			tempMap.put("salesman_id", salesman_id);
			Map<String, Object> tempSalesmanSpecialist = 
					salesmanSpecialistDao.selectSalesmanSpecialist(tempMap);
			if (null != tempSalesmanSpecialist ) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员地服申请审批已存在");
			} 
			
			//3.把业务员司机申请信息添加到表(md_salesman_Specialist表)
			if(null == tempSalesmanSpecialist){
				MDSalesmanSpecialist mdSalesmanSpecialist = new MDSalesmanSpecialist();
				BeanConvertUtil.mapToBean(mdSalesmanSpecialist, paramsMap);
				mdSalesmanSpecialist.setSalesman_id(salesman_id);
				mdSalesmanSpecialist.setIs_assistant("N");
				mdSalesmanSpecialist.setIs_marketer("N");
				mdSalesmanSpecialist.setAudit_status(Constant.AUDIT_STATUS_NON_AUDIT);
				Date now = new Date();
				mdSalesmanSpecialist.setCreated_date(now);
				mdSalesmanSpecialist.setModified_date(now);
				salesmanSpecialistDao.insertSalesmanSpecialist(mdSalesmanSpecialist);
				
				Map<String, Object> logMap = new HashMap<String, Object>();
				logMap.put("log_id", IDUtil.getUUID());
				logMap.put("salesman_id", salesman_id);
				logMap.put("category", "新增");
				logMap.put("tracked_date", now);
				logMap.put("event_desc", "新增业务员地服申请信息");
				salesmanSpecialistDao.insertSalesmanSpecialistLog(logMap);
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

	
	@Override
	public void handleSpecialistAgainApply(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = null;
		try {
		
			ValidateUtil.validateParams(paramsMap,
					new String[] { "salesman_id" });
			String salesman_id = paramsMap.get("salesman_id") + "";
			//String service_store_num = paramsMap.get("service_store_num") + "";
			//String self_store_num = paramsMap.get("self_store_num") + "";
			
			// 1.添加业务员认证申请加锁
			lockKey = "[handleSpecialistAgainApply]_" + salesman_id;
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();

			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "您的操作过于频繁,请稍后重试");
			}
			redisUtil.setStr(lockKey, salesman_id, 180);

			// 2.1 验证业务员是否实名认证通过(查询md_salesman_auth_history表) 
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("salesman_id", salesman_id);
			tempMap.put("auth_status_in", new String[]{Constant.AUTH_STATUS_AUTH});
			List<Map<String, Object>> tempSalesmanAuthHistoryList = 
					salesmanAuthHistoryDao.selectSalesmanAuthHistory(tempMap);
			if (tempSalesmanAuthHistoryList.size() == 0) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员认证申请未通过");
			}
			// 2.2验证业务员是否已经成为地服(查询md_salesman表) 
			tempMap.clear();
			tempMap.put("salesman_id", salesman_id);
			Map<String, Object> salesman = salesmanDao.selectSalesman(tempMap);
			if(salesman.get("is_specialist").equals("Y")){
				throw new BusinessException(RspCode.SALESMAN_ERROR, "你已经是一名地服，不能再次提交申请");
			}
			// 2.2验证业务员地服申请是否已经提交过(查询md_salesman_driver表) 
			tempMap.put("salesman_id", salesman_id);
			Map<String, Object> tempSalesmanSpecialist = 
					salesmanSpecialistDao.selectSalesmanSpecialist(tempMap);
			if (null == tempSalesmanSpecialist ) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员地服申请未提交过");
			}
			
			//3.把业务员地服申请信息添加到表(md_salesman_specialist表)
			//修改
			tempMap.clear();
			tempMap.put("salesman_id", salesman_id);
			//tempMap.put("service_store_num", service_store_num);
			//tempMap.put("self_store_num", self_store_num);
			tempMap.put("is_marketer", "N");
			tempMap.put("is_assistant", "N");
			tempMap.put("audit_status", Constant.AUDIT_STATUS_NON_AUDIT);
			int updateFlagSalesmanSpecialist = 
					salesmanSpecialistDao.updateSalesmanSpecialist(tempMap);
			if (updateFlagSalesmanSpecialist == 0) {
				throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
			}
			 
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("salesman_id", paramsMap.get("salesman_id"));
			logMap.put("category", "修改");
			logMap.put("tracked_date", new Date());
			logMap.put("event_desc", "新增(修改)业务员地服再次申请信息");
			salesmanSpecialistDao.insertSalesmanSpecialistLog(logMap);
		
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

	@Override
	public void headlinesListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("titel", "更棒的名字，更牛的平台，本地生活升级为“领有惠”啦");
		tempMap.put("path","http://cms.meitianhui.com/wp-content/uploads/2017/06/20170630171707-12.jpg");
		tempMap.put("tracked_date", "2017-06-18 00:00:00");
		tempMap.put("link", "http://cms.meitianhui.com/?p=2520");
		tempMap.put("date_source", "每天惠");
		resultList.add(tempMap);
		
		Map<String, Object> tempMap1 = new HashMap<String, Object>();
		tempMap1.put("titel", "山东每天惠供应链5月12日盛大开仓！");
		tempMap1.put("path","http://cms.meitianhui.com/wp-content/uploads/2017/06/20170630161706-9.jpg");
		tempMap1.put("tracked_date", "2017-05-12 00:00:00");
		tempMap1.put("link", "http://cms.meitianhui.com/?p=2538");
		tempMap1.put("date_source", "每天惠");
		resultList.add(tempMap1);
		
		Map<String, Object> tempMap2 = new HashMap<String, Object>();
		tempMap2.put("titel", "“连锁中国·实惠人民”每天惠品牌文化升级发布会&每天惠大学成立典礼");
		tempMap2.put("path","http://cms.meitianhui.com/wp-content/uploads/2017/06/20170319-1.jpg");
		tempMap2.put("tracked_date", "2017-03-19 00:00:00");
		tempMap2.put("link", "http://cms.meitianhui.com/?p=2497 ");
		tempMap2.put("date_source", "每天惠");
		resultList.add(tempMap2);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data_list", resultList);
		result.setResultData(map);
	}

	
	/**
	 ******************************************
	 * 业务员助教申请
	 ******************************************
	 */
	
	@Override
	public void assistantApplicationApplyListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		String audit_status = (String) paramsMap.get("audit_status");
		if (null != audit_status && !"".equals(audit_status)) {
			List<String> list = StringUtil.str2List(audit_status, ",");
			if (list.size() >= 1) {
				paramsMap.remove("audit_status");
				paramsMap.put("audit_status_in", list);
			}
		}
		
		List<Map<String, Object>> mDAssistantApplicationList = 
				assistantApplicationDao.selectAssistantApplicationList(paramsMap);
		for (Map<String, Object> assistantApplicationMap : mDAssistantApplicationList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("name", StringUtil.formatStr(assistantApplicationMap.get("name")));
			tempMap.put("nick_name", StringUtil.formatStr(assistantApplicationMap.get("nick_name")));
			tempMap.put("sex_key", StringUtil.formatStr(assistantApplicationMap.get("sex_key")));
			tempMap.put("contact_tel", StringUtil.formatStr(assistantApplicationMap.get("contact_tel")));
			tempMap.put("path", StringUtil.formatStr(assistantApplicationMap.get("path")));
			tempMap.put("audit_status", StringUtil.formatStr(assistantApplicationMap.get("audit_status")));
			tempMap.put("created_date", 
					DateUtil.date2Str((Date) assistantApplicationMap.get("created_date"), 
							DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("salesman_id", StringUtil.formatStr(assistantApplicationMap.get("salesman_id")));
			tempMap.put("assistant_id", StringUtil.formatStr(assistantApplicationMap.get("assistant_id")));
			tempMap.put("application_id", StringUtil.formatStr(assistantApplicationMap.get("application_id")));
			tempMap.put("stores_name", StringUtil.formatStr(assistantApplicationMap.get("stores_name")));
			tempMap.put("mss_contact_person", StringUtil.formatStr(assistantApplicationMap.get("mss_contact_person")));
			tempMap.put("mss_contact_tel", StringUtil.formatStr(assistantApplicationMap.get("mss_contact_tel")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void assistantApplicationApplyLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "application_id" });
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> mDAssistantApplicationLogList = 
				assistantApplicationDao.selectAssistantApplicationLogList(paramsMap);
		for (Map<String, Object> assistantApplicationLogMap : mDAssistantApplicationLogList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("log_id", 
					StringUtil.formatStr(assistantApplicationLogMap.get("log_id")));
			tempMap.put("application_id", 
					StringUtil.formatStr(assistantApplicationLogMap.get("application_id")));
			tempMap.put("category", 
					StringUtil.formatStr(assistantApplicationLogMap.get("category")));
			tempMap.put("tracked_date", 
					DateUtil.date2Str((Date) assistantApplicationLogMap.get("tracked_date"), 
							DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("event_desc", 
					StringUtil.formatStr(assistantApplicationLogMap.get("event_desc")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
		
	}

	@Override
	public void assistantApplicationApplyDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "application_id" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("application_id", paramsMap.get("application_id"));
		List<Map<String, Object>> assistantApplicationList = 
				assistantApplicationDao.selectAssistantApplication(tempMap);
		if (assistantApplicationList.size() > 0) {
			Map<String, Object> assistantApplication = assistantApplicationList.get(0);
			tempMap.clear();
			//助教表信息
			tempMap.put("application_id", StringUtil.formatStr(assistantApplication.get("application_id")));
			tempMap.put("stores_id", StringUtil.formatStr(assistantApplication.get("stores_id")));
			tempMap.put("assistant_id", StringUtil.formatStr(assistantApplication.get("assistant_id")));
			tempMap.put("audit_status", StringUtil.formatStr(assistantApplication.get("audit_status")));
			tempMap.put("created_date", StringUtil.formatStr(assistantApplication.get("created_date")));
			tempMap.put("modified_date", StringUtil.formatStr(assistantApplication.get("modified_date")));
			tempMap.put("remark", StringUtil.formatStr(assistantApplication.get("remark")));
			//门店信息
			tempMap.put("stores_no", StringUtil.formatStr(assistantApplication.get("stores_no")));
			tempMap.put("stores_name", StringUtil.formatStr(assistantApplication.get("stores_name")));
			tempMap.put("business_type_key", StringUtil.formatStr(assistantApplication.get("business_type_key")));
			tempMap.put("path", StringUtil.formatStr(assistantApplication.get("path")));
			tempMap.put("address", StringUtil.formatStr(assistantApplication.get("address")));
			tempMap.put("contact_person", StringUtil.formatStr(assistantApplication.get("contact_person")));
			tempMap.put("contact_tel", StringUtil.formatStr(assistantApplication.get("contact_tel")));
			result.setResultData(tempMap);
		}
	}

	@Override
	public void handleAssistantApplicationPass(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "application_id"});
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("application_id", paramsMap.get("application_id"));
		List<Map<String, Object>> assistantApplicationList = 
				assistantApplicationDao.selectAssistantApplication(tempMap);
		if (assistantApplicationList.size() == 0 ){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员助教申请不存在");
		}
		Map<String, Object> assistantApplication = assistantApplicationList.get(0);
		if (!assistantApplication.get("audit_status").equals(Constant.AUDIT_STATUS_NON_AUDIT)){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员助教申请已操作");
		}
		//修改业务员助教审批表信息
		tempMap.put("audit_status", Constant.AUDIT_STATUS_AUDIT);
		int updateFlagAssistantApplication = 
				assistantApplicationDao.updateAssistantApplication(tempMap);
		if (updateFlagAssistantApplication == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员助教审批日志
		Map<String, Object> logMapAssistantApplication = new HashMap<String, Object>();
		logMapAssistantApplication.put("log_id", IDUtil.getUUID());
		logMapAssistantApplication.put("application_id", paramsMap.get("application_id"));
		logMapAssistantApplication.put("category", "修改");
		logMapAssistantApplication.put("tracked_date", new Date());
		logMapAssistantApplication.put("event_desc", paramsMap.get("logRemark"));
		assistantApplicationDao.insertAssistantApplicationLog(logMapAssistantApplication);
		
		//把修改业务员地服表 助教状态
		tempMap.clear();
		tempMap.put("salesman_id", StringUtil.formatStr(assistantApplication.get("assistant_id")));
		tempMap.put("is_assistant", "Y");
		tempMap.put("service_store_num", "+1");
		int updateFlagSalesmanSpecialist = 
				salesmanSpecialistDao.updateSalesmanSpecialist(tempMap);
		if (updateFlagSalesmanSpecialist == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员地服日志
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("salesman_id", StringUtil.formatStr(assistantApplication.get("assistant_id")));
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());	
		logMap.put("event_desc", paramsMap.get("logRemark"));
		salesmanSpecialistDao.insertSalesmanSpecialistLog(logMap);
		
		//修改门店表  把业务员ID添加到助教字段中
		logMap.clear();
		logMap.put("stores_id", assistantApplication.get("stores_id"));
		logMap.put("assistant_id", assistantApplication.get("assistant_id"));
		StoresDao.updateMDStores(logMap);
		
		//添加到门店日志表 
		logMap.clear();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("stores_id", assistantApplication.get("stores_id"));
		logMap.put("category", "助教审批通过添加业务员(助教)ID");
		logMap.put("tracked_date", new Date());
		logMap.put("event", "添加业务员(助教)ID到assistant_id");
		StoresDao.insertMDStoresLog(logMap);
		
		//添加系统消息
		MDSystemInform mdSystemInform =new MDSystemInform();
		mdSystemInform.setInfo_id(IDUtil.getUUID());
		mdSystemInform.setInfo_comment("您提交的助教审核已审核通过。");
		mdSystemInform.setInfo_result("pass");
		mdSystemInform.setInfo_type("XXLX_02");
		mdSystemInform.setCre_by(StringUtil.formatStr(assistantApplication.get("assistant_id")));
		mdSystemInform.setCre_date(new Date());
		systemInformDao.insertSystemInform(mdSystemInform);
	}

	@Override
	public void handleAssistantApplicationReject(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "application_id", "remark" });
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("application_id", paramsMap.get("application_id"));
		List<Map<String, Object>> assistantApplicationList = 
				assistantApplicationDao.selectAssistantApplication(tempMap);
		if (assistantApplicationList.size() == 0){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员助教申请不存在");
		}
		Map<String, Object> assistantApplication = assistantApplicationList.get(0);
		if (!assistantApplication.get("audit_status").equals(Constant.AUDIT_STATUS_NON_AUDIT)){
			throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员助教申请已操作");
		}
		
		//修改业务员助教审批表信息
		tempMap.put("audit_status", Constant.AUDIT_STATUS_REJECT);
		int updateFlagAssistantApplication = 
				assistantApplicationDao.updateAssistantApplication(tempMap);
		if (updateFlagAssistantApplication == 0) {
			throw new BusinessException(RspCode.SALESMAN_PROCESSING, "操作失败,请刷新后重试");
		}
		
		//添加业务员助教审批表日志
		Map<String, Object> logMap = new HashMap<String, Object>();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("application_id", paramsMap.get("application_id"));
		logMap.put("category", "修改");
		logMap.put("tracked_date", new Date());
		logMap.put("event_desc", paramsMap.get("logRemark"));
		assistantApplicationDao.insertAssistantApplicationLog(logMap);
		
		//添加系统消息信息
		MDSystemInform mdSystemInform =new MDSystemInform();
		mdSystemInform.setInfo_id(IDUtil.getUUID());
		mdSystemInform.setInfo_comment("您提交的助教认证审核未通过。原因："+paramsMap.get("remark"));
		mdSystemInform.setInfo_result("fail");
		mdSystemInform.setInfo_type("XXLX_02");
		mdSystemInform.setCre_by(StringUtil.formatStr(assistantApplication.get("assistant_id")));
		mdSystemInform.setCre_date(new Date());
		systemInformDao.insertSystemInform(mdSystemInform);
		
		//8.修改门店信息   取消锁定门店
		logMap.clear();
		logMap.put("stores_id", StringUtil.formatStr(assistantApplication.get("stores_id")));
		logMap.put("is_assistant_locked", "N");
		logMap.put("assistant_expired_date", "assistant_expired_date");
		StoresDao.updateMDStores(logMap);
		
		//9.添加到门店日志表 
		logMap.clear();
		logMap.put("log_id", IDUtil.getUUID());
		logMap.put("stores_id", StringUtil.formatStr(assistantApplication.get("stores_id")));
		logMap.put("category", "助教审批驳回取消锁定门店");
		logMap.put("tracked_date", new Date());
		logMap.put("event", "助教审批驳回取消锁定门店把字段为N");
		StoresDao.insertMDStoresLog(logMap);
	}

	@Override
	public void handleAssistantApplicationApply(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = null;
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id", "assistant_id" , "remark" });
			String stores_id = paramsMap.get("stores_id") != null?paramsMap.get("stores_id").toString():"";
			String assistant_id = paramsMap.get("assistant_id") != null?paramsMap.get("assistant_id").toString():"";
			// 1.添加业务员助教申请加锁
			lockKey = "[handleAssistantApplicationApply]_" + stores_id;
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();

			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "您的操作过于频繁,请稍后重试");
			}
			redisUtil.setStr(lockKey, stores_id, 180); 
			//2验证业务员是否已经是地服 
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("salesman_id", assistant_id);
			Map<String, Object> tempSalesmanSpecialist = 
					salesmanSpecialistDao.selectSalesmanSpecialist(tempMap);
			if (tempSalesmanSpecialist == null) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员地服信息不存在");
			}
			
			//4验证门店是否可以被申请助教
			tempMap.clear();
			tempMap.put("stores_id", stores_id);
			List<MDStores> mdStoresList = StoresDao.selectMDStores(tempMap);
			MDStores mdStores = mdStoresList.get(0);
			if(mdStores.getIs_assistant_locked().equals("Y")){
				throw new BusinessException(RspCode.SALESMAN_ERROR, "申请的门店号已被锁定");
			}
			
			//5验证助教表中  门店必须唯一(除了驳回) 
			tempMap.clear();
			tempMap.put("stores_id", stores_id);
			tempMap.put("audit_status_in", new String[]{
					Constant.AUDIT_STATUS_AUDIT,Constant.AUDIT_STATUS_NON_AUDIT});
			List<Map<String, Object>> tempAssistantApplicationList = 
					assistantApplicationDao.selectAssistantApplication(tempMap);
			if (tempAssistantApplicationList.size() > 0) {
				throw new BusinessException(RspCode.SALESMAN_ERROR, "业务员助教申请门店号已存在");
			}
			
			//6.添加业务员助教信息(md_assistant_application表)
			MDAssistantApplication mdAssistantApplication = new MDAssistantApplication();
			BeanConvertUtil.mapToBean(mdAssistantApplication, paramsMap);
			mdAssistantApplication.setApplication_id(IDUtil.getUUID());
			mdAssistantApplication.setAudit_status(Constant.AUDIT_STATUS_NON_AUDIT);
			Date now = new Date();
			mdAssistantApplication.setCreated_date(now);
			mdAssistantApplication.setModified_date(now);
			assistantApplicationDao.insertAssistantApplication(mdAssistantApplication);
			//7.添加业务员助教日志信息
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("application_id", mdAssistantApplication.getApplication_id());
			logMap.put("category", "新增");
			logMap.put("tracked_date", now);
			logMap.put("event_desc", "新增业务员助教申请信息");
			assistantApplicationDao.insertAssistantApplicationLog(logMap);
			//8.修改门店信息   锁定门店
			Map<String, Object> reqParams = new HashMap<String, Object>();
			reqParams.put("stores_id", stores_id);
			reqParams.put("is_assistant_locked", "Y");
			reqParams.put("assistant_expired_date", "assistant_expired_date");
			StoresDao.updateMDStores(reqParams);
			
			//9.添加到门店日志表 
			logMap.clear();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("stores_id", stores_id);
			logMap.put("category", "添加助教申请锁定门店");
			logMap.put("tracked_date", new Date());
			logMap.put("event", "添加助教申请修改锁定门店字段为Y");
			StoresDao.insertMDStoresLog(logMap);
			
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

	@Override
	public void userFeedbackCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "desc1", "contact" });
		
		String desc1 = paramsMap.get("desc1")+"";
		String contact = paramsMap.get("contact")+"";
		
		// 2.调用接口   infrastructure_service_url 这个链接是加在member.properties文件中的  
		// 修改该配置文件需要和运维人员沟通 否则测试库上没有改动到该配置文件 
		String infrastructure_service_url = 
				PropertiesConfigUtil.getProperty("infrastructure_service_url");
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, String> bizParams = new HashMap<String, String>();
		reqParams.put("service", "infrastructure.feedback");
		bizParams.put("data_source", "SJLY_13");
		bizParams.put("category", "proposal");
		bizParams.put("desc1", desc1);
		bizParams.put("contact", contact);
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(infrastructure_service_url, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), 
					(String) resultMap.get("error_msg"));
		}
		
	}

	
	@Override
	public void systemInformListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "cre_by"});
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> mDSystemInformList = 
				systemInformDao.selectSystemInformList(paramsMap);
		for (Map<String, Object> systemInformMap : mDSystemInformList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("info_id", StringUtil.formatStr(systemInformMap.get("info_id")));
			tempMap.put("info_type", StringUtil.formatStr(systemInformMap.get("info_type")));
			tempMap.put("info_result", StringUtil.formatStr(systemInformMap.get("info_result")));
			tempMap.put("info_comment", StringUtil.formatStr(systemInformMap.get("info_comment")));
			tempMap.put("cre_by", StringUtil.formatStr(systemInformMap.get("cre_by")));
			tempMap.put("cre_date", 
					DateUtil.date2Str((Date) systemInformMap.get("cre_date"), 
							DateUtil.fmt_yyyyMMddHHmm));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data_list", resultList);
		result.setResultData(map);
	}

	@Override
	public void salesmanDataFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		Map<String, Object> tempMap = new HashMap<>();
		Map<String, Object> salesmanNum = salesmanDao.selectSalesmanNum(tempMap);
		tempMap.clear();
		Map<String, Object> salesmanMonthNum = salesmanDao.selectSalesmanMonthNum(tempMap);
		tempMap.clear();
		tempMap.put("salesman_count", salesmanNum.get("salesman_count"));
		tempMap.put("specialist_count", salesmanNum.get("specialist_count"));
		tempMap.put("driver_count", salesmanNum.get("driver_count"));
		tempMap.put("guider_count", salesmanNum.get("guider_count"));
		tempMap.put("buyer_count", salesmanNum.get("buyer_count"));
		tempMap.put("salesman_month_count", salesmanMonthNum.get("salesman_count"));
		tempMap.put("specialist_month_count", salesmanMonthNum.get("specialist_count"));
		tempMap.put("driver_month_count", salesmanMonthNum.get("driver_count"));
		tempMap.put("guider_month_count", salesmanMonthNum.get("guider_count"));
		tempMap.put("buyer_month_count", salesmanMonthNum.get("buyer_count"));
		result.setResultData(tempMap);
	}

	@Override
	public void systemInformCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "cre_by","result_type" });
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("contact_tel", StringUtil.formatStr(paramsMap.get("cre_by")));
		Map<String, Object> tempSalesman = salesmanDao.selectSalesman(tempMap);
		
		//添加系统消息信息
		MDSystemInform mdSystemInform =new MDSystemInform();
		if (paramsMap.get("result_type").toString().equals("fail")) {
			mdSystemInform.setInfo_comment("您提交的门店审核未通过。原因："+paramsMap.get("remark"));
		} else if (paramsMap.get("result_type").toString().equals("pass")) {
			mdSystemInform.setInfo_comment("您提交的门店审核已审核通过。");
		}
		mdSystemInform.setInfo_id(IDUtil.getUUID());
		mdSystemInform.setInfo_result(paramsMap.get("result_type").toString() != "fail"?"pass":"fail");
		mdSystemInform.setInfo_type("XXLX_03");
		mdSystemInform.setCre_by(StringUtil.formatStr(tempSalesman.get("salesman_id")));
		mdSystemInform.setCre_date(new Date());
		systemInformDao.insertSystemInform(mdSystemInform);
	}

	
	

	

	



	

	

	

	

}
