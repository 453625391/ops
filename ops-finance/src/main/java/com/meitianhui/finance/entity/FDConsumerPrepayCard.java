package com.meitianhui.finance.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 消费者亲情卡
 * 
 * @author Tiny
 *
 */
public class FDConsumerPrepayCard implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 亲情卡标识**/
	private String consumer_prepay_card_id;
	/** 会员标识 **/
	private String member_id;
	/** 卡号 **/
	private String card_no;
	/** 绑定手机号 **/
	private String mobile;
	/** 绑定时间**/
	private Date binded_date ;
	/** 解绑时间**/
	private Date unbinded_date;
	/** 状态**/
	private String status;
	/** 备注 **/
	private String remark;
	
	public String getConsumer_prepay_card_id() {
		return consumer_prepay_card_id;
	}
	public void setConsumer_prepay_card_id(String consumer_prepay_card_id) {
		this.consumer_prepay_card_id = consumer_prepay_card_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getBinded_date() {
		return binded_date;
	}
	public void setBinded_date(Date binded_date) {
		this.binded_date = binded_date;
	}
	public Date getUnbinded_date() {
		return unbinded_date;
	}
	public void setUnbinded_date(Date unbinded_date) {
		this.unbinded_date = unbinded_date;
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
