package com.meitianhui.member.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meitianhui.common.constant.CommonConstant;
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
import com.meitianhui.common.util.RedisLock;
import com.meitianhui.common.util.RedisUtil;
import com.meitianhui.common.util.StringUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.member.constant.Constant;
import com.meitianhui.member.constant.RspCode;
import com.meitianhui.member.dao.ConsumerDao;
import com.meitianhui.member.dao.ConsumerSignDao;
import com.meitianhui.member.dao.FavoriteStoreDao;
import com.meitianhui.member.dao.MemberDao;
import com.meitianhui.member.dao.StoresDao;
import com.meitianhui.member.entity.MDConsumer;
import com.meitianhui.member.entity.MDConsumerAddress;
import com.meitianhui.member.entity.MDConsumerSign;
import com.meitianhui.member.entity.MDFavoriteStore;
import com.meitianhui.member.entity.MDStores;
import com.meitianhui.member.entity.MDUserMember;
import com.meitianhui.member.service.ConsumerService;
import com.meitianhui.member.service.MemberService;

import net.sf.ehcache.store.Store;

/**
 * 消费者管理
 * 
 * @ClassName: ConsumerServiceImpl
 * @author tiny
 * @date 2017年2月23日 下午3:17:55
 *
 */
@SuppressWarnings("unchecked")
@Service
public class ConsumerServiceImpl implements ConsumerService {

	@Autowired
	public RedisUtil redisUtil;
	@Autowired
	private DocUtil docUtil;
	@Autowired
	public MemberDao memberDao;
	@Autowired
	public ConsumerDao consumerDao;
	@Autowired
	public ConsumerSignDao consumerSignDao;
	@Autowired
	public FavoriteStoreDao favoriteStoreDao;
	@Autowired
	public MemberService memberService;
	@Autowired
	public StoresDao storesDao;

