package com.meitianhui.storage.util;

import java.io.InputStream;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.meitianhui.common.util.PropertiesConfigUtil;

/***
 * 阿里云OSS操作工具类,对Bucket操作，获取生成临时私钥信息
 * 
 * @author 丁硕
 * @date 2015年12月25日
 */
public class OSSUtils {

	/** 节点地址 **/
	public static String ENDPOINT = PropertiesConfigUtil.getProperty("oss_endpoint");
	/** key **/
	public static String ACCESS_KEY_ID = PropertiesConfigUtil.getProperty("oss_access_key_id");
	/** 默认的Bucket名称 **/
	public static String DEFAULT_BUCKET_NAME = PropertiesConfigUtil.getProperty("oss_default_bucket_name");
	/** 连接OSS客户端的key **/
	public static String OSS_CLIENT = PropertiesConfigUtil.getProperty("oss_access_key_secret");
	/** 上传到OSS的访问地址，域名访问 **/
	public static String OSS_ACCESS_URL = PropertiesConfigUtil.getProperty("oss_access_url");
	
	/***
	 * 文档上传
	 * 
	 * @return
	 * @author 丁硕
	 * @date 2015年12月25日
	 */
	public static void OSSUpLoadFile(String path, String contentType, InputStream inputStream) throws Exception {
		try {
			OSSClient client = new OSSClient(ENDPOINT, ACCESS_KEY_ID,
					PropertiesConfigUtil.getProperty("oss_access_key_secret"));
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			client.putObject(DEFAULT_BUCKET_NAME, path, inputStream, meta);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
}
