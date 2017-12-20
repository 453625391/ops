package com.meitianhui.community.entity;

import java.io.Serializable;

/***
 * 环信用户/账号实体类
 * 
 * @author 丁硕
 * @date 2016年8月10日
 */
public class IMUser implements Serializable {

	private static final long serialVersionUID = -1774140513121396510L;

	private String im_user_id; // 唯一标识
	private String username; // 账号
	private String password; // 密码
	private String nickname; // 昵称
	private String head_pic_path; // 头像
	private String sex_key; // 性别，引用字典分类（XBDM）
	private String birthday; // 出生年月
	private String activated; // 状态，可选值：true/false
	private String created; // 创建日期
	private String modified; // 修改日期
	private String remark; // 备注

	public String getIm_user_id() {
		return im_user_id;
	}

	public void setIm_user_id(String im_user_id) {
		this.im_user_id = im_user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHead_pic_path() {
		return head_pic_path;
	}

	public void setHead_pic_path(String head_pic_path) {
		this.head_pic_path = head_pic_path;
	}

	public String getSex_key() {
		return sex_key;
	}

	public void setSex_key(String sex_key) {
		this.sex_key = sex_key;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getActivated() {
		return activated;
	}

	public void setActivated(String activated) {
		this.activated = activated;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
