package com.meitianhui.goods.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.goods.constant.Constant;
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.dao.GcActivityDao;
import com.meitianhui.goods.dao.GdActivityDao;
import com.meitianhui.goods.dao.GdBenefitDao;
import com.meitianhui.goods.dao.GoodsDao;
import com.meitianhui.goods.dao.LdActivityDao;
import com.meitianhui.goods.dao.PlPartcipatorDao;
import com.meitianhui.goods.dao.PsGoodsDao;
import com.meitianhui.goods.entity.GdViewSell;
import com.meitianhui.goods.entity.LdActivityProcess;
import com.meitianhui.goods.entity.PlLuckyDelivery;
import com.meitianhui.goods.service.GoodsTaskService;

/**
 * 商品任务
 * 
 * @ClassName: GoodsTaskServiceImpl
 * @author tiny
 * @date 2017年2月21日 上午11:37:46
 *
 */
@Service
@SuppressWarnings("unchecked")
public class GoodsTaskServiceImpl implements GoodsTaskService {

	private static final Logger logger = Logger.getLogger(GoodsTaskServiceImpl.class);

	@Autowired
	public RedisUtil redisUtil;

	@Autowired
	public PlPartcipatorDao plPartcipatorDao;
	@Autowired
	public PsGoodsDao psGoodsDao;
	@Autowired
	public LdActivityDao ldActivityDao;
	@Autowired
	public GoodsDao goodsDao;
	@Autowired
	public GdBenefitDao gdBenefitDao;
	@Autowired
	public GdActivityDao gdActivityDao;
	@Autowired
	public GcActivityDao gcActivityDao;

