package com.meitianhui.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.meitianhui.common.util.HttpClientUtil;

public class UploadTest {

	@Test
	public void downloadByToUpload() {
		try {
			String url = "http://127.0.0.1:8080/ops-storage/storage/downloadByToUpload";
			// String url =
			// "http://192.168.16.240:9090/ops-storage/storage/previewOps";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("file_url","https://img.alicdn.com/imgextra/i4/2942863215/TB2AmVzsORnpuFjSZFCXXX2DXXa_!!2942863215.jpg");
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void storagePreview() {
		try {
			String url = "http://127.0.0.1:8082/ops-storage/storage/preview";
			// String url =
			// "http://192.168.16.240:9090/ops-storage/storage/previewOps";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("doc_ids","2fda776a195f4cc49dcfdab53502747c");
			requestData.put("app_token", "sdssadfjlwi54u");
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void upload() {
		try {
			 String url = "https://img.alicdn.com/imgextra/i4/2942863215/TB2AmVzsORnpuFjSZFCXXX2DXXa_!!2942863215.jpg";   
		        byte[] btImg = getImageFromNetByUrl(url);   
		        if(null != btImg && btImg.length > 0){   
		            System.out.println("读取到：" + btImg.length + " 字节");   
		            String fileName = "上传图片.jpg";   
		            writeImageToDisk(btImg, fileName);   
		        }else{   
		            System.out.println("没有从该连接获得内容");   
		        }   
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	   /**  
     * 将图片写入到磁盘  
     * @param img 图片数据流  
     * @param fileName 文件保存时的名称  
     */  
    public static void writeImageToDisk(byte[] img, String fileName){   
        try {   
			String tmpFilePath = System.getProperty("java.io.tmpdir");
			File file = new File(tmpFilePath + fileName);
            FileOutputStream fops = new FileOutputStream(file); 
            fops.write(img);   
            fops.flush();   
            fops.close();   
            System.out.println("图片已经写入到C盘");   
            
            String url = "http://127.0.0.1:8080/ops-storage/storage/upload_no_token";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("up_load_file", file);
			params.put("category", "otherDocs");
			HttpClientUtil.upload(url, params);
			file.delete();
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
    }   
	 public static byte[] getImageFromNetByUrl(String strUrl){   
	        try {   
	            URL url = new URL(strUrl);   
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();   
	            conn.setRequestMethod("GET");   
	            conn.setConnectTimeout(5 * 1000);   
	            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据   
	            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据   
	            return btImg;   
	        } catch (Exception e) {   
	            e.printStackTrace();   
	        }   
	        return null;   
	    }   
	 
 /**  
 * 从输入流中获取数据  
 * @param inStream 输入流  
 * @return  
 * @throws Exception  
 */  
    public static byte[] readInputStream(InputStream inStream) throws Exception{   
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();   
        byte[] buffer = new byte[1024];   
        int len = 0;   
        while( (len=inStream.read(buffer)) != -1 ){   
            outStream.write(buffer, 0, len);   
        }   
        inStream.close();   
        return outStream.toByteArray();   
    }   
	
	public static void httpPostFile(String url, Map<String, Object> params) {
		// 实例化http客户端
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		// 实例化post提交方式
		HttpPost post = new HttpPost(url);
		try {
			// 实例化参数对象
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			for (String key : params.keySet()) {
				Object value = params.get(key);
				if (value instanceof File) {
					multipartEntityBuilder.addBinaryBody(key, (File) value);
				} else {
					multipartEntityBuilder.addTextBody(key, value + "");
				}
			}
			
			// 设置 POST 请求的实体部分
			post.setEntity(multipartEntityBuilder.build());
			// 发送 HTTP 请求
			// 执行post请求并得到返回对象 [ 到这一步我们的请求就开始了 ]
			HttpResponse httpResponse = closeableHttpClient.execute(post);
			// 解析返回请求结果
			HttpEntity entity = httpResponse.getEntity();
			StatusLine statusLine = httpResponse.getStatusLine();
			System.out.println(statusLine.getStatusCode());
			if (null != entity) {
				System.out.println("contentEncoding:" + entity.getContentEncoding());
				System.out.println("response content:" + EntityUtils.toString(entity));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} finally {
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
