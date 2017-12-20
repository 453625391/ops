package com.meitianhui.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 伙拼团订单
 * 
 * @ClassName: TsOrder
 * @author tiny
 * @date 2017年2月27日 下午4:23:07
 *
 */
public class TsOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 订单标识 **/
	private String order_id;
	/** 活动标识 **/
	private String activity_id;
	/** 订单编号 **/
	private String order_no;
	/** 订单日期 **/
	private Date order_date;
	/** 提货码**/
	private String loaded_code;
	/** 订单商品描述 **/
	private String desc1;
	/** 订单其它描述（留言） **/
	private String desc2;
	/** 数量 **/
	private Integer item_num;
	/** 支付方式 **/
	private String payment_way_key;
	/** 预付款 **/
	private BigDecimal deposit_fee;
	/** 销售价格(最后成团价格) **/
	private BigDecimal sale_price;
	/** 订单总金额 **/
	private BigDecimal total_fee;
	/** 会员分类，可选值：consumer（消费者） **/
	private String member_type_key;
	/** 会员标识 **/
	private String member_id;
	/** 会员信息 **/
	private String member_info;
	/** 收货人信息' **/
	private String consignee_json_data;
	/**
	 * 状态，可选值：normal（空）、paid（已支付）、deliveried(已发货),arrived(已到货) ,received（已收货）、cancelled（已取消）、refunded（已返款）
	 **/
	private String status;
	/** 结算状态，可选值：pending（待结算）、settled（已结算），paid（已付款） **/
	private String settle_status;
	/** 创建时间 **/
	private Date created_date;
	/** 更新时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	/** 业务备注 **/
	private String biz_remark;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
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

	public BigDecimal getDeposit_fee() {
		return deposit_fee;
	}

	public void setDeposit_fee(BigDecimal deposit_fee) {
		this.deposit_fee = deposit_fee;
	}

	public BigDecimal getSale_price() {
		return sale_price;
	}

	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	public BigDecimal getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
	}

	public String getPayment_way_key() {
		return payment_way_key;
	}

	public void setPayment_way_key(String payment_way_key) {
		this.payment_way_key = payment_way_key;
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

	public String getConsignee_json_data() {
		return consignee_json_data;
	}

	public void setConsignee_json_data(String consignee_json_data) {
		this.consignee_json_data = consignee_json_data;
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
