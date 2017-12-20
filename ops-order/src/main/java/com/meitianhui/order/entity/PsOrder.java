package com.meitianhui.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单信息
 * 
 * @author Tiny
 *
 */
public class PsOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 订单标识**/
	private String order_id;
	/** 订单编号**/
	private String order_no;
	/** 订单日期**/
	private Date order_date;
	/** 提货码**/
	private String loaded_code;
	/** 订单类型**/
	private String order_type;
	/** 订单商品描述 **/
	private String desc1;
	/** 订单其它描述 **/
	private String desc2;
	/** 订单商品数量 **/
	private Integer item_num;
	/** 支付方式 **/
	private String payment_way_key;
	/** 供应商id **/
	private String supplier_id;
	/** 供应商名称 **/
	private String supplier;
	/** 仓位id **/
	private String warehouse_id;
	/** 仓位名称 **/
	private String warehouse;
	/** 订单总金额 **/
	private BigDecimal total_fee;
	/** 订单销售总金额 **/
	private BigDecimal discount_fee;
	/** 销售价格 **/
	private BigDecimal sale_fee;
	/** 配送费 **/
	private BigDecimal delivery_fee;
	/** 会员分类，可选值：stores（便利店）、supplier（供应商）、partner（联盟商）**/
	private String member_type_key;
	/** 会员标识**/
	private String member_id;
	/** 会员标识**/
	private String member_info;
	/** 配送地址**/
	private String delivery_address;
	/** 联系人**/
	private String contact_person;
	/** 联系人电话**/
	private String contact_tel;
	/** 物流信息**/
	private String logistics;
	/** 状态，可选值：normal（正常）delete(删除)**/
	private String status;
	/**结算状态**/
	private String settle_status;
	/** 创建时间 **/
	private Date created_date;
	/** 更新时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	/** 运营系统备注 **/
	private String biz_remark;
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
	public Date getOrder_date() {
		return order_date;
	}
	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}
	public String getLoaded_code() {
		return loaded_code;
	}
	public void setLoaded_code(String loaded_code) {
		this.loaded_code = loaded_code;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public String getDesc2() {
		return desc2;
	}
	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}
	public Integer getItem_num() {
		return item_num;
	}
	public void setItem_num(Integer item_num) {
		this.item_num = item_num;
	}
	public String getPayment_way_key() {
		return payment_way_key;
	}
	public void setPayment_way_key(String payment_way_key) {
		this.payment_way_key = payment_way_key;
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
	public String getWarehouse_id() {
		return warehouse_id;
	}
	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public BigDecimal getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
	}
	public BigDecimal getDelivery_fee() {
		return delivery_fee;
	}
	public void setDelivery_fee(BigDecimal delivery_fee) {
		this.delivery_fee = delivery_fee;
	}
	public BigDecimal getDiscount_fee() {
		return discount_fee;
	}
	public void setDiscount_fee(BigDecimal discount_fee) {
		this.discount_fee = discount_fee;
	}
	public BigDecimal getSale_fee() {
		return sale_fee;
	}
	public void setSale_fee(BigDecimal sale_fee) {
		this.sale_fee = sale_fee;
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
	public String getDelivery_address() {
		return delivery_address;
	}
	public void setDelivery_address(String delivery_address) {
		this.delivery_address = delivery_address;
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
	public String getBiz_remark() {
		return biz_remark;
	}
	public void setBiz_remark(String biz_remark) {
		this.biz_remark = biz_remark;
	}
	
	
}
