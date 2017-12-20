package com.meitianhui.member.entity;

import java.io.Serializable;

/**
 * 
 * @author mole
 * @date 2016年12月19日 下午3:17:52
 */
public class MDAssistant implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2045261255281615049L;
	
	private String assistant_id    ;//'标识'
	private String assistant_name  ;//'名称' 
	private String desc1           ;//'描述1' 
	private String id_card         ;//'身份证'
	private String head_pic_path   ;//'头像' 
	private String sex_key         ;//'性别，引用数据字典分类（XBDM） ' 
	private String area_id         ;//'地区标识' 
	private String address         ;//'详细地址' 
	private String contact_tel     ;//'联系电话' 
	private String registered_date ;//'注册时间' 
	private String status          ;//'状态，可选值：normal（正常） delete（删除）' 
	private String created_date    ;//'创建时间'
	private String modified_date   ;//'修改时间'
	private String remark          ;//'备注',
	
	public String getAssistant_id() {
		return assistant_id;
	}
	public void setAssistant_id(String assistant_id) {
		this.assistant_id = assistant_id;
	}
	public String getAssistant_name() {
		return assistant_name;
	}
	public void setAssistant_name(String assistant_name) {
		this.assistant_name = assistant_name;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getHead_pic_path() {
		return head_pic_path;
	}
	public void setHead_pic_path(String head_pic_path) {
		this.head_pic_path = head_pic_path;
	}
	public String getSex_key() {
		return sex_key;
	}
	public void setSex_key(String sex_key) {
		this.sex_key = sex_key;
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
	public String getContact_tel() {
		return contact_tel;
	}
	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}
	public String getRegistered_date() {
		return registered_date;
	}
	public void setRegistered_date(String registered_date) {
		this.registered_date = registered_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getModified_date() {
		return modified_date;
	}
	public void setModified_date(String modified_date) {
		this.modified_date = modified_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
