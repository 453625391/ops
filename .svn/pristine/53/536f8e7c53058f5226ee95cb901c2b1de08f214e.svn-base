package com.meitianhui.community.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.community.service.LiveRoomService;

/***
 * 
 * 直播室异步回调控制层
 * @author 丁硕
 * @date 2016年9月6日
 */
@Controller
@RequestMapping("/liveroom/notify")
public class LiveroomNotifyController {

	@Autowired
	private LiveRoomService liveroomService;
	
	private Logger logger = Logger.getLogger(LiveroomNotifyController.class);
	
	/***
	 * 直播状态变更
	 * @param request
	 * @param params
	 * @author 丁硕
	 * @date   2016年9月6日
	 */
	@RequestMapping(value="stateChange")
	public @ResponseBody void stateChange(HttpServletRequest request, @RequestParam Map<String, String> params){
		logger.info("直播状态发生变更，相关参数：" +  FastJsonUtil.toJson(params) + ", 执行相关操作。。。。。");
		try {
			String event = params.get("event");
			if("publish_done".equals(event)){	//查询并关闭对应的直播室
				String url = params.get("spaceName") + "/" + params.get("streamName");
				Map<String, String> liveroom = liveroomService.queryLiveroomByUrl(LiveRoomService.LIVEROOM_STATUS_LIVE, url);
				if(liveroom != null && !liveroom.isEmpty()){
					Map<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put("im_user_id", liveroom.get("owner"));
					paramsMap.put("room_id", liveroom.get("id"));
					//调用关闭直播接口
					liveroomService.closeOneLiveRoom(paramsMap, new ResultData());
				}
			} else if("publish".equals(event)){	//开始一个直播室
				
			}
		} catch (Exception e){
			logger.error("回调关闭直播室失败", e);
		}
	}
	
	/***
	 * 回播地址通知
	 * @param request
	 * @param params
	 * @author 丁硕
	 * @date   2016年9月6日
	 */
	@RequestMapping(value="playback")
	public @ResponseBody void playback(HttpServletRequest request, @RequestParam Map<String, Object> params){
		logger.info("回播地址消息通知，执行相关操作。。。。。");
		String resultJson = FastJsonUtil.toJson(params);
		logger.info("相关参数：" + resultJson);
	}
	
}
