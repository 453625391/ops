package com.meitianhui.goods.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 参与抽奖信息
 * @author Administrator
 *
 */
public class PlPartcipator implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 活动参与标识 **/
	private String partcipator_id;
	/** 活动标识 **/
	private String activity_id;
	/** 会员标识 **/
	private String member_id;
	/** 活动标识 **/
	private String member_info;
	/** 会员分类 **/
	private String member_type_key;
	/** 状态 **/
	private String status;
	/** 参加时间 **/
	private Date joined_date;
	/** 创建时间 **/
	private Date created_date;
	/** 修改时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	public String getPartcipator_id() {
		return partcipator_id;
	}
	public void setPartcipator_id(String partcipator_id) {
		this.partcipator_id = partcipator_id;
	}
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_info() {
		return member_info;
	}
	public void setMember_info(String member_info) {
		this.member_info = member_info;
	}
	public String getMember_type_key() {
		return member_type_key;
	}
	public void setMember_type_key(String member_type_key) {
		this.member_type_key = member_type_key;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getJoined_date() {
		return joined_date;
	}
	public void setJoined_date(Date joined_date) {
		this.joined_date = joined_date;
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
