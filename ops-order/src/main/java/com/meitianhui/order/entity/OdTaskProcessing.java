package com.meitianhui.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/***
 * 子任务实体类
 * 
 * @author 丁硕
 * @date 2017年3月8日
 */
public class OdTaskProcessing implements Serializable {

	private static final long serialVersionUID = -3431618821505849076L;

	private String processing_id; // 任务标识
	private String task_id; // 标识
	private String task_name; // 任务名称
	private String desc1; // 任务描述
	private String json_data; // 作业成果（配图文|十组|josn数据）
	private String payment_way_key; // 支付方式
	private BigDecimal amount; // 金额
	private Date expried_date; // 截止日期
	private Date submitted_date; // 提交时间
	private String member_type_key; // 会员分类
	private String member_id; // 提交人|会员标识
	private String member_info; // 提交人信息|会员信息（名称+账号）
	private Date audited_date; // 评审时间
	private String audited_by; // 评审人
	private String audited_result; // 评审结果，可选值：pass（合格）|fail（不合格）|reject（驳回|重新提交）
	private String status; // '状态，可选值：submitted（待提交）|audited（待审核）|settled（待结算）|aborted（已中止）|closed（已完成）
	private Date created_date; // 创建时间
	private Date modified_date; // 修改时间
	private String remark; // 备注

	public String getProcessing_id() {
		return processing_id;
	}

	public void setProcessing_id(String processing_id) {
		this.processing_id = processing_id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getJson_data() {
		return json_data;
	}

	public void setJson_data(String json_data) {
		this.json_data = json_data;
	}

	public String getPayment_way_key() {
		return payment_way_key;
	}

	public void setPayment_way_key(String payment_way_key) {
		this.payment_way_key = payment_way_key;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getExpried_date() {
		return expried_date;
	}

	public void setExpried_date(Date expried_date) {
		this.expried_date = expried_date;
	}

	public Date getSubmitted_date() {
		return submitted_date;
	}

	public void setSubmitted_date(Date submitted_date) {
		this.submitted_date = submitted_date;
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

	public String getMember_info() {
		return member_info;
	}

	public void setMember_info(String member_info) {
		this.member_info = member_info;
	}

	public Date getAudited_date() {
		return audited_date;
	}

	public void setAudited_date(Date audited_date) {
		this.audited_date = audited_date;
	}

	public String getAudited_by() {
		return audited_by;
	}

	public void setAudited_by(String audited_by) {
		this.audited_by = audited_by;
	}

	public String getAudited_result() {
		return audited_result;
	}

	public void setAudited_result(String audited_result) {
		this.audited_result = audited_result;
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
