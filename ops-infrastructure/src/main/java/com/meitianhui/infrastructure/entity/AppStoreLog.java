package com.meitianhui.infrastructure.entity;

import java.io.Serializable;

/**
 * @author mole.wang 2015年12月20日
 *
 */
public class AppStoreLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4336181743716264989L;

	/**
	 * app_id
	 */
	private String app_id;
	
	/**
	 * 类别
	 */
	private String category;
	
	/**
	 * tracked_by
	 */
	private String tracked_by;
	
	/**
	 * 发生时间
	 */
	private String tracked_date;
	
	/**
	 * 事件
	 */
	private String event;

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTracked_by() {
		return tracked_by;
	}

	public void setTracked_by(String tracked_by) {
		this.tracked_by = tracked_by;
	}

	public String getTracked_date() {
		return tracked_date;
	}

	public void setTracked_date(String tracked_date) {
		this.tracked_date = tracked_date;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}
