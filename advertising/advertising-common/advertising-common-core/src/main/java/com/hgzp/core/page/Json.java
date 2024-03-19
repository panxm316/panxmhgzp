package com.hgzp.core.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.core.emnus.ResultDefines;
import lombok.Data;

import java.util.List;

/**
 * Copyright (C), 2014-2016, 国家复合出版系统工程         
 *    
 * 项目名称：hgcb-core  
 * 类全名:com.hgcb.core.page.Json    
 * 类描述：    
 * 创建人：Peij    
 * 创建时间：2017年7月13日 下午12:56:17
 * 方法列表： 
 * 修改历史：   
 * 	 1、修改人：Peij    
 * 		修改时间：2017年7月13日 下午12:56:17    
 * 		修改备注：  
 *   2、
 *
 * @version  jdk1.7
 * 
 */
@Data
public class Json<T> implements java.io.Serializable {
	/**    
	 * serialVersionUID:TODO
	 * @since JDK 1.7   
	 */    
	
	private static final long serialVersionUID = 1L;

	private boolean success = false;

	private String msg = "";

	private T obj = null;

	private Integer code;

	private long total;

	public <T> T getObj() {
		return (T)obj;
	}



	public static Json success(){
		Json json = new Json();
		json.setSuccess(true);
		return json;
	}
	public static <T> Json<T> success(T obj){
		Json<T> json = new Json<>();
		json.setSuccess(true);
		json.setObj(obj);
		return json;
	}
	public static <T> Json<T> success(long total, T obj){
		Json<T> json = new Json<>();
		json.setSuccess(true);
		json.setObj(obj);
		json.setTotal(total);
		return json;
	}
	public static <T> Json<List<T>> success(IPage<T> page){
		Json<List<T>> json = new Json<>();
		json.setSuccess(true);
		json.setObj(page.getRecords());
		json.setTotal(page.getTotal());
		return json;
	}

	public static Json success(String msg){
		Json json = new Json();
		json.setSuccess(true);
		json.setMsg(msg);
		return json;
	}
	public static <T> Json<T> success(String msg, T obj){
		Json<T> json = new Json<>();
		json.setSuccess(true);
		json.setMsg(msg);
		json.setObj(obj);
		return json;
	}


	public static Json fail(){
		Json json = new Json();
		json.setSuccess(false);
		return json;
	}
	public static Json fail(String msg){
		Json json = new Json();
		json.setSuccess(false);
		json.setMsg(msg);
		return json;
	}
	public static <T> Json<T> fail(String msg, T obj){
		Json<T> json = new Json<>();
		json.setSuccess(false);
		json.setMsg(msg);
		json.setObj(obj);
		return json;
	}
	public static Json fail(Integer code, String msg){
		Json json = new Json();
		json.setSuccess(false);
		json.setCode(code);
		json.setMsg(msg);
		return json;
	}
	public static Json fail(ResultDefines result) {
		Json json = new Json();
		json.setCode(result.getCode());
		json.setSuccess(false);
		json.setMsg(result.getMessage());
		return json;
	}


}
