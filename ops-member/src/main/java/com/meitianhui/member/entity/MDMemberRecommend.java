package com.meitianhui.member.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户注册推荐信息
 * 
 * @author Tiny
 *
 */
public class MDMemberRecommend implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 推荐标识 **/
	private String recommend_id;
	/** 推荐人身份，consumer（消费者）、stores（加盟/联盟商）、supplier（供应商） **/
	private String reference_type_key;
	/** 推荐人标识 **/
	private String reference_id;
	/** 会员身份，consumer（消费者）、stores（加盟/联盟商）、supplier（供应商） **/
	private String member_type_key;
	/** 会员标识 **/
	private String member_id;
	/** 会员手机号 **/
	private String member_mobile;
	/** 会员账号标识 **/
	private String member_user_id;
	/** 注册时间 **/
	private Date created_date;
	/** 状态 **/
	private String remark;
	
	public String getRecommend_id() {
		return recommend_id;
	}
	public void setRecommend_id(String recommend_id) {
		this.recommend_id = recommend_id;
	}
	public String getReference_type_key() {
		return reference_type_key;
	}
	public void setReference_type_key(String reference_type_key) {
		this.reference_type_key = reference_type_key;
	}
	public String getReference_id() {
		return reference_id;
	}
	public void setReference_id(String reference_id) {
		this.reference_id = reference_id;
	}
	public String getMember_type_key() {
		return member_type_key;
	}
	public void setMember_type_key(String member_type_key) {
		this.member_type_key = member_type_key;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_mobile() {
		return member_mobile;
	}
	public void setMember_mobile(String member_mobile) {
		this.member_mobile = member_mobile;
	}
	public String getMember_user_id() {
		return member_user_id;
	}
	public void setMember_user_id(String member_user_id) {
		this.member_user_id = member_user_id;
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
