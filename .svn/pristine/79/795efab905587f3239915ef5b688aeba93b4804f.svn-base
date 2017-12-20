package com.meitianhui.community.easemob.utils;

import com.meitianhui.community.easemob.wrapper.HeaderWrapper;

/***
 * 环信请求头封装工具类
 * 
 * @author 丁硕
 * @date 2016年7月21日
 */
public class HeaderHelper {
	
	public static HeaderWrapper getDefaultHeader() {
		return HeaderWrapper.newInstance().addJsonContentHeader();
	}
	
	public static HeaderWrapper getDefaultHeaderWithToken() {
		return getDefaultHeader().addAuthorization(EasemobTokenFactory.getInstance().getAuthToken(Boolean.FALSE));
	}

	public static HeaderWrapper getUploadHeaderWithToken() {
		return HeaderWrapper.newInstance().addAuthorization(EasemobTokenFactory.getInstance().getAuthToken(Boolean.FALSE)).addRestrictAccess();
	}

	public static HeaderWrapper getDownloadHeaderWithToken(String shareSecret, Boolean isThumb) {
		HeaderWrapper headerWrapper = HeaderWrapper.newInstance().addAuthorization(EasemobTokenFactory.getInstance().getAuthToken(Boolean.FALSE)).addMediaAccept().addShareSecret(shareSecret);
		if(isThumb) {
			headerWrapper.addThumbnail();
		}

		return headerWrapper;
	}
}
