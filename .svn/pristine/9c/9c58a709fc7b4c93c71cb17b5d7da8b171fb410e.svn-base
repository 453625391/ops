package com.meitianhui.storage.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.meitianhui.base.controller.BaseController;
import com.meitianhui.common.constant.CommonRspCode;
import com.meitianhui.common.constant.ResultData;
import com.meitianhui.common.exception.BusinessException;
import com.meitianhui.common.exception.SystemException;
import com.meitianhui.common.util.FastJsonUtil;
import com.meitianhui.common.util.IDUtil;
import com.meitianhui.common.util.ValidateUtil;
import com.meitianhui.storage.constant.RspCode;
import com.meitianhui.storage.service.StorageService;

/**
 * 文档服务
 * 
 * @author Tiny
 *
 */
@Controller
@RequestMapping("/storage")
public class StorageController {

	private static final Logger logger = Logger.getLogger(BaseController.class);

	@Autowired
	private StorageService storageService;

	// @Autowired
	// private RedisUtil redisUtil;

	/**
	 * 文件存储
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@RequestMapping("/upload")
	public void storageUpload(HttpServletRequest request, HttpServletResponse response) {
		ResultData result = new ResultData();
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		try {
			paramsMap.put("category", request.getParameter("category"));
			paramsMap.put("app_token", request.getParameter("app_token"));
			ValidateUtil.validateParams(paramsMap, new String[] { "app_token", "category" });
			// TODO 后期优化
			//
			// Object token_obj = redisUtil.getObj((String)
			// paramsMap.get("app_token"));
			// if (token_obj == null) {
			// throw new BusinessException(RspCode.APP_TOKEN_ERROR,
			// RspCode.MSG.get(RspCode.APP_TOKEN_ERROR));
			// }
			//
			// 多文件上传路径
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile("up_load_file");
			if (multipartFile == null) {
				throw new BusinessException(RspCode.FILE_NOT_EXIST, RspCode.MSG.get(RspCode.FILE_NOT_EXIST));
			}
			paramsMap.put("contentType", multipartFile.getContentType());
			String fiulName = multipartFile.getOriginalFilename();
			Integer index = fiulName.lastIndexOf(".");
			String subffix = fiulName.substring(index);
			paramsMap.put("subffix", subffix);
			paramsMap.put("doc_name", fiulName.substring(0, index));
			InputStream inputStream = multipartFile.getInputStream();
			paramsMap.put("inputStream", inputStream);
			storageService.handleStorageUpload(paramsMap, result);
			resultMap.put("rsp_code", "succ");
			resultMap.put("data", result.getResultData() == null ? "{}" : result.getResultData());
		} catch (BusinessException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.warn("BusinessException->service:storage/upload, errorInfo:" + e.getMsg());
		} catch (SystemException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.error("SystemException->service:storage/upload", e);
		} catch (Exception e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", CommonRspCode.SYSTEM_ERROR);
			resultMap.put("error_msg", CommonRspCode.MSG.get(CommonRspCode.SYSTEM_ERROR));
			logger.error("Exception->service:storage/upload", e);
		}
		// 将数据返回给请求方
		String resultJson = FastJsonUtil.toJson(resultMap);
		// 清空返回数据并置空,让GC更快的进行内存回收
		resultMap.clear();
		resultMap = null;
		logger.info("service:storage/upload,params:" + paramsMap.toString() + ",response:" + resultJson);
		retrunResult(response, resultJson);
	}

	/**
	 * 文件存储
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@RequestMapping("/upload_no_token")
	public void storageUploadNoToken(HttpServletRequest request, HttpServletResponse response) {
		ResultData result = new ResultData();
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		try {
			paramsMap.put("category", request.getParameter("category"));
			ValidateUtil.validateParams(paramsMap, new String[] { "category" });
			// 多文件上传路径
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile("up_load_file");
			if (multipartFile == null) {
				throw new BusinessException(RspCode.FILE_NOT_EXIST, RspCode.MSG.get(RspCode.FILE_NOT_EXIST));
			}
			paramsMap.put("contentType", multipartFile.getContentType());
			String fiulName = multipartFile.getOriginalFilename();
			Integer index = fiulName.lastIndexOf(".");
			String subffix = fiulName.substring(index);
			paramsMap.put("subffix", subffix);
			paramsMap.put("doc_name", fiulName.substring(0, index));
			InputStream inputStream = multipartFile.getInputStream();
			paramsMap.put("inputStream", inputStream);
			storageService.handleStorageUpload(paramsMap, result);
			resultMap.put("rsp_code", "succ");
			resultMap.put("data", result.getResultData() == null ? "{}" : result.getResultData());
		} catch (BusinessException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.warn("BusinessException->service:storage/upload_no_token, errorInfo:" + e.getMsg());
		} catch (SystemException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.error("SystemException->service:storage/upload_no_token", e);
		} catch (Exception e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", CommonRspCode.SYSTEM_ERROR);
			resultMap.put("error_msg", CommonRspCode.MSG.get(CommonRspCode.SYSTEM_ERROR));
			logger.error("Exception->service:storage/upload_no_token", e);
		}
		// 将数据返回给请求方
		String resultJson = FastJsonUtil.toJson(resultMap);
		// 清空返回数据并置空,让GC更快的进行内存回收
		resultMap.clear();
		resultMap = null;
		logger.info("service:storage/upload_no_token,params:" + paramsMap.toString() + ",response:" + resultJson);
		retrunResult(response, resultJson);
	}

	/**
	 * 文件预览
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@RequestMapping("/preview")
	public void preview(HttpServletRequest request, HttpServletResponse response) {
		ResultData result = new ResultData();
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		try {
			paramsMap.put("doc_ids", request.getParameter("doc_ids"));
			paramsMap.put("app_token", request.getParameter("app_token"));
			ValidateUtil.validateParams(paramsMap, new String[] { "app_token", "doc_ids" });

			// TODO 后期优化
			//
			// Object token_obj = redisUtil.getObj((String)
			// paramsMap.get("app_token"));
			// if (token_obj == null) {
			// throw new BusinessException(RspCode.APP_TOKEN_ERROR,
			// RspCode.MSG.get(RspCode.APP_TOKEN_ERROR));
			// }
			//
			String doc_ids = (String) paramsMap.get("doc_ids");
			List<String> list = Arrays.asList(doc_ids.split(","));
			if (list.size() == 0) {
				throw new BusinessException(RspCode.SYSTEM_PARAM_MISS, RspCode.MSG.get(RspCode.SYSTEM_PARAM_MISS));
			}
			storageService.previewCache(list, result);
			resultMap.put("rsp_code", "succ");
			resultMap.put("data", result.getResultData() == null ? "{}" : result.getResultData());
		} catch (BusinessException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.warn("BusinessException->service:storage/preview, errorInfo:" + e.getMsg());
		} catch (SystemException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.error("SystemException->service:storage/preview", e);
		} catch (Exception e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", CommonRspCode.SYSTEM_ERROR);
			resultMap.put("error_msg", CommonRspCode.MSG.get(CommonRspCode.SYSTEM_ERROR));
			logger.error("Exception->service:storage/preview", e);
		}
		// 将数据返回给请求方
		String resultJson = FastJsonUtil.toJson(resultMap);
		// 清空返回数据并置空,让GC更快的进行内存回收
		resultMap.clear();
		resultMap = null;
		logger.info("service:storage/preview,params:" + paramsMap.toString() + ",response:" + resultJson);
		retrunResult(response, resultJson);
	}

	/**
	 * 文件预览 返回map格式,key为doc_id,value为路径
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@RequestMapping("/storagePreview")
	public void storagePreview(HttpServletRequest request, HttpServletResponse response) {
		ResultData result = new ResultData();
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		try {
			paramsMap.put("doc_ids", request.getParameter("doc_ids"));
			paramsMap.put("app_token", request.getParameter("app_token"));
			logger.info("service:storage/storagePreview,params:" + paramsMap.toString());
			ValidateUtil.validateParams(paramsMap, new String[] { "app_token", "doc_ids" });

			// TODO 后期优化
			//
			// Object token_obj = redisUtil.getObj((String)
			// paramsMap.get("app_token"));
			// if (token_obj == null) {
			// throw new BusinessException(RspCode.APP_TOKEN_ERROR,
			// RspCode.MSG.get(RspCode.APP_TOKEN_ERROR));
			// }
			//
			String doc_ids = (String) paramsMap.get("doc_ids");
			List<String> list = Arrays.asList(doc_ids.split(","));
			if (list.size() == 0) {
				throw new BusinessException(RspCode.SYSTEM_PARAM_MISS, RspCode.MSG.get(RspCode.SYSTEM_PARAM_MISS));
			}
			storageService.storagePreviewCache(list, result);
			resultMap.put("rsp_code", "succ");
			resultMap.put("data", result.getResultData() == null ? "{}" : result.getResultData());
		} catch (BusinessException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.warn("BusinessException->service:storage/storagePreview, errorInfo:" + e.getMsg());
		} catch (SystemException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.error("SystemException->service:storage/storagePreview", e);
		} catch (Exception e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", CommonRspCode.SYSTEM_ERROR);
			resultMap.put("error_msg", CommonRspCode.MSG.get(CommonRspCode.SYSTEM_ERROR));
			logger.error("Exception->service:storage/storagePreview", e);
		}
		// 将数据返回给请求方
		String resultJson = FastJsonUtil.toJson(resultMap);
		// 清空返回数据并置空,让GC更快的进行内存回收
		resultMap.clear();
		resultMap = null;
		logger.info("service:storage/storagePreview,params:" + paramsMap.toString() + ",response:" + resultJson);
		retrunResult(response, resultJson);
	}

	/**
	 * 系统调用文件预览, 不对外开放
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@RequestMapping("/previewOps")
	public void storagePreviewOps(HttpServletRequest request, HttpServletResponse response) {
		ResultData result = new ResultData();
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		try {
			paramsMap.put("doc_ids", request.getParameter("doc_ids"));
			logger.info("service:storage/previewOps,params:" + paramsMap.toString());
			ValidateUtil.validateParams(paramsMap, new String[] { "doc_ids" });
			String doc_ids = (String) paramsMap.get("doc_ids");
			List<String> list = Arrays.asList(doc_ids.split(","));
			if (list.size() > 0) {
				storageService.storagePreviewCache(list, result);
			}
			resultMap.put("rsp_code", "succ");
			resultMap.put("data", result.getResultData() == null ? "{}" : result.getResultData());
		} catch (BusinessException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.warn("BusinessException->service:storage/previewOps, errorInfo:" + e.getMsg());
		} catch (SystemException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.error("SystemException->service:storage/previewOps", e);
		} catch (Exception e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", CommonRspCode.SYSTEM_ERROR);
			resultMap.put("error_msg", CommonRspCode.MSG.get(CommonRspCode.SYSTEM_ERROR));
			logger.error("Exception->service:storage/previewOps", e);
		}
		// 将数据返回给请求方
		String resultJson = FastJsonUtil.toJson(resultMap);
		// 清空返回数据并置空,让GC更快的进行内存回收
		resultMap.clear();
		resultMap = null;
		logger.info("service:storage/previewOps,params:" + paramsMap.toString() + ",response:" + resultJson);
		retrunResult(response, resultJson);
	}

	/**
	 * 内部接口文件预览
	 * 
	 * @param paramMap
	 * @param result
	 * @throws BusinessException
	 * @throws SystemException
	 */
	@RequestMapping("/preview_no_token")
	public void storagePreviewNoToken(HttpServletRequest request, HttpServletResponse response) {
		ResultData result = new ResultData();
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		try {
			paramsMap.put("doc_ids", request.getParameter("doc_ids"));
			logger.info("service:storage/previewOps,params:" + paramsMap.toString());
			ValidateUtil.validateParams(paramsMap, new String[] { "doc_ids" });
			String doc_ids = (String) paramsMap.get("doc_ids");
			List<String> list = Arrays.asList(doc_ids.split(","));
			if (list.size() > 0) {
				storageService.storagePreviewCache(list, result);
			}
			resultMap.put("rsp_code", "succ");
			resultMap.put("data", result.getResultData() == null ? "{}" : result.getResultData());
		} catch (BusinessException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.warn("BusinessException->service:storage/previewOps, errorInfo:" + e.getMsg());
		} catch (SystemException e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", e.getCode());
			resultMap.put("error_msg", e.getMsg());
			logger.error("SystemException->service:storage/previewOps", e);
		} catch (Exception e) {
			resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
			resultMap.put("error_code", CommonRspCode.SYSTEM_ERROR);
			resultMap.put("error_msg", CommonRspCode.MSG.get(CommonRspCode.SYSTEM_ERROR));
			logger.error("Exception->service:storage/previewOps", e);
		}
		// 将数据返回给请求方
		String resultJson = FastJsonUtil.toJson(resultMap);
		// 清空返回数据并置空,让GC更快的进行内存回收
		resultMap.clear();
		resultMap = null;
		logger.info("service:storage/previewOps,params:" + paramsMap.toString() + ",response:" + resultJson);
		retrunResult(response, resultJson);
	}

