package com.meitianhui.goods.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.goods.constant.Constant;
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.dao.LdActivityDao;
import com.meitianhui.goods.entity.LdActivity;
import com.meitianhui.goods.entity.LdActivityProcess;
import com.meitianhui.goods.service.LdActivityService;

/**
 * 抽奖活动
 * 
 * @ClassName: LdActivityServiceImpl
 * @author tiny
 * @date 2017年2月21日 上午11:37:21
 *
 */
@SuppressWarnings("unchecked")
@Service
public class LdActivityServiceImpl implements LdActivityService {

	private static final Logger logger = Logger.getLogger(LdActivityServiceImpl.class);

	@Autowired
	private DocUtil docUtil;
	@Autowired
	public LdActivityDao ldActivityDao;

	@Override
	public void ldActivityCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id", "title", "end_date", "award_name",
				"award_value", "person_num", "award_pic_path1" });
		// 手动补全门店信息
		String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "member.storeFind");
		bizParams.put("stores_id", paramsMap.get("stores_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.post(member_service_url, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> storeInfo = (Map<String, Object>) resultMap.get("data");
		paramsMap.put("store_info", StringUtil.formatStr(storeInfo.get("stores_name")));
		paramsMap.put("stores_longitude", StringUtil.formatStr(storeInfo.get("longitude")));
		paramsMap.put("stores_latitude", StringUtil.formatStr(storeInfo.get("latitude")));
		Date created_date = new Date();
		LdActivity LdActivity = new LdActivity();
		BeanConvertUtil.mapToBean(LdActivity, paramsMap);
		LdActivity.setActivity_id(IDUtil.getUUID());
		LdActivity.setActivity_type(Constant.ACTIVITIES_TYPE_DSK);
		LdActivity.setStatus(Constant.STATUS_PROCESSING);
		LdActivity.setModified_date(created_date);
		LdActivity.setCreated_date(created_date);
		LdActivity.setJoined_num(0);

		// 更新活动中的门店经纬度
		bizParams.clear();
		bizParams.put("stores_id", LdActivity.getStores_id());
		bizParams.put("stores_longitude", LdActivity.getStores_latitude());
		bizParams.put("stores_longitude", LdActivity.getStores_longitude());
		ldActivityDao.updateLdActivity(bizParams);
		// 新增数据
		// 设置参数人数
		LdActivity.setJoined_num(0);
		ldActivityDao.insertLdActivity(LdActivity);
	}

	@Override
	public void ldYyyActivityCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "stores_id", "title", "award_name", "award_value", "award_pic_path1" });
		// 手动补全门店信息
		String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "member.storeFind");
		bizParams.put("stores_id", paramsMap.get("stores_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.post(member_service_url, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> storeInfo = (Map<String, Object>) resultMap.get("data");
		paramsMap.put("stores_info", StringUtil.formatStr(storeInfo.get("stores_name")));
		paramsMap.put("stores_longitude", StringUtil.formatStr(storeInfo.get("longitude")));
		paramsMap.put("stores_latitude", StringUtil.formatStr(storeInfo.get("latitude")));
		Date created_date = new Date();
		LdActivity LdActivity = new LdActivity();
		BeanConvertUtil.mapToBean(LdActivity, paramsMap);
		LdActivity.setActivity_id(IDUtil.getUUID());
		LdActivity.setActivity_type(Constant.ACTIVITIES_TYPE_YYY);
		LdActivity.setLuck_code((new Random()).nextInt(12) + ""); // 生成四位数的中奖号码
		LdActivity.setStatus(Constant.STATUS_PROCESSING);
		LdActivity.setEnd_date(created_date); // 结束时间默认给当前时间
		LdActivity.setModified_date(created_date);
		LdActivity.setCreated_date(created_date);
		LdActivity.setJoined_num(0);
		// 更新活动中的门店经纬度
		bizParams.clear();
		bizParams.put("stores_id", LdActivity.getStores_id());
		bizParams.put("stores_longitude", LdActivity.getStores_latitude());
		bizParams.put("stores_longitude", LdActivity.getStores_longitude());
		ldActivityDao.updateLdActivity(bizParams);

		// 新增数据
		ldActivityDao.insertLdActivity(LdActivity);
	}

	@Override
	public void ldGglActivityCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "stores_id", "title", "award_name", "award_value", "award_pic_path1" });
		// 手动补全门店信息
		String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "member.storeFind");
		bizParams.put("stores_id", paramsMap.get("stores_id"));
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		String resultStr = HttpClientUtil.post(member_service_url, reqParams);
		Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
			throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
		}
		Map<String, Object> storeInfo = (Map<String, Object>) resultMap.get("data");
		paramsMap.put("stores_info", StringUtil.formatStr(storeInfo.get("stores_name")));
		paramsMap.put("stores_longitude", StringUtil.formatStr(storeInfo.get("longitude")));
		paramsMap.put("stores_latitude", StringUtil.formatStr(storeInfo.get("latitude")));
		Date created_date = new Date();
		LdActivity LdActivity = new LdActivity();
		BeanConvertUtil.mapToBean(LdActivity, paramsMap);
		LdActivity.setActivity_id(IDUtil.getUUID());
		LdActivity.setLuck_code(IDUtil.generateCode(4)); // 生成四位数的中奖号码
		LdActivity.setActivity_type(Constant.ACTIVITIES_TYPE_GGL);
		LdActivity.setStatus(Constant.STATUS_PROCESSING);
		LdActivity.setEnd_date(created_date); // 结束时间默认给当前时间
		LdActivity.setModified_date(created_date);
		LdActivity.setCreated_date(created_date);
		LdActivity.setJoined_num(0);

		// 更新活动中的门店经纬度
		bizParams.clear();
		bizParams.put("stores_id", LdActivity.getStores_id());
		bizParams.put("stores_longitude", LdActivity.getStores_latitude());
		bizParams.put("stores_longitude", LdActivity.getStores_longitude());
		ldActivityDao.updateLdActivity(bizParams);
		// 新增数据
		ldActivityDao.insertLdActivity(LdActivity);
	}

	@Override
	public void ldYyyActivityLuckDrawFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "consumer_id", "draw_token" });
		String activity_id = paramsMap.get("activity_id") + "";
		// 先查看有没有抽奖权限
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("activity_id", activity_id);
		tempMap.put("consumer_id", paramsMap.get("consumer_id"));
		tempMap.put("draw_code", paramsMap.get("draw_token"));
		List<LdActivityProcess> ldActivityProcessList = ldActivityDao.selectLdActivityProcess(tempMap);
		if (ldActivityProcessList.size() == 0) {
			throw new BusinessException(RspCode.ACTIVITY_STATUS_ERROR, "您没有抽奖权限");
		}
		// 查询中奖号码
		tempMap.clear();
		tempMap.put("activity_id", activity_id);
		List<Map<String, Object>> activitiesDetailList = ldActivityDao.selectLdActivityDetail(tempMap);
		if (activitiesDetailList.size() == 0) {
			throw new BusinessException(RspCode.ACTIVITY_STATUS_ERROR, "活动不存在");
		}
		final String luck_code = StringUtil.formatStr(activitiesDetailList.get(0).get("luck_code"));
		// 生成抽奖池
		List<String> drawCodeSet = new ArrayList<String>();
		Random r = new Random();
		while (drawCodeSet.size() < 12) {
			String code = r.nextInt(12) + "";
			if (code.equals(luck_code)) {
				continue;
			}
			drawCodeSet.add(code);
		}
		drawCodeSet.set(r.nextInt(12), luck_code);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("draw_code_list", drawCodeSet);
		resultMap.put("luck_code", luck_code);
		resultMap.put("process_id", ldActivityProcessList.get(0).getProcess_id());
		result.setResultData(resultMap);
	}

	@Override
	public void ldGglActivityLuckDrawFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "consumer_id", "draw_token" });
		String activity_id = paramsMap.get("activity_id") + "";
		Map<String, Object> tempMap = new HashMap<String, Object>();
		// 先查看有没有抽奖权限
		tempMap.put("activity_id", activity_id);
		tempMap.put("consumer_id", paramsMap.get("consumer_id"));
		tempMap.put("draw_code", paramsMap.get("draw_token"));
		List<LdActivityProcess> ldActivityProcessList = ldActivityDao.selectLdActivityProcess(tempMap);
		if (ldActivityProcessList.size() == 0) {
			throw new BusinessException(RspCode.ACTIVITY_STATUS_ERROR, "您没有抽奖权限");
		}
		// 查询中奖号码
		tempMap.clear();
		tempMap.put("activity_id", activity_id);
		List<Map<String, Object>> activitiesDetailList = ldActivityDao.selectLdActivityDetail(tempMap);
		if (activitiesDetailList.size() == 0) {
			throw new BusinessException(RspCode.ACTIVITY_STATUS_ERROR, "活动不存在");
		}
		final String luck_code = StringUtil.formatStr(activitiesDetailList.get(0).get("luck_code"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("luck_code", luck_code);
		resultMap.put("process_id", ldActivityProcessList.get(0).getProcess_id());
		result.setResultData(resultMap);
	}

	/** 抽奖活动中奖，包换摇一摇与刮刮乐 **/
	@Override
	public void ldActivtiyWinning(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "process_id", "draw_code" });
		String process_id = paramsMap.get("process_id") + "";
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("process_id", process_id);
		tempMap.put("status", Constant.STATUS_MISS);
		List<LdActivityProcess> processList = ldActivityDao.selectLdActivityProcess(tempMap);
		if (processList == null || processList.size() < 1) {
			throw new BusinessException(RspCode.ACTIVITY_STATUS_ERROR, "抽奖资格已用完");
		}
		// 更新数据
		tempMap.clear();
		tempMap.put("process_id", process_id);
		tempMap.put("draw_code", paramsMap.get("draw_code"));
		tempMap.put("status", Constant.STATUS_WIN);
		tempMap.put("modified_date",
				DateUtil.date2Str(processList.get(0).getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		ldActivityDao.updateLdActivityProcess(tempMap);
	}

	@Override
	public void nearbyLdActivityStoreFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
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
		List<Map<String, Object>> storesList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		paramsMap.put("status", Constant.STATUS_PROCESSING);
		List<Map<String, Object>> storesLocationList = ldActivityDao.selectNearbyActivityStores(paramsMap);
		if (storesLocationList != null && storesLocationList.size() > 0) {
			List<String> storesIdList = new ArrayList<String>();
			Map<String, Object> storesLocationMap = new HashMap<String, Object>();
			for (Map<String, Object> map : storesLocationList) {
				String stores_id = map.get("stores_id") + "";
				if (!storesLocationMap.containsKey(stores_id)) {
					storesIdList.add(stores_id);
				}
				storesLocationMap.put(stores_id, map.get("distance"));
			}
			// 查询对应的门店信息
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			bizParams.put("stores_id", StringUtils.join(storesIdList, ","));
			reqParams.put("service", "member.storeListForGoodsFind");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
			List<Map<String, Object>> mDStoresList = (List<Map<String, Object>>) dataMap.get("list"); // 门店列表
			for (Map<String, Object> storesMap : mDStoresList) {
				final String stores_id = StringUtil.formatStr(storesMap.get("stores_id"));
				final String distance = StringUtil.formatStr(storesLocationMap.get(stores_id));
				if (StringUtils.isEmpty(distance)) {
					logger.warn("查询附近一元购信息，距离警告， storesLocationMap: " + storesLocationMap + ", storesIdList："
							+ storesIdList);
					continue;
				}
				bizParams.clear();
				bizParams.put("stores_id", stores_id);
				bizParams.put("status", Constant.STATUS_PROCESSING);
				List<Map<String, Object>> ldActivityList = ldActivityDao.selectLdActivityDetail(bizParams);
				if (ldActivityList == null || ldActivityList.size() < 1) {
					continue;
				}
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("stores_id", stores_id);
				tempMap.put("stores_no", StringUtil.formatStr(storesMap.get("stores_no")));
				tempMap.put("stores_name", StringUtil.formatStr(storesMap.get("stores_name")));
				tempMap.put("area_id", StringUtil.formatStr(storesMap.get("area_id")));
				tempMap.put("address", StringUtil.formatStr(storesMap.get("address")));
				tempMap.put("contact_person", StringUtil.formatStr(storesMap.get("contact_person")));
				tempMap.put("contact_tel", StringUtil.formatStr(storesMap.get("contact_tel")));
				tempMap.put("distance", distance);

				List<Map<String, Object>> activityList = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> map : ldActivityList) {
					Map<String, Object> activityMap = new HashMap<String, Object>();
					activityMap.put("activity_id", map.get("activity_id"));
					activityMap.put("activity_type", map.get("activity_type"));
					activityMap.put("title", map.get("title"));
					activityMap.put("desc1", map.get("desc1"));
					activityMap.put("end_date", map.get("end_date"));
					activityMap.put("award_name", map.get("award_name"));
					activityMap.put("award_value", map.get("award_value") + "");
					activityMap.put("person_num", map.get("person_num") + "");
					activityMap.put("total_person", map.get("total_person") + "");
					activityMap.put("award_pic_path1", map.get("award_pic_path1"));
					activityMap.put("award_pic_path2", map.get("award_pic_path2"));
					activityMap.put("award_pic_path3", map.get("award_pic_path3"));
					activityMap.put("award_pic_path4", map.get("award_pic_path4"));
					activityMap.put("award_pic_path5", map.get("award_pic_path5"));
					activityMap.put("status", map.get("status"));
					Date end_date = DateUtil.str2Date(map.get("end_date") + "", DateUtil.fmt_yyyyMMddHHmmss);
					tempMap.put("difftime", (end_date.getTime() - new Date().getTime()) / 1000);
					doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(storesMap.get("logo_pic_path")), ","));
					doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("award_pic_path1")), ","));
					doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("award_pic_path2")), ","));
					doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("award_pic_path3")), ","));
					doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("award_pic_path4")), ","));
					doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("award_pic_path5")), ","));
					activityList.add(activityMap);
				}
				tempMap.put("activity_list", activityList);
				storesList.add(tempMap);
			}
		}
		// 排序由近到远
		Collections.sort(storesList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> m1, Map<String, Object> m2) {
				double distance1 = Double.parseDouble(m1.get("distance") + "");
				double distance2 = Double.parseDouble(m2.get("distance") + "");
				if (distance1 > distance2) {
					return 1;
				} else if (distance1 == distance2) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", storesList);
		result.setResultData(map);
	}

	@Override
	public void ldActivityForMemberFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			String stores_id = StringUtil.formatStr(paramsMap.get("stores_id"));
			if (!StringUtils.isEmpty(stores_id)) {
				List<String> list = StringUtil.str2List(stores_id, ",");
				if (list.size() > 1) {
					paramsMap.remove("stores_id");
					paramsMap.put("stores_id_in", list);
				}
			}
			List<Map<String, Object>> LdActivityDetailList = ldActivityDao.selectLdActivityDetail(paramsMap);
			for (Map<String, Object> map : LdActivityDetailList) {
				map.put("stores_id", StringUtil.formatStr(map.get("stores_id")));
				map.put("desc1", StringUtil.formatStr(map.get("desc1")));
				map.put("end_date", DateUtil.date2Str((Date) map.get("end_date"), DateUtil.fmt_yyyyMMddHHmmss));
				map.put("person_num", StringUtil.formatStr(map.get("person_num")));
				map.put("joined_num", StringUtil.formatStr(map.get("joined_num")));
				map.put("total_person", map.get("total_person") + "");
				map.put("award_value", map.get("award_value") + "");
				map.put("award_pic_path2", StringUtil.formatStr(map.get("award_pic_path2")));
				map.put("award_pic_path3", StringUtil.formatStr(map.get("award_pic_path3")));
				map.put("award_pic_path4", StringUtil.formatStr(map.get("award_pic_path4")));
				map.put("award_pic_path5", StringUtil.formatStr(map.get("award_pic_path5")));
				map.put("luck_code", StringUtil.formatStr(map.get("luck_code")));
				map.put("remark", StringUtil.formatStr(map.get("remark")));
				map.put("total_person", map.get("total_person") + "");
			}
			result.setResultData(LdActivityDetailList);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void ldActivityFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> LdActivityDetailList = ldActivityDao.selectLdActivityDetail(paramsMap);
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			List<String> doc_ids = new ArrayList<String>();
			for (Map<String, Object> map : LdActivityDetailList) {
				map.put("desc1", StringUtil.formatStr(map.get("desc1")));
				map.put("end_date", DateUtil.date2Str((Date) map.get("end_date"), DateUtil.fmt_yyyyMMddHHmmss));
				map.put("created_date", DateUtil.date2Str((Date) map.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				map.put("person_num", StringUtil.formatStr(map.get("person_num")));
				map.put("joined_num", StringUtil.formatStr(map.get("joined_num")));
				map.put("award_value", map.get("award_value") + "");
				map.put("luck_code", StringUtil.formatStr(map.get("luck_code")));
				map.put("remark", StringUtil.formatStr(map.get("remark")));
				map.put("total_person", StringUtil.formatStr(map.get("total_person")));
				map.put("award_pic_path2", StringUtil.formatStr(map.get("award_pic_path2")));
				map.put("award_pic_path3", StringUtil.formatStr(map.get("award_pic_path3")));
				map.put("award_pic_path4", StringUtil.formatStr(map.get("award_pic_path4")));
				map.put("award_pic_path5", StringUtil.formatStr(map.get("award_pic_path5")));
				doc_ids.add(map.get("award_pic_path1") + "");
				if (!(map.get("award_pic_path2")).equals("")) {
					doc_ids.add(map.get("award_pic_path2") + "");
				}
				if (!(map.get("award_pic_path3")).equals("")) {
					doc_ids.add(map.get("award_pic_path3") + "");
				}
				if (!(map.get("award_pic_path4")).equals("")) {
					doc_ids.add(map.get("award_pic_path4") + "");
				}
				if (!(map.get("award_pic_path5")).equals("")) {
					doc_ids.add(map.get("award_pic_path5") + "");
				}
				// 获取门店信息
				reqParams.clear();
				bizParams.clear();
				reqParams.put("service", "member.storeForOrderFind");
				bizParams.put("stores_id", map.get("stores_id"));
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.post(member_service_url, reqParams);
				Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				Map<String, Object> storeInfo = (Map<String, Object>) resultMap.get("data");
				map.put("stores_name", StringUtil.formatStr(storeInfo.get("stores_name")));
				// 如果产生了幸运号码,则查询幸运号码的获得者
				if (!StringUtil.formatStr(map.get("luck_code")).equals("")) {
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("activity_id", map.get("activity_id"));
					tempMap.put("draw_code", map.get("luck_code"));
					List<LdActivityProcess> ldActivityProcessList = ldActivityDao.selectLdActivityProcess(tempMap);
					if (ldActivityProcessList.size() > 0) {
						LdActivityProcess ldActivityProcess = ldActivityProcessList.get(0);
						reqParams.clear();
						bizParams.clear();
						resultMap.clear();
						reqParams.put("service", "member.consumerFind");
						bizParams.put("member_id", ldActivityProcess.getConsumer_id());
						reqParams.put("params", FastJsonUtil.toJson(bizParams));
						resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
						resultMap = FastJsonUtil.jsonToMap(resultStr);
						if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
							throw new BusinessException((String) resultMap.get("error_code"),
									(String) resultMap.get("error_msg"));
						}
						Map<String, Object> consumerMap = (Map<String, Object>) resultMap.get("data");
						map.put("nick_name", StringUtil.formatStr(consumerMap.get("nick_name")));
						map.put("mobile", StringUtil.formatStr(consumerMap.get("mobile")));
					}
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", LdActivityDetailList);
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
	public void ldActivityListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> LdActivityDetailList = ldActivityDao.selectLdActivityDetail(paramsMap);
			List<String> doc_ids = new ArrayList<String>();
			for (Map<String, Object> map : LdActivityDetailList) {
				map.put("desc1", StringUtil.formatStr(map.get("desc1")));
				map.put("end_date", DateUtil.date2Str((Date) map.get("end_date"), DateUtil.fmt_yyyyMMddHHmmss));
				map.put("person_num", StringUtil.formatStr(map.get("person_num")));
				map.put("joined_num", StringUtil.formatStr(map.get("joined_num")));
				map.put("luck_code", StringUtil.formatStr(map.get("luck_code")));
				map.put("remark", StringUtil.formatStr(map.get("remark")));
				map.put("total_person", map.get("total_person") + "");
				Date end_date = DateUtil.str2Date(map.get("end_date") + "", DateUtil.fmt_yyyyMMddHHmmss);
				map.put("difftime", (end_date.getTime() - new Date().getTime()) / 1000);
				map.put("award_pic_path2", StringUtil.formatStr(map.get("award_pic_path2")));
				map.put("award_pic_path3", StringUtil.formatStr(map.get("award_pic_path3")));
				map.put("award_pic_path4", StringUtil.formatStr(map.get("award_pic_path4")));
				map.put("award_pic_path5", StringUtil.formatStr(map.get("award_pic_path5")));
				doc_ids.add(map.get("award_pic_path1") + "");
				if (!(map.get("award_pic_path2")).equals("")) {
					doc_ids.add(map.get("award_pic_path2") + "");
				}
				if (!(map.get("award_pic_path3")).equals("")) {
					doc_ids.add(map.get("award_pic_path3") + "");
				}
				if (!(map.get("award_pic_path4")).equals("")) {
					doc_ids.add(map.get("award_pic_path4") + "");
				}
				if (!(map.get("award_pic_path5")).equals("")) {
					doc_ids.add(map.get("award_pic_path5") + "");
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", LdActivityDetailList);
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
	public void ldActivityForStoreListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			List<Map<String, Object>> LdActivityDetailList = ldActivityDao.selectLdActivityDetail(paramsMap);
			List<String> doc_ids = new ArrayList<String>();
			for (Map<String, Object> map : LdActivityDetailList) {
				map.put("desc1", StringUtil.formatStr(map.get("desc1")));
				map.put("end_date", DateUtil.date2Str((Date) map.get("end_date"), DateUtil.fmt_yyyyMMddHHmmss));
				map.put("person_num", map.get("person_num") + "");
				map.put("luck_code", StringUtil.formatStr(map.get("luck_code")));
				map.put("remark", StringUtil.formatStr(map.get("remark")));
				map.put("total_person", map.get("total_person") + "");
				map.put("award_pic_path2", StringUtil.formatStr(map.get("award_pic_path2")));
				map.put("award_pic_path3", StringUtil.formatStr(map.get("award_pic_path3")));
				map.put("award_pic_path4", StringUtil.formatStr(map.get("award_pic_path4")));
				map.put("award_pic_path5", StringUtil.formatStr(map.get("award_pic_path5")));
				doc_ids.add(map.get("award_pic_path1") + "");
				if (!(map.get("award_pic_path2")).equals("")) {
					doc_ids.add(map.get("award_pic_path2") + "");
				}
				if (!(map.get("award_pic_path3")).equals("")) {
					doc_ids.add(map.get("award_pic_path3") + "");
				}
				if (!(map.get("award_pic_path4")).equals("")) {
					doc_ids.add(map.get("award_pic_path4") + "");
				}
				if (!(map.get("award_pic_path5")).equals("")) {
					doc_ids.add(map.get("award_pic_path5") + "");
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", LdActivityDetailList);
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
	public void ldActivityPayInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "consumer_id", "activity_type" });
			String activity_id = paramsMap.get("activity_id") + "";
			String activity_type = paramsMap.get("activity_type") + "";

			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_PROCESSING);
			List<Map<String, Object>> activitiesDetailList = ldActivityDao.selectLdActivityDetail(tempMap);
			if (activitiesDetailList.size() == 0) {
				throw new BusinessException(RspCode.ACTIVITY_STATUS_ERROR, "活动已结束");
			}
			Map<String, Object> activitiesDetail = activitiesDetailList.get(0);
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("activity_type", activity_type);
			tempMap.put("award_name", activitiesDetail.get("award_name"));
			tempMap.put("stores_id", activitiesDetail.get("stores_id"));
			tempMap.put("consumer_id", paramsMap.get("consumer_id"));
			if (activity_type.equals(Constant.ACTIVITIES_TYPE_DSK)) {
				// 定时开
				ValidateUtil.validateParams(paramsMap, new String[] { "qty" });
				// 商品开奖前一分钟不能在进行购买操作
				Date end_date = (Date) activitiesDetailList.get(0).get("end_date");
				if (new Date().getTime() >= end_date.getTime()) {
					throw new BusinessException(RspCode.ACTIVITY_STATUS_ERROR, "活动已结束");
				}
				tempMap.put("qty", paramsMap.get("qty"));
				tempMap.put("total_fee", paramsMap.get("qty"));
			} else {
				tempMap.put("draw_token", IDUtil.getShortUUID());
				tempMap.put("total_fee", "1");
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pay_info", tempMap);
			result.setResultData(paramMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void dskActivityProcessCreateNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "consumer_id", "qty" });
			String activity_id = paramsMap.get("activity_id") + "";
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_PROCESSING);
			List<Map<String, Object>> activitiesDetailList = ldActivityDao.selectLdActivityDetail(tempMap);
			if (activitiesDetailList.size() == 0) {
				throw new BusinessException(RspCode.ACTIVITY_STATUS_ERROR, "活动已结束");
			}
			// 商品开奖前一分钟不能在进行购买操作
			Date end_date = (Date) activitiesDetailList.get(0).get("end_date");
			if (new Date().getTime() >= end_date.getTime()) {
				throw new BusinessException(RspCode.ACTIVITY_STATUS_ERROR, "活动已结束");
			}
			Integer qty = Integer.parseInt(paramsMap.get("qty") + "");
			for (int i = 0; i < qty; i++) {
				LdActivityProcess ldActivityProcess = new LdActivityProcess();
				BeanConvertUtil.mapToBean(ldActivityProcess, paramsMap);
				Date date = new Date();
				ldActivityProcess.setProcess_id(IDUtil.getUUID());
				ldActivityProcess.setStatus(Constant.STATUS_PROCESSING);
				ldActivityProcess.setCreated_date(date);
				ldActivityProcess.setModified_date(date);
				ldActivityDao.insertDskActivityProcess(ldActivityProcess);
			}
			// 更新参与人数
			tempMap.put("activity_id", activity_id);
			tempMap.put("add_num", qty + "");
			ldActivityDao.updateLdActivity(tempMap);

		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void yyyActivityProcessCreateNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "consumer_id", "draw_token" });
			LdActivityProcess ldActivityProcess = new LdActivityProcess();
			BeanConvertUtil.mapToBean(ldActivityProcess, paramsMap);
			Date date = new Date();
			ldActivityProcess.setProcess_id(IDUtil.getUUID());
			ldActivityProcess.setStatus(Constant.STATUS_MISS);
			ldActivityProcess.setDraw_code(paramsMap.get("draw_token") + "");
			ldActivityProcess.setCreated_date(date);
			ldActivityProcess.setModified_date(date);
			ldActivityDao.insertLdActivityProcess(ldActivityProcess);
			// 更新参与人数
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", ldActivityProcess.getActivity_id());
			tempMap.put("add_num", "1");
			ldActivityDao.updateLdActivity(tempMap);

		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void gglActivityProcessCreateNotify(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "consumer_id", "draw_token" });
			LdActivityProcess ldActivityProcess = new LdActivityProcess();
			BeanConvertUtil.mapToBean(ldActivityProcess, paramsMap);
			Date date = new Date();
			ldActivityProcess.setProcess_id(IDUtil.getUUID());
			ldActivityProcess.setStatus(Constant.STATUS_MISS);
			ldActivityProcess.setDraw_code(paramsMap.get("draw_token") + "");
			ldActivityProcess.setCreated_date(date);
			ldActivityProcess.setModified_date(date);
			ldActivityDao.insertLdActivityProcess(ldActivityProcess);
			// 更新参与人数
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", ldActivityProcess.getActivity_id());
			tempMap.put("add_num", "1");
			ldActivityDao.updateLdActivity(tempMap);

		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void ldActivityProcessForStoreFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<LdActivityProcess> ldActivityProcessList = ldActivityDao.selectLdActivityProcessForStore(paramsMap);
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			for (LdActivityProcess ldActivityProcess : ldActivityProcessList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("activity_id", ldActivityProcess.getActivity_id());
				tempMap.put("consumer_id", ldActivityProcess.getConsumer_id());
				tempMap.put("draw_code", ldActivityProcess.getDraw_code());
				tempMap.put("created_date",
						DateUtil.date2Str(ldActivityProcess.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("status", ldActivityProcess.getStatus());
				// 查询消费者昵称信息
				reqParams.clear();
				bizParams.clear();
				resultMap.clear();
				reqParams.put("service", "member.consumerFind");
				bizParams.put("member_id", ldActivityProcess.getConsumer_id());
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				Map<String, Object> consumerMap = (Map<String, Object>) resultMap.get("data");
				tempMap.put("nick_name", StringUtil.formatStr(consumerMap.get("nick_name")));

				// 查询登陆手机号
				reqParams.clear();
				bizParams.clear();
				resultMap.clear();
				reqParams.put("service", "member.userInfoFind");
				bizParams.put("member_id", ldActivityProcess.getConsumer_id());
				bizParams.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				Map<String, Object> userInfoMap = (Map<String, Object>) resultMap.get("data");
				tempMap.put("mobile", StringUtil.formatStr(userInfoMap.get("mobile")));
				resultList.add(tempMap);
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
	public void ldActivityProcessForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<LdActivityProcess> ldActivityProcessList = ldActivityDao.selectLdActivityProcess(paramsMap);
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			List<String> doc_ids = new ArrayList<String>();
			for (LdActivityProcess ldActivityProcess : ldActivityProcessList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				// 活动详情
				bizParams.clear();
				bizParams.put("activity_id", ldActivityProcess.getActivity_id());
				List<Map<String, Object>> LdActivityDetailList = ldActivityDao.selectLdActivityDetail(bizParams);
				if (LdActivityDetailList.size() > 0) {
					Map<String, Object> LdActivityDetailMap = LdActivityDetailList.get(0);
					tempMap.put("activity_id", ldActivityProcess.getActivity_id());
					tempMap.put("activity_type", LdActivityDetailMap.get("activity_type"));
					tempMap.put("stores_id", LdActivityDetailMap.get("stores_id") + "");
					tempMap.put("award_name", LdActivityDetailMap.get("award_name") + "");
					tempMap.put("title", LdActivityDetailMap.get("title") + "");
					tempMap.put("desc1", StringUtil.formatStr(LdActivityDetailMap.get("desc1")));
					tempMap.put("person_num", LdActivityDetailMap.get("person_num") + "");
					tempMap.put("joined_num ", StringUtil.formatStr(LdActivityDetailMap.get("joined_num")));
					tempMap.put("total_person", LdActivityDetailMap.get("total_person") + "");
					tempMap.put("award_pic_path1", StringUtil.formatStr(LdActivityDetailMap.get("award_pic_path1")));
					tempMap.put("award_pic_path2", StringUtil.formatStr(LdActivityDetailMap.get("award_pic_path2")));
					tempMap.put("award_pic_path3", StringUtil.formatStr(LdActivityDetailMap.get("award_pic_path3")));
					tempMap.put("award_pic_path4", StringUtil.formatStr(LdActivityDetailMap.get("award_pic_path4")));
					tempMap.put("award_pic_path5", StringUtil.formatStr(LdActivityDetailMap.get("award_pic_path5")));
					tempMap.put("consumer_id", ldActivityProcess.getConsumer_id());
					tempMap.put("process_id", ldActivityProcess.getProcess_id());
					tempMap.put("draw_code", ldActivityProcess.getDraw_code());
					tempMap.put("created_date",
							DateUtil.date2Str(ldActivityProcess.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
					tempMap.put("status", ldActivityProcess.getStatus());
					doc_ids.add(tempMap.get("award_pic_path1") + "");
					if (!(tempMap.get("award_pic_path2")).equals("")) {
						doc_ids.add(tempMap.get("award_pic_path2") + "");
					}
					if (!(tempMap.get("award_pic_path3")).equals("")) {
						doc_ids.add(tempMap.get("award_pic_path3") + "");
					}
					if (!(tempMap.get("award_pic_path4")).equals("")) {
						doc_ids.add(tempMap.get("award_pic_path4") + "");
					}
					if (!(tempMap.get("award_pic_path5")).equals("")) {
						doc_ids.add(tempMap.get("award_pic_path5") + "");
					}
					reqParams.clear();
					bizParams.clear();
					resultMap.clear();
					reqParams.put("service", "member.storeForOrderFind");
					bizParams.put("stores_id", tempMap.get("stores_id"));
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					String resultStr = HttpClientUtil.post(member_service_url, reqParams);
					resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						throw new BusinessException((String) resultMap.get("error_code"),
								(String) resultMap.get("error_msg"));
					}
					Map<String, Object> storeInfo = (Map<String, Object>) resultMap.get("data");
					tempMap.put("stores_name", StringUtil.formatStr(storeInfo.get("stores_name")));
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

	/***
	 * 消费者端获取门店正在进行活动的列表
	 */
	@Override
	public void ldActivityListByStoresForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
		List<Map<String, Object>> activityList = new ArrayList<Map<String, Object>>();
		List<String> doc_ids = new ArrayList<String>();
		paramsMap.put("status", Constant.STATUS_PROCESSING);
		List<Map<String, Object>> ldActivityList = ldActivityDao.selectLdActivityDetail(paramsMap);
		for (Map<String, Object> map : ldActivityList) {
			Map<String, Object> activityMap = new HashMap<String, Object>();
			activityMap.put("activity_id", map.get("activity_id"));
			activityMap.put("activity_type", map.get("activity_type"));
			activityMap.put("title", map.get("title"));
			activityMap.put("desc1", map.get("desc1"));
			activityMap.put("end_date", map.get("end_date"));
			activityMap.put("award_name", map.get("award_name"));
			activityMap.put("award_value", map.get("award_value") + "");
			activityMap.put("person_num", map.get("person_num") + "");
			activityMap.put("total_person", map.get("total_person") + "");
			activityMap.put("award_pic_path1", map.get("award_pic_path1"));
			activityMap.put("award_pic_path2", map.get("award_pic_path2"));
			activityMap.put("award_pic_path3", map.get("award_pic_path3"));
			activityMap.put("award_pic_path4", map.get("award_pic_path4"));
			activityMap.put("award_pic_path5", map.get("award_pic_path5"));
			activityMap.put("status", map.get("status"));
			Date end_date = DateUtil.str2Date(map.get("end_date") + "", DateUtil.fmt_yyyyMMddHHmmss);
			activityMap.put("difftime", (end_date.getTime() - new Date().getTime()) / 1000);
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("award_pic_path1")), ","));
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("award_pic_path2")), ","));
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("award_pic_path3")), ","));
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("award_pic_path4")), ","));
			doc_ids.addAll(StringUtil.str2List(StringUtil.formatStr(map.get("award_pic_path5")), ","));
			activityList.add(activityMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("list", activityList);
		result.setResultData(map);
	}

	/***
	 * 抽奖活动参与的消费者列表
	 */
	@Override
	public void ldActivityConsumerListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<LdActivityProcess> ldActivityProcessList = ldActivityDao.selectLdActivityProcessForStore(paramsMap);
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
		for (LdActivityProcess ldActivityProcess : ldActivityProcessList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", ldActivityProcess.getActivity_id());
			tempMap.put("consumer_id", ldActivityProcess.getConsumer_id());
			tempMap.put("draw_code", ldActivityProcess.getDraw_code());
			tempMap.put("created_date",
					DateUtil.date2Str(ldActivityProcess.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("status", ldActivityProcess.getStatus());
			// 查询消费者昵称信息
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "member.consumerFind");
			bizParams.put("member_id", ldActivityProcess.getConsumer_id());
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> consumerMap = (Map<String, Object>) resultMap.get("data");
			tempMap.put("nick_name", StringUtil.formatStr(consumerMap.get("nick_name")));
			// 查询登陆手机号
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "member.userInfoFind");
			bizParams.put("member_id", ldActivityProcess.getConsumer_id());
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> userInfoMap = (Map<String, Object>) resultMap.get("data");
			tempMap.put("mobile", StringUtil.formatStr(userInfoMap.get("mobile")));
			resultList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		result.setResultData(map);
	}

	@Override
	public void ldActivityValidateFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "activity_type", "stores_id", "activity_id", "process_id", "luck_code" });
			Map<String, Object> ldActivityMap = ldActivityDao.selectLdActivityForValidate(paramsMap);
			if (null == ldActivityMap) {
				throw new BusinessException(RspCode.ACTIVITY_LUCK_CODE_ERROR, "无效二维码");
			}
			Map<String, Object> resultDataMap = new HashMap<String, Object>();
			resultDataMap.put("award_name", ldActivityMap.get("award_name"));
			resultDataMap.put("activity_type", ldActivityMap.get("activity_type"));
			resultDataMap.put("title", ldActivityMap.get("title"));
			resultDataMap.put("created_date",
					DateUtil.date2Str((Date) ldActivityMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
			// 解析图片
			String award_pic_path1 = ldActivityMap.get("award_pic_path1") + "";
			List<String> url_list = new ArrayList<String>();
			url_list.add(award_pic_path1);
			Map<String, Object> url_map = docUtil.imageUrlFind(url_list);
			resultDataMap.put("award_pic_path1_url", url_map.get(award_pic_path1));
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
	public void handleLdActivityValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			String activity_type = StringUtil.formatStr(paramsMap.get("activity_type"));
			if (activity_type.equals(Constant.ACTIVITIES_TYPE_GGL)
					|| activity_type.equals(Constant.ACTIVITIES_TYPE_YYY)) {
				// 刮刮乐 和 摇一摇
				yyyAndgglActivityValidate(paramsMap, result);
			} else {
				// 定时开
				dskActivityValidate(paramsMap, result);
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
	 * 定时开验证
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	private void dskActivityValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "stores_id", "activity_id", "consumer_id", "draw_code" });
			String activity_id = paramsMap.get("activity_id") + "";
			String draw_code = paramsMap.get("draw_code") + "";
			String stores_id = paramsMap.get("stores_id") + "";
			String consumer_id = paramsMap.get("consumer_id") + "";
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 检查抽奖号码是否正确
			tempMap.put("activity_id", activity_id);
			tempMap.put("draw_code", draw_code);
			tempMap.put("consumer_id", consumer_id);
			tempMap.put("status", Constant.STATUS_WIN);
			List<LdActivityProcess> processList = ldActivityDao.selectLdActivityProcess(tempMap);
			if (processList.size() == 0) {
				throw new BusinessException(RspCode.ACTIVITY_LUCK_CODE_ERROR, "无效二维码");
			}
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("stores_id", stores_id);
			// 查询优惠活动
			List<Map<String, Object>> LdActivityDetailList = ldActivityDao.selectLdActivityDetail(tempMap);
			if (LdActivityDetailList.size() == 0) {
				throw new BusinessException(RspCode.ACTIVITY_NOT_EXIST, RspCode.MSG.get(RspCode.ACTIVITY_NOT_EXIST));
			}
			Map<String, Object> LdActivityDetailMap = LdActivityDetailList.get(0);
			// 验证号码是否为中奖号码
			String luck_code = StringUtil.formatStr(LdActivityDetailMap.get("luck_code"));
			if (!(luck_code).equals(draw_code)) {
				throw new BusinessException(RspCode.ACTIVITY_LUCK_CODE_ERROR,
						RspCode.MSG.get(RspCode.ACTIVITY_LUCK_CODE_ERROR));
			}
			// 获取状态验证
			String status = LdActivityDetailMap.get("status") + "";
			if (status.equals(Constant.STATUS_PROCESSING)) {
				throw new BusinessException(RspCode.ACTIVITY_PROCESSING, RspCode.MSG.get(RspCode.ACTIVITY_PROCESSING));
			} else if (status.equals(Constant.STATUS_EXCHANGED) || status.equals(Constant.STATUS_CANCEL)) {
				throw new BusinessException(RspCode.ACTIVITY_STATUS_ERROR, "无效二维码");
			}
			// 更新优惠活动的状态
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_EXCHANGED);
			tempMap.put("modified_date",
					DateUtil.date2Str((Date) LdActivityDetailMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
			int updateFlag = ldActivityDao.updateLdActivity(tempMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PROCESSING, "无效二维码");
			}
			String process_id = processList.get(0).getProcess_id();
			// 更新抽奖号码的状态
			tempMap.clear();
			tempMap.put("process_id", process_id);
			tempMap.put("status", Constant.STATUS_EXCHANGED);
			ldActivityDao.updateLdActivityProcess(tempMap);

			// 将一元购商品的金额结算给门店
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			reqParams.put("service", "finance.orderPay");
			bizParams.put("data_source", "SJLY_02");
			bizParams.put("payment_way_key", "ZFFS_05");
			bizParams.put("detail", "一元购");
			bizParams.put("amount", LdActivityDetailMap.get("total_person") + "");
			bizParams.put("out_trade_no", activity_id);
			bizParams.put("buyer_id", Constant.MEMBER_ID_MTH);
			bizParams.put("seller_id", stores_id);
			Map<String, Object> out_trade_body = new HashMap<String, Object>();
			out_trade_body.put("stores_id", stores_id);
			out_trade_body.put("total_person", LdActivityDetailMap.get("total_person") + "");
			out_trade_body.put("activity_id", LdActivityDetailMap.get("activity_id") + "");
			out_trade_body.put("award_name", LdActivityDetailMap.get("award_name") + "");
			bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(finance_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}

			tempMap.clear();
			tempMap.put("title", LdActivityDetailMap.get("title"));
			tempMap.put("award_name", LdActivityDetailMap.get("award_name"));
			tempMap.put("luck_code", luck_code);
			result.setResultData(tempMap);
			// 推送通知
			Map<String, String> extrasMap = new HashMap<String, String>();
			extrasMap.put("type", Constant.PUSH_MESSAGE_TYPE_03);
			extrasMap.put("luck_code", draw_code);
			extrasMap.put("process_id", process_id);
			String msg = "验证成功";
			pushAppMessage(consumer_id, msg, extrasMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 呱呱乐抽奖验证
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	private void yyyAndgglActivityValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "stores_id", "activity_id", "consumer_id", "process_id", "draw_code" });
			String activity_id = paramsMap.get("activity_id") + "";
			String draw_code = paramsMap.get("draw_code") + "";
			String process_id = paramsMap.get("process_id") + "";
			String stores_id = paramsMap.get("stores_id") + "";
			String consumer_id = paramsMap.get("consumer_id") + "";
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 检查抽奖号码是否正确
			tempMap.put("activity_id", activity_id);
			tempMap.put("process_id", process_id);
			tempMap.put("draw_code", draw_code);
			tempMap.put("consumer_id", paramsMap.get("consumer_id"));
			tempMap.put("status", Constant.STATUS_WIN);
			List<LdActivityProcess> processList = ldActivityDao.selectLdActivityProcess(tempMap);
			if (processList.size() == 0) {
				throw new BusinessException(RspCode.ACTIVITY_LUCK_CODE_ERROR, "无效二维码");
			}
			// 验证号码是否为中奖号码
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("stores_id", stores_id);
			tempMap.put("luck_code", draw_code);
			List<LdActivity> ldActivityList = ldActivityDao.selectLdActivity(tempMap);
			if (ldActivityList.size() == 0) {
				throw new BusinessException(RspCode.ACTIVITY_NOT_EXIST, RspCode.MSG.get(RspCode.ACTIVITY_NOT_EXIST));
			}
			LdActivity ldActivity = ldActivityList.get(0);

			// 更新抽奖号码的状态
			tempMap.clear();
			tempMap.put("process_id", process_id);
			tempMap.put("status", Constant.STATUS_EXCHANGED);
			tempMap.put("modified_date",
					DateUtil.date2Str(processList.get(0).getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
			int updateFlag = ldActivityDao.updateLdActivityProcess(tempMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
			}
			tempMap.clear();
			tempMap.put("title", ldActivity.getTitle());
			tempMap.put("award_name", ldActivity.getAward_name());
			tempMap.put("luck_code", draw_code);
			result.setResultData(tempMap);
			// 推送通知
			Map<String, String> extrasMap = new HashMap<String, String>();
			extrasMap.put("type", Constant.PUSH_MESSAGE_TYPE_03);
			extrasMap.put("luck_code", draw_code);
			extrasMap.put("process_id", process_id);
			String msg = "验证成功";
			pushAppMessage(consumer_id, msg, extrasMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void handleLdActivityCancel(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "remark" });
			String activity_id = paramsMap.get("activity_id") + "";
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", activity_id);
			List<Map<String, Object>> LdActivityDetailList = ldActivityDao.selectLdActivityDetail(tempMap);
			if (LdActivityDetailList.size() == 0) {
				throw new BusinessException(RspCode.ACTIVITY_NOT_EXIST, RspCode.MSG.get(RspCode.ACTIVITY_NOT_EXIST));
			}
			Map<String, Object> LdActivityDetail = LdActivityDetailList.get(0);

			if (!LdActivityDetail.get("status").equals(Constant.STATUS_PROCESSING)) {
				throw new BusinessException(RspCode.PROCESSING, RspCode.MSG.get(RspCode.PROCESSING));
			}
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_CANCEL);
			tempMap.put("modified_date",
					DateUtil.date2Str((Date) LdActivityDetail.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("remark", paramsMap.get("remark"));
			int updateFlag = ldActivityDao.updateLdActivity(tempMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PROCESSING, "活动正在处理中");
			}
			// 加入判断，只有一元购的取消才退款
			if (Constant.ACTIVITIES_TYPE_DSK.equals(LdActivityDetail.get("activity_type"))) {
				tempMap.clear();
				tempMap.put("activity_id", activity_id);
				List<Map<String, Object>> ldActivityProcessCountList = ldActivityDao
						.selectLdActivityProcessCount(tempMap);
				// 退款
				String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
				Map<String, String> reqParams = new HashMap<String, String>();
				Map<String, Object> bizParams = new HashMap<String, Object>();
				for (Map<String, Object> ldActivityProcessCount : ldActivityProcessCountList) {
					reqParams.clear();
					bizParams.clear();
					reqParams.put("service", "finance.orderRefund");
					bizParams.put("data_source", "SJLY_02");
					bizParams.put("order_type_key", "DDLX_03");
					bizParams.put("payment_way_key", "ZFFS_05");
					bizParams.put("detail", "一元购退款");
					bizParams.put("amount", ldActivityProcessCount.get("count_num") + "");
					bizParams.put("out_trade_no", activity_id);
					bizParams.put("buyer_id", ldActivityProcessCount.get("consumer_id"));
					bizParams.put("seller_id", Constant.MEMBER_ID_MTH);

					Map<String, Object> out_trade_body = new HashMap<String, Object>();
					out_trade_body.put("activity_id", activity_id);
					out_trade_body.put("count_num", ldActivityProcessCount.get("count_num") + "");
					out_trade_body.put("consumer_id", ldActivityProcessCount.get("consumer_id") + "");
					out_trade_body.put("award_name", LdActivityDetail.get("award_name") + "");
					bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					String resultStr = HttpClientUtil.post(finance_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						logger.error("消费者【" + ldActivityProcessCount.get("consumer_id") + "】退款"
								+ ldActivityProcessCount.get("count_num") + "元失败");
					}
					tempMap.clear();
					tempMap.put("activity_id", activity_id);
					tempMap.put("consumer_id", ldActivityProcessCount.get("consumer_id"));
					tempMap.put("status", Constant.STATUS_REFUNDED);
					tempMap.put("remark", "店东取消活动");
					ldActivityDao.updateLdActivityProcess(tempMap);
					logger.info("店东取消活动【" + activity_id + "】,进行退款操作");
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

	/**
	 * 推送消息
	 * 
	 * @param member_id
	 * @param msg
	 * @param extrasMap
	 */
	private void pushAppMessage(final String member_id, final String msg, final Map<String, String> extrasMap) {
		try {
			String notification_service_url = PropertiesConfigUtil.getProperty("notification_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("service", "notification.pushMessage");
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("msg_title", "验证通知");
			paramMap.put("msg_content", msg);
			paramMap.put("member_id", member_id);
			paramMap.put("extrasparam", FastJsonUtil.toJson(extrasMap));
			reqParams.put("params", FastJsonUtil.toJson(paramMap));
			String resultStr = HttpClientUtil.post(notification_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			String rsp_code = (String) resultMap.get("rsp_code");
			if (!rsp_code.equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.error(e.getMsg());
		} catch (SystemException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
