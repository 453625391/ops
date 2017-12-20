package com.meitianhui.common.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

/**
 * gzip工具类
 * 
 * @ClassName: GZipUtil
 * @author tiny
 * @date 2017年3月22日 下午6:23:34
 *
 */
public class GZipUtil {

	public static void unZipFile(String zipFilePath, String unzipFilePath) throws Exception {
		FileInputStream fin = null;
		GZIPInputStream gzin = null;
		FileOutputStream fout = null;
		try {
			// 建立grip压缩文件输入流
			fin = new FileInputStream(zipFilePath);
			// 建立gzip解压工作流
			gzin = new GZIPInputStream(fin);
			// 建立解压文件输出流
			fout = new FileOutputStream(unzipFilePath);
			byte[] buf = new byte[1024];
			int num;
			while ((num = gzin.read(buf, 0, buf.length)) != -1) {
				fout.write(buf, 0, num);
			}
			fout.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != fout) {
				fout.close();
			}
			if (null != gzin) {
				gzin.close();
			}
			if (null != fin) {
				fin.close();
			}
		}
	}

}