	/**
	 * 返回结果
	 * 
	 * @param response
	 * @param result
	 */
	public void retrunResult(HttpServletResponse response, String result) {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			if (null != out) {
				out.close();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 获取请求网络ip
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	/**
	 * 负载均衡后端探测
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/probe")
	public void probe(HttpServletRequest request, HttpServletResponse response) {
		retrunResult(response, "SUCC");
	}

	/**
	 * 文件下载并上传到OSS
	 * 
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("/downloadByToUpload")
	public void storageDownload(HttpServletRequest request,HttpServletResponse response)
			throws Exception {		
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
				paramsMap.put("file_url", request.getParameter("file_url"));
				try {
					ValidateUtil.validateParams(paramsMap, new String[] { "file_url" });
		
					String file_url = paramsMap.get("file_url")+"";
					
					//通过链接URL获取图片二进制数据
					URL url = new URL(file_url);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5 * 1000);
					InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
					
					//从输入流中获取数据
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					inStream.close();
					byte[] btImg = outStream.toByteArray();
					
					if (null != btImg && btImg.length > 0) {
						String fileName = "meitianhui" + IDUtil.getShortUUID() + ".jpg";
						//将图片写入到磁盘
						String tmpFilePath = System.getProperty("java.io.tmpdir");
						File file = new File(tmpFilePath + fileName);
						FileOutputStream fops = new FileOutputStream(file);
						fops.write(btImg);
						fops.flush();
						fops.close();
						//图片已经写入C盘临时目录
						ResultData result = new ResultData();
						paramsMap.clear();
						paramsMap.put("category", "oss_community");
						//设置文件格式和编码
						paramsMap.put("contentType", "image/jpeg;charset=UTF-8");
						//截取字符串  把文件名字的前缀和后缀分开
						String fiulName = fileName;
						Integer index = fiulName.lastIndexOf(".");
						String subffix = fiulName.substring(index);
						paramsMap.put("subffix", subffix);
						paramsMap.put("doc_name", fiulName.substring(0, index));
						//File --> InputStream  
						InputStream in = new FileInputStream(file);  
						paramsMap.put("inputStream", in);
						storageService.handleStorageUpload(paramsMap, result);
						resultMap.put("rsp_code", "succ");
						resultMap.put("data", result.getResultData() == null ? "{}" : result.getResultData());
						//上传成功 删除文件
						file.delete();
					} else {
						System.out.println("接口：downloadByToUpload-->没有从图片链接获得内容");
					}
				} catch (BusinessException e) {
					resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
					resultMap.put("error_code", e.getCode());
					resultMap.put("error_msg", e.getMsg());
					logger.warn("BusinessException->service:storage/downloadByToUpload, errorInfo:" + e.getMsg());
				} catch (SystemException e) {
					resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
					resultMap.put("error_code", e.getCode());
					resultMap.put("error_msg", e.getMsg());
					logger.error("SystemException->service:storage/downloadByToUpload", e);
				} catch (Exception e) {
					resultMap.put("rsp_code", CommonRspCode.RESPONSE_FAIL);
					resultMap.put("error_code", CommonRspCode.SYSTEM_ERROR);
					resultMap.put("error_msg", CommonRspCode.MSG.get(CommonRspCode.SYSTEM_ERROR));
					logger.error("Exception->service:storage/downloadByToUpload", e);
				}
				// 将数据返回给请求方
				String resultJson = FastJsonUtil.toJson(resultMap);
				// 清空返回数据并置空,让GC更快的进行内存回收
				resultMap.clear();
				resultMap = null;
				logger.info("service:storage/downloadByToUpload,params:" + paramsMap.toString() + ",response:" + resultJson);
				retrunResult(response, resultJson);
			
			}
			
}
