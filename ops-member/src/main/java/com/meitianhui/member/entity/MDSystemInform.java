package com.meitianhui.member.entity;

import java.io.Serializable;
import java.util.Date;

public class MDSystemInform implements Serializable{

	private static final long serialVersionUID =1L;
	
	private String info_id;                	//'消息标识'
	private String info_type;               //'消息类型，引用数据字典分类（XXLX）'
	private String info_result;             //'消息结果(pending,pass,fail)'
	private String info_comment;            //'消息内容'
	private String cre_by;                	//'创建用户'
	private Date cre_date;                	//'创建日期'
	public String getInfo_id() {
		return info_id;
	}
	public void setInfo_id(String info_id) {
		this.info_id = info_id;
	}
	public String getInfo_type() {
		return info_type;
	}
	public void setInfo_type(String info_type) {
		this.info_type = info_type;
	}
	public String getInfo_result() {
		return info_result;
	}
	public void setInfo_result(String info_result) {
		this.info_result = info_result;
	}
	public String getInfo_comment() {
		return info_comment;
	}
	public void setInfo_comment(String info_comment) {
		this.info_comment = info_comment;
	}
	public String getCre_by() {
		return cre_by;
	}
	public void setCre_by(String cre_by) {
		this.cre_by = cre_by;
	}
	public Date getCre_date() {
		return cre_date;
	}
	public void setCre_date(Date cre_date) {
		this.cre_date = cre_date;
	}
}
