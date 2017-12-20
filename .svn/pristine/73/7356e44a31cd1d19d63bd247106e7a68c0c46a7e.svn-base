package com.meitianhui.member.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.CommonRspCode;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.member.constant.Constant;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.dao.ConsumerDao;
import com.meitianhui.member.dao.MemberDao;
import com.meitianhui.member.dao.StoresDao;
import com.meitianhui.member.dao.StoresMemberRelDao;
import com.meitianhui.member.entity.MDConsumer;
import com.meitianhui.member.entity.MDStores;
import com.meitianhui.member.entity.MDStoresActivityPromotion;
import com.meitianhui.member.entity.MDStoresMemberRel;
import com.meitianhui.member.entity.MDStoresRecommend;
import com.meitianhui.member.entity.MDUserMember;
import com.meitianhui.member.service.MemberService;
import com.meitianhui.member.service.StoresService;

/**
 * 门店管理
 * 
 * @ClassName: StoresServiceImpl
 * @author tiny
 * @date 2017年2月23日 下午3:18:18
 *
 */
@SuppressWarnings("unchecked")
@Service
public class StoresServiceImpl implements StoresService {

	@Autowired
	private DocUtil docUtil;

	@Autowired
	public StoresDao storesDao;

	@Autowired
	public MemberDao memberDao;

	@Autowired
	public StoresMemberRelDao storesMemberListDao;

	@Autowired
	public MemberService memberService;

	@Autowired
	public RedisUtil redisUtil;
	
	@Autowired
	public ConsumerDao consumerDao;

