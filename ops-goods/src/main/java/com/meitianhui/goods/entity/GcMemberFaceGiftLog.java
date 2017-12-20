package com.meitianhui.goods.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/****
 * 见面礼红包日志
 * 
 * @author 丁硕
 * @date 2017年3月1日
 */
public class GcMemberFaceGiftLog implements Serializable {

	private static final long serialVersionUID = -4048323748041956764L;

	private String log_id; // 日志标识
	private String member_id; // 会员标识
	private String category; // 事件类别
	private BigDecimal amount; // 现金红包金额
	private String order_date; // 订单时间
	private Date tracked_date; // 发生时间
	private String event_desc; // 事件

	public String getLog_id() {
		return log_id;
	}

	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOrder_date() {
		return order_date;
	}

	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	public Date getTracked_date() {
		return tracked_date;
	}

	public void setTracked_date(Date tracked_date) {
		this.tracked_date = tracked_date;
	}

	public String getEvent_desc() {
		return event_desc;
	}

	public void setEvent_desc(String event_desc) {
		this.event_desc = event_desc;
	}

}
