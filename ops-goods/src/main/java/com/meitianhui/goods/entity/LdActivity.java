package com.meitianhui.goods.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 一元购抽奖商品
 * 
 * @author Tiny
 *
 */
public class LdActivity implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 活动标示 **/
	private String activity_id;
	/** 活动类型：DSK（定时开）、YYY（摇一摇）、GGL（刮刮乐） **/
	private String activity_type;
	/** 标题 **/
	private String title;
	/** 描述 **/
	private String desc1;
	/** 截止时间 **/
	private Date end_date;
	/** 奖品 **/
	private String award_name;
	/** 商品价值 **/
	private BigDecimal award_value;
	/** 门店id **/
	private String stores_id;
	/** 门店名称 **/
	private String stores_info;
	/** 门店经度 **/
	private String stores_longitude;
	/** 门店纬度 **/
	private String stores_latitude;
	/** 限定人数 **/
	private Integer person_num;
	/** 已参入人数 **/
	private Integer joined_num;
	/** 幸运号码 **/
	private String luck_code;
	/** 图片1 **/
	private String award_pic_path1;
	/** 图片2 **/
	private String award_pic_path2;
	/** 图片3 **/
	private String award_pic_path3;
	/** 图片4 **/
	private String award_pic_path4;
	/** 图片5 **/
	private String award_pic_path5;
	/** 状态，可选值：processing（进行中）、announced（已揭晓）、finished（已结束） **/
	private String status;
	/** 创建时间 **/
	private Date created_date;
	/** 更新时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public String getActivity_type() {
		return activity_type;
	}

	public void setActivity_type(String activity_type) {
		this.activity_type = activity_type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getAward_name() {
		return award_name;
	}

	public void setAward_name(String award_name) {
		this.award_name = award_name;
	}

	public BigDecimal getAward_value() {
		return award_value;
	}

	public void setAward_value(BigDecimal award_value) {
		this.award_value = award_value;
	}

	public String getStores_id() {
		return stores_id;
	}

	public void setStores_id(String stores_id) {
		this.stores_id = stores_id;
	}

	public String getStores_info() {
		return stores_info;
	}

	public void setStore_info(String stores_info) {
		this.stores_info = stores_info;
	}

	public String getStores_longitude() {
		return stores_longitude;
	}

	public void setStores_longitude(String stores_longitude) {
		this.stores_longitude = stores_longitude;
	}

	public String getStores_latitude() {
		return stores_latitude;
	}

	public void setStores_latitude(String stores_latitude) {
		this.stores_latitude = stores_latitude;
	}

	public Integer getPerson_num() {
		return person_num;
	}

	public void setPerson_num(Integer person_num) {
		this.person_num = person_num;
	}

	public Integer getJoined_num() {
		return joined_num;
	}

	public void setJoined_num(Integer joined_num) {
		this.joined_num = joined_num;
	}

	public String getLuck_code() {
		return luck_code;
	}

	public void setLuck_code(String luck_code) {
		this.luck_code = luck_code;
	}

	public String getAward_pic_path1() {
		return award_pic_path1;
	}

	public void setAward_pic_path1(String award_pic_path1) {
		this.award_pic_path1 = award_pic_path1;
	}

	public String getAward_pic_path2() {
		return award_pic_path2;
	}

	public void setAward_pic_path2(String award_pic_path2) {
		this.award_pic_path2 = award_pic_path2;
	}

	public String getAward_pic_path3() {
		return award_pic_path3;
	}

	public void setAward_pic_path3(String award_pic_path3) {
		this.award_pic_path3 = award_pic_path3;
	}

	public String getAward_pic_path4() {
		return award_pic_path4;
	}

	public void setAward_pic_path4(String award_pic_path4) {
		this.award_pic_path4 = award_pic_path4;
	}

	public String getAward_pic_path5() {
		return award_pic_path5;
	}

	public void setAward_pic_path5(String award_pic_path5) {
		this.award_pic_path5 = award_pic_path5;
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

}
