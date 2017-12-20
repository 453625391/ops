package com.meitianhui.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 增值服务订单信息
 * 
 * @author Tiny
 *
 */
public class CsOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 订单标识 **/
	private String order_id;
	/** 订单编号 **/
	private String order_no;
	/** 订单类型 **/
	private String order_type;
	/** 订单日期 **/
	private Date order_date;
	/** 商品标识 **/
	private String goods_id;
	/** 商品编码 **/
	private String goods_code;
	/** 商品标题 **/
	private String goods_title;
	/** 货品图片信息，JSON格式，{[title=原始图,path_id:xxxxx1],[title=缩小图,path_id:xxxxx2]} **/
	private String goods_pic_info;
	/** 供应商标识 **/
	private String supplier_id;
	/** 供应商标识 **/
	private String supplier;
	/** 数量 **/
	private Integer qty;
	/** 单位 **/
	private String goods_unit;
	/** 支付方式 **/
	private String payment_way_key;
	/** 销售价格 **/
	private BigDecimal sale_fee;
	/** 订单优惠总金额 **/
	private BigDecimal discount_fee;
	/** 订单总金额 **/
	private BigDecimal total_fee;
	/** 结算费 **/
	private BigDecimal settled_price ;
	/** 服务费 **/
	private BigDecimal service_fee;
	/** 会员分类，可选值：stores（便利店）、supplier（供应商）、partner（联盟商）**/
	private String member_type_key;
	/** 会员标识**/
	private String member_id;
	/** 会员信息**/
	private String member_info;
	/** 联系人**/
	private String contact_person;
	/** 联系人电话**/
	private String contact_tel;
	/** 物流信息**/
	private String logistics;
	/** 收货地址**/
	private String delivery_address;
	/** 状态，可选值：normal（正常）delete(删除) **/
	private String status;
	/** 结算状态 **/
	private String settle_status;
	/** 创建时间 **/
	private Date created_date;
	/** 更新时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public Date getOrder_date() {
		return order_date;
	}
	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_code() {
		return goods_code;
	}
	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}
	public String getGoods_title() {
		return goods_title;
	}
	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}
	public String getGoods_pic_info() {
		return goods_pic_info;
	}
	public void setGoods_pic_info(String goods_pic_info) {
		this.goods_pic_info = goods_pic_info;
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
	public String getGoods_unit() {
		return goods_unit;
	}
	public void setGoods_unit(String goods_unit) {
		this.goods_unit = goods_unit;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getPayment_way_key() {
		return payment_way_key;
	}
	public void setPayment_way_key(String payment_way_key) {
		this.payment_way_key = payment_way_key;
	}
	public BigDecimal getSale_fee() {
		return sale_fee;
	}
	public void setSale_fee(BigDecimal sale_fee) {
		this.sale_fee = sale_fee;
	}
	public BigDecimal getDiscount_fee() {
		return discount_fee;
	}
	public void setDiscount_fee(BigDecimal discount_fee) {
		this.discount_fee = discount_fee;
	}
	public BigDecimal getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
	}
	public BigDecimal getSettled_price() {
		return settled_price;
	}
	public void setSettled_price(BigDecimal settled_price) {
		this.settled_price = settled_price;
	}
	public BigDecimal getService_fee() {
		return service_fee;
	}
	public void setService_fee(BigDecimal service_fee) {
		this.service_fee = service_fee;
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
	public String getContact_person() {
		return contact_person;
	}
	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}
	public String getContact_tel() {
		return contact_tel;
	}
	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}
	public String getLogistics() {
		return logistics;
	}
	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}
	public String getDelivery_address() {
		return delivery_address;
	}
	public void setDelivery_address(String delivery_address) {
		this.delivery_address = delivery_address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSettle_status() {
		return settle_status;
	}
	public void setSettle_status(String settle_status) {
		this.settle_status = settle_status;
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
