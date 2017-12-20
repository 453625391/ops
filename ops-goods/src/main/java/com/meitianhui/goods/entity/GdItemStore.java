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
public class GdItemStore implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 商家商品标识 **/
	private String item_store_id;
	/** 门店标识 **/
	private String store_id;
	/** 门店名称 **/
	private String store_info;
	/** 商品标识 **/
	private String item_id;
	/** 商品编码 **/
	private String item_code;
	/** 名称 **/
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
	/** 图片 **/
	private String image_info;
	/** 商品详情图片 **/
	private String image_detail;
	/** 规格 **/
	private String specification;
	/** 包装 **/
	private String pack;
	/** 进价(成本价) **/
	private BigDecimal cost_price;
	/** 售价 **/
	private BigDecimal market_price;
	/** 会员价 **/
	private BigDecimal vip_price;
	/** 返现 **/
	private BigDecimal rebate;
	/** 商品来源 **/
	private String product_source;
	/** 产地 **/
	private String producer;
	/** 商品的重量，以千克为单位 **/
	private String weight;
	/** 可销售库存数量 **/
	private Integer sale_qty;
	/** 库存数量 **/
	private Integer stock_qty;
	/** 保质期 **/
	private String durability_period;
	/** 生产日期 **/
	private Date production_date;
	/** 供应商 **/
	private String supplier;
	/** 生产商 **/
	private String manufacturer;
	/** 是否展示,Y(是)N(否) **/
	private String is_show;
	/** 是否开放销售，Y(是)N(否) **/
	private String is_sell;
	/** 是否支付礼券兑换,Y(是)N(否) **/
	private String is_exchange;
	/** 是否为虚拟商品,Y(是)N(否) **/
	private String is_virtual;
	/** 是否是精品,Y(是)N(否) **/
	private String is_best;
	/** 否是新品,Y(是)N(否) **/
	private String is_new;
	/** 是否热销,Y(是)N(否) **/
	private String is_hot;
	/** 是否特价促销,Y(是)N(否) **/
	private String is_activity;
	/** 是否跟踪库存，Y（是）N（否） **/
	private String is_track_stock;
	/** 库存预警  **/
	private Integer stock_warning;
	/** 是否允计折扣，Y（是）N（否） **/
	private String is_discount;
	/** 停止销售，Y（是）N（否） **/
	private String is_offline;
	/** 称重计量，Y（是）N（否）**/
	private String is_weighed;
	/** 是否推荐，Y（是）N（否）**/
	private String is_recommend;
	/** 临期预警 **/
	private String expired_warning;
	/** 状态，可选值：normal（正常）delete(删除) **/
	private String status;
	/** 创建时间 **/
	private Date created_date;
	/** 更新时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	public String getItem_store_id() {
		return item_store_id;
	}
	public void setItem_store_id(String item_store_id) {
		this.item_store_id = item_store_id;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getStore_info() {
		return store_info;
	}
	public void setStore_info(String store_info) {
		this.store_info = store_info;
	}
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
	public String getImage_info() {
		return image_info;
	}
	public void setImage_info(String image_info) {
		this.image_info = image_info;
	}
	public String getImage_detail() {
		return image_detail;
	}
	public void setImage_detail(String image_detail) {
		this.image_detail = image_detail;
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
	public BigDecimal getRebate() {
		return rebate;
	}
	public void setRebate(BigDecimal rebate) {
		this.rebate = rebate;
	}
	public String getProduct_source() {
		return product_source;
	}
	public void setProduct_source(String product_source) {
		this.product_source = product_source;
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
	public String getIs_show() {
		return is_show;
	}
	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}
	public String getIs_sell() {
		return is_sell;
	}
	public void setIs_sell(String is_sell) {
		this.is_sell = is_sell;
	}
	public String getIs_exchange() {
		return is_exchange;
	}
	public void setIs_exchange(String is_exchange) {
		this.is_exchange = is_exchange;
	}
	public String getIs_virtual() {
		return is_virtual;
	}
	public void setIs_virtual(String is_virtual) {
		this.is_virtual = is_virtual;
	}
	public String getIs_best() {
		return is_best;
	}
	public void setIs_best(String is_best) {
		this.is_best = is_best;
	}
	public String getIs_new() {
		return is_new;
	}
	public void setIs_new(String is_new) {
		this.is_new = is_new;
	}
	public String getIs_hot() {
		return is_hot;
	}
	public void setIs_hot(String is_hot) {
		this.is_hot = is_hot;
	}
	public String getIs_activity() {
		return is_activity;
	}
	public void setIs_activity(String is_activity) {
		this.is_activity = is_activity;
	}
	public String getIs_track_stock() {
		return is_track_stock;
	}
	public void setIs_track_stock(String is_track_stock) {
		this.is_track_stock = is_track_stock;
	}
	public Integer getStock_warning() {
		return stock_warning;
	}
	public void setStock_warning(Integer stock_warning) {
		this.stock_warning = stock_warning;
	}
	public String getIs_discount() {
		return is_discount;
	}
	public void setIs_discount(String is_discount) {
		this.is_discount = is_discount;
	}
	public String getIs_offline() {
		return is_offline;
	}
	public void setIs_offline(String is_offline) {
		this.is_offline = is_offline;
	}
	public String getIs_weighed() {
		return is_weighed;
	}
	public void setIs_weighed(String is_weighed) {
		this.is_weighed = is_weighed;
	}
	public String getIs_recommend() {
		return is_recommend;
	}
	public void setIs_recommend(String is_recommend) {
		this.is_recommend = is_recommend;
	}
	public String getExpired_warning() {
		return expired_warning;
	}
	public void setExpired_warning(String expired_warning) {
		this.expired_warning = expired_warning;
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
