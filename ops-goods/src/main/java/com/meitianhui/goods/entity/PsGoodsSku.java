package com.meitianhui.goods.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预售商品SKU信息
 * 
 * @author Tiny
 *
 */
public class PsGoodsSku implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 货品SKU标识 **/
	private String goods_stock_id;
	/** 货品标识 **/
	private String goods_id;
	/** SKU **/
	private String sku;
	/** 描述 **/
	private String desc1;
	/** 进价（成本价） **/
	private BigDecimal cost_price;
	/** 卖价 **/
	private BigDecimal sales_price;
	/** 可销售库存量 **/
	private Integer sale_qty;
	/** 库存量 **/
	private Integer stock_qty;
	/** 状态，可选值：normal（正常）delete(删除) **/
	private String status;
	/** 创建时间 **/
	private Date created_date;
	/** 修改时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	
	public String getGoods_stock_id() {
		return goods_stock_id;
	}
	public void setGoods_stock_id(String goods_stock_id) {
		this.goods_stock_id = goods_stock_id;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public BigDecimal getCost_price() {
		return cost_price;
	}
	public void setCost_price(BigDecimal cost_price) {
		this.cost_price = cost_price;
	}
	public BigDecimal getSales_price() {
		return sales_price;
	}
	public void setSales_price(BigDecimal sales_price) {
		this.sales_price = sales_price;
	}
	public Integer getSale_qty() {
		return sale_qty;
	}
	public void setSale_qty(Integer sale_qty) {
		this.sale_qty = sale_qty;
	}
	public Integer getStock_qty() {
		return stock_qty;
	}
	public void setStock_qty(Integer stock_qty) {
		this.stock_qty = stock_qty;
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
