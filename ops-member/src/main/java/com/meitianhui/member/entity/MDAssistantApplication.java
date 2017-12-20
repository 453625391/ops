package com.meitianhui.member.entity;

import java.io.Serializable;
import java.util.Date;

public class MDAssistantApplication implements Serializable{

	private static final long serialVersionUID =1L;
	
	private String application_id;                	//'申请单标识'
	private String stores_id;                		//'门店标识'
	private String assistant_id;                	//'业务员（助教）标识'
	private String audit_status;                	//'审批状态，可选值：non-audit（待审批）、audit（通过）、reject（驳回）'
	private Date created_date;                		//'创建时间'
	private Date modified_date;                		//'修改时间'
	private String remark;                			//'认证记录标识'
	public String getApplication_id() {
		return application_id;
	}
	public void setApplication_id(String application_id) {
		this.application_id = application_id;
	}
	public String getStores_id() {
		return stores_id;
	}
	public void setStores_id(String stores_id) {
		this.stores_id = stores_id;
	}
	public String getAssistant_id() {
		return assistant_id;
	}
	public void setAssistant_id(String assistant_id) {
		this.assistant_id = assistant_id;
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
