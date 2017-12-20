package com.meitianhui.community.entity;

import java.io.Serializable;

/***
 * IM直播室
 * 
 * @author 丁硕
 * @date 2016年8月29日
 */
public class IMLiveroom implements Serializable {

	private static final long serialVersionUID = 2148897374268852190L;

	private String id; // 直播室标识
	private String name; // 直播室名称
	private String description; // 描述
	private String cover; // 封面
	private String url; // 播放地址
	private String label; // 标签
	private String affiliations; // 现有成员列表
	private String visited_count = "0"; // 观看数
	private String gift_count = "0"; // 礼物数
	private String comment_count = "0"; // 评论数
	private String owner; // 创建人
	private String status; // 状态，可选值：直播中、已结束
	private String created; // 创建日期
	private String modified; // 播放结束时间
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAffiliations() {
		return affiliations;
	}

	public void setAffiliations(String affiliations) {
		this.affiliations = affiliations;
	}

	public String getVisited_count() {
		return visited_count;
	}

	public void setVisited_count(String visited_count) {
		this.visited_count = visited_count;
	}

	public String getGift_count() {
		return gift_count;
	}

	public void setGift_count(String gift_count) {
		this.gift_count = gift_count;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
