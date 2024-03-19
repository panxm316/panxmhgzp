package com.hgzp.utils.model;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * Copyright (C), 2014-2016, 国家复合出版系统工程         
 *    
 * 项目名称：MTXTGZ  
 * 类全名:reslib.pageModel.Plupload    
 * 类描述：        Plupload是一个上传插件。 
 * 			这是一个bean类,主要存储Plupload插件上传时需要的参数。 
 *			 属性名不可随意改动. 
 * 			这里主要使用MultipartFile文件上传方法
 * 创建人：peij    
 * 创建时间：2015年9月16日 上午9:34:15
 * 方法列表： 
 * 修改历史：   
 *   1、修改人：peij    
 * 		修改时间：2015年9月16日 上午9:34:15    
 * 		修改备注：  
 *	 2、
 *
 * @version  jdk1.7
 *
 */
public class Plupload {
	/**文件临时名(打文件被分解时)或原名*/  
    private String name;  
    /**总的块数*/  
    private int chunks = -1;  
    /**当前块数（从0开始计数）*/  
    private int chunk = -1;  
    /**HttpServletRequest对象，不能直接传入进来，需要手动传入*/  
    private HttpServletRequest request;  
    /**保存文件上传信息，不能直接传入进来，需要手动传入*/  
    private MultipartFile multipartFile;  
    /*上传文件客户端id*/
    private String fileid;
    /*稿件类型*/
    private String storytype;
    
    /*地图聊天上传文件记录聊天id*/
    private String groupId;
    
    
    
    public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public int getChunks() {  
        return chunks;  
    }  
  
    public void setChunks(int chunks) {  
        this.chunks = chunks;  
    }  
  
    public int getChunk() {  
        return chunk;  
    }  
  
    public void setChunk(int chunk) {  
        this.chunk = chunk;  
    }  
  
    public HttpServletRequest getRequest() {  
        return request;  
    }  
  
    public void setRequest(HttpServletRequest request) {  
        this.request = request;  
    }  
  
    public MultipartFile getMultipartFile() {  
        return multipartFile;  
    }  
  
    public void setMultipartFile(MultipartFile multipartFile) {  
        this.multipartFile = multipartFile;  
    }

	public String getStorytype() {
		return storytype;
	}

	public void setStorytype(String storytype) {
		this.storytype = storytype;
	}    
    
}
