package com.meitianhui.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员资产操作日志
 * 
 * @author Tiny
 *
 */
public class FDMemberVoucherLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 交易标识 **/
	private String log_id;
	/** 会员标识 **/
	private String member_id;
	/** 会员类型 **/
	private String member_type_key ;
	/** 操作，income（入账）、expenditure（出账）、frozen（冻结）、activated（解冻）**/
	private String category;
	/** 交易前值**/
	private BigDecimal pre_balance;
	/** 交易值 **/
	private BigDecimal amount;
	/** 交易后值 **/
	private BigDecimal balance;
	/** 发生时间 **/
	private Date tracked_date;
	/** 备注 **/
	private String remark;
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_type_key() {
		return member_type_key;
	}
	public void setMember_type_key(String member_type_key) {
		this.member_type_key = member_type_key;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public BigDecimal getPre_balance() {
		return pre_balance;
	}
	public void setPre_balance(BigDecimal pre_balance) {
		this.pre_balance = pre_balance;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public Date getTracked_date() {
		return tracked_date;
	}
	public void setTracked_date(Date tracked_date) {
		this.tracked_date = tracked_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
