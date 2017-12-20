package com.meitianhui.infrastructure.constant;

import java.util.HashMap;
import java.util.Map;

import com.meitianhui.common.constant.CommonConstant;

public class Constant extends CommonConstant {
	/** 用户状态 **/
	public static final Map<String,String> USER_STATUS_MAP = new HashMap<String,String>();
	/** 正常 **/
	public static final String USER_STATUS_NORMAL = "normal";
	/** 未激活 **/
	public static final String USER_STATUS_INACTIVE = "inactive";
	/** 删除 **/
	public static final String USER_STATUS_DELETE = "delete";
	/** 冻结 **/
	public static final String USER_STATUS_REEZE = "reeze";
	/** 监管 **/
	public static final String USER_STATUS_SUPERVISE = "supervise";
	
	/**待核审**/
	public static final String APPSTORE_STATUS_TOREVIEW = "toreview";
	/**上线**/
	public static final String APPSTORE_STATUS_ONLINE = "online";
	/**下线**/
	public static final String APPSTORE_STATUS_OFFLINE = "offline";
	/**delete**/
	public static final String APPSTORE_STATUS_DELETE = "delete";
	
	/** token失效时间 **/
	public static final int TOKEN_TIME_OUT = 7200;
	
	static{
		USER_STATUS_MAP.put(USER_STATUS_NORMAL, "正常");
		USER_STATUS_MAP.put(USER_STATUS_INACTIVE, "未激活");
		USER_STATUS_MAP.put(USER_STATUS_DELETE, "删除");
		USER_STATUS_MAP.put(USER_STATUS_REEZE, "冻结");
		USER_STATUS_MAP.put(USER_STATUS_SUPERVISE, "监管");
	}
	
}
