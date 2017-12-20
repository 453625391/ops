package com.meitianhui.goods.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 权益活动
* @ClassName: GdActivity  
* @author tiny 
* @date 2017年2月20日 下午6:01:38  
*
 */
public class GdActivity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 活动标识**/
	private String activity_id;
	/** 活动名称 **/
	private String title;
	/** 商品标识 **/
	private String goods_id;
	/** 商品编码 **/
	private String goods_code;
	/** 商品信息**/
	private String goods_title;
	/** 商品详情，链接|图片|详情等 **/
	private String json_data;
	/** 截止日期 **/
	private Date expired_date;
	
	private int limited_point;
	/** 等级限制 **/
	private int limited_grade;
	/** 限制权益 **/
	private String limited_benefit;
	/** 状态，可选值：normal（正常）cancelled（取消） **/
	private String status;
	/** 创建时间 **/
	private Date created_date;
	/** 修改时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGoods_title() {
		return goods_title;
	}
	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_code() {
		return goods_code;
	}
	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}
	public String getJson_data() {
		return json_data;
	}
	public void setJson_data(String json_data) {
		this.json_data = json_data;
	}
	public Date getExpired_date() {
		return expired_date;
	}
	public void setExpired_date(Date expired_date) {
		this.expired_date = expired_date;
	}
	public int getLimited_grade() {
		return limited_grade;
	}
	public void setLimited_grade(int limited_grade) {
		this.limited_grade = limited_grade;
	}
	public String getLimited_benefit() {
		return limited_benefit;
	}
	public void setLimited_benefit(String limited_benefit) {
		this.limited_benefit = limited_benefit;
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
	public Date getModified_date() {
		return modified_date;
	}
	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getLimited_point() {
		return limited_point;
	}
	public void setLimited_point(int limited_point) {
		this.limited_point = limited_point;
	}

	
}
