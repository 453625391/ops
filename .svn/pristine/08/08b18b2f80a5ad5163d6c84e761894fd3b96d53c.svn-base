package com.meitianhui.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.meitianhui.common.util.HttpClientUtil;

public class InterfaceTest {

	@Test
	public void storagePreview() {
		try {
			String url = "http://127.0.0.1:8080/ops-storage/storage/previewOps";
			// String url =
			// "http://192.168.16.240:9090/ops-storage/storage/previewOps";
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("doc_ids",
					"d19d91054b6d454ba7bc6e86b81d118d,d60e6460b4584e6291ffd8dcc4c9db32,d88490b439b746a3a1b0327e47763c77");
			String result = HttpClientUtil.post(url, requestData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void upload() {
		try {
			String url = "http://127.0.0.1:8080/ops-storage/storage/upload_no_token";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("up_load_file", new File("‪C:\\pic.jpg"));
			params.put("category", "otherDocs");
			HttpClientUtil.upload(url, params);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
