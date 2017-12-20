package com.meitianhui.goods.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品信息信息
 * 
 * @author Tiny
 *
 */
public class GdItem implements Serializable {

	
	private static final long serialVersionUID = 1L;
	/** 标准商品标识**/
	private String item_id;
	/** 编码**/
	private String item_code;
	/** 名称**/
	private String item_name;
	/** 商品关键字 **/
	private String keywords;
	/** 分类标识 **/
	private String category_id;
	/** 品牌标识 **/
	private String brand_id;
	/** 条码 **/
	private String barcode;
	/** 详细描述 **/
	private String desc1;
	/** 规格**/
	private String specification;
	/** 包装**/
	private String pack;
	/** 进价(成本价)**/
	private BigDecimal cost_price;
	/** 售价**/
	private BigDecimal market_price;
	/** 会员价**/
	private BigDecimal vip_price;
	/** 产地**/
	private String producer;
	/** 供应商**/
	private String supplier;
	/** 生产商**/
	private String manufacturer;
	/** 状态，可选值：normal（正常）delete(删除)**/
	private String status;
	/** 创建时间 **/
	private Date created_date;
	/** 备注 **/
	private String remark;
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(String brand_id) {
		this.brand_id = brand_id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getPack() {
		return pack;
	}
	public void setPack(String pack) {
		this.pack = pack;
	}
	public BigDecimal getCost_price() {
		return cost_price;
	}
	public void setCost_price(BigDecimal cost_price) {
		this.cost_price = cost_price;
	}
	public BigDecimal getMarket_price() {
		return market_price;
	}
	public void setMarket_price(BigDecimal market_price) {
		this.market_price = market_price;
	}
	public BigDecimal getVip_price() {
		return vip_price;
	}
	public void setVip_price(BigDecimal vip_price) {
		this.vip_price = vip_price;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
