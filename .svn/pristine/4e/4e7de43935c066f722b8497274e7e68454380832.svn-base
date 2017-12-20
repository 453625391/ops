package com.meitianhui.sync.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.meitianhui.sync.service.SyncService;
import com.meitianhui.sync.util.DateUtil;
import com.meitianhui.sync.util.FastJsonUtil;
import com.meitianhui.sync.util.HttpClientUtil;
import com.meitianhui.sync.util.PropertiesConfigUtil;
import com.meitianhui.sync.util.StringUtil;

/**
 * 消费者信息同步 主要是密码
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
public class ConsumerInfoSync {

	@Autowired
	private SyncService syncService;

	private static final Logger logger = Logger.getLogger(ConsumerInfoSync.class);

	public void job() {
		try {
			long begin_time = System.currentTimeMillis();
			logger.info("-----消费者信息同步开始-----");
			Integer begin_last_process_point = syncService.userPasswordLastLogFind();
			logger.info("消费者信息同步---->日志记录为:" + begin_last_process_point);
			String php_customer_service_url = PropertiesConfigUtil.getProperty("php_customer_service_url");
			Map<String, String> reqParams = new HashMap<String, String>();
			reqParams.put("method", "temporary.getUserPassportEvent");
			reqParams.put("app_token", PropertiesConfigUtil.getProperty("php_customer_service_app_token"));
			reqParams.put("last_process_point", begin_last_process_point + "");
			reqParams.put("page_size", PropertiesConfigUtil.getProperty("user_sync_page_size"));
			String resultStr = HttpClientUtil.post(php_customer_service_url, reqParams);
			Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
			String errmsg = (String) resultMap.get("errmsg");
			Map<String, Object> userMap = new HashMap<String, Object>();
			// 记录最新的日志id
			Integer last_process_point = begin_last_process_point;
			if (null == errmsg) {
				List<Map<String, Object>> dateList = (List<Map<String, Object>>) resultMap.get("data");
				for (Map<String, Object> log_map : dateList) {
					logger.info("消费者信息日志---->" + log_map);
					String event_type = (String) log_map.get("event_type");
					if (!event_type.equals("USER_REGISTER") && !event_type.equals("PASSWORD_RESET")) {
						continue;
					}
					Integer log_id = Integer.parseInt(log_map.get("event_id") + "");
					String user_id = log_map.get("remark") + "";
					// 获取消费者信息
					reqParams.clear();
					reqParams.put("method", "temporary.getAccountById");
					reqParams.put("app_token", PropertiesConfigUtil.getProperty("php_customer_service_app_token"));
					reqParams.put("user_id", user_id);
					resultStr = HttpClientUtil.post(php_customer_service_url, reqParams);
					resultMap = FastJsonUtil.jsonToMap(resultStr);
					logger.info("消费者信息获取---->" + resultMap.toString());
					errmsg = (String) resultMap.get("errmsg");
					if (null == errmsg) {
						Map<String, Object> userInfoTempMap = (Map<String, Object>) resultMap.get("data");
						Map<String, Object> userInfoMap = new HashMap<String, Object>();
						userInfoMap.put("user_id", user_id);
						userInfoMap.put("mobile", StringUtil.formatStr(userInfoTempMap.get("mobile")));
						userInfoMap.put("password", StringUtil.formatStr(userInfoTempMap.get("login_password")));
						userInfoMap.put("slat", userInfoTempMap.get("slat"));
						userInfoMap.put("event_type", event_type);
						userInfoMap.put("registered_date", DateUtil.date2Str(new Date(), DateUtil.fmt_yyyyMMddHHmmss));
						userMap.put(user_id, userInfoMap);
					}
					if (last_process_point < log_id) {
						last_process_point = log_id;
					}
				}
				if (userMap.size() > 0) {
					String user_sync_service_url = PropertiesConfigUtil.getProperty("user_sync_service_url");
					reqParams.clear();
					reqParams.put("service", "sync.consumerInfoSync");
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("data_map", FastJsonUtil.toJson(userMap));
					reqParams.put("params", FastJsonUtil.toJson(tempMap));
					resultStr = HttpClientUtil.post(user_sync_service_url, reqParams);
					resultMap.clear();
					resultMap = FastJsonUtil.jsonToMap(resultStr);
					String rsp_code = (String) resultMap.get("rsp_code");
					if (rsp_code.equals("succ")) {
						logger.info("消费者信息同步---->变更通知成功");
						String desc = "消费者信息同步---->获取的条目数:" + dateList.size() + ",变更条目数" + userMap.size() + "条,耗时:"
								+ (System.currentTimeMillis() - begin_time) + "毫秒";
						syncService.userPasswordLogAdd(last_process_point, desc);
					} else {
						logger.info("消费者信息同步---->变更通知异常:" + (String) resultMap.get("error_msg"));
					}
				}
			} else {
				logger.info("消费者信息同步---->获取日志异常," + errmsg);
			}
		} catch (Exception e) {
			logger.error("消费者信息同步---->同步异常", e);
		} finally {
			logger.info("-----消费者信息同步结束-----");
		}
	}

}