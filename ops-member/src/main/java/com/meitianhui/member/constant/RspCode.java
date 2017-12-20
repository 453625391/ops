package com.meitianhui.member.constant;

import com.meitianhui.common.constant.CommonRspCode;

/**
 * 业务处理异常码及描述</br>
 * 业务码规则:英文或英文简写描述,单词和单词之间用下划线("_")分开</br>
 * 此类需要继承公共异常码CommonRepCode
 * 
 * @author tiny
 * 
 */
public class RspCode extends CommonRspCode {

	/** 会员已存在 **/
	public static String MEMBER_EXIST = "member_exist";
	/** 会员不存在 **/
	public static String MEMBER_NOT_EXIST = "member_not_exist";
	/** 推荐人已存在 **/
	public static String RECOMMEND_EXIST = "recommend_exist";
	/** 推荐人不存在 **/
	public static String RECOMMEND_NOT_EXIST = "recommend_not_exist";
	/** 推荐人不能是自己 **/
	public static String RECOMMEND_NOT_OWN = "recommend_not_own";
	
	/** 手机号已存在 **/
	public static String MOBILE_EXIST = "mobile_exist";
	/** 店铺已存在优惠活动 **/
	public static String ACTIVITY_PAYMENT_EXIST = "activity_payment_exist";
	/** 店铺不存在此优惠活动 **/
	public static String ACTIVITY_PAYMENT_NOT_EXIST = "activity_payment_not_exist";
	/** 会员已存在管理员账号 **/
	public static String MEMBER_ADMIN_EXIST = "member_admin_exist";

	/** 会员签到异常 **/
	public static String MEMBER_SIGN_ERROR = "member_sign_error";

	/** 会员评估失败 **/
	public static String MEMBER_EVALUATION_ERROR = "member_evaluation_error";

	/** 店东助手异常 **/
	public static String STORES_ASSISTANT_ERROR = "stores_assistant_error";
	
	/** 业务员操作异常 **/
	public static String SALESMAN_ERROR = "salesman_error";
	/** 业务员操作进行中 **/
	public static String SALESMAN_PROCESSING = "salesman_processing";
	
	
	/** 社区导购异常 **/
	public static String SALE_ASSISTANT_ERROR = "sale_assistant_error";

	/** 外部账号异常 **/
	public static String MEMBER_EXTERNAL_ACCOUNT_ERROR = "member_external_account_error";

	static {
		MSG.put(MEMBER_EXIST, "用户已存在");
		MSG.put(MEMBER_NOT_EXIST, "用户不存在");
		MSG.put(ACTIVITY_PAYMENT_EXIST, "店铺已存在优惠活动");
		MSG.put(ACTIVITY_PAYMENT_NOT_EXIST, "店铺不存在此优惠活动");
		MSG.put(MOBILE_EXIST, "手机号已存在");
		MSG.put(MEMBER_ADMIN_EXIST, "会员已存在管理员账号");
	}

}
