package com.meitianhui.finance.constant;

import com.meitianhui.common.constant.CommonRspCode;


/**
 * 业务处理异常码及描述</br>
 * 业务码规则:英文或英文简写描述,单词和单词之间用下划线("_")分开</br>
 * 此类需要继承公共异常码CommonRepCode
 * @author tiny
 * 
 */
public class RspCode extends CommonRspCode{
	/**数据来源错误**/
	public static String DATA_SOURCE_ERROR = "data_source_error";
	/**会员不存在**/
	public static String MEMBER_NO_EXIST = "member_no_exist";
	/**交易已存在**/
	public static String TRADE_EXIST = "trade_exist";
	/**交易流水号不存在**/
	public static String TRADE_NO_EXIST = "trade_no_exist";
	/**交易已关闭**/
	public static String TRADE_CLOSED = "trade_closed";
	/**交易失败**/
	public static String TRADE_FAIL = "trade_fail";
	/**交易码错误**/
	public static String TRADE_CODE_ERROR = "trade_code_error";
	
	
	/**亲情卡已被使用**/
	public static String PREPAY_CARD_USED = "prepay_card_used";
	/**亲情卡已激活**/
	public static String PREPAY_CARD_ACTIVATED = "prepay_card_activated";
	/**亲情卡未激活**/
	public static String PREPAY_CARD_NOT_ACTIVATE = "prepay_card_not_activate";
	/**亲情卡不存在**/
	public static String PREPAY_CARD_NO_NOT_EXIST = "prepay_card_no_not_exist";
	/**亲情卡已绑定**/
	public static String PREPAY_CARD_BOUND = "prepay_card_bound";
	/**亲情卡已销毁**/
	public static String PREPAY_CARD_DESTROY = "family_card_destroy";
	
	/**支付方式错误**/
	public static String PAYMENT_WAY_ERROR = "payment_way_error";
	/**业务类型不存在**/
	public static String BUSINESS_TYPE_NOT_EXIST = "business_type_not_exist";
	/**买家不存在**/
	public static String BUYER_NO_EXIST = "buyer_no_exist";
	/**卖家不存在**/
	public static String SELLER_NO_EXIST = "seller_no_exist";
	/**余额不足**/
	public static String BALANCE_NO_ENOUGH = "balance_no_enough";
	/**微信支付异常**/
	public static String WECHAT_ERROR = "wechat_error";
	/**支付宝支付异常**/
	public static String ALIPAY_ERROR = "alipay_error";
	/**资金账号已存在**/
	public static String CAPITAL_ACCOUNT_EXIST = "capital_account_exist";
	/**金额错误**/
	public static String TRADE_AMOUNT_ERROR = "trade_amount_error";
	/**优惠券不存在**/
	public static String COUPON_NO_EXIST = "coupon_no_exist";
	/**兑换失败**/
	public static String EXCHANGE_FAIL = "exchange_fail";
	
	/**赠送额度达到上限**/
	public static String REWARD_AMOUNT_REACHED_LIMIT = "reward_amount_reached_limit";
	
	static {
		MSG.put(DATA_SOURCE_ERROR, "数据来源错误");
		MSG.put(TRADE_EXIST, "交易已存在");
		MSG.put(TRADE_NO_EXIST, "交易流水号不存在");
		MSG.put(TRADE_CLOSED, "交易已关闭");
		MSG.put(PREPAY_CARD_USED, "亲情卡已被使用");
		MSG.put(PREPAY_CARD_ACTIVATED, "亲情卡已激活");
		MSG.put(PREPAY_CARD_NOT_ACTIVATE, "亲情卡未激活");
		MSG.put(PREPAY_CARD_NO_NOT_EXIST, "亲情卡不存在");
		MSG.put(PREPAY_CARD_BOUND, "亲情卡已绑定");
		MSG.put(PREPAY_CARD_DESTROY, "亲情卡已销毁");
		MSG.put(MEMBER_NO_EXIST, "会员不存在");
		MSG.put(BUSINESS_TYPE_NOT_EXIST, "业务类型不存在");
		MSG.put(PAYMENT_WAY_ERROR, "支付方式错误");
		MSG.put(BUYER_NO_EXIST, "买家不存在");
		MSG.put(SELLER_NO_EXIST, "卖家不存在");
		MSG.put(BALANCE_NO_ENOUGH, "余额不足");
		MSG.put(WECHAT_ERROR, "微信支付异常");
		MSG.put(ALIPAY_ERROR, "支付宝支付异常");
		MSG.put(CAPITAL_ACCOUNT_EXIST, "资金账号已存在");
		MSG.put(TRADE_AMOUNT_ERROR, "交易失败,金额不能小于0");
		MSG.put(COUPON_NO_EXIST, "优惠券不存在");
		MSG.put(REWARD_AMOUNT_REACHED_LIMIT, "赠送额度达到上限");
	}
	
}
