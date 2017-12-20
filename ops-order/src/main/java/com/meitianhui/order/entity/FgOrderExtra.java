package com.meitianhui.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单扩展信息
 * 
 * @author Tiny
 *
 */
public class FgOrderExtra implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 订单标识 **/
	private String order_id;
	/** 消费者id **/
	private String consumer_id;
	/**门店id**/
	private String stores_id;
	/** 创建时间 **/
	private Date created_date;
	/** 备注 **/
	private String remark;
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getConsumer_id() {
		return consumer_id;
	}
	public void setConsumer_id(String consumer_id) {
		this.consumer_id = consumer_id;
	}
	public String getStores_id() {
		return stores_id;
	}
	public void setStores_id(String stores_id) {
		this.stores_id = stores_id;
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
