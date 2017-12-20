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
import com.meitianhui.schedule.task.OrderTask;

@Component
public class OrderTaskImpl implements OrderTask {

	private static final Logger logger = Logger.getLogger(OrderTaskImpl.class);

	// 每1分账执行一次
	@Scheduled(cron = "0 */1 0-23 * * ?")
	@Override
	public void fgOrderAutoCancel() {
		try {
			logger.info("超级返过期订单自动取消");
			String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
			// 更新商品库存
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "order.fgOrderAutoCancel");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(order_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("超级返过期订单自动取消失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("超级返过期订单自动取消异常", e);
		} catch (Exception e) {
			logger.error("超级返过期订单自动取消异常", e);
		}
	}

	// 一分钟执行一次
	@Override
	@Scheduled(cron = "0 */1 0-23 * * ?")
	public void proPsGroupOrder() {
		try {
			logger.info("处理团购预售订单");
			String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
			// 更新商品库存
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "order.proPsGroupOrder");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(order_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("处理团购预售订单失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("处理团购预售订单异常", e);
		} catch (Exception e) {
			logger.error("处理团购预售订单异常", e);
		}
	}

	// 1小时执行一次
	@Override
	@Scheduled(cron = "0 30 */1 * * ?")
	public void psOrderAutoReceived() {
		try {
			logger.info("我要批超时订单自动确认收货");
			String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
			// 更新商品库存
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "order.psOrderAutoReceived");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(order_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("我要批超时订单自动确认收货失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("我要批超时订单自动确认收货异常", e);
		} catch (Exception e) {
			logger.error("我要批超时订单自动确认收货异常", e);
		}
	}

	// 1小时执行一次
	@Override
	@Scheduled(cron = "0 30 */1 * * ?")
	public void pcOrderAutoReceived() {
		try {
			logger.info("精选特卖超时订单自动确认收货");
			String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
			// 更新商品库存
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "order.pcOrderAutoReceived");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(order_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("精选特卖超时订单自动确认收货失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("精选特卖超时订单自动确认收货异常", e);
		} catch (Exception e) {
			logger.error("精选特卖超时订单自动确认收货异常", e);
		}
	}

	
	@Override
	@Scheduled(cron = "0 */5 1-23 * * ?")
	public void tsActivityCheck() {
		try {
			logger.info("拼团领活动验证是否成团");
			String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "order.tsActivityCheck");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(order_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("伙拼团活动验证是否成团失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("伙拼团活动验证是否成团异常", e);
		} catch (Exception e) {
			logger.error("伙拼团活动验证是否成团异常", e);
		}
	}

	@Override
	@Scheduled(cron = "0 */30 1-3 * * ?")
	public void tsOrderAutoReceived() {
		String msg = "伙拼团活动自动确认收货";
		try {
			logger.info(msg);
			String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "order.tsActivityAutoReceived");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(order_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Thread.sleep(10000);
			msg = "拼团领订单超时自动收货";
			logger.info(msg);
			reqParams.clear();
			bizParams.clear();
			resultMap.clear();
			reqParams.put("service", "order.tsOrderAutoReceived");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			resultStr = HttpClientUtil.postShort(order_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn(msg + "失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error(msg + "异常", e);
		} catch (Exception e) {
			logger.error(msg + "异常", e);
		}
	}
	
	// 夜里1点到2点执行
	@Override
	@Scheduled(cron = "0 */20 1-2 * * ?")
	public void timeOutOdTaskAutoClosed() {
		String msg = "处理过期任务";
		try {
			logger.info(msg);
			String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			reqParams.put("service", "order.odTaskTimeout");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(order_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn(msg + "失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error(msg + "异常", e);
		} catch (Exception e) {
			logger.error(msg + "异常", e);
		}
	}

}
