package com.meitianhui.sync.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.meitianhui.sync.service.SyncService;
import com.meitianhui.sync.util.FastJsonUtil;
import com.meitianhui.sync.util.HttpClientUtil;
import com.meitianhui.sync.util.PropertiesConfigUtil;
import com.meitianhui.sync.util.SyncUtil;

/**
 * 消费者礼券同步
 * 
 * @author Tiny
 *
 */
@SuppressWarnings("unchecked")
public class ConsumerVoucherSync {
	
	//当前方法是否可以调用
	public static boolean CONSUMER_FLAG  =  true;
	
	@Autowired
	private SyncService syncService;

	private static final Logger logger = Logger.getLogger(ConsumerVoucherSync.class);

	public void job() {
		if(CONSUMER_FLAG){
			CONSUMER_FLAG = false;
			try {
				long begin_time = System.currentTimeMillis();
				logger.info("-----消费者礼券同步开始-----");
				Integer begin_last_process_point = syncService.consumerVoucherLastLogFind();
				logger.info("消费者礼券---->日志记录为:" + begin_last_process_point);
				String php_customer_service_url = PropertiesConfigUtil.getProperty("php_customer_service_url");
				Map<String, String> reqParams = new HashMap<String, String>();
				reqParams.put("method", "temporary.getTicketRechangeLog");
				reqParams.put("app_token", PropertiesConfigUtil.getProperty("php_customer_service_app_token"));
				reqParams.put("last_process_point", begin_last_process_point + "");
				reqParams.put("page_size", PropertiesConfigUtil.getProperty("page_size"));
				String resultStr = HttpClientUtil.post(php_customer_service_url, reqParams);
				if (resultStr != null && !resultStr.equals("")) {
					Map<String, Object> resultMap = FastJsonUtil.jsonToMap(resultStr);
					Integer errcode = (Integer) resultMap.get("errcode");
					Map<String, Object> userMap = new HashMap<String, Object>();
					// 记录最新的日志id
					Integer last_process_point = begin_last_process_point;
					if (errcode == 0) {
						List<Map<String, Object>> dateList = (List<Map<String, Object>>) resultMap.get("data");
						for (Map<String, Object> log_map : dateList) {
							logger.info("消费者礼券---->日志:" + log_map);
							Integer log_id = Integer.parseInt(log_map.get("log_id") + "");
							String user_id = log_map.get("user_id") + "";
							String card_val = log_map.get("card_val") + "";
							// 不包含DX的数据才是需要同步的数据
							SyncUtil.userVoucherAccumulation(userMap, user_id, card_val);
							if (last_process_point < log_id) {
								last_process_point = log_id;
							}
						}
						if (userMap.size() > 0) {
							String finance_sync_service_url = PropertiesConfigUtil.getProperty("finance_sync_service_url");
							reqParams.clear();
							reqParams.put("service", "sync.consumerVoucherSync");
							Map<String, Object> tempMap = new HashMap<String, Object>();
							tempMap.put("data_map", FastJsonUtil.toJson(userMap));
							reqParams.put("params", FastJsonUtil.toJson(tempMap));
							resultStr = HttpClientUtil.post(finance_sync_service_url, reqParams);
							resultMap.clear();
							resultMap = FastJsonUtil.jsonToMap(resultStr);
							String rsp_code = (String) resultMap.get("rsp_code");
							if (rsp_code.equals("succ")) {
								logger.info("消费者礼券---->礼券变更同步成功");
								if (dateList.size() > 0) {
									String desc = "消费者礼券---->获取的条目数:" + dateList.size() + ",变更条目数" + userMap.size() + "条,耗时:"
											+ (System.currentTimeMillis() - begin_time) + "毫秒";
									syncService.consumerVoucherLogAdd(last_process_point, desc);
								}
							} else {
								dateList.clear();
								logger.warn("消费者礼券---->礼券变更同步异常:" + (String) resultMap.get("error_msg"));
							}
						}
					} else {
						logger.error("消费者礼券---->获取日志异常," + resultMap.toString());
					}
				} else {
					logger.error("消费者礼券---->日志:" + resultStr);
				}
			} catch (Exception e) {
				logger.error("消费者礼券---->同步异常", e);
			} finally {
				logger.info("-----消费者礼券同步结束-----");
				CONSUMER_FLAG = true;
			}
		}
	}
	


}