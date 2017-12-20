package com.meitianhui.infrastructure.entity;

import java.io.Serializable;

/**
 * @author mole.wang 2015年12月20日
 *
 */
public class AppStore  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3595571283432449674L;
	/**应用标识**/
	private String  app_id;
	/**应用名称**/
	private String  app_name;
	/**开发商**/
	private String  developer;
	/**回调地址，带http路径的标准URL**/
	private String  callback_url;
	/**私钥**/
	private String  private_key;
	/**描述**/
	private String  desc1;
	/**标签**/
	private String  tag;
	/**状态，可选值：toreview（待核审） online（上线）、offline（下线）、delete（删除）**/
	private String  status;
	/**创建时间**/
	private String  created_date;
	
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getCallback_url() {
		return callback_url;
	}
	public void setCallback_url(String callback_url) {
		this.callback_url = callback_url;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	
	
}
