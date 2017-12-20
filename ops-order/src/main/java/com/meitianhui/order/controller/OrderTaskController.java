package com.meitianhui.order.controller;

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
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.service.OrderTaskService;

/**
 * 订单定时任务
 * 
 * @author Tiny
 *
 */
@Controller
@RequestMapping("/orderTask")
public class OrderTaskController extends BaseController {

	@Autowired
	public RedisUtil redisUtil;

	@Autowired
	private OrderTaskService orderTaskService;

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			String operateName = request.getParameter("service");
			if ("order.fgOrderAutoCancel".equals(operateName)) {
				fgOrderAutoCancel(paramsMap, result);
			} else if ("order.proPsGroupOrder".equals(operateName)) {
				proPsGroupOrder(paramsMap, result);
			} else if ("order.psOrderAutoReceived".equals(operateName)) {
				psOrderAutoReceived(paramsMap, result);
			} else if ("order.pcOrderAutoReceived".equals(operateName)) {
				pcOrderAutoReceived(paramsMap, result);
			} else if ("order.tsActivityCheck".equals(operateName)) {
				tsActivityCheck(paramsMap, result);
			} else if ("order.odTaskTimeout".equals(operateName)) {
				odTaskTimeout(paramsMap, result);
			} else if ("order.tsOrderAutoReceived".equals(operateName)) {
				tsOrderAutoReceived(paramsMap, result);
			} else if ("order.tsActivityAutoReceived".equals(operateName)) {
				tsActivityAutoReceived(paramsMap, result);
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
	 * 领了么超时订单自动取消
	 * 
	 * @Title: fgOrderAutoCancel
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void fgOrderAutoCancel(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_fgOrderAutoCancel]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			orderTaskService.fgOrderAutoCancel(paramsMap, result);
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
	 * 处理团购预售订单
	 * 
	 * @Title: proPsGroupOrder
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void proPsGroupOrder(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_proPsGroupOrder]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			orderTaskService.proPsGroupOrder(paramsMap, result);
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
	 * 我要批超时订单自动确认收货
	 * 
	 * @Title: psOrderAutoReceived
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void psOrderAutoReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_psOrderAutoReceived]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			orderTaskService.psOrderAutoReceived(paramsMap, result);
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
	 * 精选特卖超时订单自动确认收货
	 * 
	 * @Title: pcOrderAutoReceived
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void pcOrderAutoReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_pcOrderAutoReceived]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			orderTaskService.pcOrderAutoReceived(paramsMap, result);
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
	 * 伙拼团活动验证
	 * 
	 * @Title: tsActivityCheck
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsActivityCheck(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_tsActivityCheck]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			orderTaskService.tsActivityCheck(paramsMap, result);
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
	 * 处理过期任务
	 * 
	 * @Title: odTaskTimeout
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void odTaskTimeout(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_odTaskTimeout]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			orderTaskService.handleOdTaskTimeout(paramsMap, result);
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

	/***
	 * 伙拼团订单超时自动确认收货
	 * 
	 * @Title: tsOrderAutoReceived
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderAutoReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_tsOrderAutoReceived]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			orderTaskService.tsOrderAutoReceived(paramsMap, result);
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

	/***
	 * 伙拼团活动超时自动确认收货
	 * 
	 * @Title: tsActivityAutoReceived
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsActivityAutoReceived(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_tsActivityAutoReceived";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			orderTaskService.tsActivityAutoReceived(paramsMap, result);
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
