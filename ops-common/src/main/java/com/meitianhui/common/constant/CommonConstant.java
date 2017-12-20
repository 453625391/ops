package com.meitianhui.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的常量类
 * @author Tiny
 *
 */
public class CommonConstant {

	
	/**人民币**/
	public static final String CURRENCY_CNY = "CNY";
	/** 消费者 **/
	public static final String MEMBER_TYPE_CONSUMER = "consumer";
	/** 门店 **/
	public static final String MEMBER_TYPE_STORES = "stores";
	/** 供应商 **/
	public static final String MEMBER_TYPE_SUPPLIER = "supplier";
	/** 每天惠公司 **/
	public static final String MEMBER_TYPE_COMPANY = "company";
	/** 业务员 **/
	public static final String MEMBER_TYPE_SALESMAN = "salesman";
	
	
	/** 每天惠账号 **/
	public static final String MEMBER_ID_MTH = "10000001";
	/** 惠易定 **/
	public static final String MEMBER_ID_HYD = "10000002";
	/** 匿名账号 **/
	public static final String MEMBER_ID_ANONYMITY = "10000003";
	/** 每天惠商贸公司 **/
	public static final String MEMBER_ID_BUSINESS = "10000004";
	/** 每天惠平台账号 **/
	public static final String MEMBER_ID_PLATFORM = "10000005";

	

	
	
	/**正常**/
	public static final String STATUS_NORMAL = "normal";
	/**删除**/
	public static final String STATUS_DELETED = "deleted";
	/**禁用**/
	public static final String STATUS_DISABLED = "disabled";
	
	
	/**删除**/
	public static final String STATUS_DELETE = "delete";
	/**无效**/
	public static final String STATUS_DISABLE = "disable";
	/**有效**/
	public static final String STATUS_ENABLE = "enable";
	
	/** 数据来源 **/
	public static final Map<String, String> DATA_SOURCE_MAP = new HashMap<String, String>();
	/** 消费者端App **/
	public static final String DATA_SOURCE_SJLY_01 = "SJLY_01";
	/** 店东助手App **/
	public static final String DATA_SOURCE_SJLY_02 = "SJLY_02";
	/** 运营管理系统 **/
	public static final String DATA_SOURCE_SJLY_03 = "SJLY_03";
	/** 鸿商收银系统 **/
	public static final String DATA_SOURCE_SJLY_04 = "SJLY_04";
	/** 帮你惠 **/
	public static final String DATA_SOURCE_SJLY_05 = "SJLY_05";
	/** 惠易定 **/
	public static final String DATA_SOURCE_SJLY_06 = "SJLY_06";
	/** 熟么 **/
	public static final String DATA_SOURCE_SJLY_12 = "SJLY_12";
	/** 惠易定3.0 **/
	public static final String DATA_SOURCE_SJLY_13 = "SJLY_13";
	/** 惠点收银 **/
	public static final String DATA_SOURCE_SJLY_14 = "SJLY_14";
	/** 惠驿哥 **/
	public static final String DATA_SOURCE_SJLY_15 = "SJLY_15";
	/** 搞掂APP **/
	public static final String DATA_SOURCE_SJLY_16 = "SJLY_16";
	/** 微信小程序支付 **/
	public static final String DATA_SOURCE_SJLY_17 = "SJLY_17";

	
	/** 推送消息类型-付款码推送 **/
	public static final String PUSH_MESSAGE_TYPE_01 = "1";
	/** 推送消息类型-解冻余额 **/
	public static final String PUSH_MESSAGE_TYPE_02 = "2";
	/** 推送消息类型-一元抽奖验证推送 **/
	public static final String PUSH_MESSAGE_TYPE_03 = "3";
	
	static{

		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_01, "消费者端App");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_02, "店东助手App");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_03, "运营管理系统");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_04, "鸿商收银系统");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_05, "帮你惠");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_06, "惠易定");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_12, "熟么");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_13, "惠易定3.0");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_14, "惠点收银");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_15, "惠驿哥");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_16, "搞掂APP");
		DATA_SOURCE_MAP.put(DATA_SOURCE_SJLY_17, "微信小程序");

	}
}
