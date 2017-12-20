package com.meitianhui.infrastructure.entity;

import java.io.Serializable;
import java.util.Date;

/***
 * 文档存储实体对象
 * 
 * @author 丁硕
 * @date 2016年1月11日
 */
public class IdDocument implements Serializable {

	private static final long serialVersionUID = -1027163690693403326L;

	private String doc_id; // 文档标识
	private String doc_name; // 文档名称
	private String doc_type; // 文档类型
	private String path; // 文档存储路径
	private String status; // 状态，可选值：normal（正常）、delete（删除
	private Date created_date; // 创建时间

	public String getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}

	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

	public String getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

}
