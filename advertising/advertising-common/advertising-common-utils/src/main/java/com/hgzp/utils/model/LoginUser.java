package com.hgzp.utils.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * Copyright (C), 2014-2016, 国家复合出版系统工程         
 *    
 * 项目名称：MTXTGZ  
 * 类全名:reslib.pageModel.LoginUser    
 * 类描述：    登陆用户信息
 * 创建人：wangwk    
 * 创建时间：2015年9月16日 上午9:29:54
 * 方法列表： 
 * 修改历史：   
 *   1、修改人：wangwk    
 * 		修改时间：2015年9月16日 上午9:29:54    
 * 		修改备注：  
 *	 2、
 *
 * @version  jdk1.7
 *
 */
@Data
public class LoginUser implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 用户id
	 */
	private Long userid;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 登录帐号
	 */
	private String loginname;
	/**
	 * ip
	 */
	private String ip;
	/**
	 * mac
	 */
	private String mac;
	/**
	 * 部门id
	 */
	private Long deptid;
	/**
	 * 部门名称
	 */
	private String deptname;
	/**
	 * 管理员标志
	 */
	private int adminflag;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 一级部门id
	 */
	private String firstdeptids;
	/**
	 * 内部人部标志 false:通讯员 true：社内人员
	 */
	private Boolean binner;
	/**
	 * 语言
	 */
	private String slanguage;
	/**
	 * 验证超期
	 */
	private boolean passexpired;
	/**
	 * 兼职部门id
	 */
	private String parttimeDept;
	/**
	 * token
	 */
	private String accessToken;
	/**
	 * 新消息提示音
	 */
	private Integer imsgaudio;
	/**
	 * 头像
	 */
	private String simg;

	/**
	 * 单位管理员 管理的部门ids
	 */
	private String smanagedepts;
	/**
	 * 是否负责人
	 */
	private Boolean blead;
	/**
	 * 是否业务员
	 */
	private Boolean bsalesman;

	/**
	 * 有权限使用的部门ids，不含上级部门，英文逗号分隔
	 */
	private String authedDeptIds;

	public LoginUser(Long id, String username, int adminflag) {
		this.userid = id;
		this.username = username;
		this.adminflag = adminflag;
	}

	public LoginUser(Long userid, String username, String ip, String mac) {
		this.userid = userid;
		this.username = username;
		this.ip = ip;
		this.mac = mac;
	}
	
	public LoginUser(Long userid, String username, String loginname, String ip, String mac) {
		this.userid = userid;
		this.username = username;
		this.loginname = loginname;
		this.ip = ip;
		this.mac = mac;
	}
	public LoginUser(){}


}
