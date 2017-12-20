package com.meitianhui.finance.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meitianhui.base.controller.BaseController;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.finance.constant.Constant;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.service.AlipayPayService;
import com.meitianhui.finance.service.WechatPayService;

/**
 * 商品管理定时任务
 * 
 * @author Tiny
 *
 */
@Controller
@RequestMapping("/financeTask")
public class FinanceTaskController extends BaseController {

	@Autowired
	private AlipayPayService alipayPayService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private WechatPayService wechatPayService;

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			String operateName = request.getParameter("service");
			if ("facePay.alipay.billImport".equals(operateName)) {
				facePayAlipayBillImport(paramsMap, result);
			} else if ("consumer.wechat.billImport".equals(operateName)) {
				consumerWechatBillImport(paramsMap, result);
			} else if ("stores.wechat.billImport".equals(operateName)) {
				storesWechatBillImport(paramsMap, result);
			} else if ("huidian.wechat.billImport".equals(operateName)) {
				huidianWechatBillImport(paramsMap, result);
			} else if ("shume.wechat.billImport".equals(operateName)) {
				shumeWechatBillImport(paramsMap, result);
			} else if ("cashier.wechat.billImport".equals(operateName)) {
				cashierWechatBillImport(paramsMap, result);
			} else if ("hyg.wechat.billImport".equals(operateName)) {
				hygWechatBillImport(paramsMap, result);
			} else if ("alipay.billCheck".equals(operateName)) {
				alipayBillCheck(paramsMap, result);
			} else if ("wechat.billCheck".equals(operateName)) {
				wechatBillCheck(paramsMap, result);
			} else if ("stores.stores.storeFrozeCashForUsableCash".equals(operateName)) {//把店东佣金冻结金额变成可提金额
				//storeFrozeCashForUsableCash(paramsMap, result); 
			} else {
				throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR,
						RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
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
	 * 支付宝账单导入
	 * 
	 * @Title: storesWechatBillImport
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void facePayAlipayBillImport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_facePayAlipayBillImport]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			ValidateUtil.validateParams(paramsMap, new String[] { "bill_date" });
			String bill_date = paramsMap.get("bill_date") + "";
			String app_id = PropertiesConfigUtil.getProperty("alipay.face_pay_app_id");
			String private_key = PropertiesConfigUtil.getProperty("alipay.face_pay_open_api_private_key");
			alipayPayService.alipayBillImport(bill_date, app_id, private_key);
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
	 * 消费者微信账单导入
	 * 
	 * @Title: storesWechatBillImport
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void consumerWechatBillImport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_consumerWechatBillImport]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			ValidateUtil.validateParams(paramsMap, new String[] { "bill_date" });
			String bill_date = paramsMap.get("bill_date") + "";
			String app_key = PropertiesConfigUtil.getProperty("wechat.consumer_app_key");
			String app_id = PropertiesConfigUtil.getProperty("wechat.consumer_app_id");
			String mch_id = PropertiesConfigUtil.getProperty("wechat.consumer_mch_id");
			String cert_local_path = PropertiesConfigUtil.getProperty("wechat.consumer_cert_local_path");
			String cert_password = PropertiesConfigUtil.getProperty("wechat.consumer_cert_password");
			wechatPayService.wechatBillImport(bill_date, Constant.MEMBER_TYPE_CONSUMER, app_key, app_id, mch_id,
					cert_local_path, cert_password);
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
	 * 店东微信账单导入
	 * 
	 * @Title: storesWechatBillImport
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void storesWechatBillImport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_storesWechatBillImport]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			ValidateUtil.validateParams(paramsMap, new String[] { "bill_date" });
			String bill_date = paramsMap.get("bill_date") + "";
			String app_key = PropertiesConfigUtil.getProperty("wechat.store_app_key");
			String app_id = PropertiesConfigUtil.getProperty("wechat.store_app_id");
			String mch_id = PropertiesConfigUtil.getProperty("wechat.store_mch_id");
			String cert_local_path = PropertiesConfigUtil.getProperty("wechat.store_cert_local_path");
			String cert_password = PropertiesConfigUtil.getProperty("wechat.store_cert_password");
			wechatPayService.wechatBillImport(bill_date, Constant.MEMBER_TYPE_STORES, app_key, app_id, mch_id,
					cert_local_path, cert_password);
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
	 * 惠点科技公众号微信账单导入
	 * 
	 * @Title: huidianWechatBillImport
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void huidianWechatBillImport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_huidianWechatBillImport]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			ValidateUtil.validateParams(paramsMap, new String[] { "bill_date" });
			String bill_date = paramsMap.get("bill_date") + "";
			String app_key = PropertiesConfigUtil.getProperty("wechat.huidian_app_key");
			String app_id = PropertiesConfigUtil.getProperty("wechat.huidian_app_id");
			String mch_id = PropertiesConfigUtil.getProperty("wechat.huidian_mch_id");
			String cert_local_path = PropertiesConfigUtil.getProperty("wechat.huidian_cert_local_path");
			String cert_password = PropertiesConfigUtil.getProperty("wechat.huidian_cert_password");
			wechatPayService.wechatBillImport(bill_date, "huidian", app_key, app_id, mch_id, cert_local_path,
					cert_password);
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
	 * 熟么微信账单导入
	 * 
	 * @Title: shumeWechatBillImport
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void shumeWechatBillImport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_shumeWechatBillImport]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			ValidateUtil.validateParams(paramsMap, new String[] { "bill_date" });
			String bill_date = paramsMap.get("bill_date") + "";
			String app_key = PropertiesConfigUtil.getProperty("wechat.shume_app_key");
			String app_id = PropertiesConfigUtil.getProperty("wechat.shume_app_id");
			String mch_id = PropertiesConfigUtil.getProperty("wechat.shume_mch_id");
			String cert_local_path = PropertiesConfigUtil.getProperty("wechat.shume_cert_local_path");
			String cert_password = PropertiesConfigUtil.getProperty("wechat.shume_cert_password");
			wechatPayService.wechatBillImport(bill_date, "shume", app_key, app_id, mch_id, cert_local_path,
					cert_password);
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
	 * 惠点收银微信账单导入
	 * 
	 * @Title: shumeWechatBillImport
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void cashierWechatBillImport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_cashierWechatBillImport]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			ValidateUtil.validateParams(paramsMap, new String[] { "bill_date" });
			String bill_date = paramsMap.get("bill_date") + "";
			String app_key = PropertiesConfigUtil.getProperty("wechat.cashier_app_key");
			String app_id = PropertiesConfigUtil.getProperty("wechat.cashier_app_id");
			String mch_id = PropertiesConfigUtil.getProperty("wechat.cashier_mch_id");
			String cert_local_path = PropertiesConfigUtil.getProperty("wechat.cashier_cert_local_path");
			String cert_password = PropertiesConfigUtil.getProperty("wechat.cashier_cert_password");
			wechatPayService.wechatBillImport(bill_date, "cashier", app_key, app_id, mch_id, cert_local_path,
					cert_password);
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
	 * 惠驿哥微信账单导入
	 * 
	 * @Title: shumeWechatBillImport
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void hygWechatBillImport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_hygWechatBillImport]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			ValidateUtil.validateParams(paramsMap, new String[] { "bill_date" });
			String bill_date = paramsMap.get("bill_date") + "";
			String app_key = PropertiesConfigUtil.getProperty("wechat.hyg_app_key");
			String app_id = PropertiesConfigUtil.getProperty("wechat.hyg_app_id");
			String mch_id = PropertiesConfigUtil.getProperty("wechat.hyg_mch_id");
			String cert_local_path = PropertiesConfigUtil.getProperty("wechat.hyg_cert_local_path");
			String cert_password = PropertiesConfigUtil.getProperty("wechat.hyg_cert_password");
			wechatPayService.wechatBillImport(bill_date, "hyg", app_key, app_id, mch_id, cert_local_path,
					cert_password);
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
	 * 支付宝对账
	 * 
	 * @Title: alipayBillCheck
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void alipayBillCheck(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_alipayBillCheck]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			ValidateUtil.validateParams(paramsMap, new String[] { "bill_date" });
			String bill_date = paramsMap.get("bill_date") + "";
			alipayPayService.billCheck(bill_date);

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
	 * 微信对账
	 * 
	 * @Title: wechatBillCheck
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void wechatBillCheck(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_wechatBillCheck]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			ValidateUtil.validateParams(paramsMap, new String[] { "bill_date" });
			String bill_date = paramsMap.get("bill_date") + "";
			wechatPayService.billCheck(bill_date);
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

}
