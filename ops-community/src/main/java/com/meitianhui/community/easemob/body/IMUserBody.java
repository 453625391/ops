package com.meitianhui.community.easemob.body;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.meitianhui.community.easemob.wrapper.BodyWrapper;

/***
 * 环信用户请求body
 * 
 * @author 丁硕
 * @date 2016年7月22日
 */
public class IMUserBody implements BodyWrapper {
	
	//用户名
	private String userName;
	//密码
	private String password;
	//昵称，不必填
	private String nickName;
	
	public IMUserBody(String userName, String password, String nickName) {
		super();
		this.userName = userName;
		this.password = password;
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public ContainerNode<?> getBody() {
		return JsonNodeFactory.instance.objectNode().put("username", userName).put("password", password).put("nickname", nickName);
	}

	@Override
	public Boolean validate() {
		return StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password);
	}

}
