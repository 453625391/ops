package com.meitianhui.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 领了么黑名单
 * 
 * @author Tiny
 *
 */
public class FgBlacklist implements Serializable {

	private static final long serialVersionUID = 1L;
	/** Id **/
	private String blacklist_id;
	/** 账号 **/
	private String account;
	/** 账号类型 **/
	private String account_type;
	/** 创建时间 **/
	private Date created_date;
	/** 备注 **/
	private String remark;
	
	public String getBlacklist_id() {
		return blacklist_id;
	}
	public void setBlacklist_id(String blacklist_id) {
		this.blacklist_id = blacklist_id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
