package com.meitianhui.community.entity;

import java.io.Serializable;

/***
 * IM群组信息
 * 
 * @author 丁硕
 * @date 2016年8月24日
 */
public class IMGroup implements Serializable {

	private static final long serialVersionUID = -5743231777543092857L;

	private String id; // 群组标识',
	private String name; // 群组名称',
	private String head_pic_path; // 头像',
	private String description; // 描述',
	private String is_public; // 类型，可选值：true（公开群）false（私有群）',
	private String membersonly; // 是否只有群成员可以进来发言，可选值：true:（是）false（否）',
	private String allowinvites; // 是否允许群成员邀请别人加入此群，可选值：true:（是）false（否）',
	private String maxusers; // 群成员上限，创建群组的时候设置，可修改',
	private String affiliations_count; // 现有成员总数',
	private String affiliations; // 现有成员列表',
	private String owner; // 群主',
	private String created; // 创建日期'
	private String modified; // 修改日期
	private String remark; // 备注

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHead_pic_path() {
		return head_pic_path;
	}

	public void setHead_pic_path(String head_pic_path) {
		this.head_pic_path = head_pic_path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIs_public() {
		return is_public;
	}

	public void setIs_public(String is_public) {
		this.is_public = is_public;
	}

	public String getMembersonly() {
		return membersonly;
	}

	public void setMembersonly(String membersonly) {
		this.membersonly = membersonly;
	}

	public String getAllowinvites() {
		return allowinvites;
	}

	public void setAllowinvites(String allowinvites) {
		this.allowinvites = allowinvites;
	}

	public String getMaxusers() {
		return maxusers;
	}

	public void setMaxusers(String maxusers) {
		this.maxusers = maxusers;
	}

	public String getAffiliations_count() {
		return affiliations_count;
	}

	public void setAffiliations_count(String affiliations_count) {
		this.affiliations_count = affiliations_count;
	}

	public String getAffiliations() {
		return affiliations;
	}

	public void setAffiliations(String affiliations) {
		this.affiliations = affiliations;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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