	@Override
	public void handleConsumerSync(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "user_id", "member_id", "mobile", "registered_date", "status" });
			String member_id = paramsMap.get("member_id") + "";
			String user_id = paramsMap.get("user_id") + "";
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("member_id", member_id);
			List<MDConsumer> mDConsumerList = consumerDao.selectMDConsumer(tempMap);
			MDConsumer mDConsumer = null;
			if (mDConsumerList.size() == 0) {
				// 生成消费者会员id
				mDConsumer = new MDConsumer();
				Date date = new Date();
				BeanConvertUtil.mapToBean(mDConsumer, paramsMap);
				mDConsumer.setConsumer_id(member_id);
				mDConsumer.setSex_key(Constant.SEX_03);
				mDConsumer.setCreated_date(date);
				mDConsumer.setModified_date(date);
				consumerDao.insertMDConsumer(mDConsumer);
				Map<String, Object> logMap = new HashMap<String, Object>();
				logMap.put("log_id", IDUtil.getUUID());
				logMap.put("consumer_id", member_id);
				logMap.put("category", "数据同步");
				logMap.put("tracked_date", new Date());
				logMap.put("event", "消费者同步注册");
				consumerDao.insertMDConsumerLog(logMap);
				// 删除原有的关系
				tempMap.clear();
				tempMap.put("member_id", member_id);
				tempMap.put("member_type_key", CommonConstant.MEMBER_TYPE_CONSUMER);
				memberService.userMemberRelRemove(tempMap);
				// 重新设置关系
				MDUserMember mDUserMember = new MDUserMember();
				mDUserMember.setMember_id(member_id);
				mDUserMember.setUser_id(user_id);
				mDUserMember.setMember_type_key(CommonConstant.MEMBER_TYPE_CONSUMER);
				mDUserMember.setIs_admin("Y");
				memberDao.insertMDUserMember(mDUserMember);
				// 新用户注册赠送10元免单券
				String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
				Map<String, String> reqParams = new HashMap<String, String>();
				Map<String, Object> bizParams = new HashMap<String, Object>();
				reqParams.put("service", "gdBenefit.operate.gdBenefitCreate");
				bizParams.put("benefit_type", "free_coupon");
				String expired_date = DateUtil.addDate(DateUtil.date2Str(date, DateUtil.fmt_yyyyMMdd),
						DateUtil.fmt_yyyyMMdd, 2, 1);
				bizParams.put("expired_date", expired_date + " 23:59:59");
				bizParams.put("member_id", member_id);
				bizParams.put("limited_price", "10.00");
				bizParams.put("amount", "10.00");
				bizParams.put("event", "注册成为新用户系统赠送");
				reqParams.put("params", FastJsonUtil.toJson(bizParams));
				HttpClientUtil.postShort(goods_service_url, reqParams);
			} else {
				mDConsumer = mDConsumerList.get(0);
				String mobile = StringUtil.formatStr(paramsMap.get("mobile"));
				if (!mobile.equals(mDConsumer.getMobile())) {
					tempMap.clear();
					tempMap.put("consumer_id", mDConsumer.getConsumer_id());
					tempMap.put("mobile", mobile);
					consumerDao.updateMDConsumer(tempMap);
				}
				// 删除原有的关系
				tempMap.clear();
				tempMap.put("member_id", member_id);
				tempMap.put("member_type_key", CommonConstant.MEMBER_TYPE_CONSUMER);
				memberService.userMemberRelRemove(tempMap);
				// 重新设置关系
				MDUserMember mDUserMember = new MDUserMember();
				mDUserMember.setMember_id(member_id);
				mDUserMember.setUser_id(user_id);
				mDUserMember.setMember_type_key(CommonConstant.MEMBER_TYPE_CONSUMER);
				mDUserMember.setIs_admin("Y");
				memberDao.insertMDUserMember(mDUserMember);
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
	public void consumerLoginValidate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "user_id" });
			paramsMap.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
			List<MDUserMember> mDUserMemberList = memberDao.selectMDUserMember(paramsMap);
			if (mDUserMemberList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDUserMember mDUserMember = mDUserMemberList.get(0);
			// 验证会员状态
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("consumer_id", mDUserMember.getMember_id());
			List<MDConsumer> consumerList = consumerDao.selectMDConsumer(tempMap);
			if (consumerList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDConsumer consumer = consumerList.get(0);
			String status = consumer.getStatus();
			if (!status.equals(Constant.STATUS_NORMAL)) {
				throw new BusinessException(RspCode.MEMBER_STATUS_ERROR, "会员被禁用");
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("member_id", mDUserMember.getMember_id());
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
	public void consumerEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			if (paramsMap.size() == 1) {
				throw new BusinessException(RspCode.SYSTEM_NO_PARAMS_UPDATE,
						RspCode.MSG.get(RspCode.SYSTEM_NO_PARAMS_UPDATE));
			}
			List<MDConsumer> mDConsumerList = consumerDao.selectMDConsumer(paramsMap);
			if (mDConsumerList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			consumerDao.updateMDConsumer(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
			List<MDConsumer> mDConsumerList = consumerDao.selectMDConsumer(paramsMap);
			if (mDConsumerList.size() == 0) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
			}
			MDConsumer mDConsumer = mDConsumerList.get(0);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("nick_name", StringUtil.formatStr(mDConsumer.getNick_name()));
			resultMap.put("sex_key", StringUtil.formatStr(mDConsumer.getSex_key()));
			resultMap.put("birthday", mDConsumer.getBirthday() == null ? ""
					: DateUtil.date2Str(mDConsumer.getBirthday(), DateUtil.fmt_yyyyMMdd));
			resultMap.put("full_name", StringUtil.formatStr(mDConsumer.getFull_name()));
			resultMap.put("mobile", StringUtil.formatStr(mDConsumer.getMobile()));
			String head_pic_path = StringUtil.formatStr(mDConsumer.getHead_pic_path());
			resultMap.put("head_pic_path", head_pic_path);
			List<String> url_list = new ArrayList<String>();
			url_list.add(head_pic_path);
			resultMap.put("doc_url", docUtil.imageUrlFind(url_list));
			resultMap.put("address", StringUtil.formatStr(mDConsumer.getAddress()));
			resultMap.put("marital_status_key", StringUtil.formatStr(mDConsumer.getMarital_status_key()));
			resultMap.put("montly_income", StringUtil.formatStr(mDConsumer.getMontly_income()));
			resultMap.put("id_card", StringUtil.formatStr(mDConsumer.getId_card()));
			resultMap.put("education", StringUtil.formatStr(mDConsumer.getEducation()));
			resultMap.put("industry", StringUtil.formatStr(mDConsumer.getIndustry()));
			resultMap.put("home_circle", StringUtil.formatStr(mDConsumer.getHome_circle()));
			resultMap.put("home_circle_address", StringUtil.formatStr(mDConsumer.getHome_circle_address()));
			resultMap.put("work_circle", StringUtil.formatStr(mDConsumer.getWork_circle()));
			resultMap.put("work_circle_address", StringUtil.formatStr(mDConsumer.getWork_circle_address()));
			resultMap.put("life_circle", StringUtil.formatStr(mDConsumer.getLife_circle()));
			resultMap.put("life_circle_address", StringUtil.formatStr(mDConsumer.getLife_circle_address()));
			resultMap.put("hobby_circle", StringUtil.formatStr(mDConsumer.getHobby_circle()));
			resultMap.put("grade",  mDConsumer.getGrade().intValue() + "");
			resultMap.put("registered_date",
					DateUtil.date2Str(mDConsumer.getRegistered_date(), DateUtil.fmt_yyyyMMddHHmmss));
			resultMap.put("area_id", StringUtil.formatStr(mDConsumer.getArea_id()));
			resultMap.put("area_desc", StringUtil.formatStr(mDConsumer.getArea_desc()));
			
			
			//**********************************
			// 作者：丁忍  注释：查询推荐人信息
			//**********************************
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("member_id", paramsMap.get("member_id"));
			Map<String, Object> tempBindingRecommendMap = new HashMap<String, Object>();
			tempBindingRecommendMap = consumerDao.selectUserRecommend(tempMap);
			if(tempBindingRecommendMap != null){
				resultMap.put("reference_mobile", tempBindingRecommendMap.get("reference_mobile")+"");
			}else{
				resultMap.put("reference_mobile","");
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
	public void consumerListFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			List<MDConsumer> mDConsumerList = consumerDao.selectMDConsumerList(paramsMap);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (MDConsumer mDConsumer : mDConsumerList) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("consumer_id", StringUtil.formatStr(mDConsumer.getConsumer_id()));
				resultMap.put("nick_name", StringUtil.formatStr(mDConsumer.getNick_name()));
				resultMap.put("full_name", StringUtil.formatStr(mDConsumer.getFull_name()));
				resultMap.put("mobile", StringUtil.formatStr(mDConsumer.getMobile()));
				resultMap.put("grade", StringUtil.formatStr(mDConsumer.getGrade()));
				resultMap.put("area_id", StringUtil.formatStr(mDConsumer.getArea_id()));
				resultMap.put("area_desc", StringUtil.formatStr(mDConsumer.getArea_desc()));
				resultMap.put("address", StringUtil.formatStr(mDConsumer.getAddress()));
				resultMap.put("registered_date",
						DateUtil.date2Str(mDConsumer.getRegistered_date(), DateUtil.fmt_yyyyMMddHHmmss));
				resultList.add(resultMap);
			}
			Map<String, Object> map = new HashMap<String, Object>();
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
	public void consumerLevelFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		ValidateUtil.validateParams(paramsMap, new String[] { "member_id" });
		List<MDConsumer> mDConsumerList = consumerDao.selectMDConsumer(paramsMap);
		if (mDConsumerList.size() == 0) {
			throw new BusinessException(RspCode.MEMBER_NOT_EXIST, RspCode.MSG.get(RspCode.MEMBER_NOT_EXIST));
		}
		MDConsumer mDConsumer = mDConsumerList.get(0);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("grade", mDConsumer.getGrade());
		result.setResultData(resultMap);     
	}

	@Override
	public void consumerAddressFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id" });
			List<Map<String, Object>> mDConsumerAddressList = consumerDao.selectMDConsumerAddress(paramsMap);
			for (Map<String, Object> addressMap : mDConsumerAddressList) {
				addressMap.put("created_date",
						DateUtil.date2Str((Date) addressMap.get("created_date"), DateUtil.fmt_yyyyMMddHHmmss));
			}
			result.setResultData(mDConsumerAddressList);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerAddressCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap,
					new String[] { "consumer_id", "area_id", "address", "consignee", "mobile", "is_major_addr" });
			MDConsumerAddress mDConsumerAddress = new MDConsumerAddress();
			BeanConvertUtil.mapToBean(mDConsumerAddress, paramsMap);
			mDConsumerAddress.setAddress_id(IDUtil.getUUID());
			mDConsumerAddress.setCreated_date(new Date());
			consumerDao.insertMDConsumerAddress(mDConsumerAddress);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("address_id", mDConsumerAddress.getAddress_id());
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
	public void consumerAddressEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "address_id", "consumer_id" });
			String is_major_addr = StringUtil.formatStr(paramsMap.get("is_major_addr"));
			Map<String, Object> tempMap = new HashMap<String, Object>();
			// 如果是修改默认地址,先将所有的地址设置为非默认
			if (is_major_addr.equals("Y")) {
				tempMap.clear();
				tempMap.put("consumer_id", paramsMap.get("consumer_id"));
				tempMap.put("is_major_addr", "N");
				consumerDao.updateMDConsumerAddress(tempMap);
			}
			consumerDao.updateMDConsumerAddress(paramsMap);
			// 更新消费者表中地址字段
			tempMap.clear();
			tempMap.put("is_major_addr", "Y");
			tempMap.put("address_id", paramsMap.get("address_id"));
			tempMap.put("consumer_id", paramsMap.get("consumer_id"));
			List<Map<String, Object>> mDConsumerAddressList = consumerDao.selectMDConsumerAddress(tempMap);
			if (mDConsumerAddressList.size() > 0) {
				Map<String, Object> addressMap = mDConsumerAddressList.get(0);
				String area_id = addressMap.get("area_id") + "";
				String address = addressMap.get("address") + "";
				tempMap.clear();
				tempMap.put("address", address);
				tempMap.put("area_id", area_id);
				tempMap.put("consumer_id", paramsMap.get("consumer_id"));
				consumerDao.updateMDConsumer(tempMap);
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
	public void handleConsumerAddressRemove(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "address_id" });
			consumerDao.deleteMDConsumerAddress(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consumerSign(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		RedisLock redisLock = null;
		try {
			String[] gole_poll = new String[] { "10", "20", "50" };
			ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id" });
			String consumer_id = paramsMap.get("consumer_id") + "";
			String sign_in_date = DateUtil.getFormatDate(DateUtil.fmt_yyyyMMdd);
			String lockKey = "[consumerSign]_" + consumer_id + sign_in_date;
			// redis 锁30秒超时时间
			redisLock = new RedisLock(redisUtil, lockKey, 30 * 1000);
			redisLock.lock();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("consumer_id", consumer_id);
			tempMap.put("sign_in_date", sign_in_date);
			MDConsumerSign mdConsumerSign = consumerSignDao.selectMDConsumerSign(tempMap);
			String title = "一天只能签到一次哦";
			String url = "";
			String path_url = "";
			// 未签到
			if (null == mdConsumerSign) {
				int index = RandomUtils.nextInt(gole_poll.length);
				String gold = gole_poll[index];
				title = "签到成功,+" + gold + "金币";
				String sign_in_id = IDUtil.getUUID();
				mdConsumerSign = new MDConsumerSign();
				mdConsumerSign.setSign_in_id(sign_in_id);
				mdConsumerSign.setConsumer_id(consumer_id);
				mdConsumerSign.setSign_in_date(sign_in_date);
				mdConsumerSign.setCategory("gold");
				mdConsumerSign.setRemark("签到系统赠送" + gold + "金币");
				consumerSignDao.insertMDConsumerSign(mdConsumerSign);
				// 签到送5金币
				Map<String, Object> out_trade_body = new HashMap<String, Object>();
				out_trade_body.put("sign_in_id", sign_in_id);
				out_trade_body.put("sign_date", sign_in_date);
				memberService.balancePay(Constant.MEMBER_ID_MTH, consumer_id, "ZFFS_08", new BigDecimal(gold),
						sign_in_id, "签到系统赠送", out_trade_body);
			}
			// 获取图片地址
			String goods_service_url = PropertiesConfigUtil.getProperty("goods_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			bizParams.put("category", "c_app_sign");
			reqParams.put("service", "gdAppAdvert.app.gdAppAdvertFind");
			reqParams.put("params", FastJsonUtil.toJson(bizParams));
			String resultStr = HttpClientUtil.postShort(goods_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			if (!((String) resultMap.get("rsp_code")).equals(RspCode.RESPONSE_SUCC)) {
				throw new BusinessException((String) resultMap.get("error_code"), (String) resultMap.get("error_msg"));
			}
			Map<String, Object> data = (Map<String, Object>) resultMap.get("data");
			Map<String, Object> docUrl = (Map<String, Object>) data.get("doc_url");
			List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");
			if (list.size() > 0) {
				Map<String, Object> gdAppAdvertMap = list.get(0);
				url = StringUtil.formatStr(gdAppAdvertMap.get("url"));
				String path_id = StringUtil.formatStr(gdAppAdvertMap.get("path_id"));
				path_url = docUrl.get(path_id) + "";
			}
			Map<String, Object> resultDataMap = new HashMap<String, Object>();
			resultDataMap.put("title", title);
			resultDataMap.put("path_url", path_url);
			resultDataMap.put("url", url);
			result.setResultData(resultDataMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (redisLock != null) {
				redisLock.unlock();
			}
		}
	}

	@Override
	public void consumerStatusEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id", "status" });
			String status = paramsMap.get("status") + "";
			Map<String, String> reqParams = new HashMap<String, String>();
			Map<String, Object> bizParams = new HashMap<String, Object>();
			bizParams.put("consumer_id", paramsMap.get("consumer_id"));
			bizParams.put("status", status);
			consumerDao.updateMDConsumer(bizParams);
			// 状态如果是禁用的话，则删除用户登陆的授权信息
			if (status.equals(Constant.STATUS_DISABLED)) {
				bizParams.clear();
				bizParams.put("member_id", paramsMap.get("consumer_id"));
				bizParams.put("member_type_key", Constant.MEMBER_TYPE_CONSUMER);
				List<MDUserMember> userMemberList = memberDao.selectMDUserMember(bizParams);
				String user_service_url = PropertiesConfigUtil.getProperty("user_service_url");
				for (MDUserMember um : userMemberList) {
					// 删除授权token信息
					reqParams.clear();
					bizParams.clear();
					reqParams.put("service", "infrastructure.userTokenClear");
					bizParams.put("member_id", um.getMember_id());
					bizParams.put("user_id", um.getUser_id());
					reqParams.put("params", FastJsonUtil.toJson(bizParams));
					HttpClientUtil.postShort(user_service_url, reqParams);
				}
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
	public void consumerBaseInfoFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id" });
			MDConsumer mDConsumer = consumerDao.selectMDConsumerBaseInfo(paramsMap);
			if (null == mDConsumer) {
				throw new BusinessException(RspCode.MEMBER_NOT_EXIST, "消费者信息不存在");
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("consumer_id", StringUtil.formatStr(mDConsumer.getConsumer_id()));
			resultMap.put("nick_name", StringUtil.formatStr(mDConsumer.getNick_name()));
			resultMap.put("mobile", StringUtil.formatStr(mDConsumer.getMobile()));
			resultMap.put("head_pic_path", StringUtil.formatStr(mDConsumer.getHead_pic_path()));
			resultMap.put("grade", StringUtil.formatStr(mDConsumer.getGrade()));
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
	public void favoriteStore(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id", "stores_id" });
			String is_llm_stores = StringUtil.formatStr(paramsMap.get("is_llm_stores"));
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("consumer_id", paramsMap.get("consumer_id"));
			tempMap.put("stores_id", paramsMap.get("stores_id"));
			MDFavoriteStore favoriteStore = favoriteStoreDao.selectMDFavoriteStore(tempMap);
			if (favoriteStore == null) {
				favoriteStore = new MDFavoriteStore();
				BeanConvertUtil.mapToBean(favoriteStore, paramsMap);
				favoriteStore.setCreated_date(new Date());
				if (StringUtils.isEmpty(is_llm_stores)) {
					favoriteStore.setIs_llm_stores("N");
				} else {
					if (is_llm_stores.equals("Y")) {
						// 设置其他门店为非默认
						tempMap.clear();
						tempMap.put("consumer_id", paramsMap.get("consumer_id"));
						tempMap.put("is_llm_stores", "N");
						favoriteStoreDao.updateMDFavoriteStore(tempMap);
					}
				}
				favoriteStoreDao.insertMDFavoriteStore(favoriteStore);
			} else {
				if (is_llm_stores.equals("Y")) {
					favoriteStoreEdit(paramsMap, result);
				}
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
	public void favoriteStoreEdit(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id", "stores_id", "is_llm_stores" });
			String is_llm_stores = paramsMap.get("is_llm_stores") + "";
			if (is_llm_stores.equals("Y")) {
				// 设置其他门店为非默认
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("consumer_id", paramsMap.get("consumer_id"));
				tempMap.put("is_llm_stores", "N");
				favoriteStoreDao.updateMDFavoriteStore(tempMap);
			}
			paramsMap.put("created_date", new Date());
			favoriteStoreDao.updateMDFavoriteStore(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void favoriteStoreCancel(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id", "stores_id" });
			String stores_id = StringUtil.formatStr(paramsMap.get("stores_id"));
			if (!StringUtils.isEmpty(stores_id)) {
				List<String> list = StringUtil.str2List(stores_id, ",");
				if (list.size() > 1) {
					paramsMap.remove("stores_id");
					paramsMap.put("stores_id_in", list);
				}
			}
			favoriteStoreDao.deleteMDFavoriteStore(paramsMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void favoriteStoreList(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try {
			ValidateUtil.validateParams(paramsMap, new String[] { "consumer_id" });
			List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> list = favoriteStoreDao.selectMDFavoriteStoreList(paramsMap);
			List<String> doc_ids = new ArrayList<String>();
			for (Map<String, Object> map : list) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("stores_id", map.get("stores_id"));
				tempMap.put("stores_name", map.get("stores_name"));
				tempMap.put("address", map.get("address") + "");
				tempMap.put("is_llm_stores", map.get("is_llm_stores") + "");
				// 如果街景为空,则设置街景为门头图
				String neighbor_pic_path = StringUtil.formatStr(map.get("neighbor_pic_path"));
				if (neighbor_pic_path.equals("")) {
					List<String> tempList = new ArrayList<String>();
					String new_facade_pic_path = StringUtil.formatStr(map.get("new_facade_pic_path"));
					if (!new_facade_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_facade_pic_path, "\\|"));
					}
					String new_stores_pic_path = StringUtil.formatStr(map.get("new_stores_pic_path"));
					if (!new_stores_pic_path.equals("")) {
						tempList.addAll(StringUtil.str2List(new_stores_pic_path, "\\|"));
					}
					if (tempList.size() > 0) {
						neighbor_pic_path = tempList.get(0);
					}
				}
				tempMap.put("longitude", StringUtil.formatStr(map.get("longitude")));
				tempMap.put("latitude", StringUtil.formatStr(map.get("latitude")));
				tempMap.put("neighbor_pic_path", neighbor_pic_path);
				doc_ids.addAll(StringUtil.str2List(neighbor_pic_path, ","));
				resultlist.add(tempMap);
			}
			Map<String, Object> resultDate = new HashMap<String, Object>();
			resultDate.put("list", resultlist);
			resultDate.put("doc_url", docUtil.imageUrlFind(doc_ids));
			result.setResultData(resultDate);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void defaultStore(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try{
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("contact_tel", "13713720725");
			MDStores s = storesDao.selectMDStoresForDefault(tempMap);
			tempMap.clear();
			tempMap.put("stores_name", s.getStores_name());
			tempMap.put("stores_id", s.getStores_id());
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
	public void userRecommendCreate(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try{
			ValidateUtil.validateParams(paramsMap, new String[] { "recommend_mobile", "member_id" , "member_mobile" });
			if(paramsMap.get("recommend_mobile").equals(paramsMap.get("member_mobile"))){
				throw new BusinessException(RspCode.RECOMMEND_NOT_OWN, "不能设定自己为推荐人");
			}
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("mobile", paramsMap.get("recommend_mobile"));
			List<MDConsumer> resultlist = new ArrayList<MDConsumer>();
			resultlist = consumerDao.selectMDConsumer(tempMap);
			if(resultlist.size() < 1){
				throw new BusinessException(RspCode.RECOMMEND_NOT_EXIST, "推荐人信息不存在");
			}
			tempMap.clear();
			tempMap.put("member_id", paramsMap.get("member_id"));
			Map<String, Object> resoultMap = consumerDao.selectUserRecommend(tempMap);
			if(resoultMap != null){
				throw new BusinessException(RspCode.RECOMMEND_EXIST, "您已经存在推荐人");
			}
			
			
			MDConsumer mdConsumer = resultlist.get(0);  
			tempMap.clear();
			tempMap.put("recommend_id", IDUtil.getUUID());
			tempMap.put("reference_type_key", "consumer");
			tempMap.put("reference_mobile", paramsMap.get("recommend_mobile"));
			tempMap.put("reference_id", mdConsumer.getConsumer_id());
			tempMap.put("member_id", paramsMap.get("member_id"));
			tempMap.put("member_mobile", paramsMap.get("member_mobile"));
			tempMap.put("member_type_key", "consumer");
			tempMap.put("data_source", "SJLY_01");
			tempMap.put("reference_type", "领有惠APP");
			tempMap.put("created_date", new Date());
			tempMap.put("remark", "");
			consumerDao.insertUserRecommend(tempMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void userRecommendFind(Map<String, Object> paramsMap, ResultData result)
			throws BusinessException, SystemException, Exception {
		try{
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("member_id", paramsMap.get("member_id"));
			Map<String, Object> tempBindingRecommendMap = new HashMap<String, Object>();
			tempBindingRecommendMap = consumerDao.selectUserRecommend(tempMap);
			result.setResultData(tempBindingRecommendMap);
		} catch (BusinessException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

}
