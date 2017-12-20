package com.meitianhui.goods.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽奖活动信息
 * 
 * @author Administrator
 *
 */
public class PlActivity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 活动标识 **/
	private String activity_id;
	/** 活动主题 **/
	private String title;
	/** 活动描述 **/
	private String desc1;
	/** 商品标识 **/
	private String goods_id;
	/** 活动详情 **/
	private String json_data;
	/** 奖品数量 **/
	private Integer prize_qty;
	/** 开奖时间 **/
	private Date lottery_time;
	/** 达标人数 **/
	private Integer min_num;
	/** 参与人数 **/
	private Integer total_num;
	/** 活动状态 **/
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

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getJson_data() {
		return json_data;
	}

	public void setJson_data(String json_data) {
		this.json_data = json_data;
	}

	public Integer getPrize_qty() {
		return prize_qty;
	}

	public void setPrize_qty(Integer prize_qty) {
		this.prize_qty = prize_qty;
	}

	public Date getLottery_time() {
		return lottery_time;
	}

	public void setLottery_time(Date lottery_time) {
		this.lottery_time = lottery_time;
	}
	
	public Integer getMin_num() {
		return min_num;
	}

	public void setMin_num(Integer min_num) {
		this.min_num = min_num;
	}

	public Integer getTotal_num() {
		return total_num;
	}

	public void setTotal_num(Integer total_num) {
		this.total_num = total_num;
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
