package com.meitianhui.member.entity;

import java.io.Serializable;
import java.util.Date;

public class MDSalesman implements Serializable{

	private static final long serialVersionUID =1L;
	
	private String salesman_id;                 //'业务员标识'
	private String name;               			//'业务员姓名'
	private String nick_name;           		//'昵称'
	private String desc1;                       //'描述|简介'
	private String contact_tel;                 //'联系电话'
	private String sex_key;               		//'性别，引用数据字典分类（XBDM） '
	private String head_pic_path;               //'个人头像照片 '
	private String area_id;                     //'地区标识'
	private String address;                     //'详细地址'
	private Date registered_date;               //'注册时间'
	private String id_card;                 	//'身份证'
	private String id_card_pic_path;            //'实名认证照片'
	private String auth_status;                 //'实名认证状态，可选值：non-auth（未认证）、pass（已认证）、reject（驳回）'
	private String is_driver;                 	//'是否司机，Y|N'
	private String is_specialist;               //'是否地服，Y|N'
	private String is_guider;                 	//'是否导购，Y|N'
	private String is_buyer;                	//'是否买手，Y|N'
	private String is_operator;                 //'是否运营，Y|N'
	private String status;                 		//'状态，可选值：normal（正常） delete（删除）'
	private Date created_date;                	//'创建时间'
	private Date modified_date;               	//'修改时间'
	private String remark;                      //'备注'
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
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public String getContact_tel() {
		return contact_tel;
	}
	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}
	public String getSex_key() {
		return sex_key;
	}
	public void setSex_key(String sex_key) {
		this.sex_key = sex_key;
	}
	public String getHead_pic_path() {
		return head_pic_path;
	}
	public void setHead_pic_path(String head_pic_path) {
		this.head_pic_path = head_pic_path;
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
	public Date getRegistered_date() {
		return registered_date;
	}
	public void setRegistered_date(Date registered_date) {
		this.registered_date = registered_date;
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
	public String getIs_driver() {
		return is_driver;
	}
	public void setIs_driver(String is_driver) {
		this.is_driver = is_driver;
	}
	public String getIs_specialist() {
		return is_specialist;
	}
	public void setIs_specialist(String is_specialist) {
		this.is_specialist = is_specialist;
	}
	public String getIs_guider() {
		return is_guider;
	}
	public void setIs_guider(String is_guider) {
		this.is_guider = is_guider;
	}
	public String getIs_buyer() {
		return is_buyer;
	}
	public void setIs_buyer(String is_buyer) {
		this.is_buyer = is_buyer;
	}
	public String getIs_operator() {
		return is_operator;
	}
	public void setIs_operator(String is_operator) {
		this.is_operator = is_operator;
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
