package com.meitianhui.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.meitianhui.common.util.MoneyUtil;
import com.meitianhui.common.util.PropertiesConfigUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.finance.constant.Constant;
import com.meitianhui.finance.constant.RspCode;
import com.meitianhui.finance.dao.FinanceDao;
import com.meitianhui.finance.dao.PrepayCardDao;
import com.meitianhui.finance.entity.FDConsumerPrepayCard;
import com.meitianhui.finance.entity.FDMemberAsset;
import com.meitianhui.finance.entity.FDPrepayCard;
import com.meitianhui.finance.service.PrepayCardService;

/**
 * 金融服务的服务层
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
@Service
public class PrepayCardServiceImpl implements PrepayCardService {

	@Autowired
	public FinanceDao financeDao;
	
	@Autowired
	public PrepayCardDao prepayCardDao;
	
	@Override
	public void prepayCardActivate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "card_no", "member_id" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 校验卡号是否已激活
			tempMap.put("card_no", paramsMap.get("card_no"));
			List<FDPrepayCard> fDPrepayCardList = prepayCardDao.selectFDPrepayCard(tempMap);
			if (fDPrepayCardList.size() != 0) {
				throw new BusinessException(RspCode.PREPAY_CARD_ACTIVATED,
						RspCode.MSG.get(RspCode.PREPAY_CARD_ACTIVATED));
			}
			FDPrepayCard fDPrepayCard = new FDPrepayCard();
			fDPrepayCard.setCard_id(IDUtil.getUUID());
			fDPrepayCard.setCard_no((String) paramsMap.get("card_no"));
			fDPrepayCard.setStores_id((String) paramsMap.get("member_id"));
			fDPrepayCard.setCreated_date(new Date());
			fDPrepayCard.setRemark(
					"加盟店" + (String) paramsMap.get("member_id") + "激活了" + (String) paramsMap.get("card_no") + "亲情卡");
			fDPrepayCard.setStatus("activate");
			prepayCardDao.insertFDPrepayCard(fDPrepayCard);
			tempMap.clear();
			tempMap = null;
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerPrepayCardBind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "card_no", "member_id", "remark" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 校验卡号是否已绑定
			tempMap.put("card_no", paramsMap.get("card_no"));
			List<FDConsumerPrepayCard> fDConsumerPrepayCardList = prepayCardDao.selectFDConsumerPrepayCard(tempMap);
			if (fDConsumerPrepayCardList.size() != 0) {
				throw new BusinessException(RspCode.PREPAY_CARD_BOUND, RspCode.MSG.get(RspCode.PREPAY_CARD_BOUND));
			}

			List<FDPrepayCard> fDPrepayCardList = prepayCardDao.selectFDPrepayCard(tempMap);
			if (fDPrepayCardList.size() == 0) {
				throw new BusinessException(RspCode.PREPAY_CARD_NOT_ACTIVATE,
						RspCode.MSG.get(RspCode.PREPAY_CARD_NOT_ACTIVATE));
			}
			FDConsumerPrepayCard fDConsumerPrepayCard = new FDConsumerPrepayCard();
			BeanConvertUtil.mapToBean(fDConsumerPrepayCard, paramsMap);
			fDConsumerPrepayCard.setConsumer_prepay_card_id(IDUtil.getUUID());
			fDConsumerPrepayCard.setBinded_date(new Date());
			fDConsumerPrepayCard.setStatus("valid");
			prepayCardDao.insertFDConsumerPrepayCard(fDConsumerPrepayCard);
			tempMap.clear();
			tempMap = null;
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerPrepayCardUnBind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "card_no", "member_id" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 校验卡号是否存在
			tempMap.put("card_no", paramsMap.get("card_no"));
			tempMap.put("member_id", paramsMap.get("member_id"));
			tempMap.put("status", "valid");
			List<FDConsumerPrepayCard> fDConsumerPrepayCardList = prepayCardDao.selectFDConsumerPrepayCard(paramsMap);
			if (fDConsumerPrepayCardList.size() == 0) {
				throw new BusinessException(RspCode.PREPAY_CARD_NO_NOT_EXIST,
						RspCode.MSG.get(RspCode.PREPAY_CARD_NO_NOT_EXIST));
			}
			// 设置亲情卡绑定信息销毁
			tempMap.put("status", "invalid");
			tempMap.put("unbinded_date", new Date());
			prepayCardDao.updateFDConsumerPrepayCard(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerPrepayCardScan(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "card_no" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 校验卡号是否存在
			tempMap.put("card_no", paramsMap.get("card_no"));
			tempMap.put("status", "valid");
			List<FDConsumerPrepayCard> fDConsumerPrepayCardList = prepayCardDao.selectFDConsumerPrepayCard(tempMap);
			if (null == fDConsumerPrepayCardList || fDConsumerPrepayCardList.size() == 0) {
				throw new BusinessException(RspCode.PREPAY_CARD_NO_NOT_EXIST,
						RspCode.MSG.get(RspCode.PREPAY_CARD_NO_NOT_EXIST));
			}
			FDConsumerPrepayCard fDConsumerFamilyCard = fDConsumerPrepayCardList.get(0);
			tempMap.clear();
			tempMap.put("member_id", fDConsumerFamilyCard.getMember_id());
			FDMemberAsset fDMemberAsset = financeDao.selectFDMemberAsset(tempMap);
			if (fDMemberAsset == null) {
				throw new BusinessException(RspCode.USER_NOT_EXIST, RspCode.MSG.get(RspCode.USER_NOT_EXIST));
			}

			// 查询消费者会员信息
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			reqParams.put("service", "member.consumerFind");
			bizParams.put("member_id", fDConsumerFamilyCard.getMember_id());
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(member_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			String rsp_code = (String) resultMap.get("rsp_code");
			if (!rsp_code.equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			BigDecimal voucher_balance = fDMemberAsset.getVoucher_balance();
			// 消费者礼券余额调用接口查询
			if (fDMemberAsset.getMember_type_key().equals(Constant.MEMBER_TYPE_CONSUMER)) {
				voucher_balance = new BigDecimal(0);
			}
			tempMap.clear();
			tempMap.put("member_id", fDConsumerFamilyCard.getMember_id());
			tempMap.put("nick_name", StringUtil.formatStr(dateMap.get("nick_name")));
			tempMap.put("cash_balance", fDMemberAsset.getCash_balance() + "");
			tempMap.put("cash_froze", fDMemberAsset.getCash_froze() + "");
			// 可用余额
			tempMap.put("usable_balance",
					MoneyUtil.moneySub(fDMemberAsset.getCash_balance(), fDMemberAsset.getCash_froze()) + "");
			tempMap.put("gold", fDMemberAsset.getGold() + "");
			tempMap.put("voucher_balance", voucher_balance.intValue() + "");
			result.setResultData(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void storesPrepayCardScan(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "card_no" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 校验卡号是否存在
			tempMap.put("card_no", paramsMap.get("card_no"));
			tempMap.put("status", "valid");
			List<FDConsumerPrepayCard> fDConsumerPrepayCardList = prepayCardDao.selectFDConsumerPrepayCard(tempMap);
			if (null == fDConsumerPrepayCardList || fDConsumerPrepayCardList.size() == 0) {
				throw new BusinessException(RspCode.PREPAY_CARD_NO_NOT_EXIST,
						RspCode.MSG.get(RspCode.PREPAY_CARD_NO_NOT_EXIST));
			}
			FDConsumerPrepayCard fDConsumerFamilyCard = fDConsumerPrepayCardList.get(0);
			tempMap.clear();
			tempMap.put("member_id", fDConsumerFamilyCard.getMember_id());
			FDMemberAsset fDMemberAsset = financeDao.selectFDMemberAsset(tempMap);
			if (fDMemberAsset == null) {
				throw new BusinessException(RspCode.USER_NOT_EXIST, RspCode.MSG.get(RspCode.USER_NOT_EXIST));
			}
			
			// 查询消费者会员信息
			String member_service_url = PropertiesConfigUtil.getProperty("member_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			reqParams.put("service", "member.consumerFind");
			bizParams.put("member_id", fDConsumerFamilyCard.getMember_id());
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.post(member_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			String rsp_code = (String) resultMap.get("rsp_code");
			if (!rsp_code.equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> dateMap = (Map<String, Object>) resultMap.get("data");
			BigDecimal voucher_balance = fDMemberAsset.getVoucher_balance();
			// 消费者礼券余额调用接口查询
			if (fDMemberAsset.getMember_type_key().equals(Constant.MEMBER_TYPE_CONSUMER)) {
				voucher_balance = new BigDecimal(0);
			}
			tempMap.clear();
			tempMap.put("member_id", fDConsumerFamilyCard.getMember_id());
			tempMap.put("nick_name", StringUtil.formatStr(dateMap.get("nick_name")));
			tempMap.put("cash_balance", fDMemberAsset.getCash_balance() + "");
			tempMap.put("cash_froze", fDMemberAsset.getCash_froze() + "");
			// 可用余额
			tempMap.put("usable_balance",
					MoneyUtil.moneySub(fDMemberAsset.getCash_balance(), fDMemberAsset.getCash_froze()) + "");
			tempMap.put("gold", fDMemberAsset.getGold() + "");
			tempMap.put("voucher_balance", voucher_balance.intValue() + "");
			result.setResultData(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerPrepayCardFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			paramsMap.put("status", "valid");
			// 校验卡号是否存在
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<FDConsumerPrepayCard> fDConsumerPrepayCardList = prepayCardDao.selectFDConsumerPrepayCard(paramsMap);
			if (fDConsumerPrepayCardList.size() >= 0) {
				for (FDConsumerPrepayCard prepayCard : fDConsumerPrepayCardList) {
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("card_no", prepayCard.getCard_no());
					tempMap.put("remark", StringUtil.formatStr(prepayCard.getRemark()));
					tempMap.put("status", prepayCard.getStatus());
					list.add(tempMap);
				}
			}
			result.setResultData(list);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerPrepayCardCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			paramsMap.put("status", "valid");
			Map<String, Object> countsMap = prepayCardDao.selectFDConsumerPrepayCardCount(paramsMap);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("count_num", "0");
			if (null != countsMap) {
				resultMap.put("count_num", countsMap.get("count_num") + "");
			}
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void prepayCardActivateFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			List<FDPrepayCard> prepayCardList = prepayCardDao.selectFDPrepayCard(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

			for (FDPrepayCard prepayCard : prepayCardList) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("created_date",
						DateUtil.date2Str(prepayCard.getCreated_date(), DateUtil.fmt_yyyyMMddHHmmss));
				tempMap.put("card_no", StringUtil.formatStr(prepayCard.getCard_no()));
				resultList.add(tempMap);
			}
			result.setResultData(resultList);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void prepayCardActivateCount(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			//统计当月的数据
			paramsMap.put("created_date_start", DateUtil.getFormatDate(DateUtil.fmt_yyyyMM) + "-01");
			paramsMap.put("created_date_end", DateUtil.getFormatDate(DateUtil.fmt_yyyyMMdd));
			Map<String, Object> countsMap = prepayCardDao.selectFDPrepayCardActivateCount(paramsMap);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("count_num", "0");
			if (null != countsMap) {
				resultMap.put("count_num", countsMap.get("count_num") + "");
			}
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerPrepayCardEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "card_no", "remark" });
			// 校验卡号是否存在
			List<FDConsumerPrepayCard> fDConsumerPrepayCardList = prepayCardDao.selectFDConsumerPrepayCard(paramsMap);
			if (fDConsumerPrepayCardList.size() == 0) {
				throw new BusinessException(RspCode.PREPAY_CARD_NO_NOT_EXIST,
						RspCode.MSG.get(RspCode.PREPAY_CARD_NO_NOT_EXIST));
			}
			// 修改绑定的亲情信息
			prepayCardDao.updateFDConsumerPrepayCard(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void prepayCardStatusFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "card_no" });
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("card_no", paramsMap.get("card_no"));
			List<FDPrepayCard> fDPrepayCardList = prepayCardDao.selectFDPrepayCard(tempMap);
			Map<String, String> resultMap = new HashMap<String, String>();
			if (fDPrepayCardList.size() > 0) {
				FDPrepayCard fDPrepayCard = fDPrepayCardList.get(0);
				resultMap.put("status", fDPrepayCard.getStatus());
			} else {
				resultMap.put("status", "");
			}
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void storesActivatePrepayCardFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "stores_id" });
			List<Map<String, Object>> consumerList = prepayCardDao.selectStoresActivatePrepayCard(paramsMap);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("list", consumerList);
			result.setResultData(resultMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerTempPrepayCardCardNoGet(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			String card_no = IDUtil.random(16);
			Map<String, Object> resultData = new HashMap<String, Object>();
			resultData.put("temp_card_no", "MTH" + card_no + "Z");
			result.setResultData(resultData);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerTempPrepayCardCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id", "card_no", "remark" });
			FDConsumerPrepayCard fDConsumerPrepayCard = new FDConsumerPrepayCard();
			BeanConvertUtil.mapToBean(fDConsumerPrepayCard, paramsMap);
			fDConsumerPrepayCard.setConsumer_prepay_card_id(IDUtil.getUUID());
			fDConsumerPrepayCard.setBinded_date(new Date());
			fDConsumerPrepayCard.setStatus("valid");
			prepayCardDao.insertFDConsumerPrepayCard(fDConsumerPrepayCard);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void transPrepayCardFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<Map<String, Object>> queryList = prepayCardDao.selectTransPrepayCard(paramsMap);
			for (Map<String, Object> map : queryList) {
				map.put("transaction_date",
						DateUtil.date2Str((Date) map.get("transaction_date"), DateUtil.fmt_yyyyMMddHHmmss));
			}
			result.setResultData(queryList);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

}
