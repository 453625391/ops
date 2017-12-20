package com.meitianhui.order.constant;

import java.util.LinkedHashMap;
import java.util.Map;

import com.meitianhui.common.constant.CommonConstant;

public class Constant extends CommonConstant {

	/** 我要批订单类型 - 团购订单 **/
	public static final String PS_ORDER_TYPE_ACTIVITY = "activity";
	/** 我要批订单类型-正常订单 **/
	public static final String PS_ORDER_TYPE_NORMAL = "normal";

	/** 未付款 **/
	public static final String ORDER_NONPAID = "non_paid";
	/** 团购中 **/
	public static final String ORDER_ACTIVING = "activing";
	/** 订单处理中 **/
	public static final String ORDER_PROCESSING = "processing";
	/** 订单确定 **/
	public static final String ORDER_CONFIRMED = "confirmed";
	/** 已发货 **/
	public static final String ORDER_DELIVERED = "delivered";
	/** 已到货 **/
	public static final String ORDER_ARRIVED = "arrived";
	/** 已支付 **/
	public static final String ORDER_PAYED = "payed";
	/** 已支付 **/
	public static final String ORDER_PAID = "paid";
	/** 已收货 **/
	public static final String ORDER_RECEIVED = "received";
	/** 已派工 **/
	public static final String ORDER_ASSIGNED = "assigned";
	/** 订单取消 **/
	public static final String ORDER_CANCELLED = "cancelled";

	/** 订单完成 **/
	public static final String ORDER_CLOSED = "closed";
	/** 订单成功 **/
	public static final String ORDER_SUCCEED = "succeed";
	/** 订单完失败 **/
	public static final String ORDER_FAIL = "fail";
	/** 已还款 **/
	public static final String ORDER_REFUNDED = "refunded";

	/** 订单结算状态 -待结算 **/
	public static final String ORDER_SETTLE_PENDING = "pending";
	/** 订单结算状态 -已结算 **/
	public static final String ORDER_SETTLE_SETTLED = "settled";
	/** 订单结算状态 -已付款 **/
	public static final String ORDER_SETTLE_PAID = "paid";

	/** 我要批 **/
	public static final String ORDER_TYPE_PROPRIETARY = "DDLX_01";
	/** 优惠券 **/
	public static final String ORDER_TYPE_COUPON = "DDLX_02";
	/** 一元购 **/
	public static final String ORDER_TYPE_LUCK_DRAW = "DDLX_03";
	/** 掌上便利店 **/
	public static final String ORDER_TYPE_POCKET_CONVENIENCE = "DDLX_04";
	/** 名品汇交易 **/
	public static final String ORDER_TYPE_GOLD_EXCHANGE = "DDLX_05";
	/** 惠易定交易 **/
	public static final String ORDER_TYPE_HUIYIDING = "DDLX_06";
	/** 返现订单 **/
	public static final String ORDER_TYPE_CASHBACK = "DDLX_07";
	/** 增值订单 **/
	public static final String ORDER_TYPE_APPRECIATION = "DDLX_09";
	/** 伙拼团订单 **/
	public static final String ORDER_TYPE_TSACTIVITY = "DDLX_10";

	/** 订单类型 自营订单 **/
	public static final String ORDER_TYPE_MEITIANHUI = "meitianhui";
	/** 订单类型 淘宝订单 **/
	public static final String ORDER_TYPE_TAOBAO = "taobao";

	/** 订单标签 **/
	public static final String ORDER_LABEL_FRESHMAN = "freshman";

	/** 驳回 **/
	public static final String OD_TASK_REJECT = "reject";
	/** 通过 **/
	public static final String OD_TASK_PASS = "pass";
	/** 失败 **/
	public static final String OD_TASK_FAIL = "fail";
	
	/** 阶梯价格 **/
	public static final String ACTIVITY_TYPE_LADDER = "ladder";
	
	/** 待提交 **/
	public static final String OD_TASK_PROCESSING_SUBMITTED = "submitted";
	/** 待审核 **/
	public static final String OD_TASK_PROCESSING_AUDITED = "audited";
	/** 待结算 **/
	public static final String OD_TASK_PROCESSING_SETTLED = "settled";
	/** 已中止 **/
	public static final String OD_TASK_PROCESSING_ABORTED = "aborted";
	/** 已完成 **/
	public static final String OD_TASK_PROCESSING_CLOSED = "closed";
	
	/** 账号类型 **/
	public static final String ACCOUNT_TYPE_MEITIANHUI = "meitianhui";
	/** 账号类型 **/
	public static final String ACCOUNT_TYPE_TAOBAO = "taobao";

	/** 运营商类型 **/
	public static final Map<String, String> YYS_TYPE_MAP = new LinkedHashMap<String, String>();

	/** 手机归属地 **/
	public static final Map<String, String> PROVINCE_MAP = new LinkedHashMap<String, String>();

	static {
		YYS_TYPE_MAP.put("1", "移动");
		YYS_TYPE_MAP.put("2", "联通");
		YYS_TYPE_MAP.put("3", "电信");

		PROVINCE_MAP.put("0", "全国");
		PROVINCE_MAP.put("1", "北京");
		PROVINCE_MAP.put("2", "新疆");
		PROVINCE_MAP.put("3", "重庆");
		PROVINCE_MAP.put("4", "广东");
		PROVINCE_MAP.put("5", "浙江");
		PROVINCE_MAP.put("6", "天津");
		PROVINCE_MAP.put("7", "广西");
		PROVINCE_MAP.put("8", "内蒙古");
		PROVINCE_MAP.put("9", "宁夏");
		PROVINCE_MAP.put("10", "江西");
		PROVINCE_MAP.put("11", "安徽");
		PROVINCE_MAP.put("12", "贵州");
		PROVINCE_MAP.put("13", "陕西");
		PROVINCE_MAP.put("14", "辽宁");
		PROVINCE_MAP.put("15", "山西");
		PROVINCE_MAP.put("16", "青海");
		PROVINCE_MAP.put("17", "四川");
		PROVINCE_MAP.put("18", "江苏");
		PROVINCE_MAP.put("19", "河北");
		PROVINCE_MAP.put("20", "西藏");
		PROVINCE_MAP.put("21", "附件");
		PROVINCE_MAP.put("22", "吉林");
		PROVINCE_MAP.put("23", "云南");
		PROVINCE_MAP.put("24", "上海");
		PROVINCE_MAP.put("25", "湖北");
		PROVINCE_MAP.put("26", "海南");
		PROVINCE_MAP.put("27", "甘肃");
		PROVINCE_MAP.put("28", "湖南");
		PROVINCE_MAP.put("29", "山东");
		PROVINCE_MAP.put("30", "河南");
		PROVINCE_MAP.put("31", "黑龙江");
		PROVINCE_MAP.put("32", "未知");

	}
}
