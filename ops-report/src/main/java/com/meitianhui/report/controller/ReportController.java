package com.meitianhui.report.controller;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.criteria.Order;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meitianhui.base.controller.BaseController;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.report.constant.RspCode;
import com.meitianhui.report.service.OrderService;
import com.meitianhui.report.service.StoresService;


/**
 * 
 * @author 丁忍
 *
 */
@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/report")
public class ReportController extends BaseController {

	@Autowired
	private StoresService storesService;
	
	@Autowired
	private OrderService orderService;

	public static ExecutorService threadExecutor = Executors.newFixedThreadPool(20);

	@Override
	public void operate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		String type = operateName.split("\\.")[0];
		if (type.equals("stores")) {
			storesServer(request, response, paramsMap, result);
		} else if (type.equals("order")) { 
			orderServer(request, response, paramsMap, result);
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
		if ("stores.salesassistant.storesTransactionTypeFind".equals(operateName)) {
			//门店交易类型查询
			storesService.storesTransactionTypeFind(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}
	/**
	 * 订单服务
	 * 
	 * @param request
	 * @param response
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 */
	private void orderServer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramsMap,
			ResultData result) throws BusinessException, SystemException, Exception {
		String operateName = request.getParameter("service");
		if ("order.salesassistant.orderCountAndMoney".equals(operateName)) {
			orderService.statisticsOrderCountAndMoneyFind(paramsMap, result);
		} else if ("order.salesassistant.storeCountAndConsumerCount".equals(operateName)) {
			orderService.statisticsStoreCountAndConsumerCountFind(paramsMap, result);
		} else {
			throw new BusinessException(RspCode.SYSTEM_SERVICE_ERROR, RspCode.MSG.get(RspCode.SYSTEM_SERVICE_ERROR));
		}
	}
}
