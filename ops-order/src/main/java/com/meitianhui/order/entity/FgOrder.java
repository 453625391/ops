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
public class FgOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 订单标识**/
	private String order_id;
	/** 订单编号**/
	private String order_no;
	/** 订单类型**/
	private String order_type;
	/** 订单日期**/
	private Date order_date;
	/** 会员分类，可选值：stores（便利店）、supplier（供应商）、partner（联盟商）**/
	private String member_type_key;
	/** 会员标识**/
	private String member_id;
	/** 会员信息**/
	private String member_info;
	/** 会员手机号码**/
	private String member_mobile;
	/** 订单商品描述 **/
	private String desc1;
	/** 订单商品数量 **/
	private Integer item_num;
	/** 支付方式 **/
	private String payment_way_key;
	/** 销售价格 **/
	private BigDecimal sale_fee;
	/** 订单优惠总金额 **/
	private BigDecimal discount_fee;
	/** 订单总金额 **/
	private BigDecimal total_fee;
	/** 配送地址**/
	private String delivery_address;
	/** 联系人**/
	private String contact_person;
	/** 联系人电话**/
	private String contact_tel;
	/** 物流信息**/
	private String logistics;
	/** 淘宝订单号**/
	private String external_order_no;
	/** 淘宝买家名称**/
	private String external_buyer_name;
	/** 淘宝买家支付宝账号**/
	private String external_buyer_pay_no;
	/** 数据来源 **/
	private String data_source;
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
	/** 商家备注 **/
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
	public String getMember_mobile() {
		return member_mobile;
	}
	public void setMember_mobile(String member_mobile) {
		this.member_mobile = member_mobile;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
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
	public String getExternal_order_no() {
		return external_order_no;
	}
	public void setExternal_order_no(String external_order_no) {
		this.external_order_no = external_order_no;
	}
	public String getExternal_buyer_name() {
		return external_buyer_name;
	}
	public void setExternal_buyer_name(String external_buyer_name) {
		this.external_buyer_name = external_buyer_name;
	}
	public String getExternal_buyer_pay_no() {
		return external_buyer_pay_no;
	}
	public void setExternal_buyer_pay_no(String external_buyer_pay_no) {
		this.external_buyer_pay_no = external_buyer_pay_no;
	}
	public String getData_source() {
		return data_source;
	}
	public void setData_source(String data_source) {
		this.data_source = data_source;
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
