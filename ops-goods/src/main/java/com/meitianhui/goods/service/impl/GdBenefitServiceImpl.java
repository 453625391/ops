package com.meitianhui.goods.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.BeanConvertUtil;
import com.meitianhui.common.util.DateUtil;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.HttpClientUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.goods.constant.Constant;
import com.meitianhui.goods.constant.RspCode;
import com.meitianhui.goods.dao.GdBenefitDao;
import com.meitianhui.goods.dao.PsGoodsDao;
import com.meitianhui.goods.entity.GdBenefit;
import com.meitianhui.goods.entity.PsGoods;
import com.meitianhui.goods.service.GdBenefitService;

/**
 * 会员权益服务层
 * 
 * @ClassName: GdBenefitServiceImpl
 * @Description:
 * @author tiny
 * @date 2017年2月20日 下午3:55:47
 *
 */
@Service
public class GdBenefitServiceImpl implements GdBenefitService {

	@Autowired
	public GdBenefitDao gdBenefitDao;
	
	@Autowired
	public PsGoodsDao psGoodsDao;
  
	@Override
	public void gdBenefitCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "benefit_type", "expired_date", "member_id", "limited_price", "amount", "event" });
		Date created_date = new Date();
		GdBenefit gdBenefit = new GdBenefit();
		BeanConvertUtil.mapToBean(gdBenefit, paramsMap);
		gdBenefit.setStatus(Constant.STATUS_NORMAL);
		gdBenefit.setCreated_date(created_date);
		gdBenefit.setModified_date(created_date);
		gdBenefit.setBenefit_id(IDUtil.getUUID());
		gdBenefit.setPrivate_key(System.currentTimeMillis() + "");
		gdBenefit.setRemark(StringUtil.formatStr(paramsMap.get("event")));
		gdBenefitDao.insertGdBenefit(gdBenefit);

		// 记录日志
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("log_id", IDUtil.getUUID());
		tempMap.put("member_id", gdBenefit.getMember_id());
		tempMap.put("benefit_id", gdBenefit.getBenefit_id());
		tempMap.put("category", "赠送");
		tempMap.put("tracked_date", created_date);
		tempMap.put("event", paramsMap.get("event"));
		gdBenefitDao.insertGdBenefitLog(tempMap);

		Map<String, Object> resultDate = new HashMap<String, Object>();
		resultDate.put("benefit_id", gdBenefit.getBenefit_id());
		result.setResultData(resultDate);
	}

	/**
	 * 会员权益列表查询(消费者)
	 * 
	 * @Title: gdBenefitListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void usableGdBenefitListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
		paramsMap.put("status", Constant.STATUS_NORMAL);
		List<Map<String, Object>> gdBenefitList = gdBenefitDao.selectGdBenefitListForConsumer(paramsMap);
		for (Map<String, Object> map : gdBenefitList) {
			map.put("remark", StringUtil.formatStr(map.get("remark")));
			map.put("limited_price", map.get("limited_price") + "");
			map.put("amount", map.get("amount") + "");
			map.put("expired_date", DateUtil.date2Str((Date) map.get("expired_date"), DateUtil.fmt_yyyyMMdd));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", gdBenefitList);
		result.setResultData(map);
	}

	/**
	 * 会员权益日志查询(消费者)
	 */
	@Override
	public void gdBenefitLogListForConsumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
		List<Map<String, Object>> gdBenefitList = gdBenefitDao.selectGdBenefitLog(paramsMap);
		for (Map<String, Object> map : gdBenefitList) {
			map.put("event", StringUtil.formatStr(map.get("event")));
			map.put("tracked_date", DateUtil.date2Str((Date) map.get("tracked_date"), DateUtil.fmt_yyyyMMddHHmmss));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", gdBenefitList);
		result.setResultData(map);
	}

	@Override
	public void handleFreeCouponPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "benefit_id", "event", "pay_amount" });
		// 更新会员权益信息
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("benefit_id", paramsMap.get("benefit_id"));
		tempMap.put("member_id", paramsMap.get("member_id"));
		tempMap.put("benefit_type", "free_coupon");
		tempMap.put("status", Constant.STATUS_NORMAL);
		Map<String, Object> gdBenefitMap = gdBenefitDao.selectGdBenefit(tempMap);
		if (null == gdBenefitMap) {
			throw new BusinessException(RspCode.GD_BENEFIT_ERROR, "权益券已失效");
		}
		// 免单券限制
		BigDecimal limited_price = (BigDecimal) gdBenefitMap.get("limited_price");
		// 支付金额
		BigDecimal pay_amount = new BigDecimal(paramsMap.get("pay_amount") + "");
		// 如果限制金额大于0 并且支付金额大于限制金额
		if (limited_price.compareTo(BigDecimal.ZERO) > 0 && pay_amount.compareTo(limited_price) > 0) {
			throw new BusinessException(RspCode.GD_BENEFIT_ERROR, "支付金额超过免单券的限制金额");
		}
		tempMap.clear();
		tempMap.put("benefit_id", paramsMap.get("benefit_id"));
		tempMap.put("modified_date",
				DateUtil.date2Str((Date) gdBenefitMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
		tempMap.put("status", Constant.STATUS_USED);
		int updateFlag = gdBenefitDao.updateGdBenefit(tempMap);
		if (updateFlag == 0) {
			throw new BusinessException(RspCode.PROCESSING, "操作失败,请刷新后重试");
		}
		// 记录日志
		tempMap.clear();
		tempMap.put("log_id", IDUtil.getUUID());
		tempMap.put("member_id", paramsMap.get("member_id"));
		tempMap.put("benefit_id", paramsMap.get("benefit_id"));
		tempMap.put("category", "消费");
		tempMap.put("tracked_date", new Date());
		tempMap.put("event", paramsMap.get("event"));
		gdBenefitDao.insertGdBenefitLog(tempMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleMemberPointPay(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id","goods_id"});
		// 查询会员等级
		Integer consumerLevel = 0;
		Integer consumerPoint = 0;  
		Integer goodsLevel = 0;
		Integer goodsPoint = 0 ;
		String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
		String finance_service_url = PropertiesConfigUtil.getProperty("finance_service_url");
		Map<String, String> reqParams = new HashMap<String, String>();
		Map<String, Object> bizParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = null;
		String resultStr = null;
		reqParams.put("service", "consumer.consumer.consumerLevel");
		bizParams.put("member_id", paramsMap.get("member_id"));
		//bizParams.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		resultStr = HttpClientUtil.post(member_service_url, reqParams);
		resultMap = FastJsonUtil.jsonToMap(resultStr);
		if (resultMap.get("rsp_code").toString().equals(RspCode.RESPONSE_SUCC)) {
			Map<String, Object> memberLevel = (Map<String, Object>) resultMap.get("data");
			consumerLevel=  (Integer) memberLevel.get("grade");
		}
		// 查询会员积分
		reqParams.clear();   
		bizParams.clear();
		resultStr = null;
		resultMap = null;
		reqParams.put("service", "finance.memberPointFind");
		bizParams.put("member_id", paramsMap.get("member_id"));
		//bizParams.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
		reqParams.put("params", FastJsonUtil.toJson(bizParams));
		resultStr = HttpClientUtil.post(finance_service_url, reqParams);   
		resultMap = FastJsonUtil.jsonToMap(resultStr);  
		if (resultMap.get("rsp_code").toString().equals(RspCode.RESPONSE_SUCC)) {
			Map<String, Object> memberPoint = (Map<String, Object>) resultMap.get("data");  
			consumerPoint = (Integer)memberPoint.get("point_values"); 
		}
		  
		//查询商品所需等级和积分    
		List<PsGoods> psGoodsList = psGoodsDao.selectPsGoods(paramsMap);
		if(CollectionUtils.isNotEmpty(psGoodsList)){
//			 goodsLevel = psGoodsList.get(0).getGood_level();  
//			 goodsPoint  = psGoodsList.get(0).getGood_point();
		}  
		if(consumerLevel<goodsLevel||consumerPoint<goodsPoint){
			resultMap = new HashMap<String,Object>();
			resultMap.put("msg", "积分不足，不允许兑换");  
			result.setResultData(resultMap); 
		}   

	}

	/**
	 * 会员权益券统计
	 */
	@Override
	public void usableGdBenefitCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("count_num", "0");
		paramsMap.put("status", Constant.STATUS_NORMAL);
		Map<String, Object> countMap = gdBenefitDao.selectGdBenefitCount(paramsMap);
		if (null != countMap) {
			resultMap.put("count_num", countMap.get("count_num") + "");
		}
		result.setResultData(resultMap);
	}

	/**
	 * 会员权益转让
	 * 
	 * @Title: gdBenefitListFind
	 * @param paramsMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 * @throws Exception
	 * @author tiny
	 */
	@Override
	public void handleGdBenefitTransfer(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap,
				new String[] { "benefit_id", "member_id", "member_mobile", "transfer_member_id" });
		paramsMap.put("status", Constant.STATUS_NORMAL);
		Map<String, Object> gdBenefitMap = gdBenefitDao.selectGdBenefit(paramsMap);
		if (null == gdBenefitMap) {
			throw new BusinessException(RspCode.PS_GOODS_ERROR, "权益券不存在");
		}
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("benefit_id", paramsMap.get("benefit_id"));
		tempMap.put("member_id", paramsMap.get("transfer_member_id"));
		tempMap.put("remark", paramsMap.get("member_mobile") + "转增");
		tempMap.put("modified_date",
				DateUtil.date2Str((Date) gdBenefitMap.get("modified_date"), DateUtil.fmt_yyyyMMddHHmmss));
		gdBenefitDao.updateGdBenefit(tempMap);
	}

}