	@Override
	public void handleStoreSyncRegister(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "stores_id", "stores_no", "stores_name", "contact_person", "contact_tel",
							"stores_type_key", "area_id", "address", "sys_status", "registered_date" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String member_id = (String) paramsMap.get("stores_id");
			tempMap.put("member_id", member_id);
			List<MDStores> mDStoresList = storesDao.selectMDStores(tempMap);
			if (mDStoresList.size() > 0) {
				throw new BusinessException(RspCode.MEMBER_EXIST, RspCode.MSG.get(RspCode.MEMBER_EXIST));
			}
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> bizParams = new HashMap<String, String>();

			/**
			 * 检查手机号是否已经注册成了门店， 如果手机号已经成为用户，但是不是门店,则获取用户id,
			 * 如果手机号不存在,则直接注册手机号,并获取用户id 如果门店类型存在,则直接抛出异常
			 */
			reqParams.put("service", "infrastructure.memberTypeValidateByMobile");
			bizParams.put("mobile", (String) paramsMap.get("contact_tel"));
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
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
				bizParams.put("mobile", (String) paramsMap.get("contact_tel"));
				bizParams.put("user_name", (String) paramsMap.get("contact_person"));
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
			// 新增门店
			MDStores mDStores = new MDStores();
			BeanConvertUtil.mapToBean(mDStores, paramsMap);
			// 定位默认为北京市
			String longitude = "116.405285";
			String latitude = "39.904989";
			tempMap.clear();
			tempMap.put("area_code", paramsMap.get("area_id"));
			Map<String, Object> areaMap = memberDao.selectMDArea(tempMap);
			if (areaMap != null) {
				longitude = areaMap.get("longitude") + "";
				latitude = areaMap.get("latitude") + "";
			}
			mDStores.setLongitude(new BigDecimal(longitude));
			mDStores.setLatitude(new BigDecimal(latitude));
			storesDao.insertMDStores(mDStores);
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("stores_id", member_id);
			logMap.put("category", "运营系统数据同步");
			logMap.put("tracked_date", new Date());
			logMap.put("event", "门店信息同步");
			storesDao.insertMDStoresLog(logMap);
			// 初始化会员资产信息
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "finance.memberAssetInit");
			bizParams.put("member_id", member_id);
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			// 构建门店和用户之间的关系
			MDUserMember mDUserMember = new MDUserMember();
			mDUserMember.setIs_admin("Y");
			mDUserMember.setMember_id(member_id);
			mDUserMember.setUser_id(user_id);
			mDUserMember.setMember_type_key(Constant.MEMBER_TYPE_STORES);
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
	public void storeSyncEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("member_id", (String) paramsMap.get("stores_id"));
			List<MDStores> mDStoresList = storesDao.selectMDStores(tempMap);
			if (mDStoresList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			if (paramsMap.size() == 1) {
				throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE,
						RspCode.MSG.get(RspCode.SYSTEM_NO_PARAMS_UPDATE));
			}
			// 清楚缓存数据
			memberService.clearCacheMemberInfo(mDStoresList.get(0).getStores_id(), mDStoresList.get(0).getContact_tel(),
					Constant.MEMBER_TYPE_STORES);
			storesDao.updateMDStoresSync(paramsMap);
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
	public void handleStoreSyncRegisterForHYD(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 惠易定过来的联盟商
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_name", "stores_no", "contact_person",
					"contact_tel", "area_id", "address", "registered_date" });
			String stores_id = IDUtil.getUUID();
			paramsMap.put("stores_id", stores_id);
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			/**
			 * 检查手机号是否已经注册成了门店， 如果手机号已经成为用户，但是不是门店,则获取用户id,
			 * 如果手机号不存在,则直接注册手机号,并获取用户id 如果门店类型存在,则直接抛出异常
			 */
			reqParams.put("service", "infrastructure.memberTypeValidateByMobile");
			bizParams.put("mobile", (String) paramsMap.get("contact_tel"));
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
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
				bizParams.put("mobile", (String) paramsMap.get("contact_tel"));
				bizParams.put("user_name", (String) paramsMap.get("contact_person"));
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

			// 新增门店
			MDStores mDStores = new MDStores();
			BeanConvertUtil.mapToBean(mDStores, paramsMap);
			mDStores.setSys_status(Constant.STATUS_NORMAL);
			mDStores.setBusiness_type_key("MDLX_01");
			mDStores.setBusiness_status_key("TDJD_01");
			mDStores.setStores_type_key(Constant.STORES_TYPE_02);
			// 定位默认为北京市
			String longitude = "116.405285";
			String latitude = "39.904989";
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("area_code", paramsMap.get("area_id"));
			Map<String, Object> areaMap = memberDao.selectMDArea(tempMap);
			if (areaMap != null) {
				longitude = areaMap.get("longitude") + "";
				latitude = areaMap.get("latitude") + "";
			}
			mDStores.setLongitude(new BigDecimal(longitude));
			mDStores.setLatitude(new BigDecimal(latitude));
			storesDao.insertMDStores(mDStores);
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("stores_id", stores_id);
			logMap.put("category", "惠易定数据同步");
			logMap.put("tracked_date", new Date());
			logMap.put("event", "惠易定门店信息同步");
			storesDao.insertMDStoresLog(logMap);
			// 初始化会员资产信息
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "finance.memberAssetInit");
			bizParams.put("member_id", stores_id);
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			// 构建门店和用户之间的关系
			MDUserMember mDUserMember = new MDUserMember();
			mDUserMember.setIs_admin("Y");
			mDUserMember.setMember_id(stores_id);
			mDUserMember.setUser_id(user_id);
			mDUserMember.setMember_type_key(Constant.MEMBER_TYPE_STORES);
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
	public void handleStoreSyncRegisterForHYD3(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 惠易定过来的联盟商
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id", "stores_name", "stores_no",
					"contact_person", "contact_tel", "area_id", "address", "registered_date" });
			String stores_id = paramsMap.get("stores_id") + "";
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			bizParams.put("stores_id", stores_id);
			List<MDStores> mDStoresList = storesDao.selectMDStores(bizParams);
			if (mDStoresList.size() > 0) {
				throw new BusinessException(RspCode.MEMBER_EXIST, RspCode.MSG.get(RspCode.MEMBER_EXIST));
			}
			/**
			 * 检查手机号是否已经注册成了门店， 如果手机号已经成为用户，但是不是门店,则获取用户id,
			 * 如果手机号不存在,则直接注册手机号,并获取用户id 如果门店类型存在,则直接抛出异常
			 */
			reqParams.put("service", "infrastructure.memberTypeValidateByMobile");
			bizParams.put("mobile", (String) paramsMap.get("contact_tel"));
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
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
				bizParams.put("mobile", (String) paramsMap.get("contact_tel"));
				bizParams.put("user_name", (String) paramsMap.get("contact_person"));
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

			// 新增门店
			MDStores mDStores = new MDStores();
			BeanConvertUtil.mapToBean(mDStores, paramsMap);
			mDStores.setSys_status(Constant.STATUS_NORMAL);
			mDStores.setBusiness_type_key("MDLX_01");
			mDStores.setBusiness_status_key("TDJD_01");
			mDStores.setStores_type_key(Constant.STORES_TYPE_02);
			// 定位默认为北京市
			String longitude = "116.405285";
			String latitude = "39.904989";
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("area_code", paramsMap.get("area_id"));
			Map<String, Object> areaMap = memberDao.selectMDArea(tempMap);
			if (areaMap != null) {
				longitude = areaMap.get("longitude") + "";
				latitude = areaMap.get("latitude") + "";
			}
			mDStores.setLongitude(new BigDecimal(longitude));
			mDStores.setLatitude(new BigDecimal(latitude));
			storesDao.insertMDStores(mDStores);
			Map<String, Object> logMap = new HashMap<String, Object>();
			logMap.put("log_id", IDUtil.getUUID());
			logMap.put("stores_id", stores_id);
			logMap.put("category", "惠易定数据同步");
			logMap.put("tracked_date", new Date());
			logMap.put("event", "惠易定门店信息同步");
			storesDao.insertMDStoresLog(logMap);
			// 初始化会员资产信息
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "finance.memberAssetInit");
			bizParams.put("member_id", stores_id);
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			// 构建门店和用户之间的关系
			MDUserMember mDUserMember = new MDUserMember();
			mDUserMember.setIs_admin("Y");
			mDUserMember.setMember_id(stores_id);
			mDUserMember.setUser_id(user_id);
			mDUserMember.setMember_type_key(Constant.MEMBER_TYPE_STORES);
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
	public void handleStoreActivityPaymentEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 同一个门店一个类型的活动只能有一个
			ValidateUtil.validateParams(paramsMap, new String[] { "store_activity" });
			List<Map<String, Object>> store_activity_list = FastJsonUtil
					.jsonToList(paramsMap.get("store_activity") + "");
			Map<String, Object> tempMap = new HashMap<String, Object>();
			for (Map<String, Object> storeActivity : store_activity_list) {
				ValidateUtil.validateParams(storeActivity,
						new String[] { "activity", "stores_id", "amount", "promotion", "status", "remark" });
				tempMap.clear();
				tempMap.put("activity", storeActivity.get("activity"));
				tempMap.put("stores_id", storeActivity.get("stores_id"));
				List<MDStoresActivityPromotion> list = memberDao.selectMDStoresActivityPromotion(tempMap);
				if (list.size() == 0) {
					MDStoresActivityPromotion mDStoresActivityPromotion = new MDStoresActivityPromotion();
					BeanConvertUtil.mapToBean(mDStoresActivityPromotion, storeActivity);
					mDStoresActivityPromotion.setActivity_id(IDUtil.getUUID());
					mDStoresActivityPromotion.setCreated_date(new Date());
					memberDao.insertMDStoresActivityPromotion(mDStoresActivityPromotion);
				} else {
					memberDao.updateMDStoresActivityPromotion(storeActivity);
				}
			}
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
	public void storeActivityPaymentFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			List<MDStoresActivityPromotion> list = memberDao.selectMDStoresActivityPromotion(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (MDStoresActivityPromotion promotion : list) {
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("activity_id", promotion.getActivity_id());
				temp.put("activity", promotion.getActivity());
				temp.put("amount", promotion.getAmount().intValue() + "");
				temp.put("promotion", promotion.getPromotion().intValue() + "");
				temp.put("status", promotion.getStatus());
				resultList.add(temp);
			}
			result.setResultData(resultList);
			list.clear();
			list = null;
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void nearbyHSPostListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "longitude", "latitude" });
			String longitude = paramsMap.get("longitude") + "";
			String latitude = paramsMap.get("latitude") + "";
			Double longitude_gt = Double.parseDouble(longitude) - 1;
			Double latitude_gt = Double.parseDouble(latitude) - 1;
			Double longitude_lt = Double.parseDouble(longitude) + 1;
			Double latitude_lt = Double.parseDouble(latitude) + 1;
			paramsMap.put("longitude_gt", longitude_gt);
			paramsMap.put("latitude_gt", latitude_gt);
			paramsMap.put("longitude_lt", longitude_lt);
			paramsMap.put("latitude_lt", latitude_lt);
			paramsMap.put("is_stage_hpt", "Y");
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> mDStoresList = storesDao.selectNearbyMDStores(paramsMap);
			List<String> doc_ids = new ArrayList<String>();
			for (Map<String, Object> m : mDStoresList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("stores_id", StringUtil.formatStr(m.get("stores_id")));
				tempMap.put("stores_name", StringUtil.formatStr(m.get("stores_name")));
				tempMap.put("address", StringUtil.formatStr(m.get("address")));
				tempMap.put("distance", m.get("distance") + "");
				// 如果街景为空,则设置街景为门头图
				String neighbor_pic_path = StringUtil.formatStr(m.get("neighbor_pic_path"));
				if (neighbor_pic_path.equals("")) {
					List<String> pathList = new ArrayList<String>();
					String new_facade_pic_path = StringUtil.formatStr(m.get("new_facade_pic_path"));
					if (!new_facade_pic_path.equals("")) {
						pathList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
					}
					String new_stores_pic_path = StringUtil.formatStr(m.get("new_stores_pic_path"));
					if (!new_stores_pic_path.equals("")) {
						pathList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
					}
					if (pathList.size() > 0) {
						neighbor_pic_path = pathList.get(0);
					}
				}
				tempMap.put("neighbor_pic_path", neighbor_pic_path);
				doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
				tempMap.put("business_type_desc",
						Constant.BUSINESS_TYPE_MAP.get(StringUtil.formatStr(m.get("business_type_key"))));
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", resultList);
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/***
	 * 门店营业信息查询(掌上超市信息)
	 */
	@Override
	public void storesBusinessInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		Map<String, Object> storesBusinessMap = storesDao.selectMDStoresBusinessInfoFind(paramsMap);
		if (storesBusinessMap == null || storesBusinessMap.isEmpty()) {
			throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
		}
		storesBusinessMap.put("opening_time", StringUtil.formatStr(storesBusinessMap.get("opening_time")));
		storesBusinessMap.put("deliveried_range", StringUtil.formatStr(storesBusinessMap.get("deliveried_range")));
		storesBusinessMap.put("over_amount", StringUtil.formatStr(storesBusinessMap.get("over_amount")));
		result.setResultData(storesBusinessMap);
	}
	
	@Override
	public void nearbyAllStoreForSalesassistantFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "find_type"});
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> mDStoresList  = new ArrayList<Map<String, Object>>();
			if(paramsMap.get("find_type").equals("CXLX_1") || paramsMap.get("find_type").equals("CXLX_2")){
				ValidateUtil.validateParams(paramsMap, new String[] { "longitude", "latitude" });
				if (StringUtil.formatStr(paramsMap.get("range")).equals("")) {
					paramsMap.put("range", "50000");
				}
				Map<String, Object> tempHashMap = new HashMap<String, Object>();
				String longitude = paramsMap.get("longitude") + "";
				String latitude = paramsMap.get("latitude") + "";
				String range = paramsMap.get("range") !=null?paramsMap.get("range").toString():"";
				Double longitude_gt = Double.parseDouble(longitude) - 1;
				Double latitude_gt = Double.parseDouble(latitude) - 1;
				Double longitude_lt = Double.parseDouble(longitude) + 1;
				Double latitude_lt = Double.parseDouble(latitude) + 1;
				tempHashMap.put("longitude", longitude);
				tempHashMap.put("latitude", latitude);
				tempHashMap.put("longitude_gt", longitude_gt);
				tempHashMap.put("latitude_gt", latitude_gt);
				tempHashMap.put("longitude_lt", longitude_lt);
				tempHashMap.put("latitude_lt", latitude_lt);
				tempHashMap.put("range", range);
				if(paramsMap.get("find_type").equals("CXLX_2")){
					tempHashMap.put("is_assistant_locked", "N");
				}
				tempHashMap.put("pageParam", paramsMap.get("pageParam"));
				mDStoresList = storesDao.selectNearbyMDStoresForSalesassistant(tempHashMap);
			} else if (paramsMap.get("find_type").equals("CXLX_3")){ //搞掂待审批查询
				ValidateUtil.validateParams(paramsMap, new String[] { "assistant_id"});
				Map<String, Object> tempHashMap = new HashMap<String, Object>();
				String assistant_id = paramsMap.get("assistant_id") !=null?paramsMap.get("assistant_id").toString():"";
				tempHashMap.put("assistant_id", assistant_id);
				tempHashMap.put("pageParam", paramsMap.get("pageParam"));
				mDStoresList = storesDao.selectNearbyMDStoresForSalesassistantByApproval(tempHashMap);
			} else if (paramsMap.get("find_type").equals("CXLX_4")){ //搞掂APP总查询
				Map<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("search_like", paramsMap.get("search_like"));
				mDStoresList = storesDao.selectNearbyMDStoresForSalesassistantByMain(tempHashMap);
			} else if (paramsMap.get("find_type").equals("CXLX_5")){ //搞掂APP拓店查询
				ValidateUtil.validateParams(paramsMap, new String[] { "business_developer"});
				Map<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("business_developer", paramsMap.get("business_developer"));
				tempHashMap.put("pageParam", paramsMap.get("pageParam"));
				mDStoresList = storesDao.selectNearbyMDStoresForSalesassistantBySpecialist(tempHashMap);
			} else if (paramsMap.get("find_type").equals("CXLX_6")){ //搞掂APP助教查询
				ValidateUtil.validateParams(paramsMap, new String[] { "assistant_id"});
				Map<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("assistant_id", paramsMap.get("assistant_id"));
				tempHashMap.put("pageParam", paramsMap.get("pageParam"));
				mDStoresList = storesDao.selectNearbyMDStoresForSalesassistantBySpecialist(tempHashMap);
			}
			
			List<String> doc_ids = new ArrayList<String>();
			for (Map<String, Object> m : mDStoresList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("stores_id", StringUtil.formatStr(m.get("stores_id")));
				tempMap.put("stores_no", StringUtil.formatStr(m.get("stores_no")));
				tempMap.put("stores_name", StringUtil.formatStr(m.get("stores_name")));
				tempMap.put("stores_type_key", StringUtil.formatStr(m.get("stores_type_key")));
				tempMap.put("business_type_key", StringUtil.formatStr(m.get("business_type_key")));
				tempMap.put("business_type_desc", Constant.BUSINESS_TYPE_MAP.get(m.get("business_type_key") + ""));
				tempMap.put("contact_person", StringUtil.formatStr(m.get("contact_person")));
				tempMap.put("contact_tel", StringUtil.formatStr(m.get("contact_tel")));
				tempMap.put("service_tel", StringUtil.formatStr(m.get("service_tel")));
				tempMap.put("address", StringUtil.formatStr(m.get("address")));
				tempMap.put("label", StringUtil.formatStr(m.get("label")));
				tempMap.put("latitude", StringUtil.formatStr(m.get("latitude")));
				tempMap.put("longitude", StringUtil.formatStr(m.get("longitude")));
				tempMap.put("assistant_id", StringUtil.formatStr(m.get("assistant_id")));
				tempMap.put("is_assistant_locked", StringUtil.formatStr(m.get("is_assistant_locked")));
				tempMap.put("msm_contact_tel", StringUtil.formatStr(m.get("msm_contact_tel")));
				tempMap.put("msm_name", StringUtil.formatStr(m.get("msm_name")));
				tempMap.put("distance", m.get("distance") + "");
				// 如果街景为空,则设置街景为门头图
				String neighbor_pic_path = StringUtil.formatStr(m.get("neighbor_pic_path"));
				if (neighbor_pic_path.equals("")) {
					List<String> tempList = new ArrayList<String>();
					String new_facade_pic_path = StringUtil.formatStr(m.get("new_facade_pic_path"));
					if (!new_facade_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
					}
					String new_stores_pic_path = StringUtil.formatStr(m.get("new_stores_pic_path"));
					if (!new_stores_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
					}
					if (tempList.size() > 0) {
						neighbor_pic_path = tempList.get(0);
					}
				}
				tempMap.put("neighbor_pic_path", neighbor_pic_path);
				tempMap.put("logo_pic_path", StringUtil.formatStr(m.get("logo_pic_path")));
				doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
				doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(m.get("logo_pic_path")), ","));
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("data_list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void nearbyAllStoreFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "longitude", "latitude" });
			String longitude = paramsMap.get("longitude") + "";
			String latitude = paramsMap.get("latitude") + "";
			Double longitude_gt = Double.parseDouble(longitude) - 1;
			Double latitude_gt = Double.parseDouble(latitude) - 1;
			Double longitude_lt = Double.parseDouble(longitude) + 1;
			Double latitude_lt = Double.parseDouble(latitude) + 1;
			paramsMap.put("longitude_gt", longitude_gt);
			paramsMap.put("latitude_gt", latitude_gt);
			paramsMap.put("longitude_lt", longitude_lt);
			paramsMap.put("latitude_lt", latitude_lt);
			String business_type_key = (String) paramsMap.get("business_type_key");
			if (null != business_type_key && !"".equals(business_type_key)) {
				List<String> list = StringUtil.str2List(business_type_key, ",");
				if (list.size() > 1) {
					paramsMap.remove("business_type_key");
					paramsMap.put("business_type_in", list);
				}
			}
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> mDStoresList = storesDao.selectNearbyMDStores(paramsMap);
			List<String> doc_ids = new ArrayList<String>();
			for (Map<String, Object> m : mDStoresList) {
				// 验证门店是否有优惠券信息
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("stores_id", StringUtil.formatStr(m.get("stores_id")));
				tempMap.put("stores_no", StringUtil.formatStr(m.get("stores_no")));
				tempMap.put("stores_name", StringUtil.formatStr(m.get("stores_name")));
				tempMap.put("stores_type_key", StringUtil.formatStr(m.get("stores_type_key")));
				tempMap.put("business_type_key", StringUtil.formatStr(m.get("business_type_key")));
				tempMap.put("business_type_desc", Constant.BUSINESS_TYPE_MAP.get(m.get("business_type_key") + ""));
				// contact_tel 后期废除
				tempMap.put("contact_tel", StringUtil.formatStr(m.get("service_tel")));

				tempMap.put("service_tel", StringUtil.formatStr(m.get("service_tel")));
				tempMap.put("address", StringUtil.formatStr(m.get("address")));
				tempMap.put("label", StringUtil.formatStr(m.get("label")));
				tempMap.put("latitude", StringUtil.formatStr(m.get("latitude")));
				tempMap.put("longitude", StringUtil.formatStr(m.get("longitude")));
				tempMap.put("distance", m.get("distance") + "");
				// 如果街景为空,则设置街景为门头图
				String neighbor_pic_path = StringUtil.formatStr(m.get("neighbor_pic_path"));
				if (neighbor_pic_path.equals("")) {
					List<String> tempList = new ArrayList<String>();
					String new_facade_pic_path = StringUtil.formatStr(m.get("new_facade_pic_path"));
					if (!new_facade_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
					}
					String new_stores_pic_path = StringUtil.formatStr(m.get("new_stores_pic_path"));
					if (!new_stores_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
					}
					if (tempList.size() > 0) {
						neighbor_pic_path = tempList.get(0);
					}
				}
				tempMap.put("neighbor_pic_path", neighbor_pic_path);
				tempMap.put("logo_pic_path", StringUtil.formatStr(m.get("logo_pic_path")));
				doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
				doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(m.get("logo_pic_path")), ","));
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void nearbyStoreFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "longitude", "latitude" });
			String longitude = paramsMap.get("longitude") + "";
			String latitude = paramsMap.get("latitude") + "";
			Double longitude_gt = Double.parseDouble(longitude) - 1;
			Double latitude_gt = Double.parseDouble(latitude) - 1;
			Double longitude_lt = Double.parseDouble(longitude) + 1;
			Double latitude_lt = Double.parseDouble(latitude) + 1;
			paramsMap.put("longitude_gt", longitude_gt);
			paramsMap.put("latitude_gt", latitude_gt);
			paramsMap.put("longitude_lt", longitude_lt);
			paramsMap.put("latitude_lt", latitude_lt);
			String business_type_key = (String) paramsMap.get("business_type_key");
			if (null != business_type_key && !"".equals(business_type_key)) {
				List<String> list = StringUtil.str2List(business_type_key, ",");
				if (list.size() > 1) {
					paramsMap.remove("business_type_key");
					paramsMap.put("business_type_in", list);
				}
			}
			paramsMap.put("stores_type_key", Constant.STORES_TYPE_03);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> mDStoresList = storesDao.selectNearbyMDStores(paramsMap);
			List<String> doc_ids = new ArrayList<String>();
			for (Map<String, Object> m : mDStoresList) {
				// 验证门店是否有优惠券信息
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("stores_id", StringUtil.formatStr(m.get("stores_id")));
				tempMap.put("stores_no", StringUtil.formatStr(m.get("stores_no")));
				tempMap.put("stores_name", StringUtil.formatStr(m.get("stores_name")));
				tempMap.put("stores_type_key", StringUtil.formatStr(m.get("stores_type_key")));
				tempMap.put("business_type_key", StringUtil.formatStr(m.get("business_type_key")));
				tempMap.put("business_type_desc", Constant.BUSINESS_TYPE_MAP.get(m.get("business_type_key") + ""));
				tempMap.put("address", StringUtil.formatStr(m.get("address")));
				tempMap.put("label", StringUtil.formatStr(m.get("label")));
				tempMap.put("latitude", StringUtil.formatStr(m.get("latitude")));
				tempMap.put("longitude", StringUtil.formatStr(m.get("longitude")));
				tempMap.put("distance", m.get("distance") + "");
				// 如果街景为空,则设置街景为门头图
				String neighbor_pic_path = StringUtil.formatStr(m.get("neighbor_pic_path"));
				if (neighbor_pic_path.equals("")) {
					List<String> tempList = new ArrayList<String>();
					String new_facade_pic_path = StringUtil.formatStr(m.get("new_facade_pic_path"));
					if (!new_facade_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
					}
					String new_stores_pic_path = StringUtil.formatStr(m.get("new_stores_pic_path"));
					if (!new_stores_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
					}
					if (tempList.size() > 0) {
						neighbor_pic_path = tempList.get(0);
					}
				}
				tempMap.put("neighbor_pic_path", neighbor_pic_path);
				tempMap.put("logo_pic_path", StringUtil.formatStr(m.get("logo_pic_path")));
				doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
				doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(m.get("logo_pic_path")), ","));
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void nearbyLDStoreFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "longitude", "latitude" });
			String longitude = paramsMap.get("longitude") + "";
			String latitude = paramsMap.get("latitude") + "";
			Double longitude_gt = Double.parseDouble(longitude) - 1;
			Double latitude_gt = Double.parseDouble(latitude) - 1;
			Double longitude_lt = Double.parseDouble(longitude) + 1;
			Double latitude_lt = Double.parseDouble(latitude) + 1;
			paramsMap.put("longitude_gt", longitude_gt);
			paramsMap.put("latitude_gt", latitude_gt);
			paramsMap.put("longitude_lt", longitude_lt);
			paramsMap.put("latitude_lt", latitude_lt);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> mDStoresList = storesDao.selectNearbyMDStores(paramsMap);
			List<String> doc_ids = new ArrayList<String>();
			String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Map<String, Object>> tempStoresMap = new HashMap<String, Map<String, Object>>();
			String stores_id = "";
			for (Map<String, Object> storesMap : mDStoresList) {
				stores_id += ("," + storesMap.get("stores_id"));
				tempStoresMap.put(storesMap.get("stores_id") + "", storesMap);
			}
			if (stores_id.length() > 0) {
				stores_id = stores_id.substring(1);
				reqParams.clear();
				bizParams.clear();
				resultMap.clear();
				reqParams.put("service", "goods.ldActivitiesForMemberFind");
				bizParams.put("stores_id", stores_id);
				bizParams.put("status", "processing");
				bizParams.put("activity_type", "DSK"); // 只查询一元购的活动
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(goods_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				List<Map<String, Object>> dateList = (List<Map<String, Object>>) resultMap.get("data");
				for (Map<String, Object> ldActivitiesMap : dateList) {
					stores_id = ldActivitiesMap.get("stores_id") + "";
					Map<String, Object> storesMap = tempStoresMap.get(stores_id);
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("stores_id", StringUtil.formatStr(storesMap.get("stores_id")));
					tempMap.put("stores_no", StringUtil.formatStr(storesMap.get("stores_no")));
					tempMap.put("stores_name", StringUtil.formatStr(storesMap.get("stores_name")));
					tempMap.put("area_id", StringUtil.formatStr(storesMap.get("area_id")));
					tempMap.put("address", StringUtil.formatStr(storesMap.get("address")));
					tempMap.put("contact_person", StringUtil.formatStr(storesMap.get("contact_person")));
					tempMap.put("contact_tel", StringUtil.formatStr(storesMap.get("contact_tel")));
					tempMap.put("distance", storesMap.get("distance"));
					// 如果街景为空,则设置街景为门头图
					String neighbor_pic_path = StringUtil.formatStr(storesMap.get("neighbor_pic_path"));
					if (neighbor_pic_path.equals("")) {
						List<String> pathList = new ArrayList<String>();
						String new_facade_pic_path = StringUtil.formatStr(storesMap.get("new_facade_pic_path"));
						if (!new_facade_pic_path.equals("")) {
							pathList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
						}
						String new_stores_pic_path = StringUtil.formatStr(storesMap.get("new_stores_pic_path"));
						if (!new_stores_pic_path.equals("")) {
							pathList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
						}
						if (pathList.size() > 0) {
							neighbor_pic_path = pathList.get(0);
						}
					}
					tempMap.put("neighbor_pic_path", neighbor_pic_path);
					tempMap.put("logo_pic_path", StringUtil.formatStr(storesMap.get("logo_pic_path")));
					tempMap.put("activity_id", ldActivitiesMap.get("activity_id"));
					tempMap.put("title", ldActivitiesMap.get("title"));
					tempMap.put("desc1", ldActivitiesMap.get("desc1"));
					tempMap.put("end_date", ldActivitiesMap.get("end_date"));
					tempMap.put("award_name", ldActivitiesMap.get("award_name"));
					tempMap.put("award_value", ldActivitiesMap.get("award_value") + "");
					tempMap.put("person_num", ldActivitiesMap.get("person_num") + "");
					tempMap.put("total_person", ldActivitiesMap.get("total_person") + "");
					tempMap.put("award_pic_path1", ldActivitiesMap.get("award_pic_path1"));
					tempMap.put("award_pic_path2", ldActivitiesMap.get("award_pic_path2"));
					tempMap.put("award_pic_path3", ldActivitiesMap.get("award_pic_path3"));
					tempMap.put("award_pic_path4", ldActivitiesMap.get("award_pic_path4"));
					tempMap.put("award_pic_path5", ldActivitiesMap.get("award_pic_path5"));
					tempMap.put("status", ldActivitiesMap.get("status"));
					Date end_date = DateUtil.str2Date(ldActivitiesMap.get("end_date") + "",
							DateUtil.fmt_yyyyMMddHHmmss);
					tempMap.put("difftime", (end_date.getTime() - new Date().getTime()) / 1000);
					doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
					doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(storesMap.get("logo_pic_path")), ","));
					doc_ids.addAll(
							StringUtil.str2List(StringUtil.formatStr(ldActivitiesMap.get("award_pic_path1")), ","));
					doc_ids.addAll(
							StringUtil.str2List(StringUtil.formatStr(ldActivitiesMap.get("award_pic_path2")), ","));
					doc_ids.addAll(
							StringUtil.str2List(StringUtil.formatStr(ldActivitiesMap.get("award_pic_path3")), ","));
					doc_ids.addAll(
							StringUtil.str2List(StringUtil.formatStr(ldActivitiesMap.get("award_pic_path4")), ","));
					doc_ids.addAll(
							StringUtil.str2List(StringUtil.formatStr(ldActivitiesMap.get("award_pic_path5")), ","));
					resultList.add(tempMap);
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void nearbyPsGroupGoodsListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "longitude", "latitude" });
			String longitude = paramsMap.get("longitude") + "";
			String latitude = paramsMap.get("latitude") + "";
			Double longitude_gt = Double.parseDouble(longitude) - 1;
			Double latitude_gt = Double.parseDouble(latitude) - 1;
			Double longitude_lt = Double.parseDouble(longitude) + 1;
			Double latitude_lt = Double.parseDouble(latitude) + 1;
			paramsMap.put("longitude_gt", longitude_gt);
			paramsMap.put("latitude_gt", latitude_gt);
			paramsMap.put("longitude_lt", longitude_lt);
			paramsMap.put("latitude_lt", latitude_lt);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> mDStoresList = storesDao.selectNearbyMDStores(paramsMap);
			Map<String, Object> doc_url = new HashMap<String, Object>();
			String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Map<String, Object>> tempStoresMap = new HashMap<String, Map<String, Object>>();
			String stores_id = "";
			for (Map<String, Object> storesMap : mDStoresList) {
				stores_id += ("," + storesMap.get("stores_id"));
				tempStoresMap.put(storesMap.get("stores_id") + "", storesMap);
			}
			if (stores_id.length() > 0) {
				stores_id = stores_id.substring(1);
				reqParams.clear();
				bizParams.clear();
				resultMap.clear();
				reqParams.put("service", "order.psGroupGoodsListFind");
				bizParams.put("member_id", stores_id);
				bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
				bizParams.put("status", "activing");
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(order_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
				List<Map<String, Object>> dateList = (List<Map<String, Object>>) dateMap.get("list");
				doc_url.putAll((Map<String, Object>) dateMap.get("doc_url"));
				for (Map<String, Object> psGroupGoodsMap : dateList) {
					stores_id = psGroupGoodsMap.get("member_id") + "";
					Map<String, Object> storesMap = tempStoresMap.get(stores_id);
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("stores_no", StringUtil.formatStr(storesMap.get("stores_no")));
					tempMap.put("stores_name", StringUtil.formatStr(storesMap.get("stores_name")));
					tempMap.put("delivery_address", StringUtil.formatStr(storesMap.get("delivery_address")));
					tempMap.put("distance", storesMap.get("distance"));
					tempMap.put("invitation_code", psGroupGoodsMap.get("invitation_code"));
					tempMap.put("goods_id", psGroupGoodsMap.get("goods_id"));
					tempMap.put("goods_title", psGroupGoodsMap.get("goods_title"));
					tempMap.put("retail_price", psGroupGoodsMap.get("retail_price") + "");
					tempMap.put("qty_limit", psGroupGoodsMap.get("qty_limit") + "");
					tempMap.put("sub_order_qty", psGroupGoodsMap.get("sub_order_qty") + "");
					tempMap.put("pic_info", psGroupGoodsMap.get("goods_pic_info") + "");
					resultList.add(tempMap);
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", doc_url);
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storeLoginValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "user_id" });
			paramsMap.put("member_type_key", Constant.MEMBER_TYPE_STORES);
			List<MDUserMember> mDUserMemberList = memberDao.selectMDUserMember(paramsMap);
			if (mDUserMemberList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDUserMember mDUserMember = mDUserMemberList.get(0);

			// 验证门店状态
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("stores_id", mDUserMember.getMember_id());
			List<MDStores> mDStoresList = storesDao.selectMDStores(tempMap);
			if (mDStoresList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDStores mDStores = mDStoresList.get(0);
			String business_status_key = StringUtil.formatStr(mDStores.getBusiness_status_key());
			if (!business_status_key.equals("") && business_status_key.equals("TDJD_04")) {
				throw new BusinessException(RspCode.MEMBER_EXIST, "门店已经存在");
			}
			String sys_status = mDStores.getSys_status();
			if (!sys_status.equals(Constant.STATUS_NORMAL)) {
				throw new BusinessException(RspCode.MEMBER_STATUS_ERROR, "会员被禁用");
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("member_id", mDUserMember.getMember_id());
			resultMap.put("is_admin", mDUserMember.getIs_admin());
			resultMap.put("area_id", mDStores.getArea_id());
			resultMap.put("business_type_key", mDStores.getBusiness_type_key());
			resultMap.put("stores_type_key", mDStores.getStores_type_key());
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/***
	 * 惠商驿站查询
	 */
	@Override
	public void stageStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		Map<String, Object> stageStoresMap = storesDao.selectStageStores(paramsMap);
		if (null == stageStoresMap) {
			throw new BusinessException(RspCode.MEMBER_NOT_EXIST, "门店信息不存在");
		}
		result.setResultData(stageStoresMap);
	}

	/***
	 * 惠商驿站信息编辑
	 */
	@Override
	public void stageStoresEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		if (paramsMap.size() == 1) {
			throw new BusinessException(CommonRspCode.SYSTEM_NO_PARAMS_UPDATE, "没有数据更新");
		}
		storesDao.updateMDStores(paramsMap);
	}

	@Override
	public void storeEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			if (paramsMap.size() == 1) {
				throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE,
						RspCode.MSG.get(RspCode.SYSTEM_NO_PARAMS_UPDATE));
			}
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String member_id = (String) paramsMap.get("member_id");
			tempMap.put("member_id", member_id);
			List<MDStores> mDStoresList = storesDao.selectMDStores(tempMap);
			if (mDStoresList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			storesDao.updateMDStores(paramsMap);
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
	public void storeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			if (paramsMap.size() == 0) {
				throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE, "查询参数缺失");
			}
			List<MDStores> mDStoresList = storesDao.selectNormalStores(paramsMap);
			if (mDStoresList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDStores mDStores = mDStoresList.get(0);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("stores_no", StringUtil.formatStr(mDStores.getStores_no()));
			resultMap.put("stores_name", StringUtil.formatStr(mDStores.getStores_name()));
			resultMap.put("stores_type_key", StringUtil.formatStr(mDStores.getStores_type_key()));
			resultMap.put("business_type_key", StringUtil.formatStr(mDStores.getBusiness_type_key()));
			resultMap.put("desc1", StringUtil.formatStr(mDStores.getDesc1()));
			resultMap.put("contact_person", StringUtil.formatStr(mDStores.getContact_person()));
			resultMap.put("contact_tel", StringUtil.formatStr(mDStores.getContact_tel()));
			resultMap.put("address", StringUtil.formatStr(mDStores.getAddress()));
			resultMap.put("area_id", StringUtil.formatStr(mDStores.getArea_id()));
			resultMap.put("area_desc", StringUtil.formatStr(mDStores.getArea_desc()));
			resultMap.put("area_size", StringUtil.formatStr(mDStores.getArea_size()));
			resultMap.put("label", StringUtil.formatStr(mDStores.getLabel()));
			resultMap.put("longitude", StringUtil.formatStr(mDStores.getLongitude()));
			resultMap.put("latitude", StringUtil.formatStr(mDStores.getLatitude()));
			// 如果街景为空,则设置街景为门头图
			String neighbor_pic_path = StringUtil.formatStr(mDStores.getNeighbor_pic_path());
			if (StringUtils.isEmpty(neighbor_pic_path)) {
				List<String> tempList = new ArrayList<String>();
				String new_facade_pic_path = StringUtil.formatStr(mDStores.getNew_facade_pic_path());
				if (!new_facade_pic_path.equals("")) {
					tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
				}
				String new_stores_pic_path = StringUtil.formatStr(mDStores.getNew_stores_pic_path());
				if (!new_stores_pic_path.equals("")) {
					tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
				}
				if (tempList.size() > 0) {
					neighbor_pic_path = tempList.get(0);
				}
			}
			resultMap.put("neighbor_pic_path", neighbor_pic_path);
			resultMap.put("registered_date",
					DateUtil.date2Str(mDStores.getRegistered_date(), DateUtil.fmt_yyyyMMddHHmmss));
			resultMap.put("servcie_limit", StringUtil.formatStr(mDStores.getServcie_limit()));
			resultMap.put("servcie_range", StringUtil.formatStr(mDStores.getServcie_range()));
			resultMap.put("activity_servcie", StringUtil.formatStr(mDStores.getActivity_servcie()));
			resultMap.put("activity_posters", StringUtil.formatStr(mDStores.getActivity_posters()));
			resultMap.put("logo_pic_path", StringUtil.formatStr(mDStores.getLogo_pic_path()));
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
	public void storesBaseInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			Map<String, Object> storesMap = storesDao.selectMDStoresBaseInfo(paramsMap);
			if (null == storesMap) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			storesMap.put("area_id", StringUtil.formatStr(storesMap.get("area_id")));
			storesMap.put("area_desc", StringUtil.formatStr(storesMap.get("area_desc")));
			storesMap.put("longitude", StringUtil.formatStr(storesMap.get("longitude")));
			storesMap.put("latitude", StringUtil.formatStr(storesMap.get("latitude")));
			storesMap.put("neighbor_pic_path", StringUtil.formatStr(storesMap.get("neighbor_pic_path")));
			result.setResultData(storesMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storeInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			if (paramsMap.size() == 0) {
				throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE, "查询参数缺失");
			}
			List<MDStores> mDStoresList = storesDao.selectMDStores(paramsMap);
			if (mDStoresList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDStores mDStores = mDStoresList.get(0);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("stores_no", StringUtil.formatStr(mDStores.getStores_no()));
			resultMap.put("stores_name", StringUtil.formatStr(mDStores.getStores_name()));
			resultMap.put("stores_type_key", StringUtil.formatStr(mDStores.getStores_type_key()));
			resultMap.put("business_type_key", StringUtil.formatStr(mDStores.getBusiness_type_key()));
			resultMap.put("desc1", StringUtil.formatStr(mDStores.getDesc1()));
			resultMap.put("contact_person", StringUtil.formatStr(mDStores.getContact_person()));
			resultMap.put("contact_tel", StringUtil.formatStr(mDStores.getContact_tel()));
			resultMap.put("address", StringUtil.formatStr(mDStores.getAddress()));
			resultMap.put("area_id", StringUtil.formatStr(mDStores.getArea_id()));
			resultMap.put("area_desc", StringUtil.formatStr(mDStores.getArea_desc()));
			resultMap.put("area_size", StringUtil.formatStr(mDStores.getArea_size()));
			resultMap.put("label", StringUtil.formatStr(mDStores.getLabel()));
			resultMap.put("longitude", StringUtil.formatStr(mDStores.getLongitude()));
			resultMap.put("latitude", StringUtil.formatStr(mDStores.getLatitude()));
			resultMap.put("registered_date",
					DateUtil.date2Str(mDStores.getRegistered_date(), DateUtil.fmt_yyyyMMddHHmmss));
			resultMap.put("servcie_limit", StringUtil.formatStr(mDStores.getServcie_limit()));
			resultMap.put("servcie_range", StringUtil.formatStr(mDStores.getServcie_range()));
			resultMap.put("activity_servcie", StringUtil.formatStr(mDStores.getActivity_servcie()));
			resultMap.put("activity_posters", StringUtil.formatStr(mDStores.getActivity_posters()));
			resultMap.put("logo_pic_path", StringUtil.formatStr(mDStores.getLogo_pic_path()));
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
	public void storeForOrderFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			List<MDStores> mDStoresList = storesDao.selectMDStores(paramsMap);
			if (mDStoresList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDStores mDStores = mDStoresList.get(0);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("stores_no", StringUtil.formatStr(mDStores.getStores_no()));
			resultMap.put("stores_name", StringUtil.formatStr(mDStores.getStores_name()));
			resultMap.put("stores_type_key", StringUtil.formatStr(mDStores.getStores_type_key()));
			resultMap.put("business_type_key", StringUtil.formatStr(mDStores.getBusiness_type_key()));
			resultMap.put("desc1", StringUtil.formatStr(mDStores.getDesc1()));
			resultMap.put("contact_person", StringUtil.formatStr(mDStores.getContact_person()));
			resultMap.put("contact_tel", StringUtil.formatStr(mDStores.getContact_tel()));
			resultMap.put("address", StringUtil.formatStr(mDStores.getAddress()));
			resultMap.put("area_id", StringUtil.formatStr(mDStores.getArea_id()));
			resultMap.put("area_desc", StringUtil.formatStr(mDStores.getArea_desc()));
			resultMap.put("area_size", StringUtil.formatStr(mDStores.getArea_size()));
			resultMap.put("label", StringUtil.formatStr(mDStores.getLabel()));
			resultMap.put("longitude", StringUtil.formatStr(mDStores.getLongitude()));
			resultMap.put("latitude", StringUtil.formatStr(mDStores.getLatitude()));
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
	public void storeDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("stores_id", paramsMap.get("stores_id"));
			List<MDStores> mDStoresList = storesDao.selectNormalStores(tempMap);
			if (mDStoresList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			List<String> doc_ids = new ArrayList<String>();
			MDStores mDStores = mDStoresList.get(0);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("stores_no", mDStores.getStores_no());
			resultMap.put("stores_name", mDStores.getStores_name());
			resultMap.put("stores_type_key", mDStores.getStores_type_key());
			resultMap.put("business_type_key", mDStores.getBusiness_type_key());
			resultMap.put("desc1", StringUtil.formatStr(mDStores.getDesc1()));
			resultMap.put("contact_person", mDStores.getContact_person());
			resultMap.put("contact_tel", mDStores.getContact_tel());
			/** 客服电话 **/
			resultMap.put("service_tel", StringUtil.formatStr(mDStores.getService_tel()));
			resultMap.put("head_pic_path", StringUtil.formatStr(mDStores.getHead_pic_path()));
			resultMap.put("longitude", StringUtil.formatStr(mDStores.getLongitude()));
			resultMap.put("latitude", StringUtil.formatStr(mDStores.getLatitude()));
			resultMap.put("address", mDStores.getAddress());
			resultMap.put("area_id", mDStores.getArea_id());
			resultMap.put("area_desc", StringUtil.formatStr(mDStores.getArea_desc()));
			resultMap.put("label", StringUtil.formatStr(mDStores.getLabel()));
			resultMap.put("activity_servcie", StringUtil.formatStr(mDStores.getActivity_servcie()));
			// 添加营业信息,modify by dingshuo 2017-03-28
			resultMap.put("opening_time", StringUtil.formatStr(mDStores.getOpening_time()));
			resultMap.put("deliveried_range", StringUtil.formatStr(mDStores.getDeliveried_range() + ""));
			resultMap.put("over_amount", StringUtil.formatStr(mDStores.getOver_amount() + ""));
			// 如果街景为空,则设置街景为门头图
			String neighbor_pic_path = StringUtil.formatStr(mDStores.getNeighbor_pic_path());
			if (StringUtils.isEmpty(neighbor_pic_path)) {
				List<String> tempList = new ArrayList<String>();
				String new_facade_pic_path = StringUtil.formatStr(mDStores.getNew_facade_pic_path());
				if (!StringUtils.isEmpty(new_facade_pic_path)) {
					tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
				}
				String new_stores_pic_path = StringUtil.formatStr(mDStores.getNew_stores_pic_path());
				if (!StringUtils.isEmpty(new_stores_pic_path)) {
					tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
				}
				if (tempList.size() > 0) {
					neighbor_pic_path = tempList.get(0);
				}
			}
			resultMap.put("neighbor_pic_path", neighbor_pic_path);
			resultMap.put("logo_pic_path", StringUtil.formatStr(mDStores.getLogo_pic_path()));
			resultMap.put("activity_pic_info", StringUtil.formatStr(mDStores.getActivity_pic_info()));
			resultMap.put("stores_thumbnail_path", StringUtil.formatStr(mDStores.getStores_thumbnail_path()));
			doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(mDStores.getLogo_pic_path()), ","));
			doc_ids.add(StringUtil.formatStr(mDStores.getHead_pic_path()));
			// 解析活动图片
			String activity_pic_info = StringUtil.formatStr(mDStores.getActivity_pic_info());
			if (!StringUtils.isEmpty(activity_pic_info)) {
				List<Map<String, Object>> listTemp = FastJsonUtil.jsonToList(activity_pic_info);
				for (Map<String, Object> map : listTemp) {
					doc_ids.add(map.get("path_id") + "");
				}
			}
			String stores_thumbnail_path = StringUtil.formatStr(mDStores.getStores_thumbnail_path());
			if (!StringUtils.isEmpty(stores_thumbnail_path)) {
				List<Map<String, Object>> listTemp = FastJsonUtil.jsonToList(stores_thumbnail_path);
				for (Map<String, Object> map : listTemp) {
					doc_ids.add(map.get("path_id") + "");
				}
			}

			// TODO 下一个版本废弃掉
			resultMap.put("activity_posters", StringUtil.formatStr(mDStores.getActivity_posters()));
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(mDStores.getActivity_posters()), ","));

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("detail", resultMap);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storeDetailForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id", "consumer_id" });
			List<String> doc_ids = new ArrayList<String>();
			Map<String, Object> mDStores = storesDao.selectMDStoresDetailForConsumer(paramsMap);
			if (mDStores == null) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}

			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("stores_no", mDStores.get("stores_no"));
			resultMap.put("stores_name", mDStores.get("stores_name"));
			resultMap.put("stores_type_key", mDStores.get("stores_type_key"));
			resultMap.put("business_type_key", mDStores.get("business_type_key"));
			resultMap.put("desc1", StringUtil.formatStr(mDStores.get("desc1")));
			resultMap.put("contact_person", mDStores.get("contact_person"));
			resultMap.put("contact_tel", mDStores.get("service_tel"));
			/** 客服电话 **/
			resultMap.put("service_tel", StringUtil.formatStr(mDStores.get("service_tel")));
			resultMap.put("longitude", StringUtil.formatStr(mDStores.get("longitude")));
			resultMap.put("latitude", StringUtil.formatStr(mDStores.get("latitude")));
			resultMap.put("address", mDStores.get("address"));
			resultMap.put("area_id", mDStores.get("area_id"));
			resultMap.put("area_desc", StringUtil.formatStr(mDStores.get("area_desc")));
			resultMap.put("label", StringUtil.formatStr(mDStores.get("label")));
			resultMap.put("activity_servcie", StringUtil.formatStr(mDStores.get("activity_servcie")));
			resultMap.put("favorite_flag", mDStores.get("favorite_flag"));
			// modify by dingshuo 2017.03.28 添加门店营业信息查询
			resultMap.put("opening_time", StringUtil.formatStr(mDStores.get("opening_time")));
			resultMap.put("deliveried_range", StringUtil.formatStr(mDStores.get("deliveried_range")));
			resultMap.put("over_amount", StringUtil.formatStr(mDStores.get("over_amount")));
			// 如果街景为空,则设置街景为门头图
			String neighbor_pic_path = StringUtil.formatStr(mDStores.get("neighbor_pic_path"));
			if (neighbor_pic_path.equals("")) {
				List<String> tempList = new ArrayList<String>();
				String new_facade_pic_path = StringUtil.formatStr(mDStores.get("new_facade_pic_path"));
				if (!new_facade_pic_path.equals("")) {
					tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
				}
				String new_stores_pic_path = StringUtil.formatStr(mDStores.get("new_stores_pic_path"));
				if (!new_stores_pic_path.equals("")) {
					tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
				}
				if (tempList.size() > 0) {
					neighbor_pic_path = tempList.get(0);
				}
			}
			resultMap.put("neighbor_pic_path", neighbor_pic_path);
			resultMap.put("logo_pic_path", StringUtil.formatStr(mDStores.get("logo_pic_path")));
			resultMap.put("activity_pic_info", StringUtil.formatStr(mDStores.get("activity_pic_info")));
			resultMap.put("stores_thumbnail_path", StringUtil.formatStr(mDStores.get("stores_thumbnail_path")));

			// 解析活动图片
			String activity_pic_info = StringUtil.formatStr(mDStores.get("activity_pic_info"));
			if (!StringUtils.isEmpty(activity_pic_info)) {
				List<Map<String, Object>> listTemp = FastJsonUtil.jsonToList(activity_pic_info);
				for (Map<String, Object> map : listTemp) {
					doc_ids.add(map.get("path_id") + "");
				}
			}
			String stores_thumbnail_path = StringUtil.formatStr(mDStores.get("stores_thumbnail_path"));
			if (!StringUtils.isEmpty(stores_thumbnail_path)) {
				List<Map<String, Object>> listTemp = FastJsonUtil.jsonToList(stores_thumbnail_path);
				for (Map<String, Object> map : listTemp) {
					doc_ids.add(map.get("path_id") + "");
				}
			}
			// 解析图片
			doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(mDStores.get("logo_pic_path")), ","));
			// TODO 下一个版本废弃掉
			resultMap.put("activity_posters", StringUtil.formatStr(mDStores.get("activity_posters")));
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(mDStores.get("activity_posters")), ","));

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("detail", resultMap);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storeListForGoodsFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			String stores_id = StringUtil.formatStr(paramsMap.get("stores_id"));
			List<String> list = StringUtil.str2List(stores_id, ",");
			if (list.size() > 0) {
				paramsMap.remove("stores_id");
				paramsMap.put("stores_id_in", list);
			}
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<MDStores> mDStoresList = storesDao.selectMDStoresList(paramsMap);
			for (MDStores mDStores : mDStoresList) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("stores_id", mDStores.getStores_id());
				resultMap.put("stores_no", StringUtil.formatStr(mDStores.getStores_no()));
				resultMap.put("stores_name", StringUtil.formatStr(mDStores.getStores_name()));
				resultMap.put("stores_type_key", mDStores.getStores_type_key());
				resultMap.put("business_type_key", StringUtil.formatStr(mDStores.getBusiness_type_key()));
				resultMap.put("address", mDStores.getAddress());
				resultMap.put("contact_person", mDStores.getContact_person());
				resultMap.put("contact_tel", mDStores.getContact_tel());
				resultMap.put("label", StringUtil.formatStr(mDStores.getLabel()));
				resultList.add(resultMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storeListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>();
			List<MDStores> mDStoresList = storesDao.selectMDStoresList(paramsMap);
			for (MDStores mDStores : mDStoresList) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("stores_id", mDStores.getStores_id());
				resultMap.put("stores_no", StringUtil.formatStr(mDStores.getStores_no()));
				resultMap.put("stores_name", StringUtil.formatStr(mDStores.getStores_name()));
				resultMap.put("stores_type_key", mDStores.getStores_type_key());
				resultMap.put("business_type_key", StringUtil.formatStr(mDStores.getBusiness_type_key()));
				resultMap.put("business_type_desc", Constant.BUSINESS_TYPE_MAP.get(mDStores.getBusiness_type_key()));
				resultMap.put("address", mDStores.getAddress());
				resultMap.put("longitude", StringUtil.formatStr(mDStores.getLongitude()));
				resultMap.put("latitude", StringUtil.formatStr(mDStores.getLatitude()));
				resultMap.put("contact_person", mDStores.getContact_person());
				resultMap.put("contact_tel", mDStores.getContact_tel());
				resultMap.put("label", StringUtil.formatStr(mDStores.getLabel()));
				// 如果街景为空,则设置街景为门头图
				String neighbor_pic_path = StringUtil.formatStr(mDStores.getNeighbor_pic_path());
				if (neighbor_pic_path.equals("")) {
					List<String> tempList = new ArrayList<String>();
					String new_facade_pic_path = StringUtil.formatStr(mDStores.getNew_facade_pic_path());
					if (!new_facade_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
					}
					String new_stores_pic_path = StringUtil.formatStr(mDStores.getNew_stores_pic_path());
					if (!new_stores_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
					}
					if (tempList.size() > 0) {
						neighbor_pic_path = tempList.get(0);
					}
				}
				resultMap.put("neighbor_pic_path", neighbor_pic_path);
				resultMap.put("registered_date",
						DateUtil.date2Str(mDStores.getRegistered_date(), DateUtil.fmt_yyyyMMddHHmmss));
				resultMap.put("logo_pic_path", StringUtil.formatStr(mDStores.getLogo_pic_path()));
				// 处理图片
				doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
				doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(mDStores.getLogo_pic_path()), ","));
				resultList.add(resultMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storeIdFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "device_no" });
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<MDStores> mDStoresList = storesDao.selectMDStores(paramsMap);
			if (mDStoresList.size() > 0) {
				MDStores mDStores = mDStoresList.get(0);
				resultMap.put("stores_id", mDStores.getStores_id());
			} else {
				resultMap.put("stores_id", "");
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
	public void storeFindByMobile(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "mobile" });
			String mobile = paramsMap.get("mobile") + "";
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			Map<String, Object> resultDate = new HashMap<String, Object>();
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			reqParams.put("service", "infrastructure.memberFindByMobile");
			bizParams.put("mobile", mobile);
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			String member_id = (String) dateMap.get("member_id");
			bizParams.clear();
			bizParams.put("member_id", member_id);
			List<MDStores> mDStoresList = storesDao.selectMDStores(bizParams);
			if (mDStoresList.size() > 0) {
				MDStores mDStores = mDStoresList.get(0);
				resultDate.put("member_id", member_id);
				resultDate.put("mobile", mobile);
				resultDate.put("area", mDStores.getArea_id());
				resultDate.put("address", mDStores.getAddress());
				resultDate.put("stores_name", mDStores.getStores_name());
				resultDate.put("contact_person", mDStores.getContact_person());
				resultDate.put("stores_no", mDStores.getStores_no());
				// 惠易定2.0需要
				resultDate.put("longitude", mDStores.getLongitude());
				resultDate.put("latitude", mDStores.getLatitude());
			}
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
	public void storesStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id", "sys_status" });
			String sys_status = paramsMap.get("sys_status") + "";
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			bizParams.put("stores_id", paramsMap.get("stores_id"));
			bizParams.put("sys_status", sys_status);
			storesDao.updateMDStoresSync(bizParams);
			// 如果状态是禁用，则清除会员登陆授权信息
			if (sys_status.equals(Constant.STATUS_DISABLED)) {
				bizParams.clear();
				bizParams.put("member_id", paramsMap.get("stores_id"));
				bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
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

	@Override
	public void handleStoreSyncRegisterForShume(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 熟么同步过来的联盟商
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_name", "contact_person", "contact_tel",
					"area_id", "address", "zone", "merchant_type", "registered_date" });
			String stores_id = "";
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			/**
			 * 检查手机号是否已经注册成了门店， 如果手机号已经成为用户，但是不是门店,则获取用户id,
			 * 如果手机号不存在,则直接注册手机号,并获取用户id 如果门店类型存在,则直接抛出异常
			 */
			reqParams.put("service", "infrastructure.memberTypeValidateByMobile");
			bizParams.put("mobile", (String) paramsMap.get("contact_tel"));
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(user_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			String user_flag = (String) dateMap.get("user");
			boolean register_mermber_flag = true;
			if (((String) dateMap.get("member_type")).equals("exist")) {
				stores_id = paramsMap.get("stores_id") + "";
				register_mermber_flag = false;
			}
			if (register_mermber_flag) {
				stores_id = IDUtil.getUUID();
				paramsMap.put("stores_id", stores_id);
				String user_id = null;
				if (user_flag.equals("exist")) {
					user_id = (String) dateMap.get("user_id");
				} else {
					// 如果用户不存在,则注册用户
					reqParams.clear();
					bizParams.clear();
					resultMap.clear();
					reqParams.put("service", "infrastructure.syncUserRegister");
					bizParams.put("mobile", (String) paramsMap.get("contact_tel"));
					bizParams.put("user_name", (String) paramsMap.get("contact_person"));
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
				// 新增门店
				MDStores mDStores = new MDStores();
				BeanConvertUtil.mapToBean(mDStores, paramsMap);
				mDStores.setStores_no("sm" + IDUtil.getTimestamp(3));
				mDStores.setSys_status(Constant.STATUS_NORMAL);
				mDStores.setBusiness_status_key("TDJD_01");
				mDStores.setBusiness_type_key("MDLX_01");
				mDStores.setStores_type_key(Constant.STORES_TYPE_02);
				// 定位默认为北京市
				String longitude = "116.405285";
				String latitude = "39.904989";
				bizParams.clear();
				bizParams.put("area_code", paramsMap.get("area_id") + "");
				Map<String, Object> areaMap = memberDao.selectMDArea(bizParams);
				if (areaMap != null) {
					longitude = areaMap.get("longitude") + "";
					latitude = areaMap.get("latitude") + "";
				}
				mDStores.setLongitude(new BigDecimal(longitude));
				mDStores.setLatitude(new BigDecimal(latitude));
				storesDao.insertMDStores(mDStores);
				Map<String, Object> logMap = new HashMap<String, Object>();
				logMap.put("log_id", IDUtil.getUUID());
				logMap.put("stores_id", stores_id);
				logMap.put("category", "熟么数据同步");
				logMap.put("tracked_date", new Date());
				logMap.put("event", "熟么门店信息同步");
				storesDao.insertMDStoresLog(logMap);
				// 初始化会员资产信息
				reqParams.clear();
				bizParams.clear();
				resultMap.clear();
				reqParams.put("service", "finance.memberAssetInit");
				bizParams.put("member_id", stores_id);
				bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				// 构建门店和用户之间的关系
				MDUserMember mDUserMember = new MDUserMember();
				mDUserMember.setIs_admin("Y");
				mDUserMember.setMember_id(stores_id);
				mDUserMember.setUser_id(user_id);
				mDUserMember.setMember_type_key(Constant.MEMBER_TYPE_STORES);
				memberDao.insertMDUserMember(mDUserMember);
			}
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			// 创建熟么入住商家信息
			bizParams.clear();
			String merchant_id = IDUtil.getUUID();
			bizParams.put("merchant_id", merchant_id);
			bizParams.put("member_type_key", paramsMap.get(Constant.MEMBER_TYPE_STORES));
			bizParams.put("member_id", paramsMap.get("stores_id"));
			bizParams.put("merchant_type", paramsMap.get("merchant_type"));
			bizParams.put("zone", paramsMap.get("zone"));
			bizParams.put("area_id", paramsMap.get("area_id"));
			bizParams.put("status", "enable");
			bizParams.put("created_date", paramsMap.get("registered_date"));
			bizParams.put("modified_date", paramsMap.get("registered_date"));
			bizParams.put("remark", "熟么商户入驻");
			memberDao.insertMDMerchant(bizParams);
			// 写入日志
			bizParams.clear();
			bizParams.put("log_id", IDUtil.getUUID());
			bizParams.put("category", "熟么商户入驻");
			bizParams.put("merchant_id", merchant_id);
			bizParams.put("tracked_date", new Date());
			bizParams.put("event", "手机号为" + paramsMap.get("contact_tel") + "的商户入职熟么");
			memberDao.insertMDMerchantLog(bizParams);
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
	public void shumeStoresFindByMobile(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "mobile" });
			String mobile = paramsMap.get("mobile") + "";
			String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
			Map<String, Object> resultDate = new HashMap<String, Object>();
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
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
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
			List<MDUserMember> mDUserMemberList = memberDao.selectMDUserMember(bizParams);
			if (mDUserMemberList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST,
						mobile + RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			String member_id = mDUserMemberList.get(0).getMember_id();
			// 店东
			bizParams.clear();
			bizParams.put("member_id", member_id);
			bizParams.put("business_status_neq", "TDJD_04");
			List<MDStores> mDStoresList = storesDao.selectMDStores(bizParams);
			if (mDStoresList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			List<String> doc_ids = new ArrayList<String>();
			MDStores mDStores = mDStoresList.get(0);
			resultDate.put("stores_id", mDStores.getStores_id());
			resultDate.put("stores_no", mDStores.getStores_no());
			resultDate.put("stores_name", mDStores.getStores_name());
			resultDate.put("contact_person", mDStores.getContact_person());
			resultDate.put("contact_tel", mDStores.getContact_tel());
			resultDate.put("business_type_key", mDStores.getBusiness_type_key());
			resultDate.put("stores_type_key", mDStores.getStores_type_key());
			resultDate.put("desc1", StringUtil.formatStr(mDStores.getDesc1()));
			resultDate.put("label", StringUtil.formatStr(mDStores.getLabel()));
			resultDate.put("area_id", mDStores.getArea_id());
			resultDate.put("address", mDStores.getAddress());
			resultDate.put("latitude", mDStores.getLatitude());
			resultDate.put("longitude", mDStores.getLongitude());
			// 如果街景为空,则设置街景为门头图
			String neighbor_pic_path = StringUtil.formatStr(mDStores.getNeighbor_pic_path());
			if (neighbor_pic_path.equals("")) {
				List<String> tempList = new ArrayList<String>();
				String new_facade_pic_path = StringUtil.formatStr(mDStores.getNew_facade_pic_path());
				if (!new_facade_pic_path.equals("")) {
					tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
				}
				String new_stores_pic_path = StringUtil.formatStr(mDStores.getNew_stores_pic_path());
				if (!new_stores_pic_path.equals("")) {
					tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
				}
				if (tempList.size() > 0) {
					neighbor_pic_path = tempList.get(0);
				}
			}
			resultDate.put("neighbor_pic_path", neighbor_pic_path);
			resultDate.put("logo_pic_path", StringUtil.formatStr(mDStores.getLogo_pic_path()));
			doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(mDStores.getLogo_pic_path()), ","));
			bizParams.clear();
			bizParams.put("stores_id", member_id);
			List<Map<String, Object>> shumeStoresList = storesDao.selectShumeStores(bizParams);
			if (shumeStoresList.size() == 0) {
				resultDate.put("is_shume_merchant", "N");
			} else {
				Map<String, Object> shumeStoresMap = shumeStoresList.get(0);
				resultDate.put("is_shume_merchant", "Y");
				resultDate.put("merchant_type", StringUtil.formatStr(shumeStoresMap.get("merchant_type")));
				resultDate.put("zone", StringUtil.formatStr(shumeStoresMap.get("zone")));
				resultDate.put("created_date",
						DateUtil.date2Str((Date) shumeStoresMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("stores_info", resultDate);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void nearbyAllPropFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "longitude", "latitude" });
			String longitude = paramsMap.get("longitude") + "";
			String latitude = paramsMap.get("latitude") + "";
			Double longitude_gt = Double.parseDouble(longitude) - 1;
			Double latitude_gt = Double.parseDouble(latitude) - 1;
			Double longitude_lt = Double.parseDouble(longitude) + 1;
			Double latitude_lt = Double.parseDouble(latitude) + 1;
			paramsMap.put("longitude_gt", longitude_gt);
			paramsMap.put("latitude_gt", latitude_gt);
			paramsMap.put("longitude_lt", longitude_lt);
			paramsMap.put("latitude_lt", latitude_lt);
			Map<String, Object> storesMap = new HashMap<String, Object>();
			List<Map<String, Object>> mDStoresList = storesDao.selectNearbyMDStores(paramsMap);
			List<Map<String, Object>> mDPropList = new ArrayList<Map<String, Object>>();
			List<String> tempList = new ArrayList<String>();
			for (Map<String, Object> m : mDStoresList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("stores_id", StringUtil.formatStr(m.get("stores_id")));
				tempMap.put("stores_name", StringUtil.formatStr(m.get("stores_name")));
				tempMap.put("distance", m.get("distance") + "");
				tempList.add(StringUtil.formatStr(m.get("stores_id")));
				storesMap.put(m.get("stores_id") + "", tempMap);
			}
			mDStoresList.clear();
			// 查询门店下的优惠券信息
			if (tempList.size() > 0) {
				String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
				Map<String, String> reqParams = new HashMap<String, String>();
				Map<String, String> bizParams = new HashMap<String, String>();
				reqParams.put("service", "goods.storesCouponProp");
				bizParams.put("member_id", StringUtil.list2Str(tempList));
				bizParams.put("status", "on_shelf");
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(goods_service_url, reqParams);
				Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				mDPropList = (List<Map<String, Object>>) resultMap.get("data");
				for (Map<String, Object> m : mDPropList) {
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("stores_id", m.get("stores_id") + "");
					dataMap.put("item_id", m.get("item_id") + "");
					dataMap.put("title", m.get("title") + "");
					dataMap.put("expired_date", m.get("expired_date") + "");
					// 解析门店信息
					Map<String, Object> stores = (Map<String, Object>) storesMap.get(m.get("stores_id") + "");
					dataMap.put("stores_name", stores.get("stores_name"));
					dataMap.put("distance", stores.get("distance"));
					mDStoresList.add(dataMap);
				}
				storesMap.clear();
				reqParams.clear();
				bizParams.clear();
				storesMap = null;
				reqParams = null;
				bizParams = null;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", mDStoresList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void shumeStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>();
			List<Map<String, Object>> mDStoresList = storesDao.selectShumeStores(paramsMap);
			for (Map<String, Object> m : mDStoresList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("stores_id", StringUtil.formatStr(m.get("stores_id")));
				tempMap.put("stores_no", StringUtil.formatStr(m.get("stores_no")));
				tempMap.put("stores_name", StringUtil.formatStr(m.get("stores_name")));
				tempMap.put("stores_type_key", StringUtil.formatStr(m.get("stores_type_key")));
				tempMap.put("business_type_key", StringUtil.formatStr(m.get("business_type_key")));
				tempMap.put("desc1", StringUtil.formatStr(m.get("desc1")));
				tempMap.put("area_id", StringUtil.formatStr(m.get("area_id")));
				tempMap.put("address", StringUtil.formatStr(m.get("address")));
				tempMap.put("label", StringUtil.formatStr(m.get("label")));
				tempMap.put("contact_person", StringUtil.formatStr(m.get("contact_person")));
				tempMap.put("contact_tel", StringUtil.formatStr(m.get("contact_tel")));
				tempMap.put("latitude", StringUtil.formatStr(m.get("latitude")));
				tempMap.put("longitude", StringUtil.formatStr(m.get("longitude")));
				tempMap.put("merchant_type", StringUtil.formatStr(m.get("merchant_type")));
				tempMap.put("zone_id", StringUtil.formatStr(m.get("zone_id")));
				tempMap.put("created_date",
						DateUtil.date2Str((Date) m.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				// 如果街景为空,则设置街景为门头图
				String neighbor_pic_path = StringUtil.formatStr(m.get("neighbor_pic_path"));
				if (neighbor_pic_path.equals("")) {
					List<String> tempList = new ArrayList<String>();
					String new_facade_pic_path = StringUtil.formatStr(m.get("new_facade_pic_path"));
					if (!new_facade_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
					}
					String new_stores_pic_path = StringUtil.formatStr(m.get("new_stores_pic_path"));
					if (!new_stores_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
					}
					if (tempList.size() > 0) {
						neighbor_pic_path = tempList.get(0);
					}
				}
				tempMap.put("neighbor_pic_path", neighbor_pic_path);
				tempMap.put("logo_pic_path", StringUtil.formatStr(m.get("logo_pic_path")));
				doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
				doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(m.get("logo_pic_path")), ","));
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void nearbyShumeStoresFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "longitude", "latitude" });
			String longitude = paramsMap.get("longitude") + "";
			String latitude = paramsMap.get("latitude") + "";
			Double longitude_gt = Double.parseDouble(longitude) - 1;
			Double latitude_gt = Double.parseDouble(latitude) - 1;
			Double longitude_lt = Double.parseDouble(longitude) + 1;
			Double latitude_lt = Double.parseDouble(latitude) + 1;
			paramsMap.put("longitude_gt", longitude_gt);
			paramsMap.put("latitude_gt", latitude_gt);
			paramsMap.put("longitude_lt", longitude_lt);
			paramsMap.put("latitude_lt", latitude_lt);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> mDStoresList = storesDao.selectNearbyShumeStores(paramsMap);
			List<String> doc_ids = new ArrayList<String>();
			for (Map<String, Object> m : mDStoresList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("stores_id", StringUtil.formatStr(m.get("stores_id")));
				tempMap.put("stores_no", StringUtil.formatStr(m.get("stores_no")));
				tempMap.put("stores_name", StringUtil.formatStr(m.get("stores_name")));
				tempMap.put("stores_type_key", StringUtil.formatStr(m.get("stores_type_key")));
				tempMap.put("business_type_key", StringUtil.formatStr(m.get("business_type_key")));
				tempMap.put("desc1", StringUtil.formatStr(m.get("desc1")));
				tempMap.put("area_id", StringUtil.formatStr(m.get("area_id")));
				tempMap.put("address", StringUtil.formatStr(m.get("address")));
				tempMap.put("label", StringUtil.formatStr(m.get("label")));
				tempMap.put("contact_person", StringUtil.formatStr(m.get("contact_person")));
				tempMap.put("contact_tel", StringUtil.formatStr(m.get("contact_tel")));
				tempMap.put("latitude", StringUtil.formatStr(m.get("latitude")));
				tempMap.put("longitude", StringUtil.formatStr(m.get("longitude")));
				tempMap.put("distance", m.get("distance") + "");
				tempMap.put("merchant_type", StringUtil.formatStr(m.get("merchant_type")));
				tempMap.put("zone", StringUtil.formatStr(m.get("zone")));
				tempMap.put("created_date",
						DateUtil.date2Str((Date) m.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				// 如果街景为空,则设置街景为门头图
				String neighbor_pic_path = StringUtil.formatStr(m.get("neighbor_pic_path"));
				if (neighbor_pic_path.equals("")) {
					List<String> tempList = new ArrayList<String>();
					String new_facade_pic_path = StringUtil.formatStr(m.get("new_facade_pic_path"));
					if (!new_facade_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
					}
					String new_stores_pic_path = StringUtil.formatStr(m.get("new_stores_pic_path"));
					if (!new_stores_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
					}
					if (tempList.size() > 0) {
						neighbor_pic_path = tempList.get(0);
					}
				}
				tempMap.put("neighbor_pic_path", neighbor_pic_path);
				tempMap.put("logo_pic_path", StringUtil.formatStr(m.get("logo_pic_path")));
				doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
				doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(m.get("logo_pic_path")), ","));
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 推荐门店同步
	 * 
	 * @param consumer_id
	 * @param seller_id
	 * @param voucher_amount
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void handleStoresRecommendSync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "area_id", "stores_list" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String area_id = (String) paramsMap.get("area_id");
			tempMap.put("area_id", area_id);
			storesDao.deleteMDStoresRecommend(tempMap);
			List<Map<String, Object>> stores_list = (List<Map<String, Object>>) paramsMap.get("stores_list");
			for (Map<String, Object> storesMap : stores_list) {
				ValidateUtil.validateParams(storesMap, new String[] { "stores_id", "order_no" });
				MDStoresRecommend mDStoresRecommend = new MDStoresRecommend();
				BeanConvertUtil.mapToBean(mDStoresRecommend, storesMap);
				mDStoresRecommend.setRecommend_id(IDUtil.getUUID());
				mDStoresRecommend.setCreated_date(new Date());
				mDStoresRecommend.setArea_id(area_id);
				storesDao.insertMDStoresRecommend(mDStoresRecommend);
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
	public void storesRecommendFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "area_id" });
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>();
			List<Map<String, Object>> mDStoresList = storesDao.selectMDStoresRecommendInfo(paramsMap);
			// 查询默认商家信息
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("stores_id", "ee98dfef8c604de88361553b7f5f3ad6");
			List<MDStores> storesList = storesDao.selectNormalStores(tempMap);
			if (storesList.size() > 0) {
				MDStores stores = storesList.get(0);
				tempMap.clear();
				tempMap.put("stores_id", stores.getStores_id());
				tempMap.put("stores_no", stores.getStores_no());
				tempMap.put("stores_name", stores.getStores_name());
				tempMap.put("stores_type_key", stores.getStores_type_key());
				tempMap.put("business_type_key", stores.getBusiness_type_key());
				tempMap.put("contact_person", stores.getContact_person());
				tempMap.put("contact_tel", stores.getContact_tel());
				tempMap.put("address", stores.getAddress());
				tempMap.put("desc1", StringUtil.formatStr(stores.getDesc1()));
				tempMap.put("area_id", stores.getArea_id());
				tempMap.put("area_desc", stores.getArea_desc());
				tempMap.put("label", StringUtil.formatStr(stores.getLabel()));
				tempMap.put("longitude", StringUtil.formatStr(stores.getLongitude()));
				tempMap.put("latitude", StringUtil.formatStr(stores.getLatitude()));
				tempMap.put("neighbor_pic_path", StringUtil.formatStr(stores.getNeighbor_pic_path()));
				tempMap.put("logo_pic_path", StringUtil.formatStr(stores.getLogo_pic_path()));
				mDStoresList.add(0, tempMap);
			}
			for (Map<String, Object> map : mDStoresList) {
				if (StringUtil.formatStr(map.get("stores_name")).equals("")) {
					continue;
				}
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("stores_id", map.get("stores_id"));
				resultMap.put("stores_no", StringUtil.formatStr(map.get("stores_no")));
				resultMap.put("stores_name", StringUtil.formatStr(map.get("stores_name")));
				resultMap.put("stores_type_key", map.get("stores_type_key"));
				resultMap.put("business_type_key", StringUtil.formatStr(map.get("business_type_key")));
				resultMap.put("contact_person", map.get("contact_person"));
				resultMap.put("contact_tel", map.get("contact_tel"));
				resultMap.put("address", map.get("address"));
				resultMap.put("desc1", StringUtil.formatStr(map.get("desc1")));
				resultMap.put("area_id", StringUtil.formatStr(map.get("area_id")));
				resultMap.put("area_desc", StringUtil.formatStr(map.get("area_desc")));
				resultMap.put("label", StringUtil.formatStr(map.get("label")));
				resultMap.put("longitude", StringUtil.formatStr(map.get("longitude")));
				resultMap.put("latitude", StringUtil.formatStr(map.get("latitude")));
				// 如果街景为空,则设置街景为门头图
				String neighbor_pic_path = StringUtil.formatStr(map.get("neighbor_pic_path"));
				if (neighbor_pic_path.equals("")) {
					List<String> tempList = new ArrayList<String>();
					String new_facade_pic_path = StringUtil.formatStr(map.get("new_facade_pic_path"));
					if (!new_facade_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
					}
					String new_stores_pic_path = StringUtil.formatStr(map.get("new_stores_pic_path"));
					if (!new_stores_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
					}
					if (tempList.size() > 0) {
						neighbor_pic_path = tempList.get(0);
					}
				}
				resultMap.put("neighbor_pic_path", neighbor_pic_path);
				resultMap.put("logo_pic_path", StringUtil.formatStr(map.get("logo_pic_path")));
				// 处理图片
				doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("logo_pic_path")), ","));
				doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
				resultList.add(resultMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storesRecommendForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "area_id" });
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>();
			List<Map<String, Object>> mDStoresList = storesDao.selectMDStoresRecommendInfo(paramsMap);
			for (Map<String, Object> map : mDStoresList) {
				if (StringUtil.formatStr(map.get("stores_name")).equals("")) {
					continue;
				}
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("stores_id", map.get("stores_id"));
				resultMap.put("stores_no", StringUtil.formatStr(map.get("stores_no")));
				resultMap.put("stores_name", StringUtil.formatStr(map.get("stores_name")));
				resultMap.put("stores_type_key", map.get("stores_type_key"));
				resultMap.put("business_type_key", StringUtil.formatStr(map.get("business_type_key")));
				resultMap.put("contact_person", map.get("contact_person"));
				resultMap.put("contact_tel", map.get("contact_tel"));
				resultMap.put("address", map.get("address"));
				resultMap.put("desc1", StringUtil.formatStr(map.get("desc1")));
				resultMap.put("area_id", StringUtil.formatStr(map.get("area_id")));
				resultMap.put("area_desc", StringUtil.formatStr(map.get("area_desc")));
				resultMap.put("label", StringUtil.formatStr(map.get("label")));
				resultMap.put("longitude", StringUtil.formatStr(map.get("longitude")));
				resultMap.put("latitude", StringUtil.formatStr(map.get("latitude")));
				// 如果街景为空,则设置街景为门头图
				String neighbor_pic_path = StringUtil.formatStr(map.get("neighbor_pic_path"));
				if (neighbor_pic_path.equals("")) {
					List<String> tempList = new ArrayList<String>();
					String new_facade_pic_path = StringUtil.formatStr(map.get("new_facade_pic_path"));
					if (!new_facade_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
					}
					String new_stores_pic_path = StringUtil.formatStr(map.get("new_stores_pic_path"));
					if (!new_stores_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
					}
					if (tempList.size() > 0) {
						neighbor_pic_path = tempList.get(0);
					}
				}
				resultMap.put("neighbor_pic_path", neighbor_pic_path);
				resultMap.put("logo_pic_path", StringUtil.formatStr(map.get("logo_pic_path")));
				// 处理图片
				doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("logo_pic_path")), ","));
				doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
				resultList.add(resultMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void recommendStoresListForGoodsFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "area_id" });
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> mDStoresList = storesDao.selectMDStoresRecommendInfo(paramsMap);
			for (Map<String, Object> map : mDStoresList) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("stores_id", map.get("stores_id"));
				resultList.add(resultMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public void storeAssistantInfoSync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id", "assistant_id", "assistant" });
			storesDao.updateMDStoresAssistant(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public void storeAssistantClear(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			paramsMap.put("assistant_id", "");
			paramsMap.put("assistant", "");
			storesDao.updateMDStoresAssistant(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public void storeAssistantFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			Map<String, Object> detailMap = new HashMap<String, Object>();
			detailMap.put("name", "");
			detailMap.put("mobile", "");
			detailMap.put("head_pic_path", "");
			detailMap.put("remark", "");
			detailMap.put("assistant_id", "");
			Map<String, Object> map = storesDao.selectStoresAssistant(paramsMap);
			if (null != map) {
				detailMap.put("assistant_id", StringUtil.formatStr(map.get("assistant_id")));
				detailMap.put("name", StringUtil.formatStr(map.get("assistant_name")));
				detailMap.put("mobile", StringUtil.formatStr(map.get("contact_tel")));
				detailMap.put("remark", StringUtil.formatStr(map.get("remark")));
				String head_pic_path = StringUtil.formatStr(map.get("head_pic_path"));
				if (!StringUtils.isEmpty(head_pic_path)) {
					detailMap.put("head_pic_path", docUtil.imageUrlFind(head_pic_path));
				}
			}
			result.setResultData(detailMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storesMemberRelCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id", "stores_id" });
			List<MDStoresMemberRel> storesMemberRelList = storesMemberListDao.selectMDStoresMemberRel(paramsMap);
			if (storesMemberRelList.size() == 0) {
				MDStoresMemberRel mdStoresMemberRel = new MDStoresMemberRel();
				BeanConvertUtil.mapToBean(mdStoresMemberRel, paramsMap);
				mdStoresMemberRel.setCreated_date(new Date());
				storesMemberListDao.insertMDStoresMemberRel(mdStoresMemberRel);
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
	public void salesmanStoresNumFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "salesman_id", "contact_tel"});
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("assistant_id", paramsMap.get("salesman_id"));
			Map<String, Object> assistantStoresNum = storesDao.selectSalesmanStoresNum(tempMap);
			tempMap.clear();
			tempMap.put("business_developer", paramsMap.get("contact_tel"));
			Map<String, Object> businessStoresNum = storesDao.selectSalesmanStoresNum(tempMap);
			tempMap.clear();
			Map<String, Object> Map = new HashMap<>();
			Map.put("assistantNum", assistantStoresNum.get("assistantnum").toString());
			Map.put("businessNum", businessStoresNum.get("businessnum").toString());
			result.setResultData(Map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storesForConsumerAssetListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			String stores_id = paramsMap.get("stores_id") + "";
			List<String> consumerIdList = storesRelConsumerIdList(stores_id);
			List<String> doc_ids = new ArrayList<String>();
			Map<String, Object> docTempMap = new HashMap<String, Object>();
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
						map.put("head_pic_path", StringUtil.formatStr(mDConsumer.getHead_pic_path()));
						map.put("full_name", StringUtil.formatStr(mDConsumer.getFull_name()));
						map.put("nick_name", StringUtil.formatStr(mDConsumer.getNick_name()));
						doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(mDConsumer.getHead_pic_path()), ","));
						docTempMap.put("doc_url", docUtil.imageUrlFind(doc_ids));
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
			tempMap.put("doc_url", docTempMap.get("doc_url"));
			tempMap.put("data_list", resultList);
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
	private List<String> storesRelConsumerIdList(String stores_id) 
			throws BusinessException, SystemException, Exception {
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
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return consumerIdList;
	}

}
