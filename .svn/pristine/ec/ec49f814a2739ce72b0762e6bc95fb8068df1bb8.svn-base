package com.meitianhui.community.easemob.body;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meitianhui.community.easemob.wrapper.BodyWrapper;

/***
 * 环信userNames集合body
 * 
 * @author 丁硕
 * @date 2016年8月8日
 */
public class IMUserNamesBody implements BodyWrapper {
	private String[] userIds;

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}
	
	public IMUserNamesBody(){
	}
	
	public IMUserNamesBody(String[] userIds){
		this.userIds = userIds;
	}

	public ContainerNode<?> getBody() {
		ObjectNode body = JsonNodeFactory.instance.objectNode();
		if(ArrayUtils.isNotEmpty(userIds)){
			ArrayNode arrayNode = body.putArray("usernames");
			for(String userId : userIds){
				arrayNode.add(userId);
			}
		}
		return body;
	}

	public Boolean validate() {
		return ArrayUtils.isNotEmpty(userIds);
	}
	
}
