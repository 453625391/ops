package com.meitianhui.community.easemob.body;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.meitianhui.community.easemob.wrapper.BodyWrapper;

/****
 * 环信认证token请求body
 * 
 * @author 丁硕
 * @date 2016年7月21日
 */
public class AuthTokenBody implements BodyWrapper {

	private String grant_type = "client_credentials";

	private String client_id;

	private String client_secret;

	public AuthTokenBody(String client_id, String client_secret) {
		super();
		this.client_id = client_id;
		this.client_secret = client_secret;
	}

	public ContainerNode<?> getBody() {
		return JsonNodeFactory.instance.objectNode().put("grant_type", grant_type).put("client_id", client_id).put("client_secret", client_secret);
	}

	public Boolean validate() {
		return StringUtils.isNotBlank(client_id) && StringUtils.isNotBlank(client_secret);
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}
	
	

}
