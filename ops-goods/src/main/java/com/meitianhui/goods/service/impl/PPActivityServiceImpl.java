package com.meitianhui.goods.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.DocUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.goods.constant.Constant;
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.dao.PPActivityDao;
import com.meitianhui.goods.entity.PPActivity;
import com.meitianhui.goods.entity.PPActivityDetail;
import com.meitianhui.goods.service.PPActivityService;


/**
 * 新品秀
* @ClassName: PPActivityServiceImpl  
* @author tiny 
* @date 2017年2月21日 上午11:37:07  
*
 */
@SuppressWarnings("unchecked")
@Service
public class PPActivityServiceImpl implements PPActivityService {

	@Autowired
	private DocUtil docUtil;

	@Autowired
	public PPActivityDao ppActivityDao;
	
	
	@Override
	public void ppActivityListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> ppActivityList = ppActivityDao.selectPPActivity(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>(); // 存图片信息
			for (Map<String, Object> ppActivityMap : ppActivityList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("activity_id", StringUtil.formatStr(ppActivityMap.get("activity_id")));
				tempMap.put("title", StringUtil.formatStr(ppActivityMap.get("title")));
				tempMap.put("desc1", StringUtil.formatStr(ppActivityMap.get("desc1")));
				tempMap.put("json_data", StringUtil.formatStr(ppActivityMap.get("json_data")));
				tempMap.put("start_date",
						DateUtil.date2Str((Date) ppActivityMap.get("start_date"), DateUtil.fmt_yyyyMMdd));
				tempMap.put("pic_json_data", StringUtil.formatStr(ppActivityMap.get("pic_json_data")));
				tempMap.put("duration", StringUtil.formatStr(ppActivityMap.get("duration")));
				tempMap.put("end_date", DateUtil.date2Str((Date) ppActivityMap.get("end_date"), DateUtil.fmt_yyyyMMdd));
				tempMap.put("total_num", StringUtil.formatStr(ppActivityMap.get("total_num")));
				tempMap.put("actual_total_num", StringUtil.formatStr(ppActivityMap.get("actual_total_num")));
				tempMap.put("fee", ppActivityMap.get("fee") + "");
				tempMap.put("status", StringUtil.formatStr(ppActivityMap.get("status")));
				// 店东是否报名的标识
				tempMap.put("entry", StringUtil.formatStr(ppActivityMap.get("entry")));
				tempMap.put("created_date",
						DateUtil.date2Str((Date) ppActivityMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("modified_date",
						DateUtil.date2Str((Date) ppActivityMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("remark", StringUtil.formatStr(ppActivityMap.get("remark")));
				String pic_info = StringUtil.formatStr(ppActivityMap.get("pic_json_data"));
				if (!pic_info.equals("")) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
					}
				}
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void ppActivityListForWebFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			List<Map<String, Object>> ppActivityList = ppActivityDao.selectPPActivityForWeb(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>(); // 存图片信息
			for (Map<String, Object> ppActivityMap : ppActivityList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("activity_id", StringUtil.formatStr(ppActivityMap.get("activity_id")));
				tempMap.put("title", StringUtil.formatStr(ppActivityMap.get("title")));
				tempMap.put("desc1", StringUtil.formatStr(ppActivityMap.get("desc1")));
				tempMap.put("json_data", StringUtil.formatStr(ppActivityMap.get("json_data")));
				tempMap.put("start_date",
						DateUtil.date2Str((Date) ppActivityMap.get("start_date"), DateUtil.fmt_yyyyMMdd));
				tempMap.put("json_data", StringUtil.formatStr(ppActivityMap.get("json_data")));
				tempMap.put("duration", StringUtil.formatStr(ppActivityMap.get("duration")));
				tempMap.put("end_date", DateUtil.date2Str((Date) ppActivityMap.get("end_date"), DateUtil.fmt_yyyyMMdd));
				tempMap.put("total_num", StringUtil.formatStr(ppActivityMap.get("total_num")));
				tempMap.put("actual_total_num", StringUtil.formatStr(ppActivityMap.get("actual_total_num")));
				tempMap.put("fee", ppActivityMap.get("fee") + "");
				tempMap.put("status", StringUtil.formatStr(ppActivityMap.get("status")));
				// 店东是否报名的标识
				tempMap.put("entry", StringUtil.formatStr(ppActivityMap.get("entry")));
				tempMap.put("created_date",
						DateUtil.date2Str((Date) ppActivityMap.get("created_date"), DateUtil.fmt_yyyyMMdd));
				String pic_info = StringUtil.formatStr(ppActivityMap.get("json_data"));
				if (!pic_info.equals("")) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_info);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
					}
				}
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void ppActivityAdd(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "title", "start_date", "duration", "end_date", "fee", "operator" });
			// 插入新品秀活动
			PPActivity ppActivity = new PPActivity();
			Date date = new Date();
			BeanConvertUtil.mapToBean(ppActivity, paramsMap);
			String activity_id = IDUtil.getUUID(); // 生成主键
			ppActivity.setActivity_id(activity_id);
			ppActivity.setCreated_date(date);
			ppActivity.setModified_date(date);
			ppActivity.setStatus(Constant.STATUS_ONLINE);
			ppActivityDao.insertPPActivity(ppActivity);
			// 返回activity_id
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("activity_id", activity_id);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void ppActivityEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("activity_id", paramsMap.get("activity_id"));
			// 修改新秀活动
			List<Map<String, Object>> ppActivityList = ppActivityDao.selectPPActivity(params);
			if (ppActivityList.size() == 0) {
				throw new BusinessException(RspCode.PP_ACTIVITY_ERROR, "操作失败,要修改的活动不存在");
			}
			Map<String, Object> ppActivity = ppActivityList.get(0);

			paramsMap.put("modified_date",
					DateUtil.date2Str((Date) ppActivity.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
			int updateFlag = ppActivityDao.updatePPActivity(paramsMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PP_ACTIVITY_ERROR, "操作失败,请刷新后重试");
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void ppActivityDelete(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id" });
			ppActivityDao.deletePPActivity(paramsMap);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void ppActivityDetailListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> ppActivityDetailList = ppActivityDao.selectPPActivityDetail(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<String> doc_ids = new ArrayList<String>(); // 存图片信息
			for (Map<String, Object> ppActivityDetailMap : ppActivityDetailList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("detail_id", StringUtil.formatStr(ppActivityDetailMap.get("detail_id")));
				tempMap.put("activity_id", StringUtil.formatStr(ppActivityDetailMap.get("activity_id")));
				tempMap.put("stores_id", StringUtil.formatStr(ppActivityDetailMap.get("stores_id")));
				String stores_json_data = StringUtil.formatStr(ppActivityDetailMap.get("stores_json_data"));
				if (!StringUtils.isEmpty(stores_json_data)) {
					Map<String, Object> storesMap = FastJsonUtil.jsonToMap(stores_json_data);
					tempMap.put("stores_name", StringUtil.formatStr(storesMap.get("stores_name")));
					tempMap.put("contact_person", StringUtil.formatStr(storesMap.get("contact_person")));
					tempMap.put("contact_tel", StringUtil.formatStr(storesMap.get("contact_tel")));
					tempMap.put("address", StringUtil.formatStr(storesMap.get("address")));
				}
				tempMap.put("pic_json_data", StringUtil.formatStr(ppActivityDetailMap.get("pic_json_data")));
				tempMap.put("acreage", StringUtil.formatStr(ppActivityDetailMap.get("acreage")));
				tempMap.put("dms", StringUtil.formatStr(ppActivityDetailMap.get("dms")));
				tempMap.put("is_chosen", StringUtil.formatStr(ppActivityDetailMap.get("is_chosen")));
				tempMap.put("created_date",
						DateUtil.date2Str((Date) ppActivityDetailMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("modified_date", DateUtil.date2Str((Date) ppActivityDetailMap.get("modified_date"),
						DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("remark", StringUtil.formatStr(ppActivityDetailMap.get("remark")));
				String pic_json_data = StringUtil.formatStr(ppActivityDetailMap.get("pic_json_data"));
				if (!StringUtils.isEmpty(pic_json_data)) {
					List<Map<String, Object>> tempList = FastJsonUtil.jsonToList(pic_json_data);
					for (Map<String, Object> m : tempList) {
						if (!StringUtil.formatStr(m.get("path_id")).equals("")) {
							doc_ids.add(m.get("path_id") + "");
						}
					}
				}
				resultList.add(tempMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("doc_url", docUtil.imageUrlFind(doc_ids));
			map.put("list", resultList);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void ppActivityDetailAdd(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "stores_id", "acreage", "dms",
					"stores_name", "contact_person", "contact_tel", "address" });
			// 新品秀活动报名
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("activity_id", paramsMap.get("activity_id"));
			tempMap.put("stores_id", paramsMap.get("stores_id"));
			List<Map<String, Object>> ppActivityDetailList = ppActivityDao.selectPPActivityDetail(tempMap);
			if (ppActivityDetailList.size() != 0) {
				throw new BusinessException(RspCode.PP_ACTIVITY_ERROR, "您已经参与过此活动了,请勿重复提交");
			}
			tempMap.clear();
			PPActivityDetail ppActivityDetail = new PPActivityDetail();
			Date date = new Date();
			tempMap.put("stores_name", paramsMap.get("stores_name") + "");
			tempMap.put("contact_person", paramsMap.get("contact_person") + "");
			tempMap.put("contact_tel", paramsMap.get("contact_tel") + "");
			tempMap.put("address", paramsMap.get("address") + "");

			BeanConvertUtil.mapToBean(ppActivityDetail, paramsMap);
			// 生成主键
			String datail_id = IDUtil.getUUID();
			ppActivityDetail.setStores_json_data(FastJsonUtil.toJson(tempMap));
			ppActivityDetail.setDetail_id(datail_id);
			ppActivityDetail.setCreated_date(date);
			ppActivityDetail.setModified_date(date);
			// 是否选中默认为不选中（选中为Y）
			ppActivityDetail.setIs_chosen("N");
			ppActivityDao.insertPPActivityDetail(ppActivityDetail);
			// 报名成功后报名人数加1 actual_total_num = total_num+actual_total_num
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("activity_id", paramsMap.get("activity_id"));
			params.put("apply_num", "1");
			ppActivityDao.updatePPActivityNum(params);
			// 返回datail_id
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("datail_id", datail_id);
			result.setResultData(map);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void ppActivityDetailEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "activity_id", "detail_id", "chosen" });
			// 修改新秀活动
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("detail_id", paramsMap.get("detail_id"));
			List<Map<String, Object>> ppActivityDetailList = ppActivityDao.selectPPActivityDetail(params);
			if (ppActivityDetailList.size() == 0) {
				throw new BusinessException(RspCode.PP_ACTIVITY_ERROR, "操作失败,活动不存在");
			}
			Map<String, Object> ppActivityDetail = ppActivityDetailList.get(0);
			if (ppActivityDetail.get("is_chosen").equals(paramsMap.get("chosen"))) {
				throw new BusinessException(RspCode.PP_ACTIVITY_ERROR, "操作失败,请勿重复提交");
			}
			paramsMap.put("modified_date",
					DateUtil.date2Str((Date) ppActivityDetail.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
			int updateFlag = ppActivityDao.updatePPActivityDetail(paramsMap);
			if (updateFlag == 0) {
				throw new BusinessException(RspCode.PP_ACTIVITY_ERROR, "操作失败,请刷新后重试");
			}
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void ppActivityDetailDelete(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "detail_id" });
			// TODO增加判断

			ppActivityDao.deletePPActivityDetail(paramsMap);
		} catch (Exception e) {
			throw e;
		}
	}



}
