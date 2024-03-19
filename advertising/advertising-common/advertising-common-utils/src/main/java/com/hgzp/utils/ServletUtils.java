package com.hgzp.utils;

import com.hgzp.utils.file.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Http与Servlet工具类.
 * 
 */
@Component
public  class ServletUtils {

	/** 文件上传下载存放路径	 */
	private static final String UPLOAD_PATH = "/uploadfile/";
	private static String SHARE_UPLOAD_PATH ;

	@Value("${SHARE_UPLOAD_PATH:#{null}}")
	private String SHARE_UPLOAD_PATH_VALUE;

	// -- Content Type 定义 --//
	public static final String EXCEL_TYPE = "application/vnd.ms-excel";
	public static final String HTML_TYPE = "text/html";
	public static final String JS_TYPE = "text/javascript";
	public static final String JSON_TYPE = "application/json";
	public static final String XML_TYPE = "text/xml";
	public static final String TEXT_TYPE = "text/plain";

	// -- Header 定义 --//
	public static final String AUTHENTICATION_HEADER = "Authorization";

	// -- 常用数值定义 --//
	public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

	@PostConstruct
	public void init(){
		ServletUtils.SHARE_UPLOAD_PATH = SHARE_UPLOAD_PATH_VALUE;
	}

	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		// Http 1.0 header
		response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000);
		// Http 1.1 header
		response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
	}

	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setDisableCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");
		// Http 1.1 header
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
	}



	/**
	 * 设置让浏览器弹出下载对话框的Header.
	 * 
	 * @param fileName
	 *            下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("filename", URLEncoder.encode(fileName, "utf-8"));
		} catch (UnsupportedEncodingException e) {
		}
	}


	/**
	 *  从header中获取referer 截取其中的域名
	 * @param request
	 * @return referer中的域名
	 */
	public static String getRefererHost(HttpServletRequest request) {
		String referer = request.getHeader("referer");
		if (null != referer && referer.contains("//")) {
			referer = referer.substring(referer.indexOf("//") + 2);
			referer = referer.substring(0, referer.indexOf("/"));
			if (referer.contains(":")) {
				referer = referer.substring(0, referer.indexOf(":"));
			}
		}
		return referer;
	}


	/**
	 * 获取项目名称路径
	 */
	public static String getContentpath(HttpServletRequest request) {
		return request.getContextPath();
	}

	/**
	 * 获取项目绝对路径 jar包所在目录
	 */
	public static String getRealPath(HttpServletRequest request) {
		if(StringUtils.hasText(SHARE_UPLOAD_PATH)){
			return SHARE_UPLOAD_PATH;
		}
		ApplicationHome home = new ApplicationHome(ServletUtils.class);
		File jarFile = home.getSource();
		String jarPath = jarFile.getParentFile().toString();
		return jarPath;
	}

	/**
	 * 获取项目工程名
	 * 例如：http://localhost:8080/test
	 */
	public static String getProjectPath(HttpServletRequest request) {
		return  "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
	
	/**
	 * 生成临时文件名
	 *    传入的文件名即为存储的文件名
	 * @param request
	 * @param fileName
	 *            文件名称
	 */
	public static String generateTempFileName(HttpServletRequest request, String fileName) {
		return getRealPath(request) + UPLOAD_PATH  + fileName;
	}
	
	/**
	 * 生成临时文件名
	 *    传入的文件名即为存储的文件名
	 * @param request
	 *            文件名称
	 */
	public static String generateTempFilePath(HttpServletRequest request) {
		return getRealPath(request) + UPLOAD_PATH  ;
	}
	

	/**
	 * 生成临时文件名
	 *    传入的文件名改为uuid
	 * @param request
	 * @param fileName
	 *            文件名称
	 */
	public static String generateTempFileNameUUID(HttpServletRequest request, String fileName) {
		return getRealPath(request) + UPLOAD_PATH + System.currentTimeMillis() + "/" + FileUtils.uuidFileName(fileName);
	}
	
	
	
}
