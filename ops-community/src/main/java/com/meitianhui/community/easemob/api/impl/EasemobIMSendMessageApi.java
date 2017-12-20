package com.meitianhui.community.easemob.api.impl;

import com.meitianhui.community.easemob.api.IMSendMessageApi;
import com.meitianhui.community.easemob.body.MessageBody;
import com.meitianhui.community.easemob.constant.EasemobConstant;
import com.meitianhui.community.easemob.constant.HttpMethod;
import com.meitianhui.community.easemob.utils.HeaderHelper;
import com.meitianhui.community.easemob.utils.HttpClientRestApiInvoker;
import com.meitianhui.community.easemob.wrapper.HeaderWrapper;
import com.meitianhui.community.easemob.wrapper.ResponseWrapper;

public class EasemobIMSendMessageApi implements IMSendMessageApi {

	private static final String ROOT_URI = EasemobConstant.EASEMOB_API_SERVICE_URL + "/message";

	@Override
	public ResponseWrapper sendMessage(MessageBody body) {
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		return HttpClientRestApiInvoker.sendRequest(HttpMethod.POST, ROOT_URI, header, body);
	}
	
}