	/**
	 * 刷新失效优惠券状态
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	@Override
	public void couponStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {

		// 会员过期的优惠券修改为失效
		String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		reqParams.put("service", "finance.disabledCouponStatusUpdate");
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		HttpClientUtil.post(finance_service_url, reqParams);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("gt_date", DateUtil.addDate(DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss),
				DateUtil.fmt_yyyyMMddHHmmss, 3, -1));
		tempMap.put("lt_date", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		// 失效的商品下架
		goodsDao.updateDisabledSysitemItemStatus(tempMap);
		// 过期的优惠券状态修改为失效
		goodsDao.updateDisabledSysitemItemSkuStatus(tempMap);
	}

	@Override
	public void ldActivitiesStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		// 一元购定时开奖
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("status", Constant.STATUS_PROCESSING);
		tempMap.put("activity_type", Constant.ACTIVITIES_TYPE_DSK);
		tempMap.put("gt_date", DateUtil.addDate(DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss),
				DateUtil.fmt_yyyyMMddHHmmss, 3, -1));
		tempMap.put("lt_date", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		List<Map<String, Object>> ldActivitiesDetailList = ldActivityDao.selectLdActivityDetail(tempMap);
		for (Map<String, Object> map : ldActivitiesDetailList) {
			Integer person_num = Integer.parseInt(map.get("person_num") + "");
			Integer total_person = Integer.parseInt(map.get("total_person") + "");
			if (total_person >= person_num) {
				// 开奖
				activityAnnounced(map.get("activity_id") + "", total_person);
			} else {
				// 活动失效
				ldActivityDisabled(map.get("activity_id") + "", (Date) map.get("modify_date"));
			}
		}
	}

	/**
	 * 活动中奖号码揭晓
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	private void activityAnnounced(final String activity_id, Integer total_person)
			throws BusinessException, SystemException, Exception {
		try {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 计算中奖号码
			tempMap.put("activity_id", activity_id);
			List<LdActivityProcess> ldActivityProcessList = ldActivityDao.selectLdActivityProcess(tempMap);
			long sum = 0;
			for (LdActivityProcess ldActivityProcess : ldActivityProcessList) {
				// 时间转换成Unix时间
				sum += ldActivityProcess.getCreated_date().getTime() / 1000;
			}
			final String luck_code = sum % total_person + 10000001 + "";

			// 设置中奖号码的状态为中奖，非中奖号码的状态为非中奖
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("draw_code_eq", luck_code);
			tempMap.put("status", Constant.STATUS_WIN);
			ldActivityDao.updateLdActivityProcess(tempMap);

			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("draw_code_neq", luck_code);
			tempMap.put("status", Constant.STATUS_MISS);
			ldActivityDao.updateLdActivityProcess(tempMap);

			// 设置一元购商品中奖号码
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("luck_code", luck_code);
			tempMap.put("status", Constant.STATUS_ANNOUNCED);
			ldActivityDao.updateLdActivity(tempMap);

			// 查询中奖的消费者信息
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_WIN);
			List<LdActivityProcess> ldActivityWinList = ldActivityDao.selectLdActivityProcess(tempMap); // 中奖名单
			if (ldActivityWinList != null && ldActivityWinList.size() > 0) {
				final String consumer_id = ldActivityWinList.get(0).getConsumer_id();
				// 发送中奖短信
				try {
					Map<String, Object> tempParams = new HashMap<String, Object>();
					tempParams.put("activity_id", activity_id);
					List<Map<String, Object>> ldActivitiesDetail = ldActivityDao.selectLdActivityDetail(tempParams);
					if (ldActivitiesDetail.size() > 0) {
						tempParams.clear();
						String stores_info = StringUtil.formatStr(ldActivitiesDetail.get(0).get("store_info"));
						String title = StringUtil.formatStr(ldActivitiesDetail.get(0).get("title"));
						String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
						Map<String, String> requestParams = new HashMap<String, String>();
						tempParams.put("member_id", consumer_id);
						tempParams.put("member_type_key", "consumer");
						requestParams.put("params", FastJsonUtil.toJson(tempParams));
						requestParams.put("service", "member.userInfoFind");
						String resultStr = HttpClientUtil.post(member_service_url, requestParams);
						Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
						if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
							throw new BusinessException((String) resultMap.get("error_code"),
									(String) resultMap.get("error_msg"));
						}
						tempParams.clear();
						requestParams.clear();
						Map<String, Object> userInfo = (Map<String, Object>) resultMap.get("data");
						// 发送中奖短信
						String msg = "恭喜您在“［" + title + "］”活动中成功获得奖品！请您前往［" + stores_info + "］出示二维码领取！";
						sendMsg(userInfo.get("mobile") + "", msg);
					}
				} catch (Exception e) {
					logger.error("给中奖消费者发送短信失败！", e);
				}
			}
			logger.info("一元购活动【" + activity_id + "】开奖成功");
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 活动失效
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	private void ldActivityDisabled(String activity_id, Date modify_date)
			throws BusinessException, SystemException, Exception {
		try {
			logger.info("活动【" + activity_id + "】未达成,进行退款操作");
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_UNREACHED);
			tempMap.put("modify_date", DateUtil.date2Str(modify_date, DateUtil.fmt_yyyyMMddHHmmss));
			int updateFlag = ldActivityDao.updateLdActivity(tempMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PROCESSING, "活动正在处理中");
			}
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_PROCESSING);
			List<Map<String, Object>> ldActivityProcessCountList = ldActivityDao.selectLdActivityProcessCount(tempMap);
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
				bizParams.put("detail", "一元抽奖退款");
				bizParams.put("amount", ldActivityProcessCount.get("count_num") + "");
				bizParams.put("out_trade_no", activity_id);
				bizParams.put("buyer_id", ldActivityProcessCount.get("consumer_id"));
				bizParams.put("seller_id", Constant.MEMBER_ID_MTH);
				bizParams.put("activity_id", activity_id);
				bizParams.put("out_trade_body", FastJsonUtil.toJson(bizParams));
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
				tempMap.put("remark", "活动未达成");
				ldActivityDao.updateLdActivityProcess(tempMap);
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
	public void goodsViewCountSave(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {

		Map<String, Integer> viewMap = (Map<String, Integer>) redisUtil.getObj("ps_goods_view");
		for (String goods_id : viewMap.keySet()) {
			if (goods_id.length() > 50) {
				logger.error("goods_id异常:" + goods_id);
				continue;
			}
			Integer view_count = viewMap.get(goods_id);
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("goods_id", goods_id);
			tempMap.put("view", view_count);
			int updateFlag = psGoodsDao.updateGdViewSell(tempMap);
			if (updateFlag == 0) { // 更新失败
				// 插入商品记录
				GdViewSell gdViewSell = new GdViewSell();
				gdViewSell.setGoods_id(goods_id);
				gdViewSell.setView(view_count);
				gdViewSell.setSell(0);
				psGoodsDao.insertGdViewSell(gdViewSell);
			}
		}
		viewMap.clear();
		redisUtil.setObj("ps_goods_view", viewMap);
	}

	/**
	 * 红包过期
	 */
	@Override
	public void updateDisabledGcActivity(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		// 过期的红包状态修改为失效
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("gt_date", DateUtil.addDate(DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss),
				DateUtil.fmt_yyyyMMddHHmmss, 3, -6));
		tempMap.put("lt_date", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		gcActivityDao.updateDisabledGcActivity(tempMap);
	}

