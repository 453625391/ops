package com.meitianhui.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 免费领样订单信息
 * 
 * @author Tiny
 *
 */
public class OdSettlement implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 结算标识 **/
	private String settlement_id;
	/** 数据来源 **/
	private String data_source;
	/** 结算日期 **/
	private Date settled_date;
	/** 供应商标识 **/
	private String supplier_id;
	/** 供应商名称 **/
	private String supplier;
	/** 结算金额 **/
	private BigDecimal amount;
	/** 结算金额 **/
	private Integer order_qty;
	/** 订单金额 **/
	private BigDecimal order_amount;
	/** 平台服务费 **/
	private BigDecimal commission_fee;
	/** 状态 **/
	private String status;
	/** 操作人 **/
	private String operator;
	/** 创建时间 **/
	private Date created_date;
	/** 更新时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	
	public String getSettlement_id() {
		return settlement_id;
	}
	public void setSettlement_id(String settlement_id) {
		this.settlement_id = settlement_id;
	}
	public String getData_source() {
		return data_source;
	}
	public void setData_source(String data_source) {
		this.data_source = data_source;
	}
	public Date getSettled_date() {
		return settled_date;
	}
	public void setSettled_date(Date settled_date) {
		this.settled_date = settled_date;
	}
	public String getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getOrder_qty() {
		return order_qty;
	}
	public void setOrder_qty(Integer order_qty) {
		this.order_qty = order_qty;
	}
	public BigDecimal getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(BigDecimal order_amount) {
		this.order_amount = order_amount;
	}
	public BigDecimal getCommission_fee() {
		return commission_fee;
	}
	public void setCommission_fee(BigDecimal commission_fee) {
		this.commission_fee = commission_fee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
