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
public class PsSubOrder implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** 订单标识**/
	private String order_id;
	/** 订单编号**/
	private String order_no;
	/** 父订单标识**/
	private String parent_order_id;
	/** 邀请码**/
	private String invitation_code;
	/** 订单日期**/
	private Date order_date;
	/** 提货码**/
	private String loaded_code;
	/** 订单商品描述 **/
	private String desc1;
	/** 消费者留言 **/
	private String desc2;
	/** 会员分类，可选值：stores（便利店）**/
	private String member_type_key;
	/** 会员标识**/
	private String member_id;
	/** 会员信息**/
	private String member_info;
	/** 商品id**/
	private String goods_id;
	/** 商品编码**/
	private String goods_code;
	/** 门店id**/
	private String stores_id;
	/** 门店名称**/
	private String stores_name;
	/** 支付方式 **/
	private String payment_way_key;
	/** 商品数量 **/
	private Integer qty;
	/** 团购价格 **/
	private BigDecimal retail_price;
	/** 订单总金额 **/
	private BigDecimal total_fee;
	/** 联系人**/
	private String contact_person;
	/** 联系人电话**/
	private String contact_tel;
	/** 状态，可选值：normal（正常）delete(删除)**/
	private String status;
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
	public String getParent_order_id() {
		return parent_order_id;
	}
	public void setParent_order_id(String parent_order_id) {
		this.parent_order_id = parent_order_id;
	}
	public String getInvitation_code() {
		return invitation_code;
	}
	public void setInvitation_code(String invitation_code) {
		this.invitation_code = invitation_code;
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
	public String getDesc2() {
		return desc2;
	}
	public void setDesc2(String desc2) {
		this.desc2 = desc2;
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
	public String getPayment_way_key() {
		return payment_way_key;
	}
	public void setPayment_way_key(String payment_way_key) {
		this.payment_way_key = payment_way_key;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public BigDecimal getRetail_price() {
		return retail_price;
	}
	public void setRetail_price(BigDecimal retail_price) {
		this.retail_price = retail_price;
	}
	public BigDecimal getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
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
