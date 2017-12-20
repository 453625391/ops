package com.meitianhui.member.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.meitianhui.common.util.MoneyUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.member.constant.Constant;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.dao.AssistantApplicationDao;
import com.meitianhui.member.dao.MemberDao;
import com.meitianhui.member.dao.StoresDao;
import com.meitianhui.member.entity.MDStores;
import com.meitianhui.member.entity.MDStoresServiceFee;
import com.meitianhui.member.service.MemberTaskService;

/**
 * 会员定时任务
 * 
 * @ClassName: MemberTaskServiceImpl
 * @author tiny
 * @date 2017年2月23日 下午3:18:39
 *
 */
@SuppressWarnings("unchecked")
@Service
public class MemberTaskServiceImpl implements MemberTaskService {

	private static final Logger logger = Logger.getLogger(MemberTaskServiceImpl.class);

	@Autowired
	public MemberDao memberDao;

	@Autowired
	public StoresDao storesDao;

	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private AssistantApplicationDao AssistantApplicationDao;

	@Override
	public void handleAssistantServiceFree(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_assistantServiceFree]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);
			
			//1检索  1.1已经锁定  1.2业务员（助教）标识 为空    1.3时间是小于当前时间的三天后
			// 如果这三个条件都满足就证明 可以把门店对应的助教申请单改为驳回
			Map<String , Object> tempMap = new HashMap<>();
			tempMap.put("assistant_expired_date", new Date());
			tempMap.put("is_assistant_locked", "Y");
			List<MDStores> mdStoresList = storesDao.selectMDStores(tempMap);
			//2遍历所获得的门店ID对应的助教申请单 改为驳回
			for (int i = 0; i < mdStoresList.size(); i++) {
				tempMap.clear();
				tempMap.put("stores_id", mdStoresList.get(i));
				tempMap.put("audit_status", "reject");
				AssistantApplicationDao.updateAssistantApplication(tempMap);
			}
			
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
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
	public void handleFreezeServiceFree(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_freezeServiceFree]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			// 冻结联盟店
			paramsMap.put("stores_type_key", Constant.STORES_TYPE_02);
			List<String> businessStatusList = new ArrayList<String>();
			businessStatusList.add("TDJD_01");
			businessStatusList.add("TDJD_02");
			businessStatusList.add("TDJD_03");
			paramsMap.put("business_status_key_in", businessStatusList);
			List<MDStores> mDStoresList = storesDao.selectMDStoresForServiceFree(paramsMap);
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String tracked_date = DateUtil.getFormatDate(DateUtil.fmt_yyyyMM);
			Map<String, Object> tempMap = new HashMap<String, Object>();
			for (MDStores mDStores : mDStoresList) {
				tempMap.clear();
				tempMap.put("tracked_date", tracked_date);
				tempMap.put("stores_id", mDStores.getStores_id());
				tempMap.put("category", "freeze_service_free");
				Map<String, Object> logMap = memberDao.selectMDStoresScheduleLog(tempMap);
				if (null != logMap) {
					continue;
				}
				// 冻结金额
				resultMap.clear();
				reqParams.clear();
				bizParams.clear();
				reqParams.put("service", "finance.balanceFreeze");
				bizParams.put("data_source", Constant.DATA_SOURCE_SJLY_03);
				bizParams.put("member_id", mDStores.getStores_id());
				bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
				bizParams.put("amount", "98");
				bizParams.put("detail", "会员费冻结");
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				// 修改联盟店的服务费状态为1
				tempMap.clear();
				tempMap.put("stores_id", mDStores.getStores_id());
				tempMap.put("service_fee", "1");
				storesDao.updateMDStores(tempMap);
				// 记录日志
				tempMap.clear();
				tempMap.put("log_id", IDUtil.getUUID());
				tempMap.put("stores_id", mDStores.getStores_id());
				tempMap.put("category", "freeze_service_free");
				tempMap.put("tracked_date", new Date());
				tempMap.put("event", "冻结会员服务费");
				memberDao.insertMDStoresScheduleLog(tempMap);
				sendMsg(mDStores.getContact_tel(), "您的账户被冻结98元用作技术服务费，本月赚取2000金币可免除费用并自动解冻。");
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
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
	public void handleProServiceFree(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_proServiceFree]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			// 查询所有的门店
			List<String> businessStatusList = new ArrayList<String>();
			businessStatusList.add("TDJD_01");
			businessStatusList.add("TDJD_02");
			businessStatusList.add("TDJD_03");
			paramsMap.put("business_status_key_in", businessStatusList);
			List<MDStores> mDStoresList = storesDao.selectMDStoresForServiceFree(paramsMap);
			String tracked_date = DateUtil.getFormatDate(DateUtil.fmt_yyyyMM);
			Map<String, Object> tempMap = new HashMap<String, Object>();
			for (MDStores mDStores : mDStoresList) {
				// 检测是本月会员是否已经做了结算,如果已经完成了结算，则跳过结算
				tempMap.clear();
				tempMap.put("tracked_date", tracked_date);
				tempMap.put("stores_id", mDStores.getStores_id());
				tempMap.put("category", "settlement_service_free");
				Map<String, Object> logMap = memberDao.selectMDStoresScheduleLog(tempMap);
				if (null != logMap) {
					continue;
				}
				if (mDStores.getStores_type_key().equals(Constant.STORES_TYPE_03)) {
					/**
					 * 处理加盟店(检测金币余额是否大于等于2000,如果是,则进行扣除金币,然后送店东98元)
					 **/
					franchiseServiceFee(mDStores);
				} else {
					/**
					 * 处理联盟店逻辑 检测是否服务费状态是否为1,如果为1,检测金币是否大于等于2000,如果金币余额不够,则扣除金币,
					 * 解冻98元, 如果金币余额不够，检测可用余额是否大于0，如果是，则进行解冻98元,然后扣除98元,
					 * 如果不是,则设置会员服务非状态为2
					 **/
					alliedShopsServiceFee(mDStores);
				}
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
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
	 * 加盟店会员费结算逻辑
	 * 
	 * @param mDStores
	 */
	public void franchiseServiceFee(MDStores mDStores) throws BusinessException, SystemException, Exception {
		try {
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			// 查询余额
			reqParams.put("service", "finance.memberAssetFind");
			bizParams.put("member_id", mDStores.getStores_id());
			bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> assetMap = (Map<String, Object>) resultMap.get("data");
			String gold = assetMap.get("gold") + "";
			// 判断可用金币余额是否足够
			if (MoneyUtil.moneyComp(gold, "2000")) {

				MDStoresServiceFee serviceFee = new MDStoresServiceFee();
				serviceFee.setDate_id(DateUtil.getFormatDate("yyyyMMdd"));
				serviceFee.setCreated_date(new Date());
				serviceFee.setStores_id(mDStores.getStores_id());
				serviceFee.setCash(new BigDecimal("98"));
				serviceFee.setGold(0);
				serviceFee.setRemark("加盟店会员费结算成功");
				memberDao.insertMDStoresServiceFee(serviceFee);

				// 记录日志
				bizParams.clear();
				bizParams.put("log_id", IDUtil.getUUID());
				bizParams.put("stores_id", mDStores.getStores_id());
				bizParams.put("category", "settlement_service_free");
				bizParams.put("tracked_date", new Date());
				bizParams.put("event", "加盟店店会员费结算");
				memberDao.insertMDStoresScheduleLog(bizParams);
				// 扣金币
				resultMap.clear();
				reqParams.clear();
				bizParams.clear();
				reqParams.put("service", "finance.balancePay");
				bizParams.put("data_source", "SJLY_03");
				bizParams.put("payment_way_key", "ZFFS_08");
				bizParams.put("detail", "会员费");
				bizParams.put("amount", "2000");
				bizParams.put("buyer_id", mDStores.getStores_id());
				bizParams.put("seller_id", Constant.MEMBER_ID_MTH);
				bizParams.put("out_trade_body", FastJsonUtil.toJson(bizParams));
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				// 送现金
				reqParams.put("service", "finance.balancePay");
				bizParams.put("data_source", "SJLY_03");
				bizParams.put("payment_way_key", "ZFFS_05");
				bizParams.put("detail", "会员费");
				bizParams.put("amount", "98");
				bizParams.put("buyer_id", Constant.MEMBER_ID_MTH);
				bizParams.put("seller_id", mDStores.getStores_id());
				bizParams.put("out_trade_body", FastJsonUtil.toJson(bizParams));
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					logger.error("会员id【" + mDStores.getStores_id() + "】会员费结算异常");
				}
				// 发送短信
				sendMsg(mDStores.getContact_tel(), "恭喜您本月赚取超过2000金币，系统已充值98元以资奖励。");
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
	 * 联盟店会员费结算逻辑
	 * 
	 * @param mDStores
	 */
	public void alliedShopsServiceFee(MDStores mDStores) throws BusinessException, SystemException, Exception {
		try {
			/**
			 * 处理联盟店(检测是否服务费状态是否为1,如果为1,检测金币是否大于等于2000,是则扣除金币,解冻98元,
			 * 如果不是，检测可用余额是否大于0，如果是，则进行解冻98元，然后扣除98元，并设置会员服务状态为0)
			 **/
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			MDStoresServiceFee serviceFee = new MDStoresServiceFee();
			serviceFee.setDate_id(DateUtil.getFormatDate("yyyyMMdd"));
			serviceFee.setCreated_date(new Date());
			serviceFee.setStores_id(mDStores.getStores_id());
			serviceFee.setCash(new BigDecimal("0"));
			serviceFee.setGold(0);
			// 联盟店会员服务费的结算状态(默认为异常)
			Integer service_fee = 2;
			// 如果联盟店未冻结金额,则不进行结算
			if (mDStores.getService_fee() == 1) {
				// 查询余额
				reqParams.put("service", "finance.memberAssetFind");
				bizParams.put("member_id", mDStores.getStores_id());
				bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
				resultMap = FastJsonUtil.jsonToMap(resultStr);
				if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
					throw new BusinessException((String) resultMap.get("error_code"),
							(String) resultMap.get("error_msg"));
				}
				Map<String, Object> assetMap = (Map<String, Object>) resultMap.get("data");
				String gold = assetMap.get("gold") + "";
				// 判断可用金币余额是否足够
				if (MoneyUtil.moneyComp(gold, "2000")) {
					// 扣金币
					resultMap.clear();
					reqParams.clear();
					bizParams.clear();
					reqParams.put("service", "finance.balancePay");
					bizParams.put("data_source", "SJLY_03");
					bizParams.put("payment_way_key", "ZFFS_08");
					bizParams.put("detail", "会员费");
					bizParams.put("amount", "2000");
					bizParams.put("buyer_id", mDStores.getStores_id());
					bizParams.put("seller_id", Constant.MEMBER_ID_MTH);
					bizParams.put("out_trade_body", FastJsonUtil.toJson(bizParams));
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
					resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						throw new BusinessException((String) resultMap.get("error_code"),
								(String) resultMap.get("error_msg"));
					}
					// 解冻余额
					resultMap.clear();
					reqParams.clear();
					bizParams.clear();
					reqParams.put("service", "finance.balanceUnFreeze");
					bizParams.put("data_source", Constant.DATA_SOURCE_SJLY_03);
					bizParams.put("member_id", mDStores.getStores_id());
					bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
					bizParams.put("detail", "会员费解冻");
					bizParams.put("amount", "98");
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
					resultMap = FastJsonUtil.jsonToMap(resultStr);
					if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
						throw new BusinessException((String) resultMap.get("error_code"),
								(String) resultMap.get("error_msg"));
					}
					serviceFee.setGold(2000);
					// 发送短信
					sendMsg(mDStores.getContact_tel(), "恭喜您本月赚取超过2000金币，系统已返还98元技术服务费。");
					service_fee = 0;
				} else {
					BigDecimal cash_balance = new BigDecimal(assetMap.get("cash_balance") + "");
					BigDecimal cash_froze = new BigDecimal(assetMap.get("cash_froze") + "");
					BigDecimal usable_balance = MoneyUtil.moneySub(cash_balance, cash_froze);
					// 判断可用余额是否足够
					if (MoneyUtil.moneyComp(usable_balance, new BigDecimal("0.00"))) {
						// 解冻余额
						resultMap.clear();
						reqParams.clear();
						bizParams.clear();
						reqParams.put("service", "finance.serviceFeeUnFreeze");
						bizParams.put("data_source", Constant.DATA_SOURCE_SJLY_03);
						bizParams.put("member_id", mDStores.getStores_id());
						bizParams.put("member_type_key", Constant.MEMBER_TYPE_STORES);
						bizParams.put("detail", "会员费解冻");
						reqParams.put("params", FastJsonUtil.toJson(bizParams));
						resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
						resultMap = FastJsonUtil.jsonToMap(resultStr);
						if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
							throw new BusinessException((String) resultMap.get("error_code"),
									(String) resultMap.get("error_msg"));
						}
						// 扣除余额
						resultMap.clear();
						reqParams.clear();
						bizParams.clear();
						reqParams.put("service", "finance.balancePay");
						bizParams.put("data_source", "SJLY_03");
						bizParams.put("payment_way_key", "ZFFS_05");
						bizParams.put("detail", "会员费扣除");
						bizParams.put("amount", "98");
						bizParams.put("buyer_id", mDStores.getStores_id());
						bizParams.put("seller_id", Constant.MEMBER_ID_MTH);
						bizParams.put("out_trade_body", FastJsonUtil.toJson(bizParams));
						reqParams.put("params", FastJsonUtil.toJson(bizParams));
						resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
						resultMap = FastJsonUtil.jsonToMap(resultStr);
						if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
							throw new BusinessException((String) resultMap.get("error_code"),
									(String) resultMap.get("error_msg"));
						}
						serviceFee.setCash(new BigDecimal("98"));
						sendMsg(mDStores.getContact_tel(), "由于金币余额不足2000，系统已从您账户扣除98元用于技术服务费，谢谢合作。");
						service_fee = 0;
					}
				}
				// 更新会员费状态
				if (service_fee == 2) {
					bizParams.clear();
					bizParams.put("stores_id", mDStores.getStores_id());
					bizParams.put("service_fee", service_fee);
					storesDao.updateMDStores(bizParams);
				}
				// 会员费状态为0,表示结算整个,记录日志
				if (service_fee == 0) {
					serviceFee.setRemark("联盟店会员费结算成功");
					memberDao.insertMDStoresServiceFee(serviceFee);
				}

				// 记录日志
				bizParams.clear();
				bizParams.put("log_id", IDUtil.getUUID());
				bizParams.put("stores_id", mDStores.getStores_id());
				bizParams.put("category", "settlement_service_free");
				bizParams.put("tracked_date", new Date());
				bizParams.put("event", "联盟店会员费结算");
				memberDao.insertMDStoresScheduleLog(bizParams);
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
