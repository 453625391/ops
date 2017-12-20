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
public class PcOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 订单标识 **/
	private String order_id;
	/** 订单编号 **/
	private String order_no;
	/** 订单日期 **/
	private Date order_date;
	/** 提货码**/
	private String loaded_code;
	/** 订单商品描述 **/
	private String desc1;
	/** 便利店会员id **/
	private String stores_id;
	/** 便利店名称 **/
	private String stores_name;
	/** 消费者会员id **/
	private String consumer_id;
	/** 订单商品数量 **/
	private Integer item_num;
	/** 支付方式 **/
	private String payment_way_key;
	/** 销售价格 **/
	private BigDecimal sale_fee;
	/** 订单销售总金额 **/
	private BigDecimal discount_fee;
	/** 订单总金额 **/
	private BigDecimal total_fee;
	/** 返现金额 **/
	private BigDecimal rebate_fee;
	/** 配送地址 **/
	private String delivery_address;
	/** 联系人 **/
	private String contact_person;
	/** 联系人电话 **/
	private String contact_tel;
	/** 物流信息 **/
	private String logistics;
	/** 状态，可选值：normal（正常）delete(删除) **/
	private String status;
	/** 发货时间 **/
	private Date delivery_date;
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

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getStores_id() {
		return stores_id;
	}

	public void setStores_id(String stores_id) {
		this.stores_id = stores_id;
	}

	public String getStores_name() {
		return stores_name;
	}

	public void setStores_name(String stores_name) {
		this.stores_name = stores_name;
	}

	public String getConsumer_id() {
		return consumer_id;
	}

	public void setConsumer_id(String consumer_id) {
		this.consumer_id = consumer_id;
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

	public BigDecimal getRebate_fee() {
		return rebate_fee;
	}

	public void setRebate_fee(BigDecimal rebate_fee) {
		this.rebate_fee = rebate_fee;
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

	public Date getDelivery_date() {
		return delivery_date;
	}

	public void setDelivery_date(Date delivery_date) {
		this.delivery_date = delivery_date;
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
