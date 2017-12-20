package com.meitianhui.member.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 消费者地址信息
 * 
 * @author Tiny
 *
 */
public class MDConsumerAddress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 地址标示 **/
	private String address_id;
	/** 消费者标识 **/
	private String consumer_id;
	/** 区域id **/
	private String area_id;
	/** 详细地址 **/
	private String address;
	/** 邮编 **/
	private String zip_code;
	/** 收货人 **/
	private String consignee;
	/** 收货人手机号 **/
	private String mobile;
	/** 是否是默认收货地址 **/
	private String is_major_addr;
	/** 创建时间 **/
	private Date created_date;
	/** 备注 **/
	private String remark;
	
	public String getAddress_id() {
		return address_id;
	}
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}
	public String getConsumer_id() {
		return consumer_id;
	}
	public void setConsumer_id(String consumer_id) {
		this.consumer_id = consumer_id;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip_code() {
		return zip_code;
	}
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIs_major_addr() {
		return is_major_addr;
	}
	public void setIs_major_addr(String is_major_addr) {
		this.is_major_addr = is_major_addr;
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
