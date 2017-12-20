package com.meitianhui.member.entity;

import java.io.Serializable;

/**
 * 会员与用户关系
 * 
 * @author Tiny
 *
 */
public class MDUserMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 用户标识 **/
	private String user_id;
	/** 会员分类，引用字典分类（HYFL) **/
	private String member_type_key;
	/** 会员标识 **/
	private String member_id;
	/** 是否管理员，可选值：Y(是)N(不是) **/
	private String is_admin;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getMember_type_key() {
		return member_type_key;
	}

	public void setMember_type_key(String member_type_key) {
		this.member_type_key = member_type_key;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(String is_admin) {
		this.is_admin = is_admin;
	}

}
