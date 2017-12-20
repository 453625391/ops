package com.meitianhui.schedule.task.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.schedule.constant.RspCode;
import com.meitianhui.schedule.task.GoodsTask;

@Component
public class GoodsTaskImpl implements GoodsTask {

	private static final Logger logger = Logger.getLogger(GoodsTaskImpl.class);

	// 每1分账执行一次
	@Scheduled(cron = "0 */1 0-23 * * ?")
	@Override
	public void couponStatusRefresh() {
		try {
			logger.info("刷新失效优惠券状态");
			String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
			// 更新商品库存
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "goods.couponStatusRefresh");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("刷新失效优惠券状态失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("刷新失效优惠券状态异常", e);
		} catch (Exception e) {
			logger.error("刷新失效优惠券状态异常", e);
		}
	}

	// 一分钟执行一次
	@Override
	@Scheduled(cron = "0 */1 0-23 * * ?")
	public void ldActivitiesStatusRefresh() {
		try {
			logger.info("活动中奖号码揭晓");
			String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
			// 更新商品库存
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "goods.ldActivitiesStatusRefresh");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("活动中奖号码揭晓失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("活动中奖号码揭晓异常", e);
		} catch (Exception e) {
			logger.error("活动中奖号码揭晓异常", e);
		}
	}

	// 每天夜里4点到6点，每30分钟执行一次
	@Override
	@Scheduled(cron = "0 0/30 4-6 * * ?")
	public void goodsViewCountSave() {
		try {
			logger.info("商品浏览量更新至数据库");
			String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "goods.goodsViewCountSave");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("商品浏览量更新至数据库失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("商品浏览量更新至数据库异常", e);
		} catch (Exception e) {
			logger.error("商品浏览量更新至数据库异常", e);
		}
	}

	@Override
	@Scheduled(cron = "0 */1 0-23 * * ?")
	public void plActivityStatusRefresh() {
		try {
			logger.info("刷新抽奖活动状态");
			String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "goods.plActivityStatusRefresh");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("刷新抽奖活动状态失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("刷新抽奖活动状态异常", e);
		} catch (Exception e) {
			logger.error("刷新抽奖活动状态异常", e);
		}
	}

	@Override
	@Scheduled(cron = "0 */30 1-3 * * ?")
	public void updateDisabledGcActivity() {
		try {
			logger.info("处理过期红包");
			String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "goods.updateDisabledGcActivity");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("处理过期红包失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("处理过期红包异常", e);
		} catch (Exception e) {
			logger.error("处理过期红包异常", e);
		}
	}

	@Override
	@Scheduled(cron = "0 */30 1-3 * * ?")
	public void gdBenefitStatusRefresh() {
		try {
			logger.info("处理过期权益卷");
			String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "goods.gdBenefitStatusRefresh");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("处理过期权益卷失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("处理过期权益卷异常", e);
		} catch (Exception e) {
			logger.error("处理过期权益卷异常", e);
		}
	}

	@Override
	@Scheduled(cron = "0 */30 1-3 * * ?")
	public void gdActivityStatusRefresh() {
		try {
			logger.info("处理过期权益活动");
			String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "goods.gdActivityStatusRefresh");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("处理过期权益活动失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("处理过期权益活动异常", e);
		} catch (Exception e) {
			logger.error("处理过期权益活动异常", e);
		}
	}

	@Override
	@Scheduled(cron = "0 */20 1-2 * * ?")
	public void psGoodsAutoOffline() {
		try {
			logger.info("领了么商品码自动下架");
			String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "goods.psGoodsAutoOffline");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("领了么商品码自动下架失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("领了么商品码自动下架异常", e);
		} catch (Exception e) {
			logger.error("领了么商品码自动下架异常", e);
		}
	}

}
