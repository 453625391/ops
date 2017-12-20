package com.meitianhui.member.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商信息
 * 
 * @author Tiny
 *
 */
public class MDSupplier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 供应商标识 **/
	private String supplier_id;
	/** 供应商编号 **/
	private String supplier_no;
	/** 供应商名称 **/
	private String supplier_name;
	/** 供应商业务类型，引用数据字典分类（GYSLB） **/
	private String business_type_key;
	/** 详细描述1 **/
	private String desc1;
	/** 详细描述2 **/
	private String desc2;
	/** 所在行政地区 **/
	private String area_id;
	/** 所在行政地区 **/
	private String area_desc;
	/** 详细地址 **/
	private String address;
	/** 联系人 **/
	private String contact_person;
	/** 联系电话 **/
	private String contact_tel;
	/** 邮箱 **/
	private String email;
	/** 法人代表 **/
	private String legal_person;
	/** 法人代表电话 **/
	private String legal_person_tel;
	/** 财务联系人 **/
	private String finance_contact;
	/** 财务联系电话 **/
	private String finance_contact_tel;
	/** 签约人 **/
	private String contractor;
	/** 企业性质，引用数据字典分类（GYSQYXZ） **/
	private String nature_business;
	/** 注册资金 **/
	private String registered_capital;
	/** 状态 **/
	private String sys_status;
	/** 注册时间 **/
	private Date registered_date;
	/** 标签 **/
	private String label;
	/** 法人身份证图片 **/
	private String id_card_pic_path;
	/**
	 * 企业三证扫描图片（1） 营业执照 Business license （2） 组织机构代码证 Organization code
	 * certificate （3） 税务登记证 Tax registration certificate
	 **/
	private String certification_pic_path;
	/** 许可证扫描图片 **/
	private String licence_pic_path;
	/** 商标图片 **/
	private String logo_pic_path;
	/** 装修前门头图片 **/
	private String old_facade_pic_path;
	/** 装修后门头图片 **/
	private String new_facade_pic_path;
	/** 装修前店内陈列照片 **/
	private String old_stores_pic_path;
	/** 装修后店内陈列照片 **/
	private String new_stores_pic_path;
	/** 创建时间 **/
	private Date created_date;
	/** 备注 **/
	private String remark;

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getSupplier_no() {
		return supplier_no;
	}

	public void setSupplier_no(String supplier_no) {
		this.supplier_no = supplier_no;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getBusiness_type_key() {
		return business_type_key;
	}

	public void setBusiness_type_key(String business_type_key) {
		this.business_type_key = business_type_key;
	}

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getDesc2() {
		return desc2;
	}

	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getArea_desc() {
		return area_desc;
	}

	public void setArea_desc(String area_desc) {
		this.area_desc = area_desc;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLegal_person() {
		return legal_person;
	}

	public void setLegal_person(String legal_person) {
		this.legal_person = legal_person;
	}

	public String getLegal_person_tel() {
		return legal_person_tel;
	}

	public void setLegal_person_tel(String legal_person_tel) {
		this.legal_person_tel = legal_person_tel;
	}

	public String getFinance_contact() {
		return finance_contact;
	}

	public void setFinance_contact(String finance_contact) {
		this.finance_contact = finance_contact;
	}

	public String getFinance_contact_tel() {
		return finance_contact_tel;
	}

	public void setFinance_contact_tel(String finance_contact_tel) {
		this.finance_contact_tel = finance_contact_tel;
	}

	public String getContractor() {
		return contractor;
	}

	public void setAgent_id(String contractor) {
		this.contractor = contractor;
	}

	public String getNature_business() {
		return nature_business;
	}

	public void setNature_business(String nature_business) {
		this.nature_business = nature_business;
	}

	public String getRegistered_capital() {
		return registered_capital;
	}

	public void setRegistered_capital(String registered_capital) {
		this.registered_capital = registered_capital;
	}

	public String getSys_status() {
		return sys_status;
	}

	public void setSys_status(String sys_status) {
		this.sys_status = sys_status;
	}

	public Date getRegistered_date() {
		return registered_date;
	}

	public void setRegistered_date(Date registered_date) {
		this.registered_date = registered_date;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getId_card_pic_path() {
		return id_card_pic_path;
	}

	public void setId_card_pic_path(String id_card_pic_path) {
		this.id_card_pic_path = id_card_pic_path;
	}

	public String getCertification_pic_path() {
		return certification_pic_path;
	}

	public void setCertification_pic_path(String certification_pic_path) {
		this.certification_pic_path = certification_pic_path;
	}

	public String getLicence_pic_path() {
		return licence_pic_path;
	}

	public void setLicence_pic_path(String licence_pic_path) {
		this.licence_pic_path = licence_pic_path;
	}

	public String getLogo_pic_path() {
		return logo_pic_path;
	}

	public void setLogo_pic_path(String logo_pic_path) {
		this.logo_pic_path = logo_pic_path;
	}

	public String getOld_facade_pic_path() {
		return old_facade_pic_path;
	}

	public void setOld_facade_pic_path(String old_facade_pic_path) {
		this.old_facade_pic_path = old_facade_pic_path;
	}

	public String getNew_facade_pic_path() {
		return new_facade_pic_path;
	}

	public void setNew_facade_pic_path(String new_facade_pic_path) {
		this.new_facade_pic_path = new_facade_pic_path;
	}

	public String getOld_stores_pic_path() {
		return old_stores_pic_path;
	}

	public void setOld_stores_pic_path(String old_stores_pic_path) {
		this.old_stores_pic_path = old_stores_pic_path;
	}

	public String getNew_stores_pic_path() {
		return new_stores_pic_path;
	}

	public void setNew_stores_pic_path(String new_stores_pic_path) {
		this.new_stores_pic_path = new_stores_pic_path;
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
