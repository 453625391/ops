package com.meitianhui.goods.controller;

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
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.service.GoodsTaskService;

/**
 * 商品管理定时任务
 * 
 * @author Tiny
 *
 */
@Controller
@RequestMapping("/goodsTask")
public class GoodsTaskController extends BaseController {
	@Autowired
	public RedisUtil redisUtil;

	@Autowired
	private GoodsTaskService goodsTaskService;

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			String operateName = request.getParameter("service");
			if ("goods.couponStatusRefresh".equals(operateName)) {
				couponStatusRefresh(paramsMap, result);
			} else if ("goods.ldActivitiesStatusRefresh".equals(operateName)) {
				ldActivitiesStatusRefresh(paramsMap, result);
			} else if ("goods.goodsViewCountSave".equals(operateName)) {
				goodsViewCountSave(paramsMap, result);
			} else if ("goods.updateDisabledGcActivity".equals(operateName)) {
				updateDisabledGcActivity(paramsMap, result);
			} else if ("goods.plActivityStatusRefresh".equals(operateName)) {
				plActivityStatusRefresh(paramsMap, result);
			} else if ("goods.gdBenefitStatusRefresh".equals(operateName)) {
				gdBenefitStatusRefresh(paramsMap, result);
			} else if ("goods.gdActivityStatusRefresh".equals(operateName)) {
				gdActivityStatusRefresh(paramsMap, result);
			} else if ("goods.psGoodsAutoOffline".equals(operateName)) {
				psGoodsAutoOffline(paramsMap, result);
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
	 * 刷新失效优惠券状态
	* @Title: couponStatusRefresh  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	public void couponStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_couponStatusRefresh]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			goodsTaskService.couponStatusRefresh(paramsMap, result);
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
	 * 活动揭晓
	* @Title: ldActivitiesStatusRefresh  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	public void ldActivitiesStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_ldActivitiesStatusRefresh]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			goodsTaskService.ldActivitiesStatusRefresh(paramsMap, result);
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
	 * 同步商品流量数量
	* @Title: goodsViewCountSave  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	public void goodsViewCountSave(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_goodsViewCountSave]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			goodsTaskService.goodsViewCountSave(paramsMap, result);
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
	 * 处理过期红包
	* @Title: updateDisabledGcActivity  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	public void updateDisabledGcActivity(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_updateDisabledGcActivity]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			goodsTaskService.updateDisabledGcActivity(paramsMap, result);
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
	 * 抽奖活动状态更新
	* @Title: plActivityStatusRefresh  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	public void plActivityStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_plActivityStatusRefresh]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			goodsTaskService.plActivityStatusRefresh(paramsMap, result);
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
	 * 权益劵状态刷新
	* @Title: gdBenefitStatusRefresh  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	public void gdBenefitStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_gdBenefitStatusRefresh]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			goodsTaskService.gdBenefitStatusRefresh(paramsMap, result);
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
	 * 权益活动状态刷新
	* @Title: gdActivityStatusRefresh  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	public void gdActivityStatusRefresh(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_gdActivityStatusRefresh]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			goodsTaskService.gdActivityStatusRefresh(paramsMap, result);
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
	 * 商品自动下架
	* @Title: psGoodsAutoOffline  
	* @param paramsMap
	* @param result
	* @throws BusinessException
	* @throws SystemException
	* @throws Exception
	* @author tiny
	 */
	public void psGoodsAutoOffline(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock lock = null;
		String lockKey = "[task_psGoodsAutoOffline]";
		try {
			lock = new RedisLock(redisUtil, lockKey, 10 * 1000);
			lock.lock();
			String lockValue = redisUtil.getStr(lockKey);
			if (StringUtil.isNotEmpty(lockValue)) {
				return;
			}
			redisUtil.setStr(lockKey, lockKey, 600);

			goodsTaskService.psGoodsAutoOffline(paramsMap, result);
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
