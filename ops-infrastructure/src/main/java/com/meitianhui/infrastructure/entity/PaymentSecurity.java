package com.meitianhui.infrastructure.entity;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 支付密码安全信息
 * @author mole.wang 2016年1月8日
 *
 */
public class PaymentSecurity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4002034308172750365L;
	private String user_id           ;//用户标识', 
	private String payment_password  ;//支付密码', 
	private String small_direct      ;//小额免密，可选值：Y（是）N（否）', 
	private BigDecimal small_direct_amount ;  //最大免密支付额度
	private String sms_notify        ;//短信通知，可选值：Y（是）N（否）', 
	private String created_date      ;//创建时间', 
	private String remark            ;//备注' 
	
	public BigDecimal getSmall_direct_amount() {
		return small_direct_amount;
	}
	public void setSmall_direct_amount(BigDecimal small_direct_amount) {
		this.small_direct_amount = small_direct_amount;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPayment_password() {
		return payment_password;
	}
	public void setPayment_password(String payment_password) {
		this.payment_password = payment_password;
	}
	public String getSmall_direct() {
		return small_direct;
	}
	public void setSmall_direct(String small_direct) {
		this.small_direct = small_direct;
	}
	public String getSms_notify() {
		return sms_notify;
	}
	public void setSms_notify(String sms_notify) {
		this.sms_notify = sms_notify;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
