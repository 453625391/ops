package com.meitianhui.goods.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.meitianhui.goods.dao.GoodsDao;
import com.meitianhui.goods.dao.PlPartcipatorDao;
import com.meitianhui.goods.dao.PsGoodsDao;
import com.meitianhui.goods.entity.PlActivity;
import com.meitianhui.goods.entity.PlPartcipator;
import com.meitianhui.goods.entity.PsGoods;
import com.meitianhui.goods.service.PlPartcipatorService;

/**
 * 抽奖服务
 * 
 * @ClassName: PlPartcipatorServiceImpl
 * @author tiny
 * @date 2017年2月21日 上午11:22:08
 *
 */
@SuppressWarnings("unchecked")
@Service
public class PlPartcipatorServiceImpl implements PlPartcipatorService {
	@Autowired
	private DocUtil docUtil;
	@Autowired
	public GoodsDao goodsDao;

	@Autowired
	public PsGoodsDao psGoodsDao;

	@Autowired
	public PlPartcipatorDao plPartcipatorDao;

	@Override
	public void plActivityCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "title", "goods_id", "prize_qty", "lottery_time", "min_num" });
			// 判断活动商品是否存在
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", paramsMap.get("goods_id"));
			tempMap.put("category", "奖品");
			List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(tempMap);
			if (psGoodsList.size() == 0) {
				throw new BusinessException(RspCode.PL_ACTIVITY_ERROR, RspCode.MSG.get(RspCode.PS_GOODS_NOT_EXIST));
			}
			PsGoods psGoods = psGoodsList.get(0);
			// 开始上架活动
			PlActivity plActivity = new PlActivity();
			Date date = new Date();
			BeanConvertUtil.mapToBean(plActivity, paramsMap);
			plActivity.setActivity_id(IDUtil.getUUID());
			plActivity.setJson_data(psGoods.getPic_info());
			plActivity.setCreated_date(date);
			plActivity.setModified_date(date);
			plActivity.setTotal_num(0);
			plActivity.setStatus(Constant.STATUS_ONLINE);
			plPartcipatorDao.insertPlActivity(plActivity);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void plActivityListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			String status = StringUtil.formatStr(paramsMap.get("status"));
			if (!StringUtils.isEmpty(status)) {
				List<String> list = StringUtil.str2List(status, ",");
				if (list.size() > 1) {
					paramsMap.remove("status");
					paramsMap.put("status_in", list);
				}
			}
			List<Map<String, Object>> plActivityList = plPartcipatorDao.selectPlActivityForOp(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>(); // 存图片
			for (Map<String, Object> plActivityMap : plActivityList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("activity_id", plActivityMap.get("activity_id"));
				tempMap.put("title", plActivityMap.get("title"));
				tempMap.put("desc1", StringUtil.formatStr(plActivityMap.get("desc1")));
				tempMap.put("goods_id", plActivityMap.get("goods_id"));
				tempMap.put("goods_title", plActivityMap.get("goods_title"));
				String json_data = StringUtil.formatStr(plActivityMap.get("json_data"));
				if (!json_data.equals("")) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(json_data);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
					}
				}
				tempMap.put("json_data", StringUtil.formatStr(plActivityMap.get("json_data")));
				tempMap.put("prize_qty", plActivityMap.get("prize_qty") + "");
				tempMap.put("min_num", plActivityMap.get("min_num") + "");
				tempMap.put("lottery_time",
						DateUtil.date2Str((Date) plActivityMap.get("lottery_time"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("total_num", plActivityMap.get("total_num") + "");
				tempMap.put("status", StringUtil.formatStr(plActivityMap.get("status")));
				tempMap.put("created_date",
						DateUtil.date2Str((Date) plActivityMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("remark", StringUtil.formatStr(plActivityMap.get("remark")));
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
	public void plActivityListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			paramsMap.put("status", Constant.STATUS_ONLINE);
			List<Map<String, Object>> plActivityList = plPartcipatorDao.selectPlActivityForConsumer(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>(); // 存图片
			for (Map<String, Object> plActivityMap : plActivityList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("activity_id", plActivityMap.get("activity_id"));
				tempMap.put("title", plActivityMap.get("title"));
				tempMap.put("goods_title", plActivityMap.get("goods_title"));
				String json_data = StringUtil.formatStr(plActivityMap.get("json_data"));
				if (!json_data.equals("")) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(json_data);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
					}
				}
				tempMap.put("json_data", StringUtil.formatStr(plActivityMap.get("json_data")));
				tempMap.put("prize_qty", StringUtil.formatStr(plActivityMap.get("prize_qty")));
				tempMap.put("min_num", plActivityMap.get("min_num") + "");
				Date lottery_time = (Date) plActivityMap.get("lottery_time");
				tempMap.put("diff_time", (lottery_time.getTime() - new Date().getTime()) + "");
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
	public void plActivityListForConsumerDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
			List<Map<String, Object>> plActivityList = plPartcipatorDao.selectPlActivityForConsumer(paramsMap);
			// 存图片
			List<String> doc_ids = new ArrayList<String>();
			Map<String, Object> plActivityMap = plActivityList.get(0);
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", plActivityMap.get("activity_id"));
			tempMap.put("title", plActivityMap.get("title"));
			tempMap.put("desc1", StringUtil.formatStr(plActivityMap.get("desc1")));
			tempMap.put("goods_id", plActivityMap.get("goods_id"));
			tempMap.put("goods_title", plActivityMap.get("goods_title"));
			tempMap.put("goods_desc1", plActivityMap.get("goods_desc1"));
			String json_data = StringUtil.formatStr(plActivityMap.get("json_data"));
			if (!json_data.equals("")) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(json_data);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
			String goods_pic_detail_info = StringUtil.formatStr(plActivityMap.get("goods_pic_detail_info"));
			if (!goods_pic_detail_info.equals("")) {
				List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(goods_pic_detail_info);
				for (Map<String, Object> m : tempList) {
					if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
						doc_ids.add(m.get("path_id") + "");
					}
				}
			}
			tempMap.put("json_data", StringUtil.formatStr(plActivityMap.get("json_data")));
			tempMap.put("goods_pic_detail_info", StringUtil.formatStr(plActivityMap.get("goods_pic_detail_info")));
			tempMap.put("prize_qty", StringUtil.formatStr(plActivityMap.get("prize_qty")));
			tempMap.put("min_num", plActivityMap.get("min_num") + "");
			tempMap.put("lottery_time",
					DateUtil.date2Str((Date) plActivityMap.get("lottery_time"), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("total_num", StringUtil.formatStr(plActivityMap.get("total_num")));
			Date lottery_time = (Date) plActivityMap.get("lottery_time");
			tempMap.put("diff_time", (lottery_time.getTime() - new Date().getTime()) + "");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("detail", tempMap);
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
	public void plActivityCancel(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "remark" });
			// 判断活动是否存在
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", paramsMap.get("activity_id"));
			List<Map<String, Object>> plActivityList = plPartcipatorDao.selectPlActivityForOp(tempMap);
			if (plActivityList.size() == 0) {
				throw new BusinessException(RspCode.PL_ACTIVITY_ERROR, "活动不存在");
			}
			// 判断活动是否已经取消,防止重复取消
			Map<String, Object> plActivityMap = plActivityList.get(0);
			String status = plActivityMap.get("status") + "";
			if (!status.equals(Constant.STATUS_ONLINE)) {
				throw new BusinessException(RspCode.PL_ACTIVITY_ERROR, "活动已经结束，取消失败");
			}
			// 活动取消
			tempMap.put("status", Constant.STATUS_CANCELLED);
			tempMap.put("remark", paramsMap.get("remark"));
			tempMap.put("modified_date",
					DateUtil.date2Str((Date) plActivityMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
			int updateFlag = plPartcipatorDao.updatePlActivity(tempMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PL_ACTIVITY_ERROR, "操作失败,请刷新后重试");
			}
			// 用户返还抽奖次数
			tempMap.clear();
			tempMap.put("activity_id", paramsMap.get("activity_id"));
			plPartcipatorDao.deletePlPartcipatorByActivity(tempMap);

		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void plPartcipatorAdd(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "member_id", "member_type_key" });

			// 因为设计到跨库，因此先获取会员信息
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> paramMap = new HashMap<String, String>();
			reqParams.put("service", "member.userInfoFind");
			paramMap.put("member_id", paramsMap.get("member_id") + "");
			paramMap.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
			paramMap.put("is_admin", "Y");
			reqParams.put("params", FastJsonUtil.toJson(paramMap));
			String resultStr = HttpClientUtil.post(member_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> userInfoMap = (Map<String, Object>) resultMap.get("data");
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 登陆手机号
			tempMap.put("mobile", StringUtil.formatStr(userInfoMap.get("mobile")));
			String member_info = FastJsonUtil.toJson(tempMap);
			// 判断活动是否存在(需要查询状态为上线的活动)
			tempMap.clear();
			tempMap.put("activity_id", paramsMap.get("activity_id"));
			tempMap.put("status", Constant.STATUS_ONLINE);
			List<Map<String, Object>> plActivityList = plPartcipatorDao.selectPlActivityForOp(tempMap);
			if (plActivityList.size() == 0) {
				throw new BusinessException(RspCode.PL_ACTIVITY_ERROR, "活动已结束");
			}
			// 检测活动的开奖时间
			Map<String, Object> plActivity = plActivityList.get(0);
			Date lottery_time = (Date) plActivity.get("lottery_time");
			if (new Date().getTime() >= lottery_time.getTime()) {
				throw new BusinessException(RspCode.PL_ACTIVITY_ERROR, "活动已结束");
			}
			// 检测抽奖人的可用抽奖次数
			tempMap.clear();
			tempMap.put("member_id", paramsMap.get("member_id"));
			tempMap.put("member_type_key", paramsMap.get("member_type_key"));
			Map<String, Object> memberLotteryNum = goodsDao.callMemberLotteryNum(paramsMap);
			if (memberLotteryNum == null) {
				throw new BusinessException(RspCode.PL_ACTIVITY_ERROR, "您的抽奖次数已用尽");
			}
			String lottery_num = memberLotteryNum.get("lottery_num") + "";
			if (Integer.parseInt(lottery_num) <= 0) {
				throw new BusinessException(RspCode.PL_ACTIVITY_ERROR, "您的抽奖次数已用尽");
			}
			PlPartcipator plPartcipator = new PlPartcipator();
			Date date = new Date();
			BeanConvertUtil.mapToBean(plPartcipator, paramsMap);

			plPartcipator.setPartcipator_id(IDUtil.getUUID());
			// 设置会员信息
			plPartcipator.setMember_info(member_info);
			// 活动进行中
			plPartcipator.setStatus(Constant.STATUS_IN_PROGRESS);
			plPartcipator.setJoined_date(date);
			plPartcipator.setCreated_date(date);
			plPartcipator.setModified_date(date);
			plPartcipatorDao.insertPlPartcipator(plPartcipator);
			// 参加成功活动表添加参加次数
			tempMap.clear();
			tempMap.put("activity_id", paramsMap.get("activity_id") + "");
			tempMap.put("total_num", "1");
			plPartcipatorDao.updatePlActivity(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void plLuckyDeliveryListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			String status = StringUtil.formatStr(paramsMap.get("status"));
			if (!StringUtils.isEmpty(status)) {
				List<String> list = StringUtil.str2List(status, ",");
				if (list.size() > 1) {
					paramsMap.remove("status");
					paramsMap.put("status_in", list);
				}
			}
			List<Map<String, Object>> plActivityList = plPartcipatorDao.selectPlLuckyDeliveryForOp(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> plActivityMap : plActivityList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("lucky_delivery_id", plActivityMap.get("lucky_delivery_id"));
				tempMap.put("activity_id", plActivityMap.get("activity_id"));
				tempMap.put("title", plActivityMap.get("title"));
				tempMap.put("goods_title", plActivityMap.get("goods_title"));
				tempMap.put("desc1", StringUtil.formatStr(plActivityMap.get("desc1")));
				Map<String, Object> memberInfo = FastJsonUtil.jsonToMap(plActivityMap.get("member_info") + "");
				tempMap.put("mobile", memberInfo.get("mobile"));
				tempMap.put("contact_person", StringUtil.formatStr(plActivityMap.get("contact_person")));
				tempMap.put("contact_tel", StringUtil.formatStr(plActivityMap.get("contact_tel")));
				tempMap.put("delivery_area_id", StringUtil.formatStr(plActivityMap.get("delivery_area_id")));
				tempMap.put("delivery_address", StringUtil.formatStr(plActivityMap.get("delivery_address")));
				tempMap.put("logistics", StringUtil.formatStr(plActivityMap.get("logistics")));
				tempMap.put("status", plActivityMap.get("status"));
				tempMap.put("win_num", plActivityMap.get("win_num"));
				tempMap.put("created_date",
						DateUtil.date2Str((Date) plActivityMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("modified_date",
						DateUtil.date2Str((Date) plActivityMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("remark", StringUtil.formatStr(plActivityMap.get("remark")));
				tempMap.put("lucky_count", StringUtil.formatStr(plActivityMap.get("lucky_count")));
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
	public void plLuckyDeliveryListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
			List<Map<String, Object>> plActivityList = plPartcipatorDao.selectPlLuckyDeliveryForConsumer(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> plActivityMap : plActivityList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("lucky_delivery_id", plActivityMap.get("lucky_delivery_id"));
				tempMap.put("activity_id", plActivityMap.get("activity_id"));
				Map<String, Object> memberInfo = FastJsonUtil.jsonToMap(plActivityMap.get("member_info") + "");
				tempMap.put("mobile", memberInfo.get("mobile"));
				tempMap.put("title", plActivityMap.get("title"));
				tempMap.put("goods_title", plActivityMap.get("goods_title"));
				tempMap.put("desc1", StringUtil.formatStr(plActivityMap.get("desc1")));
				tempMap.put("contact_person", StringUtil.formatStr(plActivityMap.get("contact_person")));
				tempMap.put("contact_tel", StringUtil.formatStr(plActivityMap.get("contact_tel")));
				tempMap.put("delivery_area_id", StringUtil.formatStr(plActivityMap.get("delivery_area_id")));
				tempMap.put("delivery_address", StringUtil.formatStr(plActivityMap.get("delivery_address")));
				tempMap.put("logistics", StringUtil.formatStr(plActivityMap.get("logistics")));
				tempMap.put("status", plActivityMap.get("status"));
				tempMap.put("win_num", plActivityMap.get("win_num"));
				tempMap.put("created_date",
						DateUtil.date2Str((Date) plActivityMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("remark", StringUtil.formatStr(plActivityMap.get("remark")));
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

	@Deprecated
	@Override
	public void plPartcipatorListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> plPartcipatorList = plPartcipatorDao.selectPlPartcipatorForOp(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>(); // 存图片信息
			for (Map<String, Object> plPartcipatorMap : plPartcipatorList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("partcipator_id", plPartcipatorMap.get("partcipator_id"));
				tempMap.put("activity_id", plPartcipatorMap.get("activity_id"));
				tempMap.put("title", plPartcipatorMap.get("title"));
				tempMap.put("desc1", StringUtil.formatStr(plPartcipatorMap.get("desc1")));
				tempMap.put("status", plPartcipatorMap.get("status"));
				tempMap.put("json_data", StringUtil.formatStr(plPartcipatorMap.get("json_data")));
				String json_data = StringUtil.formatStr(plPartcipatorMap.get("json_data"));
				if (!json_data.equals("")) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(json_data);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
					}
				}
				tempMap.put("prize_qty", StringUtil.formatStr(plPartcipatorMap.get("prize_qty")));
				tempMap.put("logistics", StringUtil.formatStr(plPartcipatorMap.get("logistics")));
				tempMap.put("pa_status", StringUtil.formatStr(plPartcipatorMap.get("pa_status")));
				tempMap.put("joined_date",
						DateUtil.date2Str((Date) plPartcipatorMap.get("joined_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("created_date",
						DateUtil.date2Str((Date) plPartcipatorMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("remark", StringUtil.formatStr(plPartcipatorMap.get("remark")));
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
	public void plPartcipatorListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			List<Map<String, Object>> plPartcipatorList = plPartcipatorDao.selectPlPartcipatorForConsumer(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>(); // 存图片信息
			for (Map<String, Object> plPartcipatorMap : plPartcipatorList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("activity_id", plPartcipatorMap.get("activity_id"));
				tempMap.put("title", plPartcipatorMap.get("title"));
				tempMap.put("desc1", StringUtil.formatStr(plPartcipatorMap.get("desc1")));
				tempMap.put("contact_person", StringUtil.formatStr(plPartcipatorMap.get("contact_person")));
				tempMap.put("pa_status", StringUtil.formatStr(plPartcipatorMap.get("pa_status")));
				tempMap.put("contact_tel", StringUtil.formatStr(plPartcipatorMap.get("contact_tel")));
				tempMap.put("delivery_area_id", StringUtil.formatStr(plPartcipatorMap.get("delivery_area_id")));
				tempMap.put("delivery_address", StringUtil.formatStr(plPartcipatorMap.get("delivery_address")));
				tempMap.put("goods_title", plPartcipatorMap.get("goods_title"));
				tempMap.put("goods_desc1", plPartcipatorMap.get("goods_desc1"));
				tempMap.put("json_data", StringUtil.formatStr(plPartcipatorMap.get("json_data")));
				String json_data = StringUtil.formatStr(plPartcipatorMap.get("json_data"));
				if (!json_data.equals("")) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(json_data);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
					}
				}
				tempMap.put("lottery_time",
						DateUtil.date2Str((Date) plPartcipatorMap.get("lottery_time"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("prize_qty", StringUtil.formatStr(plPartcipatorMap.get("prize_qty")));
				tempMap.put("logistics", StringUtil.formatStr(plPartcipatorMap.get("logistics")));
				tempMap.put("pld_status", StringUtil.formatStr(plPartcipatorMap.get("pld_status")));
				String win_num = StringUtil.formatStr(plPartcipatorMap.get("win_num"));
				if (StringUtils.isEmpty(win_num)) {
					win_num = "0";
				}
				tempMap.put("win_num", win_num);
				tempMap.put("member_count", StringUtil.formatStr(plPartcipatorMap.get("member_count")));
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
	public void plLuckyDeliveryDelivered(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "lucky_delivery_id", "logistics" });
			// 判断活动是否存在
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("lucky_delivery_id", paramsMap.get("lucky_delivery_id"));
			tempMap.put("status", Constant.STATUS_UNDELIVERED);
			List<Map<String, Object>> plLuckyDeliveryList = plPartcipatorDao.selectPlLuckyDeliveryForOp(tempMap);
			if (plLuckyDeliveryList.size() == 0) {
				throw new BusinessException(RspCode.PL_ACTIVITY_ERROR, "中奖纪录不存在");
			}
			Map<String, Object> plLuckyDeliveryMap = plLuckyDeliveryList.get(0);

			// 发货
			tempMap.clear();
			tempMap.put("lucky_delivery_id", paramsMap.get("lucky_delivery_id"));
			tempMap.put("status", Constant.STATUS_DELIVERED);
			tempMap.put("remark", paramsMap.get("remark"));
			tempMap.put("logistics", paramsMap.get("logistics"));
			tempMap.put("modified_date",
					DateUtil.date2Str((Date) plLuckyDeliveryMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
			int updateFlag = plPartcipatorDao.updatePlLuckyDelivery(tempMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PL_ACTIVITY_ERROR, "操作失败,请刷新后重试");
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
