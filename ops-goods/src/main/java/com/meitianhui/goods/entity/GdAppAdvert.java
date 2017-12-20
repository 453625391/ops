package com.meitianhui.goods.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 广告
* @ClassName: GdAdvert  
* @author tiny 
* @date 2017年4月5日 下午4:41:26  
*
 */
public class GdAppAdvert implements Serializable{

	private static final long serialVersionUID = 1L;
	/** 唯一标识**/
	private String advert_id;
	/** 业务app，可选值：wyp_app（我要批）|cjf_app（领了么）|hpt_app（伙拼团）|mph_app（名品汇）|c_app（本地生活） **/
	private String category;
	/** 广告内容json数组，标题|图片|链接 **/
	private String json_data;
	/** 创建时间 **/
	private Date created_date;
	/** 修改时间 **/
	private Date modified_date;
	/** 备注 **/
	private String remark;
	
	public String getAdvert_id() {
		return advert_id;
	}
	public void setAdvert_id(String advert_id) {
		this.advert_id = advert_id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getJson_data() {
		return json_data;
	}
	public void setJson_data(String json_data) {
		this.json_data = json_data;
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
