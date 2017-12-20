package com.meitianhui.community.easemob.wrapper;

import com.fasterxml.jackson.databind.node.ContainerNode;

/***
 * 请求内容封装接口
 * 
 * @author 丁硕
 * @date 2016年7月21日
 */
public interface BodyWrapper {

	ContainerNode<?> getBody();

	Boolean validate();
}
