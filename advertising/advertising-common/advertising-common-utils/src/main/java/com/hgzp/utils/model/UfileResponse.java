package com.hgzp.utils.model;
/**
 * Copyright (C), 2014-2016, 国家复合出版系统工程         
 *    
 * 项目名称：hgcb-utils  
 * 类全名:com.hgcb.utils.model.UfileRsponse    
 * 类描述： 统一文件返回 对象   
 * 创建人：wangwk    
 * 创建时间：2017年6月14日 下午2:08:57
 * 方法列表： 
 * 修改历史：   
 * 	 1、修改人：wangwk    
 * 		修改时间：2017年6月14日 下午2:08:57    
 * 		修改备注：  
 *   2、
 *
 * @version  jdk1.7
 * 
 */
public class UfileResponse {

	private Integer status_code;  //访问返回码  200正常
	private String sha1;			//上传时返回文件的sha1
	private String request;   
	private String error_message;    //统一文件返回的错误信息
	private Integer error_code;     //统一文件返回的错误码
	
	private String content;       //返回的内容
	
	/**
	 * 
	 * 方法名称:isSuccess.<br/> 
	 * 方法功能: 判断上传是否成功.<br/>
	 * @author wangwk 
	 * @return 
	 * @since JDK 1.7
	 */
	public boolean isSuccess(){
		if( (status_code != null && status_code == 200) 
				&& (error_code == null || error_code == 0) ){
			return true;
		}
		return false;
	}
	
	
	public Integer getStatus_code() {
		return status_code;
	}
	public void setStatus_code(Integer status_code) {
		this.status_code = status_code;
	}
	public String getSha1() {
		return sha1;
	}
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getError_message() {
		return error_message;
	}
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	public Integer getError_code() {
		return error_code;
	}
	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}


	@Override
	public String toString() {
		return "UfileResponse [status_code=" + status_code + ", sha1=" + sha1 + ", request=" + request
				+ ", error_message=" + error_message + ", error_code=" + error_code + ", content=" + content + "]";
	}
	
	
	
	
}


