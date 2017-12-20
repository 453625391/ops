package com.meitianhui.common.util;

/**
 * 文件操作
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件操作的工具类
 * 
 * @author Tiny
 *
 */
public class FileUtil {

	/**
	 * 读取文件的内容
	 * 
	 * @Title: fileToStr
	 * @param as_filePath
	 * @return
	 * @author tiny
	 */
	public static String fileToStr(String as_filePath) {
		String ls_str = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(as_filePath));
			String line;

			while ((line = reader.readLine()) != null) {
				ls_str += line + "\n";
			}

			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ls_str;
	}

	/**
	 * 获取文件内容
	 */
	public static String fileToStrUTF8(String filePath) {
		String str = "";
		try {
			File fileName = new File(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			String line;

			while ((line = br.readLine()) != null) {
				str += line + "\n";
			}

			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return str;
	}

	/**
	 * 逐行解析文件
	 * 
	 * @Title: fileToArrayList
	 * @param as_filePath
	 * @return
	 * @author tiny
	 */
	public static List<String> fileToArrayList(String as_filePath) {
		List<String> list = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(as_filePath));
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	/**
	 * 追加字符串到文件
	 * 
	 * @Title: strToFile
	 * @param as_str
	 * @param as_filePath
	 * @param append
	 * @author tiny
	 */
	public static void strToFile(String as_str, String as_filePath, boolean append) {
		try {
			BufferedReader reader = new BufferedReader(new StringReader(as_str));
			File file_filePath = new File(as_filePath);
			if (!file_filePath.exists()) {
				String ls_filePath_parent = file_filePath.getParent();
				File file_filePath_parent = new File(ls_filePath_parent);
				file_filePath_parent.mkdirs();
			}

			PrintWriter out = new PrintWriter(new FileWriter(as_filePath, append));
			while ((as_str = reader.readLine()) != null) {
				out.println(as_str);
			}
			out.close();
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 将字符串写入文件
	 * 
	 * @Title: strToFile
	 * @param as_str
	 * @param as_filePath
	 * @author tiny
	 */
	public static void strToFile(String as_str, String as_filePath) {
		strToFile(as_str, as_filePath, false);
	}

	/**
	 * 获取目录下文件名称
	 * 
	 * @Title: getFileNameByPath
	 * @param as_filePath
	 * @return
	 * @author tiny
	 */
	public static List<String> getFileNameByPath(String as_filePath) {
		List<String> al_fileList = new ArrayList<String>();
		File lf_filePath = new File(as_filePath);
		File[] lf_tmpFileList = null;

		if (!lf_filePath.exists())
			lf_filePath.mkdir();
		if (lf_filePath.isDirectory()) {
			lf_tmpFileList = lf_filePath.listFiles();
		}

		for (int i = 0; i < lf_tmpFileList.length; i++) {
			if (lf_tmpFileList[i].isFile()) {
				al_fileList.add(lf_tmpFileList[i].getPath());
			}
		}
		return al_fileList;
	}

	/**
	 * 获取文件名称和路径
	 * 
	 * @Title: getFileNameAndPath
	 * @param as_filePath
	 * @return
	 * @author tiny
	 */
	public static Map<String, String> getFileNameAndPath(String as_filePath) {
		Map<String, String> al_fileList = new HashMap<String, String>();
		File lf_filePath = new File(as_filePath);
		File[] lf_tmpFileList = null;

		if (!lf_filePath.exists())
			lf_filePath.mkdir();
		if (lf_filePath.isDirectory()) {
			lf_tmpFileList = lf_filePath.listFiles();
		}

		for (int i = 0; i < lf_tmpFileList.length; i++) {
			if (lf_tmpFileList[i].isDirectory()) {
				al_fileList.put(lf_tmpFileList[i].getName(), lf_tmpFileList[i].getPath());
			}
		}

		return al_fileList;
	}

	public static Map<String, String> getFileNameAndFile(String as_filePath) {
		Map<String, String> al_fileList = new HashMap<String, String>();
		File lf_filePath = new File(as_filePath);
		File[] lf_tmpFileList = null;

		if (!lf_filePath.exists())
			lf_filePath.mkdir();
		if (lf_filePath.isDirectory()) {
			lf_tmpFileList = lf_filePath.listFiles();
		}

		for (int i = 0; i < lf_tmpFileList.length; i++) {
			if (!lf_tmpFileList[i].isDirectory()) {
				al_fileList.put(lf_tmpFileList[i].getName(), lf_tmpFileList[i].getPath());
			}
		}

		return al_fileList;
	}

	public static String getFileParentDirName(String fileName) {
		File file = new File(fileName);
		return file.getParent();
	}

	/**
	 * 文件是否被修改过
	 * 
	 * @Title: isModified
	 * @return
	 * @author tiny
	 */
	public static boolean isModified() {
		boolean mflag = false;
		long lastTime = 0;
		File file = new File("");
		long newTime = file.lastModified();
		if (newTime > lastTime) {
			mflag = true;
		}
		file = null;
		return mflag;
	}

	/**
	 * 创建文件,如果文件存在,就删除再创建
	 * 
	 * @Title: createFile_del
	 * @param as_fileNamePath
	 * @author tiny
	 */
	public static void createFile_del(String as_fileNamePath) {
		File file = new File(as_fileNamePath);
		if (file.exists()) {
			file.delete();
		}

		File path = file.getParentFile();
		if (!path.exists()) {
			path.mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void createFile(String as_fileNamePath) {
		File file = new File(as_fileNamePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void createDir(String pathName) {
		File file = new File(pathName);

		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 删除目录下所有文件
	 * 
	 * @Title: delAllFile
	 * @param filePath
	 * @author tiny
	 */
	public static void delListFile(String filePath) {
		File file = new File(filePath);
		File[] fileList = file.listFiles();
		if (fileList != null) {
			for (int i = 0; i < fileList.length; i++) {
				File fileTemp = fileList[i];
				//如果是文件,则删除,如果是目录
				if (fileTemp.isFile()) {
					fileTemp.delete();
				}
			}
		}
		file.delete();
	}

}
