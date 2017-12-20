package com.meitianhui.member.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员费记录
 * 
 * @author Tiny
 *
 */
public class MDStoresServiceFee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 消费者标识 **/
	private String date_id;
	/** 加盟店标识 **/
	private String stores_id;
	/** 现金 **/
	private BigDecimal cash;
	/** 金币 **/
	private Integer gold;
	/** 创建时间 **/
	private Date created_date;
	/** 备注 **/
	private String remark;
	public String getDate_id() {
		return date_id;
	}
	public void setDate_id(String date_id) {
		this.date_id = date_id;
	}
	public String getStores_id() {
		return stores_id;
	}
	public void setStores_id(String stores_id) {
		this.stores_id = stores_id;
	}
	public BigDecimal getCash() {
		return cash;
	}
	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}
	public Integer getGold() {
		return gold;
	}
	public void setGold(Integer gold) {
		this.gold = gold;
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
