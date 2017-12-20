package com.meitianhui.notification.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息发送
 * @author Tiny
 *
 */
public class IdMessageQueue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**队列标识**/
	private String queue_id;
	/**消息内容**/
	private String message;
	/**发送者**/
	private String sender;
	/** 接受者**/
	private String receiver;
	/**发送日期**/
	private Date send_date;
	
	public String getQueue_id() {
		return queue_id;
	}
	public void setQueue_id(String queue_id) {
		this.queue_id = queue_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Date getSend_date() {
		return send_date;
	}
	public void setSend_date(Date send_date) {
		this.send_date = send_date;
	}
	
	
	
}
