package com.meitianhui.infrastructure.entity;

import java.io.Serializable;

/**
 * @author mole.wang 2015年12月20日
 *
 */
public class UserToken implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6497197159861401755L;

	/**
	 * id
	 */
	private String user_id;
	
	/**
	 * token
	 */
	private String token;
	
	/**
	 * ip
	 */
	private String ip;
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
