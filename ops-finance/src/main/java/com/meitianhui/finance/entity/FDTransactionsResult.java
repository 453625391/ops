package com.meitianhui.finance.entity;

import java.util.Date;

/**
 * 交易结果表
 * 
 * @author Tiny
 *
 */
public class FDTransactionsResult extends FDTransactions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 交易凭证 **/
	private String transaction_body;
	/** 买家账户类型 **/
	private String buyer_member_type;
	/** 买方资金账号 **/
	private String buyer_account_no;
	/** 买方账号名称 **/
	private String buyer_account_name;
	/** 买方名称 **/
	private String buyer_name;
	/** 买方手机号 **/
	private String buyer_contact ;
	/** 卖家账户类型 **/
	private String seller_member_type;
	/** 卖家资金账号 **/
	private String seller_account_no;
	/** 卖家账号名称 **/
	private String seller_account_name;
	/** 卖家名称 **/
	private String seller_name;
	/** 卖家手机号 **/
	private String seller_contact;
	/** 交易结果 **/
	private String transaction_status;
	/** 交易关闭时间 **/
	private Date closed_date;
	/** 备注 **/
	private String remark;
	
	public String getTransaction_body() {
		return transaction_body;
	}
	public void setTransaction_body(String transaction_body) {
		this.transaction_body = transaction_body;
	}
	public String getBuyer_member_type() {
		return buyer_member_type;
	}
	public void setBuyer_member_type(String buyer_member_type) {
		this.buyer_member_type = buyer_member_type;
	}
	public String getBuyer_account_no() {
		return buyer_account_no;
	}
	public void setBuyer_account_no(String buyer_account_no) {
		this.buyer_account_no = buyer_account_no;
	}
	public String getBuyer_account_name() {
		return buyer_account_name;
	}
	public void setBuyer_account_name(String buyer_account_name) {
		this.buyer_account_name = buyer_account_name;
	}
	public String getBuyer_name() {
		return buyer_name;
	}
	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}
	public String getBuyer_contact() {
		return buyer_contact;
	}
	public void setBuyer_contact(String buyer_contact) {
		this.buyer_contact = buyer_contact;
	}
	public String getSeller_member_type() {
		return seller_member_type;
	}
	public void setSeller_member_type(String seller_member_type) {
		this.seller_member_type = seller_member_type;
	}
	public String getSeller_account_no() {
		return seller_account_no;
	}
	public void setSeller_account_no(String seller_account_no) {
		this.seller_account_no = seller_account_no;
	}
	public String getSeller_account_name() {
		return seller_account_name;
	}
	public void setSeller_account_name(String seller_account_name) {
		this.seller_account_name = seller_account_name;
	}
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
	public String getSeller_contact() {
		return seller_contact;
	}
	public void setSeller_contact(String seller_contact) {
		this.seller_contact = seller_contact;
	}
	public String getTransaction_status() {
		return transaction_status;
	}
	public void setTransaction_status(String transaction_status) {
		this.transaction_status = transaction_status;
	}
	public Date getClosed_date() {
		return closed_date;
	}
	public void setClosed_date(Date closed_date) {
		this.closed_date = closed_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
