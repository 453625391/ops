package com.meitianhui.order.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meitianhui.base.controller.BaseController;
import com.meitianhui.common.constant.PageParam;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.service.CsOrderService;
import com.meitianhui.order.service.FgOrderService;
import com.meitianhui.order.service.GeOrderService;
import com.meitianhui.order.service.MobileRechargeService;
import com.meitianhui.order.service.OdTaskService;
import com.meitianhui.order.service.OrderService;
import com.meitianhui.order.service.PcOrderService;
import com.meitianhui.order.service.PeOrderService;
import com.meitianhui.order.service.PsOrderService;
import com.meitianhui.order.service.TsActivityService;
import com.meitianhui.order.service.TsOrderService;

/**
 * 订单管理
 * 
 * @author Tiny
 *
 */
@Controller
@RequestMapping("/order")
@SuppressWarnings("unchecked")
public class OrderController extends BaseController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private PsOrderService psOrderService;

	@Autowired
	private PcOrderService pcOrderService;

	@Autowired
	private FgOrderService fgOrderService;

	@Autowired
	private MobileRechargeService mobileRechargeService;

	@Autowired
	private CsOrderService csOrderService;

	@Autowired
	private GeOrderService geOrderService;
	
	@Autowired
	private PeOrderService peOrderService;

	@Autowired
	private TsActivityService tsActivityService;

	@Autowired
	private TsOrderService tsOrderService;

	@Autowired
	private OdTaskService odTaskService;

	public static ExecutorService threadExecutor = Executors.newFixedThreadPool(20);

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		String type = operateName.split("\\.")[0];
		if (type.equals("tsActivity")) {
			// 伙拼团
			tsActivityServer(request, response, paramsMap, result);
		} else if (type.equals("tsOrder")) {
			// 伙拼团订单
			tsOrderServer(request, response, paramsMap, result);
		} else if (type.equals("odTask")) {
			// 任务发布
			odTaskServer(request, response, paramsMap, result);
		} else if (type.equals("pcOrder")) {
			// 掌上便利店
			pcOrderServer(request, response, paramsMap, result);
		}else if (type.equals("fgOrder")) {
			// 供应商下领了么商品订单统计
			fgOrderServer(request, response, paramsMap, result);
		} else {
			appServer(request, response, paramsMap, result);
		}
	}

	/**
	 * 伙拼团活动
	 * 
	 * @Title: tsActivityServer
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	private void tsActivityServer(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("tsActivity.consumer.ladderTsActivityForConsumerCreate".equals(operateName)) {
			tsActivityService.ladderTsActivityForConsumerCreate(paramsMap, result);
		} else if ("tsActivity.consumer.ladderTsActivityCreate".equals(operateName)) {
			tsActivityService.ladderTsActivityCreate(paramsMap, result);
		} else if ("tsActivity.consumer.tsActivityCancel".equals(operateName)) {
			tsActivityService.handleTsActivityCancelForConsumer(paramsMap, result);
		} else if ("tsActivity.consumer.tsActivityCountForH5".equals(operateName)) {
			tsActivityService.tsActivityCountForH5(paramsMap, result);
		} else if ("tsActivity.consumer.tsActivityDetail".equals(operateName)) {
			tsActivityService.tsActivityDetail(paramsMap, result);
		} else if ("tsActivity.stores.tsActivityListPageFind".equals(operateName)) {
			tsActivityListForStoresPageFind(request, paramsMap, result);
		} else if ("tsActivity.consumer.nearbyTsActivityListFind".equals(operateName)) {
			nearbyTsActivityListFind(paramsMap, result);
		} else if ("tsActivity.consumer.sponsorTsActivityListPageFind".equals(operateName)) {
			sponsorTsActivityListPageFind(request, paramsMap, result);
		} else if ("tsActivity.consumer.qualificationValidate".equals(operateName)) {
			tsActivityService.qualificationValidate(paramsMap, result);
		} else if ("tsActivity.consumer.joinListPageFind".equals(operateName)) {
			joinListPageFind(request, paramsMap, result);
		} else if ("tsActivity.operate.tsActivityListPageFind".equals(operateName)) {
			tsActivityListForOpPageFind(request, paramsMap, result);
		} else if ("tsActivity.operate.tsActivityEdit".equals(operateName)) {
			tsActivityService.tsActivityEdit(paramsMap, result);
		} else if ("tsActivity.operate.tsActivityCancel".equals(operateName)) {
			tsActivityService.handleTsActivityCancelForOp(paramsMap, result);
		} else if ("tsActivity.operate.tsActivityDeliver".equals(operateName)) {
			tsActivityService.handleTsActivityDeliver(paramsMap, result);
		} else if ("tsActivity.stores.tsActivityReceived".equals(operateName)) {
			tsActivityService.handleTsActivityReceived(paramsMap, result);
		} else if ("tsActivity.stores.tsActivityDetail".equals(operateName)) {
			tsActivityService.tsActivityDetail(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR,
					RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR) + ";service:" + operateName);
		}
	}

	/***
	 * 任务发布
	 * 
	 * @author 丁硕
	 * @date 2017年3月8日
	 */
	private void odTaskServer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("odTask.operate.odTaskCreate".equals(operateName)) {// 新增任务
			odTaskService.odTaskCreate(paramsMap, result);
		} else if ("odTask.operate.odTaskUpdate".equals(operateName)) {// 任务更新
			odTaskService.odTaskUpdate(paramsMap, result);
		} else if ("odTask.operate.odTaskForOpListPageFind".equals(operateName)) { // 运营任务列表查询
			odTaskForOpListPageFind(request, paramsMap, result);
		} else if ("odTask.operate.odTaskDetailForOpFind".equals(operateName)) { // 任务详情查询
			odTaskService.odTaskDetailForOpFind(paramsMap, result);
		} else if ("odTask.operate.odTaskProcessingForOpListPageFind".equals(operateName)) { // 子任务列表查询
			odTaskProcessingForOpListPageFind(request, paramsMap, result);
		} else if ("odTask.operate.odTaskProcessingDetailForOpFind".equals(operateName)) { // 子任务详情查询
			odTaskService.odTaskProcessingDetailForOpFind(paramsMap, result);
		} else if ("odTask.operate.odTaskStop".equals(operateName)) { // 运营终止任务
			odTaskService.handleOdTaskStopForOp(paramsMap, result);
		} else if ("odTask.stores.odTaskNewListPageFind".equals(operateName)) { // 店东新任务列表
			paramsMap.put("suitable_app", "store");
			storesOdTaskNewListPageFind(request, paramsMap, result);
		} else if ("odTask.stores.odTaskProcessingListPageFind".equals(operateName)) { // 店东任务列表
			storesOdTaskProcessingListPageFind(request, paramsMap, result);
		} else if ("odTask.stores.odTaskNewCountForStoresFind".equals(operateName)) { // 店东任务数统计
			paramsMap.put("suitable_app", "store");
			odTaskService.odTaskNewCountForMemberFind(paramsMap, result);
		} else if ("odTask.stores.odTaskDetailFind".equals(operateName)) { // 任务详情查询
			odTaskService.odTaskForMemberDetailFind(paramsMap, result);
		} else if ("odTask.stores.odTaskProcessingDetailFind".equals(operateName)) { // 子任务查询
			odTaskService.odTaskProcessingForMemberDetailFind(paramsMap, result);
		} else if ("odTask.stores.odTaskJoin".equals(operateName)) { // 参与任务
			paramsMap.put("suitable_app", "store");
			odTaskService.handleOdTaskJoin(paramsMap, result);
		} else if ("odTask.stores.odTaskProcessingSubmit".equals(operateName)) { // 提交任务
			odTaskService.handleOdTaskProcessingSubmit(paramsMap, result);
		} else if ("odTask.app.odTaskProcessingLogListPageFind".equals(operateName)) { // 提交任务
			odTaskProcessingLogListPageFind(request, paramsMap, result);
		} else if ("odTask.operate.odTaskProcessingPass".equals(operateName)) { // 审核通过
			odTaskService.handleOdTaskProcessingPass(paramsMap, result);
		} else if ("odTask.operate.odTaskProcessingFail".equals(operateName)) { // 任务失败
			odTaskService.handleOdTaskProcessingFail(paramsMap, result);
		} else if ("odTask.operate.odTaskProcessingReject".equals(operateName)) { // 驳回任务
			odTaskService.handleOdTaskProcessingReject(paramsMap, result);
		} else if ("odTask.operate.odTaskProcessingSettle".equals(operateName)) { // 任务结算
			odTaskService.handleOdTaskProcessingSettle(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR,
					RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR) + ";service:" + operateName);
		}
	}

	/**
	 * 查询附近某一商品的团购列表
	 * 
	 * @Title: nearbyTsActivityListFind
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void nearbyTsActivityListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			paramsMap.put("range", "2000");
			tsActivityService.nearbyTsActivityListFind(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 发起的伙拼团列表
	 * 
	 * @Title: sponsorTsActivityListFind
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void sponsorTsActivityListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			tsActivityService.sponsorTsActivityListFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 发起的伙拼团列表
	 * 
	 * @Title: sponsorTsActivityListFind
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsActivityListForStoresPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			tsActivityService.tsActivityListForStoresPageFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 伙拼团活动列表
	 * 
	 * @Title: tsActivityListForOpPageFind
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsActivityListForOpPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			tsActivityService.tsActivityListForOpFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 伙拼团参与记录
	 * 
	 * @Title: joinListPageFind
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void joinListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			tsActivityService.joinListFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 伙拼团订单
	 * 
	 * @Title: tsActivityServer
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	private void tsOrderServer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("tsOrder.consumer.ladderTsOrderValidate".equals(operateName)) {
			tsOrderService.ladderTsOrderValidate(paramsMap, result);
		} else if ("tsOrder.consumer.ladderTsOrderCreate".equals(operateName)) {
			tsOrderService.ladderTsOrderCreate(paramsMap, result);
		} else if ("tsOrder.consumer.freeCouponPay".equals(operateName)) {
			tsOrderService.handleFreeCouponPay(paramsMap, result);
		} else if ("tsOrder.consumer.tsOrderCancel".equals(operateName)) {
			tsOrderService.handleTsOrderCancelForConsumer(paramsMap, result);
		} else if ("tsOrder.consumer.tsOrderReceived".equals(operateName)) {
			tsOrderService.handleTsOrderReceived(paramsMap, result);
		} else if ("tsOrder.consumer.tsOrderDetailFind".equals(operateName)) {
			tsOrderService.tsOrderDetailForConsumerFind(paramsMap, result);
		} else if ("tsOrder.consumer.tsOrderListPageFind".equals(operateName)) {
			tsOrderListForConsumerPageFind(request, paramsMap, result);
		} else if ("tsOrder.stores.tsOrderListPageFind".equals(operateName)) {
			tsOrderListForStoresPageFind(request, paramsMap, result);
		} else if ("tsOrder.operate.tsOrderListPageFind".equals(operateName)) {
			tsOrderListForOpPageFind(request, paramsMap, result);
		} else if ("tsOrder.operate.tsOrderCancel".equals(operateName)) {
			tsOrderService.tsOrderCancelForOp(paramsMap, result);
		} else if ("tsOrder.operate.tsOrderSettleListPageFind".equals(operateName)) {
			tsOrderSettleListForOpPageFind(request, paramsMap, result);
		} else if ("tsOrder.operate.tsOrderSettleCount".equals(operateName)) {
			tsOrderService.tsOrderSettleCount(paramsMap, result);
		} else if ("tsOrder.operate.tsOrderSettle".equals(operateName)) {
			tsOrderService.handleTsOrderSettle(paramsMap, result);
		} else if ("tsOrder.stores.tsOrderCountForStoresSale".equals(operateName)) {
			tsOrderService.tsOrderForStoresSaleCountFind(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}

	/**
	 * 伙拼团订单列表
	 * 
	 * @Title: tsOrderListForOpPageFind
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderListForOpPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			tsOrderService.tsOrderListForOpFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 伙拼团订单结算列表
	 * 
	 * @Title: tsOrderSettleListForOpPageFind
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderSettleListForOpPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			tsOrderService.tsOrderSettleListForOpFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 伙拼团订单列表
	 * 
	 * @Title: joinTsActivityListFind
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderListForConsumerPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			tsOrderService.tsOrderListForConsumerFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 伙拼团订单列表
	 * 
	 * @Title: joinTsActivityListFind
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void tsOrderListForStoresPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			tsOrderService.tsOrderListFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 掌上便利店
	 * 
	 * @Title: pcOrderServer
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	private void pcOrderServer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("pcOrder.stores.pcOrderSellCountForStoresSale".equals(operateName)) {
			pcOrderService.pcOrderSellCountForStoresSaleFind(paramsMap, result);
		} else if ("pcOrder.operation.pcOrderListFind".equals(operateName)) { // 运营掌上便利店订单查询
			pcOrderListPageFindForOp(request, paramsMap, result);
		} else if ("pcOrder.operation.pcOrderAssigned".equals(operateName)) { // 掌上便利店订单派工
			pcOrderService.handlePcOrderAssigned(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}
	
	/**
	 * 供应商下领了么商品统计
	 * 
	 * @Title: pcOrderServer
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	private void fgOrderServer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("fgOrder.supply.fgOrderSupplyCount".equals(operateName)) {
			fgOrderService.fgOrderSupplyCount(paramsMap, result);
		}else if ("fgOrder.consumer.fgOrderCount".equals(operateName)) {
			fgOrderService.selectFgOrderByStatus(paramsMap, result);  
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}  

	/**
	 * 老接口
	 * 
	 * @Title: appServer
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	private void appServer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			String operateName = request.getParameter("service");
			if ("order.wypOrderCreate".equals(operateName)) {
				psOrderService.wypOrderCreate(paramsMap, result);
			} else if ("order.wypOrderPayInfoFind".equals(operateName)) {
				psOrderService.wypOrderPayInfoFind(paramsMap, result);
			} else if ("order.wypOrderPayNotity".equals(operateName)) {
				psOrderService.handleWypOrderPayNotity(paramsMap, result);
			} else if ("order.psOrderEdit".equals(operateName)) {
				psOrderService.psOrderEdit(paramsMap, result);
			} else if ("order.psOrderStatusEdit".equals(operateName)) {
				psOrderService.psOrderStatusEdit(paramsMap, result);
			} else if ("order.psOrderDelivered".equals(operateName)) {
				psOrderService.handlePsOrderDelivered(paramsMap, result);
			} else if ("order.psOrderReceived".equals(operateName)) {
				psOrderService.handlePsOrderReceived(paramsMap, result);
			} else if ("order.psOrderCancelled".equals(operateName)) {
				psOrderService.handlePsOrderCancelled(paramsMap, result);
			} else if ("order.psOrderRefund".equals(operateName)) {
				psOrderService.handlePsOrderRefund(paramsMap, result);
			} else if ("order.psOrderListPageFindForOp".equals(operateName)) { // 运营我要批订单查询列表
				psOrderListPageFindForOp(request, paramsMap, result);
			} else if ("order.wypOrderListForOpPageFind".equals(operateName)) {
				wypOrderListForOpPageFind(request, paramsMap, result);
			} else if ("order.wypOrderItemForOpFind".equals(operateName)) {
				psOrderService.wypOrderItemForOpFind(paramsMap, result);
			} else if ("order.wypOrderListForStoresPageFind".equals(operateName)) {
				wypOrderListForStoresPageFind(request, paramsMap, result);
			} else if ("order.wypOrderItemForStoresFind".equals(operateName)) {
				psOrderService.wypOrderItemForStoresFind(paramsMap, result);
			} else if ("order.psOrderListPageFind".equals(operateName)) {
				psOrderListPageFind(request, paramsMap, result);
			} else if ("order.psOrderListForOpPageFind".equals(operateName)) {
				psOrderListForOpPageFind(request, paramsMap, result);
			} else if ("order.psOrderSettlementCount".equals(operateName)) {
				psOrderService.psOrderSettlementCount(paramsMap, result);
			} else if ("order.initiateGroup".equals(operateName)) {
				psOrderService.handleInitiateGroup(paramsMap, result);
			} else if ("order.psGroupGoodsListFind".equals(operateName)) {
				psOrderService.psGroupGoodsListFind(paramsMap, result);
			} else if ("order.psGroupGoodsDetailFind".equals(operateName)) {
				psOrderService.psGroupGoodsDetailFind(paramsMap, result);
			} else if ("order.psGroupSubOrderCreateNotify".equals(operateName)) {
				psOrderService.psGroupSubOrderCreateNotify(paramsMap, result);
			} else if ("order.psOrderCount".equals(operateName)) {
				psOrderService.psOrderCountFind(paramsMap, result);
			} else if ("order.psGroupSubOrderForConsumerListPageFind".equals(operateName)) {
				psGroupSubOrderForConsumerListPageFind(request, paramsMap, result);
			} else if ("order.psGroupOrderForStoresListPageFind".equals(operateName)) {
				psGroupOrderForStoresListPageFind(request, paramsMap, result);
			} else if ("order.psGroupSubOrderForStoresListPageFind".equals(operateName)) {
				psGroupSubOrderForStoresListPageFind(request, paramsMap, result);
			} else if ("order.psGroupSubOrderForOpListPageFind".equals(operateName)) {
				psGroupSubOrderForOpListPageFind(request, paramsMap, result);
			} else if ("order.psSubOrderCanCelled".equals(operateName)) {
				psOrderService.handlePsSubOrderCanCelled(paramsMap, result);
			} else if ("order.psSubOrderReceived".equals(operateName)) {
				psOrderService.handlePsSubOrderReceived(paramsMap, result);
			} else if ("order.psOrderSettlement".equals(operateName)) {
				psOrderService.handlePsOrderSettlement(paramsMap, result);
			} else if ("order.freeGetValidate".equals(operateName)) {
				fgOrderService.freeGetValidate(paramsMap, result);
			} else if ("order.consumerFreeGetRecordCreate".equals(operateName)) {
				fgOrderService.consumerFreeGetRecordCreate(paramsMap, result);
			} else if ("order.storesFreeGetRecordCreate".equals(operateName)) {
				fgOrderService.storesFreeGetRecordCreate(paramsMap, result);
			} else if ("order.localFreeGet".equals(operateName)) {
				fgOrderService.handleLocalFreeGet(paramsMap, result);
			} else if ("order.fgOrderCreateNotity".equals(operateName)) {
				fgOrderService.handleFgOrderCreateNotity(paramsMap, result);
			} else if ("order.fgOrderCancelled".equals(operateName)) {
				fgOrderService.handleFgOrderCancelled(paramsMap, result);
			} else if ("order.fgOrderCancelledForOp".equals(operateName)) {
				fgOrderService.handleFgOrderCancelledForOp(paramsMap, result);
			} else if ("order.fgOrderForOpEdit".equals(operateName)) {
				fgOrderService.fgOrderForOpEdit(paramsMap, result);
			} else if ("order.freeGetOrderImport".equals(operateName)) {
				freeGetOrderImport(paramsMap, result);
			} else if ("order.fgOrderListForOpPageFind".equals(operateName)) {
				fgOrderListForOpPageFind(request, paramsMap, result);
			} else if("order.fgOrderListForMoneyFind".equals(operateName)){
				fgOrderService.fgOrderListForMoneyFind(paramsMap, result);
			} else if("order.fgOrderFormMoneyTabulationFind".equals(operateName)){
				fgOrderService.fgOrderFormMoneyTabulationFind(paramsMap,result);
			} else if ("order.operation.fgOrderDetailFind".equals(operateName)) {
				fgOrderService.fgOrderDetailForOperationFind(paramsMap, result);
			} else if ("order.consumer.fgOrderDetailFind".equals(operateName)) {
				fgOrderService.fgOrderDetailForConsumerFind(paramsMap, result);
			} else if ("order.own.fgOrderDetailFind".equals(operateName)) {
				fgOrderService.fgOrderDetailForOwnFind(paramsMap, result);
			} else if ("order.fgOrderSettlementCount".equals(operateName)) {
				fgOrderService.fgOrderSettlementCount(paramsMap, result);
			} else if ("order.fgOrderListPageFind".equals(operateName)) {
				fgOrderListPageFind(request, paramsMap, result);
			} else if ("order.storesRecomConsumerFreeGetListFind".equals(operateName)) {
				fgOrderService.storesRecomConsumerFreeGetListFind(paramsMap, result);
			} else if ("order.storesRecomConsumerFreeGetListFindForMemberList".equals(operateName)) {
				fgOrderService.storesRecomConsumerFreeGetListFindForMemberList(paramsMap, result);
			} else if ("order.fgOrderConfirm".equals(operateName)) {
				fgOrderService.handleFgOrderConfirm(paramsMap, result);
			} else if ("order.fgOrderMessage".equals(operateName)) {
				fgOrderService.handleFgOrderMessage(paramsMap, result);
			} else if ("order.fgOrderSettlement".equals(operateName)) {
				fgOrderService.handleFgOrderSettlement(paramsMap, result);
			} else if ("order.fgOrderRevoke".equals(operateName)) {
				fgOrderService.handleFgOrderRevoke(paramsMap, result);
			} else if ("order.fgOrderExceptionCount".equals(operateName)) {
				fgOrderService.fgOrderExceptionCount(paramsMap, result);
			} else if ("order.fgOrderViolationCount".equals(operateName)) {
				fgOrderService.fgOrderViolationCount(paramsMap, result);
			} else if ("order.fgBlacklistCreate".equals(operateName)) {
				fgOrderService.fgBlacklistCreate(paramsMap, result);
			} else if ("order.fgBlacklistPageFind".equals(operateName)) {
				fgBlacklistPageFind(request, paramsMap, result);
			} else if ("order.fgBlacklistDelete".equals(operateName)) {
				fgOrderService.fgBlacklistDelete(paramsMap, result);
			} else if ("order.fgViolationCheck".equals(operateName)) {
				fgOrderService.fgViolationCheck(paramsMap, result);
			} else if ("order.orderSettlementListPageFind".equals(operateName)) {
				orderSettlementListPageFind(request, paramsMap, result);
			} else if ("order.orderSettlementPay".equals(operateName)) { // 订单结算付款
				orderService.handleOrderSettlementPay(paramsMap, result);
			} else if ("order.pcOrderPayCreateNotify".equals(operateName)) {
				pcOrderService.handlePcOrderPayCreateNotify(paramsMap, result);
			} else if ("order.pcOrderDetailPageForConsumerFind".equals(operateName)) {
				pcOrderDetailPageForConsumerFind(request, paramsMap, result);
			} else if ("order.zjsOrderForConsumerPageFind".equals(operateName)) {
				zjsOrderForConsumerPageFind(request, paramsMap, result);
			} else if ("order.orderByloadedCodeForStoresFind".equals(operateName)) {
				orderService.orderByloadedCodeForStoresFind(paramsMap, result);
			} else if ("order.pcOrderDetailPageFind".equals(operateName)) {
				pcOrderDetailPageFind(request, paramsMap, result);
			} else if ("order.pcOrderCancelled".equals(operateName)) {
				pcOrderService.handlePcOrderCancelled(paramsMap, result);
			} else if ("order.pcOrderDeliver".equals(operateName)) {
				pcOrderService.handlePcOrderDeliver(paramsMap, result);
			} else if ("order.pcOrderReceived".equals(operateName)) {
				pcOrderService.handlePcOrderReceived(paramsMap, result);
			} else if ("order.pcOrderPayedNum".equals(operateName)) {
				pcOrderPayedNum(paramsMap, result);
			} else if ("order.geOrderPayCreateNotify".equals(operateName)) {
				geOrderService.handleGeOrderPayCreateNotify(paramsMap, result);
			} else if ("order.geOrderDetailPageFind".equals(operateName)) {
				geOrderDetailPageFind(request, paramsMap, result);
			} else if ("order.geOrderDelivered".equals(operateName)) {
				geOrderService.handleGeOrderDelivered(paramsMap, result);
			} else if ("order.geOrderEdit".equals(operateName)) {
				geOrderService.geOrderEdit(paramsMap, result);
			} else if ("order.geOrderReceived".equals(operateName)) {
				geOrderService.handleGeOrderReceived(paramsMap, result);
			} else if ("geOrder.operation.geOrderPageListFind".equals(operateName)) { // 运营名品汇列表查询
				geOrderPageListFindForOp(request, paramsMap, result);
			} else if ("geOrder.operation.geOrderSettlementCount".equals(operateName)) { // 名品汇结算订单统计
				geOrderService.geOrderSettlementCount(paramsMap, result);
			} else if ("geOrder.operation.geOrderSettlement".equals(operateName)) { // 名品汇订单结算
				geOrderService.handleGeOrderSettlement(paramsMap, result);
			} else if ("order.orderLogisticsFind".equals(operateName)) {
				orderService.orderLogisticsFind(paramsMap, result);
			} else if ("recharger.app.mobileAttribution".equals(operateName)) {
				mobileRechargeService.mobileAttribution(paramsMap, result);
			} else if ("csOrder.app.phoneBillOrderCreateNotity".equals(operateName)) {
				csOrderService.phoneBillOrderCreateNotity(paramsMap, result);
			} else if ("csOrder.app.phoneBillOrderPageListFind".equals(operateName)) {
				phoneBillOrderPageListFind(request, paramsMap, result);
			} else if ("csOrder.operation.csOrderPageListFind".equals(operateName)) {
				csOrderPageListForOpFind(request, paramsMap, result);
			} else if ("csOrder.operation.csOrderSettlementCount".equals(operateName)) { // 增值订单结算统计
				csOrderService.csOrderSettlementCount(paramsMap, result);
			} else if ("csOrder.operation.handleCsOrderSettlement".equals(operateName)) { // 增值订单结算
				csOrderService.handleCsOrderSettlement(paramsMap, result);
			} else if ("peOrder.peOrderPayCreateNotify".equals(operateName)) {
				//积分**********************************************
				peOrderService.handlePeOrderPayCreateNotify(paramsMap, result);
			} else if ("peOrder.peOrderDetailPageFind".equals(operateName)) {
				//积分**********************************************
				peOrderDetailPageFind(request, paramsMap, result);
			} else if ("peOrder.peOrderDelivered".equals(operateName)) {
				peOrderService.handlePeOrderDelivered(paramsMap, result);
			} else if ("peOrder.geOrderEdit".equals(operateName)) {
				peOrderService.peOrderEdit(paramsMap, result);
			} else if ("peOrder.peOrderReceived".equals(operateName)) {
				peOrderService.handlePeOrderReceived(paramsMap, result);
			} else if ("peOrder.operation.peOrderPageListFind".equals(operateName)) { 
				peOrderPageListFindForOp(request, paramsMap, result);
			} else if ("peOrder.operation.peOrderSettlementCount".equals(operateName)) { 
				peOrderService.peOrderSettlementCount(paramsMap, result);
			} else if ("peOrder.operation.peOrderSettlement".equals(operateName)) { 
				peOrderService.handlePeOrderSettlement(paramsMap, result);
			} else {
				throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR,
						RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR) + ";service:" + operateName);
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
	 * 名品汇商品列表
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void peOrderDetailPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 引入分页查询
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			peOrderService.peOrderDetailFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/***
	 * 运营名品汇列表查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date 2016年12月28日
	 */
	public void peOrderPageListFindForOp(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 引入分页查询
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			peOrderService.peOrderListFindForOp(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	

	/**
	 * 运营系统我要批订单分页列表查询（新）
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psOrderListPageFindForOp(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			psOrderService.selectPsOrderForOp(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 我要批订单分页查询(运营系统)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void wypOrderListForOpPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			psOrderService.wypOrderListForOpFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 我要批订单分页查询(运营系统)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void wypOrderListForStoresPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			psOrderService.wypOrderListForStoresFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 我要批订单列表查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psOrderListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			psOrderService.psOrderListFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 我要批订单列表查询(运营老版本，后面删除)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psOrderListForOpPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			psOrderService.psOrderListForOpFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 团购预售子订单列表查询(本地生活)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupSubOrderForConsumerListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			psOrderService.psGroupSubOrderForConsumerListFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 团购预售主订单列表查询(店东端)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupOrderForStoresListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			psOrderService.psGroupOrderForStoresListFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 团购预售子订单列表查询(店东端)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupSubOrderForStoresListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			psOrderService.psGroupSubOrderForStoresListFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 团购预售子订单列表查询(店东端)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void psGroupSubOrderForOpListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			psOrderService.psGroupSubOrderForOpListFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询商品列表(消费者端)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void pcOrderDetailPageForConsumerFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			pcOrderService.pcOrderDetailForConsumerFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询商品列表(消费者端)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void zjsOrderForConsumerPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			orderService.zjsOrderListForConsumerFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询商品列表
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void pcOrderDetailPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 引入分页查询
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}

			String stores_id = StringUtil.formatStr(paramsMap.get("stores_id"));
			if (!StringUtils.isEmpty(stores_id)) {
				List<String> list = StringUtil.str2List(stores_id, ",");
				if (list.size() > 1) {
					paramsMap.remove("stores_id");
					paramsMap.put("stores_id_in", list);
				}
			}
			pcOrderService.pcOrderDetailFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
			// 统计已支付的订单数
			ResultData resultCount = new ResultData();
			paramsMap.remove("pageParam");
			pcOrderService.pcOrderPayedCount(paramsMap, resultCount);
			Map<String, Object> countMap = (Map<String, Object>) resultCount.getResultData();
			resultData.put("payed_num", countMap.get("count_num"));
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/***
	 * 运营掌上便利店订单查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date 2016年12月30日
	 */
	public void pcOrderListPageFindForOp(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 引入分页查询
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			pcOrderService.pcOrderListFindForOp(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询商品列表
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void pcOrderPayedNum(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			pcOrderService.pcOrderPayedCount(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 领了么订单导入
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void freeGetOrderImport(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			fgOrderService.handleFreeGetOrderImport(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 领了么订单分页查询(运营系统)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void fgOrderListForOpPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			fgOrderService.fgOrderListForOpFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 领了么样订单查询(APP调用)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void fgOrderListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			fgOrderService.fgOrderList(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 黑名单分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void fgBlacklistPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			fgOrderService.fgBlacklistFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 超级返订单结算列表分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void orderSettlementListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			orderService.orderSettlementList(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 名品汇商品列表
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void geOrderDetailPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 引入分页查询
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			geOrderService.geOrderDetailFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/***
	 * 运营名品汇列表查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date 2016年12月28日
	 */
	public void geOrderPageListFindForOp(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 引入分页查询
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			geOrderService.geOrderListFindForOp(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 话费充值订单列表(运营)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void phoneBillOrderPageListFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 引入分页查询
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			csOrderService.phoneBillOrderListForAppFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 增值订单订单列表(运营)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void csOrderPageListForOpFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			// 引入分页查询
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			csOrderService.csOrderListForOpFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			resultData.put("page", pageParam);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	// =========================================================任务发布====================================================================================================

	/***
	 * 运营任务列表查询
	 * 
	 * @author 丁硕
	 * @date 2017年3月8日
	 */
	private void odTaskForOpListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		PageParam pageParam = getPageParam(request);
		if (null != pageParam) {
			paramsMap.put("pageParam", pageParam);
		}
		odTaskService.odTaskListForOpFind(paramsMap, result);
		Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
		resultData.put("page", pageParam);
	}

	/***
	 * 运营子任务分页列表查询
	 * 
	 * @author 丁硕
	 * @date 2017年3月9日
	 */
	private void odTaskProcessingForOpListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		PageParam pageParam = getPageParam(request);
		if (null != pageParam) {
			paramsMap.put("pageParam", pageParam);
		}
		odTaskService.odTaskProcessingListForOpFind(paramsMap, result);
		Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
		resultData.put("page", pageParam);
	}

	/***
	 * 店东新任务列表查询
	 * 
	 * @author 丁硕
	 * @date 2017年3月8日
	 */
	private void storesOdTaskNewListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		PageParam pageParam = getPageParam(request);
		if (null != pageParam) {
			paramsMap.put("pageParam", pageParam);
		}
		odTaskService.odTaskNewListForMemberFind(paramsMap, result);
		Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
		resultData.put("page", pageParam);
	}

	/**
	 * 店东任务列表查询
	 * 
	 * @author 丁硕
	 * @date 2017年3月8日
	 */
	private void storesOdTaskProcessingListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		PageParam pageParam = getPageParam(request);
		if (null != pageParam) {
			paramsMap.put("pageParam", pageParam);
		}
		odTaskService.odTaskProcessingListForMemberFind(paramsMap, result);
		Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
		resultData.put("page", pageParam);
	}

	/**
	 * 店东任务列表查询
	 * 
	 * @author 丁硕
	 * @date 2017年3月8日
	 */
	private void odTaskProcessingLogListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		PageParam pageParam = getPageParam(request);
		if (null != pageParam) {
			paramsMap.put("pageParam", pageParam);
		}
		odTaskService.odTaskProcessingLogListFind(paramsMap, result);
		Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
		resultData.put("page", pageParam);
	}
}
