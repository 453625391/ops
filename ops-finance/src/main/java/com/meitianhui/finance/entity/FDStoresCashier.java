package com.meitianhui.finance.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 加盟店收银信息
 * 
 * @author Tiny
 *
 */
public class FDStoresCashier implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 收银流水id**/
	private String flow_id;
	/** 收银交易号 **/
	private String flow_no;
	/** 加盟店会员 **/
	private String stores_id;
	/** 应收现金**/
	private BigDecimal amount;
	/** 抵扣（优惠）现金 **/
	private BigDecimal discount_amount;
	/** 减免现金 **/
	private BigDecimal reduce_amount;
	/** 返现金 **/
	private BigDecimal rebate_cash;
	/** 送金币 **/
	private Integer reward_gold;
	/** 支付金额 **/
	private BigDecimal pay_amount;
	/** 支付方式 **/
	private String payment_way_key;
	/** 收银数据 **/
	private String json_data;
	/** 收银员(用户id)**/
	private String cashier_id;
	/** 记录时间 **/
	private Date created_date;
	/** 备注 **/
	private String remark;
	
	public String getFlow_id() {
		return flow_id;
	}
	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}
	public String getFlow_no() {
		return flow_no;
	}
	public void setFlow_no(String flow_no) {
		this.flow_no = flow_no;
	}
	public String getStores_id() {
		return stores_id;
	}
	public void setStores_id(String stores_id) {
		this.stores_id = stores_id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getDiscount_amount() {
		return discount_amount;
	}
	public void setDiscount_amount(BigDecimal discount_amount) {
		this.discount_amount = discount_amount;
	}
	public BigDecimal getReduce_amount() {
		return reduce_amount;
	}
	public void setReduce_amount(BigDecimal reduce_amount) {
		this.reduce_amount = reduce_amount;
	}
	public BigDecimal getRebate_cash() {
		return rebate_cash;
	}
	public void setRebate_cash(BigDecimal rebate_cash) {
		this.rebate_cash = rebate_cash;
	}
	public Integer getReward_gold() {
		return reward_gold;
	}
	public void setReward_gold(Integer reward_gold) {
		this.reward_gold = reward_gold;
	}
	public BigDecimal getPay_amount() {
		return pay_amount;
	}
	public void setPay_amount(BigDecimal pay_amount) {
		this.pay_amount = pay_amount;
	}
	public String getPayment_way_key() {
		return payment_way_key;
	}
	public void setPayment_way_key(String payment_way_key) {
		this.payment_way_key = payment_way_key;
	}
	public String getJson_data() {
		return json_data;
	}
	public void setJson_data(String json_data) {
		this.json_data = json_data;
	}
	public String getCashier_id() {
		return cashier_id;
	}
	public void setCashier_id(String cashier_id) {
		this.cashier_id = cashier_id;
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
