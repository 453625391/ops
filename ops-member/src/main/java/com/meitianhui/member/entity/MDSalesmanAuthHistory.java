package com.meitianhui.member.entity;

import java.io.Serializable;
import java.util.Date;

public class MDSalesmanAuthHistory implements Serializable{

	private static final long serialVersionUID =1L;
	
	private String history_id;                	//'认证记录标识'
	private String salesman_id;                	//'业务员标识'
	private String name;						//'姓名'
	private String sex_key;						//'性别'
	private String id_card;                 	//'身份证'
	private String id_card_pic_path;            //'实名认证照片'
	private String auth_status;                 //'实名认证状态，可选值：non-auth（未认证）、pass（已认证）、reject（驳回）'
	private Date created_date;                  //'创建时间'
	private Date modified_date;                 //'修改时间'
	private String remark;                 		//'备注'
	public String getHistory_id() {
		return history_id;
	}
	public void setHistory_id(String history_id) {
		this.history_id = history_id;
	}
	public String getSalesman_id() {
		return salesman_id;
	}
	public void setSalesman_id(String salesman_id) {
		this.salesman_id = salesman_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex_key() {
		return sex_key;
	}
	public void setSex_key(String sex_key) {
		this.sex_key = sex_key;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getId_card_pic_path() {
		return id_card_pic_path;
	}
	public void setId_card_pic_path(String id_card_pic_path) {
		this.id_card_pic_path = id_card_pic_path;
	}
	public String getAuth_status() {
		return auth_status;
	}
	public void setAuth_status(String auth_status) {
		this.auth_status = auth_status;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public Date getModified_date() {
		return modified_date;
	}
	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
