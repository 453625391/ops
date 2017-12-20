package com.meitianhui.notification.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信统计
 * @author Tiny
 *
 */
public class IdSmsStatistics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String statistics_id;
	private String sms_source;
	private String mobile;
	private String sms;
	private Date tracked_date;
	
	public String getStatistics_id() {
		return statistics_id;
	}
	public void setStatistics_id(String statistics_id) {
		this.statistics_id = statistics_id;
	}
	public String getSms_source() {
		return sms_source;
	}
	public void setSms_source(String sms_source) {
		this.sms_source = sms_source;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	public Date getTracked_date() {
		return tracked_date;
	}
	public void setTracked_date(Date tracked_date) {
		this.tracked_date = tracked_date;
	}

}
