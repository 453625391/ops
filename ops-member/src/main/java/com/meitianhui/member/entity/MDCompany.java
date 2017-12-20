package com.meitianhui.member.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 消费者
 * 
 * @author Tiny
 *
 */
public class MDCompany implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 消费者标识 **/
	private String company_id;
	/** 公司代码 **/
	private String company_code;
	/** 公司名称 **/
	private String company_name;
	/** 公司类型，引用数据字典分类（GZZLX） **/
	private String company_type_key;
	/** 上级组织id **/
	private String parent_id;
	/** 创建时间 **/
	private Date registered_date;
	/** 描述1 **/
	private String desc1;
	/** 地区标识 **/
	private String area_id;
	/** 详细地址 **/
	private String address;
	/** 联系人 **/
	private String contact_person;
	/** 联系电话 **/
	private String contact_tel;
	/** 经营状态，可选值：opening（正常）pending（异常）closed（停业） **/
	private String business_status;
	/** 状态，可选值：normal（正常） delete（删除） **/
	private String status;
	/** 创建时间 **/
	private Date created_date;
	/** 修改时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getCompany_code() {
		return company_code;
	}
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_type_key() {
		return company_type_key;
	}
	public void setCompany_type_key(String company_type_key) {
		this.company_type_key = company_type_key;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public Date getRegistered_date() {
		return registered_date;
	}
	public void setRegistered_date(Date registered_date) {
		this.registered_date = registered_date;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContact_person() {
		return contact_person;
	}
	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}
	public String getContact_tel() {
		return contact_tel;
	}
	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}
	public String getBusiness_status() {
		return business_status;
	}
	public void setBusiness_status(String business_status) {
		this.business_status = business_status;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
