package com.meitianhui.community.easemob.body;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.node.ContainerNode;
import com.meitianhui.community.easemob.constant.MsgType;

/***
 * 环信透传消息
 * 
 * @author 丁硕
 * @date 2016年8月10日
 */
public class CommandMessageBody extends MessageBody {
	private String action;

	public CommandMessageBody(String targetType, String[] targets, String from, String action, Map<String, String> ext) {
		super(targetType, targets, from, ext);
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public ContainerNode<?> getBody() {
		if(!this.isInit()){
			this.getMsgBody().putObject("msg").put("type", MsgType.CMD).put("action", action);
			this.setInit(true);
		}

		return this.getMsgBody();
	}

	@Override
	public Boolean validate() {
		return super.validate() && StringUtils.isNotBlank(action);
	}
}
