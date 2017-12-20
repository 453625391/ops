package com.meitianhui.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 门店佣金比率
 * 
 * @author dingren
 *
 */
public class FDCashCommissionRate implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 起始日期  **/
	private String date_from;
	/** 结束日期  **/
	private String date_to;
	/** 佣金比率  **/
	private BigDecimal commission_rate;
	/** 创建日期 **/
	private Date created_date;
	/** 创建人 **/
	private String created_by;
	public String getDate_from() {
		return date_from;
	}
	public void setDate_from(String date_from) {
		this.date_from = date_from;
	}
	public String getDate_to() {
		return date_to;
	}
	public void setDate_to(String date_to) {
		this.date_to = date_to;
	}
	public BigDecimal getCommission_rate() {
		return commission_rate;
	}
	public void setCommission_rate(BigDecimal commission_rate) {
		this.commission_rate = commission_rate;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
}
