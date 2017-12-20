package com.meitianhui.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.MD5Util;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.finance.constant.Constant;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.controller.FinanceController;
import com.meitianhui.finance.dao.TransactionDao;
import com.meitianhui.finance.entity.FDTransactionsResult;
import com.meitianhui.finance.service.NotifyService;
import com.meitianhui.finance.service.TradeService;

@SuppressWarnings("unchecked")
@Service
public class NotifyServiceImpl implements NotifyService {
	private static final Logger logger = Logger.getLogger(NotifyServiceImpl.class);
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private TradeService tradeService;
	@Autowired
	public TransactionDao transactionDao;

	@Override
	public void financeChangeSMSNotify(final String member_id, final String member_type_key, final String msg) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (member_id.equals(Constant.MEMBER_ID_MTH) || member_id.equals(Constant.MEMBER_ID_HYD)
							|| member_id.equals(Constant.MEMBER_ID_ANONYMITY)
							|| member_id.equals(Constant.MEMBER_ID_BUSINESS)
							|| member_id.equals(Constant.MEMBER_ID_PLATFORM)) {
						return;
					}
					// 获取会员手机号
					String mobile = null;

					// 增加缓存处理
					String cache_key = "[financeChangeSMSNotify]_" + member_id + member_type_key;
					String obj_str = redisUtil.getStr(cache_key);
					if (StringUtil.isNotEmpty(obj_str)) {
						mobile = obj_str;
					} else {
						String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
						Map<String, String> reqParams = new LinkedHashMap<String, String>();
						Map<String, String> paramMap = new LinkedHashMap<String, String>();
						reqParams.put("service", "member.memberInfoFindByMemberId");
						paramMap.put("member_id", member_id);
						paramMap.put("member_type_key", member_type_key);
						reqParams.put("params", FastJsonUtil.toJson(paramMap));
						String resultStr = HttpClientUtil.post(member_service_url, reqParams);
						Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
						String rsp_code = (String) resultMap.get("rsp_code");
						if (!rsp_code.equals(RspCode.RESPONSE_SUCC)) {
							throw new BusinessException((String) resultMap.get("error_code"),
									(String) resultMap.get("error_msg"));
						}
						Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
						if (member_type_key.equals(Constant.MEMBER_TYPE_CONSUMER)) {
							mobile = dateMap.get("mobile") + "";
						} else if (member_type_key.equals(Constant.MEMBER_TYPE_STORES)) {
							mobile = dateMap.get("contact_tel") + "";
						} else if (member_type_key.equals(Constant.MEMBER_TYPE_SUPPLIER)) {
							mobile = dateMap.get("contact_tel") + "";
						}
						redisUtil.setStr(cache_key, mobile, 1800);
					}

