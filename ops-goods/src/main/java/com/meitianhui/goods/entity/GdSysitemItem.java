package com.meitianhui.goods.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 标准商品
 * 
 * @author Tiny
 *
 */
public class GdSysitemItem implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 商品标识 **/
	private String item_id;
	/** 编码 **/
	private String item_code;
	/** 标题 **/
	private String title;
	/** 会员分类 **/
	private String member_type_key;
	/** 会员标识 **/
	private String member_id;
	/** 会员信息 **/
	private String member_info;
	/** 关键字 **/
	private String keywords;
	/** 分类标识 **/
	private String category_id;
	/** 品牌标识 **/
	private String brand_id;
	/** 条码 **/
	private String barcode;
	/** 描述 **/
	private String desc1;
	/** 图片信息 **/
	private String pic_path;
	/** 规格 **/
	private String specification;
	/** 包装 **/
	private String pack;
	/** 进价（成本价） **/
	private BigDecimal cost_price;
	/** 市场价 **/
	private BigDecimal market_price;
	/** vip 价格 **/
	private BigDecimal vip_price;
	/** 售价（礼券） **/
	private Integer market_price_voucher;
	/** 售价（金币） **/
	private Integer market_price_gold;
	/** 售价（积分） **/
	private Integer market_price_bonus;
	/** 产地 **/
	private String producer;
	/** 重量 **/
	private String weight;
	/** 库存量 **/
	private Integer stock_qty;
	/** 失效时间 **/
	private Date expired_date;
	/** 保质期 **/
	private String durability_period;
	/** 生产日期 **/
	private Date production_date;
	/** 供应商 **/
	private String supplier;
	/** 生产商 **/
	private String manufacturer;
	/** 排序 **/
	private String sort_order;
	/** 状态，可选值：pending(待定)onsell(上架)offsell(下架) **/
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getPic_path() {
		return pic_path;
	}
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
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
	public Integer getMarket_price_voucher() {
		return market_price_voucher;
	}
	public void setMarket_price_voucher(Integer market_price_voucher) {
		this.market_price_voucher = market_price_voucher;
	}
	public Integer getMarket_price_gold() {
		return market_price_gold;
	}
	public void setMarket_price_gold(Integer market_price_gold) {
		this.market_price_gold = market_price_gold;
	}
	public Integer getMarket_price_bonus() {
		return market_price_bonus;
	}
	public void setMarket_price_bonus(Integer market_price_bonus) {
		this.market_price_bonus = market_price_bonus;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public Integer getStock_qty() {
		return stock_qty;
	}
	public void setStock_qty(Integer stock_qty) {
		this.stock_qty = stock_qty;
	}
	public Date getExpired_date() {
		return expired_date;
	}
	public void setExpired_date(Date expired_date) {
		this.expired_date = expired_date;
	}
	public String getDurability_period() {
		return durability_period;
	}
	public void setDurability_period(String durability_period) {
		this.durability_period = durability_period;
	}
	public Date getProduction_date() {
		return production_date;
	}
	public void setProduction_date(Date production_date) {
		this.production_date = production_date;
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
	public String getSort_order() {
		return sort_order;
	}
	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
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
