package com.meitianhui.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.order.constant.Constant;
import com.meitianhui.order.constant.OrderIDUtil;
import com.meitianhui.order.constant.RspCode;
import com.meitianhui.order.dao.OdTaskDao;
import com.meitianhui.order.entity.OdTask;
import com.meitianhui.order.entity.OdTaskProcessing;
import com.meitianhui.order.entity.OdTaskProcessingLog;
import com.meitianhui.order.service.OdTaskService;

/**
 * 伙拼团活动
 * 
 * @ClassName: TsActivityServiceImpl
 * @author tiny
 * @date 2017年2月27日 下午7:06:06
 *
 */

/**
 * 任务管理
 * 
 * @ClassName: OdTaskServiceImpl
 * @author tiny
 * @date 2017年3月8日 下午2:56:30
 *
 */
@Service
public class OdTaskServiceImpl implements OdTaskService {

	private static final Logger logger = Logger.getLogger(OdTaskServiceImpl.class);
	@Autowired
	private DocUtil docUtil;
	@Autowired
	private OdTaskDao odTaskDao;

	/***
	 * 运营创建任务
	 */
	@Override
	public void odTaskCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "task_name", "desc1", "json_data", "payment_way_key",
				"amount", "scope", "expried_date", "suitable_app", "status" });
		OdTask odTask = new OdTask();
		BeanConvertUtil.mapToBean(odTask, paramsMap);
		odTask.setTask_id(IDUtil.getUUID());
		odTask.setModified_date(new Date());
		odTask.setCreated_date(odTask.getModified_date());
		odTaskDao.insertOdTask(odTask);
	}

	/***
	 * 运营创建任务
	 */
	@Override
	public void odTaskUpdate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "task_id", "status" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("status", Constant.STATUS_NORMAL);
		tempMap.put("task_id", paramsMap.get("task_id"));
		List<OdTask> taskList = odTaskDao.selectOdTaskDetailList(tempMap);
		if (taskList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "任务不存在");
		}
		odTaskDao.updateOdTask(paramsMap);
	}

	/***
	 * 运营任务列表查询
	 */
	@Override
	public void odTaskListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<Map<String, Object>> taskList = odTaskDao.selectOdTaskListForOp(paramsMap);
		for (Map<String, Object> odTaskMap : taskList) {
			odTaskMap.put("json_data", StringUtil.formatStr(odTaskMap.get("json_data")));
			BigDecimal amount = (BigDecimal) odTaskMap.get("amount");
			String payment_way_key = odTaskMap.get("payment_way_key") + "";
			if (payment_way_key.equals("gold")) {
				odTaskMap.put("amount", amount.intValue() + "");
			} else {
				odTaskMap.put("amount", amount + "");
			}
			odTaskMap.put("expried_date",
					DateUtil.date2Str((Date) odTaskMap.get("expried_date"), DateUtil.fmt_yyyyMMdd));
			odTaskMap.put("created_date",
					DateUtil.date2Str((Date) odTaskMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
			odTaskMap.put("remark", StringUtil.formatStr(odTaskMap.get("remark")));
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", taskList);
		result.setResultData(resultMap);
	}

	/***
	 * 运营任务详情查询
	 */
	@Override
	public void odTaskDetailForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "task_id" });
		List<OdTask> odTaskList = odTaskDao.selectOdTaskDetailList(paramsMap);
		if (odTaskList.size() != 1) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "任务不存在");
		}
		OdTask odTask = odTaskList.get(0);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("task_id", odTask.getTask_id());
		tempMap.put("task_name", odTask.getTask_name());
		tempMap.put("desc1", odTask.getDesc1());
		tempMap.put("json_data", StringUtil.formatStr(odTask.getJson_data()));
		tempMap.put("payment_way_key", odTask.getPayment_way_key());
		if (odTask.getPayment_way_key().equals("gold")) {
			tempMap.put("amount", odTask.getAmount().intValue() + "");
		} else {
			tempMap.put("amount", odTask.getAmount() + "");
		}
		tempMap.put("scope", odTask.getScope());
		tempMap.put("path", odTask.getPath());
		tempMap.put("expried_date", DateUtil.date2Str(odTask.getExpried_date(), DateUtil.fmt_yyyyMMdd));
		tempMap.put("suitable_app", odTask.getSuitable_app());
		tempMap.put("status", odTask.getStatus());
		tempMap.put("created_date", DateUtil.date2Str(odTask.getCreated_date(), DateUtil.fmt_yyyyMMdd));
		tempMap.put("remark", StringUtil.formatStr(odTask.getRemark()));
		result.setResultData(tempMap);
	}

	/***
	 * 运营子任务查询
	 */
	@Override
	public void odTaskProcessingListForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		List<OdTaskProcessing> taskProcessingList = odTaskDao.selectOdTaskProcessingListForOp(paramsMap);
		List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		for (OdTaskProcessing odTaskProcessing : taskProcessingList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("processing_id", odTaskProcessing.getProcessing_id());
			tempMap.put("task_id", odTaskProcessing.getTask_id());
			tempMap.put("task_name", odTaskProcessing.getTask_name());
			tempMap.put("desc1", odTaskProcessing.getDesc1());
			tempMap.put("json_data", odTaskProcessing.getJson_data());
			tempMap.put("payment_way_key", odTaskProcessing.getPayment_way_key());
			if (odTaskProcessing.getPayment_way_key().equals("gold")) {
				tempMap.put("amount", odTaskProcessing.getAmount().intValue() + "");
			} else {
				tempMap.put("amount", odTaskProcessing.getAmount() + "");
			}
			tempMap.put("member_type_key", odTaskProcessing.getMember_type_key());
			tempMap.put("member_id", odTaskProcessing.getMember_id() + "");
			tempMap.put("member_info", odTaskProcessing.getMember_info());
			tempMap.put("expried_date", DateUtil.date2Str(odTaskProcessing.getExpried_date(), DateUtil.fmt_yyyyMMdd));
			tempMap.put("submitted_date",
					DateUtil.date2Str(odTaskProcessing.getSubmitted_date(), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("audited_date",
					DateUtil.date2Str(odTaskProcessing.getAudited_date(), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("audited_by", StringUtil.formatStr(odTaskProcessing.getAudited_by()));
			tempMap.put("audited_result", StringUtil.formatStr(odTaskProcessing.getAudited_result()));
			tempMap.put("status", odTaskProcessing.getStatus());
			tempMap.put("remark", StringUtil.formatStr(odTaskProcessing.getRemark()));
			tempMap.put("created_date",
					DateUtil.date2Str(odTaskProcessing.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
			tempMap.put("modified_date",
					DateUtil.date2Str(odTaskProcessing.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
			tempList.add(tempMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", tempList);
		result.setResultData(resultMap);
	}

	/***
	 * 运营子任务详情查询
	 */
	@Override
	public void odTaskProcessingDetailForOpFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "processing_id" });
		List<OdTaskProcessing> odTaskProcessingList = odTaskDao.selectOdTaskProcessingDetailList(paramsMap);
		if (odTaskProcessingList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "任务不存在");
		}
		OdTaskProcessing odTaskProcessing = odTaskProcessingList.get(0);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("processing_id", odTaskProcessing.getProcessing_id());
		tempMap.put("task_id", odTaskProcessing.getTask_id());
		tempMap.put("task_name", odTaskProcessing.getTask_name());
		tempMap.put("desc1", odTaskProcessing.getDesc1());
		tempMap.put("json_data", odTaskProcessing.getJson_data());
		tempMap.put("payment_way_key", odTaskProcessing.getPayment_way_key());
		if (odTaskProcessing.getPayment_way_key().equals("gold")) {
			tempMap.put("amount", odTaskProcessing.getAmount().intValue() + "");
		} else {
			tempMap.put("amount", odTaskProcessing.getAmount() + "");
		}
		tempMap.put("member_type_key", odTaskProcessing.getMember_type_key());
		tempMap.put("member_id", odTaskProcessing.getMember_id());
		tempMap.put("member_info", odTaskProcessing.getMember_info());
		tempMap.put("submitted_date",
				DateUtil.date2Str(odTaskProcessing.getSubmitted_date(), DateUtil.fmt_yyyyMMddHHmmss));
		tempMap.put("audited_date", DateUtil.date2Str(odTaskProcessing.getAudited_date(), DateUtil.fmt_yyyyMMddHHmmss));
		tempMap.put("audited_by", StringUtil.formatStr(odTaskProcessing.getAudited_by()));
		tempMap.put("audited_result", StringUtil.formatStr(odTaskProcessing.getAudited_result()));
		tempMap.put("status", odTaskProcessing.getStatus());
		tempMap.put("remark", StringUtil.formatStr(odTaskProcessing.getRemark()));
		tempMap.put("created_date", DateUtil.date2Str(odTaskProcessing.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
		result.setResultData(tempMap);
	}

	/***
	 * 运营停止任务
	 */
	@Override
	public void handleOdTaskStopForOp(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "task_id" });
		List<OdTask> taskList = odTaskDao.selectOdTaskDetailList(paramsMap);
		if (taskList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "任务不存在");
		}
		OdTask odTask = taskList.get(0);
		if (!"processing".equals(odTask.getStatus())) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "操作失败，请刷新后重试");
		}
		// 中止任务
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("task_id", odTask.getTask_id());
		tempMap.put("status", "finished");
		tempMap.put("modified_date", DateUtil.date2Str(odTask.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = odTaskDao.updateOdTask(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "操作失败，请刷新后重试");
		}
		tempMap.clear();
		tempMap.put("task_id", odTask.getTask_id());
		List<OdTaskProcessing> odTaskProcessingList = odTaskDao.selectOdTaskProcessingList(tempMap);
		for (OdTaskProcessing otp : odTaskProcessingList) {
			if (otp.getStatus().equals(Constant.OD_TASK_PROCESSING_SUBMITTED)) {
				odTaskProcessingLogCreate(otp.getProcessing_id(), "操作", "运营人员取消任务");
				tempMap.clear();
				tempMap.put("processing_id", otp.getProcessing_id());
				tempMap.put("status", Constant.OD_TASK_PROCESSING_ABORTED);
				odTaskDao.updateOdTaskProcessing(tempMap);
				if (updateFlag == 0) {
					throw new BusinessException(RspCode.OD_TASK_ERROR, "操作失败，请刷新后重试");
				}
			}
		}
	}

	/***
	 * 会员新任务列表
	 * 
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author 丁硕
	 * @date 2017年3月8日
	 */
	@Override
	public void odTaskNewListForMemberFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "member_id", "member_type_key", "area_id", "suitable_app" });
		String area_id = paramsMap.get("area_id") + "";
		if (area_id.length() < 6) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "区域编码错误");
		}
		List<String> areaIdList = new ArrayList<String>();
		areaIdList.add(area_id);
		areaIdList.add(area_id.substring(0, area_id.length() - 1) + "0");
		areaIdList.add(area_id.substring(0, area_id.length() - 2) + "00");
		areaIdList.add(area_id.substring(0, area_id.length() - 3) + "000");
		areaIdList.add(area_id.substring(0, area_id.length() - 4) + "0000");
		areaIdList.add("100000");
		paramsMap.put("areaList", areaIdList);
		List<OdTask> taskList = odTaskDao.selectNewOdTaskListForMember(paramsMap);
		List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		for (OdTask odTask : taskList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("task_id", odTask.getTask_id());
			tempMap.put("task_name", odTask.getTask_name());
			tempMap.put("desc1", odTask.getDesc1());
			tempMap.put("status", odTask.getStatus());
			tempMap.put("payment_way_key", odTask.getPayment_way_key());
			if (odTask.getPayment_way_key().equals("gold")) {
				tempMap.put("amount", odTask.getAmount().intValue() + "");
			} else {
				tempMap.put("amount", odTask.getAmount() + "");
			}
			tempMap.put("expried_date", DateUtil.date2Str(odTask.getExpried_date(), "yyyy/MM/dd"));
			tempMap.put("remark", StringUtil.formatStr(odTask.getExpried_date()));
			tempList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", tempList);
		result.setResultData(map);
	}

	/***
	 * 会员任务列表查询
	 */
	@Override
	public void odTaskProcessingListForMemberFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "status" });
		String status = StringUtil.formatStr(paramsMap.get("status"));
		List<String> list = StringUtil.str2List(status, ",");
		if (list.size() > 1) {
			paramsMap.remove("status");
			paramsMap.put("status_in", list);
		}
		List<OdTaskProcessing> taskProcessingList = odTaskDao.selectOdTaskProcessingList(paramsMap);
		List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		for (OdTaskProcessing taskProcessing : taskProcessingList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("processing_id", taskProcessing.getProcessing_id());
			tempMap.put("task_id", taskProcessing.getTask_id());
			tempMap.put("task_name", taskProcessing.getTask_name());
			tempMap.put("desc1", taskProcessing.getDesc1());
			tempMap.put("payment_way_key", taskProcessing.getPayment_way_key());
			if (taskProcessing.getPayment_way_key().equals("gold")) {
				tempMap.put("amount", taskProcessing.getAmount().intValue() + "");
			} else {
				tempMap.put("amount", taskProcessing.getAmount() + "");
			}
			tempMap.put("expried_date", DateUtil.date2Str(taskProcessing.getExpried_date(), "yyyy/MM/dd"));
			tempMap.put("status", taskProcessing.getStatus());
			tempMap.put("remark", StringUtil.formatStr(taskProcessing.getRemark()));
			tempList.add(tempMap);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", tempList);
		result.setResultData(map);
	}

	/***
	 * 会员参与任务
	 */
	@Override
	public void handleOdTaskJoin(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "member_name",
				"member_mobile", "task_id", "suitable_app" });
		List<OdTask> taskList = odTaskDao.selectNewOdTaskListForMember(paramsMap);
		if (taskList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "任务不存在或已结束");
		}
		// 参与会员信息
		Map<String, Object> memberMap = new HashMap<String, Object>();
		memberMap.put("name", paramsMap.get("member_name"));
		memberMap.put("mobile", paramsMap.get("member_mobile"));

		OdTask task = taskList.get(0);
		// 新增参与信息
		OdTaskProcessing odTaskprocessing = new OdTaskProcessing();
		odTaskprocessing.setProcessing_id(OrderIDUtil.getOrderNo());
		odTaskprocessing.setTask_id(task.getTask_id());
		odTaskprocessing.setTask_name(task.getTask_name());
		odTaskprocessing.setJson_data(task.getJson_data());
		odTaskprocessing.setDesc1(task.getDesc1());
		odTaskprocessing.setPayment_way_key(task.getPayment_way_key());
		odTaskprocessing.setAmount(task.getAmount());
		odTaskprocessing.setExpried_date(task.getExpried_date());
		odTaskprocessing.setMember_id(paramsMap.get("member_id") + "");
		odTaskprocessing.setMember_type_key(paramsMap.get("member_type_key") + "");
		odTaskprocessing.setMember_info(FastJsonUtil.toJson(memberMap));
		odTaskprocessing.setStatus(Constant.OD_TASK_PROCESSING_SUBMITTED);
		odTaskprocessing.setCreated_date(new Date());
		odTaskprocessing.setModified_date(odTaskprocessing.getCreated_date());
		odTaskDao.insertOdTaskProcessing(odTaskprocessing);

		// 记录日志
		odTaskProcessingLogCreate(odTaskprocessing.getProcessing_id(), "操作", "领取任务");
		// 返回结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("processing_id", odTaskprocessing.getProcessing_id());
		result.setResultData(resultMap);
	}

	/***
	 * 会员提交任务
	 */
	@Override
	public void handleOdTaskProcessingSubmit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "processing_id", "member_id", "member_type_key", "json_data" });
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("processing_id", paramsMap.get("processing_id"));
		tempMap.put("member_id", paramsMap.get("member_id"));
		tempMap.put("member_type_key", paramsMap.get("member_type_key"));
		tempMap.put("status", Constant.OD_TASK_PROCESSING_SUBMITTED);
		List<OdTaskProcessing> odTaskProcessingList = odTaskDao.selectOdTaskProcessingDetailList(tempMap);
		if (odTaskProcessingList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "操作失败，请刷新后重试");
		}
		OdTaskProcessing odTaskProcessing = odTaskProcessingList.get(0);
		String date = DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMddHHmmss);
		paramsMap.put("status", Constant.OD_TASK_PROCESSING_AUDITED);
		paramsMap.put("submitted_date", date);
		paramsMap.put("modified_date",
				DateUtil.date2Str(odTaskProcessing.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = odTaskDao.updateOdTaskProcessing(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "操作失败，请刷新后重试");
		}
		// 记录日志
		odTaskProcessingLogCreate(odTaskProcessing.getProcessing_id(), "操作", "提交任务");
	}

	/***
	 * 任务审核 -通过
	 */
	@Override
	public void handleOdTaskProcessingPass(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "processing_id", "audited_by" });
		List<OdTaskProcessing> odTaskProcessingList = odTaskDao.selectOdTaskProcessingDetailList(paramsMap);
		if (odTaskProcessingList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "任务不存在");
		}
		OdTaskProcessing odTaskProcessing = odTaskProcessingList.get(0);
		if (!Constant.OD_TASK_PROCESSING_AUDITED.equals(odTaskProcessing.getStatus())) {
			throw new BusinessException(RspCode.PROCESSING, "操作失败，请刷新后重试");
		}
		String date = DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMddHHmmss);
		paramsMap.put("audited_result", Constant.OD_TASK_PASS);
		paramsMap.put("status", Constant.OD_TASK_PROCESSING_SETTLED);
		paramsMap.put("audited_date", date);
		paramsMap.put("modified_date",
				DateUtil.date2Str(odTaskProcessing.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		int updateFlag = odTaskDao.updateOdTaskProcessing(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "操作失败，请刷新后重试");
		}
		// 记录日志
		odTaskProcessingLogCreate(odTaskProcessing.getProcessing_id(), "操作",
				"任务审核通过,审批人:" + paramsMap.get("audited_by"));
	}

	/***
	 * 任务审核 -失败
	 */
	@Override
	public void handleOdTaskProcessingFail(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "processing_id", "audited_by", "remark" });
		List<OdTaskProcessing> odTaskProcessingList = odTaskDao.selectOdTaskProcessingDetailList(paramsMap);
		if (odTaskProcessingList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "任务不存在");
		}
		OdTaskProcessing odTaskProcessing = odTaskProcessingList.get(0);
		if (!Constant.OD_TASK_PROCESSING_AUDITED.equals(odTaskProcessing.getStatus())) {
			throw new BusinessException(RspCode.PROCESSING, "操作失败，请刷新后重试");
		}
		String date = DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMddHHmmss);
		paramsMap.put("status", Constant.OD_TASK_PROCESSING_ABORTED);
		paramsMap.put("audited_date", date);
		paramsMap.put("modified_date",
				DateUtil.date2Str(odTaskProcessing.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		paramsMap.put("audited_result", Constant.OD_TASK_FAIL);
		paramsMap.put("remark", paramsMap.get("remark"));
		int updateFlag = odTaskDao.updateOdTaskProcessing(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "操作失败，请刷新后重试");
		}
		// 记录日志
		odTaskProcessingLogCreate(odTaskProcessing.getProcessing_id(), "操作",
				"审核失败:" + paramsMap.get("remark") + ",审批人:" + paramsMap.get("audited_by"));
	}

	/***
	 * 任务审核 -驳回
	 */
	@Override
	public void handleOdTaskProcessingReject(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "processing_id", "audited_by", "remark" });
		List<OdTaskProcessing> odTaskProcessingList = odTaskDao.selectOdTaskProcessingDetailList(paramsMap);
		if (odTaskProcessingList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "任务不存在");
		}
		OdTaskProcessing odTaskProcessing = odTaskProcessingList.get(0);
		if (!Constant.OD_TASK_PROCESSING_AUDITED.equals(odTaskProcessing.getStatus())) {
			throw new BusinessException(RspCode.PROCESSING, "操作失败，请刷新后重试");
		}
		String date = DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMddHHmmss);
		paramsMap.put("status", Constant.OD_TASK_PROCESSING_SUBMITTED);
		paramsMap.put("audited_result", Constant.OD_TASK_REJECT);
		paramsMap.put("audited_date", date);
		paramsMap.put("modified_date",
				DateUtil.date2Str(odTaskProcessing.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		paramsMap.put("remark", paramsMap.get("remark"));
		int updateFlag = odTaskDao.updateOdTaskProcessing(paramsMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "操作失败，请刷新后重试");
		}
		// 记录日志
		odTaskProcessingLogCreate(odTaskProcessing.getProcessing_id(), "操作",
				"驳回重提交:" + paramsMap.get("remark") + ",审批人:" + paramsMap.get("audited_by"));
	}

	/***
	 * 任务审核 -结算
	 */
	@Override
	public void handleOdTaskProcessingSettle(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "processing_id", "operator" });
		paramsMap.put("status", Constant.OD_TASK_PROCESSING_SETTLED);
		List<OdTaskProcessing> odTaskProcessingList = odTaskDao.selectOdTaskProcessingList(paramsMap);
		if (odTaskProcessingList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "待结算记录不存在");
		}
		OdTaskProcessing otp = odTaskProcessingList.get(0);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("processing_id", otp.getProcessing_id());
		tempMap.put("status", Constant.OD_TASK_PROCESSING_CLOSED);
		tempMap.put("modified_date", DateUtil.date2Str(otp.getModified_date(), DateUtil.fmt_yyyyMMddHHmmss));
		tempMap.put("remark", "操作人：" + paramsMap.get("operator"));
		int updateFlag = odTaskDao.updateOdTaskProcessing(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "操作失败，请刷新后重试");
		}
		// 支付结算金额
		String payment_way_key = otp.getPayment_way_key();
		if (payment_way_key.equals("cash")) {
			payment_way_key = "ZFFS_05";
		} else if (payment_way_key.equals("gold")) {
			payment_way_key = "ZFFS_08";
		} else {
			return;
		}
		// 结算成功，记录日志
		odTaskProcessingLogCreate(otp.getProcessing_id(), "结算",
				"结算完成,结算金额:" + otp.getAmount() + ",操作人:" + paramsMap.get("operator"));
		// 结算支付
		taskSettlePay(otp.getMember_id(), payment_way_key, otp.getAmount(), otp.getProcessing_id(), otp.getTask_name());
	}

	/***
	 * 会员可以领取的任务总数查询
	 * 
	 * @author 丁硕
	 * @date 2017年3月8日
	 */
	@Override
	public void odTaskNewCountForMemberFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "member_id", "member_type_key", "area_id", "suitable_app" });
		String area_id = paramsMap.get("area_id") + "";
		if (area_id.length() < 6) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "区域编码错误");
		}
		List<String> areaIdList = new ArrayList<String>();
		areaIdList.add(area_id);
		areaIdList.add(area_id.substring(0, area_id.length() - 1) + "0");
		areaIdList.add(area_id.substring(0, area_id.length() - 2) + "00");
		areaIdList.add(area_id.substring(0, area_id.length() - 3) + "000");
		areaIdList.add(area_id.substring(0, area_id.length() - 4) + "0000");
		areaIdList.add("100000");
		paramsMap.put("areaList", areaIdList);
		long count = odTaskDao.odTaskNewCountForMemberFind(paramsMap);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("task_count", String.valueOf(count));
		result.setResultData(resultMap);
	}

	/***
	 * APP任务详情查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void odTaskForMemberDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "task_id" });
		List<OdTask> odTaskList = odTaskDao.selectOdTaskDetailList(paramsMap);
		if (odTaskList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "任务不存在");
		}
		OdTask odTask = odTaskList.get(0);
		List<String> doc_ids = new ArrayList<String>();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("task_id", odTask.getTask_id());
		tempMap.put("task_name", odTask.getTask_name());
		tempMap.put("desc1", odTask.getDesc1());
		tempMap.put("json_data", StringUtil.formatStr(odTask.getJson_data()));
		tempMap.put("payment_way_key", odTask.getPayment_way_key());
		if (odTask.getPayment_way_key().equals("gold")) {
			tempMap.put("amount", odTask.getAmount().intValue() + "");
		} else {
			tempMap.put("amount", odTask.getAmount() + "");
		}
		tempMap.put("expried_date", DateUtil.date2Str(odTask.getExpried_date(), "yyyy/MM/dd"));
		tempMap.put("status", odTask.getStatus());
		tempMap.put("remark", StringUtil.formatStr(odTask.getRemark()));
		tempMap.put("created_date", DateUtil.date2Str(odTask.getCreated_date(), DateUtil.fmt_yyyyMMdd));

		String json_data = odTask.getJson_data();
		if (StringUtils.isNotEmpty(json_data)) {
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(json_data);
			for (Map<String, Object> m : tempList) {
				if (StringUtils.isNotEmpty(StringUtil.formatStr(m.get("path_id")))) {
					doc_ids.add(m.get("path_id") + "");
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("detail", tempMap);
		result.setResultData(map);
	}

	/***
	 * 会员子任务详情查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void odTaskProcessingForMemberDetailFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "member_type_key", "processing_id" });
		List<OdTaskProcessing> odTaskProcessingList = odTaskDao.selectOdTaskProcessingDetailList(paramsMap);
		if (odTaskProcessingList.size() == 0) {
			throw new BusinessException(RspCode.OD_TASK_ERROR, "任务不存在");
		}
		OdTaskProcessing odTaskProcessing = odTaskProcessingList.get(0);
		List<String> doc_ids = new ArrayList<String>();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("processing_id", odTaskProcessing.getProcessing_id());
		tempMap.put("task_id", odTaskProcessing.getTask_id());
		tempMap.put("task_name", odTaskProcessing.getTask_name());
		tempMap.put("desc1", odTaskProcessing.getDesc1());
		tempMap.put("json_data", odTaskProcessing.getJson_data());
		tempMap.put("payment_way_key", odTaskProcessing.getPayment_way_key());
		if (odTaskProcessing.getPayment_way_key().equals("gold")) {
			tempMap.put("amount", odTaskProcessing.getAmount().intValue() + "");
		} else {
			tempMap.put("amount", odTaskProcessing.getAmount() + "");
		}
		tempMap.put("expried_date", DateUtil.date2Str(odTaskProcessing.getExpried_date(), "yyyy/MM/dd"));
		tempMap.put("submitted_date", DateUtil.date2Str(odTaskProcessing.getSubmitted_date(), DateUtil.fmt_yyyyMMdd));
		tempMap.put("status", odTaskProcessing.getStatus());
		tempMap.put("remark", StringUtil.formatStr(odTaskProcessing.getRemark()));
		tempMap.put("created_date", DateUtil.date2Str(odTaskProcessing.getCreated_date(), DateUtil.fmt_yyyyMMdd));

		String json_data = odTaskProcessing.getJson_data();
		if (StringUtils.isNotEmpty(json_data)) {
			List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(json_data);
			for (Map<String, Object> m : tempList) {
				if (StringUtils.isNotEmpty(StringUtil.formatStr(m.get("path_id")))) {
					doc_ids.add(m.get("path_id") + "");
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doc_url", docUtil.imageUrlFind(doc_ids));
		map.put("detail", tempMap);
		result.setResultData(map);
	}

	@Override
	public void odTaskProcessingLogListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "processing_id" });
		List<Map<String, Object>> logList = odTaskDao.selectOdTaskProcessingLogList(paramsMap);
		for (Map<String, Object> logMap : logList) {
			logMap.put("tracked_date",
					DateUtil.date2Str((Date) logMap.get("tracked_date"), DateUtil.fmt_yyyyMMddHHmmss));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", logList);
		result.setResultData(map);
	}

	/**
	 * 记录任务日志
	 * 
	 * @Title: odTaskProcessingLogCreate
	 * @param processing_id
	 * @param event_desc
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	private void odTaskProcessingLogCreate(String processing_id, String category, String event_desc)
			throws BusinessException, SystemException, Exception {
		OdTaskProcessingLog processingLog = new OdTaskProcessingLog();
		processingLog.setLog_id(IDUtil.getUUID());
		processingLog.setTracked_date(new Date());
		processingLog.setProcessing_id(processing_id);
		processingLog.setEvent_desc(event_desc);
		processingLog.setCategory(category);
		odTaskDao.insertOdTaskProcessingLog(processingLog);
	}

	/**
	 * 任务结算支付
	 * 
	 * @Title: taskSettlePay
	 * @param member_id
	 * @param amount
	 * @param order_no
	 * @author tiny
	 */
	private void taskSettlePay(final String member_id, final String payment_way_key, final BigDecimal amount,
			final String processing_id, final String taskName) throws BusinessException, SystemException, Exception {
		try {
			String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			reqParams.put("service", "finance.balancePay");
			bizParams.put("data_source", "SJLY_03");
			bizParams.put("order_type_key", "DDLX_16");
			bizParams.put("buyer_id", Constant.MEMBER_ID_MTH);
			bizParams.put("seller_id", member_id);
			bizParams.put("payment_way_key", payment_way_key);
			bizParams.put("amount", amount + "");
			bizParams.put("detail", "任务结算");
			bizParams.put("out_trade_no", processing_id);
			Map<String, String> out_trade_body = new HashMap<String, String>();
			out_trade_body.put("taskName", taskName);
			out_trade_body.put("processing_id", processing_id);
			bizParams.put("out_trade_body", FastJsonUtil.toJson(out_trade_body));
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(finance_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
		} catch (BusinessException e) {
			logger.warn("任务结算失败," + e.getMsg());
		} catch (SystemException e) {
			logger.error("任务结算异常", e);
		} catch (Exception e) {
			logger.error("任务结算异常", e);
		}
	}

}
