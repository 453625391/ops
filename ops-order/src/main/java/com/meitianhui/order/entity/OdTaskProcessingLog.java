package com.meitianhui.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务执行日志
 * 
 * @author 丁硕
 * @date 2017年3月8日
 */
public class OdTaskProcessingLog implements Serializable {

	private static final long serialVersionUID = -6992205847575753279L;

	private String log_id; // 日志标识
	private String category; // 事件分类
	private String processing_id; // 任务标识
	private Date tracked_date; // 发生时间
	private String event_desc; //

	public String getLog_id() {
		return log_id;
	}

	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProcessing_id() {
		return processing_id;
	}

	public void setProcessing_id(String processing_id) {
		this.processing_id = processing_id;
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
