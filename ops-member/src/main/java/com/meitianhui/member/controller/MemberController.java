package com.meitianhui.member.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.meitianhui.member.constant.Constant;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.service.AssistantService;
import com.meitianhui.member.service.CompanyService;
import com.meitianhui.member.service.ConsumerService;
import com.meitianhui.member.service.MemberExternalAccountService;
import com.meitianhui.member.service.MemberService;
import com.meitianhui.member.service.MemberTaskService;
import com.meitianhui.member.service.SaleAssistantService;
import com.meitianhui.member.service.SalesmanService;
import com.meitianhui.member.service.StoresService;
import com.meitianhui.member.service.SupplierService;

/**
 * 会员管理
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private AssistantService assistantService;
	@Autowired
	private SaleAssistantService saleAssistantService;
	@Autowired
	private StoresService storesService;
	@Autowired
	private ConsumerService consumerService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private MemberTaskService memberTaskService;
	@Autowired
	private MemberExternalAccountService memberExternalAccountService;
	@Autowired
	private SalesmanService salesmanService;

    public static ExecutorService threadExecutor = Executors.newFixedThreadPool(20);

	@Override  
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		String type = operateName.split("\\.")[0];
		if (type.equals("assistant")) {
			assistantServer(request, response, paramsMap, result); //2017-8-31 废除
		} else if (type.equals("salesman")) { // 业务员 服务接口
			salesmanServer(request, response, paramsMap, result);
		} else if (type.equals("saleAssistant")) {
			saleAssistantServer(request, response, paramsMap, result);
		} else if (type.equals("consumer")) {
			consumerServer(request, response, paramsMap, result);
		} else if (type.equals("stores")) {
			storesServer(request, response, paramsMap, result);
		} else if (type.equals("memberExternalAccount")) {
			memberExternalAccountService(request, response, paramsMap, result);
		} else {
			appServer(request, response, paramsMap, result);
		}
	}

	/**
	 * 业务员服务
	 * 
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void salesmanServer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("salesman.operate.salesmanCreate".equals(operateName)) {
			//业务员添加
			salesmanService.salesmanCreate(paramsMap, result);
		} else if ("salesman.operate.salesmanLogListPageFind".equals(operateName)){
			//业务员日志列表查询（运维系统）
			salesmanLogListPageFind(request, paramsMap, result);
		} else if ("salesman.operate.salesmanListPageFind".equals(operateName)) {
			//业务员列表查询
			salesmanListForOpPageFind(request, paramsMap, result);
		} else if ("salesman.operate.salesmanEdit".equals(operateName)) {
			//业务员修改（运维系统）
			salesmanService.salesmanEdit(paramsMap, result);
		} else if ("salesman.salesassistant.salesmanEdit".equals(operateName)) {
			//业务员修改（搞掂APP）
			salesmanService.salesmanForSalesassistantEdit(paramsMap, result);
		} else if ("salesman.operate.salesmanRoleDelete".equals(operateName)) {
			//业务员角色删除
			salesmanService.handleSalesmanRoleDelete(paramsMap, result);
		} else if ("salesman.operate.salesmanDetailFind".equals(operateName)) {
			//业务员详情查询（运维系统）
			salesmanService.salesmanDetailFind(paramsMap, result);
		} else if ("salesman.salesassistant.salesmanDetailFind".equals(operateName)) {
			//业务员详情查询（搞掂APP）
			salesmanService.salesmanDetailForSalesassistantFind(paramsMap, result);
		} else if ("salesman.operate.salesmanLoginValidate".equals(operateName)){
			//业务员登录校验
			salesmanService.salesmanLoginValidate(paramsMap, result);
		} else if ("salesman.operate.salesmanJurisdictionForOperate".equals(operateName)){
			//是否给业务员权限运营
			salesmanService.salesmanJurisdictionForOperate(paramsMap, result);
		} else if ("salesman.salesassistant.authApply".equals(operateName)){
			//业务员认证申请添加
			salesmanService.handleAuthApply(paramsMap, result);
		} else if ("salesman.operate.authApplyListPageFind".equals(operateName)){
			//业务员认证申请列表查询
			authApplyListPageFind(request, paramsMap, result);
		} else if ("salesman.operate.authApplyLogListPageFind".equals(operateName)){
			//业务员认证申请日志列表查询
			authApplyLogListPageFind(request, paramsMap, result);
		} else if ("salesman.operate.authApplyDetailFind".equals(operateName)){
			//业务员认证申请详情查询（运维系统）
			salesmanService.authApplyDetailFind(paramsMap, result);
		} else if ("salesman.salesassistant.authApplyDetailFind".equals(operateName)){
			//业务员认证申请详情查询（搞掂APP）
			salesmanService.authApplyDetailForSalesassistantFind(paramsMap, result);
		} else if ("salesman.operate.authPass".equals(operateName)){
			//业务员认证申请通过
			salesmanService.handleAuthPass(paramsMap, result);
		} else if ("salesman.operate.authReject".equals(operateName)){
			//业务员认证申请驳回
			salesmanService.handleAuthReject(paramsMap, result);
		} else if ("salesman.salesassistant.driverApply".equals(operateName)){
			//业务员司机申请添加
			salesmanService.driverApply(paramsMap, result);
		} else if ("salesman.salesassistant.driverAgainApply".equals(operateName)){
			//业务员司机再次申请修改
			salesmanService.handleDriverAgainApply(paramsMap, result);
		} else if ("salesman.operate.driverApplyListPageFind".equals(operateName)){
			//业务员司机申请列表查询
			driverApplyListPageFind(request, paramsMap, result);
		} else if ("salesman.operate.driverApplyLogListPageFind".equals(operateName)){
			//业务员司机申请日志列表查询
			driverApplyLogListPageFind(request, paramsMap, result);
		} else if ("salesman.operate.driverApplyDetailFind".equals(operateName)){
			//业务员司机申请详情查询（运维系统）
			salesmanService.driverApplyDetailFind(paramsMap, result);
		} else if ("salesman.salesassistant.driverApplyDetailFind".equals(operateName)){
			//业务员司机申请详情查询（搞掂APP）
			salesmanService.driverApplyDetailForSalesassistantFind(paramsMap, result);
		}else if ("salesman.operate.driverPass".equals(operateName)){
			//业务员司机申请通过
			salesmanService.handleDriverPass(paramsMap, result);
		} else if ("salesman.operate.driverReject".equals(operateName)){
			//业务员司机申请驳回
			salesmanService.handleDriverReject(paramsMap, result);
		} else if ("salesman.salesassistant.specialistApply".equals(operateName)){
			//业务员地服申请添加
			salesmanService.specialistApply(paramsMap, result);
		} else if ("salesman.salesassistant.specialistAgainApply".equals(operateName)){
			//业务员地服再次申请修改
			salesmanService.handleSpecialistAgainApply(paramsMap, result);
		} else if ("salesman.operate.specialistApplyListPageFind".equals(operateName)){
			//业务员地服申请列表查询
			specialistApplyListPageFind(request, paramsMap, result);
		} else if ("salesman.operate.specialistApplyLogListPageFind".equals(operateName)){
			//业务员地服申请日志列表查询
			specialistApplyLogListPageFind(request, paramsMap, result);
		} else if ("salesman.operate.specialistApplyDetailFind".equals(operateName)){
			//业务员地服申请详情查询（运维系统）
			salesmanService.specialistApplyDetailFind(paramsMap, result);
		} else if ("salesman.salesassistant.specialistApplyDetailFind".equals(operateName)){
			//业务员地服申请详情查询（搞掂APP）
			salesmanService.specialistApplyDetailForSalesassistantFind(paramsMap, result);
		} else if ("salesman.operate.specialistPass".equals(operateName)){
			//业务员地服申请通过
			salesmanService.handleSpecialistPass(paramsMap, result);
		} else if ("salesman.operate.specialistReject".equals(operateName)){
			//业务员地服申请驳回
			salesmanService.handleSpecialistReject(paramsMap, result);
		} else if ("salesman.salesassistant.messageHeadlines".equals(operateName)){
			//消息头条
			headlinesListPageFind(request, paramsMap, result);
		} else if ("salesman.salesassistant.assistantApplicationApply".equals(operateName)){
			//业务员助教申请添加
			salesmanService.handleAssistantApplicationApply(paramsMap, result);
		} else if ("salesman.operate.assistantApplicationApplyListPageFind".equals(operateName)){
			//业务员助教申请列表查询
			assistantApplicationApplyListPageFind(request, paramsMap, result);
		} else if ("salesman.operate.assistantApplicationApplyLogListPageFind".equals(operateName)){
			//业务员助教申请日志列表查询
			assistantApplicationApplyLogListPageFind(request, paramsMap, result);
		} else if ("salesman.operate.assistantApplicationApplyDetailFind".equals(operateName)){
			//业务员助教申请详情查询
			salesmanService.assistantApplicationApplyDetailFind(paramsMap, result);
		} else if ("salesman.operate.assistantApplicationPass".equals(operateName)){
			//业务员助教申请通过
			salesmanService.handleAssistantApplicationPass(paramsMap, result);
		} else if ("salesman.operate.assistantApplicationReject".equals(operateName)){
			//业务员助教申请驳回
			salesmanService.handleAssistantApplicationReject(paramsMap, result);
		} else if ("salesman.salesassistant.feedback".equals(operateName)){
			//用户反馈
			salesmanService.userFeedbackCreate(paramsMap, result);
		} else if ("salesman.salesassistant.systemInform".equals(operateName)){
			//系统通知
			systemInformListPageFind(request, paramsMap, result);
		} else if ("salesman.operate.systemInformCreate".equals(operateName)){
			//添加系统通知
			salesmanService.systemInformCreate(paramsMap, result);
		} else if ("salesman.salesassistant.salesmanDataFind".equals(operateName)) {
			//业务员数据
			salesmanService.salesmanDataFind(paramsMap, result);
		} else if ("salesman.operate.storesBySpecialistSync".equals(operateName)){
			//门店对应的业务员地服同步更新
			storesService.storeAssistantInfoSync(paramsMap, result);
		} else if ("salesman.operate.storesBySpecialistClear".equals(operateName)){
			//门店对应的业务员地服清空
			storesService.storeAssistantClear(paramsMap, result);
		} else if ("salesman.operate.storesBySpecialistListFind".equals(operateName)){
			//查询门店对应的业务员地服的列表
			assistantServiceStoresListPageFind(request, paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}
	
	/**
	 * 系统通知分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void systemInformListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.systemInformListFind(paramsMap, result);
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
	 * 业务员日志分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void salesmanLogListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.salesmanLogListFind(paramsMap, result);
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
	 * 业务员助教申请日志分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void assistantApplicationApplyLogListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.assistantApplicationApplyLogListFind(paramsMap, result);
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
	 * 业务员地服申请分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void assistantApplicationApplyListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.assistantApplicationApplyListFind(paramsMap, result);
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
	 * 消息头条分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void headlinesListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.headlinesListFind(paramsMap, result);
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
	 * 业务员地服申请日志分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void specialistApplyLogListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.specialistApplyLogListFind(paramsMap, result);
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
	 * 业务员地服申请分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void specialistApplyListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.specialistApplyListFind(paramsMap, result);
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
	 * 业务员司机申请日志分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void driverApplyLogListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.driverApplyLogListFind(paramsMap, result);
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
	 * 业务员认证申请日志分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void authApplyLogListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.authApplyLogListFind(paramsMap, result);
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
	 * 业务员司机申请分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void driverApplyListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.driverApplyListFind(paramsMap, result);
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
	 * 业务员认证申请分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void authApplyListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.authApplyListFind(paramsMap, result);
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
	 * 业务员列表分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void salesmanListForOpPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			salesmanService.salesmanListForOpFind(paramsMap, result);
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
	 * 店东助手服务
	 * 
	 * @Title: assistantServer
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	private void assistantServer(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("assistant.operate.storesAssistantCreate".equals(operateName)) {
			assistantService.storesAssistantCreate(paramsMap, result);
		} else if ("assistant.operate.storesAssistantListPageFind".equals(operateName)) {
			storesAssistantListPageFind(request, paramsMap, result);
		} else if ("assistant.operate.assistantServiceStoresListPageFind".equals(operateName)) {
			assistantServiceStoresListPageFind(request, paramsMap, result);
		} else if ("assistant.operate.storesAssistantDetailFind".equals(operateName)) {
			assistantService.storesAssistantDetailFind(paramsMap, result);
		} else if ("assistant.operate.storesAssistantEdit".equals(operateName)) {
			assistantService.storesAssistantEdit(paramsMap, result);
		} else if ("assistant.operate.storesAssistantDelete".equals(operateName)) {
			assistantService.storesAssistantDelete(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}

	/**
	 * 社区导购
	 * 
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	private void saleAssistantServer(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("saleAssistant.consumer.saleAssistantApply".equals(operateName)) {
			saleAssistantService.handleSaleAssistantApply(paramsMap, result);
		} else if ("saleAssistant.stores.saleAssistantInvite".equals(operateName)) {
			saleAssistantService.handleSaleAssistantInvite(paramsMap, result);
		} else if ("saleAssistant.stores.saleAssistantApprove".equals(operateName)) {
			saleAssistantService.handleSaleAssistantApprove(paramsMap, result);
		} else if ("saleAssistant.stores.saleAssistantReject".equals(operateName)) {
			saleAssistantService.handleSaleAssistantReject(paramsMap, result);
		} else if ("saleAssistant.consumer.saleAssistantForConsumerListPageFind".equals(operateName)) {
			saleAssistantForConsumerListPageFind(request, paramsMap, result);
		} else if ("saleAssistant.stores.saleAssistantAppliedForStoresListPageFind".equals(operateName)) {
			saleAssistantAppliedForStoresListPageFind(request, paramsMap, result);
		} else if ("saleAssistant.stores.saleAssistantApprovedForStoresListPageFind".equals(operateName)) {
			saleAssistantApprovedForStoresListPageFind(request, paramsMap, result);
		} else if ("saleAssistant.member.saleAssistantHistoryPageFind".equals(operateName)) {
			saleAssistantHistoryPageFind(request, paramsMap, result);
		} else if ("saleAssistant.stores.saleAssistantForStoresCancel".equals(operateName)) {
			saleAssistantService.handleSaleAssistantForStoresCancel(paramsMap, result);
		} else if ("saleAssistant.consumer.saleAssistantForConsumerCancel".equals(operateName)) {
			saleAssistantService.handleSaleAssistantForConsumerCancel(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}

	/**
	 * 消费者服务
	 * 
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	private void consumerServer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("consumer.consumer.consumerBaseInfoFind".equals(operateName)) {
			consumerService.consumerBaseInfoFind(paramsMap, result);
		} else if ("consumer.consumer.consumerSign".equals(operateName)) {
			consumerService.consumerSign(paramsMap, result);
		} else if ("consumer.consumer.consumerLevel".equals(operateName)) {    
			consumerService.consumerLevelFind(paramsMap, result);   
		}//被推荐人注册后输入推荐人手机号，绑定推荐关系     
		else if ("consumer.customer.memberRegisterRecommend".equals(operateName)) {
			memberService.handleCustomerRecommendMemberRegister(paramsMap, result);
		}//查询是否有推荐人
			else if ("consumer.customer.memberRegisterRecommendByMemberId".equals(operateName)) {
			memberService.memberRegisterRecommendFindByMemberId(paramsMap, result);
		} else if ("consumer.consumer.userRecommendCreate".equals(operateName)) {
			//添加消费者对应的推荐人
			consumerService.userRecommendCreate(paramsMap, result);
		} else if ("consumer.consumer.userRecommendFind".equals(operateName)) {
			//查询消费者对应的推荐人
			consumerService.userRecommendFind(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}

	/**
	 * 门店服务
	 * 
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	private void storesServer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("stores.stores.storesBaseInfoFind".equals(operateName)) {
			storesService.storesBaseInfoFind(paramsMap, result);
		} else if ("stores.stores.storesBusinessInfoFind".equals(operateName)) { // 门店营业信息查询(掌上超市信息)
			storesService.storesBusinessInfoFind(paramsMap, result);
		} else if ("stores.consumer.storesTypeGroupFind".equals(operateName)) {
			storesTypeGroupFind(paramsMap, result);
		} else if ("stores.consumer.nearbyStoresTypeListPageFind".equals(operateName)) {
			nearbyStoresTypeListForCousumerPageFind(request, paramsMap, result);
		} else if ("stores.salesassistant.nearbyStoresTypeListPageFind".equals(operateName)) {
			//分组查询附近所有的门店(搞掂APP)
			nearbyStoresTypeListForSalesassistantPageFind(request, paramsMap, result);
		} else if ("stores.salesassistant.salesmanStoresNumFind".equals(operateName)) {
			//业务员对应的拓店，助教门店数量查询
			storesService.salesmanStoresNumFind(paramsMap, result);
		} else if ("stores.salesassistant.storesForConsumerAssetListPageFind".equals(operateName)) {
			//门店对应的会员资产列表分页查询
			storesForConsumerAssetListPageFind(request, paramsMap, result);
		} else if ("stores.consumer.nearbyHSPostListPageFind".equals(operateName)) {
			nearbyHSPostListPageFind(request, paramsMap, result);
		} else if ("stores.stores.stageStoresFind".equals(operateName)) {
			storesService.stageStoresFind(paramsMap, result);
		} else if ("stores.app.storesMemberRelCreate".equals(operateName)) {
			storesService.storesMemberRelCreate(paramsMap, result);
		} else if ("stores.stores.stageStoresEdit".equals(operateName)) {
			// TODO APP暂时不开放惠商驿站的信息编辑
			// storesService.stageStoresEdit(paramsMap, result);
		} else if ("stores.operate.stageStoresEdit".equals(operateName)) {
			storesService.stageStoresEdit(paramsMap, result); 
		} else if ("stores.consumer.defaultStore".equals(operateName)) {
			consumerService.defaultStore(paramsMap, result);//消费者查询指定默认门店
		} else if ("stores.consumer.favoriteStore".equals(operateName)) {
			consumerService.favoriteStore(paramsMap, result);//消费者收藏门店
		} else if ("stores.consumer.favoriteStoreEdit".equals(operateName)) {
			consumerService.favoriteStoreEdit(paramsMap, result);//消费收藏门店编辑
		} else if ("stores.consumer.favoriteStoreCancel".equals(operateName)) {
			consumerService.favoriteStoreCancel(paramsMap, result);//消费者取消收藏门店	
		} else if ("stores.consumer.favoriteStoreList".equals(operateName)) {
			consumerService.favoriteStoreList(paramsMap, result);//消费者收藏门店列表
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}

	/**
	 * 门店对应的会员资产列表分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesForConsumerAssetListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			storesService.storesForConsumerAssetListFind(paramsMap, result);
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
	
	
	public void storesTypeGroupFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (String key : Constant.STORES_TYPE_GROUP_MAP.keySet()) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("index", key);
				tempMap.put("content", Constant.STORES_TYPE_GROUP_MAP.get(key));
				list.add(tempMap);
			}
			result.setResultData(list);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 分组查询附近所有的门店(消费者 领有惠APP)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyStoresTypeListForCousumerPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			if (StringUtil.formatStr(paramsMap.get("range")).equals("")) {
				paramsMap.put("range", "30000");
			}
			String stores_type_group = StringUtil.formatStr(paramsMap.get("stores_type_group"));
			if (!stores_type_group.equals("")) {
				paramsMap.remove("stores_type_group");
				if (stores_type_group.equals(Constant.STORES_TYPE_GROUP_01)) {
					// 便利店
					paramsMap.put("business_type_key", "MDLX_01");
				} else if (stores_type_group.equals(Constant.STORES_TYPE_GROUP_02)) {
					// 美食
					paramsMap.put("business_type_key", "MDLX_05");
				} else if (stores_type_group.equals(Constant.STORES_TYPE_GROUP_03)) {
					// 水果鲜花
					paramsMap.put("business_type_key", "MDLX_21,MDLX_25");
				} else if (stores_type_group.equals(Constant.STORES_TYPE_GROUP_04)) {
					// 购物
					paramsMap.put("business_type_key", "MDLX_11,MDLX_26");
				} else if (stores_type_group.equals(Constant.STORES_TYPE_GROUP_05)) {
					// 生活服务
					paramsMap.put("business_type_key", "MDLX_07,MDLX_10,MDLX_13,MDLX_14,MDLX_15,MDLX_30");
				} else if (stores_type_group.equals(Constant.STORES_TYPE_GROUP_06)) {
					// 医药
					paramsMap.put("business_type_key", "MDLX_03,MDLX_27");
				} else if (stores_type_group.equals(Constant.STORES_TYPE_GROUP_07)) {
					// 美业
					paramsMap.put("business_type_key", "MDLX_04,MDLX_19");
				} else if (stores_type_group.equals(Constant.STORES_TYPE_GROUP_08)) {
					// 休闲娱乐
					paramsMap.put("business_type_key", "MDLX_06,MDLX_09,MDLX_16,MDLX_17,MDLX_18,MDLX_23,MDLX_28");
				} else if (stores_type_group.equals(Constant.STORES_TYPE_GROUP_09)) {
					// 住宿
					paramsMap.put("business_type_key", "MDLX_02");
				} else if (stores_type_group.equals(Constant.STORES_TYPE_GROUP_10)) {
					// 更多
					paramsMap.put("business_type_key", "MDLX_22");
				}
			}
			storesService.nearbyAllStoreFind(paramsMap, result);
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
	 * 分组查询附近所有的门店(搞掂APP)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyStoresTypeListForSalesassistantPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			storesService.nearbyAllStoreForSalesassistantFind(paramsMap, result);
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
	 * 查询附近的惠商驿站
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyHSPostListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "longitude", "latitude" });
			// 查询经纬度所在的区域
			memberService.handleAreaCodeFind(paramsMap, result);
			Map<String, Object> resultData = (Map<String, Object>) result.getResultData();
			String areaCode = resultData.get("areaCode") + "";
			paramsMap.put("city", areaCode.substring(0, 4));

			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			paramsMap.put("range", "2000");
			storesService.nearbyHSPostListFind(paramsMap, result);
			resultData = (Map<String, Object>) result.getResultData();
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
	 * 领了么外部账号服务(绑定淘宝等外部系统账号)
	 * 
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	private void memberExternalAccountService(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> paramsMap, ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("memberExternalAccount.app.memberExternalAccountCreate".equals(operateName)) {
			memberExternalAccountService.memberExternalAccountCreate(paramsMap, result);
		} else if ("memberExternalAccount.app.memberExternalAccountListFind".equals(operateName)) { // 门店营业信息查询(掌上超市信息)
			memberExternalAccountService.memberExternalAccountListForAppFind(paramsMap, result);
		} else if ("memberExternalAccount.app.memberExternalAccountEdit".equals(operateName)) { // 门店营业信息查询(掌上超市信息)
			memberExternalAccountEditForApp(paramsMap, result);
		} else if ("memberExternalAccount.operate.memberExternalAccountListPageFind".equals(operateName)) { // 门店营业信息查询(掌上超市信息)
			memberExternalAccountListForOpPageFind(request, paramsMap, result);
		} else if ("memberExternalAccount.operate.memberExternalAccountEdit".equals(operateName)) { // 门店营业信息查询(掌上超市信息)
			memberExternalAccountEditForOperate(paramsMap, result);
		} else if ("memberExternalAccount.operate.memberExternalAccountDelete".equals(operateName)) { // 门店营业信息查询(掌上超市信息)
			memberExternalAccountService.memberExternalAccountDelete(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}

	/**
	 * 编辑绑定账号信息(app)
	 * 
	 * @Title: memberExternalAccountEditForApp
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	public void memberExternalAccountEditForApp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			memberExternalAccountService.memberExternalAccountEdit(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 编辑绑定账号信息(运营)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberExternalAccountEditForOperate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			memberExternalAccountService.memberExternalAccountForOpEdit(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询附近的惠商驿站
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberExternalAccountListForOpPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			memberExternalAccountService.memberExternalAccountListForOpFind(paramsMap, result);
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
	 * app的服务(后期废除)
	 * 
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	private void appServer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("member.companySync".equals(operateName)) {
			companyService.handleMdCompanySync(paramsMap, result);
		} else if ("member.companyFind".equals(operateName)) {
			companyService.companyFind(paramsMap, result);
		} else if ("member.storeAssistantInfoSync".equals(operateName)) {
			storesService.storeAssistantInfoSync(paramsMap, result);//门店助手信息更新（运营平台） 2017-8-31 废除
		} else if ("member.storeAssistantFind".equals(operateName)) {
			storesService.storeAssistantFind(paramsMap, result);//门店助手信息查询（不知道那里用到）2017-8-31 废除
		} else if ("member.storesRecommendSync".equals(operateName)) {
			storesService.handleStoresRecommendSync(paramsMap, result);
		} else if ("member.storesRecommendFind".equals(operateName)) {
			storesService.storesRecommendFind(paramsMap, result);
		} else if ("member.storesRecommendForOpFind".equals(operateName)) {
			storesService.storesRecommendForOpFind(paramsMap, result);
		} else if ("member.storeAssistantClear".equals(operateName)) {
			storesService.storeAssistantClear(paramsMap, result);//门店信息清空（运营平台） 2017-8-31 废除
		} else if ("member.recommendStoresListForGoodsFind".equals(operateName)) {
			storesService.recommendStoresListForGoodsFind(paramsMap, result);
		} else if ("member.storeSyncRegister".equals(operateName)) {
			storesService.handleStoreSyncRegister(paramsMap, result);
		} else if ("member.storeSyncEdit".equals(operateName)) {
			storesService.storeSyncEdit(paramsMap, result);
		} else if ("member.storesStatusEdit".equals(operateName)) {
			storesService.storesStatusEdit(paramsMap, result);
		} else if ("member.storeActivityPaymentEdit".equals(operateName)) {
			storesService.handleStoreActivityPaymentEdit(paramsMap, result);
		} else if ("member.storeActivityPaymentFind".equals(operateName)) {
			storesService.storeActivityPaymentFind(paramsMap, result);
		} else if ("member.storeLoginValidate".equals(operateName)) {
			storesService.storeLoginValidate(paramsMap, result);
		} else if ("member.storeDetailFind".equals(operateName)) {
			storesService.storeDetailFind(paramsMap, result);
		} else if ("member.storeDetailForConsumerFind".equals(operateName)) {
			storesService.storeDetailForConsumerFind(paramsMap, result);
		} else if ("member.storeListForGoodsFind".equals(operateName)) {
			storesService.storeListForGoodsFind(paramsMap, result);
		} else if ("member.storeListPageFind".equals(operateName)) {
			storeListPageFind(request, paramsMap, result);
		} else if ("member.storeIdFind".equals(operateName)) {
			storesService.storeIdFind(paramsMap, result);
		} else if ("member.storeFindByMobile".equals(operateName)) {
			storesService.storeFindByMobile(paramsMap, result);
		} else if ("member.storeEdit".equals(operateName)) {
			storesService.storeEdit(paramsMap, result);
		} else if ("member.storeInfoFind".equals(operateName)) {
			storesService.storeInfoFind(paramsMap, result);
		} else if ("member.storeFind".equals(operateName)) {
			storesService.storeFind(paramsMap, result);
		} else if ("member.storeForOrderFind".equals(operateName)) {
			storesService.storeForOrderFind(paramsMap, result);
		} else if ("member.nearbyStoreFind".equals(operateName)) {
			nearbyStoreFind(request, paramsMap, result);
		} else if ("member.nearbyStoreByServiceTypeFind".equals(operateName)) {
			nearbyStoreByServiceTypeFind(request, paramsMap, result);
		} else if ("member.nearbyAllPropFind".equals(operateName)) {
			nearbyAllPropFind(paramsMap, result);
		} else if ("member.nearbyLDStoreFind".equals(operateName)) {
			nearbyLDStoreFind(paramsMap, result);
		} else if ("member.nearbyPsGroupGoodsListFind".equals(operateName)) {
			nearbyPsGroupGoodsListFind(paramsMap, result);
		} else if ("member.nearbyAllStoreFind".equals(operateName)) {
			nearbyAllStoreFind(request, paramsMap, result);
		} else if ("member.nearbyAllStoreFindForHomePage".equals(operateName)) {
			nearbyAllStoreFindForHomePage(request, paramsMap, result);
		} else if ("member.nearbyAllStoreFindForFreeGet".equals(operateName)) {
			nearbyAllStoreFindForFreeGet(request, paramsMap, result);
		} else if ("member.storeSyncRegisterForShume".equals(operateName)) {
			storesService.handleStoreSyncRegisterForShume(paramsMap, result);
		} else if ("member.shumeStoresFindByMobile".equals(operateName)) {
			storesService.shumeStoresFindByMobile(paramsMap, result);
		} else if ("member.shumeStoresFind".equals(operateName)) {
			storesService.shumeStoresFind(paramsMap, result);
		} else if ("member.nearbyShumeStoresFind".equals(operateName)) {
			storesService.nearbyShumeStoresFind(paramsMap, result);
		} else if ("member.storeSyncRegisterForHYD".equals(operateName)) {
			storesService.handleStoreSyncRegisterForHYD(paramsMap, result);
		} else if ("member.storeSyncRegisterForHYD3".equals(operateName)) {
			storesService.handleStoreSyncRegisterForHYD3(paramsMap, result);
		} else if ("member.consumerInfoUpdate".equals(operateName)) {
			consumerService.consumerEdit(paramsMap, result);
		} else if ("member.consumerLoginValidate".equals(operateName)) {
			consumerService.consumerLoginValidate(paramsMap, result);
		} else if ("member.consumerStatusEdit".equals(operateName)) {
			consumerService.consumerStatusEdit(paramsMap, result);
		} else if ("member.consumerSync".equals(operateName)) {
			consumerService.handleConsumerSync(paramsMap, result);
		} else if ("member.consumerFind".equals(operateName)) {
			consumerService.consumerFind(paramsMap, result);
		} else if ("member.consumerAddressCreate".equals(operateName)) {
			consumerAddressCreate(paramsMap, result);
		} else if ("member.consumerAddressFind".equals(operateName)) {
			consumerService.consumerAddressFind(paramsMap, result);
		} else if ("member.consumerAddressEdit".equals(operateName)) {
			consumerService.consumerAddressEdit(paramsMap, result);
		} else if ("member.consumerAddressRemove".equals(operateName)) {
			consumerService.handleConsumerAddressRemove(paramsMap, result);
		} else if ("member.consumerListPageFind".equals(operateName)) {
			consumerListPageFind(request, paramsMap, result);
		} else if ("member.supplierSyncRegister".equals(operateName)) {
			supplierService.handleSupplierSyncRegister(paramsMap, result);
		} else if ("member.supplierSyncEdit".equals(operateName)) {
			supplierService.handleSupplierSyncEdit(paramsMap, result);
		} else if ("member.supplierFind".equals(operateName)) {
			supplierService.supplierFind(paramsMap, result);
		} else if ("member.supplierListPromotionFind".equals(operateName)) {
			supplierService.supplierListPromotionFind(paramsMap, result);
		} else if ("member.supplierSyncRegisterForHYD".equals(operateName)) {
			supplierService.handleSupplierSyncRegisterForHYD(paramsMap, result);
		} else if ("member.supplierSyncRegisterForHYD3".equals(operateName)) {
			supplierService.handleSupplierSyncRegisterForHYD3(paramsMap, result);
		} else if ("member.supplierLoginValidate".equals(operateName)) {
			supplierService.supplierLoginValidate(paramsMap, result);
		} else if ("member.supplierStatusEdit".equals(operateName)) {
			supplierService.supplierStatusEdit(paramsMap, result);
		}
		/** 未拆分的接口 **/
		else if ("member.areaExport".equals(operateName)) {
			memberService.areaExport(paramsMap, result);
		} else if ("member.memberRegisterRecommend".equals(operateName)) {
			memberService.handleMemberRegisterRecommend(paramsMap, result);
		} else if ("member.memberRegisterRecommendPageFind".equals(operateName)) {
			memberRegisterRecommendPageFind(request, paramsMap, result);
		} else if ("member.memberRegisterRecommendCount".equals(operateName)) {
			memberService.memberRegisterRecommendCount(paramsMap, result);
		} else if ("member.recommendmemberAsssetListPageFind".equals(operateName)) {
			recommendmemberAsssetListPageFind(request, paramsMap, result);
		} else if ("member.memberIdFind".equals(operateName)) {
			memberService.memberIdFind(paramsMap, result);
		} else if ("member.userIdFind".equals(operateName)) {
			memberService.userIdFind(paramsMap, result);
		} else if ("member.userInfoFind".equals(operateName)) {
			memberService.userInfoFind(paramsMap, result);
		} else if ("member.memberTypeFind".equals(operateName)) {
			memberService.memberTypeFind(paramsMap, result);
		} else if ("member.memberInfoFindByMobile".equals(operateName)) {
			memberService.memberInfoFindByMobile(paramsMap, result);
		} else if ("member.memberInfoFindByMemberId".equals(operateName)) {
			memberService.memberInfoFindByMemberId(paramsMap, result);
		} else if ("member.appMsgNotify".equals(operateName)) {
			memberService.appMsgNotify(paramsMap, result);
		} else if ("member.appMsgNotifyFind".equals(operateName)) {
			memberService.appMsgNotifyFind(paramsMap, result);
		} else if ("member.appMsgNotifyQuery".equals(operateName)) {
			// TODO 店东2.9.3 本地2.11.3版本后废除
			memberService.appMsgNotifyFind(paramsMap, result);
		} else if ("member.storeFeedback".equals(operateName)) {
			memberService.handleStoreFeedback(paramsMap, result);
		} else if ("member.memberUserFind".equals(operateName)) {
			memberService.memberUserFind(paramsMap, result);
		} else if ("member.memberUserRemove".equals(operateName)) {
			memberService.handleMemberUserRemove(paramsMap, result);
		} else if ("member.memberUserCreate".equals(operateName)) {
			memberService.handleMemberUserCreate(paramsMap, result);
		} else if ("member.storesRelConsumerList".equals(operateName)) {
			memberService.storesRelConsumerList(paramsMap, result);
		} else if ("member.storesRelConsumerAssetList".equals(operateName)) {
			memberService.storesRelConsumerAssetList(paramsMap, result);
		} else if ("member.receiveServiceFee".equals(operateName)) {
			memberTaskService.handleProServiceFree(paramsMap, result);
		} else if ("member.storesServiceFeeListPageFind".equals(operateName)) {
			storesServiceFeeListPageFind(request, paramsMap, result);
		} else if ("member.assistantEvaluationCreate".equals(operateName)) {
			memberService.assistantEvaluationCreate(paramsMap, result);
		} else if ("member.assistantEvaluationCountFind".equals(operateName)) {
			memberService.assistantEvaluationCountFind(paramsMap, result);
		} else if ("member.areaCodeFind".equals(operateName)) {
			areaCodeFind(paramsMap, result);
		} else if ("member.stageStoresEdit".equals(operateName)) {
			storesService.stageStoresEdit(paramsMap, result);
		} else if ("member.defaultStore".equals(operateName)) {
			consumerService.defaultStore(paramsMap, result);//消费者查询指定默认门店
		} else if ("member.favoriteStore".equals(operateName)) {
			consumerService.favoriteStore(paramsMap, result);//消费者收藏门店
		} else if ("member.favoriteStoreEdit".equals(operateName)) {
			consumerService.favoriteStoreEdit(paramsMap, result);//消费收藏门店编辑
		} else if ("member.favoriteStoreCancel".equals(operateName)) {
			consumerService.favoriteStoreCancel(paramsMap, result);//消费者取消收藏门店
		} else if ("member.favoriteStoreList".equals(operateName)) {
			consumerService.favoriteStoreList(paramsMap, result);//消费者收藏门店列表
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}

	/**
	 * 查询附近的加盟商
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyStoreFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			paramsMap.put("range", "20000");
			storesService.nearbyStoreFind(paramsMap, result);
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
	 * 查询附近的加盟商
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyStoreByServiceTypeFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			paramsMap.put("range", "20000");
			storesService.nearbyStoreFind(paramsMap, result);
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
	 * 查询门店附近的优惠券
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyAllPropFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = new PageParam();
			paramsMap.put("pageParam", pageParam);
			paramsMap.put("range", 20000);
			storesService.nearbyAllPropFind(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询附近有一元购商品的门店
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyLDStoreFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			paramsMap.put("range", "20000");
			storesService.nearbyLDStoreFind(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询附近有一元购商品的门店
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyPsGroupGoodsListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			paramsMap.put("range", "20000");
			storesService.nearbyPsGroupGoodsListFind(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询商店列表(消费者端)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storeListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			} else {
				pageParam = new PageParam();
				pageParam.setPage_size(20);
				paramsMap.put("pageParam", pageParam);
			}
			storesService.storeListFind(paramsMap, result);
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
	 * 查询附近的门店
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyAllStoreFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			if (StringUtil.formatStr(paramsMap.get("range")).equals("")) {
				paramsMap.put("range", "20000");
			}
			storesService.nearbyAllStoreFind(paramsMap, result);
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
	 * 查询附近的所有门店(首页)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyAllStoreFindForHomePage(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			} else {
				pageParam = new PageParam();
			}
			paramsMap.put("pageParam", pageParam);
			paramsMap.put("range", "20000");
			storesService.nearbyAllStoreFind(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询附近的所有门店(领了么)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void nearbyAllStoreFindForFreeGet(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			paramsMap.put("range", "20000");
			storesService.nearbyAllStoreFind(paramsMap, result);
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
	 * 会员注册退款列表查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void memberRegisterRecommendPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			memberService.memberRegisterRecommendFind(paramsMap, result);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("list", result.getResultData());
			resultMap.put("page", pageParam);
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 店东推荐注册的会员的资产列表
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void recommendmemberAsssetListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			memberService.recommendmemberAsssetList(paramsMap, result);
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
	 * 查询附近的本地兑商家
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void consumerAddressCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			consumerService.consumerAddressCreate(paramsMap, result);
			Map<String, Object> resultMap = (Map<String, Object>) result.getResultData();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("address_id", resultMap.get("address_id"));
			tempMap.put("consumer_id", paramsMap.get("consumer_id"));
			tempMap.put("is_major_addr", "Y");
			consumerService.consumerAddressEdit(tempMap, new ResultData());
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 会员费列表分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void consumerListPageFind(HttpServletRequest request, Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			consumerService.consumerListFind(paramsMap, result);
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
	 * 会员费列表分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesServiceFeeListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			memberService.storesServiceFeeListFind(paramsMap, result);
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
	 * 店东助手服务门店列表分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void assistantServiceStoresListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			assistantService.assistantServiceStoresListFind(paramsMap, result);
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
	 * 消费者查询社区导购信息
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void saleAssistantForConsumerListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			saleAssistantService.saleAssistantForConsumerFind(paramsMap, result);
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
	 * 店东查询社区导购信息(已申请)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void saleAssistantAppliedForStoresListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			saleAssistantService.saleAssistantAppliedForStoresFind(paramsMap, result);
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
	 * 店东查询社区导购信息(已批准)
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void saleAssistantApprovedForStoresListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			saleAssistantService.saleAssistantApprovedForStoresFind(paramsMap, result);
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
	 * 社区导购操作日志查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void saleAssistantHistoryPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			saleAssistantService.saleAssistantHistoryFind(paramsMap, result);
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
	 * 获取区域编码
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void areaCodeFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			memberService.handleAreaCodeFind(paramsMap, result);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 店东助手列表分页查询
	 * 
	 * @param request
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public void storesAssistantListPageFind(HttpServletRequest request, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		try {
			PageParam pageParam = getPageParam(request);
			if (null != pageParam) {
				paramsMap.put("pageParam", pageParam);
			}
			assistantService.storesAssistantListFind(paramsMap, result);
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

}
