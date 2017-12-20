import java.util.LinkedHashMap;
import java.util.Map;

import com.meitianhui.util.HttpClientUtil;

public class HttpTest {

	public static void main(String[] args) {
		try {
			Map<String, String> reqParams = new LinkedHashMap<String, String>();
			String url = "http://tool.zzblo.com/Api/Md5/decrypt";
			reqParams.put("secret", "e10adc3949ba59abbe56e057f20f883e");
			reqParams.put("readyState", "1");
			//String result = HttpClientUtil.post(url, reqParams);
			String result1 = HttpClientUtil.get(url, "secret=e10adc3949ba59abbe56e057f20f883e&readyState=1");
			//System.out.println(result);
			System.out.println(result1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
