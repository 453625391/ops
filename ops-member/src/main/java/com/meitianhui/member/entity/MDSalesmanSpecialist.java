package com.meitianhui.member.entity;

import java.io.Serializable;
import java.util.Date;

public class MDSalesmanSpecialist implements Serializable{

	private static final long serialVersionUID =1L;
	
	private String salesman_id;                	//'业务员标识'
	private String is_marketer;                	//'是否拓店，Y|N'
	private String is_assistant;                //'是否助教，Y|N'
	private String service_store_num;           //'服务门店数'
	private String self_store_num;              //'自营门店数'
	private String audit_status;               	//'审批状态，可选值：non-audit（待审批）、pass（通过）、reject（驳回）'
	private Date created_date;               	//'创建时间'
	private Date modified_date;               //'修改时间'
	private String remark;                		//'备注'
	public String getSalesman_id() {
		return salesman_id;
	}
	public void setSalesman_id(String salesman_id) {
		this.salesman_id = salesman_id;
	}
	public String getIs_marketer() {
		return is_marketer;
	}
	public void setIs_marketer(String is_marketer) {
		this.is_marketer = is_marketer;
	}
	public String getIs_assistant() {
		return is_assistant;
	}
	public void setIs_assistant(String is_assistant) {
		this.is_assistant = is_assistant;
	}
	public String getService_store_num() {
		return service_store_num;
	}
	public void setService_store_num(String service_store_num) {
		this.service_store_num = service_store_num;
	}
	public String getSelf_store_num() {
		return self_store_num;
	}
	public void setSelf_store_num(String self_store_num) {
		this.self_store_num = self_store_num;
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
