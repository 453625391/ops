package com.meitianhui.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 门店佣金日志记录
 * 
 * @author dingren
 *
 */
public class FDCashCommissionLog implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 日志标识 **/
	private String log_id;
	/** 门店标识 **/
	private String store_id;
	/** 佣金金额 **/
	private BigDecimal commission_cash;
	/** 记账方向，可选值：income（结算）、expenditure（提现） **/
	private String booking_mark;
	/** 创建日期 **/
	private Date created_date;
	/** 备注 **/
	private String remark;
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public BigDecimal getCommission_cash() {
		return commission_cash;
	}
	public void setCommission_cash(BigDecimal commission_cash) {
		this.commission_cash = commission_cash;
	}
	public String getBooking_mark() {
		return booking_mark;
	}
	public void setBooking_mark(String booking_mark) {
		this.booking_mark = booking_mark;
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
