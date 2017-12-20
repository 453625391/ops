package com.meitianhui.community.easemob.body;

import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meitianhui.community.easemob.wrapper.BodyWrapper;

/***
 * 公用body
 * 
 * @author 丁硕
 * @date 2016年8月30日
 */
public class CommonBody implements BodyWrapper {
	
	private ObjectNode body = JsonNodeFactory.instance.objectNode(); 

	@Override
	public ContainerNode<?> getBody() {
		return this.body;
	}

	@Override
	public Boolean validate() {
		return true;
	}
	
	public ObjectNode put(String key, Long value){
		return body.put(key, value);
	}
	
	public ObjectNode put(String key, Integer value){
		return body.put(key, value);
	}
	
	public ObjectNode put(String key, String value){
		return body.put(key, value);
	}
	
	public ObjectNode put(String key, Boolean value){
		return body.put(key, value);
	}

}
