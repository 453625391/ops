package com.meitianhui.sync.util;

import java.util.Map;

public class SyncUtil {

	/**
	 * 用户礼券累加
	 * 
	 * @param map
	 * @param user
	 * @param card_val
	 */
	public static void userVoucherAccumulation(Map<String, Object> map, String user_id, String trade_card_val) {
		String pre_card_val = (String) map.get(user_id);
		if(pre_card_val == null){
			map.put(user_id, trade_card_val);
		}else{
			map.put(user_id,MoneyUtil.moneyAdd(pre_card_val , trade_card_val));
		}
	}

}
