package com.meitianhui.finance.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 消费者亲情卡
 * 
 * @author Tiny
 *
 */
public class FDPrepayCard implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 亲情卡标识**/
	private String card_id;
	/** 卡号 **/
	private String card_no;
	/** 加盟店会员标识 **/
	private String stores_id;
	/** 描述 **/
	private String desc1;
	/** 状态**/
	private String status;
	/** 绑定时间**/
	private Date created_date ;
	/** 备注 **/
	private String remark;
	
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getStores_id() {
		return stores_id;
	}
	public void setStores_id(String stores_id) {
		this.stores_id = stores_id;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