					if (StringUtil.isNotEmpty(mobile)) {
						// 发送短信
						String notification_service_url = PropertiesConfigUtil.getProperty("notification_service_url");
						Map<String, String> reqParams = new HashMap<String, String>();
						Map<String, String> bizParams = new HashMap<String, String>();
						reqParams.put("service", "notification.SMSSend");
						bizParams.put("sms_source", Constant.DATA_SOURCE_SJLY_03);
						bizParams.put("mobiles", mobile);
						bizParams.put("msg", msg);
						reqParams.put("params", FastJsonUtil.toJson(bizParams));
						HttpClientUtil.postShort(notification_service_url, reqParams);
					}
				} catch (BusinessException e) {
					logger.error(e.getMsg());
				} catch (SystemException e) {
					logger.error(e.getMsg());
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		});
	}

	@Override
	public void financeChangeAppNotify(final String member_id, final String msg) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "member.appMsgNotify");
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("message", msg);
					paramMap.put("receiver", member_id);
					reqParams.put("params", FastJsonUtil.toJson(paramMap));
					HttpClientUtil.postShort(member_service_url, reqParams);
				} catch (BusinessException e) {
					logger.error(e.getMsg());
				} catch (SystemException e) {
					logger.error(e.getMsg());
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});
	}

	@Override
	public void pushAppNotify(final String data_source, final String member_id, final String msg) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String notification_service_url = PropertiesConfigUtil.getProperty("notification_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "notification.pushNotification");
					Map<String, String> paramMap = new HashMap<String, String>();
					Map<String, String> extrasparam = new HashMap<String, String>();
					paramMap.put("title", "交易通知");
					paramMap.put("alert", msg);
					paramMap.put("member_id", member_id);
					paramMap.put("data_source", data_source);
					extrasparam.put("type", "1");
					paramMap.put("extrasparam", FastJsonUtil.toJson(extrasparam));
					reqParams.put("params", FastJsonUtil.toJson(paramMap));
					HttpClientUtil.postShort(notification_service_url, reqParams);
				} catch (BusinessException e) {
					logger.error(e.getMsg());
				} catch (SystemException e) {
					logger.error(e.getMsg());
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});
	}

	@Override
	public void pushAppMessage(final String member_id, final String msg, final Map<String, String> extrasMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String notification_service_url = PropertiesConfigUtil.getProperty("notification_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "notification.pushMessage");
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("msg_title", "交易通知");
					paramMap.put("msg_content", msg);
					paramMap.put("member_id", member_id);
					paramMap.put("extrasparam", FastJsonUtil.toJson(extrasMap));
					reqParams.put("params", FastJsonUtil.toJson(paramMap));
					HttpClientUtil.postShort(notification_service_url, reqParams);
				} catch (BusinessException e) {
					logger.error(e.getMsg());
				} catch (SystemException e) {
					logger.error(e.getMessage());
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});
	}

	/**
	 * 交易注册(付款码)
	 * 
	 * @param map
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void tradeNotify(FDTransactionsResult fDTransactionsResult) {
		try {
			String transaction_no = StringUtil.formatStr(fDTransactionsResult.getTransaction_no());
			String order_type_key = StringUtil.formatStr(fDTransactionsResult.getOrder_type_key());
			Map<String, Object> orderMap = FastJsonUtil.jsonToMap(fDTransactionsResult.getOut_trade_body());
			// 设置订单类型
			orderMap.put("order_type_key", order_type_key);
			orderMap.put("payment_way_key", fDTransactionsResult.getPayment_way_key());
			if (order_type_key.equals(Constant.ORDER_TYPE_WYP)) {
				wypOrderPayNotity(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_COUPON)) {
				couponCreateNotify(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_LUCK_DRAW)) {
				String activity_type = StringUtil.formatStr(orderMap.get("activity_type"));
				if (activity_type.equals("YYY")) {
					yyyActivityOrderCreateNotify(transaction_no, orderMap);
				} else if (activity_type.equals("GGL")) {
					gglActivityOrderCreateNotify(transaction_no, orderMap);
				} else {
					dskActivityOrderCreateNotify(transaction_no, orderMap);
				}
			} else if (order_type_key.equals(Constant.ORDER_TYPE_LOCAL_SALE)) {
				pcOrderPayCreateNotify(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_GOLD_EXCHANGE)) {
				orderMap.put("amount", fDTransactionsResult.getAmount() + "");
				geOrderPayCreateNotify(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_CASHBACK)) {
				orderMap.put("amount", fDTransactionsResult.getAmount() + "");
				orderMap.put("member_id", fDTransactionsResult.getBuyer_id());
				fgOrderPayCreateNotify(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_HUIYIDING2)) {
				orderMap.put("member_id", fDTransactionsResult.getBuyer_id());
				hyd2OrderPayCreateNotify(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_PRESELL)) {
				psGroupPayCreateNotify(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_HUIYIDING3)) {
				orderMap.put("member_id", fDTransactionsResult.getBuyer_id());
				orderMap.put("price", fDTransactionsResult.getAmount() + "");
				hyd3OrderPayCreateNotify(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_PHONE_BILL)) {
				orderMap.put("amount", fDTransactionsResult.getAmount() + "");
				phoneBillOrderCreateNotity(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_TS)) {
				orderMap.put("amount", fDTransactionsResult.getAmount() + "");
				tsActivityPayCreateNotify(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_GROUP)) {
				orderMap.put("amount", fDTransactionsResult.getAmount() + "");
				tsOrderPayCreateNotify(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			} else if (order_type_key.equals(Constant.ORDER_TYPE_POINT)) {
				orderMap.put("amount", fDTransactionsResult.getAmount() + "");
				peOrderPayCreateNotify(transaction_no, fDTransactionsResult.getAmount(), orderMap);
			}	
		} catch (SystemException e) {
			logger.error("订单通知异常,订单类型：" + fDTransactionsResult.getOrder_type_key(), e);
		} catch (Exception e) {
			logger.error("订单通知异常,订单类型：" + fDTransactionsResult.getOrder_type_key(), e);
		}
	}

	/**
	 * 我要批订单通知
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void wypOrderPayNotity(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("我要批订单->" + paramsMap.toString());
					String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "order.wypOrderPayNotity");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));  
					String resultStr = HttpClientUtil.post(order_service_url, reqParams);  
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					String rsp_code = (String) resultMap.get("rsp_code");
					if (!rsp_code.equals(RspCode.RESPONSE_SUCC)) {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							// 订单退款
							Thread.sleep(1000);
							logger.error("我要批订单创建异常->" + error_msg);
							String payment_way_key = paramsMap.get("payment_way_key") + "";
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "我要批交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", payment_way_key);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(bizParams));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 优惠券订单
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void couponCreateNotify(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("优惠券订单->" + paramsMap.toString());
					String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "goods.couponCreateNotify");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							Thread.sleep(1000);
							// 交易冲正
							logger.error("优惠券交易退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "优惠券退款");
							bizParams.put("order_type_key", Constant.ORDER_TYPE_COUPON);
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 话费充值
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void phoneBillOrderCreateNotity(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("话费充值订单->" + paramsMap.toString());
					String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "csOrder.app.phoneBillOrderCreateNotity");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(order_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
						String order_no = (String) dataMap.get("order_no");
						Map<String, Object> bizParams = new HashMap<String, Object>();
						bizParams.put("transaction_no", transaction_no);
						bizParams.put("out_trade_no", order_no);
						transactionDao.updateFDTransactionsResult(bizParams);
					} else {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							Thread.sleep(1000);
							// 交易冲正
							logger.error("话费充值交易退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "话费充值交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("话费充值交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 一元抽奖订单
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void dskActivityOrderCreateNotify(final String transaction_no, final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("一元抽奖订单->" + paramsMap.toString());
					String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "goods.dskActivityProcessCreateNotify");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							Thread.sleep(1000);
							// 交易冲正
							logger.error("定时开交易退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "一元抽奖交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("consumer_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", paramsMap.get("qty") + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("一元抽交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 摇一摇订单
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void yyyActivityOrderCreateNotify(final String transaction_no, final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("摇一摇订单->" + paramsMap.toString());
					String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "goods.yyyActivityProcessCreateNotify");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							// 交易冲正
							Thread.sleep(1000);
							logger.error("摇一摇交易退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "摇一摇交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("consumer_id"));
							bizParams.put("out_member_id", paramsMap.get("stores_id"));
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", "1.00");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("摇一摇交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 刮刮乐订单
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void gglActivityOrderCreateNotify(final String transaction_no, final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("刮刮乐订单->" + paramsMap.toString());
					String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "goods.gglActivityProcessCreateNotify");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(goods_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							Thread.sleep(1000);
							// 交易冲正
							logger.error("刮刮乐交易退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "刮刮乐交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("consumer_id"));
							bizParams.put("out_member_id", paramsMap.get("stores_id"));
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", "1.00");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 便利精选特卖
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void pcOrderPayCreateNotify(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("精选特卖订单->" + paramsMap.toString());
					String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "order.pcOrderPayCreateNotify");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(order_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
						String order_no = (String) dataMap.get("order_no");
						Map<String, Object> bizParams = new HashMap<String, Object>();
						bizParams.put("transaction_no", transaction_no);
						bizParams.put("out_trade_no", order_no);
						transactionDao.updateFDTransactionsResult(bizParams);
					} else {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							Thread.sleep(1000);
							// 交易冲正
							logger.error("精选特卖交易退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "精选特卖交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("consumer_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("精选特卖交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 金币兑订单
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void geOrderPayCreateNotify(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("名品汇订单->" + paramsMap.toString());
					String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "order.geOrderPayCreateNotify");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(order_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
						String order_no = (String) dataMap.get("order_no");
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("transaction_no", transaction_no);
						paramMap.put("out_trade_no", order_no);
						transactionDao.updateFDTransactionsResult(paramMap);
					} else {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							Thread.sleep(1000);
							// 交易冲正
							logger.error("名品汇交易退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "品牌领交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("名品汇交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 积分兑订单
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void peOrderPayCreateNotify(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				
				String error_msg = null;
				try {
					logger.info("会员权益订单->" + paramsMap.toString());
					String order_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "gdActivity.consumer.gdActivityDeliveryCreate");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(order_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
						String order_no = (String) dataMap.get("order_no");
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("transaction_no", transaction_no);
						paramMap.put("out_trade_no", order_no);
						transactionDao.updateFDTransactionsResult(paramMap);
					} else {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {	
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							Thread.sleep(1000);
							// 交易冲正
							logger.error("会员权益退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "会员权益交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_09);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("名品汇交易退款异常", e2);
					}
				}	
			}
		});
	}
	
	
	/**
	 * 领了么订单
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void fgOrderPayCreateNotify(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("领了么订单->" + paramsMap.toString());
					String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "order.fgOrderCreateNotity");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(order_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
						String order_no = (String) dataMap.get("order_no");
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("transaction_no", transaction_no);
						paramMap.put("out_trade_no", order_no);
						transactionDao.updateFDTransactionsResult(paramMap);
					} else {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							// 交易冲正
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "淘淘领交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							logger.error("领了么交易退款->" + error_msg);
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("领了么交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 惠易定订单
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void hyd2OrderPayCreateNotify(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("惠易定2.0订单->" + paramsMap.toString());
					String hyd_service_url = PropertiesConfigUtil.getProperty("hyd_service_url") + "payment/notify";
					String hyd_sign_key = PropertiesConfigUtil.getProperty("hyd_sign_key");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("payment_id", paramsMap.get("payment_id") + "");
					reqParams.put("status", "1");
					reqParams.put("transaction_no", transaction_no);
					reqParams.put("transaction_no", transaction_no);
					reqParams.put("payment_way_key", paramsMap.get("payment_way_key") + "");
					reqParams.put("sign", MD5Util.sign(createLinkString(reqParams), hyd_sign_key, "utf-8"));
					logger.info("惠易定2.0支付结果通知->request:" + reqParams.toString());
					String resultStr = HttpClientUtil.post(hyd_service_url, reqParams);
					logger.info("惠易定2.0支付结果通知->response:" + resultStr);
					if (resultStr.equals(RspCode.RESPONSE_SUCC)) {
						Map<String, Object> bizParams = new HashMap<String, Object>();
						bizParams.put("transaction_no", transaction_no);
						bizParams.put("out_trade_no", paramsMap.get("payment_id"));
						transactionDao.updateFDTransactionsResult(bizParams);
					} else {
						error_msg = resultStr;
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							logger.error("惠易定2.0交易退款->error_msg:" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "惠易定2.0交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", paramsMap.get("payment_id"));
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("惠易定惠易定2.0交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 团购预售订单
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void psGroupPayCreateNotify(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("团购预售订单->" + paramsMap.toString());
					String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "order.psGroupSubOrderCreateNotify");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(order_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
						String order_no = (String) dataMap.get("order_no");
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("transaction_no", transaction_no);
						paramMap.put("out_trade_no", order_no);
						transactionDao.updateFDTransactionsResult(paramMap);
					} else {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							Thread.sleep(1000);
							// 订单退款
							logger.error("团购预售订单退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", Constant.DATA_SOURCE_SJLY_03);
							bizParams.put("detail", "团购预售订单退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 惠易定3.0订单
	 * 
	 * @param transaction_no
	 * @param paramsMap
	 */
	private void hyd3OrderPayCreateNotify(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("惠易定3.0订单->" + paramsMap.toString());
					String hyd3_service_url = PropertiesConfigUtil.getProperty("hyd3_service_url")
							+ "javaApis/hydAccount/payCallback";
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("payment_id", paramsMap.get("payment_id") + "");
					reqParams.put("payment_way_key", paramsMap.get("payment_way_key") + "");
					reqParams.put("transaction_no", transaction_no);
					reqParams.put("price", paramsMap.get("price") + "");
					reqParams.put("result", "succ");
					logger.info("惠易定3.0支付请求->request:" + reqParams.toString());
					String resultStr = HttpClientUtil.post(hyd3_service_url, reqParams);
					logger.info("惠易定3.0支付结果通知->response:" + resultStr);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					Integer status = (Integer) resultMap.get("status");
					if (status != 200) {
						error_msg = "通知失败,status->" + status;
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							logger.error("惠易定交易退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", "SJLY_03");
							bizParams.put("detail", "惠易定3.0交易退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", paramsMap.get("payment_id"));
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("惠易定交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 伙拼团活动创建
	 * 
	 * @Title: tsOrderPayCreateNotify
	 * @param transaction_no
	 * @param paramsMap
	 * @author tiny
	 */
	private void tsActivityPayCreateNotify(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("拼团领创建首笔订单->" + paramsMap.toString());
					String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "tsActivity.consumer.ladderTsActivityCreate");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(order_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
						String order_no = (String) dataMap.get("order_no");
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("transaction_no", transaction_no);
						paramMap.put("out_trade_no", order_no);
						transactionDao.updateFDTransactionsResult(paramMap);
					} else {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							Thread.sleep(1000);
							// 订单退款
							logger.error("拼团领创建首笔订单退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", Constant.DATA_SOURCE_SJLY_03);
							bizParams.put("detail", "拼团领订单退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("交易退款异常", e2);
					}
				}
			}
		});
	}

	/**
	 * 伙拼团活动创建
	 * 
	 * @Title: tsOrderPayCreateNotify
	 * @param transaction_no
	 * @param paramsMap
	 * @author tiny
	 */
	private void tsOrderPayCreateNotify(final String transaction_no, final BigDecimal amount,
			final Map<String, Object> paramsMap) {
		FinanceController.threadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String error_msg = null;
				try {
					logger.info("拼团领订单->" + paramsMap.toString());
					String order_service_url = PropertiesConfigUtil.getProperty("order_service_url");
					Map<String, String> reqParams = new HashMap<String, String>();
					reqParams.put("service", "tsOrder.consumer.ladderTsOrderCreate");
					reqParams.put("params", FastJsonUtil.toJson(paramsMap));
					String resultStr = HttpClientUtil.post(order_service_url, reqParams);
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
						String order_no = (String) dataMap.get("order_no");
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("transaction_no", transaction_no);
						paramMap.put("out_trade_no", order_no);
						transactionDao.updateFDTransactionsResult(paramMap);
					} else {
						error_msg = resultMap.get("error_msg") + "";
					}
				} catch (BusinessException e) {
					error_msg = e.getMsg();
				} catch (SystemException e) {
					error_msg = e.getMsg();
				} catch (Exception e) {
					error_msg = e.getMessage();
				} finally {
					try {
						if (StringUtils.isNotBlank(error_msg)) {
							Thread.sleep(1000);
							// 订单退款
							logger.error("拼团领订单退款->" + error_msg);
							Map<String, Object> bizParams = new HashMap<String, Object>();
							bizParams.put("data_source", Constant.DATA_SOURCE_SJLY_03);
							bizParams.put("detail", "拼团领订单退款");
							bizParams.put("order_type_key", paramsMap.get("order_type_key"));
							bizParams.put("out_trade_no", transaction_no);
							bizParams.put("in_member_id", paramsMap.get("member_id"));
							bizParams.put("out_member_id", Constant.MEMBER_ID_MTH);
							bizParams.put("payment_way_key", Constant.PAYMENT_WAY_05);
							bizParams.put("amount", amount + "");
							bizParams.put("out_trade_body", FastJsonUtil.toJson(paramsMap));
							tradeService.orderRefund(bizParams, new ResultData());
						}
					} catch (Exception e2) {
						logger.error("交易退款异常", e2);
					}
				}
			}
		});
	}

	public String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = (String) params.get(key);
			// 拼接时，不包括最后一个&字符
			if (i == keys.size() - 1) {
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

}
