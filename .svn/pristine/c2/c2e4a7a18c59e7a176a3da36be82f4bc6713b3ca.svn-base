package com.meitianhui.goods.service.impl;

import java.math.BigDecimal;
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
import com.meitianhui.common.util.MoneyUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.goods.constant.Constant;
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.dao.GcActivityDao;
import com.meitianhui.goods.entity.GcActivity;
import com.meitianhui.goods.entity.GcActivityDetail;
import com.meitianhui.goods.entity.GcMemberFaceGift;
import com.meitianhui.goods.entity.GcMemberFaceGiftLog;
import com.meitianhui.goods.service.GcActivityService;

/***
 * 红包、见面礼业务实现类
 * 
 * @author 丁硕
 * @date 2017年3月1日
 */
@Service
public class GcActivityServiceImpl implements GcActivityService {

	@Autowired
	private GcActivityDao gcActivityDao;

	@Autowired
	public RedisUtil redisUtil;

	@Override
	public void gcActivityCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "gift_type", "expired_date", "gift_qty", "gift_value",
				"member_qty", "operator", "remark" });
		GcActivity gcActivity = new GcActivity();
		BeanConvertUtil.mapToBean(gcActivity, paramsMap);
		gcActivity.setActivity_id(IDUtil.getUUID());
		gcActivity.setOperated_time(new Date());
		gcActivityDao.insertGcActivity(gcActivity);
	}

	@Override
	public void gcActivityListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> gcActivityList = gcActivityDao.selectGcActivity(paramsMap);
			for (Map<String, Object> gcActivity : gcActivityList) {
				gcActivity.put("operated_time",
						DateUtil.date2Str((Date) gcActivity.get("operated_time"), DateUtil.fmt_yyyyMMddHHmmss));
				gcActivity.put("member_qty", gcActivity.get("member_qty") + "");
				gcActivity.put("gift_qty", gcActivity.get("gift_qty") + "");
				gcActivity.put("gift_value", gcActivity.get("gift_value") + "");
				gcActivity.put("member_qty", gcActivity.get("member_qty") + "");
				gcActivity.put("remark", StringUtil.formatStr(gcActivity.get("remark")));
				resultList.add(gcActivity);
			}
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("list", resultList);
			result.setResultData(temp);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void gcActivityDetailListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<GcActivityDetail> gcActivityDetailList = gcActivityDao.selectGcActivityDetail(paramsMap);
			for (GcActivityDetail gcActivityDetail : gcActivityDetailList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("detail_id", gcActivityDetail.getDetail_id());
				tempMap.put("activity_id", gcActivityDetail.getActivity_id());
				String gift_type = gcActivityDetail.getGift_type();
				BigDecimal gift_value = gcActivityDetail.getGift_value();
				if (gift_type.equals("gold")) {
					tempMap.put("gift_value", gift_value.intValue() + "");
				} else {
					tempMap.put("gift_value", gift_value + "");
				}
				tempMap.put("gift_type", gift_type);
				tempMap.put("expired_date",
						DateUtil.date2Str((Date) gcActivityDetail.getExpired_date(), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("created_date",
						DateUtil.date2Str((Date) gcActivityDetail.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("status", gcActivityDetail.getStatus());
				resultList.add(tempMap);
			}
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("list", resultList);
			result.setResultData(temp);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void gcActivityDetailCountFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key" });
			Map<String, Object> resultMap = new HashMap<String, Object>();
			paramsMap.put("status", "disable");
			Map<String, Object> countsMap = gcActivityDao.selectGcActivityDetailCount(paramsMap);
			resultMap.put("count_num", "0");
			if (null != countsMap) {
				resultMap.put("count_num", countsMap.get("count_num") + "");
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
	public void handleGcActivityOpen(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "detail_id", "member_type_key", "member_id" });
			paramsMap.put("status", Constant.STATUS_DISABLE);
			List<GcActivityDetail> gcActivityDetailList = gcActivityDao.selectGcActivityDetail(paramsMap);
			if (gcActivityDetailList.size() != 1) {
				throw new BusinessException(RspCode.GC_ACTIVITY, "操作失败,请刷新后重试");
			}
			GcActivityDetail gcActivityDetail = gcActivityDetailList.get(0);
			Date expired_date = gcActivityDetail.getExpired_date();
			if (expired_date.getTime() < new Date().getTime()) {
				throw new BusinessException(RspCode.GC_ACTIVITY, "红包已失效");
			}
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("modified_date",
					DateUtil.date2Str((Date) gcActivityDetail.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("detail_id", paramsMap.get("detail_id"));
			tempMap.put("member_type_key", paramsMap.get("member_type_key"));
			tempMap.put("member_type_key", paramsMap.get("member_type_key"));
			tempMap.put("status", Constant.STATUS_ENABLE);
			int updateFlag = gcActivityDao.updateGcActivityDetail(tempMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PP_ACTIVITY_ERROR, "操作失败,请刷新后重试");
			}
			String gift_type = gcActivityDetail.getGift_type() + "";
			String payment_way_key = "";
			if (gift_type.equals("cash")) {
				payment_way_key = "ZFFS_05";
			} else if (gift_type.equals("gold")) {
				payment_way_key = "ZFFS_08";
			}
			if (!payment_way_key.equals("")) {
				// 进行红包充值操作
				String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
				Map<String, String> reqParams = new HashMap<String, String>();
				Map<String, Object> bizParams = new HashMap<String, Object>();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				reqParams.put("service", "finance.balancePay");
				bizParams.put("data_source", "SJLY_03");
				bizParams.put("payment_way_key", payment_way_key);
				bizParams.put("detail", "红包");
				bizParams.put("amount", gcActivityDetail.getGift_value() + "");
				bizParams.put("out_trade_no", gcActivityDetail.getDetail_id());
				bizParams.put("buyer_id", Constant.MEMBER_ID_MTH);
				bizParams.put("seller_id", paramsMap.get("member_id"));
				Map<String, Object> out_trade_body = new HashMap<String, Object>();
				out_trade_body.put("detail_id", gcActivityDetail.getDetail_id());
				out_trade_body.put("gift_value", gcActivityDetail.getGift_value());
				out_trade_body.put("gift_type", gcActivityDetail.getGift_type());
				bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
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

	// ============================================见面礼======================================================

	/****
	 * 扫一扫获取见面礼
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleGcActivityScanQRCode(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "member_id", "mobile", "scan_member_id", "scan_member_type_key" });
		Map<String, Object> resultDataMap = new HashMap<String, Object>(); // 返回数据
		String member_id = paramsMap.get("member_id") + ""; // 扫描方会员ID
		String scan_member_id = paramsMap.get("scan_member_id") + ""; // 扫描的会员ID
		String scan_member_type_key = paramsMap.get("scan_member_type_key") + ""; // 扫描的会员类型

		// 自己不能扫自己
		if (member_id.equals(scan_member_id)) {
			resultDataMap.put("gift_type", "0"); // 未赠送
			resultDataMap.put("gift_msg", "加为好友， 奖励红包，请前去「钱包」 查看");
		} else {
			// 第一步，检查扫描的会员是否存在
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			bizParams.put("member_id", scan_member_id);
			bizParams.put("member_type_key", scan_member_type_key);
			reqParams.put("service", "member.memberInfoFindByMemberId");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(member_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			// 扫描方信息
			Map<String, Object> resultData = (Map<String, Object>) resultMap.get("data");

			// 第二步，检查是否达到送见面礼条件,扫描店东只给消费者赠送，扫描会员双方都赠送
			// 1、扫描的是店东二维码，检查是否扫描过此店，如果没有，赠送见面礼
			if (CommonConstant.MEMBER_TYPE_STORES.equals(scan_member_type_key)) {
				resultDataMap.put("gift_type", "0"); // 未赠送
				resultDataMap.put("gift_msg", "收藏店铺， 奖励红包， 请前去「钱包」 查看");

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("gift_type", Constant.GC_ACTIVITY_TYPE_FACE_GIFT);
				params.put("from_member_id", scan_member_id);
				params.put("from_member_type_key", CommonConstant.MEMBER_TYPE_STORES);
				params.put("to_member_id", member_id);
				params.put("to_member_type_key", CommonConstant.MEMBER_TYPE_CONSUMER);
				long count = gcActivityDao.selectActivityDetailCount(params);
				if (count < 1) { // 没有扫描过
					// 赠送见面礼
					Map<String, Object> from_member_info = new HashMap<String, Object>();
					from_member_info.put("member_id", scan_member_id);
					from_member_info.put("member_type_key", scan_member_type_key);
					from_member_info.put("mobile", resultData.get("contact_tel"));
					Map<String, Object> to_member_info = new HashMap<String, Object>();
					to_member_info.put("member_id", member_id);
					to_member_info.put("member_type_key", CommonConstant.MEMBER_TYPE_CONSUMER);
					to_member_info.put("mobile", paramsMap.get("mobile"));
					this.giveMemberFaceGift(from_member_info, to_member_info);
					resultDataMap.put("gift_type", "1"); // 只给消费者送了
				}

				// 加入收藏功能
				bizParams.clear();
				reqParams.clear();
				bizParams.put("consumer_id", member_id);
				bizParams.put("stores_id", scan_member_id);
				reqParams.put("service", "member.favoriteStore");
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				HttpClientUtil.postShort(member_service_url, reqParams);
			} else if (CommonConstant.MEMBER_TYPE_CONSUMER.equals(scan_member_type_key)) {
				resultDataMap.put("gift_type", "0"); // 未赠送
				resultDataMap.put("gift_msg", "加为好友， 奖励红包，请前去「钱包」 查看");

				boolean isGive = false; // 是否赠送，默认为否
				// 只要有一方没有领过见面礼，就都赠送
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("gift_type", Constant.GC_ACTIVITY_TYPE_FACE_GIFT);
				params.put("to_member_type_key", CommonConstant.MEMBER_TYPE_CONSUMER);
				params.put("to_member_id", member_id);
				long count = gcActivityDao.selectActivityDetailCount(params);
				if (count < 1) {
					isGive = true;
				} else {
					params.put("to_member_id", scan_member_id);
					count = gcActivityDao.selectActivityDetailCount(params);
					if (count < 1) {
						isGive = true;
					}
				}
				if (isGive) { // 双方都赠送
					// 赠送见面礼
					Map<String, Object> from_member_info = new HashMap<String, Object>();
					from_member_info.put("member_id", scan_member_id);
					from_member_info.put("member_type_key", scan_member_type_key);
					from_member_info.put("mobile", resultData.get("mobile"));
					Map<String, Object> to_member_info = new HashMap<String, Object>();
					to_member_info.put("member_id", member_id);
					to_member_info.put("member_type_key", CommonConstant.MEMBER_TYPE_CONSUMER);
					to_member_info.put("mobile", paramsMap.get("mobile"));

					// 扫描双方都赠送见面礼
					this.giveMemberFaceGift(from_member_info, to_member_info);
					this.giveMemberFaceGift(to_member_info, from_member_info);
					resultDataMap.put("gift_type", "2");
				}
			} else {
				throw new BusinessException(RspCode.GC_ACTIVITY, "会员类型不存在");
			}
		}
		result.setResultData(resultDataMap);
	}

	/***
	 * 赠送会员见面礼
	 * 
	 * @param from_member_info
	 *            被扫描方
	 * @param to_member_info
	 *            扫描方（消费者）
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date 2017年3月1日
	 */
	private void giveMemberFaceGift(final Map<String, Object> from_member_info,
			final Map<String, Object> to_member_info) throws BusinessException, SystemException, Exception {
		// 相关默认值
		BigDecimal default_gift_value = new BigDecimal(10); // 默认第次赠送10元
		Date default_expired_date = DateUtil.parseToDate("2017-06-30 23:59:59"); // 默认过期时间
		Date now = new Date();

		String member_id = to_member_info.get("member_id") + "";
		// 新增红包记录
		GcActivityDetail activityDetail = new GcActivityDetail();
		activityDetail.setDetail_id(IDUtil.getUUID());
		activityDetail.setActivity_id("TEMP"); // 前端兼容问题，暂时需要写入值
		activityDetail.setGift_type(Constant.GC_ACTIVITY_TYPE_FACE_GIFT);
		activityDetail.setStatus(Constant.STATUS_ENABLE);
		activityDetail.setFrom_member_id(from_member_info.get("member_id") + "");
		activityDetail.setFrom_member_type_key(from_member_info.get("member_type_key") + "");
		activityDetail.setFrom_member_info(FastJsonUtil.toJson(from_member_info));
		activityDetail.setTo_member_id(member_id);
		activityDetail.setTo_member_type_key(to_member_info.get("member_type_key") + "");
		activityDetail.setTo_member_info(FastJsonUtil.toJson(to_member_info));
		activityDetail.setGift_value(default_gift_value);
		activityDetail.setExpired_date(default_expired_date);
		activityDetail.setModified_date(now);
		activityDetail.setCreated_date(now);

		// 判断赠送否达到条件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("member_id", member_id);
		List<GcMemberFaceGift> gcMemberFaceGiftList = gcActivityDao.selectGcMemberFaceGift(params);
		if (gcMemberFaceGiftList.size() < 1) {
			// 发见面礼，新增统计记录
			gcActivityDao.insertGcActivityDetail(activityDetail);

			// 新增红包统计记录
			GcMemberFaceGift gcMemberFaceGift = new GcMemberFaceGift();
			gcMemberFaceGift.setMember_id(member_id);
			gcMemberFaceGift.setMember_info(FastJsonUtil.toJson(to_member_info));
			gcMemberFaceGift.setCash_amount(activityDetail.getGift_value());
			gcMemberFaceGift.setCash_balance(activityDetail.getGift_value());
			gcMemberFaceGift.setModified_date(now);
			gcMemberFaceGift.setCreated_date(now);
			gcActivityDao.insertGcMemberFaceGift(gcMemberFaceGift);
		} else {
			// 如果累计赠送达到200，则不赠送
			GcMemberFaceGift gcMemberFaceGift = gcMemberFaceGiftList.get(0);
			boolean flag = MoneyUtil.moneyComp(gcMemberFaceGift.getCash_amount(), new BigDecimal(200));
			if (!flag) { // 未满200，进行赠送
				gcActivityDao.insertGcActivityDetail(activityDetail);
				Map<String, Object> updateParams = new HashMap<String, Object>();
				updateParams.put("member_id", gcMemberFaceGift.getMember_id());
				updateParams.put("modified_date",
						DateUtil.date2Str(gcMemberFaceGift.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
				updateParams.put("add_cash_amount", activityDetail.getGift_value());
				updateParams.put("add_cash_balance", activityDetail.getGift_value());
				int updateFlag = gcActivityDao.updateGcMemberFaceGift(updateParams);
				if (updateFlag == 0) {
					throw new BusinessException(RspCode.PROCESSING, "操作失败,请刷新后重试");
				}
			}
		}
	}

	/***
	 * 见面礼红包支付到账 每次交易到款一块，每天最多只能5块
	 */
	@Override
	public void handleGcActivityFaceGiftPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "member_id", "member_type_key", "order_no", "order_date" });
		final String member_id = paramsMap.get("member_id") + "";
		final String order_date = paramsMap.get("order_date") + "";
		// 加锁
		String lockKey = "[handleGcActivityFaceGiftPay]_" + member_id;
		RedisLock lock = new RedisLock(redisUtil, lockKey, 30 * 1000); // 30秒超时时间
		lock.lock();
		// 1、首先账户是否有可用余额
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("member_id", member_id);
		List<GcMemberFaceGift> gcMemberFaceGiftList = gcActivityDao.selectGcMemberFaceGift(params);
		if (gcMemberFaceGiftList.size() > 0) {
			try {
				GcMemberFaceGift gcMemberFaceGift = gcMemberFaceGiftList.get(0);
				boolean isCanPay = MoneyUtil.moneyComp(gcMemberFaceGift.getCash_amount(), new BigDecimal(1));
				if (isCanPay) {
					// 再检查是否达到每天的限制交易额（5块）
					params.clear();
					params.put("member_id", member_id);
					params.put("order_date", order_date);
					List<Map<String, Object>> logList = gcActivityDao.selectGcMemberFaceGiftTotalAmount(params);
					if (logList.size() > 0) {
						Map<String, Object> log = logList.get(0);
						isCanPay = MoneyUtil.moneyComp("4", log.get("total_amount") + ""); // 大于等于4，即小于5
					}
				}
				// 进行见面礼解冻
				if (isCanPay) {
					// 生成日志信息
					GcMemberFaceGiftLog memberFaceGiftLog = new GcMemberFaceGiftLog();
					memberFaceGiftLog.setLog_id(IDUtil.getUUID());
					memberFaceGiftLog.setCategory("pay");
					memberFaceGiftLog.setAmount(new BigDecimal("1"));
					memberFaceGiftLog.setMember_id(member_id);
					memberFaceGiftLog.setEvent_desc("见面礼领取");
					memberFaceGiftLog.setOrder_date(order_date);
					memberFaceGiftLog.setTracked_date(new Date());
					gcActivityDao.insertGcMemberFaceGiftLog(memberFaceGiftLog);

					// 更新余额信息
					params.clear();
					params.put("member_id", member_id);
					params.put("add_cash_balance", "-1");
					int updateFlag = gcActivityDao.updateGcMemberFaceGift(params);
					if (updateFlag == 0) {
						throw new BusinessException(RspCode.PROCESSING, "操作失败,请刷新后重试");
					}

					// 进行红包充值操作
					String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					Map<String, Object> bizParams = new HashMap<String, Object>();
					Map<String, Object> resultMap = new HashMap<String, Object>();
					reqParams.put("service", "finance.balancePay");
					bizParams.put("data_source", "SJLY_03");
					bizParams.put("payment_way_key", "ZFFS_05");
					bizParams.put("detail", "见面礼");
					bizParams.put("amount", memberFaceGiftLog.getAmount() + "");
					bizParams.put("out_trade_no", paramsMap.get("order_no"));
					bizParams.put("buyer_id", Constant.MEMBER_ID_MTH);
					bizParams.put("seller_id", member_id);
					Map<String, Object> out_trade_body = new HashMap<String, Object>();
					out_trade_body.put("log_id", memberFaceGiftLog.getLog_id());
					out_trade_body.put("amount", memberFaceGiftLog.getAmount() + "");
					out_trade_body.put("order_no", paramsMap.get("order_no"));
					out_trade_body.put("order_date", paramsMap.get("order_date"));
					bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
					resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						throw new BusinessException((String) resultMap.get("error_code"),
								(String) resultMap.get("error_msg"));
					}
				}
			} finally {
				// 解锁
				lock.unlock();
			}
		}
	}

}
