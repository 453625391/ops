package com.meitianhui.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信对账单日志
 * 
 * @author Tiny
 *
 */
public class FDWechatBillLog implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 日志id **/
	private String log_id;
	/** 交易编号 **/
	private String trade_no;
	/** 商户交易编号 **/
	private String out_trade_no;
	/** 账单时间 **/
	private String bill_date;
	/** app类型 **/
	private String app_type;
	/** 描述 **/
	private String desc1;
	/** 交易时间 **/
	private Date trade_date;
	/** 交易账户 **/
	private String trade_account;
	/** 订单金额 **/
	private BigDecimal order_amount;
	/** 状态 **/
	private String status;
	/** 创建时间 **/
	private Date created_date;
	/** 修改时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	
	
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getBill_date() {
		return bill_date;
	}
	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}
	public String getApp_type() {
		return app_type;
	}
	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public Date getTrade_date() {
		return trade_date;
	}
	public void setTrade_date(Date trade_date) {
		this.trade_date = trade_date;
	}
	public String getTrade_account() {
		return trade_account;
	}
	public void setTrade_account(String trade_account) {
		this.trade_account = trade_account;
	}
	public BigDecimal getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(BigDecimal order_amount) {
		this.order_amount = order_amount;
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
