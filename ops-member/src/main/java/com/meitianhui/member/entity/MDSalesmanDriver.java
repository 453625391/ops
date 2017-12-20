package com.meitianhui.member.entity;

import java.io.Serializable;
import java.util.Date;

public class MDSalesmanDriver implements Serializable{

	private static final long serialVersionUID =1L;
	
	private String salesman_id;                	//'业务员标识'
	private String car_model;                	//'车型'
	private String car_number;                	//'车牌'
	private String capacity;                	//'载重'
	private String appearance_pic_path;         //'车辆外观'
	private String driving_license_pic_path;    //'驾驶证副本'
	private String driving_permit_pic_path;     //'行驶证副本'
	private String audit_status;                //'审批状态，可选值：non-audit（待审批）、pass（通过）、reject（驳回）'
	private Date created_date;                //'创建时间'
	private Date modified_date;               //'修改时间'
	private String remark;                		//'备注'
	public String getSalesman_id() {
		return salesman_id;
	}
	public void setSalesman_id(String salesman_id) {
		this.salesman_id = salesman_id;
	}
	public String getCar_model() {
		return car_model;
	}
	public void setCar_model(String car_model) {
		this.car_model = car_model;
	}
	public String getCar_number() {
		return car_number;
	}
	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getAppearance_pic_path() {
		return appearance_pic_path;
	}
	public void setAppearance_pic_path(String appearance_pic_path) {
		this.appearance_pic_path = appearance_pic_path;
	}
	public String getDriving_license_pic_path() {
		return driving_license_pic_path;
	}
	public void setDriving_license_pic_path(String driving_license_pic_path) {
		this.driving_license_pic_path = driving_license_pic_path;
	}
	public String getDriving_permit_pic_path() {
		return driving_permit_pic_path;
	}
	public void setDriving_permit_pic_path(String driving_permit_pic_path) {
		this.driving_permit_pic_path = driving_permit_pic_path;
	}
	public String getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(String audit_status) {
		this.audit_status = audit_status;
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