	/**
	 * 刷新抽奖活动状态
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	@Override
	public void plActivityStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		// 揭晓抽奖活动
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("status", Constant.STATUS_ONLINE);
		tempMap.put("gt_date", DateUtil.addDate(DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss),
				DateUtil.fmt_yyyyMMddHHmmss, 3, -1));
		tempMap.put("lt_date", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		List<Map<String, Object>> ldActivitiesDetailList = plPartcipatorDao.selectPlActivityForOp(tempMap);
		for (Map<String, Object> map : ldActivitiesDetailList) {
			Integer prize_qty = Integer.parseInt(map.get("prize_qty") + "");
			Integer min_num = Integer.parseInt(map.get("min_num") + "");
			Integer total_num = Integer.parseInt(map.get("total_num") + "");
			if (total_num >= min_num) {
				// 开奖
				plActivityOpenLottery(map.get("activity_id") + "", prize_qty, (Date) map.get("modify_date"));
			} else {
				// 活动取消
				plActivityDisabled(map.get("activity_id") + "", (Date) map.get("modify_date"));
			}
		}
	}

	/**
	 * 刷新过期的权益卷
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	@Override
	public void gdBenefitStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {

		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("gt_date", DateUtil.addDate(DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss),
				DateUtil.fmt_yyyyMMddHHmmss, 3, -3));
		tempMap.put("lt_date", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		gdBenefitDao.updateExpiredGdBenefit(tempMap);
	}

	/**
	 * 刷新过期的权益活动
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	@Override
	public void gdActivityStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {

		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("gt_date", DateUtil.addDate(DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss),
				DateUtil.fmt_yyyyMMddHHmmss, 3, -3));
		tempMap.put("lt_date", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		gdActivityDao.updateExpiredGdActivity(tempMap);
	}

	/**
	 * 商品自动下架
	 */
	@Override
	public void psGoodsAutoOffline(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {

		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("gt_date", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMddHHmmss));
		goodsDao.updatePsGoodsOffline(tempMap);
	}

