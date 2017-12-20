package com.meitianhui.member.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.CommonConstant;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BaiduAPIUtil;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.MoneyUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.member.constant.Constant;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.dao.AssistantDao;
import com.meitianhui.member.dao.ConsumerDao;
import com.meitianhui.member.dao.MemberDao;
import com.meitianhui.member.dao.StoresDao;
import com.meitianhui.member.dao.SupplierDao;
import com.meitianhui.member.entity.MDAssistantEvaluation;
import com.meitianhui.member.entity.MDConsumer;
import com.meitianhui.member.entity.MDMemberRecommend;
import com.meitianhui.member.entity.MDStores;
import com.meitianhui.member.entity.MDStoresServiceFee;
import com.meitianhui.member.entity.MDSupplier;
import com.meitianhui.member.entity.MDUserMember;
import com.meitianhui.member.service.MemberService;
import com.meitianhui.member.service.StoresService;

/**
 * 会员管理服务层
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
@Service
public class MemberServiceImpl implements MemberService {

	private static final Logger logger = Logger.getLogger(MemberServiceImpl.class);
	@Autowired
	public RedisUtil redisUtil;
	@Autowired
	private DocUtil docUtil;
	@Autowired
	public MemberDao memberDao;

	@Autowired
	public StoresDao storesDao;

	@Autowired
	public ConsumerDao consumerDao;

	@Autowired
	public SupplierDao supplierDao;

	@Autowired
	public AssistantDao assistantDao;

	@Autowired
	public StoresService storesService;

	@Override
	public void areaExport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			List<Map<String, Object>> areaCodeList = new ArrayList<Map<String, Object>>();
			tempMap.put("type_key", "DQLX_02");
			List<Map<String, Object>> shengList = memberDao.selectMDAreaCode(tempMap);
			for (Map<String, Object> shengMap : shengList) {
				Map<String, Object> shengAreaMap = new LinkedHashMap<String, Object>();
				String area_id = shengMap.get("area_id") + "";
				shengAreaMap.put("id", shengMap.get("area_code"));
				shengAreaMap.put("value", shengMap.get("area_name"));
				List<Map<String, Object>> childrenList = childrenCodeFind(area_id);
				if (childrenList.size() > 0) {
					shengAreaMap.put("children", childrenCodeFind(area_id));
				}
				areaCodeList.add(shengAreaMap);
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询区域的下级节点
	 * 
	 * @param parent_id
	 * @return
	 * @throws BusinessException
	 * @throws SystemException
	 */
	private List<Map<String, Object>> childrenCodeFind(String parent_id)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> areaCodeList = new ArrayList<Map<String, Object>>();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("parent_id", parent_id);
			List<Map<String, Object>> areaTempList = memberDao.selectMDAreaCode(tempMap);
			for (Map<String, Object> areaTempMap : areaTempList) {
				Map<String, Object> areaMap = new LinkedHashMap<String, Object>();
				String area_id = areaTempMap.get("area_id") + "";
				areaMap.put("id", areaTempMap.get("area_code"));
				areaMap.put("value", areaTempMap.get("area_name"));
				List<Map<String, Object>> childrenList = childrenCodeFind(area_id);
				if (childrenList.size() > 0) {
					areaMap.put("children", childrenCodeFind(area_id));
				}
				areaCodeList.add(areaMap);
			}
			return areaCodeList;
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleMemberRegisterRecommend(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "reference_type_key", "reference_id",
					"member_type_key", "member_id", "member_mobile", "member_user_id" });
			MDMemberRecommend mDMemberRecommend = new MDMemberRecommend();
			BeanConvertUtil.mapToBean(mDMemberRecommend, paramsMap);
			mDMemberRecommend.setRecommend_id(IDUtil.getUUID());
			mDMemberRecommend.setCreated_date(new Date());
			memberDao.insertMDMemberRecommend(mDMemberRecommend);

			// 如果推荐门店是门店,注册的会员是消费者,则保存店东与消费者的关联关系
			String stores_type_key = paramsMap.get("reference_type_key") + "";
			String consuemr_type_key = paramsMap.get("member_type_key") + "";
			if (stores_type_key.equals(Constant.MEMBER_TYPE_STORES)
					&& consuemr_type_key.equals(Constant.MEMBER_TYPE_CONSUMER)) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("consumer_id", paramsMap.get("member_id"));
				tempMap.put("stores_id", paramsMap.get("reference_id"));
				storesService.storesMemberRelCreate(tempMap, new ResultData());
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleCustomerRecommendMemberRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "mobile", "member_id" });
		// 根据推荐人手机号查出user_id, mobile 推荐人手机号 ，member_id 会员标示，即被推荐人标识
		Map<String, Object> resultMap = null;
		String infrastructure_service_url = PropertiesConfigUtil.getProperty("infrastructure_service_url");
		Map<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("service", "infrastructure.memberTypeFindByMobile");
		reqParams.put("params", FastJsonUtil.toJson(paramsMap));
		String resultStr = HttpClientUtil.post(infrastructure_service_url, reqParams);
		resultMap = FastJsonUtil.jsonToMap(resultStr);
		String rsp_code = (String) resultMap.get("rsp_code");
		if (rsp_code.equals(RspCode.RESPONSE_SUCC)) {
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			String user_id = dateMap.get("user_id").toString();
			// paramsMap.clear();
			paramsMap.put("user_id", user_id);
			paramsMap.put("member_type_key", CommonConstant.MEMBER_TYPE_CONSUMER);
			// 根据user_id查出member_id
			List<MDUserMember> list = memberDao.selectMDUserMember(paramsMap);
			if (CollectionUtils.isNotEmpty(list)) {
				String consumer_id = list.get(0).getMember_id();
				// paramsMap.clear();
				paramsMap.put("consumer_id", consumer_id);
				// 根据id查询是否注册过消费者
				List<MDConsumer> consumers = consumerDao.selectMDConsumerById(paramsMap);
				if (CollectionUtils.isNotEmpty(consumers)) {
					// 绑定推荐关系
					MDMemberRecommend recommend = new MDMemberRecommend();
					recommend.setRecommend_id(IDUtil.getUUID());
					recommend.setReference_type_key(CommonConstant.MEMBER_TYPE_CONSUMER);
					recommend.setMember_type_key(CommonConstant.MEMBER_TYPE_CONSUMER);
					recommend.setMember_id(paramsMap.get("member_id").toString());
					recommend.setMember_mobile(consumers.get(0).getMobile()); // 会员手机号
					recommend.setMember_user_id(list.get(0).getUser_id());
					memberDao.insertMDMemberRecommend(recommend);
				}
			}
		}
	}

	@Override
	public void memberRegisterRecommendFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "reference_type_key", "reference_id" });
			List<Map<String, Object>> memberRecommendList = memberDao.selectMDMemberRecommend(paramsMap);
			for (Map<String, Object> m : memberRecommendList) {
				m.put("created_date", DateUtil.date2Str((Date) m.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
			}
			result.setResultData(memberRecommendList);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Override
	public void memberRegisterRecommendFindByMemberId(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
		List<Map<String, Object>> memberRecommendList = memberDao.selectMDMemberRecommend(paramsMap);
		for (Map<String, Object> m : memberRecommendList) {
			m.put("reference_id", m.get("reference_id"));
		}
		result.setResultData(memberRecommendList);
	}

	@Override
	public void memberRegisterRecommendFindIsConsumer(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_mobile" });
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> memberRecommendList = memberDao.selectMDMemberRecommend(paramsMap);
		if (CollectionUtils.isNotEmpty(memberRecommendList)) {
			resultMap.put("reference_type_key", memberRecommendList.get(0).get("reference_type_key"));
		}
		result.setResultData(memberRecommendList);
	}

	@Override
	public void memberRegisterRecommendCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "reference_type_key", "reference_id" });
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("count_num", "0");
			Map<String, Object> countMap = memberDao.selectMDMemberRecommendCount(paramsMap);
			if (null != countMap) {
				resultMap.put("count_num", countMap.get("count_num") + "");
			}
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
	public void recommendmemberAsssetList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "reference_type_key", "reference_id" });
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> bizParams = new HashMap<String, String>();
			List<Map<String, Object>> memberRecommendList = memberDao.selectMDMemberRecommend(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> m : memberRecommendList) {
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("member_mobile", m.get("member_mobile"));
				// 查询资产
				reqParams.clear();
				bizParams.clear();
				reqParams.put("service", "finance.memberAssetFind");
				bizParams.put("member_id", (String) m.get("member_id"));
				bizParams.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
				Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				Map<String, Object> assetMap = (Map<String, Object>) resultMap.get("data");
				temp.put("cash_balance", assetMap.get("cash_balance") + "");
				temp.put("gold", assetMap.get("gold") + "");
				resultList.add(temp);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("list", resultList);
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
	public void memberInfoFindByMobile(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "mobile", "member_type_key" });
			Map<String, Object> resultDate = new HashMap<String, Object>();
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();

			String mobile = paramsMap.get("mobile") + "";
			String member_type_key = paramsMap.get("member_type_key") + "";
			// 增加缓存
			String cacheKey = "[memberInfoFindByMobile]_" + mobile + "_" + member_type_key;
			Object obj = redisUtil.getObj(cacheKey);
			if (null != obj) {
				resultDate = (Map<String, Object>) obj;
				result.setResultData(resultDate);
				return;
			}

			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			reqParams.put("service", "infrastructure.userFindByMobile");
			bizParams.put("mobile", mobile);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			String user_id = (String) dateMap.get("user_id");
			if (user_id.equals("")) {
				throw new BusinessException(RspCode.USER_NOT_EXIST, mobile + RspCode.MSG.get(RspCode.USER_NOT_EXIST));
			}
			bizParams.clear();
			bizParams.put("user_id", user_id);
			bizParams.put("member_type_key", member_type_key);
			List<MDUserMember> mDUserMemberList = memberDao.selectMDUserMember(bizParams);
			if (mDUserMemberList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			String member_id = mDUserMemberList.get(0).getMember_id();
			if (member_type_key.equals(Constant.MEMBER_TYPE_CONSUMER)) {
				// 消费者
				bizParams.clear();
				bizParams.put("consumer_id", member_id);
				MDConsumer mDConsumer = consumerDao.selectMDConsumerBaseInfo(bizParams);
				if (null != mDConsumer) {
					resultDate.put("consumer_id", mDConsumer.getConsumer_id());
					resultDate.put("nick_name", StringUtil.formatStr(mDConsumer.getNick_name()));
					resultDate.put("mobile", mobile);
					resultDate.put("grade", mDConsumer.getGrade() + "");
					resultDate.put("head_pic_path", mDConsumer.getHead_pic_path() + "");
					resultDate.put("address", StringUtil.formatStr(mDConsumer.getAddress()));
				} else {
					throw new BusinessException(RspCode.MEMBER_NOT_EXIST,
							mobile + RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
				}
			} else if (member_type_key.equals(Constant.MEMBER_TYPE_STORES)) {
				// 店东
				bizParams.clear();
				bizParams.put("stores_id", member_id);
				List<MDStores> mDStoresList = storesDao.selectNormalStores(bizParams);
				if (mDStoresList.size() == 0) {
					throw new BusinessException(RspCode.MEMBER_NOT_EXIST,
							mobile + RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
				}
				MDStores mDStores = mDStoresList.get(0);
				resultDate.put("stores_id", mDStores.getStores_id());
				resultDate.put("stores_no", mDStores.getStores_no());
				resultDate.put("mobile", mobile);
				resultDate.put("area", mDStores.getArea_id());
				resultDate.put("address", mDStores.getAddress());
				resultDate.put("stores_name", mDStores.getStores_name());
				resultDate.put("contact_person", mDStores.getContact_person());
				resultDate.put("stores_type_key", mDStores.getStores_type_key());
				resultDate.put("business_type_key", mDStores.getBusiness_type_key());
				resultDate.put("business_status_key", mDStores.getBusiness_status_key());
				resultDate.put("longitude", mDStores.getLongitude() + "");
				resultDate.put("latitude", mDStores.getLatitude() + "");
			} else if (member_type_key.equals(Constant.MEMBER_TYPE_SUPPLIER)) {
				// 供应商
				bizParams.clear();
				bizParams.put("supplier_id", member_id);
				List<MDSupplier> supplierList = supplierDao.selectMDSupplier(bizParams);
				if (supplierList.size() == 0) {
					throw new BusinessException(RspCode.MEMBER_NOT_EXIST,
							mobile + RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
				}
				MDSupplier supplier = supplierList.get(0);
				resultDate.put("supplier_id", supplier.getSupplier_id());
				resultDate.put("supplier_no", supplier.getSupplier_no());
				resultDate.put("supplier_name", supplier.getSupplier_name());
			}
			// 缓存数据，有效时间为24小时
			redisUtil.setObj(cacheKey, resultDate, 86400);
			result.setResultData(resultDate);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberInfoFindByMemberId(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			Map<String, Object> resultDate = new HashMap<String, Object>();
			String member_id = paramsMap.get("member_id") + "";
			String member_type_key = paramsMap.get("member_type_key") + "";

			// 增加缓存
			String cacheKey = "[memberInfoFindByMemberId]_" + member_id + "_" + member_type_key;
			Object obj = redisUtil.getObj(cacheKey);
			if (null != obj) {
				resultDate = (Map<String, Object>) obj;
				result.setResultData(resultDate);
				return;
			}

			// 返回的数据
			Map<String, Object> bizParams = new HashMap<String, Object>();
			if (member_type_key.equals(Constant.MEMBER_TYPE_CONSUMER)) {
				// 消费者
				bizParams.clear();
				bizParams.put("consumer_id", member_id);
				MDConsumer mDConsumer = consumerDao.selectMDConsumerBaseInfo(bizParams);
				if (null == mDConsumer) {
					throw new BusinessException(RspCode.MEMBER_NOT_EXIST, "消费者【" + member_id + "】不存在");
				}
				resultDate.put("consumer_id", mDConsumer.getConsumer_id());
				resultDate.put("nick_name", StringUtil.formatStr(mDConsumer.getNick_name()));
				resultDate.put("mobile", StringUtil.formatStr(mDConsumer.getMobile()));
			} else if (member_type_key.equals(Constant.MEMBER_TYPE_STORES)) {
				// 店东
				bizParams.clear();
				bizParams.put("stores_id", member_id);
				List<MDStores> mDStoresList = storesDao.selectNormalStores(bizParams);
				if (mDStoresList.size() == 0) {
					throw new BusinessException(RspCode.MEMBER_NOT_EXIST, "门店【" + member_id + "】不存在");
				}
				MDStores mDStores = mDStoresList.get(0);
				resultDate.put("stores_id", mDStores.getStores_id());
				resultDate.put("contact_tel", mDStores.getContact_tel());
				resultDate.put("stores_name", mDStores.getStores_name());
				resultDate.put("contact_person", mDStores.getContact_person());
			} else if (member_type_key.equals(Constant.MEMBER_TYPE_SUPPLIER)) {
				// 供应商
				bizParams.clear();
				bizParams.put("supplier_id", member_id);
				List<MDSupplier> supplierList = supplierDao.selectMDSupplier(bizParams);
				if (supplierList.size() == 0) {
					throw new BusinessException(RspCode.MEMBER_NOT_EXIST, "供应商【" + member_id + "】不存在");
				}
				MDSupplier supplier = supplierList.get(0);
				resultDate.put("supplier_id", supplier.getSupplier_id());
				resultDate.put("contact_person", StringUtil.formatStr(supplier.getContact_person()));
				resultDate.put("contact_tel", StringUtil.formatStr(supplier.getContact_tel()));
				resultDate.put("supplier_name", StringUtil.formatStr(supplier.getSupplier_name()));
			}
			// 缓存数据，有效时间为24小时
			redisUtil.setObj(cacheKey, resultDate, 86400);
			result.setResultData(resultDate);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberIdFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "user_id", "member_type_key" });
			List<MDUserMember> mDUserMemberList = memberDao.selectMDUserMember(paramsMap);
			if (mDUserMemberList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDUserMember mDUserMember = mDUserMemberList.get(0);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("member_id", mDUserMember.getMember_id());
			resultMap.put("is_admin", mDUserMember.getIs_admin());
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
	public void memberTypeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "user_id" });
			List<MDUserMember> mDUserMemberList = memberDao.selectMDUserMember(paramsMap);
			String member_type = "";
			for (MDUserMember mDUserMember : mDUserMemberList) {
				member_type = member_type + "," + mDUserMember.getMember_type_key();
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (member_type.trim().length() > 0) {
				member_type = member_type.substring(1);
			}
			resultMap.put("member_type", member_type);
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
	public void userIdFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			List<MDUserMember> mDUserMemberList = memberDao.selectMDUserMember(paramsMap);
			if (mDUserMemberList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDUserMember mDUserMember = mDUserMemberList.get(0);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("user_id", mDUserMember.getUser_id());
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
	public void userInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			List<MDUserMember> mDUserMemberList = memberDao.selectMDUserMember(paramsMap);
			if (mDUserMemberList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDUserMember mDUserMember = mDUserMemberList.get(0);
			/**
			 * 查询用户信息
			 */
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> bizParams = new HashMap<String, String>();
			reqParams.put("service", "infrastructure.userFind");
			bizParams.put("user_id", mDUserMember.getUser_id());
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> userInfoMap = (Map<String, Object>) resultMap.get("data");
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("user_id", mDUserMember.getUser_id());
			tempMap.put("mobile", StringUtil.formatStr(userInfoMap.get("mobile")));
			result.setResultData(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storesRelConsumerList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String stores_id = paramsMap.get("stores_id") + "";
			// 文档id
			List<String> doc_ids = new ArrayList<String>();
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> consumerIdList = storesRelConsumerIdList(stores_id);
			if (consumerIdList.size() > 0) {
				tempMap.clear();
				tempMap.put("consumer_id_in", consumerIdList);
				List<MDConsumer> consumerList = consumerDao.selectMDConsumer(tempMap);
				// 返回的list数据
				for (MDConsumer mDConsumer : consumerList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("consumer_id", mDConsumer.getConsumer_id());
					map.put("nick_name", StringUtil.formatStr(mDConsumer.getNick_name()));
					map.put("head_pic_path", StringUtil.formatStr(mDConsumer.getHead_pic_path()));
					map.put("mobile", StringUtil.formatStr(mDConsumer.getMobile()));
					doc_ids.add(StringUtil.formatStr(mDConsumer.getHead_pic_path()));
					resultList.add(map);
				}
			}
			tempMap.clear();
			tempMap.put("doc_url", docUtil.imageUrlFind(doc_ids));
			tempMap.put("list", resultList);
			result.setResultData(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void storesRelConsumerAssetList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			String stores_id = paramsMap.get("stores_id") + "";
			List<String> consumerIdList = storesRelConsumerIdList(stores_id);
			if (consumerIdList.size() > 0) {
				Map<String, String> reqParams = new HashMap<String, String>();
				Map<String, Object> bizParams = new HashMap<String, Object>();
				if (consumerIdList.size() > 0) {
					tempMap.clear();
					tempMap.put("consumer_id_in", consumerIdList);
					List<MDConsumer> consumerList = consumerDao.selectMDConsumer(tempMap);
					// 返回的list数据
					for (MDConsumer mDConsumer : consumerList) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("consumer_id", mDConsumer.getConsumer_id());
						map.put("mobile", StringUtil.formatStr(mDConsumer.getMobile()));
						map.put("nick_name", StringUtil.formatStr(mDConsumer.getNick_name()));
						resultList.add(map);
					}
				}
				// 查询会员资产信息
				reqParams.clear();
				bizParams.clear();
				reqParams.put("service", "finance.memberAssetListFind");
				bizParams.put("member_id", StringUtil.list2Str(consumerIdList));
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
				Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
					List<Map<String, Object>> list = (List<Map<String, Object>>) dataMap.get("list");
					for (Map<String, Object> tm : list) {
						String member_id = tm.get("member_id") + "";
						for (Map<String, Object> map : resultList) {
							String consumer_id = map.get("consumer_id") + "";
							if (member_id.equals(consumer_id)) {
								map.put("cash_balance", tm.get("cash_balance") + "");
								map.put("gold", tm.get("gold") + "");
								break;
							}
						}
					}
				}
			}
			tempMap.clear();
			tempMap.put("list", resultList);
			result.setResultData(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询和店东有关系的会员
	 * 
	 * @param stores_id
	 * @return
	 */
	private List<String> storesRelConsumerIdList(String stores_id) {
		List<String> consumerIdList = new ArrayList<String>();
		try {
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
			Map<String, Object> resultMap = new HashMap<String, Object>();
			// 消费者列表
			Set<String> consumerSet = new HashSet<String>();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 推荐注册
			tempMap.put("reference_type_key", "stores");
			tempMap.put("reference_id", stores_id);
			List<Map<String, Object>> recommendList = memberDao.selectMDMemberRecommend(tempMap);
			for (Map<String, Object> map : recommendList) {
				consumerSet.add(map.get("member_id") + "");
			}
			// 绑定亲情卡记录
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			reqParams.put("service", "finance.storesActivatePrepayCardFind");
			bizParams.put("stores_id", stores_id);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
				List<Map<String, Object>> list = (List<Map<String, Object>>) dataMap.get("list");
				for (Map<String, Object> tm : list) {
					consumerSet.add(tm.get("member_id") + "");
				}
			}
			// 查询店东领了么推荐
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "order.storesRecomConsumerFreeGetListFindForMemberList");
			bizParams.put("stores_id", stores_id);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(order_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
				List<Map<String, Object>> list = (List<Map<String, Object>>) dataMap.get("list");
				for (Map<String, Object> tm : list) {
					consumerSet.add(tm.get("member_id") + "");
				}
			}
			// 余额交易记录
			resultMap.clear();
			reqParams.clear();
			bizParams.clear();
			reqParams.put("service", "finance.tradeConsumerListForMemberList");
			bizParams.put("stores_id", stores_id);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
				List<Map<String, Object>> list = (List<Map<String, Object>>) dataMap.get("list");
				for (Map<String, Object> tm : list) {
					consumerSet.add(tm.get("member_id") + "");
				}
			}
			consumerIdList.addAll(consumerSet);
		} catch (BusinessException e) {
			logger.info(e.getMsg());
		} catch (SystemException e) {
			logger.error(e.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return consumerIdList;
	}

	@Override
	public void storesServiceFeeListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>();
			List<MDStoresServiceFee> list = memberDao.selectMDStoresServiceFee(paramsMap);
			for (MDStoresServiceFee storesServiceFee : list) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("date_id", storesServiceFee.getDate_id());
				tempMap.put("stores_id", storesServiceFee.getStores_id());
				tempMap.put("cash", storesServiceFee.getCash() + "");
				tempMap.put("gold", storesServiceFee.getGold() + "");
				tempMap.put("remark", StringUtil.formatStr(storesServiceFee.getRemark()));
				resultlist.add(tempMap);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("list", resultlist);
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 创建店东助手等级评估
	 * 
	 * @param paramsMap
	 */
	@Override
	public void assistantEvaluationCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "assistant_id", "stores_id", "evaluation_by",
					"criteria_val1", "criteria_val2", "criteria_val3", "criteria_val4", "criteria_val5" });
			String criteria_val1 = paramsMap.get("criteria_val1") + "";
			if (criteria_val1.length() == 4) {
				paramsMap.put("criteria_val1", criteria_val1.substring(3));
			}
			String criteria_val2 = paramsMap.get("criteria_val2") + "";
			if (criteria_val2.length() == 4) {
				paramsMap.put("criteria_val2", criteria_val2.substring(3));
			}
			String criteria_val3 = paramsMap.get("criteria_val3") + "";
			if (criteria_val3.length() == 4) {
				paramsMap.put("criteria_val3", criteria_val3.substring(3));
			}
			String criteria_val4 = paramsMap.get("criteria_val4") + "";
			if (criteria_val4.length() == 4) {
				paramsMap.put("criteria_val4", criteria_val4.substring(3));
			}
			String criteria_val5 = paramsMap.get("criteria_val5") + "";
			if (criteria_val5.length() == 4) {
				paramsMap.put("criteria_val5", criteria_val5.substring(3));
			}

			Date date = new Date();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("stores_id", paramsMap.get("stores_id"));
			tempMap.put("assistant_id", paramsMap.get("assistant_id"));
			tempMap.put("evaluation_date", DateUtil.getFormatDate(DateUtil.fmt_yyyyMM));
			List<MDAssistantEvaluation> list = memberDao.selectMDAssistantEvaluation(tempMap);
			if (list.size() > 0) {
				throw new BusinessException(RspCode.MEMBER_EVALUATION_ERROR, "本月您完成评价,请勿重复评价");
			}
			MDAssistantEvaluation mdAssistantEvaluation = new MDAssistantEvaluation();
			BeanConvertUtil.mapToBean(mdAssistantEvaluation, paramsMap);
			mdAssistantEvaluation.setEvaluation_id(IDUtil.getUUID());
			mdAssistantEvaluation.setCriteria_item1("响应及时");
			mdAssistantEvaluation.setCriteria_item2("专业技能");
			mdAssistantEvaluation.setCriteria_item3("品行端正");
			mdAssistantEvaluation.setCriteria_item4("积极主动");
			mdAssistantEvaluation.setCriteria_item5("工作成果");

			mdAssistantEvaluation.setEvaluation_date(date);
			mdAssistantEvaluation.setCreated_date(date);
			mdAssistantEvaluation.setModified_date(date);
			Integer total_numeric_val = 0;
			total_numeric_val = mdAssistantEvaluation.getCriteria_val1() + mdAssistantEvaluation.getCriteria_val2()
					+ mdAssistantEvaluation.getCriteria_val3() + mdAssistantEvaluation.getCriteria_val4()
					+ mdAssistantEvaluation.getCriteria_val5();
			mdAssistantEvaluation.setTotal_numeric_val(total_numeric_val);

			memberDao.insertMDAssistantEvaluation(mdAssistantEvaluation);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 创建店东助手等级评估
	 * 
	 * @param paramsMap
	 */
	@Override
	public void assistantEvaluationCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "assistant_id" });
			paramsMap.put("evaluation_date", DateUtil.getFormatDate(DateUtil.fmt_yyyyMM));
			Map<String, Object> qrymap = memberDao.selectMDAssistantEvaluationCount(paramsMap);
			if (qrymap != null) {
				BigDecimal cou_num = new BigDecimal(qrymap.get("cou_num") + "");
				qrymap.put("criteria_val1_sum",
						MoneyUtil.moneydiv(new BigDecimal(qrymap.get("criteria_val1_sum") + ""), cou_num).intValue()
								+ "");
				qrymap.put("criteria_val2_sum",
						MoneyUtil.moneydiv(new BigDecimal(qrymap.get("criteria_val2_sum") + ""), cou_num).intValue()
								+ "");
				qrymap.put("criteria_val3_sum",
						MoneyUtil.moneydiv(new BigDecimal(qrymap.get("criteria_val3_sum") + ""), cou_num).intValue()
								+ "");
				qrymap.put("criteria_val4_sum",
						MoneyUtil.moneydiv(new BigDecimal(qrymap.get("criteria_val4_sum") + ""), cou_num).intValue()
								+ "");
				qrymap.put("criteria_val5_sum",
						MoneyUtil.moneydiv(new BigDecimal(qrymap.get("criteria_val5_sum") + ""), cou_num).intValue()
								+ "");
			} else {
				qrymap = new HashMap<String, Object>();
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("detail", qrymap);
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
	public void handleAreaCodeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			String areaCode = "440304";
			ValidateUtil.validateParams(paramsMap, new String[] { "longitude", "latitude" });
			try {
				Map<String, Object> loaclInfo = BaiduAPIUtil.getLocationInfo(paramsMap.get("latitude") + "",
						paramsMap.get("longitude") + "");
				String adcode = StringUtil.formatStr(loaclInfo.get("adcode"));
				// 区域编码是6位数
				if (adcode.length() == 6) {
					areaCode = adcode;
				}
			} catch (Exception e) {
				logger.error("定位失败,经纬度信息:" + paramsMap.toString(), e);
			}
			// 记录定位信息
			String consumer_id = StringUtil.formatStr(paramsMap.get("consumer_id"));
			if (!StringUtils.isEmpty(consumer_id)) {
				// 记录消费者的经纬度信息

				paramsMap.put("location_id", IDUtil.getUUID());
				paramsMap.put("area_id", areaCode);
				paramsMap.put("created_date", new Date());
				consumerDao.insertMDConsumerLocation(paramsMap);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("areaCode", areaCode);
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 解除用户和会员之间的关系
	 * 
	 * @param paramsMap
	 */
	@Override
	public void userMemberRelRemove(Map<String, Object> paramsMap) {
		try {
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> bizParams = new HashMap<String, String>();
			List<MDUserMember> userMemberList = memberDao.selectMDUserMember(paramsMap);
			for (MDUserMember um : userMemberList) {
				// 删除关联关系
				bizParams.clear();
				bizParams.put("member_id", um.getMember_id());
				bizParams.put("user_id", um.getUser_id());
				memberDao.deleteMDUserMember(paramsMap);
				// 删除授权token信息
				reqParams.clear();
				bizParams.clear();
				reqParams.put("service", "infrastructure.userTokenClear");
				bizParams.put("member_id", um.getMember_id());
				bizParams.put("user_id", um.getUser_id());
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				HttpClientUtil.postShort(user_service_url, reqParams);
				// 记录日志
				if (um.getMember_type_key().equals(Constant.MEMBER_TYPE_STORES)) {
					Map<String, Object> logMap = new HashMap<String, Object>();
					logMap.put("log_id", IDUtil.getUUID());
					logMap.put("stores_id", um.getMember_id());
					logMap.put("category", "编辑");
					logMap.put("tracked_date", new Date());
					logMap.put("event", "解除和用户" + um.getUser_id() + "之间的关系");
					storesDao.insertMDStoresLog(logMap);
				}
			}
		} catch (Exception e) {
			logger.error("授权信息清除异常", e);
		}
	}

	private void SMSNotify(final String mobile, final String msg) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> reqParams = new HashMap<String, String>();
					Map<String, String> paramMap = new HashMap<String, String>();
					String notification_service_url = PropertiesConfigUtil.getProperty("notification_service_url");
					paramMap.clear();
					reqParams.clear();
					paramMap.put("sms_source", Constant.DATA_SOURCE_SJLY_03);
					paramMap.put("mobiles", mobile);
					paramMap.put("msg", msg);
					reqParams.put("service", "notification.SMSSend");
					reqParams.put("params", FastJsonUtil.toJson(paramMap));
					HttpClientUtil.post(notification_service_url, reqParams);
				} catch (SystemException e) {
					logger.error("发送短信通知异常", e);
				} catch (Exception e) {
					logger.error("发送短信通知异常", e);
				}
			}
		}).start();
	}

	@Override
	public void handleStoreFeedback(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "device_type", "stores_name", "contact_person", "mobile", "old_account" });
			paramsMap.put("feedback_id", IDUtil.getUUID());
			paramsMap.put("created_date", new Date());
			memberDao.insertMDStoreFeedback(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void appMsgNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "receiver", "message" });
			String member_id = (String) paramsMap.get("receiver");
			String key = Constant.APP_NOTIFY + "_" + member_id;
			redisUtil.setList(key, (String) paramsMap.get("message"));
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void appMsgNotifyFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			String member_id = (String) paramsMap.get("member_id");
			String key = Constant.APP_NOTIFY + "_" + member_id;
			List<String> msgList = redisUtil.getList(key);
			redisUtil.del(key);
			result.setResultData(msgList);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void memberUserFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("member_id", paramsMap.get("member_id"));
			tempMap.put("member_type_key", paramsMap.get("member_type_key"));
			List<MDUserMember> userMemberList = memberDao.selectMDUserMember(tempMap);
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> bizParams = new HashMap<String, String>();
			for (MDUserMember um : userMemberList) {
				Map<String, Object> map = new HashMap<String, Object>();
				String user_id = um.getUser_id();
				reqParams.clear();
				bizParams.clear();
				reqParams.put("service", "infrastructure.userFind");
				bizParams.put("user_id", user_id);
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
				Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
					map.put("user_id", um.getUser_id());
					map.put("mobile", (String) dateMap.get("mobile"));
					map.put("user_name", (String) dateMap.get("user_name"));
					map.put("email", (String) dateMap.get("email"));
					map.put("registered_date", (String) dateMap.get("registered_date"));
					map.put("is_admin", um.getIs_admin());
					resultList.add(map);
				}
			}
			result.setResultData(resultList);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleMemberUserRemove(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "mobile" });
			String member_type_key = paramsMap.get("member_type_key") + "";
			String member_id = paramsMap.get("member_id") + "";
			// 清楚缓存数据
			if (member_type_key.equals(Constant.MEMBER_TYPE_STORES)) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("stores_id", member_id);
				List<MDStores> mDStoresList = storesDao.selectMDStores(tempMap);
				// 清楚缓存数据
				clearCacheMemberInfo(member_id, mDStoresList.get(0).getContact_tel(), member_type_key);
			}
			userMemberRelRemove(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleMemberUserCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "member_id", "member_type_key", "mobile", "user_name", "is_admin" });
			String member_id = paramsMap.get("member_id") + "";
			String member_type_key = paramsMap.get("member_type_key") + "";
			String mobile = paramsMap.get("mobile") + "";
			// 检查会员是否存在
			Map<String, Object> tempMap = new HashMap<String, Object>();
			if (member_type_key.equals(Constant.MEMBER_TYPE_STORES)) {
				tempMap.put("stores_id", member_id);
				List<MDStores> storesList = storesDao.selectMDStores(tempMap);
				if (storesList.size() == 0) {
					throw new BusinessException(RspCode.MEMBER_NOT_EXIST, "门店不存在");
				}
			} else if (member_type_key.equals(Constant.MEMBER_TYPE_SUPPLIER)) {
				tempMap.put("supplier_id", member_id);
				List<MDSupplier> supplierList = supplierDao.selectMDSupplier(tempMap);
				if (supplierList.size() == 0) {
					throw new BusinessException(RspCode.MEMBER_NOT_EXIST, "供应商不存在");
				}
			}
			String is_admin = paramsMap.get("is_admin") + "";
			// 如果创建的是管理员账号,则需要校验会员对应的管理员账号是否存在
			if (is_admin.equals("Y")) {
				tempMap.clear();
				tempMap.put("member_id", member_id);
				tempMap.put("member_type_key", Constant.MEMBER_TYPE_STORES);
				tempMap.put("is_admin", is_admin);
				List<MDUserMember> list = memberDao.selectMDUserMember(tempMap);
				if (list.size() > 0) {
					throw new BusinessException(RspCode.MEMBER_ADMIN_EXIST, "会员已存在管理用员账号");
				}
			}

			/**
			 * 检查手机号是否已经注册成了门店， 如果手机号已经成为用户，但是不是门店,则获取用户id,
			 * 如果手机号不存在,则直接注册手机号,并获取用户id
			 */
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> bizParams = new HashMap<String, String>();
			reqParams.put("service", "infrastructure.syncUserRegister");
			bizParams.put("mobile", mobile);
			bizParams.put("user_name", (String) paramsMap.get("user_name"));
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			String user_id = (String) dateMap.get("user_id");

			// 解除手机号对应的用户绑定关系
			tempMap.clear();
			tempMap.put("user_id", user_id);
			tempMap.put("member_type_key", member_type_key);
			userMemberRelRemove(tempMap);

			MDUserMember mDUserMember = new MDUserMember();
			mDUserMember.setIs_admin(is_admin);
			mDUserMember.setMember_id(member_id);
			mDUserMember.setUser_id(user_id);
			mDUserMember.setMember_type_key(member_type_key);
			memberDao.insertMDUserMember(mDUserMember);
			if (member_type_key.equals(Constant.MEMBER_TYPE_STORES)) {
				Map<String, Object> logMap = new HashMap<String, Object>();
				logMap.put("log_id", IDUtil.getUUID());
				logMap.put("stores_id", member_id);
				logMap.put("category", "编辑");
				logMap.put("tracked_date", new Date());
				logMap.put("event", "创建和用户" + user_id + "之间的关系");
				storesDao.insertMDStoresLog(logMap);
				// 更新门店的账号信息
				if (is_admin.equals("Y")) {
					tempMap.clear();
					tempMap.put("stores_id", member_id);
					tempMap.put("contact_tel", mobile);
					storesDao.updateMDStoresSync(tempMap);
					// 清除缓存数据
					clearCacheMemberInfo(member_id, mobile, Constant.MEMBER_TYPE_STORES);
				}
				SMSNotify((String) paramsMap.get("mobile"), "尊敬的每天惠用户,我们为您生成了新的店东助手登录账号【"
						+ (String) paramsMap.get("mobile") + "】,如有任何问题欢迎继续通过用户反馈功能联系我们,谢谢合作。");
			} else if (member_type_key.equals(Constant.MEMBER_TYPE_SUPPLIER)) {
				Map<String, Object> logMap = new HashMap<String, Object>();
				logMap.put("log_id", IDUtil.getUUID());
				logMap.put("supplier_id", member_id);
				logMap.put("category", "编辑");
				logMap.put("tracked_date", new Date());
				logMap.put("event", "创建和用户" + user_id + "之间的关系");
				supplierDao.insertMDSupplierLog(logMap);
				// 更新门店的账号信息
				if (is_admin.equals("Y")) {
					tempMap.clear();
					tempMap.put("supplier_id", member_id);
					tempMap.put("contact_tel", mobile);
					supplierDao.updateMDSupplierSync(tempMap);
					// 清除缓存数据
					clearCacheMemberInfo(member_id, mobile, Constant.MEMBER_TYPE_STORES);
				}
				SMSNotify(mobile, "尊敬的每天惠用户,我们为您生成了新的登录账号【" + mobile + "】,如有任何问题欢迎继续通过用户反馈功能联系我们,谢谢合作。");
			}
			tempMap.clear();
			reqParams.clear();
			bizParams.clear();
			tempMap = null;
			reqParams = null;
			bizParams = null;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 清楚缓存信息
	 * 
	 * @param member_id
	 * @param member_type_key
	 */
	public void clearCacheMemberInfo(String member_id, String mobile, String member_type_key) throws Exception {
		try {
			// 清楚通过会员id获取会员信息缓存
			redisUtil.del("[memberInfoFindByMemberId]_" + member_id + "_" + member_type_key);
			// 清楚通过手机号获取会员信息缓存
			redisUtil.del("[memberInfoFindByMobile]_" + mobile + "_" + member_type_key);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 订单退款
	 * 
	 * @Title: refundOrder
	 * @param member_id
	 * @param amount
	 * @param order_no
	 * @author tiny
	 */
	@Override
	public void balancePay(String buyer_id, String seller_id, String payment_way_key, BigDecimal amount,
			String out_trade_no, String detail, Map<String, Object> out_trade_body) throws Exception {
		String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "finance.balancePay");
		bizParams.put("data_source", "SJLY_03");
		bizParams.put("buyer_id", buyer_id);
		bizParams.put("seller_id", seller_id);
		bizParams.put("payment_way_key", payment_way_key);
		bizParams.put("amount", amount + "");
		bizParams.put("detail", detail);
		bizParams.put("out_trade_no", out_trade_no);
		bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
	}

}
