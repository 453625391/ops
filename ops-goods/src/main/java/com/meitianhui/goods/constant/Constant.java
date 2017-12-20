package com.meitianhui.goods.constant;

import java.util.LinkedHashMap;
import java.util.Map;

import com.meitianhui.common.constant.CommonConstant;

public class Constant extends CommonConstant {

	/** 商品状态 正常 **/
	public static final String STATUS_NORMAL = "normal";
	/** 商品状态 上架 **/
	public static final String STATUS_ON_SHELF = "on_shelf";
	/** 商品状态 下架 **/
	public static final String STATUS_OFF_SHELF = "off_shelf";
	/** 商品状态 忽略 **/
	public static final String STATUS_NOT_PASS = "not_pass";
	/** 商品状态 删除 **/
	public static final String STATUS_DELETE = "delete";

	/** 驳回 **/
	public static final String STATUS_REJECT = "reject";
	/** 待定 **/
	public static final String STATUS_PENDING = "pending";

	/** 商品状态 激活 **/
	public static final String STATUS_ACTIVATED = "activated";
	/** 商品状态 验证 **/
	public static final String STATUS_VERIFIED = "verified";
	/** 商品状态 作废 **/
	public static final String STATUS_REVOKED = "revoked";

	/** 一元抽奖活动状态 进行中 **/
	public static final String STATUS_PROCESSING = "processing";
	/** 一元抽奖活动状态 已揭晓 **/
	public static final String STATUS_ANNOUNCED = "announced";
	/** 一元抽奖活动状态 已取消 **/
	public static final String STATUS_CANCEL = "cancel";
	/** 一元抽奖活动状态 未达成 **/
	public static final String STATUS_UNREACHED = "unreached";
	/** 一元抽奖活动状态 已兑换 **/
	public static final String STATUS_EXCHANGED = "exchanged";

	/** 状态 中奖 **/
	public static final String STATUS_WIN = "win";
	/** 状态 未中奖 **/
	public static final String STATUS_MISS = "miss";
	/** 状态 已退款 **/
	public static final String STATUS_REFUNDED = "refunded";
	/** 状态-进行中 **/
	public static final String STATUS_IN_PROGRESS = "in-progress";
	/** 状态-（未中奖） **/
	public static final String STATUS_LOST = "lost";

	/** 状态-已过期 **/
	public static final String STATUS_EXPIRED = "expired";
	/** 状态-已使用 **/
	public static final String STATUS_USED = "used";
	/** 状态-已转让 **/
	public static final String STATUS_TRANSFERRED = "transferred";

	/** 状态-待发货 **/
	public static final String STATUS_UNDELIVERED = "undelivered";
	/** 状态-已发货 **/
	public static final String STATUS_DELIVERED = "delivered";
	/** 状态-已收货 **/
	public static final String STATUS_RECEIVED = "received";
	/** 状态-取消 **/
	public static final String STATUS_CANCELLED = "cancelled";
	/** 状态-完成 **/
	public static final String STATUS_CLOSED = "closed";
	/** 状态-开奖 **/
	public static final String STATUS_LOTTERY = "lottery";
	/** 活动状态 上线 **/
	public static final String STATUS_ONLINE = "online";
	/** 活动状态 下线 **/
	public static final String STATUS_OFFLINE = "offline";

	/**  **/
	public static final String CATEGORY_PRESELL = "";
	/** 新品 **/
	public static final String CATEGORY_NEW = "新品";
	/** 尾货 **/
	public static final String CATEGORY_TAIL = "尾货";
	/** 兑换 **/
	public static final String CATEGORY_EXCHANGE = "兑换";

	/** 优惠券类型 现金购买 **/
	public static final String COUPON_CATEGORY_CASH = "a30b4937-0474-11e6-b922-fcaa1490ccaf";
	/** 优惠券类型 礼券兑换 **/
	public static final String COUPON_CATEGORY_VOUCHER = "a865d8cf-0474-11e6-b922-fcaa1490ccaf";
	/** 优惠券类型 免费领取 **/
	public static final String COUPON_CATEGORY_FREE = "ac3446f0-0474-11e6-b922-fcaa1490ccaf";

	/** 我要批 每天抢 **/
	public static final String ACTIVITY_01 = "HDMS_01";
	/** 我要批 每日一惠 **/
	public static final String ACTIVITY_02 = "HDMS_02";
	/** 超级返 热卖 **/
	public static final String ACTIVITY_03 = "HDMS_03";

	/** 抽奖活动类型 定时开（定时开） **/
	public static final String ACTIVITIES_TYPE_DSK = "DSK";
	/** 抽奖活动类型 摇一摇 **/
	public static final String ACTIVITIES_TYPE_YYY = "YYY";
	/** 抽奖活动类型 刮刮乐 **/
	public static final String ACTIVITIES_TYPE_GGL = "GGL";

	/** 商品来源 我要批 **/
	public static final String GOODS_PRODUCT_SOURCE_WOYAOPI = "woyaopi";
	/** 商品来源 惠易定 **/
	public static final String GOODS_PRODUCT_SOURCE_HUIYIDING = "huiyiding";
	/** 商品来源 自定义 **/
	public static final String GOODS_PRODUCT_SOURCE_ZIDINGYI = "zidingyi";
	/** 商品来源 领了么 **/
	public static final String GOODS_PRODUCT_SOURCE_LINGLEME = "lingleme";
	/** 商品来源 伙拼团 **/
	public static final String GOODS_PRODUCT_SOURCE_HUOPINTUAN = "huopintuan";

	/** 红包类型 现金 **/
	public static final String GC_ACTIVITY_TYPE_CASH = "cash";
	/** 红包类型 金币 **/
	public static final String GC_ACTIVITY_TYPE_GOLD = "gold";
	/** 红包类型 见面礼 **/
	public static final String GC_ACTIVITY_TYPE_FACE_GIFT = "face_gift";

	/** 规格类型 **/
	public static final Map<String, String> SPECIFICATION_UNIT_TYPE = new LinkedHashMap<String, String>();


	static {
		SPECIFICATION_UNIT_TYPE.put("01", "箱");
		SPECIFICATION_UNIT_TYPE.put("02", "包");
		SPECIFICATION_UNIT_TYPE.put("03", "袋");
		SPECIFICATION_UNIT_TYPE.put("04", "件");
		SPECIFICATION_UNIT_TYPE.put("05", "块");
		SPECIFICATION_UNIT_TYPE.put("06", "个");
		SPECIFICATION_UNIT_TYPE.put("07", "支");
		SPECIFICATION_UNIT_TYPE.put("08", "只");
		SPECIFICATION_UNIT_TYPE.put("09", "瓶");
		SPECIFICATION_UNIT_TYPE.put("10", "罐");
		SPECIFICATION_UNIT_TYPE.put("11", "条");
		SPECIFICATION_UNIT_TYPE.put("12", "盒");
		SPECIFICATION_UNIT_TYPE.put("13", "片");
		SPECIFICATION_UNIT_TYPE.put("14", "根");
		SPECIFICATION_UNIT_TYPE.put("15", "张");
		SPECIFICATION_UNIT_TYPE.put("16", "卷");
		SPECIFICATION_UNIT_TYPE.put("17", "套");
		SPECIFICATION_UNIT_TYPE.put("18", "段");
		SPECIFICATION_UNIT_TYPE.put("19", "幅");
		SPECIFICATION_UNIT_TYPE.put("20", "项");
		
	}
}