	/**
	 * 活动开奖
	 * 
	 * @param activity_id
	 * @param prize_qty
	 * @throws BusinessException
	 * @throws SystemException
	 */
	private void plActivityOpenLottery(final String activity_id, Integer prize_qty, Date modify_date)
			throws BusinessException, SystemException, Exception {
		try {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 设置活动已经开奖
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_OFFLINE);
			tempMap.put("modify_date", DateUtil.date2Str(modify_date, DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("remark", "抽奖活动达成");
			plPartcipatorDao.updatePlActivity(tempMap);

			// 参与此活动的用户
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_IN_PROGRESS);
			List<Map<String, Object>> plPartcipatorList = plPartcipatorDao.selectPlPartcipator(tempMap);
			// 进行抽奖
			for (int i = 1; i <= prize_qty; i++) {
				// 从用户参与总数中随机抽取一个用户
				int totle = plPartcipatorList.size();
				int luck_code = RandomUtils.nextInt(totle);
				Map<String, Object> plPartcipatorMap = plPartcipatorList.get(luck_code);
				// 更新中奖记录的状态
				tempMap.clear();
				tempMap.put("activity_id", activity_id);
				tempMap.put("partcipator_id", plPartcipatorMap.get("partcipator_id"));
				tempMap.put("member_id", plPartcipatorMap.get("member_id"));
				tempMap.put("member_type_key", plPartcipatorMap.get("member_type_key"));
				tempMap.put("status", Constant.STATUS_WIN);
				plPartcipatorDao.updatePlPartcipator(tempMap);
				// 从集合中去除中奖的用户
				plPartcipatorList.remove(luck_code);
			}
			// 设置未中奖的用户未未中奖
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("where_status", Constant.STATUS_IN_PROGRESS);
			tempMap.put("status", Constant.STATUS_LOST);
			plPartcipatorDao.updatePlPartcipator(tempMap);
			// 查询用户中奖的次数
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_WIN);
			List<Map<String, Object>> winMemberList = plPartcipatorDao.selectWinMemberGroupFind(tempMap);
			// 生成中奖数据
			List<PlLuckyDelivery> plLuckyDeliveryList = new ArrayList<PlLuckyDelivery>();
			Date date = new Date();
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, String> bizParams = new HashMap<String, String>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			for (Map<String, Object> winMemberMap : winMemberList) {
				String member_id = winMemberMap.get("member_id") + "";
				String member_type_key = winMemberMap.get("member_type_key") + "";
				PlLuckyDelivery plLuckyDelivery = new PlLuckyDelivery();
				plLuckyDelivery.setLucky_delivery_id(IDUtil.getUUID());
				plLuckyDelivery.setStatus(Constant.STATUS_UNDELIVERED);
				plLuckyDelivery.setActivity_id(activity_id);
				plLuckyDelivery.setMember_id(member_id);
				plLuckyDelivery.setMember_type_key(member_type_key);
				// 获取会员的地址信息
				// 因为设计到跨库，因此先获取会员信息
				reqParams.clear();
				bizParams.clear();
				resultMap.clear();
				reqParams.put("service", "member.consumerAddressFind");
				bizParams.put("consumer_id", member_id + "");
				bizParams.put("is_major_addr", "Y");
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.post(member_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				List<Map<String, Object>> consumerAddressList = (List<Map<String, Object>>) resultMap.get("data");
				if (consumerAddressList.size() > 0) {
					Map<String, Object> memberAddressInfo = consumerAddressList.get(0);
					plLuckyDelivery.setContact_person(StringUtil.formatStr(memberAddressInfo.get("consignee")));
					plLuckyDelivery.setContact_tel(StringUtil.formatStr(memberAddressInfo.get("mobile")));
					plLuckyDelivery.setDelivery_area_id(StringUtil.formatStr(memberAddressInfo.get("area_id")));
					String area_desc = StringUtil.formatStr(memberAddressInfo.get("area_desc"));
					String address = StringUtil.formatStr(memberAddressInfo.get("address"));
					plLuckyDelivery.setDelivery_address(area_desc.replace("中国", "").replace(",", "") + address);
				}
				plLuckyDelivery.setWin_num(Integer.parseInt(winMemberMap.get("cou_num") + ""));
				plLuckyDelivery.setCreated_date(date);
				plLuckyDelivery.setModified_date(date);
				plLuckyDeliveryList.add(plLuckyDelivery);
			}
			plPartcipatorDao.insertPlLuckyDelivery(plLuckyDeliveryList);
			logger.info("抽奖活动【" + activity_id + "】开奖成功");
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 抽奖活动失效
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	private void plActivityDisabled(String activity_id, Date modify_date)
			throws BusinessException, SystemException, Exception {
		try {
			logger.info("抽奖活动【" + activity_id + "】未达成");
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", activity_id);
			tempMap.put("status", Constant.STATUS_CANCELLED);
			tempMap.put("modify_date", DateUtil.date2Str(modify_date, DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("remark", "抽奖活动未达成");
			int updateFlag = plPartcipatorDao.updatePlActivity(tempMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PROCESSING, "活动正在处理中");
			}
			// 删除抽奖记录
			tempMap.clear();
			tempMap.put("activity_id", activity_id);
			plPartcipatorDao.deletePlPartcipatorByActivity(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 * @param msg
	 */
	private void sendMsg(final String mobiles, final String msg) {
		try {
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			// 发送短信
			String notification_service_url = PropertiesConfigUtil.getProperty("notification_service_url");
			bizParams.put("sms_source", Constant.DATA_SOURCE_SJLY_03);
			bizParams.put("mobiles", mobiles);
			bizParams.put("msg", msg);
			reqParams.put("service", "notification.SMSSend");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			HttpClientUtil.post(notification_service_url, reqParams);
		} catch (Exception e) {
			logger.error("发送短信通知异常", e);
		}
	}

}
