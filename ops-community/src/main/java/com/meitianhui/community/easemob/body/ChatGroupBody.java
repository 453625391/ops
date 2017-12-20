package com.meitianhui.community.easemob.body;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meitianhui.community.easemob.wrapper.BodyWrapper;

/***
 * 环信群组请求body
 * 
 * @author 丁硕
 * @date 2016年7月22日
 */
public class ChatGroupBody implements BodyWrapper {
	// 群组名称
	private String groupName;
	// 群组描述
	private String desc;
	// 群组类型：true：公开群，false：私有群。
	private Boolean isPublic;
	// 群成员上限，创建群组的时候设置，可修改。
	private Long maxUsers;
	// 加入公开群是否需要批准，默认值是false（加入公开群不需要群主批准），此属性为必选的，私有群必须为true
	private Boolean approval;
	// 群组的管理员，此属性为必须的
	private String owner;
	// 群组成员，此属性为可选的，但是如果加了此项，数组元素至少一个（注：群主不需要写入到members里面）
	private String[] members;

	public ChatGroupBody(String groupName, String desc, Boolean isPublic, Long maxUsers, Boolean approval, String owner,
			String[] members) {
		this.groupName = groupName;  
		this.desc = desc;
		this.isPublic = isPublic;
		this.maxUsers = maxUsers;
		this.approval = approval;
		this.owner = owner;
		this.members = members;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getDesc() {
		return desc;
	}

	public Boolean getPublic() {
		return isPublic;
	}

	public Long getMaxUsers() {
		return maxUsers;
	}

	public Boolean getApproval() {
		return approval;
	}

	public String getOwner() {
		return owner;
	}

	public String[] getMembers() {
		return members;
	}

	public ContainerNode<?> getBody() {
		ObjectNode body = JsonNodeFactory.instance.objectNode();
		body.put("groupname", groupName).put("desc", desc).put("public", isPublic).put("approval", approval)
				.put("owner", owner);
		if (null != maxUsers) {
			body.put("maxusers", maxUsers);
		}
		if (ArrayUtils.isNotEmpty(members)) {
			ArrayNode membersNode = body.putArray("members");
			for (String member : members) {
				membersNode.add(member);
			}
		}

		return body;
	}

	public Boolean validate() {
		return StringUtils.isNotBlank(groupName) && StringUtils.isNotBlank(desc) && null != isPublic && null != approval
				&& StringUtils.isNotBlank(owner);
	}
}
